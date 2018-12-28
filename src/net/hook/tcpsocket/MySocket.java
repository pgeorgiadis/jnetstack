package net.hook.tcpsocket;

import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;

public class MySocket extends Socket {
	public MySocket(SocketImpl impl) throws SocketException {
		super(impl);
	}
}
