package net.protocol.ip.tcp.socket;

import java.io.IOException;
import java.io.OutputStream;

public class TCPSendBuffer extends OutputStream {
	private RingBuffer buffer;
	private long buffer_seq_offset;
	
	public void initOffset(long a) {
		this.buffer_seq_offset = a;
	}
	
	public TCPSendBuffer() {
		buffer = new RingBuffer(1024);
		buffer_seq_offset = 0;
	}
	
	public TCPSendBuffer(int length) {
		buffer = new RingBuffer(length);
		buffer_seq_offset = 0;
	}
	
	//Methods implementing the functionality needed by TCPSender and TCPReceiver
	public int availableData(long first_seq) {
		return buffer.availableBytes()-(int)((first_seq-buffer_seq_offset-1) & 0xff);
	}
	
	public int availableData() {
		return this.availableData(this.buffer_seq_offset+1);
	}
	
	public byte[] get(long first_seq, int length) throws IOException {
		int tmp = (int)((first_seq-buffer_seq_offset) & 0xff);
		return buffer.read(length, tmp-1, false);
	}
	
	public byte[] get(int length) throws IOException {
		return this.get(this.buffer_seq_offset+1, length);
	}
	
	public void acknowlege(long ack_seq) {
		int tmp = (int)((ack_seq-buffer_seq_offset) & 0xff);
		buffer.advanceRHead(tmp);
		buffer_seq_offset = ack_seq;
	}
	
	public void write(byte[] b, int off, int len) throws IOException {
		byte[] tmp = new byte[len];
		System.arraycopy(b, off, tmp, 0, len);
		buffer.write(tmp, 0, true);
	}
	
	public void write(byte[] b) throws IOException {
		buffer.write(b, 0, true);
	}
	
	public void write(int b) throws IOException {
		byte[] tmp = {(byte)(b & 0xff)};
		buffer.write(tmp, 0, true);
	}
}