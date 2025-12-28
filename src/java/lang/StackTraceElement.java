package java.lang;

public final class StackTraceElement {
	String className, methodName;
	int bytecode, line;
        
        private String declaringClass;
        private String fileName;
        private int lineNumber;

        StackTraceElement parent;
        Class declaringClazz;
    
        @Override
	public String toString() {
	    return className + "." + methodName + ", bytecode " + bytecode + ", line " + line;
	}
}