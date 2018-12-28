package net.protocol.ip.tcp.socket;

public class RingBuffer {
	/** The array where the buffers data are stored */
	private byte[] buffer;

	/** The position of the write head */
	private int wh;

	/** The position of the read head */
	private int rh;

	/**
	 * Creates a new RingBuffer
	 * @param	size The size of the buffer
	 */
	public RingBuffer(int size) {
		//initialize the buffer
		//the size is +1 because one byte is used to separate the read and write heads
		this.buffer = new byte[size+2];
		
		//initialize the head positions
		this.wh = 1;
		this.rh = 0;
	}

	/**
	 * Reads a portion of data from the buffer
	 * @param	length specifies how many bytes to be read
	 * @param	advance specifies if the read head should be adnanced to the new position or not
	 * @return	the next 'length' bytes from the buffer as a byte array
	 */
	public byte[] read(int length, int offset, boolean advance) {
		synchronized (buffer) {
			while (this.availableBytes() < length+offset) {
				System.out.println("read: "+this.availableBytes()+" < "+length+offset);
				try {
					buffer.wait();
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			
			int last_position = this.wrap(rh + length + offset);
			
			byte[] b = new byte[length];
			if (last_position > rh)
				System.arraycopy(buffer, rh + 1 + offset, b, 0, length);
			else {
				int a = buffer.length - rh - 1 - offset;
				System.arraycopy(buffer, rh + 1 + offset, b, 0, a);
				System.arraycopy(buffer, 0, b, a, b.length - a);
			}
			if (advance)
				rh = wrap(rh + length + offset);
			
			buffer.notifyAll();
			
			return b;
		}		
	}

	/**
	 * Writes some data into the buffer
	 * @param	b the data to be writen
	 */
	public void write(byte[] b, int offset, boolean advance) {
		synchronized(buffer) {
			while (this.availableSpace() < b.length+offset) {
				System.out.println("write: "+this.availableSpace()+" < "+(b.length+offset));
				try {
					buffer.wait();
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			
			if (wh + offset > buffer.length) {
				System.arraycopy(b, 0, buffer, wh+offset-buffer.length, b.length);
			} else if (wh + offset + b.length > buffer.length) {
				int a = buffer.length-wh-offset;
				try {
					System.arraycopy(b, 0, buffer, wh+offset, buffer.length-wh-offset);
				} catch (RuntimeException e) {
					System.out.println(e.getMessage()+" of:"+(wh+offset)+" len:"+(buffer.length-wh-offset));
				}
				try {
					System.arraycopy(b, a, buffer, 0, b.length-a);
				} catch (RuntimeException e) {
					System.out.println(e.getMessage()+" of1:"+a+" of2:0 len:"+(b.length-a));
				}
			} else {
				System.arraycopy(b, 0, buffer, wh+offset, b.length);
			}
			
			if (advance)
				wh = this.wrap(wh+b.length+offset);
			
			buffer.notifyAll();
		}
	}

	/**
	 * Returns the amount of the available data in the buffer
	 * @return the amount of the available data in the buffer
	 */
	public int availableBytes() {
		if (rh < wh)
			return wh - rh - 1;

		return wh - rh - 1 + buffer.length;
	}

	/**
	 * Returns the amount of the available space in the buffer
	 * @return the amount of the available space in the buffer
	 */
	public int availableSpace() {
		if (wh < rh)
			return rh - wh;
		
		return rh - wh + buffer.length - 1;
	}

	private int wrap(int pos) {
		if (pos >= buffer.length)
			pos -= buffer.length;

		return pos;
	}
	
	public void advanceRHead(int a) {
		synchronized(buffer) {
			rh = wrap(rh+a);
			
			buffer.notifyAll();
		}
	}
	
	public void advanceWHead(int a) {
		synchronized(buffer) {
			wh = wrap(wh+a);
			
			buffer.notifyAll();
		}
	}
}
