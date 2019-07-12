package java.lang;

import jx.zero.*;

import java.io.PrintStream;
import java.util.Objects;

public class Throwable
{
    class StackFrame {
	String className, methodName;
	int bytecode, line;
        @Override
	public String toString() {
	    return className + "." + methodName + ", bytecode " + bytecode + ", line " + line;
	}
    }

    private StackFrame[] backtrace;
    private String message;
    private static final boolean createStackTrace = false;
    private Throwable cause = this;
    public Throwable(String message) {
	this.message = message;
	if (createStackTrace) {
	    fillInStackTrace();
	    //printStackTrace(System.out);
	}
    }
    
    public Throwable(String message, Throwable cause) {
        fillInStackTrace();
        this.message = message;
        this.cause = cause;
    }
    
    public Throwable() {
	this("");
    }
    
    @Override
    public String toString() {
	return getClass().getName() + ": " + message;
    }
    
    public String getMessage() {
	return message;
    }
    
    private static final int MYOWN_STACK = 0;
    private  void fillInStackTrace() {
	CPUManager c = (CPUManager)InitialNaming.getInitialNaming().lookup("CPUManager");
	backtrace = new StackFrame[c.getStackDepth()-MYOWN_STACK];
	for(int i = 0; i < backtrace.length - MYOWN_STACK; i++) {
	    backtrace[i] = new StackFrame();
	    backtrace[i].className = c.getStackFrameClassName(i + MYOWN_STACK);
	    backtrace[i].methodName = c.getStackFrameMethodName(i + MYOWN_STACK);
	    backtrace[i].line = c.getStackFrameLine(i + MYOWN_STACK);
	    backtrace[i].bytecode = c.getStackFrameBytecode(i + MYOWN_STACK);
	    
	}
    }
    
    public void printStackTrace() {
	if (System.err != null) {
	    printStackTrace(System.err);
	} else if (System.out != null) {
	    printStackTrace(System.out);
	}
    }
    
    public void printStackTrace(PrintStream s) {	
	  if (this.message != null) s.println(this.message);
	  for (int i = 0; i < backtrace.length; i++)
	      s.println(i + " " + backtrace[i]);
    }
    public String getLocalizedMessage() {return null;}
    
    public synchronized Throwable initCause(Throwable cause) {
        if (this.cause != this)
            throw new IllegalStateException("Can't overwrite cause with " +
                                            Objects.toString(cause, "a null"), this);
        if (cause == this)
            throw new IllegalArgumentException("Self-causation not permitted", this);
        this.cause = cause;
        return this;
    }
}
