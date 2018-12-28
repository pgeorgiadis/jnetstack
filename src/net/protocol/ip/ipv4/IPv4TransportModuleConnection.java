package net.protocol.ip.ipv4;

import net.core.packet.Packet;
import net.core.packet.PacketCriteria;

/**
 * IPvTransportModuleConnection is a descriptor with some info required from the IP 
 * protocol module. It describes how the IP module must handle (encapsulate/decapsulate) 
 * packets of this protocol.
 * <br/>
 * This mechanism is provided for protocols other than the transport protocols of the 
 * TCP/IP suite, that use the standar method for packet transmission (the inject method
 * of the IP module). The TCP/IP transport protocols (TCP, UDP, ICMP etc) use the 
 * send mehtod instead.
 * <br/>
 * This mechanism works as follows:<br/>
 * When a module want to use the IP module, it must create a IPv4TransportModuleConnection
 * instance, with the local and destination IP addresses that will use, the protocol type
 * of the transmisions and a TransportPacketCriteria instance, which will be used from 
 * the IP module, to identify packets of this protocol.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since
 * @see		net.core.packet.PacketCriteria	
 */
public class IPv4TransportModuleConnection {
	/** The local IP address of the described communication */
	private IPv4Address local;
	/** The destination IP address of the described communication */
	private IPv4Address destination;
	/** The protocol type of this communication */
	private int protocol;
	/** The criteria object to use, to identify packets of this communication */
	private PacketCriteria criteria;
	
	/**
	 * Gets the destination
	 * @return	the destination
	 */
	public IPv4Address getDestination() {
		return destination;
	}
	
	/**
	 * Sets the destination
	 * @param	destination the destination
	 */
	public void setEndHost(IPv4Address destination) {
		this.destination = destination;
	}
	
	/**
	 * Gets the local address
	 * @return	the local address
	 */
	public IPv4Address getLocalAddress() {
		return local;
	}
	
	/**
	 * Sets the local address
	 * @param	local the local address
	 */
	public void setLocalAddress(IPv4Address local) {
		this.local = local;
	}
	
	/**
	 * Gets the protocol type
	 * @return	 the protocol type
	 */
	public int getProtocol() {
		return protocol;
	}
	
	/**
	 * Gets the protocol type
	 * @param	protocol the protocol type
	 */
	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}
	
	/**
	 * Test if a certain packet matches the given criteria for this protocol
	 * @param	packet the packet to test
	 * @return	true if the packet mathces the given criteria for this protocol
	 */
	public boolean match(Packet packet) {
		return criteria.match(packet);
	}
	
	/**
	 * Sets the criteria object to be used for matching packets of this protocol
	 * @param	criteria the criteria object to be used for matching packets of this protocol
	 */
	public void setCriteria(PacketCriteria criteria) {
		this.criteria = criteria;
	}
}
