package java.util;

public abstract class AbstractCollection<E> implements Collection<E> {
    public boolean equals(E a, E b) {throw new Error("");}
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }    
    public abstract int size();
    public void clear() {
        /*Iterator<E> it = iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }*/
    }
    public boolean isEmpty() {
        return size() == 0;
    }
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        /*for (E e : c)
            if (add(e))
                modified = true;*/
        return modified;
    }
    public boolean contains(Object o) {
        /*Iterator<E> it = iterator();
        if (o==null) {
            while (it.hasNext())
                if (it.next()==null)
                    return true;
        } else {
            while (it.hasNext())
                if (o.equals(it.next()))
                    return true;
        }*/
        return false;
    }
    public boolean containsAll(Collection<?> c) {
        /*for (Object e : c)
            if (!contains(e))
                return false;*/
        return true;
    }
    public abstract Iterator<E> iterator();
    public boolean remove(Object o) {
        Iterator<E> it = iterator();
        if (o==null) {
            while (it.hasNext()) {
                if (it.next()==null) {
                    it.remove();
                    return true;
                }
            }
        } else {
            while (it.hasNext()) {
                if (o.equals(it.next())) {
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        /*Iterator<?> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }*/
        return modified;
    }
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            /*if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }*/
        }
        return modified;
    }
    public Object[] toArray() {
        // Estimate size of array; be prepared to see more or fewer elements
        Object[] r = new Object[size()];
        Iterator<E> it = iterator();
        /*for (int i = 0; i < r.length; i++) {
            if (! it.hasNext()) // fewer elements than expected
                return Arrays.copyOf(r, i);
            r[i] = it.next();
        }
        return it.hasNext() ? finishToArray(r, it) : r;*/
        return null;
    }
}
