package jx.zero;

import jx.InitNaming;

public class LookupHelper {
    private static final CPUManager cpuManager;
    public static boolean verbose = true;
    
    static {
	cpuManager = (CPUManager) InitialNaming.getInitialNaming().lookup("CPUManager");
    }

    public static Portal NEWwaitUntilPortalAvailable(Naming naming, String name) {
	return naming.lookupOrWait(name);
    }

    public static Portal waitUntilPortalAvailable(Naming naming, String name) {
	Portal p = null;
	if (verbose) Debug.out.println("Lookup " + name);
	for(;;) {
	    p = InitNaming.lookup(name);
	    if (p != null) break;
            for(int i = 0; i < 20; i++) cpuManager.yield();
	}
	if (verbose) Debug.out.println("    --> " + name);
	return p;
    }
}
