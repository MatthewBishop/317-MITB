package com.jagex.entity.model;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.cache.Archive;
import com.jagex.io.Buffer;

public final class IDK {

    public static void unpackConfig(Archive archive)
    {
        Buffer buffer = new Buffer(archive.getEntry("idk.dat"));
        IDK.length = buffer.readUShort();
        if(IDK.cache == null)
            IDK.cache = new IDK[IDK.length];
        for(int j = 0; j < IDK.length; j++)
        {
            if(IDK.cache[j] == null)
                IDK.cache[j] = new IDK();
            IDK.cache[j].readValues(buffer);
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
                this.part = buffer.readUByte();
            else
            if(i == 2)
            {
                int j = buffer.readUByte();
                this.bodyModels = new int[j];
                for(int k = 0; k < j; k++)
                    this.bodyModels[k] = buffer.readUShort();

            } else
            if(i == 3)
                this.validStyle = true;
            else
            if(i >= 40 && i < 50)
                this.originalColours[i - 40] = buffer.readUShort();
            else
            if(i >= 50 && i < 60)
                this.replacementColours[i - 50] = buffer.readUShort();
            else
            if(i >= 60 && i < 70)
                this.headModels[i - 60] = buffer.readUShort();
            else
                System.out.println("Error unrecognised config code: " + i);
        } while(true);
    }

    public boolean method537()
    {
        if(this.bodyModels == null)
            return true;
        boolean flag = true;
        for(int j = 0; j < this.bodyModels.length; j++)
            if(!Model.method463(this.bodyModels[j]))
                flag = false;

        return flag;
    }

    public Model method538()
    {
        if(this.bodyModels == null)
            return null;
        Model aclass30_sub2_sub4_sub6s[] = new Model[this.bodyModels.length];
        for(int i = 0; i < this.bodyModels.length; i++)
            aclass30_sub2_sub4_sub6s[i] = Model.method462(this.bodyModels[i]);

        Model model;
        if(aclass30_sub2_sub4_sub6s.length == 1)
            model = aclass30_sub2_sub4_sub6s[0];
        else
            model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);
        for(int j = 0; j < 6; j++)
        {
            if(this.originalColours[j] == 0)
                break;
            model.method476(this.originalColours[j], this.replacementColours[j]);
        }

        return model;
    }

    public boolean method539()
    {
        boolean flag1 = true;
        for(int i = 0; i < 5; i++)
            if(this.headModels[i] != -1 && !Model.method463(this.headModels[i]))
                flag1 = false;

        return flag1;
    }

    public Model method540()
    {
        Model aclass30_sub2_sub4_sub6s[] = new Model[5];
        int j = 0;
        for(int k = 0; k < 5; k++)
            if(this.headModels[k] != -1)
                aclass30_sub2_sub4_sub6s[j++] = Model.method462(this.headModels[k]);

        Model model = new Model(j, aclass30_sub2_sub4_sub6s);
        for(int l = 0; l < 6; l++)
        {
            if(this.originalColours[l] == 0)
                break;
            model.method476(this.originalColours[l], this.replacementColours[l]);
        }

        return model;
    }

    private IDK()
    {
        this.part = -1;
        this.originalColours = new int[6];
        this.replacementColours = new int[6];
        this.validStyle = false;
    }

    public static int length;
    public static IDK cache[];
    public int part;
    private int[] bodyModels;
    private final int[] originalColours;
    private final int[] replacementColours;
    private final int[] headModels = {
        -1, -1, -1, -1, -1
    };
    public boolean validStyle;
}
