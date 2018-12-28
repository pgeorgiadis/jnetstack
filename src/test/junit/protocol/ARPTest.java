package test.junit.protocol;

import net.core.NetCore;
import net.core.NetworkException;
import net.protocol.arp.ARPConstants;
import net.protocol.ethernet.EthernetAddress;
import net.protocol.ethernet.EthernetDevice;
import net.protocol.ip.ipv4.IPv4Interface;
import net.protocol.ip.ipv4.IPv4Packet;
import junit.framework.TestCase;

public class ARPTest extends TestCase {
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
	
	public void testFindAddress() {
		try {
			IPv4Packet packet = new IPv4Packet(10);
			packet.setSrcAddress(iface1.getAddress());
			packet.setDstAddress(iface2.getAddress());
			
			EthernetAddress[] expected = new EthernetAddress[] {
					new EthernetAddress(new byte[] {11, 11, 11, 22, 22, 22}),
					new EthernetAddress(new byte[] {33, 33, 33, 44, 44, 44})
			};
			EthernetAddress[] result = core.arp.resolve(packet, ARPConstants.P_IPv4, ARPConstants.H_ETHERNET);
			
			assertTrue(result != null);
			assertEquals(expected[0], result[0]);
			assertEquals(expected[1], result[1]);
		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}