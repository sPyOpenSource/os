// Implementation of the Inetaddress class 
// for metaxa OS

// currently only a static HACK

package java.net;


public class InetAddress
{
    final static InetAddress INADDR_ANY = new InetAddress(-1);

    final static byte[] faui48lIP = {(byte)131, (byte)188, 34, (byte)240 };
    final static byte[] faui45IP = {(byte)131, (byte)188, 34, (byte)45 };
    final static byte[] faui40pIP = {(byte)131, (byte)188, 34, (byte)77 };
    final static byte[] laptop = {(byte)192, (byte)168, (byte)90, (byte)218};
    final static byte[] home = {(byte)192, (byte)168, (byte)1, (byte)14};
    final static byte[] localIP = home;
    final static String localName = "localhost";
    static final int IPv4 = 1;
    static class InetAddressHolder {
        /**
         * Reserve the original application specified hostname.
         *
         * The original hostname is useful for domain-based endpoint
         * identification (see RFC 2818 and RFC 6125).  If an address
         * was created with a raw IP address, a reverse name lookup
         * may introduce endpoint identification security issue via
         * DNS forging.
         *
         * Oracle JSSE provider is using this original hostname, via
         * sun.misc.JavaNetAccess, for SSL/TLS endpoint identification.
         *
         * Note: May define a new public method in the future if necessary.
         */
        String originalHostName;

        InetAddressHolder() {}

        InetAddressHolder(String hostName, int address, int family) {
            this.originalHostName = hostName;
            this.hostName = hostName;
            this.address = address;
            this.family = family;
        }

        void init(String hostName, int family) {
            this.originalHostName = hostName;
            this.hostName = hostName;
            if (family != -1) {
                this.family = family;
            }
        }

        String hostName;

        String getHostName() {
            return hostName;
        }

        String getOriginalHostName() {
            return originalHostName;
        }

        /**
         * Holds a 32-bit IPv4 address.
         */
        int address;

        int getAddress() {
            return address;
        }

        /**
         * Specifies the address family type, for instance, '1' for IPv4
         * addresses, and '2' for IPv6 addresses.
         */
        int family;

        int getFamily() {
            return family;
        }
    }

    /* Used to store the serializable fields of InetAddress */
    final transient InetAddressHolder holder;

    InetAddressHolder holder() {
        return holder;
    }
    /**
     * Constructor for the Socket.accept() method.
     * This creates an empty InetAddress, which is filled in by
     * the accept() method.  This InetAddress, however, is not
     * put in the address cache, since it is not created by name.
     */
    InetAddress() {
        holder = new InetAddressHolder();
    }
    
    public static InetAddress[] getAllByName(String host)  throws UnknownHostException  {
        InetAddress[] dst = new InetAddress[1];
        dst[0] = getByName(host);
        return dst;    
    }
  
    public static InetAddress getByAddress(byte[] addr)
        throws UnknownHostException {
        return getByAddress(null, addr);
    }
  
    public static InetAddress getByAddress(String host, byte[] addr)
        throws UnknownHostException {
        if (host != null && host.length() > 0 && host.charAt(0) == '[') {
            if (host.charAt(host.length()-1) == ']') {
                host = host.substring(1, host.length() -1);
            }
        }
        if (addr != null) {
            //if (addr.length == Inet4Address.INADDRSZ) {
                return new Inet4Address(host, addr);
            /*} else if (addr.length == Inet6Address.INADDRSZ) {
                byte[] newAddr
                    = IPAddressUtil.convertFromIPv4MappedAddress(addr);
                if (newAddr != null) {
                    return new Inet4Address(host, newAddr);
                } else {
                    return new Inet6Address(host, addr);
                }
            }*/
        }
        throw new UnknownHostException("addr is of illegal length");
    }
  
    public static InetAddress getLocalHost()  throws UnknownHostException  {
        return new InetAddress(localIP);
    }
  
    public static InetAddress getByName(String host) throws UnknownHostException   {
        if (host == null) throw new UnknownHostException("hostname is null");
        if (host.equals("faui45")) return new InetAddress(faui45IP);
        if (host.equals("faui40p")) return new InetAddress(faui40pIP);
        if (host.equals(localName)) return new InetAddress(localIP);
        if (host.equals("laptop")) return new InetAddress(laptop);
        if (host.equals("home")) return new InetAddress(home);
        throw new UnknownHostException("unknown host " + host); 
    }

    /**
     * Utility routine to check if the InetAddress in a wildcard address.
     * @return a {@code boolean} indicating if the Inetaddress is
     *         a wildcard address.
     * @since 1.4
     */
    public boolean isAnyLocalAddress() {
        return false;
    }
    
    private static int i(byte b)  {
        return b & 0xFF;
    }
  
    private static byte b(int i) {
        return (byte) i;
    }
  
  
    String hostname = null;
    byte[] addr;
  
  @Override
  public boolean equals(Object obj)
    {
      if (!(obj instanceof InetAddress))
	return false;
      
      InetAddress a = (InetAddress) obj;
      
      if (a.addr.length != addr.length)
	return false;
      
      for (int i = 0; i < addr.length; i++)
	if (a.addr[i] != addr[i])
	  return false;
      
      return true;
    }
  
  public byte[] getAddress()  {
    return addr;
  }
  
  public String getHostName()  {
    return hostname;
  }
  
  public int hashCode()
    {
      byte[] v = new byte[4];
      
      for (int i = 0; i < addr.length; i++)
	v[i & 3] += addr[i];
      
      return	(i(v[0]) <<  0) +
	(i(v[1]) <<  8) +
	(i(v[2]) << 16) +
	(i(v[3]) << 24);
    }
  
  public String toString()
	{
		return	"" + i(addr[0]) +
			"." + i(addr[1]) +
			"." + i(addr[2]) +
			"." + i(addr[3]);
	}
  
  InetAddress(byte[] addr)
    {
        this();
      this.addr = addr;
      
    }
  
  InetAddress(int addr)
    {
        this();
      byte[] b = new byte[4];
      b[0] = b(addr);	addr >>>= 8;
      b[1] = b(addr);	addr >>>= 8;
      b[2] = b(addr);	addr >>>= 8;
      b[3] = b(addr);
      
      this.addr = b;
    }
}

