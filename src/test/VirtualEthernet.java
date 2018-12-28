package test;

import os.Queue;

import net.core.NetCore;
import net.core.NetModule;
import net.core.packet.Packet;
import net.core.packet.PacketCapsule;
import net.protocol.ethernet.EthernetAddress;
import net.protocol.ethernet.EthernetDevice;
import net.protocol.ethernet.EthernetPacket;

/**
 * The VirtualEthernet class is a virtual EthernetDevice. It for testing purposes of this 
 * implementation
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	17.8.05
 */
public class VirtualEthernet extends EthernetDevice implements Runnable {
	protected static VirtualEthernet[] ether = new VirtualEthernet[2];
	
	private static final int INITIALIZING = 0;
	private static final int RUNNING = 1;
	private static final int EXITING = 2;
	
	private Queue queue;
	
	private Thread thread;
	
	private int state;
	
	/**
	 * Creates a new VirtualEthernet device. It adds the newly created device to the static
	 * ether registry too.
	 * @param	name the device name
	 * @param	mac the EthernetAddress of the new device
	 */
	public VirtualEthernet(NetCore core, String name, EthernetAddress mac) {
		super(core, name, mac);
		this.queue = new Queue();
		state = INITIALIZING;
		
		thread = new Thread(this);
		thread.setName(this.name+" thread");
		
		VirtualEthernet.add(this);
	}
	
	public NetModule inject(PacketCapsule capsule) {
		for (int i=0; i<ether.length; i++)
			if (ether[i] != null)
				core().getThreadPoolService().getThread().pushToHandle(ether[i], capsule.getPacket());
		return null;
	}
	
	public NetModule handle(PacketCapsule capsule) {
		queue.put(capsule.getPacket());
		return null;
	}
	
	private Packet getReceived() {
		while(queue.getSize() != 0) {
			EthernetPacket e = (EthernetPacket)queue.get();
			EthernetAddress a = e.getDstAddress();
			if ((a.equals(this.getMACAddress())) || (e.getDstAddress().equals(EthernetAddress.BROADCAST_MAC)))
				return e;
		}
		return null;
	}
	
	/**
	 * Starts this device. Usualy the devices have a thread that operates them. This method creates
	 * this thread and starts the operation of this device.
	 */
	public void start() {
		if (state != RUNNING) {
			state = RUNNING;
			thread.start();
		}
	}
	
	public void stop() {
		state = EXITING;
		
		try {
			thread.join();
		} catch (InterruptedException e) {}
	}
	
	public void run() {
		while(state == RUNNING) {
			EthernetPacket p = (EthernetPacket)this.getReceived();
			try {
				if (p == null) {
					Thread.sleep(10);
					continue;
				}
				
				if (this.getModule() != null)
					core().getThreadPoolService().getThread().pushToHandle(this.getModule(), p);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public static void reset() {
		for (int i=0; i<ether.length; i++) {
			if (ether[i] != null) ether[i].stop();
		}
		
		ether = new VirtualEthernet[2];
	}
	
	/**
	 * Adds a VirtualEthernetDevice to the static ether registry.
	 * @param	ve the VirtualEthernet device to be added.
	 */
	private static void add(VirtualEthernet ve) {
		for (int i=0; i<ether.length; i++)
			if (ether[i] == null) {
				ether[i] = ve;
				return;
			}
		VirtualEthernet[] tmp = new VirtualEthernet[ether.length+5];
		System.arraycopy(ether, 0, tmp, 0, ether.length);
		tmp[ether.length] = ve;
		ether = tmp;
	}
}