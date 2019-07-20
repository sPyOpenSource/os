package java.lang;

public final class StringBuilder extends AbstractStringBuilder implements java.io.Serializable, CharSequence
{

    /** use serialVersionUID for interoperability */
    static final long serialVersionUID = 4383685877147921099L;

    /**
     * Constructs a string builder with no characters in it and an
     * initial capacity of 16 characters.
     */
    public StringBuilder() {
        super(16);
    }

    /**
     * Constructs a string builder with no characters in it and an
     * initial capacity specified by the <code>capacity</code> argument.
     *
     * @param      capacity  the initial capacity.
     * @throws     NegativeArraySizeException  if the <code>capacity</code>
     *               argument is less than <code>0</code>.
     */
    public StringBuilder(int capacity) {
        super(capacity);
    }

    /**
     * Constructs a string builder initialized to the contents of the
     * specified string. The initial capacity of the string builder is
     * <code>16</code> plus the length of the string argument.
     *
     * @param   str   the initial contents of the buffer.
     * @throws    NullPointerException if <code>str</code> is <code>null</code>
     */
    public StringBuilder(String str) {
        super(str.length() + 16);
        append(str);
    }
    
    @Override
    public StringBuilder append(String str){
        super.append(str);
        return this;
    }
    
    @Override
    public StringBuilder append(Object obj){
        return append(String.valueOf(obj));
    }
    
    @Override
    public StringBuilder append(int i){
        super.append(i);
        return this;
    }
    
    @Override
    public StringBuilder append(long lng){
        super.append(lng);
        return this;
    }
    
    @Override
    public StringBuilder append(char c) {
            super.append(c);
            return this;
    }

    @Override
    public String toString() {
        // Create a copy, don't share the array
        return new String(value, 0, count);
    }
}
