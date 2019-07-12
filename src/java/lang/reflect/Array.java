package java.lang.reflect;

public final class Array {
	private Array() {}
	public static Object newInstance(Class componentType, int length)
	    throws NegativeArraySizeException {throw new Error();}
        public static int getLength(Object array) {
        if (array instanceof Object[]) {
            return ((Object[]) array).length;
        } else if (array instanceof boolean[]) {
            return ((boolean[]) array).length;
        } else if (array instanceof byte[]) {
            return ((byte[]) array).length;
        } else if (array instanceof char[]) {
            return ((char[]) array).length;
        } else if (array instanceof double[]) {
            return ((double[]) array).length;
        } else if (array instanceof float[]) {
            return ((float[]) array).length;
        } else if (array instanceof int[]) {
            return ((int[]) array).length;
        } else if (array instanceof long[]) {
            return ((long[]) array).length;
        } else if (array instanceof short[]) {
            return ((short[]) array).length;
        }
        //throw badArray(array);
        return 0;
      }
}
