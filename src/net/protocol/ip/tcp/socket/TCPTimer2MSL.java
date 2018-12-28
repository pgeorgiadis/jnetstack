package net.protocol.ip.tcp.socket;

import java.util.TimerTask;

public class TCPTimer2MSL extends TimerTask {
	private TCPSocket socket;
	
	public TCPTimer2MSL(TCPSocket socket) {
		this.socket = socket;
	}
	
	public void run() {
		socket.setStatus(TCPSocketConstants.CLOSED);
	}
}