/* Arrays.java -- Utility class with methods to operate on arrays
   Copyright (C) 1998, 1999, 2000, 2001, 2002 Free Software Foundation, Inc.

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


package java.util;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * This class contains various static utility methods performing operations on
 * arrays, and a method to provide a List "view" of an array to facilitate
 * using arrays with Collection-based APIs. All methods throw a
 * {@link NullPointerException} if the parameter array is null.
 * <p>
 *
 * Implementations may use their own algorithms, but must obey the general
 * properties; for example, the sort must be stable and n*log(n) complexity.
 * Sun's implementation of sort, and therefore ours, is a tuned quicksort,
 * adapted from Jon L. Bentley and M. Douglas McIlroy's "Engineering a Sort
 * Function", Software-Practice and Experience, Vol. 23(11) P. 1249-1265
 * (November 1993). This algorithm offers n*log(n) performance on many data
 * sets that cause other quicksorts to degrade to quadratic performance.
 *
 * @author Original author unknown
 * @author Bryce McKinlay
 * @author Eric Blake <ebb9@email.byu.edu>
 * @see Comparable
 * @see Comparator
 * @since 1.2
 * @status updated to 1.4
 */
public class Arrays
{
    @SuppressWarnings("unchecked")
    public static <T> T[] copyOf(T[] original, int newLength) {
        return (T[]) copyOf(original, newLength, original.getClass());
    }
    
    public static char[] copyOf(char[] original, int newLength) {
        char[] copy = new char[newLength];
        System.arraycopy(original, 0, copy, 0,
                         Math.min(original.length, newLength));
        return copy;
    }
    
    public static <T,U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
        @SuppressWarnings("unchecked")
        T[] copy = ((Object)newType == (Object)Object[].class)
            ? (T[]) new Object[newLength]
            : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, 0, copy, 0,
                         Math.min(original.length, newLength));
        return copy;
    }
    
    public static <T> Stream<T> stream(T[] array) {
        //return stream(array, 0, array.length);
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    public static IntStream stream(int[] values) {
        throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Copies the specified range of the specified array into a new array.
     * The initial index of the range (<tt>from</tt>) must lie between zero
     * and <tt>original.length</tt>, inclusive.  The value at
     * <tt>original[from]</tt> is placed into the initial element of the copy
     * (unless <tt>from == original.length</tt> or <tt>from == to</tt>).
     * Values from subsequent elements in the original array are placed into
     * subsequent elements in the copy.  The final index of the range
     * (<tt>to</tt>), which must be greater than or equal to <tt>from</tt>,
     * may be greater than <tt>original.length</tt>, in which case
     * <tt>null</tt> is placed in all elements of the copy whose index is
     * greater than or equal to <tt>original.length - from</tt>.  The length
     * of the returned array will be <tt>to - from</tt>.
     * <p>
     * The resulting array is of exactly the same class as the original array.
     *
     * @param <T> the class of the objects in the array
     * @param original the array from which a range is to be copied
     * @param from the initial index of the range to be copied, inclusive
     * @param to the final index of the range to be copied, exclusive.
     *     (This index may lie outside the array.)
     * @return a new array containing the specified range from the original array,
     *     truncated or padded with nulls to obtain the required length
     * @throws ArrayIndexOutOfBoundsException if {@code from < 0}
     *     or {@code from > original.length}
     * @throws IllegalArgumentException if <tt>from &gt; to</tt>
     * @throws NullPointerException if <tt>original</tt> is null
     * @since 1.6
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] copyOfRange(T[] original, int from, int to) {
        return copyOfRange(original, from, to, (Class<? extends T[]>) original.getClass());
    }

    /**
     * Copies the specified range of the specified array into a new array.
     * The initial index of the range (<tt>from</tt>) must lie between zero
     * and <tt>original.length</tt>, inclusive.  The value at
     * <tt>original[from]</tt> is placed into the initial element of the copy
     * (unless <tt>from == original.length</tt> or <tt>from == to</tt>).
     * Values from subsequent elements in the original array are placed into
     * subsequent elements in the copy.  The final index of the range
     * (<tt>to</tt>), which must be greater than or equal to <tt>from</tt>,
     * may be greater than <tt>original.length</tt>, in which case
     * <tt>null</tt> is placed in all elements of the copy whose index is
     * greater than or equal to <tt>original.length - from</tt>.  The length
     * of the returned array will be <tt>to - from</tt>.
     * The resulting array is of the class <tt>newType</tt>.
     *
     * @param <U> the class of the objects in the original array
     * @param <T> the class of the objects in the returned array
     * @param original the array from which a range is to be copied
     * @param from the initial index of the range to be copied, inclusive
     * @param to the final index of the range to be copied, exclusive.
     *     (This index may lie outside the array.)
     * @param newType the class of the copy to be returned
     * @return a new array containing the specified range from the original array,
     *     truncated or padded with nulls to obtain the required length
     * @throws ArrayIndexOutOfBoundsException if {@code from < 0}
     *     or {@code from > original.length}
     * @throws IllegalArgumentException if <tt>from &gt; to</tt>
     * @throws NullPointerException if <tt>original</tt> is null
     * @throws ArrayStoreException if an element copied from
     *     <tt>original</tt> is not of a runtime type that can be stored in
     *     an array of class <tt>newType</tt>.
     * @since 1.6
     */
    public static <T,U> T[] copyOfRange(U[] original, int from, int to, Class<? extends T[]> newType) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        @SuppressWarnings("unchecked")
        T[] copy = ((Object)newType == (Object)Object[].class)
            ? (T[]) new Object[newLength]
            : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, from, copy, 0,
                         Math.min(original.length - from, newLength));
        return copy;
    }
  /**
   * This class is non-instantiable.
   */
  private Arrays()
  {
  }

// binarySearch
  /**
   * Perform a binary search of a byte array for a key. The array must be
   * sorted (as by the sort() method) - if it is not, the behaviour of this
   * method is undefined, and may be an infinite loop. If the array contains
   * the key more than once, any one of them may be found. Note: although the
   * specification allows for an infinite loop if the array is unsorted, it
   * will not happen in this implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @return the index at which the key was found, or -n-1 if it was not
   *         found, where n is the index of the first value higher than key or
   *         a.length if there is no such value.
   */
  public static int binarySearch(byte[] a, byte key)
  {
    int low = 0;
    int hi = a.length - 1;
    int mid = 0;
    while (low <= hi)
      {
        mid = (low + hi) >> 1;
        final byte d = a[mid];
        if (d == key)
          return mid;
        else if (d > key)
          hi = mid - 1;
        else
          // This gets the insertion point right on the last loop.
          low = ++mid;
      }
    return -mid - 1;
  }

  /**
   * Perform a binary search of a char array for a key. The array must be
   * sorted (as by the sort() method) - if it is not, the behaviour of this
   * method is undefined, and may be an infinite loop. If the array contains
   * the key more than once, any one of them may be found. Note: although the
   * specification allows for an infinite loop if the array is unsorted, it
   * will not happen in this implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @return the index at which the key was found, or -n-1 if it was not
   *         found, where n is the index of the first value higher than key or
   *         a.length if there is no such value.
   */
  public static int binarySearch(char[] a, char key)
  {
    int low = 0;
    int hi = a.length - 1;
    int mid = 0;
    while (low <= hi)
      {
        mid = (low + hi) >> 1;
        final char d = a[mid];
        if (d == key)
          return mid;
        else if (d > key)
          hi = mid - 1;
        else
          // This gets the insertion point right on the last loop.
          low = ++mid;
      }
    return -mid - 1;
  }

  /**
   * Perform a binary search of a short array for a key. The array must be
   * sorted (as by the sort() method) - if it is not, the behaviour of this
   * method is undefined, and may be an infinite loop. If the array contains
   * the key more than once, any one of them may be found. Note: although the
   * specification allows for an infinite loop if the array is unsorted, it
   * will not happen in this implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @return the index at which the key was found, or -n-1 if it was not
   *         found, where n is the index of the first value higher than key or
   *         a.length if there is no such value.
   */
  public static int binarySearch(short[] a, short key)
  {
    int low = 0;
    int hi = a.length - 1;
    int mid = 0;
    while (low <= hi)
      {
        mid = (low + hi) >> 1;
        final short d = a[mid];
        if (d == key)
          return mid;
        else if (d > key)
          hi = mid - 1;
        else
          // This gets the insertion point right on the last loop.
          low = ++mid;
      }
    return -mid - 1;
  }

  /**
   * Perform a binary search of an int array for a key. The array must be
   * sorted (as by the sort() method) - if it is not, the behaviour of this
   * method is undefined, and may be an infinite loop. If the array contains
   * the key more than once, any one of them may be found. Note: although the
   * specification allows for an infinite loop if the array is unsorted, it
   * will not happen in this implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @return the index at which the key was found, or -n-1 if it was not
   *         found, where n is the index of the first value higher than key or
   *         a.length if there is no such value.
   */
  public static int binarySearch(int[] a, int key)
  {
    int low = 0;
    int hi = a.length - 1;
    int mid = 0;
    while (low <= hi)
      {
        mid = (low + hi) >> 1;
        final int d = a[mid];
        if (d == key)
          return mid;
        else if (d > key)
          hi = mid - 1;
        else
          // This gets the insertion point right on the last loop.
          low = ++mid;
      }
    return -mid - 1;
  }

  /**
   * Perform a binary search of a long array for a key. The array must be
   * sorted (as by the sort() method) - if it is not, the behaviour of this
   * method is undefined, and may be an infinite loop. If the array contains
   * the key more than once, any one of them may be found. Note: although the
   * specification allows for an infinite loop if the array is unsorted, it
   * will not happen in this implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @return the index at which the key was found, or -n-1 if it was not
   *         found, where n is the index of the first value higher than key or
   *         a.length if there is no such value.
   */
  public static int binarySearch(long[] a, long key)
  {
    int low = 0;
    int hi = a.length - 1;
    int mid = 0;
    while (low <= hi)
      {
        mid = (low + hi) >> 1;
        final long d = a[mid];
        if (d == key)
          return mid;
        else if (d > key)
          hi = mid - 1;
        else
          // This gets the insertion point right on the last loop.
          low = ++mid;
      }
    return -mid - 1;
  }

  /**
   * Perform a binary search of a float array for a key. The array must be
   * sorted (as by the sort() method) - if it is not, the behaviour of this
   * method is undefined, and may be an infinite loop. If the array contains
   * the key more than once, any one of them may be found. Note: although the
   * specification allows for an infinite loop if the array is unsorted, it
   * will not happen in this implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @return the index at which the key was found, or -n-1 if it was not
   *         found, where n is the index of the first value higher than key or
   *         a.length if there is no such value.
   */
  public static int binarySearch(float[] a, float key)
  {
    // Must use Float.compare to take into account NaN, +-0.
    int low = 0;
    int hi = a.length - 1;
    int mid = 0;
    while (low <= hi)
      {
        mid = (low + hi) >> 1;
        final int r = Float.compare(a[mid], key);
        if (r == 0)
          return mid;
        else if (r > 0)
          hi = mid - 1;
        else
          // This gets the insertion point right on the last loop
          low = ++mid;
      }
    return -mid - 1;
  }

  /**
   * Perform a binary search of a double array for a key. The array must be
   * sorted (as by the sort() method) - if it is not, the behaviour of this
   * method is undefined, and may be an infinite loop. If the array contains
   * the key more than once, any one of them may be found. Note: although the
   * specification allows for an infinite loop if the array is unsorted, it
   * will not happen in this implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @return the index at which the key was found, or -n-1 if it was not
   *         found, where n is the index of the first value higher than key or
   *         a.length if there is no such value.
   */
  public static int binarySearch(double[] a, double key)
  {
    // Must use Double.compare to take into account NaN, +-0.
    int low = 0;
    int hi = a.length - 1;
    int mid = 0;
    while (low <= hi)
      {
        mid = (low + hi) >> 1;
        final int r = Double.compare(a[mid], key);
        if (r == 0)
          return mid;
        else if (r > 0)
          hi = mid - 1;
        else
          // This gets the insertion point right on the last loop
          low = ++mid;
      }
    return -mid - 1;
  }

  /**
   * Perform a binary search of an Object array for a key, using the natural
   * ordering of the elements. The array must be sorted (as by the sort()
   * method) - if it is not, the behaviour of this method is undefined, and may
   * be an infinite loop. Further, the key must be comparable with every item
   * in the array. If the array contains the key more than once, any one of
   * them may be found. Note: although the specification allows for an infinite
   * loop if the array is unsorted, it will not happen in this (JCL)
   * implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @return the index at which the key was found, or -n-1 if it was not
   *         found, where n is the index of the first value higher than key or
   *         a.length if there is no such value.
   * @throws ClassCastException if key could not be compared with one of the
   *         elements of a
   * @throws NullPointerException if a null element in a is compared
   */
  public static int binarySearch(Object[] a, Object key)
  {
    return binarySearch(a, key, null);
  }

  /**
   * Perform a binary search of an Object array for a key, using a supplied
   * Comparator. The array must be sorted (as by the sort() method with the
   * same Comparator) - if it is not, the behaviour of this method is
   * undefined, and may be an infinite loop. Further, the key must be
   * comparable with every item in the array. If the array contains the key
   * more than once, any one of them may be found. Note: although the
   * specification allows for an infinite loop if the array is unsorted, it
   * will not happen in this (JCL) implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @param c the comparator by which the array is sorted; or null to
   *        use the elements' natural order
   * @return the index at which the key was found, or -n-1 if it was not
   *         found, where n is the index of the first value higher than key or
   *         a.length if there is no such value.
   * @throws ClassCastException if key could not be compared with one of the
   *         elements of a
   * @throws NullPointerException if a null element is compared with natural
   *         ordering (only possible when c is null)
   */
  public static int binarySearch(Object[] a, Object key, Comparator c)
  {
      throw new Error("NOT IMPL");
      /*
    int low = 0;
    int hi = a.length - 1;
    int mid = 0;
    while (low <= hi)
      {
        mid = (low + hi) >> 1;
        final int d = Collections.compare(key, a[mid], c);
        if (d == 0)
          return mid;
        else if (d < 0)
          hi = mid - 1;
        else
          // This gets the insertion point right on the last loop
          low = ++mid;
      }
    return -mid - 1;
      */
  }


// equals
  /**
   * Compare two boolean arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @return true if a1 and a2 are both null, or if a2 is of the same length
   *         as a1, and for each 0 <= i < a1.length, a1[i] == a2[i]
   */
  public static boolean equals(boolean[] a1, boolean[] a2)
  {
    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2)
      return true;

    //try {
        // If they're the same length, test each element
        if (a1.length == a2.length){
            for(int i = 0; i < a1.length; i++){
              if (a1[i] != a2[i])
                return false;
            }
            return true;
        }
    //} catch (NullPointerException e) {
        // If one is null, we get a harmless NullPointerException
    //}

    return false;
  }

  /**
   * Compare two byte arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @return true if a1 and a2 are both null, or if a2 is of the same length
   *         as a1, and for each 0 <= i < a1.length, a1[i] == a2[i]
   */
  public static boolean equals(byte[] a1, byte[] a2)
  {
    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2)
      return true;

    //try {
        // If they're the same length, test each element
        if (a1.length == a2.length){
            int i = a1.length;
            while (--i >= 0)
              if (a1[i] != a2[i])
                return false;
            return true;
        }
    //} catch (NullPointerException e) {
        // If one is null, we get a harmless NullPointerException
    //}
    return false;
  }

  /**
   * Compare two char arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @return true if a1 and a2 are both null, or if a2 is of the same length
   *         as a1, and for each 0 <= i < a1.length, a1[i] == a2[i]
   */
  public static boolean equals(char[] a1, char[] a2)
  {
    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2)
      return true;

    //try {
    // If they're the same length, test each element
    /*if (a1.length == a2.length) {
        int i = a1.length;
        while (--i >= 0)
          if (a1[i] != a2[i])
            return false;
        return true;
    }*/
    //} catch (NullPointerException e) {
        // If one is null, we get a harmless NullPointerException
    //}
    return false;
  }

  /**
   * Compare two short arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @return true if a1 and a2 are both null, or if a2 is of the same length
   *         as a1, and for each 0 <= i < a1.length, a1[i] == a2[i]
   */
  public static boolean equals(short[] a1, short[] a2)
  {
    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2)
      return true;

    //try {
        // If they're the same length, test each element
        if (a1.length == a2.length) {
            int i = a1.length;
            while (--i >= 0)
              if (a1[i] != a2[i])
                return false;
            return true;
        }
    //} catch (NullPointerException e) {
        // If one is null, we get a harmless NullPointerException
    //}
    return false;
  }

  /**
   * Compare two int arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @return true if a1 and a2 are both null, or if a2 is of the same length
   *         as a1, and for each 0 <= i < a1.length, a1[i] == a2[i]
   */
  public static boolean equals(int[] a1, int[] a2)
  {
    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2)
      return true;

    //try {
        // If they're the same length, test each element
        if (a1.length == a2.length) {
            int i = a1.length;
            while (--i >= 0)
              if (a1[i] != a2[i])
                return false;
            return true;
        }
    //} catch (NullPointerException e) {
        // If one is null, we get a harmless NullPointerException
    //}
    return false;
  }

  /**
   * Compare two long arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @return true if a1 and a2 are both null, or if a2 is of the same length
   *         as a1, and for each 0 <= i < a1.length, a1[i] == a2[i]
   */
  public static boolean equals(long[] a1, long[] a2)
  {
    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2)
      return true;

    //try {
        // If they're the same length, test each element
        if (a1.length == a2.length)
          {
            int i = a1.length;
            while (--i >= 0)
              if (a1[i] != a2[i])
                return false;
            return true;
          }
    //} catch (NullPointerException e) {
        // If one is null, we get a harmless NullPointerException
    //}
    return false;
  }

  /**
   * Compare two float arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @return true if a1 and a2 are both null, or if a2 is of the same length
   *         as a1, and for each 0 <= i < a1.length, a1[i] == a2[i]
   */
  public static boolean equals(float[] a1, float[] a2)
  {
    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2)
      return true;

    // Must use Float.compare to take into account NaN, +-0.
    //try {
        // If they're the same length, test each element
        if (a1.length == a2.length) {
            int i = a1.length;
            while (--i >= 0)
              if (Float.compare(a1[i], a2[i]) != 0)
                return false;
            return true;
        }
    //} catch (NullPointerException e) {
        // If one is null, we get a harmless NullPointerException
    //}
    return false;
  }

  /**
   * Compare two double arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @return true if a1 and a2 are both null, or if a2 is of the same length
   *         as a1, and for each 0 <= i < a1.length, a1[i] == a2[i]
   */
  public static boolean equals(double[] a1, double[] a2)
  {
    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2)
      return true;

    // Must use Double.compare to take into account NaN, +-0.
    //try {
        // If they're the same length, test each element
        if (a1.length == a2.length) {
            int i = a1.length;
            while (--i >= 0)
              if (Double.compare(a1[i], a2[i]) != 0)
                return false;
            return true;
        }
    //} catch (NullPointerException e) {
        // If one is null, we get a harmless NullPointerException
    //}
    return false;
  }

  /**
   * Compare two Object arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @return true if a1 and a2 are both null, or if a1 is of the same length
   *         as a2, and for each 0 <= i < a.length, a1[i] == null ?
   *         a2[i] == null : a1[i].equals(a2[i]).
   */
  //public static boolean equals(Object[] a1, Object[] a2) {
      //throw new Error("NOT IMPL");
      /*
    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2)
      return true;

    try
      {
        // If they're the same length, test each element
        if (a1.length == a2.length)
          {
            int i = a1.length;
            while (--i >= 0)
              if (! AbstractCollection.equals(a1[i], a2[i]))
                return false;
            return true;
          }
      }
    catch (NullPointerException e)
      {
        // If one is null, we get a harmless NullPointerException
      }
    return false;
      */
 // }


// fill
  /**
   * Fill an array with a boolean value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   */
  public static void fill(boolean[] a, boolean val)
  {
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with a boolean value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   * @throws IllegalArgumentException if fromIndex &gt; toIndex
   * @throws ArrayIndexOutOfBoundsException if fromIndex &lt; 0
   *         || toIndex &gt; a.length
   */
  public static void fill(boolean[] a, int fromIndex, int toIndex, boolean val)
  {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException();
    for (int i = fromIndex; i < toIndex; i++)
      a[i] = val;
  }

  /**
   * Fill an array with a byte value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   */
  public static void fill(byte[] a, byte val)
  {
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with a byte value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   * @throws IllegalArgumentException if fromIndex &gt; toIndex
   * @throws ArrayIndexOutOfBoundsException if fromIndex &lt; 0
   *         || toIndex &gt; a.length
   */
  public static void fill(byte[] a, int fromIndex, int toIndex, byte val)
  {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException();
    for (int i = fromIndex; i < toIndex; i++)
      a[i] = val;
  }

  /**
   * Fill an array with a char value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   */
  public static void fill(char[] a, char val)
  {
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with a char value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   * @throws IllegalArgumentException if fromIndex &gt; toIndex
   * @throws ArrayIndexOutOfBoundsException if fromIndex &lt; 0
   *         || toIndex &gt; a.length
   */
  public static void fill(char[] a, int fromIndex, int toIndex, char val)
  {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException();
    for (int i = fromIndex; i < toIndex; i++)
      a[i] = val;
  }

  /**
   * Fill an array with a short value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   */
  public static void fill(short[] a, short val)
  {
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with a short value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   * @throws IllegalArgumentException if fromIndex &gt; toIndex
   * @throws ArrayIndexOutOfBoundsException if fromIndex &lt; 0
   *         || toIndex &gt; a.length
   */
  public static void fill(short[] a, int fromIndex, int toIndex, short val)
  {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException();
    for (int i = fromIndex; i < toIndex; i++)
      a[i] = val;
  }

  /**
   * Fill an array with an int value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   */
  public static void fill(int[] a, int val)
  {
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with an int value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   * @throws IllegalArgumentException if fromIndex &gt; toIndex
   * @throws ArrayIndexOutOfBoundsException if fromIndex &lt; 0
   *         || toIndex &gt; a.length
   */
  public static void fill(int[] a, int fromIndex, int toIndex, int val)
  {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException();
    for (int i = fromIndex; i < toIndex; i++)
      a[i] = val;
  }

  /**
   * Fill an array with a long value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   */
  public static void fill(long[] a, long val)
  {
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with a long value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   * @throws IllegalArgumentException if fromIndex &gt; toIndex
   * @throws ArrayIndexOutOfBoundsException if fromIndex &lt; 0
   *         || toIndex &gt; a.length
   */
  public static void fill(long[] a, int fromIndex, int toIndex, long val)
  {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException();
    for (int i = fromIndex; i < toIndex; i++)
      a[i] = val;
  }

  /**
   * Fill an array with a float value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   */
  public static void fill(float[] a, float val)
  {
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with a float value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   * @throws IllegalArgumentException if fromIndex &gt; toIndex
   * @throws ArrayIndexOutOfBoundsException if fromIndex &lt; 0
   *         || toIndex &gt; a.length
   */
  public static void fill(float[] a, int fromIndex, int toIndex, float val)
  {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException();
    for (int i = fromIndex; i < toIndex; i++)
      a[i] = val;
  }

  /**
   * Fill an array with a double value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   */
  public static void fill(double[] a, double val)
  {
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with a double value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   * @throws IllegalArgumentException if fromIndex &gt; toIndex
   * @throws ArrayIndexOutOfBoundsException if fromIndex &lt; 0
   *         || toIndex &gt; a.length
   */
  public static void fill(double[] a, int fromIndex, int toIndex, double val)
  {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException();
    for (int i = fromIndex; i < toIndex; i++)
      a[i] = val;
  }

  /**
   * Fill an array with an Object value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   * @throws ClassCastException if val is not an instance of the element
   *         type of a.
   */
  public static void fill(Object[] a, Object val)
  {
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with an Object value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   * @throws ClassCastException if val is not an instance of the element
   *         type of a.
   * @throws IllegalArgumentException if fromIndex &gt; toIndex
   * @throws ArrayIndexOutOfBoundsException if fromIndex &lt; 0
   *         || toIndex &gt; a.length
   */
  public static void fill(Object[] a, int fromIndex, int toIndex, Object val)
  {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException();
    for (int i = fromIndex; i < toIndex; i++)
      a[i] = val;
  }


// sort
  // Thanks to Paul Fisher <rao@gnu.org> for finding this quicksort algorithm
  // as specified by Sun and porting it to Java. The algorithm is an optimised
  // quicksort, as described in Jon L. Bentley and M. Douglas McIlroy's
  // "Engineering a Sort Function", Software-Practice and Experience, Vol.
  // 23(11) P. 1249-1265 (November 1993). This algorithm gives n*log(n)
  // performance on many arrays that would take quadratic time with a standard
  // quicksort.

  /**
   * Performs a stable sort on the elements, arranging them according to their
   * natural order.
   *
   * @param a the byte array to sort
   */
  public static void sort(byte[] a)
  {
    qsort(a, 0, a.length);
  }

  /**
   * Performs a stable sort on the elements, arranging them according to their
   * natural order.
   *
   * @param a the byte array to sort
   * @param fromIndex the first index to sort (inclusive)
   * @param toIndex the last index to sort (exclusive)
   * @throws IllegalArgumentException if fromIndex &gt; toIndex
   * @throws ArrayIndexOutOfBoundsException if fromIndex &lt; 0
   *         || toIndex &gt; a.length
   */
  public static void sort(byte[] a, int fromIndex, int toIndex)
  {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException();
    qsort(a, fromIndex, toIndex - fromIndex);
  }

  /**
   * Finds the index of the median of three array elements.
   *
   * @param a the first index
   * @param b the second index
   * @param c the third index
   * @param d the array
   * @return the index (a, b, or c) which has the middle value of the three
   */
  private static int med3(int a, int b, int c, byte[] d)
  {
    return (d[a] < d[b]
            ? (d[b] < d[c] ? b : d[a] < d[c] ? c : a)
            : (d[b] > d[c] ? b : d[a] > d[c] ? c : a));
  }

  /**
   * Swaps the elements at two locations of an array
   *
   * @param i the first index
   * @param j the second index
   * @param a the array
   */
  private static void swap(int i, int j, byte[] a)
  {
    byte c = a[i];
    a[i] = a[j];
    a[j] = c;
  }

  /**
   * Swaps two ranges of an array.
   *
   * @param i the first range start
   * @param j the second range start
   * @param n the element count
   * @param a the array
   */
  private static void vecswap(int i, int j, int n, byte[] a)
  {
    for ( ; n > 0; i++, j++, n--)
      swap(i, j, a);
  }

  /**
   * Performs a recursive modified quicksort.
   *
   * @param a the array to sort
   * @param from the start index (inclusive)
   * @param count the number of elements to sort
   */
  private static void qsort(byte[] array, int from, int count)
  {
    // Use an insertion sort on small arrays.
    if (count <= 7)
      {
        for (int i = from + 1; i < from + count; i++)
          for (int j = i; j > 0 && array[j - 1] > array[j]; j--)
            swap(j, j - 1, array);
        return;
      }

    // Determine a good median element.
    int mid = count / 2;
    int lo = from;
    int hi = from + count - 1;

    if (count > 40)
      { // big arrays, pseudomedian of 9
        int s = count / 8;
        lo = med3(lo, lo + s, lo + s + s, array);
        mid = med3(mid - s, mid, mid + s, array);
        hi = med3(hi - s - s, hi - s, hi, array);
      }
    mid = med3(lo, mid, hi, array);

    int a, b, c, d;
    int comp;

    // Pull the median element out of the fray, and use it as a pivot.
    swap(from, mid, array);
    a = b = from + 1;
    c = d = hi;

    // Repeatedly move b and c to each other, swapping elements so
    // that all elements before index b are less than the pivot, and all
    // elements after index c are greater than the pivot. a and b track
    // the elements equal to the pivot.
    while (true)
      {
        while (b <= c && (comp = array[b] - array[from]) <= 0)
          {
            if (comp == 0)
              {
                swap(a, b, array);
                a++;
              }
            b++;
          }
        while (c >= b && (comp = array[c] - array[from]) >= 0)
          {
            if (comp == 0)
              {
                swap(c, d, array);
                d--;
              }
            c--;
          }
        if (b > c)
          break;
        swap(b, c, array);
        b++;
        c--;
      }

    // Swap pivot(s) back in place, the recurse on left and right sections.
    int span;
    span = Math.min(a - from, b - a);
    vecswap(from, b - span, span, array);

    span = Math.min(d - c, hi - d - 1);
    vecswap(b, hi - span + 1, span, array);

    span = b - a;
    if (span > 1)
      qsort(array, from, span);

    span = d - c;
    if (span > 1)
      qsort(array, hi - span + 1, span);
  }

  /**
   * Performs a stable sort on the elements, arranging them according to their
   * natural order.
   *
   * @param a the char array to sort
   */
  public static void sort(char[] a)
  {
    qsort(a, 0, a.length);
  }

  /**
   * Performs a stable sort on the elements, arranging them according to their
   * natural order.
   *
   * @param a the char array to sort
   * @param fromIndex the first index to sort (inclusive)
   * @param toIndex the last index to sort (exclusive)
   * @throws IllegalArgumentException if fromIndex &gt; toIndex
   * @throws ArrayIndexOutOfBoundsException if fromIndex &lt; 0
   *         || toIndex &gt; a.length
   */
  public static void sort(char[] a, int fromIndex, int toIndex)
  {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException();
    qsort(a, fromIndex, toIndex - fromIndex);
  }

  /**
   * Finds the index of the median of three array elements.
   *
   * @param a the first index
   * @param b the second index
   * @param c the third index
   * @param d the array
   * @return the index (a, b, or c) which has the middle value of the three
   */
  private static int med3(int a, int b, int c, char[] d)
  {
    return (d[a] < d[b]
            ? (d[b] < d[c] ? b : d[a] < d[c] ? c : a)
            : (d[b] > d[c] ? b : d[a] > d[c] ? c : a));
  }

  /**
   * Swaps the elements at two locations of an array
   *
   * @param i the first index
   * @param j the second index
   * @param a the array
   */
  private static void swap(int i, int j, char[] a)
  {
    char c = a[i];
    a[i] = a[j];
    a[j] = c;
  }

  /**
   * Swaps two ranges of an array.
   *
   * @param i the first range start
   * @param j the second range start
   * @param n the element count
   * @param a the array
   */
  private static void vecswap(int i, int j, int n, char[] a)
  {
    for ( ; n > 0; i++, j++, n--)
      swap(i, j, a);
  }

  /**
   * Performs a recursive modified quicksort.
   *
   * @param a the array to sort
   * @param from the start index (inclusive)
   * @param count the number of elements to sort
   */
  private static void qsort(char[] array, int from, int count)
  {
    // Use an insertion sort on small arrays.
    if (count <= 7)
      {
        for (int i = from + 1; i < from + count; i++)
          for (int j = i; j > 0 && array[j - 1] > array[j]; j--)
            swap(j, j - 1, array);
        return;
      }

    // Determine a good median element.
    int mid = count / 2;
    int lo = from;
    int hi = from + count - 1;

    if (count > 40)
      { // big arrays, pseudomedian of 9
        int s = count / 8;
        lo = med3(lo, lo + s, lo + s + s, array);
        mid = med3(mid - s, mid, mid + s, array);
        hi = med3(hi - s - s, hi - s, hi, array);
      }
    mid = med3(lo, mid, hi, array);

    int a, b, c, d;
    int comp;

    // Pull the median element out of the fray, and use it as a pivot.
    swap(from, mid, array);
    a = b = from + 1;
    c = d = hi;

    // Repeatedly move b and c to each other, swapping elements so
    // that all elements before index b are less than the pivot, and all
    // elements after index c are greater than the pivot. a and b track
    // the elements equal to the pivot.
    while (true)
      {
        while (b <= c && (comp = array[b] - array[from]) <= 0)
          {
            if (comp == 0)
              {
                swap(a, b, array);
                a++;
              }
            b++;
          }
        while (c >= b && (comp = array[c] - array[from]) >= 0)
          {
            if (comp == 0)
              {
                swap(c, d, array);
                d--;
              }
            c--;
          }
        if (b > c)
          break;
        swap(b, c, array);
        b++;
        c--;
      }

    // Swap pivot(s) back in place, the recurse on left and right sections.
    int span;
    span = Math.min(a - from, b - a);
    vecswap(from, b - span, span, array);

    span = Math.min(d - c, hi - d - 1);
    vecswap(b, hi - span + 1, span, array);

    span = b - a;
    if (span > 1)
      qsort(array, from, span);

    span = d - c;
    if (span > 1)
      qsort(array, hi - span + 1, span);
  }

  /**
   * Performs a stable sort on the elements, arranging them according to their
   * natural order.
   *
   * @param a the short array to sort
   */
  public static void sort(short[] a)
  {
    qsort(a, 0, a.length);
  }

  /**
   * Performs a stable sort on the elements, arranging them according to their
   * natural order.
   *
   * @param a the short array to sort
   * @param fromIndex the first index to sort (inclusive)
   * @param toIndex the last index to sort (exclusive)
   * @throws IllegalArgumentException if fromIndex &gt; toIndex
   * @throws ArrayIndexOutOfBoundsException if fromIndex &lt; 0
   *         || toIndex &gt; a.length
   */
  public static void sort(short[] a, int fromIndex, int toIndex)
  {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException();
    qsort(a, fromIndex, toIndex - fromIndex);
  }

  /**
   * Finds the index of the median of three array elements.
   *
   * @param a the first index
   * @param b the second index
   * @param c the third index
   * @param d the array
   * @return the index (a, b, or c) which has the middle value of the three
   */
  private static int med3(int a, int b, int c, short[] d)
  {
    return (d[a] < d[b]
            ? (d[b] < d[c] ? b : d[a] < d[c] ? c : a)
            : (d[b] > d[c] ? b : d[a] > d[c] ? c : a));
  }

  /**
   * Swaps the elements at two locations of an array
   *
   * @param i the first index
   * @param j the second index
   * @param a the array
   */
  private static void swap(int i, int j, short[] a)
  {
    short c = a[i];
    a[i] = a[j];
    a[j] = c;
  }

  /**
   * Swaps two ranges of an array.
   *
   * @param i the first range start
   * @param j the second range start
   * @param n the element count
   * @param a the array
   */
  private static void vecswap(int i, int j, int n, short[] a)
  {
    for ( ; n > 0; i++, j++, n--)
      swap(i, j, a);
  }

  /**
   * Performs a recursive modified quicksort.
   *
   * @param a the array to sort
   * @param from the start index (inclusive)
   * @param count the number of elements to sort
   */
  private static void qsort(short[] array, int from, int count)
  {
    // Use an insertion sort on small arrays.
    if (count <= 7)
      {
        for (int i = from + 1; i < from + count; i++)
          for (int j = i; j > 0 && array[j - 1] > array[j]; j--)
            swap(j, j - 1, array);
        return;
      }

    // Determine a good median element.
    int mid = count / 2;
    int lo = from;
    int hi = from + count - 1;

    if (count > 40)
      { // big arrays, pseudomedian of 9
        int s = count / 8;
        lo = med3(lo, lo + s, lo + s + s, array);
        mid = med3(mid - s, mid, mid + s, array);
        hi = med3(hi - s - s, hi - s, hi, array);
      }
    mid = med3(lo, mid, hi, array);

    int a, b, c, d;
    int comp;

    // Pull the median element out of the fray, and use it as a pivot.
    swap(from, mid, array);
    a = b = from + 1;
    c = d = hi;

    // Repeatedly move b and c to each other, swapping elements so
    // that all elements before index b are less than the pivot, and all
    // elements after index c are greater than the pivot. a and b track
    // the elements equal to the pivot.
    while (true)
      {
        while (b <= c && (comp = array[b] - array[from]) <= 0)
          {
            if (comp == 0)
              {
                swap(a, b, array);
                a++;
              }
            b++;
          }
        while (c >= b && (comp = array[c] - array[from]) >= 0)
          {
            if (comp == 0)
              {
                swap(c, d, array);
                d--;
              }
            c--;
          }
        if (b > c)
          break;
        swap(b, c, array);
        b++;
        c--;
      }

    // Swap pivot(s) back in place, the recurse on left and right sections.
    int span;
    span = Math.min(a - from, b - a);
    vecswap(from, b - span, span, array);

    span = Math.min(d - c, hi - d - 1);
    vecswap(b, hi - span + 1, span, array);

    span = b - a;
    if (span > 1)
      qsort(array, from, span);

    span = d - c;
    if (span > 1)
      qsort(array, hi - span + 1, span);
  }

  /**
   * Performs a stable sort on the elements, arranging them according to their
   * natural order.
   *
   * @param a the int array to sort
   */
  public static void sort(int[] a)
  {
    qsort(a, 0, a.length);
  }

  /**
   * Performs a stable sort on the elements, arranging them according to their
   * natural order.
   *
   * @param a the int array to sort
   * @param fromIndex the first index to sort (inclusive)
   * @param toIndex the last index to sort (exclusive)
   * @throws IllegalArgumentException if fromIndex &gt; toIndex
   * @throws ArrayIndexOutOfBoundsException if fromIndex &lt; 0
   *         || toIndex &gt; a.length
   */
  public static void sort(int[] a, int fromIndex, int toIndex)
  {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException();
    qsort(a, fromIndex, toIndex - fromIndex);
  }

  /**
   * Finds the index of the median of three array elements.
   *
   * @param a the first index
   * @param b the second index
   * @param c the third index
   * @param d the array
   * @return the index (a, b, or c) which has the middle value of the three
   */
  private static int med3(int a, int b, int c, int[] d)
  {
    return (d[a] < d[b]
            ? (d[b] < d[c] ? b : d[a] < d[c] ? c : a)
            : (d[b] > d[c] ? b : d[a] > d[c] ? c : a));
  }

  /**
   * Swaps the elements at two locations of an array
   *
   * @param i the first index
   * @param j the second index
   * @param a the array
   */
  private static void swap(int i, int j, int[] a)
  {
    int c = a[i];
    a[i] = a[j];
    a[j] = c;
  }

  /**
   * Swaps two ranges of an array.
   *
   * @param i the first range start
   * @param j the second range start
   * @param n the element count
   * @param a the array
   */
  private static void vecswap(int i, int j, int n, int[] a)
  {
    for ( ; n > 0; i++, j++, n--)
      swap(i, j, a);
  }

  /**
   * Compares two integers in natural order, since a - b is inadequate.
   *
   * @param a the first int
   * @param b the second int
   * @return &lt; 0, 0, or &gt; 0 accorting to the comparison
   */
  private static int compare(int a, int b)
  {
    return a < b ? -1 : a == b ? 0 : 1;
  }

  /**
   * Performs a recursive modified quicksort.
   *
   * @param a the array to sort
   * @param from the start index (inclusive)
   * @param count the number of elements to sort
   */
  private static void qsort(int[] array, int from, int count)
  {
    // Use an insertion sort on small arrays.
    if (count <= 7)
      {
        for (int i = from + 1; i < from + count; i++)
          for (int j = i; j > 0 && array[j - 1] > array[j]; j--)
            swap(j, j - 1, array);
        return;
      }

    // Determine a good median element.
    int mid = count / 2;
    int lo = from;
    int hi = from + count - 1;

    if (count > 40)
      { // big arrays, pseudomedian of 9
        int s = count / 8;
        lo = med3(lo, lo + s, lo + s + s, array);
        mid = med3(mid - s, mid, mid + s, array);
        hi = med3(hi - s - s, hi - s, hi, array);
      }
    mid = med3(lo, mid, hi, array);

    int a, b, c, d;
    int comp;

    // Pull the median element out of the fray, and use it as a pivot.
    swap(from, mid, array);
    a = b = from + 1;
    c = d = hi;

    // Repeatedly move b and c to each other, swapping elements so
    // that all elements before index b are less than the pivot, and all
    // elements after index c are greater than the pivot. a and b track
    // the elements equal to the pivot.
    while (true)
      {
        while (b <= c && (comp = compare(array[b], array[from])) <= 0)
          {
            if (comp == 0)
              {
                swap(a, b, array);
                a++;
              }
            b++;
          }
        while (c >= b && (comp = compare(array[c], array[from])) >= 0)
          {
            if (comp == 0)
              {
                swap(c, d, array);
                d--;
              }
            c--;
          }
        if (b > c)
          break;
        swap(b, c, array);
        b++;
        c--;
      }

    // Swap pivot(s) back in place, the recurse on left and right sections.
    int span;
    span = Math.min(a - from, b - a);
    vecswap(from, b - span, span, array);

    span = Math.min(d - c, hi - d - 1);
    vecswap(b, hi - span + 1, span, array);

    span = b - a;
    if (span > 1)
      qsort(array, from, span);

    span = d - c;
    if (span > 1)
      qsort(array, hi - span + 1, span);
  }

  /**
   * Performs a stable sort on the elements, arranging them according to their
   * natural order.
   *
   * @param a the long array to sort
   */
  public static void sort(long[] a)
  {
    qsort(a, 0, a.length);
  }

  /**
   * Performs a stable sort on the elements, arranging them according to their
   * natural order.
   *
   * @param a the long array to sort
   * @param fromIndex the first index to sort (inclusive)
   * @param toIndex the last index to sort (exclusive)
   * @throws IllegalArgumentException if fromIndex &gt; toIndex
   * @throws ArrayIndexOutOfBoundsException if fromIndex &lt; 0
   *         || toIndex &gt; a.length
   */
  public static void sort(long[] a, int fromIndex, int toIndex)
  {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException();
    qsort(a, fromIndex, toIndex - fromIndex);
  }

  /**
   * Finds the index of the median of three array elements.
   *
   * @param a the first index
   * @param b the second index
   * @param c the third index
   * @param d the array
   * @return the index (a, b, or c) which has the middle value of the three
   */
  private static int med3(int a, int b, int c, long[] d)
  {
    return (d[a] < d[b]
            ? (d[b] < d[c] ? b : d[a] < d[c] ? c : a)
            : (d[b] > d[c] ? b : d[a] > d[c] ? c : a));
  }

  /**
   * Swaps the elements at two locations of an array
   *
   * @param i the first index
   * @param j the second index
   * @param a the array
   */
  private static void swap(int i, int j, long[] a)
  {
    long c = a[i];
    a[i] = a[j];
    a[j] = c;
  }

  /**
   * Swaps two ranges of an array.
   *
   * @param i the first range start
   * @param j the second range start
   * @param n the element count
   * @param a the array
   */
  private static void vecswap(int i, int j, int n, long[] a)
  {
    for ( ; n > 0; i++, j++, n--)
      swap(i, j, a);
  }

  /**
   * Compares two longs in natural order, since a - b is inadequate.
   *
   * @param a the first long
   * @param b the second long
   * @return &lt; 0, 0, or &gt; 0 accorting to the comparison
   */
  private static int compare(long a, long b)
  {
    return a < b ? -1 : a == b ? 0 : 1;
  }

  /**
   * Performs a recursive modified quicksort.
   *
   * @param a the array to sort
   * @param from the start index (inclusive)
   * @param count the number of elements to sort
   */
  private static void qsort(long[] array, int from, int count)
  {
    // Use an insertion sort on small arrays.
    if (count <= 7)
      {
        for (int i = from + 1; i < from + count; i++)
          for (int j = i; j > 0 && array[j - 1] > array[j]; j--)
            swap(j, j - 1, array);
        return;
      }

    // Determine a good median element.
    int mid = count / 2;
    int lo = from;
    int hi = from + count - 1;

    if (count > 40)
      { // big arrays, pseudomedian of 9
        int s = count / 8;
        lo = med3(lo, lo + s, lo + s + s, array);
        mid = med3(mid - s, mid, mid + s, array);
        hi = med3(hi - s - s, hi - s, hi, array);
      }
    mid = med3(lo, mid, hi, array);

    int a, b, c, d;
    int comp;

    // Pull the median element out of the fray, and use it as a pivot.
    swap(from, mid, array);
    a = b = from + 1;
    c = d = hi;

    // Repeatedly move b and c to each other, swapping elements so
    // that all elements before index b are less than the pivot, and all
    // elements after index c are greater than the pivot. a and b track
    // the elements equal to the pivot.
    while (true){
        while (b <= c && (comp = compare(array[b], array[from])) <= 0){
            if (comp == 0){
                swap(a, b, array);
                a++;
            }
            b++;
        }
        while (c >= b && (comp = compare(array[c], array[from])) >= 0){
            if (comp == 0){
                swap(c, d, array);
                d--;
            }
            c--;
        }
        if (b > c)
          break;
        swap(b, c, array);
        b++;
        c--;
    }

    // Swap pivot(s) back in place, the recurse on left and right sections.
    int span;
    span = Math.min(a - from, b - a);
    vecswap(from, b - span, span, array);

    span = Math.min(d - c, hi - d - 1);
    vecswap(b, hi - span + 1, span, array);

    span = b - a;
    if (span > 1)
      qsort(array, from, span);

    span = d - c;
    if (span > 1)
      qsort(array, hi - span + 1, span);
  }

  /**
   * Performs a stable sort on the elements, arranging them according to their
   * natural order.
   *
   * @param a the float array to sort
   */
  public static void sort(float[] a)
  {
    qsort(a, 0, a.length);
  }

  /**
   * Performs a stable sort on the elements, arranging them according to their
   * natural order.
   *
   * @param a the float array to sort
   * @param fromIndex the first index to sort (inclusive)
   * @param toIndex the last index to sort (exclusive)
   * @throws IllegalArgumentException if fromIndex &gt; toIndex
   * @throws ArrayIndexOutOfBoundsException if fromIndex &lt; 0
   *         || toIndex &gt; a.length
   */
  public static void sort(float[] a, int fromIndex, int toIndex)
  {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException();
    qsort(a, fromIndex, toIndex - fromIndex);
  }

  /**
   * Finds the index of the median of three array elements.
   *
   * @param a the first index
   * @param b the second index
   * @param c the third index
   * @param d the array
   * @return the index (a, b, or c) which has the middle value of the three
   */
  private static int med3(int a, int b, int c, float[] d)
  {
    return (Float.compare(d[a], d[b]) < 0
            ? (Float.compare(d[b], d[c]) < 0 ? b
               : Float.compare(d[a], d[c]) < 0 ? c : a)
            : (Float.compare(d[b], d[c]) > 0 ? b
               : Float.compare(d[a], d[c]) > 0 ? c : a));
  }

  /**
   * Swaps the elements at two locations of an array
   *
   * @param i the first index
   * @param j the second index
   * @param a the array
   */
  private static void swap(int i, int j, float[] a)
  {
    float c = a[i];
    a[i] = a[j];
    a[j] = c;
  }

  /**
   * Swaps two ranges of an array.
   *
   * @param i the first range start
   * @param j the second range start
   * @param n the element count
   * @param a the array
   */
  private static void vecswap(int i, int j, int n, float[] a)
  {
    for ( ; n > 0; i++, j++, n--)
      swap(i, j, a);
  }

  /**
   * Performs a recursive modified quicksort.
   *
   * @param a the array to sort
   * @param from the start index (inclusive)
   * @param count the number of elements to sort
   */
  private static void qsort(float[] array, int from, int count)
  {
    // Use an insertion sort on small arrays.
    if (count <= 7)
      {
        for (int i = from + 1; i < from + count; i++)
          for (int j = i;
               j > 0 && Float.compare(array[j - 1], array[j]) > 0;
               j--)
            {
              swap(j, j - 1, array);
            }
        return;
      }

    // Determine a good median element.
    int mid = count / 2;
    int lo = from;
    int hi = from + count - 1;

    if (count > 40)
      { // big arrays, pseudomedian of 9
        int s = count / 8;
        lo = med3(lo, lo + s, lo + s + s, array);
        mid = med3(mid - s, mid, mid + s, array);
        hi = med3(hi - s - s, hi - s, hi, array);
      }
    mid = med3(lo, mid, hi, array);

    int a, b, c, d;
    int comp;

    // Pull the median element out of the fray, and use it as a pivot.
    swap(from, mid, array);
    a = b = from + 1;
    c = d = hi;

    // Repeatedly move b and c to each other, swapping elements so
    // that all elements before index b are less than the pivot, and all
    // elements after index c are greater than the pivot. a and b track
    // the elements equal to the pivot.
    while (true)
      {
        while (b <= c && (comp = Float.compare(array[b], array[from])) <= 0)
          {
            if (comp == 0)
              {
                swap(a, b, array);
                a++;
              }
            b++;
          }
        while (c >= b && (comp = Float.compare(array[c], array[from])) >= 0)
          {
            if (comp == 0)
              {
                swap(c, d, array);
                d--;
              }
            c--;
          }
        if (b > c)
          break;
        swap(b, c, array);
        b++;
        c--;
      }

    // Swap pivot(s) back in place, the recurse on left and right sections.
    int span;
    span = Math.min(a - from, b - a);
    vecswap(from, b - span, span, array);

    span = Math.min(d - c, hi - d - 1);
    vecswap(b, hi - span + 1, span, array);

    span = b - a;
    if (span > 1)
      qsort(array, from, span);

    span = d - c;
    if (span > 1)
      qsort(array, hi - span + 1, span);
  }

  /**
   * Performs a stable sort on the elements, arranging them according to their
   * natural order.
   *
   * @param a the double array to sort
   */
  public static void sort(double[] a)
  {
    qsort(a, 0, a.length);
  }

  /**
   * Performs a stable sort on the elements, arranging them according to their
   * natural order.
   *
   * @param a the double array to sort
   * @param fromIndex the first index to sort (inclusive)
   * @param toIndex the last index to sort (exclusive)
   * @throws IllegalArgumentException if fromIndex &gt; toIndex
   * @throws ArrayIndexOutOfBoundsException if fromIndex &lt; 0
   *         || toIndex &gt; a.length
   */
  public static void sort(double[] a, int fromIndex, int toIndex)
  {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException();
    qsort(a, fromIndex, toIndex - fromIndex);
  }

  /**
   * Finds the index of the median of three array elements.
   *
   * @param a the first index
   * @param b the second index
   * @param c the third index
   * @param d the array
   * @return the index (a, b, or c) which has the middle value of the three
   */
  private static int med3(int a, int b, int c, double[] d)
  {
    return (Double.compare(d[a], d[b]) < 0
            ? (Double.compare(d[b], d[c]) < 0 ? b
               : Double.compare(d[a], d[c]) < 0 ? c : a)
            : (Double.compare(d[b], d[c]) > 0 ? b
               : Double.compare(d[a], d[c]) > 0 ? c : a));
  }

  /**
   * Swaps the elements at two locations of an array
   *
   * @param i the first index
   * @param j the second index
   * @param a the array
   */
  private static void swap(int i, int j, double[] a)
  {
    double c = a[i];
    a[i] = a[j];
    a[j] = c;
  }

  /**
   * Swaps two ranges of an array.
   *
   * @param i the first range start
   * @param j the second range start
   * @param n the element count
   * @param a the array
   */
  private static void vecswap(int i, int j, int n, double[] a)
  {
    for ( ; n > 0; i++, j++, n--)
      swap(i, j, a);
  }

  /**
   * Performs a recursive modified quicksort.
   *
   * @param a the array to sort
   * @param from the start index (inclusive)
   * @param count the number of elements to sort
   */
  private static void qsort(double[] array, int from, int count)
  {
    // Use an insertion sort on small arrays.
    if (count <= 7)
      {
        for (int i = from + 1; i < from + count; i++)
          for (int j = i;
               j > 0 && Double.compare(array[j - 1], array[j]) > 0;
               j--)
            {
              swap(j, j - 1, array);
            }
        return;
      }

    // Determine a good median element.
    int mid = count / 2;
    int lo = from;
    int hi = from + count - 1;

    if (count > 40)
      { // big arrays, pseudomedian of 9
        int s = count / 8;
        lo = med3(lo, lo + s, lo + s + s, array);
        mid = med3(mid - s, mid, mid + s, array);
        hi = med3(hi - s - s, hi - s, hi, array);
      }
    mid = med3(lo, mid, hi, array);

    int a, b, c, d;
    int comp;

    // Pull the median element out of the fray, and use it as a pivot.
    swap(from, mid, array);
    a = b = from + 1;
    c = d = hi;

    // Repeatedly move b and c to each other, swapping elements so
    // that all elements before index b are less than the pivot, and all
    // elements after index c are greater than the pivot. a and b track
    // the elements equal to the pivot.
    while (true)
      {
        while (b <= c && (comp = Double.compare(array[b], array[from])) <= 0)
          {
            if (comp == 0)
              {
                swap(a, b, array);
                a++;
              }
            b++;
          }
        while (c >= b && (comp = Double.compare(array[c], array[from])) >= 0)
          {
            if (comp == 0)
              {
                swap(c, d, array);
                d--;
              }
            c--;
          }
        if (b > c)
          break;
        swap(b, c, array);
        b++;
        c--;
      }

    // Swap pivot(s) back in place, the recurse on left and right sections.
    int span;
    span = Math.min(a - from, b - a);
    vecswap(from, b - span, span, array);

    span = Math.min(d - c, hi - d - 1);
    vecswap(b, hi - span + 1, span, array);

    span = b - a;
    if (span > 1)
      qsort(array, from, span);

    span = d - c;
    if (span > 1)
      qsort(array, hi - span + 1, span);
  }

  /**
   * Sort an array of Objects according to their natural ordering. The sort is
   * guaranteed to be stable, that is, equal elements will not be reordered.
   * The sort algorithm is a mergesort with the merge omitted if the last
   * element of one half comes before the first element of the other half. This
   * algorithm gives guaranteed O(n*log(n)) time, at the expense of making a
   * copy of the array.
   *
   * @param a the array to be sorted
   * @throws ClassCastException if any two elements are not mutually
   *         comparable
   * @throws NullPointerException if an element is null (since
   *         null.compareTo cannot work)
   * @see Comparable
   */
  public static void sort(Object[] a)
  {
    sort(a, 0, a.length, null);
  }

  /**
   * Sort an array of Objects according to a Comparator. The sort is
   * guaranteed to be stable, that is, equal elements will not be reordered.
   * The sort algorithm is a mergesort with the merge omitted if the last
   * element of one half comes before the first element of the other half. This
   * algorithm gives guaranteed O(n*log(n)) time, at the expense of making a
   * copy of the array.
   *
   * @param a the array to be sorted
   * @param c a Comparator to use in sorting the array; or null to indicate
   *        the elements' natural order
   * @throws ClassCastException if any two elements are not mutually
   *         comparable by the Comparator provided
   * @throws NullPointerException if a null element is compared with natural
   *         ordering (only possible when c is null)
   */
  public static void sort(Object[] a, Comparator c)
  {
    sort(a, 0, a.length, c);
  }

  /**
   * Sort an array of Objects according to their natural ordering. The sort is
   * guaranteed to be stable, that is, equal elements will not be reordered.
   * The sort algorithm is a mergesort with the merge omitted if the last
   * element of one half comes before the first element of the other half. This
   * algorithm gives guaranteed O(n*log(n)) time, at the expense of making a
   * copy of the array.
   *
   * @param a the array to be sorted
   * @param fromIndex the index of the first element to be sorted
   * @param toIndex the index of the last element to be sorted plus one
   * @throws ClassCastException if any two elements are not mutually
   *         comparable
   * @throws NullPointerException if an element is null (since
   *         null.compareTo cannot work)
   * @throws ArrayIndexOutOfBoundsException, if fromIndex and toIndex
   *         are not in range.
   * @throws IllegalArgumentException if fromIndex > toIndex
   */
  public static void sort(Object[] a, int fromIndex, int toIndex)
  {
    sort(a, fromIndex, toIndex, null);
  }

  /**
   * Sort an array of Objects according to a Comparator. The sort is
   * guaranteed to be stable, that is, equal elements will not be reordered.
   * The sort algorithm is a mergesort with the merge omitted if the last
   * element of one half comes before the first element of the other half. This
   * algorithm gives guaranteed O(n*log(n)) time, at the expense of making a
   * copy of the array.
   *
   * @param a the array to be sorted
   * @param fromIndex the index of the first element to be sorted
   * @param toIndex the index of the last element to be sorted plus one
   * @param c a Comparator to use in sorting the array; or null to indicate
   *        the elements' natural order
   * @throws ClassCastException if any two elements are not mutually
   *         comparable by the Comparator provided
   * @throws ArrayIndexOutOfBoundsException, if fromIndex and toIndex
   *         are not in range.
   * @throws IllegalArgumentException if fromIndex > toIndex
   * @throws NullPointerException if a null element is compared with natural
   *         ordering (only possible when c is null)
   */
  public static void sort(Object[] a, int fromIndex, int toIndex, Comparator c)
  {
      throw new Error("NOT IMPL");
      /*
    if (fromIndex > toIndex)
      throw new IllegalArgumentException("fromIndex " + fromIndex
                                         + " > toIndex " + toIndex);

    // In general, the code attempts to be simple rather than fast, the
    // idea being that a good optimising JIT will be able to optimise it
    // better than I can, and if I try it will make it more confusing for
    // the JIT. First presort the array in chunks of length 6 with insertion
    // sort. A mergesort would give too much overhead for this length.
    for (int chunk = fromIndex; chunk < toIndex; chunk += 6)
      {
        int end = Math.min(chunk + 6, toIndex);
        for (int i = chunk + 1; i < end; i++)
          {
            if (Collections.compare(a[i - 1], a[i], c) > 0)
              {
                // not already sorted
                int j = i;
                Object elem = a[j];
                do
                  {
                    a[j] = a[j - 1];
                    j--;
                  }
                while (j > chunk
                       && Collections.compare(a[j - 1], elem, c) > 0);
                a[j] = elem;
              }
          }
      }

    int len = toIndex - fromIndex;
    // If length is smaller or equal 6 we are done.
    if (len <= 6)
      return;

    Object[] src = a;
    Object[] dest = new Object[len];
    Object[] t = null; // t is used for swapping src and dest

    // The difference of the fromIndex of the src and dest array.
    int srcDestDiff = -fromIndex;

    // The merges are done in this loop
    for (int size = 6; size < len; size <<= 1)
      {
        for (int start = fromIndex; start < toIndex; start += size << 1)
          {
            // mid is the start of the second sublist;
            // end the start of the next sublist (or end of array).
            int mid = start + size;
            int end = Math.min(toIndex, mid + size);

            // The second list is empty or the elements are already in
            // order - no need to merge
            if (mid >= end
                || Collections.compare(src[mid - 1], src[mid], c) <= 0)
              {
                System.arraycopy(src, start,
                                 dest, start + srcDestDiff, end - start);

                // The two halves just need swapping - no need to merge
              }
            else if (Collections.compare(src[start], src[end - 1], c) > 0)
              {
                System.arraycopy(src, start,
                                 dest, end - size + srcDestDiff, size);
                System.arraycopy(src, mid,
                                 dest, start + srcDestDiff, end - mid);

              }
            else
              {
                // Declare a lot of variables to save repeating
                // calculations.  Hopefully a decent JIT will put these
                // in registers and make this fast
                int p1 = start;
                int p2 = mid;
                int i = start + srcDestDiff;

                // The main merge loop; terminates as soon as either
                // half is ended
                while (p1 < mid && p2 < end)
                  {
                    dest[i++] =
                      src[(Collections.compare(src[p1], src[p2], c) <= 0
                           ? p1++ : p2++)];
                  }

                // Finish up by copying the remainder of whichever half
                // wasn't finished.
                if (p1 < mid)
                  System.arraycopy(src, p1, dest, i, mid - p1);
                else
                  System.arraycopy(src, p2, dest, i, end - p2);
              }
          }
        // swap src and dest ready for the next merge
        t = src;
        src = dest;
        dest = t;
        fromIndex += srcDestDiff;
        toIndex += srcDestDiff;
        srcDestDiff = -srcDestDiff;
      }

    // make sure the result ends up back in the right place.  Note
    // that src and dest may have been swapped above, so src
    // contains the sorted array.
    if (src != a)
      {
        // Note that fromIndex == 0.
        System.arraycopy(src, 0, a, srcDestDiff, toIndex);
      }
      */
  }

  /**
   * Returns a list "view" of the specified array. This method is intended to
   * make it easy to use the Collections API with existing array-based APIs and
   * programs. Changes in the list or the array show up in both places. The
   * list does not support element addition or removal, but does permit
   * value modification. The returned list implements both Serializable and
   * RandomAccess.
   *
   * @param a the array to return a view of
   * @return a fixed-size list, changes to which "write through" to the array
   * @see Serializable
   * @see RandomAccess
   * @see Arrays.ArrayList
   */
  public static List asList(final Object[] a)
  {
    return new Arrays.ArrayList(a);
  }
  
    public static int hashCode(long a[]) {
        if (a == null)
            return 0;

        int result = 1;
        for (long element : a) {
            int elementHash = (int)(element ^ (element >>> 32));
            result = 31 * result + elementHash;
        }

        return result;
    }
    
    public static int hashCode(Object a[]) {
        if (a == null)
            return 0;

        int result = 1;

        for (Object element : a)
            result = 31 * result + (element == null ? 0 : element.hashCode());

        return result;
    }
    
    public static boolean deepEquals(Object[] a1, Object[] a2) {
        if (a1 == a2)
            return true;
        if (a1 == null || a2==null)
            return false;
        int length = a1.length;
        if (a2.length != length)
            return false;

        for (int i = 0; i < length; i++) {
            Object e1 = a1[i];
            Object e2 = a2[i];

            if (e1 == e2)
                continue;
            if (e1 == null)
                return false;

            // Figure out whether the two elements are equal
            boolean eq = deepEquals0(e1, e2);

            if (!eq)
                return false;
        }
        return true;
    }
    
    static boolean deepEquals0(Object e1, Object e2) {
        //assert e1 != null;
        boolean eq;
        if (e1 instanceof Object[] && e2 instanceof Object[])
            eq = deepEquals ((Object[]) e1, (Object[]) e2);
        else if (e1 instanceof byte[] && e2 instanceof byte[])
            eq = equals((byte[]) e1, (byte[]) e2);
        else if (e1 instanceof short[] && e2 instanceof short[])
            eq = equals((short[]) e1, (short[]) e2);
        else if (e1 instanceof int[] && e2 instanceof int[])
            eq = equals((int[]) e1, (int[]) e2);
        else if (e1 instanceof long[] && e2 instanceof long[])
            eq = equals((long[]) e1, (long[]) e2);
        else if (e1 instanceof char[] && e2 instanceof char[])
            eq = equals((char[]) e1, (char[]) e2);
        else if (e1 instanceof float[] && e2 instanceof float[])
            eq = equals((float[]) e1, (float[]) e2);
        else if (e1 instanceof double[] && e2 instanceof double[])
            eq = equals((double[]) e1, (double[]) e2);
        else if (e1 instanceof boolean[] && e2 instanceof boolean[])
            eq = equals((boolean[]) e1, (boolean[]) e2);
        else
            eq = e1.equals(e2);
        return eq;
    }
    
  /**
   * Inner class used by {@link #asList(Object[])} to provide a list interface
   * to an array. The name, though it clashes with java.util.ArrayList, is
   * Sun's choice for Serialization purposes. Element addition and removal
   * is prohibited, but values can be modified.
   *
   * @author Eric Blake <ebb9@email.byu.edu>
   * @status updated to 1.4
   */
  private static class ArrayList<E> extends AbstractList<E>
    implements Serializable, RandomAccess
  {
    // We override the necessary methods, plus others which will be much
    // more efficient with direct iteration rather than relying on iterator().

    /**
     * Compatible with JDK 1.4.
     */
    private static final long serialVersionUID = -2764017481108945198L;

    /**
     * The array we are viewing.
     * @serial the array
     */
    private final E[] a;

    /**
     * Construct a list view of the array.
     * @param a the array to view
     * @throws NullPointerException if a is null
     */
    ArrayList(E[] a)
    {
      // We have to explicitly check.
      if (a == null)
        throw new NullPointerException();
      this.a = a;
    }

    /*@Override
    public Object get(int index)
    {
      return a[index];
    }*/

    public int size()
    {
      return a.length;
    }

    public E set(int index, E element)
    {
      E old = a[index];
      a[index] = element;
      return old;
    }

    /*public boolean contains(Object o)
    {
      return lastIndexOf(o) >= 0;
    }*/

    public int indexOf(Object o) {
            E[] a = this.a;
            if (o == null) {
                for (int i = 0; i < a.length; i++)
                    if (a[i] == null)
                        return i;
            } else {
                for (int i = 0; i < a.length; i++)
                    if (o.equals(a[i]))
                        return i;
            }
            return -1;
        }

    /*public int lastIndexOf(Object o)
    {
      int i = a.length;
      while (--i >= 0)
        if (this.equals(o, a[i]))
          return i;
      return -1;
    }*/

    public Object[] toArray()
    {
      return (Object[]) a.clone();
    }

    /*public Object[] toArray(Object[] array)
    {
      int size = a.length;
      if (array.length < size)
        array = (Object[])Array.newInstance(array.getClass().getComponentType(), size);
      else if (array.length > size)
        array[size] = null;

      System.arraycopy(a, 0, array, 0, size);
      return array;
    }*/

        @Override
        public E get(int index) {
            throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ListIterator<E> listIterator() {
            throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ListIterator listIterator(int size) {
            throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void add(int index, E element) {
            throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int lastIndexOf(Object o) {
            throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public <T> T[] toArray(T[] a) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
  }
}
