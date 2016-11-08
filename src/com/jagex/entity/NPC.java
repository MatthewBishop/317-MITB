package com.jagex.entity;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.cache.anim.Animation;
import com.jagex.cache.anim.Frame;
import com.jagex.cache.anim.Graphic;
import com.jagex.cache.def.EntityDef;
import com.jagex.entity.model.Model;

public final class NPC extends Entity
{

    private Model method450()
    {
        if(super.anim >= 0 && super.anInt1529 == 0)
        {
            int k = Animation.animations[super.anim].anIntArray353[super.anInt1527];
            int i1 = -1;
            if(super.anInt1517 >= 0 && super.anInt1517 != super.anInt1511)
                i1 = Animation.animations[super.anInt1517].anIntArray353[super.anInt1518];
            return desc.method164(i1, k, Animation.animations[super.anim].anIntArray357);
        }
        int l = -1;
        if(super.anInt1517 >= 0)
            l = Animation.animations[super.anInt1517].anIntArray353[super.anInt1518];
        return desc.method164(-1, l, null);
    }

    public Model getRotatedModel()
    {
        if(desc == null)
            return null;
        Model model = method450();
        if(model == null)
            return null;
        super.height = model.modelHeight;
        if(super.anInt1520 != -1 && super.anInt1521 != -1)
        {
            Graphic graphic = Graphic.graphics[super.anInt1520];
            Model model_1 = SpotAnimModel.getModel(graphic);
            if(model_1 != null)
            {
                int j = graphic.animation.anIntArray353[super.anInt1521];
                Model model_2 = new Model(true, Frame.isInvalid(j), false, model_1);
                model_2.method475(0, -super.anInt1524, 0);
                model_2.method469();
                model_2.method470(j);
                model_2.anIntArrayArray1658 = null;
                model_2.anIntArrayArray1657 = null;
                if(graphic.breadthScale != 128 || graphic.depthScale != 128)
                    model_2.method478(graphic.breadthScale, graphic.breadthScale, graphic.depthScale);
                model_2.method479(64 + graphic.ambience, 850 + graphic.modelShadow, -30, -50, -30, true);
                Model aModel[] = {
                        model, model_2
                };
                model = new Model(aModel);
            }
        }
        if(desc.aByte68 == 1)
            model.aBoolean1659 = true;
        return model;
    }

    public boolean isVisible()
    {
        return desc != null;
    }

    public NPC()
    {
    }

    public EntityDef desc;
}
