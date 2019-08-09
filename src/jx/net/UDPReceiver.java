package jx.net;

import jx.zero.Memory;

public interface UDPReceiver extends jx.zero.Portal {
    UDPData receive(Memory buf, int timeoutMillis);
    UDPData receive(Memory buf);
    void close();
}
