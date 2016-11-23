package com.jagex.sound;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.io.Buffer;

final class Class39
{

    private float method541(int i, int j, float f)
    {
        float f1 = this.anIntArrayArrayArray667[i][0][j] + f * (this.anIntArrayArrayArray667[i][1][j] - this.anIntArrayArrayArray667[i][0][j]);
            f1 *= 0.001525879F;
            return 1.0F - (float)Math.pow(10D, -f1 / 20F);
    }

    private float method542(float f)
    {
        float f1 = 32.7032F * (float)Math.pow(2D, f);
        return (f1 * 3.141593F) / 11025F;
    }

    private float method543(float f, int i, int j)
    {
        float f1 = this.anIntArrayArrayArray666[j][0][i] + f * (this.anIntArrayArrayArray666[j][1][i] - this.anIntArrayArrayArray666[j][0][i]);
        f1 *= 0.0001220703F;
        return this.method542(f1);
    }

    public int method544(int i, float f)
    {
        if(i == 0)
        {
            float f1 = this.anIntArray668[0] + (this.anIntArray668[1] - this.anIntArray668[0]) * f;
            f1 *= 0.003051758F;
            Class39.aFloat671 = (float)Math.pow(0.10000000000000001D, f1 / 20F);
            Class39.anInt672 = (int)(Class39.aFloat671 * 65536F);
        }
        if(this.anIntArray665[i] == 0)
            return 0;
        float f2 = this.method541(i, 0, f);
        Class39.aFloatArrayArray669[i][0] = -2F * f2 * (float)Math.cos(this.method543(f, 0, i));
        Class39.aFloatArrayArray669[i][1] = f2 * f2;
        for(int k = 1; k < this.anIntArray665[i]; k++)
        {
            float f3 = this.method541(i, k, f);
            float f4 = -2F * f3 * (float)Math.cos(this.method543(f, k, i));
            float f5 = f3 * f3;
            Class39.aFloatArrayArray669[i][k * 2 + 1] = Class39.aFloatArrayArray669[i][k * 2 - 1] * f5;
            Class39.aFloatArrayArray669[i][k * 2] = Class39.aFloatArrayArray669[i][k * 2 - 1] * f4 + Class39.aFloatArrayArray669[i][k * 2 - 2] * f5;
            for(int j1 = k * 2 - 1; j1 >= 2; j1--)
                Class39.aFloatArrayArray669[i][j1] += Class39.aFloatArrayArray669[i][j1 - 1] * f4 + Class39.aFloatArrayArray669[i][j1 - 2] * f5;

            Class39.aFloatArrayArray669[i][1] += Class39.aFloatArrayArray669[i][0] * f4 + f5;
            Class39.aFloatArrayArray669[i][0] += f4;
        }

        if(i == 0)
        {
            for(int l = 0; l < this.anIntArray665[0] * 2; l++)
                Class39.aFloatArrayArray669[0][l] *= Class39.aFloat671;

        }
        for(int i1 = 0; i1 < this.anIntArray665[i] * 2; i1++)
            Class39.anIntArrayArray670[i][i1] = (int)(Class39.aFloatArrayArray669[i][i1] * 65536F);

        return this.anIntArray665[i] * 2;
    }

    public void method545(Buffer buffer, Class29 class29)
    {
        int i = buffer.readUByte();
        this.anIntArray665[0] = i >> 4;
        this.anIntArray665[1] = i & 0xf;
        if(i != 0)
        {
            this.anIntArray668[0] = buffer.readUShort();
            this.anIntArray668[1] = buffer.readUShort();
            int j = buffer.readUByte();
            for(int k = 0; k < 2; k++)
            {
                for(int l = 0; l < this.anIntArray665[k]; l++)
                {
                    this.anIntArrayArrayArray666[k][0][l] = buffer.readUShort();
                    this.anIntArrayArrayArray667[k][0][l] = buffer.readUShort();
                }

            }

            for(int i1 = 0; i1 < 2; i1++)
            {
                for(int j1 = 0; j1 < this.anIntArray665[i1]; j1++)
                    if((j & 1 << i1 * 4 << j1) != 0)
                    {
                        this.anIntArrayArrayArray666[i1][1][j1] = buffer.readUShort();
                        this.anIntArrayArrayArray667[i1][1][j1] = buffer.readUShort();
                    } else
                    {
                        this.anIntArrayArrayArray666[i1][1][j1] = this.anIntArrayArrayArray666[i1][0][j1];
                        this.anIntArrayArrayArray667[i1][1][j1] = this.anIntArrayArrayArray667[i1][0][j1];
                    }

            }

            if(j != 0 || this.anIntArray668[1] != this.anIntArray668[0])
                class29.method326(buffer);
        } else
        {
            this.anIntArray668[0] = this.anIntArray668[1] = 0;
        }
    }

    public Class39()
    {
        this.anIntArray665 = new int[2];
        this.anIntArrayArrayArray666 = new int[2][2][4];
        this.anIntArrayArrayArray667 = new int[2][2][4];
        this.anIntArray668 = new int[2];
    }

    final int[] anIntArray665;
    private final int[][][] anIntArrayArrayArray666;
    private final int[][][] anIntArrayArrayArray667;
    private final int[] anIntArray668;
    private static final float[][] aFloatArrayArray669 = new float[2][8];
    static final int[][] anIntArrayArray670 = new int[2][8];
    private static float aFloat671;
    static int anInt672;

}
