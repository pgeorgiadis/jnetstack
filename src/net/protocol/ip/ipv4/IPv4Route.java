package net.protocol.ip.ipv4;

/**
 * IPv4Route is a route entry
 * @author JPG
 * @since
 */
public class IPv4Route {
	/** The target subnet of this entry */
	IPv4Address subnet;
	/** The subnet mask of the target subnet */
	IPv4Address mask;
	/** The gateway to use for this destinations */
	IPv4Address gateway;
	
	/**
	 * Creates a new IP route entry
	 * @param	subnet the target subnet of this entry
	 * @param	mask the subnet mask of the target subnet
	 * @param	gateway the gateway to use for this destinations
	 */
	public IPv4Route(IPv4Address subnet, IPv4Address mask, IPv4Address gateway) {
		this.subnet = subnet;
		this.mask = mask;
		this.gateway = gateway;
	}
	
	/**
	 * Creates a new IP route entry with default subnet mask 255.255.255.0
	 * @param	subnet the target subnet of this entry
	 * @param	gateway the gateway to use for this destinations
	 */
	public IPv4Route(IPv4Address subnet, IPv4Address gateway) {
		this.subnet = subnet;
		this.mask = new IPv4Address(new byte[] {~0, ~0, ~0, 0});
		this.gateway = gateway;
	}
	
	/**
	 * Gets the target subnet of this entry
	 * @return	the target subnet of this entry
	 */
	public IPv4Address getSubnet() {
		return subnet;
	}
	
	/**
	 * Sets the target subnet of this entry
	 * @param	subnet the target subnet
	 */
	public void setSubnet(IPv4Address subnet) {
		this.subnet = subnet;
	}
	
	/**
	 * Gets the subnet mask of this entry
	 * @return	the subnet mask
	 */
	public IPv4Address getMask() {
		return mask;
	}
	
	/**
	 * Sets the subnet mask of this entry
	 * @param	mask the subnet mask
	 */
	public void setMask(IPv4Address mask) {
		this.mask = mask;
	}
	
	/**
	 * Gets the gateway of this entry
	 * @return the gateway of this entry
	 */
	public IPv4Address getGateway() {
		return this.gateway;
	}
	
	/**
	 * Sets the gateway of this entry
	 * @param	gateway the gateway
	 */
	public void setGateway(IPv4Address gateway) {
		this.gateway = gateway;
	}
	
	/**
	 * Tests if the specified address is in the destination network described by this entry
	 * @param	address the address to be tested
	 * @return	true if the address is in the subnet described by this entry or else false
	 */
	public boolean includesDestination(IPv4Address address) {
		byte[] a1 = subnet.getAddress();
		byte[] a2 = address.getAddress();
		byte[] b = mask.getAddress();
		for (int i=0; i<4; i++) {
			if ((a1[i] & b[i]) != (a2[i] & b[i]))
				return false;
		}
		return true;
	}
	
	public String toString() {
		return this.subnet+"\t"+this.mask+"\t"+this.gateway;
	}
}
