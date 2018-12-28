package net.protocol.ip.ipv4;

import os.Queue;
import net.core.NetCore;
import net.core.NetModule;
import net.core.NetworkDevice;
import net.core.packet.Packet;
import net.core.packet.PacketCapsule;

/**
 * The LocalLoopback is the driver for loopback devices.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	17.8.05
 */
public class LocalLoopback extends NetworkDevice implements Runnable {
	Queue queue;
	
	/**
	 * Creates a LocalLoopback device.
	 * @param	name the device name of the loopback device
	 */
	public LocalLoopback(NetCore core, String name) {
		super(core, name);
		this.receiver = this;
		this.queue = new Queue();
	}
	
	public NetModule inject(PacketCapsule capsule) {
		synchronized(queue) {
			queue.put(capsule.getPacket());
			queue.notify();
		}
		return null;
	}

	public NetModule handle(PacketCapsule capsule) {
		return null;
	}
	
	private Packet getReceived() {
		return (Packet)queue.get();
	}
	
	/**
	 * Starts this device. Usualy the devices have a thread that operates them. This method creates
	 * this thread and starts the operation of this device.
	 */
	public void start() {
		Thread t = new Thread(this);
		t.setName(this.name+" thread");
		t.start();
	}
	
	public void run() {
		while(true) {
			Packet p = getReceived();
			try {
				if (p == null) {
					synchronized(queue) {
						queue.wait();
					}
					continue;
				} else if (receiver != null)
					core().getThreadPoolService().getThread().pushToHandle(receiver, p);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
}
