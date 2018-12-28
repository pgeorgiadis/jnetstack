package test.speed;

import java.util.Random;

public class ArrayEqualityTest {
	byte[] a, b;
	
	public ArrayEqualityTest() {
		Random r = new Random();
		a = new byte[4];
		b = new byte[4];
		r.nextBytes(a);
		r.nextBytes(b);
	}
	
	public static void main(String[] args) {
		int repeats = 1000000000;
		long start, stop;
		float time;
		
		ArrayEqualityTest test = new ArrayEqualityTest();
		
		start = System.currentTimeMillis();
		for (int i=0; i<repeats; i++)
			test.forLoopCheck();
		stop = System.currentTimeMillis();
		time = (stop-start);
		System.out.println("forLoopCheck time: "+time);
		
		start = System.currentTimeMillis();
		for (int i=0; i<repeats; i++)
			test.fourChecks();
		stop = System.currentTimeMillis();
		time = (stop-start);
		System.out.println("forLoopCheck time: "+time);
		
		start = System.currentTimeMillis();
		for (int i=0; i<repeats; i++)
			test.converted();
		stop = System.currentTimeMillis();
		time = (stop-start);
		System.out.println("forLoopCheck time: "+time);
	}
	
	public boolean forLoopCheck() {
		for (int i=0; i<4; i++)
			if (a[i] != b[i]) return false;
		return true;
	}
	
	public boolean fourChecks() {
		if (a[0] != b[0]) return false;
		if (a[1] != b[1]) return false;
		if (a[2] != b[2]) return false;
		if (a[3] != b[3]) return false;
		return true;
	}
	
	public boolean converted() {
		int aa = (((a[0] & 0xff) << 24) | ((a[1] & 0xff) << 16) | ((a[2] & 0xff) << 8) | (a[3] & 0xff));
		int bb = (((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16) | ((b[2] & 0xff) << 8) | (b[3] & 0xff));
		if (aa == bb) return true;
		else return false;
	}
}