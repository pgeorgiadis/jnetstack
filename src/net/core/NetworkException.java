package net.core;

/**
 * The NetworkException is the Superclass of all exceptions thrown from the networking subsystem
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	4.4.2005
 */
public class NetworkException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public NetworkException(){
		super();
	}
	
	public NetworkException(String s){
		super(s);
	}
	
	public NetworkException(Exception e){
		super(e);
	}
}
