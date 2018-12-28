package test.junit.device;

import os.OSException;
import os.devicemanager.DeviceManager;
import net.core.NetCore;
import net.protocol.ethernet.EthernetDevice;

public class VirtualEthernetTest extends EthernetDeviceTest {
	public VirtualEthernetTest() {
		NetCore core = new NetCore();
		
		try {
			e0 = (EthernetDevice)DeviceManager.getDevice("ve0");
			e1 = (EthernetDevice)DeviceManager.getDevice("ve1");
		} catch (OSException e) {
			e.printStackTrace();
		}
	}
}
