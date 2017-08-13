package com.jagex.entity;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.cache.anim.Graphic;
import com.jagex.entity.model.Model;

public final class AnimableObject extends Animable {

    public AnimableObject(int i, int j, int l, int i1, int j1, int k1,
                         int l1)
    {
        this.aBoolean1567 = false;
        this.aSpotAnim_1568 = Graphic.graphics[i1];
        this.anInt1560 = i;
        this.anInt1561 = l1;
        this.anInt1562 = k1;
        this.anInt1563 = j1;
        this.anInt1564 = j + l;
            this.aBoolean1567 = false;
    }

    @Override
	public Model getModel()
    {
       return AnimableRenderer.getModel(this);
    }
    
	public void method454(int i)
    {
        for(this.anInt1570 += i; this.anInt1570 > this.aSpotAnim_1568.animation.duration(this.anInt1569);)
        {
            this.anInt1570 -= this.aSpotAnim_1568.animation.duration(this.anInt1569) + 1;
            this.anInt1569++;
            if(this.anInt1569 >= this.aSpotAnim_1568.animation.frameCount && (this.anInt1569 < 0 || this.anInt1569 >= this.aSpotAnim_1568.animation.frameCount))
            {
                this.anInt1569 = 0;
                this.aBoolean1567 = true;
            }
        }

    }

    public final int anInt1560;
    public final int anInt1561;
    public final int anInt1562;
    public final int anInt1563;
    public final int anInt1564;
    public boolean aBoolean1567;
    final Graphic aSpotAnim_1568;
    int anInt1569;
    private int anInt1570;
}
