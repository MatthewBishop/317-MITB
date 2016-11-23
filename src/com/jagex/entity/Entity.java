package com.jagex.entity;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.cache.anim.Animation;

public class Entity extends Animable {

    public final void setPos(int i, int j, boolean flag)
    {
        if(this.anim != -1 && Animation.animations[this.anim].walkingPrecedence == 1)
            this.anim = -1;
        if(!flag)
        {
            int k = i - this.smallX[0];
            int l = j - this.smallY[0];
            if(k >= -8 && k <= 8 && l >= -8 && l <= 8)
            {
                if(this.smallXYIndex < 9)
                    this.smallXYIndex++;
                for(int i1 = this.smallXYIndex; i1 > 0; i1--)
                {
                    this.smallX[i1] = this.smallX[i1 - 1];
                    this.smallY[i1] = this.smallY[i1 - 1];
                    this.aBooleanArray1553[i1] = this.aBooleanArray1553[i1 - 1];
                }

                this.smallX[0] = i;
                this.smallY[0] = j;
                this.aBooleanArray1553[0] = false;
                return;
            }
        }
        this.smallXYIndex = 0;
        this.anInt1542 = 0;
        this.anInt1503 = 0;
        this.smallX[0] = i;
        this.smallY[0] = j;
        this.x = this.smallX[0] * 128 + this.anInt1540 * 64;
        this.y = this.smallY[0] * 128 + this.anInt1540 * 64;
    }

    public final void method446()
    {
        this.smallXYIndex = 0;
        this.anInt1542 = 0;
    }

    public final void updateHitData(int j, int k, int l)
    {
        for(int i1 = 0; i1 < 4; i1++)
            if(this.hitsLoopCycle[i1] <= l)
            {
                this.hitArray[i1] = k;
                this.hitMarkTypes[i1] = j;
                this.hitsLoopCycle[i1] = l + 70;
                return;
            }
    }

    public final void moveInDir(boolean flag, int i)
    {
        int j = this.smallX[0];
        int k = this.smallY[0];
        if(i == 0)
        {
            j--;
            k++;
        }
        if(i == 1)
            k++;
        if(i == 2)
        {
            j++;
            k++;
        }
        if(i == 3)
            j--;
        if(i == 4)
            j++;
        if(i == 5)
        {
            j--;
            k--;
        }
        if(i == 6)
            k--;
        if(i == 7)
        {
            j++;
            k--;
        }
        if(this.anim != -1 && Animation.animations[this.anim].walkingPrecedence == 1)
            this.anim = -1;
        if(this.smallXYIndex < 9)
            this.smallXYIndex++;
        for(int l = this.smallXYIndex; l > 0; l--)
        {
            this.smallX[l] = this.smallX[l - 1];
            this.smallY[l] = this.smallY[l - 1];
            this.aBooleanArray1553[l] = this.aBooleanArray1553[l - 1];
        }
            this.smallX[0] = j;
            this.smallY[0] = k;
            this.aBooleanArray1553[0] = flag;
    }

    public int entScreenX;
	public int entScreenY;
	public final int index = -1;
	public boolean isVisible()
    {
        return false;
    }

    Entity()
    {
        this.smallX = new int[10];
        this.smallY = new int[10];
        this.interactingEntity = -1;
        this.anInt1504 = 32;
        this.anInt1505 = -1;
        this.height = 200;
        this.anInt1511 = -1;
        this.anInt1512 = -1;
        this.hitArray = new int[4];
        this.hitMarkTypes = new int[4];
        this.hitsLoopCycle = new int[4];
        this.anInt1517 = -1;
        this.anInt1520 = -1;
        this.anim = -1;
        this.loopCycleStatus = -1000;
        this.textCycle = 100;
        this.anInt1540 = 1;
        this.aBoolean1541 = false;
        this.aBooleanArray1553 = new boolean[10];
        this.anInt1554 = -1;
        this.anInt1555 = -1;
        this.anInt1556 = -1;
        this.anInt1557 = -1;
    }

    public final int[] smallX;
    public final int[] smallY;
    public int interactingEntity;
    public int anInt1503;
    public int anInt1504;
    public int anInt1505;
    public String textSpoken;
    public int height;
    public int turnDirection;
    public int anInt1511;
    public int anInt1512;
    public int anInt1513;
    public final int[] hitArray;
    public final int[] hitMarkTypes;
    public final int[] hitsLoopCycle;
    public int anInt1517;
    public int anInt1518;
    public int anInt1519;
    public int anInt1520;
    public int anInt1521;
    public int anInt1522;
    public int anInt1523;
    public int anInt1524;
    public int smallXYIndex;
    public int anim;
    public int anInt1527;
    public int anInt1528;
    public int anInt1529;
    public int anInt1530;
    public int anInt1531;
    public int loopCycleStatus;
    public int currentHealth;
    public int maxHealth;
    public int textCycle;
    public int anInt1537;
    public int anInt1538;
    public int anInt1539;
    public int anInt1540;
    public boolean aBoolean1541;
    public int anInt1542;
    public int anInt1543;
    public int anInt1544;
    public int anInt1545;
    public int anInt1546;
    public int anInt1547;
    public int anInt1548;
    public int anInt1549;
    public int x;
    public int y;
    public int anInt1552;
    public final boolean[] aBooleanArray1553;
    public int anInt1554;
    public int anInt1555;
    public int anInt1556;
    public int anInt1557;
}
