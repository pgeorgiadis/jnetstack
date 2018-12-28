package test.junit.protocol;

import test.Ping;
import net.core.NetCore;
import net.core.NetworkDevice;
import net.core.NetworkException;
import net.protocol.ethernet.EthernetDevice;
import net.protocol.ip.ipv4.IPv4Interface;
import junit.framework.TestCase;

public class ICMPTest extends TestCase {
	NetCore stack1;
	EthernetDevice dev1;
	NetworkDevice dev3;
	IPv4Interface iface1;
	IPv4Interface iface3;
	
	public void setUp() {
		stack1 = new NetCore();
		
		try {
			dev1 = (EthernetDevice)stack1.getNetworkDevice("ve0");
			dev3 = stack1.getNetworkDevice("lo0");
		} catch (NetworkException e) {
			System.out.println("FATAL ERROR!");
			System.out.println(e.getMessage());
			return;
		}
		dev1.setModule(stack1.ethernet);
		dev3.setModule(stack1.ip);
		
		iface1 = new IPv4Interface(new int[] {192, 168, 0, 1}, stack1.ethernet, dev1);
		iface3 = new IPv4Interface(new int[] {127, 0, 0, 1}, dev3, dev3);
		stack1.ip.addInterface(iface1);
		stack1.ip.addInterface(iface3);
	}
	
	public void testPINGtoSameLocalVE() {
		try {
			Ping ping = new Ping(iface1.getAddress(), iface1.getAddress());
			stack1.icmp.addListener(ping);
			
			ping.send(stack1);
			Thread.sleep(2000);
			assertEquals(true, ping.done);
		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	public void testPINGtoLOOPBACK() {
		try {
			Ping ping = new Ping(iface3.getAddress(), iface3.getAddress());
			stack1.icmp.addListener(ping);
			
			ping.send(stack1);
			Thread.sleep(2000);
			assertTrue(ping.done);
		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
