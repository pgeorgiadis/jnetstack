package net.protocol.ethernet;

/**
 * The class EthernetAddress represents the ethernet (MAC) address.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	10.4.2005
 */
public class EthernetAddress {
	/** This is the Broadcast MAC address, know as FF:FF:FF:FF:FF:FF. */
	public static final EthernetAddress BROADCAST_MAC = new EthernetAddress(new byte[] {~0, ~0, ~0, ~0, ~0, ~0});
	
	/**
	 * The byte[] whichs holds the ethernet address. The length of this array should always be
	 * 6 bytes.
	 */
	private byte[] address;
	
	/**
	 * Creates a new EthernetAddress with address 00:00:00:00:00:00.
	 */
	public EthernetAddress(){
		this.address = new byte[6];
	}
	
	/**
	 * Creates a new EthernetAddress from byte[].
	 * @param	b the address we want to create in byte[] form.
	 * @throws	IllegalArgumentException when the b array length is not 6 bytes.
	 */
	public EthernetAddress(byte[] b) {
		if (b.length != 6)
			throw new IllegalArgumentException("MAC Addresses must have length 6 byte");
		this.address = new byte[6];
		this.setAddress(b);
	}
	
	/**
	 * Creates a new EthernetAddress from String.
	 * @param	s the address we want to create as a String.
	 * @throws	IllegalArgumentException if the String is not in the form XX:XX:XX:XX:XX:XX.
	 */
	public EthernetAddress(String s) {
		this.address = new byte[6];
		s = s.concat(":");
		for (int i=0; i<6; i++) {
			int dot = s.indexOf(':');
			this.address[i] = EthernetAddress.hexToByte(s.substring(0, dot));
			s = s.substring(dot+1);
		}
		if (!s.equals(""))
			throw new IllegalArgumentException("MAC address must be in form of 00:00:00:00:00:00");
	}
	
	/**
	 * Gets this address as a byte[].
	 * @return	a byte[] containing this address.
	 */
	public byte[] getAddress() {
		byte[] b = new byte[6];
		System.arraycopy(address, 0, b, 0, 6);
		return b;
	}
	
	/**
	 * Sets this address.
	 * @param	b the address we want to set in byte[] form.
	 * @throws	ArrayIndexOutOfBoundsException if the given byte[] length is not 6 bytes.
	 */
	public void setAddress(byte[] b) throws ArrayIndexOutOfBoundsException {
		if(b.length != 6)
			throw new ArrayIndexOutOfBoundsException();
		System.arraycopy(b, 0, address, 0, 6);
	}
	
	public boolean equals(Object o) {
		try {
			EthernetAddress a = (EthernetAddress)o;
			for (int i=0; i<6; i++)
				if (a.address[i] != address[i]) return false;
			return true;
		} catch (ClassCastException e) {return false;}
	}
	
	public String toString() {
		return Integer.toHexString(0xff & address[0])+":"+Integer.toHexString(0xff & address[1])+":"+
		Integer.toHexString(0xff & address[2])+":"+Integer.toHexString(0xff & address[3])+":"+
		Integer.toHexString(0xff & address[4])+":"+Integer.toHexString(0xff & address[5]);
	}
	
	
	public static byte hexToByte(String s) {
		s = s.toLowerCase();
		byte[] h = new byte[2];
		try {
			h[0] = Byte.parseByte(s.substring(0, 1));
		} catch (NumberFormatException e) {
			switch(s.charAt(0)) {
			case 'a': h[0] = 10; break;
			case 'b': h[0] = 11; break;
			case 'c': h[0] = 12; break;
			case 'd': h[0] = 13; break;
			case 'e': h[0] = 14; break;
			case 'f': h[0] = 15; break;
			default: throw new IllegalArgumentException("Values are not hex");
			}
		}
		try{
			h[1] = Byte.parseByte(s.substring(1));
		} catch (NumberFormatException e) {
			char c = s.charAt(1);
			switch(c) {
			case 'a': h[1] = 10; break;
			case 'b': h[1] = 11; break;
			case 'c': h[1] = 12; break;
			case 'd': h[1] = 13; break;
			case 'e': h[1] = 14; break;
			case 'f': h[1] = 15; break;
			default: throw new IllegalArgumentException("Values are not hex");
			}
		}
		return (byte)((h[0]<<4) | (h[1] & 0xff));
	}
}
