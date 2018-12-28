package test.junit.device;

import java.util.Random;

import test.TestModule;

import net.core.NetCore;
import net.core.packet.Packet;
import net.core.packet.RawPacket;
import net.core.packet.container.PacketContainer;
import net.protocol.ip.ipv4.LocalLoopback;
import junit.framework.TestCase;

public class LoopbackTest extends TestCase {
	public void testLoopback() {
		try {
			NetCore core = new NetCore();
			
			LocalLoopback lo0 = new LocalLoopback(core, "lo0");
			
			TestModule test_module = new TestModule();
			lo0.setModule(test_module);
			lo0.start();
			
			Random r = new Random();
			byte[] b = new byte[15];
			r.nextBytes(b);
			RawPacket raw = new RawPacket(PacketContainer.createContainer(b));
			
			core.getThreadPoolService().getThread().pushToInject(lo0, raw);
			Thread.sleep(1000);
			Packet packet = test_module.getReceived();
			
			assertEquals(raw, packet);
		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
