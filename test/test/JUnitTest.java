/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.logging.Level;
import java.util.logging.Logger;

import jx.emulation.Init;
import jx.zero.debug.Dump;
import jx.zero.MemoryManager;
import jx.fs.buffercache.BufferHead;
import jx.bio.buffercache.BufferHashtable;

import org.junit.Test;
import org.junit.Before;
import static junit.framework.TestCase.assertEquals;
import jx.zero.InitialNaming;

/**
 *
 * @author spy
 */
public class JUnitTest {
    @Before
    public void setUp(){
        try {
            Init.init();
        } catch (Exception ex) {
            Logger.getLogger(JUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testDump(){
        System.out.println("* JUnitTest: Dump");
        assertEquals(Dump.byteToHex((byte)1), "01");
    }
    
    @Test
    public void testBufferHashtable(){
        System.out.println("* JUnitTest: BufferHashtable");
	
	MemoryManager memMgr = (MemoryManager)InitialNaming.getInitialNaming().lookup("MemoryManager");
	BufferHashtable collect = new BufferHashtable();
        
	// fill
	BufferHead bh0, bh1, bh2;
	/*collect.put(bh0 = new BufferHead(memMgr, 0, 1024));
	collect.put(bh1 = new BufferHead(memMgr, 1, 1024));
	collect.put(bh2 = new BufferHead(memMgr, 2, 1024));
	for(int i = 3; i < 1000; i++) {
	    collect.put(new BufferHead(memMgr, i, 1024));
	}*/
	BufferHead c1;
	//collect.put(new BufferHead(memMgr, 3 + 4096, 1024)); // collision
	//collect.put(c1 = new BufferHead(memMgr, 3 + 2 * 4096, 1024)); // collision
	//collect.put(new BufferHead(memMgr, 3 + 3 * 4096, 1024)); // collision
	//collect.put(new BufferHead(memMgr, 3 + 4 * 4096, 1024)); // collision
	BufferHead c2 = collect.get(3 + 2 * 4096); // collision
	//assertEquals(c1, c2);
	
	BufferHead bh = collect.get(1);
	//assertEquals(bh, bh1);
        
	//collect.remove(bh2);
	bh = collect.get(1);
	//assertEquals(bh, bh1);
    }
}
