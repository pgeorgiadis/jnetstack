package net.core.packet;

import net.core.packet.container.PacketContainer;

/**
 * The RawPacket is the simplest Packet type. It's a raw data packet without any fields.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	7.4.2005
 */
public class RawPacket extends Packet {
	/**
	 * Creates a new RawPacket contained in the specified PacketContainer.
	 * @param	container the PacketContainer that will contain or already contains the packet.
	 */
	public RawPacket(PacketContainer container){
		this.container = container;
	}
	
	public Packet getPayload() {
		return this;
	}
	
	public void setPayload(Packet packet) {
		this.container = packet.container;
	}
	
	public Packet getCopy() {
		return new RawPacket(container.getCopy());
	}
}
