package jx.net.protocols;

import jx.zero.*;

import jx.timer.TimerManager;

import jx.net.IPAddress;
import jx.net.NetInit;

import jx.devices.net.*;
import jx.init.InitNaming;


public class StartNetworkProtocols {
    /**
     * args[0] NetworkDevice
     * args[1] TimerManager
     * args[2] NetInit
     * @param args
     * @throws java.lang.Exception
     */

    public static void main(String[] args) throws Exception {
	Naming naming = InitialNaming.getInitialNaming();
        //naming = new InitNaming(naming);
	NetworkDevice nic = (NetworkDevice)LookupHelper.waitUntilPortalAvailable(naming, args[0]);

	MemoryManager memMgr = (MemoryManager) naming.lookup("MemoryManager");

	Memory[] bufs = new Memory[10];
	for(int i = 0; i < bufs.length; i++) {
	    bufs[i] = memMgr.alloc(1514);
	}
	final TimerManager timerManager = (TimerManager)LookupHelper.waitUntilPortalAvailable(naming, args[1]);
	IPAddress myAddress = null;
	if (args.length == 4) {
	    myAddress = new IPAddress(args[3]);
	} 
	NetInit net = new jx.netmanager.NetInit(nic, timerManager, bufs, myAddress);
	naming.registerPortal(net, args[2]);
        naming.registerPortal(timerManager, "TimerManager");
    }
}
