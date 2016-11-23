package com.jagex.sound;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.io.Buffer;

final class Synthesizer
{

    public static void method166()
    {
        Synthesizer.anIntArray116 = new int[32768];
        for(int i = 0; i < 32768; i++)
            if(Math.random() > 0.5D)
                Synthesizer.anIntArray116[i] = 1;
            else
                Synthesizer.anIntArray116[i] = -1;

        Synthesizer.anIntArray117 = new int[32768];
        for(int j = 0; j < 32768; j++)
            Synthesizer.anIntArray117[j] = (int)(Math.sin(j / 5215.1903000000002D) * 16384D);

        Synthesizer.anIntArray115 = new int[0x35d54];
    }

    public int[] method167(int i, int j)
    {
        for(int k = 0; k < i; k++)
            Synthesizer.anIntArray115[k] = 0;

        if(j < 10)
            return Synthesizer.anIntArray115;
        double d = i / (j + 0.0D);
        this.aClass29_98.resetValues();
        this.aClass29_99.resetValues();
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        if(this.aClass29_100 != null)
        {
            this.aClass29_100.resetValues();
            this.aClass29_101.resetValues();
            l = (int)(((this.aClass29_100.anInt539 - this.aClass29_100.anInt538) * 32.768000000000001D) / d);
            i1 = (int)((this.aClass29_100.anInt538 * 32.768000000000001D) / d);
        }
        int k1 = 0;
        int l1 = 0;
        int i2 = 0;
        if(this.aClass29_102 != null)
        {
            this.aClass29_102.resetValues();
            this.aClass29_103.resetValues();
            k1 = (int)(((this.aClass29_102.anInt539 - this.aClass29_102.anInt538) * 32.768000000000001D) / d);
            l1 = (int)((this.aClass29_102.anInt538 * 32.768000000000001D) / d);
        }
        for(int j2 = 0; j2 < 5; j2++)
            if(this.anIntArray106[j2] != 0)
            {
                Synthesizer.anIntArray118[j2] = 0;
                Synthesizer.anIntArray119[j2] = (int)(this.anIntArray108[j2] * d);
                Synthesizer.anIntArray120[j2] = (this.anIntArray106[j2] << 14) / 100;
                Synthesizer.anIntArray121[j2] = (int)(((this.aClass29_98.anInt539 - this.aClass29_98.anInt538) * 32.768000000000001D * Math.pow(1.0057929410678534D, this.anIntArray107[j2])) / d);
                Synthesizer.anIntArray122[j2] = (int)((this.aClass29_98.anInt538 * 32.768000000000001D) / d);
            }

        for(int k2 = 0; k2 < i; k2++)
        {
            int l2 = this.aClass29_98.method328(i);
            int j4 = this.aClass29_99.method328(i);
            if(this.aClass29_100 != null)
            {
                int j5 = this.aClass29_100.method328(i);
                int j6 = this.aClass29_101.method328(i);
                l2 += this.method168(j6, j1, this.aClass29_100.anInt540) >> 1;
                j1 += (j5 * l >> 16) + i1;
            }
            if(this.aClass29_102 != null)
            {
                int k5 = this.aClass29_102.method328(i);
                int k6 = this.aClass29_103.method328(i);
                j4 = j4 * ((this.method168(k6, i2, this.aClass29_102.anInt540) >> 1) + 32768) >> 15;
                i2 += (k5 * k1 >> 16) + l1;
            }
            for(int l5 = 0; l5 < 5; l5++)
                if(this.anIntArray106[l5] != 0)
                {
                    int l6 = k2 + Synthesizer.anIntArray119[l5];
                    if(l6 < i)
                    {
                        Synthesizer.anIntArray115[l6] += this.method168(j4 * Synthesizer.anIntArray120[l5] >> 15, Synthesizer.anIntArray118[l5], this.aClass29_98.anInt540);
                        Synthesizer.anIntArray118[l5] += (l2 * Synthesizer.anIntArray121[l5] >> 16) + Synthesizer.anIntArray122[l5];
                    }
                }

        }

        if(this.aClass29_104 != null)
        {
            this.aClass29_104.resetValues();
            this.aClass29_105.resetValues();
            int i3 = 0;
            boolean flag = false;
            boolean flag1 = true;
            for(int i7 = 0; i7 < i; i7++)
            {
                int k7 = this.aClass29_104.method328(i);
                int i8 = this.aClass29_105.method328(i);
                int k4;
                if(flag1)
                    k4 = this.aClass29_104.anInt538 + ((this.aClass29_104.anInt539 - this.aClass29_104.anInt538) * k7 >> 8);
                else
                    k4 = this.aClass29_104.anInt538 + ((this.aClass29_104.anInt539 - this.aClass29_104.anInt538) * i8 >> 8);
                if((i3 += 256) >= k4)
                {
                    i3 = 0;
                    flag1 = !flag1;
                }
                if(flag1)
                    Synthesizer.anIntArray115[i7] = 0;
            }

        }
        if(this.anInt109 > 0 && this.anInt110 > 0)
        {
            int j3 = (int)(this.anInt109 * d);
            for(int l4 = j3; l4 < i; l4++)
                Synthesizer.anIntArray115[l4] += (Synthesizer.anIntArray115[l4 - j3] * this.anInt110) / 100;

        }
        if(this.aClass39_111.anIntArray665[0] > 0 || this.aClass39_111.anIntArray665[1] > 0)
        {
            this.aClass29_112.resetValues();
            int k3 = this.aClass29_112.method328(i + 1);
            int i5 = this.aClass39_111.method544(0, k3 / 65536F);
            int i6 = this.aClass39_111.method544(1, k3 / 65536F);
            if(i >= i5 + i6)
            {
                int j7 = 0;
                int l7 = i6;
                if(l7 > i - i5)
                    l7 = i - i5;
                for(; j7 < l7; j7++)
                {
                    int j8 = (int)((long)Synthesizer.anIntArray115[j7 + i5] * (long)Class39.anInt672 >> 16);
                    for(int k8 = 0; k8 < i5; k8++)
                        j8 += (int)((long)Synthesizer.anIntArray115[(j7 + i5) - 1 - k8] * (long)Class39.anIntArrayArray670[0][k8] >> 16);

                    for(int j9 = 0; j9 < j7; j9++)
                        j8 -= (int)((long)Synthesizer.anIntArray115[j7 - 1 - j9] * (long)Class39.anIntArrayArray670[1][j9] >> 16);

                    Synthesizer.anIntArray115[j7] = j8;
                    k3 = this.aClass29_112.method328(i + 1);
                }

                char c = '\200';
                l7 = c;
                do
                {
                    if(l7 > i - i5)
                        l7 = i - i5;
                    for(; j7 < l7; j7++)
                    {
                        int l8 = (int)((long)Synthesizer.anIntArray115[j7 + i5] * (long)Class39.anInt672 >> 16);
                        for(int k9 = 0; k9 < i5; k9++)
                            l8 += (int)((long)Synthesizer.anIntArray115[(j7 + i5) - 1 - k9] * (long)Class39.anIntArrayArray670[0][k9] >> 16);

                        for(int i10 = 0; i10 < i6; i10++)
                            l8 -= (int)((long)Synthesizer.anIntArray115[j7 - 1 - i10] * (long)Class39.anIntArrayArray670[1][i10] >> 16);

                        Synthesizer.anIntArray115[j7] = l8;
                        k3 = this.aClass29_112.method328(i + 1);
                    }

                    if(j7 >= i - i5)
                        break;
                    i5 = this.aClass39_111.method544(0, k3 / 65536F);
                    i6 = this.aClass39_111.method544(1, k3 / 65536F);
                    l7 += c;
                } while(true);
                for(; j7 < i; j7++)
                {
                    int i9 = 0;
                    for(int l9 = (j7 + i5) - i; l9 < i5; l9++)
                        i9 += (int)((long)Synthesizer.anIntArray115[(j7 + i5) - 1 - l9] * (long)Class39.anIntArrayArray670[0][l9] >> 16);

                    for(int j10 = 0; j10 < i6; j10++)
                        i9 -= (int)((long)Synthesizer.anIntArray115[j7 - 1 - j10] * (long)Class39.anIntArrayArray670[1][j10] >> 16);

                    Synthesizer.anIntArray115[j7] = i9;
                    int l3 = this.aClass29_112.method328(i + 1);
                }

            }
        }
        for(int i4 = 0; i4 < i; i4++)
        {
            if(Synthesizer.anIntArray115[i4] < -32768)
                Synthesizer.anIntArray115[i4] = -32768;
            if(Synthesizer.anIntArray115[i4] > 32767)
                Synthesizer.anIntArray115[i4] = 32767;
        }

        return Synthesizer.anIntArray115;
    }

    private int method168(int i, int k, int l)
    {
        if(l == 1)
            if((k & 0x7fff) < 16384)
                return i;
            else
                return -i;
        if(l == 2)
            return Synthesizer.anIntArray117[k & 0x7fff] * i >> 14;
        if(l == 3)
            return ((k & 0x7fff) * i >> 14) - i;
        if(l == 4)
            return Synthesizer.anIntArray116[k / 2607 & 0x7fff] * i;
        else
            return 0;
    }

    public void method169(Buffer buffer)
    {
        this.aClass29_98 = new Class29();
        this.aClass29_98.method325(buffer);
        this.aClass29_99 = new Class29();
        this.aClass29_99.method325(buffer);
        int i = buffer.readUByte();
        if(i != 0)
        {
            buffer.position--;
            this.aClass29_100 = new Class29();
            this.aClass29_100.method325(buffer);
            this.aClass29_101 = new Class29();
            this.aClass29_101.method325(buffer);
        }
        i = buffer.readUByte();
        if(i != 0)
        {
            buffer.position--;
            this.aClass29_102 = new Class29();
            this.aClass29_102.method325(buffer);
            this.aClass29_103 = new Class29();
            this.aClass29_103.method325(buffer);
        }
        i = buffer.readUByte();
        if(i != 0)
        {
            buffer.position--;
            this.aClass29_104 = new Class29();
            this.aClass29_104.method325(buffer);
            this.aClass29_105 = new Class29();
            this.aClass29_105.method325(buffer);
        }
        for(int j = 0; j < 10; j++)
        {
            int k = buffer.readUSmart();
            if(k == 0)
                break;
            this.anIntArray106[j] = k;
            this.anIntArray107[j] = buffer.readSmart();
            this.anIntArray108[j] = buffer.readUSmart();
        }

        this.anInt109 = buffer.readUSmart();
        this.anInt110 = buffer.readUSmart();
        this.anInt113 = buffer.readUShort();
        this.anInt114 = buffer.readUShort();
        this.aClass39_111 = new Class39();
        this.aClass29_112 = new Class29();
        this.aClass39_111.method545(buffer, this.aClass29_112);
    }

    public Synthesizer()
    {
        this.anIntArray106 = new int[5];
        this.anIntArray107 = new int[5];
        this.anIntArray108 = new int[5];
        this.anInt110 = 100;
        this.anInt113 = 500;
    }

    private Class29 aClass29_98;
    private Class29 aClass29_99;
    private Class29 aClass29_100;
    private Class29 aClass29_101;
    private Class29 aClass29_102;
    private Class29 aClass29_103;
    private Class29 aClass29_104;
    private Class29 aClass29_105;
    private final int[] anIntArray106;
    private final int[] anIntArray107;
    private final int[] anIntArray108;
    private int anInt109;
    private int anInt110;
    private Class39 aClass39_111;
    private Class29 aClass29_112;
    int anInt113;
    int anInt114;
    private static int[] anIntArray115;
    private static int[] anIntArray116;
    private static int[] anIntArray117;
    private static final int[] anIntArray118 = new int[5];
    private static final int[] anIntArray119 = new int[5];
    private static final int[] anIntArray120 = new int[5];
    private static final int[] anIntArray121 = new int[5];
    private static final int[] anIntArray122 = new int[5];

}
