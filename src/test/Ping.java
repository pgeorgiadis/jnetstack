package test;

import net.core.NetCore;
import net.protocol.ip.icmp.ICMPConstants;
import net.protocol.ip.icmp.ICMPEchoRequest;
import net.protocol.ip.icmp.ICMPListener;
import net.protocol.ip.icmp.ICMPPacket;
import net.protocol.ip.ipv4.IPv4Address;

public class Ping implements ICMPListener {
	NetCore core;
	public ICMPEchoRequest request;
	public IPv4Address src, dst;
	public long start, stop;
	public boolean done = false;
	private static int id = 0;
	private static int seq = 0;
	
	public Ping(IPv4Address src, IPv4Address dst) {
		this.src = src;
		this.dst = dst;
		
		request = new ICMPEchoRequest(0);
		request.setType(ICMPConstants.ECHO_REQUEST);
		request.setCode(0);
		request.setIdentifier(id++);
		request.setSequenceNumber(seq++);
	}
	
	public void send(NetCore core) {
		this.core = core;
		start = System.currentTimeMillis();
		core.icmp.injectICMP(request, src, dst);
	}
	
	public boolean match(IPv4Address src, int type) {
		return !done;
	}
	
	public void handle(ICMPPacket packet, IPv4Address src, IPv4Address dst) {
		stop = System.currentTimeMillis();
		long time = stop-start;
		if (time <= 1)
			System.out.println("Ping response from "+src+" time was: <1ms");
		else
			System.out.println("Ping response from "+src+" time was: "+(stop-start)+"ms");
		done = true;
		core.icmp.removeListener(this);
	}
}
