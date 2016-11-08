package com.jagex.cache.anim;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.cache.Archive;
import com.jagex.io.Stream;

public final class Graphic {

    public static void unpackConfig(Archive archive)
    {
        Stream stream = new Stream(archive.getEntry("spotanim.dat"));
        int length = stream.readUnsignedWord();
        if(graphics == null)
            graphics = new Graphic[length];
        for(int j = 0; j < length; j++)
        {
            if(graphics[j] == null)
                graphics[j] = new Graphic();
            graphics[j].id = j;
            graphics[j].readValues(stream);
        }

    }

    private void readValues(Stream stream)
    {
        do
        {
            int i = stream.readUnsignedByte();
            if(i == 0)
                return;
            if(i == 1)
                model = stream.readUnsignedWord();
            else
            if(i == 2)
            {
                animationId = stream.readUnsignedWord();
                if(Animation.animations != null)
                    animation = Animation.animations[animationId];
            } else
            if(i == 4)
                breadthScale = stream.readUnsignedWord();
            else
            if(i == 5)
                depthScale = stream.readUnsignedWord();
            else
            if(i == 6)
                orientation = stream.readUnsignedWord();
            else
            if(i == 7)
                ambience = stream.readUnsignedByte();
            else
            if(i == 8)
                modelShadow = stream.readUnsignedByte();
            else
            if(i >= 40 && i < 50)
                originalColours[i - 40] = stream.readUnsignedWord();
            else
            if(i >= 50 && i < 60)
                replacementColours[i - 50] = stream.readUnsignedWord();
            else
                System.out.println("Error unrecognised spotanim config code: " + i);
        } while(true);
    }

    public static Graphic graphics[];
    public int id;
    public int model;
    private int animationId = -1;
    public Animation animation;
    public final int[] originalColours = new int[6];
    public final int[] replacementColours = new int[6];
    public int breadthScale = 128;
    public int depthScale = 128;
    public int orientation;
    public int ambience;
    public int modelShadow;

}
