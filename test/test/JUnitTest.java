/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import static junit.framework.TestCase.assertEquals;
import org.junit.Test;

/**
 *
 * @author spy
 */
public class JUnitTest {
    @Test
    public void testAssert(){
        System.out.println("* JUnitTest: assert");
        assertEquals(0, 0);
    }
}
