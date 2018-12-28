package test;

import os.threadpool.ThreadPool;
import os.threadpool.WorkerThread;
import net.core.NetCore;
import net.core.NetModule;
import net.core.packet.Packet;
import net.core.packet.PacketCapsule;
import net.core.packet.container.PacketContainer;
import net.protocol.ethernet.EthernetAddress;
import net.protocol.ethernet.EthernetDevice;
import net.protocol.ethernet.EthernetPacket;

public class EthernetProbe extends EthernetDevice implements Runnable {
	boolean running = true;
	
	public EthernetProbe(NetCore core, String name, EthernetAddress mac, String probe_to) throws Exception {
		super(core, name, mac);
		
		byte[] dev = probe_to.getBytes();
		int result = this.init(dev, 2000, true);
		if (result != 0) {
			if(result == -1)
				throw new Exception(this.getError(0));
			else if(result == -2)
				throw new Exception(this.getError(1));
		}
		
		Thread t = new Thread(this);
		t.start();
	}
	
	public native int init(byte[] dev, int caplen, boolean prom);
	public native String getError(int source);
	public native int setFilter(byte[] filter);
	public native byte[] receiveNative();
	public native int sendNative(byte[] b);
	public native void close();
	public native void printPacket();
	
	public Packet receive() {return null;}
	public NetModule handle(PacketCapsule p) {return null;}
	
	public NetModule inject(PacketCapsule capsule) {
		EthernetPacket packet = (EthernetPacket)capsule.getPacket();
		if (sendNative(packet.getContainer().toArray()) == -1) {
			System.out.println("EthernetProbe error: "+this.getError(1)+"");
		}
		return null;
	}
	
	static{
		System.load("/usr/lib/jnetprobe.so");
	}
	
	public void run() {
		while(running) {
			byte[] b = this.receiveNative();
			EthernetPacket p = new EthernetPacket(PacketContainer.createContainer(b));
			if ((p.getDstAddress().equals(mac) || p.getDstAddress().equals(EthernetAddress.BROADCAST_MAC)) && receiver != null) {
				NetCore c = core();
				ThreadPool t = c.getThreadPoolService();
				WorkerThread w = t.getThread();
				w.pushToHandle(receiver, p);
			}
		}
	}
}
