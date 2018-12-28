package net.hook.udpsocket;

import java.net.DatagramSocket;
import java.net.DatagramSocketImpl;

public class MyDatagramSocket extends DatagramSocket {
	public MyDatagramSocket(DatagramSocketImpl impl) {
		super(impl);
	}
}
