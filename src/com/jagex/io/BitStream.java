package com.jagex.io;

public class BitStream extends Stream {

	public BitStream(byte[] abyte0) {
		super(abyte0);
	}

    public int bitPosition;
    private static final int[] anIntArray1409 = {
        0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 
        1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff, 0x3ffff, 0x7ffff, 
        0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 
        0x3fffffff, 0x7fffffff, -1
    };
    
    public void initBitAccess()
    {
        bitPosition = currentOffset * 8;
    }

    public int readBits(int i)
    {
        int k = bitPosition >> 3;
        int l = 8 - (bitPosition & 7);
        int i1 = 0;
        bitPosition += i;
        for(; i > l; l = 8)
        {
            i1 += (buffer[k++] & anIntArray1409[l]) << i - l;
            i -= l;
        }
        if(i == l)
            i1 += buffer[k] & anIntArray1409[l];
        else
            i1 += buffer[k] >> l - i & anIntArray1409[i];
        return i1;
    }

    public void finishBitAccess()
    {
        currentOffset = (bitPosition + 7) / 8;
    }
    

    public int method434()
    {
        currentOffset += 2;
            return ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
    }

    public int method435()
    {
        currentOffset += 2;
        return ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] - 128 & 0xff);
    }

    public int method436()
    {
        currentOffset += 2;
        return ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] - 128 & 0xff);
    }
    

    public int method437()
    {
        currentOffset += 2;
        int j = ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
        if(j > 32767)
            j -= 0x10000;
        return j;
    }

    public int method438()
    {
        currentOffset += 2;
        int j = ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] - 128 & 0xff);
        if(j > 32767)
            j -= 0x10000;
        return j;
    }

    public int method440()
    {
        currentOffset += 4;
        return ((buffer[currentOffset - 3] & 0xff) << 24) + ((buffer[currentOffset - 4] & 0xff) << 16) + ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
    }
    
    public int method439()
    {
            currentOffset += 4;
            return ((buffer[currentOffset - 2] & 0xff) << 24) + ((buffer[currentOffset - 1] & 0xff) << 16) + ((buffer[currentOffset - 4] & 0xff) << 8) + (buffer[currentOffset - 3] & 0xff);
    }
    
    public int method426()
    {
            return buffer[currentOffset++] - 128 & 0xff;
    }

    public int method427()
    {
        return -buffer[currentOffset++] & 0xff;
    }

    public int method428()
    {
        return 128 - buffer[currentOffset++] & 0xff;
    }

    public byte method429()
    {
            return (byte)(-buffer[currentOffset++]);
    }

    public byte method430()
    {
        return (byte)(128 - buffer[currentOffset++]);
    }
    

	public void method442(int i, int j, byte abyte0[]) {
		for (int k = (j + i) - 1; k >= j; k--)
			abyte0[k] = buffer[currentOffset++];

	}
}
