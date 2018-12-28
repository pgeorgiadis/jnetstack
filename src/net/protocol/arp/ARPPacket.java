package net.protocol.arp;

import net.core.packet.NetworkPacketException;
import net.core.packet.Packet;
import net.core.packet.container.PacketContainer;

/**
 * The ARPPacket represents the arp packets.
 * <br>
 * ARPPacket provides accessors to all arp packet fields.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	14.8.2005
 */
public class ARPPacket extends Packet {
	/**
	 * Creates a new ARPPacket contained in the specified PacketContainer.
	 * @param	container the PacketContainer that will contain or already contains the packet.
	 */
	public ARPPacket(PacketContainer container) {
		this.container = container;
	}
	
	/**
	 * Creates a new ARPPacket for hardware address length h_addr_len and protocol address length
	 * p_addr_len.
	 * @param h_addr_len the specified hardware addresslength.
	 * @param p_addr_len the specified protocol address length.
	 */
	public ARPPacket(int h_addr_len, int p_addr_len) {
		int packet_size = 8+2*h_addr_len+2*p_addr_len;
		container = PacketContainer.createContainer(packet_size);
		this.setHardwareAddressLength(h_addr_len);
		this.setProtocolAddressLength(p_addr_len);
	}
	
	/**
	 * Returns the value of the hardware type field.
	 * @return	the value of the hardware type field.
	 * @see		ARPConstants
	 */
	public int getHardwareType() {
		return container.read16(0);
	}
	
	/**
	 * Changes the value of the hardware type field.
	 * @param	type the new value.
	 * @see		ARPConstants
	 */
	public void setHardwareType(int type) {
		container.write16(0, type);
	}
	
	/**
	 * Returns the value of the protocol type field.
	 * @return	the value of the protocol type field.
	 * @see		ARPConstants
	 */
	public int getProtocolType() {
		return container.read16(2);
	}
	
	/**
	 * Changes the value of the protocol type field.
	 * @param	type the new value.
	 * @see		ARPConstants
	 */
	public void setProtocolType(int type) {
		container.write16(2, type);
	}
	
	/**
	 * Returns the value of the hardware address length field.
	 * @return	the value of the hardware address length field.
	 */
	public int getHardwareAddressLength() {
		return container.read(4);
	}
	
	/**
	 * Changes the value of the hardware address length field.
	 * @param	n the new value.
	 */
	public void setHardwareAddressLength(int n) {
		container.write(4, n);
	}
	
	/**
	 * Returns the value of the protocol address length field.
	 * @return	the value of the protocol address length field.
	 */
	public int getProtocolAddressLength() {
		return container.read(5);
	}
	
	/**
	 * Changes the value of the protocol address length field.
	 * @param	n the new value.
	 */
	public void setProtocolAddressLength(int n) {
		container.write(5, n);
	}
	
	/**
	 * Returns the value of the opcode field.
	 * @return	the value of the opcode field.
	 * @see		ARPConstants
	 */
	public int getOpcode() {
		return container.read16(6);
	}
	
	/**
	 * Changes the value of the opcode field.
	 * @param	opcode the new value.
	 * @see		ARPConstants
	 */
	public void setOpcode(int opcode) {
		container.write16(6, opcode);
	}
	
	/**
	 * Returns the source hardware address of this packet.
	 * @return	the source hardware address of this packet.
	 * @throws	NetworkPacketException if the hardware address length field has not been set.
	 */
	public byte[] getSrcHAddress() throws NetworkPacketException {
		int hl = this.getHardwareAddressLength();
		if (hl == 0)
			throw new NetworkPacketException("Hardware address length is not set");
		return container.read(8, hl);
	}
	
	/**
	 * Changes the source hardware address of this packet.
	 * @param	a the new address.
	 * @throws	NetworkPacketException if the hardware address length has not been set, or the values
	 * 			of the address length and the argument length does not match.
	 */
	public void setSrcHAddress(byte[] a) throws NetworkPacketException {
		int hl = this.getHardwareAddressLength();
		if (hl == 0)
			throw new NetworkPacketException("Hardware address length is not set");
		if (hl != a.length)
			throw new NetworkPacketException("Hardware address length and argument length does not match");
		container.write(8, a, 0, hl);
	}
	
	/**
	 * Returns the source protocol address of this packet.
	 * @return	the source protocol address of this packet.
	 * @throws	NetworkPacketException if the protocol address length field has not been set.
	 */
	public byte[] getSrcPAddress() throws NetworkPacketException {
		int hl = this.getHardwareAddressLength();
		int pl = this.getProtocolAddressLength();
		if (hl == 0)
			throw new NetworkPacketException("Hardware address length is not set");
		if (pl == 0)
			throw new NetworkPacketException("Protocol address length is not set");
		return container.read(8+hl, pl);
	}
	
	/**
	 * Changes the source protocol address of this packet.
	 * @param	a the new address.
	 * @throws	NetworkPacketException if the hardware or protocol address length has not been 
	 * 			set, or the values of the address length and the argument length does not match.
	 */
	public void setSrcPAddress(byte[] a) throws NetworkPacketException {
		int hl = this.getHardwareAddressLength();
		int pl = this.getProtocolAddressLength();
		if (hl == 0)
			throw new NetworkPacketException("Hardware address length is not set");
		if (pl == 0)
			throw new NetworkPacketException("Protocol address length is not set");
		if (pl != a.length)
			throw new NetworkPacketException("Protocol address length and argument length does not match");
		container.write(8+hl, a, 0, pl);
	}
	
	/**
	 * Returns the destination hardware address of this packet.
	 * @return	the destination hardware address of this packet.
	 * @throws	NetworkPacketException if the hardware or protocol address length field has not 
	 * 			been set.
	 */
	public byte[] getDstHAddress() throws NetworkPacketException {
		int hl = this.getHardwareAddressLength();
		int pl = this.getProtocolAddressLength();
		if (hl == 0)
			throw new NetworkPacketException("Hardware address length is not set");
		if (pl == 0)
			throw new NetworkPacketException("Protocol address length is not set");
		return container.read(8+hl+pl, hl);
	}
	
	/**
	 * Changes the destination hardware address of this packet.
	 * @param	a the new address.
	 * @throws	NetworkPacketException if the hardware address length has not been set, or the values
	 * 			of the address length and the argument length does not match.
	 */
	public void setDstHAddress(byte[] a) throws NetworkPacketException {
		int hl = this.getHardwareAddressLength();
		int pl = this.getProtocolAddressLength();
		if (hl == 0)
			throw new NetworkPacketException("Hardware address length is not set");
		if (pl == 0)
			throw new NetworkPacketException("Protocol address length is not set");
		if (hl != a.length)
			throw new NetworkPacketException("Hardware address length and argument length does not match");
		container.write(8+hl+pl, a, 0, hl);
	}
	
	/**
	 * Returns the destination protocol address of this packet.
	 * @return	the destination protocol address of this packet.
	 * @throws	NetworkPacketException if the hardware or protocol address length field has not been set.
	 */
	public byte[] getDstPAddress() throws NetworkPacketException {
		int hl = this.getHardwareAddressLength();
		int pl = this.getProtocolAddressLength();
		if (hl == 0)
			throw new NetworkPacketException("Hardware address length is not set");
		if (pl == 0)
			throw new NetworkPacketException("Protocol address length is not set");
		return container.read(8+2*hl+pl, pl);
	}
	
	/**
	 * Changes the destination protocol address of this packet.
	 * @param	a the new address.
	 * @throws	NetworkPacketException if the hardware address length has not been set, or the values
	 * 			of the address length and the argument length does not match.
	 */
	public void setDstPAddress(byte[] a) throws NetworkPacketException {
		int hl = this.getHardwareAddressLength();
		int pl = this.getProtocolAddressLength();
		if (hl == 0)
			throw new NetworkPacketException("Hardware address length is not set");
		if (pl == 0)
			throw new NetworkPacketException("Protocol address length is not set");
		if (pl != a.length)
			throw new NetworkPacketException("Protocol address length and argument length does not match");
		container.write(8+2*hl+pl, a, 0, pl);
	}
	
	public Packet getPayload() {
		return null;
	}
	
	public void setPayload(Packet packet) {
		return;
	}
	
	public Packet getCopy() {
		return new ARPPacket(container.getCopy());
	}
}
