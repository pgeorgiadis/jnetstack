package net.protocol.ip.udp;

import java.util.ArrayList;

import net.core.packet.Packet;
import net.protocol.ip.ipv4.IPv4Address;
import net.protocol.ip.ipv4.IPv4Constants;
import net.protocol.ip.ipv4.IPv4Protocol;

/**
 * The UDPProtocol is the module responsible to handle UDP packets.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	21.9.2005
 */
public class UDPProtocol {
	/** the IP module of the system */
	private IPv4Protocol ip;
	/** The active udp packet listeners */
	private ArrayList listeners;
	
	/**
	 * Creates a new UDPProtocol instance.
	 * @param	ip the IP module of the system
	 */
	public UDPProtocol(IPv4Protocol ip){
		this.ip = ip;
		this.listeners = new ArrayList();
	}
	
	/**
	 * Injects a UDP packet into this module. This udp packet will be checksumed and
	 * then forwarded to the IP module.
	 * @param	packet the packet to be injected
	 * @param	src the source IP address to use to send this packet
	 * @param	dst the destination IP address of this udp packet
	 */
	public void sendUDP(UDPPacket packet, IPv4Address src, IPv4Address dst) {
		UDPPseudoHeader pseudo_header = new UDPPseudoHeader(src, dst, packet);
		packet.setChecksum(pseudo_header.getChecksum());
		
		ip.send(packet, src, dst, IPv4Constants.UDP);
	}
	
	public void handle(Packet packet, IPv4Address src, IPv4Address dst) {
		UDPPacket p = new UDPPacket(packet.getContainer());
		
		synchronized(listeners) {
			for(int i=0; i<listeners.size(); i++) {
				UDPListener l = (UDPListener)listeners.get(i);
				if (l.match(dst, p.getDstPort()))
					l.handle(p, src, dst);
			}
		}
	}
	
	/**
	 * Adds a UDPListener
	 * @param	listener the UDPListener to be added
	 */
	public void addListener(UDPListener listener) {
		synchronized(listeners) {
			this.listeners.add(listener);
		}
	}
	
	/**
	 * Removes a UDPListener
	 * @param	listener the UDPListener to be removed
	 */
	public void removeListener(UDPListener listener) {
		synchronized(listeners) {
			this.listeners.remove(listener);
		}
	}
}
