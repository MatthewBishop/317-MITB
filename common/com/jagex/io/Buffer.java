package com.jagex.io;

public class Buffer {

	public Buffer(byte abyte0[]) {
		this.payload = abyte0;
		this.position = 0;
	}

	public int readUByte() {
		return this.payload[this.position++] & 0xff;
	}

	public byte readByte() {
		return this.payload[this.position++];
	}

	public int readUShort() {
		this.position += 2;
		return ((this.payload[this.position - 2] & 0xff) << 8) + (this.payload[this.position - 1] & 0xff);
	}

	public int readShort() {
		this.position += 2;
		int i = ((this.payload[this.position - 2] & 0xff) << 8) + (this.payload[this.position - 1] & 0xff);
		if (i > 32767)
			i -= 0x10000;
		return i;
	}

	public int readUTriByte() {
		this.position += 3;
		return ((this.payload[this.position - 3] & 0xff) << 16) + ((this.payload[this.position - 2] & 0xff) << 8)
				+ (this.payload[this.position - 1] & 0xff);
	}

	public int readInt() {
		this.position += 4;
		return ((this.payload[this.position - 4] & 0xff) << 24) + ((this.payload[this.position - 3] & 0xff) << 16)
				+ ((this.payload[this.position - 2] & 0xff) << 8) + (this.payload[this.position - 1] & 0xff);
	}

	public long readLong() {
		long l = this.readInt() & 0xffffffffL;
		long l1 = this.readInt() & 0xffffffffL;
		return (l << 32) + l1;
	}

	public String readString() {
		int i = this.position;
		while (this.payload[this.position++] != 10)
			;
		return new String(this.payload, i, this.position - i - 1);
	}

	public byte[] readStringBytes() {
		int i = this.position;
		while (this.payload[this.position++] != 10)
			;
		byte abyte0[] = new byte[this.position - i - 1];
		System.arraycopy(this.payload, i, abyte0, i - i, this.position - 1 - i);
		return abyte0;
	}

	public void readData(byte abyte0[], int j, int i) {
		for (int l = j; l < j + i; l++)
			abyte0[l] = this.payload[this.position++];
	}

	public int readSmart() {
		int i = this.payload[this.position] & 0xff;
		if (i < 128)
			return this.readUByte() - 64;
		else
			return this.readUShort() - 49152;
	}

	public int readUSmart() {
		int i = this.payload[this.position] & 0xff;
		if (i < 128)
			return this.readUByte();
		else
			return this.readUShort() - 32768;
	}

	public byte payload[];
	public int position;
}
