/* java.util.MissingResourceException
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
 * This exception is thrown when a resource is missing.
 *
 * @see ResourceBundle
 * @author Jochen Hoenicke
 */
public class MissingResourceException extends RuntimeException
{
  private static final long serialVersionUID = -4876345176062000401L;

  /**
   * The name of the resource bundle requested by user.
   */
  private String className;

  /**
   * The key of the resource in the bundle requested by user.
   */
  private String key;

  /**
   * Creates a new exception, with the specified parameters.
   * @param s the detail message.
   * @param className the name of the resource bundle.
   * @param key the key of the missing resource.
   */
  public MissingResourceException(String s, String className, String key)
  {
    super(s);
    this.className = className;
    this.key = key;
  }

  /**
   * Gets the name of the resource bundle, for which a resource is missing.
   * @return the name of the resource bundle.
   */
  public String getClassName()
  {
    return className;
  }

  /**
   * Gets the key of the resource that is missing bundle, this is an empty
   * string if the whole resource bundle is missing.
   * @return the name of the resource bundle.
   */
  public String getKey()
  {
    return key;
  }
}
