package com.jagex.sound;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.io.PacketStream;
import com.jagex.io.Buffer;

public final class Sounds {

    private Sounds()
    {
        this.aClass6Array329 = new Synthesizer[10];
    }

    public static void unpack(Buffer buffer)
    {
        Sounds.aByteArray327 = new byte[0x6baa8];
        Sounds.aStream_328 = new PacketStream(Sounds.aByteArray327);
        Synthesizer.method166();
        do
        {
            int j = buffer.readUShort();
            if(j == 65535)
                return;
            Sounds.aSoundsArray325s[j] = new Sounds();
            Sounds.aSoundsArray325s[j].method242(buffer);
            Sounds.anIntArray326[j] = Sounds.aSoundsArray325s[j].method243();
        } while(true);
    }

    public static Buffer method241(int i, int j)
    {
        if(Sounds.aSoundsArray325s[j] != null)
        {
            Sounds sounds = Sounds.aSoundsArray325s[j];
            return sounds.method244(i);
        } else
        {
            return null;
        }
    }

    private void method242(Buffer buffer)
    {
        for(int i = 0; i < 10; i++)
        {
            int j = buffer.readUByte();
            if(j != 0)
            {
                buffer.position--;
                this.aClass6Array329[i] = new Synthesizer();
                this.aClass6Array329[i].method169(buffer);
            }
        }
        this.anInt330 = buffer.readUShort();
        this.anInt331 = buffer.readUShort();
    }

    private int method243()
    {
        int j = 0x98967f;
        for(int k = 0; k < 10; k++)
            if(this.aClass6Array329[k] != null && this.aClass6Array329[k].anInt114 / 20 < j)
                j = this.aClass6Array329[k].anInt114 / 20;

        if(this.anInt330 < this.anInt331 && this.anInt330 / 20 < j)
            j = this.anInt330 / 20;
        if(j == 0x98967f || j == 0)
            return 0;
        for(int l = 0; l < 10; l++)
            if(this.aClass6Array329[l] != null)
                this.aClass6Array329[l].anInt114 -= j * 20;

        if(this.anInt330 < this.anInt331)
        {
            this.anInt330 -= j * 20;
            this.anInt331 -= j * 20;
        }
        return j;
    }

    private Buffer method244(int i)
    {
        int k = this.method245(i);
        Sounds.aStream_328.position = 0;
        Sounds.aStream_328.writeInt(0x52494646);
        Sounds.aStream_328.writeLEInt(36 + k);
        Sounds.aStream_328.writeInt(0x57415645);
        Sounds.aStream_328.writeInt(0x666d7420);
        Sounds.aStream_328.writeLEInt(16);
        Sounds.aStream_328.writeLEShort2(1);
        Sounds.aStream_328.writeLEShort2(1);
        Sounds.aStream_328.writeLEInt(22050);
        Sounds.aStream_328.writeLEInt(22050);
        Sounds.aStream_328.writeLEShort2(1);
        Sounds.aStream_328.writeLEShort2(8);
        Sounds.aStream_328.writeInt(0x64617461);
        Sounds.aStream_328.writeLEInt(k);
        Sounds.aStream_328.position += k;
        return Sounds.aStream_328;
    }

    private int method245(int i)
    {
        int j = 0;
        for(int k = 0; k < 10; k++)
            if(this.aClass6Array329[k] != null && this.aClass6Array329[k].anInt113 + this.aClass6Array329[k].anInt114 > j)
                j = this.aClass6Array329[k].anInt113 + this.aClass6Array329[k].anInt114;

        if(j == 0)
            return 0;
        int l = (22050 * j) / 1000;
        int i1 = (22050 * this.anInt330) / 1000;
        int j1 = (22050 * this.anInt331) / 1000;
        if(i1 < 0 || i1 > l || j1 < 0 || j1 > l || i1 >= j1)
            i = 0;
        int k1 = l + (j1 - i1) * (i - 1);
        for(int l1 = 44; l1 < k1 + 44; l1++)
            Sounds.aByteArray327[l1] = -128;

        for(int i2 = 0; i2 < 10; i2++)
            if(this.aClass6Array329[i2] != null)
            {
                int j2 = (this.aClass6Array329[i2].anInt113 * 22050) / 1000;
                int i3 = (this.aClass6Array329[i2].anInt114 * 22050) / 1000;
                int ai[] = this.aClass6Array329[i2].method167(j2, this.aClass6Array329[i2].anInt113);
                for(int l3 = 0; l3 < j2; l3++)
                    Sounds.aByteArray327[l3 + i3 + 44] += (byte)(ai[l3] >> 8);

            }

        if(i > 1)
        {
            i1 += 44;
            j1 += 44;
            l += 44;
            int k2 = (k1 += 44) - l;
            for(int j3 = l - 1; j3 >= j1; j3--)
                Sounds.aByteArray327[j3 + k2] = Sounds.aByteArray327[j3];

            for(int k3 = 1; k3 < i; k3++)
            {
                int l2 = (j1 - i1) * k3;
                System.arraycopy(Sounds.aByteArray327, i1, Sounds.aByteArray327, i1 + l2, j1 - i1);

            }

            k1 -= 44;
        }
        return k1;
    }

    private static final Sounds[] aSoundsArray325s = new Sounds[5000];
    public static final int[] anIntArray326 = new int[5000];
    private static byte[] aByteArray327;
    private static PacketStream aStream_328;
    private final Synthesizer[] aClass6Array329;
    private int anInt330;
    private int anInt331;

}
