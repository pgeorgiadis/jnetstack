package net.protocol.ip.icmp;

import java.util.ArrayList;
import java.util.List;

import net.core.packet.Packet;
import net.core.services.LoggingService;
import net.protocol.ip.ipv4.IPv4Address;
import net.protocol.ip.ipv4.IPv4Constants;
import net.protocol.ip.ipv4.IPv4Protocol;

/**
 * The ICMPProtocol is the module responsible to handle ICMP packets.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	21.8.2005
 */
public class ICMPProtocol {
	/** The IP module of the system */
	private IPv4Protocol ip;
	/** The active icmp packet listeners */
	private List<ICMPListener> listeners;
	
	/**
	 * Initializes the ICMPProtocol module
	 * @param	ip the IP module of the system
	 */
	public ICMPProtocol(IPv4Protocol ip) {
		this.ip = ip;
		this.listeners = new ArrayList<ICMPListener>();
	}
	
	/**
	 * Injects a ICMP packet into this module. This icmp packet will be checksumed and
	 * then forwarded to the IP module.
	 * @param	packet the packet to be injected
	 * @param	src the source IP address to use to send this packet
	 * @param	dst the destination IP address of this icmp packet
	 */
	public void injectICMP(ICMPPacket packet, IPv4Address src, IPv4Address dst) {
		packet.setICMPChecksum(0);
		int sum = Packet.checksum(packet.getContainer(), 0, packet.getSize());
		packet.setICMPChecksum(sum);
		
		ip.send(packet, src, dst, IPv4Constants.ICMP);
	}
	
	public void handle(Packet packet, IPv4Address src, IPv4Address dst) {
		ICMPPacket p = new ICMPPacket(packet.getContainer());
		
		int sum = Packet.checksum(packet.getContainer(), 0, packet.getSize());
		if (sum != -65536) {
			ip.log(LoggingService.LOG_LEVEL_ERROR, "ICMPProtocol: Packet discarded: wrong checksum (sum was:"+sum+")");
			return;
		}
		
		if (p.getType() == ICMPConstants.ECHO_REQUEST) {
			ICMPPacket reply = (ICMPPacket)p.getCopy();
			reply.setType(ICMPConstants.ECHO_REPLY);
			this.injectICMP(reply, dst, src);
		}
		if (p.getType() == ICMPConstants.ECHO_REPLY) {
			synchronized(listeners) {
				for(int i=0; i<listeners.size(); i++) {
					ICMPListener l = (ICMPListener)listeners.get(i);
					if (l.match(src, p.getType()))
						l.handle(p, src, dst);
				}
			}
		}
	}
	
	/**
	 * Adds a ICMPListener
	 * @param	listener the ICMPListener to be added
	 */
	public void addListener(ICMPListener listener) {
		synchronized(listeners) {
			this.listeners.add(listener);
		}
	}
	
	/**
	 * Removes a ICMPListener
	 * @param	listener the ICMPListener to be removed
	 */
	public void removeListener(ICMPListener listener) {
		synchronized(listeners) {
			this.listeners.remove(listener);
		}
	}
}
