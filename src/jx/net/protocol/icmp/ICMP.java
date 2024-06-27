package jx.net.protocol.icmp;

import java.util.logging.Level;
import java.util.logging.Logger;
import jx.zero.*;

import jx.fs.buffer.separator.MemoryConsumer;
import jx.net.IPConsumer;
import jx.net.IPData;
import jx.net.IPSender;
import jx.net.NetInit;
import jx.net.PacketsProducer;
import jx.net.PacketsConsumer;
import jx.net.UnknownAddressException;
import jx.net.protocol.ip.IP;

public class ICMP implements MemoryConsumer, IPConsumer {
  
    private final PacketsConsumer lowerConsumer;
    private final PacketsProducer lowerProducer;
    private IP ipLayer;
    NetInit net;
    IPSender sender;
    IPData data;
    static final boolean dumpAll = true; // switch on to see all icmp frames

    private static final int TYPE_ECHO = 8;

    public ICMP(PacketsConsumer lowerConsumer, PacketsProducer lowerProducer, NetInit net) {
        this.net = net;
	this.lowerProducer = lowerProducer;
	this.lowerConsumer = lowerConsumer; 
	lowerProducer.registerConsumer(this, "ICMP");
    }

    public boolean register(Object ip) {
	if (ipLayer != null)
	    return false;
	ipLayer = (IP)ip;
        //PacketsConsumer lowerConsumer = ipLayer.getTransmitter(lowerConsumer, dst, TYPE_ECHO);
	return true;
    }
    
    @Override
    public Memory processMemory(Memory buf) {
	Debug.out.println("ICMP packet received: offset=");
        MemoryManager memMgr = (MemoryManager) InitialNaming.getInitialNaming().lookup("MemoryManager");
        //Memory mem = memMgr.alloc(98);
        //mem.copyFromMemory(buf, 0, 0, 98);
	ICMPFormat icmp = new ICMPFormat(buf, 0x22);
	int type = icmp.getType();
	int code = icmp.getCode();
	int checksum = icmp.getChecksum();
	Debug.out.println("ICMP type: " + type + ", code=" + code + ", checksum=" + checksum);
	if (type == TYPE_ECHO) {
	    Debug.out.println("  ICMP ECHO RECEIVED. (ping)");
            //try {
                icmp.insertType((byte)0);
                icmp.CalculateChecksum();
                sender = net.getIPSender(data.getSourceAddress(), 1);
                Debug.out.println(buf.size());
                sender.send(icmp.getMemory());
            //} catch (UnknownAddressException ex) {
                //Logger.getLogger(ICMP.class.getName()).log(Level.SEVERE, null, ex);
            //}
	}
	return buf;
    }

    @Override
    public Memory processIP(IPData data) {
        this.data = data;
        return processMemory(data.getMemory());
    }
}
