#include <msp430g2553.h>
/*
 * File: REM_Sleep_Cycle
 * Author: James Drewelow
 * Date: 01/09/2018
 * Description: Program to detect REM sleep by reading EOG sensor data and
 * determining frequency of eye movement. Data is transmitted via UART to a BLE
 * module to be received and plotted on an Android device. The MCU will listen
 * for commands from the Android device to blink LEDs and power a vibration motor.
 */

//-----------------Initialize variables----------------------//
int LED_cue = 0;
int motor_cue = 1;
int sleep_mode = 0;
int REM_bool = 0;               //boolean variable to signal REM sleep detected
int cmd_from_android = 0;       //variable for receiving BT commands from Android device
int LED_brightness = 10;
int freq_counter = 0;
volatile int prev_freq_state;
volatile int freq_state;
int timer_count = 0;
const unsigned long int unit_length = 100000; //unit length for Morse Code signal, 1000 cycles = 1ms


int main(void)
{

     WDTCTL = WDTPW | WDTHOLD;         // stop watchdog timer

    //----------------Configure Timer_A0----------------------//
    TACCTL0 = CCIE;
    TA0CTL |= TASSEL_2 + MC_1 + ID_3;
    TA0CCR0 = 1245;                    // 1245*100 interrupts = 1 second

    //----------------Configure Timer_A1----------------------//
    TA1CCTL1 = OUTMOD_7; // used for pwm to set pin high if counting to CCR0 and reset to low when reaching CCR1
    TA1CCR0 = 100; //period
    TA1CCR1 = 0; //duty cycle, meaning TA1CCR1 has to be less than or equal to TA1CCR0
    TA1CTL = TASSEL_2 + MC_1;

    //------------------- Configure the Clocks -------------------//
    DCOCTL  = 0;                       // Select lowest DCOx and MODx settings
    BCSCTL1 = CALBC1_1MHZ;             // Set range
    DCOCTL  = CALDCO_1MHZ;             // Set DCO step + modulation

    //------------------- Configure pins--------------------------//
    P1OUT &= 0x00;                         //clear P1OUT
    P2DIR &= 0x00;                         //clear P2DIR
    P1SEL  |=  BIT1 + BIT2;            // P1.1 UCA0RXD input
    P1SEL2 |=  BIT1 + BIT2;            // P1.2 UCA0TXD output
    P1DIR  |=  BIT3 + BIT4 + BIT6 + BIT7 ;  //P1.3,1.4,1.7 set as outputs
    P1DIR  &=  ~BIT0 + ~BIT5;          //P1.0,1.5 set as input
    P2DIR &= ~BIT1 + ~BIT2;              //P2.1 and 2.2 set as outputs (turned off by default)
    P2DIR |= BIT0; //mldp bit to High>?
    P2SEL |= BIT1 + BIT2;              //P2.1 and 2.2 set for PWM (Comment out to control LEDs normally)
    P1OUT |= BIT4; //set reset high

    //--------------------Configure ADC--------------------------//
    ADC10CTL0 &= ~ENC; //disabled to allow modification
    ADC10CTL0 = ADC10SHT_1 + ADC10ON + ADC10SR + MSC;
    ADC10CTL1 = INCH_0 + SHS_0 + ADC10DIV_0 + ADC10SSEL_0 + CONSEQ_1;
    ADC10AE0 |= BIT0; //analog input enabled A0 at pin1.0

    //------------ Configuring the UART(USCI_A0) -----------------//

    UCA0CTL1 |=  UCSSEL_2 + UCSWRST;  // USCI Clock = SMCLK,USCI_A0 disabled
    UCA0BR0   =  104;                 // 104 From datasheet table
    UCA0BR1   =  0;                   // -selects baudrate =9600bps,clk = SMCLK
    UCA0MCTL  =  UCBRS_1;             // Modulation value = 1 from datasheet
    UCA0CTL1 &= ~UCSWRST;             // Clear UCSWRST to enable USCI_A0

    //---------------- Enabling the interrupts ------------------//

    IE2 |= UCA0RXIE;                  // Enable Rx interrupt
    _BIS_SR(GIE);                     // Enable the global interrupt

    //------------------------- Main ----------------------------//
    while(1)
    {
        TA1CCR1 = LED_brightness;
        /*
         * Using Morse Code standards, 1 unit length = 50ms, but it felt too quick, so if 1e6 clock cycles = 1sec, 100000 cycles = 100ms.
         * The morse code for LD is .-..-.., which is 26 units long (2.6sec).
         */
        switch (cmd_from_android)
        {
         case 0x42: //Decimal:66, Protocol Training
         {
            //P2DIR |= BIT1;   //lights on
            volatile unsigned int i; //for loop value
            for(i=1; i>0; i-- ){

            //switchings bits on and off as according to morse code L
            P2DIR |= BIT1 + BIT2; //on
            __delay_cycles(unit_length); //wait one unit
            P2DIR &= ~BIT1 + ~BIT2; //off
            __delay_cycles(unit_length); //wait one unit
            P2DIR |= BIT1 + BIT2; //on
            __delay_cycles(3 * unit_length); //wait three units
            P2DIR &= ~BIT1 + ~BIT2; //off
            __delay_cycles(unit_length); //wait one unit
            P2DIR |= BIT1 + BIT2; //on
            __delay_cycles(unit_length); //wait one unit
            P2DIR &= ~BIT1 + ~BIT2; //off
            __delay_cycles(unit_length); //wait one unit
            P2DIR |= BIT1 + BIT2; //on
            __delay_cycles(unit_length); //wait one unit
            P2DIR &= ~BIT1 + ~BIT2; //off

            //switchings bits on and off as according to morse code D
            __delay_cycles(3 * unit_length); //wait three unit
            P2DIR |= BIT1 + BIT2; //on
            __delay_cycles(3 * unit_length); //wait three units
            P2DIR &= ~BIT1 + ~BIT2; //off
            __delay_cycles(unit_length); //wait one unit
            P2DIR |= BIT1 + BIT2; //on
            __delay_cycles(unit_length); //wait one unit
            P2DIR &= ~BIT1 + ~BIT2; //off
            __delay_cycles(unit_length); //wait one unit
            P2DIR |= BIT1 + BIT2; //on
            __delay_cycles(unit_length); //wait one unit
            P2DIR &= ~BIT1 + ~BIT2; //off
            __delay_cycles(7 * unit_length); //wait seven units
            }
            cmd_from_android = 0; //exit the case>?
            break;
         }

         case 0x43 : //Decimal:67, LED OFF toggle button I think
             {
              P2DIR &= ~BIT1 + ~BIT2;   //lights off

              cmd_from_android = 0;
              break;
             }
         case 0x44 : //Decimal:68
             {
                 /*
              __delay_cycles(unit_length); //delay between entering case and tracking seek bar
              //start tracking SeekBar
              while(cmd_from_android != 0x65){ //Decimal: 101
                  P2DIR |= BIT1 + BIT2; //LEDs on to see PWM change in real time
                  LED_brightness = cmd_from_android; //receive a cmd_from_android seekBar progress value
                  TA1CCR1 = LED_brightness; //set brightness to PWM duty cycle
              }
              //stop tracking SeekBar
               *
               */ //remove later
              cmd_from_android = 0; //reset cmd_from_android good practices
              P2DIR &= ~BIT1 + ~BIT2; //LEDs off
              break;
              }
         case 0x49 : //Decimal:73, START SLEEP
             {
                 sleep_mode = 1;
                 cmd_from_android = 0;
                 break;
             }

        } //end switch(cmd_from_android)

        if(sleep_mode)
        {
            IE2 |= UCA0TXIE; //interrupt enable for uart
            cmd_from_android = 0;
        }

       while(sleep_mode) //if the transmit interrupt is enabled
       {
           P2DIR &= ~BIT1 + BIT2; //make sure lights are off when entering sleep mode

           TACCTL0 &= ~CCIE; //disable timer measuring sleep for REM freq.

         //  ADC10CTL0 |= ENC + ADC10SC; //adc data capture
         //  while(ADC10CTL1 & ADC10BUSY);
         //  ADC10CTL0 &= ~ENC;

/*//remove later
// ------------ DETECTION ALGORITHM FOR DETERMINING REM ------------ //
           if(cmd_from_android == 0x4A) //Decimal:74, START REM
           {
               TACCTL0 &= ~CCIE; //disable timer A0 for testing

               REM_bool = 1;
               cmd_from_android = 0;
           }
// ------------ END DETECTION ALGORITHM FOR DETERMINING REM ------------ //

            if(REM_bool)
            {
                IE2 |= UCA0TXIE;
            }

            //TA1CCR1 = LED_brightness;
            while(REM_bool)
            {
                ADC10CTL0 |= ENC + ADC10SC;
                while(ADC10CTL1 & ADC10BUSY);
                ADC10CTL0 &= ~ENC;

                switch(cmd_from_android)
                {
                case 0x4D: //Decimal:77
                {
                    //P2DIR |= BIT1; //lights on
                    P2DIR |= BIT1 + BIT2; //on
                    cmd_from_android = 0;
                    break;
                }
                case 0x4E: //Decimal:78
                {
                    P2DIR &= ~BIT1 + ~BIT2; //lights off
                    cmd_from_android = 0;
                    break;
                }
                case 0x4F: //Decimal:79
                {
                    P2DIR |= BIT2;              //P2.1 and 2.2 set as output
                    cmd_from_android = 0;
                    break;
                }
                case 0x50: //Decimal:80
                {
                    P2DIR &= ~BIT2;              //P2.1 and 2.2 set as output
                    cmd_from_android = 0;
                    break;
                }
                case 0x51: //Decimal:81
                {
                    P1OUT |= BIT7;
                    cmd_from_android = 0;
                    break;
                }
                case 0x52: //Decimal:82
                {
                    P1OUT &= ~BIT7;
                    cmd_from_android = 0;
                    break;
                }
                default:
                {}

                } //end switch(cmd_from_android)

                if(cmd_from_android == 0x49) //Decimal:75 (0x4B)
                {
                    REM_bool = 0;
                    sleep_mode = 0;
                    IE2 &= ~UCA0TXIE;
                }
            }
            P1OUT &= ~BIT7; //turn off motor
            P2DIR &= ~BIT1 + ~BIT2;              //P2.1 and 2.2 set as inputs (turned off)
            */ //remove later
            if(cmd_from_android == 0x49) //Decimal:73, arbitrary command from android to exit sleep mode
            {
                sleep_mode = 0;
                cmd_from_android = 0;
                IE2 &= ~UCA0TXIE;
            }

       }

    }

}

//------------------------Timer A0 interrupt-----------------------//

#pragma vector=TIMER0_A0_VECTOR
__interrupt void TIMER0A0_ISR(void) //Maybe move some of this code into main
{
   /* freq_state = P1IN & BIT5;
    if(timer_count == 100)
    {
       if(freq_counter >= 20 )  //10 Hz
       {
           REM_bool = 1;
       }

       else
       {
           REM_bool = 0; //set to one for testing main loop
       }

       freq_counter = 0;
       timer_count = 0;
    }

    else
    {
        if(freq_state != prev_freq_state)
               {
                freq_counter++;
               }

        timer_count++;
    }

    prev_freq_state = freq_state; */
}


//-----------Transmit and Receive interrupts-----------------------//

#pragma vector = USCIAB0TX_VECTOR
__interrupt void TransmitInterrupt(void)
{

  if(sleep_mode)//REM_bool                      //if the user is in REM sleep, send REM data
  {
     ADC10CTL0 |= ENC + ADC10SC; //adc data capture
     while(ADC10CTL1 & ADC10BUSY);
     ADC10CTL0 &= ~ENC;

     UCA0TXBUF = ADC10MEM >> 2;        // UART stored and sent in UCA0TXBUF, divide by 4 for easier transmission
  //  UCA0TXBUF = 99;
  //    UCA0TXBUF = 73;
  }

}

#pragma vector = USCIAB0RX_VECTOR
__interrupt void ReceiveInterrupt(void)
{
    cmd_from_android = UCA0RXBUF;
}
