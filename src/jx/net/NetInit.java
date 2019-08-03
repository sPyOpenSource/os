package jx.net;

import jx.zero.Memory;

public interface NetInit extends jx.zero.Portal {
    TCPSocket getTCPSocket(int port, IPAddress ip, Memory[] bufs);
    public Memory getTCPBuffer();

    UDPReceiver getUDPReceiver(int port, Memory[] bufs);
    UDPSender getUDPSender(int localPort, IPAddress dst, int remotePort) throws jx.net.UnknownAddressException;
    public Memory getUDPBuffer(int size);
    public Memory getUDPBuffer();

    IPReceiver getIPReceiver(Memory[] bufs);
    IPReceiver getIPReceiver(Memory[] bufs, String proto);
    //IPSender getIPSender(IPAddress dst) throws jx.net.UnknownAddressException;
    IPSender getIPSender(IPAddress dst, int id) throws jx.net.UnknownAddressException;
    public Memory getIPBuffer(int size);
    public Memory getIPBuffer();

    public IPAddress getLocalAddress();

}


