package com.jagex.draw;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.link.Cacheable;

public class DrawingArea extends Cacheable {

    public static void initDrawingArea(int i, int j, int ai[])
    {
        DrawingArea.pixels = ai;
        DrawingArea.width = j;
        DrawingArea.height = i;
        DrawingArea.setDrawingArea(i, 0, j, 0);
    }

    public static void defaultDrawingAreaSize()
    {
            DrawingArea.topX = 0;
            DrawingArea.topY = 0;
            DrawingArea.bottomX = DrawingArea.width;
            DrawingArea.bottomY = DrawingArea.height;
            DrawingArea.centerX = DrawingArea.bottomX - 1;
            DrawingArea.centerY = DrawingArea.bottomX / 2;
    }

    public static void setDrawingArea(int i, int j, int k, int l)
    {
        if(j < 0)
            j = 0;
        if(l < 0)
            l = 0;
        if(k > DrawingArea.width)
            k = DrawingArea.width;
        if(i > DrawingArea.height)
            i = DrawingArea.height;
        DrawingArea.topX = j;
        DrawingArea.topY = l;
        DrawingArea.bottomX = k;
        DrawingArea.bottomY = i;
        DrawingArea.centerX = DrawingArea.bottomX - 1;
        DrawingArea.centerY = DrawingArea.bottomX / 2;
        DrawingArea.anInt1387 = DrawingArea.bottomY / 2;
    }

    public static void setAllPixelsToZero()
    {
        int i = DrawingArea.width * DrawingArea.height;
        for(int j = 0; j < i; j++)
            DrawingArea.pixels[j] = 0;

    }

    public static void method335(int i, int j, int k, int l, int i1, int k1)
    {
        if(k1 < DrawingArea.topX)
        {
            k -= DrawingArea.topX - k1;
            k1 = DrawingArea.topX;
        }
        if(j < DrawingArea.topY)
        {
            l -= DrawingArea.topY - j;
            j = DrawingArea.topY;
        }
        if(k1 + k > DrawingArea.bottomX)
            k = DrawingArea.bottomX - k1;
        if(j + l > DrawingArea.bottomY)
            l = DrawingArea.bottomY - j;
        int l1 = 256 - i1;
        int i2 = (i >> 16 & 0xff) * i1;
        int j2 = (i >> 8 & 0xff) * i1;
        int k2 = (i & 0xff) * i1;
        int k3 = DrawingArea.width - k;
        int l3 = k1 + j * DrawingArea.width;
        for(int i4 = 0; i4 < l; i4++)
        {
            for(int j4 = -k; j4 < 0; j4++)
            {
                int l2 = (DrawingArea.pixels[l3] >> 16 & 0xff) * l1;
                int i3 = (DrawingArea.pixels[l3] >> 8 & 0xff) * l1;
                int j3 = (DrawingArea.pixels[l3] & 0xff) * l1;
                int k4 = ((i2 + l2 >> 8) << 16) + ((j2 + i3 >> 8) << 8) + (k2 + j3 >> 8);
                DrawingArea.pixels[l3++] = k4;
            }

            l3 += k3;
        }
    }

    public static void drawPixels(int h, int y, int x, int color, int w)
    {
        if(x < DrawingArea.topX)
        {
            w -= DrawingArea.topX - x;
            x = DrawingArea.topX;
        }
        if(y < DrawingArea.topY)
        {
            h -= DrawingArea.topY - y;
            y = DrawingArea.topY;
        }
        if(x + w > DrawingArea.bottomX)
            w = DrawingArea.bottomX - x;
        if(y + h > DrawingArea.bottomY)
            h = DrawingArea.bottomY - y;
        int k1 = DrawingArea.width - w;
        int l1 = x + y * DrawingArea.width;
        for(int i2 = -h; i2 < 0; i2++)
        {
            for(int j2 = -w; j2 < 0; j2++)
                DrawingArea.pixels[l1++] = color;

            l1 += k1;
        }

    }

    public static void fillPixels(int i, int j, int k, int l, int i1)
    {
        DrawingArea.method339(i1, l, j, i);
        DrawingArea.method339((i1 + k) - 1, l, j, i);
        DrawingArea.method341(i1, l, k, i);
        DrawingArea.method341(i1, l, k, (i + j) - 1);
    }

    public static void method338(int y, int height, int k, int l, int i1, int j1)
    {
        DrawingArea.method340(l, i1, y, k, j1);
        DrawingArea.method340(l, i1, (y + height) - 1, k, j1);
        if(height >= 3)
        {
            DrawingArea.method342(l, j1, k, y + 1, height - 2);
            DrawingArea.method342(l, (j1 + i1) - 1, k, y + 1, height - 2);
        }
    }

    public static void method339(int i, int j, int k, int l)
    {
        if(i < DrawingArea.topY || i >= DrawingArea.bottomY)
            return;
        if(l < DrawingArea.topX)
        {
            k -= DrawingArea.topX - l;
            l = DrawingArea.topX;
        }
        if(l + k > DrawingArea.bottomX)
            k = DrawingArea.bottomX - l;
        int i1 = l + i * DrawingArea.width;
        for(int j1 = 0; j1 < k; j1++)
            DrawingArea.pixels[i1 + j1] = j;

    }

    private static void method340(int i, int j, int k, int l, int i1)
    {
        if(k < DrawingArea.topY || k >= DrawingArea.bottomY)
            return;
        if(i1 < DrawingArea.topX)
        {
            j -= DrawingArea.topX - i1;
            i1 = DrawingArea.topX;
        }
        if(i1 + j > DrawingArea.bottomX)
            j = DrawingArea.bottomX - i1;
        int j1 = 256 - l;
        int k1 = (i >> 16 & 0xff) * l;
        int l1 = (i >> 8 & 0xff) * l;
        int i2 = (i & 0xff) * l;
        int i3 = i1 + k * DrawingArea.width;
        for(int j3 = 0; j3 < j; j3++)
        {
            int j2 = (DrawingArea.pixels[i3] >> 16 & 0xff) * j1;
            int k2 = (DrawingArea.pixels[i3] >> 8 & 0xff) * j1;
            int l2 = (DrawingArea.pixels[i3] & 0xff) * j1;
            int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
            DrawingArea.pixels[i3++] = k3;
        }

    }

    public static void method341(int i, int j, int k, int l)
    {
        if(l < DrawingArea.topX || l >= DrawingArea.bottomX)
            return;
        if(i < DrawingArea.topY)
        {
            k -= DrawingArea.topY - i;
            i = DrawingArea.topY;
        }
        if(i + k > DrawingArea.bottomY)
            k = DrawingArea.bottomY - i;
        int j1 = l + i * DrawingArea.width;
        for(int k1 = 0; k1 < k; k1++)
            DrawingArea.pixels[j1 + k1 * DrawingArea.width] = j;

    }

    private static void method342(int i, int j, int k, int l, int i1)
    {
        if(j < DrawingArea.topX || j >= DrawingArea.bottomX)
            return;
        if(l < DrawingArea.topY)
        {
            i1 -= DrawingArea.topY - l;
            l = DrawingArea.topY;
        }
        if(l + i1 > DrawingArea.bottomY)
            i1 = DrawingArea.bottomY - l;
        int j1 = 256 - k;
        int k1 = (i >> 16 & 0xff) * k;
        int l1 = (i >> 8 & 0xff) * k;
        int i2 = (i & 0xff) * k;
        int i3 = j + l * DrawingArea.width;
        for(int j3 = 0; j3 < i1; j3++)
        {
            int j2 = (DrawingArea.pixels[i3] >> 16 & 0xff) * j1;
            int k2 = (DrawingArea.pixels[i3] >> 8 & 0xff) * j1;
            int l2 = (DrawingArea.pixels[i3] & 0xff) * j1;
            int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
            DrawingArea.pixels[i3] = k3;
            i3 += DrawingArea.width;
        }

    }

    public DrawingArea()
    {
    }

    public static int pixels[];
    public static int width;
    public static int height;
    public static int topY;
    public static int bottomY;
    public static int topX;
    public static int bottomX;
    public static int centerX;
    public static int centerY;
    public static int anInt1387;


}
