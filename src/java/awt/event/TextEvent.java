/* TextEvent.java -- Event for text changes
   Copyright (C) 1999 Free Software Foundation, Inc.

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

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


package java.awt.event;

/**
  * This event is generated when text changes for an object.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class TextEvent extends java.awt.AWTEvent implements java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * This is the first id in the range of event ids used by this class.
  */
public static final int TEXT_FIRST = 900;

/**
  * This is the last id in the range of event ids used by this class.
  */
public static final int TEXT_LAST = 900;

/**
  * This event id indicates that the text of an object has changed.
  */
public static final int TEXT_VALUE_CHANGED = 900;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>TextEvent</code> with the
  * specified source and id.
  *
  * @param source The object that generated this event.
  * @param id The event id for this event.
  */
public
TextEvent(Object source, int id)
{
  super(source, id);
}

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * Returns a string identifying this event.
  *
  * @return A string identifying this event.
  */
public String
paramString()
{
  return(getClass().getName() + " source=" + getSource() + " id=" + getID());
}

} // class TextEvent

