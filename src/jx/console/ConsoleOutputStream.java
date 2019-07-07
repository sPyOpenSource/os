package jx.console;

import java.io.OutputStream;
import java.io.IOException;

class ConsoleOutputStream extends OutputStream {
    VirtualConsoleImpl console;;
    ConsoleOutputStream(VirtualConsoleImpl c) { console = c; }
    @Override
    public void write(int b) throws IOException {
	console.putc((char)b);
    }
}
