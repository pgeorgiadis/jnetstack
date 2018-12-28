package net.core.services;

import net.core.NetworkException;

public class EncapsulationServiceException extends NetworkException {
	private static final long serialVersionUID = 1L;
	
	public EncapsulationServiceException(){
		super();
	}
	
	public EncapsulationServiceException(String s){
		super(s);
	}
	
	public EncapsulationServiceException(Exception e){
		super(e);
	}
}
