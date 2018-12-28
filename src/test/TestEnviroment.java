package test;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import net.core.NetCore;
import net.hook.tcpsocket.MySocket;
import net.hook.tcpsocket.SocketImplHook;
import net.hook.udpsocket.DatagramSocketImplHook;
import net.hook.udpsocket.MyDatagramSocket;
import net.protocol.ethernet.EthernetAddress;
import net.protocol.ip.ipv4.IPv4Address;
import net.protocol.ip.ipv4.IPv4Exception;
import net.protocol.ip.ipv4.IPv4Interface;
import net.protocol.ip.ipv4.IPv4Route;

public class TestEnviroment {
	NetCore core;

	EthernetAddress mac0;

	EthernetProbe ve0;

	IPv4Address ip;

	IPv4Interface iface;

	InputStreamReader istream;

	BufferedReader bufRead;
	
	static boolean running = true;

	public static void main(String[] args) {
		try {
			TestEnviroment t = new TestEnviroment();
			t.core = new NetCore();

			t.mac0 = new EthernetAddress("00:0d:93:72:1b:1a");
			t.ve0 = new EthernetProbe(t.core, "ve0", t.mac0, "en0");
			t.ve0.setModule(t.core.ethernet);

			t.ip = new IPv4Address("10.131.0.6");
			t.iface = new IPv4Interface(t.ip, t.core.ethernet, t.ve0);
			t.core.ip.addInterface(t.iface);

			System.out.println("Test enviroment started");
			System.out.println();
			t.info();

			t.istream = new InputStreamReader(System.in);
			t.bufRead = new BufferedReader(t.istream);
			while (true) {
				try {
					String cmd = t.bufRead.readLine();
					if (cmd.equals("exit")) {
						System.exit(0);
					} else if (cmd.startsWith("info")) {
						t.info();
					} else if (cmd.startsWith("route ")) {
						t.route(cmd.substring(6));
					} else if (cmd.startsWith("ping")) {
						t.ping(cmd.substring(5));
					} else if (cmd.startsWith("udp")) {
						t.udpEcho(cmd.substring(4));
					} else if (cmd.startsWith("tcp connect")) {
						t.tcpConnect();
					} else if (cmd.startsWith("tcp transfer")) {
						t.tcpTransfer();
					} else if (cmd.startsWith("tcp echo"))	{
						t.tcpEcho();
					}
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void info() {
		System.out.println("Mac address: " + mac0);
		System.out.println("IP address: " + ip);
		System.out.println();
	}

	public void route(String args) throws IOException {
		if (args.startsWith("print")) {
			if (args.length() == 5) {
				System.out.println(core.ip.getRoutingTable().toString());
			} else {
				// print route
				IPv4Address destination = new IPv4Address(args.substring(6));
				try {
					String r = core.ip.getRoutingTable().getRoute(destination)
							.toString();
					String i = core.ip.getInterfaceForDestination(destination)
							.toString();
					System.out.println("gateway for destination " + destination
							+ " is " + r + " and will be sent via interface "
							+ i);
					System.out.println();
				} catch (IPv4Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} else if (args.startsWith("add")) {
			// add route
			System.out.print("destination: ");
			IPv4Address destination = new IPv4Address(bufRead.readLine());

			System.out.print("mask[255.255.255.0]: ");
			String m = bufRead.readLine();
			if (m.equals(""))
				m = "255.255.255.0";
			IPv4Address mask = new IPv4Address(m);

			System.out.print("gateway: ");
			IPv4Address gateway = new IPv4Address(bufRead.readLine());
			IPv4Route route = new IPv4Route(destination, mask, gateway);
			core.ip.getRoutingTable().addRoute(route);
			System.out.println();
		}
	}

	public void ping(String host) throws IOException {
		for (int i = 0; i < 4; i++) {
			Ping ping = new Ping(this.ip, new IPv4Address(host));
			core.icmp.addListener(ping);

			ping.send(core);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println();
	}
	
	public void tcpConnect() throws Exception {
		SocketImplHook impl1 = new SocketImplHook(core);
		MySocket socket = new MySocket(impl1);
		
		System.out.print("host: ");
		String host = bufRead.readLine();
		System.out.print("port: ");
		int port = Integer.parseInt(bufRead.readLine());
		
		IPv4Address remoteAddress = new IPv4Address(host);
		InetSocketAddress addr = 
			new InetSocketAddress(InetAddress.getByAddress(remoteAddress.getAddress()), port);
		
		socket.connect(addr);
		
		Thread.sleep(2000);
		
		final InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();
		
		running = true;
		
		Thread tin = new Thread() {
			public void run() {
				try {
					int a;
					while(running) {
						a = in.available();
						if (a == 0) continue;
						byte[] b = new byte[a];
						in.read(b);
						System.out.print(new String(b));
						Thread.sleep(100);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		tin.start();
		
		while(running) {
			String user_input = bufRead.readLine();
			if (user_input.equals("socket_close"))
				running = false;
			else
				out.write((user_input+"\r\n").getBytes());
		}
	}
	
	public void tcpTransfer() throws Exception {
		SocketImplHook impl1 = new SocketImplHook(core);
		MySocket socket = new MySocket(impl1);
		
		IPv4Address remoteAddress = new IPv4Address("10.131.0.3");
		InetSocketAddress addr = new InetSocketAddress(InetAddress.getByAddress(remoteAddress.getAddress()), 1050);
		
		socket.connect(addr);
		
		Thread.sleep(5000);
		
		InputStream in = socket.getInputStream();
		
		long target = 58687488L;
		
		FileOutputStream fos = new FileOutputStream("first.bin");
		long received = 0;
		int a;
		while(true) {
			a = in.available();
			if (a == 0) continue;
			received += a;
			byte[] b = new byte[a];
			in.read(b);
			fos.write(b);
			if (received == target)
				break;
		}
		
		fos.close();
		in.close();
		socket.close();
	}
	
	public void tcpEcho() throws Exception {
		SocketImplHook impl1 = new SocketImplHook(core);
		MySocket socket = new MySocket(impl1);
		
		IPv4Address remoteAddress = new IPv4Address("10.131.0.3");
		InetSocketAddress addr = new InetSocketAddress(InetAddress.getByAddress(remoteAddress.getAddress()), 1050);
		
		socket.connect(addr);
		
		Thread.sleep(5000);
		
		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();
		
		while(true) {
			int a = in.available();
			if (a < 1) {
				Thread.sleep(10);
				continue;
			}
			byte[] b = new byte[a];
			in.read(b);
			out.write(b);
		}
	}

	public void udpEcho(String args) {
		try {
			DatagramSocketImplHook impl = new DatagramSocketImplHook(core);
			
			InetSocketAddress src = new InetSocketAddress(InetAddress
					.getByAddress(new IPv4Address("10.131.0.6").getAddress()), 1025);
			MyDatagramSocket socket = new MyDatagramSocket(impl);
			socket.bind(src);
			
			if (args.startsWith("echo")) {
				System.out.println("Starting UDP echo server at port 1025 (will echo up to 10 packets)");
				
				for (int i=0; i<10; i++) {
					byte[] b = new byte[1024];
					DatagramPacket r = new DatagramPacket(b, b.length);
					socket.receive(r);
					DatagramPacket s = new DatagramPacket(r.getData(), r.getLength(), r.getAddress(), 1025);
					socket.send(s);
					System.out.println("packet echoed");
				}
			} else {
				System.out.println("Sending 10 udp test packets to "+args);
				
				InetSocketAddress dst = new InetSocketAddress(InetAddress
						.getByAddress(new IPv4Address(args).getAddress()), 1025);
				
				for (int i=0; i<10; i++) {
					DatagramPacket s = new DatagramPacket("test".getBytes(), 4, dst);
					socket.send(s);
				}
				
				System.out.println("...done");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
}