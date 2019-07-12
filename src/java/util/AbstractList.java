package java.util;

public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> 
{
    protected int capacityIncrement;
    protected int elementCount;
    protected E[] elementData;

    protected final void ensureCapacity(int minCapacity)
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
}
