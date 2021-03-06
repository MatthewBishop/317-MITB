package com.jagex.io;

import java.math.BigInteger;

import com.jagex.net.ISAACRandomGen;

public class PacketStream extends Buffer {

	private static final boolean ENABLE_RSA = false;
	private static final BigInteger RSA_EXPONENT = new BigInteger("65537");
	private static final BigInteger RSA_MODULUS = new BigInteger(
			"143690958001225849100503496893758066948984921380482659564113596152800934352119496873386875214251264258425208995167316497331786595942754290983849878549630226741961610780416197036711585670124061149988186026407785250364328460839202438651793652051153157765358767514800252431284681765433239888090564804146588087023");

	public PacketStream(byte[] abyte0) {
		super(abyte0);
	}

	public void writeOpcode(int i) {
		this.payload[this.position++] = (byte) (i + this.encryption.getNextKey());
	}

	public ISAACRandomGen encryption;

	public void encodeRSA() {
		int length = this.position;
		this.position = 0;
		byte[] buffer = new byte[length];
		this.readData(buffer, 0, length);
		byte[] rsa = buffer;

		if (PacketStream.ENABLE_RSA) {
			rsa = new BigInteger(buffer).modPow(PacketStream.RSA_EXPONENT, PacketStream.RSA_MODULUS).toByteArray();
		}

		this.position = 0;
		this.writeByte(rsa.length);
		this.writeBytes(rsa, 0, rsa.length);
	}

	public void writeTriByte(int i) {
		this.payload[this.position++] = (byte) (i >> 16);
		this.payload[this.position++] = (byte) (i >> 8);
		this.payload[this.position++] = (byte) i;
	}

	public void writeLong(long l) {
		this.payload[this.position++] = (byte) (int) (l >> 56);
		this.payload[this.position++] = (byte) (int) (l >> 48);
		this.payload[this.position++] = (byte) (int) (l >> 40);
		this.payload[this.position++] = (byte) (int) (l >> 32);
		this.payload[this.position++] = (byte) (int) (l >> 24);
		this.payload[this.position++] = (byte) (int) (l >> 16);
		this.payload[this.position++] = (byte) (int) (l >> 8);
		this.payload[this.position++] = (byte) (int) l;
	}

	public void writeJString(String s) {
		// s.getBytes(0, s.length(), buffer, currentOffset); //deprecated
		System.arraycopy(s.getBytes(), 0, this.payload, this.position, s.length());
		this.position += s.length();
		this.payload[this.position++] = 10;
	}

	public void writeSizeByte(int i) {
		this.payload[this.position - i - 1] = (byte) i;
	}



	public void writeShortA(int j) {
		this.payload[this.position++] = (byte) (j >> 8);
		this.payload[this.position++] = (byte) (j + 128);
	}

	public void writeLEShortA(int j) {
		this.payload[this.position++] = (byte) (j + 128);
		this.payload[this.position++] = (byte) (j >> 8);
	}

	public void writeReverseDataA(int i, byte abyte0[], int j) {
		for (int k = (i + j) - 1; k >= i; k--)
			this.payload[this.position++] = (byte) (abyte0[k] + 128);

	}

	public void writeNegatedByte(int i) {
		this.payload[this.position++] = (byte) (-i);
	}

	public void writeByteS(int j) {
		this.payload[this.position++] = (byte) (128 - j);
	}

	public void writeLEShort(int i) {
		this.payload[this.position++] = (byte) i;
		this.payload[this.position++] = (byte) (i >> 8);
	}
	
	public void writeLEShort2(int i) {
		this.payload[this.position++] = (byte) i;
		this.payload[this.position++] = (byte) (i >> 8);
	}

	public void writeLEInt(int j) {
		this.payload[this.position++] = (byte) j;
		this.payload[this.position++] = (byte) (j >> 8);
		this.payload[this.position++] = (byte) (j >> 16);
		this.payload[this.position++] = (byte) (j >> 24);
	}

	public void writeByte(int i) {
		this.payload[this.position++] = (byte) i;
	}

	public void writeShort(int i) {
		this.payload[this.position++] = (byte) (i >> 8);
		this.payload[this.position++] = (byte) i;
	}

	public void writeInt(int i) {
		this.payload[this.position++] = (byte) (i >> 24);
		this.payload[this.position++] = (byte) (i >> 16);
		this.payload[this.position++] = (byte) (i >> 8);
		this.payload[this.position++] = (byte) i;
	}

	public void writeBytes(byte abyte0[], int j, int i) {
		for (int k = j; k < j + i; k++)
			this.payload[this.position++] = abyte0[k];

	}

}
