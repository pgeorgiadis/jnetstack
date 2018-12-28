package net.core.packet.container;

import java.util.Random;


import junit.framework.TestCase;

public class PacketContainerTest extends TestCase {
	PacketContainer container;
	Random r = new Random();
	
	public void testSimpleRW() {
		for (int i=0; i<250; i++) {
			container.write(4, i);
			assertEquals(i, container.read(4));
		}
	}
	
	public void testSimpleRW16() {
		for (int i=0; i<65536; i++) {
			container.write16(4, i);
			int a = container.read16(4);
			assertEquals(i, a);
		}
	}
	
	public void testSimpleRWArray() {
		int l = container.getSize();
		
		byte[] a = new byte[l];
		r.nextBytes(a);
		container.write(0, a, 0, l);
		byte[] b1 = container.read(0, l);
		for (int i=0; i<l; i++) {
			assertEquals(a[i], b1[i]);
		}
		
		try{
			container.write(l/2, a, 0, l);
		}catch(Exception e) {
			assertTrue(true);
		}
	}
	
	public void testUnsignedRW() {
		byte i = 0;
		do {
			container.u_write(4, i);
			assertEquals(i, container.u_read(4));
			i++;
		} while(i != 0);
	}
	
	public void testUnsignedRW16() {
		short i = 0;
		do {
			container.u_write16(4, i);
			assertEquals(i, container.u_read16(4));
			i++;
		} while(i != 0);
	}
	
	public void testUnsignedRW32() {
		for (int i=0; i<65538; i++) {
			container.u_write32(4, i);
			assertEquals(i, container.u_read32(4));
			i++;
		}
		for (int i=16777210; i<16777218; i++) {
			container.u_write32(4, i);
			assertEquals(i, container.u_read32(4));
			i++;
		}
		int i = 2147483640;
		do {
			container.u_write32(4, i);
			assertEquals(i, container.u_read32(4));
			i++;
		} while (i != -2147483640);
	}
}