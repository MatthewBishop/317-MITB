package com.jagex.cache.graphics;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.Constants;
import com.jagex.cache.Archive;
import com.jagex.io.Buffer;
import com.jagex.link.Cacheable;
import com.jagex.util.FileOperations;

public final class IndexedImage extends Cacheable {

    public IndexedImage(Archive archive, String s, int i)
    {
        Buffer buffer = new Buffer(archive.getEntry(s + ".dat"));
        Buffer buffer_1 = new Buffer(archive.getEntry("index.dat"));
        buffer_1.position = buffer.readUShort();
        this.resizeWidth = buffer_1.readUShort();
        this.resizeHeight = buffer_1.readUShort();
        int j = buffer_1.readUByte();
        this.palette = new int[j];
        for(int k = 0; k < j - 1; k++)
            this.palette[k + 1] = buffer_1.readUTriByte();

        for(int l = 0; l < i; l++)
        {
            buffer_1.position += 2;
            buffer.position += buffer_1.readUShort() * buffer_1.readUShort();
            buffer_1.position++;
        }

        this.drawOffsetX = buffer_1.readUByte();
        this.drawOffsetY = buffer_1.readUByte();
        this.width = buffer_1.readUShort();
        this.height = buffer_1.readUShort();
        int i1 = buffer_1.readUByte();
        int j1 = this.width * this.height;
        this.raster = new byte[j1];
        if(i1 == 0)
        {
            for(int k1 = 0; k1 < j1; k1++)
                this.raster[k1] = buffer.readByte();

            return;
        }
        if(i1 == 1)
        {
            for(int l1 = 0; l1 < this.width; l1++)
            {
                for(int i2 = 0; i2 < this.height; i2++)
                    this.raster[l1 + i2 * this.width] = buffer.readByte();

            }

        }
    }

	public String location = Constants.findcachedir() + "Indexed/";
	
    public IndexedImage(String s, int i)
    {
        Buffer buffer = new Buffer(FileOperations.ReadFile(this.location + s + ".dat"));
        Buffer buffer_1 = new Buffer(FileOperations.ReadFile(this.location + "index.dat"));
        buffer_1.position = buffer.readUShort();
        this.resizeWidth = buffer_1.readUShort();
        this.resizeHeight = buffer_1.readUShort();
        int j = buffer_1.readUByte();
        this.palette = new int[j];
        for(int k = 0; k < j - 1; k++)
            this.palette[k + 1] = buffer_1.readUTriByte();

        for(int l = 0; l < i; l++)
        {
            buffer_1.position += 2;
            buffer.position += buffer_1.readUShort() * buffer_1.readUShort();
            buffer_1.position++;
        }

        this.drawOffsetX = buffer_1.readUByte();
        this.drawOffsetY = buffer_1.readUByte();
        this.width = buffer_1.readUShort();
        this.height = buffer_1.readUShort();
        int i1 = buffer_1.readUByte();
        int j1 = this.width * this.height;
        this.raster = new byte[j1];
        if(i1 == 0)
        {
            for(int k1 = 0; k1 < j1; k1++)
                this.raster[k1] = buffer.readByte();

            return;
        }
        if(i1 == 1)
        {
            for(int l1 = 0; l1 < this.width; l1++)
            {
                for(int i2 = 0; i2 < this.height; i2++)
                    this.raster[l1 + i2 * this.width] = buffer.readByte();

            }

        }
    }
    
    public void downscale()
    {
        this.resizeWidth /= 2;
        this.resizeHeight /= 2;
        byte abyte0[] = new byte[this.resizeWidth * this.resizeHeight];
        int i = 0;
        for(int j = 0; j < this.height; j++)
        {
            for(int k = 0; k < this.width; k++)
                abyte0[(k + this.drawOffsetX >> 1) + (j + this.drawOffsetY >> 1) * this.resizeWidth] = this.raster[i++];

        }

        this.raster = abyte0;
        this.width = this.resizeWidth;
        this.height = this.resizeHeight;
        this.drawOffsetX = 0;
            this.drawOffsetY = 0;
    }

    public void resize()
    {
        if(this.width == this.resizeWidth && this.height == this.resizeHeight)
            return;
        byte abyte0[] = new byte[this.resizeWidth * this.resizeHeight];
        int i = 0;
        for(int j = 0; j < this.height; j++)
        {
            for(int k = 0; k < this.width; k++)
                abyte0[k + this.drawOffsetX + (j + this.drawOffsetY) * this.resizeWidth] = this.raster[i++];

        }

        this.raster = abyte0;
        this.width = this.resizeWidth;
        this.height = this.resizeHeight;
        this.drawOffsetX = 0;
        this.drawOffsetY = 0;
    }

    public void flipHorizontally()
    {
        byte abyte0[] = new byte[this.width * this.height];
        int j = 0;
        for(int k = 0; k < this.height; k++)
        {
            for(int l = this.width - 1; l >= 0; l--)
                abyte0[j++] = this.raster[l + k * this.width];

        }

        this.raster = abyte0;
        this.drawOffsetX = this.resizeWidth - this.width - this.drawOffsetX;
    }

    public void flipVertically()
    {
        byte abyte0[] = new byte[this.width * this.height];
        int i = 0;
        for(int j = this.height - 1; j >= 0; j--)
        {
            for(int k = 0; k < this.width; k++)
                abyte0[i++] = this.raster[k + j * this.width];

        }

        this.raster = abyte0;
        this.drawOffsetY = this.resizeHeight - this.height - this.drawOffsetY;
    }

    public void offsetColour(int redOffset, int greenOffset, int blueOffset)
    {
        for(int i1 = 0; i1 < this.palette.length; i1++)
        {
            int j1 = this.palette[i1] >> 16 & 0xff;
            j1 += redOffset;
            if(j1 < 0)
                j1 = 0;
            else
            if(j1 > 255)
                j1 = 255;
            int k1 = this.palette[i1] >> 8 & 0xff;
            k1 += greenOffset;
            if(k1 < 0)
                k1 = 0;
            else
            if(k1 > 255)
                k1 = 255;
            int l1 = this.palette[i1] & 0xff;
            l1 += blueOffset;
            if(l1 < 0)
                l1 = 0;
            else
            if(l1 > 255)
                l1 = 255;
            this.palette[i1] = (j1 << 16) + (k1 << 8) + l1;
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
