package com.jagex.cache.anim;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.io.Buffer;

public final class Frame
{

    public static void init(int i)
    {
        frames = new Frame[i + 1];
        opaque = new boolean[i + 1];
        for(int j = 0; j < i + 1; j++)
            opaque[j] = true;

    }

    public static void load(byte abyte0[])
    {
        Buffer buffer = new Buffer(abyte0);
        buffer.position = abyte0.length - 8;
        int i = buffer.readUShort();
        int j = buffer.readUShort();
        int k = buffer.readUShort();
        int l = buffer.readUShort();
        int i1 = 0;
        Buffer buffer_1 = new Buffer(abyte0);
        buffer_1.position = i1;
        i1 += i + 2;
        Buffer buffer_2 = new Buffer(abyte0);
        buffer_2.position = i1;
        i1 += j;
        Buffer buffer_3 = new Buffer(abyte0);
        buffer_3.position = i1;
        i1 += k;
        Buffer buffer_4 = new Buffer(abyte0);
        buffer_4.position = i1;
        i1 += l;
        Buffer buffer_5 = new Buffer(abyte0);
        buffer_5.position = i1;
        FrameBase frameBase = new FrameBase(buffer_5);
        int k1 = buffer_1.readUShort();
        int ai[] = new int[500];
        int ai1[] = new int[500];
        int ai2[] = new int[500];
        int ai3[] = new int[500];
        for(int l1 = 0; l1 < k1; l1++)
        {
            int i2 = buffer_1.readUShort();
            Frame frame = frames[i2] = new Frame();
            frame.anInt636 = buffer_4.readUByte();
            frame.aClass18_637 = frameBase;
            int j2 = buffer_1.readUByte();
            int k2 = -1;
            int l2 = 0;
            for(int i3 = 0; i3 < j2; i3++)
            {
                int j3 = buffer_2.readUByte();
                if(j3 > 0)
                {
                    if(frameBase.transformationType[i3] != 0)
                    {
                        for(int l3 = i3 - 1; l3 > k2; l3--)
                        {
                            if(frameBase.transformationType[l3] != 0)
                                continue;
                            ai[l2] = l3;
                            ai1[l2] = 0;
                            ai2[l2] = 0;
                            ai3[l2] = 0;
                            l2++;
                            break;
                        }

                    }
                    ai[l2] = i3;
                    char c = '\0';
                    if(frameBase.transformationType[i3] == 3)
                        c = '\200';
                    if((j3 & 1) != 0)
                        ai1[l2] = buffer_3.readSmart();
                    else
                        ai1[l2] = c;
                    if((j3 & 2) != 0)
                        ai2[l2] = buffer_3.readSmart();
                    else
                        ai2[l2] = c;
                    if((j3 & 4) != 0)
                        ai3[l2] = buffer_3.readSmart();
                    else
                        ai3[l2] = c;
                    k2 = i3;
                    l2++;
                    if(frameBase.transformationType[i3] == 5)
                        opaque[i2] = false;
                }
            }

            frame.transformationCount = l2;
            frame.transformationIndices = new int[l2];
            frame.transformX = new int[l2];
            frame.transformY = new int[l2];
            frame.transformZ = new int[l2];
            for(int k3 = 0; k3 < l2; k3++)
            {
                frame.transformationIndices[k3] = ai[k3];
                frame.transformX[k3] = ai1[k3];
                frame.transformY[k3] = ai2[k3];
                frame.transformZ[k3] = ai3[k3];
            }

        }

    }

    public static void clearFrames()
    {
        frames = null;
    }

    public static Frame lookup(int index) {
		return (frames == null) ? null : frames[index];
	}

    public static boolean isInvalid(int i)
    {
        return i == -1;
    }

    private Frame()
    {
    }

    private static Frame[] frames;
    public int anInt636;
    public FrameBase aClass18_637;
    public int transformationCount;
    public int transformationIndices[];
    public int transformX[];
    public int transformY[];
    public int transformZ[];
    private static boolean[] opaque;

}
