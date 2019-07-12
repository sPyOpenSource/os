/* ParsePosition.java -- Keep track of position while parsing.
   Copyright (C) 1998, 1999 Free Software Foundation, Inc.

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


package java.text;

/**
  * This class is used to keep track of the current position during parsing
  * operations.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class ParsePosition
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the index of the current parse position.
  */
private int index;

/**
  * This is the index of the position where an error occurred during parsing.
  */
private int error_index = -1;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a new instance of <code>ParsePosition</code> to
  * have the specified initial index value.
  *
  * @param index The initial parsing index.
  */
public
ParsePosition(int index)
{
  this.index = index;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the current parsing index.
  *
  * @return The current parsing index
  */
public int
getIndex()
{
  return(index);
}

/*************************************************************************/

/**
  * This method sets the current parsing index to the specified value.
  *
  * @param index The new parsing index.
  */
public void
setIndex(int index)
{
  this.index = index;
}

/*************************************************************************/

/**
  * This method returns the error index value.  This value defaults to -1
  * unless explicitly set to another value.
  *
  * @return The error index.
  */
public int
getErrorIndex()
{
  return(error_index);
}

/*************************************************************************/

/**
  * This method sets the error index to the specified value.
  *
  * @param error_index The new error index
  */
public void
setErrorIndex(int error_index)
{
  this.error_index = error_index;
}

/*************************************************************************/

/**
  * This method tests the specified object for equality with this object.  The
  * two objects will be considered equal if and only if all of the following
  * conditions are met.
  * <p>
  * <ul>
  * <li>The specified object is not <code>null</code>.
  * <li>The specified object is an instance of <code>ParsePosition</code>.
  * <li>The specified object has the same index and error index as this object. 
  * </ul>
  *
  * @param obj The <code>Object</code> to test for equality against this object.
  *
  * @return <code>true</code> if the specified object is equal to this object, <code>false</code> otherwise.
  */
public boolean
equals(Object obj)
{
  if (obj == null)
    return(false);

  if (!(obj instanceof ParsePosition))
    return(false);

  ParsePosition pp = (ParsePosition)obj;

  if (pp.getIndex() != getIndex())
    return(false);

  if (pp.getErrorIndex() != getErrorIndex())
    return(false);

  return(true);
}

/*************************************************************************/

/**
  * This method returns a <code>String</code> representation of this
  * object.
  *
  * @return A <code>String</code> that represents this object.
  */
public String
toString()
{
  return(getClass().getName() + "[index=" + getIndex() +
         ",errorIndex=" + getErrorIndex() + "]");
}


} // class ParsePosition

