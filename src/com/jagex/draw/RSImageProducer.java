package com.jagex.draw;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import java.awt.*;
import java.awt.image.*;

public final class RSImageProducer
{

	public final int[] canvasRaster;
	public final int canvasWidth;
	public final int canvasHeight;
	private final BufferedImage bufferedImage;

	public RSImageProducer(int canvasWidth, int canvasHeight) {
		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		bufferedImage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
		canvasRaster = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
		initDrawingArea();
	}

	public void drawGraphics(int y, Graphics graphics, int x) {
		graphics.drawImage(bufferedImage, x, y, null);
	}

	public void initDrawingArea() {
		DrawingArea.initDrawingArea(canvasHeight, canvasWidth, canvasRaster);
	}
  /*  public RSImageProducer(int i, int j, Component component)
    {
        canvasWidth = i;
        canvasHeight = j;
        canvasRaster = new int[i * j];
        aColorModel318 = new DirectColorModel(32, 0xff0000, 65280, 255);
        anImage320 = component.createImage(this);
        method239();
        component.prepareImage(anImage320, this);
        method239();
        component.prepareImage(anImage320, this);
        method239();
        component.prepareImage(anImage320, this);
        initDrawingArea();
    }

    public void initDrawingArea()
    {
        DrawingArea.initDrawingArea(canvasHeight, canvasWidth, canvasRaster);
    }

    public void drawGraphics(int i, Graphics g, int k)
    {
        method239();
        g.drawImage(anImage320, k, i, this);
    }

    public synchronized void addConsumer(ImageConsumer imageconsumer)
    {
        anImageConsumer319 = imageconsumer;
        imageconsumer.setDimensions(canvasWidth, canvasHeight);
        imageconsumer.setProperties(null);
        imageconsumer.setColorModel(aColorModel318);
        imageconsumer.setHints(14);
    }

    public synchronized boolean isConsumer(ImageConsumer imageconsumer)
    {
        return anImageConsumer319 == imageconsumer;
    }

    public synchronized void removeConsumer(ImageConsumer imageconsumer)
    {
        if(anImageConsumer319 == imageconsumer)
            anImageConsumer319 = null;
    }

    public void startProduction(ImageConsumer imageconsumer)
    {
        addConsumer(imageconsumer);
    }

    public void requestTopDownLeftRightResend(ImageConsumer imageconsumer)
    {
        System.out.println("TDLR");
    }

    private synchronized void method239()
    {
        if(anImageConsumer319 != null)
        {
            anImageConsumer319.setPixels(0, 0, canvasWidth, canvasHeight, aColorModel318, canvasRaster, 0, canvasWidth);
            anImageConsumer319.imageComplete(2);
        }
    }

    public boolean imageUpdate(Image image, int i, int j, int k, int l, int i1)
    {
        return true;
    }

    public final int[] canvasRaster;
    private final int canvasWidth;
    private final int canvasHeight;
    private final ColorModel aColorModel318;
    private ImageConsumer anImageConsumer319;
    private final Image anImage320;*/
}
