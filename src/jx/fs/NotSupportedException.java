package jx.fs;

/**
 * The file system does not support this operation.
 * Das Dateisystem "versteht" diese Operation nicht
 */
public class NotSupportedException extends FSException {
    public NotSupportedException() {
	super();
    }

    public NotSupportedException(String msg) {
	super(msg);
    }
}
