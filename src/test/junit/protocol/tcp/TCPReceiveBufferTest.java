package test.junit.protocol.tcp;

import java.io.IOException;
import java.util.Random;

import net.protocol.ip.tcp.socket.TCPReceiveBuffer;
import junit.framework.TestCase;

public class TCPReceiveBufferTest extends TestCase {
	public void testWrite() {
		TCPReceiveBuffer s = new TCPReceiveBuffer(0);
		
		assertEquals(0, s.available());
		
		byte[] b = {1, 2, 3, 4, 5, 6};
		assertEquals(6, s.put(b, 0));
		assertEquals(6, s.available());
		assertEquals(1018, s.availableSpace());
		
		Random r = new Random();
		b = new byte[1000];
		r.nextBytes(b);
		assertEquals(1006, s.put(b, 6));
		assertEquals(1006, s.available());
		assertEquals(18, s.availableSpace());
		
		b = new byte[17];
		r.nextBytes(b);
		assertEquals(1023, s.put(b, 1006));
		assertEquals(1023, s.available());
		assertEquals(1, s.availableSpace());
	}
	
	public void testReadWrite() {
		try {
			TCPReceiveBuffer s = new TCPReceiveBuffer(0);
			
			assertEquals(0, s.available());
			assertEquals(1024, s.availableSpace());
			
			byte[] b = {1, 2, 3, 4, 5, 6};
			assertEquals(6, s.put(b, 0));
			assertEquals(6, s.available());
			assertEquals(1018, s.availableSpace());
			
			byte[] r = new byte[6];
			s.read(r);
			assertEquals(0, s.available());
			assertEquals(1024, s.availableSpace());
			for (int i=0; i<b.length; i++)
				assertEquals(b[i], r[i]);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testSequences() {
		try{
			TCPReceiveBuffer s = new TCPReceiveBuffer(0);
			
			assertEquals(0, s.available());
			
			byte[] b = {1, 2, 3, 4, 5, 6};
			assertEquals(6, s.put(b, 0));
			assertEquals(6, s.available());
			assertEquals(1018, s.availableSpace());
			
			byte[] b2 = {10, 11, 12};
			assertEquals(6, s.put(b2, 9));
			assertEquals(6, s.available());
			assertEquals(1018, s.availableSpace());
			
			byte[] b3 = {7, 8, 9};
			assertEquals(12, s.put(b3, 6));
			assertEquals(12, s.available());
			assertEquals(1012, s.availableSpace());
			
			byte[] r = new byte[12];
			s.read(r);
			
			assertEquals(0, s.available());
			assertEquals(1024, s.availableSpace());
			for (int i=0; i<b.length; i++)
				assertEquals(b[i], r[i]);
			assertEquals(0, s.available());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
}