/* ByteArrayInputStream.java -- Read an array as a stream
   Copyright (C) 1998 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.
 
GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


package java.io;

/**
  * This class permits an array of bytes to be read as an input stream.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class ByteArrayInputStream extends InputStream
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The array that contains the data supplied during read operations
  */
protected byte[] buf;

/**
  * The array index of the next byte to be read from the buffer <code>buf</code>
  */
protected int pos;

/**
  * The currently marked position in the stream.  This defaults to 0, so a
  * reset operation on the stream resets it to read from array index 0 in
  * the buffer - even if the stream was initially created with an offset
  * greater than 0
  */
protected int mark;

/**
  * This indicates the maximum number of bytes that can be read from this
  * stream.  It is the array index of the position after the last valid
  * byte in the buffer <code>buf</code>
  */
protected int count;
  
/*************************************************************************/

/**
  * Create a new ByteArrayInputStream that will read bytes from the passed
  * in byte array.  This stream will read from the beginning to the end
  * of the array.  It is identical to calling an overloaded constructor
  * as <code>ByteArrayInputStream(buf, 0, buf.length)</code>.
  * <p>
  * Note that this array is not copied.  If its contents are changed 
  * while this stream is being read, those changes will be reflected in the
  * bytes supplied to the reader.  Please use caution in changing the 
  * contents of the buffer while this stream is open.
  *
  * @param buf The byte array buffer this stream will read from.
  */
public
ByteArrayInputStream(byte[] buf)
{
  this(buf, 0, buf.length);
}

/*************************************************************************/

/**
  * Create a new ByteArrayInputStream that will read bytes from the passed
  * in byte array.  This stream will read from position <code>offset</code> in
  * the array for a length of <code>length</code> bytes past <code>offset</code>.  If the
  * stream is reset to a position before <code>offset</code> then more than
  * <code>length</code> bytes can be read from the stream.  The <code>length</code> value
  * should be viewed as the array index one greater than the last position
  * in the buffer to read.
  * <p>
  * Note that this array is not copied.  If its contents are changed 
  * while this stream is being read, those changes will be reflected in the
  * bytes supplied to the reader.  Please use caution in changing the 
  * contents of the buffer while this stream is open.
  *
  * @param buf The byte array buffer this stream will read from.
  * @param offset The index into the buffer to start reading bytes from
  * @param length The number of bytes to read from the buffer
  */
public
ByteArrayInputStream(byte[] buf, int offset, int length)
{
  this.buf = buf;
  this.pos = offset;
  this.count = length;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method closes this stream and releases any resources associated
  * with it.
  *
  * @exception IOException If an error occurs.
  */
public void
close() throws IOException
{
  super.close();
}

/*************************************************************************/

/**
  * This method returns the number of bytes available to be read from this
  * stream.  The value returned will be equal to <code>count - pos</code>.
  *
  * @return The number of bytes that can be read from this stream before blocking, which is all of them
  */
public int
available()
{
  return(count - pos);
}

/*************************************************************************/

/**
  * This method overrides the <code>markSupported</code> method in <code>InputStream</code>
  * in order to return <code>true</code> - indicating that this stream class
  * supports mark/reset functionality.
  *
  * @return <code>true</code> to indicate that this class supports mark/reset.
  */
public boolean
markSupported()
{
  return(true);
}

/*************************************************************************/

/**
  * This method sets the mark position in this stream to the current
  * position.  Note that the <code>readlimit</code> parameter in this method does
  * nothing as this stream is always capable of remembering all the bytes
  * int it.
  * <p>
  * Note that in this class the mark position is set by default to
  * position 0 in the stream.  This is in constrast to some other stream types
  * where there is no default mark position.
  *
  * @param readlimit The number of bytes this stream must remember.  This parameter is ignored.
  */
public void
mark(int readlimit)
{
  mark = pos;
}

/*************************************************************************/

/**
  * This method sets the read position in the stream to the mark point by
  * setting the <code>pos</code> variable equal to the <code>mark</code> variable.
  * Since a mark can be set anywhere in the array, the mark/reset methods
  * int this class can be used to provide random search capabilities for
  * this type of stream.
  */
public void
reset()
{
  pos = mark;
} 

/*************************************************************************/

/**
  * This method attempts to skip the requested number of bytes in the
  * input stream.  It does this by advancing the <code>pos</code> value by the
  * specified number of bytes.  It this would exceed the length of the
  * buffer, then only enough bytes are skipped to position the stream at
  * the end of the buffer.  The actual number of bytes skipped is returned.
  *
  * @param num_bytes The requested number of bytes to skip
  *
  * @return The actual number of bytes skipped.
  */
public synchronized long
skip(long num_bytes)
{
  if (num_bytes > (count - pos))
    {
      int retval = count - pos;
      pos = count;
      return(retval);
    }

  pos += num_bytes;
  return(num_bytes); 
}

/*************************************************************************/

/**
  * This method reads one byte from the stream.  The <code>pos</code> counter is
  * advanced to the next byte to be read.  The byte read is returned as
  * an int in the range of 0-255.  If the stream position is already at the
  * end of the buffer, no byte is read and a -1 is returned in order to
  * indicate the end of the stream.
  *
  * @return The byte read, or -1 if end of stream
  */
public synchronized int
read()
{
  if (pos >= count)
    return(-1);

  ++pos;

  return((buf[pos - 1] & 0xFF));
}  

/*************************************************************************/

/**
  * This method reads bytes from the stream and stores them into a caller
  * supplied buffer.  It starts storing the data at index <code>offset</code> into
  * the buffer and attempts to read <code>len</code> bytes.  This method can
  * return before reading the number of bytes requested if the end of the
  * stream is encountered first.  The actual number of bytes read is 
  * returned.  If no bytes can be read because the stream is already at 
  * the end of stream position, a -1 is returned.
  * <p>
  * This method does not block.
  *
  * @param buf The array into which the bytes read should be stored.
  * @param offset The offset into the array to start storing bytes
  * @param len The requested number of bytes to read
  *
  * @return The actual number of bytes read, or -1 if end of stream.
  */
public synchronized int
read(byte[] buf, int offset, int len)
{
  if (len == 0)
    return(0);

  if (pos == count)
    return(-1);

  // All requested bytes can be read
  if (len < (count - pos))
    {
      System.arraycopy(this.buf, pos, buf, offset, len);
      pos += len;
      return(len);
    }
  // Cannot read all requested bytes because there aren't enough left in buf
  else
    {
      System.arraycopy(this.buf, pos, buf, offset, count - pos);

      int retval = count - pos;
      pos = count;
      return(retval);
    }
}

} // class ByteArrayInputStream

