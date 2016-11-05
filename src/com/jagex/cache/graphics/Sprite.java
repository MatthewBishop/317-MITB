package com.jagex.cache.graphics;

import java.awt.*;
import java.awt.image.PixelGrabber;

import com.jagex.cache.Archive;
import com.jagex.io.Stream;
import com.jagex.link.Cacheable;

public final class Sprite extends Cacheable {

	public Sprite(int i, int j) {
		raster = new int[i * j];
		width = resizeWidth = i;
		height = resizeHeight = j;
		horizontalOffset = verticalOffset = 0;
	}

	public Sprite(byte abyte0[], Component component) {
		try {
			Image image = Toolkit.getDefaultToolkit().createImage(abyte0);
			MediaTracker mediatracker = new MediaTracker(component);
			mediatracker.addImage(image, 0);
			mediatracker.waitForAll();
			width = image.getWidth(component);
			height = image.getHeight(component);
			resizeWidth = width;
			resizeHeight = height;
			horizontalOffset = 0;
			verticalOffset = 0;
			raster = new int[width * height];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, width, height, raster, 0, width);
			pixelgrabber.grabPixels();
		} catch (Exception _ex) {
			System.out.println("Error converting jpg");
		}
	}

	public Sprite(Archive archive, String s, int i) {
		Stream stream = new Stream(archive.getEntry(s + ".dat"));
		Stream stream_1 = new Stream(archive.getEntry("index.dat"));
		stream_1.currentOffset = stream.readUnsignedWord();
		resizeWidth = stream_1.readUnsignedWord();
		resizeHeight = stream_1.readUnsignedWord();
		int j = stream_1.readUnsignedByte();
		int ai[] = new int[j];
		for (int k = 0; k < j - 1; k++) {
			ai[k + 1] = stream_1.read3Bytes();
			if (ai[k + 1] == 0)
				ai[k + 1] = 1;
		}

		for (int l = 0; l < i; l++) {
			stream_1.currentOffset += 2;
			stream.currentOffset += stream_1.readUnsignedWord() * stream_1.readUnsignedWord();
			stream_1.currentOffset++;
		}

		horizontalOffset = stream_1.readUnsignedByte();
		verticalOffset = stream_1.readUnsignedByte();
		width = stream_1.readUnsignedWord();
		height = stream_1.readUnsignedWord();
		int i1 = stream_1.readUnsignedByte();
		int j1 = width * height;
		raster = new int[j1];
		if (i1 == 0) {
			for (int k1 = 0; k1 < j1; k1++)
				raster[k1] = ai[stream.readUnsignedByte()];

			return;
		}
		if (i1 == 1) {
			for (int l1 = 0; l1 < width; l1++) {
				for (int i2 = 0; i2 < height; i2++)
					raster[l1 + i2 * width] = ai[stream.readUnsignedByte()];

			}

		}
	}


	public void recolour(int redOffset, int greenOffset, int blueOffset) {
		for (int i1 = 0; i1 < raster.length; i1++) {
			int j1 = raster[i1];
			if (j1 != 0) {
				int k1 = j1 >> 16 & 0xff;
				k1 += redOffset;
				if (k1 < 1)
					k1 = 1;
				else if (k1 > 255)
					k1 = 255;
				int l1 = j1 >> 8 & 0xff;
				l1 += greenOffset;
				if (l1 < 1)
					l1 = 1;
				else if (l1 > 255)
					l1 = 255;
				int i2 = j1 & 0xff;
				i2 += blueOffset;
				if (i2 < 1)
					i2 = 1;
				else if (i2 > 255)
					i2 = 255;
				raster[i1] = (k1 << 16) + (l1 << 8) + i2;
			}
		}

	}
	
	public void resize() {
		int ai[] = new int[resizeWidth * resizeHeight];
		for (int j = 0; j < height; j++) {
			System.arraycopy(raster, j * width, ai, j + verticalOffset * resizeWidth + horizontalOffset, width);
		}

		raster = ai;
		width = resizeWidth;
		height = resizeHeight;
		horizontalOffset = 0;
		verticalOffset = 0;
	}

	public int raster[];
	public int width;
	public int height;
	public int horizontalOffset;
	public int verticalOffset;
	public int resizeWidth;
	public int resizeHeight;
}
