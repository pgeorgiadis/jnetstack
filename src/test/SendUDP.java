package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import net.protocol.ip.ipv4.IPv4Address;

public class SendUDP {
	public static void main(String[] args) {
		try {
			DatagramSocket socket = new DatagramSocket(1025, InetAddress
					.getByAddress(new IPv4Address("10.131.0.3").getAddress()));
			
			InetSocketAddress dst = new InetSocketAddress(InetAddress
					.getByAddress(new IPv4Address("10.131.0.6").getAddress()), 1025);
			DatagramPacket packet = new DatagramPacket("dokimi".getBytes(), 6, dst);
			socket.send(packet);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
