package com.jagex.link;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 


public class Cacheable extends Linkable {

    public final void unlinkCacheable()
    {
        if(this.previousCacheable == null)
        {
        } else
        {
            this.previousCacheable.nextCacheable = this.nextCacheable;
            this.nextCacheable.previousCacheable = this.previousCacheable;
            this.nextCacheable = null;
            this.previousCacheable = null;
        }
    }

    public Cacheable()
    {
    }

    Cacheable nextCacheable;
    Cacheable previousCacheable;
}
