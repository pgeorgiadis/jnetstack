package test.junit.protocol.tcp;

import net.core.NetCore;
import net.core.NetworkException;
import net.protocol.ethernet.EthernetDevice;
import net.protocol.ip.ipv4.IPv4Interface;
import net.protocol.ip.tcp.socket.TCPSocketConstants;
import net.protocol.ip.tcp.socket.TCPSocket;
import junit.framework.TestCase;

public class LocalTCPConnectionTest extends TestCase {
	NetCore core = new NetCore();
	EthernetDevice dev1;
	EthernetDevice dev2;
	IPv4Interface iface1;
	IPv4Interface iface2;
	
	public void setUp() {
		try {
			dev1 = (EthernetDevice)core.getNetworkDevice("ve0");
			dev2 = (EthernetDevice)core.getNetworkDevice("ve1");
		} catch (NetworkException e) {
			System.out.println("FATAL ERROR!");
			System.out.println(e.getMessage());
			return;
		}
		dev1.setModule(core.ethernet);
		dev2.setModule(core.ethernet);
		
		iface1 = new IPv4Interface(new int[] {192, 168, 0, 1}, core.ethernet, dev1);
		iface2 = new IPv4Interface(new int[] {192, 168, 0, 2}, core.ethernet, dev2);
		core.ip.addInterface(iface1);
		core.ip.addInterface(iface2);
	}
	
	public void testConnect() {
		TCPSocket socket1 = new TCPSocket(core.tcp, iface1.getAddress(), iface2.getAddress(), 10, 10);
		TCPSocket socket2 = new TCPSocket(core.tcp, iface2.getAddress(), iface1.getAddress(), 10, 10);
		
		socket1.connect();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {e.printStackTrace();}
		
		assertEquals(TCPSocketConstants.ESTABLISHED, socket1.getStatus());
		assertEquals(TCPSocketConstants.ESTABLISHED, socket2.getStatus());
	}
	
	public void testDisconnect() {
		TCPSocket socket1 = new TCPSocket(core.tcp, iface1.getAddress(), iface2.getAddress(), 10, 10);
		TCPSocket socket2 = new TCPSocket(core.tcp, iface2.getAddress(), iface1.getAddress(), 10, 10);
		
		socket1.connect();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {e.printStackTrace();}
		
		assertEquals(TCPSocketConstants.ESTABLISHED, socket1.getStatus());
		assertEquals(TCPSocketConstants.ESTABLISHED, socket2.getStatus());
		
		socket1.disconnect();
		try {Thread.sleep(3000);
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
	}
	
	public void testReset() {
		TCPSocket socket1 = new TCPSocket(core.tcp, iface1.getAddress(), iface2.getAddress(), 10, 10);
		TCPSocket socket2 = new TCPSocket(core.tcp, iface2.getAddress(), iface1.getAddress(), 10, 10);
		
		socket1.connect();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {e.printStackTrace();}
		
		assertEquals(TCPSocketConstants.ESTABLISHED, socket1.getStatus());
		assertEquals(TCPSocketConstants.ESTABLISHED, socket2.getStatus());
		
		socket1.reset();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {e.printStackTrace();}
		
		assertEquals(TCPSocketConstants.LISTEN, socket1.getStatus());
		assertEquals(TCPSocketConstants.LISTEN, socket2.getStatus());
	}
}