package net.protocol.ip.ipv4;

import net.core.NetworkException;

/**
 * The IPv4Exception is the Superclass of all exceptions that are thrown from the ip version 4
 * module.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	
 */
public class IPv4Exception extends NetworkException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new IPv4Exception.
	 */
	public IPv4Exception(){
		super();
	}
	
	public IPv4Exception(String s){
		super(s);
	}
	
	public IPv4Exception(Exception e){
		super(e);
	}
}
