#include <msp430g2553.h>

//------------DISABLE OPTIMIZATIONS IN CCS Project->properties->build->disable all optimizations

/*
 * File: Lucidio
 * Date: 04/14/2019
 * Description: Program to detect REM sleep by reading EOG sensor data and
 * determining frequency of eye movement. Data is transmitted via UART to a BLE
 * module to be received and plotted on an Android device. The MCU will listen
 * for commands from the Android device to blink LEDs.
 */

//Phone Commands
const unsigned char commandPattern[] = "LD";  //if this sequence of characters is received, next char will be read as android command (in RX interrupt)
const unsigned int commandPatternSize = sizeof(commandPattern) - 1;
int commandNotification =0;
int commandPending = 0;
unsigned char cmd_from_android = 0;       //variable for receiving BT commands from Android device

/*  from phone:
 *  'P'=protocol
 *  'L'= LED set
 *  'W' = wake
 *  'S' = sleep mode
 *  'R' = Rem notification
 */

//RN4020 commands
unsigned char SETSETTINGS[] = "SR,32000400\n";
unsigned char GETSETTINGS[] = "GR\n";
unsigned char REBOOT[] = "R,1\n";
unsigned char RESET[] = "SF,2\n";
unsigned char ECHO[] = "+\r\n"; //for debugging
unsigned char REMOTE_COMMAND_ENABLE[] = "!,1\n"; //for implementing bluetooth settings setup from phone in the future
unsigned char REMOTE_COMMAND_DISABLE[] = "!,0\n";
unsigned char SETNAME[] = "SN,Lucidio\n";
unsigned char GETNAME[] = "GN\n";
unsigned char GETFIRMWARE[] = "GDF\n";
unsigned char MLDP[] = "I\n"; //Can turn mldp mode on  and off, but it turns on automatically anyways
unsigned char SETPOWER[] = "SP,7\n";
unsigned char GETPOWER[] = "GP\r\n";
//-----------------Initialize variables----------------------//
const int BUFFER_MAX_SIZE = 100;
unsigned char readBuffer[BUFFER_MAX_SIZE]; //implemented as ring buffer to receive and store UART messages
unsigned int bufferTail =0; //buffer read index used to keep track of last index value read
unsigned int bufferHead =0; //used to keep track of the most recent buffer write index.
unsigned char lastRead[BUFFER_MAX_SIZE]; //the most recent read values, auto updated with read_ReadBuffer()
char send[20]; // debugging array to see outgoing uart
unsigned int sendIndex = 0;
int sleep_mode = 0;
int REM_notification = 0;               //boolean variable to signal REM sleep detected
int LED_dutycycle = 10;
long counter =0;
volatile int prev_freq_state;
volatile int freq_state;
int timer_count = 0;
const unsigned long int unit_length = 100; //unit length for Morse Code signal, 1000 cycles = 1ms

//utility functions (implemented after main() function)
void wait( long);
void sendUartCmd(unsigned char[], int);
void write_ReadBuffer(unsigned char); //write to the read buffer array, readBuffer[] (UART)
void read_ReadBuffer(); //updates lastRead[] to the new values read over UART
void clear_ReadBuffer(); //set buffer values to null
void factoryResetBLE();
int BLEReadyForData();
int responseAOK();
void rebootBLE();
void wakeBLE();
void sleepBLE();
void strcopy(unsigned char[], unsigned char[], int );
int strcompare(unsigned char*, unsigned char*, int);
void ledOn();
void ledOff();
void toggleSleepMode();
void protocol1();
void patternDetector(const unsigned char*, int);

int main(void)
{
    WDTCTL = WDTPW | WDTHOLD;         // stop watchdog timer

    //------------------- Configure the Clocks -------------------//
    BCSCTL1 = CALBC1_1MHZ;
    BCSCTL3 = LFXT1S_2;
    DCOCTL  = CALDCO_1MHZ;             // Set DCO step + modulation

    //------------ Configuring the UART(USCI_A0) -----------------//
    P1SEL  |=  BIT1 + BIT2;                 // P1.1 UCA0RXD input
    P1SEL2 |=  BIT1 + BIT2;                 // P1.2 UCA0TXD output
    UCA0CTL1 |=  UCSWRST;  //USCI_A0 disabled
    UCA0CTL1 |=  UCSSEL_2;  // USCI Clock = SMCLK,
    UCA0BR0   =  8;                 //From datasheet table
    UCA0BR1   =  0;                   // -selects baudrate =115200 bps,clk = SMCLK
    UCA0MCTL  = BIT3 + BIT2;             // from datasheet
    UCA0CTL1 &= ~UCSWRST;             // Clear UCSWRST to enable USCI_A0

    //--------------------Configure ADC--------------------------//
    ADC10CTL0 &= ~ENC; //disabled to allow modification
    ADC10CTL0 = ADC10SHT_1 + ADC10ON + ADC10SR + MSC;
    ADC10CTL1 = INCH_0 + SHS_0 + ADC10DIV_0 + ADC10SSEL_0 + CONSEQ_1;
    ADC10AE0 |= BIT0; //analog input enabled A0 at pin1.0

    //----------------Configure Timer_A0----------------------//
    TA0CTL |= TASSEL_1 + ID_3 + MC_1;  //sourced from ACLK (12kHz clock) and divide by 8 (1500 Hz)
    TA0CCTL0 = CCIE;
    TA0CCR0 = 10;                    //    (1500 clock cycles) / (150samples/sec) = 10 cycles/sample

    //----------------Configure Timer_A1----------------------// for LEDS pwm -- can't make it work
    TA1CCTL1 = OUTMOD_7; // used for pwm to set pin high if counting to CCR0 and reset to low when reaching CCR1
    TA1CCR0 = 100; //period
    TA1CCR1 = 0; //duty cycle, meaning TA1CCR1 has to be less than or equal to TA1CCR0
    TA1CTL = TASSEL_2 + MC_1 + ID_3;
    P2SEL |= BIT1 + BIT2;                 //Set up P2.1 and 2.2 for PWM with TA1 (TA1 controls these pins)

    //------------------- Configure pins--------------------------//

    P1DIR  |=  BIT3 + BIT4 + BIT6;           //P1.3,1.4,1.6 set as outputs (LED1,LED2,WAKE_SW)
    P1DIR  &=  ~(BIT0 + BIT5);               //P1.0,1.5 set as input
    P1OUT |= BIT4;                          //LED1 always on
    P1OUT &= ~BIT3;
    P2DIR |= BIT0 + BIT1 + BIT2 + BIT5;                          //BIT0=MLDP
   // P2OUT |= BIT0;                   // BIT4=used for uart flow control RTS if enabled on ble chip(might use for MLDP)  BIT5 = HW_WAKE
    //P2DIR &= ~(BIT3);                     //P2.3 set as input (default)
    P2OUT &= ~(BIT0);                   //Set pin 2.0 low (MLDP)
    ledOff();

    //---------------- Enabling the interrupts ------------------//

    IE2 |= UCA0RXIE;                 // Enable uart RX interrupt
    __enable_interrupt();           // Enable the global interrupt

    //---------------- Commands to Set Up Bluetooth ------------------//
    factoryResetBLE();
    sendUartCmd(SETSETTINGS, sizeof(SETSETTINGS)); //RN4020 freeezes and needs a manual power cycle to work again (followed by commenting out this code)
    sendUartCmd(SETNAME, sizeof(SETNAME));
    //sendUartCmd(SETPOWER, sizeof(SETPOWER));
    rebootBLE(); //reboot after changing a setting
//    sendUartCmd(GETSETTINGS, sizeof(GETSETTINGS)); //RN4020 sends back the current SR command settings
//    sendUartCmd(GETNAME, sizeof(GETNAME));
//    sendUartCmd(GETPOWER, sizeof(GETPOWER));


    //------------------------- Main ----------------------------//
    while(1)
    {

        /*
         * Using Morse Code standards, 1 unit length = 50ms, but it felt too quick, so if 1e6 clock cycles = 1sec, 100000 cycles = 100ms.
         * The morse code for LD is .-..-.., which is 26 units long (2.6sec).
         */
        switch (cmd_from_android)
        {
         case 'P': // Protocol Training
         {
            //P2DIR |= BIT1;   //lights on
            unsigned int i; //for loop value
            for(i=1; i>0; i-- ){
                protocol1();
            }
            cmd_from_android = 0; //exit the case>?
            break;
         }
         case 'L' : //Decimal:68
             {
              //start tracking SeekBar
              wait(200);
              while(readBuffer[bufferHead-1] != 101){ //Decimal: 101
              ledOn(); //LEDs on to see PWM change in real time
              LED_dutycycle = readBuffer[bufferHead-1]; //receive a cmd_from_android seekBar progress value
              TA1CCR1 = LED_dutycycle;
              }
              //stop tracking SeekBars
              //remove later
              wait(100);
              LED_dutycycle = readBuffer[bufferHead-1];
              TA1CCR1 = LED_dutycycle;
              cmd_from_android = 0; //reset cmd_from_android good practices
              ledOff();
              break;
              }
         case 'S' : //Decimal:73, START SLEEP
             {
                 sleep_mode = 1;
                 ledOff();
                 cmd_from_android = 0;
                 break;
             }

        } //end switch(cmd_from_android)

       while(sleep_mode)
       {

            if(cmd_from_android == 'R'){
                protocol1();
                cmd_from_android = 0;
            }
            if(cmd_from_android == 'W') //command from android to exit sleep mode
            {
                sleep_mode = 0;
                cmd_from_android = 0;
            }
       }
    }
}

//------------------------Timer A0 interrupt-----------------------// controls both LED pwm and voltage data frequency

#pragma vector=TIMER0_A0_VECTOR
__interrupt void TIMER0A0_ISR(void) //Maybe move some of this code into main
{
    if(sleep_mode)//if the user is in REM sleep, send REM data
    {
       P1OUT |= BIT3; //led blink to show data transfer
       ADC10CTL0 |= ENC + ADC10SC; //adc data capture
       while(ADC10CTL1 & ADC10BUSY);
       ADC10CTL0 &= ~ENC;
       IE2 |= UCA0TXIE;                    //enable uart TX interrupt
       UCA0TXBUF = ADC10MEM >> 2;        // UART stored and sent in UCA0TXBUF, divide by 4 for easier transmission
       P1OUT &= ~BIT3;
    }
}

//-----------Transmit and Receive interrupts-----------------------//

#pragma vector = USCIAB0TX_VECTOR
__interrupt void TransmitInterrupt(void)
{
  IE2 &= ~UCA0TXIE; //disable TX interrupt so it only sends one byte.
}

#pragma vector = USCIAB0RX_VECTOR
__interrupt void ReceiveInterrupt(void)
{
    unsigned char data;
    data = (unsigned char)UCA0RXBUF;
    write_ReadBuffer((unsigned char)UCA0RXBUF);
    if(commandNotification){
        cmd_from_android = data;
        commandNotification = 0;
    }

    if(data == commandPattern[commandPatternSize-1]){
        patternDetector(commandPattern, commandPatternSize);
    }

}

void patternDetector(const unsigned char *pattern, int length){
    unsigned int readIndex;
    if(bufferHead < length){
        readIndex = bufferHead - length + 50;
    }
    else{
        readIndex = bufferHead - length;
    }
    unsigned int i;
    for(i=0;i<length;i++){
        if(readBuffer[readIndex] != commandPattern[i]){
            commandNotification = 0;
            return;
        }
        else{
            readIndex++;
            if(readIndex == BUFFER_MAX_SIZE){
                readIndex = 0;
            }
        }
    }
    commandNotification = 1;
}

void sendUartCmd(unsigned char string[], int length){
    if(string != 0){
        int i=0;
        while(i < length-1){
            while(!(IFG2 & UCA0TXIFG));         //wait for previous transfer to send
            IE2 |= UCA0TXIE;                    //enable uart TX interrupt
            UCA0TXBUF = string[i];              //add character to TX buffer
            send[sendIndex] = string[i];
            i++;
            sendIndex++;
        }
        sendIndex = 0;
        wait(100);
        read_ReadBuffer();
    }
}

void factoryResetBLE(){
    P2OUT |= BIT5;
    P1OUT &= ~BIT6;
    wait(5);
    P1OUT |= BIT6;
    wait(5);
    P1OUT &= ~BIT6;
    wait(5);
    P1OUT |= BIT6;
    wait(5);
    P1OUT &= ~BIT6;
    wait(5);
    P1OUT |= BIT6;
    wait(5);
    P1OUT &= ~BIT6;
    wait(5);
    P1OUT |= BIT6;
    wait(5);
    P1OUT &= ~BIT6;
    wait(5);
    P1OUT |= BIT6;
    wait(5);
    P2OUT |= BIT5;
    wait(5);
    sendUartCmd(RESET, sizeof(RESET));
    wait(2000);
    read_ReadBuffer();
}

int BLEReadyForData(){ //only used if uart flow control setting is set on bluetooth chip
   if(P2DIR & BIT3){     //if pin 2.3 is set to 1 (output mode),
        P2DIR &= ~BIT3;           //set pin 2.3 to input mode
    }

    if(P2IN & BIT3){ //check CTS pin 2.3 is high
        return 1;
    }
    else {
        return 0;
    }
}

void write_ReadBuffer(unsigned char data){ //write to the read buffer array, readBuffer[]
//    if(bufferHead == bufferTail){ //buffer overflow
//        readBuffer();
//    }
    if(bufferHead == BUFFER_MAX_SIZE){
        bufferHead = 0;
    }
    readBuffer[bufferHead] = data;
    bufferHead++;
}

void read_ReadBuffer(){ //reads all the latest data into lastRead[]
    int i=0;
    if(bufferTail == bufferHead) return;

    while(bufferTail != bufferHead){
        if(bufferTail == BUFFER_MAX_SIZE){
            bufferTail = 0;
        }
        lastRead[i] = readBuffer[bufferTail];
        i++;
        bufferTail++;
    }
    for(;i<BUFFER_MAX_SIZE;i++){
        lastRead[i] = '\0';
    }
}

void clear_ReadBuffer(){
    bufferTail=0;
    bufferHead=0;
    int i;
    for(i=0;i<BUFFER_MAX_SIZE;i++){
        readBuffer[i] = 0;
    }
}

int responseAOK(){
    unsigned char aok[] = "AOK";
    int i;
    for(i=0; i < sizeof(aok) - 1; i++){
        if(aok[i] != lastRead[i]){
            return 0;
        }
    }
    return 1;
}

void rebootBLE(){
    sendUartCmd(REBOOT, sizeof(REBOOT));
    wait(2000);
}

void wakeBLE(){
    P1OUT |= BIT6;
}
void sleepBLE(){
    P1OUT &= ~BIT6;
}

void wait(long milliseconds){
    int i;
    for(i=0;i<milliseconds;i++){
        __delay_cycles(1000);
    }
}

void ledOn(){
    TA1CCR1 = LED_dutycycle;
}

void ledOff(){
    TA1CCR1 = 0; //turns off pwm light timer
}

void toggleSleepMode(){
    if(sleep_mode==1){
        sleep_mode = 0;
    }
    else sleep_mode = 1;
}

void protocol1(){ //'LD' in morse code

    //switchings bits on and off as according to morse code L
    ledOn();
    wait(unit_length);
    ledOff();
    wait(unit_length);  //wait one unit
    ledOn();
    wait(unit_length*3); //wait three units
    ledOff();
    wait(unit_length);//wait one unit
    ledOn();
    wait(unit_length); //wait one unit
    ledOff();
    wait(unit_length); //wait one unit
    ledOn();
    wait(unit_length); //wait one unit
    ledOff();

    //switchings bits on and off as according to morse code D
    wait(unit_length);  //wait three unit
    ledOn();
    wait(unit_length*3);  //wait three units
    ledOff();
    wait(unit_length);  //wait one unit
    ledOn();
    wait(unit_length);  //wait one unit
    ledOff();
    wait(unit_length);  //wait one unit
    ledOn();
    wait(unit_length); //wait one unit
    ledOff();
    wait(unit_length*7);  //wait seven units
}

void strcopy(unsigned char* strParent, unsigned char* strcopy, int length){
    int i;
    for(i=0;i<length;i++){
        *strcopy = *strParent;
        strcopy++;
        strParent++;
    }
}

int strcompare(unsigned char* str2, unsigned char* str1, int length){
    int i;
    for(i=0;i<length;i++){
        if(*str1 != *str2){
            return 0;
        }
        str1++;
        str2++;
    }
    return 1;
}
