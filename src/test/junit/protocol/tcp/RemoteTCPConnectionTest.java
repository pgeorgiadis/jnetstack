package test.junit.protocol.tcp;

import net.core.NetCore;
import net.core.NetworkException;
import net.protocol.arp.ARPConstants;
import net.protocol.arp.ARPException;
import net.protocol.ethernet.EthernetDevice;
import net.protocol.ip.ipv4.IPv4Interface;
import net.protocol.ip.ipv4.IPv4Packet;
import net.protocol.ip.tcp.socket.TCPSocketConstants;
import net.protocol.ip.tcp.socket.TCPSocket;
import junit.framework.TestCase;

public class RemoteTCPConnectionTest extends TestCase {
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
	
	public void testConnect() {
		System.out.println("starting test: Connect");
		TCPSocket socket1 = new TCPSocket(stack1.tcp, iface1.getAddress(), iface2.getAddress(), 10, 10);
		TCPSocket socket2 = new TCPSocket(stack2.tcp, iface2.getAddress(), iface1.getAddress(), 10, 10);
		
		socket1.connect();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {e.printStackTrace();}
		
		assertEquals(TCPSocketConstants.ESTABLISHED, socket1.getStatus());
		assertEquals(TCPSocketConstants.ESTABLISHED, socket2.getStatus());
		System.out.println("test ended");
	}
	
	public void testDisconnect() {
		System.out.println("starting test: Disconnect");
		TCPSocket socket1 = new TCPSocket(stack1.tcp, iface1.getAddress(), iface2.getAddress(), 10, 10);
		TCPSocket socket2 = new TCPSocket(stack2.tcp, iface2.getAddress(), iface1.getAddress(), 10, 10);
		
		socket1.connect();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {e.printStackTrace();}
		
		assertEquals(TCPSocketConstants.ESTABLISHED, socket1.getStatus());
		assertEquals(TCPSocketConstants.ESTABLISHED, socket2.getStatus());
		
		socket1.disconnect();
		try {Thread.sleep(2000);
		} catch (InterruptedException e) {e.printStackTrace();}
		socket2.disconnect();
		try {Thread.sleep(2000);
		} catch (InterruptedException e) {e.printStackTrace();}
		
		assertEquals(TCPSocketConstants.TIME_WAIT, socket1.getStatus());
		assertEquals(TCPSocketConstants.CLOSED, socket2.getStatus());
		
		System.out.println("Waiting for 2MSL timer (It will take about 1 minute)");
		
		try {
			Thread.sleep(65000);
		} catch (InterruptedException e) {e.printStackTrace();}
		
		assertEquals(TCPSocketConstants.CLOSED, socket1.getStatus());
		assertEquals(TCPSocketConstants.CLOSED, socket2.getStatus());
		System.out.println("test ended");
	}
	
	public void testReset() {
		System.out.println("starting test: Reset");
		TCPSocket socket1 = new TCPSocket(stack1.tcp, iface1.getAddress(), iface2.getAddress(), 10, 10);
		TCPSocket socket2 = new TCPSocket(stack2.tcp, iface2.getAddress(), iface1.getAddress(), 10, 10);
		
		socket1.connect();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {e.printStackTrace();}
		
		assertEquals(TCPSocketConstants.ESTABLISHED, socket1.getStatus());
		assertEquals(TCPSocketConstants.ESTABLISHED, socket2.getStatus());
		
		socket1.reset();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {e.printStackTrace();}
		
		assertEquals(TCPSocketConstants.LISTEN, socket1.getStatus());
		assertEquals(TCPSocketConstants.LISTEN, socket2.getStatus());
		System.out.println("test ended");
	}
}