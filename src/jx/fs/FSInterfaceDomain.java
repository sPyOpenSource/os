package jx.fs;

import jx.zero.*;
import jx.zero.debug.*;
import jx.devices.bio.BlockIO;

/* DummyClock */
import jx.zero.Clock;
import jx.zero.CycleTime;

final class MyDummyClock implements Clock {
    int t;
    @Override
    public int getTimeInMillis() { return t++; }
    @Override
    public long getTicks() {return 0;}
    @Override
    public int getTicks_low(){return 0;}
    @Override
    public int getTicks_high(){return 0;}
    @Override
    public  void getCycles(CycleTime c){}
    @Override
    public void subtract(CycleTime result, CycleTime a, CycleTime b){}
    @Override
    public int toMicroSec(CycleTime c) {return 0;}
    @Override
    public int toNanoSec(CycleTime c) {return 0;}
    @Override
    public int toMilliSec(CycleTime c) {return 0;}
}
/*=============*/

public class FSInterfaceDomain {

    Naming naming;

    static final int EXT2FS_BLOCKSIZE = 1024;

    public static void init(final Naming naming, String[] args) {
	DebugChannel d = (DebugChannel) naming.lookup("DebugChannel0");
	CPUManager cpuManager = (CPUManager) naming.lookup("CPUManager");
	Debug.out = new DebugPrintStream(new DebugOutputStream(d));

	String bioname = args[0];

	Debug.out.println("Domain FSInterfaceDomain speaking.");
	cpuManager.setThreadName("FSDomain-Main");

	BlockIO bio = (BlockIO) LookupHelper.waitUntilPortalAvailable(naming, bioname);

	new FSInterfaceDomain(naming, bio);
    }

    FSInterfaceDomain(final Naming naming, BlockIO bio) {
	try {
	    this.naming = naming;

	    Debug.out.println("Create FileSystem on BlockIO");
	    Debug.out.println("Capacity: " + bio.getCapacity());

	    //final jx.fs.javafs.FileSystem jfs = new jx.fs.javafs.FileSystem();
	    Clock clock = new MyDummyClock();
	    //jfs.init(bio, new jx.bio.buffercache.BufferCache(bio, clock, 800, 1000, 100, EXT2FS_BLOCKSIZE), clock);	    	    
	    //jfs.build("TestFS", 1024);

	    //final FilesystemImpl ifs = new FilesystemImpl(jfs);

	    Debug.out.println("FileSystem is ready !!!");
	    //naming.registerPortal(ifs, "FSInterface");

	} catch(Exception e) {
	    e.printStackTrace();
	    throw new Error();
	}
    }
}
