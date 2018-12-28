package net.core.packet;

import net.core.NetworkException;

/**
 * The NetworkPacketException is the Superclass of all Exceptions that a packet may throw.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	14.8.2005
 */
public class NetworkPacketException extends NetworkException {
	private static final long serialVersionUID = 1L;
	
	public NetworkPacketException(){
		super();
	}
	
	public NetworkPacketException(String s){
		super(s);
	}
	
	public NetworkPacketException(Exception e){
		super(e);
	}
}
