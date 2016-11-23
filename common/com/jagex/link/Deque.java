package com.jagex.link;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class Deque {

    public Deque()
    {
        this.tail = new Linkable();
        this.tail.next = this.tail;
        this.tail.previous = this.tail;
    }

    public void pushBack(Linkable linkable)
    {
        if(linkable.previous != null)
            linkable.unlink();
        linkable.previous = this.tail.previous;
        linkable.next = this.tail;
        linkable.previous.next = linkable;
        linkable.next.previous = linkable;
    }

    public void pushFront(Linkable linkable)
    {
        if(linkable.previous != null)
            linkable.unlink();
        linkable.previous = this.tail;
        linkable.next = this.tail.next;
        linkable.previous.next = linkable;
        linkable.next.previous = linkable;
    }

    public Linkable popFront()
    {
        Linkable linkable = this.tail.next;
        if(linkable == this.tail)
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
        Linkable linkable = this.tail.next;
        if(linkable == this.tail)
        {
            this.current = null;
            return null;
        } else
        {
            this.current = linkable.next;
            return linkable;
        }
    }

    public Linkable getTail()
    {
        Linkable linkable = this.tail.previous;
        if(linkable == this.tail)
        {
            this.current = null;
            return null;
        } else
        {
            this.current = linkable.previous;
            return linkable;
        }
    }

    public Linkable getNext()
    {
        Linkable linkable = this.current;
        if(linkable == this.tail)
        {
            this.current = null;
            return null;
        } else
        {
            this.current = linkable.next;
            return linkable;
        }
    }

    public Linkable getPrevious()
    {
        Linkable linkable = this.current;
        if(linkable == this.tail)
        {
            this.current = null;
            return null;
        }
        this.current = linkable.previous;
            return linkable;
    }

    public void clear()
    {
        if(this.tail.next == this.tail)
            return;
        do
        {
            Linkable linkable = this.tail.next;
            if(linkable == this.tail)
                return;
            linkable.unlink();
        } while(true);
    }

    private final Linkable tail;
    private Linkable current;
}
