package AI;
/**
 * This is the input class of AI.
 * 
 * @author X. Wang 
 * @version 1.0
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jx.zero.Debug;
import jx.zero.Naming;
import jx.zero.debug.DebugChannel;

//import org.opencv.core.Mat;
//import org.opencv.videoio.VideoCapture;

public abstract class AIBaseInput implements Runnable
{
    /**
     * This is the initialization of AIInput class 
     */
    protected final AIBaseMemory mem;
    protected final Naming naming;
    private final static int BUFFER_SIZE = 1024;
    
    /**
     * Constructor for objects of class AIInput
     * @param mem
     */
    public AIBaseInput(AIBaseMemory mem)
    {
    	this.mem = mem;
        naming = null;
    }
    
    public AIBaseInput(AIBaseMemory mem, Naming naming)
    {
    	this.mem = mem;
        this.naming = naming;
    }
    
    /*protected void getImageFromWebcam(VideoCapture cap, String name){
        Mat image = new Mat();
        cap.read(image);
        if(!image.empty())
            mem.addInfo(new Info(image), name);
    }*/
    
    private void ImportMemoryTxt(){
        try {
            BufferedReader log = new BufferedReader(new FileReader(mem.getLogPath() + "LOG.TXT"));
            String memory = log.readLine();
            /*if(memory != null)
                mem.ImportTxt(mem.getLogPath() + memory);*/
        } catch (IOException ex) {
            //Logger.getLogger(AIBaseInput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void ImportMemory(){
        /*try {
            String memory;
            try (BufferedReader log = new BufferedReader(new FileReader(mem.getLogPath() + "LOG.TXT"))) {
                memory = log.readLine();
            }
            if(memory != null)
                mem.ImportTxt(mem.getLogPath() + memory);
        } catch (IOException ex) {
            Logger.getLogger(AIBaseInput.class.getName()).log(Level.SEVERE, null, ex);
            ImportMemoryTxt();
        }*/
    }

    @Override
    public void run() {
        jx.zero.debug.DebugOutputStream out = new jx.zero.debug.DebugOutputStream((DebugChannel) naming.lookup("DebugChannel0"));
	Debug.out = new jx.zero.debug.DebugPrintStream(out);
        Debug.out.println("AIInput running...");
        ImportMemory();
        /*Thread ReceiveFromNetwork = new Thread(){
            @Override
            public void run(){
                while(true)
                    mem.ReceiveFromNetwork(BUFFER_SIZE);
            }
        };
        //ReceiveFromNetwork.start();
        Thread AddWebsocketClient = new Thread(){
            @Override
            public void run(){
                while(true)
                    mem.AddWebsocketClient();
            }
        };
        AddWebsocketClient.start();
        Thread();*/
    }
    
    abstract protected void Thread();
}