package java.net;

import java.io.IOException;

import jx.net.IPAddress;
import jx.net.NetInit;
import jx.net.UDPData;
import jx.net.UDPSender;
import jx.net.UDPReceiver;
import jx.net.protocol.udp.UDPFormat;
import jx.net.UnknownAddressException;
import jx.zero.Debug;
import jx.zero.InitialNaming;
import jx.zero.Memory;

public class DatagramSocket
{ 
    //FileDescriptor fd;
    int port = 0;
    UDPSender sender;
    NetInit net;
    UDPReceiver receiver;
    
    protected void finalize() {
      /*if (fd != null)
	close();*/
    }
  
    public synchronized void close() {
      // fd = null;
    }
  
    public void receive(DatagramPacket p) throws IOException  {
        int len = UDPFormat.requiresSpace();
        Memory buf = net.getUDPBuffer(len + p.length + 34);
        UDPData udp = receiver.receive(buf);
        //UDPFormat b = new UDPFormat(udp.mem);
        buf.copyToByteArray(p.buff, 0, 34, 5);
        for(int i = 0; i < 5; i++){
            Debug.out.println(p.buff[i]);
        }
        /*for(;;) {
          Buffer x = p.getFirst();
          if (x != first) {
            first = x;
            byte[] b = first.getData();
            dumpPacket(b);
          }
        }

        //recv(fd, p.buff, p.addr.addr, other);
        p.length = 0;
        p.port = 0;
        synchronized(this)  { 
           try { wait(); } catch(InterruptedException e) {}
        }*/
    }
  
    public void send(DatagramPacket p) throws IOException {
        try {
            sender = net.getUDPSender(port, new IPAddress(p.addr.getAddress()), p.getPort());
        } catch (UnknownAddressException ex) {
            //Logger.getLogger(DatagramSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        int len = UDPFormat.requiresSpace();
        Memory buf = net.getUDPBuffer(len + p.length + 34);

        buf.copyFromByteArray(p.buff, 0, len + 34, p.length);
	Debug.out.println("SENDING udp REQUEST...");
        buf = sender.send(buf);
    }
  
    public int getLocalPort()   {
        return port;
    }
  
    public DatagramSocket(int port) throws SocketException   {
        this.port = port;
        net = (NetInit) InitialNaming.getInitialNaming().lookup("NIC");
        
	receiver = net.getUDPReceiver(port, new Memory[] { 
            net.getUDPBuffer(0), net.getUDPBuffer(0), net.getUDPBuffer(0), net.getUDPBuffer(0)
        }); 
    }
  
    public DatagramSocket() throws SocketException {
        this(0);
    }
}
