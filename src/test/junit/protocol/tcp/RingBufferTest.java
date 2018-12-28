package test.junit.protocol.tcp;

import java.util.Random;

import junit.framework.TestCase;

import net.protocol.ip.tcp.socket.RingBuffer;

public class RingBufferTest extends TestCase {
	public void testWrite() {
		RingBuffer a = new RingBuffer(1024);
		
		assertEquals(0, a.availableBytes());
		assertEquals(1024, a.availableSpace());
		
		byte[] b = {1, 2, 3, 4, 5, 6};
		a.write(b, 0, true);
		assertEquals(6, a.availableBytes());
		assertEquals(1018, a.availableSpace());
		
		Random r = new Random();
		b = new byte[1018];
		r.nextBytes(b);
		System.out.println(a.availableSpace());
		a.write(b, 0, true);
		
		assertEquals(1024, a.availableBytes());
		assertEquals(0, a.availableSpace());
	}
	
	public void testReadWrite() {
		RingBuffer a = new RingBuffer(1024);
		Random rnd = new Random();

		byte[] expected = new byte[317];
		try {
			for (int j = 0; j < 100; j++) {
				rnd.nextBytes(expected);

				synchronized(a) {
					a.write(expected, 0, true);

					int l1 = (int)(Math.random() * expected.length);
					byte[] result = a.read(l1, 0, true);
					for (int i = 0; i < result.length; i++) {
						assertEquals(expected[i], result[i]);
					}
					int l2 = expected.length - l1;
					result = a.read(l2, 0, true);
					for (int i = 0; i < result.length; i++) {
						assertEquals(expected[l1 + i], result[i]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	public void testConcurency() {
		final RingBuffer a = new RingBuffer(1024);
		
		Thread reader = new Thread() {
			public void run() {
				for (int i=0; i<1000; i++) {
					a.read(512, 0, true);
					System.out.println("reader-"+i);
				}
			}
		};
		
		Thread writer = new Thread() {
			public void run() {
				for (int i=0; i<4000; i++) {
					a.write(new byte[128], 0, true);
					System.out.println("writer-"+i);
				}
			}
		};
		
		reader.start();
		writer.start();
		
		try {
			writer.join();
			reader.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}