package com.jagex.entity;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.cache.anim.Animation;
import com.jagex.cache.def.EntityDef;
import com.jagex.entity.model.Model;

public final class NPC extends Entity
{

    Model method450()
    {
        if(super.anim >= 0 && super.anInt1529 == 0)
        {
            int k = Animation.animations[super.anim].primaryFrames[super.anInt1527];
            int i1 = -1;
            if(super.anInt1517 >= 0 && super.anInt1517 != super.anInt1511)
                i1 = Animation.animations[super.anInt1517].primaryFrames[super.anInt1518];
            return this.desc.method164(i1, k, Animation.animations[super.anim].interleaveOrder);
        }
        int l = -1;
        if(super.anInt1517 >= 0)
            l = Animation.animations[super.anInt1517].primaryFrames[super.anInt1518];
        return this.desc.method164(-1, l, null);
    }

    @Override
	public Model getModel()
    {
        return AnimableRenderer.getModel(this);
    }

	@Override
	public boolean isVisible()
    {
        return this.desc != null;
    }

    public NPC()
    {
    }

    public EntityDef desc;
}
