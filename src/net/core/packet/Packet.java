package net.core.packet;

import net.core.packet.container.PacketContainer;

/**
 * The class Packet is the Superclass of all Packet types. For example IPv4PAcket, EthernetPacket etc.
 * The Packet types are just a collection of methods that allow the programmer to read and
 * write from and to the specific fileds of a network packet. All this methods use the read and write
 * methods of the PacketContainer class to store this fields to the PacketContaienr of this Packet.
 * <br>
 * Packet also provides some common methods, like checksums etc.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	6.4.2005
 */
public abstract class Packet {
	/** The PacketContainer that currently holds this packet data. */
	protected PacketContainer container;
	
	/**
	 * Gets the PacketContainer that currently holds this packet.
	 * @return the container of this packet.
	 */
	public PacketContainer getContainer() {
		return this.container;
	}
	
	/**
	 * Sets the PacketContainer of this packet.
	 * @param container the new container that will this packet use.
	 */
	public void setContainer(PacketContainer container) {
		this.container = container;
	}
	
	/**
	 * Returns the payload of this Packet as another packet object.
	 * @return	the payload of this Packet.
	 */
	public abstract Packet getPayload();
	
	/**
	 * Sets the payload of this Packet.
	 * @param	packet the payload to be set.
	 */
	public abstract void setPayload(Packet packet);
	
	/**
	 * Returns the size of this packet. In most cases the size of the packet is the size of it's
	 * container.
	 * @return	the size of this packet.
	 */
	public int getSize() {
		return container.getSize();
	}
	
	/**
	 * Creates a copy of this packet.
	 * @return	a copy of this packet.
	 */
	public abstract Packet getCopy();
	
	public boolean equals(Object o) {
		try {
			Packet p = (Packet)o;
			return this.getContainer().equals(p.getContainer());
		} catch (RuntimeException e) {
			return false;
		}
	}
	
	/**
	 * Calculates the checksum value for a given byte[].
	 * @param	b the byte[] that will be used to calculate the checsum.
	 * @return	the checksum value of this bytes.
	 */
	public static int checksum(byte[] b) {
		int sum = 0;
		int tmp1, tmp2;
		int mask = ~65536;
		for (int i = 0; i<b.length; i += 2) {
			sum += (((b[i] & 0xFF) << 8) ^ (b[i + 1] & 0xFF));
			tmp1 = sum >> 16;
			tmp2 = sum & mask;
			sum = tmp1 + tmp2;
		}
		return ~sum;
	}
	
	public static int checksum1(byte[] b) {
		int sum = 0;
		int tmp1, tmp2;
		int mask = ~65536;
		for (int i = 0; i<b.length; i += 2) {
			sum += (((b[i] & 0xFF) << 8) ^ (b[i + 1] & 0xFF));
			if (sum > 65535) {
				tmp1 = sum >> 16;
				tmp2 = sum & mask;
				sum = tmp1 + tmp2;
			}
		}
		return ~sum;
	}
	
	public static int checksum2(byte[] b) {
		int sum = 0;
		int mask = ~65536;
		for (int i = 0; i<b.length; i += 2) {
			sum += (((b[i] & 0xFF) << 8) ^ (b[i + 1] & 0xFF));
			sum += sum & mask;
		}
		return ~sum;
	}
	
	public static int checksum3(byte[] b) {
		int sum = 0;
		int mask = ~65536;
		for (int i = 0; i<b.length; i += 2) {
			sum += (((b[i] & 0xFF) << 8) | (b[i + 1] & 0xFF));
			sum += sum & mask;
		}
		return ~sum;
	}
	
	public static int checksum4(byte[] b) {
		int sum = 0;
		int mask = ~65536;
		for (int i = 0; i<b.length; i += 2) {
			sum += (((b[i] & 0xFF) << 8) | (b[i + 1] & 0xFF));
		}
		while(sum > 65535) sum += sum & mask;
		return ~sum;
	}
	
	/**
	 * Calculates the checksum value for a range of a given PacketContainer.
	 * @param	c the PacketContainer that will be used to calculate the checsum.
	 * @param	offset the starting offset of the range that will this method sum.
	 * @param	length the length of the range that will this method sum.
	 * @return	the checksum value of this bytes.
	 */
	public static int checksum(PacketContainer c, int offset, int length) {
		return checksum(c,offset,length,0);
	}
	
	public static int checksum(PacketContainer c, int offset, int length, int initial_sum) {
		int sum = initial_sum;
		int tmp1, tmp2;
		int mask = ~65536;
		for (int i = 0; i<length; i += 2) {
			try {
				sum += (((c.u_read(i) & 0xFF) << 8) ^ (c.u_read(i+1) & 0xFF));
			} catch (ArrayIndexOutOfBoundsException e) {
				//A tricky way to use 1 byte padding if the container is not multiple of 2 bytes long
				sum += (((c.u_read(i) & 0xFF) << 8) ^ (0 & 0xFF));
			}
			if (sum > 65535) {
				tmp1 = sum >> 16;
				tmp2 = sum & mask;
				sum = tmp1 + tmp2;
			}
		}
		return ~sum;
	}
}
