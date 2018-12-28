package net.core.packet.container;

/**
 * The BitMask is a utility class used to read and write specific bits from and to
 * a PacketContainer. It can be used for other purposes also, as it is a tool to convert a
 * byte[] to a boolean[] and visversa.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	16.8.2005
 */
public class BitMask {
	/** The boolean array that holds the bits of this mask. */
	boolean[] bits;
	
	/**
	 * Creates a BitMask that can contain up to length bits.
	 * @param	length the length of this BitMask.
	 * @throws	IllegalArgumentException if the length is not a multiple of 8.
	 */
	public BitMask(int length) {
		if (length%8 != 0)
			throw new IllegalArgumentException("BitMask length must be multiple of 8");
		bits = new boolean[length];
	}
	
	/**
	 * Creates a BitMask by parsing a given byte[].
	 * @param	b the byte[] to be parsed.
	 */
	public BitMask(byte[] b) {
		bits = new boolean[8*b.length];
		for (int i=0; i<b.length; i++) {
			int a = b[i];
			for (int j=7; j>=0; j--) {
				bits[j+(8*i)] = (a%2 != 0);
				a = a >> 1;
			}
		}
	}
	
	/**
	 * Converts this BitMask to a byte[].
	 * @return	this BitMask as a byte[].
	 */
	public byte[] toByteArray() {
		byte[] b = new byte[bits.length/8];
		for (int i=0; i<b.length; i++) {
			int a = 0;
			for (int j=8*i; j<8*(i+1); j++) {
				if (bits[j]) a++;
				a = a << 1;
			}
			a = a >> 1;
			b[i] = (byte)a;
		}
		return b;
	}
	
	/**
	 * Gets the value of the bit in the given position.
	 * @param	position the position we want to check.
	 * @return	the value of the bit in the given position.
	 */
	public boolean getBit(int position) {
		return bits[position];
	}
	
	/**
	 * Sets the value of the bit in the given position.
	 * @param	position the position we want to set.
	 * @param	value the new value that we want to set to the given position.
	 */
	public void setBit(int position, boolean value) {
		bits[position] = value;
	}
	
	/**
	 * Changes all bit's to zero (false).
	 */
	public void zeroAll() {
		for (int i=0; i<bits.length; i++) {
			bits[i] = false;
		}
	}
	
	/**
	 * Gets the numerical value that a range of bits of this BitMask represent.
	 * @param	offset the starting offset of the range.
	 * @param	length the length of the range.
	 * @return	the value that this range of bit's represent.
	 * @throws	IllegalArgumentException if the length is greater than 31 or if the requested range
	 * 			is out of the BitMask range.
	 */
	public int evaluate(int offset, int length) {
		if (length > 31) throw new IllegalArgumentException("length should not be greater than 31");
		if (offset+length > bits.length) throw new IllegalArgumentException("arguments are out of bit mask");
		int sum = 0;
		for (int i=0; i<length; i++) {
			if (bits[offset+i]) sum += Math.pow(2, length-i-1);
		}
		return sum;
	}
	
	/**
	 * Sets the numerical value that a range of bits of this BitMask represent.
	 * @param	offset the starting offset of the range.
	 * @param	length the length of the range.
	 * @param	value the value we want to set.
	 * @throws	IllegalArgumentException if: 1)the length is greater than 31<br>
	 * 			2)the requested range is out of this BitMask<br>
	 * 			3)the value to be writen does not fit in this bits.
	 */
	public void set(int offset, int length, int value) {
		if (length > 31) throw new IllegalArgumentException("length should not be greater than 31");
		if (offset+length > bits.length) throw new IllegalArgumentException("arguments are out of bit mask");
		if (value >= Math.pow(2, length)) throw new IllegalArgumentException("Value is to big for this length");
		for (int i=offset+length-1; i>=offset; i--) {
			if (value%2 != 0) bits[i] = true;
			else bits[i] = false;
			value = value >> 1;
		}
	}
	
	/**
	 * Returns thhis BitMask as a boolean[].
	 * @return	this BitMask as a boolean[].
	 */
	public boolean[] toBooleanArray() {
		boolean[] b = new boolean[bits.length];
		System.arraycopy(bits, 0, b, 0, bits.length);
		return b;
	}
}
