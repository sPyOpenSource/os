package AI;

import jx.zero.Debug;
import jx.zero.Naming;

/**
 * This is a class initialize an artificial intelligence service.
 * 
 * @author X. Wang
 * @version 1.0
 */
public final class AI 
{
    // instance variables
    private final AIMemory mem = new AIMemory();
    private final AIInput  inp;
    private final AILogic  log;
    private final AIOutput oup;
    private final Thread   logThread, inpThread, oupThread;
    
    /**
     * Constructor for objects of class AI
     */
    public AI()
    {
        // Initialize instance variables
        mem.setLogPath("/AI/");
        inp = new AIInput(mem);
        log = new AILogic(mem);
        oup = new AIOutput(mem);
	logThread = new Thread(log);
        inpThread = new Thread(inp);
        oupThread = new Thread(oup);
    }
    
    public void start()
    {
        Debug.out.println("AI running...");
    	logThread.start();
        inpThread.start(); 
        oupThread.start();
    }
}