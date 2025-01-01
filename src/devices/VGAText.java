package devices;

import kernel.BIOS;

public class VGAText extends TextDriver {
  public int textmodeAddress;
  
  private static final int CRTCind=0x03D4;  //VGA-CRTC-index
  private static final int CRTCdat=0x03D5;  //VGA-CRTC-data
  private int colorFG;
  private int colorBG;
  
  public VGAText() {
    textCols=80;
    textLines=25;
    textmodeAddress=0xB8000;
    reInit();
  }
  
  private void reInit() {
    cursorX=cursorY=0;
    colorFG=0x07;
    colorBG=0x00;
    enableCursor(true);
  }
  
  public void setTextMode() {
    MAGIC.wMem16(BIOS.EAX, (short)3);
    BIOS.rint(0x10);
    reInit();
  }
  
  public void setColor(int fg, int bg) {
    colorFG=fg;
    colorBG=bg;
  }

  public void putChar (int x, int y, char c){
    int NV=((int)c&0xFF)|((colorFG&0xF)<<8)|((colorBG&7)<<12);
    int offset;
    
    offset=y*textCols+x;
    if (offset<0 || offset>=textCols*textLines) return;    //don't try to put characters outside screen
    MAGIC.wMem16(textmodeAddress+(offset<<1), (short)NV);
  }

  public void enableCursor(boolean on) {
    int dummy;
    
    MAGIC.wIOs8(CRTCind, (byte)0x0A);      //cursor start register
    dummy=(int)MAGIC.rIOs8(CRTCdat)&0xDF;      //save other bits
    if (!on) dummy|=0x20;      //bit set means cursor off
    MAGIC.wIOs8(CRTCdat, (byte)dummy);
  }

  public void setCursor(int newX, int newY) {
    int newVal; //cursor position
    
    if (newX<0 || newX>=textCols || newY<0 || newY>=textLines) return;
    newVal=newX+newY*textCols;
    MAGIC.wIOs8(CRTCind, (byte)0x0E);      //cursor location high
    MAGIC.wIOs8(CRTCdat, (byte)(newVal>>8));
    MAGIC.wIOs8(CRTCind, (byte)0x0F);      //cursor location low
    MAGIC.wIOs8(CRTCdat, (byte)(newVal&0xFF));
    cursorX=newX;
    cursorY=newY;
  }

  private void clearLine(int y) {
    int i, offset=(y*textCols)>>1;
    int NV=(int)' '|((colorFG&7)<<8)|((colorBG&3)<<12);
    int dummy=NV|(NV<<16); //write two displayed characters at once

    for (i=0; i<(textCols>>>1); i++) {
      MAGIC.wMem32(textmodeAddress+((offset+i)<<2), dummy);
    }
  }

  public void scroll() {
    int i, diff, max;
    
    diff=textCols<<1;
    max=textmodeAddress+((textCols*textLines)<<1);
    for (i=textmodeAddress+diff; i<max; i+=4) MAGIC.wMem32(i-diff, MAGIC.rMem32(i));
    clearLine(textLines-1);
  }

  public void cls() {
    int i;

    for (i=0; i<textLines; i++) clearLine(i);
  }
}
