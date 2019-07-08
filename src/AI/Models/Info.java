package AI.Models;

import java.io.Serializable;
//import org.opencv.core.Mat;
//import org.opencv.videoio.VideoCapture;

/**
 *
 * @author X. Wang
 */
public class Info implements Serializable{
    private final String name;
    //private final Mat image;
    //private final Computer computer;
    private final long t = System.currentTimeMillis();
    //private final VideoCapture cap;
    
    /*public Info(VideoCapture cap){
        this.cap = cap;
        image = null;
        computer = null;
        name = null;
    }*/
    
    public Info(String name){
        this.name = name;
        //image = null;
        //computer = null;
        //cap = null;
    }
    
    /*public Info(Mat image){
        this.image = image;
        name = null;
        computer = null;
        cap = null;
    }*/
    
    public String getPayload(){
        return name;
    }
    
    /*public Mat getImage(){
        return image;
    }*/
   
    
    public long getTime(){
        return t;
    }
    
    /*public void close(){
        if(cap != null)
            cap.release();
    }*/
}
