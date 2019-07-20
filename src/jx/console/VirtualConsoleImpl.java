package jx.console;

import java.io.InputStream;
import java.io.OutputStream;
import jx.devices.Screen;
import jx.devices.Keyboard;

import jx.zero.*;

public class VirtualConsoleImpl implements VirtualConsole {
    Memory backup;
    Memory video;
    Memory buffer;
    Screen screen;
    Keyboard keyboard;
    int x,y;
    private OutputStream out;
    private OutputStream err;
    private InputStream in;
    boolean active;
    ConsoleImpl cons;
    
    public VirtualConsoleImpl(MemoryManager memMgr, ConsoleImpl cons, Screen screen, Keyboard keyboard) {
	this.cons = cons;
	this.video = screen.getVideoMemory();
	this.screen = screen;
	this.keyboard = keyboard;
	backup = memMgr.alloc(2 * 80 * 25);
	backup.fill16((short)0x0f00, 0, 80*24);


	/*
	final Scancodes scancodes = new Scancodes(out);
	KeyListener listener = new KeyListener() {
	    public void keyPressed(int scancode) {
	    }
	};
	keyboard.addKeyListener(listener);
	*/

	out= new ConsoleOutputStream(this);
	err= new ConsoleOutputStream(this);
	in = new ConsoleInputStream(this);	

    }

    @Override
    public void putc(int c) {
	switch (c) {
	case '\n':
	    if (y==24) {
		buffer.copy(80*2, 0, 80*24*2);
		buffer.fill16((short)0x0f00, 80*24, 80);
	    } else {
		y++;
	    }
      
	    /* fall through... */
	case '\r':
	    x = 0;
	    break;
	case '\b':
	    if (x > 0) x--;
	    break;
	case '\t':
	    do {
		putc(' ');
	    } while ((x & 7) != 0);
	    break;
      
	default:
	    /* Wrap if we reach the end of a line.  */
	    if (x >= 80) {
		putc('\n');
	    }
      
	    /* Stuff the character into the video buffer. */
	    buffer.set8((80*y + x) * 2,  (byte)c);
	    buffer.set8((80*y + x) * 2 + 1,  (byte)0x0f);
	    x++;
	    break;
	}
	if (active) cons.moveCursorTo(x,y);
    }

    @Override
    public int getc() {
	if (active) {
	    int c = keyboard.getc();
	    putc(c); // local echo
	    return c;
	} else {
	    // block until becoming active
	    for(;;);
	}
    }

    @Override
    public void activate() {
	active = true;
	buffer = video;
    }
    @Override
    public void deactivate() {
	active = false;
	buffer = backup;
    }

    @Override
    public InputStream getInputStream() {
	return in;
    }
    @Override
    public OutputStream getOutputStream() {
	return out;
    }
    @Override
    public OutputStream getErrorStream() {
	return err;
    }
}
