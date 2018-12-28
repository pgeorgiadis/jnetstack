package test.junit.hook;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import net.core.NetCore;
import net.core.NetworkDevice;
import net.core.NetworkException;
import net.hook.udpsocket.DatagramSocketImplHook;
import net.hook.udpsocket.MyDatagramSocket;
import net.protocol.ethernet.EthernetDevice;
import net.protocol.ip.ipv4.IPv4Interface;
import junit.framework.TestCase;

public class TestUDPHook extends TestCase {
	NetCore core = new NetCore();
	EthernetDevice dev1;
	EthernetDevice dev2;
	NetworkDevice dev3;
	IPv4Interface iface1;
	IPv4Interface iface2;
	IPv4Interface iface3;
	
	public void setUp() {
		try {
			dev1 = (EthernetDevice)core.getNetworkDevice("ve0");
			dev2 = (EthernetDevice)core.getNetworkDevice("ve1");
			dev3 = core.getNetworkDevice("lo0");
		} catch (NetworkException e) {
			System.out.println("FATAL ERROR!");
			System.out.println(e.getMessage());
			return;
		}
		dev1.setModule(core.ethernet);
		dev2.setModule(core.ethernet);
		dev3.setModule(core.ip);
		
		iface1 = new IPv4Interface(new int[] {192, 168, 0, 1}, core.ethernet, dev1);
		iface2 = new IPv4Interface(new int[] {192, 168, 0, 2}, core.ethernet, dev2);
		iface3 = new IPv4Interface(new int[] {127, 0, 0, 1}, dev3, dev3);
		core.ip.addInterface(iface1);
		core.ip.addInterface(iface2);
		core.ip.addInterface(iface3);
	}
	
	public void testUDPHook() {
		try {
			DatagramSocketImplHook impl = new DatagramSocketImplHook(core);
			
			MyDatagramSocket socket1 = new MyDatagramSocket(impl);
			MyDatagramSocket socket2 = new MyDatagramSocket(impl);
			
			InetSocketAddress src = 
				new InetSocketAddress(InetAddress.getByAddress(iface1.getAddress().getAddress()), 1025);
			InetSocketAddress dst = 
				new InetSocketAddress(InetAddress.getByAddress(iface2.getAddress().getAddress()), 1025);
			
			socket1.bind(src);
			socket2.bind(dst);
			
			DatagramPacket p = new DatagramPacket("test".getBytes(), 4, dst);
			socket1.send(p);
			Thread.sleep(1000);
			DatagramPacket r = new DatagramPacket("     ".getBytes(), 5);
			socket2.receive(r);
			System.out.println("|"+new String(r.getData())+"|");
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
