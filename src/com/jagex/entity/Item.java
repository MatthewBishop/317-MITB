package com.jagex.entity;

import com.jagex.entity.model.Model;

public final class Item extends Animable {

	@Override
	public final Model getModel() {
		return AnimableRenderer.getModel(this);
	}

	public int id;
	public int amount;
}
