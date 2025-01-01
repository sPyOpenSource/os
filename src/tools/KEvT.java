package tools;

import binimp.ByteData;

public class KEvT {
  //there are some more, all duplicated in keyboard-driver
  public final static short MD_NONE        = (short)0x0000;
  public final static short MD_LEFT_CTRL   = (short)0x0001;
  public final static short MD_RIGHT_CTRL  = (short)0x0002;
  public final static short MD_LEFT_ALT    = (short)0x0004;
  public final static short MD_RIGHT_ALT   = (short)0x0008;
  public final static short MD_LEFT_SHIFT  = (short)0x0010;
  public final static short MD_RIGHT_SHIFT = (short)0x0020;
  public final static short MD_CAPS_LOCK   = (short)0x0040;
  public final static short MD_NUM_LOCK    = (short)0x0080;
  public final static short MD_SCROLL_LOCK = (short)0x0100;
  public final static short MD_LEFT_WIN    = (short)0x0200;
  public final static short MD_RIGHT_WIN   = (short)0x0400;
  public final static short MD_CONTEXT     = (short)0x0800;
  //some useful combinations
  public final static short MD_ANY_CTRL    = (short)0x0003;
  public final static short MD_ANY_ALT     = (short)0x000C;
  public final static short MD_ANY_SHIFT   = (short)0x0030;
  public final static short MD_ANY_WIN     = (short)0x0600;

  //constants for control-keys, mapped in 1-31 of ASCII
  public final static char KC_BACKSPACE = (char)0x0008;
  public final static char KC_TAB       = (char)0x0009;
  public final static char KC_ENTER     = (char)0x000A;
  public final static char KC_ESC       = (char)0x001B;
  //constants for control-keys without mapping
  public final static char KC_UNKNOWN   = (char)0xFF00;
  public final static char KC_F1        = (char)0xFF01;
  public final static char KC_F2        = (char)0xFF02;
  public final static char KC_F3        = (char)0xFF03;
  public final static char KC_F4        = (char)0xFF04;
  public final static char KC_F5        = (char)0xFF05;
  public final static char KC_F6        = (char)0xFF06;
  public final static char KC_F7        = (char)0xFF07;
  public final static char KC_F8        = (char)0xFF08;
  public final static char KC_F9        = (char)0xFF09;
  public final static char KC_F10       = (char)0xFF0A;
  public final static char KC_F11       = (char)0xFF0B;
  public final static char KC_F12       = (char)0xFF0C;
  public final static char KC_PRINT     = (char)0xFF0D;
  public final static char KC_SCROLL    = (char)0xFF0E;
  public final static char KC_PAUSE     = (char)0xFF0F;
  public final static char KC_CURSLEFT  = (char)0xFF10;
  public final static char KC_CURSRIGHT = (char)0xFF11;
  public final static char KC_CURSUP    = (char)0xFF12;
  public final static char KC_CURSDOWN  = (char)0xFF13;
  public final static char KC_HOME      = (char)0xFF14;
  public final static char KC_END       = (char)0xFF15;
  public final static char KC_PAGEUP    = (char)0xFF16;
  public final static char KC_PAGEDOWN  = (char)0xFF17;
  public final static char KC_INS       = (char)0xFF18;
  public final static char KC_DEL       = (char)0xFF19;
  public final static char KC_CENTER    = (char)0xFF1A; //used for '5' with NUM off
  public final static char KC_NUM       = (char)0xFF1B;
  public final static char KC_SYSRQ     = (char)0xFF1C;
  public final static char KC_BREAK     = (char)0xFF1D;
  public final static char KC_LCTRL     = (char)0xFF1E;
  public final static char KC_RCTRL     = (char)0xFF1F;
  public final static char KC_LALT      = (char)0xFF20;
  public final static char KC_RALT      = (char)0xFF21;
  public final static char KC_LSHIFT    = (char)0xFF22;
  public final static char KC_RSHIFT    = (char)0xFF23;
  public final static char KC_CAPSLOCK  = (char)0xFF24;
  public final static char KC_LWIN      = (char)0xFF25;
  public final static char KC_RWIN      = (char)0xFF26;
  public final static char KC_CONTEXT   = (char)0xFF27;

  //mask to map all make/break-codes to make-codes, which is useful for special key-handling
  public final static int KC_MAKEMASK = 0xFFFFFF7F;
  
  //constant table for translation
  private final static int codeVal[] = {0x47, 0x48, 0x49, 0x4B, 0x4C,
    0x4D, 0x4F, 0x50, 0x51, 0x52, 0x53, 0, //end NUM-block with 0
    0x1D, 0x2A, 0x36, 0x38, 0x3A,
    0x3B, 0x3C, 0x3D, 0x3E, 0x3F,
    0x40, 0x41, 0x42, 0x43, 0x44,
    0x45, 0x46, 0x54, 0x57, 0x58,
    0xE01D, 0xE037, 0xE038, 0xE046, 0xE047,
    0xE048, 0xE049, 0xE04B, 0xE04D, 0xE04F,
    0xE050, 0xE051, 0xE052, 0xE053, 0xE05B,
    0xE05C, 0xE05D, 0xE11D45};
  private final static char transVal[] = {KC_HOME, KC_CURSUP, KC_PAGEUP, KC_CURSLEFT, KC_CENTER,
    KC_CURSRIGHT, KC_END, KC_CURSDOWN, KC_PAGEDOWN, KC_INS, KC_DEL, '\u0000', //end num-block for index-matching
    KC_LCTRL, KC_LSHIFT, KC_RSHIFT, KC_LALT, KC_CAPSLOCK,
    KC_F1, KC_F2, KC_F3, KC_F4, KC_F5,
    KC_F6, KC_F7, KC_F8, KC_F9, KC_F10,
    KC_NUM, KC_SCROLL, KC_SYSRQ, KC_F11, KC_F12,
    KC_RCTRL, KC_PRINT, KC_RALT, KC_BREAK, KC_HOME,
    KC_CURSUP, KC_PAGEUP, KC_CURSLEFT, KC_CURSRIGHT, KC_END,
    KC_CURSDOWN, KC_PAGEDOWN, KC_INS, KC_DEL, KC_LWIN,
    KC_RWIN, KC_CONTEXT, KC_PAUSE};
  
  //loadable table for translation
  private final static int TRANSCODES = 0x60;
  private byte[] translation;
  
  public boolean useTable(int country) {
    switch (country) {
      //case 1: translation=USKeyTable.data; return true;
      case 49: translation=ByteData.DEKeyTable; return true;
    }
    return false;
  }
  
  public char getChar(short code, int codeExt) {
    boolean ctrl=false, shift=false, alt=false, altgr=false;

    if (translation==null) return (char)0x0000; //not initialized

    if (codeExt>0xFFFF //unsupported 3-byte-key-codes (ie.: pause)
        || (codeExt>>>8)==0xE0 //no extended keys supported
        || codeExt>=0xFF //unsupported 2-byte-key-code
        || (codeExt&0x80)>0 //break-code -> no character
        || codeExt>=TRANSCODES) return (char)0x0000; //unsupported make-code
    
    if ((code&MD_ANY_SHIFT)!=(short)0) shift=true;
    if ((code&MD_CAPS_LOCK)!=(short)0) shift=!shift;
    if ((code&MD_ANY_CTRL)!=(short)0) ctrl=true;
    if ((code&MD_LEFT_ALT)!=(short)0) alt=true;
    if ((code&MD_RIGHT_ALT)!=(short)0) altgr=true;

    if (ctrl && alt) { //ctrl+alt should emulate altgr
      altgr=true;
      ctrl=false;
      alt=false;
    }
    
    if ((ctrl || alt) //no regular key
        || (altgr && shift)) return (char)0x0000; //invalid modifier-combination for regular key
    
    if (shift) return (char)((int)translation[codeExt+TRANSCODES]&0xFF); //shift char
    if (altgr) return (char)((int)translation[codeExt+TRANSCODES*2]&0xFF); //altgr char
    if ((code&MD_NUM_LOCK)==(short)0 && codeExt>=0x47 && codeExt<=0x53 //these keys are not normal characters and have to be taken with getKey(.)
        && codeExt!=0x4A && codeExt!=0x4E) return (char)0x0000; //keys in between are independent of NUM-state and correctly assigned in layer0-array
    return (char)((int)translation[codeExt]&0xFF); //normal char
  }

  public char getKey(short code, int codeExt) {
    char c;
    int ind=0;
    
    if ((codeExt&0x80)!=0) return (char)0x0000; //break-code -> no character
    if ((code&MD_NUM_LOCK)==(short)0) { //scan for NUM-off-chars
      while (codeVal[ind]!=0) {
        if (codeVal[ind]==codeExt) return transVal[ind];
        ind++;
      }
    }
    else while (codeVal[ind++]!=0); //step over NUM-off-chars
    if (codeExt<=0x7F && translation!=null
        && (c=(char)((int)translation[codeExt]&0xFF))!=(char)0x0000) return c; //a normal character-key
    while (ind<codeVal.length) { //search through translation-table
      if (codeVal[ind]==codeExt) return transVal[ind];
      ind++;
    }
    return KC_UNKNOWN;
  }
}
