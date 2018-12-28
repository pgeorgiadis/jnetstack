package net.protocol.ip.tcp.socket;

import net.protocol.ip.ipv4.IPv4Address;
import net.protocol.ip.tcp.TCPListener;
import net.protocol.ip.tcp.TCPPacket;

public class TCPReceiver implements TCPSocketConstants, TCPListener {
	private TCPSocket socket;
	private TCPReceiveBuffer buffer;
	
	protected TCPReceiver(TCPSocket socket) {
		this.socket = socket;
		this.buffer = new TCPReceiveBuffer(0, TCPSocket.receive_buffer_size);
	}
	
	protected int getLocalMaxWindow() {
		return buffer.availableSpace();
	}
	
	public TCPReceiveBuffer getBuffer() {
		return this.buffer;
	}
	
	protected void finReceived(TCPPacket fin, IPv4Address src, IPv4Address dst) {
		TCPPacket p = new TCPPacket(0);
		p.setSrcPort(fin.getDstPort());
		p.setDstPort(fin.getSrcPort());
		p.setControlBits(ACK, true);
		p.setSeqNumber(socket.sent);
		p.setAckNumber(fin.getSeqNumber());
		
		socket.setStatus(CLOSE_WAIT);
		socket.tcp.sendTCP(p, socket.local, socket.remote);
	}
	
	public void handle(TCPPacket packet, IPv4Address src, IPv4Address dst) {
		int status = socket.getStatus();
		socket.remote_window = packet.getWindow();
		
		//TODO handle PSH and URG flags
		
//		System.out.println(src+"->"+dst+" "+packet);
		
		if (packet.getControlBits(RST)) {
			socket.setStatus(LISTEN);
		} else if (socket.getStatus() == ESTABLISHED) {
			if (packet.getControlBits(FIN)) {
				socket.sender.sendAck(packet.getSeqNumber());
				socket.setStatus(CLOSE_WAIT);
			} else if (packet.getPayload().getSize() != 0) {
				//update acknowledged pointer
				socket.acknowledged = packet.getAckNumber();
				
				//handle data
				byte[] data = packet.getPayload().getContainer().toArray();
				long tmp = buffer.put(data, packet.getSeqNumber());
				if (tmp == TCPReceiveBuffer.DISCARD) {
					//TODO response for this discarded packet (Fast retransmit)
					System.out.println("TCPReceiverBuffer, my buffer responded DISCARD");
					System.out.println("Fast retransmit? I don't know how to use this? :p");
				} else {
					socket.received = tmp;
				}
			}
		} else if (packet.getControlBits(SYN)) {
			if (status == SYN_SENT && packet.getControlBits(TCPSocketConstants.ACK)) {
				if (packet.getAckNumber() == socket.sent+1) {
					socket.received = packet.getSeqNumber()+1;
					socket.acknowledged = packet.getAckNumber();
					socket.sent = packet.getAckNumber();
					socket.setStatus(ESTABLISHED);
					this.buffer.initAcked(socket.received);
					socket.sender.getBuffer().initOffset(socket.sent);
					socket.getSender().start();
				}
				else {
					socket.sender.sendRst();
				}
			} else if (status == LISTEN) {
				socket.received = packet.getSeqNumber()+1;
				socket.acknowledged = packet.getAckNumber();
				socket.sent = packet.getAckNumber();
				socket.sender.sendSynAck();
				socket.setStatus(SYN_RCVD);
			}
		} else if (packet.getControlBits(FIN)) {
			if (packet.getControlBits(ACK)) {
				if (status == FIN_WAIT_1) {
					socket.sender.sendAck(packet.getSeqNumber());
					socket.setStatus(TIME_WAIT);
					TCPTimer2MSL t = new TCPTimer2MSL(socket);
					socket.timer.schedule(t, 2 * MSL);
				}
			} else {
				socket.sender.sendAck(packet.getSeqNumber());
				if (status == ESTABLISHED)
					socket.setStatus(CLOSE_WAIT);
				else if (status == FIN_WAIT_1)
					socket.setStatus(CLOSING);
				else if (status == FIN_WAIT_2) {
					socket.setStatus(TIME_WAIT);
					TCPTimer2MSL t = new TCPTimer2MSL(socket);
					socket.timer.schedule(t, 2 * MSL);
				}
			}
		} else if (packet.getControlBits(ACK)) {
			if (status == SYN_RCVD) {
				socket.setStatus(ESTABLISHED);
				this.buffer.initAcked(socket.received);
				socket.sender.getBuffer().initOffset(socket.sent);
				socket.getSender().start();
			}
			else if (status == LAST_ACK)
				socket.setStatus(CLOSED);
			else if (status == FIN_WAIT_1)
				socket.setStatus(FIN_WAIT_2);
			else if (status == CLOSING) {
				socket.setStatus(TIME_WAIT);
				TCPTimer2MSL t = new TCPTimer2MSL(socket);
				socket.timer.schedule(t, 2 * MSL);
			}
		}
	}
	
	public boolean match(IPv4Address src, IPv4Address dst, int src_port, int dst_port) {
		if (
				(dst.equals(socket.local)) &&
				(dst_port == socket.local_port) &&
				(src.equals(socket.remote)) &&
				(src_port == socket.remote_port)
				)
			return true;
		return false;
	}
}