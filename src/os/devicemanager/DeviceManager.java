package os.devicemanager;

import java.util.Hashtable;

import os.OSException;

import test.VirtualEthernet;


import net.core.NetCore;
import net.protocol.ethernet.EthernetAddress;
import net.protocol.ip.ipv4.LocalLoopback;

/**
 * The class DeviceManager represents the part of the OS that handles the Devices. It 
 * initializes the devices and the device drivers.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	13.6.2005
 */
public class DeviceManager {
	/** The devices that are curently available from the DeviceManager. */
	public static Hashtable devices;
	
	/**
	 * Initializes the devices of the system. This method is usualy called from the VM class loader.
	 */
	public static void init(NetCore core) {
		devices = new Hashtable();
		
		try{
			VirtualEthernet.reset();
			
			EthernetAddress mac1 = new EthernetAddress(new byte[] {11, 11, 11, 22, 22, 22});
			VirtualEthernet ve0 = new VirtualEthernet(core, "ve0", mac1);
			devices.put("ve0", ve0);
			ve0.start();
			
			EthernetAddress mac2 = new EthernetAddress(new byte[] {33, 33, 33, 44, 44, 44});
			VirtualEthernet ve1 = new VirtualEthernet(core, "ve1", mac2);
			devices.put("ve1", ve1);
			ve1.start();
			
			LocalLoopback lo0 = new LocalLoopback(core, "lo0");
			devices.put("lo0", lo0);
			lo0.start();
		}catch(Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the device with the specified device name if it exists in current configuration.
	 * @param	name the device name of the device we want.
	 * @return	the Device that has the specified device name.
	 * @throws	OSException if the device was not found.
	 */
	public static Device getDevice(String name) throws OSException {
		Object o = devices.get(name);
		if (o == null) throw new OSException("Unable to find device");
		return (Device)o;
	}
}
