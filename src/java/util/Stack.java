package java.util;

public class Stack<E> extends Vector<E>{
   public E pop() { 
       E       obj;
        int     len = size();

        obj = peek();
        removeElementAt(len - 1);

        return obj; 
   }
   public E peek() { 
       int     len = size();

        if (len == 0)
            throw new EmptyStackException();
        return elementAt(len - 1);
   }
   public E push(E item) { 
       //addElement(item);
       return item;
   }
   public void Stack() { throw new Error("NOT IMPLEMENTED"); }
   public boolean empty() { throw new Error("NOT IMPLEMENTED"); }
   public int search(java.lang.Object arg0) { throw new Error("NOT IMPLEMENTED"); }
}
