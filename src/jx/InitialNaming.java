package jx;

import jx.zero.*;
import jx.zero.debug.*;

import java.util.Hashtable;
import java.util.Enumeration;

public class InitialNaming
    {
    static Naming baseNaming;
    static Hashtable names = new Hashtable();

    public static InitialNaming getInitialNaming() {
        return new InitialNaming();
    }
    
    public InitialNaming(){
        
    }
    
    public InitialNaming(Naming baseNaming) {
	InitialNaming.baseNaming = baseNaming;

	// enable debugging
	DebugSupport debugSupport = (DebugSupport)baseNaming.lookup("DebugSupport");
	debugSupport.registerMonitorCommand("initns", new MonitorCommand() {
                @Override
		public void execCommand(String[] args) {
		    Enumeration n = names.keys();
		    for(; n.hasMoreElements();) {
			String name = (String)n.nextElement();
			Debug.out.println(name);
		    }
		}
                @Override
		public String getHelp() {
		    return "Dump contents of DomainInit name server";
		}
	    });

	// copy well known portals of domainzero to our hashtable
	add("CPUManager");
	add("DebugSupport");
	add("Clock");
	add("DebugChannel0");
	add("DebugSupport");
	add("HLSchedulerSupport");
	add("LLSchedulerSupport");
	add("JAVASchedulerSupport");
	add("SMPCPUManager");
	add("MemoryManager");
	add("DomainManager");
	add("ComponentManager");
	add("BootFS");
	add("Ports");
	add("Profiler");
	add("IRQ");
	add("NetEmulation");
	add("FBEmulation");
	add("DiskEmulation");
	add("TimerEmulation");
    }

    public static void registerPortal(Portal portal, String name) {
	names.put(name, portal);
    }
    
    public static Portal lookup(String name) {
	return (Portal) names.get(name);
    }
    
    private static void add(String name) {
	Portal p = baseNaming.lookup(name);
	if (p == null) return;
	names.put(name, p);
    }

    private void serviceFinalizer() {
	Debug.out.println("*****  InitNaming: THIS SERVICE TERMINATES NOW ***");
    }
}
