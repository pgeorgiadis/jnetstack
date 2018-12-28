package net.protocol.ip.tcp.socket;

public interface TCPSocketConstants {
	/* Socket states */
	public static final int CLOSED = 0;
	public static final int LISTEN = 1;
	public static final int SYN_RCVD = 2;
	public static final int SYN_SENT = 3;
	public static final int ESTABLISHED = 4;
	public static final int TIME_WAIT = 5;
	public static final int FIN_WAIT_1 = 6;
	public static final int FIN_WAIT_2 = 7;
	public static final int CLOSING = 8;
	public static final int CLOSE_WAIT = 9;
	public static final int LAST_ACK = 10;
	
	/* Controlbits */
	public static final int URG = 10;
	public static final int ACK = 11;
	public static final int PSH = 12;
	public static final int RST = 13;
	public static final int SYN = 14;
	public static final int FIN = 15;
	
	/* Maximum Segment Life time */
	public static final int MSL = 30000;
	
	/* How many time to wait in SYN_RCVD state (before assuming that the client is down */
	public static final int SYN_RCVD_TIMEOUT = 60000;
	
	/* Timeout */
	public static final long PACKET_TIMEOUT = 3000;
}
