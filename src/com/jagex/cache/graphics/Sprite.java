package com.jagex.cache.graphics;

import java.awt.*;
import java.awt.image.PixelGrabber;

import javax.swing.ImageIcon;

import com.jagex.Constants;
import com.jagex.cache.Archive;
import com.jagex.io.Buffer;
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

	public String location = Constants.findcachedir() + "Sprites/";
	
	public Sprite(String img) {
		try {
			Image image = Toolkit.getDefaultToolkit().getImage(location + img + ".png");
			ImageIcon sprite = new ImageIcon(image);
			width = sprite.getIconWidth();
			height = sprite.getIconHeight();
			resizeWidth = width;
			resizeHeight = height;
			horizontalOffset = 0;
			verticalOffset = 0;
			raster = new int[width * height];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, width, height, raster, 0, width);
			pixelgrabber.grabPixels();
			image = null;
			setTransparency(255, 0, 255);
		} catch (Exception _ex) {
			System.out.println(_ex);
		}
	}
	
	private void setTransparency(int transRed, int transGreen, int transBlue) {
		for (int index = 0; index < raster.length; index++)
			if (((raster[index] >> 16) & 255) == transRed && ((raster[index] >> 8) & 255) == transGreen && (raster[index] & 255) == transBlue)
				raster[index] = 0;
	}
	
	public Sprite(Archive archive, String s, int i) {
		Buffer buffer = new Buffer(archive.getEntry(s + ".dat"));
		Buffer buffer_1 = new Buffer(archive.getEntry("index.dat"));
		buffer_1.position = buffer.readUShort();
		resizeWidth = buffer_1.readUShort();
		resizeHeight = buffer_1.readUShort();
		int j = buffer_1.readUByte();
		int ai[] = new int[j];
		for (int k = 0; k < j - 1; k++) {
			ai[k + 1] = buffer_1.readUTriByte();
			if (ai[k + 1] == 0)
				ai[k + 1] = 1;
		}

		for (int l = 0; l < i; l++) {
			buffer_1.position += 2;
			buffer.position += buffer_1.readUShort() * buffer_1.readUShort();
			buffer_1.position++;
		}

		horizontalOffset = buffer_1.readUByte();
		verticalOffset = buffer_1.readUByte();
		width = buffer_1.readUShort();
		height = buffer_1.readUShort();
		int i1 = buffer_1.readUByte();
		int j1 = width * height;
		raster = new int[j1];
		if (i1 == 0) {
			for (int k1 = 0; k1 < j1; k1++)
				raster[k1] = ai[buffer.readUByte()];

			return;
		}
		if (i1 == 1) {
			for (int l1 = 0; l1 < width; l1++) {
				for (int i2 = 0; i2 < height; i2++)
					raster[l1 + i2 * width] = ai[buffer.readUByte()];

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
