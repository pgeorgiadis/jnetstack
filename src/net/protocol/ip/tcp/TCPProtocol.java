package net.protocol.ip.tcp;

import java.util.ArrayList;
import java.util.Random;

import net.core.NetCore;
import net.core.packet.Packet;
import net.core.services.LoggingService;
import net.protocol.ip.ipv4.IPv4Address;
import net.protocol.ip.ipv4.IPv4Constants;
import net.protocol.ip.ipv4.IPv4Protocol;

/**
 * The TCPProtocol is the module responsible to handle tcp packets.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	21.8.2005
 */
public class TCPProtocol {
	private ArrayList listeners;
	private IPv4Protocol ip;
	
	/**
	 * Creates a new TCPProtocol.
	 * @param ip
	 */
	public TCPProtocol(IPv4Protocol ip, NetCore core){
		this.ip = ip;
		this.listeners = new ArrayList();
	}
	
	/**
	 * Sends a TCP packet.
	 * @param	packet the TCP packet to be sent
	 * @param	src the source IP address to use for the transmission
	 * @param	dst the destination IP address of this TCP packet.
	 */
	public void sendTCP(TCPPacket packet, IPv4Address src, IPv4Address dst) {
		TCPPseudoHeader pseudo_header = new TCPPseudoHeader(src, dst, packet);
		packet.setChecksum(pseudo_header.getChecksum());
		
		ip.log(LoggingService.DEBUG_VERBOSE, ">TCP packet, from "+src+" to "+dst+" ["+packet+"]");
		
		ip.send(packet, src, dst, IPv4Constants.TCP);
	}
	
	/**
	 * Encapsulates a packet into a TCP packet and then sends it.
	 * @param	packet the packet to encapsulate
	 * @param	src the source IP address to use for the transmission
	 * @param	dst the destination IP address of this TCP packet.
	 */
	public void handle(Packet packet, IPv4Address src, IPv4Address dst) {
		TCPPacket p = new TCPPacket(packet.getContainer());
		
		ip.log(LoggingService.DEBUG_VERBOSE, "<TCP packet, from "+src+" to "+dst+" ["+p+"]");
		
		synchronized(listeners) {
			for(int i=0; i<listeners.size(); i++) {
				TCPListener l = (TCPListener)listeners.get(i);
				if (l.match(src, dst, p.getSrcPort(), p.getDstPort()))
					l.handle(p, src, dst);
			}
		}
	}
	
	/**
	 * Adds a TCPListener
	 * @param	listener the TCPListener to be added
	 */
	public void addListener(TCPListener listener) {
		synchronized(listeners) {
			this.listeners.add(listener);
		}
	}
	
	/**
	 * Removes a TCPListener
	 * @param	listener the TCPListener to be removed
	 */
	public void removeListener(TCPListener listener) {
		synchronized(listeners) {
			this.listeners.remove(listener);
		}
	}
	
	public void log(String message) {
		ip.log(LoggingService.LOG_LEVEL_INFO, message);
	}
	
	public int getFreePort() {
		//TODO improve this method
		Random r = new Random();
		byte[] b = new byte[1];
		r.nextBytes(b);
		return b[0] & 0xff;
	}
}
