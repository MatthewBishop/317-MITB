package com.jagex.link;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class Queue {

    public Queue()
    {
        head = new Cacheable();
        head.nextCacheable = head;
        head.previousCacheable = head;
    }

    public void push(Cacheable cacheable)
    {
        if(cacheable.previousCacheable != null)
            cacheable.unlinkCacheable();
        cacheable.previousCacheable = head.previousCacheable;
        cacheable.nextCacheable = head;
        cacheable.previousCacheable.nextCacheable = cacheable;
        cacheable.nextCacheable.previousCacheable = cacheable;
    }

    public Cacheable pop()
    {
        Cacheable cacheable = head.nextCacheable;
        if(cacheable == head)
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
        Cacheable cacheable = head.nextCacheable;
        if(cacheable == head)
        {
            current = null;
            return null;
        } else
        {
            current = cacheable.nextCacheable;
            return cacheable;
        }
    }

    public Cacheable getNext()
    {
        Cacheable cacheable = current;
        if(cacheable == head)
        {
            current = null;
            return null;
        } else
        {
            current = cacheable.nextCacheable;
            return cacheable;
        }
    }

    public int size()
    {
        int i = 0;
        for(Cacheable cacheable = head.nextCacheable; cacheable != head; cacheable = cacheable.nextCacheable)
            i++;

        return i;
    }

    private final Cacheable head;
    private Cacheable current;
}
