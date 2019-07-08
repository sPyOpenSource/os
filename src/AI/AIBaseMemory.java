package AI;

//import AI.Models.Computer;
import AI.Models.Info;
//import AI.util.WebsocketServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

        
/**
 * This is the memory class of AI.
 * 
 * @author X. Wang 
 * @version 1.0
 */
public class AIBaseMemory
{
    // instance variables
    private double emotion = 0;
    //private ConcurrentHashMap<String, List<Info>> dict = new ConcurrentHashMap<>();
    //private DatagramSocket socket;
    private String LOG;
    private final String[] shortterm;
    //private final WebsocketServer server;
    
    /**
     * Constructor for objects of class AIMemory
     */
    public AIBaseMemory()
    {
        // Initialize instance variables
        shortterm = new String[20];
        /*try {
            socket = new DatagramSocket(8000); //todo: safety test
        } catch (SocketException ex) {
            Logger.getLogger(AIBaseMemory.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        //server = new WebsocketServer(9090);    //todo: safety test
    }
    
    public void SaveShort(String x, int n)
    {
        shortterm[n % shortterm.length] = x;    
    }

    public String GetShortMemory(int j)
    {
        return shortterm[j % shortterm.length];
    }
    
    public int getShortLength()
    {
        return shortterm.length;
    }
    
    /*public void addInfo(Info info, String key){
        if(info != null && key != null)
            search(key).add(info);
    }
    
    public Info dequeFirst(String key){
        List<Info> messages = search(key);
        if (messages.size() > 0)
            return messages.remove(0);
        return null;
    }
    
    public Info dequeLast(String key){
        List<Info> messages = search(key);
        int length = messages.size();
        if (length > 0)
            return messages.remove(length - 1);
        return null; 
    }
    
    public Info getLast(String key){
        List<Info> messages = search(key);
        int length = messages.size();
        if (length > 0)
            return messages.get(length - 1);
        return null;
    }*/

    public void addEmotion()
    {
        emotion += 0.1;
    }
    
    public void minEmotion()
    {
        emotion -= 0.1;
    }
    
    public double getEmotion(){
        return emotion;
    }
    
    /*public List<Info> search(String key)
    {
        if (dict.containsKey(key)){
            return dict.get(key);
        } else {
            List<Info> temp = new ArrayList<>();
            dict.put(key, temp);
            return temp;
        }
    }*/
    
    /*public void removeAll(String key){
        if (dict.containsKey(key))
            dict.get(key).clear();
    }*/
    
    /*public Enumeration<String> getKeys(){
        return dict.keys();
    }*/
    
    /*public void ReceiveFromNetwork(int bufferSize){
        byte[] buffer = new byte[bufferSize];
        DatagramPacket inPacket = new DatagramPacket(buffer, bufferSize);
        try {
            socket.receive(inPacket);
        } catch (IOException ex) {
            Logger.getLogger(AIBaseMemory.class.getName()).log(Level.SEVERE, null, ex);
        }
        String info = new String(buffer, 0, inPacket.getLength());
        if("subscribe".equals(info))
            addInfo(new Info(new Computer(inPacket.getSocketAddress())), "networkClients");
        else
            addInfo(new Info(info), "incomingMessages");
    }
    
    public void setLogPath(String path){
        LOG = path;
    }
    
    public String getLogPath(){
        return LOG;
    }
    
    public void AddWebsocketClient(){
        final Info info = new Info(new Computer(server.WaitOnConnection()));
        addInfo(info, "networkClients");
        Thread thread = new Thread(){
            @Override
            public void run(){
                while(info.isOnline()){
                    String message = info.Receive();
                    if (message != null)
                        addInfo(new Info(message), "incomingMessages");
                }
            }
        };
        thread.start();
    }*/
    
    /*public void CleanClients(){
        for(Iterator<Info> iterator = search("networkClients").iterator(); iterator.hasNext();){
            Info client = iterator.next();
            if(!client.isOnline())
                iterator.remove();
        }
    }*/
    
    /*public void Save(String file){
        try {
            ObjectOutputStream memory = new ObjectOutputStream(new FileOutputStream(new File(file + ".dict")));
            removeAll("networkClients");
            removeAll("colorCameraImages");
            removeAll("depthCameraImages");
            removeAll("the webcam");
            memory.writeObject(dict);
        } catch (IOException ex) {
            Logger.getLogger(AIBaseMemory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    
    /*public void Import(String file){
        try {
            ObjectInputStream memory = new ObjectInputStream(new FileInputStream(new File(file + ".dict")));
            dict = (ConcurrentHashMap<String, List<Info>>) memory.readObject();
        } catch (IOException | ClassNotFoundException ex){
            Logger.getLogger(AIBaseMemory.class.getName()).log(Level.SEVERE, null, ex);
            ImportTxt(file);
        }
    }*/
    
    /*public void ImportTxt(String file){
        try {
            BufferedReader memory = new BufferedReader(new FileReader(file + ".TXT"));
            String line;
            while((line = memory.readLine()) != null){
                String[] pair = line.split("::");
                if (pair.length == 2)
                    addInfo(new Info(pair[1]), pair[0]);
                else
                    addInfo(new Info(""), pair[0]);
            memory.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(AIBaseMemory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}