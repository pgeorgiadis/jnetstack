package net.protocol.ip.tcp.socket;

import java.io.IOException;

import net.core.packet.RawPacket;
import net.core.packet.container.PacketContainer;
import net.protocol.ip.tcp.TCPPacket;

public class TCPSender implements TCPSocketConstants, Runnable {
	private TCPSocket socket;
	private TCPSendBuffer buffer;
	private PendingPacketsRegistry pending;
	
	protected TCPSender(TCPSocket socket) {
		this.socket = socket;
		buffer = new TCPSendBuffer(TCPSocket.send_buffer_size);
		pending = new PendingPacketsRegistry();
	}
	
	public TCPSendBuffer getBuffer() {
		return this.buffer;
	}
	
	protected void start() {
		Thread t = new Thread(this, "socket thread");
		t.start();
	}
	
	private void send(TCPPacket packet) {
		packet.setSrcPort(socket.local_port);
		packet.setDstPort(socket.remote_port);
		packet.setWindow(socket.receiver.getBuffer().availableSpace());
		
//		System.out.println(socket.local+"->"+socket.remote+" "+packet);
		
		socket.tcp.sendTCP(packet, socket.local, socket.remote);
	}
	
	public void sendSyn() {
		TCPPacket p = new TCPPacket(0);
		p.setControlBits(TCPSocketConstants.SYN, true);
		p.setSeqNumber(socket.sent);
		p.setAckNumber(0);
		
		this.send(p);
	}
	
	protected void sendSynAck() {
		TCPPacket p = new TCPPacket(0);
		p.setControlBits(TCPSocketConstants.SYN, true);
		p.setControlBits(TCPSocketConstants.ACK, true);
		p.setSeqNumber(socket.sent);
		p.setAckNumber(socket.received);
		
		this.send(p);
	}
	
	public void sendAck(long ack) {
		TCPPacket p = new TCPPacket(0);
		p.setControlBits(TCPSocketConstants.ACK, true);
		p.setSeqNumber(socket.sent);
		p.setAckNumber(ack);
		
		this.send(p);
	}
	
	public void sendFin() {
		TCPPacket p = new TCPPacket(0);
		p.setControlBits(TCPSocketConstants.FIN, true);
		p.setSeqNumber(socket.sent);
		p.setAckNumber(socket.received);
		
		this.send(p);
	}
	
	public void sendRst() {
		TCPPacket p = new TCPPacket(0);
		p.setControlBits(TCPSocketConstants.RST, true);
		p.setSeqNumber(socket.sent);
		p.setAckNumber(socket.received);
		
		this.send(p);
	}
	
	public void send() {
		long start_from;
		
		if (pending.lastSent() != -1) {
			start_from = pending.lastSent();
		}
		else {
			start_from = socket.sent;
		}
		
		pending.clear(socket.acknowledged);
		TCPPacket timedout = pending.getTimedout();
		if (timedout != null) {
			System.out.println("Sending again timedout packet "+timedout);
			timedout.setAckNumber(socket.received);
			this.send(timedout);
			return;
		}
		
		int length = this.decideDataLength(start_from);
		
		if (length < 0) {
			socket.tcp.log("TCPSender.send(): Warning! negative number where non-negative expected");
			return;
		}
		
		if (length == 0 && socket.last_received == socket.received) {
			try {
				//TODO try to improve this using synchronization.
				Thread.sleep(5);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return;
		}
		
		try {
			TCPPacket packet = new TCPPacket(length);
			packet.setSeqNumber(start_from);
			packet.setAckNumber(socket.received);
			
			if (socket.last_received != socket.received) {
				packet.setControlBits(TCPSocketConstants.ACK, true);
				socket.last_received = packet.getAckNumber();
			}
			
			if (length > 0) {
				byte[] b = buffer.get(start_from + 1, length);
				packet.setPayload(new RawPacket(PacketContainer.createContainer(b)));
			}
			
			this.send(packet);
			socket.sent+=length;
			
			if (length > 0)
				this.pending.appendPacket(packet, length);
		} catch (IOException e) {
			socket.tcp.log(e.getMessage());
		}
	}
	
	private int decideDataLength(long start_from_seq) {
		int available = buffer.availableData(start_from_seq + 1);
		
		//TODO thread control logic + TCP send desicion (Slow start, Fast retransmit)
		
		int length = 0;
		
		if (available > 0) {
			//Nagle Algorithm
			if (socket.acknowledged == socket.sent)
				length = Math.min(available, socket.remote_window);
			else if (socket.isNoDelay())
				length = Math.min(available, socket.remote_window+(int)((socket.acknowledged-socket.sent) & 0xff));
		}
		
		//TODO calculate and use MSS
		if (length > 1024)
			length = 1024;
		
		return length;
	}
	
	public void run() {
		// sleep for a couple of ms to avoid sendig data before the 3-way handshake's last ack arives to the sender
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while(buffer != null) {
			this.send();
		}
	}
	
	public void close() {
		this.buffer = null;
	}
}