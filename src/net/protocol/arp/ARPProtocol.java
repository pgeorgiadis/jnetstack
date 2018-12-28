package net.protocol.arp;

import java.util.ArrayList;

import net.core.AbstractNetModule;
import net.core.NetCore;
import net.core.NetModule;
import net.core.NetworkException;
import net.core.packet.NetworkPacketException;
import net.core.packet.Packet;
import net.core.packet.PacketCapsule;
import net.core.services.LoggingService;
import net.protocol.ethernet.EthernetAddress;
import net.protocol.ethernet.EthernetDevice;
import net.protocol.ip.ipv4.IPv4Address;
import net.protocol.ip.ipv4.IPv4Interface;
import net.protocol.ip.ipv4.IPv4Packet;

/**
 * The ARPProtocol is the module responsible to handle ARP packets.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	19.8.2005
 */
public class ARPProtocol extends AbstractNetModule {
	/** The arp table of the system. It is the cache of already known arp entries. */
	private ARPTable table;
	/** The active ARP discovery procedures. */
	private ArrayList<ARPDiscovery> discoveries;
	/** The ARP request timeout. */
	private int request_timeout;
	
	/**
	 * Creates a new ARPProtocol. The default ARP request timeout is set to 1 second.
	 * @param	core the networking subsystem core
	 */
	public ARPProtocol(NetCore core){
		super(core);
		this.table = new ARPTable(core.getSchedulerService());
		this.discoveries = new ArrayList<ARPDiscovery>();
		this.request_timeout = 2000;
	}
	
	/**
	 * Returns a pair of MAC addresses (source and destination) that the EthernetProtocol should 
	 * use to send a certain packet.
	 * @param	p the packet that EthernetProtocol want to send.
	 * @param	protocol_type the protocol type of the encapsulated protocol
	 * @param	hardware_type the hardware type of the requesting protocol module (EthernetProtocol).
	 * @return	a pair of MAC addresses (source and destination) that the EthernetProtocol should 
	 * 			use to send a certain packet.
	 * @see		ARPConstants
	 */
	public EthernetAddress[] resolve(Packet p, int protocol_type, int hardware_type) throws ARPException {
		switch (protocol_type) {
		case ARPConstants.P_IPv4:
			try {
				IPv4Packet packet = (IPv4Packet)p;
				IPv4Address src_addr = packet.getSrcAddress();
				IPv4Interface iface = core.ip.getInterface(src_addr);
				EthernetDevice dev = (EthernetDevice)iface.getDevice();
				IPv4Address dst_addr = packet.getDstAddress();
				
				if (!core.ip.isConnectedRoute(packet.getDstAddress()))
					dst_addr = core.ip.getRoutingTable().getRoute(packet.getDstAddress());		//it's not a connected route, the remote mac must be the routers mac
				
				//look if we have answer in the cache
				ARPEntry entry = table.getEntry(dst_addr);
				if (entry != null)
					return new EthernetAddress[] {dev.getMACAddress(), (EthernetAddress)entry.getH_address()};		//we have, return the addresses
				
				log(LoggingService.DEBUG_INFO, "ARPProtocol, starting arp discovery");
				//we don't have, initiate ARPDiscovery
				ARPDiscovery find = new ARPDiscovery(this, dev, src_addr.getAddress(), dst_addr.getAddress());
				
				try {
					//wait for 3 arp requests
					synchronized(find) {
						find.wait(3*this.request_timeout);
					}
				} catch (InterruptedException e) {e.printStackTrace();}
				
				//try to get the answer
				if (find.getAnswer() == null)
					throw new ARPException("No arp reply for address "+src_addr);
				//return new EthernetAddress[] {dev.getMACAddress(), find.getAnswer()[1]};
				return find.getAnswer();
			} catch (NetworkException e) {
				if (e.getClass() == ARPException.class) throw (ARPException)e;
				throw new ARPException(e);
			}
		default:
			//if the packe was not IPv4Packet throw exception, we dont know how to handle other types
			throw new ARPException("ARP resolution for network protocols other than IPv4 is not implemented yet");
		}
	}
	
	/**
	 * Returns the configured ARP request timeout.
	 * @return	the conigured ARP request timeout.
	 */
	public int getRequest_timeout() {
		return request_timeout;
	}
	
	public NetModule inject(PacketCapsule capsule) {
		return core.ethernet;
	}
	
	public NetModule handle(PacketCapsule capsule) {
		ARPPacket a = new ARPPacket(capsule.getPacket().getContainer());
		int opcode = a.getOpcode();
		if (opcode == ARPConstants.OP_REQUEST) {
			//the packet is a request we must send a reply
			try {
				log(LoggingService.DEBUG_VERBOSE, "<ARP who has:"+new IPv4Address(a.getDstPAddress())+" tell:"+new IPv4Address(a.getSrcPAddress()));
				
				final ARPPacket reply = new ARPPacket(6, 4);
				reply.setOpcode(ARPConstants.OP_REPLY);
				IPv4Address ip_dst = new IPv4Address(a.getDstPAddress());
				IPv4Interface iface = core.ip.getInterface(ip_dst);
				if (iface == null) return null;
				EthernetDevice eth_dev = (EthernetDevice)iface.getDevice();
				
				reply.setHardwareType(ARPConstants.H_ETHERNET);
				reply.setProtocolType(ARPConstants.P_IPv4);
				reply.setSrcHAddress(eth_dev.getMACAddress().getAddress());
				reply.setSrcPAddress(iface.getAddress().getAddress());
				reply.setDstHAddress(a.getSrcHAddress());
				reply.setDstPAddress(a.getSrcPAddress());
				
				log(LoggingService.DEBUG_VERBOSE, ">ARP "+new IPv4Address(reply.getSrcPAddress())+" is at "+new EthernetAddress(reply.getSrcHAddress()));
				core.getThreadPoolService().getThread().pushToInject(core.ethernet, reply);
			} catch (NetworkException e) {}
		}
		else if (opcode == ARPConstants.OP_REPLY) {
			//the packet is an ARP reply
			try {
				log(LoggingService.DEBUG_VERBOSE, ">ARP "+new IPv4Address(a.getSrcPAddress())+" is at "+new EthernetAddress(a.getSrcHAddress()));
				
				IPv4Address ip = new IPv4Address(a.getSrcPAddress());
				EthernetAddress mac = new EthernetAddress(a.getSrcHAddress());
				ARPEntry entry = new ARPEntry(ip, mac);
				
				//add this entry to our cache for later use
				table.addEntry(entry);
				
				//find if we have active discovery for this ip address
				ARPDiscovery discovery = getDiscovery(ip);
				//if we have put the answer
				if (discovery != null)
					discovery.putAnswer(entry);
			} catch (NetworkPacketException e) {
				log(LoggingService.LOG_LEVEL_ERROR, "ARPProtocol: Unable to handle ARP reply: "+e.getMessage());
			}
		}
		return null;
	}
	
	/**
	 * Finds if there is active ARPDiscovery for a particular IP address and if there is one
	 * returns it.
	 * @param	ip the wanted IP address.
	 * @return	The active ARPDiscovery related to this IP address or null if there is none.
	 * @throws	NetworkPacketException If there is a problem with the ARPDiscovery.
	 */
	private ARPDiscovery getDiscovery(IPv4Address ip) throws NetworkPacketException {
		ARPDiscovery discovery = null;
		synchronized(discoveries) {
			for(int i=0; i<discoveries.size(); i++) {
				discovery = (ARPDiscovery)discoveries.get(i);
				IPv4Address tmp = new IPv4Address(discovery.getRequest().getDstPAddress());
				if (tmp.equals(ip))
					break;
			}
			discoveries.notifyAll();
			return discovery;
		}
	}
	
	/**
	 * Adds a ARP discovery procedure to the registry of the active ARPDiscovery(s)
	 * @param	d the ARPDiscovery to be added.
	 */
	public void addDiscovery(ARPDiscovery d) {
		synchronized(discoveries) {
			discoveries.add(d);
			discoveries.notifyAll();
		}
	}
	
	/**
	 * Removes a ARPDiscovery when it is not active anymore.
	 * @param	d the ARPDiscovery to be removed.
	 */
	public void removeDiscovery(ARPDiscovery d) {
		synchronized(discoveries) {
			discoveries.remove(d);
			discoveries.notifyAll();
		}
	}
}
