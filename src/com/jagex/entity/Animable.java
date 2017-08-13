package com.jagex.entity;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.

// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.entity.model.Class33;
import com.jagex.entity.model.Model;
import com.jagex.link.Cacheable;

public abstract class Animable extends Cacheable {

	public void method443(Animable animable, int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2) {
		if(animable instanceof Model) {
			Model model = (Model) animable;
			model.method443(model, i, j, k, l, i1, j1, k1, l1, i2);
			return;
		}
		Model model = animable.getModel();
		if (model != null) {
			animable.modelHeight = model.modelHeight;
			model.method443(model, i, j, k, l, i1, j1, k1, l1, i2);
		}
	}

	Model getModel() {
		return null;
	}

	//abstract void lol();
	public Class33 aClass33Array1425[];
	public int modelHeight = 1000;

}
