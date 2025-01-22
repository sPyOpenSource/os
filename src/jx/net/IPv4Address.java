package jx.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import jx.zero.Debug;

public class IPv4Address implements IPAddress {
    public int addr;
    public byte[] bytes = new byte[4];
    public static final IPAddress ANY = new IPv4Address("0.0.0.0");
    public static final IPv4Address BROADCAST = new IPv4Address("255.255.255.255");
    public static InetAddress BROADCAST_ADDRESS = (Inet4Address) BROADCAST.toInetAddress();
    private InetAddress inetAddress;
    private static final int length = 4;
    private static final IPAddress DEFAULT_ANY_SUBNETMASK = new IPv4Address("0.0.0.0");
    private static final IPAddress DEFAULT_CLASS_A_SUBNETMASK = new IPv4Address("255.0.0.0");
    private static final IPAddress DEFAULT_CLASS_B_SUBNETMASK = new IPv4Address("255.255.0.0");
    private static final IPAddress DEFAULT_CLASS_C_SUBNETMASK = new IPv4Address("255.255.255.0");
    private static final IPAddress DEFAULT_CLASS_D_SUBNETMASK = new IPv4Address("255.255.255.0");
    
    /**
     * Convert to a java.net.InetAddress
     * @see java.net.InetAddress
     * @see java.net.Inet4Address
     * @return This address as java.net.InetAddress
     */
    @Override
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
    @Override
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
            res[i] = (byte) (bytes[i] & mask.getBytes()[i]);
        }
        return new IPv4Address(res);
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
    
    public IPv4Address(byte[] b) {
	this(b[0], b[1], b[2], b[3]);
    }
    public IPv4Address(int a, int b, int c, int d) {
	this(toIPAddr(a, b, c, d));	
    }
    public IPv4Address(String a) {
	this(parseIPAddr(a));
    }
    public IPv4Address(int a) {
        addr = a;
	toBytes(a);
    }
    @Override
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

    @Override
    public String getHostName() { return toString(); }// TODO
    //public static IPAddress getLocalHost() { return null; } // TODO

    @Override
    public String toString() {
	StringBuilder buf = new StringBuilder();
        int a = addr;
	for(int i = 0; i < 4; i++) {
	    buf.append(a & 0xff);
	    a >>= 8;
	    if (i + 1 < 4) buf.append(".");
	}
	return buf.toString();
    }

    @Override
    public boolean equals(Object o) {
	if (! (o instanceof IPAddress)) return false;
	return ((IPAddress)o).getAddress() == addr;
    }

    @Override
    public byte[] getBytes() {
	return bytes;
    }

    private void toBytes(int addr) {
	bytes[3] = (byte)((addr >> 24) & 0xff);
	bytes[2] = (byte)((addr >> 16) & 0xff);
	bytes[1] = (byte)((addr >> 8)  & 0xff);
	bytes[0] = (byte)( addr        & 0xff);
    }

    @Override
    public boolean isAnyLocalAddress() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
