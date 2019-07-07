package jx.zero.memory;

import java.io.InputStream;
import jx.zero.ReadOnlyMemory;

public class MemoryInputStream extends InputStream { 
    private final ReadOnlyMemory buf;
    private final int count;
    private int pos;
    
    @Override
    public int available() {
	return count - pos;
    }

    @Override
    public void reset() {
	pos = 0;
    }

    @Override
    public int read() {
	if (pos >= count)
	    return -1;
	else
	    return ((int)buf.get8(pos++)) & 0xFF;
    }

    @Override
    public int read(byte[] b, int off, int len)	{
	if (pos >= count)
	    return -1;
	int d = count - pos;
	int bytes = d < len ? d : len;
	buf.copyToByteArray(b, off, pos, bytes);
	pos += bytes;
	return bytes;
    }

    @Override
    public long skip(long n) {
	int target = (int) (pos + n);
	if (target > count) {
	    int bytes = count - pos;
	    pos = count;
	    return bytes;
	}
	
	pos = (int) target;
	
	return n;
    }

    public MemoryInputStream(ReadOnlyMemory m) {
	buf = m;
	count = m.size();
    }
}
