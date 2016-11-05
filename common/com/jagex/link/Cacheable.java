package com.jagex.link;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 


public class Cacheable extends Linkable {

    public final void unlinkCacheable()
    {
        if(previousCacheable == null)
        {
        } else
        {
            previousCacheable.nextCacheable = nextCacheable;
            nextCacheable.previousCacheable = previousCacheable;
            nextCacheable = null;
            previousCacheable = null;
        }
    }

    public Cacheable()
    {
    }

    Cacheable nextCacheable;
    Cacheable previousCacheable;
}
