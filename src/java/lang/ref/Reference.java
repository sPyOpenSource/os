/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java.lang.ref;

/**
 *
 * @author spy
 */
public class Reference<T> {
    
    private T target; //don't chang the var name ,access by vm
    private T referent;

    volatile ReferenceQueue<? super T> queue;
    @SuppressWarnings("rawtypes")
    Reference next;
    
    Reference(T referent) {
        this(referent, null);
    }

    Reference(T referent, ReferenceQueue<? super T> queue) {
        this.referent = referent;
        this.queue = null;//(queue == null) ? ReferenceQueue.NULL : queue;
    }
    
    public T get() {
        return this.referent;
    }
    
}
