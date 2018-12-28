package test.junit.protocol;

import net.protocol.ethernet.EthernetAddress;
import junit.framework.TestCase;

public class EthernetTest extends TestCase {
	public void testMacParser() {
		assertEquals(0, EthernetAddress.hexToByte("00"));
		assertEquals(1, EthernetAddress.hexToByte("01"));
		assertEquals(15, EthernetAddress.hexToByte("0f"));
		assertEquals(16, EthernetAddress.hexToByte("10"));
		assertEquals(17, EthernetAddress.hexToByte("11"));
		assertEquals(17, EthernetAddress.hexToByte("11"));
		assertEquals(~0, EthernetAddress.hexToByte("ff"));
	}
}