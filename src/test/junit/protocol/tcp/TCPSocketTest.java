package test.junit.protocol.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import net.core.NetCore;
import net.core.NetworkException;
import net.protocol.arp.ARPConstants;
import net.protocol.arp.ARPException;
import net.protocol.ethernet.EthernetDevice;
import net.protocol.ip.ipv4.IPv4Address;
import net.protocol.ip.ipv4.IPv4Interface;
import net.protocol.ip.ipv4.IPv4Packet;
import net.protocol.ip.tcp.socket.TCPSocket;
import junit.framework.TestCase;

public class TCPSocketTest extends TestCase {
	NetCore stack1, stack2;
	EthernetDevice dev1;
	EthernetDevice dev2;
	IPv4Address addr1;
	IPv4Address addr2;
	IPv4Interface iface1;
	IPv4Interface iface2;
	
	public void setUp() {
		stack1 = new NetCore();
		stack2 = new NetCore();
		
		try {
			dev1 = (EthernetDevice)stack1.getNetworkDevice("ve0");
			dev2 = (EthernetDevice)stack2.getNetworkDevice("ve1");
		} catch (NetworkException e) {
			System.out.println("FATAL ERROR!");
			System.out.println(e.getMessage());
			return;
		}
		
		dev1.setModule(stack1.ethernet);
		dev2.setModule(stack2.ethernet);
		
		addr1 = new IPv4Address(new int[] {192, 168, 0, 1});
		addr2 = new IPv4Address(new int[] {192, 168, 0, 2});
		
		iface1 = new IPv4Interface(addr1, stack1.ethernet, dev1);
		iface2 = new IPv4Interface(addr2, stack2.ethernet, dev2);
		
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
	
	public void testSimpleTransmition() {
		TCPSocket s1 = new TCPSocket(stack1.tcp, addr1, addr2, 1025, 1025);
		TCPSocket s2 = new TCPSocket(stack2.tcp, addr2, addr1, 1025, 1025);
		s1.connect();
		
		OutputStream s1_out = (OutputStream)s1.getSender().getBuffer();
		OutputStream s2_out = (OutputStream)s2.getSender().getBuffer();
		InputStream s1_in = (InputStream)s1.getReceiver().getBuffer();
		InputStream s2_in = (InputStream)s2.getReceiver().getBuffer();
		
		try {
			int sent = 1;
			s1_out.write(sent);
			int read = s2_in.read();
			assertEquals(sent, read);
			
			s2_out.write(sent);
			read = s1_in.read();
			assertEquals(sent, read);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		s1.disconnect();
		s2.disconnect();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}
		System.out.println();
	}
	
	public void testTwoByteTransmition() {
		TCPSocket s1 = new TCPSocket(stack1.tcp, addr1, addr2, 1025, 1025);
		TCPSocket s2 = new TCPSocket(stack2.tcp, addr2, addr1, 1025, 1025);
		s1.connect();
		
		OutputStream s1_out = (OutputStream)s1.getSender().getBuffer();
		InputStream s2_in = (InputStream)s2.getReceiver().getBuffer();
		
		try {
			int sent = 1;
			s1_out.write(sent);
			int read = s2_in.read();
			assertEquals(sent, read);
			
			s1_out.write(sent);
			read = s2_in.read();
			assertEquals(sent, read);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testArrayTransfer() {
		TCPSocket s1 = new TCPSocket(stack1.tcp, addr1, addr2, 1025, 1025);
		TCPSocket s2 = new TCPSocket(stack2.tcp, addr2, addr1, 1025, 1025);
		s1.connect();
		
		OutputStream s1_out = (OutputStream)s1.getSender().getBuffer();
		InputStream s2_in = (InputStream)s2.getReceiver().getBuffer();
		
		Random r = new Random();
		byte[] b = new byte[512];
		r.nextBytes(b);
		
		try {
			s1_out.write(b);
			
			for (int i=0; i<512; i++) {
				int read = s2_in.read();
				assertEquals(b[i], read);
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
}
