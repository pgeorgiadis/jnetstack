package test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TestServer {
	public static void main(String args[]) throws Exception {
		ServerSocket ss = new ServerSocket(1050);
		Socket s = ss.accept();
		
		System.out.println("accepted");
		
		OutputStream out = s.getOutputStream();
		InputStream in = s.getInputStream();
		
//		FileInputStream fis = new FileInputStream("/mnt/storage2/iso/jnode-x86-0.2.1.iso");
		Random rnd = new Random();
		
		long counter = 0;
		long start = System.currentTimeMillis();
		long stop;
//		while(fis.available() > 0) {
		while(true) {
//			int a = fis.available();
//			if (a > 1024)
//				a = 1024;
			
			int a = 1024;
			
			counter += a;
			
			byte[] b = new byte[a];
//			fis.read(b);
			rnd.nextBytes(b);
			out.write(b);
			
			stop = System.currentTimeMillis();
			
			byte[] c = new byte[a];
			in.read(c);
			for(int i = 0; i<a; i++) {
				if (b[i] != c[i]) {
					System.out.println("Mismatch in position "+(counter+i)+" send="+b[i]+" received="+c[i]);
				}
			}
			
			System.out.println((counter*8000/(stop-start))+" bps");
		}
	}
}
