package java.lang;

public class Error extends Throwable {
    public Error() { super(); }
    public Error(String s) { super(s); }
    public Error(String message, Throwable cause) {
        super(message, cause);
    }
}

