package net.protocol.ip.tcp.socket;

import java.util.Timer;

import net.protocol.ip.ipv4.IPv4Address;
import net.protocol.ip.tcp.TCPProtocol;

public class TCPSocket implements TCPSocketConstants {
	//	os parameters parameters
	static int receive_buffer_size = 32768;
	static int send_buffer_size = 32768;
	
	TCPProtocol tcp;
	TCPSender sender;
	TCPReceiver receiver;
	Timer timer;
	
	//sockets unique id
	IPv4Address local;
	IPv4Address remote;
	int local_port;
	int remote_port;
	
	//socket parameters
	int status;
	boolean so_linger;
	boolean no_delay;
	
	//counters
	long sent;
	long acknowledged;
	long last_received;
	long received;
	int remote_window;
	int urgent;
	
	public TCPSocket(TCPProtocol tcp, IPv4Address src, IPv4Address dst, int src_port, int dst_port) {
		this.tcp = tcp;
		this.local = src;
		this.remote = dst;
		this.timer = new Timer();
		
		this.local_port = src_port;
		this.remote_port = dst_port;
		
		this.sender = new TCPSender(this);
		this.receiver = new TCPReceiver(this);
		
		tcp.addListener(receiver);
		//Random r = new Random();
		this.sent = 12; //r.nextInt();
		
		this.status = LISTEN;
	}
	
	public IPv4Address getLocal() {
		return local;
	}

	public void setLocal(IPv4Address local) {
		this.local = local;
	}

	public int getLocal_port() {
		return local_port;
	}

	public void setLocal_port(int local_port) {
		this.local_port = local_port;
	}

	public IPv4Address getRemote() {
		return remote;
	}

	public void setRemote(IPv4Address remote) {
		this.remote = remote;
	}

	public int getRemote_port() {
		return remote_port;
	}

	public void setRemote_port(int remote_port) {
		this.remote_port = remote_port;
	}

	public void connect() {
		sender.sendSyn();
		status = SYN_SENT;
		TimetoutDuringItitialization t = new TimetoutDuringItitialization(this);
		this.timer.schedule(t, SYN_RCVD_TIMEOUT);
	}
	
	public void disconnect() {
		sender.sendFin();
		if (status == SYN_SENT)
			status = CLOSED;
		else if (status == SYN_RCVD || status == ESTABLISHED)
			status = FIN_WAIT_1;
		else if (status == CLOSE_WAIT)
			status = LAST_ACK;
	}
	
	public void close() {
		if (status != CLOSED)
			this.reset();
		sender.close();
		tcp.removeListener(receiver);
	}
	
	public void reset() {
		sender.sendRst();
		status = LISTEN;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public boolean isSoLinger() {
		return so_linger;
	}
	
	public void setSoLinger(boolean b) {
		this.so_linger = b;
	}
	
	public boolean isNoDelay() {
		return no_delay;
	}
	
	public void setNoDelay(boolean b) {
		this.no_delay = b;
	}
	
	public TCPSender getSender() {
		return sender;
	}
	
	public TCPReceiver getReceiver() {
		return receiver;
	}
}