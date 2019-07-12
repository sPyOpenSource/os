/* SecureRandomSpi.java --- Secure Random Service Provider Interface
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


package java.security;
import java.io.Serializable;
//import SecureRandom;
/**
   SecureRandomSpi is the Service Provider Interface for SecureRandom
   providers. It provides an interface for providers to the 
   SecureRandom engine to write their own pseudo-random number
   generator.

   @since JDK 1.2	

   @author Mark Benvenuto <ivymccough@worldnet.att.net>
*/

public abstract class SecureRandomSpi implements Serializable
{

  /**
     Default Constructor for SecureRandomSpi
  */
  public SecureRandomSpi()
  {}

  /**
     Updates the seed for SecureRandomSpi but does not reset seed. 
     It does to this so repeated called never decrease randomness.
  */
  protected abstract void engineSetSeed(byte[] seed);


  /**
     Gets a user specified number of bytes depending on the length
     of the array?

     @param bytes array to fill with random bytes
  */
  protected abstract void engineNextBytes(byte[] bytes);

  /**
     Gets a user specified number of bytes specified by the 
     parameter.

     @param numBytes number of random bytes to generate

     @return an array full of random bytes
  */
  protected abstract byte[] engineGenerateSeed(int numBytes);
}
