package com.jagex.map;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.cache.Archive;
import com.jagex.io.Buffer;

public final class Flo {

    public static void init(Archive archive)
    {
        Buffer buffer = new Buffer(archive.getEntry("flo.dat"));
        int cacheSize = buffer.readUShort();
        if(Flo.cache == null)
            Flo.cache = new Flo[cacheSize];
        for(int j = 0; j < cacheSize; j++)
        {
            if(Flo.cache[j] == null)
                Flo.cache[j] = new Flo();
            Flo.cache[j].decode(buffer);
        }

    }

    private void decode(Buffer buffer)
    {
        do
        {
            int i = buffer.readUByte();
            boolean dummy;
            if(i == 0)
                return;
            else
            if(i == 1)
            {
                this.groundRgb = buffer.readUTriByte();
                this.rgbToHsl(this.groundRgb);
            } else
            if(i == 2)
                this.textureId = buffer.readUByte();
            else
            if(i == 3)
                dummy = true;
            else
            if(i == 5)
                this.shadowed = false;
            else
            if(i == 6)
                buffer.readString();
            else
            if(i == 7)
            {
                int j = this.hue;
                int k = this.saturation;
                int l = this.luminance;
                int i1 = this.weightedHue;
                int j1 = buffer.readUTriByte();
                this.rgbToHsl(j1);
                this.anInt1198 = this.hslColour;
                this.hue = j;
                this.saturation = k;
                this.luminance = l;
                this.weightedHue = i1;
                this.chroma = i1;
            } else
            {
                System.out.println("Error unrecognised config code: " + i);
            }
        } while(true);
    }

	int anInt1198 = -1; 
    private void rgbToHsl(int i)
    {
        double d = (i >> 16 & 0xff) / 256D;
        double d1 = (i >> 8 & 0xff) / 256D;
        double d2 = (i & 0xff) / 256D;
        double d3 = d;
        if(d1 < d3)
            d3 = d1;
        if(d2 < d3)
            d3 = d2;
        double d4 = d;
        if(d1 > d4)
            d4 = d1;
        if(d2 > d4)
            d4 = d2;
        double d5 = 0.0D;
        double d6 = 0.0D;
        double d7 = (d3 + d4) / 2D;
        if(d3 != d4)
        {
            if(d7 < 0.5D)
                d6 = (d4 - d3) / (d4 + d3);
            if(d7 >= 0.5D)
                d6 = (d4 - d3) / (2D - d4 - d3);
            if(d == d4)
                d5 = (d1 - d2) / (d4 - d3);
            else
            if(d1 == d4)
                d5 = 2D + (d2 - d) / (d4 - d3);
            else
            if(d2 == d4)
                d5 = 4D + (d - d1) / (d4 - d3);
        }
        d5 /= 6D;
        this.hue = (int)(d5 * 256D);
        this.saturation = (int)(d6 * 256D);
        this.luminance = (int)(d7 * 256D);
        if(this.saturation < 0)
            this.saturation = 0;
        else
        if(this.saturation > 255)
            this.saturation = 255;
        if(this.luminance < 0)
            this.luminance = 0;
        else
        if(this.luminance > 255)
            this.luminance = 255;
        if(d7 > 0.5D)
            this.chroma = (int)((1.0D - d7) * d6 * 512D);
        else
            this.chroma = (int)(d7 * d6 * 512D);
        if(this.chroma < 1)
            this.chroma = 1;
        this.weightedHue = (int)(d5 * this.chroma);
        
        
        int k = (this.hue + (int)(Math.random() * 16D)) - 8;
        if(k < 0)
            k = 0;
        else
        if(k > 255)
            k = 255;
        int l = (this.saturation + (int)(Math.random() * 48D)) - 24;
        if(l < 0)
            l = 0;
        else
        if(l > 255)
            l = 255;
        int i1 = (this.luminance + (int)(Math.random() * 48D)) - 24;
        if(i1 < 0)
            i1 = 0;
        else
        if(i1 > 255)
            i1 = 255;
        this.hslColour = this.combine(k, l, i1);
    }

    private int combine(int i, int j, int k)
    {
        if(k > 179)
            j /= 2;
        if(k > 192)
            j /= 2;
        if(k > 217)
            j /= 2;
        if(k > 243)
            j /= 2;
        return (i / 4 << 10) + (j / 32 << 7) + k / 2;
    }

    private Flo()
    {
        this.textureId = -1;
        this.shadowed = true;
    }

    public static Flo cache[];
    public int groundRgb;
    public int textureId;
    public boolean shadowed;
    public int hue;
    public int saturation;
    public int luminance;
    public int weightedHue;
    public int chroma;
    public int hslColour;
}
