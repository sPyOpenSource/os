package jx.emulation;

import jx.zero.*;
import java.util.*;
import jx.InitialNaming;

public class NamingImpl implements Naming {
    Clock clock;
    Vector portals = new Vector();
    BlockIOFile ide;
    
    // BlockIOFile ide;
    public NamingImpl() {
	clock = new ClockImpl(); 
        registerPortal(clock, "Clock");
	registerPortal(new ProfilerImpl(), "Profiler");
	registerPortal(new MemoryManagerImpl(), "MemoryManager");
    }
    
    @Override
    public Portal lookup(String name) {
	if (name.equals("IDE") && ide == null) {
	    ide = new BlockIOFile("/tmp/jfs_file", 2 * 1024 * 20);
	    registerPortal(ide, "IDE");
	}
	return (Portal) InitialNaming.lookup(name);
    }

    // FIXME jgbauman: Quickfix does this work
    @Override
    public Portal lookupOrWait(String depName) {
       return (Portal) InitialNaming.lookup(depName);
    }

    @Override
    public void registerPortal(Portal dep, String name) {
	InitialNaming.registerPortal(dep, name);
    }
}
