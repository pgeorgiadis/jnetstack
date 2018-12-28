package test.junit.container;

import net.core.packet.Packet;
import junit.framework.TestCase;

public class ChecksumTest extends TestCase {
	public void test1() {
		String[] s = {
				"45", "00", "05", "d0", "15", "8e", "40", "00", "70", "06",
				"b9", "02", "57", "00", "d5", "11", "0a", "83", "00", "03"
				};
		byte[] b = new byte[20];
		for(int i=0; i<20; i++) {
			int tmp = Integer.decode("0x"+s[i]).byteValue();
			b[i] = (byte)(tmp & 0xff);
		}
		print(b);
	}
	
	public void test2() {
		byte[] b = {5, 0, 0, 64, 22, 80, 0, 0, -128, 1, 15, 99, 10, -125, 0, 3, 10, -125, 0, 6};
		print(b);
	}
	
	private void print(byte[] b) {
		for (int i=0; i<20; i++)
			System.out.println(b[i++]+"\t"+b[i++]+"\t"+b[i++]+"\t"+b[i]);
		System.out.println(Packet.checksum(b));
		System.out.println(Packet.checksum1(b));
		System.out.println(Packet.checksum2(b));
		System.out.println(Packet.checksum3(b));
		System.out.println(Packet.checksum4(b));
		System.out.println();
	}
}