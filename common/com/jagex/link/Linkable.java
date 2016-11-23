package com.jagex.link;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public class Linkable {

    public final void unlink()
    {
        if(this.previous == null)
        {
        } else
        {
            this.previous.next = this.next;
            this.next.previous = this.previous;
            this.next = null;
            this.previous = null;
        }
    }

    public Linkable()
    {
    }

    long key;
    Linkable next;
    Linkable previous;
}
