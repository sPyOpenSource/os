package AI;
        
/**
 * This is the memory class of AI.
 * 
 * @author X. Wang 
 * @version 1.0
 */
public class AIMemory extends AIBaseMemory
{
    // instance variables
    //private SerialPort serialPort;
    
    /**
     * Constructor for objects of class AIMemory
     */
    public AIMemory()
    {
        // Initialize instance variables
        /*try {
            serialPort = (SerialPort)CommPortIdentifier.getPortIdentifier("/dev/ttyACM0").open(this.getClass().getName(), 2000);
            serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE); 
        } catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException ex) {
            Logger.getLogger(AIMemory.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    /*public SerialPort getSerialPort(){
        return serialPort;
    }*/
}