package test.speed;

import java.util.Random;

import net.core.NetCore;
import net.core.NetworkDevice;
import net.core.NetworkException;
import net.core.packet.Packet;
import net.core.packet.RawPacket;
import net.core.packet.container.PacketContainer;
import net.protocol.ethernet.EthernetDevice;
import net.protocol.ip.ipv4.IPv4Address;
import net.protocol.ip.ipv4.IPv4Constants;
import net.protocol.ip.ipv4.IPv4Interface;
import net.protocol.ip.ipv4.IPv4TransportModuleConnection;
import net.protocol.ip.tcp.TCPCriteria;
import net.protocol.ip.tcp.TCPListener;
import net.protocol.ip.tcp.TCPPacket;

public class StressTest {
	NetCore core = new NetCore();
	EthernetDevice dev1;
	EthernetDevice dev2;
	NetworkDevice dev3;
	IPv4Interface iface1;
	IPv4Interface iface2;
	IPv4Interface iface3;
	int repeats;
	
	public static void main(String[] args) {
		StressTest st = new StressTest();
		st.init();
		st.testTCPoIP();
	}
	
	public void init() {
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
		
		core.tcp.addListener(new TCPDataDump());
		
		repeats = 10;
	}
	
	public void testTCPoIP() {
		try {
			Random r = new Random();
			byte[] b = new byte[20];
			r.nextBytes(b);
			Packet expected = new RawPacket(PacketContainer.createContainer(b));
			
			IPv4TransportModuleConnection user = new IPv4TransportModuleConnection();
			user.setEndHost(iface2.getAddress());
			user.setLocalAddress(iface1.getAddress());
			user.setProtocol(IPv4Constants.TCP);
			user.setCriteria(new TCPCriteria(1, -1));
			core.ip.addTransportConnection(user);
			
			long start, stop;
			start = System.currentTimeMillis();
			for (int i=0; i<repeats; i++) {
				TCPPacket p = new TCPPacket(expected.getSize());
				p.setSrcPort(1);
				p.setDstPort(1);
				p.setPayload(expected);
				core.tcp.sendTCP(p, iface1.getAddress(), iface2.getAddress());
			}
			stop = System.currentTimeMillis();
			
			System.out.println((stop-start));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}

class TCPDataDump implements TCPListener {
	public boolean match(IPv4Address src, IPv4Address dst, int src_port, int dst_port) {
		return true;
	}
	
	public void handle(TCPPacket packet, IPv4Address src, IPv4Address dst) {
		RawPacket r = new RawPacket(packet.getContainer());
		System.out.println(new String(r.getContainer().toArray()));
	}
}