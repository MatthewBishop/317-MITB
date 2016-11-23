package com.jagex.entity;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.Client;
import com.jagex.cache.anim.Animation;
import com.jagex.cache.def.ObjectDef;
import com.jagex.cache.setting.VariableBits;
import com.jagex.entity.model.Model;

public final class Animable_Sub5 extends Animable {

    @Override
	public Model getModel()
    {
        int j = -1;
        if(this.aAnimation_1607 != null)
        {
            int k = Client.loopCycle - this.anInt1608;
            if(k > 100 && this.aAnimation_1607.loopOffset > 0)
                k = 100;
            while(k > this.aAnimation_1607.duration(this.anInt1599))
            {
                k -= this.aAnimation_1607.duration(this.anInt1599);
                this.anInt1599++;
                if(this.anInt1599 < this.aAnimation_1607.frameCount)
                    continue;
                this.anInt1599 -= this.aAnimation_1607.loopOffset;
                if(this.anInt1599 >= 0 && this.anInt1599 < this.aAnimation_1607.frameCount)
                    continue;
                this.aAnimation_1607 = null;
                break;
            }
            this.anInt1608 = Client.loopCycle - k;
            if(this.aAnimation_1607 != null)
                j = this.aAnimation_1607.primaryFrames[this.anInt1599];
        }
        ObjectDef class46;
        if(this.anIntArray1600 != null)
            class46 = this.method457();
        else
            class46 = ObjectDef.forID(this.anInt1610);
        if(class46 == null)
        {
            return null;
        } else
        {
            return class46.method578(this.anInt1611, this.anInt1612, this.anInt1603, this.anInt1604, this.anInt1605, this.anInt1606, j);
        }
    }

    private ObjectDef method457()
    {
        int i = -1;
        if(this.anInt1601 != -1)
        {
            i = VariableBits.get(this.anInt1601, Animable_Sub5.clientInstance.variousSettings);
        } else
        if(this.anInt1602 != -1)
            i = Animable_Sub5.clientInstance.variousSettings[this.anInt1602];
        if(i < 0 || i >= this.anIntArray1600.length || this.anIntArray1600[i] == -1)
            return null;
        else
            return ObjectDef.forID(this.anIntArray1600[i]);
    }

    public Animable_Sub5(int i, int j, int k, int l, int i1, int j1,
                         int k1, int l1, boolean flag)
    {
        this.anInt1610 = i;
        this.anInt1611 = k;
        this.anInt1612 = j;
        this.anInt1603 = j1;
        this.anInt1604 = l;
        this.anInt1605 = i1;
        this.anInt1606 = k1;
        if(l1 != -1)
        {
            this.aAnimation_1607 = Animation.animations[l1];
            this.anInt1599 = 0;
            this.anInt1608 = Client.loopCycle;
            if(flag && this.aAnimation_1607.loopOffset != -1)
            {
                this.anInt1599 = (int)(Math.random() * this.aAnimation_1607.frameCount);
                this.anInt1608 -= (int)(Math.random() * this.aAnimation_1607.duration(this.anInt1599));
            }
        }
        ObjectDef class46 = ObjectDef.forID(this.anInt1610);
        this.anInt1601 = class46.anInt774;
        this.anInt1602 = class46.anInt749;
        this.anIntArray1600 = class46.childrenIDs;
    }

    private int anInt1599;
    private final int[] anIntArray1600;
    private final int anInt1601;
    private final int anInt1602;
    private final int anInt1603;
    private final int anInt1604;
    private final int anInt1605;
    private final int anInt1606;
    private Animation aAnimation_1607;
    private int anInt1608;
    public static Client clientInstance;
    private final int anInt1610;
    private final int anInt1611;
    private final int anInt1612;
}
