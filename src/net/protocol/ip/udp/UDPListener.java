package net.protocol.ip.udp;

import net.protocol.ip.ipv4.IPv4Address;

/**
 * UDPListener are listeners for udp type packets. They have a match method, which is
 * called from the UDPProtocol module when a udp packet is received. If the match returns 
 * true, then the packet is delivered to this listener via its handle method.
 * @author	JPG
 * @since	
 */
public interface UDPListener {
	/**
	 * Tests if a certain packet is intented to be delivered to this UDPListener
	 * @param	dst the destination IP of this udp packet
	 * @param	dst_port the destination udp port
	 * @return	true if this udp packet is intented to be delivered to this listener or else false
	 */
	public boolean match(IPv4Address dst, int dst_port);
	
	/**
	 * Delivers a udp packet to this listener
	 * @param	packet the udp packet
	 * @param	src the source IP address of this packet
	 * @param	dst the destination IP address of this packet
	 */
	public abstract void handle(UDPPacket packet, IPv4Address src, IPv4Address dst);
}
