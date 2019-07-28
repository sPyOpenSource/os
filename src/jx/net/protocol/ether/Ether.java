package jx.net.protocol.ether;

import jx.devices.net.NetworkDevice;
import jx.zero.Debug;
import jx.zero.debug.Dump;
import jx.zero.*;
import jx.buffer.separator.*;
import jx.net.dispatch.Dispatch;
import jx.net.PacketsProducer;
import jx.net.PacketsConsumer;
import jx.net.PacketsConsumer1;
import jx.net.EtherConsumer1;
import jx.net.EtherProducer1;
import jx.net.EtherData;
import jx.buffer.multithread.MultiThreadList;

public class Ether  implements PacketsProducer, EtherProducer1 {
    static final boolean dumpAll = true; // switch on to see all ether frames
    static final boolean dumpSend = false;
    static final boolean debug = false;
    private final static boolean debugPacketNotice = false;
    private final static boolean dumpSpecial = false;
    
    public final static int ETHERNET_MAXIMUM_FRAME_SIZE = 1514;

    private final NetworkDevice dev;

    private final Dispatch dispatch;

    private final byte[] ownHardwareAddress;

    public static final int ADDR_SIZE = 6;
    public final byte[] ETHER_BCAST  =   {(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff};

    // code duplication with jx.netmanager.UDPReceiver

    public static final int PROTO_IP   = 0x0800;
    public static final int PROTO_ARP  = 0x0806;
    public static final int PROTO_RARP = 0x8035;

    private final CPUManager cpuManager = (CPUManager)InitialNaming.getInitialNaming().lookup("CPUManager");

    private EtherConsumer1 myIPConsumer;
    private EtherConsumer1 myARPConsumer;
    private EtherConsumer1 myRARPConsumer;
    private boolean avoidSplitting = true;

    {
	dispatch = new Dispatch(3);
	dispatch.add(0x0800, "IP");
	dispatch.add(0x0806, "ARP");
	dispatch.add(0x8035, "RARP");
    }

    private static final boolean discardOld = true;

    public Ether(NetworkDevice dev, byte[] ownEtherAddress) {
	this.dev = dev;
	ownHardwareAddress = ownEtherAddress;
    }

    public int getMTU() {
	return dev.getMTU();
    }

    
    public byte[] getMacAddress() {
	return ownHardwareAddress;
    }

    /**
     * Install a  consumer that receives packets from the network
     * This method is called by higher layers.
     * @param consumer
     * @param name
     * @return 
     */
    @Override
    public boolean registerConsumer1(EtherConsumer1 consumer, String name) {
        switch (name) {
            case "IP":
                myIPConsumer = consumer;
                Debug.out.println("Ether: Registered IP consumer");
                break;
            case "ARP":
                myARPConsumer = consumer;
                break;
            case "RARP":
                myRARPConsumer = consumer;
                break;
            default:
                throw new Error("Unknown protocol " + name);
        }
	avoidSplitting = true;
	return true;
    }
    
    /**
     * Install a  consumer that receives packets from the network
     * This method is called by higher layers.
     * @param consumer
     * @param name
     * @return 
     */
    @Override
    public boolean registerConsumer(MemoryConsumer consumer, String name) {
	return dispatch.registerConsumer(consumer, name);
    }

    public PacketsConsumer1 getTransmitter1(final byte[] dest, String name) {
	final int id = dispatch.findID(name);
	return new PacketsConsumer1() {
                @Override
		public Memory processMemory(Memory userbuf, int offset, int size) {
		    if (debugPacketNotice) Debug.out.println("Ether.transmit: " + size);
		    Memory buf = userbuf;
		    offset -= EtherFormat.requiresSpace();
		    EtherFormat e = new EtherFormat(buf, offset);
		    e.insertDestAddress(dest);
		    e.insertSourceAddress(ownHardwareAddress);
		    e.insertType(id);
		    if (dumpAll) {
			e.dump();
		    }
		    Memory ret = dev.transmit1(buf, offset, EtherFormat.requiresSpace() + size);		    
		    //ret = ret.revoke();		    
		    return ret;
		}
                @Override
		public int requiresSpace() {return EtherFormat.requiresSpace();}
                @Override
		public int getMTU() {return 1000; /*TODO*/}
	    };
    }

    /**
     * Get a consumer that transmits packets to the network
     * This method is called by higher layers.
     * @param dest
     * @param name
     * @return 
     */
    public PacketsConsumer getTransmitter(final byte[] dest, String name) {
	final int id = dispatch.findID(name);
	return new PacketsConsumer() {
                @Override
		public Memory processMemory(Memory userbuf) {
		    if (debugPacketNotice) Debug.out.println("Ether.transmit: " + userbuf.size());
		    Memory buf = userbuf.joinPrevious(); // no joinAll() to preserve packet size 
		    EtherFormat e = new EtherFormat(buf);
		    e.insertDestAddress(dest);
		    e.insertSourceAddress(ownHardwareAddress);
		    e.insertType(id);
		    Memory ret = dev.transmit(buf);		    
		    //ret = ret.revoke();		    
		    int space = e.length();
		    Memory[] arr = new Memory[2];
		    ret = ret.joinAll();
		    if (ret.size() != 1514) throw new Error("Packet is too small: " + ret.size());
		    ret.split2(space, arr);
		    if (arr[1] == null) Debug.throwError();
		    return arr[1];
		}
                @Override
		public int requiresSpace() {return EtherFormat.requiresSpace();}
                @Override
		public int getMTU() {return 1000; /*TODO*/}

	    };
    }

    /** get MemoryNonBlockingConsumer that receives packets from the NIC device driver layer
     * @param bufs
     * @return  */
    public NonBlockingMemoryConsumer getNonBlockingReceiver(Memory[] bufs) {
	final MultiThreadList usableBufs = new MultiThreadList();
	usableBufs.setListName("usableBufs");
        for (Memory buf : bufs) {
            usableBufs.appendElement(new PacketContainer(buf));
        }
//	usableBufs.enableRecording("Ether-available-queue");
	final MultiThreadList filledBufs = new MultiThreadList();
	filledBufs.setListName("filledBufs");
//	filledBufs.enableRecording("Ether-receive-queue");
	if (bufs == null) throw new Error("non-buffer ether processing disabled");

	/*	
	DebugSupport debugSupport = (DebugSupport) InitialNaming.getInitialNaming().lookup("DebugSupport");
	debugSupport.registerMonitorCommand("dumpetherqueue", new MonitorCommand() {
		public void execCommand(String[] args) {
		    Debug.out.println("***********************************************");
		    Debug.out.println("*             ETHERQUEUE                      *");
		    Debug.out.println("***********************************************");
		    filledBufs.dump();
		}
		public String getHelp() {
		    return "";
		}
	    });
	*/

	class EtherQueueConsumerThread extends Thread { //  thread to process received packets
	    EtherQueueConsumerThread() {
		super("Etherpacket-Queue");
	    }
            @Override
	    public void run() {
		int event_rcv = cpuManager.createNewEvent("EtherRcv");
		for(;;) {
		    cpuManager.recordEvent(event_rcv);
                    PacketContainer c = (PacketContainer) filledBufs.undockFirstElement();
                    Memory newMem = c.getMem();
		    if (!avoidSplitting) {
			if (debugPacketNotice) Debug.out.println("Ether.receive: " + newMem.size());
			EtherFormat e = new EtherFormat(newMem);
			if (dumpAll) {
			    Debug.out.println("Ether packet received.");
			    e.dump();
			}
			int id = e.getType();
			Memory[] arr = new Memory[3];
			newMem.split3(EtherFormat.requiresSpace(), newMem.size()-EtherFormat.requiresSpace(), arr);
			Memory data = arr[1];
			newMem = dispatch.dispatch(id, data);
			//Debug.out.println("Unknown packet type. Ignored.");
			    
			c.setMem(newMem.revoke());
		    } else {
			if (debugPacketNotice) Debug.out.println("Ether.receive: " + c.getSize());
			EtherFormat e = new EtherFormat(newMem, c.getOffset());
			if (dumpAll) {
			    Debug.out.println("Ether packet received.");
			    e.dump();
			}
			int id = e.getType();
			EtherData data = new EtherData();
			data.srcAddress = e.getSourceAddress();
			data.dstAddress = e.getDestAddress();
			data.mem = newMem;
			data.offset = c.getOffset() + EtherFormat.requiresSpace();
			data.size = c.getSize() - EtherFormat.requiresSpace();
			switch(id) {
                            case PROTO_IP:
                                if (myIPConsumer == null) {
                                    Debug.out.println("NO IP consumer");
                                    break;
                                }
                                if (dumpAll) 
                                    Debug.out.println("Ether: received IP packet");
                                newMem = myIPConsumer.processEther1(data);
                                break;
                            case PROTO_ARP:
                                if (myARPConsumer == null) {
                                    Debug.out.println("NO ARP consumer");
                                    break;
                                }
                                if (dumpAll) {
                                    Debug.out.println("Ether: received ARP packet");
                                    Dump.xdump(data.mem, data.offset, 16);
                                }
                                newMem = myARPConsumer.processEther1(data);
                                break;
                            case PROTO_RARP:
                                if (myRARPConsumer == null) {
                                    Debug.out.println("NO RARP consumer");
                                    break;
                                }
                                if (dumpAll)
                                    Debug.out.println("Ether: received RARP packet");
                                newMem = myRARPConsumer.processEther1(data);
                                break;
                            default: 
                                Debug.out.println("Unknown packet. ID=" + id);
			} 
			if (debug)
			    if (newMem == null)
				throw new Error("newMem == null");
			c.setMem(newMem);
			usableBufs.appendElement(c);
		    }
		}
	    }
	}
	new EtherQueueConsumerThread().start();

	
	class EtherNonBlockingMemoryConsumerImpl implements EtherNonBlockingMemoryConsumer, Service  {
            @Override
	    public Memory processMemory(Memory mem, int offs, int size) {
		if (dumpAll) Debug.out.println("Ether packet received");
		PacketContainer c = (PacketContainer)usableBufs.nonblockingUndockFirstElement();
		
		if (c == null) {
		    if (discardOld) {
			Debug.out.println("jx.net.Ether: no buffer available, must drop a packet!");
			c = (PacketContainer) filledBufs.nonblockingUndockFirstElement();
			if (c == null) throw new Error("where are my buffers gone?");
		    } else {
			return mem; // discard recent packet
		    }
		}
		Memory result = c.getMem();
		if (avoidSplitting)
		    c.setData(mem,offs,size);
		else
		    c.setData(mem.revoke(),offs,size);
		filledBufs.appendElement(c);
		return result;
	    }
	}
	return new EtherNonBlockingMemoryConsumerImpl();
    }
    

    public byte[] getBroadcastAddr() {
	return ETHER_BCAST;
    }    

    public Memory transmitARPBroadcast(Memory userbuf/*, int count*/) {
	Memory buf = userbuf.joinPrevious();
	//Debug.out.println("jx.net.Ether: ARP broadcast");
	return transmitSpecial(ownHardwareAddress, ETHER_BCAST, dispatch.findID("ARP"), buf);

    }

    public Memory transmitARPBroadcast1(Memory userbuf) {
	return transmitSpecial(ownHardwareAddress, ETHER_BCAST, dispatch.findID("ARP"), userbuf);
    }

    // buf already has full range
    public Memory transmitSpecial(byte src[], byte dest[], int type, Memory buf) {

	EtherFormat e = new EtherFormat(buf);
	e.insertDestAddress(dest);
	e.insertSourceAddress(src);
	e.insertType(type);
	if (dumpSpecial) {
	    Debug.out.println("jx.net.Ether: special");
	    Dump.xdump1(buf, 0, 128);
	}
	return dev.transmit(buf);
    }
    
    class PacketContainer {
	private Memory mem;
	private int offs = 0, size = 0;
	
	public PacketContainer(Memory mem) { setMem(mem);}
	public int getOffset() {
	    if(!avoidSplitting) 
		throw new Error("when splitting the Memory, an extra offset info is not needed");
	    return offs; }
	public int getSize()   {
 	    if(!avoidSplitting)
		throw new Error("when splitting the Memory, an extra size info is not needed");
	    return size; }
	public Memory getMem() { return mem; }
	public void setMem(Memory mem) { this.mem = mem; }
	public void setData(Memory mem, int offs, int size){
	    setMem(mem);
	    this.offs = offs;
	    this.size = size;
	}
    }
}


interface EtherNonBlockingMemoryConsumer extends NonBlockingMemoryConsumer, Portal {}
