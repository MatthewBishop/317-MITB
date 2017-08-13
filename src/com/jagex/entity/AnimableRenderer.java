package com.jagex.entity;

import com.jagex.Client;
import com.jagex.cache.anim.Frame;
import com.jagex.cache.anim.Graphic;
import com.jagex.cache.def.ItemDef;
import com.jagex.cache.def.ObjectDef;
import com.jagex.entity.model.Model;
import com.jagex.link.Cache;

public class AnimableRenderer {

	public static Model getModel(AnimableObject object)
	{
	    Model model = AnimableRenderer.getModel(object.aSpotAnim_1568);
	    if(model == null)
	        return null;
	    int j = object.aSpotAnim_1568.animation.primaryFrames[object.anInt1569];
	    Model model_1 = new Model(true, Frame.isInvalid(j), false, model);
	    if(!object.aBoolean1567)
	    {
	        model_1.method469();
	        model_1.method470(j);
	        model_1.anIntArrayArray1658 = null;
	        model_1.anIntArrayArray1657 = null;
	    }
	    if(object.aSpotAnim_1568.breadthScale != 128 || object.aSpotAnim_1568.depthScale != 128)
	        model_1.method478(object.aSpotAnim_1568.breadthScale, object.aSpotAnim_1568.breadthScale, object.aSpotAnim_1568.depthScale);
	    if(object.aSpotAnim_1568.orientation != 0)
	    {
	        if(object.aSpotAnim_1568.orientation == 90)
	            model_1.method473();
	        if(object.aSpotAnim_1568.orientation == 180)
	        {
	            model_1.method473();
	            model_1.method473();
	        }
	        if(object.aSpotAnim_1568.orientation == 270)
	        {
	            model_1.method473();
	            model_1.method473();
	            model_1.method473();
	        }
	    }
	    model_1.method479(64 + object.aSpotAnim_1568.ambience, 850 + object.aSpotAnim_1568.modelShadow, -30, -50, -30, true);
	    return model_1;
	}

	public static final Model getModel(Item item) {
		return ItemDef.forID(item.id).asGroundStack(item.amount);
	}

	public static Model getModel(NPC npc)
	{
	    if(npc.desc == null)
	        return null;
	    Model model = npc.method450();
	    if(model == null)
	        return null;
	    npc.height = model.modelHeight;
	    if(npc.anInt1520 != -1 && npc.anInt1521 != -1)
	    {
	        Graphic graphic = Graphic.graphics[npc.anInt1520];
	        Model model_1 = AnimableRenderer.getModel(graphic);
	        if(model_1 != null)
	        {
	            int j = graphic.animation.primaryFrames[npc.anInt1521];
	            Model model_2 = new Model(true, Frame.isInvalid(j), false, model_1);
	            model_2.method475(0, -npc.anInt1524, 0);
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
	    if(npc.desc.aByte68 == 1)
	        model.aBoolean1659 = true;
	    return model;
	}

	public static Model getModel(Player player)
	 {
	     if(!player.visible)
	         return null;
	     Model model = player.method452();
	     if(model == null)
	         return null;
	     player.height = model.modelHeight;
	     model.aBoolean1659 = true;
	     if(player.aBoolean1699)
	         return model;
	     if(player.anInt1520 != -1 && player.anInt1521 != -1)
	     {
	         Graphic graphic = Graphic.graphics[player.anInt1520];
	         Model model_2 = AnimableRenderer.getModel(graphic);
	         if(model_2 != null)
	         {
	             Model model_3 = new Model(true, Frame.isInvalid(player.anInt1521), false, model_2);
	             model_3.method475(0, -player.anInt1524, 0);
	             model_3.method469();
	             model_3.method470(graphic.animation.primaryFrames[player.anInt1521]);
	             model_3.anIntArrayArray1658 = null;
	             model_3.anIntArrayArray1657 = null;
	             if(graphic.breadthScale != 128 || graphic.depthScale != 128)
	                 model_3.method478(graphic.breadthScale, graphic.breadthScale, graphic.depthScale);
	             model_3.method479(64 + graphic.ambience, 850 + graphic.modelShadow, -30, -50, -30, true);
	             Model aclass30_sub2_sub4_sub6_1s[] = {
	                     model, model_3
	             };
	             model = new Model(aclass30_sub2_sub4_sub6_1s);
	         }
	     }
	     if(player.aModel_1714 != null)
	     {
	         if(Client.loopCycle >= player.anInt1708)
	             player.aModel_1714 = null;
	         if(Client.loopCycle >= player.anInt1707 && Client.loopCycle < player.anInt1708)
	         {
	             Model model_1 = player.aModel_1714;
	             model_1.method475(player.anInt1711 - player.x, player.anInt1712 - player.anInt1709, player.anInt1713 - player.y);
	             if(player.turnDirection == 512)
	             {
	                 model_1.method473();
	                 model_1.method473();
	                 model_1.method473();
	             } else
	             if(player.turnDirection == 1024)
	             {
	                 model_1.method473();
	                 model_1.method473();
	             } else
	             if(player.turnDirection == 1536)
	                 model_1.method473();
	             Model aclass30_sub2_sub4_sub6s[] = {
	                     model, model_1
	             };
	             model = new Model(aclass30_sub2_sub4_sub6s);
	             if(player.turnDirection == 512)
	                 model_1.method473();
	             else
	             if(player.turnDirection == 1024)
	             {
	                 model_1.method473();
	                 model_1.method473();
	             } else
	             if(player.turnDirection == 1536)
	             {
	                 model_1.method473();
	                 model_1.method473();
	                 model_1.method473();
	             }
	             model_1.method475(player.x - player.anInt1711, player.anInt1709 - player.anInt1712, player.y - player.anInt1713);
	         }
	     }
	     model.aBoolean1659 = true;
	     return model;
	 }

	public static Model getModel(Projectile projectile)
	{
	    Model model = AnimableRenderer.getModel(projectile.graphic);
	    if(model == null)
	        return null;
	    int j = -1;
	    if(projectile.graphic.animation != null)
	        j = projectile.graphic.animation.primaryFrames[projectile.frameIndex];
	    Model model_1 = new Model(true, Frame.isInvalid(j), false, model);
	    if(j != -1)
	    {
	        model_1.method469();
	        model_1.method470(j);
	        model_1.anIntArrayArray1658 = null;
	        model_1.anIntArrayArray1657 = null;
	    }
	    if(projectile.graphic.breadthScale != 128 || projectile.graphic.depthScale != 128)
	        model_1.method478(projectile.graphic.breadthScale, projectile.graphic.breadthScale, projectile.graphic.depthScale);
	    model_1.method474(projectile.pitch);
	    model_1.method479(64 + projectile.graphic.ambience, 850 + projectile.graphic.modelShadow, -30, -50, -30, true);
	        return model_1;
	}

	public static Model getModel(RenderableObject object)
	{
	    int j = -1;
	    if(object.aAnimation_1607 != null)
	    {
	        int k = Client.loopCycle - object.anInt1608;
	        if(k > 100 && object.aAnimation_1607.loopOffset > 0)
	            k = 100;
	        while(k > object.aAnimation_1607.duration(object.anInt1599))
	        {
	            k -= object.aAnimation_1607.duration(object.anInt1599);
	            object.anInt1599++;
	            if(object.anInt1599 < object.aAnimation_1607.frameCount)
	                continue;
	            object.anInt1599 -= object.aAnimation_1607.loopOffset;
	            if(object.anInt1599 >= 0 && object.anInt1599 < object.aAnimation_1607.frameCount)
	                continue;
	            object.aAnimation_1607 = null;
	            break;
	        }
	        object.anInt1608 = Client.loopCycle - k;
	        if(object.aAnimation_1607 != null)
	            j = object.aAnimation_1607.primaryFrames[object.anInt1599];
	    }
	    ObjectDef class46;
	    if(object.anIntArray1600 != null)
	        class46 = object.method457();
	    else
	        class46 = ObjectDef.forID(object.anInt1610);
	    if(class46 == null)
	    {
	        return null;
	    } else
	    {
	        return class46.method578(object.anInt1611, object.anInt1612, object.anInt1603, object.anInt1604, object.anInt1605, object.anInt1606, j);
	    }
	}

	private static Model getModel(Graphic anim)
	{
	    Model model = (Model) AnimableRenderer.spotanimcache.get(anim.id);
	    if(model != null)
	        return model;
	    model = Model.method462(anim.model);
	    if(model == null)
	        return null;
	    for(int i = 0; i < 6; i++)
	        if(anim.originalColours[0] != 0)
	            model.method476(anim.originalColours[i], anim.replacementColours[i]);
	
	    AnimableRenderer.spotanimcache.put(anim.id, model);
	    return model;
	}

	public static Cache spotanimcache = new Cache(30);
}
