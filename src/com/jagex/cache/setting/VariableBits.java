package com.jagex.cache.setting;

import com.jagex.cache.Archive;
import com.jagex.io.Buffer;

public final class VariableBits {

	private static VariableBits bits[];

	private static int BIT_MASKS[];
	
	static {
        VariableBits.BIT_MASKS = new int[32];
        int i = 2;
        for(int k = 0; k < 32; k++)
        {
            VariableBits.BIT_MASKS[k] = i - 1;
            i += i;
        }
	}

	public static int get(int index, int[] settings) {
        VariableBits variableBits = VariableBits.bits[index];
        int setting = variableBits.setting;
        int low = variableBits.low;
        int high = variableBits.high;
        int mask = VariableBits.BIT_MASKS[high - low];
        return settings[setting] >> low & mask;
	}
	
	public static void unpackConfig(Archive archive) {
		Buffer buffer = new Buffer(archive.getEntry("varbit.dat"));
		int cacheSize = buffer.readUShort();
		if (VariableBits.bits == null)
			VariableBits.bits = new VariableBits[cacheSize];
		for (int j = 0; j < cacheSize; j++) {
			if (VariableBits.bits[j] == null)
				VariableBits.bits[j] = new VariableBits();
			VariableBits.bits[j].readValues(buffer);
		}

		if (buffer.position != buffer.payload.length)
			System.out.println("varbit load mismatch");
	}

	private int setting;
	private int low;
	private int high;
	
	private void readValues(Buffer buffer) {
		do {
			int j = buffer.readUByte();
			if (j == 0)
				return;
			if (j == 1) {
				this.setting = buffer.readUShort();
				this.low = buffer.readUByte();
				this.high = buffer.readUByte();
			} else if (j == 10)
				buffer.readString();
			else if (j == 3 || j == 4)
				buffer.readInt();
			else if (j != 2)
				System.out.println("Error unrecognised config code: " + j);
		} while (true);
	}

}
