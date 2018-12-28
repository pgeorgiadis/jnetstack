package net.protocol.ip.tcp.socket;

import java.util.TimerTask;

public class TimetoutDuringItitialization extends TimerTask {
	private TCPSocket socket;
	
	public TimetoutDuringItitialization(TCPSocket socket) {
		this.socket = socket;
	}
	
	public void run() {
		if (socket.getStatus() == TCPSocketConstants.SYN_SENT)
			socket.setStatus(TCPSocketConstants.LISTEN);
	}
}
