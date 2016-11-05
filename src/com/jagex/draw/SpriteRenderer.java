package com.jagex.draw;

import com.jagex.cache.graphics.IndexedImage;
import com.jagex.cache.graphics.Sprite;

public class SpriteRenderer {

	private static void method351(int i, int j, int ai[], int k, int ai1[], int l, int i1, int j1, int k1, int l1) {
		int j2 = 256 - k1;
		for (int k2 = -i1; k2 < 0; k2++) {
			for (int l2 = -j; l2 < 0; l2++) {
				k = ai1[i++];
				if (k != 0) {
					int i3 = ai[l1];
					ai[l1++] = ((k & 0xff00ff) * k1 + (i3 & 0xff00ff) * j2 & 0xff00ff00)
							+ ((k & 0xff00) * k1 + (i3 & 0xff00) * j2 & 0xff0000) >> 8;
				} else {
					l1++;
				}
			}
	
			l1 += j1;
			i += l;
		}
	}

	public static void drawSprite1(Sprite sprite, int x, int y, int alpha) {
		x += sprite.horizontalOffset;
		y += sprite.verticalOffset;
		int i1 = x + y * DrawingArea.width;
		int j1 = 0;
		int k1 = sprite.height;
		int l1 = sprite.width;
		int i2 = DrawingArea.width - l1;
		int j2 = 0;
		if (y < DrawingArea.topY) {
			int k2 = DrawingArea.topY - y;
			k1 -= k2;
			y = DrawingArea.topY;
			j1 += k2 * l1;
			i1 += k2 * DrawingArea.width;
		}
		if (y + k1 > DrawingArea.bottomY)
			k1 -= (y + k1) - DrawingArea.bottomY;
		if (x < DrawingArea.topX) {
			int l2 = DrawingArea.topX - x;
			l1 -= l2;
			x = DrawingArea.topX;
			j1 += l2;
			i1 += l2;
			j2 += l2;
			i2 += l2;
		}
		if (x + l1 > DrawingArea.bottomX) {
			int i3 = (x + l1) - DrawingArea.bottomX;
			l1 -= i3;
			j2 += i3;
			i2 += i3;
		}
		if (!(l1 <= 0 || k1 <= 0)) {
			method351(j1, l1, DrawingArea.pixels, 0, sprite.raster, j2, k1, i2, alpha, i1);
		}
	}

	public static void draw(Sprite sprite, int y, int theta, int ai[], int k, int ai1[], int i1, int j1, int k1, int x, int i2) {
		try {
			int cx = -x / 2;
			int cy = -y / 2;
			int sin = (int) (Math.sin((double) theta / 326.11000000000001D) * 65536D);
			int cos = (int) (Math.cos((double) theta / 326.11000000000001D) * 65536D);
			sin = sin * k >> 8;
			cos = cos * k >> 8;
			int j3 = (i2 << 16) + (cy * sin + cx * cos);
			int k3 = (i1 << 16) + (cy * cos - cx * sin);
			int l3 = k1 + j1 * DrawingArea.width;
			for (j1 = 0; j1 < y; j1++) {
				int i4 = ai1[j1];
				int j4 = l3 + i4;
				int k4 = j3 + cos * i4;
				int l4 = k3 - sin * i4;
				for (k1 = -ai[j1]; k1 < 0; k1++) {
					DrawingArea.pixels[j4++] = sprite.raster[(k4 >> 16) + (l4 >> 16) * sprite.width];
					k4 += cos;
					l4 -= sin;
				}
	
				j3 += sin;
				k3 += cos;
				l3 += DrawingArea.width;
			}
	
		} catch (Exception _ex) {
		}
	}

	/**
	 * In most 317s, they just have y, theta, and x as params.
	 */
	public static void method353(Sprite sprite, int x, int y, int width, int height, double theta, int j, int l, int j1) {
		try {
			int midX = -width / 2;
			int midY = -height / 2;
			int sin = (int) (Math.sin(theta) * 65536D);
			int cos = (int) (Math.cos(theta) * 65536D);
			sin = sin * j1 >> 8;
			cos = cos * j1 >> 8;
			int i3 = (l << 16) + midY * sin + midX * cos;
			int j3 = (j << 16) + midY * cos - midX * sin;
			int destOffset = x + y * DrawingArea.width;
	
			for (y = 0; y < height; y++) {
				int destIndex = destOffset;
				int i4 = i3;
				int j4 = j3;
	
				for (x = -width; x < 0; x++) {
					int colour = sprite.raster[(i4 >> 16) + (j4 >> 16) * sprite.width];
					if (colour != 0) {
						DrawingArea.pixels[destIndex++] = colour;
					} else {
						destIndex++;
					}
	
					i4 += cos;
					j4 -= sin;
				}
	
				i3 += sin;
				j3 += cos;
				destOffset += DrawingArea.width;
			}
		} catch (Exception ex) {
		}
	}

	public static void method354(Sprite sprite, IndexedImage indexedImage, int i, int j) {
		j += sprite.horizontalOffset;
		i += sprite.verticalOffset;
		int k = j + i * DrawingArea.width;
		int l = 0;
		int i1 = sprite.height;
		int j1 = sprite.width;
		int k1 = DrawingArea.width - j1;
		int l1 = 0;
		if (i < DrawingArea.topY) {
			int i2 = DrawingArea.topY - i;
			i1 -= i2;
			i = DrawingArea.topY;
			l += i2 * j1;
			k += i2 * DrawingArea.width;
		}
		if (i + i1 > DrawingArea.bottomY)
			i1 -= (i + i1) - DrawingArea.bottomY;
		if (j < DrawingArea.topX) {
			int j2 = DrawingArea.topX - j;
			j1 -= j2;
			j = DrawingArea.topX;
			l += j2;
			k += j2;
			l1 += j2;
			k1 += j2;
		}
		if (j + j1 > DrawingArea.bottomX) {
			int k2 = (j + j1) - DrawingArea.bottomX;
			j1 -= k2;
			l1 += k2;
			k1 += k2;
		}
		if (!(j1 <= 0 || i1 <= 0)) {
			method355(sprite.raster, j1, indexedImage.raster, i1, DrawingArea.pixels, 0, k1, k, l1, l);
		}
	}

	private static void method355(int ai[], int i, byte abyte0[], int j, int ai1[], int k, int l, int i1, int j1, int k1) {
		int l1 = -(i >> 2);
		i = -(i & 3);
		for (int j2 = -j; j2 < 0; j2++) {
			for (int k2 = l1; k2 < 0; k2++) {
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
			}
	
			for (int l2 = i; l2 < 0; l2++) {
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
			}
	
			i1 += l;
			k1 += j1;
		}
	
	}

	public static void drawSprite(Sprite sprite, int x, int y) {
		x += sprite.horizontalOffset;
		y += sprite.verticalOffset;
		int l = x + y * DrawingArea.width;
		int i1 = 0;
		int j1 = sprite.height;
		int k1 = sprite.width;
		int l1 = DrawingArea.width - k1;
		int i2 = 0;
		if (y < DrawingArea.topY) {
			int j2 = DrawingArea.topY - y;
			j1 -= j2;
			y = DrawingArea.topY;
			i1 += j2 * k1;
			l += j2 * DrawingArea.width;
		}
		if (y + j1 > DrawingArea.bottomY)
			j1 -= (y + j1) - DrawingArea.bottomY;
		if (x < DrawingArea.topX) {
			int k2 = DrawingArea.topX - x;
			k1 -= k2;
			x = DrawingArea.topX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if (x + k1 > DrawingArea.bottomX) {
			int l2 = (x + k1) - DrawingArea.bottomX;
			k1 -= l2;
			i2 += l2;
			l1 += l2;
		}
		if (!(k1 <= 0 || j1 <= 0)) {
			method349(DrawingArea.pixels, sprite.raster, 0, i1, l, k1, j1, l1, i2);
		}
	}
	
	private static void method349(int ai[], int ai1[], int i, int j, int k, int l, int i1, int j1, int k1) {
		int l1 = -(l >> 2);
		l = -(l & 3);
		for (int i2 = -i1; i2 < 0; i2++) {
			for (int j2 = l1; j2 < 0; j2++) {
				i = ai1[j++];
				if (i != 0)
					ai[k++] = i;
				else
					k++;
				i = ai1[j++];
				if (i != 0)
					ai[k++] = i;
				else
					k++;
				i = ai1[j++];
				if (i != 0)
					ai[k++] = i;
				else
					k++;
				i = ai1[j++];
				if (i != 0)
					ai[k++] = i;
				else
					k++;
			}

			for (int k2 = l; k2 < 0; k2++) {
				i = ai1[j++];
				if (i != 0)
					ai[k++] = i;
				else
					k++;
			}

			k += j1;
			j += k1;
		}

	}

	public static void drawOpaque(Sprite sprite, int x, int y) {
		x += sprite.horizontalOffset;
		y += sprite.verticalOffset;
		int l = x + y * DrawingArea.width;
		int i1 = 0;
		int j1 = sprite.height;
		int k1 = sprite.width;
		int l1 = DrawingArea.width - k1;
		int i2 = 0;
		if (y < DrawingArea.topY) {
			int j2 = DrawingArea.topY - y;
			j1 -= j2;
			y = DrawingArea.topY;
			i1 += j2 * k1;
			l += j2 * DrawingArea.width;
		}
		if (y + j1 > DrawingArea.bottomY)
			j1 -= (y + j1) - DrawingArea.bottomY;
		if (x < DrawingArea.topX) {
			int k2 = DrawingArea.topX - x;
			k1 -= k2;
			x = DrawingArea.topX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if (x + k1 > DrawingArea.bottomX) {
			int l2 = (x + k1) - DrawingArea.bottomX;
			k1 -= l2;
			i2 += l2;
			l1 += l2;
		}
		if (k1 <= 0 || j1 <= 0) {
		} else {
			method347(l, k1, j1, i2, i1, l1, sprite.raster, DrawingArea.pixels);
		}
	}

	private static void method347(int i, int j, int k, int l, int i1, int k1, int ai[], int ai1[]) {
		int l1 = -(j >> 2);
		j = -(j & 3);
		for (int i2 = -k; i2 < 0; i2++) {
			for (int j2 = l1; j2 < 0; j2++) {
				ai1[i++] = ai[i1++];
				ai1[i++] = ai[i1++];
				ai1[i++] = ai[i1++];
				ai1[i++] = ai[i1++];
			}
	
			for (int k2 = j; k2 < 0; k2++)
				ai1[i++] = ai[i1++];
	
			i += k1;
			i1 += l;
		}
	}

	public static void initRaster(Sprite sprite) {
		DrawingArea.initDrawingArea(sprite.height, sprite.width, sprite.raster);
	}
}
