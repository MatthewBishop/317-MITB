package com.jagex;

import com.jagex.cache.graphics.IndexedImage;
import com.jagex.cache.graphics.Sprite;
import com.jagex.draw.render.SpriteRenderer;
import com.jagex.entity.model.Model;

public class Minimap {

	static final int[] compassClipStarts = new int[33];
	static final int[] compassClipLengths = new int[33];
	static final int[] minimapLineLengths = new int[152];
	static final int[] minimapLineStarts = new int[152];
	public static void prepare(IndexedImage mapBack) {

        System.out.println(mapBack.height);
        System.out.println(mapBack.width);
        System.out.println(mapBack.drawOffsetX);
        System.out.println(mapBack.drawOffsetY);
		for (int j6 = 0; j6 < 33; j6++) {
			int k6 = 999;
			int i7 = 0;
			for (int k7 = 0; k7 < 34; k7++) {
				if (mapBack.raster[k7 + j6 * mapBack.width] == 0) {
					if (k6 == 999)
						k6 = k7;
					continue;
				}
				if (k6 == 999)
					continue;
				i7 = k7;
				break;
			}
			Minimap.compassClipStarts[j6] = k6;
			Minimap.compassClipLengths[j6] = i7 - k6;
		}
		for (int l6 = 1; l6 < 153; l6++) {
			int j7 = 999;
			int l7 = 0;
			for (int j8 = 24; j8 < 177; j8++) {
				if (mapBack.raster[j8 + l6 * mapBack.width] == 0 && (j8 > 34 || l6 > 34)) {
					if (j7 == 999) {
						j7 = j8;
					}
					continue;
				}
				if (j7 == 999) {
					continue;
				}
				l7 = j8;
				break;
			}
			Minimap.minimapLineStarts[l6 - 1] = j7 - 24;
			Minimap.minimapLineLengths[l6 - 1] = l7 - j7;
		}
	}
	static void drawOntoMinimap(Sprite sprite, int x, int y, int minimapInt1, int minimapInt2, int minimapInt3)
	    {
	        int k = minimapInt1 + minimapInt2 & 0x7ff;
	        int l = x * x + y * y;
	        if(l > 6400)
	            return;
	        int i1 = Model.SINE[k];
	        int j1 = Model.COSINE[k];
	        i1 = (i1 * 256) / (minimapInt3 + 256);
	        j1 = (j1 * 256) / (minimapInt3 + 256);
	        int k1 = y * i1 + x * j1 >> 16;
	        int l1 = y * j1 - x * i1 >> 16;
	    //    if(l > 2500)
	      //  {
	         //   SpriteRenderer.method354(sprite, mapBack, 85 - l1 - sprite.resizeHeight / 2 - 4, ((77 + k1) - sprite.resizeWidth / 2) + 4 + (Client.frameWidth - 167));
	      //  } else
	        {
	            SpriteRenderer.drawSprite(sprite, ((77 + k1) - sprite.resizeWidth / 2) + 4 + (Client.frameWidth - 167), 85 - l1 - sprite.resizeHeight / 2 - 4);
	        }
	        
	/*		if (Client.frameMode == Client.ScreenMode.FIXED) {
				SpriteRenderer.drawSprite(sprite, ((94 + k1) - sprite.resizeWidth / 2) + 4 + 30, 83 - l1 - sprite.resizeHeight / 2 - 4 + 5);
			} else {
				SpriteRenderer.drawSprite(sprite, ((77 + k1) - sprite.resizeWidth / 2) + 4 + (Client.frameWidth - 167), 85 - l1 - sprite.resizeHeight / 2 - 4);
			}*/
	    }
	static void method81(Sprite sprite, int j, int k, int minimapInt1, int minimapInt2, int minimapInt3)
	{
	    int l = k * k + j * j;
	    if(l > 4225 && l < 0x15f90)
	    {
	   /*     int i1 = minimapInt1 + minimapInt2 & 0x7ff;
	        int j1 = Model.SINE[i1];
	        int k1 = Model.COSINE[i1];
	        j1 = (j1 * 256) / (minimapInt3 + 256);
	        k1 = (k1 * 256) / (minimapInt3 + 256);
	        int l1 = j * j1 + k * k1 >> 16;
	        int i2 = j * k1 - k * j1 >> 16;
	        double d = Math.atan2(l1, i2);
	        int j2 = (int)(Math.sin(d) * 63D);
	        int k2 = (int)(Math.cos(d) * 57D);
	        SpriteRenderer.method353(mapEdge, 94 + j2 + 4 - 10, 83 - k2 - 20, 20, 20, d, 15, 15, 256);*/
	    } else
	    {
	        Minimap.drawOntoMinimap(sprite, k, j, minimapInt1, minimapInt2, minimapInt3);
	    }
	}
}
