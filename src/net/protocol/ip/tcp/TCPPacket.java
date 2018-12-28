package net.protocol.ip.tcp;

import net.core.packet.Packet;
import net.core.packet.RawPacket;
import net.core.packet.container.BitMask;
import net.core.packet.container.PacketContainer;
import net.protocol.ip.tcp.socket.TCPSocketConstants;

/**
 *  The TCPPacket represents the tcp packets.
 * <br>
 * TCPPacket provides accessors to all tcp packet fields.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	21.8.2005
 */
public class TCPPacket extends Packet {
	/**
	 * Creates a new TCPPacket contained in the specified PacketContainer. It also sets the data 
	 * offset field to 5.
	 * @param	container the PacketContainer that will contain or already contains the packet.
	 */
	public TCPPacket(PacketContainer container) {
		this.container = container;
		this.setHeaderLength(5);
	}
	
	/**
	 * Creates a new TCPPacket with the specified payload length.
	 * @param	payload_len the payload length.
	 */
	public TCPPacket(int payload_len) {
		this.container = PacketContainer.createContainer(payload_len+20);
		this.setHeaderLength(5);
	}
	
	/**
	 * Returns the source TCP port of the packet.
	 * @return	the source TCP port of the packet.
	 */
	public int getSrcPort() {
		return container.read16(0);
	}
	
	/**
	 * Changes the source port of the packet.
	 * @param	port the new source port of the packet.
	 */
	public void setSrcPort(int port) {
		container.write16(0, port);
	}
	
	/**
	 * Returns the destination TCP port of the packet.
	 * @return	the destination TCP port of the packet.
	 */
	public int getDstPort() {
		return container.read16(2);
	}
	
	/**
	 * Changes the destination port of the packet.
	 * @param	port the new destination port of the packet.
	 */
	public void setDstPort(int port) {
		container.write16(2, port);
	}
	
	/**
	 * Returns the value of sequence number field of the packet.
	 * @return	the value of sequence number field of the packet.
	 */
	public long getSeqNumber() {
//		StringBuffer sb = new StringBuffer();
//		BitMask bm = new BitMask(new byte[] {container.u_read(4)});
//		boolean[] b = bm.toBooleanArray();
//		for (int i=0; i<b.length; i++)
//			sb.append(b[i]?"1":"0");
//		bm = new BitMask(new byte[] {container.u_read(5)});
//		b = bm.toBooleanArray();
//		for (int i=0; i<b.length; i++)
//			sb.append(b[i]?"1":"0");
//		bm = new BitMask(new byte[] {container.u_read(6)});
//		b = bm.toBooleanArray();
//		for (int i=0; i<b.length; i++)
//			sb.append(b[i]?"1":"0");
//		bm = new BitMask(new byte[] {container.u_read(7)});
//		b = bm.toBooleanArray();
//		for (int i=0; i<b.length; i++)
//			sb.append(b[i]?"1":"0");
//		System.out.println(sb.toString());
		return container.read32(4);
	}
	
	/**
	 * Changes the value of the sequence number field of the packet.
	 * @param	seq the new value.
	 */
	public void setSeqNumber(long seq) {
		container.write32(4, seq);
	}
	
	/**
	 * Returns the value of acknowledge number field of the packet.
	 * @return	the value of acknowledge number field of the packet.
	 */
	public long getAckNumber() {
		return container.read32(8);
	}
	
	/**
	 * Changes the value of the acknowledge number field of the packet.
	 * @param	ack the new value.
	 */
	public void setAckNumber(long ack) {
		container.write32(8, ack);
	}
	
	/**
	 * Returns the value of data offset field of the packet.
	 * @return	the value of data offset field of the packet.
	 */
	public int getHeaderLength() {
		BitMask m = new BitMask(container.read(12, 1));
		return m.evaluate(0, 4);
	}
	
	/**
	 * Changes the value of the data offset field of the packet.
	 * @param	offset the new value.
	 */
	public void setHeaderLength(int offset) {
		BitMask m = new BitMask(container.read(12, 1));
		m.set(0, 4, offset);
		container.write(12, m.toByteArray(), 0, 1);
	}
	
	/**
	 * Tests if the NS flag is set.
	 * @return true if the NS flag is set or false if not.
	 */
	public boolean isNS() {
		BitMask m = new BitMask(container.read(12, 2));
		return m.getBit(7);
	}
	
	/**
	 * Changes the state of the NS flag.
	 * @param ns the new state of the NS flag.
	 */
	public void setNS(boolean ns) {
		BitMask m = new BitMask(container.read(12, 2));
		m.setBit(7, ns);
		container.write(12, m.toByteArray(), 0, 2);
	}
	
	/**
	 * Tests if the CWR flag is set.
	 * @return true if the CWR flag is set or false if not.
	 */
	public boolean isCWR() {
		BitMask m = new BitMask(container.read(12, 2));
		return m.getBit(8);
	}
	
	/**
	 * Changes the state of the CWR flag.
	 * @param cwr the new state of the CWR flag.
	 */
	public void setCWR(boolean cwr) {
		BitMask m = new BitMask(container.read(12, 2));
		m.setBit(8, cwr);
		container.write(12, m.toByteArray(), 0, 2);
	}
	
	/**
	 * Tests if the ECE flag is set.
	 * @return true if the ECE flag is set or false if not.
	 */
	public boolean isECE() {
		BitMask m = new BitMask(container.read(12, 2));
		return m.getBit(9);
	}
	
	/**
	 * Changes the state of the ECE flag.
	 * @param ece the new state of the ECE flag.
	 */
	public void setECE(boolean ece) {
		BitMask m = new BitMask(container.read(12, 2));
		m.setBit(9, ece);
		container.write(12, m.toByteArray(), 0, 2);
	}
	
	/**
	 * Returns the value of the specified control bit flag.
	 * @param	c the control bit.
	 * @return	the value of sequence number field of the packet.
	 * @see		TCPSocketConstants
	 */
	public boolean getControlBits(int c) {
		BitMask m = new BitMask(container.read(12, 2));
		return m.getBit(c);
	}
	
	/**
	 * Changes the status of the specified control bit flag.
	 * @param	c the control bit to be changed.
	 * @param	b the new value.
	 * @see		TCPSocketConstants
	 */
	public void setControlBits(int c, boolean b) {
		BitMask m = new BitMask(container.read(12, 2));
		m.setBit(c, b);
		container.write(12, m.toByteArray(), 0, 2);
	}
	
	/**
	 * Returns the value of window field of the packet.
	 * @return	the value of window field of the packet.
	 */
	public int getWindow() {
		return container.read16(14);
	}
	
	/**
	 * Changes the value of the window field of the packet.
	 * @param	w the new value.
	 */
	public void setWindow(int w) {
		container.write16(14, w);
	}
	
	/**
	 * Returns the value of checksum field of the packet.
	 * @return	the value of checksum field of the packet.
	 */
	public int getChecksum() {
		return container.read16(16);
	}
	
	/**
	 * Changes the value of the checksum field of the packet.
	 * @param	c the new value.
	 */
	public void setChecksum(int c) {
		container.write16(16, c);
	}
	
	/**
	 * Returns the value of urgent pointer field of the packet.
	 * @return	the value of urgent pointer field of the packet.
	 */
	public int getUrgent() {
		return container.read16(18);
	}
	
	/**
	 * Changes the value of the urgend pointer field of the packet.
	 * @param	u the new value.
	 */
	public void setUrgent(int u) {
		container.write16(18, u);
	}
	
	public Packet getPayload() {
		PacketContainer c = PacketContainer.createContainer(container.getSize() - this.getHeaderLength()*4);
		PacketContainer.containerCopy(container, this.getHeaderLength()*4, c, 0, c.getSize());
		return new RawPacket(c);
	}
	
	public void setPayload(Packet payload) {
		PacketContainer c = payload.getContainer();
		PacketContainer.containerCopy(c, 0, container, this.getHeaderLength()*4, c.getSize());
	}
	
	public Packet getCopy() {
		return new TCPPacket(container.getCopy());
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("src port:"+this.getSrcPort()+" dst port:"+this.getDstPort());
		if (this.getControlBits(TCPSocketConstants.SYN))
			sb.append(", SYN");
		if (this.getControlBits(TCPSocketConstants.ACK))
			sb.append(", ACK");
		if (this.getControlBits(TCPSocketConstants.FIN))
			sb.append(", FIN");
		if (this.getControlBits(TCPSocketConstants.RST))
			sb.append(", RST");
		
		sb.append(" seq="+this.getSeqNumber());
		sb.append(" ack="+this.getAckNumber());
		
		sb.append(" data:"+this.getPayload().getSize());
		return sb.toString();
	}
}
