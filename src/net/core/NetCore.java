package net.core;

import net.core.services.EncapsulationService;
import net.core.services.LoggingService;
import net.core.services.NetService;
import net.core.services.SchedulerService;
import net.core.services.ThreadPoolService;
import net.protocol.arp.ARPProtocol;
import net.protocol.ethernet.EthernetProtocol;
import net.protocol.ip.icmp.ICMPProtocol;
import net.protocol.ip.ipv4.IPv4Protocol;
import net.protocol.ip.tcp.TCPProtocol;
import net.protocol.ip.udp.UDPProtocol;
import os.OSException;
import os.devicemanager.DeviceManager;
import os.threadpool.ThreadPool;

/**
 * NetCore is the core of the networking subsystem. It provides to the modules access to some common 
 * OS services, like logging etc. It also provides access to other "internal" services of the 
 * subsystem, like the EncapsulationService.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	4.4.2005
 */
public class NetCore {
	// Modules
	public EthernetProtocol ethernet;
	public ARPProtocol arp;
	public IPv4Protocol ip;
	public TCPProtocol tcp;
	public UDPProtocol udp;
	public ICMPProtocol icmp;
	
	// Services
	private LoggingService logging_service;
	private SchedulerService scheduler_service;
	private EncapsulationService encapsulation_service;
	private ThreadPoolService threadpool_service;
	
	/**
	 * Creates and initializes the networking subsystem
	 */
	public NetCore() {
		try {
			DeviceManager.init(this);
			
			logging_service = new LoggingService(LoggingService.LOG_LEVEL_INFO);
			scheduler_service = new SchedulerService();
			threadpool_service = new ThreadPoolService(10);
			encapsulation_service = new EncapsulationService(this);
			
			arp = new ARPProtocol(this);
			ethernet = new EthernetProtocol(this);
			ip = new IPv4Protocol(this);
			tcp = new TCPProtocol(ip, this);
			udp = new UDPProtocol(ip);
			icmp = new ICMPProtocol(ip);
		} catch (Exception e) {
			System.out.println("Unable to initialize NetCore");
			System.out.println(e.getClass()+" "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the module with the specified module name.
	 * @param	name the name of the module
	 * @return	the module with the specified name
	 * @throws	NetoworkException if there is no module with the specified name
	 */
	public NetModule getModule(String name) throws NetworkException {
		if (name.equals("ethernet"))
			return ethernet;
		else if (name.equals("arp"))
			return arp;
		else if (name.equals("ip"))
			return ip;
		throw new NetworkException("Module not found");
	}
	
	/**
	 * Gets a NetworkDevice from the operating system.
	 * @param	name the device name.
	 * @return	the NetworkDevice.
	 * @throws	NetworkException if the Device was not a NetworkDevice or no Device found with this name.
	 */
	public NetworkDevice getNetworkDevice(String name) throws NetworkException {
		try {
			Object o = DeviceManager.getDevice(name);
			NetworkDevice dev = (NetworkDevice)o;
			return dev;
		}
		catch (OSException e) {
			throw new NetworkException(e);
		}
		catch (ClassCastException e) {
			throw new NetworkException("Device is not a network device");
		}
	}
	
	/**
	 * Gets the module with the specified module name.
	 * @param	name the name of the module
	 * @return	the module with the specified name
	 * @throws	NetoworkException if there is no module with the specified name
	 */
	public NetService getService(String name) throws NetworkException {
		if (name.equals("encapsulation_service"))
			return encapsulation_service;
		else if (name.equals("logging_service"))
			return logging_service;
		else if (name.equals("scheduler_service"))
			return scheduler_service;
		else if (name.equals("threadpool_service"))
			return threadpool_service;
		throw new NetworkException("Module not found");
	}
	
	/**
	 * Gets the encapsulation service of the network subsystem
	 * @return	the encapsulation service
	 */
	public EncapsulationService getEncapsulationService() {
		return encapsulation_service;
	}
	
	/**
	 * Gets the logging service of the network subsystem
	 * @return	the logging service
	 */
	public LoggingService getLoggingService() {
		return logging_service;
	}
	
	/**
	 * Gets the scheduler service of the network subsystem
	 * @return	the scheduler service
	 */
	public SchedulerService getSchedulerService() {
		return scheduler_service;
	}
	
	/**
	 * Gets the threadpool service of the network subsystem
	 * @return	the threadpool service
	 */
	public ThreadPool getThreadPoolService() {
		return threadpool_service;
	}
}
