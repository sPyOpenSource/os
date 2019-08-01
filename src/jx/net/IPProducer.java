package jx.net;

public interface IPProducer {
    public boolean registerConsumer(IPConsumer consumer, String name);
}
