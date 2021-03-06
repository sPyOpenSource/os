package java.util;

import java.io.Serializable;

class ArrayEnumeration implements Enumeration, Serializable
{
    protected Object[] data;
    protected int index;

    // Methods

    public boolean hasMoreElements()
    {
	return index < data.length;
    }

    public Object nextElement() throws NoSuchElementException
    {
	if (index >= data.length)
	    throw new NoSuchElementException();
	return data[index++];
    }

    // Constructors

    ArrayEnumeration(Object[] data)
    {
	this.data = data;
	index = 0;
    }
}

public class Vector<E> extends AbstractList<E> implements List<E>, Cloneable, Serializable
{ 
    // Fields


    // Constructors
    
    public Vector(int initialCapacity, int capacityIncrement)
    {
	this.capacityIncrement = capacityIncrement;
	elementData = new Object[initialCapacity];
	elementCount = 0;
    }
    
    public Vector(int initialCapacity)
    {
	this(initialCapacity, 0);
    }
    
    public Vector()
    {
	this(10);
    }

    // Methods

    public final int capacity()
    {
	return elementData.length;
    }


    /*
    public Object clone()
    {
	try
	    {
		Vector v = (Vector) super.clone();
		v.elementData = (Object[]) elementData.clone();

		return v;
	    }
	catch (CloneNotSupportedException e)
	    {
		return null;
	    }
    }
    */
    // TEST
    public Object clone()
    {
	Vector c = new Vector();
	for(int i = 0; i < size(); i++)
	    c.addElement(elementAt(i));
	return c;
    }

    public final void trimToSize()
    {
	/*E[] newData = new E[elementCount];
	copyInto(newData);
	elementData = newData;*/
    }

    public final void addElement(E obj)
    {
	add(obj);
    }

    public final void insertElementAt(E obj, int index)
    {
	if (index > elementCount)
	    throw new IndexOutOfBoundsException();
	ensureCapacity(elementCount + 1);
	for (int i = elementCount; i > index; i--)
	    elementData[i] = elementData[i - 1];
	elementData[index] = obj;
	elementCount++;
    }

    @Override
    public final int size()
    {
	return elementCount;
    }

    @Override
    public final boolean isEmpty()
    {
	return (elementCount == 0);
    }

    public final Object firstElement()
	throws NoSuchElementException
    {
	if (isEmpty())
	    throw new NoSuchElementException();
	return elementData[0];
    }

    public final Object lastElement()
	throws NoSuchElementException
    {
	if (isEmpty())
	    throw new NoSuchElementException();
	return elementData[elementCount - 1];
    }

    public E elementAt(int index)
    {
	if (index >= elementCount)
	    throw new IndexOutOfBoundsException();
	return elementData(index);
    }
    
@SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }
    
    /*public final Object get(int index) {
	return elementAt(index);
    }*/

    public final Enumeration elements()
    {
	Object[] data = new Object[elementCount];
	copyInto(data);
	return new ArrayEnumeration(data);
    }

    public final int indexOf(Object obj, int index)
    {
	for (int i = index; i < elementCount; i++)
	    if (elementData[i] != null && elementData[i].equals(obj))
		return i;
	return -1;
    }

    @Override
    public final int indexOf(Object obj)
    {
	return indexOf(obj, 0);
    }

    @Override
    public final boolean contains(Object obj)
    {
	return (indexOf(obj) >= 0);
    }

    public final int lastIndexOf(Object obj, int index)
    {
	int k = -1;
	for (int i = 0; i <= index; i++)
	    if (elementData[i].equals(obj))
		k = i;
	return k;
    }

    @Override
    public final int lastIndexOf(Object obj)
    {
	return lastIndexOf(obj, elementCount - 1);
    }

    public final void removeAllElements()
    {
	while (elementCount > 0)
	    elementData[--elementCount] = null;
    }

    @Override
    public final void clear() {
	removeAllElements();
    }

    public final void removeElementAt(int index)
    {
	for (int i = index + 1; i < elementCount; i++)
	    elementData[i - 1] = elementData[i];
	elementCount--;
    }

    /*public final Object remove(int index) {
	Object o = elementData[index];

	for (int i = index + 1; i < elementCount; i++)
	    elementData[i - 1] = elementData[i];
	elementCount--;

	return o;
    }*/

    public final boolean removeElement(Object obj)
    {
    for (int i = 0; i < elementCount; i++)
	if (elementData[i].equals(obj))
	    {
		removeElementAt(i);
		return true;
	    }
    return false;
    }
/**
     * Copies the components of this vector into the specified array.
     * The item at index {@code k} in this vector is copied into
     * component {@code k} of {@code anArray}.
     *
     * @param  anArray the array into which the components get copied
     * @throws NullPointerException if the given array is null
     * @throws IndexOutOfBoundsException if the specified array is not
     *         large enough to hold all the components of this vector
     * @throws ArrayStoreException if a component of this vector is not of
     *         a runtime type that can be stored in the specified array
     * @see #toArray(Object[])
     */
    public synchronized void copyInto(Object[] anArray) {
        System.arraycopy(elementData, 0, anArray, 0, elementCount);
    }
    @Override
    public final boolean remove(Object o) {
	return removeElement(o);
    }

    public final void setElementAt(E obj, int index)
    {
	if (index >= elementCount)
	    throw new IndexOutOfBoundsException();
	elementData[index] = obj;
    }

    public final void setSize(int newSize)
    {
	while (elementCount > newSize)
	    elementData[--elementCount] = null;
	ensureCapacity(newSize);
	while (elementCount < newSize)
	    elementData[elementCount++] = null;
    }
    
    @Override
    public final String toString()
    {
	StringBuilder buff = new StringBuilder("[");
	for (int i = 0; i < elementCount; i++)
	    {
		if (i > 0)
		    buff.append(", ");
		
		if (elementData[i] != null)
		    buff.append(elementData[i].toString());
		else
		    buff.append("null");
	    }
	buff.append("]");

	return buff.toString();
    }

    @Override
    public Object[] toArray() {
	Object[] ret = new Object[elementCount];
        System.arraycopy(elementData, 0, ret, 0, elementCount);
	return ret;
    }

    @Override
    public E remove(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public E get(int index) {
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        return (E)elementData[index];
    }

    /*@Override
    public synchronized void copyInto(E[] anArray) {
        System.arraycopy(elementData, 0, anArray, 0, elementCount);
    }*/

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
    public E set(int index, E o) {
        throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
