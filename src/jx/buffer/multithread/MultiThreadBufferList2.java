package jx.buffer.multithread;

import jx.zero.*;

public class MultiThreadBufferList2 extends MultiThreadBufferList {

    public MultiThreadBufferList2() {
	super(new Buffer2(null), null);
    }

    public MultiThreadBufferList2(Memory[] bufs) {
	super(new Buffer2(bufs[0]), null);
	if (verbose) {
	    cpuManager.dump("MultiThreadBufferList2(Memory[" + bufs.length + "])", this);
	}
	for(int i = 1; i < bufs.length; i++) {
	    if (verbose) Debug.out.println("       loop:" + i);
	    appendElement(new Buffer2(bufs[i]));
	}
	if (verbose) dump();
    }
}
