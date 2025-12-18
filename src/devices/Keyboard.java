package devices;

import tools.EventBuffer;

public class Keyboard extends Device {
  public boolean dontRepeatModifiers, dontRepeatNormalKeys;

  //needed ones are duplicated in KEvT==KeyEventTranslator
  private final static short LEFT_CTRL   = (short)0x0001;
  private final static short RIGHT_CTRL  = (short)0x0002;
  private final static short LEFT_ALT    = (short)0x0004;
  private final static short RIGHT_ALT   = (short)0x0008;
  private final static short LEFT_SHIFT  = (short)0x0010;
  private final static short RIGHT_SHIFT = (short)0x0020;
  private final static short CAPS_LOCK   = (short)0x0040;
  private final static short NUM_LOCK    = (short)0x0080;
  private final static short SCROLL_LOCK = (short)0x0100;
  private final static short LEFT_WIN    = (short)0x0200;
  private final static short RIGHT_WIN   = (short)0x0400;
  private final static short CONTEXT     = (short)0x0800;
  
  private byte myHID;
  private short modifier;
  private int lastScan, lastKey;
  
  public Keyboard() {
    myHID = ++DeviceList.in.HIDs;
    toggleModifierAndRefreshLEDs((short)0);
  }
  
  @Override
  public void handleIRQ(int no) {
    int actScan, fullScan;
    boolean mod;
    
    //get scancode and try to categorize it
    actScan=((int)MAGIC.rIOs8(0x60))&0xFF;
    lastScan=(lastScan<<8)|actScan;
    //if (general MF2-Prefix, 2-Byte-Prefix, last was 2-Byte-Prefix) key is not yet complete
    if ((lastScan&~1)==0xE0 || (lastScan>>>8)==0xE1) return;
    fullScan=lastScan;
    lastScan=0;
    if (isInternalModifier(fullScan)) return;
    mod=isRegularModifier(fullScan);
    
    //don't repeat modifier if dontRepeatModifiers==true
    if (dontRepeatModifiers && mod && fullScan==lastKey) return;
    //don't repeat normal key if dontRepeatNormalKeys==true
    if (dontRepeatNormalKeys && !mod && fullScan==lastKey) return;
    
    //check for sysreq, otherwise insert into event buffer
    if (isSysRequest(fullScan)) MAGIC.inline(0xCC);
    else {
      DeviceList.in.insertEvent(EventBuffer.TP_KEYB, myHID, modifier, fullScan);
      lastKey=fullScan;
    }
  }
  
  private void setModifier(short mod, boolean on) {
    modifier&=~mod;
    if (on) modifier|=mod;
  }
  
  private void toggleModifierAndRefreshLEDs(short mod) {
    byte LEDcode = (byte)0;
    modifier ^= mod;
    if ((modifier & SCROLL_LOCK) != (short)0) LEDcode |= (byte)1;
    if ((modifier & NUM_LOCK)    != (short)0) LEDcode |= (byte)2;
    if ((modifier & CAPS_LOCK)   != (short)0) LEDcode |= (byte)4;
    while (((int)MAGIC.rIOs8(0x64)&0x2) != 0) /* wait */;
    MAGIC.wIOs8(0x60, (byte)0xED);
    while (((int)MAGIC.rIOs8(0x64) & 0x2) != 0) /* wait */;
    MAGIC.wIOs8(0x60, LEDcode);
  }
  
  private boolean isInternalModifier(int code) {
    if ((code&0xFF) >= 0xF0) return true; //don't handle ACK
    switch (code) {
      case 0xE02A: case 0xE0AA: case 0xE0B6: case 0xE036:
        return true;
    }
    return false;
  }
  
  private boolean isRegularModifier(int code) {
    boolean make=(code&0x80)==0;
    switch (code&~0x80) {
      case 0x1D:   setModifier(LEFT_CTRL,   make); break;
      case 0xE01D: setModifier(RIGHT_CTRL,  make); break;
      case 0x38:   setModifier(LEFT_ALT,    make); break;
      case 0xE038: setModifier(RIGHT_ALT,   make); break;
      case 0x2A:   setModifier(LEFT_SHIFT,  make); break;
      case 0xE02A: setModifier(RIGHT_SHIFT, make); break;
      case 0xE05B: setModifier(LEFT_WIN,    make); break;
      case 0xE05C: setModifier(RIGHT_WIN,   make); break;
      case 0xE05D: setModifier(CONTEXT,     make); break;
      case 0x3A:
        if (make && lastScan != 0x3A) //switch only at down-event and at first time
          toggleModifierAndRefreshLEDs(CAPS_LOCK);
        break;
      case 0x45:
        if (make && lastScan != 0x45) //switch only at down-event and first time
          toggleModifierAndRefreshLEDs(NUM_LOCK);
        break;
      case 0x46:
        if (make && lastScan != 0x46) //switch only at down-event and first time
          toggleModifierAndRefreshLEDs(SCROLL_LOCK);
        break;
      default: //no modifier
        return false;
    }
    //modifier
    return true;
  }
  
  private boolean isSysRequest(int code) {
    return (modifier & (LEFT_CTRL |RIGHT_CTRL)) !=(short)0
        && (modifier & (LEFT_SHIFT|RIGHT_SHIFT))!=(short)0
        && code == 0x01;
  }
}
