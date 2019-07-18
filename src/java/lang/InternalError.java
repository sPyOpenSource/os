package java.lang;

import java.io.IOException;

public class InternalError extends VirtualMachineError {

    public InternalError(CloneNotSupportedException e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public InternalError() {
        super();
    }
    public InternalError(String message) {
        super(message);
    }

    public InternalError(String message, Throwable cause) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public InternalError(IOException ex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public InternalError(Throwable ex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
