package net.protocol.arp;

import net.core.packet.NetworkPacketException;
import net.core.packet.Packet;
import net.protocol.ethernet.EthernetAddress;
import net.protocol.ethernet.EthernetDevice;

/**
 * The ARPDiscovery creates an ARP request packet. Then this packet is sent  through the ethernet module 
 * 3 times with intervals equal to the systems ARP request timeout.<br>
 * When the apropriate ARP reply arrive through the ARPProtocol before all the timeouts end, 
 * this ARPDiscovery instance is answered and it's getAnswer() method returns the needed MAC addresses. 
 * (The ARPProtocol usualy adds the apropriate ARPEntry to the ARPTable for later use).<br>
 * If timed out the getAnswer() method returns null.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	8.9.2005
 * @see		ARPProtocol#resolve(Packet, int, int)
 */
public class ARPDiscovery extends Thread {
	/** The ARPProtocol that created this ARPDiscovery. */
	private ARPProtocol parent;
	/** The device that will be used for this ARPDiscovery. */
	private EthernetDevice device;
	/** The generated ARP request. */
	private ARPPacket request;
	/** The answer of this ARPDiscovery. It is null until the end of the discovery procedure. */
	private ARPEntry answer;
	
	/**
	 * Creates a new ARPDiscovery.
	 * @param	parent the ARPProtocol that will be used as the parent for this discovery procedure.
	 * @param	device the device that will be used for this discovery procedure.
	 * @param	p_my the source IP address that will be used for this discovery procedure.
	 * @param	p_dst the destination IP addres which is related to the requested MAC address.
	 * @throws	NetworkPacketException if one of the IP addresses is greater than 4 bytes.
	 */
	public ARPDiscovery(ARPProtocol parent, EthernetDevice device, byte[] p_my, byte[] p_dst)
	throws NetworkPacketException {
		this.setName("ARP discovery");
		this.parent = parent;
		this.device = device;
		
		request = new ARPPacket(6, 4);
		request.setHardwareType(ARPConstants.H_ETHERNET);
		request.setProtocolType(ARPConstants.P_IPv4);
		request.setOpcode(ARPConstants.OP_REQUEST);
		request.setDstHAddress(EthernetAddress.BROADCAST_MAC.getAddress());
		request.setDstPAddress(p_dst);
		request.setSrcHAddress(device.getMACAddress().getAddress());
		request.setSrcPAddress(p_my);
		
		parent.addDiscovery(this);
		
		this.start();
	}
	
	/**
	 * Returns the ARPRequest of this discovery procedure.
	 * @return	the ARPRequest of this discovery procedure.
	 */
	public ARPPacket getRequest() {
		return this.request;
	}
	
	/**
	 * Returns the answer of this discovery.
	 * @return	the answer of this discovery.
	 */
	public EthernetAddress[] getAnswer() {
		if (answer == null) return null;
		return new EthernetAddress[] {device.getMACAddress(), answer.getH_address()};
	}
	
	/**
	 * Called from the ARPProtocol to fill the answer of this ARPDiscovery.
	 * @param	entry the answer of this ARPDiscovery.
	 */
	public void putAnswer(ARPEntry entry) {
		if (this.answer != null) return;
		this.answer = entry;
		synchronized(this) {
			this.notifyAll();
		}
	}
	
	public void run() {
		try {
			for (int i=0; i<3; i++) {
				parent.core().getThreadPoolService().getThread().pushToInject(parent, request);
				Thread.sleep(parent.getRequest_timeout());
				if (answer != null) break;
			}
		} catch (InterruptedException e) {e.printStackTrace();}
		parent.removeDiscovery(this);
	}
}
