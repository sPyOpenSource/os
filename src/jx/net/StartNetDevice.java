package jx.net;

import jx.zero.*;

import jx.timer.*;

import jx.devices.pci.*;

import metaxa.os.devices.net.D3C905;
import metaxa.os.devices.net.ComInit;

import jx.devices.*;

import jx.zero.debug.Dump;

import jx.devices.net.*;

import jx.devices.net.emulation.EmulNetFinder;
import jx.init.InitNaming;


public class StartNetDevice {
    
    static D3C905 init3com(final Naming naming) {
	//SleepManager sleepManager = new SleepManagerImpl();
	TimerManager timerManager = (TimerManager)LookupHelper.waitUntilPortalAvailable(naming, "TimerManager");
	

	Debug.out.println("lookup PCI Access Point...");
	PCIAccess bus = (PCIAccess)LookupHelper.waitUntilPortalAvailable(naming, "PCIAccess");

	Debug.out.println("scanning PCIBus for 3c905 devices...");
	ComInit com = new ComInit(timerManager, /*sleepManager*/null, null);
	final D3C905[] nics = com.findDevice(bus);
	if (nics == null) return null;
	final D3C905 nic = nics[0];

	if (nic == null) return null;

	if (! nic.NicOpen()) {
	    throw new Error("Cannot initialize network card.");
	}

	nic.NICSetReceiveMode(null);
	nic.unmaskInterrupts();

	Debug.out.print("Ethernet address: ");
	Dump.xdump(nic.getMACAddress(), 6);

	return nic;
    }


    public static void main(String[] args) {
	Naming naming = InitialNaming.getInitialNaming();
        //naming = new InitNaming(naming);
	Device[] devices = null;
	NetworkDevice nic;

	EmulNetFinder cFinder = new EmulNetFinder(args[1], args[2]);
	devices = cFinder.find(new String[] {});
	//Debug.out.println("cfinder");
	if (devices != null) {
	    naming.registerPortal((NetworkDevice)devices[0], args[0]);
	    return;
	}
Debug.out.println("3com");
	if ((nic = init3com(naming)) != null ) {
	    naming.registerPortal(nic, args[0]);
	    return;	    
	}

	throw new Error("No network device found");
    }
}
