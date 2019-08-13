package java.util;

public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> 
{
    protected int capacityIncrement;
    protected int elementCount;
    
    /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer. Any
     * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * will be expanded to DEFAULT_CAPACITY when the first element is added.
     */
    transient Object[] elementData; // non-private to simplify nested class access
    
    @Override
    public void sort(Comparator<? super E> c) {
        Object[] a = this.toArray();
        Arrays.sort(a, (Comparator) c);
        ListIterator<E> i = this.listIterator();
        for (Object e : a) {
            i.next();
            i.set((E) e);
        }
    }
    
    protected void ensureCapacity(int minCapacity)
    {
	if (minCapacity <= elementData.length) return;

	Object[] newData = null;

	try {
	    int newSize =
		capacityIncrement > 0
		? elementData.length + capacityIncrement
		: elementData.length * 2;
	    
	    if (newSize < minCapacity) newSize = minCapacity;
	    newData = new Object[newSize];
	} catch (Error error) {
	    newData = new Object[minCapacity]; 
	}

	elementData = Arrays.copyOf(newData, newData.length);;
    }

    /*public final void copyInto(E[] array)
    {
	System.arraycopy(
			 elementData, 0,
			 array, 0,
			 elementCount
			 );
    }*/
    
    @Override
    public boolean add(E e) {
        ensureCapacity(elementCount + 1);
	elementData[elementCount] = e;
	elementCount++;
        return true;
    }
    
    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Iterator<E> iterator() {
	throw new Error("ITERATOR");
    }
    
    @Override
    public E remove(int index) {
        /*rangeCheck(index);
        checkForComodification();
        E result = l.remove(index+offset);
        this.modCount = l.modCount;
        size--;*/
        return null;
    }
    
    @Override
    public int size() {
        //checkForComodification();
        return elementCount;
    }
    
    @Override
    abstract public E get(int index);
    
    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }
    
    @Override
    public ListIterator<E> listIterator(final int index) {
        /*rangeCheckForAdd(index);

        return new ListItr(index);*/
        throw new Error("Object method not implemented");
    }
    
    @Override
    public int indexOf(Object o) {
        ListIterator<E> it = listIterator();
        if (o==null) {
            while (it.hasNext())
                if (it.next()==null)
                    return it.previousIndex();
        } else {
            while (it.hasNext())
                if (o.equals(it.next()))
                    return it.previousIndex();
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(Object o) {
        ListIterator<E> it = listIterator(size());
        if (o==null) {
            while (it.hasPrevious())
                if (it.previous()==null)
                    return it.nextIndex();
        } else {
            while (it.hasPrevious())
                if (o.equals(it.previous()))
                    return it.nextIndex();
        }
        return -1;
    }
    
    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        /*return (this instanceof RandomAccess ?
                new RandomAccessSubList<>(this, fromIndex, toIndex) :
                new SubList<>(this, fromIndex, toIndex));*/
        throw new Error("Object method not implemented");
    }
}
