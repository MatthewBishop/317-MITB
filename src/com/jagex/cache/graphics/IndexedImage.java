package com.jagex.cache.graphics;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.cache.Archive;
import com.jagex.io.Stream;
import com.jagex.link.Cacheable;

public final class IndexedImage extends Cacheable {

    public IndexedImage(Archive archive, String s, int i)
    {
        Stream stream = new Stream(archive.getEntry(s + ".dat"));
        Stream stream_1 = new Stream(archive.getEntry("index.dat"));
        stream_1.currentOffset = stream.readUnsignedWord();
        resizeWidth = stream_1.readUnsignedWord();
        resizeHeight = stream_1.readUnsignedWord();
        int j = stream_1.readUnsignedByte();
        palette = new int[j];
        for(int k = 0; k < j - 1; k++)
            palette[k + 1] = stream_1.read3Bytes();

        for(int l = 0; l < i; l++)
        {
            stream_1.currentOffset += 2;
            stream.currentOffset += stream_1.readUnsignedWord() * stream_1.readUnsignedWord();
            stream_1.currentOffset++;
        }

        drawOffsetX = stream_1.readUnsignedByte();
        drawOffsetY = stream_1.readUnsignedByte();
        width = stream_1.readUnsignedWord();
        height = stream_1.readUnsignedWord();
        int i1 = stream_1.readUnsignedByte();
        int j1 = width * height;
        raster = new byte[j1];
        if(i1 == 0)
        {
            for(int k1 = 0; k1 < j1; k1++)
                raster[k1] = stream.readSignedByte();

            return;
        }
        if(i1 == 1)
        {
            for(int l1 = 0; l1 < width; l1++)
            {
                for(int i2 = 0; i2 < height; i2++)
                    raster[l1 + i2 * width] = stream.readSignedByte();

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
