package net.protocol.ip.icmp;

import net.core.packet.Packet;
import net.core.packet.RawPacket;
import net.core.packet.container.PacketContainer;

/**
 *  The ICMPPacket represents the icmp packets.
 * <br>
 * ICMPPacket provides accessors to all icmp packet fields.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	21.8.2005
 */
public class ICMPPacket extends Packet {
	protected ICMPPacket() {}
	
	/**
	 * Creates a new ICMPPacket contained in the specified PacketContainer.
	 * @param	container the PacketContainer that will contain or already contains the packet.
	 */
	public ICMPPacket(PacketContainer container) {
		this.container = container;
	}
	
	/**
	 * Creates a new ICMPPacket with a specified data length.
	 * @param	size the data length.
	 */
	public ICMPPacket(int size) {
		this.container = PacketContainer.createContainer(size+4);
	}
	
	/**
	 * Returns the icmp type of this packet.
	 * @return	the icmp type of this packet.
	 * @see		ICMPConstants
	 */
	public int getType() {
		return container.read(0);
	}
	
	/**
	 * Changes the icmp type of this packet.
	 * @param	type the new icmp type value.
	 * @see		ICMPConstants
	 */
	public void setType(int type) {
		container.write(0, type);
	}
	
	/**
	 * Returns the icmp code of this packet.
	 * @return	the ICMP code of this packet.
	 * @see		ICMPConstants
	 */
	public int getCode() {
		return container.read(1);
	}
	
	/**
	 * Changes the icmp code of this packet.
	 * @param	code the new icmp code.
	 */
	public void setCode(int code) {
		container.write(1, code);
	}
	
	/**
	 * Returns the ICMP checksum value of this packet.
	 * @return	the ICMP checksum value of this packet.
	 */
	public int getICMPChecksum() {
		return container.read16(2);
	}
	
	/**
	 * Changes the ICMP checksum value of this packet.
	 * @param	c the new ICMP checksum value.
	 */
	public void setICMPChecksum(int c) {
		container.write16(2, c);
	}
	
	public Packet getPayload() {
		return new RawPacket(container.getSubContainer(4, container.getSize()-8));
	}
	
	public void setPayload(Packet payload) {
		PacketContainer c = payload.getContainer();
		PacketContainer.containerCopy(c, 0, container, 8, c.getSize());
	}
	
	public Packet getCopy() {
		return new ICMPPacket(container.getCopy());
	}
}
