package net.core.packet.container;

/**
 * The FastByteArrayContainer is almost identical to ByteArrayContainer. The only difference is
 * that the FastByteArrayContainer method getSubContainer returns a FastByteArrayPacketContainer
 * that uses the same byte[] as the one in which the getSubContainer called.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	14.8.2005
 */
class FastByteArrayContainer extends PacketContainer {
	/** The byte[] that holds the bytes of this container. */
	protected byte[] data;
	
	protected int mask_offset;
	protected int mask_length;
	
	/**
	 * Creates a new ByteArrayContainer with the specified length.
	 * @param	length the length of the container.
	 */
	public FastByteArrayContainer(int length) {
		this.data = new byte[length];
		this.mask_offset = 0;
		this.mask_length = length;
	}
	
	/**
	 * Creates a new ByteArrayContainer from a byte[]. The ByteArrayContainer will use a copy
	 * of this byte[].
	 * @param	b the byte[] with the containers data.
	 */
	private FastByteArrayContainer(byte[] b, int mask_offset, int mask_length) {
		this.data = b;
		this.mask_offset = mask_offset;
		this.mask_length = mask_length;
	}
	
	public int read(int p) {
		if (p >= mask_length)
			throw new ArrayIndexOutOfBoundsException("The requested position is out of mask");
		
		return data[mask_offset+p] & 0xff;
	}
	
	public int read16(int p) {
		if (p >= mask_length || p+2 > mask_length) throw new ArrayIndexOutOfBoundsException("The requested position is out of mask");
		
		return (((data[mask_offset+p] & 0xff) << 8) | (data[mask_offset+p+1] & 0xff));
	}
	
	public long read32(int p) {
		if (p >= mask_length || p+4 > mask_length)
			throw new ArrayIndexOutOfBoundsException("The requested position is out of mask");
		
		return (long)(
				((data[mask_offset+p] & 0xff) << 24) |
				((data[mask_offset+p+1] & 0xff) << 16) | 
				((data[mask_offset+p+2] & 0xff) << 8) | 
				(data[mask_offset+p+3] & 0xff)
				);
	}
	
	public byte[] read(int p, int length) {
		if (p >= mask_length || p+length > mask_length)
			throw new ArrayIndexOutOfBoundsException("The requested position is out of mask");
		
		byte[] b = new byte[length];
		for (int i=0; i<length; i++) {
			b[i] = data[mask_offset+p+i];
		}
		return b;
	}
	
	public void write(int p, int v) {
		if (p >= mask_length)
			throw new ArrayIndexOutOfBoundsException("The requested position is out of mask");
		
		data[mask_offset+p] = (byte)v;
	}
	
	public void write16(int p, int v) {
		if (p >= mask_length || p+2 > mask_length)
			throw new ArrayIndexOutOfBoundsException("The requested position is out of mask");
		
		data[mask_offset+p] = (byte)(v >> 8);
		data[mask_offset+p+1] = (byte)v;
	}
	
	public void write32(int p, long v) {
		if (p >= mask_length || p+4 > mask_length)
			throw new ArrayIndexOutOfBoundsException("The requested position is out of mask");
		
		data[mask_offset+p] = (byte)(v >> 24);
		data[mask_offset+p+1] = (byte)(v >> 16);
		data[mask_offset+p+2] = (byte)(v >> 8);
		data[mask_offset+p+3] = (byte)v;
	}
	
	public void write(int p, byte[] b, int offset, int length) {
		if (p >= mask_length || p+length > mask_length)
			throw new ArrayIndexOutOfBoundsException("The requested position is out of mask");
		
		for (int i=0; i<length; i++) {
			data[mask_offset+p+i] = b[offset+i];
		}
	}
	
	public byte u_read(int p) {
		if (p >= mask_length)
			throw new ArrayIndexOutOfBoundsException("The requested position is out of mask");
		
		return data[mask_offset+p];
	}
	
	public short u_read16(int p) {
		if (p >= mask_length || p+2 > mask_length)
			throw new ArrayIndexOutOfBoundsException("The requested position is out of mask");
		
		return (short)(((data[mask_offset+p] & 0xff) << 8) | (data[mask_offset+p+1] & 0xff));
	}
	
	public int u_read32(int p) {
		if (p >= mask_length || p+4 > mask_length)
			throw new ArrayIndexOutOfBoundsException("The requested position is out of mask");
		
		return (
				((data[mask_offset+p] & 0xff) << 24) |
				((data[mask_offset+p+1] & 0xff) << 16) |
				((data[mask_offset+p+2] & 0xff) << 8) |
				(data[mask_offset+p+3] & 0xff)
				);
	}
	
	public void u_write(int p, byte v) {
		if (p >= mask_length)
			throw new ArrayIndexOutOfBoundsException("The requested position is out of mask");
		
		data[mask_offset+p] = v;
	}
	
	public void u_write16(int p, short v) {
		if (p >= mask_length || p+2 > mask_length)
			throw new ArrayIndexOutOfBoundsException("The requested position is out of mask");
		
		int a = v & 0xffff;
		data[mask_offset+p] = (byte)(a >> 8);
		data[mask_offset+p+1] = (byte)a;
	}
	
	public void u_write32(int p, int v) {
		if (p >= mask_length || p+4 > mask_length)
			throw new ArrayIndexOutOfBoundsException("The requested position is out of mask");
		
		int a = v & 0xffffffff;
		data[mask_offset+p] = (byte)(a >> 24);
		data[mask_offset+p+1] = (byte)(a >> 16);
		data[mask_offset+p+2] = (byte)(a >> 8);
		data[mask_offset+p+3] = (byte)a;
	}
	
	public void copyToArray(int p, byte[] b, int offset, int length) {
		if (p >= mask_length || p+length > mask_length)
			throw new ArrayIndexOutOfBoundsException("The requested position is out of mask");
		
		for (int i=0; i<length; i++) {
			b[offset+i] = data[mask_offset+p+i];
		}
	}
	
	public PacketContainer getSubContainer(int offset, int length) {
		if (offset >= mask_length || offset+length > mask_length)
			throw new ArrayIndexOutOfBoundsException("The requested position is out of mask");
		
		return new FastByteArrayContainer(this.data, mask_offset+offset, length);
	}
	
	public int getSize() {
		return mask_length;
	}
	
	public byte[] toArray() {
		byte[] b = new byte[mask_length];
		System.arraycopy(data, mask_offset, b, 0, mask_length);
		return b;
	}
	
	public PacketContainer getCopy() {
		return new FastByteArrayContainer(this.toArray(), 0, mask_length);
	}
}
