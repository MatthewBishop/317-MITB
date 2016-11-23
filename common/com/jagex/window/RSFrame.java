package com.jagex.window;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import java.awt.*;

public final class RSFrame extends Frame {

	private final RSApplet applet;
	private final Insets insets;
	private static final long serialVersionUID = 1L;

	public RSFrame(RSApplet applet, int width, int height, boolean resizable, boolean fullscreen) {
		this.applet = applet;
		this.setTitle("Fagex");
		this.setResizable(resizable);
		this.setUndecorated(fullscreen);
		this.setVisible(true); 
		this.insets = this.getInsets();
		if (resizable) {
			this.setMinimumSize(new Dimension(766 + this.insets.left + this.insets.right, 536 + this.insets.top + this.insets.bottom));
		}
		this.setSize(width + this.insets.left + this.insets.right, height + this.insets.top + this.insets.bottom);
		this.setLocationRelativeTo(null);
		this.setBackground(Color.BLACK);
		this.requestFocus();
		this.toFront();
	}

	@Override
	public Graphics getGraphics() {
		final Graphics graphics = super.getGraphics();
		Insets insets = this.getInsets();
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		graphics.translate(insets != null ? insets.left : 0, insets != null ? insets.top : 0);
		return graphics;
	}

	public int getFrameWidth() {
		Insets insets = this.getInsets();
		return this.getWidth() - (insets.left + insets.right);
	}

	public int getFrameHeight() {
		Insets insets = this.getInsets();
		return this.getHeight() - (insets.top + insets.bottom);
	}

	@Override
	public void update(Graphics graphics) {
		this.applet.update(graphics);
	}

	@Override
	public void paint(Graphics graphics) {
		this.applet.paint(graphics);
	}
}