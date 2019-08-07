package jx.net.protocol.bootp;

import java.net.Inet4Address;
import java.util.logging.Level;
import java.util.logging.Logger;
import jx.zero.*;
import jx.zero.debug.*;

import jx.net.format.Format;
import jx.net.IPAddress;
import metaxa.os.devices.net.EthernetAdress;
import metaxa.os.devices.net.WrongEthernetAdressFormat;

/**
  * Write BOOTP header data according to the BOOTP format in the buffer.
  * RFC951 basic protocol
  * RFC1048 vendor information
  *
  */
public class BOOTPFormat extends Format {
    /**
     * Size of the BOOTP header (236 bytes)
     */
    public static final int SIZE = 236;
  public static final byte REQUEST = 1;
  public static final byte REPLY = 2;
  public BOOTPFormat(Memory buf, int offset) { 
    super(buf, offset);
  }
  public BOOTPFormat(Memory buf) { 
    super(buf, 0);
  }
  
  public void insertOp(byte op) {  
    writeByte(0, op);
  }
  public void insertHtype(byte htype){
    writeByte(1, htype);  
  }
  public void insertHlen(byte hlen){
    writeByte(2, hlen);  
  }
  public void insertHops(byte hops){
    writeByte(3, hops);  
  }
  public void insertXid(int xid){
    writeInt(4, xid);  
  }
  public void insertSecs(short secs){
    writeShort(8, secs);  
  }
  // 2 bytes unused
  public void insertCiaddr(int ciaddr){
    writeInt(12, ciaddr);  
  }
  public void insertYiaddr(int yiaddr){
    writeInt(16, yiaddr);  
  }
  public void insertSiaddr(int siaddr){
    writeInt(20, siaddr);  
  }
  public void insertGiaddr(int giaddr){
    writeInt(24, giaddr);  
  }
  public void insertHwaddr(byte[] hwaddr /* 16 bytes */){
    writeBytes(28, hwaddr);
  }
  public void insertSname(byte[] sname /* 64 bytes */){
    writeBytes(44, sname);
  }
  public void insertFile(byte[] file /* 128 bytes */){
    writeBytes(108, file);
  }
  public void insertVend(byte[] vend /* 64 bytes */){
    writeBytes(236, vend);
  }

/**
     * Gets the opcode
     * @return 
     */
    public int getOpcode() {
        return readByte(0);
    }
    
    /**
     * Gets the transaction ID
     * @return 
     */
    public int getTransactionID() {
        return readInt(4);
    }
    
  public IPAddress getYiaddr() {
      return readAddress(16);
  }
  
  public IPAddress getClientIPAddress(){
      return readAddress(12);
  }
  
  /**
     * Gets the server IP address
     * @return 
     */
    public IPAddress getServerIPAddress() {
        return readAddress(20);
    }
    
    public IPAddress getGatewayIPAddress() {
        return readAddress(24);
    }
    
/**
     * Gets the client hardware address
     * @return 
     */
    public EthernetAdress getClientHwAddress() {
        final byte[] addr = new byte[6];
        for (int i = 0; i < addr.length; i++) {
            addr[i] = (byte) readByte(28 + i);
        }
        try {
            return new EthernetAdress(addr);
        } catch (WrongEthernetAdressFormat ex) {
            //Logger.getLogger(BOOTPFormat.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
  public int length() { 
    return requiresSpace(); 
  }
    
    public void dump() {
	Debug.out.println("BOOTP-Packet:");
	Dump.xdump1(buf, offset, 48);
    }

  public static int requiresSpace() { 
      return 300;
  }
}
