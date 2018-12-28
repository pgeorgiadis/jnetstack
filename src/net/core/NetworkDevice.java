package net.core;

import os.devicemanager.Device;

/**
 * The NetworkDevice is the Superclass of all network devices. Every NetworkDevice has a 
 * associated NetModule that handles any packet received from this device, to which it 
 * forwards the incomming packets.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	13.8.2005
 */
public abstract class NetworkDevice extends Device implements NetModule {
	protected NetCore core;
	
	/** The NetModule that will receive the incoming packets from this NetworkDevice. */
	protected NetModule receiver;
	
	/**
	 * Creates a new NetworkDevice with the given device name and associated to the specified NetCore.
	 * @param	core the associated NetCore
	 * @param	name the device name of the NetworkDevice.
	 */
	public NetworkDevice(NetCore core, String name) {
		super(name);
		this.core = core;
	}
	
	/**
	 * Gets the NetModule that currently receives the incomming packets from this NetworkDevice.
	 * @return	the NetModule that currently receives the incomming packets from this NetworkDevice.
	 */
	public NetModule getModule() {
		return receiver;
	}
	
	/**
	 * Sets the NetModule that will receive the incomming packets from this NetworkDevice.
	 * @param 	module the NetModule to be assigned.
	 */
	public void setModule(NetModule module) {
		this.receiver = module;
	}
	
	public NetCore core() {
		return core;
	}
	
	public void log(int level, String message) {
		//TODO how we treat the log of NetworkDevices?
		System.out.println(message);
	}
	
	public void log(int level, Throwable t) {
		//TODO how we treat the log of NetworkDevices?
		System.out.println(t.getMessage());
	}

	public void initialize(Object config) {}

	public void shutdown() {}
}
