package jx.fs.javafs;

import jx.zero.*;
import jx.zero.debug.*;

public class Main {
    public static void init(Naming naming) {
	DebugChannel d = (DebugChannel) naming.lookup("DebugChannel0");
	Debug.out = new jx.zero.debug.DebugPrintStream(new jx.zero.debug.DebugOutputStream(d));
	Debug.err = new jx.zero.debug.DebugPrintStream(new jx.zero.debug.DebugOutputStream(d));
	Debug.in  = new jx.zero.debug.DebugInputStream(d);

	Portal JAVAFSdep = new jx.fs.javafs.FileSystem();
	naming.registerPortal(JAVAFSdep, "JavaFS");
    }
}
