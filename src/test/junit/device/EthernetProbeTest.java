package test.junit.device;

import test.EthernetProbe;
import net.core.NetCore;
import net.protocol.ethernet.EthernetAddress;
import net.protocol.ip.ipv4.IPv4Interface;

public class EthernetProbeTest {
	EthernetProbe ve0;
	
	public static void main(String[] args) {
		test();
	}
	
	public static void test() {
		try {
			NetCore core = new NetCore();
			
			EthernetAddress mac0 = new EthernetAddress("00:0d:93:72:1b:1a");
			EthernetProbe ve0 = new EthernetProbe(core, "ve0", mac0, "en0");
			ve0.setModule(core.ethernet);
			
			IPv4Interface iface = new IPv4Interface("10.131.0.6", core.ethernet, ve0);
			core.ip.addInterface(iface);
			
			System.out.println("Test enviroment started");
			
			while(true) {}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}