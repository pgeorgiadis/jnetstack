package net.protocol.ethernet;

import net.core.AbstractNetModule;
import net.core.NetCore;
import net.core.NetModule;
import net.core.packet.Packet;
import net.core.packet.PacketCapsule;
import net.core.services.EncapsulationServiceException;
import net.core.services.LoggingService;
import net.protocol.arp.ARPConstants;

/**
 * The EthernetProtocol is the module responsible to handle Ethernet frames.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	20.8.2005
 */
public class EthernetProtocol extends AbstractNetModule {
	/** The of EthernetDevices from which this EthernetProtocol accepts packets. */
	private EthernetDevice[] devices;
	
	/**
	 * Creates a new EthernetProtocol and registers it to the NetManager.
	 * @param	core the curent NetManager.
	 */
	public EthernetProtocol(NetCore core) {
		super(core);
		this.devices = new EthernetDevice[10];
	}
	
	/**
	 * Add one EthernetDevice to the device registry.
	 * @param	dev the EthernetDevice to be added.
	 */
	public void addDevice(EthernetDevice dev) {
		for (int i=0; i<devices.length; i++) {
			if (devices[i] == null) {
				devices[i] = dev;
				return;
			}
		}
		EthernetDevice[] tmp = new EthernetDevice[devices.length+5];
		System.arraycopy(devices, 0, tmp, 0, devices.length);
		tmp[devices.length] = dev;
		this.devices = tmp;
	}
	
	/**
	 * Returns the device from the devices registry that has the specified MAC address.
	 * @param	mac the MAC address.
	 * @return	the EthernetDevice from the registry that has the specified MAC address.
	 */
	public EthernetDevice getDevice(EthernetAddress mac) {
		for (int i=0; i<devices.length; i++) {
			if (devices[i] != null && devices[i].getMACAddress().equals(mac))
				return devices[i];
		}
		return null;
	}
	
	/**
	 * Returns the device from the devices registry that has the specified device name.
	 * @param	name the device name.
	 * @return	the EthernetDevice from the registry that has the specified device name.
	 */
	public EthernetDevice getDevice(String name) {
		for (int i=0; i<devices.length; i++) {
			if (devices[i] != null && devices[i].getName().equals(name))
				return devices[i];
		}
		return null;
	}
	
	/**
	 * Removes a EthernetDevice from the devices registry.
	 * @param	name the device name of the device to be removed.
	 */
	public void removeDevice(String name) {
		for (int i=0; i<devices.length; i++) {
			if (devices[i] != null && devices[i].getName().equals(name)) {
				devices[i].setModule(null);
				devices[i] = null;
			}
		}
	}
	
	public NetModule inject(PacketCapsule capsule) {
		EthernetPacket p;
		
		try {
			//get encapsulation hints
			core.getEncapsulationService().getHints(capsule, ARPConstants.H_ETHERNET);
			//prepare the frame for this IP packet
			p = new EthernetPacket(capsule.getPacket().getContainer().getSize());
			p.setPayload(capsule.getPacket());
			//ask ARPProtocol the ethertype of this packet
			p.setEthertype(((Integer)capsule.getHint("ethertype")).intValue());
			p.setSrcAddress((EthernetAddress)capsule.getHint("src_mac"));
			p.setDstAddress((EthernetAddress)capsule.getHint("dst_mac"));
			log(LoggingService.DEBUG_VERBOSE, p.getSrcAddress()+"->"+p.getDstAddress()+" ethertype="+p.getEthertype());
		} catch(EncapsulationServiceException e) {
			log(LoggingService.LOG_LEVEL_ERROR, "EthernetProtocol: Unable to send packet: "+e.getMessage());
			return null;
		}
		
		//get the device that has the same MAC address with the frame's src MAC address
		EthernetDevice dev = this.getDevice(p.getSrcAddress());
		//use this address to send the frame
		capsule.setPacket(p);
		return dev;
	}
	
	public NetModule handle(PacketCapsule capsule) {
		//the packet should be ethernet frame
		EthernetPacket packet = new EthernetPacket(capsule.getPacket().getContainer());
		//extract the payload
		Packet payload = packet.getPayload();
		//view the ethertype and forward the payload to the apropriate module
		capsule.setPacket(payload);
		switch(packet.getEthertype()) {
		case(EthernetConstants.ETH_P_ARP):
			return core.arp;
		case(EthernetConstants.ETH_P_IP):
			return core.ip;
		default:
			return null;
		}
	}
}
