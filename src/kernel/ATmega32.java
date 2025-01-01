package kernel;

public class ATmega32 {
  //values for memory access (port access would be 0x20 lower)
  public final static short TIMSK  = (short)0x59;
  public final static short TIFR   = (short)0x58;
  public final static short TCCR1A = (short)0x4F;
  public final static short TCCR1B = (short)0x4E;
  public final static short TCNT1  = (short)0x4C;
  public final static short ICR1L  = (short)0x46;
  public final static short TCCR2  = (short)0x45;
  public final static short TCNT2  = (short)0x44;
  public final static short WDTCR  = (short)0x41;
  public final static short UCSRC  = (short)0x40;
  public final static short PORTA  = (short)0x3B;
  public final static short DDRA   = (short)0x3A;
  public final static short PINA   = (short)0x39;
  public final static short PORTB  = (short)0x38;
  public final static short DDRB   = (short)0x37;
  public final static short PINB   = (short)0x36;
  public final static short PORTC  = (short)0x35;
  public final static short DDRC   = (short)0x34;
  public final static short PINC   = (short)0x33;
  public final static short PORTD  = (short)0x32;
  public final static short DDRD   = (short)0x31;
  public final static short PIND   = (short)0x30;
  public final static short SPDR   = (short)0x2F;
  public final static short SPSR   = (short)0x2E;
  public final static short SPCR   = (short)0x2D;
  public final static short UDR    = (short)0x2C;
  public final static short UCSRA  = (short)0x2B;
  public final static short UCSRB  = (short)0x2A;
  public final static short UBRRL  = (short)0x29;
  public final static short ADMUX  = (short)0x27;
  public final static short ADCSRA = (short)0x26;
  public final static short ADCH   = (short)0x25;
  public final static short ADCL   = (short)0x24;
  //addresses for IRQ-handler, only internally used for MARKER.enterCodeAddr(...)
  public final static int IRQ_TIMER2_OVF  = 0x0016;
  public final static int IRQ_TIMER1_CAPT = 0x001A;
  public final static int IRQ_TIMER1_OVF  = 0x0026;
  public final static int IRQ_TIMER0_OVF  = 0x0026;
  public final static int IRQ_RXCOMPLETE  = 0x0036;
  public final static int IRQ_TXCOMPLETE  = 0x003E;
  public final static int IRQ_ADC         = 0x0042;
  public final static int STARTMETHOD     = 0x0060;
}
