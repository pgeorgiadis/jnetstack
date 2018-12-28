package net.protocol.ip.tcp.socket;

import net.protocol.ip.tcp.TCPPacket;

public class PendingPacketsRegistry {
	private PendingPacket pending;
	
	public void clear(long acked) {
		while (pending != null) {
			if (pending.isPending(acked)) {
				return;
			}
			this.pending = pending.getNextPending();
		}
	}

	public TCPPacket getTimedout() {
		if (this.pending != null && this.pending.isTimedout()) {
			pending.touch();
			return this.pending.getPacket();
		}
		return null;
	}

	public void appendPacket(TCPPacket packet, int data_size) {
		PendingPacket tmp = pending;
		if (tmp != null) {
			while (tmp.getNextPending() != null) {
				tmp = tmp.getNextPending();
			}
			PendingPacket p = new PendingPacket(packet, data_size);
			tmp.setNextPending(p);
		}
		else {
			this.pending = new PendingPacket(packet, data_size);
		}
	}
	
	public long lastSent() {
		if (pending != null)
			return pending.getPacket().getSeqNumber()+pending.getDataSize();
		else
			return -1;
	}
}
