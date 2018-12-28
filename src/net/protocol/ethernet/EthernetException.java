package net.protocol.ethernet;

import net.core.NetworkException;

/**
 * The EthernetException is the Superclass of all exceptions that are thrown from the ethernet
 * related entities.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	
 */
public class EthernetException extends NetworkException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new EthernetException.
	 */
	public EthernetException(){
		super();
	}
	
	public EthernetException(String s){
		super(s);
	}
	
	public EthernetException(Exception e){
		super(e);
	}
}
