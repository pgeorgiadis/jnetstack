package net.protocol.ip.tcp.socket;

import net.protocol.ip.tcp.TCPPacket;

public class PendingPacket {
	private long age;
	private TCPPacket packet;
	private int data_size;
	private PendingPacket next_pending;
	
	public PendingPacket(TCPPacket packet, int data_size) {
		this.data_size = data_size;
		this.age = System.currentTimeMillis();
		this.packet = packet;
	}
	
	public boolean isPending(long acked) {
		if (acked < packet.getSeqNumber()+data_size) {
			return true;
		}
		return false;
	}
	
	public boolean isTimedout() {
		if ((System.currentTimeMillis() - this.age) > TCPSocketConstants.PACKET_TIMEOUT)
			return true;
		return false;
	}
	
	public PendingPacket getNextPending() {
		return this.next_pending;
	}
	
	public void setNextPending(PendingPacket p) {
		this.next_pending = p;
	}
	
	public TCPPacket getPacket() {
		return this.packet;
	}
	
	public int getDataSize() {
		return this.data_size;
	}
	
	public void touch() {
		this.age = System.currentTimeMillis(); 
	}
}
