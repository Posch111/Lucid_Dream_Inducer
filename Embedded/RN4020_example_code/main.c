#include <msp430.h>
#include<stdio.h>
char buffer[20]; //buffer to receive

// TODO
//ADC code
//RN4020 configuration code
//recieve data code.. what is our protocall?
//


//char type[] = {"SUW,2A19,13\r"};         //what are we returning?... battery status
char type[] = {"SUW,123456789012345678901234567890CC,0123\r"};//write to private characteristic
volatile int sendPointer = 0;
volatile unsigned int receive = 0;
int data = 1237;

//takes int input ranging from 0 to 9999 and inserts it into the type char array to be sent to private characterisitc
void int_to_char(int x)
{
    type[40] = (x%10)+48;
    x = x/10;
    type[39] = (x%10)+48;
    x = x/10;
    type[38] = (x%10)+48;
    x = x/10;
    type[37] = (x%10)+48;
}

void Configure_UART()

{

P1SEL = BIT1 + BIT2;

P1SEL2 = BIT1 + BIT2;

UCA0CTL1 |= UCSSEL_2;

UCA0BR0 = 0X08;

UCA0BR1 = 0X00;

UCA0MCTL = UCBRS2 + UCBRS0;

UCA0CTL1 &= ~UCSWRST;

}
//configure pins

void Configure_Pins()

{

P1DIR |= BIT0; //Pin 0, LED connection

P1OUT &= ~BIT0;

//&nbsp;

}
//main loop

void main(void)

{
//insert data into send char array
int_to_char(data);


WDTCTL = WDTPW | WDTHOLD; // Stop watchdog timer

BCSCTL1 = CALBC1_1MHZ; // Set range

DCOCTL = CALDCO_1MHZ;

Configure_UART();

Configure_Pins();
//enable send and receive with uart
UC0IE |= UCA0RXIE+UCA0TXIE;

__bis_SR_register(CPUOFF + GIE); //set cpuoff and enable interrupts

while(1)
{
data = data + 1;
if(data > 2000) data = 0;
int_to_char(data);
}
}

//cpu is turned on whenever interrupt occurs and in main routine its off(stack restored)
//recieve code
#pragma vector = USCIAB0RX_VECTOR

__interrupt void USCI0RX_ISR(void)

{

P1OUT &=~BIT6;

buffer[receive++] = UCA0RXBUF;

if(UCA0RXBUF == '\n')

{
receive = 0;

__bic_SR_register(GIE); //disable interrupt as we want to make a decision now

char conn = buffer[0];

if(conn != 'C')

{

char c = buffer[9]; //decide number to be accessed

switch(c)

{

case '1': P1OUT |= BIT0;

break;

case '0': P1OUT &= ~BIT0;

break;

}

}

__bis_SR_register(GIE); //okay to talk again

}

}


//tx routine
// UART TX ISR routine
#pragma vector = USCIAB0TX_VECTOR
__interrupt void USCI0TX_ISR(void)
{
  UCA0TXBUF = type[sendPointer++];
    if(type[sendPointer] == '\0')
    {
      sendPointer = 0;
      //UC0IE &= ~UCA0TXIE;
    }
}


