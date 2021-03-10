/* Applet.java -- Java base applet class
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
 
As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


package java.applet;

import java.awt.Dimension;
import java.util.Locale;

/**
  * This is the base applet class.  An applet is a Java program that
  * runs inside a web browser or other applet viewer in a restricted
  * environment.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Applet extends java.awt.Panel implements java.io.Serializable
{
  // The applet stub for this applet
  private AppletStub stub;

  /**
    * Default constructor for subclasses.
    */
  public Applet() {}

  /**
    * Returns the value of the specified parameter that was specified in 
    * the &lt;APPLET&gt; tag for this applet.
    *
    * @param name The parameter name.
     * @return 
    */
  public String getParameter(String name)
  {
    return (stub.getParameter (name));
  }

  /**
    * Returns the applet context for this applet.
    *
    * @return The applet context for this applet.
    */
  public AppletContext getAppletContext()
  {
    return (stub.getAppletContext ());
  }

  /**
    * Tests whether or not this applet is currently active.
    *
    * @return <code>true</code> if this applet is active, <code>false</code>
    * otherwise.
    */
  public boolean isActive()
  {
    return (stub.isActive ());
  }

  /**
    * Requests that the applet window for this applet be resized.
    *
    * @param width The new width in pixels.
    * @param height The new height in pixels.
    */
  @Override
  public void resize(int width, int height)
  {
    stub.appletResize (width, height);
  }

  /**
    * Requests that the applet window for this applet be resized.
    *
    * @param dim The <code>Dimension</code> object with the requested
    * width and height.
    */
  @Override
  public void resize(Dimension dim)
  {
    resize (dim.width, dim.height);
  }

  /**
    * Returns the locale for this applet, if it has been set.  If no applet
    * specific locale has been set, the default locale is returned.
    *
    * @return The locale for this applet.
    */
  @Override
  public Locale getLocale()
  {
    return super.getLocale();
  }

  /**
    * Returns a descriptive string with applet defined information.  The
    * implementation in this class returns <code>null</code>.  Applets who
    * wish to return this information should override.
    *
    * @return A string describing the applet.
    */
  public String getAppletInfo()
  {
    return (null);
  }

  /**
    * Returns a list of parameters this applet supports.  Each element of
    * the array is a list of three strings with the name of the parameter,
    * the data type or valid values, and a description.  This method is
    * optional and the default implementation returns <code>null</code>.
    *
    * @return The list of parameters supported by this applet.
    */
  public String[][] getParameterInfo()
  {
    return (null);
  }

  /**
    * This method is called when the applet is first loaded.  The default
    * implementation does nothing.  Applets that wish to do one time
    * initialization should override.
    */
  public void init() {}

  /**
    * This method is called when the applet is being unloaded.  The default
    * implementation does nothing.  Applets that need to clean up resources
    * on exit should override.
    */
  public void destroy() {}

  /**
    * This method is called when the applet should start running.  This is
    * normally each time a web page containing it is loaded.  The default
    * implemention does nothing.  Subclasses should override.
    */
  public void start() {}

  /**
    * This method is called when the applet should stop running.  This is
    * normally when the next web page is loaded.  The default implementation
    * does nothing.
    */
  public void stop() {}

  /**
    * The browser calls this method to set the applet's stub, which is the
    * low level interface to the browser.
    *
    * @param stub The applet stub for this applet.
    */
  public final void setStub (AppletStub stub)
  {
    this.stub = stub;
  }

} // class Applet
