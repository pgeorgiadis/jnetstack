package net.core;

import net.core.packet.PacketCapsule;

/**
 * The class NetModule is the Superclass of all devices and protocol modules.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	4.4.2005
 */
public interface NetModule {
	/**
	 * Initializes this NetModule
	 * @param	config a data structure that contains the init parameters of this NetModules. 
	 * This data structure depends on the implementation of the NetModule
	 */
	public void initialize(Object config);
	
	/**
	 * Delivers a packet to this NetModule.
	 * @param	a capsule containing the received packet
	 */
	public abstract NetModule handle(PacketCapsule capsule);
	
	/**
	 * Injects a packet into this NetModule. This NetModule must proccess this packet
	 * and forward it as appropriate.
	 * @param	capsule a capsule containing the received packet
	 */
	public abstract NetModule inject(PacketCapsule capsule);
	
	/**
	 * Returns the NetCore
	 * @return	the parent NetCore of this NetModule.
	 */
	public abstract NetCore core();
	
	/**
	 * Appends a message to the logger of the NetCore.
	 * @param	level the log level of this message.
	 * @param	message the message to loged.
	 */
	public void log(int level, String message);
	
	/**
	 * Appends a throwable object to the log of the NetCore
	 * @param	level the log level of this message.
	 * @param	t the Throwable to be loged.
	 */
	public void log(int level, Throwable t);
	
	/**
	 * Orders this NetModule to shutdown itself
	 */
	public void shutdown();
}
