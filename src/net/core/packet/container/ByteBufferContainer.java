package net.core.packet.container;

import java.nio.ByteBuffer;

/**
 * The ByteBufferContainer uses java.nio.ByteBuffer to store the bytes of a packet.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	16.9.2005
 */
class ByteBufferContainer extends PacketContainer {
	/** The ByteBuffer that holds the data of this container. */
	ByteBuffer data;
	
	/**
	 * Creates a new ByteBufferContainer with the specified size.
	 * @param	size the size of the container.
	 */
	public ByteBufferContainer(int size) {
		this.data = ByteBuffer.allocate(size);
	}
	
	/**
	 * Creates a new ByteBufferContainer from the specified ByteBuffer. The new ByteBuffer will
	 * use a copy of this ByteBuffer.
	 * @param data the ByteBuffer that will be used
	 */
	protected ByteBufferContainer(ByteBuffer data) {
		this.data = data;
	}
	
	public int read(int p) {
		return data.get(p) & 0xff;
	}
	
	public int read16(int p) {
		return (((data.get(p) & 0xff) << 8) | (data.get(p+1) & 0xff));
	}
	
	public long read32(int p) {
		return (
				((data.get(p) & 0xff) << 32) |
				((data.get(p+1) & 0xff) << 16) | 
				((data.get(p+2) & 0xff) << 8) | 
				(data.get(p+3) & 0xff)
				);
	}
	
	public byte[] read(int p, int length) {
		byte[] b = new byte[length];
		data.position(p);
		data.get(b, 0 ,length);
		return b;
	}
	
	public void write(int p, int v) {
		data.put(p, (byte)v);
	}
	
	public void write16(int p, int v) {
		data.putShort(p, (short)v);
	}
	
	public void write32(int p, long v) {
		data.putLong(p, v);
	}
	
	public void write(int p, byte[] b, int offset, int length) {
		data.position(p);
		data.put(b, offset, length);
	}
	
	public byte u_read(int p) {
		return data.get(p);
	}
	
	public short u_read16(int p) {
		return data.getShort(p);
	}
	
	public int u_read32(int p) {
		return data.getInt(p);
	}
	
	public void u_write(int p, byte v) {
		data.put(p, v);
	}
	
	public void u_write16(int p, short v) {
		data.putShort(p, v);
	}
	
	public void u_write32(int p, int v) {
		data.putInt(p, v);
	}
	
	public void copyToArray(int p, byte[] b, int offset, int length) {
		data.position(p);
		data.get(b, offset, length);
	}
	
	public PacketContainer getSubContainer(int offset, int length) {
		byte[] b = this.read(offset, length);
		return new ByteBufferContainer(ByteBuffer.wrap(b));
	}
	
	public int getSize() {
		return data.capacity();
	}
	
	public byte[] toArray() {
		return data.array();
	}
	
	public PacketContainer getCopy() {
		ByteBufferContainer c = new ByteBufferContainer(this.data.duplicate());
		return c;
	}
}
