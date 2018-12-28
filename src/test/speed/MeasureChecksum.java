package test.speed;

import java.util.Random;

import net.core.packet.Packet;
import net.core.packet.container.PacketContainer;

public class MeasureChecksum {
	public static void main(String[] args) {
		int times = 10000000;
		System.out.println(measureChecksum(times));
		System.out.println(measureChecksum1(times));
		System.out.println(measureChecksum2(times));
		System.out.println(measureChecksum3(times));
		System.out.println(measureChecksum4(times));
		System.out.println(measureRealisticIndirect(times));
		System.out.println(measureRealisticDirect(times));
	}
	
	public static String measureChecksum(int times) {
		long start, stop;
		byte[] b = initializeArray();
		start = System.currentTimeMillis();
		for (int i=0; i<times; i++) Packet.checksum(b);
		stop = System.currentTimeMillis();
		return "Checksum speed is "+times/(stop-start)+" checksums/ms";
	}
	
	public static String measureChecksum1(int times) {
		long start, stop;
		byte[] b = initializeArray();
		start = System.currentTimeMillis();
		for (int i=0; i<times; i++) Packet.checksum1(b);
		stop = System.currentTimeMillis();
		return "Checksum1 speed is "+times/(stop-start)+" checksums/ms";
	}
	
	public static String measureChecksum2(int times) {
		long start, stop;
		byte[] b = initializeArray();
		start = System.currentTimeMillis();
		for (int i=0; i<times; i++) Packet.checksum2(b);
		stop = System.currentTimeMillis();
		return "Checksum2 speed is "+times/(stop-start)+" checksums/ms";
	}
	
	public static String measureChecksum3(int times) {
		long start, stop;
		byte[] b = initializeArray();
		start = System.currentTimeMillis();
		for (int i=0; i<times; i++) Packet.checksum3(b);
		stop = System.currentTimeMillis();
		return "Checksum3 speed is "+times/(stop-start)+" checksums/ms";
	}
	
	public static String measureChecksum4(int times) {
		long start, stop;
		byte[] b = initializeArray();
		start = System.currentTimeMillis();
		for (int i=0; i<times; i++) Packet.checksum4(b);
		stop = System.currentTimeMillis();
		return "Checksum4 speed is "+times/(stop-start)+" checksums/ms";
	}
	
	public static String measureRealisticIndirect(int times) {
		long start, stop;
		byte[] b = initializeArray();
		PacketContainer c = PacketContainer.createContainer(b);
		start = System.currentTimeMillis();
		for (int i=0; i<times; i++) Packet.checksum(c.read(0, 20));
		stop = System.currentTimeMillis();
		return "Realistic Indirect Container checksum speed is "+times/(stop-start)+" checksums/ms";
	}
	
	public static String measureRealisticDirect(int times) {
		long start, stop;
		byte[] b = initializeArray();
		PacketContainer c = PacketContainer.createContainer(b);
		start = System.currentTimeMillis();
		for (int i=0; i<times; i++) Packet.checksum(c, 0, 20);
		stop = System.currentTimeMillis();
		return "Realistic Direct Container checksum speed is "+times/(stop-start)+" checksums/ms";
	}

	private static byte[] initializeArray() {
		Random r = new Random();
		byte[] b = new byte[20];
		r.nextBytes(b);
		b[10] = 0;
		b[11] = 0;
		return b;
	}
}