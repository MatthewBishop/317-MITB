package com.jagex;

import java.awt.Graphics;

import com.jagex.cache.Archive;
import com.jagex.cache.graphics.IndexedImage;
import com.jagex.cache.graphics.Sprite;
import com.jagex.draw.RSImageProducer;

public class FlameManager implements Runnable {

	private IndexedImage[] aBackgroundArray1152s;
	private Sprite aClass30_Sub2_Sub1_Sub1_1201;
	private Sprite aClass30_Sub2_Sub1_Sub1_1202;
	
    private int anInt1040;
    private int anInt1041; 
    private int anInt1275;
	private int[] anIntArray1190;
	private int[] anIntArray1191;
	private int[] anIntArray828;
    private int[] anIntArray829;
    private int[] anIntArray850;
    private int[] anIntArray851;
    private int[] anIntArray852;
    private int[] anIntArray853;
    private final int[] anIntArray969 = new int[256];

    private volatile boolean drawingFlames;

    private Graphics graphics;
    private RSImageProducer left;

    private RSImageProducer right;
    
    public volatile boolean running;
    public int flameTick;
    public FlameManager(Graphics graphics, RSImageProducer left, RSImageProducer right, Archive archive) {
		this.graphics = graphics;
		this.left = left;
		this.right = right;
		
        running = false;

        drawingFlames = false;
        aBackgroundArray1152s = new IndexedImage[12];
        int fl_icon = 0;//getParameter("fl_icon")

        if(fl_icon == 0)
        {
            for(int k = 0; k < 12; k++)
                aBackgroundArray1152s[k] = new IndexedImage(archive, "runes", k);

        } else
        {
            for(int l = 0; l < 12; l++)
                aBackgroundArray1152s[l] = new IndexedImage(archive, "runes", 12 + (l & 3));

        }
        aClass30_Sub2_Sub1_Sub1_1201 = new Sprite(128, 265);
        aClass30_Sub2_Sub1_Sub1_1202 = new Sprite(128, 265);
        System.arraycopy(left.anIntArray315, 0, aClass30_Sub2_Sub1_Sub1_1201.raster, 0, 33920);

        System.arraycopy(right.anIntArray315, 0, aClass30_Sub2_Sub1_Sub1_1202.raster, 0, 33920);
        
        anIntArray851 = new int[256];
        for(int k1 = 0; k1 < 64; k1++)
            anIntArray851[k1] = k1 * 0x40000;

        for(int l1 = 0; l1 < 64; l1++)
            anIntArray851[l1 + 64] = 0xff0000 + 1024 * l1;

        for(int i2 = 0; i2 < 64; i2++)
            anIntArray851[i2 + 128] = 0xffff00 + 4 * i2;

        for(int j2 = 0; j2 < 64; j2++)
            anIntArray851[j2 + 192] = 0xffffff;

        anIntArray852 = new int[256];
        for(int k2 = 0; k2 < 64; k2++)
            anIntArray852[k2] = k2 * 1024;

        for(int l2 = 0; l2 < 64; l2++)
            anIntArray852[l2 + 64] = 65280 + 4 * l2;

        for(int i3 = 0; i3 < 64; i3++)
            anIntArray852[i3 + 128] = 65535 + 0x40000 * i3;

        for(int j3 = 0; j3 < 64; j3++)
            anIntArray852[j3 + 192] = 0xffffff;

        anIntArray853 = new int[256];
        for(int k3 = 0; k3 < 64; k3++)
            anIntArray853[k3] = k3 * 4;

        for(int l3 = 0; l3 < 64; l3++)
            anIntArray853[l3 + 64] = 255 + 0x40000 * l3;

        for(int i4 = 0; i4 < 64; i4++)
            anIntArray853[i4 + 128] = 0xff00ff + 1024 * i4;

        for(int j4 = 0; j4 < 64; j4++)
            anIntArray853[j4 + 192] = 0xffffff;

        anIntArray850 = new int[256];
        anIntArray1190 = new int[32768];
        anIntArray1191 = new int[32768];
        randomizeBackground(null);
        anIntArray828 = new int[32768];
        anIntArray829 = new int[32768];
        if(!running)
        {
            running = true;
            Utils.startRunnable(this, 2);
        }
	}

    private void calcFlamesPosition()
    {
        char c = '\u0100';
        for(int j = 10; j < 117; j++)
        {
            int k = (int)(Math.random() * 100D);
            if(k < 50)
                anIntArray828[j + (c - 2 << 7)] = 255;
        }
        for(int l = 0; l < 100; l++)
        {
            int i1 = (int)(Math.random() * 124D) + 2;
            int k1 = (int)(Math.random() * 128D) + 128;
            int k2 = i1 + (k1 << 7);
            anIntArray828[k2] = 192;
        }

        for(int j1 = 1; j1 < c - 1; j1++)
        {
            for(int l1 = 1; l1 < 127; l1++)
            {
                int l2 = l1 + (j1 << 7);
                anIntArray829[l2] = (anIntArray828[l2 - 1] + anIntArray828[l2 + 1] + anIntArray828[l2 - 128] + anIntArray828[l2 + 128]) / 4;
            }

        }

        anInt1275 += 128;
        if(anInt1275 > anIntArray1190.length)
        {
            anInt1275 -= anIntArray1190.length;
            int i2 = (int)(Math.random() * 12D);
            randomizeBackground(aBackgroundArray1152s[i2]);
        }
        for(int j2 = 1; j2 < c - 1; j2++)
        {
            for(int i3 = 1; i3 < 127; i3++)
            {
                int k3 = i3 + (j2 << 7);
                int i4 = anIntArray829[k3 + 128] - anIntArray1190[k3 + anInt1275 & anIntArray1190.length - 1] / 5;
                if(i4 < 0)
                    i4 = 0;
                anIntArray828[k3] = i4;
            }

        }

        System.arraycopy(anIntArray969, 1, anIntArray969, 0, c - 1);

        anIntArray969[c - 1] = (int)(Math.sin((double)Client.loopCycle / 14D) * 16D + Math.sin((double)Client.loopCycle / 15D) * 14D + Math.sin((double)Client.loopCycle / 16D) * 12D);
        if(anInt1040 > 0)
            anInt1040 -= 4;
        if(anInt1041 > 0)
            anInt1041 -= 4;
        if(anInt1040 == 0 && anInt1041 == 0)
        {
            int l3 = (int)(Math.random() * 2000D);
            if(l3 == 0)
                anInt1040 = 1024;
            if(l3 == 1)
                anInt1041 = 1024;
        }
    }

    public void destroy() {
        running = false;
        while(drawingFlames)
        {
            running = false;
            try
            {
                Thread.sleep(50L);
            }
            catch(Exception _ex) { }
        }
        aBackgroundArray1152s = null;
        anIntArray850 = null;
        anIntArray851 = null;
        anIntArray852 = null;
        anIntArray853 = null;
        anIntArray1190 = null;
        anIntArray1191 = null;
        anIntArray828 = null;
        anIntArray829 = null;
        aClass30_Sub2_Sub1_Sub1_1201 = null;
        aClass30_Sub2_Sub1_Sub1_1202 = null;
	}
    
    private void doFlamesDrawing()
    {
        char c = '\u0100';
        if(anInt1040 > 0)
        {
            for(int i = 0; i < 256; i++)
                if(anInt1040 > 768)
                    anIntArray850[i] = method83(anIntArray851[i], anIntArray852[i], 1024 - anInt1040);
                else
                if(anInt1040 > 256)
                    anIntArray850[i] = anIntArray852[i];
                else
                    anIntArray850[i] = method83(anIntArray852[i], anIntArray851[i], 256 - anInt1040);

        } else
        if(anInt1041 > 0)
        {
            for(int j = 0; j < 256; j++)
                if(anInt1041 > 768)
                    anIntArray850[j] = method83(anIntArray851[j], anIntArray853[j], 1024 - anInt1041);
                else
                if(anInt1041 > 256)
                    anIntArray850[j] = anIntArray853[j];
                else
                    anIntArray850[j] = method83(anIntArray853[j], anIntArray851[j], 256 - anInt1041);

        } else
        {
            System.arraycopy(anIntArray851, 0, anIntArray850, 0, 256);

        }
        System.arraycopy(aClass30_Sub2_Sub1_Sub1_1201.raster, 0, left.anIntArray315, 0, 33920);

        int i1 = 0;
        int j1 = 1152;
        for(int k1 = 1; k1 < c - 1; k1++)
        {
            int l1 = (anIntArray969[k1] * (c - k1)) / c;
            int j2 = 22 + l1;
            if(j2 < 0)
                j2 = 0;
            i1 += j2;
            for(int l2 = j2; l2 < 128; l2++)
            {
                int j3 = anIntArray828[i1++];
                if(j3 != 0)
                {
                    int l3 = j3;
                    int j4 = 256 - j3;
                    j3 = anIntArray850[j3];
                    int l4 = left.anIntArray315[j1];
                    left.anIntArray315[j1++] = ((j3 & 0xff00ff) * l3 + (l4 & 0xff00ff) * j4 & 0xff00ff00) + ((j3 & 0xff00) * l3 + (l4 & 0xff00) * j4 & 0xff0000) >> 8;
                } else
                {
                    j1++;
                }
            }

            j1 += j2;
        }

        left.drawGraphics(0, graphics, 0);
        System.arraycopy(aClass30_Sub2_Sub1_Sub1_1202.raster, 0, right.anIntArray315, 0, 33920);

        i1 = 0;
        j1 = 1176;
        for(int k2 = 1; k2 < c - 1; k2++)
        {
            int i3 = (anIntArray969[k2] * (c - k2)) / c;
            int k3 = 103 - i3;
            j1 += i3;
            for(int i4 = 0; i4 < k3; i4++)
            {
                int k4 = anIntArray828[i1++];
                if(k4 != 0)
                {
                    int i5 = k4;
                    int j5 = 256 - k4;
                    k4 = anIntArray850[k4];
                    int k5 = right.anIntArray315[j1];
                    right.anIntArray315[j1++] = ((k4 & 0xff00ff) * i5 + (k5 & 0xff00ff) * j5 & 0xff00ff00) + ((k4 & 0xff00) * i5 + (k5 & 0xff00) * j5 & 0xff0000) >> 8;
                } else
                {
                    j1++;
                }
            }

            i1 += 128 - k3;
            j1 += 128 - k3 - i3;
        }

        right.drawGraphics(0, graphics, 637);
    }
    
    private int method83(int i, int j, int k)
    {
        int l = 256 - k;
        return ((i & 0xff00ff) * l + (j & 0xff00ff) * k & 0xff00ff00) + ((i & 0xff00) * l + (j & 0xff00) * k & 0xff0000) >> 8;
    }
    
    private void randomizeBackground(IndexedImage indexedImage)
    {
        int j = 256;
        for(int k = 0; k < anIntArray1190.length; k++)
            anIntArray1190[k] = 0;

        for(int l = 0; l < 5000; l++)
        {
            int i1 = (int)(Math.random() * 128D * (double)j);
            anIntArray1190[i1] = (int)(Math.random() * 256D);
        }

        for(int j1 = 0; j1 < 20; j1++)
        {
            for(int k1 = 1; k1 < j - 1; k1++)
            {
                for(int i2 = 1; i2 < 127; i2++)
                {
                    int k2 = i2 + (k1 << 7);
                    anIntArray1191[k2] = (anIntArray1190[k2 - 1] + anIntArray1190[k2 + 1] + anIntArray1190[k2 - 128] + anIntArray1190[k2 + 128]) / 4;
                }

            }

            int ai[] = anIntArray1190;
            anIntArray1190 = anIntArray1191;
            anIntArray1191 = ai;
        }

        if(indexedImage != null)
        {
            int l1 = 0;
            for(int j2 = 0; j2 < indexedImage.height; j2++)
            {
                for(int l2 = 0; l2 < indexedImage.width; l2++)
                    if(indexedImage.raster[l1++] != 0)
                    {
                        int i3 = l2 + 16 + indexedImage.drawOffsetX;
                        int j3 = j2 + 16 + indexedImage.drawOffsetY;
                        int k3 = i3 + (j3 << 7);
                        anIntArray1190[k3] = 0;
                    }

            }

        }
    }

    @Override
	public void run() {
        drawingFlames = true;
        try
        {
            long l = System.currentTimeMillis();
            int count = 0;
            int sleep = 20;
            while(running) 
            {
                flameTick++;
                calcFlamesPosition();
                calcFlamesPosition();
                doFlamesDrawing();
                if(++count > 10)
                {
                    long l1 = System.currentTimeMillis();
                    int k = (int)(l1 - l) / 10 - sleep;
                    sleep = 40 - k;
                    if(sleep < 5)
                        sleep = 5;
                    count = 0;
                    l = l1;
                }
                try
                {
                    Thread.sleep(sleep);
                }
                catch(Exception _ex) { }
            }
        }
        catch(Exception _ex) { }
        drawingFlames = false;
	}
}
