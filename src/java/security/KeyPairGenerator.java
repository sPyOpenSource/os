/* KeyPairGenerator.java --- Key Pair Generator Class
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
import java.security.spec.AlgorithmParameterSpec;

/**
   KeyPairGenerator is the class used to generate key pairs
   for a security algorithm.
   
   The KeyPairGenerator is created with the getInstance()
   methods. The class is used to generate public and private
   keys for an algorithm and associate it with 
   algorithm parameters.
   
   @author Mark Benvenuto
*/
public abstract class KeyPairGenerator extends KeyPairGeneratorSpi
{

  private Provider provider;
  private String algorithm;

  /**
     Constructs a new KeyPairGenerator

     @param algorithm the algorithm to use
  */
  protected KeyPairGenerator(String algorithm)
  {
    this.algorithm = algorithm;
    this.provider = null;
  }

  /**
     Returns the name of the algorithm used

     @return A string with the name of the algorithm
  */
  public String getAlgorithm()
  {
    return algorithm;
  }

  /** 
      Gets an instance of the KeyPairGenerator class 
      which generates key pairs for the specified algorithm. 
      If the algorithm is not found then, it throws NoSuchAlgorithmException.

      @param algorithm the name of algorithm to choose
      @return a AlgorithmParameterGenerator repesenting the desired algorithm

      @throws NoSuchAlgorithmException if the algorithm is not implemented by providers
  */
  public static KeyPairGenerator getInstance(String algorithm) throws NoSuchAlgorithmException
  {
    Provider[] p = Security.getProviders ();

    for (int i = 0; i < p.length; i++)
      {
	String classname = p[i].getProperty ("KeyPairGenerator." + algorithm);
	if (classname != null)
	  return getInstance (classname, algorithm, p[i]);
      }

    throw new NoSuchAlgorithmException (algorithm);

  }

  /** 
      Gets an instance of the KeyPairGenerator class 
      which generates key pairs for the specified algorithm. 
      If the algorithm is not found then, it throws NoSuchAlgorithmException.

      @param algorithm the name of algorithm to choose
      @param provider the name of the provider to find the algorithm in
      @return a AlgorithmParameterGenerator repesenting the desired algorithm

      @throws NoSuchAlgorithmException if the algorithm is not implemented by the provider
      @throws NoSuchProviderException if the provider is not found
  */
  public static KeyPairGenerator getInstance(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException
  {
    Provider p = Security.getProvider(provider);
    if( p == null)
      throw new NoSuchProviderException();

    return getInstance (p.getProperty ("KeyPairGenerator." + algorithm),
			algorithm, p);
  }

  private static KeyPairGenerator getInstance (String classname,
					       String algorithm,
					       Provider provider)
    throws NoSuchAlgorithmException
  {
    try {
      Object o = Class.forName( classname ).newInstance();
      KeyPairGenerator kpg;
      if( o instanceof KeyPairGeneratorSpi )
	kpg = (KeyPairGenerator)(new DummyKeyPairGenerator( (KeyPairGeneratorSpi)o , algorithm ));
      else
	{
	  kpg = (KeyPairGenerator)o;
	  kpg.algorithm = algorithm;
	}

      kpg.provider = provider;
      return kpg;		
    } catch( ClassNotFoundException cnfe) {
      throw new NoSuchAlgorithmException("Class not found");
    } catch( InstantiationException ie) {
      throw new NoSuchAlgorithmException("Class instantiation failed");
    } catch( IllegalAccessException iae) {
      throw new NoSuchAlgorithmException("Illegal Access");
    }
  }

  /**
     Gets the provider that the class is from.

     @return the provider of this class
  */
  public final Provider getProvider()
  {
    return provider;
  }

  /**
     Initializes the KeyPairGenerator for the specified key size.
     (Since no source of randomness is specified, a default one is
     provided.)

     @param keysize Size of key to generate
  */
  public void initialize(int keysize)
  {
    initialize( keysize, new SecureRandom() );
  }

  /**
     Initializes the KeyPairGenerator for the specified key size
     and specified SecureRandom.

     @param keysize Size of key to generate
     @param random SecureRandom to use

     @since JDK 1.2
  */
  public void initialize(int keysize, SecureRandom random)
  {
    initialize( keysize, random );
  }

  /**
     Initializes the KeyPairGenerator with the specified
     AlgorithmParameterSpec class.
     (Since no source of randomness is specified, a default one is
     provided.)

     @param params AlgorithmParameterSpec to initialize with

     @since JDK 1.2
  */
  public void initialize(AlgorithmParameterSpec params) throws InvalidAlgorithmParameterException
  {
    initialize( params, new SecureRandom() );
  }

  /**
     Initializes the KeyPairGenerator with the specified
     AlgorithmParameterSpec class and specified SecureRandom.

     @param params AlgorithmParameterSpec to initialize with
     @param random SecureRandom to use

     @since JDK 1.2
  */
  public void initialize(AlgorithmParameterSpec params, SecureRandom random) throws InvalidAlgorithmParameterException
  {
    super.initialize( params, random );
  }

  /**
     Generates a KeyPair according the rules for the algorithm.
     Unless intialized, algorithm defaults will be used. It 
     creates a unique key pair each time.

     Same as generateKeyPair();

     @return a key pair
  */
  public final KeyPair genKeyPair()
  {
    try {
      return getInstance("DSA", "GNU").generateKeyPair();
    } catch( Exception e ) {
      System.err.println("genKeyPair failed: " + e );
      e.printStackTrace();
      return null;
    }
  }

  /**
     Generates a KeyPair according the rules for the algorithm.
     Unless intialized, algorithm defaults will be used. It 
     creates a unique key pair each time.

     Same as genKeyPair();

     @return a key pair
  */
  public KeyPair generateKeyPair()
  {
    return generateKeyPair();
  }

}
