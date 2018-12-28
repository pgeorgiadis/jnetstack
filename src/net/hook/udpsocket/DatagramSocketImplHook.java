package net.hook.udpsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocketImpl;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;

import os.Queue;

import net.core.NetCore;
import net.core.packet.RawPacket;
import net.core.packet.container.PacketContainer;
import net.protocol.ip.ipv4.IPv4Address;
import net.protocol.ip.udp.UDPListener;
import net.protocol.ip.udp.UDPPacket;

public class DatagramSocketImplHook extends DatagramSocketImpl implements UDPListener {
	private NetCore core;
	private IPv4Address local_address;
	private int local_port;
	
	private int ttl;
	private Queue queue;
	
	public DatagramSocketImplHook(NetCore core) {
		this.core = core;
		this.queue = new Queue();
	}
	
	public void handle(UDPPacket packet, IPv4Address src, IPv4Address dst) {
		ReceivedDatagram r = new ReceivedDatagram(
				packet.getPayload().getContainer().toArray(), src.getAddress(), packet.getSrcPort());
		queue.put(r);
	}
	
	public boolean match(IPv4Address dst, int dst_port) {
		if ((!this.local_address.equals(dst)) || (this.local_port != dst_port))
			return false;
		return true;
	}
	
	protected void bind(int port, InetAddress address) throws SocketException {
		this.local_port = port;
		this.local_address = new IPv4Address(address.getAddress());
	}
	
	protected void close() {
		core.udp.removeListener(this);
	}
	
	protected void create() throws SocketException {
		core.udp.addListener(this);
	}
	
	protected int getTimeToLive() throws IOException {
		return ttl;
	}
	
	protected byte getTTL() throws IOException {
		return (byte)ttl;
	}
	
	protected void join(InetAddress arg0) throws IOException {
		//TODO Related to multicast implementation
	}
	
	protected void joinGroup(SocketAddress arg0, NetworkInterface arg1) throws IOException {
		//TODO Related to multicast implementation
	}
	
	protected void leave(InetAddress arg0) throws IOException {
		//TODO Related to multicast implementation
	}
	
	protected void leaveGroup(SocketAddress arg0, NetworkInterface arg1) throws IOException {
		//TODO Related to multicast implementation
	}
	
	protected int peek(InetAddress address) throws IOException {
		//TODO Auto-generated method stub
		ReceivedDatagram r = (ReceivedDatagram)queue.peek();
		return r.getPort();
	}
	
	protected int peekData(DatagramPacket datagram) throws IOException {
		//TODO Auto-generated method stub
		ReceivedDatagram r = (ReceivedDatagram)queue.peek();
		return r.getPort();
	}
	
	protected void receive(DatagramPacket datagram) throws IOException {
		ReceivedDatagram r = null;
		do {
			r = (ReceivedDatagram)queue.get();
		} while (r == null);
		datagram.setAddress(r.getAddress());
		datagram.setData(r.getData());
		datagram.setLength(r.getData().length);
		datagram.setPort(r.getPort());
		datagram.setSocketAddress(r.getSocketAddress());
	}
	
	protected void send(DatagramPacket datagram) throws IOException {
		byte[] data = datagram.getData();
		RawPacket packet = new RawPacket(PacketContainer.createContainer(data));
		IPv4Address address = new IPv4Address(datagram.getAddress().getAddress());
		int port = datagram.getPort();
		
		UDPPacket p = new UDPPacket(packet.getSize());
		p.setSrcPort(local_port);
		p.setDstPort(port);
		p.setPayload(packet);
		
		core.udp.sendUDP(p, local_address, address);
	}
	
	protected void setTimeToLive(int ttl) throws IOException {
		this.ttl = ttl;
	}
	
	protected void setTTL(byte ttl) throws IOException {
		this.ttl = (byte)(ttl & 0xff);
	}
	
	public Object getOption(int arg0) throws SocketException {
		//TODO Related to IP<->Transport protocols extension for Special options
		return null;
	}
	
	public void setOption(int arg0, Object arg1) throws SocketException {
		//TODO Related to IP<->Transport protocols extension for Special options
	}
}
