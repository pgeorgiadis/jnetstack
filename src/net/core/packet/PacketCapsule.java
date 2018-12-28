package net.core.packet;

import java.util.Hashtable;

/**
 * A packet capsule is used as a container, that holds a packet during processing from the system.
 * @author	Pavlos Georgiadis (aka JPG) jpg@freemail.gr
 * @since	1.5.2006
 */
public class PacketCapsule {
	Packet packet;
	Hashtable<String,Object> hints;
	
	/**
	 * Creates a new PacketCapsule, that contains the specified packet
	 * @param	packet the packet to be included in this packet capsule
	 */
	public PacketCapsule(Packet packet) {
		this.packet = packet;
	}
	
	/**
	 * Gets the packet that this capsule curently holds
	 * @return the packet that this capsule curently holds
	 */
	public Packet getPacket() {
		return packet;
	}
	
	/**
	 * Sets the packet that this capsule curently holds
	 * @param	packet the packet that this capsule curently holds
	 */
	public void setPacket(Packet packet) {
		this.packet = packet;
	}
	
	/**
	 * Gets one of the hints contained in this descriptor
	 * @param	hint_name the hint's name
	 * @return	The value of this hint
	 */
	public Object getHint(String hint_name) {
		if (hints != null)
			return hints.get(hint_name);
		return null;
	}
	
	/**
	 * Sets one of the hints contained in this descriptor
	 * @param	hint_name the hint's name
	 * @param	hint the value of the hint to be set
	 */
	public void setHint(String hint_name, Object hint) {
		if (hints == null)
			hints = new Hashtable<String, Object>();
		hints.put(hint_name, hint);
	}
}
