package net.protocol.ip.tcp;

import net.protocol.ip.ipv4.IPv4Address;

/**
 * TCPListener are listeners for tcp type packets. They have a match method, which is
 * called from the TCPProtocol module when a tcp packet is received. If the match 
 * returns true, then the packet is delivered to this listener via its handle method.
 * @author	JPG
 * @since	
 */
public interface TCPListener {
	/**
	 * Tests if a certain packet is intented to be delivered to this TCPListener
	 * @param	src the source IP of this tcp packet
	 * @param	dst the destination IP of this tcp packet
	 * @param	src_port the source tcp port
	 * @param	dst_port the destination tcp port
	 * @return	true if this tcp packet is intented to be delivered to this listener or else false
	 */
	public boolean match(IPv4Address src, IPv4Address dst, int src_port, int dst_port);
	
	/**
	 * Delivers a tcp packet to this listener
	 * @param	packet the tcp packet
	 * @param	src the source IP address of this packet
	 * @param	dst the destination IP address of this packet
	 */
	public abstract void handle(TCPPacket packet, IPv4Address src, IPv4Address dst);
}
