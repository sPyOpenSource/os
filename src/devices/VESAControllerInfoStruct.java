package devices;

public class VESAControllerInfoStruct extends STRUCT {
  @SJC(offset=0x00) public int id;
  @SJC(offset=0x04) public byte versionDecimal;
  @SJC(offset=0x05) public byte version;
  @SJC(offset=0x0E) public short videoModePtrOff;
  @SJC(offset=0x10) public short videoModePtrSeg;
}
