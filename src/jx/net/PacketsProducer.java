package jx.net;

import jx.buffer.separator.MemoryConsumer;


public interface PacketsProducer {
    public boolean registerConsumer(MemoryConsumer consumer, String name);
    // public IPAddress getSource(Memory buf);
}
