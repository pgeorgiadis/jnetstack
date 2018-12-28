package net.core.packet.container;

import java.nio.ByteBuffer;

/**
 * The DirectByteBufferContainer like ByteBufferContainer uses java.nio.ByteBuffers to store
 * the bytes of a packet, but instead of indirect ByteBuffers it uses direct ByteBuffers.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	16.9.2005
 */
class DirectByteBufferContainer extends ByteBufferContainer {
	/**
	 * Creates a new DirectByteBuffer with the specified size
	 * @param	size the size of the container
	 */
	public DirectByteBufferContainer(int size) {
		super(size);
		this.data = ByteBuffer.allocateDirect(size);
	}
	
	public PacketContainer getCopy() {
		ByteBufferContainer c = new ByteBufferContainer(this.data.duplicate());
		return c;
	}
}
