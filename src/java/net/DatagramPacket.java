package java.net;

public final class DatagramPacket
{ 
    byte[] buff;
    int length;
    InetAddress addr;
    int port;
  
    public InetAddress getAddress()
    {
        return addr;
    }
    
    public void setAddress(InetAddress addr){
        this.addr = addr;
    }
  
    public byte[] getData()
    {
        return buff;
    }
  
    public int getLength()
    {
        return length;
    }
  
    public int getPort()
    {
        return port;
    }
    
    public void setPort(int port){
        this.port = port;
    }
  
    public DatagramPacket(byte ibuf[], int ilength)
    {
        buff = ibuf;
        length = ilength;
    }
  
    public DatagramPacket(byte ibuf[], int ilength, InetAddress iaddr, int iport)
    {
        this(ibuf, ilength);
        addr = iaddr;
        port = iport;
    }
}
