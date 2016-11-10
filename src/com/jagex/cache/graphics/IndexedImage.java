package com.jagex.cache.graphics;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.cache.Archive;
import com.jagex.io.Buffer;
import com.jagex.link.Cacheable;

public final class IndexedImage extends Cacheable {

    public IndexedImage(Archive archive, String s, int i)
    {
        Buffer buffer = new Buffer(archive.getEntry(s + ".dat"));
        Buffer buffer_1 = new Buffer(archive.getEntry("index.dat"));
        buffer_1.position = buffer.readUShort();
        resizeWidth = buffer_1.readUShort();
        resizeHeight = buffer_1.readUShort();
        int j = buffer_1.readUByte();
        palette = new int[j];
        for(int k = 0; k < j - 1; k++)
            palette[k + 1] = buffer_1.readUTriByte();

        for(int l = 0; l < i; l++)
        {
            buffer_1.position += 2;
            buffer.position += buffer_1.readUShort() * buffer_1.readUShort();
            buffer_1.position++;
        }

        drawOffsetX = buffer_1.readUByte();
        drawOffsetY = buffer_1.readUByte();
        width = buffer_1.readUShort();
        height = buffer_1.readUShort();
        int i1 = buffer_1.readUByte();
        int j1 = width * height;
        raster = new byte[j1];
        if(i1 == 0)
        {
            for(int k1 = 0; k1 < j1; k1++)
                raster[k1] = buffer.readByte();

            return;
        }
        if(i1 == 1)
        {
            for(int l1 = 0; l1 < width; l1++)
            {
                for(int i2 = 0; i2 < height; i2++)
                    raster[l1 + i2 * width] = buffer.readByte();

            }

        }
    }

    public void downscale()
    {
        resizeWidth /= 2;
        resizeHeight /= 2;
        byte abyte0[] = new byte[resizeWidth * resizeHeight];
        int i = 0;
        for(int j = 0; j < height; j++)
        {
            for(int k = 0; k < width; k++)
                abyte0[(k + drawOffsetX >> 1) + (j + drawOffsetY >> 1) * resizeWidth] = raster[i++];

        }

        raster = abyte0;
        width = resizeWidth;
        height = resizeHeight;
        drawOffsetX = 0;
            drawOffsetY = 0;
    }

    public void resize()
    {
        if(width == resizeWidth && height == resizeHeight)
            return;
        byte abyte0[] = new byte[resizeWidth * resizeHeight];
        int i = 0;
        for(int j = 0; j < height; j++)
        {
            for(int k = 0; k < width; k++)
                abyte0[k + drawOffsetX + (j + drawOffsetY) * resizeWidth] = raster[i++];

        }

        raster = abyte0;
        width = resizeWidth;
        height = resizeHeight;
        drawOffsetX = 0;
        drawOffsetY = 0;
    }

    public void flipHorizontally()
    {
        byte abyte0[] = new byte[width * height];
        int j = 0;
        for(int k = 0; k < height; k++)
        {
            for(int l = width - 1; l >= 0; l--)
                abyte0[j++] = raster[l + k * width];

        }

        raster = abyte0;
        drawOffsetX = resizeWidth - width - drawOffsetX;
    }

    public void flipVertically()
    {
        byte abyte0[] = new byte[width * height];
        int i = 0;
        for(int j = height - 1; j >= 0; j--)
        {
            for(int k = 0; k < width; k++)
                abyte0[i++] = raster[k + j * width];

        }

        raster = abyte0;
        drawOffsetY = resizeHeight - height - drawOffsetY;
    }

    public void offsetColour(int redOffset, int greenOffset, int blueOffset)
    {
        for(int i1 = 0; i1 < palette.length; i1++)
        {
            int j1 = palette[i1] >> 16 & 0xff;
            j1 += redOffset;
            if(j1 < 0)
                j1 = 0;
            else
            if(j1 > 255)
                j1 = 255;
            int k1 = palette[i1] >> 8 & 0xff;
            k1 += greenOffset;
            if(k1 < 0)
                k1 = 0;
            else
            if(k1 > 255)
                k1 = 255;
            int l1 = palette[i1] & 0xff;
            l1 += blueOffset;
            if(l1 < 0)
                l1 = 0;
            else
            if(l1 > 255)
                l1 = 255;
            palette[i1] = (j1 << 16) + (k1 << 8) + l1;
        }
    }

    public byte raster[];
    public final int[] palette;
    public int width;
    public int height;
    public int drawOffsetX;
    public int drawOffsetY;
    public int resizeWidth;
    private int resizeHeight;
}
