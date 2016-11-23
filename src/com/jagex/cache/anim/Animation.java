package com.jagex.cache.anim;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.cache.Archive;
import com.jagex.io.Buffer;

public final class Animation {

    public static void init(Archive archive)
    {
        Buffer buffer = new Buffer(archive.getEntry("seq.dat"));
        int length = buffer.readUShort();
        if(Animation.animations == null)
            Animation.animations = new Animation[length];
        for(int j = 0; j < length; j++)
        {
            if(Animation.animations[j] == null)
                Animation.animations[j] = new Animation();
            Animation.animations[j].decode(buffer);
        }
    }

    public int duration(int i)
    {
        int j = this.durations[i];
        if(j == 0)
        {
            Frame frame = Frame.lookup(this.primaryFrames[i]);
            if(frame != null)
                j = this.durations[i] = frame.anInt636;
        }
        if(j == 0)
            j = 1;
        return j;
    }

    private void decode(Buffer buffer)
    {
        do
        {
            int i = buffer.readUByte();
            if(i == 0)
                break;
            if(i == 1)
            {
                this.frameCount = buffer.readUByte();
                this.primaryFrames = new int[this.frameCount];
                this.secondaryFrames = new int[this.frameCount];
                this.durations = new int[this.frameCount];
                for(int j = 0; j < this.frameCount; j++)
                {
                    this.primaryFrames[j] = buffer.readUShort();
                    this.secondaryFrames[j] = buffer.readUShort();
                    if(this.secondaryFrames[j] == 65535)
                        this.secondaryFrames[j] = -1;
                    this.durations[j] = buffer.readUShort();
                }

            } else
            if(i == 2)
                this.loopOffset = buffer.readUShort();
            else
            if(i == 3)
            {
                int k = buffer.readUByte();
                this.interleaveOrder = new int[k + 1];
                for(int l = 0; l < k; l++)
                    this.interleaveOrder[l] = buffer.readUByte();

                this.interleaveOrder[k] = 0x98967f;
            } else
            if(i == 4)
                this.stretches = true;
            else
            if(i == 5)
                this.priority = buffer.readUByte();
            else
            if(i == 6)
                this.playerOffhand = buffer.readUShort();
            else
            if(i == 7)
                this.playerMainhand = buffer.readUShort();
            else
            if(i == 8)
                this.maximumLoops = buffer.readUByte();
            else
            if(i == 9)
                this.animatingPrecedence = buffer.readUByte();
            else
            if(i == 10)
                this.walkingPrecedence = buffer.readUByte();
            else
            if(i == 11)
                this.replayMode = buffer.readUByte();
            else
            if(i == 12)
                buffer.readInt();
            else
                System.out.println("Error unrecognised seq config code: " + i);
        } while(true);
        if(this.frameCount == 0)
        {
            this.frameCount = 1;
            this.primaryFrames = new int[1];
            this.primaryFrames[0] = -1;
            this.secondaryFrames = new int[1];
            this.secondaryFrames[0] = -1;
            this.durations = new int[1];
            this.durations[0] = -1;
        }
        if(this.animatingPrecedence == -1)
            if(this.interleaveOrder != null)
                this.animatingPrecedence = 2;
            else
                this.animatingPrecedence = 0;
        if(this.walkingPrecedence == -1)
        {
            if(this.interleaveOrder != null)
            {
                this.walkingPrecedence = 2;
                return;
            }
            this.walkingPrecedence = 0;
        }
    }

    private Animation()
    {
        this.loopOffset = -1;
        this.stretches = false;
        this.priority = 5;
        this.playerOffhand = -1;
        this.playerMainhand = -1;
        this.maximumLoops = 99;
        this.animatingPrecedence = -1;
        this.walkingPrecedence = -1;
        this.replayMode = 2;
    }

    public static Animation animations[];
    public int frameCount;
    public int primaryFrames[];
    public int secondaryFrames[];
    private int[] durations;
    public int loopOffset;
    public int interleaveOrder[];
    public boolean stretches;
    public int priority;
    public int playerOffhand;
    public int playerMainhand;
    public int maximumLoops;
    public int animatingPrecedence;
    public int walkingPrecedence;
    public int replayMode;
}
