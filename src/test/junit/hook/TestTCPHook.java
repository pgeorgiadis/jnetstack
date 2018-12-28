package test.junit.hook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Random;

import junit.framework.TestCase;
import net.core.NetCore;
import net.core.NetworkException;
import net.hook.tcpsocket.MySocket;
import net.hook.tcpsocket.SocketImplHook;
import net.protocol.arp.ARPConstants;
import net.protocol.arp.ARPException;
import net.protocol.ethernet.EthernetDevice;
import net.protocol.ip.ipv4.IPv4Interface;
import net.protocol.ip.ipv4.IPv4Packet;

public class TestTCPHook extends TestCase {
	NetCore stack1;
	NetCore stack2;
	EthernetDevice dev1_1;
	EthernetDevice dev2_1;
	IPv4Interface iface1;
	IPv4Interface iface2;
	
	public void setUp() {
		stack1 = new NetCore();
		stack2 = new NetCore();
		
		try {
			dev1_1 = (EthernetDevice)stack1.getNetworkDevice("ve0");
			dev2_1 = (EthernetDevice)stack2.getNetworkDevice("ve1");
		} catch (NetworkException e) {
			System.out.println("FATAL ERROR!");
			System.out.println(e.getMessage());
			return;
		}
		dev1_1.setModule(stack1.ethernet);
		dev2_1.setModule(stack2.ethernet);
		
		iface1 = new IPv4Interface(new int[] {192, 168, 0, 1}, stack1.ethernet, dev1_1);
		iface2 = new IPv4Interface(new int[] {192, 168, 0, 2}, stack2.ethernet, dev2_1);
		stack1.ip.addInterface(iface1);
		stack2.ip.addInterface(iface2);
		
		try {
			IPv4Packet p = new IPv4Packet(10);
			p.setSrcAddress(iface1.getAddress());
			p.setDstAddress(iface2.getAddress());
			stack1.arp.resolve(p, ARPConstants.P_IPv4, ARPConstants.H_ETHERNET);
			
			p.setSrcAddress(iface2.getAddress());
			p.setDstAddress(iface1.getAddress());
			stack2.arp.resolve(p, ARPConstants.P_IPv4, ARPConstants.H_ETHERNET);
		} catch (ARPException e) {e.printStackTrace();}
	}
	
	public void testTCPHook() {
		try {
			SocketImplHook impl1 = new SocketImplHook(stack1);
			SocketImplHook impl2 = new SocketImplHook(stack2);
			
			MySocket socket1 = new MySocket(impl1);
			MySocket socket2 = new MySocket(impl2);
			
			InetSocketAddress addr1 = 
				new InetSocketAddress(InetAddress.getByAddress(iface2.getAddress().getAddress()), 1025);
			InetSocketAddress addr2 = 
				new InetSocketAddress(InetAddress.getByAddress(iface1.getAddress().getAddress()), 1025);
			
			socket1.connect(addr1);
			socket2.connect(addr2);
			
			OutputStream out1 = socket1.getOutputStream();
			InputStream in1 = socket1.getInputStream();
			
			OutputStream out2 = socket2.getOutputStream();
			InputStream in2 = socket2.getInputStream();
			
			transfer(out1, in2, 5);
			transfer(out2, in1, 5);
			transfer(out1, in2, 5);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	private void transfer(OutputStream out, InputStream in, int times) throws IOException, InterruptedException {
		for (int i=0; i<times; i++) {
			Random rnd = new Random();
			byte[] b = new byte[10];
			rnd.nextBytes(b);
			out.write(b);
			byte[] r = new byte[10];
			in.read(r);
			for (int j=0; j<r.length; j++)
				assertEquals(b[j], r[j]);
		}
	}
}
