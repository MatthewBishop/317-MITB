package com.jagex.draw.render;

import com.jagex.cache.graphics.IndexedImage;
import com.jagex.draw.DrawingArea;

public class IndexedImageRenderer {

	public static void draw(IndexedImage image, int x, int y)
	{
	    x += image.drawOffsetX;
	    y += image.drawOffsetY;
	    int l = x + y * DrawingArea.width;
	    int i1 = 0;
	    int j1 = image.height;
	    int k1 = image.width;
	    int l1 = DrawingArea.width - k1;
	    int i2 = 0;
	    if(y < DrawingArea.topY)
	    {
	        int j2 = DrawingArea.topY - y;
	        j1 -= j2;
	        y = DrawingArea.topY;
	        i1 += j2 * k1;
	        l += j2 * DrawingArea.width;
	    }
	    if(y + j1 > DrawingArea.bottomY)
	        j1 -= (y + j1) - DrawingArea.bottomY;
	    if(x < DrawingArea.topX)
	    {
	        int k2 = DrawingArea.topX - x;
	        k1 -= k2;
	        x = DrawingArea.topX;
	        i1 += k2;
	        l += k2;
	        i2 += k2;
	        l1 += k2;
	    }
	    if(x + k1 > DrawingArea.bottomX)
	    {
	        int l2 = (x + k1) - DrawingArea.bottomX;
	        k1 -= l2;
	        i2 += l2;
	        l1 += l2;
	    }
	    if(!(k1 <= 0 || j1 <= 0))
	    {
	        IndexedImageRenderer.draw(j1, DrawingArea.pixels, image.raster, l1, l, k1, i1, image.palette, i2);
	    }
	}

	private static void draw(int i, int ai[], byte abyte0[], int j, int k, int l,
	                       int i1, int ai1[], int j1)
	{
	    int k1 = -(l >> 2);
	    l = -(l & 3);
	    for(int l1 = -i; l1 < 0; l1++)
	    {
	        for(int i2 = k1; i2 < 0; i2++)
	        {
	            byte byte1 = abyte0[i1++];
	            if(byte1 != 0)
	                ai[k++] = ai1[byte1 & 0xff];
	            else
	                k++;
	            byte1 = abyte0[i1++];
	            if(byte1 != 0)
	                ai[k++] = ai1[byte1 & 0xff];
	            else
	                k++;
	            byte1 = abyte0[i1++];
	            if(byte1 != 0)
	                ai[k++] = ai1[byte1 & 0xff];
	            else
	                k++;
	            byte1 = abyte0[i1++];
	            if(byte1 != 0)
	                ai[k++] = ai1[byte1 & 0xff];
	            else
	                k++;
	        }
	
	        for(int j2 = l; j2 < 0; j2++)
	        {
	            byte byte2 = abyte0[i1++];
	            if(byte2 != 0)
	                ai[k++] = ai1[byte2 & 0xff];
	            else
	                k++;
	        }
	
	        k += j;
	        i1 += j1;
	    }
	
	}

}
