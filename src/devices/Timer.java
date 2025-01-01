package devices;

public class Timer extends Device {
  private int cnt;
  
  public void handleIRQ(int no) {
    int dig=(int)'-';
    
    cnt++;
    switch (cnt&3) {
      //already initialized: case 0: dig=(int)'-'; break;
      case 1: dig=(int)'\\'; break;
      case 2: dig=(int)'|'; break;
      case 3: dig=(int)'/'; break;
    }
    MAGIC.wMem16(0xB8F9E, (short)(dig|0x2F00));
  }
}
