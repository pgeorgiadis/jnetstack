package net.protocol.arp;

import net.core.NetworkException;

/**
 * ARP protocol related exception
 * @author JPG
 *
 */
public class ARPException extends NetworkException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new ARPException.
	 */
	public ARPException(){
		super();
	}
	
	public ARPException(String s){
		super(s);
	}
	
	public ARPException(Exception e){
		super(e);
	}
}
