package com.jagex.entity;

import com.jagex.cache.def.ItemDef;
import com.jagex.entity.model.Model;

public final class Item extends Animable {

	@Override
	public final Model getModel() {
		return ItemDef.forID(this.id).asGroundStack(this.amount);
	}

	public int id;
	public int amount;
}
