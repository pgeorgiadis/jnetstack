package net.protocol.ip.ipv4;

/**
 * IPv4Address represents the IP addresses for the IP version 4 protocol.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	10.4.2005
 */
public class IPv4Address {
	/** The byte[] whichs holds the IP address. The length of this array should always be 4 bytes. */
	byte[] address;
	
	/**
	 * Creates a new IPv4Address with address 0.0.0.0.
	 */
	public IPv4Address(){
		this.address = new byte[4];
	}
	
	/**
	 * Creates a new IPv4Address from a given byte[].
	 * @param	b the IP address we want to create in byte[] form.
	 * @throws	IllegalArgumentException when the b array length is not 4 bytes.
	 */
	public IPv4Address(byte[] b) {
		if (b.length != 4)
			throw new IllegalArgumentException("IPv4 addresses must have length 4 byte");
		this.address = new byte[4];
		this.setAddress(b);
	}
	
	/**
	 * Creates a new IPv4Address from a given int[].
	 * @param	a the IP address we want to create in int[] form.
	 * @throws	IllegalArgumentException when the b array length is not 4 bytes or if one of the
	 * 			integers in the a array is greater than 255.
	 */
	public IPv4Address(int[] a) {
		this.address = new byte[4];
		if (a.length != 4)
			throw new IllegalArgumentException("IPv4 addresses must have length 4 byte");
		for (int i=0; i<4; i++) {
			if (a[i] > 255)
				throw new IllegalArgumentException("IPv4 byte cannot be larger than 255");
			this.address[i] = (byte)(a[i] & 0xff);
		}
	}
	
	/**
	 * Creates a new IPv4Address from String.
	 * @param	s the IP address we want to create as a String.
	 * @throws	IllegalArgumentException if the String is not in the form X.X.X.X.
	 */
	public IPv4Address(String s) {
		this.address = new byte[4];
		s = s.concat(".");
		for (int i=0; i<4; i++) {
			int dot = s.indexOf('.');
			int tmp = Integer.parseInt(s.substring(0, dot));
			if (tmp > 255)
				throw new IllegalArgumentException("IPv4 byte cannot be larger than 255");
			this.address[i] = (byte)(tmp & 0xff);
			s = s.substring(dot+1);
		}
		if (!s.equals(""))
			throw new IllegalArgumentException("IPv4 address must be in the form of X.X.X.X");
	}
	
	/**
	 * Returns this address as a byte[].
	 * @return	a byte[] containing this address.
	 */
	public byte[] getAddress() {
		byte[] b = new byte[4];
		System.arraycopy(address, 0, b, 0, 4);
		return b;
	}
	
	/**
	 * Sets this address.
	 * @param	b the address we want to set in byte[] form.
	 * @throws	ArrayIndexOutOfBoundsException if the given byte[] length is not 4 bytes.
	 */
	public void setAddress(byte[] b) {
		if(b.length != 4)
			throw new IllegalArgumentException("IPv4 Address length must be 4 bytes");
		System.arraycopy(b, 0, address, 0, 4);
	}
	
	public String toString() {
		return (address[0]&0xff)+"."+(address[1]&0xff)+"."+(address[2]&0xff)+"."+(address[3]&0xff);
	}
	
	public boolean equals(Object o) {
		try {
			IPv4Address a = (IPv4Address)o;
			for (int i=0; i<4; i++)
				if (a.address[i] != address[i]) return false;
			return true;
		} catch (ClassCastException e) {return false;}
	}
}
