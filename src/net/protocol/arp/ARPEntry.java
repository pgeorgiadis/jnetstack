package net.protocol.arp;

import net.protocol.ethernet.EthernetAddress;
import net.protocol.ip.ipv4.IPv4Address;

/**
 * The ARPEntry is a key value pair of know IP - MAC address.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	8.9.2005
 */
public class ARPEntry {
	/** The IP address of this entry. */
	IPv4Address p_address;
	/** The MAC address of this entry. */
	EthernetAddress h_address;
	/** The timestamp this entry was created. */
	long created;
	
	/**
	 * Creates a new ARPEntry with a given IP and MAC address.
	 * @param	p_address the specified IP address.
	 * @param	h_address the specified MAC address.
	 */
	public ARPEntry(IPv4Address p_address, EthernetAddress h_address) {
		this.p_address = p_address;
		this.h_address = h_address;
		this.created = System.currentTimeMillis();
	}
	
	/**
	 * Returns the MAC address of this ARP entry.
	 * @return	the MAC address of this ARP entry.
	 */
	public EthernetAddress getH_address() {
		return h_address;
	}
	
	/**
	 * Changes the MAC address of this ARPEntry.
	 * @param	h_address the new MAC address.
	 */
	public void setH_address(EthernetAddress h_address) {
		this.h_address = h_address;
	}
	
	/**
	 * Returns the IP address of this ARP entry.
	 * @return	the IP address of this ARP entry.
	 */
	public IPv4Address getP_address() {
		return p_address;
	}
	
	/**
	 * Changes the IP address of this ARPEntry.
	 * @param	p_address the new IP address.
	 */
	public void setP_address(IPv4Address p_address) {
		this.p_address = p_address;
	}
	
	/**
	 * Returns the timestamp of the creation of this ARPEntry.
	 * @return	the timestamp of the creation of this ARPEntry.
	 */
	public long getCreated() {
		return created;
	}
}
