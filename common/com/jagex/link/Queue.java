package com.jagex.link;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class Queue {

    public Queue()
    {
        this.head = new Cacheable();
        this.head.nextCacheable = this.head;
        this.head.previousCacheable = this.head;
    }

    public void push(Cacheable cacheable)
    {
        if(cacheable.previousCacheable != null)
            cacheable.unlinkCacheable();
        cacheable.previousCacheable = this.head.previousCacheable;
        cacheable.nextCacheable = this.head;
        cacheable.previousCacheable.nextCacheable = cacheable;
        cacheable.nextCacheable.previousCacheable = cacheable;
    }

    public Cacheable pop()
    {
        Cacheable cacheable = this.head.nextCacheable;
        if(cacheable == this.head)
        {
            return null;
        } else
        {
            cacheable.unlinkCacheable();
            return cacheable;
        }
    }

    public Cacheable peek()
    {
        Cacheable cacheable = this.head.nextCacheable;
        if(cacheable == this.head)
        {
            this.current = null;
            return null;
        } else
        {
            this.current = cacheable.nextCacheable;
            return cacheable;
        }
    }

    public Cacheable getNext()
    {
        Cacheable cacheable = this.current;
        if(cacheable == this.head)
        {
            this.current = null;
            return null;
        } else
        {
            this.current = cacheable.nextCacheable;
            return cacheable;
        }
    }

    public int size()
    {
        int i = 0;
        for(Cacheable cacheable = this.head.nextCacheable; cacheable != this.head; cacheable = cacheable.nextCacheable)
            i++;

        return i;
    }

    private final Cacheable head;
    private Cacheable current;
}
