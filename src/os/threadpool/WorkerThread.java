package os.threadpool;

import net.core.NetModule;
import net.core.packet.Packet;
import net.core.packet.PacketCapsule;

public class WorkerThread extends Thread {
	private static final int HANDLE = 0;
	private static final int INJECT = 1;
	
	private static final int FREE = 0;
	private static final int PREPARE = 1;
	private static final int PUSH = 2;
	
	private PacketCapsule capsule;
	private NetModule module;
	private int push_method;
	private int state;
	
	/**
	 * Creates a new WorkerThread.
	 */
	public WorkerThread() {
		this.capsule = new PacketCapsule(null);
		this.setState(FREE);
		this.start();
	}
	
	/**
	 * Push a packet through a chain of modules, starting with the specified module.
	 * The push method that will be used is the method specified through the push_method.
	 * @param	m the module that will start the process of this packet.
	 * @param	packet the packet that will be processed
	 */
	private void push(NetModule m, Packet packet) {
		this.capsule.setPacket(packet);
		this.module = m;
		this.setState(PUSH);
	}
	
	/**
	 * Sets the push method of this WorkerThread to WorkerThread.HANDLE and then calls the push method
	 * for the specified module and packet.
	 * @param	m the module that will start the process of this packet.
	 * @param	packet the packet that will be processed
	 */
	public void pushToHandle(NetModule m, Packet packet) {
		push_method = HANDLE;
		this.push(m, packet);
	}
	
	/**
	 * Sets the push method of this WorkerThread to WorkerThread.INJECT and then calls the push method
	 * for the specified module and packet.
	 * @param	m the module that will start the process of this packet.
	 * @param	packet the packet that will be processed
	 */
	public void pushToInject(NetModule m, Packet packet) {
		push_method = INJECT;
		this.push(m, packet);
	}
	
	/**
	 * Checks if the current state of this WorkerThread is WorkerThread.FREE.
	 * @return	true if the current state of this WorkerThread is WorkerThread.FREE. Or else false.
	 */
	public boolean isFree() {
		return (state==FREE);
	}
	
	/**
	 * Clears this WorkerThread and sets it's state to FREE.
	 */
	public void setFree() {
		this.capsule.setPacket(null);
		this.module = null;
		this.setState(FREE);
	}
	
	/**
	 * Sets the state of this WorkerThread to WorkerThread.PREPARE and returns this WorkerThread.
	 * @return	this WorkerThread in prepare state.
	 */
	public WorkerThread engage() {
		this.setState(PREPARE);
		return this;
	}
	
	/**
	 * Changes the state of this WorkerThread. The valid values are the values of WorkerTherad.FREE, 
	 * WorkerThread.PREPARE and WorkerThread.PUSH.
	 * @param	new_state the new state of the WorkerThread.
	 */
	private void setState(int new_state) {
		this.state = new_state;
	}
	
	public void run() {
		while(true) {
			if (state != PUSH) {
				try {Thread.sleep(50);
				} catch (InterruptedException e) {e.printStackTrace();}
				continue;
			}
			
			if (this.push_method == HANDLE) {
				do {
					module = module.handle(capsule);
				} while (module != null);
			}
			else if (this.push_method == INJECT) {
				do {
					module = module.inject(capsule);
				} while (module != null);
			}
			
			this.setFree();
		}
	}
	
	public String toString() {
		return this.getName()+"["+state+"]";
	}
}
