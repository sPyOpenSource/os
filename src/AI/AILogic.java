package AI;

import AI.Models.Info;
import AI.Models.Vector3D;
//import AI.util.PointCloud;

/**
 * This is the logic class of AI.
 * 
 * @author X. Wang 
 * @version 1.0
 */
public class AILogic extends AIBaseLogic
{
    // instance variables
    private final double threshold = 1000000, filter = 0.99;
    //private final MotionDetection colorCamera;
    //private final PointCloud depthCamera;
    //private final VectorFilter accFilter, magFilter;

    /**
     * Constructor for objects of class AILogic
     * @param mem
     */
    public AILogic(AIMemory mem)
    {
        // Initialize instance variables        
	super(mem);
        //colorCamera  = new MotionDetection(filter, threshold);
        //depthCamera = new PointCloud();
        /*accFilter = new VectorFilter(10, 10, 0.0001, 0.1, 0.1 / 3);
        magFilter = new VectorFilter(10, 10, 0.0001, 0.1, 0.1 / 3);
        accFilter.init(new Vector3D(0d, 0d, 0d), new Vector3D(0d, 0d, 0d));
        magFilter.init(new Vector3D(0d, 0d, 0d), new Vector3D(0d, 0d, 0d));*/
    }

    private void ProcessImages() {    
        /*if (index % 60 == 0){
            colorCamera.saveBack("/home/spy/color.jpg");
        }
        index++;
        colorCamera.UpdatePosition(mem.dequeFirst("colorCameraImages"));
        depthCamera.Calculate(mem.dequeFirst("depthCameraImages"), colorCamera.getX(), colorCamera.getY());*/
    }

    @Override
    protected void Thread() {
        ProcessImages();
    }

    @Override
    protected void Messages(Info info) {
        /*String[] result = info.getPayload().split(",");
        if (result.length == 9){
            Vector3D gyr = new Vector3D(Double.parseDouble(result[3]), Double.parseDouble(result[4]), Double.parseDouble(result[5]));
            //VectorMat answerAcc = accFilter.Filter(new Vector3D(Double.parseDouble(result[0]), Double.parseDouble(result[1]), Double.parseDouble(result[2])), gyr);
            //mem.addInfo(new Info(answerAcc.getX(0).Display()), "outgoingMessages");
        }*/
    }
}