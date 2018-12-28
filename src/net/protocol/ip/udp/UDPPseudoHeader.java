package net.protocol.ip.udp;

import net.core.packet.Packet;
import net.core.packet.container.PacketContainer;
import net.protocol.ip.ipv4.IPv4Address;
import net.protocol.ip.ipv4.IPv4Constants;

/**
 * UDPPseudoHeader is a utility class which is used to calculate the checksum of the 
 * UDP packets
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 */
public class UDPPseudoHeader {
	private UDPPacket packet;
	private PacketContainer c;
	
	/**
	 * Creates a new UDPPseudoHeader
	 * @param	src the source IP address to write in this pseudoheader
	 * @param	dst the destination IP address to write in this pseudoheader
	 * @param	packet the packet
	 */
	public UDPPseudoHeader(IPv4Address src, IPv4Address dst, UDPPacket packet) {
		c = PacketContainer.createContainer(12);
		c.write(0, src.getAddress(), 0, 4);
		c.write(4, dst.getAddress(), 0, 4);
		c.write(8, 0);
		c.write(9, IPv4Constants.UDP);
		c.write16(10, packet.getLength());
		
		this.packet = packet;
	}
	
	/**
	 * Calculate the checksum of this pseudoheader
	 * @return the checksum of this pseudoheader
	 */
	public int getChecksum() {
		int pseudoheader_sum = ~(Packet.checksum(c, 0, 12));
		
		return Packet.checksum(packet.getContainer(), 0, packet.getLength(), pseudoheader_sum);
	}
}
