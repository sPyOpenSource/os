package AI.util;

/**
 *
 * @author X. Wang
 */
public class PID {
    private final double kp, ki, kd;
    private double temp, max, min, setpoint;
    
    public PID(double kp, double ki, double kd){
        this.kp  = kp;
        this.ki  = ki;
        this.kd  = kd;
        max      = 150;
        min      = -150;
        temp     = 0;
        setpoint = 0;
    }
    
    public void setRange(double max, double min){
        this.max = max;
        this.min = min;
    }
    
    public void setPoint(double setpoint){
        this.setpoint = setpoint;
    }
    
    public double Compute(double x, double v, double dt){
        double error = setpoint - x;
        temp += error * dt * ki;
        if(temp > max)
            temp = max;
        else if (temp < min)
            temp = min;
        double output = temp + error * kp - x * kd;
        if (output > max)
            output = max;
        else if(output < min)
            output = min;
        return output;
    }
}
