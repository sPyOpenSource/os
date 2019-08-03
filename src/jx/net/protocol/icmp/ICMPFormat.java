package jx.net.protocol.icmp;

import jx.zero.*;
import jx.net.format.Format;
import jx.net.protocol.ip.IPv4Utils;

/**
  * ICMP header
  * RFC 792
  */

/*

    0  Echo Reply

    3  Destination Unreachable

    4  Source Quench

    5  Redirect

    8  Echo

   11  Time Exceeded

   12  Parameter Problem

   13  Timestamp

   14  Timestamp Reply

   15  Information Request

   16  Information Reply

*/

public class ICMPFormat extends Format {
    public ICMPFormat(Memory buf, int offset) { 
        super(buf, offset);
    }

    public void insertType(byte type) {  
        writeByte(0, type);
    }

    public  int getType() {
        return readByte(0);
    }

    public  int getCode() {
        return readByte(1);
    }
    
    public  int getChecksum() {
        return readShort(2);
    }
    
    public void CalculateChecksum(){
        writeShort(2, (short)0);
        final short ccs = IPv4Utils.calcChecksum(buf, 0x22, buf.size() - 0x22);
        writeShort(2, (short)(((ccs >> 8) & 0xff) | ((ccs << 8) & 0xff00)));
    }

    @Override
    public int length() { 
        return 16; 
    }
}
