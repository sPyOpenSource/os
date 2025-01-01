package tools;

public class EventBuffer extends RingBuffer {
  public final static byte TP_KEYB  = (byte)1;
  public final static byte TP_MOUSE = (byte)2;
  
  public byte HIDs;
  
  public EventBuffer(int size) {
    super(size);
  }
  
  public boolean insertEvent(byte HIDType, byte HIDNr, short code, int codeExt) {
    //bits in event:
    // 63..32: codeExt
    // 31..16: code
    // 15.. 8: devID
    //  7.. 0: devType
    if ((HIDType==(byte)-1) || (HIDNr==(byte)-1)) return false; //no valid HID
    //return false if no space left, true if successfull insertion
    return putLong((((long)HIDType)&0xFFl) | ((((long)HIDNr)&0xFFl)<<8)
        | ((((long)code)&0xFFFFl)<<16) |((((long)codeExt)&0xFFFFFFFFl)<<32));
  }
  
  public boolean eventAvail() {
    return readAvail()>=8;
  }
  
  public long fetchEvent() {
    return getLong();
  }
  
  public byte extractHIDType(long event) {
    return (byte)event;
  }
  
  public byte extractHIDNr(long event) {
    return (byte)(event>>>8);
  }

  public short extractCode(long event) {
    return (short)(event>>>16);
  }

  public int extractCodeExt(long event) {
    return (int)(event>>>32);
  }
}
