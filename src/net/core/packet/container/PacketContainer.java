package net.core.packet.container;

/**
 * The PacketContainer is the Superclass of all PacketContainer types. PacketContainer is 
 * responsible to contain the network packet. It is actualy an abstraction for arrays, 
 * ByteBuffers and other methods, that can be used to store a network packet. It provides 
 * some usefull methods to read and write and convert from and to values.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	28.3.2005
 */
public abstract class PacketContainer {
	private static ContainerFactory container_factory = new ContainerFactory("net.core.packet.container.ByteArrayContainer");
	
	/**
	 * Reads a single byte from container.
	 * @param	position the position of the container that we want to read.
	 * @return	the value of the requested position, as a int value.
	 */
	public abstract int read(int position);
	
	/**
	 * Reads two bytes from the container. The two bytes are used to create a 16-bit value,
	 * in which the first byte is the 8 MSB and the second byte the LSB.
	 * @param	position the position of the first of the bytes that we want to read.
	 * @return	the value of this pair of bytes.
	 */
	public abstract int read16(int position);
	
	/**
	 * Reads 4 bytes from the container. The 4 bytes are then used to create a 32-bit value,
	 * in the same order as in the packet.
	 * @param	position the position of the first of the bytes that we want to read.
	 * @return	the value of this 4 byte word.
	 */
	public abstract long read32(int position);
	
	/**
	 * Reads a portion of bytes from the container and returns them as a byte[].
	 * @param	position the position of the offset of the bytes we want to read.
	 * @param	length the length of the bytes we want.
	 * @return	a byte[] with the requested bytes.
	 */
	public abstract byte[] read(int position, int length);
	
	/**
	 * Writes a value as a byte into the given position. The value must be in the range  0-255
	 * otherwise the 24 MSB of the int value will be discarded.
	 * @param	position the position of the container that we want to write to.
	 * @param	value the value that we want to write.
	 */
	public abstract void write(int position, int value);
	
	/**
	 * Writes a value as a series of two bytes starting from the given position. The value
	 * must be in the range 0-65535, otherwise the 16 MSB will be discarded. 
	 * @param	position the position of the first byte that will be used to store this value.
	 * @param	value the value that we want to write.
	 */
	public abstract void write16(int position, int value);
	
	/**
	 * Writes a value as a series of 4 bytes starting from the given position. The value
	 * must be in the range 0-2^32, otherwise the 32 MSB will be discarded.
	 * @param	position the position of the first byte that will be used to store this value.
	 * @param	value the value that we want to write.
	 */
	public abstract void write32(int position, long value);
	
	/**
	 * Writes a part of a byte[] into the container.
	 * @param	position the position in container of the first byte that will be writen.
	 * @param	data the byte[] that will be writen to the container.
	 * @param	offset the starting offset of the data.
	 * @param	length the length of data.
	 */
	public abstract void write(int position, byte[] data, int offset, int length);
	
	/**
	 * Reads a byte from the container.
	 * @param	position the position of the container to be read.
	 * @return	the value of the byte in the given position.
	 */
	public abstract byte u_read(int position);
	
	/**
	 * Reads two bytes from the container and returns them as a short value. The returned value
	 * is a short, where the first byte is the 8 MSB and the second byte the 8 LSB.
	 * @param	position the position of the first byte to be read.
	 * @return	the short equivalent of this two bytes.
	 */
	public abstract short u_read16(int position);
	
	/**
	 * Reads four bytes from the container and returns them as a int value. The returned value
	 * is a int, where the four bytes are plased in order to create this 32bit int value.
	 * @param	position the position of the first byte to be read.
	 * @return	the int equivalent of this four bytes.
	 */
	public abstract int u_read32(int position);
	
	/**
	 * Writes a byte in the container.
	 * @param	position the position of the byte to be writen.
	 * @param	value the value of the byte to be writen.
	 */
	public abstract void u_write(int position, byte value);
	
	/**
	 * Writes a short value as two bytes in the container. The 8 MSB of the short are writen as
	 * the first byte and the 8 LSB are writen in the next position.
	 * @param	position the position of the first byte to be writen.
	 * @param	value the short value that we want to write.
	 */
	public abstract void u_write16(int position, short value);
	
	/**
	 * Writes a int value as four bytes in the container. The int value is separated to 8 byte
	 * values, and then they are writen continusly starting from the given position.
	 * @param	position the position of the first byte to be writen.
	 * @param	value the int value that we want to write.
	 */
	public abstract void u_write32(int position, int value);
	
	/**
	 * Copies a packet container to a byte[].
	 * @param	position the position of the first byte of the container to be copied to the byte[].
	 * @param	b the byte array that will be the destination of the copy.
	 * @param	offset the starting offset at the b array.
	 * @param	length the length of the bytes to be copied.
	 */
	public abstract void copyToArray(int position, byte[] b, int offset, int length);
	
	/**
	 * Returs a part of this container as another container.
	 * @param	offset the starting offset of the subcontainer.
	 * @param	length the length of the subcontainer.
	 * @return	a part of this container starting from offset and with the given length as
	 * 			a PacketContainer.
	 */
	public abstract PacketContainer getSubContainer(int offset, int length);
	
	/**
	 * Returns the size of this container.
	 * @return	the size of this container.
	 */
	public abstract int getSize();
	
	/**
	 * Returns this container as a byte[].
	 * @return	a byte[] with the same contents as this container.
	 */
	public abstract byte[] toArray();
	
	/**
	 * Returns a copy of this container (similar to clone()).
	 * @return	a copy of this container.
	 */
	public abstract PacketContainer getCopy();
	
	public boolean equals(Object o) {
		PacketContainer c;
		try {
			c = (PacketContainer)o;
		} catch (RuntimeException e) {
			return false;
		}
		if (this.getSize() != c.getSize())
			return false;
		for (int i=0; i<this.getSize(); i++)
			if (this.u_read(i) != c.u_read(i))
				return false;
		return true;
	}
	
	/**
	 * Copies a PacketContainer into another. It's very similar to System.arrayCopy method.
	 * @param	src the source container.
	 * @param	s_offset the starting offset at the source container.
	 * @param	dst the destination container.
	 * @param	d_offset the starting offset at the destination container.
	 * @param	length the length of the bytes to be copied.
	 */
	public static void containerCopy(PacketContainer src, int s_offset, PacketContainer dst, int d_offset, int length) {
		dst.write(d_offset, src.read(s_offset, length), 0, length);
	}
	
	public static PacketContainer createContainer(int size) {
		return container_factory.createContainer(size);
	}

	public static PacketContainer createContainer(byte[] data) {
		return container_factory.createContainer(data);
	}
}
