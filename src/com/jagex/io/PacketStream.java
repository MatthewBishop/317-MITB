package com.jagex.io;

import java.math.BigInteger;

import com.jagex.Constants;
import com.jagex.net.ISAACRandomGen;

public class PacketStream extends Stream {

	private static final boolean ENABLE_RSA = false;
	private static final BigInteger RSA_EXPONENT = new BigInteger("65537");
	private static final BigInteger RSA_MODULUS = new BigInteger(
			"143690958001225849100503496893758066948984921380482659564113596152800934352119496873386875214251264258425208995167316497331786595942754290983849878549630226741961610780416197036711585670124061149988186026407785250364328460839202438651793652051153157765358767514800252431284681765433239888090564804146588087023");

	
	
	public PacketStream(byte[] abyte0) {
		super(abyte0);
	}


    public void createFrame(int i)
    {
        buffer[currentOffset++] = (byte)(i + encryption.getNextKey());
    }
    

    public ISAACRandomGen encryption;
    
    public void encodeRSA()
    {
		int length = currentOffset;
		currentOffset = 0;
		byte[] buffer = new byte[length];
		readBytes(length, 0, buffer);
		byte[] rsa = buffer;

		if (ENABLE_RSA) {
			rsa = new BigInteger(buffer).modPow(RSA_EXPONENT, RSA_MODULUS).toByteArray();
		}

		currentOffset = 0;
        writeWordBigEndian(rsa.length);
        writeBytes(rsa, rsa.length, 0);
    }
    

    public void writeDWordBigEndian(int i)
    {
        buffer[currentOffset++] = (byte)(i >> 16);
        buffer[currentOffset++] = (byte)(i >> 8);
        buffer[currentOffset++] = (byte)i;
    }



    public void writeQWord(long l)
    {
            buffer[currentOffset++] = (byte)(int)(l >> 56);
            buffer[currentOffset++] = (byte)(int)(l >> 48);
            buffer[currentOffset++] = (byte)(int)(l >> 40);
            buffer[currentOffset++] = (byte)(int)(l >> 32);
            buffer[currentOffset++] = (byte)(int)(l >> 24);
            buffer[currentOffset++] = (byte)(int)(l >> 16);
            buffer[currentOffset++] = (byte)(int)(l >> 8);
            buffer[currentOffset++] = (byte)(int)l;
    }

    public void writeString(String s)
    {
        //s.getBytes(0, s.length(), buffer, currentOffset);    //deprecated
        System.arraycopy(s.getBytes(), 0, buffer, currentOffset, s.length());
        currentOffset += s.length();
        buffer[currentOffset++] = 10;
    }



    public void writeBytes(int i)
    {
        buffer[currentOffset - i - 1] = (byte)i;
    }
    

    public void method431(int i)
    {
        buffer[currentOffset++] = (byte)i;
        buffer[currentOffset++] = (byte)(i >> 8);
    }

    public void method432(int j)
    {
        buffer[currentOffset++] = (byte)(j >> 8);
        buffer[currentOffset++] = (byte)(j + 128);
    }

    public void method433(int j)
    {
        buffer[currentOffset++] = (byte)(j + 128);
        buffer[currentOffset++] = (byte)(j >> 8);
    }


    public void method441(int i, byte abyte0[], int j)
    {
        for(int k = (i + j) - 1; k >= i; k--)
            buffer[currentOffset++] = (byte)(abyte0[k] + 128);

    }

    public void method424(int i)
    {
        buffer[currentOffset++] = (byte)(-i);
    }

    public void method425(int j)
    {
        buffer[currentOffset++] = (byte)(128 - j);
    }
    
	public void method400(int i) {
		buffer[currentOffset++] = (byte) i;
		buffer[currentOffset++] = (byte) (i >> 8);
	}

	
	public void method403(int j) {
		buffer[currentOffset++] = (byte) j;
		buffer[currentOffset++] = (byte) (j >> 8);
		buffer[currentOffset++] = (byte) (j >> 16);
		buffer[currentOffset++] = (byte) (j >> 24);
	}
    
	public void writeWordBigEndian(int i) {
		buffer[currentOffset++] = (byte) i;
	}

	public void writeWord(int i) {
		buffer[currentOffset++] = (byte) (i >> 8);
		buffer[currentOffset++] = (byte) i;
	}


	public void writeDWord(int i) {
		buffer[currentOffset++] = (byte) (i >> 24);
		buffer[currentOffset++] = (byte) (i >> 16);
		buffer[currentOffset++] = (byte) (i >> 8);
		buffer[currentOffset++] = (byte) i;
	}



	public void writeBytes(byte abyte0[], int i, int j) {
		for (int k = j; k < j + i; k++)
			buffer[currentOffset++] = abyte0[k];

	}

    
}
