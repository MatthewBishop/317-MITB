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
			this.buffer = output;
			stream = new Buffer(this.buffer);
			this.extracted = true;
		} else {
			this.buffer = data;
			this.extracted = false;
		}
		this.entries = stream.readUShort();
		this.identifiers = new int[this.entries];
		this.extractedSizes = new int[this.entries];
		this.sizes = new int[this.entries];
		this.indices = new int[this.entries];
		int offset = stream.position + this.entries * 10;
		for (int file = 0; file < this.entries; file++) {
			this.identifiers[file] = stream.readInt();
			this.extractedSizes[file] = stream.readUTriByte();
			this.sizes[file] = stream.readUTriByte();
			this.indices[file] = offset;
			offset += this.sizes[file];
		}
	}

	public byte[] getEntry(String s) {
		int hash = 0;
		s = s.toUpperCase();
		for (int j = 0; j < s.length(); j++)
			hash = (hash * 61 + s.charAt(j)) - 32;

		for (int k = 0; k < this.entries; k++)
			if (this.identifiers[k] == hash) {
				byte output[] = new byte[this.extractedSizes[k]];
				if (!this.extracted) {
					BZip2Decompressor.decompress(output, this.extractedSizes[k], this.buffer, this.sizes[k], this.indices[k]);
				} else {
					System.arraycopy(this.buffer, this.indices[k], output, 0, this.extractedSizes[k]);

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
