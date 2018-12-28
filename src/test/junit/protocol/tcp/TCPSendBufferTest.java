package test.junit.protocol.tcp;

import java.io.IOException;
import java.util.Random;

import net.protocol.ip.tcp.socket.TCPSendBuffer;
import junit.framework.TestCase;

public class TCPSendBufferTest extends TestCase {
	public void testWrite() {
		try {
			TCPSendBuffer s = new TCPSendBuffer();
			
			assertEquals(0, s.availableData());
			
			byte[] b = {1, 2, 3, 4, 5, 6};
			s.write(b);
			assertEquals(6, s.availableData());
			
			s.write(50);
			assertEquals(7, s.availableData());
			
			Random r = new Random();
			b = new byte[1000];
			r.nextBytes(b);
			s.write(b);
			assertEquals(1007, s.availableData());
			
			b = new byte[17];
			r.nextBytes(b);
			s.write(b);
			assertEquals("Warning write opperation possibly overwrites unreaded data - ", 1024, s.availableData());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testReadWrite() {
		try {
			TCPSendBuffer s = new TCPSendBuffer();
			
			int a = s.availableData();
			assertEquals(0, a);
			
			byte[] b = {1, 2, 3, 4, 5, 6};
			s.write(b);
			assertEquals(6, s.availableData());
			byte[] r = s.get(1, 6);
			assertEquals(b.length, r.length);
			for (int i=0; i<b.length; i++)
				assertEquals(b[i], r[i]);
			assertEquals(6, s.availableData());
			assertEquals(1, s.availableData(6));
			
			s.acknowlege(5);
			assertEquals(1, s.availableData());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testSequences() {
		try {
			TCPSendBuffer s = new TCPSendBuffer();
			
			assertEquals(0, s.availableData());
			
			byte[] b = {1, 2, 3, 4, 5, 6};
			s.write(b);
			assertEquals(6, s.availableData());
			assertEquals(1, s.availableData(6));
			
			byte[] c = {7, 8, 9, 10, 11, 12};
			s.write(c);
			assertEquals(12, s.availableData());
			assertEquals(7, s.availableData(6));
			
			byte[] r = s.get(6);
			s.acknowlege(6);
			assertEquals(6, s.availableData());
			assertEquals(6, s.availableData(7));
			assertEquals(1, s.availableData(12));
			assertEquals(b.length, r.length);
			for (int i=0; i<6; i++)
				assertEquals(b[i], r[i]);
			
			r = s.get(8, 3);
			s.acknowlege(8);
			assertEquals(4, s.availableData());
			assertEquals(4, s.availableData(9));
			assertEquals(3, r.length);
			for (int i=0; i<3; i++)
				assertEquals(c[i+1], r[i]);
		} catch (IOException e) {}
	}
}