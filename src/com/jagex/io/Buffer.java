package com.jagex.io;

public class Buffer {

	public Buffer(byte abyte0[]) {
		payload = abyte0;
		position = 0;
	}

	public int readUByte() {
		return payload[position++] & 0xff;
	}

	public byte readByte() {
		return payload[position++];
	}

	public int readUShort() {
		position += 2;
		return ((payload[position - 2] & 0xff) << 8) + (payload[position - 1] & 0xff);
	}

	public int readShort() {
		position += 2;
		int i = ((payload[position - 2] & 0xff) << 8) + (payload[position - 1] & 0xff);
		if (i > 32767)
			i -= 0x10000;
		return i;
	}

	public int readUTriByte() {
		position += 3;
		return ((payload[position - 3] & 0xff) << 16) + ((payload[position - 2] & 0xff) << 8)
				+ (payload[position - 1] & 0xff);
	}

	public int readInt() {
		position += 4;
		return ((payload[position - 4] & 0xff) << 24) + ((payload[position - 3] & 0xff) << 16)
				+ ((payload[position - 2] & 0xff) << 8) + (payload[position - 1] & 0xff);
	}

	public long readLong() {
		long l = (long) readInt() & 0xffffffffL;
		long l1 = (long) readInt() & 0xffffffffL;
		return (l << 32) + l1;
	}

	public String readString() {
		int i = position;
		while (payload[position++] != 10)
			;
		return new String(payload, i, position - i - 1);
	}

	public byte[] readStringBytes() {
		int i = position;
		while (payload[position++] != 10)
			;
		byte abyte0[] = new byte[position - i - 1];
		System.arraycopy(payload, i, abyte0, i - i, position - 1 - i);
		return abyte0;
	}

	public void readData(byte abyte0[], int j, int i) {
		for (int l = j; l < j + i; l++)
			abyte0[l] = payload[position++];
	}

	public int readSmart() {
		int i = payload[position] & 0xff;
		if (i < 128)
			return readUByte() - 64;
		else
			return readUShort() - 49152;
	}

	public int readUSmart() {
		int i = payload[position] & 0xff;
		if (i < 128)
			return readUByte();
		else
			return readUShort() - 32768;
	}

	public byte payload[];
	public int position;
}
