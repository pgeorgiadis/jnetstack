package net.core.services;

import net.core.NetCore;
import net.core.packet.NetworkPacketException;
import net.core.packet.PacketCapsule;
import net.protocol.arp.ARPConstants;
import net.protocol.arp.ARPException;
import net.protocol.arp.ARPPacket;
import net.protocol.ethernet.EthernetAddress;
import net.protocol.ethernet.EthernetConstants;
import net.protocol.ip.ipv4.IPv4Packet;

/**
 * EncapsulationService provides usefull information needed to encapsulate a packet into another.
 * This service is a centralized mechanism that "knows" how to encapsulate the packets of a 
 * protocol into another packet.
 * <br/>
 * The basic method of this service is the getHints method. The getHints method takes two arguments,
 * the ID of the protocol that will carry the encapsulated packet and the actual packet that
 * will be carried. The getHints method returns then a descriptor containing all the information
 * that the carrier protocol will need, in order to encapsulate the payload packet.
 * @author	Pavlos Georgiadis (aka JPG) jpg@freemail.gr
 * @since	12.2.2006
 */
public class EncapsulationService implements NetService {
	/** The assosiated NetCore of this EncapsulationService */
	NetCore core;
	
	/**
	 * Initializes the EncapsulationService.
	 * @param	core the assosiated NetCore of this EncapsulationService
	 */
	public EncapsulationService(NetCore core) {
		this.core = core;
	}
	
	/**
	 * Returns a descriptor containing all the information that the carrier protocol will 
	 * need, in order to encapsulate the payload packet
	 * @param	carrier_protocol the carrier protocol
	 * @param	capsule a PacketCapsule containing the payload packet
	 * @throws	EncapsulationServiceException If this encapsulation is not currently supported by
	 * the encapsulation service or if anything goes wrong during the collection of the required information
	 */
	public void getHints(PacketCapsule capsule, int carrier_protocol) throws EncapsulationServiceException {
		if (capsule.getPacket().getClass() == IPv4Packet.class) {
			//encapsulated protocol is IP use ARP to find hints
			EthernetAddress[] tmp;
			try {
				tmp = core.arp.resolve(capsule.getPacket(), ARPConstants.P_IPv4, carrier_protocol);
			} catch (ARPException e) {
				throw new EncapsulationServiceException(e);
			}
			capsule.setHint("src_mac", tmp[0]);
			capsule.setHint("dst_mac", tmp[1]);
			capsule.setHint("ethertype", new Integer(EthernetConstants.ETH_P_IP));
		} else if (capsule.getPacket().getClass() == ARPPacket.class) {
			ARPPacket p = (ARPPacket)capsule.getPacket();
			try {
				capsule.setHint("src_mac", new EthernetAddress(p.getSrcHAddress()));
				capsule.setHint("dst_mac", new EthernetAddress(p.getDstHAddress()));
			} catch (NetworkPacketException e) {
				throw new EncapsulationServiceException(e);
			}
			capsule.setHint("ethertype", new Integer(EthernetConstants.ETH_P_ARP));
		}
	}
}
