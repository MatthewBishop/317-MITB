package com.jagex.cache.anim;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.cache.Archive;
import com.jagex.io.Buffer;

public final class Animation {

    public static void unpackConfig(Archive archive)
    {
        Buffer buffer = new Buffer(archive.getEntry("seq.dat"));
        int length = buffer.readUShort();
        if(animations == null)
            animations = new Animation[length];
        for(int j = 0; j < length; j++)
        {
            if(animations[j] == null)
                animations[j] = new Animation();
            animations[j].readValues(buffer);
        }
    }

    public int duration(int i)
    {
        int j = anIntArray355[i];
        if(j == 0)
        {
            Frame frame = Frame.lookup(anIntArray353[i]);
            if(frame != null)
                j = anIntArray355[i] = frame.anInt636;
        }
        if(j == 0)
            j = 1;
        return j;
    }

    private void readValues(Buffer buffer)
    {
        do
        {
            int i = buffer.readUByte();
            if(i == 0)
                break;
            if(i == 1)
            {
                anInt352 = buffer.readUByte();
                anIntArray353 = new int[anInt352];
                anIntArray354 = new int[anInt352];
                anIntArray355 = new int[anInt352];
                for(int j = 0; j < anInt352; j++)
                {
                    anIntArray353[j] = buffer.readUShort();
                    anIntArray354[j] = buffer.readUShort();
                    if(anIntArray354[j] == 65535)
                        anIntArray354[j] = -1;
                    anIntArray355[j] = buffer.readUShort();
                }

            } else
            if(i == 2)
                anInt356 = buffer.readUShort();
            else
            if(i == 3)
            {
                int k = buffer.readUByte();
                anIntArray357 = new int[k + 1];
                for(int l = 0; l < k; l++)
                    anIntArray357[l] = buffer.readUByte();

                anIntArray357[k] = 0x98967f;
            } else
            if(i == 4)
                aBoolean358 = true;
            else
            if(i == 5)
                anInt359 = buffer.readUByte();
            else
            if(i == 6)
                anInt360 = buffer.readUShort();
            else
            if(i == 7)
                anInt361 = buffer.readUShort();
            else
            if(i == 8)
                anInt362 = buffer.readUByte();
            else
            if(i == 9)
                anInt363 = buffer.readUByte();
            else
            if(i == 10)
                anInt364 = buffer.readUByte();
            else
            if(i == 11)
                anInt365 = buffer.readUByte();
            else
            if(i == 12)
                buffer.readInt();
            else
                System.out.println("Error unrecognised seq config code: " + i);
        } while(true);
        if(anInt352 == 0)
        {
            anInt352 = 1;
            anIntArray353 = new int[1];
            anIntArray353[0] = -1;
            anIntArray354 = new int[1];
            anIntArray354[0] = -1;
            anIntArray355 = new int[1];
            anIntArray355[0] = -1;
        }
        if(anInt363 == -1)
            if(anIntArray357 != null)
                anInt363 = 2;
            else
                anInt363 = 0;
        if(anInt364 == -1)
        {
            if(anIntArray357 != null)
            {
                anInt364 = 2;
                return;
            }
            anInt364 = 0;
        }
    }

    private Animation()
    {
        anInt356 = -1;
        aBoolean358 = false;
        anInt359 = 5;
        anInt360 = -1;
        anInt361 = -1;
        anInt362 = 99;
        anInt363 = -1;
        anInt364 = -1;
        anInt365 = 2;
    }

    public static Animation animations[];
    public int anInt352;
    public int anIntArray353[];
    public int anIntArray354[];
    private int[] anIntArray355;
    public int anInt356;
    public int anIntArray357[];
    public boolean aBoolean358;
    public int anInt359;
    public int anInt360;
    public int anInt361;
    public int anInt362;
    public int anInt363;
    public int anInt364;
    public int anInt365;
    public static int anInt367;
}
