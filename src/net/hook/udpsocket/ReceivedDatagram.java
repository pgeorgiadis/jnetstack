package net.hook.udpsocket;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class ReceivedDatagram {
	private InetAddress address;
	private int port;
	private byte[] data;
	
	public ReceivedDatagram(byte[] data, byte[] address, int port) {
		this.data = data;
		try {
			this.address = InetAddress.getByAddress(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.port = port;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
	
	public SocketAddress getSocketAddress() {
		return new InetSocketAddress(address, port);
	}
}
