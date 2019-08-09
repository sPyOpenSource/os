package jx.buffer.multithread;

/**
 * The operations that are available to the producer of buffers.
 */
public interface BufferProducer {
    public void appendElement(Buffer bh);
}
