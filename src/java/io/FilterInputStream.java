
// (C) 1997 Glynn Clements <glynn@sensei.co.uk> - Freely Redistributable

package java.io;

public class FilterInputStream extends InputStream
{ 
	protected InputStream in;

        @Override
	public int available() throws IOException
	{
		return in.available();
	}

        @Override
	public void close() throws IOException
	{
		in.close();
	}

        @Override
	public void mark(int readlimit)
	{
		in.mark(readlimit);
	}

        @Override
	public boolean markSupported()
	{
		return in.markSupported();
	}

        @Override
	public int read() throws IOException
	{
		return in.read();
	}

        @Override
	public int read(byte[] b) throws IOException
	{
		return read(b, 0, b.length);
	}

        @Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		return in.read(b, off, len);
	}

        @Override
	public void reset() throws IOException
	{
		in.reset();
	}

        @Override
	public long skip(long n) throws IOException
	{
		return in.skip(n);
	}

	protected FilterInputStream(InputStream in)
	{
		this.in = in;
	}
}

