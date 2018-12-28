package net.protocol.ip.icmp;

import net.core.packet.container.PacketContainer;

/**
 * ICMPEchoRequest represents a ICMP echo request packet.
 * @author JPG
 */
public class ICMPEchoRequest extends ICMPPacket {
	/**
	 * Creates a new ICMPEchoRequest packet with a given packet container
	 * @param	container the packet container of this packet
	 */
	public ICMPEchoRequest(PacketContainer container) {
		super();
		if (container.getSize() < 8)
			this.container = PacketContainer.createContainer(8);
		PacketContainer.containerCopy(container, 0, this.container, 0, container.getSize());
	}
	
	/**
	 * Creates a new ICMPEchoRequest packet.
	 * @param	size the size of the packet to be created
	 */
	public ICMPEchoRequest(int size) {
		super();
		this.container = PacketContainer.createContainer(size+8);
	}
	
	/**
	 * Gets the value of the identifier field of this packet
	 * @return	the value of the identifier field of this packet
	 */
	public int getIdentifier() {
		return container.read16(4);
	}
	
	/**
	 * Sets the value of the identifier field of this packet
	 * @param	id the value of the identifier field of this packet
	 */
	public void setIdentifier(int id) {
		container.write16(4, id);
	}
	
	/**
	 * Gets the value of the sequence number field of this packet
	 * @return	the value of the identifier field of this packet
	 */
	public int getSequenceNumber() {
		return container.read16(6);
	}
	
	/**
	 * Sets the value of the sequence number  field of this packet
	 * @param	seq the value of the identifier field of this packet
	 */
	public void setSequenceNumber(int seq) {
		container.write16(6, seq);
	}
}
