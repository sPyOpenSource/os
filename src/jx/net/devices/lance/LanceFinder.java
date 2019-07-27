package jx.net.devices.lance;

import jx.devices.DeviceFinder;
import jx.devices.Device;
import jx.zero.*;

import jx.devices.pci.*;
import jx.init.InitNaming;
import org.jnode.driver.net.lance.LanceDriver;
import org.jnode.driver.net.lance.LanceFlags;

public class LanceFinder implements DeviceFinder {
    final static int    NIC_VENDOR_ID = 0x1022;           
    final static int    NIC_PCI_DEVICE_ID_Am79C970 = 0x2000;
    final static String DESCRIPTION = "Chip Am79C970/1/3/5; PCnet PCI Ethernet Controller";
    private Ports ports;
    private IRQ irq;
    private MemoryManager rm;


    //@Override
    public Device[] find(String[] args) {
        Naming naming = InitialNaming.getInitialNaming();
        this.ports = (Ports)naming.lookup("Ports");
	this.irq = (IRQ)naming.lookup("IRQ");
        this.rm = (MemoryManager)naming.lookup("MemoryManager");
	Debug.out.println("lookup PCI Access Point...");
	PCIAccess bus;
	int counter = 0;
	for(;;) {
	    bus = (PCIAccess)InitNaming.lookup("PCIAccess");
	    if (bus == null) {
		if (counter % 20 == 0) { counter = 0; Debug.out.println("NetInit still waiting for PCI");}
		counter++;
		Thread.yield();
	    } else {
		break;
	    }
	}	
	return findDevice(bus);
    }

    public LanceDriver[] findDevice(PCIAccess pci) {
	int deviceID = -1;
	int vendorID = -1;
	LanceDriver helper;
	/* query for network interface cards of any vendor */
	PCIDevice[] devInfo = pci.getDevicesByClass(PCI.CLASSCODE_CLASS_MASK, PCI.BASECLASS_NETWORK);
	if (devInfo == null || devInfo.length == 0) {
	    Debug.out.println("no network devices of any vendor found! ");
	    return null;
	}
        // search for supported NICs
        for (PCIDevice devInfo1 : devInfo) {
            deviceID = devInfo1.getDeviceID() & 0xffff;
            vendorID = devInfo1.getVendorID() & 0xffff;
            Debug.out.println("Vendor=" + Integer.toHexString(vendorID) + ", Device=" + Integer.toHexString(deviceID));
            if (vendorID ==  NIC_VENDOR_ID) {
                switch (deviceID) {
                    case NIC_PCI_DEVICE_ID_Am79C970:
                        Debug.out.println("10/100 Base-TX NIC found");
                        return new LanceDriver[]{new LanceDriver(devInfo1, new LanceFlags("test"), irq, ports, rm)};
                    default:
                        Debug.out.println("ERROR: Unsupported NIC found");
                }
            }
        }
	return null;
    }
}
