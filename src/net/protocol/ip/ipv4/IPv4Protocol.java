package net.protocol.ip.ipv4;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import net.core.AbstractNetModule;
import net.core.NetCore;
import net.core.NetModule;
import net.core.NetworkException;
import net.core.packet.Packet;
import net.core.packet.PacketCapsule;
import net.core.packet.container.PacketContainer;
import net.core.services.LoggingService;

/**
 * The ICMPProtocol is the module responsible to handle ip version 4 packets.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	20.8.2005
 */
public class IPv4Protocol extends AbstractNetModule {
	/** Registry of all the current fragment sets. */
	private Hashtable fragment_registry;
	/** The IP version 4 interfaces that are currently configured and running. */
	private Hashtable ip_interfaces;
	/** The available IPv4 routes for this host */
	IPv4RoutingTable routing_table;
	/** A registry with all the active transport module connections. */
	private ArrayList transport_connections;
	
	/**
	 * Creates a new IPv4Protocol.
	 * @param core
	 */
	public IPv4Protocol(NetCore core) {
		super(core);
		fragment_registry = new Hashtable();
		ip_interfaces = new Hashtable();
		routing_table = new IPv4RoutingTable();
		transport_connections = new ArrayList();
	}
	
	/**
	 * Sends a TCP/IP suite transport layer packet to the specified destination address
	 * @param	p the packet to be sent
	 * @param	src the source IP address to use for the transmission
	 * @param	dst the destination IP address of this packet
	 * @param	protocol the protocol ID of the transport layer which is sending this packet
	 */
	public void send(Packet p, IPv4Address src, IPv4Address dst, int protocol) {
		IPv4Interface iface;
		try {
			if (src == null) {
				iface = this.getInterfaceForDestination(dst);
			} else {
				iface = this.getInterface(src);
			}
		} catch (IPv4Exception e) {
			log(LoggingService.LOG_LEVEL_ERROR, "IPv4Protocol: Unable to send IPv4 packet: "+e.getMessage());
			log(LoggingService.LOG_LEVEL_ERROR, src+" to "+dst+" protocol: "+protocol);
			return;
		}
		
		//prepare the IP packet
		IPv4Packet packet = new IPv4Packet(p.getSize());
		packet.setPayload(p);
		packet.setDefaults();
		packet.setSrcAddress(iface.getAddress());
		packet.setDstAddress(dst);
		packet.setIdentification(iface.nextID());
		packet.setProtocol(protocol);
		
		//checksum the packet
		packet.setChecksum(0);
		int length = packet.getIHL();
		int sum = Packet.checksum(packet.getContainer(), 0, length*4);
		packet.setChecksum(sum);
		
		//use the interfaces assosiated protocol to send the packet
		core().getThreadPoolService().getThread().pushToInject(iface.getProtocol(), packet);
	}
	
	public NetModule inject(PacketCapsule capsule) {
		try {
			//find the apropriate transport connection
			IPv4TransportModuleConnection entry = this.getTransportConnection(capsule.getPacket());
			
			this.send(capsule.getPacket(), entry.getLocalAddress(), entry.getDestination(), entry.getProtocol());
		} catch (IPv4Exception e) {
			log(LoggingService.LOG_LEVEL_ERROR, "IPv4Protocol: Unable to send IPv4 packet: "+e.getMessage());
		}
		return null;
	}
	
	public NetModule handle(PacketCapsule capsule) {
		IPv4Packet packet = new IPv4Packet(capsule.getPacket().getContainer());
		int length = packet.getIHL();
		
		//test if the packet is for one of our interfaces
		try {
			this.getInterface(packet.getDstAddress());
		} catch (IPv4Exception e) {
			log(LoggingService.LOG_LEVEL_INFO,
					"IPv4Protocol: Packet discarded: "+packet.getDstAddress()+" is not one of my IPv4 addresses");
			return null;
		}
		
		//test the checksum of the packet
		int sum = Packet.checksum(packet.getContainer(), 0, length*4);
		if (sum != -65536) {
			log(LoggingService.LOG_LEVEL_WARNING,
					"IPv4Protocol: Packet discarded: wrong checksum (sum was:"+sum+")");
			return null;
		}
		
		Packet payload;
		if (packet.isMoreFragments()){
			//it is fragment, find the related fragment set and put this one too
			int id = packet.getIdentification();
			int offset = packet.getFragmentOffset();
			PacketContainer fragment = packet.getPayload().getContainer();
			IPv4FragmentSet set = (IPv4FragmentSet)fragment_registry.get(new Integer(id));
			boolean completed = set.addFragment(offset, fragment, packet.isMoreFragments());
			//if this was the last fragment that we expected bind it and forward it to the apropriate module
			if (completed) {
				try{
					payload = set.bind();
				}catch (NetworkException e) {
					log(LoggingService.LOG_LEVEL_ERROR, "IPv4Protocol: fragments bind failed: "+e.getMessage());
					return null;
				}
			} else
				return null;
		} else
			payload = packet.getPayload();
		
		//find the apropriate module and forward the payload
		switch(packet.getProtocol()) {
		case(IPv4Constants.TCP):
			core.tcp.handle(payload, packet.getSrcAddress(), packet.getDstAddress());
			break;
		case(IPv4Constants.UDP):
			core.udp.handle(payload, packet.getSrcAddress(), packet.getDstAddress());
			break;
		case(IPv4Constants.ICMP):
			core.icmp.handle(payload, packet.getSrcAddress(), packet.getDstAddress());
			break;
		}
		return null;
	}
	
	public boolean isConnectedRoute(IPv4Address address) {
		Iterator i = ip_interfaces.values().iterator();
		while(i.hasNext()) {
			IPv4Interface iface = (IPv4Interface)i.next();
			if (iface.isSameNet(address))
				return true;
		}
		return false;
	}
	
	/**
	 * Returns the IPv4Interface that has the specified IPv4Address.
	 * @param	address the address of the IPv4Interface that we want.
	 * @return	the IPv4Interface that has the specified IPv4Address.
	 * @throws	IPv4Exception there is no IPv4Interface with this address.
	 */
	public IPv4Interface getInterface(IPv4Address address) throws IPv4Exception {
		Iterator i = ip_interfaces.keySet().iterator();
		while(i.hasNext()) {
			IPv4Address a = (IPv4Address)i.next();
			if (a.equals(address))
				return (IPv4Interface)ip_interfaces.get(a);
		}
		throw new IPv4Exception("IPv4 Interface "+address+" not found");
	}
	
	/**
	 * Searches for a interface that is in the same subnet as the specified destination address. This
	 * method finds a route that we can use for the specified destination.
	 * @param	destination the destination we want to reach
	 * @return	the apropriate interface that we can use to reach the specified destination
	 * @throws	IPv4Exception if there is no route to this destination
	 */
	public IPv4Interface getInterfaceForDestination(IPv4Address destination) throws IPv4Exception {
		Iterator i = ip_interfaces.values().iterator();
		while(i.hasNext()) {
			IPv4Interface iface = (IPv4Interface)i.next();
			if (iface.isSameNet(destination))
				return iface;
		}
		
		IPv4Address address = routing_table.getRoute(destination);
		if (address != null)
			return this.getInterfaceForDestination(address);
		
		throw new IPv4Exception("The destination is not in any local network: "+destination);
	}
	
	/**
	 * Adds one IPv4Interface to the registry.
	 * @param	iface the IPv4Interface to be added.
	 */
	public void addInterface(IPv4Interface iface) {
		ip_interfaces.put(iface.getAddress(), iface);
	}
	
	/**
	 * Removes one IPv4Interface from the interface registry.
	 * @param	address the IPv4Interface that will be removed.
	 */
	public void removeInterface(IPv4Address address) {
		try {
			ip_interfaces.remove(this.getInterface(address).getAddress());
		} catch (IPv4Exception e) {
			log(LoggingService.LOG_LEVEL_WARNING,
					"IPv4Protocol: Unable to remove IPv4 interface: "+e.getMessage());
		}
	}
	
	/**
	 * Gets the routing table of the system
	 * @return the routing table of the system
	 */
	public IPv4RoutingTable getRoutingTable() {
		return routing_table;
	}
	
	/**
	 * Returns the transport connection that match with this packet.
	 * @param	packet the packet to test if it match.
	 * @return	the related transport connection.
	 * @throws	IPv4Exception if no transport connection found
	 */
	public IPv4TransportModuleConnection getTransportConnection(Packet packet) throws IPv4Exception {
		synchronized(transport_connections) {
			for(int i=0; i<transport_connections.size(); i++) {
				IPv4TransportModuleConnection user = (IPv4TransportModuleConnection)transport_connections.get(i);
				if (user.match(packet))
					return user;
			}
			throw new IPv4Exception("Transport connection not found");
		}
	}
	
	/**
	 * Adds a transport connection to the transport connections registry.
	 * @param	connection the transport connection to be added.
	 */
	public void addTransportConnection(IPv4TransportModuleConnection connection) {
		synchronized(transport_connections) {
			transport_connections.add(connection);
		}
	}
	
	/**
	 * Removes a transport connection from the registry.
	 * @param	connection the transport connection to be removed.
	 */
	public void removeTransportConnection(IPv4TransportModuleConnection connection) {
		synchronized(transport_connections) {
			transport_connections.remove(connection);
		}
	}
}
