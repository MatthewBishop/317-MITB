package com.jagex.cache;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.

// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.io.Stream;

public final class Archive {

	public Archive(byte data[]) {
		Stream stream = new Stream(data);
		int length = stream.read3Bytes();
		int decompressedLength = stream.read3Bytes();
		if (decompressedLength != length) {
			byte output[] = new byte[length];
			BZip2Decompressor.decompress(output, length, data, decompressedLength, 6);
			buffer = output;
			stream = new Stream(buffer);
			extracted = true;
		} else {
			buffer = data;
			extracted = false;
		}
		entries = stream.readUnsignedWord();
		identifiers = new int[entries];
		extractedSizes = new int[entries];
		sizes = new int[entries];
		indices = new int[entries];
		int offset = stream.currentOffset + entries * 10;
		for (int file = 0; file < entries; file++) {
			identifiers[file] = stream.readDWord();
			extractedSizes[file] = stream.read3Bytes();
			sizes[file] = stream.read3Bytes();
			indices[file] = offset;
			offset += sizes[file];
		}
	}

	public byte[] getEntry(String s) {
		int hash = 0;
		s = s.toUpperCase();
		for (int j = 0; j < s.length(); j++)
			hash = (hash * 61 + s.charAt(j)) - 32;

		for (int k = 0; k < entries; k++)
			if (identifiers[k] == hash) {
				byte output[] = new byte[extractedSizes[k]];
				if (!extracted) {
					BZip2Decompressor.decompress(output, extractedSizes[k], buffer, sizes[k], indices[k]);
				} else {
					System.arraycopy(buffer, indices[k], output, 0, extractedSizes[k]);

				}
				return output;
			}

		return null;
	}

	private final byte[] buffer;
	private final int entries;
	private final int[] identifiers;
	private final int[] extractedSizes;
	private final int[] sizes;
	private final int[] indices;
	private final boolean extracted;
}
