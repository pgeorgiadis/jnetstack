package net.core.packet.container;


public class DirectBufferContainerTest extends PacketContainerTest {
	public DirectBufferContainerTest() {
		this.container = new DirectByteBufferContainer(20);
	}
}
