package com.jagex.entity.model;

import com.jagex.Constants;
import com.jagex.draw.DrawingArea;
import com.jagex.draw.Texture;

public class ModelRenderer {
	
	public static void nullLoader() {
        aBooleanArray1663 = null;
        aBooleanArray1664 = null;
        anIntArray1665 = null;
        anIntArray1666 = null;
        anIntArray1667 = null;
        anIntArray1668 = null;
        anIntArray1669 = null;
        anIntArray1670 = null;
        anIntArray1671 = null;
        anIntArrayArray1672 = null;
        anIntArray1673 = null;
        anIntArrayArray1674 = null;
        anIntArray1675 = null;
        anIntArray1676 = null;
        anIntArray1677 = null;
        modelIntArray3 = null;
        modelIntArray4 = null;
	}
	
	static {
        modelIntArray3 = Texture.palette;
        modelIntArray4 = Texture.anIntArray1469;
	}

    private static int[] anIntArray1671 = new int[1500];
    private static int[][] anIntArrayArray1672 = new int[1500][512];
    private static int[] anIntArray1673 = new int[12];
    private static int[][] anIntArrayArray1674 = new int[12][2000];
    private static int[] anIntArray1675 = new int[2000];
    private static int[] anIntArray1676 = new int[2000];
    private static int[] anIntArray1677 = new int[12];
    private static final int[] anIntArray1678 = new int[10];
    private static final int[] anIntArray1679 = new int[10];
    private static final int[] anIntArray1680 = new int[10];
    
    private static boolean[] aBooleanArray1663 = new boolean[4096];
    private static boolean[] aBooleanArray1664 = new boolean[4096];
    private static int[] anIntArray1665 = new int[4096];
    private static int[] anIntArray1666 = new int[4096];
    private static int[] anIntArray1667 = new int[4096];
    private static int[] anIntArray1668 = new int[4096];
    private static int[] anIntArray1669 = new int[4096];
    private static int[] anIntArray1670 = new int[4096];
    
    private static int[] modelIntArray3;
    private static int[] modelIntArray4;
    
    public static void render(Model model, int pitch, int yaw, int roll, int cameraPitch, int cameraX, int cameraY, int cameraZ)
    {
        int viewX = Texture.originViewX;
        int viewY = Texture.originViewY;
        int j2 = Model.SINE[pitch];
        int k2 = Model.COSINE[pitch];
        int l2 = Model.SINE[yaw];
        int i3 = Model.COSINE[yaw];
        int j3 = Model.SINE[roll];
        int k3 = Model.COSINE[roll];
        int l3 = Model.SINE[cameraPitch];
        int i4 = Model.COSINE[cameraPitch];
        int j4 = cameraY * l3 + cameraZ * i4 >> 16;
        for(int v = 0; v < model.vertexCount; v++)
        {
            int x = model.anIntArray1627[v];
            int y = model.anIntArray1628[v];
            int z = model.anIntArray1629[v];
            if(roll != 0)
            {
                int k5 = y * j3 + x * k3 >> 16;
                y = y * k3 - x * j3 >> 16;
                x = k5;
            }
            if(pitch != 0)
            {
                int l5 = y * k2 - z * j2 >> 16;
                z = y * j2 + z * k2 >> 16;
                y = l5;
            }
            if(yaw != 0)
            {
                int i6 = z * l2 + x * i3 >> 16;
                z = z * i3 - x * l2 >> 16;
                x = i6;
            }
            x += cameraX;
            y += cameraY;
            z += cameraZ;
            int j6 = y * i4 - z * l3 >> 16;
            z = y * l3 + z * i4 >> 16;
            y = j6;
            anIntArray1667[v] = z - j4;
            anIntArray1665[v] = viewX + (x << 9) / z;
            anIntArray1666[v] = viewY + (y << 9) / z;
            if(model.anInt1642 > 0)
            {
                anIntArray1668[v] = x;
                anIntArray1669[v] = y;
                anIntArray1670[v] = z;
            }
        }

        try
        {
            method483(model, false, false, 0);
        }
        catch(Exception _ex)
        {
        }
    }
    
	public static void method443(Model model, int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, int i2)
    {
        int j2 = l1 * i1 - j1 * l >> 16;
        int k2 = k1 * j + j2 * k >> 16;
        int l2 = model.anInt1650 * k >> 16;
        int i3 = k2 + l2;
        if(i3 <= 50 || k2 >= 3500)
            return;
        int j3 = l1 * l + j1 * i1 >> 16;
        int k3 = j3 - model.anInt1650 << Constants.VIEW_DISTANCE;
        if(k3 / i3 >= DrawingArea.centerY)
            return;
        int l3 = j3 + model.anInt1650 << Constants.VIEW_DISTANCE;
        if(l3 / i3 <= -DrawingArea.centerY)
            return;
        int i4 = k1 * k - j2 * j >> 16;
        int j4 = model.anInt1650 * j >> 16;
        int k4 = i4 + j4 << Constants.VIEW_DISTANCE;
        if(k4 / i3 <= -DrawingArea.anInt1387)
            return;
        int l4 = j4 + (model.modelHeight * k >> 16);
        int i5 = i4 - l4 << Constants.VIEW_DISTANCE;
        if(i5 / i3 >= DrawingArea.anInt1387)
            return;
        int j5 = l2 + (model.modelHeight * j >> 16);
        boolean flag = false;
        if(k2 - j5 <= 50)
            flag = true;
        boolean flag1 = false;
        if(i2 > 0 && ModelRenderer.aBoolean1684)
        {
            int k5 = k2 - l2;
            if(k5 <= 50)
                k5 = 50;
            if(j3 > 0)
            {
                k3 /= i3;
                l3 /= k5;
            } else
            {
                l3 /= i3;
                k3 /= k5;
            }
            if(i4 > 0)
            {
                i5 /= i3;
                k4 /= k5;
            } else
            {
                k4 /= i3;
                i5 /= k5;
            }
            int i6 = ModelRenderer.anInt1685 - Texture.originViewX;
            int k6 = ModelRenderer.anInt1686 - Texture.originViewY;
            if(i6 > k3 && i6 < l3 && k6 > i5 && k6 < k4)
                if(model.aBoolean1659)
                	ModelRenderer.anIntArray1688[ModelRenderer.anInt1687++] = i2;
                else
                    flag1 = true;
        }
        int l5 = Texture.originViewX;
        int j6 = Texture.originViewY;
        int l6 = 0;
        int i7 = 0;
        if(i != 0)
        {
            l6 = Model.SINE[i];
            i7 = Model.COSINE[i];
        }
        for(int j7 = 0; j7 < model.vertexCount; j7++)
        {
            int k7 = model.anIntArray1627[j7];
            int l7 = model.anIntArray1628[j7];
            int i8 = model.anIntArray1629[j7];
            if(i != 0)
            {
                int j8 = i8 * l6 + k7 * i7 >> 16;
                i8 = i8 * i7 - k7 * l6 >> 16;
                k7 = j8;
            }
            k7 += j1;
            l7 += k1;
            i8 += l1;
            int k8 = i8 * l + k7 * i1 >> 16;
            i8 = i8 * i1 - k7 * l >> 16;
            k7 = k8;
            k8 = l7 * k - i8 * j >> 16;
            i8 = l7 * j + i8 * k >> 16;
            l7 = k8;
            anIntArray1667[j7] = i8 - k2;
            if(i8 >= 50)
            {
                anIntArray1665[j7] = l5 + (k7 << Constants.VIEW_DISTANCE) / i8;
                anIntArray1666[j7] = j6 + (l7 << Constants.VIEW_DISTANCE) / i8;
            } else
            {
                anIntArray1665[j7] = -5000;
                flag = true;
            }
            if(flag || model.anInt1642 > 0)
            {
                anIntArray1668[j7] = k7;
                anIntArray1669[j7] = l7;
                anIntArray1670[j7] = i8;
            }
        }

        try
        {
            method483(model, flag, flag1, i2);
        }
        catch(Exception _ex)
        {
        }
    }
	
    private static void method483(Model model, boolean flag, boolean flag1, int i)
    {
        for(int j = 0; j < model.anInt1652; j++)
            anIntArray1671[j] = 0;

        for(int k = 0; k < model.anInt1630; k++)
            if(model.anIntArray1637 == null || model.anIntArray1637[k] != -1)
            {
                int l = model.anIntArray1631[k];
                int k1 = model.anIntArray1632[k];
                int j2 = model.anIntArray1633[k];
                int i3 = anIntArray1665[l];
                int l3 = anIntArray1665[k1];
                int k4 = anIntArray1665[j2];
                if(flag && (i3 == -5000 || l3 == -5000 || k4 == -5000))
                {
                    aBooleanArray1664[k] = true;
                    int j5 = (anIntArray1667[l] + anIntArray1667[k1] + anIntArray1667[j2]) / 3 + model.anInt1653;
                    anIntArrayArray1672[j5][anIntArray1671[j5]++] = k;
                } else
                {
                    if(flag1 && method486(ModelRenderer.anInt1685, ModelRenderer.anInt1686, anIntArray1666[l], anIntArray1666[k1], anIntArray1666[j2], i3, l3, k4))
                    {
                    	ModelRenderer.anIntArray1688[ModelRenderer.anInt1687++] = i;
                        flag1 = false;
                    }
                    if((i3 - l3) * (anIntArray1666[j2] - anIntArray1666[k1]) - (anIntArray1666[l] - anIntArray1666[k1]) * (k4 - l3) > 0)
                    {
                        aBooleanArray1664[k] = false;
                        aBooleanArray1663[k] = i3 < 0 || l3 < 0 || k4 < 0 || i3 > DrawingArea.centerX || l3 > DrawingArea.centerX || k4 > DrawingArea.centerX;
                        int k5 = (anIntArray1667[l] + anIntArray1667[k1] + anIntArray1667[j2]) / 3 + model.anInt1653;
                        anIntArrayArray1672[k5][anIntArray1671[k5]++] = k;
                    }
                }
            }

        if(model.anIntArray1638 == null)
        {
            for(int i1 = model.anInt1652 - 1; i1 >= 0; i1--)
            {
                int l1 = anIntArray1671[i1];
                if(l1 > 0)
                {
                    int ai[] = anIntArrayArray1672[i1];
                    for(int j3 = 0; j3 < l1; j3++)
                        method484(model, ai[j3]);

                }
            }

            return;
        }
        for(int j1 = 0; j1 < 12; j1++)
        {
            anIntArray1673[j1] = 0;
            anIntArray1677[j1] = 0;
        }

        for(int i2 = model.anInt1652 - 1; i2 >= 0; i2--)
        {
            int k2 = anIntArray1671[i2];
            if(k2 > 0)
            {
                int ai1[] = anIntArrayArray1672[i2];
                for(int i4 = 0; i4 < k2; i4++)
                {
                    int l4 = ai1[i4];
                    int l5 = model.anIntArray1638[l4];
                    int j6 = anIntArray1673[l5]++;
                    anIntArrayArray1674[l5][j6] = l4;
                    if(l5 < 10)
                        anIntArray1677[l5] += i2;
                    else
                    if(l5 == 10)
                        anIntArray1675[j6] = i2;
                    else
                        anIntArray1676[j6] = i2;
                }

            }
        }

        int l2 = 0;
        if(anIntArray1673[1] > 0 || anIntArray1673[2] > 0)
            l2 = (anIntArray1677[1] + anIntArray1677[2]) / (anIntArray1673[1] + anIntArray1673[2]);
        int k3 = 0;
        if(anIntArray1673[3] > 0 || anIntArray1673[4] > 0)
            k3 = (anIntArray1677[3] + anIntArray1677[4]) / (anIntArray1673[3] + anIntArray1673[4]);
        int j4 = 0;
        if(anIntArray1673[6] > 0 || anIntArray1673[8] > 0)
            j4 = (anIntArray1677[6] + anIntArray1677[8]) / (anIntArray1673[6] + anIntArray1673[8]);
        int i6 = 0;
        int k6 = anIntArray1673[10];
        int ai2[] = anIntArrayArray1674[10];
        int ai3[] = anIntArray1675;
        if(i6 == k6)
        {
            i6 = 0;
            k6 = anIntArray1673[11];
            ai2 = anIntArrayArray1674[11];
            ai3 = anIntArray1676;
        }
        int i5;
        if(i6 < k6)
            i5 = ai3[i6];
        else
            i5 = -1000;
        for(int l6 = 0; l6 < 10; l6++)
        {
            while(l6 == 0 && i5 > l2) 
            {
                method484(model, ai2[i6++]);
                if(i6 == k6 && ai2 != anIntArrayArray1674[11])
                {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    ai2 = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if(i6 < k6)
                    i5 = ai3[i6];
                else
                    i5 = -1000;
            }
            while(l6 == 3 && i5 > k3) 
            {
                method484(model, ai2[i6++]);
                if(i6 == k6 && ai2 != anIntArrayArray1674[11])
                {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    ai2 = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if(i6 < k6)
                    i5 = ai3[i6];
                else
                    i5 = -1000;
            }
            while(l6 == 5 && i5 > j4) 
            {
                method484(model, ai2[i6++]);
                if(i6 == k6 && ai2 != anIntArrayArray1674[11])
                {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    ai2 = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if(i6 < k6)
                    i5 = ai3[i6];
                else
                    i5 = -1000;
            }
            int i7 = anIntArray1673[l6];
            int ai4[] = anIntArrayArray1674[l6];
            for(int j7 = 0; j7 < i7; j7++)
                method484(model, ai4[j7]);

        }

        while(i5 != -1000) 
        {
            method484(model, ai2[i6++]);
            if(i6 == k6 && ai2 != anIntArrayArray1674[11])
            {
                i6 = 0;
                ai2 = anIntArrayArray1674[11];
                k6 = anIntArray1673[11];
                ai3 = anIntArray1676;
            }
            if(i6 < k6)
                i5 = ai3[i6];
            else
                i5 = -1000;
        }
    }

    private static void method484(Model model, int i)
    {
        if(aBooleanArray1664[i])
        {
            method485(model, i);
            return;
        }
        int j = model.anIntArray1631[i];
        int k = model.anIntArray1632[i];
        int l = model.anIntArray1633[i];
        Texture.aBoolean1462 = aBooleanArray1663[i];
        if(model.anIntArray1639 == null)
            Texture.currentAlpha = 0;
        else
            Texture.currentAlpha = model.anIntArray1639[i];
        int i1;
        if(model.anIntArray1637 == null)
            i1 = 0;
        else
            i1 = model.anIntArray1637[i] & 3;
        if(i1 == 0)
        {
            Texture.method374(anIntArray1666[j], anIntArray1666[k], anIntArray1666[l], anIntArray1665[j], anIntArray1665[k], anIntArray1665[l], model.anIntArray1634[i], model.anIntArray1635[i], model.anIntArray1636[i]);
            return;
        }
        if(i1 == 1)
        {
            Texture.method376(anIntArray1666[j], anIntArray1666[k], anIntArray1666[l], anIntArray1665[j], anIntArray1665[k], anIntArray1665[l], modelIntArray3[model.anIntArray1634[i]]);
            return;
        }
        if(i1 == 2)
        {
            int j1 = model.anIntArray1637[i] >> 2;
            int l1 = model.anIntArray1643[j1];
            int j2 = model.anIntArray1644[j1];
            int l2 = model.anIntArray1645[j1];
            Texture.method378(anIntArray1666[j], anIntArray1666[k], anIntArray1666[l], anIntArray1665[j], anIntArray1665[k], anIntArray1665[l], model.anIntArray1634[i], model.anIntArray1635[i], model.anIntArray1636[i], anIntArray1668[l1], anIntArray1668[j2], anIntArray1668[l2], anIntArray1669[l1], anIntArray1669[j2], anIntArray1669[l2], anIntArray1670[l1], anIntArray1670[j2], anIntArray1670[l2], model.anIntArray1640[i]);
            return;
        }
        if(i1 == 3)
        {
            int k1 = model.anIntArray1637[i] >> 2;
            int i2 = model.anIntArray1643[k1];
            int k2 = model.anIntArray1644[k1];
            int i3 = model.anIntArray1645[k1];
            Texture.method378(anIntArray1666[j], anIntArray1666[k], anIntArray1666[l], anIntArray1665[j], anIntArray1665[k], anIntArray1665[l], model.anIntArray1634[i], model.anIntArray1634[i], model.anIntArray1634[i], anIntArray1668[i2], anIntArray1668[k2], anIntArray1668[i3], anIntArray1669[i2], anIntArray1669[k2], anIntArray1669[i3], anIntArray1670[i2], anIntArray1670[k2], anIntArray1670[i3], model.anIntArray1640[i]);
        }
    }

    private static void method485(Model model, int i)
    {
        int j = Texture.originViewX;
        int k = Texture.originViewY;
        int l = 0;
        int i1 = model.anIntArray1631[i];
        int j1 = model.anIntArray1632[i];
        int k1 = model.anIntArray1633[i];
        int l1 = anIntArray1670[i1];
        int i2 = anIntArray1670[j1];
        int j2 = anIntArray1670[k1];
        if(l1 >= 50)
        {
            anIntArray1678[l] = anIntArray1665[i1];
            anIntArray1679[l] = anIntArray1666[i1];
            anIntArray1680[l++] = model.anIntArray1634[i];
        } else
        {
            int k2 = anIntArray1668[i1];
            int k3 = anIntArray1669[i1];
            int k4 = model.anIntArray1634[i];
            if(j2 >= 50)
            {
                int k5 = (50 - l1) * modelIntArray4[j2 - l1];
                anIntArray1678[l] = j + (k2 + ((anIntArray1668[k1] - k2) * k5 >> 16) << Constants.VIEW_DISTANCE) / 50;
                anIntArray1679[l] = k + (k3 + ((anIntArray1669[k1] - k3) * k5 >> 16) << Constants.VIEW_DISTANCE) / 50;
                anIntArray1680[l++] = k4 + ((model.anIntArray1636[i] - k4) * k5 >> 16);
            }
            if(i2 >= 50)
            {
                int l5 = (50 - l1) * modelIntArray4[i2 - l1];
                anIntArray1678[l] = j + (k2 + ((anIntArray1668[j1] - k2) * l5 >> 16) << Constants.VIEW_DISTANCE) / 50;
                anIntArray1679[l] = k + (k3 + ((anIntArray1669[j1] - k3) * l5 >> 16) << Constants.VIEW_DISTANCE) / 50;
                anIntArray1680[l++] = k4 + ((model.anIntArray1635[i] - k4) * l5 >> 16);
            }
        }
        if(i2 >= 50)
        {
            anIntArray1678[l] = anIntArray1665[j1];
            anIntArray1679[l] = anIntArray1666[j1];
            anIntArray1680[l++] = model.anIntArray1635[i];
        } else
        {
            int l2 = anIntArray1668[j1];
            int l3 = anIntArray1669[j1];
            int l4 = model.anIntArray1635[i];
            if(l1 >= 50)
            {
                int i6 = (50 - i2) * modelIntArray4[l1 - i2];
                anIntArray1678[l] = j + (l2 + ((anIntArray1668[i1] - l2) * i6 >> 16) << Constants.VIEW_DISTANCE) / 50;
                anIntArray1679[l] = k + (l3 + ((anIntArray1669[i1] - l3) * i6 >> 16) << Constants.VIEW_DISTANCE) / 50;
                anIntArray1680[l++] = l4 + ((model.anIntArray1634[i] - l4) * i6 >> 16);
            }
            if(j2 >= 50)
            {
                int j6 = (50 - i2) * modelIntArray4[j2 - i2];
                anIntArray1678[l] = j + (l2 + ((anIntArray1668[k1] - l2) * j6 >> 16) << Constants.VIEW_DISTANCE) / 50;
                anIntArray1679[l] = k + (l3 + ((anIntArray1669[k1] - l3) * j6 >> 16) << Constants.VIEW_DISTANCE) / 50;
                anIntArray1680[l++] = l4 + ((model.anIntArray1636[i] - l4) * j6 >> 16);
            }
        }
        if(j2 >= 50)
        {
            anIntArray1678[l] = anIntArray1665[k1];
            anIntArray1679[l] = anIntArray1666[k1];
            anIntArray1680[l++] = model.anIntArray1636[i];
        } else
        {
            int i3 = anIntArray1668[k1];
            int i4 = anIntArray1669[k1];
            int i5 = model.anIntArray1636[i];
            if(i2 >= 50)
            {
                int k6 = (50 - j2) * modelIntArray4[i2 - j2];
                anIntArray1678[l] = j + (i3 + ((anIntArray1668[j1] - i3) * k6 >> 16) << Constants.VIEW_DISTANCE) / 50;
                anIntArray1679[l] = k + (i4 + ((anIntArray1669[j1] - i4) * k6 >> 16) << Constants.VIEW_DISTANCE) / 50;
                anIntArray1680[l++] = i5 + ((model.anIntArray1635[i] - i5) * k6 >> 16);
            }
            if(l1 >= 50)
            {
                int l6 = (50 - j2) * modelIntArray4[l1 - j2];
                anIntArray1678[l] = j + (i3 + ((anIntArray1668[i1] - i3) * l6 >> 16) << Constants.VIEW_DISTANCE) / 50;
                anIntArray1679[l] = k + (i4 + ((anIntArray1669[i1] - i4) * l6 >> 16) << Constants.VIEW_DISTANCE) / 50;
                anIntArray1680[l++] = i5 + ((model.anIntArray1634[i] - i5) * l6 >> 16);
            }
        }
        int j3 = anIntArray1678[0];
        int j4 = anIntArray1678[1];
        int j5 = anIntArray1678[2];
        int i7 = anIntArray1679[0];
        int j7 = anIntArray1679[1];
        int k7 = anIntArray1679[2];
        if((j3 - j4) * (k7 - j7) - (i7 - j7) * (j5 - j4) > 0)
        {
            Texture.aBoolean1462 = false;
            if(l == 3)
            {
                if(j3 < 0 || j4 < 0 || j5 < 0 || j3 > DrawingArea.centerX || j4 > DrawingArea.centerX || j5 > DrawingArea.centerX)
                    Texture.aBoolean1462 = true;
                int l7;
                if(model.anIntArray1637 == null)
                    l7 = 0;
                else
                    l7 = model.anIntArray1637[i] & 3;
                if(l7 == 0)
                    Texture.method374(i7, j7, k7, j3, j4, j5, anIntArray1680[0], anIntArray1680[1], anIntArray1680[2]);
                else
                if(l7 == 1)
                    Texture.method376(i7, j7, k7, j3, j4, j5, modelIntArray3[model.anIntArray1634[i]]);
                else
                if(l7 == 2)
                {
                    int j8 = model.anIntArray1637[i] >> 2;
                    int k9 = model.anIntArray1643[j8];
                    int k10 = model.anIntArray1644[j8];
                    int k11 = model.anIntArray1645[j8];
                    Texture.method378(i7, j7, k7, j3, j4, j5, anIntArray1680[0], anIntArray1680[1], anIntArray1680[2], anIntArray1668[k9], anIntArray1668[k10], anIntArray1668[k11], anIntArray1669[k9], anIntArray1669[k10], anIntArray1669[k11], anIntArray1670[k9], anIntArray1670[k10], anIntArray1670[k11], model.anIntArray1640[i]);
                } else
                if(l7 == 3)
                {
                    int k8 = model.anIntArray1637[i] >> 2;
                    int l9 = model.anIntArray1643[k8];
                    int l10 = model.anIntArray1644[k8];
                    int l11 = model.anIntArray1645[k8];
                    Texture.method378(i7, j7, k7, j3, j4, j5, model.anIntArray1634[i], model.anIntArray1634[i], model.anIntArray1634[i], anIntArray1668[l9], anIntArray1668[l10], anIntArray1668[l11], anIntArray1669[l9], anIntArray1669[l10], anIntArray1669[l11], anIntArray1670[l9], anIntArray1670[l10], anIntArray1670[l11], model.anIntArray1640[i]);
                }
            }
            if(l == 4)
            {
                if(j3 < 0 || j4 < 0 || j5 < 0 || j3 > DrawingArea.centerX || j4 > DrawingArea.centerX || j5 > DrawingArea.centerX || anIntArray1678[3] < 0 || anIntArray1678[3] > DrawingArea.centerX)
                    Texture.aBoolean1462 = true;
                int i8;
                if(model.anIntArray1637 == null)
                    i8 = 0;
                else
                    i8 = model.anIntArray1637[i] & 3;
                if(i8 == 0)
                {
                    Texture.method374(i7, j7, k7, j3, j4, j5, anIntArray1680[0], anIntArray1680[1], anIntArray1680[2]);
                    Texture.method374(i7, k7, anIntArray1679[3], j3, j5, anIntArray1678[3], anIntArray1680[0], anIntArray1680[2], anIntArray1680[3]);
                    return;
                }
                if(i8 == 1)
                {
                    int l8 = modelIntArray3[model.anIntArray1634[i]];
                    Texture.method376(i7, j7, k7, j3, j4, j5, l8);
                    Texture.method376(i7, k7, anIntArray1679[3], j3, j5, anIntArray1678[3], l8);
                    return;
                }
                if(i8 == 2)
                {
                    int i9 = model.anIntArray1637[i] >> 2;
                    int i10 = model.anIntArray1643[i9];
                    int i11 = model.anIntArray1644[i9];
                    int i12 = model.anIntArray1645[i9];
                    Texture.method378(i7, j7, k7, j3, j4, j5, anIntArray1680[0], anIntArray1680[1], anIntArray1680[2], anIntArray1668[i10], anIntArray1668[i11], anIntArray1668[i12], anIntArray1669[i10], anIntArray1669[i11], anIntArray1669[i12], anIntArray1670[i10], anIntArray1670[i11], anIntArray1670[i12], model.anIntArray1640[i]);
                    Texture.method378(i7, k7, anIntArray1679[3], j3, j5, anIntArray1678[3], anIntArray1680[0], anIntArray1680[2], anIntArray1680[3], anIntArray1668[i10], anIntArray1668[i11], anIntArray1668[i12], anIntArray1669[i10], anIntArray1669[i11], anIntArray1669[i12], anIntArray1670[i10], anIntArray1670[i11], anIntArray1670[i12], model.anIntArray1640[i]);
                    return;
                }
                if(i8 == 3)
                {
                    int j9 = model.anIntArray1637[i] >> 2;
                    int j10 = model.anIntArray1643[j9];
                    int j11 = model.anIntArray1644[j9];
                    int j12 = model.anIntArray1645[j9];
                    Texture.method378(i7, j7, k7, j3, j4, j5, model.anIntArray1634[i], model.anIntArray1634[i], model.anIntArray1634[i], anIntArray1668[j10], anIntArray1668[j11], anIntArray1668[j12], anIntArray1669[j10], anIntArray1669[j11], anIntArray1669[j12], anIntArray1670[j10], anIntArray1670[j11], anIntArray1670[j12], model.anIntArray1640[i]);
                    Texture.method378(i7, k7, anIntArray1679[3], j3, j5, anIntArray1678[3], model.anIntArray1634[i], model.anIntArray1634[i], model.anIntArray1634[i], anIntArray1668[j10], anIntArray1668[j11], anIntArray1668[j12], anIntArray1669[j10], anIntArray1669[j11], anIntArray1669[j12], anIntArray1670[j10], anIntArray1670[j11], anIntArray1670[j12], model.anIntArray1640[i]);
                }
            }
        }
    }
    
    private static boolean method486(int i, int j, int k, int l, int i1, int j1, int k1,
            int l1)
    {
        if(j < k && j < l && j < i1)
            return false;
        if(j > k && j > l && j > i1)
            return false;
        return !(i < j1 && i < k1 && i < l1) && (i <= j1 || i <= k1 || i <= l1);
    }

	public static final int[] anIntArray1688 = new int[1000];
	public static int anInt1687;
	public static int anInt1686;
	public static int anInt1685;
	public static boolean aBoolean1684;
}
