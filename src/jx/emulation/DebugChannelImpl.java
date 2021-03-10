package jx.emulation;

import java.io.*;
import jx.zero.debug.*;

class DebugChannelImpl implements DebugChannel {
    @Override
    public void write(int b) {
	System.out.write(b);
    }
    @Override
    public int read() {
	try {
	    return System.in.read();
	}catch(IOException ex) {
	}
	return -1;
    }
    @Override
    public  void writeBuf(byte[] b, int off, int len) {
	for(int i=off; i<off+len; i++) {
	    write(b[i]);
	}
    }
}
