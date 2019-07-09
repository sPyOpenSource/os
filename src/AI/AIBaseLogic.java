package AI;

import AI.Models.Info;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the logic class of AI.
 * 
 * @author X. Wang 
 * @version 1.0
 */
public abstract class AIBaseLogic implements Runnable
{
    // instance variables
    protected final AIBaseMemory mem;
    private final double dt = 0.02; // in s
    private final double percentage = 0.5;
        
    /**
     * Constructor for objects of class AILogic
     * @param mem
     */
    public AIBaseLogic(AIBaseMemory mem)
    {
        // sign an instance variable        
	this.mem = mem;
    }

    /**
     * this is a wait method
     * 
     * @param t in seconds
     */
    public static void Wait(double t)
    {
        /*if(t > 0){
            try {
                Thread.sleep((int)(t * 1000));
            } catch (InterruptedException e) {
                Logger.getLogger(AIBaseLogic.class.getName()).log(Level.SEVERE, null, e);
            }   
        }*/	
    }
    
    /*private void ProcessMessages(){
        while(true){
            Info info = mem.dequeFirst("incomingMessages");
            if(info == null)
                break;
            String output = mem.GetShortMemory(Wish(mem.getShortLength()));
            if (output != null)
                mem.addInfo(info, output);
            mem.SaveShort(info.getPayload(), Wish(mem.getShortLength()));
            Fuzzy(info);
            Info response = Induction(info);
            if (response == null)
                response = RandomSentence();
            if(response != null){
                mem.addInfo(response, "outgoingMessages");
                mem.SaveShort(response.getPayload(), Wish(mem.getShortLength()));
            }
            Messages(info);
        }
    }

    private Info Induction(Info info) // this function is wrong
    {
        List<Info> messages = mem.search(info.getPayload());
        int s = messages.size();
        if(s > 0){
            mem.addEmotion();
            return messages.get(Wish(s));
        }
        mem.minEmotion();
        return null;
    }
    
    private void Fuzzy(Info info)
    {
    	double p = 0;
        int l = mem.getShortLength();
    	for (int i = 0; i < l; i++)
    	{
            if (info.getPayload().equals(mem.GetShortMemory(i)))
                p = p + 1.0 / l;
    	}
    	if(p > percentage){
            mem.addEmotion();
            mem.addInfo(info, "longterm");
        } else {
            mem.minEmotion();
        }
    }*/
    
    /*private Info RandomSentence()
    {
    	String info = mem.GetShortMemory(Wish(mem.getShortLength()));
        if (info == null)
            return Induction(new Info("longterm"));
        return new Info(info);
    }*/
    
    private int Wish(int length) // this function is wrong
    {
        return 0;//ThreadLocalRandom.current().nextInt(0, length);
    }
    
    public void ChangeShort()
    {
        /*mem.search("longterm").forEach((_item) -> {
            mem.SaveShort(_item.getPayload(), Wish(mem.getShortLength()));   
            Wait(10*Math.pow(Math.E, mem.getEmotion()));
        });*/
    }

    @Override
    public void run() {
        /*Thread Mutation = new Thread(){
            @Override
            public void run(){
                while(true){
                    ChangeShort();
                    Wait(dt);
                }
            }
        };
        Mutation.start();*/
        /*while(true){
            ProcessMessages();
            //mem.CleanClients();
            Thread();
            Wait(dt);
        }*/
    }
    
    abstract protected void Thread();
    
    abstract protected void Messages(Info info);
}