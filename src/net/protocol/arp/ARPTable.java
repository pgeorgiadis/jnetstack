package net.protocol.arp;

import java.util.TimerTask;

import net.core.services.SchedulerService;
import net.protocol.ip.ipv4.IPv4Address;

/**
 * The ARPTable holds all the ARP addresses that are currently known to the system.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	8.9.2005
 * @see		ARPEntry
 */
public class ARPTable {
	/** The array that holds the ARP table entries. */
	private ARPEntry[] arp_table;
	/** The maximum age of the ARP table entries. */
	private int max_entry_age;
	
	/**
	 * Creates a new ARPTable with initial space for 25 entries and a maximum entry age of 15 minutes. It also 
	 * schedules periodicly clean-ups of this ARPTable, to remove aged entries.
	 */
	public ARPTable(SchedulerService scheduler) {
		this.arp_table = new ARPEntry[25];
		this.max_entry_age = 900000;
		
		scheduler.schedule(
				new TimerTask() {
					public void run() {
						cleanUp();
						if (max_entry_age == 0) {
							this.cancel();
						}
					}
				},
				this.max_entry_age,
				this.max_entry_age);
	}
	
	/**
	 * Returns the entry of the table with the specified IP address.
	 * @param	ip the IP address we search for.
	 * @return	the entry of the table with the specified IP address.
	 */
	public ARPEntry getEntry(IPv4Address ip) {
		for (int i=0; i<arp_table.length; i++) {
			if (arp_table[i] == null) break;
			if (arp_table[i].getP_address().equals(ip)) {
				return arp_table[i];
			}
		}
		return null;
	}
	
	/**
	 * Adds one entry to the ARP table. If the ARP table is full, it is expanded.
	 * @param	e the entry to be added.
	 */
	public void addEntry(ARPEntry e){
		for (int i=0; i<arp_table.length; i++) {
			if (arp_table[i] == null) {
				arp_table[i] = e;
				return;
			}
		}
		ARPEntry[] tmp = new ARPEntry[arp_table.length+10];
		System.arraycopy(arp_table, 0, tmp, 0, arp_table.length);
		tmp[arp_table.length] = e;
		this.arp_table = tmp;
	}
	
	/**
	 * Removes the entry of the ARP table that has the specified IP address.
	 * @param	ip the IP address of the entry to be removed.
	 */
	public void remove(IPv4Address ip){
		for (int i=0; i<arp_table.length; i++) {
			if (arp_table[i].getP_address().equals(ip)) {
				arp_table[i] = null;
				return;
			}
		}
	}
	
	/**
	 * Checks the age of all the entries of the table and removes the entries that have passed.
	 * the maximum entry age.
	 */
	public void cleanUp(){
		long now = System.currentTimeMillis();
		for (int i=0; i<arp_table.length; i++) {
			if (arp_table[i] == null) continue;
			if (now - arp_table[i].getCreated() > this.max_entry_age)
				arp_table[i] = null;
		}
	}
	
	public void finalize() throws Throwable {
		this.max_entry_age = 0;
		super.finalize();
	}
}
