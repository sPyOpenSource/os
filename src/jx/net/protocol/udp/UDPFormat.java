package jx.net.protocol.udp;

import jx.zero.*;
import jx.zero.debug.*;

import jx.net.format.Format;

/**
 * Format of the UDP header:
 *
 *   unsigned short src;
 *   unsigned short dest;
 *   unsigned short len;
 *   unsigned short chksum;
 */

public class UDPFormat extends Format {
    static final boolean dumpAll = false; // switch on to see all udp frames
    public UDPFormat(Memory buf) { this(buf, 34);}
    public UDPFormat(Memory buf, int offset) { 
	super(buf, offset);
	if (dumpAll)Debug.out.println("UDP-Packet:");
	if (dumpAll)Dump.xdump1(buf, offset, 32);
    }
    
    /* write */
    public void insertSourcePort(int port)     {  writeShort(0, (short)port);  }
    public void insertDestPort(int port)     {  writeShort(2, (short)port);  }
    public void insertLength(int len){  
	if (dumpAll) Debug.out.println("INSERTLENGTH MIT " + len);
	writeShort(4, (short)len);
    }
    public void insertChecksum(int checksum)     {  writeShort(6, (short)checksum);  }
    
    /* read */
    int getSourcePort()     {  return readUnsignedShort(0);  }
    int getDestPort()     {  return readUnsignedShort(2);  }
    public int getLength()     {  return readUnsignedShort(4);  }
    int getChecksum()     {  return readUnsignedShort(6);  }
    final public int length() { return 8; }
    public static int requiresSpace() { return 8; }
    public static final int SIZE = 8;


    public void dump() {
	Debug.out.println("UDP-Packet:");
	Debug.out.println("   SourcePort: " + getSourcePort());
	Debug.out.println("   DestPort: " + getDestPort());
	Debug.out.println("   Length: " + getLength());
	Debug.out.println("   Checksum:" + getChecksum());
	Debug.out.println("   First 16 bytes of data:");
	Dump.xdump(buf, offset + requiresSpace(), 16);
    }
}
