package test.junit.container;

import net.core.packet.container.BitMask;
import junit.framework.TestCase;

public class BitMaskTest extends TestCase {
	public void testCreate() {
		for (int l=1; l<4; l++) {
			byte i = 0;
			do {
				byte[] b1 = new byte[l];
				for (int j=0; j<b1.length; j++) {
					b1[j] = i;
				}
				BitMask m1 = new BitMask(b1);
				byte[] r1 = m1.toByteArray();
				for (int j=0; j<b1.length; j++) {
					assertEquals(b1[j], r1[j]);
				}
				i++;
			} while(i != 0);
		}
	}
	
	public void testSetGetBit() {
		byte[] b = new byte[] {0, 1, 2};
		BitMask m = new BitMask(b);
		
		assertFalse(m.getBit(5));
		assertTrue(m.getBit(15));
		assertTrue(m.getBit(22));
		m.setBit(5, true);
		assertTrue(m.getBit(5));
		m.setBit(15, false);
		assertFalse(m.getBit(15));
	}
	
	public void testSetEvaluate() {
		byte[] b = new byte[] {0, 1, 2};
		BitMask m = new BitMask(b);
		
		assertEquals(0, m.evaluate(2, 13));
		assertEquals(1, m.evaluate(12, 4));
		assertEquals(2, m.evaluate(12, 5));
		assertEquals(258, m.evaluate(14, 10));
		
		for (int i=0; i<1024; i++) {
			m.set(5, 10, i);
			assertEquals(i, m.evaluate(5, 10));
		}
	}
	
	public void testOverlayedSets() {
		byte[] b1 = new byte[] {0};
		
		BitMask m1 = new BitMask(b1);
		m1.set(0, 4, 4);
		byte[] b2 = m1.toByteArray();
		
		BitMask m2 = new BitMask(b2);
		m2.set(4, 4, 5);
		byte[] b3 = m2.toByteArray();
		
		assertEquals(69, b3[0]);
	}
}