/* AbstractMap.java -- Abstract implementation of most of Map
   Copyright (C) 1998, 1999, 2000 Free Software Foundation, Inc.

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


// TO DO:
// comments
// test suite

package java.util;

public abstract class AbstractMap<K,V> implements Map<K,V>
{
  /**
   * Remove all entries from this Map. This default implementation calls
   * entrySet().clear().
   *
   * @throws UnsupportedOperationException
   * @specnote The JCL book claims that this implementation always throws 
   *           UnsupportedOperationException, while the online docs claim it
   *           calls entrySet().clear(). We take the later to be correct.
   */
  public void clear()
  {
    entrySet().clear();
  }

  public boolean containsKey(Object key)
  {
    Object k;
    Set es = entrySet();
    Iterator entries = es.iterator();
    int size = size();
    for (int pos = 0; pos < size; pos++)
      {
	k = ((Map.Entry) entries.next()).getKey();
	if (key == null ? k == null : key.equals(k))
	  return true;
      }
    return false;
  }

  public boolean containsValue(Object value)
  {
    Object v;
    Set es = entrySet();
    Iterator entries = es.iterator();
    int size = size();
    for (int pos = 0; pos < size; pos++)
      {
	v = ((Map.Entry) entries.next()).getValue();
	if (value == null ? v == null : value.equals(v))
	  return true;
      }
    return false;
  }

  public abstract Set entrySet();

  public boolean equals(Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof Map))
      return false;

    Map m = (Map) o;
    Set s = m.entrySet();
    Iterator itr = entrySet().iterator();
    int size = size();

    if (m.size() != size)
      return false;

    for (int pos = 0; pos < size; pos++)
      {
	if (!s.contains(itr.next()))
	  return false;
      }
    return true;
  }

  public V get(Object key)
  {
    Set s = entrySet();
    Iterator entries = s.iterator();
    int size = size();

    for (int pos = 0; pos < size; pos++)
      {
	Map.Entry<K,V> entry = (Map.Entry) entries.next();
	Object k = entry.getKey();
	if (key == null ? k == null : key.equals(k))
	  return entry.getValue();
      }

    return null;
  }

  public int hashCode()
  {
    int hashcode = 0;
    Iterator itr = entrySet().iterator();
    int size = size();
    for (int pos = 0; pos < size; pos++)
      {
	hashcode += itr.next().hashCode();
      }
    return hashcode;
  }

  public boolean isEmpty()
  {
    return size() == 0;
  }

  public Set keySet()
  {
    /*if (this.keySet == null)
      {
	this.keySet = new AbstractSet()
	{
	  public int size()
	  {
	    return AbstractMap.this.size();
	  }

	  public boolean contains(Object key)
	  {
	    return AbstractMap.this.containsKey(key);
	  }

	  public Iterator iterator()
	  {
	    return new Iterator()
	    {
	      Iterator map_iterator = AbstractMap.this.entrySet().iterator();

	      public boolean hasNext()
	      {
		return map_iterator.hasNext();
	      }

	      public Object next()
	      {
		return ((Map.Entry) map_iterator.next()).getKey();
	      }

	      public void remove()
	      {
		map_iterator.remove();
	      }
	    };
	  }
	};
      }*/

    return this.keySet;
  }

  public V put(K key, V value)
  {
    throw new UnsupportedOperationException();
  }

  public void putAll(Map<? extends K, ? extends V> m)
  {
    Map.Entry<? extends K, ? extends V> entry;
    Iterator entries = m.entrySet().iterator();
    int size = m.size();

    for (int pos = 0; pos < size; pos++)
      {
	entry = (Map.Entry) entries.next();
	put(entry.getKey(), entry.getValue());
      }
  }

  public V remove(Object key)
  {
    Iterator entries = entrySet().iterator();
    int size = size();

    for (int pos = 0; pos < size; pos++)
      {
	Map.Entry<K,V> entry = (Map.Entry) entries.next();
	Object k = entry.getKey();
	if (key == null ? k == null : key.equals(k))
	  {
	    V value = entry.getValue();
	    entries.remove();
	    return value;
	  }
      }

    return null;
  }

  public int size()
  {
    return entrySet().size();
  }

  public String toString()
  {
    Iterator entries = entrySet().iterator();
    int size = size();
    String r = "{";
    for (int pos = 0; pos < size; pos++)
      {
	r += entries.next();
	if (pos < size - 1)
	  r += ", ";
      }
    r += "}";
    return r;
  }

  public Collection<V> values()
  {
    /*if (this.valueCollection == null)
      {
	this.valueCollection = new AbstractCollection()
	{
	  public int size()
	  {
	    return AbstractMap.this.size();
	  }

	  public Iterator iterator()
	  {
	    return new Iterator()
	    {
	      Iterator map_iterator = AbstractMap.this.entrySet().iterator();

	      public boolean hasNext()
	      {
		return map_iterator.hasNext();
	      }

	      public Object next()
	      {
		return ((Map.Entry) map_iterator.next()).getValue();
	      }

	      public void remove()
	      {
		map_iterator.remove();
	      }
	    };
	  }
	};
      }*/

    return this.values;
  }

  transient Collection<V> values;
  transient Set<K>        keySet;
  
  /**
     * An Entry maintaining an immutable key and value.  This class
     * does not support method <tt>setValue</tt>.  This class may be
     * convenient in methods that return thread-safe snapshots of
     * key-value mappings.
     *
     * @since 1.6
     */
    public static class SimpleImmutableEntry<K,V>
        implements Entry<K,V>, java.io.Serializable
    {
        private static final long serialVersionUID = 7138329143949025153L;

        private final K key;
        private final V value;

        /**
         * Creates an entry representing a mapping from the specified
         * key to the specified value.
         *
         * @param key the key represented by this entry
         * @param value the value represented by this entry
         */
        public SimpleImmutableEntry(K key, V value) {
            this.key   = key;
            this.value = value;
        }

        /**
         * Creates an entry representing the same mapping as the
         * specified entry.
         *
         * @param entry the entry to copy
         */
        public SimpleImmutableEntry(Entry<? extends K, ? extends V> entry) {
            this.key   = entry.getKey();
            this.value = entry.getValue();
        }

        /**
         * Returns the key corresponding to this entry.
         *
         * @return the key corresponding to this entry
         */
        public K getKey() {
            return key;
        }

        /**
         * Returns the value corresponding to this entry.
         *
         * @return the value corresponding to this entry
         */
        public V getValue() {
            return value;
        }

        /**
         * Replaces the value corresponding to this entry with the specified
         * value (optional operation).  This implementation simply throws
         * <tt>UnsupportedOperationException</tt>, as this class implements
         * an <i>immutable</i> map entry.
         *
         * @param value new value to be stored in this entry
         * @return (Does not return)
         * @throws UnsupportedOperationException always
         */
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        /**
         * Compares the specified object with this entry for equality.
         * Returns {@code true} if the given object is also a map entry and
         * the two entries represent the same mapping.  More formally, two
         * entries {@code e1} and {@code e2} represent the same mapping
         * if<pre>
         *   (e1.getKey()==null ?
         *    e2.getKey()==null :
         *    e1.getKey().equals(e2.getKey()))
         *   &amp;&amp;
         *   (e1.getValue()==null ?
         *    e2.getValue()==null :
         *    e1.getValue().equals(e2.getValue()))</pre>
         * This ensures that the {@code equals} method works properly across
         * different implementations of the {@code Map.Entry} interface.
         *
         * @param o object to be compared for equality with this map entry
         * @return {@code true} if the specified object is equal to this map
         *         entry
         * @see    #hashCode
         */
        /*public boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;
            return eq(key, e.getKey()) && eq(value, e.getValue());
        }*/

        /**
         * Returns the hash code value for this map entry.  The hash code
         * of a map entry {@code e} is defined to be: <pre>
         *   (e.getKey()==null   ? 0 : e.getKey().hashCode()) ^
         *   (e.getValue()==null ? 0 : e.getValue().hashCode())</pre>
         * This ensures that {@code e1.equals(e2)} implies that
         * {@code e1.hashCode()==e2.hashCode()} for any two Entries
         * {@code e1} and {@code e2}, as required by the general
         * contract of {@link Object#hashCode}.
         *
         * @return the hash code value for this map entry
         * @see    #equals
         */
        public int hashCode() {
            return (key   == null ? 0 :   key.hashCode()) ^
                   (value == null ? 0 : value.hashCode());
        }

        /**
         * Returns a String representation of this map entry.  This
         * implementation returns the string representation of this
         * entry's key followed by the equals character ("<tt>=</tt>")
         * followed by the string representation of this entry's value.
         *
         * @return a String representation of this map entry
         */
        public String toString() {
            return key + "=" + value;
        }
    }
}
