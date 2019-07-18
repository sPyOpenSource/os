package java.util;

public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> 
{
    protected int capacityIncrement;
    protected int elementCount;
    protected E[] elementData;
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

	E[] newData = null;;

	try {
	    int newSize =
		capacityIncrement > 0
		? elementData.length + capacityIncrement
		: elementData.length * 2;
	    
	    if (newSize < minCapacity) newSize = minCapacity;
	    //newData = new E[newSize];
	} catch (Error error) {
	    //newData = new E[minCapacity]; 
	}

	copyInto(newData);
	elementData = newData;
    }

    public final void copyInto(E[] array)
    {
	System.arraycopy(
			 elementData, 0,
			 array, 0,
			 elementCount
			 );
    }
    
    public boolean add(E e) {
        ensureCapacity(elementCount + 1);
	elementData[elementCount] = e;
	elementCount++;
        return true;
    }
     public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }
    public Iterator<E> iterator() {
	throw new Error("ITERATOR");
    }
    
    public E remove(int index) {
        /*rangeCheck(index);
        checkForComodification();
        E result = l.remove(index+offset);
        this.modCount = l.modCount;
        size--;*/
        return null;
    }
    public int size() {
        //checkForComodification();
        return elementCount;
    }
    abstract public E get(int index);
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }
    public ListIterator<E> listIterator(final int index) {
        /*rangeCheckForAdd(index);

        return new ListItr(index);*/
        throw new Error("Object method not implemented");
    }
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
      public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }
      public List<E> subList(int fromIndex, int toIndex) {
        /*return (this instanceof RandomAccess ?
                new RandomAccessSubList<>(this, fromIndex, toIndex) :
                new SubList<>(this, fromIndex, toIndex));*/
        throw new Error("Object method not implemented");
    }

}
