package net.protocol.ip.ipv4;

import java.util.Hashtable;

import net.core.NetworkException;
import net.core.packet.Packet;
import net.core.packet.RawPacket;
import net.core.packet.container.PacketContainer;

/**
 * The IPv4FragmentSet is a set of IP version 4 fragments.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	
 */
public class IPv4FragmentSet {
	/** The Hashtable that holds the fragments. */
	private Hashtable fragments;
	/** The number of fragments that have not arrive yet. */
	private int missing_fragments;
	/** The total length of all the fragments. */
	private int total_len;
	
	/**
	 * Creates a new IPv4FragmentSet.
	 */
	public IPv4FragmentSet() {
		this.fragments = new Hashtable();
	}
	
	/**
	 * Returns the total length of all the fragments that have arrived until now.
	 * @return	the total length of all the fragments that have arrived until now.
	 */
	public int getTotal_len() {
		return total_len;
	}
	
	/**
	 * Sets the total length of all the fragments that have arrived until now.
	 * @param	total_len the total length of all the fragments that have arrived until now.
	 */
	public void setTotal_len(int total_len) {
		this.total_len = total_len;
	}
	
	/**
	 * Adds one fragment to the fragment set.
	 * @param	offset the fragment offset of this fragment.
	 * @param	c the PacketContainer that contains this fragment.
	 * @param	more true if there are more fragments in this set, else false.
	 * @return	true if all the fragments of this set have arrive.
	 */
	public boolean addFragment(int offset, PacketContainer c, boolean more) {
		fragments.put(new Integer(offset), c);
		
		missing_fragments--;
		if (!more) {
			missing_fragments = fragments.size()-offset-1;
		}
		if (missing_fragments == 0)
			return true;
		return false;
	}
	
	/**
	 * Concatenates all the fragments of this set with their apropriate order.
	 * @return	the result Packet of the concatenation.
	 * @throws	NetworkException if there are missing fragments.
	 */
	public Packet bind() throws NetworkException {
		if (missing_fragments != 0) throw new NetworkException("Missing fragments");
		
		PacketContainer c = PacketContainer.createContainer(total_len);
		int c_offset = 0;
		for (int i=0; i<fragments.size(); i++) {
			PacketContainer f = (PacketContainer)fragments.get(new Integer(i));
			PacketContainer.containerCopy(f, 0, c, c_offset, f.getSize());
			c_offset += f.getSize();
		}
		return new RawPacket(c);
	}
}
