package jx.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import jx.zero.Debug;

public class IPAddress {
    public static final IPAddress ANY = new IPAddress("0.0.0.0");
    public static final IPAddress BROADCAST = new IPAddress("255.255.255.255");
    public static InetAddress BROADCAST_ADDRESS = (Inet4Address) BROADCAST.toInetAddress();
    private InetAddress inetAddress;
    int addr;
    private static final int length = 4;
    byte[] bytes;
    private static final IPAddress DEFAULT_ANY_SUBNETMASK = new IPAddress("0.0.0.0");
    private static final IPAddress DEFAULT_CLASS_A_SUBNETMASK = new IPAddress("255.0.0.0");
    private static final IPAddress DEFAULT_CLASS_B_SUBNETMASK = new IPAddress("255.255.0.0");
    private static final IPAddress DEFAULT_CLASS_C_SUBNETMASK = new IPAddress("255.255.255.0");
    private static final IPAddress DEFAULT_CLASS_D_SUBNETMASK = new IPAddress("255.255.255.0");
    
/**
     * Convert to a java.net.InetAddress
     * @see java.net.InetAddress
     * @see java.net.Inet4Address
     * @return This address as java.net.InetAddress
     */
    public InetAddress toInetAddress() {
        if (inetAddress == null) {
            try {
                inetAddress = InetAddress.getByAddress(bytes);
            } catch (UnknownHostException ex) {
                throw new RuntimeException(ex);
            }
        }
        return inetAddress;
    }
    
/**
     * Gets the default subnet mask for this address 
     */
    public IPAddress getDefaultSubnetmask() {
        if (isAny()) {
            return DEFAULT_ANY_SUBNETMASK;
        } else if (isClassA()) {
            return DEFAULT_CLASS_A_SUBNETMASK;
        } else if (isClassB()) {
            return DEFAULT_CLASS_B_SUBNETMASK;
        } else if (isClassC()) {
            return DEFAULT_CLASS_C_SUBNETMASK;
        } else if (isClassD()) {
            return DEFAULT_CLASS_D_SUBNETMASK;
        } else {
            throw new IllegalArgumentException("Unknown address class");
        }
    }
    /**
     * Calculate the and or this address with the given mask.
     * @param mask
     */
    public IPAddress and(IPAddress mask) {
        final byte[] res = new byte[length];
        for (int i = 0; i < length; i++) {
            res[i] = (byte) (bytes[i] & mask.bytes[i]);
        }
        return new IPAddress(res);
    }
    /** 
     * Is this a class A address.
     * Class A = 0.0.0.0 - 127.255.255.255
     */
    public boolean isClassA() {
        final int b0 = bytes[0] & 0xFF;
        return (b0 >= 0) && (b0 <= 127);
    }

    /** 
     * Is this a class B address.
     * Class B = 128.0.0.0 - 191.255.255.255
     */
    public boolean isClassB() {
        final int b0 = bytes[0] & 0xFF;
        return (b0 >= 128) && (b0 <= 191);
    }

    /** 
     * Is this a class C address.
     * Class C = 192.0.0.0 - 223.255.255.255
     */
    public boolean isClassC() {
        final int b0 = bytes[0] & 0xFF;
        return (b0 >= 192) && (b0 <= 223);
    }

    /** 
     * Is this a class D (multicast) address.
     * Class D = 224.0.0.0 - 239.255.255.255
     */
    public boolean isClassD() {
        final int b0 = bytes[0] & 0xFF;
        return (b0 >= 224) && (b0 <= 239);
    }
    /**
     * Is this an Any address.
     * An Any address is equal to "0.0.0.0"
     * @return 
     */
    public boolean isAny() {
        for (int i = 0; i < length; i++) {
            if (bytes[i] != 0) {
                return false;
            }
        }
        return true;
    }
    
    public IPAddress(byte[] b) {
	this(b[0], b[1], b[2], b[3]);
    }
    public IPAddress(int a, int b, int c, int d) {
	this(toIPAddr(a,b,c,d));	
    }
    public IPAddress(String a) {
	this(parseIPAddr(a));
    }
    public IPAddress(int a) {
	addr = a;
	bytes = toBytes();
    }
    public int getAddress() {
	return addr;
    }
    private static int toIPAddr(int a, int b, int c, int d) {
	return ((((((d & 0xff) << 8) | (c & 0xff)) << 8) | (b & 0xff)) << 8) | (a & 0xff);
    }
    private static int parseIPAddr(String addr) {
	byte[] ip = new byte[4];
	int cut0 = 0;
	for(int i = 0; i < 3; i++) {
	    int cut = addr.indexOf(".", cut0);
	    if (cut == -1) {
		return -1;
	    }
	    String num = addr.substring(cut0, cut);
	    if (num == null) {
		Debug.out.println("NO IP ADDRESS at IPAddress");
		return -1;
	    }
	    ip[i] = (byte)Integer.parseInt(num);
	    cut0 = cut+1;
	}
	ip[3] = (byte)Integer.parseInt(addr.substring(cut0, addr.length()));
	return toIPAddr(ip[0], ip[1], ip[2], ip[3]);
    }

    public String getHostName() { return toString();}// TODO
    //public static IPAddress getLocalHost() { return null; } // TODO

    @Override
    public String toString() {
	StringBuilder buf = new StringBuilder();
	int a = addr;
	for(int i=0; i<4; i++) {
	    buf.append(a&0xff);
	    a >>= 8;
	    if (i+1<4) buf.append(".");
	}
	return buf.toString();
    }

    @Override
    public boolean equals(Object o) {
	if (! (o instanceof IPAddress)) return false;
	return ((IPAddress)o).addr == addr;
    }

    public byte[] getBytes() {
	return bytes;
    }

    private byte[] toBytes() {
	byte[] ret = new byte[4];
	ret[3] = (byte)((addr >> 24) & 0xff);
	ret[2] = (byte)((addr >> 16) & 0xff);
	ret[1] = (byte)((addr >> 8)  & 0xff);
	ret[0] = (byte)( addr        & 0xff);
	return ret;
    }
}
