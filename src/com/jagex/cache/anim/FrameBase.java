package com.jagex.cache.anim;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.io.Stream;

public final class FrameBase
{

    public FrameBase(Stream stream)
    {
        int anInt341 = stream.readUnsignedByte();
        transformationType = new int[anInt341];
        labels = new int[anInt341][];
        for(int j = 0; j < anInt341; j++)
            transformationType[j] = stream.readUnsignedByte();

        for(int k = 0; k < anInt341; k++)
        {
            int l = stream.readUnsignedByte();
            labels[k] = new int[l];
            for(int i1 = 0; i1 < l; i1++)
                labels[k][i1] = stream.readUnsignedByte();

        }

    }

    public final int[] transformationType;
    public final int[][] labels;
}
