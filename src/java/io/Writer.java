/* Writer.java -- Base class for character output streams
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
  * This abstract class forms the base of the hierarchy of classes that 
  * write output as a stream of chars.  It provides a common set of methods
  * for writing chars to stream.  Subclasses implement and/or extend these
  * methods to write chars in a particular manner or to a particular 
  * destination such as a file on disk or network connection.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class Writer
{

/*************************************************************************/

/**
  * This is the object used to synchronize criticial code sections for
  * thread safety.  Subclasses should use this field instead of using
  * synchronized methods or explicity synchronizations on <code>this</code>
  */
protected Object lock;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This is the default no-argument constructor for this class.  This method
  * will set up the class to synchronize criticial sections on itself.
  */
protected
Writer()
{
  lock = this;
}

/*************************************************************************/

/**
  * This method initializes a <code>Writer</code> that will synchronize
  * on the specified <code>Object</code>.
  *
  * @param obj The <code>Object</code> to use for synchronizing critical sections
  */
protected
Writer(Object lock)
{
  this.lock = lock;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method forces any data that may have been buffered to be written
  * to the underlying output device.  Please note that the host environment
  * might perform its own buffering unbeknowst to Java.  In that case, a
  * write made (for example, to a disk drive) might be cached in OS
  * buffers instead of actually being written to disk.
  *
  * @exception IOException If an error occurs
  */
public abstract void
flush() throws IOException;

/*************************************************************************/

/**
  * This method closes the stream.  Any internal or native resources associated
  * with this stream are freed.  Any subsequent attempt to access the stream
  * might throw an exception.
  * <p>
  * This method in this class does nothing.
  *
  * @exception IOException If an error occurs
  */
public abstract void
close() throws IOException;

/*************************************************************************/

/**
  * This method writes a single char to the output stream. 
  *
  * @param b The char to be written to the output stream, passed as an int
  *
  * @exception IOException If an error occurs
  */
public void
write(int b) throws IOException
{
  char[] buf = new char[1];

  buf[0] = (char)b;
  write(buf, 0, buf.length);
}

/*************************************************************************/

/**
  * This method all the writes char from the passed array to the output stream.
  * This method is equivalent to <code>write(buf, 0, buf.length)</code> which
  * is exactly how it is implemented in this class.
  *
  * @param buf The array of char to write
  *
  * @exception IOException If an error occurs
  */
public void
write(char[] buf) throws IOException
{
  write(buf, 0, buf.length);
}

/*************************************************************************/

/**
  * This method writes <code>len</code> char from the specified array
  * <code>buf</code> starting at index <code>offset</code> into the array.
  * <p>
  * Subclasses must provide an implementation of this abstract method.
  *
  * @param buf The array of char to write from
  * @param offset The index into the array to start writing from
  * @param len The number of char to write
  * 
  * @exception IOException If an error occurs
  */
public abstract void
write(char[] buf, int offset, int len) throws IOException;

/*************************************************************************/

/**
  * This method writes all the characters in a <code>String</code> to the
  * output.
  *
  * @param str The <code>String</code> whose chars are to be written.
  *
  * @param IOException If an error occurs
  */
public void
write(String str) throws IOException
{
  write(str, 0, str.length());
} 

/*************************************************************************/

/**
  * This method writes <code>len</code> chars from the <code>String</code>
  * starting at position <code>offset</code>.
  *
  * @param str The <code>String</code> that is to be written
  * @param offset The character offset into the <code>String</code> to start writing from
  * @param len The number of chars to write
  *
  * @exception IOException If an error occurs
  */
public void
write(String str, int offset, int len) throws IOException
{
  char[] buf = new char[len];

  str.getChars(offset, offset + len, buf, 0);
  write(buf, 0, buf.length);
}

} // class Writer

