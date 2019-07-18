package test;

import AI.AIBaseMemory;
import AI.Models.Vector3D;
import AI.Models.Info;
import AI.util.PID;

import org.junit.Test;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;

/**
 *
 * @author X. Wang
 */
public class JUnitTest extends TestCase{
    private final static AIBaseMemory MEMORY = new AIBaseMemory();
    
    public JUnitTest(){
    }
    
    @Test
    public void testEmotions(){
        System.out.println("* JUnitTest: testEmotions()");
        MEMORY.addEmotion();
        assertEquals(MEMORY.getEmotion(), 0.1);
    }
    
    @Test
    public void testLogpath(){
        System.out.println("* JUnitTest: testLogpath()");
        assertEquals(MEMORY.getLogPath(), null);
        MEMORY.setLogPath("test");
        assertEquals(MEMORY.getLogPath(), "test");
    }
    
    @Test
    public void testVector3D(){
        Vector3D vector = new Vector3D(1.0, 1.0, 1.0);
        System.out.println("* JUnitTest: testVector3D()");
        assertEquals(vector.Dot(vector), 3.0);
    }
    
    @Test
    public void testPID(){
        PID pid = new PID(1, 1, 1);
        System.out.println("* JUnitTest: testPID()");
        double y = pid.Compute(1, 1, 1);
        assertEquals(y, - 3.0);
        y = pid.Compute(10, 20, 1);
        assertEquals(y, - 31.0);
        y = pid.Compute(20, 30, 1);
        assertEquals(y, -71.0);
        y = pid.Compute(50, 20, 1);
        assertEquals(y, - 150.0);
        y = pid.Compute(- 150, 20, 1);
        assertEquals(y, 150.0);
        y = pid.Compute(0, 20, 1);
        assertEquals(y, 69.0);
        y = pid.Compute(10, 10, 10);
        assertEquals(y, - 51.0);
        y = pid.Compute(10, 10, 10);
        assertEquals(y, - 150.0);
    }
    
    @Test
    public void testInfo(){
        Info info = new Info("test");
        System.out.println("* JUnitTest: testInfo()");
        assertEquals(info.getPayload(), "test");
    }
}
