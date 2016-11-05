package com.jagex.entity;

import com.jagex.cache.anim.Graphic;
import com.jagex.entity.model.Model;
import com.jagex.link.Cache;

public class SpotAnimModel {

	public static Model getModel(Graphic anim)
	{
	    Model model = (Model) SpotAnimModel.spotanimcache.get(anim.anInt404);
	    if(model != null)
	        return model;
	    model = Model.method462(anim.model);
	    if(model == null)
	        return null;
	    for(int i = 0; i < 6; i++)
	        if(anim.originalColours[0] != 0)
	            model.method476(anim.originalColours[i], anim.replacementColours[i]);
	
	    SpotAnimModel.spotanimcache.put(anim.anInt404, model);
	    return model;
	}

	public static Cache spotanimcache = new Cache(30);

}
