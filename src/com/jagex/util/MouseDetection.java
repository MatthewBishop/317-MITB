package com.jagex.util;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.Client;

public final class MouseDetection
        implements Runnable
{

    @Override
	public void run()
    {
        while(this.running) 
        {
            synchronized(this.syncObject)
            {
                if(this.coordsIndex < 500)
                {
                    this.coordsX[this.coordsIndex] = this.clientInstance.applet.mouseX;
                    this.coordsY[this.coordsIndex] = this.clientInstance.applet.mouseY;
                    this.coordsIndex++;
                }
            }
            try
            {
                Thread.sleep(50L);
            }
            catch(Exception _ex) { }
        }
    }

    public MouseDetection(Client client1)
    {
        this.syncObject = new Object();
        this.coordsY = new int[500];
        this.running = true;
        this.coordsX = new int[500];
        this.clientInstance = client1;
    }

    private Client clientInstance;
    public final Object syncObject;
    public final int[] coordsY;
    public boolean running;
    public final int[] coordsX;
    public int coordsIndex;
}
