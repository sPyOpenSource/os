package jx.zero.debug;

import java.io.OutputStream;
import java.io.IOException;

public class DebugOutputStream extends OutputStream {
  DebugChannel debugChannel;
  
  public DebugOutputStream(DebugChannel debugChannel) {
      this.debugChannel = debugChannel;
      /*debugChannel.write('X');*/
  }

  @Override
  public void write(int b) throws IOException {
    debugChannel.write(b);
  }
    /**
     * atomically write this buffer
     */
  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    debugChannel.writeBuf(b,off,len);
  }
}
