package net.hook.tcpsocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;

import net.core.NetCore;
import net.protocol.ip.ipv4.IPv4Address;
import net.protocol.ip.ipv4.IPv4Exception;
import net.protocol.ip.tcp.socket.TCPSocket;

public class SocketImplHook extends SocketImpl {
	NetCore core;
	TCPSocket socket;
	
	public SocketImplHook(NetCore core) {
		this.core = core;
	}
	
	protected void create(boolean arg0) throws IOException {
		// TODO Auto-generated method stub
		this.socket = new TCPSocket(core.tcp, null, null, 0, 0);
	}
	
	protected void connect(String remote_host, int remote_port) throws IOException {
		this.connect(InetAddress.getByName(remote_host), remote_port);
	}
	
	protected void connect(InetAddress remote_address, int remote_port) throws IOException {
		try {
			IPv4Address dst = new IPv4Address(remote_address.getAddress());
			IPv4Address src = core.ip.getInterfaceForDestination(dst).getAddress();
			
			System.out.println(src+" "+dst);
			
			this.socket.setLocal(src);
			this.socket.setRemote(dst);
			this.socket.setLocal_port(core.tcp.getFreePort());
			this.socket.setRemote_port(remote_port);
			
			this.socket.connect();
		} catch (IPv4Exception e) {
			throw new IOException(e.getMessage());
		}
	}
	
	protected void connect(SocketAddress address, int timeout) throws IOException {
		InetSocketAddress sockAddr = (InetSocketAddress) address;
		
		InetAddress addr = sockAddr.getAddress();
	    if (addr == null)
	    	throw new IllegalArgumentException("unresolved address " + sockAddr);

	    int port = sockAddr.getPort();
	    
	    connect (addr, port);
	    
	    //TODO do something with the timeout too
	}
	
	protected void bind(InetAddress arg0, int arg1) throws IOException {
		// TODO Auto-generated method stub
		throw new SocketException("bind is not implemented");
	}
	
	protected void listen(int arg0) throws IOException {
		// TODO Auto-generated method stub
		throw new SocketException("listen is not implemented");
	}
	
	protected void accept(SocketImpl arg0) throws IOException {
		// TODO Auto-generated method stub
		throw new SocketException("accept is not implemented");
	}
	
	protected InputStream getInputStream() throws IOException {
		return socket.getReceiver().getBuffer();
	}
	
	protected OutputStream getOutputStream() throws IOException {
		return socket.getSender().getBuffer();
	}
	
	protected int available() throws IOException {
		return socket.getReceiver().getBuffer().available();
	}
	
	protected void close() throws IOException {
		socket.disconnect();
	}
	
	protected void sendUrgentData(int arg0) throws IOException {
		// TODO Auto-generated method stub
		throw new SocketException("sendUrgentData is not implemented");
	}
	
	public void setOption(int arg0, Object arg1) throws SocketException {
		// TODO Auto-generated method stub
		throw new SocketException("setOption is not implemented");
	}
	
	public Object getOption(int arg0) throws SocketException {
		// TODO Auto-generated method stub
		throw new SocketException("getOption is not implemented");
	}
}
