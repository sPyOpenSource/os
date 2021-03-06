/* DSAPrivateKeySpec.java --- DSA Private Key Specificaton class
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


package java.security.spec;
import java.math.BigInteger;

/**
	DSA Private Key class Specification. Used to maintain the DSA
	Private Keys.

	@since JDK 1.2

	@author Mark Benvenuto
*/
public class DSAPrivateKeySpec extends Object implements KeySpec 
{
  private BigInteger x = null;
  private BigInteger p = null;
  private BigInteger q = null;
  private BigInteger g = null;

  /**
     Constructs a new DSAPrivateKeySpec with the specified x, p, q, and g.

     @param x the private key
     @param p the prime
     @param q the sub-prime
     @param g the base
  */
  public DSAPrivateKeySpec(BigInteger x, BigInteger p, BigInteger q, BigInteger g) 
  {
    this.x = x;
    this.p = p;
    this.q = q;
    this.g = g;
  }

  /**
     Returns private key x for the DSA algorithm.

     @return Returns the requested BigInteger
  */
  public BigInteger getX() 
  {
    return this.x;
  }

  /**
     Returns p for the DSA algorithm.

     @return Returns the requested BigInteger
  */
  public BigInteger getP() 
  {
    return this.q;
  }

  /**
     Returns p for the DSA algorithm.

     @return Returns the requested BigInteger
  */
  public BigInteger getQ() 
  {
    return this.q;
  }

  /**
     Returns g for the DSA algorithm.

     @return Returns the requested BigInteger
  */
  public BigInteger getG() 
  {
    return this.g;
  }

}
