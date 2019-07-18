package java.lang;

public class Error extends Throwable {
    public Error() { super(); }
    public Error(String s) { super(s); }
    public Error(String message, Throwable cause) {
        super(message, cause);
    }

    public Error(Exception ex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

