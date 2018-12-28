package net.protocol.ip.ipv4;

import net.core.packet.Packet;
import net.core.packet.RawPacket;
import net.core.packet.container.BitMask;
import net.core.packet.container.PacketContainer;

/**
 * The IPv4Packet represents the ip version 4 packets.
 * <br>
 * IPv4Packet provides accessors to all ip version 4 packet fields.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	16.8.2005
 */
public class IPv4Packet extends Packet {
	/**
	 * Creates a new IPv4Packet contained in the specified PacketContainer. It also sets the IHL 
	 * field (Internet Header Length) to 5, which is the usual value for this field and the Total 
	 * length field to the length of the specified container.
	 * @param	container the PacketContainer that will contain or already contains the packet.
	 */
	public IPv4Packet(PacketContainer container){
		this.setContainer(container);
	}
	
	/**
	 * Creates a new IPv4Packet with a specified payload length. It also sets the IHL field 
	 * (Internet Header Length) to 5, which is the usual value for this field and the Total 
	 * length field to 5+payload_len.
	 * @param	payload_len the payload length of the packet.
	 */
	public IPv4Packet(int payload_len) {
		this.container = PacketContainer.createContainer(20+payload_len);
		
		BitMask m = new BitMask(container.read(0, 1));
		m.setBit(1, true);
		container.write(0, m.toByteArray(), 0, 1);
		
		this.setIHL(5);
		this.setTotalLength(payload_len+20);
	}
	
	/**
	 * Sets some default values to the fields of the packet
	 */
	public void setDefaults() {
		this.setTotalLength(this.getSize());
		this.setDontFragment(false);
		this.setMoreFragments(false);
		this.setTTL(255);
	}
	
	/**
	 * Returns the value of the IHL (Internet header length) field.
	 * @return	the value of the IHL (Internet header length) field.
	 */
	public int getIHL() {
		BitMask m = new BitMask(container.read(0, 1));
		return m.evaluate(4, 4);
	}
	
	/**
	 * Changes the value of the IHL (Internet header length) field.
	 * @param	ihl the new value.
	 */
	public void setIHL(int ihl) {
		BitMask m = new BitMask(container.read(0, 1));
		m.set(4, 4, ihl);
		container.write(0, m.toByteArray(), 0, 1);
	}
	
	/**
	 * Returns the value of the presedense field.
	 * @return	the value of the presedense field.
	 */
	public int getPresedence() {
		BitMask m = new BitMask(container.read(1, 1));
		return m.evaluate(0, 3);
	}
	
	/**
	 * Changes the value of the presedence field.
	 * @param	p the new value.
	 */
	public void setPresedence(int p) {
		BitMask m = new BitMask(container.read(1, 1));
		m.set(0, 3, p);
		container.write(1, m.toByteArray(), 0, 1);
	}
	
	/**
	 * Tests if the Minimize Delay flag is set.
	 * @return	True if the Minimize Delay flag is set or false if it is not.
	 */
	public boolean isMinimizeDelay() {
		BitMask m = new BitMask(container.read(1, 1));
		return m.getBit(3);
	}
	
	/**
	 * Sets the Minimize Delay flag.
	 * @param	d the new value of the Minimize Delay flag.
	 */
	public void setMinimizeDelay(boolean d) {
		BitMask m = new BitMask(container.read(1, 1));
		m.setBit(3, d);
		container.write(1, m.toByteArray(), 0, 1);
	}
	
	/**
	 * Tests if the Maximize Throughput flag is set.
	 * @return	True if the Maximize Throughput flag is set or false if it is not.
	 */
	public boolean isMaximizeThroughput() {
		BitMask m = new BitMask(container.read(1, 1));
		return m.getBit(4);
	}
	
	/**
	 * Sets the Maximize Throughput flag.
	 * @param	t the new value of the Maximize Throughput flag.
	 */
	public void setMaximizeThroughput(boolean t) {
		BitMask m = new BitMask(container.read(1, 1));
		m.setBit(4, t);
		container.write(1, m.toByteArray(), 0, 1);
	}
	
	/**
	 * Tests if the Maximize Reliability flag is set.
	 * @return	True if the Maximize Reliability flag is set  or false if it is not.
	 */
	public boolean isMaximizeReliability() {
		BitMask m = new BitMask(container.read(1, 1));
		return m.getBit(5);
	}
	
	/**
	 * Sets the Maximize Reliability flag.
	 * @param	r the new value of the Maximize Reliability flag.
	 */
	public void setMaximizeReliability(boolean r) {
		BitMask m = new BitMask(container.read(1, 1));
		m.setBit(5, r);
		container.write(1, m.toByteArray(), 0, 1);
	}
	
	/**
	 * Tests if the Minimize Monetary cost flag is set.
	 * @return	True if the Minimize Monetary cost flag is set  or false if it is not.
	 */
	public boolean isMinimizeMonetaryCost() {
		BitMask m = new BitMask(container.read(1, 1));
		return m.getBit(6);
	}
	
	/**
	 * Sets the Minimize Monetary cost flag.
	 * @param	c the new value of the Minimize Monetary cost flag.
	 */
	public void setMinimizeMonetaryCost(boolean c) {
		BitMask m = new BitMask(container.read(1, 1));
		m.setBit(6, c);
		container.write(1, m.toByteArray(), 0, 1);
	}
	
	/**
	 * Returns the value of the total length field.
	 * @return	the value of the total length field.
	 */
	public int getTotalLength() {
		return container.read16(2);
	}
	
	/**
	 * Changes the value of the total length field.
	 * @param	len the new value.
	 */
	public void setTotalLength(int len) {
		container.write16(2, len);
	}
	
	/**
	 * Returns the value of the identification field.
	 * @return	the value of the identification field.
	 */
	public int getIdentification() {
		return container.read16(4);
	}
	
	/**
	 * Changes the value of the identification field.
	 * @param	id the new value.
	 */
	public void setIdentification(int id) {
		container.write16(4, id);
	}
	
	/**
	 * Tests if the Don't Fragment flag is set.
	 * @return	True if the Maximize Reliability flag is set  or false if it is not.
	 */
	public boolean isDontFragment() {
		BitMask m = new BitMask(container.read(6, 1));
		return m.getBit(1);
	}
	
	/**
	 * Sets the Don't Fragment flag.
	 * @param	df the new value of the Don't Fragment flag.
	 */
	public void setDontFragment(boolean df) {
		BitMask m = new BitMask(container.read(6, 1));
		m.setBit(1, df);
		container.write(6, m.toByteArray(), 0, 1);
	}
	
	/**
	 * Tests if the More Fragments flag is set.
	 * @return	True if the More Fragments flag is set  or false if it is not.
	 */
	public boolean isMoreFragments() {
		BitMask m = new BitMask(container.read(6, 1));
		return m.getBit(2);
	}
	
	/**
	 * Sets the More Fragments flag.
	 * @param	mf the new value of the More Fragments flag.
	 */
	public void setMoreFragments(boolean mf) {
		BitMask m = new BitMask(container.read(6, 1));
		m.setBit(2, mf);
		container.write(6, m.toByteArray(), 0, 1);
	}
	
	/**
	 * Returns the value of the Fragment offset field.
	 * @return	the value of the Fragment offset field.
	 */
	public int getFragmentOffset() {
		BitMask m = new BitMask(container.read(6, 2));
		return m.evaluate(3, 13);
	}
	
	/**
	 * Changes the value of the Fragment offset field.
	 * @param	offset the new value.
	 */
	public void setFragmentOffset(int offset) {
		BitMask m = new BitMask(container.read(6, 2));
		m.set(3, 13, offset);
		container.write(6, m.toByteArray(), 0, 2);
	}
	
	/**
	 * Returns the TTL field value.
	 * @return	the TTL field value.
	 */
	public int getTTL() {
		return container.read(8);
	}
	
	/**
	 * Changes the TTL field value.
	 * @param	ttl the new value.
	 */
	public void setTTL(int ttl) {
		container.write(8, ttl);
	}
	
	/**
	 * Returns the protocol field value.
	 * @return	the protocol field value.
	 */
	public int getProtocol() {
		return container.read(9);
	}
	
	/**
	 * Returns the protocol field value.
	 * @param	protocol the new value.
	 */
	public void setProtocol(int protocol) {
		container.write(9, protocol);
	}
	
	/**
	 * Returns the checksum field value.
	 * @return	the checksum field value.
	 */
	public int getChecksum() {
		return container.read16(10);
	}
	
	/**
	 * Returns the checksum field value.
	 * @param	c the new value.
	 */
	public void setChecksum(int c) {
		container.write16(10, c);
	}
	
	/**
	 * Returns the source IP address of this packet.
	 * @return	the source IP address of this packet.
	 */
	public IPv4Address getSrcAddress() {
		return new IPv4Address(container.read(12, 4));
	}
	
	/**
	 * Changes the source IP address of this packet.
	 * @param	address the new IP address
	 */
	public void setSrcAddress(IPv4Address address) {
		container.write(12, address.getAddress(), 0, 4);
	}
	
	/**
	 * Returns the destination IP address of this packet.
	 * @return	the destination IP address of this packet.
	 */
	public IPv4Address getDstAddress() {
		return new IPv4Address(container.read(16, 4));
	}
	
	/**
	 * Changes the destination IP address of this packet.
	 * @param	address the new IP address
	 */
	public void setDstAddress(IPv4Address address) {
		container.write(16, address.getAddress(), 0, 4);
	}
	
	public Packet getPayload() {
		int offset = this.getIHL()*4;
		int length = this.getTotalLength() - offset;
		RawPacket r;
		r = new RawPacket(container.getSubContainer(offset, length));
		return r;
	}
	
	public void setPayload(Packet payload) {
		PacketContainer c = payload.getContainer();
		int ihl = this.getIHL();
		PacketContainer.containerCopy(c, 0, container, ihl*4, c.getSize());
	}
	
	public void setContainer(PacketContainer container) {
		this.container = container;
		BitMask m = new BitMask(container.read(0, 1));
		m.set(0, 4, 4);
		container.write(0, m.toByteArray(), 0, 1);
	}
	
	public Packet getCopy() {
		return new IPv4Packet(container.getCopy());
	}
}
