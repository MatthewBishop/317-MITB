package com.jagex.cache.anim;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.io.Buffer;

public final class FrameBase
{

    public FrameBase(Buffer buffer)
    {
        int anInt341 = buffer.readUByte();
        this.transformationType = new int[anInt341];
        this.labels = new int[anInt341][];
        for(int j = 0; j < anInt341; j++)
            this.transformationType[j] = buffer.readUByte();

        for(int k = 0; k < anInt341; k++)
        {
            int l = buffer.readUByte();
            this.labels[k] = new int[l];
            for(int i1 = 0; i1 < l; i1++)
                this.labels[k][i1] = buffer.readUByte();

        }

    }

    public final int[] transformationType;
    public final int[][] labels;
}
