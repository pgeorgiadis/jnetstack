package net.core.packet;


/**
 * PacketCriteria objects are used test packets that match some particular criteria.
 * For example all packets that has a certain byte set to a particular value.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	
 */
public interface PacketCriteria {
	/**
	 * Tests if the given packet matches this criteria
	 * @param	packet the packet to be tested
	 * @return	true if the given packet matches this criteria
	 */
	public boolean match(Packet packet);
}
