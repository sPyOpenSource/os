package java.lang;

public class RuntimeException extends Exception {
    public RuntimeException() {	super(); }
    public RuntimeException(String s) {	super(s); }
    public RuntimeException(Throwable cause) {
        super(cause);
    }
    public RuntimeException(String s, Throwable cause) {
        super(s, cause);
    }
}
