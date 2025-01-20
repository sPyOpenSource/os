package jx.emulation;

import java.util.HashMap;
import jx.zero.*;

public class NamingImpl implements Naming {
    private HashMap portalNames = new HashMap();
    
    public NamingImpl() {
        registerPortal(new ClockImpl(), "Clock");
	registerPortal(new ProfilerImpl(), "Profiler");
	registerPortal(new MemoryManagerImpl(), "MemoryManager");
        registerPortal(new BlockIOFile("/tmp/jfs_file", 2 * 1024 * 20), "IDE");
    }
    
    @Override
    public Portal lookup(String name) {
	return (Portal) portalNames.get(name);
    }

    // FIXME jgbauman: Quickfix does this work
    @Override
    public Portal lookupOrWait(String depName) {
       return (Portal) portalNames.get(depName);
    }

    @Override
    public void registerPortal(Portal dep, String name) {
	portalNames.put(name, dep);
    }
}
