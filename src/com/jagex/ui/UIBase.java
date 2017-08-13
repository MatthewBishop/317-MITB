package com.jagex.ui;

import com.jagex.Client;

public abstract class UIBase {

	Client client;

	public UIBase(Client client) {
		this.client = client;
	}
	
	abstract boolean handleClick();
	
	abstract void onClick(int clickX, int clickY);
	
	abstract void draw();
	
}
