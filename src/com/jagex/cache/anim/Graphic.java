package com.jagex.cache.anim;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.cache.Archive;
import com.jagex.io.Buffer;

public final class Graphic {

    public static void unpackConfig(Archive archive)
    {
        Buffer buffer = new Buffer(archive.getEntry("spotanim.dat"));
        int length = buffer.readUShort();
        if(Graphic.graphics == null)
            Graphic.graphics = new Graphic[length];
        for(int j = 0; j < length; j++)
        {
            if(Graphic.graphics[j] == null)
                Graphic.graphics[j] = new Graphic();
            Graphic.graphics[j].id = j;
            Graphic.graphics[j].readValues(buffer);
        }

    }

    private void readValues(Buffer buffer)
    {
        do
        {
            int i = buffer.readUByte();
            if(i == 0)
                return;
            if(i == 1)
                this.model = buffer.readUShort();
            else
            if(i == 2)
            {
                this.animationId = buffer.readUShort();
                if(Animation.animations != null)
                    this.animation = Animation.animations[this.animationId];
            } else
            if(i == 4)
                this.breadthScale = buffer.readUShort();
            else
            if(i == 5)
                this.depthScale = buffer.readUShort();
            else
            if(i == 6)
                this.orientation = buffer.readUShort();
            else
            if(i == 7)
                this.ambience = buffer.readUByte();
            else
            if(i == 8)
                this.modelShadow = buffer.readUByte();
            else
            if(i >= 40 && i < 50)
                this.originalColours[i - 40] = buffer.readUShort();
            else
            if(i >= 50 && i < 60)
                this.replacementColours[i - 50] = buffer.readUShort();
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
