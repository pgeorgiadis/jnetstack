package net.protocol.ip.tcp;

import net.core.packet.Packet;
import net.core.packet.container.PacketContainer;
import net.protocol.ip.ipv4.IPv4Address;
import net.protocol.ip.ipv4.IPv4Constants;

/**
 * TCPPseudoHeader is a utility class which is used to calculate the checksum of the 
 * TCP packets
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 */
public class TCPPseudoHeader {
	private TCPPacket packet;
	PacketContainer c;
	
	/**
	 * Creates a new TCPPseudoHeader
	 * @param	src the source IP address to write in this pseudoheader
	 * @param	dst the destination IP address to write in this pseudoheader
	 * @param	packet the packet
	 */
	public TCPPseudoHeader(IPv4Address src, IPv4Address dst, TCPPacket packet) {
		c = PacketContainer.createContainer(12);
		c.write(0, src.getAddress(), 0, 4);
		c.write(4, dst.getAddress(), 0, 4);
		c.write(8, 0);
		c.write(9, IPv4Constants.TCP);
		c.write16(10, packet.getSize());
		
		this.packet = packet;
	}
	
	/**
	 * Calculate the checksum of this pseudoheader
	 * @return the checksum of this pseudoheader
	 */
	public int getChecksum() {
		int pseudoheader_sum = ~(Packet.checksum(c, 0, 12));
		
		return Packet.checksum(packet.getContainer(), 0, packet.getSize(), pseudoheader_sum);
	}
}
