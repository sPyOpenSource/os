
package test;

import AI.util.Sha1;
import java.util.logging.Level;
import java.util.logging.Logger;

import jx.emulation.Init;
import jx.zero.debug.Dump;
import jx.fs.buffercache.BufferHead;
import jx.bio.buffercache.BufferHashtable;

import org.junit.Test;
import org.junit.Before;
import static junit.framework.TestCase.assertEquals;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	//MemoryManager memMgr = (MemoryManager)InitialNaming.getInitialNaming().lookup("MemoryManager");
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
    
    @Test
    public void testSHA1(){
        String testString = "Hello World";
        System.out.println("Input:  " + testString);
        // 1. Get the SHA-1 instance
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(JUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // 2. Perform the hash
        byte[] hashBytes = digest.digest(testString.getBytes(StandardCharsets.UTF_8));
        
        // 3. Convert bytes to Hex string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        assertEquals(Sha1.hash(testString), hexString.toString());
        Matcher get = Pattern.compile("^GET").matcher("Sec-WebSocket-Key: 1111");
        Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher("Sec-WebSocket-Key: 1111");
        match.find();
        String accept = Base64.getEncoder().encodeToString(
            digest.digest(
            (match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11")
                .getBytes(StandardCharsets.UTF_8)
            )
        );
        String accept2 = AI.util.Base64.encode(
            Sha1.hexToBytes(Sha1.hash(match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11"))
        );
        assertEquals(accept,accept2);
    }
}
