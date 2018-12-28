package net.protocol.ethernet;

import net.core.packet.Packet;
import net.core.packet.RawPacket;
import net.core.packet.container.PacketContainer;

/**
 * The EthernetPacket represents the ethernet frames. It is named EthernetPacket and not EthernetFrame
 * to stay close with the names of all other Packet Subclasses.
 * <br>
 * EthernetPacket provides accessors to all ethernet packet fields.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	14.8.2005
 */
public class EthernetPacket extends Packet {
	/**
	 * Creates a new EthernetPacket contained in the specified PacketContainer.
	 * @param	container the PacketContainer that will contain or already contains the frame.
	 */
	public EthernetPacket(PacketContainer container) {
		this.setContainer(container);
	}
	
	/**
	 * Creates a new EthernetPacket with a specified payload length.
	 * @param	payload_len the payload length of the frame.
	 */
	public EthernetPacket(int payload_len) {
		int size = 14+payload_len;
		this.container = PacketContainer.createContainer(size<60?60:size);
	}
	
	/**
	 * Returns the destination MAC address of this frame.
	 * @return	the destination MAC address of this frame.
	 */
	public EthernetAddress getDstAddress() {
		return new EthernetAddress(container.read(0, 6));
	}
	
	/**
	 * Changes the destination MAC address of this frame.
	 * @param	mac the destination MAC address.
	 */
	public void setDstAddress(EthernetAddress mac) {
		container.write(0, mac.getAddress(), 0, 6);
	}
	
	/**
	 * Changes the destination MAC address of this frame.
	 * @param	b the destination MAC address as a byte[].
	 * @throws	IllegalArgumentException when the b array length is not 6 bytes.
	 */
	public void setDstAddress(byte[] b) {
		if (b.length != 6)
			throw new IllegalArgumentException("MAC Addresses must have length 6 byte");
		container.write(0, b, 0, 6);
	}
	
	/**
	 * Returns the source MAC address of this frame.
	 * @return	the source MAC address of this frame.
	 */
	public EthernetAddress getSrcAddress() {
		return new EthernetAddress(container.read(6, 6));
	}
	
	/**
	 * Changes the source MAC address of this frame.
	 * @param	mac the source MAC address.
	 */
	public void setSrcAddress(EthernetAddress mac) {
		container.write(6, mac.getAddress(), 0, 6);
	}
	
	/**
	 * Changes the source MAC address of this frame.
	 * @param	b the source MAC address as a byte[].
	 * @throws	IllegalArgumentException when the b array length is not 6 bytes.
	 */
	public void setSrcAddress(byte[] b) {
		if (b.length != 6)
			throw new IllegalArgumentException("MAC Addresses must have length 6 byte");
		container.write(6, b, 0, 6);
	}
	
	/**
	 * Returns the ethertype field value of this frame.
	 * @return	the ethertype field value of this frame.
	 * @see		EthernetConstants
	 */
	public int getEthertype() {
		return container.read16(12);
	}
	
	/**
	 * Changes the ethertype field value of this frame.
	 * @param	i the new value of the ethertype frame.
	 * @see		EthernetConstants
	 */
	public void setEthertype(int i) {
		container.write16(12, i);
	}
	
	public void setContainer(PacketContainer container) {
		if (container.getSize() < 60) {
			PacketContainer new_container = PacketContainer.createContainer(60);
			PacketContainer.containerCopy(container, 0, new_container, 0, container.getSize());
			this.container = new_container;
		} else {
			this.container = container;
		}
	}
	
	public Packet getPayload() {
		int length = this.container.getSize() - 14;
		return new RawPacket(container.getSubContainer(14, length));
	}
	
	public void setPayload(Packet payload) {
		PacketContainer c = payload.getContainer();
		PacketContainer.containerCopy(c, 0, container, 14, c.getSize());
	}
	
	public Packet getCopy() {
		return new EthernetPacket(container.getCopy());
	}
}
