package net.protocol.ethernet;

import net.core.NetCore;
import net.core.NetworkDevice;
import net.core.NetModule;

/**
 * The class EthernetDevice is the Superclass of all ethernet device.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	14.8.2005
 */
public abstract class EthernetDevice extends NetworkDevice {
	/** The EthernetAddress of the ethernet device. */
	protected EthernetAddress mac;
	
	/**
	 * Creates a new EthernetDevice with the specified device name and MAC address.
	 * @param	name the device name of the EthernetDevice.
	 * @param	mac the MAC address of the EthernetDevice.
	 */
	public EthernetDevice(NetCore core, String name, EthernetAddress mac) {
		super(core, name);
		this.mac = mac;
	}
	
	/**
	 * Returns the EtehrnetAddress of this device.
	 * @return	the EthernetAddress of this device.
	 */
	public EthernetAddress getMACAddress() {
		return mac;
	}
	
	/**
	 * Changes the EthernetAddress of this device.
	 * @param	mac the EthrenetAddress of this device.
	 */
	public void setMACAddress(EthernetAddress mac) {
		this.mac = mac;
	}
	
	/**
	 * Changes the NetModule that will receive the incomming packets from this NetworkDevice. The
	 * NetModule that usualy receives packets from an EthernetDevice is one EthernetProtocol. In this 
	 * case we must register this device to the EthernetProtocol too.
	 */
	public void setModule(NetModule protocol) {
		if (protocol == null)
			this.receiver = null;
		try {
			EthernetProtocol p = (EthernetProtocol)protocol;
			p.addDevice(this);
		} catch (RuntimeException e) {}
		super.setModule(protocol);
	}
}
