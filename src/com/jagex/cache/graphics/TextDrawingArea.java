package com.jagex.cache.graphics;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import java.util.Random;

import com.jagex.cache.Archive;
import com.jagex.draw.DrawingArea;
import com.jagex.io.Buffer;
import com.jagex.link.Cacheable;

public final class TextDrawingArea extends Cacheable {

    public TextDrawingArea(boolean flag, String s, Archive archive)
    {
        this.aByteArrayArray1491 = new byte[256][];
        this.anIntArray1492 = new int[256];
        this.anIntArray1493 = new int[256];
        this.anIntArray1494 = new int[256];
        this.anIntArray1495 = new int[256];
        this.anIntArray1496 = new int[256];
        this.aRandom1498 = new Random();
        this.aBoolean1499 = false;
        Buffer buffer = new Buffer(archive.getEntry(s + ".dat"));
        Buffer buffer_1 = new Buffer(archive.getEntry("index.dat"));
        buffer_1.position = buffer.readUShort() + 4;
        int k = buffer_1.readUByte();
        if(k > 0)
            buffer_1.position += 3 * (k - 1);
        for(int l = 0; l < 256; l++)
        {
            this.anIntArray1494[l] = buffer_1.readUByte();
            this.anIntArray1495[l] = buffer_1.readUByte();
            int i1 = this.anIntArray1492[l] = buffer_1.readUShort();
            int j1 = this.anIntArray1493[l] = buffer_1.readUShort();
            int k1 = buffer_1.readUByte();
            int l1 = i1 * j1;
            this.aByteArrayArray1491[l] = new byte[l1];
            if(k1 == 0)
            {
                for(int i2 = 0; i2 < l1; i2++)
                    this.aByteArrayArray1491[l][i2] = buffer.readByte();

            } else
            if(k1 == 1)
            {
                for(int j2 = 0; j2 < i1; j2++)
                {
                    for(int l2 = 0; l2 < j1; l2++)
                        this.aByteArrayArray1491[l][j2 + l2 * i1] = buffer.readByte();

                }

            }
            if(j1 > this.anInt1497 && l < 128)
                this.anInt1497 = j1;
            this.anIntArray1494[l] = 1;
            this.anIntArray1496[l] = i1 + 2;
            int k2 = 0;
            for(int i3 = j1 / 7; i3 < j1; i3++)
                k2 += this.aByteArrayArray1491[l][i3 * i1];

            if(k2 <= j1 / 7)
            {
                this.anIntArray1496[l]--;
                this.anIntArray1494[l] = 0;
            }
            k2 = 0;
            for(int j3 = j1 / 7; j3 < j1; j3++)
                k2 += this.aByteArrayArray1491[l][(i1 - 1) + j3 * i1];

            if(k2 <= j1 / 7)
                this.anIntArray1496[l]--;
        }

        if(flag)
        {
            this.anIntArray1496[32] = this.anIntArray1496[73];
        } else
        {
            this.anIntArray1496[32] = this.anIntArray1496[105];
        }
    }

    public void method380(String s, int i, int j, int k)
    {
        this.method385(j, s, k, i - this.method384(s));
    }

    public void drawText(int i, String s, int k, int l)
    {
        this.method385(i, s, k, l - this.method384(s) / 2);
    }

    public void method382(int i, int j, String s, int l, boolean flag)
    {
        this.method389(flag, j - this.getTextWidth(s) / 2, i, s, l);
    }

    public int getTextWidth(String s)
    {
        if(s == null)
            return 0;
        int j = 0;
        for(int k = 0; k < s.length(); k++)
            if(s.charAt(k) == '@' && k + 4 < s.length() && s.charAt(k + 4) == '@')
                k += 4;
            else
                j += this.anIntArray1496[s.charAt(k)];

        return j;
    }

    public int method384(String s)
    {
        if(s == null)
            return 0;
        int j = 0;
        for(int k = 0; k < s.length(); k++)
            j += this.anIntArray1496[s.charAt(k)];
        return j;
    }

    public void method385(int i, String s, int j, int l)
    {
        if(s == null)
            return;
        j -= this.anInt1497;
        for(int i1 = 0; i1 < s.length(); i1++)
        {
            char c = s.charAt(i1);
            if(c != ' ')
                this.method392(this.aByteArrayArray1491[c], l + this.anIntArray1494[c], j + this.anIntArray1495[c], this.anIntArray1492[c], this.anIntArray1493[c], i);
            l += this.anIntArray1496[c];
        }
    }

    public void method386(int i, String s, int j, int k, int l)
    {
        if(s == null)
            return;
        j -= this.method384(s) / 2;
        l -= this.anInt1497;
        for(int i1 = 0; i1 < s.length(); i1++)
        {
            char c = s.charAt(i1);
            if(c != ' ')
                this.method392(this.aByteArrayArray1491[c], j + this.anIntArray1494[c], l + this.anIntArray1495[c] + (int)(Math.sin(i1 / 2D + k / 5D) * 5D), this.anIntArray1492[c], this.anIntArray1493[c], i);
            j += this.anIntArray1496[c];
        }

    }

    public void method387(int i, String s, int j, int k, int l)
    {
        if(s == null)
            return;
        i -= this.method384(s) / 2;
        k -= this.anInt1497;
        for(int i1 = 0; i1 < s.length(); i1++)
        {
            char c = s.charAt(i1);
            if(c != ' ')
                this.method392(this.aByteArrayArray1491[c], i + this.anIntArray1494[c] + (int)(Math.sin(i1 / 5D + j / 5D) * 5D), k + this.anIntArray1495[c] + (int)(Math.sin(i1 / 3D + j / 5D) * 5D), this.anIntArray1492[c], this.anIntArray1493[c], l);
            i += this.anIntArray1496[c];
        }

    }

    public void method388(int i, String s, int j, int k, int l, int i1)
    {
        if(s == null)
            return;
        double d = 7D - i / 8D;
        if(d < 0.0D)
            d = 0.0D;
        l -= this.method384(s) / 2;
        k -= this.anInt1497;
        for(int k1 = 0; k1 < s.length(); k1++)
        {
            char c = s.charAt(k1);
            if(c != ' ')
                this.method392(this.aByteArrayArray1491[c], l + this.anIntArray1494[c], k + this.anIntArray1495[c] + (int)(Math.sin(k1 / 1.5D + j) * d), this.anIntArray1492[c], this.anIntArray1493[c], i1);
            l += this.anIntArray1496[c];
        }

    }

    public void method389(boolean flag1, int i, int j, String s, int k)
    {
        this.aBoolean1499 = false;
        int l = i;
        if(s == null)
            return;
        k -= this.anInt1497;
        for(int i1 = 0; i1 < s.length(); i1++)
            if(s.charAt(i1) == '@' && i1 + 4 < s.length() && s.charAt(i1 + 4) == '@')
            {
                int j1 = this.getColorByName(s.substring(i1 + 1, i1 + 4));
                if(j1 != -1)
                    j = j1;
                i1 += 4;
            } else
            {
                char c = s.charAt(i1);
                if(c != ' ')
                {
                    if(flag1)
                        this.method392(this.aByteArrayArray1491[c], i + this.anIntArray1494[c] + 1, k + this.anIntArray1495[c] + 1, this.anIntArray1492[c], this.anIntArray1493[c], 0);
                    this.method392(this.aByteArrayArray1491[c], i + this.anIntArray1494[c], k + this.anIntArray1495[c], this.anIntArray1492[c], this.anIntArray1493[c], j);
                }
                i += this.anIntArray1496[c];
            }
        if(this.aBoolean1499)
            DrawingArea.method339(k + (int)(this.anInt1497 * 0.69999999999999996D), 0x800000, i - l, l);
    }

    public void method390(int i, int j, String s, int k, int i1)
    {
        if(s == null)
            return;
        this.aRandom1498.setSeed(k);
        int j1 = 192 + (this.aRandom1498.nextInt() & 0x1f);
        i1 -= this.anInt1497;
        for(int k1 = 0; k1 < s.length(); k1++)
            if(s.charAt(k1) == '@' && k1 + 4 < s.length() && s.charAt(k1 + 4) == '@')
            {
                int l1 = this.getColorByName(s.substring(k1 + 1, k1 + 4));
                if(l1 != -1)
                    j = l1;
                k1 += 4;
            } else
            {
                char c = s.charAt(k1);
                if(c != ' ')
                {
                        this.method394(192, i + this.anIntArray1494[c] + 1, this.aByteArrayArray1491[c], this.anIntArray1492[c], i1 + this.anIntArray1495[c] + 1, this.anIntArray1493[c], 0);
                    this.method394(j1, i + this.anIntArray1494[c], this.aByteArrayArray1491[c], this.anIntArray1492[c], i1 + this.anIntArray1495[c], this.anIntArray1493[c], j);
                }
                i += this.anIntArray1496[c];
                if((this.aRandom1498.nextInt() & 3) == 0)
                    i++;
            }

    }

    private int getColorByName(String s)
    {
        if(s.equals("red"))
            return 0xff0000;
        if(s.equals("gre"))
            return 65280;
        if(s.equals("blu"))
            return 255;
        if(s.equals("yel"))
            return 0xffff00;
        if(s.equals("cya"))
            return 65535;
        if(s.equals("mag"))
            return 0xff00ff;
        if(s.equals("whi"))
            return 0xffffff;
        if(s.equals("bla"))
            return 0;
        if(s.equals("lre"))
            return 0xff9040;
        if(s.equals("dre"))
            return 0x800000;
        if(s.equals("dbl"))
            return 128;
        if(s.equals("or1"))
            return 0xffb000;
        if(s.equals("or2"))
            return 0xff7000;
        if(s.equals("or3"))
            return 0xff3000;
        if(s.equals("gr1"))
            return 0xc0ff00;
        if(s.equals("gr2"))
            return 0x80ff00;
        if(s.equals("gr3"))
            return 0x40ff00;
        if(s.equals("str"))
            this.aBoolean1499 = true;
        if(s.equals("end"))
            this.aBoolean1499 = false;
        return -1;
    }

    private void method392(byte abyte0[], int i, int j, int k, int l, int i1)
    {
        int j1 = i + j * DrawingArea.width;
        int k1 = DrawingArea.width - k;
        int l1 = 0;
        int i2 = 0;
        if(j < DrawingArea.topY)
        {
            int j2 = DrawingArea.topY - j;
            l -= j2;
            j = DrawingArea.topY;
            i2 += j2 * k;
            j1 += j2 * DrawingArea.width;
        }
        if(j + l >= DrawingArea.bottomY)
            l -= ((j + l) - DrawingArea.bottomY) + 1;
        if(i < DrawingArea.topX)
        {
            int k2 = DrawingArea.topX - i;
            k -= k2;
            i = DrawingArea.topX;
            i2 += k2;
            j1 += k2;
            l1 += k2;
            k1 += k2;
        }
        if(i + k >= DrawingArea.bottomX)
        {
            int l2 = ((i + k) - DrawingArea.bottomX) + 1;
            k -= l2;
            l1 += l2;
            k1 += l2;
        }
        if(!(k <= 0 || l <= 0))
        {
            this.method393(DrawingArea.pixels, abyte0, i1, i2, j1, k, l, k1, l1);
        }
    }

    private void method393(int ai[], byte abyte0[], int i, int j, int k, int l, int i1,
            int j1, int k1)
    {
        int l1 = -(l >> 2);
        l = -(l & 3);
        for(int i2 = -i1; i2 < 0; i2++)
        {
            for(int j2 = l1; j2 < 0; j2++)
            {
                if(abyte0[j++] != 0)
                    ai[k++] = i;
                else
                    k++;
                if(abyte0[j++] != 0)
                    ai[k++] = i;
                else
                    k++;
                if(abyte0[j++] != 0)
                    ai[k++] = i;
                else
                    k++;
                if(abyte0[j++] != 0)
                    ai[k++] = i;
                else
                    k++;
            }

            for(int k2 = l; k2 < 0; k2++)
                if(abyte0[j++] != 0)
                    ai[k++] = i;
                else
                    k++;

            k += j1;
            j += k1;
        }

    }

    private void method394(int i, int j, byte abyte0[], int k, int l, int i1,
                           int j1)
    {
        int k1 = j + l * DrawingArea.width;
        int l1 = DrawingArea.width - k;
        int i2 = 0;
        int j2 = 0;
        if(l < DrawingArea.topY)
        {
            int k2 = DrawingArea.topY - l;
            i1 -= k2;
            l = DrawingArea.topY;
            j2 += k2 * k;
            k1 += k2 * DrawingArea.width;
        }
        if(l + i1 >= DrawingArea.bottomY)
            i1 -= ((l + i1) - DrawingArea.bottomY) + 1;
        if(j < DrawingArea.topX)
        {
            int l2 = DrawingArea.topX - j;
            k -= l2;
            j = DrawingArea.topX;
            j2 += l2;
            k1 += l2;
            i2 += l2;
            l1 += l2;
        }
        if(j + k >= DrawingArea.bottomX)
        {
            int i3 = ((j + k) - DrawingArea.bottomX) + 1;
            k -= i3;
            i2 += i3;
            l1 += i3;
        }
        if(k <= 0 || i1 <= 0)
            return;
        this.method395(abyte0, i1, k1, DrawingArea.pixels, j2, k, i2, l1, j1, i);
    }

    private void method395(byte abyte0[], int i, int j, int ai[], int l, int i1,
                           int j1, int k1, int l1, int i2)
    {
        l1 = ((l1 & 0xff00ff) * i2 & 0xff00ff00) + ((l1 & 0xff00) * i2 & 0xff0000) >> 8;
        i2 = 256 - i2;
        for(int j2 = -i; j2 < 0; j2++)
        {
            for(int k2 = -i1; k2 < 0; k2++)
                if(abyte0[l++] != 0)
                {
                    int l2 = ai[j];
                    ai[j++] = (((l2 & 0xff00ff) * i2 & 0xff00ff00) + ((l2 & 0xff00) * i2 & 0xff0000) >> 8) + l1;
                } else
                {
                    j++;
                }

            j += k1;
            l += j1;
        }

    }

    private final byte[][] aByteArrayArray1491;
    private final int[] anIntArray1492;
    private final int[] anIntArray1493;
    private final int[] anIntArray1494;
    private final int[] anIntArray1495;
    private final int[] anIntArray1496;
    public int anInt1497;
    private final Random aRandom1498;
    private boolean aBoolean1499;
}
