/* Boolean.java -- object wrapper for boolean
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


package java.lang;

import java.io.Serializable;

/**
 * Instances of class <code>Boolean</code> represent primitive 
 * <code>boolean</code> values.
 *
 * @author Paul Fisher
 * @since JDK1.0
 */ 
public final class Boolean implements Serializable
{
    static final long serialVersionUID = -3665804199014368530L;
    
    /**
     * This field is a <code>Boolean</code> object representing the
     * primitive value <code>true</code>.
     */
    public static final Boolean TRUE  = new Boolean(true);
    
    /**
     * This field is a <code>Boolean</code> object representing the 
     * primitive value <code>false</code>.
     */
     public static final Boolean FALSE = new Boolean(false);
    public static Class TYPE;

    /**
     * The primitive type <code>boolean</code> is represented by this 
     * <code>Class</code> object.
     */
    //public static final Class TYPE = Class.getPrimitiveClass("boolean");
    
    private boolean value;
    
    /**
     * Create a <code>Boolean</code> object representing the value of the 
     * argument <code>value</code>
     *
     * @param value the primitive value of this <code>Boolean</code>
     */    
    public Boolean(boolean value) {
	this.value = value;
    }
    
    /**
     * Creates a <code>Boolean</code> object representing the primitive 
     * <code>true</code> if and only if <code>s</code> matches 
     * the string "true" ignoring case, otherwise the object will represent 
     * the primitive <code>false</code>.
     *
     * @param s the <code>String</code> representation of <code>true</code>
     *   or false
     */
    public Boolean(String s) {
	value = (s != null && s.equalsIgnoreCase("true"));
    }

    /**
     * Return the primitive <code>boolean</code> value of this 
     * <code>Boolean</code> object.
     */
    public boolean booleanValue() {
	return value;
    }

    /**
     * Calls <code>Boolean(String)</code> to create the new object.
     * @see #Boolean(java.lang.String)
     */
    public static Boolean valueOf(String s) {
	return new Boolean(s);
    }

    /**
     * Returns the integer <code>1231</code> if this object represents 
     * the primitive <code>true</code> and the integer <code>1237</code>
     * otherwise.
     */    
    public int hashCode() {
	return (value) ? 1231 : 1237;
    }

    /**
     * If the <code>obj</code> is an instance of <code>Boolean</code> and
     * has the same primitive value as this object then <code>true</code>
     * is returned.  In all other cases, including if the <code>obj</code>
     * is <code>null</code>, <code>false</code> is returned.
     *
     * @param obj possibly an instance of any <code>Class</code>
     * @return <code>false</code> is <code>obj</code> is an instance of
     *   <code>Boolean</code> and has the same primitive value as this 
     *   object.
     */    
    public boolean equals(Object obj) {
	return (obj instanceof Boolean && value == ((Boolean)obj).value);
    }

    /**
     * If the value of the system property <code>name</code> matches
     * "true" ignoring case then the function returns <code>true</code>.
     */
    public static boolean getBoolean(String name) {
	String val = System.getProperty(name);
	return (val != null && val.equalsIgnoreCase("true"));
    }
    
    /**
     * Returns "true" if the value of this object is <code>true</code> and
     * returns "false" if the value of this object is <code>false</code>.
     */
    public String toString()
    {
	return (value) ? "true" : "false";
    }   
}
