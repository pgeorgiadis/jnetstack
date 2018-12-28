package test.junit.protocol;

import java.util.Random;

import net.core.NetCore;
import net.core.NetworkException;
import net.core.packet.Packet;
import net.protocol.ethernet.EthernetDevice;
import net.protocol.ip.ipv4.IPv4Address;
import net.protocol.ip.ipv4.IPv4Interface;
import net.protocol.ip.ipv4.IPv4Route;
import junit.framework.TestCase;

public class IPTest extends TestCase {
	NetCore core = new NetCore();
	EthernetDevice dev1;
	EthernetDevice dev2;
	IPv4Interface iface1;
	IPv4Interface iface2;
	
	public void initNet() {
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
		iface2 = new IPv4Interface(new int[] {192, 168, 1, 2}, core.ethernet, dev2);
		core.ip.addInterface(iface1);
		core.ip.addInterface(iface2);
	}
	
	public void testCalculateBroadcast() {
		IPv4Address ip = new IPv4Address(new byte[] {10, 0, 0, 1});
		IPv4Address mask = new IPv4Address(new byte[] {~0, 0, 0, 0});
		IPv4Address broadcast = IPv4Interface.calculateBroadcast(ip, mask);
		IPv4Address expected = new IPv4Address(new byte[] {10, ~0, ~0, ~0});
		assertEquals(expected, broadcast);
	}
	
	public void testIPAddressParser() {
		IPv4Address ip1 = new IPv4Address("10.0.0.1");
		IPv4Address ip2 = new IPv4Address("10.0.0.255");
		assertEquals(new IPv4Address(new byte[] {10, 0, 0, 1}), ip1);
		assertEquals(new IPv4Address(new byte[] {10, 0, 0, ~0}), ip2);
		try{
			new IPv4Address("10.0.0.1lalala");
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	public void testChecksum() {
		Random r = new Random();
		byte[] b = new byte[20];
		
		for (int i=0; i<100; i++) {
			r.nextBytes(b);
			b[10] = 0;
			b[11] = 0;
			
			int sum = Packet.checksum(b);
			b[10] = (byte)(sum >> 8);
			b[11] = (byte)sum;
			int check = Packet.checksum(b);
			assertTrue(check == -65536);
		}
	}
	
	/*public void testIPoEthernet() {
		this.initNet();
		
		try {
			Random r = new Random();
			byte[] b = new byte[10];
			r.nextBytes(b);
			RawPacket expected = new RawPacket(new ByteArrayContainer(b));
			
			IPv4Packet packet = new IPv4Packet(expected.getSize());
			packet.setPayload(expected);
			packet.setDefaults();
			packet.setSrcAddress(iface1.getAddress());
			packet.setDstAddress(iface2.getAddress());
			packet.setIdentification(0);
			packet.setChecksum(0);
			int sum = Packet.checksum(packet.getContainer().read(0, packet.getIHL()*4));
			packet.setChecksum(sum);
			
			core.ethernet.send(packet);
			
			try {
				Thread.sleep(100);
			} catch(Exception e) {}
			Queue q = core.debug.q;
			Object o = q.get();
			RawPacket result = (RawPacket)o;
			
			assertTrue(expected.equals(result));
			for (int i=0; i<result.getSize(); i++) {
				assertEquals(expected.getContainer().u_read(i), result.getContainer().u_read(i));
			}
		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}*/
	
	public void testRouting() {
		try {
			this.initNet();
			
			IPv4Route r;
			r = new IPv4Route(new IPv4Address("192.168.3.0"), new IPv4Address("192.168.1.2"));
			core.ip.getRoutingTable().addRoute(r);
			r = new IPv4Route(new IPv4Address("0.0.0.0"), new IPv4Address("0.0.0.0"), new IPv4Address("192.168.0.4"));
			core.ip.getRoutingTable().addRoute(r);
			
			assertEquals(iface2.getAddress(), core.ip.getRoutingTable().getRoute(new IPv4Address("192.168.3.5")));
			assertEquals(new IPv4Address("192.168.0.4"), core.ip.getRoutingTable().getRoute(new IPv4Address("194.42.54.4")));
			assertEquals(iface2, core.ip.getInterfaceForDestination(new IPv4Address("192.168.3.6")));
			assertEquals(iface1, core.ip.getInterfaceForDestination(new IPv4Address("194.42.54.4")));
		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}