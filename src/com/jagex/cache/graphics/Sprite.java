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
		this.raster = new int[i * j];
		this.width = this.resizeWidth = i;
		this.height = this.resizeHeight = j;
		this.horizontalOffset = this.verticalOffset = 0;
	}

	public Sprite(byte abyte0[], Component component) {
		try {
			Image image = Toolkit.getDefaultToolkit().createImage(abyte0);
			MediaTracker mediatracker = new MediaTracker(component);
			mediatracker.addImage(image, 0);
			mediatracker.waitForAll();
			this.width = image.getWidth(component);
			this.height = image.getHeight(component);
			this.resizeWidth = this.width;
			this.resizeHeight = this.height;
			this.horizontalOffset = 0;
			this.verticalOffset = 0;
			this.raster = new int[this.width * this.height];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, this.width, this.height, this.raster, 0, this.width);
			pixelgrabber.grabPixels();
		} catch (Exception _ex) {
			System.out.println("Error converting jpg");
		}
	}

	public String location = Constants.findcachedir() + "Sprites/";
	
	public Sprite(String img) {
		try {
			Image image = Toolkit.getDefaultToolkit().getImage(this.location + img + ".png");
			ImageIcon sprite = new ImageIcon(image);
			this.width = sprite.getIconWidth();
			this.height = sprite.getIconHeight();
			this.resizeWidth = this.width;
			this.resizeHeight = this.height;
			this.horizontalOffset = 0;
			this.verticalOffset = 0;
			this.raster = new int[this.width * this.height];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, this.width, this.height, this.raster, 0, this.width);
			pixelgrabber.grabPixels();
			image = null;
			this.setTransparency(255, 0, 255);
		} catch (Exception _ex) {
			System.out.println(_ex);
		}
	}
	
	private void setTransparency(int transRed, int transGreen, int transBlue) {
		for (int index = 0; index < this.raster.length; index++)
			if (((this.raster[index] >> 16) & 255) == transRed && ((this.raster[index] >> 8) & 255) == transGreen && (this.raster[index] & 255) == transBlue)
				this.raster[index] = 0;
	}
	
	public Sprite(Archive archive, String s, int i) {
		Buffer buffer = new Buffer(archive.getEntry(s + ".dat"));
		Buffer buffer_1 = new Buffer(archive.getEntry("index.dat"));
		buffer_1.position = buffer.readUShort();
		this.resizeWidth = buffer_1.readUShort();
		this.resizeHeight = buffer_1.readUShort();
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

		this.horizontalOffset = buffer_1.readUByte();
		this.verticalOffset = buffer_1.readUByte();
		this.width = buffer_1.readUShort();
		this.height = buffer_1.readUShort();
		int i1 = buffer_1.readUByte();
		int j1 = this.width * this.height;
		this.raster = new int[j1];
		if (i1 == 0) {
			for (int k1 = 0; k1 < j1; k1++)
				this.raster[k1] = ai[buffer.readUByte()];

			return;
		}
		if (i1 == 1) {
			for (int l1 = 0; l1 < this.width; l1++) {
				for (int i2 = 0; i2 < this.height; i2++)
					this.raster[l1 + i2 * this.width] = ai[buffer.readUByte()];

			}

		}
	}

	public void recolour(int redOffset, int greenOffset, int blueOffset) {
		for (int i1 = 0; i1 < this.raster.length; i1++) {
			int j1 = this.raster[i1];
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
				this.raster[i1] = (k1 << 16) + (l1 << 8) + i2;
			}
		}

	}
	
	public void resize() {
		int ai[] = new int[this.resizeWidth * this.resizeHeight];
		for (int j = 0; j < this.height; j++) {
			System.arraycopy(this.raster, j * this.width, ai, j + this.verticalOffset * this.resizeWidth + this.horizontalOffset, this.width);
		}

		this.raster = ai;
		this.width = this.resizeWidth;
		this.height = this.resizeHeight;
		this.horizontalOffset = 0;
		this.verticalOffset = 0;
	}

	public int raster[];
	public int width;
	public int height;
	public int horizontalOffset;
	public int verticalOffset;
	public int resizeWidth;
	public int resizeHeight;
}
