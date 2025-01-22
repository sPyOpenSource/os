/* HashMap.java -- a class providing a basic hashtable data structure,
   mapping Object --> Object
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


package java.util;

import java.io.IOException;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Consumer;
import jx.zero.Debug;

/**
 * This class provides a hashtable-backed implementation of the
 * Map interface.  
 *
 * It uses a hash-bucket approach; that is, hash
 * collisions are handled by linking the new node off of the
 * pre-existing node (or list of nodes).  In this manner, techniques 
 * such as linear probing (which can casue primary clustering) and 
 * rehashing (which does not fit very well with Java's method of 
 * precomputing hash codes) are avoided.  
 *
 * Under ideal circumstances (no collisions, HashMap offers O(1) 
 * performance on most operations (<pre>containsValue()</pre> is,
 * of course, O(n)).  In the worst case (all keys map to the same 
 * hash code -- very unlikely), most operations are O(n).
 *
 * HashMap is part of the JDK1.2 Collections API.  It differs from 
 * Hashtable in that it accepts the null key and null values, and it
 * does not support "Enumeration views."
 *
 * @author         Jon Zeppieri
 * @version        $Revision: 1.1 $
 * @modified       $Id: HashMap.java,v 1.1 2002/04/30 09:10:32 golm Exp $
 */
public class HashMap<K,V> extends AbstractMap<K,V>
  implements Map<K,V>, Cloneable, Serializable
{
  // STATIC (CLASS) VARIABLES ------------------------------------------

  /** 
   * the default capacity for an instance of HashMap -- I think this
   * is low, and perhaps it shoudl be raised; Sun's documentation mildly
   * suggests that this (11) is the correct value, though
   */
  private static final int DEFAULT_CAPACITY = 11;

  /** the default load factor of a HashMap */
  private static final float DEFAULT_LOAD_FACTOR = 0.75F;

  /** used internally to represent the null key */
  private static final HashMap.Null NULL_KEY = new HashMap.Null();

  /** used internally to parameterize the creation of set/collection views */
  private static final int KEYS = 0;

  /** used internally to parameterize the creation of set/collection views */
  private static final int VALUES = 1;

  /** used internally to parameterize the creation of set/collection views */
  private static final int ENTRIES = 2;
  
  /**
     * The bin count threshold for untreeifying a (split) bin during a
     * resize operation. Should be less than TREEIFY_THRESHOLD, and at
     * most 6 to mesh with shrinkage detection under removal.
     */
    static final int UNTREEIFY_THRESHOLD = 6;

  private static final long serialVersionUID = 362498820763181265L;

  // INSTANCE VARIABLES -------------------------------------------------

  /** the capacity of this HashMap:  denotes the size of the bucket array */
  protected int capacity;

  /** the size of this HashMap:  denotes the number of key-value pairs */
  protected int size;

  /** the load factor of this HashMap:  used in computing the threshold 
   * @serial
   */
  protected float loadFactor;

  /* the rounded product of the capacity and the load factor; when the number of
   * elements exceeds the threshold, the HashMap calls <pre>rehash()</pre>
   * @serial
   */
  private int threshold;

  /** 
   * this data structure contains the actual key-value mappings; a
   * <pre>BucketList</pre> is a lightweight linked list of "Buckets",
   * which, in turn, are linked nodes containing a key-value mapping 
   * and a reference to the "next" Bucket in the list
   */
  private Bucket<K,V>[] buckets = new Bucket[11];

  /** 
   * counts the number of modifications this HashMap has undergone; used by Iterators
   * to know when to throw ConcurrentModificationExceptions (idea ripped-off from
   * Stuart Ballard's AbstractList implementation) 
   */
  protected int modCount;


  // CONSTRUCTORS ---------------------------------------------------------

  /**
   * construct a new HashMap with the default capacity and the default
   * load factor
   */
  public HashMap()
  {
    init(DEFAULT_CAPACITY);//, DEFAULT_LOAD_FACTOR);
  }

  /**
   * construct a new HashMap with a specific inital capacity and load factor
   *
   * @param   initialCapacity     the initial capacity of this HashMap (>=0)
   * @param   initialLoadFactor   the load factor of this HashMap 
   *                              (a misnomer, really, since the load factor of
   *                              a HashMap does not change)
   * 
   * @throws   IllegalArgumentException    if (initialCapacity < 0) ||
   *                                          (initialLoadFactor > 1.0) ||
   *                                          (initialLoadFactor <= 0.0)
   */
  public HashMap(int initialCapacity, float initialLoadFactor)
    throws IllegalArgumentException
  {
    if (initialCapacity < 0 || initialLoadFactor <= 0
	|| initialLoadFactor > 1)
      throw new IllegalArgumentException();
    else
      init(initialCapacity);//, initialLoadFactor);
  }

  /**
   * construct a new HashMap with a specific inital capacity 
   *
   * @param   initialCapacity     the initial capacity of this HashMap (>=0)
   *
   * @throws   IllegalArgumentException    if (initialCapacity < 0)
   */
  public HashMap(int initialCapacity) throws IllegalArgumentException
  {
    if (initialCapacity < 0)
      throw new IllegalArgumentException();
    else
      init(initialCapacity);//, DEFAULT_LOAD_FACTOR);
  }

  /**
   * construct a new HashMap from the given Map
   * 
   * every element in Map t will be put into this new HashMap
   *
   * @param     t        a Map whose key / value pairs will be put into
   *                     the new HashMap.  <b>NOTE: key / value pairs
   *                     are not cloned in this constructor</b>
   */
  public HashMap(Map t)
  {
    int mapSize = t.size() * 2;
    init(((mapSize > DEFAULT_CAPACITY) ? mapSize : DEFAULT_CAPACITY));//, DEFAULT_LOAD_FACTOR);
    putAll(t);
  }
  
  final class Values extends AbstractCollection<V> {
        public final int size()                 { return size; }
        public final void clear()               { HashMap.this.clear(); }
        public final Iterator<V> iterator()     { 
            return null;//new ValueIterator(); 
        }
        public final boolean contains(Object o) { return containsValue(o); }
        public final Spliterator<V> spliterator() {
            return null;//new ValueSpliterator<>(HashMap.this, 0, -1, 0, 0);
        }
        public final void forEach(Consumer<? super V> action) {
            Node<K,V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (int i = 0; i < tab.length; ++i) {
                    for (Node<K,V> e = tab[i]; e != null; e = e.next)
                        action.accept(e.value);
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
    }
  
   /**
     * The table, initialized on first use, and resized as
     * necessary. When allocated, length is always a power of two.
     * (We also tolerate length zero in some operations to allow
     * bootstrapping mechanics that are currently not needed.)
     */
    transient Node<K,V>[] table;

    /**
     * Holds cached entrySet(). Note that AbstractMap fields are used
     * for keySet() and values().
     */
    transient Set<Map.Entry<K,V>> entrySet;
    
/**
     * Reset to initial default state.  Called by clone and readObject.
     */
    void reinitialize() {
        table = null;
        entrySet = null;
        keySet = null;
        values = null;
        modCount = 0;
        threshold = 0;
        size = 0;
    }
    
    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;
    
    /**
     * Returns a power of two size for the given target capacity.
     */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
    
    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
    
     /**
     * Initializes or doubles table size.  If null, allocates in
     * accord with initial capacity target held in field threshold.
     * Otherwise, because we are using power-of-two expansion, the
     * elements from each bin must either stay at same index, or move
     * with a power of two offset in the new table.
     *
     * @return the table
     */
    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        }
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
            Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode)
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }
    
      /**
     * The bin count threshold for using a tree rather than list for a
     * bin.  Bins are converted to trees when adding an element to a
     * bin with at least this many nodes. The value must be greater
     * than 2 and should be at least 8 to mesh with assumptions in
     * tree removal about conversion back to plain bins upon
     * shrinkage.
     */
    static final int TREEIFY_THRESHOLD = 8;
    
    /**
     * Implements Map.put and related methods
     *
     * @param hash hash for key
     * @param key the key
     * @param value the value to put
     * @param onlyIfAbsent if true, don't change existing value
     * @param evict if false, the table is in creation mode.
     * @return previous value, or null if none
     */
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = null;//newNode(hash, key, value, null);
        else {
            Node<K,V> e; K k;
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = null;//newNode(hash, key, value, null);
                        /*if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);*/
                        break;
                    }
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                //afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold)
            resize();
       // afterNodeInsertion(evict);
        return null;
    }

    /**
     * Implements Map.putAll and Map constructor
     *
     * @param m the map
     * @param evict false when initially constructing this map, else
     * true (relayed to method afterNodeInsertion).
     */
    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
        int s = m.size();
        if (s > 0) {
            if (table == null) { // pre-size
                float ft = ((float)s / loadFactor) + 1.0F;
                int t = ((ft < (float)MAXIMUM_CAPACITY) ?
                         (int)ft : MAXIMUM_CAPACITY);
                if (t > threshold)
                    threshold = tableSizeFor(t);
            }
            else if (s > threshold)
                resize();
            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                putVal(hash(key), key, value, false, evict);
            }
        }
    }
    
    /**
     * Implements Map.get and related methods
     *
     * @param hash hash for key
     * @param key the key
     * @return the node, or null if none
     */
    final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (first = tab[(n - 1) & hash]) != null) {
            if (first.hash == hash && // always check first node
                ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                if (first instanceof TreeNode)
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }
    
    /**
     * Implements Map.remove and related methods
     *
     * @param hash hash for key
     * @param key the key
     * @param value the value to match if matchValue, else ignored
     * @param matchValue if true only remove if value is equal
     * @param movable if false do not move other nodes while removing
     * @return the node, or null if none
     */
    final Node<K,V> removeNode(int hash, Object key, Object value,
                               boolean matchValue, boolean movable) {
        Node<K,V>[] tab; Node<K,V> p; int n, index;
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (p = tab[index = (n - 1) & hash]) != null) {
            Node<K,V> node = null, e; K k; V v;
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                node = p;
            else if ((e = p.next) != null) {
                if (p instanceof TreeNode)
                    node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
                else {
                    do {
                        if (e.hash == hash &&
                            ((k = e.key) == key ||
                             (key != null && key.equals(k)))) {
                            node = e;
                            break;
                        }
                        p = e;
                    } while ((e = e.next) != null);
                }
            }
            if (node != null && (!matchValue || (v = node.value) == value ||
                                 (value != null && value.equals(v)))) {
                if (node instanceof TreeNode)
                    ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
                else if (node == p)
                    tab[index] = node.next;
                else
                    p.next = node.next;
                ++modCount;
                --size;
                //afterNodeRemoval(node);
                return node;
            }
        }
        return null;
    }
    
/**
     * Basic hash bin node, used for most entries.  (See below for
     * TreeNode subclass, and in LinkedHashMap for its Entry subclass.)
     */
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public final K getKey()        { return key; }
        @Override
        public final V getValue()      { return value; }
        @Override
        public final String toString() { return key + "=" + value; }

        @Override
        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override
        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) &&
                    Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }
  // PUBLIC METHODS ---------------------------------------------------------

  /** @return the number of kay-value mappings currently in this Map */
  @Override
  public int size()
  {
    return size;
  }

  /** @return true if there are no key-value mappings currently in this Map */
  @Override
  public boolean isEmpty()
  {
    return size == 0;
  }

  /** empties this HashMap of all elements */
  @Override
  public void clear()
  {
    size = 0;
    modCount++;
    buckets = new Bucket[capacity];
  }

  /** 
   * returns a shallow clone of this HashMap (i.e. the Map itself is cloned, but
   * its contents are not)
     * @return 
   */
  @Override
  public Object clone()
  {
    Map.Entry entry;
    Iterator it = entrySet().iterator();
    HashMap clone = new HashMap(capacity, loadFactor);
    while (it.hasNext())
      {
	entry = (Map.Entry) it.next();
	clone.internalPut(entry.getKey(), entry.getValue());
      }
    return clone;
  }

  /** returns a "set view" of this HashMap's keys
     * @return  */
  @Override
  public Set keySet()
  {
    return new HashMapSet(KEYS);
  }

  /** returns a "set view" of this HashMap's entries
     * @return  */
  @Override
  public Set entrySet()
  {
    return new HashMapSet(ENTRIES);
  }

 /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own <tt>remove</tt> operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not
     * support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a view of the values contained in this map
     */
  @Override
    public Collection<V> values() {
        Collection<V> vs = values;
        if (vs == null) {
            vs = new Values();
            values = vs;
        }
        return vs;
    }
    
    /**
     * Computes key.hashCode() and spreads (XORs) higher bits of hash
     * to lower.  Because the table uses power-of-two masking, sets of
     * hashes that vary only in bits above the current mask will
     * always collide. (Among known examples are sets of Float keys
     * holding consecutive whole numbers in small tables.)  So we
     * apply a transform that spreads the impact of higher bits
     * downward. There is a tradeoff between speed, utility, and
     * quality of bit-spreading. Because many common sets of hashes
     * are already reasonably distributed (so don't benefit from
     * spreading), and because we use trees to handle large sets of
     * collisions in bins, we just XOR some shifted bits in the
     * cheapest possible way to reduce systematic lossage, as well as
     * to incorporate impact of the highest bits that would otherwise
     * never be used in index calculations because of table bounds.
     */
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
    
/**
     * Entry for Tree bins. Extends LinkedHashMap.Entry (which in turn
     * extends Node) so can be used as extension of either regular or
     * linked node.
     */
    static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
        TreeNode<K,V> parent;  // red-black tree links
        TreeNode<K,V> left;
        TreeNode<K,V> right;
        TreeNode<K,V> prev;    // needed to unlink next upon deletion
        boolean red;
        TreeNode(int hash, K key, V val, Node<K,V> next) {
            super(hash, key, val, next);
        }

        /**
         * Returns root of tree containing this node.
         */
        final TreeNode<K,V> root() {
            for (TreeNode<K,V> r = this, p;;) {
                if ((p = r.parent) == null)
                    return r;
                r = p;
            }
        }

        /**
         * Ensures that the given root is the first node of its bin.
         */
        static <K,V> void moveRootToFront(Node<K,V>[] tab, TreeNode<K,V> root) {
            int n;
            if (root != null && tab != null && (n = tab.length) > 0) {
                int index = (n - 1) & root.hash;
                TreeNode<K,V> first = (TreeNode<K,V>)tab[index];
                if (root != first) {
                    Node<K,V> rn;
                    tab[index] = root;
                    TreeNode<K,V> rp = root.prev;
                    if ((rn = root.next) != null)
                        ((TreeNode<K,V>)rn).prev = rp;
                    if (rp != null)
                        rp.next = rn;
                    if (first != null)
                        first.prev = root;
                    root.next = first;
                    root.prev = null;
                }
                assert checkInvariants(root);
            }
        }

        /**
         * Finds the node starting at root p with the given hash and key.
         * The kc argument caches comparableClassFor(key) upon first use
         * comparing keys.
         */
        final TreeNode<K,V> find(int h, Object k, Class<?> kc) {
            TreeNode<K,V> p = this;
            do {
                int ph, dir; K pk;
                TreeNode<K,V> pl = p.left, pr = p.right, q;
                if ((ph = p.hash) > h)
                    p = pl;
                else if (ph < h)
                    p = pr;
                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                    return p;
                else if (pl == null)
                    p = pr;
                else if (pr == null)
                    p = pl;
                /*else if ((kc != null 
                        ||(kc = comparableClassFor(k)) != null
                        ) 
                        && (dir = compareComparables(kc, k, pk)) != 0
                        )
                    p = (dir < 0) ? pl : pr;*/
                else if ((q = pr.find(h, k, kc)) != null)
                    return q;
                else
                    p = pl;
            } while (p != null);
            return null;
        }

        /**
         * Calls find for root node.
         */
        final TreeNode<K,V> getTreeNode(int h, Object k) {
            return ((parent != null) ? root() : this).find(h, k, null);
        }

        /**
         * Tie-breaking utility for ordering insertions when equal
         * hashCodes and non-comparable. We don't require a total
         * order, just a consistent insertion rule to maintain
         * equivalence across rebalancings. Tie-breaking further than
         * necessary simplifies testing a bit.
         */
        static int tieBreakOrder(Object a, Object b) {
            int d;
            if (a == null || b == null ||
                (d = a.getClass().getName().
                 compareTo(b.getClass().getName())) == 0)
                d = (System.identityHashCode(a) <= System.identityHashCode(b) ?
                     -1 : 1);
            return d;
        }

        /**
         * Forms tree of the nodes linked from this node.
         * @return root of tree
         */
        final void treeify(Node<K,V>[] tab) {
            TreeNode<K,V> root = null;
            for (TreeNode<K,V> x = this, next; x != null; x = next) {
                next = (TreeNode<K,V>)x.next;
                x.left = x.right = null;
                if (root == null) {
                    x.parent = null;
                    x.red = false;
                    root = x;
                }
                else {
                    K k = x.key;
                    int h = x.hash;
                    Class<?> kc = null;
                    for (TreeNode<K,V> p = root;;) {
                        int dir, ph;
                        K pk = p.key;
                        if ((ph = p.hash) > h)
                            dir = -1;
                        else if (ph < h)
                            dir = 1;
                        /*else if ((kc == null &&
                                  (kc = comparableClassFor(k)) == null) ||
                                 (dir = compareComparables(kc, k, pk)) == 0)
                            dir = tieBreakOrder(k, pk);

                        TreeNode<K,V> xp = p;
                        if ((p = (dir <= 0) ? p.left : p.right) == null) {
                            x.parent = xp;
                            if (dir <= 0)
                                xp.left = x;
                            else
                                xp.right = x;
                            root = balanceInsertion(root, x);
                            break;
                        }*/
                    }
                }
            }
            moveRootToFront(tab, root);
        }

        /**
         * Returns a list of non-TreeNodes replacing those linked from
         * this node.
         */
        final Node<K,V> untreeify(HashMap<K,V> map) {
            Node<K,V> hd = null, tl = null;
            for (Node<K,V> q = this; q != null; q = q.next) {
                Node<K,V> p = null;//map.replacementNode(q, null);
                if (tl == null)
                    hd = p;
                else
                    tl.next = p;
                tl = p;
            }
            return hd;
        }
    
        /**
         * Tree version of putVal.
         */
        final TreeNode<K,V> putTreeVal(HashMap<K,V> map, Node<K,V>[] tab,
                                       int h, K k, V v) {
            Class<?> kc = null;
            boolean searched = false;
            TreeNode<K,V> root = (parent != null) ? root() : this;
            for (TreeNode<K,V> p = root;;) {
                int dir, ph; K pk;
                if ((ph = p.hash) > h)
                    dir = -1;
                else if (ph < h)
                    dir = 1;
                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                    return p;
                /*else if ((kc == null &&
                          (kc = comparableClassFor(k)) == null) ||
                         (dir = compareComparables(kc, k, pk)) == 0) {
                    if (!searched) {
                        TreeNode<K,V> q, ch;
                        searched = true;
                        if (((ch = p.left) != null &&
                             (q = ch.find(h, k, kc)) != null) ||
                            ((ch = p.right) != null &&
                             (q = ch.find(h, k, kc)) != null))
                            return q;
                    }
                    dir = tieBreakOrder(k, pk);
                }

                TreeNode<K,V> xp = p;
                if ((p = (dir <= 0) ? p.left : p.right) == null) {
                    Node<K,V> xpn = xp.next;
                    TreeNode<K,V> x = null;//map.newTreeNode(h, k, v, xpn);
                    if (dir <= 0)
                        xp.left = x;
                    else
                        xp.right = x;
                    xp.next = x;
                    x.parent = x.prev = xp;
                    if (xpn != null)
                        ((TreeNode<K,V>)xpn).prev = x;
                    moveRootToFront(tab, balanceInsertion(root, x));
                    return null;
                }*/
            }
        }

        /**
         * Removes the given node, that must be present before this call.
         * This is messier than typical red-black deletion code because we
         * cannot swap the contents of an interior node with a leaf
         * successor that is pinned by "next" pointers that are accessible
         * independently during traversal. So instead we swap the tree
         * linkages. If the current tree appears to have too few nodes,
         * the bin is converted back to a plain bin. (The test triggers
         * somewhere between 2 and 6 nodes, depending on tree structure).
         */
        final void removeTreeNode(HashMap<K,V> map, Node<K,V>[] tab,
                                  boolean movable) {
            int n;
            if (tab == null || (n = tab.length) == 0)
                return;
            int index = (n - 1) & hash;
            TreeNode<K,V> first = (TreeNode<K,V>)tab[index], root = first, rl;
            TreeNode<K,V> succ = (TreeNode<K,V>)next, pred = prev;
            if (pred == null)
                tab[index] = first = succ;
            else
                pred.next = succ;
            if (succ != null)
                succ.prev = pred;
            if (first == null)
                return;
            if (root.parent != null)
                root = root.root();
            if (root == null || root.right == null ||
                (rl = root.left) == null || rl.left == null) {
                tab[index] = first.untreeify(map);  // too small
                return;
            }
            TreeNode<K,V> p = this, pl = left, pr = right, replacement;
            if (pl != null && pr != null) {
                TreeNode<K,V> s = pr, sl;
                while ((sl = s.left) != null) // find successor
                    s = sl;
                boolean c = s.red; s.red = p.red; p.red = c; // swap colors
                TreeNode<K,V> sr = s.right;
                TreeNode<K,V> pp = p.parent;
                if (s == pr) { // p was s's direct parent
                    p.parent = s;
                    s.right = p;
                }
                else {
                    TreeNode<K,V> sp = s.parent;
                    if ((p.parent = sp) != null) {
                        if (s == sp.left)
                            sp.left = p;
                        else
                            sp.right = p;
                    }
                    if ((s.right = pr) != null)
                        pr.parent = s;
                }
                p.left = null;
                if ((p.right = sr) != null)
                    sr.parent = p;
                if ((s.left = pl) != null)
                    pl.parent = s;
                if ((s.parent = pp) == null)
                    root = s;
                else if (p == pp.left)
                    pp.left = s;
                else
                    pp.right = s;
                if (sr != null)
                    replacement = sr;
                else
                    replacement = p;
            }
            else if (pl != null)
                replacement = pl;
            else if (pr != null)
                replacement = pr;
            else
                replacement = p;
            if (replacement != p) {
                TreeNode<K,V> pp = replacement.parent = p.parent;
                if (pp == null)
                    root = replacement;
                else if (p == pp.left)
                    pp.left = replacement;
                else
                    pp.right = replacement;
                p.left = p.right = p.parent = null;
            }

            TreeNode<K,V> r = p.red ? root : balanceDeletion(root, replacement);

            if (replacement == p) {  // detach
                TreeNode<K,V> pp = p.parent;
                p.parent = null;
                if (pp != null) {
                    if (p == pp.left)
                        pp.left = null;
                    else if (p == pp.right)
                        pp.right = null;
                }
            }
            if (movable)
                moveRootToFront(tab, r);
        }

        /**
         * Splits nodes in a tree bin into lower and upper tree bins,
         * or untreeifies if now too small. Called only from resize;
         * see above discussion about split bits and indices.
         *
         * @param map the map
         * @param tab the table for recording bin heads
         * @param index the index of the table being split
         * @param bit the bit of hash to split on
         */
        final void split(HashMap<K,V> map, Node<K,V>[] tab, int index, int bit) {
            TreeNode<K,V> b = this;
            // Relink into lo and hi lists, preserving order
            TreeNode<K,V> loHead = null, loTail = null;
            TreeNode<K,V> hiHead = null, hiTail = null;
            int lc = 0, hc = 0;
            for (TreeNode<K,V> e = b, next; e != null; e = next) {
                next = (TreeNode<K,V>)e.next;
                e.next = null;
                if ((e.hash & bit) == 0) {
                    if ((e.prev = loTail) == null)
                        loHead = e;
                    else
                        loTail.next = e;
                    loTail = e;
                    ++lc;
                }
                else {
                    if ((e.prev = hiTail) == null)
                        hiHead = e;
                    else
                        hiTail.next = e;
                    hiTail = e;
                    ++hc;
                }
            }

            if (loHead != null) {
                if (lc <= UNTREEIFY_THRESHOLD)
                    tab[index] = loHead.untreeify(map);
                else {
                    tab[index] = loHead;
                    if (hiHead != null) // (else is already treeified)
                        loHead.treeify(tab);
                }
            }
            if (hiHead != null) {
                if (hc <= UNTREEIFY_THRESHOLD)
                    tab[index + bit] = hiHead.untreeify(map);
                else {
                    tab[index + bit] = hiHead;
                    if (loHead != null)
                        hiHead.treeify(tab);
                }
            }
        }

        /* ------------------------------------------------------------ */
        // Red-black tree methods, all adapted from CLR

        static <K,V> TreeNode<K,V> rotateLeft(TreeNode<K,V> root,
                                              TreeNode<K,V> p) {
            TreeNode<K,V> r, pp, rl;
            if (p != null && (r = p.right) != null) {
                if ((rl = p.right = r.left) != null)
                    rl.parent = p;
                if ((pp = r.parent = p.parent) == null)
                    (root = r).red = false;
                else if (pp.left == p)
                    pp.left = r;
                else
                    pp.right = r;
                r.left = p;
                p.parent = r;
            }
            return root;
        }

        static <K,V> TreeNode<K,V> rotateRight(TreeNode<K,V> root,
                                               TreeNode<K,V> p) {
            TreeNode<K,V> l, pp, lr;
            if (p != null && (l = p.left) != null) {
                if ((lr = p.left = l.right) != null)
                    lr.parent = p;
                if ((pp = l.parent = p.parent) == null)
                    (root = l).red = false;
                else if (pp.right == p)
                    pp.right = l;
                else
                    pp.left = l;
                l.right = p;
                p.parent = l;
            }
            return root;
        }

        static <K,V> TreeNode<K,V> balanceInsertion(TreeNode<K,V> root,
                                                    TreeNode<K,V> x) {
            x.red = true;
            for (TreeNode<K,V> xp, xpp, xppl, xppr;;) {
                if ((xp = x.parent) == null) {
                    x.red = false;
                    return x;
                }
                else if (!xp.red || (xpp = xp.parent) == null)
                    return root;
                if (xp == (xppl = xpp.left)) {
                    if ((xppr = xpp.right) != null && xppr.red) {
                        xppr.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x = xpp;
                    }
                    else {
                        if (x == xp.right) {
                            root = rotateLeft(root, x = xp);
                            xpp = (xp = x.parent) == null ? null : xp.parent;
                        }
                        if (xp != null) {
                            xp.red = false;
                            if (xpp != null) {
                                xpp.red = true;
                                root = rotateRight(root, xpp);
                            }
                        }
                    }
                }
                else {
                    if (xppl != null && xppl.red) {
                        xppl.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x = xpp;
                    }
                    else {
                        if (x == xp.left) {
                            root = rotateRight(root, x = xp);
                            xpp = (xp = x.parent) == null ? null : xp.parent;
                        }
                        if (xp != null) {
                            xp.red = false;
                            if (xpp != null) {
                                xpp.red = true;
                                root = rotateLeft(root, xpp);
                            }
                        }
                    }
                }
            }
        }

        static <K,V> TreeNode<K,V> balanceDeletion(TreeNode<K,V> root,
                                                   TreeNode<K,V> x) {
            for (TreeNode<K,V> xp, xpl, xpr;;)  {
                if (x == null || x == root)
                    return root;
                else if ((xp = x.parent) == null) {
                    x.red = false;
                    return x;
                }
                else if (x.red) {
                    x.red = false;
                    return root;
                }
                else if ((xpl = xp.left) == x) {
                    if ((xpr = xp.right) != null && xpr.red) {
                        xpr.red = false;
                        xp.red = true;
                        root = rotateLeft(root, xp);
                        xpr = (xp = x.parent) == null ? null : xp.right;
                    }
                    if (xpr == null)
                        x = xp;
                    else {
                        TreeNode<K,V> sl = xpr.left, sr = xpr.right;
                        if ((sr == null || !sr.red) &&
                            (sl == null || !sl.red)) {
                            xpr.red = true;
                            x = xp;
                        }
                        else {
                            if (sr == null || !sr.red) {
                                if (sl != null)
                                    sl.red = false;
                                xpr.red = true;
                                root = rotateRight(root, xpr);
                                xpr = (xp = x.parent) == null ?
                                    null : xp.right;
                            }
                            if (xpr != null) {
                                xpr.red = (xp == null) ? false : xp.red;
                                if ((sr = xpr.right) != null)
                                    sr.red = false;
                            }
                            if (xp != null) {
                                xp.red = false;
                                root = rotateLeft(root, xp);
                            }
                            x = root;
                        }
                    }
                }
                else { // symmetric
                    if (xpl != null && xpl.red) {
                        xpl.red = false;
                        xp.red = true;
                        root = rotateRight(root, xp);
                        xpl = (xp = x.parent) == null ? null : xp.left;
                    }
                    if (xpl == null)
                        x = xp;
                    else {
                        TreeNode<K,V> sl = xpl.left, sr = xpl.right;
                        if ((sl == null || !sl.red) &&
                            (sr == null || !sr.red)) {
                            xpl.red = true;
                            x = xp;
                        }
                        else {
                            if (sl == null || !sl.red) {
                                if (sr != null)
                                    sr.red = false;
                                xpl.red = true;
                                root = rotateLeft(root, xpl);
                                xpl = (xp = x.parent) == null ?
                                    null : xp.left;
                            }
                            if (xpl != null) {
                                xpl.red = (xp == null) ? false : xp.red;
                                if ((sl = xpl.left) != null)
                                    sl.red = false;
                            }
                            if (xp != null) {
                                xp.red = false;
                                root = rotateRight(root, xp);
                            }
                            x = root;
                        }
                    }
                }
            }
        }

        /**
         * Recursive invariant check
         */
        static <K,V> boolean checkInvariants(TreeNode<K,V> t) {
            TreeNode<K,V> tp = t.parent, tl = t.left, tr = t.right,
                tb = t.prev, tn = (TreeNode<K,V>)t.next;
            if (tb != null && tb.next != t)
                return false;
            if (tn != null && tn.prev != t)
                return false;
            if (tp != null && t != tp.left && t != tp.right)
                return false;
            if (tl != null && (tl.parent != t || tl.hash > t.hash))
                return false;
            if (tr != null && (tr.parent != t || tr.hash < t.hash))
                return false;
            if (t.red && tl != null && tl.red && tr != null && tr.red)
                return false;
            if (tl != null && !checkInvariants(tl))
                return false;
            return !(tr != null && !checkInvariants(tr));
        }
    }
        
  /** 
   * returns true if the supplied object equals (<pre>equals()</pre>) a key
   * in this HashMap 
   *
   * @param       key        the key to search for in this HashMap
     * @return 
   */
  @Override
  public boolean containsKey(Object key)
  {
    return (internalGet(key) != null);
  }

  /**
   * returns true if this HashMap contains a value <pre>o</pre>, such that
   * <pre>o.equals(value)</pre>.
   *
   * @param      value       the value to search for in this Hashtable
     * @return 
   */
  @Override
  public boolean containsValue(Object value)
  {
    int i;
    Bucket list;

    for (i = 0; i < capacity; i++)
      {
	list = buckets[i];
	if (list != null && list.containsValue(value))
	  return true;
      }
    return false;
  }

  /*
   * return the value in this Hashtable associated with the supplied key, or <pre>null</pre>
   * if the key maps to nothing
   *
   * @param     key      the key for which to fetch an associated value
   */
  @Override
  public V get(Object key)
  {
    Map.Entry<K,V> oResult = internalGet(key);
    return (oResult == null) ? null : oResult.getValue();
  }

  /**
   * puts the supplied value into the Map, mapped by the supplied key
   *
   * @param       key        the HashMap key used to locate the value
   * @param       value      the value to be stored in the HashMap
     * @return 
   */
  @Override
  public V put(K key, V value)
  {
    return internalPut(key, value);
  }

  /**
   * removes from the HashMap and returns the value which is mapped by the 
   * supplied key; if the key maps to nothing, then the HashMap remains unchanged,
   * and <pre>null</pre> is returned
   *
   * @param    key     the key used to locate the value to remove from the HashMap
     * @return 
   */
  @Override
  public V remove(Object key)
  {
    Bucket<K,V> list;
    int index;
    V result = null;
    if (size > 0)
      {
	index = hash(((key == null) ? NULL_KEY : key));
	list = buckets[index];
	if (list != null)
	  {
	    result = list.removeByKey(key);
	    if (result != null)
	      {
		size--;
		modCount++;
		if (list.first == null)
		  buckets[index] = null;
	      }
	  }
      }
    return result;
  }


  // PRIVATE METHODS -----------------------------------------------------------

    /** 
    * puts the given key-value pair into this HashMap; a private method is used
    * because it is called by the rehash() method as well as the put() method,
    * and if a subclass overrides put(), then rehash would do funky things
    * if it called put()
    *
    * @param       key        the HashMap key used to locate the value
    * @param       value      the value to be stored in the HashMap
    */
    private V internalPut(K key, V value)
    {
        HashMapEntry<K,V> entry;
        Bucket<K,V> list;
        int hashIndex;
        V oResult;
        Debug.out.println("key");
        K oRealKey = key;//((key == null) ? NULL_KEY : key);

        entry = new HashMapEntry<>(oRealKey, value);
        hashIndex = hash(oRealKey);
        list = buckets[hashIndex];
        if (list == null)
        {
            list = new Bucket();
            buckets[hashIndex] = list;
        }
        oResult = list.add(entry);
        
        if (oResult == null)
        {
            modCount++;
            if (size++ == threshold)
                rehash();
            return null;
        } else {
            // SEH: if key already exists, we don't rehash & we don't update the modCount
            // because it is not a "structural" modification
            return oResult;
        }
    }

  /** 
   * a private method, called by all of the constructors to initialize a new HashMap
   *
   * @param   initialCapacity     the initial capacity of this HashMap (>=0)
   * @param   initialLoadFactor   the load factor of this HashMap 
   *                              (a misnomer, really, since the load factor of
   *                              a HashMap does not change)
   */
  private void init(int initialCapacity)//, float initialLoadFactor)
  {
    size = 0;
    modCount = 0;
    capacity = initialCapacity;
    loadFactor = 0.75f;//initialLoadFactor;
    threshold = 8;//(int) (capacity * loadFactor);
    buckets = new Bucket[capacity];
  }

  /** private -- simply hashes a non-null Object to its array index */
  /*private int hash(Object key)
  {
    return Math.abs(key.hashCode() % capacity);
  }*/

  /** 
   * increases the size of the HashMap and rehashes all keys to new array indices;
   * this is called when the addition of a new value would cause size() > threshold
   */
  private void rehash()
  {
    int i;
    Bucket[]data = buckets;
    Bucket.Node<K,V> node;

    modCount++;
    capacity = (capacity * 2) + 1;
    size = 0;
    threshold = 11;//(int) (capacity * loadFactor);
    buckets = new Bucket[capacity];
    for (i = 0; i < data.length; i++)
      {
	if (data[i] != null)
	  {
	    node = data[i].first;
	    while (node != null)
	      {
		internalPut(node.getKey(), node.getValue());
		node = node.next;
	      }
	  }
      }
  }

    /** 
    * a private method which does the "dirty work" (or some of it anyway) of fetching a value
    * with a key
    *
    *  @param     key      the key for which to fetch an associated value
    */
    private Map.Entry<K,V> internalGet(Object key)
    {
        Bucket<K,V> list;
        if (size == 0)
        {
            return null;
        }
        else
        {
            list = buckets[hash(((key == null) ? NULL_KEY : key))];
            return (list == null) ? null : list.getEntryByKey(key);
        }
    }

  /**
   * a private method used by inner class HashMapSet to implement its own 
   * <pre>contains(Map.Entry)</pre> method; returns true if the supplied
   * key / value pair is found in this HashMap (again, using <pre>equals()</pre>,
   * rather than <pre>==</pre>)
   *
   * @param      entry      a Map.Entry to match against key / value pairs in 
   *                        this HashMap
   */
  private boolean containsEntry(Map.Entry entry)
  {
    Map.Entry oInternalEntry;
    if (entry == null)
      {
	return false;
      }
    else
      {
	oInternalEntry = internalGet(entry.getKey());
	return (oInternalEntry != null && oInternalEntry.equals(entry));
      }
  }

  /**
   * Serializes this object to the given stream.
   * @serialdata the <i>capacity</i>(int) that is the length of the
   * bucket array, the <i>size</i>(int) of the hash map are emitted
   * first.  They are followed by size entries, each consisting of
   * a key (Object) and a value (Object).
   */
  private void writeObject(ObjectOutputStream s) throws IOException
  {
    // the fields
    s.defaultWriteObject();

    s.writeInt(capacity);
    s.writeInt(size);
    Iterator it = entrySet().iterator();
    while (it.hasNext())
      {
	Map.Entry oEntry = (Map.Entry) it.next();
	s.writeObject(oEntry.getKey());
	s.writeObject(oEntry.getValue());
      }
  }

  /**
   * Deserializes this object from the given stream.
   * @serialdata the <i>capacity</i>(int) that is the length of the
   * bucket array, the <i>size</i>(int) of the hash map are emitted
   * first.  They are followed by size entries, each consisting of
   * a key (Object) and a value (Object).
   */
  private void readObject(ObjectInputStream s)
    throws IOException, ClassNotFoundException
  {
    // the fields
    s.defaultReadObject();

    capacity = s.readInt();
    int iLen = s.readInt();
    size = 0;
    modCount = 0;
    buckets = new Bucket[capacity];

    for (int i = 0; i < iLen; i++)
      {
	K oKey = null;//s.readObject();
	V oValue = null;//s.readObject();
	internalPut(oKey, oValue);
      }
  }

  // INNER CLASSES -------------------------------------------------------------
  // ---------------------------------------------------------------------------

  /**
   * an inner class providing a Set view of a HashMap; this implementation is 
   * parameterized to view either a Set of keys or a Set of Map.Entry objects
   *
   * Note:  a lot of these methods are implemented by AbstractSet, and would work 
   * just fine without any meddling, but far greater efficiency can be gained by
   * overriding a number of them.  And so I did.
   *
   * @author      Jon Zeppieri
   * @version     $Revision: 1.1 $
   * @modified    $Id: HashMap.java,v 1.1 2002/04/30 09:10:32 golm Exp $
   */
  private class HashMapSet extends AbstractSet implements Set
  {
    /** the type of this Set view:  KEYS or ENTRIES */
    private final int setType;

    /** construct a new HashtableSet with the supplied view type */
    HashMapSet(int type)
    {
      setType = type;
    }

    /**
     * adding an element is unsupported; this method simply throws an exception 
     *
     * @throws       UnsupportedOperationException
     */
    @Override
    public boolean add(Object o) throws UnsupportedOperationException
    {
      throw new UnsupportedOperationException();
    }

    /**
     * adding an element is unsupported; this method simply throws an exception 
     *
     * @throws       UnsupportedOperationException
     */
    @Override
    public boolean addAll(Collection c) throws UnsupportedOperationException
    {
      throw new UnsupportedOperationException();
    }

    /**
     * clears the backing HashMap; this is a prime example of an overridden implementation
     * which is far more efficient than its superclass implementation (which uses an iterator
     * and is O(n) -- this is an O(1) call)
     */
    @Override
    public void clear()
    {
      HashMap.this.clear();
    }

    /**
     * returns true if the supplied object is contained by this Set
     *
     * @param     o       an Object being testing to see if it is in this Set
     */
    @Override
    public boolean contains(Object o)
    {
      if (setType == KEYS)
	return HashMap.this.containsKey(o);
      else
	return (o instanceof Map.Entry) ? HashMap.this.
	  containsEntry((Map.Entry) o) : false;
    }

    /** 
     * returns true if the backing HashMap is empty (which is the only case either a KEYS
     * Set or an ENTRIES Set would be empty)
     */
    @Override
    public boolean isEmpty()
    {
      return HashMap.this.isEmpty();
    }

    /**
     * removes the supplied Object from the Set
     *
     * @param      o       the Object to be removed
     */
    @Override
    public boolean remove(Object o)
    {
      if (setType == KEYS)
	return (HashMap.this.remove(o) != null);
      else
	return (o instanceof Map.Entry) ?
	  (HashMap.this.remove(((Map.Entry) o).getKey()) != null) : false;
    }

    /** returns the size of this Set (always equal to the size of the backing Hashtable) */
    @Override
    public int size()
    {
      return HashMap.this.size();
    }

    /** returns an Iterator over the elements of this Set */
    @Override
    public Iterator iterator()
    {
      return new HashMapIterator(setType);
    }

    @Override
    public boolean containsAll(Collection c) {
        throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeAll(Collection c) {
        throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean retainAll(Collection c) {
        throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] toArray() {
        throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] toArray(Object[] a) {
        throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  }

  /**
   * Like the above Set view, except this one if for values, which are not
   * guaranteed to be unique in a Map; this prvides a Bag of values
   * in the HashMap
   *
   * @author       Jon Zeppieri
   * @version      $Revision: 1.1 $
   * @modified     $Id: HashMap.java,v 1.1 2002/04/30 09:10:32 golm Exp $
   */
  private class HashMapCollection extends AbstractCollection
    implements Collection
  {
    /** a trivial contructor for HashMapCollection */
    HashMapCollection()
    {
    }

    /** 
     * adding elements is not supported by this Collection;
     * this method merely throws an exception
     *
     * @throws     UnsupportedOperationException
     */
    @Override
    public boolean add(Object o) throws UnsupportedOperationException
    {
      throw new UnsupportedOperationException();
    }

    /** 
     * adding elements is not supported by this Collection;
     * this method merely throws an exception
     *
     * @throws     UnsupportedOperationException
     */
    @Override
    public boolean addAll(Collection c) throws UnsupportedOperationException
    {
      throw new UnsupportedOperationException();
    }

    /** removes all elements from this Collection (and from the backing HashMap) */
    @Override
    public void clear()
    {
      HashMap.this.clear();
    }

    /** 
     * returns true if this Collection contains at least one Object which equals() the
     * supplied Object
     *
     * @param         o        the Object to compare against those in the Set
     */
    @Override
    public boolean contains(Object o)
    {
      return HashMap.this.containsValue(o);
    }

    /** returns true IFF the Collection has no elements */
    @Override
    public boolean isEmpty()
    {
      return HashMap.this.isEmpty();
    }

    /** returns the size of this Collection */
    @Override
    public int size()
    {
      return HashMap.this.size();
    }

    /** returns an Iterator over the elements in this Collection */
    @Override
    public Iterator iterator()
    {
      return new HashMapIterator(VALUES);
    }
  }

  /**
   * a class which implements the Iterator interface and is used for
   * iterating over HashMaps;
   * this implementation is parameterized to give a sequential view of
   * keys, values, or entries; it also allows the removal of elements, 
   * as per the Javasoft spec.
   *
   * @author       Jon Zeppieri
   * @version      $Revision: 1.1 $
   * @modified     $Id: HashMap.java,v 1.1 2002/04/30 09:10:32 golm Exp $
   */
  class HashMapIterator implements Iterator
  {
    /** the type of this Iterator: KEYS, VALUES, or ENTRIES */
    private final int myType;
    /** 
     * the number of modifications to the backing Hashtable for which
     * this Iterator can account (idea ripped off from Stuart Ballard)
     */
    private int knownMods;
    /** the location of our sequential "cursor" */
    private int position;
    /** the current index of the BucketList array */
    private int bucketIndex;
    /** a reference, originally null, to the specific Bucket our "cursor" is pointing to */
    private Bucket.Node currentNode;
    /** a reference to the current key -- used fro removing elements via the Iterator */
    private Object currentKey;

    /** construct a new HashtableIterator with the supllied type: KEYS, VALUES, or ENTRIES */
    HashMapIterator(int type)
    {
      myType = type;
      knownMods = HashMap.this.modCount;
      position = 0;
      bucketIndex = -1;
      currentNode = null;
      currentKey = null;
    }

    /** 
     * Stuart Ballard's code:  if the backing HashMap has been altered through anything 
     * but <i>this</i> Iterator's <pre>remove()</pre> method, we will give up right here,
     * rather than risking undefined behavior
     *
     * @throws    ConcurrentModificationException
     */
    private void checkMod()
    {
      if (knownMods != HashMap.this.modCount)
	throw new ConcurrentModificationException();
    }

    /** returns true if the Iterator has more elements */
    @Override
    public boolean hasNext()
    {
      checkMod();
      return position < HashMap.this.size();
    }

    /** returns the next element in the Iterator's sequential view */
    @Override
    public Object next()
    {
      Bucket list = null;
      Object result;
      checkMod();
      try
	{
	  while (currentNode == null)
	    {
	      while (list == null)
		list = HashMap.this.buckets[++bucketIndex];
	      currentNode = list.first;
	    }
	  currentKey = currentNode.getKey();
	  result = (myType == KEYS) ? currentKey :
	    ((myType == VALUES) ? currentNode.getValue() : currentNode);
	  currentNode = currentNode.next;
	}
      catch (Exception e)
	{
	  throw new NoSuchElementException();
	}
      position++;
      return result;
    }

    /** 
     * removes from the backing HashMap the last element which was fetched with the
     * <pre>next()</pre> method
     */
    @Override
    public void remove()
    {
      checkMod();
      if (currentKey == null)
	{
	  throw new IllegalStateException();
	}
      else
	{
	  HashMap.this.remove(currentKey);
	  knownMods++;
	  position--;
	  currentKey = null;
	}
    }
  }

    /**
    * a singleton instance of this class (HashMap.NULL_KEY)
    * is used to represent the null key in HashMap objects
    *
    * @author     Jon Zeppieri
    * @version    $Revision: 1.1 $
    * @modified   $Id: HashMap.java,v 1.1 2002/04/30 09:10:32 golm Exp $
    */
    private static class Null
    {
        /** trivial constructor */
        Null()
        {
        }
    }

    /**
    * a HashMap version of Map.Entry -- one thing in this implementation is
    * HashMap-specific:  if the key is HashMap.NULL_KEY, getKey() will return
    * null
    *
    * Simply, a key / value pair
    *
    * @author      Jon Zeppieri
    * @version     $Revision: 1.1 $
    * @modified    $Id: HashMap.java,v 1.1 2002/04/30 09:10:32 golm Exp $
    */
    private static class HashMapEntry<K,V> extends Bucket.Node<K,V> implements Map.Entry<K,V>
    {
        K key;
        /** construct a new HashMapEntry with the given key and value */
        public HashMapEntry(K key, V value)
        {
            super(key, value);
            this.key = key;
        }

        /**
         * if the key == HashMap.NULL_KEY, null is returned, otherwise the actual
         * key is returned
         */
        @Override
        public K getKey()
        {
            K oResult = key;//super.getKey();
            return (oResult == HashMap.NULL_KEY) ? null : oResult;
        }
    }
    // EOF -----------------------------------------------------------------------
}
