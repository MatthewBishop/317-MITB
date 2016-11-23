package com.jagex.io;

public class BitBuffer extends Buffer {

	public BitBuffer(byte[] abyte0) {
		super(abyte0);
	}

	public int bitPosition;
	private static final int[] BIT_MASKS = { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383,
			32767, 65535, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff,
			0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff, -1 };

	public void enableBitAccess() {
		this.bitPosition = this.position * 8;
	}

	public int readBits(int amount) {
		int k = this.bitPosition >> 3;
		int l = 8 - (this.bitPosition & 7);
		int i1 = 0;
		this.bitPosition += amount;
		for (; amount > l; l = 8) {
			i1 += (this.payload[k++] & BitBuffer.BIT_MASKS[l]) << amount - l;
			amount -= l;
		}
		if (amount == l)
			i1 += this.payload[k] & BitBuffer.BIT_MASKS[l];
		else
			i1 += this.payload[k] >> l - amount & BitBuffer.BIT_MASKS[amount];
		return i1;
	}

	public void disableBitAccess() {
		this.position = (this.bitPosition + 7) / 8;
	}

	public int readLEUShort() {
		this.position += 2;
		return ((this.payload[this.position - 1] & 0xff) << 8) + (this.payload[this.position - 2] & 0xff);
	}

	public int readUShortA() {
		this.position += 2;
		return ((this.payload[this.position - 2] & 0xff) << 8) + (this.payload[this.position - 1] - 128 & 0xff);
	}

	public int readLEUShortA() {
		this.position += 2;
		return ((this.payload[this.position - 1] & 0xff) << 8) + (this.payload[this.position - 2] - 128 & 0xff);
	}

	public int readLEShort() {
		this.position += 2;
		int j = ((this.payload[this.position - 1] & 0xff) << 8) + (this.payload[this.position - 2] & 0xff);
		if (j > 32767)
			j -= 0x10000;
		return j;
	}

	public int readLEShortA() {
		this.position += 2;
		int j = ((this.payload[this.position - 1] & 0xff) << 8) + (this.payload[this.position - 2] - 128 & 0xff);
		if (j > 32767)
			j -= 0x10000;
		return j;
	}

	public int readIMEInt() {
		this.position += 4;
		return ((this.payload[this.position - 3] & 0xff) << 24) + ((this.payload[this.position - 4] & 0xff) << 16)
				+ ((this.payload[this.position - 1] & 0xff) << 8) + (this.payload[this.position - 2] & 0xff);
	}

	public int readMEInt() {
		this.position += 4;
		return ((this.payload[this.position - 2] & 0xff) << 24) + ((this.payload[this.position - 1] & 0xff) << 16)
				+ ((this.payload[this.position - 4] & 0xff) << 8) + (this.payload[this.position - 3] & 0xff);
	}

	public int readUByteA() {
		return this.payload[this.position++] - 128 & 0xff;
	}

	public int readNegUByte() {
		return -this.payload[this.position++] & 0xff;
	}

	public int readUByteS() {
		return 128 - this.payload[this.position++] & 0xff;
	}

	public byte readNegByte() {
		return (byte) (-this.payload[this.position++]);
	}

	public byte readByteS() {
		return (byte) (128 - this.payload[this.position++]);
	}

	public void readReverseData(int i, int j, byte abyte0[]) {
		for (int k = (j + i) - 1; k >= j; k--)
			abyte0[k] = this.payload[this.position++];

	}
}
