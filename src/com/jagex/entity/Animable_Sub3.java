package com.jagex.entity;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.cache.anim.Frame;
import com.jagex.cache.anim.Graphic;
import com.jagex.entity.model.Model;

public final class Animable_Sub3 extends Animable {

    public Animable_Sub3(int i, int j, int l, int i1, int j1, int k1,
                         int l1)
    {
        aBoolean1567 = false;
        aSpotAnim_1568 = Graphic.graphics[i1];
        anInt1560 = i;
        anInt1561 = l1;
        anInt1562 = k1;
        anInt1563 = j1;
        anInt1564 = j + l;
            aBoolean1567 = false;
    }

    public Model getRotatedModel()
    {
        Model model = SpotAnimModel.getModel(aSpotAnim_1568);
        if(model == null)
            return null;
        int j = aSpotAnim_1568.animation.anIntArray353[anInt1569];
        Model model_1 = new Model(true, Frame.isInvalid(j), false, model);
        if(!aBoolean1567)
        {
            model_1.method469();
            model_1.method470(j);
            model_1.anIntArrayArray1658 = null;
            model_1.anIntArrayArray1657 = null;
        }
        if(aSpotAnim_1568.breadthScale != 128 || aSpotAnim_1568.depthScale != 128)
            model_1.method478(aSpotAnim_1568.breadthScale, aSpotAnim_1568.breadthScale, aSpotAnim_1568.depthScale);
        if(aSpotAnim_1568.orientation != 0)
        {
            if(aSpotAnim_1568.orientation == 90)
                model_1.method473();
            if(aSpotAnim_1568.orientation == 180)
            {
                model_1.method473();
                model_1.method473();
            }
            if(aSpotAnim_1568.orientation == 270)
            {
                model_1.method473();
                model_1.method473();
                model_1.method473();
            }
        }
        model_1.method479(64 + aSpotAnim_1568.ambience, 850 + aSpotAnim_1568.modelShadow, -30, -50, -30, true);
        return model_1;
    }

    public void method454(int i)
    {
        for(anInt1570 += i; anInt1570 > aSpotAnim_1568.animation.duration(anInt1569);)
        {
            anInt1570 -= aSpotAnim_1568.animation.duration(anInt1569) + 1;
            anInt1569++;
            if(anInt1569 >= aSpotAnim_1568.animation.anInt352 && (anInt1569 < 0 || anInt1569 >= aSpotAnim_1568.animation.anInt352))
            {
                anInt1569 = 0;
                aBoolean1567 = true;
            }
        }

    }

    public final int anInt1560;
    public final int anInt1561;
    public final int anInt1562;
    public final int anInt1563;
    public final int anInt1564;
    public boolean aBoolean1567;
    private final Graphic aSpotAnim_1568;
    private int anInt1569;
    private int anInt1570;
}
