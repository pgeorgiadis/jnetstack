package net.protocol.ip.tcp;

import net.core.packet.Packet;
import net.core.packet.PacketCriteria;

/**
 * TCPCriteria matches TCP packets.
 * @author JPG
 */
public class TCPCriteria implements PacketCriteria {
	/** The source port we want the TCP packet to match */
	int src_port;
	/** The destination port we want the TCP packet to match */
	int dst_port;
	
	/**
	 * Creates a new TCPCriteria object that will match all TCP packets witch also
	 * have source and destination ports as specified here. If the port number specified
	 * here is -1, it will match any port number.
	 * @param	src_port the source port to match
	 * @param	dst_port the destination port to match
	 */
	public TCPCriteria(int src_port, int dst_port) {
		this.src_port = src_port;
		this.dst_port = dst_port;
	}
	
	public boolean match(Packet packet) {
		try {
			TCPPacket p = (TCPPacket)packet;
			if ((p.getSrcPort() == src_port || src_port == -1) && (p.getDstPort() == dst_port || dst_port == -1))
				return true;
		} catch (RuntimeException e) {}
		return false;
	}
}
