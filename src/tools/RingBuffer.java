package tools;

public class RingBuffer {
  private byte[] buffer;
  private int size, readPtr, writePtr;
  
  public RingBuffer(int is) {
    size=is;
    buffer=new byte[size];
    //readPtr and writePtr are initialized to 0
  }
  
  public int readAvail() {
    int res=writePtr-readPtr;
    if (res<0) return res+size;
    return res;
  }
  
  public int writeAvail() {
    int res=readPtr-writePtr;
    if (res<=0) res+=size;
    return res-1;
  }
  
  public boolean putByte(byte b) {
    if (writeAvail()<1) return false;
    buffer[writePtr++]=b;
    writePtr%=size;
    return true;
  }
  
  public boolean putShort(short s) {
    if (writeAvail()<2) return false;
    putByte((byte)s);
    putByte((byte)(s>>>8));
    return true;
  }
  
  public boolean putInt(int i) {
    if (writeAvail()<4) return false;
    putShort((short)i);
    putShort((short)(i>>>16));
    return true;
  }
  
  public boolean putLong(long l) {
    if (writeAvail()<8) return false;
    putInt((int)l);
    putInt((int)(l>>>32));
    return true;
  }
  
  public byte getByte() {
    byte res;
    if (readAvail()<1) return (byte)0;
    res=buffer[readPtr];
    readPtr=(readPtr+1)%size;
    return res;
  }
  
  public short getShort() {
    int b1=(int)getByte()&0xFF, b2=(int)getByte()&0xFF;
    return (short)((b2<<8)|b1);
  }
  
  public int getInt() {
    int s1=(int)getShort()&0xFFFF, s2=(int)getShort()&0xFFFF;
    return ((s2<<16)|s1);
  }
  
  public long getLong() {
    int i1=getInt(), i2=getInt();
    return (((long)i2)<<32)|(((long)i1)&0xFFFFFFFFl);
  }
}
