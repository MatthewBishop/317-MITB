package com.jagex.sound;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.io.Buffer;

final class Class29
{

    public void method325(Buffer buffer)
    {
        this.anInt540 = buffer.readUByte();
            this.anInt538 = buffer.readInt();
            this.anInt539 = buffer.readInt();
            this.method326(buffer);
    }

    public void method326(Buffer buffer)
    {
        this.anInt535 = buffer.readUByte();
        this.anIntArray536 = new int[this.anInt535];
        this.anIntArray537 = new int[this.anInt535];
        for(int i = 0; i < this.anInt535; i++)
        {
            this.anIntArray536[i] = buffer.readUShort();
            this.anIntArray537[i] = buffer.readUShort();
        }

    }

    void resetValues()
    {
        this.anInt541 = 0;
        this.anInt542 = 0;
        this.anInt543 = 0;
        this.anInt544 = 0;
        this.anInt545 = 0;
    }

    int method328(int i)
    {
        if(this.anInt545 >= this.anInt541)
        {
            this.anInt544 = this.anIntArray537[this.anInt542++] << 15;
            if(this.anInt542 >= this.anInt535)
                this.anInt542 = this.anInt535 - 1;
            this.anInt541 = (int)((this.anIntArray536[this.anInt542] / 65536D) * i);
            if(this.anInt541 > this.anInt545)
                this.anInt543 = ((this.anIntArray537[this.anInt542] << 15) - this.anInt544) / (this.anInt541 - this.anInt545);
        }
        this.anInt544 += this.anInt543;
        this.anInt545++;
        return this.anInt544 - this.anInt543 >> 15;
    }

    public Class29()
    {
    }

    private int anInt535;
    private int[] anIntArray536;
    private int[] anIntArray537;
    int anInt538;
    int anInt539;
    int anInt540;
    private int anInt541;
    private int anInt542;
    private int anInt543;
    private int anInt544;
    private int anInt545;
    public static int anInt546;
}
