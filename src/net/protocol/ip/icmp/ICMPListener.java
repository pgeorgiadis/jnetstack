package net.protocol.ip.icmp;

import net.protocol.ip.ipv4.IPv4Address;

/**
 * ICMPListeners are listeners for icmp type packets. They have a match method, which is
 * called from the ICMPProtocol module when a icmp packet is received. If the match returns 
 * true, then the packet is delivered to this listener via the handle method.
 * @author JPG
 *
 */
public interface ICMPListener {
	/**
	 * Tests if a certain packet is intented to be delivered to this ICMPListener
	 * @param	src the source IP of this icmp packet
	 * @param	type the icmp type of this icmp packet
	 * @return	true if this icmp packet is intented to be delivered to this listener or else false
	 */
	public boolean match(IPv4Address src, int type);
	
	/**
	 * Delivers a icmp packet to this listener
	 * @param	packet the icmp packet
	 * @param	src the source IP address of this packet
	 * @param	dst the destination IP address of this packet
	 */
	public abstract void handle(ICMPPacket packet, IPv4Address src, IPv4Address dst);
}
