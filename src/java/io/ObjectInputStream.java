package java.io;
public class ObjectInputStream {

    private SerialCallbackContext curContext;
   public int read(byte[] args0, int args1, int args2) { throw new Error("NOT IMPLEMENTED"); }
   public int readInt() { throw new Error("NOT IMPLEMENTED"); }
   public float readFloat() { throw new Error("NOT IMPLEMENTED"); }
   public java.lang.Object readObject() { throw new Error("NOT IMPLEMENTED"); }
   public ObjectInputStream(java.io.InputStream arg0) { throw new Error("NOT IMPLEMENTED"); }
   public void defaultReadObject() { throw new Error("NOT IMPLEMENTED"); }
   public void close() { throw new Error("NOT IMPLEMENTED"); }
   /**
     * Provide access to the persistent fields read from the input stream.
     */
    public static abstract class GetField {

        /**
         * Get the ObjectStreamClass that describes the fields in the stream.
         *
         * @return  the descriptor class that describes the serializable fields
         */
        public abstract ObjectStreamClass getObjectStreamClass();

        /**
         * Return true if the named field is defaulted and has no value in this
         * stream.
         *
         * @param  name the name of the field
         * @return true, if and only if the named field is defaulted
         * @throws IOException if there are I/O errors while reading from
         *         the underlying <code>InputStream</code>
         * @throws IllegalArgumentException if <code>name</code> does not
         *         correspond to a serializable field
         */
        public abstract boolean defaulted(String name) throws IOException;

        /**
         * Get the value of the named boolean field from the persistent field.
         *
         * @param  name the name of the field
         * @param  val the default value to use if <code>name</code> does not
         *         have a value
         * @return the value of the named <code>boolean</code> field
         * @throws IOException if there are I/O errors while reading from the
         *         underlying <code>InputStream</code>
         * @throws IllegalArgumentException if type of <code>name</code> is
         *         not serializable or if the field type is incorrect
         */
        public abstract boolean get(String name, boolean val)
            throws IOException;

        /**
         * Get the value of the named byte field from the persistent field.
         *
         * @param  name the name of the field
         * @param  val the default value to use if <code>name</code> does not
         *         have a value
         * @return the value of the named <code>byte</code> field
         * @throws IOException if there are I/O errors while reading from the
         *         underlying <code>InputStream</code>
         * @throws IllegalArgumentException if type of <code>name</code> is
         *         not serializable or if the field type is incorrect
         */
        public abstract byte get(String name, byte val) throws IOException;

        /**
         * Get the value of the named char field from the persistent field.
         *
         * @param  name the name of the field
         * @param  val the default value to use if <code>name</code> does not
         *         have a value
         * @return the value of the named <code>char</code> field
         * @throws IOException if there are I/O errors while reading from the
         *         underlying <code>InputStream</code>
         * @throws IllegalArgumentException if type of <code>name</code> is
         *         not serializable or if the field type is incorrect
         */
        public abstract char get(String name, char val) throws IOException;

        /**
         * Get the value of the named short field from the persistent field.
         *
         * @param  name the name of the field
         * @param  val the default value to use if <code>name</code> does not
         *         have a value
         * @return the value of the named <code>short</code> field
         * @throws IOException if there are I/O errors while reading from the
         *         underlying <code>InputStream</code>
         * @throws IllegalArgumentException if type of <code>name</code> is
         *         not serializable or if the field type is incorrect
         */
        public abstract short get(String name, short val) throws IOException;

        /**
         * Get the value of the named int field from the persistent field.
         *
         * @param  name the name of the field
         * @param  val the default value to use if <code>name</code> does not
         *         have a value
         * @return the value of the named <code>int</code> field
         * @throws IOException if there are I/O errors while reading from the
         *         underlying <code>InputStream</code>
         * @throws IllegalArgumentException if type of <code>name</code> is
         *         not serializable or if the field type is incorrect
         */
        public abstract int get(String name, int val) throws IOException;

        /**
         * Get the value of the named long field from the persistent field.
         *
         * @param  name the name of the field
         * @param  val the default value to use if <code>name</code> does not
         *         have a value
         * @return the value of the named <code>long</code> field
         * @throws IOException if there are I/O errors while reading from the
         *         underlying <code>InputStream</code>
         * @throws IllegalArgumentException if type of <code>name</code> is
         *         not serializable or if the field type is incorrect
         */
        public abstract long get(String name, long val) throws IOException;

        /**
         * Get the value of the named float field from the persistent field.
         *
         * @param  name the name of the field
         * @param  val the default value to use if <code>name</code> does not
         *         have a value
         * @return the value of the named <code>float</code> field
         * @throws IOException if there are I/O errors while reading from the
         *         underlying <code>InputStream</code>
         * @throws IllegalArgumentException if type of <code>name</code> is
         *         not serializable or if the field type is incorrect
         */
        public abstract float get(String name, float val) throws IOException;

        /**
         * Get the value of the named double field from the persistent field.
         *
         * @param  name the name of the field
         * @param  val the default value to use if <code>name</code> does not
         *         have a value
         * @return the value of the named <code>double</code> field
         * @throws IOException if there are I/O errors while reading from the
         *         underlying <code>InputStream</code>
         * @throws IllegalArgumentException if type of <code>name</code> is
         *         not serializable or if the field type is incorrect
         */
        public abstract double get(String name, double val) throws IOException;

        /**
         * Get the value of the named Object field from the persistent field.
         *
         * @param  name the name of the field
         * @param  val the default value to use if <code>name</code> does not
         *         have a value
         * @return the value of the named <code>Object</code> field
         * @throws IOException if there are I/O errors while reading from the
         *         underlying <code>InputStream</code>
         * @throws IllegalArgumentException if type of <code>name</code> is
         *         not serializable or if the field type is incorrect
         */
        public abstract Object get(String name, Object val) throws IOException;
    }
/**
     * Reads the persistent fields from the stream and makes them available by
     * name.
     *
     * @return  the <code>GetField</code> object representing the persistent
     *          fields of the object being deserialized
     * @throws  ClassNotFoundException if the class of a serialized object
     *          could not be found.
     * @throws  IOException if an I/O error occurs.
     * @throws  NotActiveException if the stream is not currently reading
     *          objects.
     * @since 1.2
     */
    public ObjectInputStream.GetField readFields()
        throws IOException, ClassNotFoundException
    {
        throw new Error("NOT IMPLEMENTED");
    }
}
