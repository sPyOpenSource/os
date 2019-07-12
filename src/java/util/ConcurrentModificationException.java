/* ConcurrentModificationException.java -- Data structure concurrently modified
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


package java.util;

/**
 * Exception that is thrown by the collections classes when it is detected that
 * a modification has been made to a data structure when this is not allowed,
 * such as when a collection is structurally modified while an Iterator is
 * operating over it. In cases where this can be detected, a
 * ConcurrentModificationException will be thrown. An Iterator that detects this
 * condition is referred to as fail-fast.
 */
public class ConcurrentModificationException extends RuntimeException
{
  private static final long serialVersionUID = -3666751008965953603L;

  /**
   * Constructs a ConcurrentModificationException with no detail message.
   */
  public ConcurrentModificationException()
  {
    super();
  }

  /**
   * Constructs a ConcurrentModificationException with a detail message.
   *
   * @param detail the detail message for the exception
   */
  public ConcurrentModificationException(String detail)
  {
    super(detail);
  }
}
