package test;

import net.core.NetCore;
import net.core.NetModule;
import net.core.packet.Packet;
import net.core.packet.PacketCapsule;

public class TestModule implements NetModule {
	Packet packet;
	
	public NetModule handle(PacketCapsule capsule) {
		this.packet = capsule.getPacket();
		return null;
	}
	
	public Packet getReceived() {
		return this.packet;
	}
	
	public NetModule inject(PacketCapsule capsule) {return null;}
	public NetCore core() {return null;}
	public void log(int level, String message) {}
	public void log(int level, Throwable t) {}
	public void initialize(Object config) {}
	public void shutdown() {}
}
