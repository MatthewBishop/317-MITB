package com.jagex.cache;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.

// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.io.Buffer;

public final class Archive {

	public Archive(byte data[]) {
		Buffer stream = new Buffer(data);
		int length = stream.readUTriByte();
		int decompressedLength = stream.readUTriByte();
		if (decompressedLength != length) {
			byte output[] = new byte[length];
			BZip2Decompressor.decompress(output, length, data, decompressedLength, 6);
			buffer = output;
			stream = new Buffer(buffer);
			extracted = true;
		} else {
			buffer = data;
			extracted = false;
		}
		entries = stream.readUShort();
		identifiers = new int[entries];
		extractedSizes = new int[entries];
		sizes = new int[entries];
		indices = new int[entries];
		int offset = stream.position + entries * 10;
		for (int file = 0; file < entries; file++) {
			identifiers[file] = stream.readInt();
			extractedSizes[file] = stream.readUTriByte();
			sizes[file] = stream.readUTriByte();
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
