package jx.keyboard;

import jx.zero.*;
import jx.wm.WindowManager;

public class Main
{
    public static void main(String[] args) {
	String wmName = args[0];
	KeyboardImpl	keyboard;
	final boolean debug = false; 
	
	keyboard = new KeyboardImpl ();

	if (keyboard.keyboardHardwareAvailable()) {
	    Debug.out.println ("    init keyboard");
	    keyboard.init();
	    Debug.out.println ("    keyboard initialized");
	    Naming naming = InitialNaming.getInitialNaming();
	    WindowManager m_cWindowManager = (WindowManager)LookupHelper.waitUntilPortalAvailable(naming, wmName);
	    new KeyboardListener (m_cWindowManager, keyboard);	
	    new MouseListener (m_cWindowManager, keyboard);
	} else {
	    Debug.out.println ("************ NO KEYBOARD FOUND!! **************");
	}
	
    }

}
