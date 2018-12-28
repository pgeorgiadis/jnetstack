package net.protocol.ip.udp;

import net.core.packet.Packet;
import net.core.packet.RawPacket;
import net.core.packet.container.PacketContainer;
import net.protocol.ip.tcp.TCPPacket;

/**
 *  The UDPPacket represent the udp packets.
 * <br>
 * UDPPacket provides accessors to all udp packet fields.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	21.9.2005
 */
public class UDPPacket extends Packet {
	/**
	 * Creates a new UDPPacket contained in the specified PacketContainer.
	 * @param	container the PacketContainer that will contain or already contains the packet.
	 */
	public UDPPacket(PacketContainer container) {
		this.container = container;
	}
	
	/**
	 * Creates a new TCPPacket with the specified payload length.
	 * @param	payload_len the payload length.
	 */
	public UDPPacket(int payload_len) {
		this.container = PacketContainer.createContainer(payload_len+8);
		this.setLength(payload_len+8);
	}
	
	/**
	 * Returns the source UDP port of the packet.
	 * @return	the source UDP port of the packet.
	 */
	public int getSrcPort() {
		return container.read16(0);
	}
	
	/**
	 * Changes the source port of the packet.
	 * @param	port the new source port of the packet.
	 */
	public void setSrcPort(int port) {
		container.write16(0, port);
	}
	
	/**
	 * Returns the destination UDP port of the packet.
	 * @return	the destination UDP port of the packet.
	 */
	public int getDstPort() {
		return container.read16(2);
	}
	
	/**
	 * Changes the destination port of the packet.
	 * @param	port the new destination port of the packet.
	 */
	public void setDstPort(int port) {
		container.write16(2, port);
	}
	
	/**
	 * Returns the value of the length field of the packet.
	 * @return	the value of the length field of the packet.
	 */
	public int getLength() {
		return container.read16(4);
	}
	
	/**
	 * Changes the value of the length field of the packet.
	 * @param	length the new value.
	 */
	public void setLength(int length) {
		container.write16(4, length);
	}
	
	/**
	 * Returns the value of checksum field of the packet.
	 * @return	the value of checksum field of the packet.
	 */
	public int getChecksum() {
		return container.read16(6);
	}
	
	/**
	 * Changes the value of the checksum field of the packet.
	 * @param	c the new value.
	 */
	public void setChecksum(int c) {
		container.write16(6, c);
	}
	
	public Packet getPayload() {
		PacketContainer c = PacketContainer.createContainer(container.getSize() - 8);
		PacketContainer.containerCopy(container, 8, c, 0, c.getSize());
		return new RawPacket(c);
	}
	
	public void setPayload(Packet payload) {
		PacketContainer c = payload.getContainer();
		PacketContainer.containerCopy(c, 0, container, 8, c.getSize());
	}
	
	public Packet getCopy() {
		return new TCPPacket(container.getCopy());
	}
}
