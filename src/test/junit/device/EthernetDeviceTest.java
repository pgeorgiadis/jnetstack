package test.junit.device;

import os.threadpool.WorkerThread;
import test.TestModule;
import test.VirtualEthernet;
import junit.framework.TestCase;
import net.protocol.ethernet.EthernetAddress;
import net.protocol.ethernet.EthernetDevice;
import net.protocol.ethernet.EthernetPacket;

public class EthernetDeviceTest extends TestCase {
	EthernetDevice e0, e1;
	
	public void testEthernet() {
		try {
			EthernetPacket p1 = new EthernetPacket(10);
			p1.setSrcAddress(e0.getMACAddress());
			p1.setDstAddress(e1.getMACAddress());
			
			EthernetPacket p2 = new EthernetPacket(10);
			p2.setSrcAddress(e0.getMACAddress());
			p2.setDstAddress(EthernetAddress.BROADCAST_MAC);
			
			EthernetPacket p3 = new EthernetPacket(10);
			p3.setSrcAddress(e0.getMACAddress());
			p3.setDstAddress(e0.getMACAddress());
			
			TestModule test_module0 = new TestModule();
			TestModule test_module1 = new TestModule();
			e0.setModule(test_module0);
			e1.setModule(test_module1);
			
			EthernetPacket p;
			
			WorkerThread worker1 = new WorkerThread();
			worker1.engage();
			worker1.pushToInject(e0, p1);
			Thread.sleep(2000);
			p = (EthernetPacket)test_module1.getReceived();
			assertEquals(p1, p);
			
			WorkerThread worker2 = new WorkerThread();
			worker2.engage();
			worker2.pushToInject(e0, p2);
			Thread.sleep(2000);
			p = (EthernetPacket)test_module0.getReceived();
			assertEquals(p2, p);
			p = (EthernetPacket)test_module1.getReceived();
			assertEquals(p2, p);
			
			WorkerThread worker3 = new WorkerThread();
			worker3.engage();
			worker3.pushToInject(e0, p3);
			Thread.sleep(10000);
			p = (EthernetPacket)test_module0.getReceived();
			assertEquals(p3, p);
		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Override
	protected void tearDown() throws Exception {
		VirtualEthernet.reset();
		
		super.tearDown();
	}
}
