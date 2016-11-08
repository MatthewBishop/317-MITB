package com.jagex.cache.anim;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.io.Stream;

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
        Stream stream = new Stream(abyte0);
        stream.currentOffset = abyte0.length - 8;
        int i = stream.readUnsignedWord();
        int j = stream.readUnsignedWord();
        int k = stream.readUnsignedWord();
        int l = stream.readUnsignedWord();
        int i1 = 0;
        Stream stream_1 = new Stream(abyte0);
        stream_1.currentOffset = i1;
        i1 += i + 2;
        Stream stream_2 = new Stream(abyte0);
        stream_2.currentOffset = i1;
        i1 += j;
        Stream stream_3 = new Stream(abyte0);
        stream_3.currentOffset = i1;
        i1 += k;
        Stream stream_4 = new Stream(abyte0);
        stream_4.currentOffset = i1;
        i1 += l;
        Stream stream_5 = new Stream(abyte0);
        stream_5.currentOffset = i1;
        FrameBase frameBase = new FrameBase(stream_5);
        int k1 = stream_1.readUnsignedWord();
        int ai[] = new int[500];
        int ai1[] = new int[500];
        int ai2[] = new int[500];
        int ai3[] = new int[500];
        for(int l1 = 0; l1 < k1; l1++)
        {
            int i2 = stream_1.readUnsignedWord();
            Frame frame = frames[i2] = new Frame();
            frame.anInt636 = stream_4.readUnsignedByte();
            frame.aClass18_637 = frameBase;
            int j2 = stream_1.readUnsignedByte();
            int k2 = -1;
            int l2 = 0;
            for(int i3 = 0; i3 < j2; i3++)
            {
                int j3 = stream_2.readUnsignedByte();
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
                        ai1[l2] = stream_3.method421();
                    else
                        ai1[l2] = c;
                    if((j3 & 2) != 0)
                        ai2[l2] = stream_3.method421();
                    else
                        ai2[l2] = c;
                    if((j3 & 4) != 0)
                        ai3[l2] = stream_3.method421();
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
