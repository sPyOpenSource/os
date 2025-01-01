package devices;

public class VESAModeInfoStruct extends STRUCT {
  public final static short ATTR_SUPPORTED  = (short)0x0001;
  public final static short ATTR_GRAPHICAL  = (short)0x0010;
  public final static short ATTR_LINFRMBUF  = (short)0x0080;
  public final static short ATTR_DOUBLESCAN = (short)0x0100;
  public final static short ATTR_INTERLACED = (short)0x0200;
  
  @SJC(offset=0x00) public short attributes;
  @SJC(offset=0x12) public short xRes;
  @SJC(offset=0x14) public short yRes;
  @SJC(offset=0x19) public byte colDepth;
  @SJC(offset=0x28) public int lfbAddress;
}
