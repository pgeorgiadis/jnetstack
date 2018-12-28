package net.protocol.ip.tcp.socket;

import java.io.IOException;
import java.io.InputStream;

public class TCPReceiveBuffer extends InputStream {
	public final static int DISCARD = -1;
	
	private RingBuffer buffer;
	private long acked;
	private long[][] acktable;
	
	public void initAcked(long a) {
		this.acked = a;
	}
	
	public TCPReceiveBuffer(long acked) {
		buffer = new RingBuffer(1024);
		acktable = new long[50][2];
		this.acked = acked;
	}
	
	public TCPReceiveBuffer(long acked, int length) {
		buffer = new RingBuffer(length);
		acktable = new long[50][2];
		this.acked = acked;
	}
	
	// Methods for TCP receiver
	public int availableSpace() {
			return buffer.availableSpace();
	}
	
	public long put(byte[] b, long seq_offset) {
		long old_acked = acked;
		int off = (int)((seq_offset - acked) & 0xffff);	//TODO &0xff ???
		
		if (off < 0)
			return TCPReceiveBuffer.DISCARD;
		
		if (this.availableSpace()-off < b.length)
			return TCPReceiveBuffer.DISCARD;

		buffer.write(b, off, false);
		
		if (off == 0) {
			acked += b.length;
			this.advance();
		} else {
			for (int i=0; i<acktable.length; i++) {
				if (acktable[i][0] == 0) {
					acktable[i][0] = seq_offset;
					acktable[i][1] = b.length;
					break;
				}
			}
		}
		buffer.advanceWHead((int)((acked-old_acked) & 0xffff));
		
		return acked;
	}
	
	private void advance() {
		boolean found_older = false;
		for (int i=0; i<acktable.length; i++) {
			if ((acktable[i][0] != 0) && (acktable[i][0] <= acked+1)) {
				acked = Math.max(acked, acktable[i][0]+acktable[i][1]);
				acktable[i][0] = 0;
				acktable[i][1] = 0;
				found_older = true;
			}
		}
		if (found_older) advance();
	}
	
	// Methods for InputStream
	public int available() {
			return buffer.availableBytes();
	}
	
	public void close() throws IOException {
		super.close();
	}
	
	public boolean markSupported() {
		return false;
	}
	
	public int read(byte[] b, int off, int len) throws IOException {
		if (len == 0)
			return len;
		
		byte[] tmp = buffer.read(len, 0, true);
		System.arraycopy(tmp, 0, b, off, len);
		return len;
	}
	
	public int read(byte[] b) throws IOException {
		return this.read(b, 0, b.length);
	}
	
	public long skip(long n) throws IOException {
		int a = (int)(n & 0xffff);		//TODO &0xff ???
			buffer.advanceRHead(a);
		return a;
	}
	
	public int read() throws IOException {
		byte[] tmp = buffer.read(1, 0, true);
		return tmp[0];
	}
}