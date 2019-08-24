package java.lang;

public final class StackTraceElement {
	String className, methodName;
	int bytecode, line;
        @Override
	public String toString() {
	    return className + "." + methodName + ", bytecode " + bytecode + ", line " + line;
	}
    }