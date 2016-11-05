package com.jagex.link;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class Deque {

    public Deque()
    {
        tail = new Linkable();
        tail.next = tail;
        tail.previous = tail;
    }

    public void pushBack(Linkable linkable)
    {
        if(linkable.previous != null)
            linkable.unlink();
        linkable.previous = tail.previous;
        linkable.next = tail;
        linkable.previous.next = linkable;
        linkable.next.previous = linkable;
    }

    public void pushFront(Linkable linkable)
    {
        if(linkable.previous != null)
            linkable.unlink();
        linkable.previous = tail;
        linkable.next = tail.next;
        linkable.previous.next = linkable;
        linkable.next.previous = linkable;
    }

    public Linkable popFront()
    {
        Linkable linkable = tail.next;
        if(linkable == tail)
        {
            return null;
        } else
        {
            linkable.unlink();
            return linkable;
        }
    }

    public Linkable getFront()
    {
        Linkable linkable = tail.next;
        if(linkable == tail)
        {
            current = null;
            return null;
        } else
        {
            current = linkable.next;
            return linkable;
        }
    }

    public Linkable getTail()
    {
        Linkable linkable = tail.previous;
        if(linkable == tail)
        {
            current = null;
            return null;
        } else
        {
            current = linkable.previous;
            return linkable;
        }
    }

    public Linkable getNext()
    {
        Linkable linkable = current;
        if(linkable == tail)
        {
            current = null;
            return null;
        } else
        {
            current = linkable.next;
            return linkable;
        }
    }

    public Linkable getPrevious()
    {
        Linkable linkable = current;
        if(linkable == tail)
        {
            current = null;
            return null;
        }
        current = linkable.previous;
            return linkable;
    }

    public void clear()
    {
        if(tail.next == tail)
            return;
        do
        {
            Linkable linkable = tail.next;
            if(linkable == tail)
                return;
            linkable.unlink();
        } while(true);
    }

    private final Linkable tail;
    private Linkable current;
}
