package net.core.packet.container;

/**
 * The ByteArrayContainer uses a byte[] to store the bytes of a packet.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	14.8.2005
 */
class ByteArrayContainer extends PacketContainer {
	/** The byte[] that holds the bytes of this container. */
	protected byte[] data;
	
	/**
	 * Creates a new ByteArrayContainer with the specified length.
	 * @param	length the length of the container.
	 */
	public ByteArrayContainer(int length) {
		this.data = new byte[length];
	}
	
	/**
	 * Creates a new ByteArrayContainer from a byte[]. The ByteArrayContainer will use a copy
	 * of this byte[].
	 * @param	b the byte[] with the containers data.
	 */
	private ByteArrayContainer(byte[] b) {
		this.data = (byte[])b.clone();
	}
	
	public int read(int p) {
		return data[p] & 0xff;
	}
	
	public int read16(int p) {
		return (((data[p] & 0xff) << 8) | (data[p+1] & 0xff));
	}
	
	public long read32(int p) {
		return (long)(
				((data[p] & 0xff) << 24) |
				((data[p+1] & 0xff) << 16) | 
				((data[p+2] & 0xff) << 8) | 
				(data[p+3] & 0xff)
				);
	}
	
	public byte[] read(int p, int length) {
		byte[] b = new byte[length];
		for (int i=0; i<length; i++) {
			b[i] = data[p+i];
		}
		return b;
	}
	
	public void write(int p, int v) {
		data[p] = (byte)v;
	}
	
	public void write16(int p, int v) {
		data[p] = (byte)(v >> 8);
		data[p+1] = (byte)v;
	}
	
	public void write32(int p, long v) {
		data[p] = (byte)(v >> 24);
		data[p+1] = (byte)(v >> 16);
		data[p+2] = (byte)(v >> 8);
		data[p+3] = (byte)v;
	}
	
	public void write(int p, byte[] b, int offset, int length) {
		for (int i=0; i<length; i++) {
			data[p+i] = b[offset+i];
		}
	}
	
	public byte u_read(int p) {
		return data[p];
	}
	
	public short u_read16(int p) {
		return (short)(((data[p] & 0xff) << 8) | (data[p+1] & 0xff));
	}
	
	public int u_read32(int p) {
		return (
				((data[p] & 0xff) << 24) |
				((data[p+1] & 0xff) << 16) |
				((data[p+2] & 0xff) << 8) |
				(data[p+3] & 0xff)
				);
	}
	
	public void u_write(int p, byte v) {
		data[p] = v;
	}
	
	public void u_write16(int p, short v) {
		int a = v & 0xffff;
		data[p] = (byte)(a >> 8);
		data[p+1] = (byte)a;
	}
	
	public void u_write32(int p, int v) {
		int a = v & 0xffffffff;
		data[p] = (byte)(a >> 24);
		data[p+1] = (byte)(a >> 16);
		data[p+2] = (byte)(a >> 8);
		data[p+3] = (byte)a;
	}
	
	public void copyToArray(int p, byte[] b, int offset, int length) {
		for (int i=0; i<length; i++) {
			b[offset+i] = data[p+i];
		}
	}
	
	public PacketContainer getSubContainer(int offset, int length) {
		byte[] b = new byte[length];
		try {
			System.arraycopy(this.data, offset, b, 0, length);
		} catch (RuntimeException e) {
			System.out.println("ByteArrayContainer.getSubContainer");
			e.printStackTrace();
		}
		return new ByteArrayContainer(b);
	}
	
	public int getSize() {
		return data.length;
	}
	
	public byte[] toArray() {
		byte[] b = new byte[data.length];
		System.arraycopy(data, 0, b, 0, data.length);
		return b;
	}
	
	public PacketContainer getCopy() {
		return new ByteArrayContainer(this.toArray());
	}
}
