package com.jagex.map;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.link.Linkable;

public final class Ground extends Linkable {

    public Ground(int i, int j, int k)
    {
        this.locs = new Object5[5];
        this.locFlags = new int[5];
        this.renderLevel = this.level = i;
        this.anInt1308 = j;
        this.anInt1309 = k;
    }

    int level;
    final int anInt1308;
    final int anInt1309;
    final int renderLevel;
    public TileUnderlay underlay;
    public Class40 overlay;
    public Object1 obj1;
    public Object2 obj2;
    public Object3 obj3;
    public Object4 obj4;
    int anInt1317;
    public final Object5[] locs;
    final int[] locFlags;
    int anInt1320;
    int anInt1321;
    boolean aBoolean1322;
    boolean aBoolean1323;
    boolean aBoolean1324;
    int anInt1325;
    int anInt1326;
    int anInt1327;
    int anInt1328;
    public Ground bridge;
}
