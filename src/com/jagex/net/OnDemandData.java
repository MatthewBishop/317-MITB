package com.jagex.net;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.link.Cacheable;

public final class OnDemandData extends Cacheable {

    public OnDemandData()
    {
        this.incomplete = true;
    }

    public int dataType;
    public byte buffer[];
    public int ID;
    boolean incomplete;
    int loopCycle;
}
