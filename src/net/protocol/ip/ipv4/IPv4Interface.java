package net.protocol.ip.ipv4;

import net.core.NetworkDevice;
import net.core.NetModule;

/**
 * The IPv4Interface instances represent a IP interface.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	19.8.2005
 */
public class IPv4Interface {
	/** The host address of this IP interface. */
	private IPv4Address address;
	/** The netmask of the subnet of this interface. */
	private IPv4Address mask;
	/** The broadcast address of the subnet. */
	private IPv4Address broadcast;
	/** The device that this interface uses. */
	private NetworkDevice device;
	/** The protocol to which this interface sends packets. */
	private NetModule protocol;
	/**
	 * The value of the next identification that this interface will provide for the Identification
	 * field of the IP packets.
	 */
	private short id = 0;
	
	/**
	 * Creates a new IPv4Interface.
	 * @param	ip the host address of this interface.
	 * @param	protocol the protocol that this interface will use.
	 * @param	device the device that this interface will use.
	 */
	public IPv4Interface(int[] ip, NetModule protocol, NetworkDevice device) {
		this.address = new IPv4Address(ip);
		this.mask = new IPv4Address(new byte[] {~0, ~0, ~0, 0});
		this.broadcast = IPv4Interface.calculateBroadcast(address, mask);
		this.protocol = protocol;
		this.device = device;
	}
	
	/**
	 * Creates a new IPv4Interface.
	 * @param	ip the host address of this interface.
	 * @param	protocol the protocol that this interface will use.
	 * @param	device the device that this interface will use.
	 */
	public IPv4Interface(String ip, NetModule protocol, NetworkDevice device) {
		this.address = new IPv4Address(ip);
		this.mask = new IPv4Address(new byte[] {~0, ~0, ~0, 0});
		this.broadcast = IPv4Interface.calculateBroadcast(address, mask);
		this.protocol = protocol;
		this.device = device;
	}
	
	/**
	 * Creates a new IPv4Interface.
	 * @param	address the host address of this interface.
	 * @param	protocol the protocol that this interface will use.
	 * @param	device the device that this interface will use.
	 */
	public IPv4Interface(IPv4Address address, NetModule protocol, NetworkDevice device) {
		this.address = address;
		this.mask = new IPv4Address(new byte[] {~0, ~0, ~0, 0});
		this.broadcast = IPv4Interface.calculateBroadcast(address, mask);
		this.protocol = protocol;
		this.device = device;
	}
	
	/**
	 * Creates a new IPv4Interface.
	 * @param	address the host address of this interface.
	 * @param	mask the netmask of the subnet.
	 * @param	protocol the protocol that this interface will use.
	 * @param	device the device that this interface will use.
	 */
	public IPv4Interface(IPv4Address address, IPv4Address mask, NetModule protocol, NetworkDevice device) {
		this.address = address;
		this.mask = mask;
		this.protocol = protocol;
		this.device = device;
	}
	
	/**
	 * Returns the host address of this interface.
	 * @return	the host address of this interface.
	 */
	public IPv4Address getAddress() {
		return address;
	}
	
	/**
	 * Changes the host address of this interface.
	 * @param address the new host address.
	 */
	public void setAddress(IPv4Address address) {
		this.address = address;
		this.broadcast = IPv4Interface.calculateBroadcast(address, this.mask);
	}
	
	/**
	 * Returns the broadcast address of the subnet of this interface.
	 * @return	the broadcast address of the subnet of this interface.
	 */
	public IPv4Address getBroadcast() {
		return broadcast;
	}
	
	/**
	 * Returns the device that this interface is using.
	 * @return	the device that this interface is using.
	 */
	public NetworkDevice getDevice() {
		return device;
	}
	
	/**
	 * Changes the device that this interface is using.
	 * @param device the new device that this interface will use.
	 */
	public void setDevice(NetworkDevice device) {
		this.device = device;
	}
	
	/**
	 * Returns the protocol that this interface is using.
	 * @return	the protocol that this interface is using.
	 */
	public NetModule getProtocol() {
		return protocol;
	}
	
	/**
	 * Changes the protocol that this interface is using.
	 * @param protocol the new protocol that this interface will use.
	 */
	public void setProtocol(NetModule protocol) {
		this.protocol = protocol;
	}
	
	/**
	 * Returns the next Identification number for the packets of this interface.
	 * @return	the next Identification number for the packets of this interface.
	 */
	public int nextID() {
		return (int)id++;
	}
	
	/**
	 * Tests if the specified address is on the same subnet with this interface
	 * @param	a the address to check
	 * @return	true if the specified address is in the same subnet with this interface or else false
	 */
	public boolean isSameNet(IPv4Address a) {
		byte[] a1 = address.getAddress();
		byte[] a2 = a.getAddress();
		byte[] b = mask.getAddress();
		for (int i=0; i<4; i++) {
			if ((a1[i] & b[i]) != (a2[i] & b[i]))
				return false;
		}
		return true;
	}
	
	public String toString() {
		return "iface["+this.address+"/"+this.mask+"]";
	}
	
	/**
	 * Calculates the broadcast address from the host address and netmask.
	 * @param	ip the host address.
	 * @param	mask the netmask.
	 * @return	the broadcast address of the subnet of ip.
	 */
	public static IPv4Address calculateBroadcast(IPv4Address ip, IPv4Address mask) {
		byte[] a1 = ip.getAddress();
		byte[] a2 = mask.getAddress();
		byte[] b = new byte[4];
		for (int i=0; i<4; i++) {
			a2[i] = (byte)~a2[i];
			b[i] = (byte)(a1[i] | a2[i]);
		}
		return new IPv4Address(b);
	}
}
