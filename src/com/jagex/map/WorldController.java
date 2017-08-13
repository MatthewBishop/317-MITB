package com.jagex.map;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.Constants;
import com.jagex.draw.DrawingArea;
import com.jagex.draw.Texture;
import com.jagex.entity.Animable;
import com.jagex.entity.model.Class33;
import com.jagex.entity.model.Model;
import com.jagex.link.Deque;


public final class WorldController {

    public WorldController(int ai[][][])
    {
        int i = 104;//was parameter
        int j = 104;//was parameter
        int k = 4;//was parameter
        this.aBoolean434 = true;
        this.obj5Cache = new GameObject[5000];
        this.anIntArray486 = new int[10000];
        this.anIntArray487 = new int[10000];
        this.anInt437 = k;
        this.anInt438 = j;
        this.anInt439 = i;
            this.tiles = new Ground[k][j][i];
            this.levelTileCycles = new int[k][j + 1][i + 1];
            this.anIntArrayArrayArray440 = ai;
            this.initToNull();
    }

    public static void nullLoader()
    {
        WorldController.aClass28Array462 = null;
        Occluders.destroy();
        WorldController.aClass19_477 = null;
        WorldController.aBooleanArrayArrayArrayArray491 = null;
        WorldController.visibilityMap = null;
    }

    public void initToNull()
    {
        for(int j = 0; j < this.anInt437; j++)
        {
            for(int k = 0; k < this.anInt438; k++)
            {
                for(int i1 = 0; i1 < this.anInt439; i1++)
                    this.tiles[j][k][i1] = null;

            }

        }
        Occluders.clear();
        for(int k1 = 0; k1 < this.obj5CacheCurrPos; k1++)
            this.obj5Cache[k1] = null;

        this.obj5CacheCurrPos = 0;
        for(int l1 = 0; l1 < WorldController.aClass28Array462.length; l1++)
            WorldController.aClass28Array462[l1] = null;

    }

    public void method275(int i)
    {
        this.anInt442 = i;
        for(int k = 0; k < this.anInt438; k++)
        {
            for(int l = 0; l < this.anInt439; l++)
                if(this.tiles[i][k][l] == null)
                    this.tiles[i][k][l] = new Ground(i, k, l);

        }

    }

    public void method276(int i, int j)
    {
        Ground class30_sub3 = this.tiles[0][j][i];
        for(int l = 0; l < 3; l++)
        {
            Ground class30_sub3_1 = this.tiles[l][j][i] = this.tiles[l + 1][j][i];
            if(class30_sub3_1 != null)
            {
                class30_sub3_1.level--;
                for(int j1 = 0; j1 < class30_sub3_1.objectCount; j1++)
                {
                    GameObject class28 = class30_sub3_1.locs[j1];
                    if((class28.key >> 29 & 3) == 2 && class28.minX == j && class28.minY == i)
                        class28.plane--;
                }

            }
        }
        if(this.tiles[0][j][i] == null)
            this.tiles[0][j][i] = new Ground(0, j, i);
        this.tiles[0][j][i].bridge = class30_sub3;
        this.tiles[3][j][i] = null;
    }

    public void setCollisionPlane(int i, int j, int k, int l)
    {
        Ground class30_sub3 = this.tiles[i][j][k];
        if(class30_sub3 != null)
        {
            this.tiles[i][j][k].anInt1321 = l;
        }
    }

    /*
     method279(level, x, z, 0, 0, -1, tileHeight, tileHeightEast, tileHeightNorthEast, tileHeightNorth,
										ObjectManager.light(i_66_, i_73_), ObjectManager.light(i_66_, i_70_), ObjectManager.light(i_66_, i_71_), ObjectManager.light(i_66_, i_72_), 0, 0, 0, 0, underlayMinimapColor, 0)
     
     *worldController.method279(z, centreX, centreY, shape, rotation, overlay_texture, centreHeight,
											eastHeight, northEastHeight, northHeight,
											
											ObjectManager.light(underlay_hsl_real, centreLight), ObjectManager.light(underlay_hsl_real, eastLight),
											ObjectManager.light(underlay_hsl_real, northEastLight),
											
											ObjectManager.light(underlay_hsl_real, northLight),
											
											 this.checkedLight(overlay_hsl, centreLight),
											this.checkedLight(overlay_hsl, eastLight), this.checkedLight(overlay_hsl, northEastLight),
											this.checkedLight(overlay_hsl, northLight), i22, overlay_rgb);
     */
    public void addTile(int z, int x, int y, int shape, int rotation, int overlay_texture, int centreHeight, 
            int eastHeight, int northEastHeight, int northHeight, int underlayCentreLight, int underlayEastLight, int underlayNorthEastLight, int underlayNorthLight, 
            int overlayCentreLight, int overlayEastLight, int overlayNorthEastLight, int overlayNorthLight, int underlay_rgb, int overlay_rgb)
    {
        if(shape == 0)
        {
            TileUnderlay underlay = new TileUnderlay(underlayCentreLight, underlayEastLight, underlayNorthEastLight, underlayNorthLight, -1, underlay_rgb, false);
            for(int i5 = z; i5 >= 0; i5--)
                if(this.tiles[i5][x][y] == null)
                    this.tiles[i5][x][y] = new Ground(i5, x, y);

            this.tiles[z][x][y].underlay = underlay;
            return;
        }
        if(shape == 1)
        {
            TileUnderlay underlay = new TileUnderlay(overlayCentreLight, overlayEastLight, overlayNorthEastLight, overlayNorthLight, overlay_texture, overlay_rgb, centreHeight == eastHeight && centreHeight == northEastHeight && centreHeight == northHeight);
            for(int j5 = z; j5 >= 0; j5--)
                if(this.tiles[j5][x][y] == null)
                    this.tiles[j5][x][y] = new Ground(j5, x, y);

            this.tiles[z][x][y].underlay = underlay;
            return;
        }
        Class40 overlay = new Class40(y, overlayCentreLight, underlayNorthLight, northEastHeight, overlay_texture, overlayNorthEastLight, rotation, underlayCentreLight, underlay_rgb, underlayNorthEastLight, northHeight, eastHeight, centreHeight, shape, overlayNorthLight, overlayEastLight, underlayEastLight, x, overlay_rgb);
        for(int k5 = z; k5 >= 0; k5--)
            if(this.tiles[k5][x][y] == null)
                this.tiles[k5][x][y] = new Ground(k5, x, y);

        this.tiles[z][x][y].overlay = overlay;
    }

    public void method280(int i, int j, int k, Animable class30_sub2_sub4, byte byte0, int i1,
                          int j1)
    {
        if(class30_sub2_sub4 == null)
            return;
        Object3 class49 = new Object3();
        class49.aClass30_Sub2_Sub4_814 = class30_sub2_sub4;
        class49.anInt812 = j1 * 128 + 64;
        class49.anInt813 = k * 128 + 64;
        class49.anInt811 = j;
        class49.uid = i1;
        class49.aByte816 = byte0;
        if(this.tiles[i][j1][k] == null)
            this.tiles[i][j1][k] = new Ground(i, j1, k);
        this.tiles[i][j1][k].obj3 = class49;
    }

    public void method281(int i, int j, Animable class30_sub2_sub4, int k, Animable class30_sub2_sub4_1, Animable class30_sub2_sub4_2,
                          int l, int i1)
    {
        Object4 object4 = new Object4();
        object4.aClass30_Sub2_Sub4_48 = class30_sub2_sub4_2;
        object4.anInt46 = i * 128 + 64;
        object4.anInt47 = i1 * 128 + 64;
        object4.anInt45 = k;
        object4.uid = j;
        object4.aClass30_Sub2_Sub4_49 = class30_sub2_sub4;
        object4.aClass30_Sub2_Sub4_50 = class30_sub2_sub4_1;
        int j1 = 0;
        Ground class30_sub3 = this.tiles[l][i][i1];
        if(class30_sub3 != null)
        {
            for(int k1 = 0; k1 < class30_sub3.objectCount; k1++)
                if(class30_sub3.locs[k1].renderable instanceof Model)
                {
                    int l1 = ((Model)class30_sub3.locs[k1].renderable).anInt1654;
                    if(l1 > j1)
                        j1 = l1;
                }

        }
        object4.anInt52 = j1;
        if(this.tiles[l][i][i1] == null)
            this.tiles[l][i][i1] = new Ground(l, i, i1);
        this.tiles[l][i][i1].obj4 = object4;
    }

    public void method282(int i, Animable class30_sub2_sub4, int j, int k, byte byte0, int l,
                          Animable class30_sub2_sub4_1, int i1, int j1, int k1)
    {
        if(class30_sub2_sub4 == null && class30_sub2_sub4_1 == null)
            return;
        Object1 object1 = new Object1();
        object1.uid = j;
        object1.aByte281 = byte0;
        object1.anInt274 = l * 128 + 64;
        object1.anInt275 = k * 128 + 64;
        object1.anInt273 = i1;
        object1.aClass30_Sub2_Sub4_278 = class30_sub2_sub4;
        object1.aClass30_Sub2_Sub4_279 = class30_sub2_sub4_1;
        object1.orientation = i;
        object1.orientation1 = j1;
        for(int l1 = k1; l1 >= 0; l1--)
            if(this.tiles[l1][l][k] == null)
                this.tiles[l1][l][k] = new Ground(l1, l, k);

        this.tiles[k1][l][k].obj1 = object1;
    }

    public void method283(int i, int j, int k, int i1, int j1, int k1,
                          Animable class30_sub2_sub4, int l1, byte byte0, int i2, int j2)
    {
        if(class30_sub2_sub4 == null)
            return;
        Object2 class26 = new Object2();
        class26.uid = i;
        class26.aByte506 = byte0;
        class26.anInt500 = l1 * 128 + 64 + j1;
        class26.anInt501 = j * 128 + 64 + i2;
        class26.anInt499 = k1;
        class26.aClass30_Sub2_Sub4_504 = class30_sub2_sub4;
        class26.anInt502 = j2;
        class26.anInt503 = k;
        for(int k2 = i1; k2 >= 0; k2--)
            if(this.tiles[k2][l1][j] == null)
                this.tiles[k2][l1][j] = new Ground(k2, l1, j);

        this.tiles[i1][l1][j].obj2 = class26;
    }

    public boolean method284(int i, byte byte0, int j, int k, Animable class30_sub2_sub4, int l, int i1,
                             int j1, int k1, int l1)
    {
        if(class30_sub2_sub4 == null)
        {
            return true;
        } else
        {
            int i2 = l1 * 128 + 64 * l;
            int j2 = k1 * 128 + 64 * k;
            return this.method287(i1, l1, k1, l, k, i2, j2, j, class30_sub2_sub4, j1, false, i, byte0);
        }
    }

    public boolean method285(int i, int j, int k, int l, int i1, int j1,
                             int k1, Animable class30_sub2_sub4, boolean flag)
    {
        if(class30_sub2_sub4 == null)
            return true;
        int l1 = k1 - j1;
        int i2 = i1 - j1;
        int j2 = k1 + j1;
        int k2 = i1 + j1;
        if(flag)
        {
            if(j > 640 && j < 1408)
                k2 += 128;
            if(j > 1152 && j < 1920)
                j2 += 128;
            if(j > 1664 || j < 384)
                i2 -= 128;
            if(j > 128 && j < 896)
                l1 -= 128;
        }
        l1 /= 128;
        i2 /= 128;
        j2 /= 128;
        k2 /= 128;
        return this.method287(i, l1, i2, (j2 - l1) + 1, (k2 - i2) + 1, k1, i1, k, class30_sub2_sub4, j, true, l, (byte)0);
    }

    public boolean method286(int j, int k, Animable class30_sub2_sub4, int l, int i1, int j1,
                             int k1, int l1, int i2, int j2, int k2)
    {
        return class30_sub2_sub4 == null || this.method287(j, l1, k2, (i2 - l1) + 1, (i1 - k2) + 1, j1, k, k1, class30_sub2_sub4, l, true, j2, (byte) 0);
    }

    private boolean method287(int i, int minX, int minY, int l, int i1, int j1, int k1,
            int l1, Animable class30_sub2_sub4, int i2, boolean flag, int j2, byte byte0)
    {
        for(int k2 = minX; k2 < minX + l; k2++)
        {
            for(int l2 = minY; l2 < minY + i1; l2++)
            {
                if(k2 < 0 || l2 < 0 || k2 >= this.anInt438 || l2 >= this.anInt439)
                    return false;
                Ground class30_sub3 = this.tiles[i][k2][l2];
                if(class30_sub3 != null && class30_sub3.objectCount >= 5)
                    return false;
            }

        }

        GameObject object = new GameObject();
        object.key = j2;
        object.config = byte0;
        object.plane = i;
        object.centreX = j1;
        object.centreY = k1;
        object.renderHeight = l1;
        object.renderable = class30_sub2_sub4;
        object.yaw = i2;
        object.minX = minX;
        object.minY = minY;
        object.maxX = (minX + l) - 1;
        object.maxY = (minY + i1) - 1;
        for(int i3 = minX; i3 < minX + l; i3++)
        {
            for(int j3 = minY; j3 < minY + i1; j3++)
            {
                int k3 = 0;
                if(i3 > minX)
                    k3++;
                if(i3 < (minX + l) - 1)
                    k3 += 4;
                if(j3 > minY)
                    k3 += 8;
                if(j3 < (minY + i1) - 1)
                    k3 += 2;
                for(int l3 = i; l3 >= 0; l3--)
                    if(this.tiles[l3][i3][j3] == null)
                        this.tiles[l3][i3][j3] = new Ground(l3, i3, j3);

                Ground class30_sub3_1 = this.tiles[i][i3][j3];
                class30_sub3_1.locs[class30_sub3_1.objectCount] = object;
                class30_sub3_1.locFlags[class30_sub3_1.objectCount] = k3;
                class30_sub3_1.attributes |= k3;
                class30_sub3_1.objectCount++;
            }

        }

        if(flag)
            this.obj5Cache[this.obj5CacheCurrPos++] = object;
        return true;
    }

    public void clearObj5Cache()
    {
        for(int i = 0; i < this.obj5CacheCurrPos; i++)
        {
            GameObject gameObject = this.obj5Cache[i];
            this.method289(gameObject);
            this.obj5Cache[i] = null;
        }

        this.obj5CacheCurrPos = 0;
    }

    private void method289(GameObject class28)
    {
        for(int j = class28.minX; j <= class28.maxX; j++)
        {
            for(int k = class28.minY; k <= class28.maxY; k++)
            {
                Ground class30_sub3 = this.tiles[class28.plane][j][k];
                if(class30_sub3 != null)
                {
                    for(int l = 0; l < class30_sub3.objectCount; l++)
                    {
                        if(class30_sub3.locs[l] != class28)
                            continue;
                        class30_sub3.objectCount--;
                        for(int i1 = l; i1 < class30_sub3.objectCount; i1++)
                        {
                            class30_sub3.locs[i1] = class30_sub3.locs[i1 + 1];
                            class30_sub3.locFlags[i1] = class30_sub3.locFlags[i1 + 1];
                        }

                        class30_sub3.locs[class30_sub3.objectCount] = null;
                        break;
                    }

                    class30_sub3.attributes = 0;
                    for(int j1 = 0; j1 < class30_sub3.objectCount; j1++)
                        class30_sub3.attributes |= class30_sub3.locFlags[j1];

                }
            }

        }

    }

    public void method290(int i, int k, int l, int i1)
    {
        Ground class30_sub3 = this.tiles[i1][l][i];
        if(class30_sub3 == null)
            return;
        Object2 class26 = class30_sub3.obj2;
        if(class26 != null)
        {
            int j1 = l * 128 + 64;
            int k1 = i * 128 + 64;
            class26.anInt500 = j1 + ((class26.anInt500 - j1) * k) / 16;
            class26.anInt501 = k1 + ((class26.anInt501 - k1) * k) / 16;
        }
    }

    public void method291(int i, int j, int k, byte byte0)
    {
        Ground class30_sub3 = this.tiles[j][i][k];
        if(byte0 != -119)
            this.aBoolean434 = !this.aBoolean434;
        if(class30_sub3 != null)
        {
            class30_sub3.obj1 = null;
        }
    }

    public void method292(int j, int k, int l)
    {
        Ground class30_sub3 = this.tiles[k][l][j];
        if(class30_sub3 != null)
        {
            class30_sub3.obj2 = null;
        }
    }

    public void method293(int i, int k, int l)
    {
        Ground class30_sub3 = this.tiles[i][k][l];
        if(class30_sub3 == null)
            return;
        for(int j1 = 0; j1 < class30_sub3.objectCount; j1++)
        {
            GameObject class28 = class30_sub3.locs[j1];
            if((class28.key >> 29 & 3) == 2 && class28.minX == k && class28.minY == l)
            {
                this.method289(class28);
                return;
            }
        }

    }

    public void method294(int i, int j, int k)
    {
        Ground class30_sub3 = this.tiles[i][k][j];
        if(class30_sub3 == null)
            return;
        class30_sub3.obj3 = null;
    }

    public void method295(int i, int j, int k)
    {
        Ground class30_sub3 = this.tiles[i][j][k];
        if(class30_sub3 != null)
        {
            class30_sub3.obj4 = null;
        }
    }

    public Object1 method296(int i, int j, int k)
    {
        Ground class30_sub3 = this.tiles[i][j][k];
        if(class30_sub3 == null)
            return null;
        else
            return class30_sub3.obj1;
    }

    public Object2 method297(int i, int k, int l)
    {
        Ground class30_sub3 = this.tiles[l][i][k];
        if(class30_sub3 == null)
            return null;
        else
            return class30_sub3.obj2;
    }

    public GameObject method298(int i, int j, int k)
    {
        Ground class30_sub3 = this.tiles[k][i][j];
        if(class30_sub3 == null)
            return null;
        for(int l = 0; l < class30_sub3.objectCount; l++)
        {
            GameObject class28 = class30_sub3.locs[l];
            if((class28.key >> 29 & 3) == 2 && class28.minX == i && class28.minY == j)
                return class28;
        }
        return null;
    }

    public Object3 method299(int i, int j, int k)
    {
        Ground class30_sub3 = this.tiles[k][j][i];
        if(class30_sub3 == null || class30_sub3.obj3 == null)
            return null;
        else
            return class30_sub3.obj3;
    }

    public int method300(int i, int j, int k)
    {
        Ground class30_sub3 = this.tiles[i][j][k];
        if(class30_sub3 == null || class30_sub3.obj1 == null)
            return 0;
        else
            return class30_sub3.obj1.uid;
    }

    public int method301(int i, int j, int l)
    {
        Ground class30_sub3 = this.tiles[i][j][l];
        if(class30_sub3 == null || class30_sub3.obj2 == null)
            return 0;
        else
            return class30_sub3.obj2.uid;
    }

    public int method302(int i, int j, int k)
    {
        Ground class30_sub3 = this.tiles[i][j][k];
        if(class30_sub3 == null)
            return 0;
        for(int l = 0; l < class30_sub3.objectCount; l++)
        {
            GameObject class28 = class30_sub3.locs[l];
            if((class28.key >> 29 & 3) == 2 && class28.minX == j && class28.minY == k)
                return class28.key;
        }

        return 0;
    }

    public int method303(int i, int j, int k)
    {
        Ground class30_sub3 = this.tiles[i][j][k];
        if(class30_sub3 == null || class30_sub3.obj3 == null)
            return 0;
        else
            return class30_sub3.obj3.uid;
    }

    public int method304(int i, int j, int k, int l)
    {
        Ground class30_sub3 = this.tiles[i][j][k];
        if(class30_sub3 == null)
            return -1;
        if(class30_sub3.obj1 != null && class30_sub3.obj1.uid == l)
            return class30_sub3.obj1.aByte281 & 0xff;
        if(class30_sub3.obj2 != null && class30_sub3.obj2.uid == l)
            return class30_sub3.obj2.aByte506 & 0xff;
        if(class30_sub3.obj3 != null && class30_sub3.obj3.uid == l)
            return class30_sub3.obj3.aByte816 & 0xff;
        for(int i1 = 0; i1 < class30_sub3.objectCount; i1++)
            if(class30_sub3.locs[i1].key == l)
                return class30_sub3.locs[i1].config & 0xff;

        return -1;
    }

    public void method305(int i, int k, int i1)
    {
        int j = 64;//was parameter
        int l = 768;//was parameter
        int j1 = (int)Math.sqrt(k * k + i * i + i1 * i1);
        int k1 = l * j1 >> 8;
        for(int l1 = 0; l1 < this.anInt437; l1++)
        {
            for(int i2 = 0; i2 < this.anInt438; i2++)
            {
                for(int j2 = 0; j2 < this.anInt439; j2++)
                {
                    Ground class30_sub3 = this.tiles[l1][i2][j2];
                    if(class30_sub3 != null)
                    {
                        Object1 class10 = class30_sub3.obj1;
                        if(class10 != null && class10.aClass30_Sub2_Sub4_278 != null && class10.aClass30_Sub2_Sub4_278.aClass33Array1425 != null)
                        {
                            this.method307(l1, 1, 1, i2, j2, (Model)class10.aClass30_Sub2_Sub4_278);
                            if(class10.aClass30_Sub2_Sub4_279 != null && class10.aClass30_Sub2_Sub4_279.aClass33Array1425 != null)
                            {
                                this.method307(l1, 1, 1, i2, j2, (Model)class10.aClass30_Sub2_Sub4_279);
                                this.method308((Model)class10.aClass30_Sub2_Sub4_278, (Model)class10.aClass30_Sub2_Sub4_279, 0, 0, 0, false);
                                ((Model)class10.aClass30_Sub2_Sub4_279).method480(j, k1, k, i, i1);
                            }
                            ((Model)class10.aClass30_Sub2_Sub4_278).method480(j, k1, k, i, i1);
                        }
                        for(int k2 = 0; k2 < class30_sub3.objectCount; k2++)
                        {
                            GameObject class28 = class30_sub3.locs[k2];
                            if(class28 != null && class28.renderable != null && class28.renderable.aClass33Array1425 != null)
                            {
                                this.method307(l1, (class28.maxX - class28.minX) + 1, (class28.maxY - class28.minY) + 1, i2, j2, (Model)class28.renderable);
                                ((Model)class28.renderable).method480(j, k1, k, i, i1);
                            }
                        }

                        Object3 class49 = class30_sub3.obj3;
                        if(class49 != null && class49.aClass30_Sub2_Sub4_814.aClass33Array1425 != null)
                        {
                            this.method306(i2, l1, (Model)class49.aClass30_Sub2_Sub4_814, j2);
                            ((Model)class49.aClass30_Sub2_Sub4_814).method480(j, k1, k, i, i1);
                        }
                    }
                }

            }

        }

    }

    private void method306(int i, int j, Model model, int k)
    {
        if(i < this.anInt438)
        {
            Ground class30_sub3 = this.tiles[j][i + 1][k];
            if(class30_sub3 != null && class30_sub3.obj3 != null && class30_sub3.obj3.aClass30_Sub2_Sub4_814.aClass33Array1425 != null)
                this.method308(model, (Model)class30_sub3.obj3.aClass30_Sub2_Sub4_814, 128, 0, 0, true);
        }
        if(k < this.anInt438)
        {
            Ground class30_sub3_1 = this.tiles[j][i][k + 1];
            if(class30_sub3_1 != null && class30_sub3_1.obj3 != null && class30_sub3_1.obj3.aClass30_Sub2_Sub4_814.aClass33Array1425 != null)
                this.method308(model, (Model)class30_sub3_1.obj3.aClass30_Sub2_Sub4_814, 0, 0, 128, true);
        }
        if(i < this.anInt438 && k < this.anInt439)
        {
            Ground class30_sub3_2 = this.tiles[j][i + 1][k + 1];
            if(class30_sub3_2 != null && class30_sub3_2.obj3 != null && class30_sub3_2.obj3.aClass30_Sub2_Sub4_814.aClass33Array1425 != null)
                this.method308(model, (Model)class30_sub3_2.obj3.aClass30_Sub2_Sub4_814, 128, 0, 128, true);
        }
        if(i < this.anInt438 && k > 0)
        {
            Ground class30_sub3_3 = this.tiles[j][i + 1][k - 1];
            if(class30_sub3_3 != null && class30_sub3_3.obj3 != null && class30_sub3_3.obj3.aClass30_Sub2_Sub4_814.aClass33Array1425 != null)
                this.method308(model, (Model)class30_sub3_3.obj3.aClass30_Sub2_Sub4_814, 128, 0, -128, true);
        }
    }

    private void method307(int i, int j, int k, int l, int i1, Model model)
    {
        boolean flag = true;
        int j1 = l;
        int k1 = l + j;
        int l1 = i1 - 1;
        int i2 = i1 + k;
        for(int j2 = i; j2 <= i + 1; j2++)
            if(j2 != this.anInt437)
            {
                for(int k2 = j1; k2 <= k1; k2++)
                    if(k2 >= 0 && k2 < this.anInt438)
                    {
                        for(int l2 = l1; l2 <= i2; l2++)
                            if(l2 >= 0 && l2 < this.anInt439 && (!flag || k2 >= k1 || l2 >= i2 || l2 < i1 && k2 != l))
                            {
                                Ground class30_sub3 = this.tiles[j2][k2][l2];
                                if(class30_sub3 != null)
                                {
                                    int i3 = (this.anIntArrayArrayArray440[j2][k2][l2] + this.anIntArrayArrayArray440[j2][k2 + 1][l2] + this.anIntArrayArrayArray440[j2][k2][l2 + 1] + this.anIntArrayArrayArray440[j2][k2 + 1][l2 + 1]) / 4 - (this.anIntArrayArrayArray440[i][l][i1] + this.anIntArrayArrayArray440[i][l + 1][i1] + this.anIntArrayArrayArray440[i][l][i1 + 1] + this.anIntArrayArrayArray440[i][l + 1][i1 + 1]) / 4;
                                    Object1 class10 = class30_sub3.obj1;
                                    if(class10 != null && class10.aClass30_Sub2_Sub4_278 != null && class10.aClass30_Sub2_Sub4_278.aClass33Array1425 != null)
                                        this.method308(model, (Model)class10.aClass30_Sub2_Sub4_278, (k2 - l) * 128 + (1 - j) * 64, i3, (l2 - i1) * 128 + (1 - k) * 64, flag);
                                    if(class10 != null && class10.aClass30_Sub2_Sub4_279 != null && class10.aClass30_Sub2_Sub4_279.aClass33Array1425 != null)
                                        this.method308(model, (Model)class10.aClass30_Sub2_Sub4_279, (k2 - l) * 128 + (1 - j) * 64, i3, (l2 - i1) * 128 + (1 - k) * 64, flag);
                                    for(int j3 = 0; j3 < class30_sub3.objectCount; j3++)
                                    {
                                        GameObject class28 = class30_sub3.locs[j3];
                                        if(class28 != null && class28.renderable != null && class28.renderable.aClass33Array1425 != null)
                                        {
                                            int k3 = (class28.maxX - class28.minX) + 1;
                                            int l3 = (class28.maxY - class28.minY) + 1;
                                            this.method308(model, (Model)class28.renderable, (class28.minX - l) * 128 + (k3 - j) * 64, i3, (class28.minY - i1) * 128 + (l3 - k) * 64, flag);
                                        }
                                    }

                                }
                            }

                    }

                j1--;
                flag = false;
            }

    }

    private void method308(Model model, Model model_1, int i, int j, int k, boolean flag)
    {
        this.anInt488++;
        int l = 0;
        int ai[] = model_1.anIntArray1627;
        int i1 = model_1.vertexCount;
        for(int j1 = 0; j1 < model.vertexCount; j1++)
        {
            Class33 class33 = model.aClass33Array1425[j1];
            Class33 class33_1 = model.aClass33Array1660[j1];
            if(class33_1.anInt605 != 0)
            {
                int i2 = model.anIntArray1628[j1] - j;
                if(i2 <= model_1.anInt1651)
                {
                    int j2 = model.anIntArray1627[j1] - i;
                    if(j2 >= model_1.anInt1646 && j2 <= model_1.anInt1647)
                    {
                        int k2 = model.anIntArray1629[j1] - k;
                        if(k2 >= model_1.anInt1649 && k2 <= model_1.anInt1648)
                        {
                            for(int l2 = 0; l2 < i1; l2++)
                            {
                                Class33 class33_2 = model_1.aClass33Array1425[l2];
                                Class33 class33_3 = model_1.aClass33Array1660[l2];
                                if(j2 == ai[l2] && k2 == model_1.anIntArray1629[l2] && i2 == model_1.anIntArray1628[l2] && class33_3.anInt605 != 0)
                                {
                                    class33.anInt602 += class33_3.anInt602;
                                    class33.anInt603 += class33_3.anInt603;
                                    class33.anInt604 += class33_3.anInt604;
                                    class33.anInt605 += class33_3.anInt605;
                                    class33_2.anInt602 += class33_1.anInt602;
                                    class33_2.anInt603 += class33_1.anInt603;
                                    class33_2.anInt604 += class33_1.anInt604;
                                    class33_2.anInt605 += class33_1.anInt605;
                                    l++;
                                    this.anIntArray486[j1] = this.anInt488;
                                    this.anIntArray487[l2] = this.anInt488;
                                }
                            }

                        }
                    }
                }
            }
        }

        if(l < 3 || !flag)
            return;
        for(int k1 = 0; k1 < model.anInt1630; k1++)
            if(this.anIntArray486[model.anIntArray1631[k1]] == this.anInt488 && this.anIntArray486[model.anIntArray1632[k1]] == this.anInt488 && this.anIntArray486[model.anIntArray1633[k1]] == this.anInt488)
                model.anIntArray1637[k1] = -1;

        for(int l1 = 0; l1 < model_1.anInt1630; l1++)
            if(this.anIntArray487[model_1.anIntArray1631[l1]] == this.anInt488 && this.anIntArray487[model_1.anIntArray1632[l1]] == this.anInt488 && this.anIntArray487[model_1.anIntArray1633[l1]] == this.anInt488)
                model_1.anIntArray1637[l1] = -1;

    }

    public void method309(int ai[], int i, int k, int l, int i1)
    {
        int j = 512;//was parameter
        Ground class30_sub3 = this.tiles[k][l][i1];
        if(class30_sub3 == null)
            return;
        TileUnderlay tileUnderlay = class30_sub3.underlay;
        if(tileUnderlay != null)
        {
            int j1 = tileUnderlay.rgb;
            if(j1 == 0)
                return;
            for(int k1 = 0; k1 < 4; k1++)
            {
                ai[i] = j1;
                ai[i + 1] = j1;
                ai[i + 2] = j1;
                ai[i + 3] = j1;
                i += j;
            }

            return;
        }
        Class40 class40 = class30_sub3.overlay;
        if(class40 == null)
            return;
        int l1 = class40.anInt684;
        int i2 = class40.anInt685;
        int j2 = class40.anInt686;
        int k2 = class40.anInt687;
        int ai1[] = this.anIntArrayArray489[l1];
        int ai2[] = this.anIntArrayArray490[i2];
        int l2 = 0;
        if(j2 != 0)
        {
            for(int i3 = 0; i3 < 4; i3++)
            {
                ai[i] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                ai[i + 1] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                ai[i + 2] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                ai[i + 3] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                i += j;
            }

            return;
        }
        for(int j3 = 0; j3 < 4; j3++)
        {
            if(ai1[ai2[l2++]] != 0)
                ai[i] = k2;
            if(ai1[ai2[l2++]] != 0)
                ai[i + 1] = k2;
            if(ai1[ai2[l2++]] != 0)
                ai[i + 2] = k2;
            if(ai1[ai2[l2++]] != 0)
                ai[i + 3] = k2;
            i += j;
        }

    }

    public static void method310(int i, int j, int k, int l, int ai[])
    {
        WorldController.anInt495 = 0;
        WorldController.anInt496 = 0;
        WorldController.anInt497 = k;
        WorldController.anInt498 = l;
        WorldController.anInt493 = k / 2;
        WorldController.anInt494 = l / 2;
        boolean aflag[][][][] = new boolean[9][32][53][53];
        for(int i1 = 128; i1 <= 384; i1 += 32)
        {
            for(int j1 = 0; j1 < 2048; j1 += 64)
            {
                WorldController.anInt458 = Model.SINE[i1];
                WorldController.anInt459 = Model.COSINE[i1];
                WorldController.anInt460 = Model.SINE[j1];
                WorldController.anInt461 = Model.COSINE[j1];
                int l1 = (i1 - 128) / 32;
                int j2 = j1 / 64;
                for(int l2 = -26; l2 <= 26; l2++)
                {
                    for(int j3 = -26; j3 <= 26; j3++)
                    {
                        int k3 = l2 * 128;
                        int i4 = j3 * 128;
                        boolean flag2 = false;
                        for(int k4 = -i; k4 <= j; k4 += 128)
                        {
                            if(!WorldController.method311(ai[l1] + k4, i4, k3))
                                continue;
                            flag2 = true;
                            break;
                        }

                        aflag[l1][j2][l2 + 25 + 1][j3 + 25 + 1] = flag2;
                    }

                }

            }

        }

        for(int k1 = 0; k1 < 8; k1++)
        {
            for(int i2 = 0; i2 < 32; i2++)
            {
                for(int k2 = -25; k2 < 25; k2++)
                {
                    for(int i3 = -25; i3 < 25; i3++)
                    {
                        boolean flag1 = false;
label0:
                        for(int l3 = -1; l3 <= 1; l3++)
                        {
                            for(int j4 = -1; j4 <= 1; j4++)
                            {
                                if(aflag[k1][i2][k2 + l3 + 25 + 1][i3 + j4 + 25 + 1])
                                    flag1 = true;
                                else
                                if(aflag[k1][(i2 + 1) % 31][k2 + l3 + 25 + 1][i3 + j4 + 25 + 1])
                                    flag1 = true;
                                else
                                if(aflag[k1 + 1][i2][k2 + l3 + 25 + 1][i3 + j4 + 25 + 1])
                                {
                                    flag1 = true;
                                } else
                                {
                                    if(!aflag[k1 + 1][(i2 + 1) % 31][k2 + l3 + 25 + 1][i3 + j4 + 25 + 1])
                                        continue;
                                    flag1 = true;
                                }
                                break label0;
                            }

                        }

                        WorldController.aBooleanArrayArrayArrayArray491[k1][i2][k2 + 25][i3 + 25] = flag1;
                    }

                }

            }

        }

    }

    private static boolean method311(int i, int j, int k)
    {
        int l = j * WorldController.anInt460 + k * WorldController.anInt461 >> 16;
        int i1 = j * WorldController.anInt461 - k * WorldController.anInt460 >> 16;
        int j1 = i * WorldController.anInt458 + i1 * WorldController.anInt459 >> 16;
        int k1 = i * WorldController.anInt459 - i1 * WorldController.anInt458 >> 16;
        if(j1 < 50 || j1 > 3500)
            return false;
        int l1 = WorldController.anInt493 + (l << Constants.VIEW_DISTANCE) / j1;
        int i2 = WorldController.anInt494 + (k1 << Constants.VIEW_DISTANCE) / j1;
        return l1 >= WorldController.anInt495 && l1 <= WorldController.anInt497 && i2 >= WorldController.anInt496 && i2 <= WorldController.anInt498;
    }

    public void method312(int i, int j)
    {
        WorldController.isClicked = true;
        WorldController.anInt468 = j;
        WorldController.anInt469 = i;
        WorldController.anInt470 = -1;
        WorldController.anInt471 = -1;
    }

    public void method313(int i, int j, int k, int l, int activeLevel, int j1)
    {
        if(i < 0)
            i = 0;
        else
        if(i >= this.anInt438 * 128)
            i = this.anInt438 * 128 - 1;
        if(j < 0)
            j = 0;
        else
        if(j >= this.anInt439 * 128)
            j = this.anInt439 * 128 - 1;
        WorldController.cycle++;
        WorldController.anInt458 = Model.SINE[j1];
        WorldController.anInt459 = Model.COSINE[j1];
        WorldController.anInt460 = Model.SINE[k];
        WorldController.anInt461 = Model.COSINE[k];
        WorldController.visibilityMap = WorldController.aBooleanArrayArrayArrayArray491[(j1 - 128) / 32][k / 64];
        WorldController.cameraX = i;
        WorldController.cameraY = l;
        WorldController.cameraZ = j;
        WorldController.cameraTileX = i / 128;
        WorldController.cameraTileZ = j / 128;
        WorldController.activeLevel = activeLevel;
        WorldController.anInt449 = WorldController.cameraTileX - 25;
        if(WorldController.anInt449 < 0)
            WorldController.anInt449 = 0;
        WorldController.anInt451 = WorldController.cameraTileZ - 25;
        if(WorldController.anInt451 < 0)
            WorldController.anInt451 = 0;
        WorldController.anInt450 = WorldController.cameraTileX + 25;
        if(WorldController.anInt450 > this.anInt438)
            WorldController.anInt450 = this.anInt438;
        WorldController.anInt452 = WorldController.cameraTileZ + 25;
        if(WorldController.anInt452 > this.anInt439)
            WorldController.anInt452 = this.anInt439;
        Occluders.update(WorldController.visibilityMap, activeLevel, cameraTileX, cameraTileZ, cameraX, cameraY, cameraZ);
        WorldController.anInt446 = 0;
        for(int k1 = this.anInt442; k1 < this.anInt437; k1++)
        {
            Ground aclass30_sub3[][] = this.tiles[k1];
            for(int i2 = WorldController.anInt449; i2 < WorldController.anInt450; i2++)
            {
                for(int k2 = WorldController.anInt451; k2 < WorldController.anInt452; k2++)
                {
                    Ground class30_sub3 = aclass30_sub3[i2][k2];
                    if(class30_sub3 != null)
                        if(class30_sub3.anInt1321 > activeLevel || !WorldController.visibilityMap[(i2 - WorldController.cameraTileX) + 25][(k2 - WorldController.cameraTileZ) + 25] && this.anIntArrayArrayArray440[k1][i2][k2] - l < 2000)
                        {
                            class30_sub3.aBoolean1322 = false;
                            class30_sub3.aBoolean1323 = false;
                            class30_sub3.anInt1325 = 0;
                        } else
                        {
                            class30_sub3.aBoolean1322 = true;
                            class30_sub3.aBoolean1323 = true;
                            class30_sub3.aBoolean1324 = class30_sub3.objectCount > 0;
                            WorldController.anInt446++;
                        }
                }

            }

        }

        for(int l1 = this.anInt442; l1 < this.anInt437; l1++)
        {
            Ground aclass30_sub3_1[][] = this.tiles[l1];
            for(int l2 = -25; l2 <= 0; l2++)
            {
                int i3 = WorldController.cameraTileX + l2;
                int k3 = WorldController.cameraTileX - l2;
                if(i3 >= WorldController.anInt449 || k3 < WorldController.anInt450)
                {
                    for(int i4 = -25; i4 <= 0; i4++)
                    {
                        int k4 = WorldController.cameraTileZ + i4;
                        int i5 = WorldController.cameraTileZ - i4;
                        if(i3 >= WorldController.anInt449)
                        {
                            if(k4 >= WorldController.anInt451)
                            {
                                Ground class30_sub3_1 = aclass30_sub3_1[i3][k4];
                                if(class30_sub3_1 != null && class30_sub3_1.aBoolean1322)
                                    this.method314(class30_sub3_1, true);
                            }
                            if(i5 < WorldController.anInt452)
                            {
                                Ground class30_sub3_2 = aclass30_sub3_1[i3][i5];
                                if(class30_sub3_2 != null && class30_sub3_2.aBoolean1322)
                                    this.method314(class30_sub3_2, true);
                            }
                        }
                        if(k3 < WorldController.anInt450)
                        {
                            if(k4 >= WorldController.anInt451)
                            {
                                Ground class30_sub3_3 = aclass30_sub3_1[k3][k4];
                                if(class30_sub3_3 != null && class30_sub3_3.aBoolean1322)
                                    this.method314(class30_sub3_3, true);
                            }
                            if(i5 < WorldController.anInt452)
                            {
                                Ground class30_sub3_4 = aclass30_sub3_1[k3][i5];
                                if(class30_sub3_4 != null && class30_sub3_4.aBoolean1322)
                                    this.method314(class30_sub3_4, true);
                            }
                        }
                        if(WorldController.anInt446 == 0)
                        {
                            WorldController.isClicked = false;
                            return;
                        }
                    }

                }
            }

        }

        for(int j2 = this.anInt442; j2 < this.anInt437; j2++)
        {
            Ground aclass30_sub3_2[][] = this.tiles[j2];
            for(int j3 = -25; j3 <= 0; j3++)
            {
                int l3 = WorldController.cameraTileX + j3;
                int j4 = WorldController.cameraTileX - j3;
                if(l3 >= WorldController.anInt449 || j4 < WorldController.anInt450)
                {
                    for(int l4 = -25; l4 <= 0; l4++)
                    {
                        int j5 = WorldController.cameraTileZ + l4;
                        int k5 = WorldController.cameraTileZ - l4;
                        if(l3 >= WorldController.anInt449)
                        {
                            if(j5 >= WorldController.anInt451)
                            {
                                Ground class30_sub3_5 = aclass30_sub3_2[l3][j5];
                                if(class30_sub3_5 != null && class30_sub3_5.aBoolean1322)
                                    this.method314(class30_sub3_5, false);
                            }
                            if(k5 < WorldController.anInt452)
                            {
                                Ground class30_sub3_6 = aclass30_sub3_2[l3][k5];
                                if(class30_sub3_6 != null && class30_sub3_6.aBoolean1322)
                                    this.method314(class30_sub3_6, false);
                            }
                        }
                        if(j4 < WorldController.anInt450)
                        {
                            if(j5 >= WorldController.anInt451)
                            {
                                Ground class30_sub3_7 = aclass30_sub3_2[j4][j5];
                                if(class30_sub3_7 != null && class30_sub3_7.aBoolean1322)
                                    this.method314(class30_sub3_7, false);
                            }
                            if(k5 < WorldController.anInt452)
                            {
                                Ground class30_sub3_8 = aclass30_sub3_2[j4][k5];
                                if(class30_sub3_8 != null && class30_sub3_8.aBoolean1322)
                                    this.method314(class30_sub3_8, false);
                            }
                        }
                        if(WorldController.anInt446 == 0)
                        {
                            WorldController.isClicked = false;
                            return;
                        }
                    }

                }
            }

        }

        WorldController.isClicked = false;
    }

    private void method314(Ground class30_sub3, boolean flag)
    {
        WorldController.aClass19_477.pushBack(class30_sub3);
        do
        {
            Ground class30_sub3_1;
            do
            {
                class30_sub3_1 = (Ground)WorldController.aClass19_477.popFront();
                if(class30_sub3_1 == null)
                    return;
            } while(!class30_sub3_1.aBoolean1323);
            int i = class30_sub3_1.anInt1308;
            int j = class30_sub3_1.anInt1309;
            int k = class30_sub3_1.level;
            int l = class30_sub3_1.renderLevel;
            Ground aclass30_sub3[][] = this.tiles[k];
            if(class30_sub3_1.aBoolean1322)
            {
                if(flag)
                {
                    if(k > 0)
                    {
                        Ground class30_sub3_2 = this.tiles[k - 1][i][j];
                        if(class30_sub3_2 != null && class30_sub3_2.aBoolean1323)
                            continue;
                    }
                    if(i <= WorldController.cameraTileX && i > WorldController.anInt449)
                    {
                        Ground class30_sub3_3 = aclass30_sub3[i - 1][j];
                        if(class30_sub3_3 != null && class30_sub3_3.aBoolean1323 && (class30_sub3_3.aBoolean1322 || (class30_sub3_1.attributes & 1) == 0))
                            continue;
                    }
                    if(i >= WorldController.cameraTileX && i < WorldController.anInt450 - 1)
                    {
                        Ground class30_sub3_4 = aclass30_sub3[i + 1][j];
                        if(class30_sub3_4 != null && class30_sub3_4.aBoolean1323 && (class30_sub3_4.aBoolean1322 || (class30_sub3_1.attributes & 4) == 0))
                            continue;
                    }
                    if(j <= WorldController.cameraTileZ && j > WorldController.anInt451)
                    {
                        Ground class30_sub3_5 = aclass30_sub3[i][j - 1];
                        if(class30_sub3_5 != null && class30_sub3_5.aBoolean1323 && (class30_sub3_5.aBoolean1322 || (class30_sub3_1.attributes & 8) == 0))
                            continue;
                    }
                    if(j >= WorldController.cameraTileZ && j < WorldController.anInt452 - 1)
                    {
                        Ground class30_sub3_6 = aclass30_sub3[i][j + 1];
                        if(class30_sub3_6 != null && class30_sub3_6.aBoolean1323 && (class30_sub3_6.aBoolean1322 || (class30_sub3_1.attributes & 2) == 0))
                            continue;
                    }
                } else
                {
                    flag = true;
                }
                class30_sub3_1.aBoolean1322 = false;
                if(class30_sub3_1.bridge != null)
                {
                    Ground class30_sub3_7 = class30_sub3_1.bridge;
                    if(class30_sub3_7.underlay != null)
                    {
                        if(!this.isTileOccluded(0, i, j))
                            this.method315(class30_sub3_7.underlay, 0, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, i, j);
                    } else
                    if(class30_sub3_7.overlay != null && !this.isTileOccluded(0, i, j))
                        this.method316(i, WorldController.anInt458, WorldController.anInt460, class30_sub3_7.overlay, WorldController.anInt459, j, WorldController.anInt461);
                    Object1 class10 = class30_sub3_7.obj1;
                    if(class10 != null)
                        class10.aClass30_Sub2_Sub4_278.method443(class10.aClass30_Sub2_Sub4_278, 0, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, class10.anInt274 - WorldController.cameraX, class10.anInt273 - WorldController.cameraY, class10.anInt275 - WorldController.cameraZ, class10.uid);
                    for(int i2 = 0; i2 < class30_sub3_7.objectCount; i2++)
                    {
                        GameObject class28 = class30_sub3_7.locs[i2];
                        if(class28 != null)
                            class28.renderable.method443(class28.renderable, class28.yaw, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, class28.centreX - WorldController.cameraX, class28.renderHeight - WorldController.cameraY, class28.centreY - WorldController.cameraZ, class28.key);
                    }

                }
                boolean flag1 = false;
                if(class30_sub3_1.underlay != null)
                {
                    if(!this.isTileOccluded(l, i, j))
                    {
                        flag1 = true;
                        this.method315(class30_sub3_1.underlay, l, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, i, j);
                    }
                } else
                if(class30_sub3_1.overlay != null && !this.isTileOccluded(l, i, j))
                {
                    flag1 = true;
                    this.method316(i, WorldController.anInt458, WorldController.anInt460, class30_sub3_1.overlay, WorldController.anInt459, j, WorldController.anInt461);
                }
                int j1 = 0;
                int j2 = 0;
                Object1 class10_3 = class30_sub3_1.obj1;
                Object2 class26_1 = class30_sub3_1.obj2;
                if(class10_3 != null || class26_1 != null)
                {
                    if(WorldController.cameraTileX == i)
                        j1++;
                    else
                    if(WorldController.cameraTileX < i)
                        j1 += 2;
                    if(WorldController.cameraTileZ == j)
                        j1 += 3;
                    else
                    if(WorldController.cameraTileZ > j)
                        j1 += 6;
                    j2 = WorldController.anIntArray478[j1];
                    class30_sub3_1.anInt1328 = WorldController.anIntArray480[j1];
                }
                if(class10_3 != null)
                {
                    if((class10_3.orientation & WorldController.anIntArray479[j1]) != 0)
                    {
                        if(class10_3.orientation == 16)
                        {
                            class30_sub3_1.anInt1325 = 3;
                            class30_sub3_1.anInt1326 = WorldController.anIntArray481[j1];
                            class30_sub3_1.anInt1327 = 3 - class30_sub3_1.anInt1326;
                        } else
                        if(class10_3.orientation == 32)
                        {
                            class30_sub3_1.anInt1325 = 6;
                            class30_sub3_1.anInt1326 = WorldController.anIntArray482[j1];
                            class30_sub3_1.anInt1327 = 6 - class30_sub3_1.anInt1326;
                        } else
                        if(class10_3.orientation == 64)
                        {
                            class30_sub3_1.anInt1325 = 12;
                            class30_sub3_1.anInt1326 = WorldController.anIntArray483[j1];
                            class30_sub3_1.anInt1327 = 12 - class30_sub3_1.anInt1326;
                        } else
                        {
                            class30_sub3_1.anInt1325 = 9;
                            class30_sub3_1.anInt1326 = WorldController.anIntArray484[j1];
                            class30_sub3_1.anInt1327 = 9 - class30_sub3_1.anInt1326;
                        }
                    } else
                    {
                        class30_sub3_1.anInt1325 = 0;
                    }
                    if((class10_3.orientation & j2) != 0 && !this.isWallOccluded(l, i, j, class10_3.orientation))
                        class10_3.aClass30_Sub2_Sub4_278.method443(class10_3.aClass30_Sub2_Sub4_278, 0, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, class10_3.anInt274 - WorldController.cameraX, class10_3.anInt273 - WorldController.cameraY, class10_3.anInt275 - WorldController.cameraZ, class10_3.uid);
                    if((class10_3.orientation1 & j2) != 0 && !this.isWallOccluded(l, i, j, class10_3.orientation1))
                        class10_3.aClass30_Sub2_Sub4_279.method443(class10_3.aClass30_Sub2_Sub4_279, 0, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, class10_3.anInt274 - WorldController.cameraX, class10_3.anInt273 - WorldController.cameraY, class10_3.anInt275 - WorldController.cameraZ, class10_3.uid);
                }
                if(class26_1 != null && !this.isOccluded(l, i, j, class26_1.aClass30_Sub2_Sub4_504.modelHeight))
                    if((class26_1.anInt502 & j2) != 0)
                        class26_1.aClass30_Sub2_Sub4_504.method443(class26_1.aClass30_Sub2_Sub4_504, class26_1.anInt503, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, class26_1.anInt500 - WorldController.cameraX, class26_1.anInt499 - WorldController.cameraY, class26_1.anInt501 - WorldController.cameraZ, class26_1.uid);
                    else
                    if((class26_1.anInt502 & 0x300) != 0)
                    {
                        int j4 = class26_1.anInt500 - WorldController.cameraX;
                        int l5 = class26_1.anInt499 - WorldController.cameraY;
                        int k6 = class26_1.anInt501 - WorldController.cameraZ;
                        int i8 = class26_1.anInt503;
                        int k9;
                        if(i8 == 1 || i8 == 2)
                            k9 = -j4;
                        else
                            k9 = j4;
                        int k10;
                        if(i8 == 2 || i8 == 3)
                            k10 = -k6;
                        else
                            k10 = k6;
                        if((class26_1.anInt502 & 0x100) != 0 && k10 < k9)
                        {
                            int i11 = j4 + WorldController.anIntArray463[i8];
                            int k11 = k6 + WorldController.anIntArray464[i8];
                            class26_1.aClass30_Sub2_Sub4_504.method443(class26_1.aClass30_Sub2_Sub4_504, i8 * 512 + 256, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, i11, l5, k11, class26_1.uid);
                        }
                        if((class26_1.anInt502 & 0x200) != 0 && k10 > k9)
                        {
                            int j11 = j4 + WorldController.anIntArray465[i8];
                            int l11 = k6 + WorldController.anIntArray466[i8];
                            class26_1.aClass30_Sub2_Sub4_504.method443(class26_1.aClass30_Sub2_Sub4_504, i8 * 512 + 1280 & 0x7ff, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, j11, l5, l11, class26_1.uid);
                        }
                    }
                if(flag1)
                {
                    Object3 class49 = class30_sub3_1.obj3;
                    if(class49 != null)
                        class49.aClass30_Sub2_Sub4_814.method443(class49.aClass30_Sub2_Sub4_814, 0, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, class49.anInt812 - WorldController.cameraX, class49.anInt811 - WorldController.cameraY, class49.anInt813 - WorldController.cameraZ, class49.uid);
                    Object4 object4_1 = class30_sub3_1.obj4;
                    if(object4_1 != null && object4_1.anInt52 == 0)
                    {
                        if(object4_1.aClass30_Sub2_Sub4_49 != null)
                            object4_1.aClass30_Sub2_Sub4_49.method443(object4_1.aClass30_Sub2_Sub4_49, 0, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, object4_1.anInt46 - WorldController.cameraX, object4_1.anInt45 - WorldController.cameraY, object4_1.anInt47 - WorldController.cameraZ, object4_1.uid);
                        if(object4_1.aClass30_Sub2_Sub4_50 != null)
                            object4_1.aClass30_Sub2_Sub4_50.method443(object4_1.aClass30_Sub2_Sub4_50, 0, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, object4_1.anInt46 - WorldController.cameraX, object4_1.anInt45 - WorldController.cameraY, object4_1.anInt47 - WorldController.cameraZ, object4_1.uid);
                        if(object4_1.aClass30_Sub2_Sub4_48 != null)
                            object4_1.aClass30_Sub2_Sub4_48.method443(object4_1.aClass30_Sub2_Sub4_48, 0, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, object4_1.anInt46 - WorldController.cameraX, object4_1.anInt45 - WorldController.cameraY, object4_1.anInt47 - WorldController.cameraZ, object4_1.uid);
                    }
                }
                int k4 = class30_sub3_1.attributes;
                if(k4 != 0)
                {
                    if(i < WorldController.cameraTileX && (k4 & 4) != 0)
                    {
                        Ground class30_sub3_17 = aclass30_sub3[i + 1][j];
                        if(class30_sub3_17 != null && class30_sub3_17.aBoolean1323)
                            WorldController.aClass19_477.pushBack(class30_sub3_17);
                    }
                    if(j < WorldController.cameraTileZ && (k4 & 2) != 0)
                    {
                        Ground class30_sub3_18 = aclass30_sub3[i][j + 1];
                        if(class30_sub3_18 != null && class30_sub3_18.aBoolean1323)
                            WorldController.aClass19_477.pushBack(class30_sub3_18);
                    }
                    if(i > WorldController.cameraTileX && (k4 & 1) != 0)
                    {
                        Ground class30_sub3_19 = aclass30_sub3[i - 1][j];
                        if(class30_sub3_19 != null && class30_sub3_19.aBoolean1323)
                            WorldController.aClass19_477.pushBack(class30_sub3_19);
                    }
                    if(j > WorldController.cameraTileZ && (k4 & 8) != 0)
                    {
                        Ground class30_sub3_20 = aclass30_sub3[i][j - 1];
                        if(class30_sub3_20 != null && class30_sub3_20.aBoolean1323)
                            WorldController.aClass19_477.pushBack(class30_sub3_20);
                    }
                }
            }
            if(class30_sub3_1.anInt1325 != 0)
            {
                boolean flag2 = true;
                for(int k1 = 0; k1 < class30_sub3_1.objectCount; k1++)
                {
                    if(class30_sub3_1.locs[k1].anInt528 == WorldController.cycle || (class30_sub3_1.locFlags[k1] & class30_sub3_1.anInt1325) != class30_sub3_1.anInt1326)
                        continue;
                    flag2 = false;
                    break;
                }

                if(flag2)
                {
                    Object1 class10_1 = class30_sub3_1.obj1;
                    if(!this.isWallOccluded(l, i, j, class10_1.orientation))
                        class10_1.aClass30_Sub2_Sub4_278.method443(class10_1.aClass30_Sub2_Sub4_278, 0, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, class10_1.anInt274 - WorldController.cameraX, class10_1.anInt273 - WorldController.cameraY, class10_1.anInt275 - WorldController.cameraZ, class10_1.uid);
                    class30_sub3_1.anInt1325 = 0;
                }
            }
            if(class30_sub3_1.aBoolean1324)
                try
                {
                    int i1 = class30_sub3_1.objectCount;
                    class30_sub3_1.aBoolean1324 = false;
                    int l1 = 0;
label0:
                    for(int k2 = 0; k2 < i1; k2++)
                    {
                        GameObject class28_1 = class30_sub3_1.locs[k2];
                        if(class28_1.anInt528 == WorldController.cycle)
                            continue;
                        for(int k3 = class28_1.minX; k3 <= class28_1.maxX; k3++)
                        {
                            for(int l4 = class28_1.minY; l4 <= class28_1.maxY; l4++)
                            {
                                Ground class30_sub3_21 = aclass30_sub3[k3][l4];
                                if(class30_sub3_21.aBoolean1322)
                                {
                                    class30_sub3_1.aBoolean1324 = true;
                                } else
                                {
                                    if(class30_sub3_21.anInt1325 == 0)
                                        continue;
                                    int l6 = 0;
                                    if(k3 > class28_1.minX)
                                        l6++;
                                    if(k3 < class28_1.maxX)
                                        l6 += 4;
                                    if(l4 > class28_1.minY)
                                        l6 += 8;
                                    if(l4 < class28_1.maxY)
                                        l6 += 2;
                                    if((l6 & class30_sub3_21.anInt1325) != class30_sub3_1.anInt1327)
                                        continue;
                                    class30_sub3_1.aBoolean1324 = true;
                                }
                                continue label0;
                            }

                        }

                        WorldController.aClass28Array462[l1++] = class28_1;
                        int i5 = WorldController.cameraTileX - class28_1.minX;
                        int i6 = class28_1.maxX - WorldController.cameraTileX;
                        if(i6 > i5)
                            i5 = i6;
                        int i7 = WorldController.cameraTileZ - class28_1.minY;
                        int j8 = class28_1.maxY - WorldController.cameraTileZ;
                        if(j8 > i7)
                            class28_1.anInt527 = i5 + j8;
                        else
                            class28_1.anInt527 = i5 + i7;
                    }

                    while(l1 > 0) 
                    {
                        int i3 = -50;
                        int l3 = -1;
                        for(int j5 = 0; j5 < l1; j5++)
                        {
                            GameObject class28_2 = WorldController.aClass28Array462[j5];
                            if(class28_2.anInt528 != WorldController.cycle)
                                if(class28_2.anInt527 > i3)
                                {
                                    i3 = class28_2.anInt527;
                                    l3 = j5;
                                } else
                                if(class28_2.anInt527 == i3)
                                {
                                    int j7 = class28_2.centreX - WorldController.cameraX;
                                    int k8 = class28_2.centreY - WorldController.cameraZ;
                                    int l9 = WorldController.aClass28Array462[l3].centreX - WorldController.cameraX;
                                    int l10 = WorldController.aClass28Array462[l3].centreY - WorldController.cameraZ;
                                    if(j7 * j7 + k8 * k8 > l9 * l9 + l10 * l10)
                                        l3 = j5;
                                }
                        }

                        if(l3 == -1)
                            break;
                        GameObject class28_3 = WorldController.aClass28Array462[l3];
                        class28_3.anInt528 = WorldController.cycle;
                        if(!this.isAreaOccluded(l, class28_3.minX, class28_3.maxX, class28_3.minY, class28_3.maxY, class28_3.renderable.modelHeight))
                            class28_3.renderable.method443(class28_3.renderable, class28_3.yaw, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, class28_3.centreX - WorldController.cameraX, class28_3.renderHeight - WorldController.cameraY, class28_3.centreY - WorldController.cameraZ, class28_3.key);
                        for(int k7 = class28_3.minX; k7 <= class28_3.maxX; k7++)
                        {
                            for(int l8 = class28_3.minY; l8 <= class28_3.maxY; l8++)
                            {
                                Ground class30_sub3_22 = aclass30_sub3[k7][l8];
                                if(class30_sub3_22.anInt1325 != 0)
                                    WorldController.aClass19_477.pushBack(class30_sub3_22);
                                else
                                if((k7 != i || l8 != j) && class30_sub3_22.aBoolean1323)
                                    WorldController.aClass19_477.pushBack(class30_sub3_22);
                            }

                        }

                    }
                    if(class30_sub3_1.aBoolean1324)
                        continue;
                }
                catch(Exception _ex)
                {
                    class30_sub3_1.aBoolean1324 = false;
                }
            if(!class30_sub3_1.aBoolean1323 || class30_sub3_1.anInt1325 != 0)
                continue;
            if(i <= WorldController.cameraTileX && i > WorldController.anInt449)
            {
                Ground class30_sub3_8 = aclass30_sub3[i - 1][j];
                if(class30_sub3_8 != null && class30_sub3_8.aBoolean1323)
                    continue;
            }
            if(i >= WorldController.cameraTileX && i < WorldController.anInt450 - 1)
            {
                Ground class30_sub3_9 = aclass30_sub3[i + 1][j];
                if(class30_sub3_9 != null && class30_sub3_9.aBoolean1323)
                    continue;
            }
            if(j <= WorldController.cameraTileZ && j > WorldController.anInt451)
            {
                Ground class30_sub3_10 = aclass30_sub3[i][j - 1];
                if(class30_sub3_10 != null && class30_sub3_10.aBoolean1323)
                    continue;
            }
            if(j >= WorldController.cameraTileZ && j < WorldController.anInt452 - 1)
            {
                Ground class30_sub3_11 = aclass30_sub3[i][j + 1];
                if(class30_sub3_11 != null && class30_sub3_11.aBoolean1323)
                    continue;
            }
            class30_sub3_1.aBoolean1323 = false;
            WorldController.anInt446--;
            Object4 object4 = class30_sub3_1.obj4;
            if(object4 != null && object4.anInt52 != 0)
            {
                if(object4.aClass30_Sub2_Sub4_49 != null)
                    object4.aClass30_Sub2_Sub4_49.method443(object4.aClass30_Sub2_Sub4_49, 0, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, object4.anInt46 - WorldController.cameraX, object4.anInt45 - WorldController.cameraY - object4.anInt52, object4.anInt47 - WorldController.cameraZ, object4.uid);
                if(object4.aClass30_Sub2_Sub4_50 != null)
                    object4.aClass30_Sub2_Sub4_50.method443(object4.aClass30_Sub2_Sub4_50, 0, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, object4.anInt46 - WorldController.cameraX, object4.anInt45 - WorldController.cameraY - object4.anInt52, object4.anInt47 - WorldController.cameraZ, object4.uid);
                if(object4.aClass30_Sub2_Sub4_48 != null)
                    object4.aClass30_Sub2_Sub4_48.method443(object4.aClass30_Sub2_Sub4_48, 0, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, object4.anInt46 - WorldController.cameraX, object4.anInt45 - WorldController.cameraY - object4.anInt52, object4.anInt47 - WorldController.cameraZ, object4.uid);
            }
            if(class30_sub3_1.anInt1328 != 0)
            {
                Object2 class26 = class30_sub3_1.obj2;
                if(class26 != null && !this.isOccluded(l, i, j, class26.aClass30_Sub2_Sub4_504.modelHeight))
                    if((class26.anInt502 & class30_sub3_1.anInt1328) != 0)
                        class26.aClass30_Sub2_Sub4_504.method443(class26.aClass30_Sub2_Sub4_504, class26.anInt503, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, class26.anInt500 - WorldController.cameraX, class26.anInt499 - WorldController.cameraY, class26.anInt501 - WorldController.cameraZ, class26.uid);
                    else
                    if((class26.anInt502 & 0x300) != 0)
                    {
                        int l2 = class26.anInt500 - WorldController.cameraX;
                        int j3 = class26.anInt499 - WorldController.cameraY;
                        int i4 = class26.anInt501 - WorldController.cameraZ;
                        int k5 = class26.anInt503;
                        int j6;
                        if(k5 == 1 || k5 == 2)
                            j6 = -l2;
                        else
                            j6 = l2;
                        int l7;
                        if(k5 == 2 || k5 == 3)
                            l7 = -i4;
                        else
                            l7 = i4;
                        if((class26.anInt502 & 0x100) != 0 && l7 >= j6)
                        {
                            int i9 = l2 + WorldController.anIntArray463[k5];
                            int i10 = i4 + WorldController.anIntArray464[k5];
                            class26.aClass30_Sub2_Sub4_504.method443(class26.aClass30_Sub2_Sub4_504, k5 * 512 + 256, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, i9, j3, i10, class26.uid);
                        }
                        if((class26.anInt502 & 0x200) != 0 && l7 <= j6)
                        {
                            int j9 = l2 + WorldController.anIntArray465[k5];
                            int j10 = i4 + WorldController.anIntArray466[k5];
                            class26.aClass30_Sub2_Sub4_504.method443(class26.aClass30_Sub2_Sub4_504, k5 * 512 + 1280 & 0x7ff, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, j9, j3, j10, class26.uid);
                        }
                    }
                Object1 class10_2 = class30_sub3_1.obj1;
                if(class10_2 != null)
                {
                    if((class10_2.orientation1 & class30_sub3_1.anInt1328) != 0 && !this.isWallOccluded(l, i, j, class10_2.orientation1))
                        class10_2.aClass30_Sub2_Sub4_279.method443(class10_2.aClass30_Sub2_Sub4_279, 0, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, class10_2.anInt274 - WorldController.cameraX, class10_2.anInt273 - WorldController.cameraY, class10_2.anInt275 - WorldController.cameraZ, class10_2.uid);
                    if((class10_2.orientation & class30_sub3_1.anInt1328) != 0 && !this.isWallOccluded(l, i, j, class10_2.orientation))
                        class10_2.aClass30_Sub2_Sub4_278.method443(class10_2.aClass30_Sub2_Sub4_278, 0, WorldController.anInt458, WorldController.anInt459, WorldController.anInt460, WorldController.anInt461, class10_2.anInt274 - WorldController.cameraX, class10_2.anInt273 - WorldController.cameraY, class10_2.anInt275 - WorldController.cameraZ, class10_2.uid);
                }
            }
            if(k < this.anInt437 - 1)
            {
                Ground class30_sub3_12 = this.tiles[k + 1][i][j];
                if(class30_sub3_12 != null && class30_sub3_12.aBoolean1323)
                    WorldController.aClass19_477.pushBack(class30_sub3_12);
            }
            if(i < WorldController.cameraTileX)
            {
                Ground class30_sub3_13 = aclass30_sub3[i + 1][j];
                if(class30_sub3_13 != null && class30_sub3_13.aBoolean1323)
                    WorldController.aClass19_477.pushBack(class30_sub3_13);
            }
            if(j < WorldController.cameraTileZ)
            {
                Ground class30_sub3_14 = aclass30_sub3[i][j + 1];
                if(class30_sub3_14 != null && class30_sub3_14.aBoolean1323)
                    WorldController.aClass19_477.pushBack(class30_sub3_14);
            }
            if(i > WorldController.cameraTileX)
            {
                Ground class30_sub3_15 = aclass30_sub3[i - 1][j];
                if(class30_sub3_15 != null && class30_sub3_15.aBoolean1323)
                    WorldController.aClass19_477.pushBack(class30_sub3_15);
            }
            if(j > WorldController.cameraTileZ)
            {
                Ground class30_sub3_16 = aclass30_sub3[i][j - 1];
                if(class30_sub3_16 != null && class30_sub3_16.aBoolean1323)
                    WorldController.aClass19_477.pushBack(class30_sub3_16);
            }
        } while(true);
    }

    private void method315(TileUnderlay tileUnderlay, int i, int j, int k, int l, int i1, int j1,
            int k1)
    {
        int l1;
        int i2 = l1 = (j1 << 7) - WorldController.cameraX;
        int j2;
        int k2 = j2 = (k1 << 7) - WorldController.cameraZ;
        int l2;
        int i3 = l2 = i2 + 128;
        int j3;
        int k3 = j3 = k2 + 128;
        int l3 = this.anIntArrayArrayArray440[i][j1][k1] - WorldController.cameraY;
        int i4 = this.anIntArrayArrayArray440[i][j1 + 1][k1] - WorldController.cameraY;
        int j4 = this.anIntArrayArrayArray440[i][j1 + 1][k1 + 1] - WorldController.cameraY;
        int k4 = this.anIntArrayArrayArray440[i][j1][k1 + 1] - WorldController.cameraY;
        int l4 = k2 * l + i2 * i1 >> 16;
        k2 = k2 * i1 - i2 * l >> 16;
        i2 = l4;
        l4 = l3 * k - k2 * j >> 16;
        k2 = l3 * j + k2 * k >> 16;
        l3 = l4;
        if(k2 < 50)
            return;
        l4 = j2 * l + i3 * i1 >> 16;
        j2 = j2 * i1 - i3 * l >> 16;
        i3 = l4;
        l4 = i4 * k - j2 * j >> 16;
        j2 = i4 * j + j2 * k >> 16;
        i4 = l4;
        if(j2 < 50)
            return;
        l4 = k3 * l + l2 * i1 >> 16;
        k3 = k3 * i1 - l2 * l >> 16;
        l2 = l4;
        l4 = j4 * k - k3 * j >> 16;
        k3 = j4 * j + k3 * k >> 16;
        j4 = l4;
        if(k3 < 50)
            return;
        l4 = j3 * l + l1 * i1 >> 16;
        j3 = j3 * i1 - l1 * l >> 16;
        l1 = l4;
        l4 = k4 * k - j3 * j >> 16;
        j3 = k4 * j + j3 * k >> 16;
        k4 = l4;
        if(j3 < 50)
            return;
        int a_x = Texture.originViewX + (i2 << Constants.VIEW_DISTANCE) / k2;
        int a_y = Texture.originViewY + (l3 << Constants.VIEW_DISTANCE) / k2;
        int b_x = Texture.originViewX + (i3 << Constants.VIEW_DISTANCE) / j2;
        int b_y = Texture.originViewY + (i4 << Constants.VIEW_DISTANCE) / j2;
        int c_x = Texture.originViewX + (l2 << Constants.VIEW_DISTANCE) / k3;
        int c_y = Texture.originViewY + (j4 << Constants.VIEW_DISTANCE) / k3;
        int d_x = Texture.originViewX + (l1 << Constants.VIEW_DISTANCE) / j3;
        int d_y = Texture.originViewY + (k4 << Constants.VIEW_DISTANCE) / j3;

        Texture.currentAlpha = 0;
        if((c_x - d_x) * (b_y - d_y) - (c_y - d_y) * (b_x - d_x) > 0)
        {
            Texture.aBoolean1462 = c_x < 0 || d_x < 0 || b_x < 0 || c_x > DrawingArea.centerX || d_x > DrawingArea.centerX || b_x > DrawingArea.centerX;
            if(WorldController.isClicked && this.isMouseWithinTriangle(WorldController.anInt468, WorldController.anInt469, c_y, d_y, b_y, c_x, d_x, b_x))
            {
                WorldController.anInt470 = j1;
                WorldController.anInt471 = k1;
            }
            if(tileUnderlay.texture == -1)
            {
                if(tileUnderlay.northeastColor != 0xbc614e)
                    Texture.method374(c_y, d_y, b_y, c_x, d_x, b_x, tileUnderlay.northeastColor, tileUnderlay.northwestColor, tileUnderlay.southeastColor);
            } else
            if(!WorldController.lowMem)
            {
                if(tileUnderlay.flat)
                    Texture.method378(c_y, d_y, b_y, c_x, d_x, b_x, tileUnderlay.northeastColor, tileUnderlay.northwestColor, tileUnderlay.southeastColor, i2, i3, l1, l3, i4, k4, k2, j2, j3, tileUnderlay.texture);
                else
                    Texture.method378(c_y, d_y, b_y, c_x, d_x, b_x, tileUnderlay.northeastColor, tileUnderlay.northwestColor, tileUnderlay.southeastColor, l2, l1, i3, j4, k4, i4, k3, j3, j2, tileUnderlay.texture);
            } else
            {
                int i7 = WorldController.anIntArray485[tileUnderlay.texture];
                Texture.method374(c_y, d_y, b_y, c_x, d_x, b_x, this.method317(i7, tileUnderlay.northeastColor), this.method317(i7, tileUnderlay.northwestColor), this.method317(i7, tileUnderlay.southeastColor));
            }
        }
        if((a_x - b_x) * (d_y - b_y) - (a_y - b_y) * (d_x - b_x) > 0)
        {
            Texture.aBoolean1462 = a_x < 0 || b_x < 0 || d_x < 0 || a_x > DrawingArea.centerX || b_x > DrawingArea.centerX || d_x > DrawingArea.centerX;
            if(WorldController.isClicked && this.isMouseWithinTriangle(WorldController.anInt468, WorldController.anInt469, a_y, b_y, d_y, a_x, b_x, d_x))
            {
                WorldController.anInt470 = j1;
                WorldController.anInt471 = k1;
            }
            if(tileUnderlay.texture == -1)
            {
                if(tileUnderlay.southwestColor != 0xbc614e)
                {
                    Texture.method374(a_y, b_y, d_y, a_x, b_x, d_x, tileUnderlay.southwestColor, tileUnderlay.southeastColor, tileUnderlay.northwestColor);
                }
            } else
            {
                if(!WorldController.lowMem)
                {
                    Texture.method378(a_y, b_y, d_y, a_x, b_x, d_x, tileUnderlay.southwestColor, tileUnderlay.southeastColor, tileUnderlay.northwestColor, i2, i3, l1, l3, i4, k4, k2, j2, j3, tileUnderlay.texture);
                    return;
                }
                int j7 = WorldController.anIntArray485[tileUnderlay.texture];
                Texture.method374(a_y, b_y, d_y, a_x, b_x, d_x, this.method317(j7, tileUnderlay.southwestColor), this.method317(j7, tileUnderlay.southeastColor), this.method317(j7, tileUnderlay.northwestColor));
            }
        }
    }

    private void method316(int i, int j, int k, Class40 class40, int l, int i1,
                           int j1)
    {
        int k1 = class40.vertexX.length;
        for(int l1 = 0; l1 < k1; l1++)
        {
            int i2 = class40.vertexX[l1] - WorldController.cameraX;
            int k2 = class40.vertexY[l1] - WorldController.cameraY;
            int i3 = class40.vertexZ[l1] - WorldController.cameraZ;
            int k3 = i3 * k + i2 * j1 >> 16;
            i3 = i3 * j1 - i2 * k >> 16;
            i2 = k3;
            k3 = k2 * l - i3 * j >> 16;
            i3 = k2 * j + i3 * l >> 16;
            k2 = k3;
            if(i3 < 50)
                return;
            if(class40.triangleTextureIndex != null)
            {
                Class40.vertexSceneX[l1] = i2;
                Class40.vertexSceneY[l1] = k2;
                Class40.vertexSceneZ[l1] = i3;
            }
            Class40.tmpScreenX[l1] = Texture.originViewX + (i2 << Constants.VIEW_DISTANCE) / i3;
            Class40.tmpScreenY[l1] = Texture.originViewY + (k2 << Constants.VIEW_DISTANCE) / i3;
        }

        Texture.currentAlpha = 0;
        k1 = class40.triangleVertexA.length;
        for(int j2 = 0; j2 < k1; j2++)
        {
            int a = class40.triangleVertexA[j2];
            int b = class40.triangleVertexB[j2];
            int c = class40.triangleVertexC[j2];
            int a_x = Class40.tmpScreenX[a];
            int b_x = Class40.tmpScreenX[b];
            int c_x = Class40.tmpScreenX[c];
            int a_y = Class40.tmpScreenY[a];
            int b_y = Class40.tmpScreenY[b];
            int c_y = Class40.tmpScreenY[c];
            if((a_x - b_x) * (c_y - b_y) - (a_y - b_y) * (c_x - b_x) > 0)
            {
                Texture.aBoolean1462 = a_x < 0 || b_x < 0 || c_x < 0 || a_x > DrawingArea.centerX || b_x > DrawingArea.centerX || c_x > DrawingArea.centerX;
                if(WorldController.isClicked && this.isMouseWithinTriangle(WorldController.anInt468, WorldController.anInt469, a_y, b_y, c_y, a_x, b_x, c_x))
                {
                    WorldController.anInt470 = i;
                    WorldController.anInt471 = i1;
                }
                if(class40.triangleTextureIndex == null || class40.triangleTextureIndex[j2] == -1)
                {
                    if(class40.triangleColorA[j2] != 0xbc614e)
                        Texture.method374(a_y, b_y, c_y, a_x, b_x, c_x, class40.triangleColorA[j2], class40.triangleColorB[j2], class40.triangleColorC[j2]);
                } else
                if(!WorldController.lowMem)
                {
                    if(class40.isFlat)
                        Texture.method378(a_y, b_y, c_y, a_x, b_x, c_x, class40.triangleColorA[j2], class40.triangleColorB[j2], class40.triangleColorC[j2], Class40.vertexSceneX[0], Class40.vertexSceneX[1], Class40.vertexSceneX[3], Class40.vertexSceneY[0], Class40.vertexSceneY[1], Class40.vertexSceneY[3], Class40.vertexSceneZ[0], Class40.vertexSceneZ[1], Class40.vertexSceneZ[3], class40.triangleTextureIndex[j2]);
                    else
                        Texture.method378(a_y, b_y, c_y, a_x, b_x, c_x, class40.triangleColorA[j2], class40.triangleColorB[j2], class40.triangleColorC[j2], Class40.vertexSceneX[a], Class40.vertexSceneX[b], Class40.vertexSceneX[c], Class40.vertexSceneY[a], Class40.vertexSceneY[b], Class40.vertexSceneY[c], Class40.vertexSceneZ[a], Class40.vertexSceneZ[b], Class40.vertexSceneZ[c], class40.triangleTextureIndex[j2]);
                } else
                {
                    int k5 = WorldController.anIntArray485[class40.triangleTextureIndex[j2]];
                    Texture.method374(a_y, b_y, c_y, a_x, b_x, c_x, this.method317(k5, class40.triangleColorA[j2]), this.method317(k5, class40.triangleColorB[j2]), this.method317(k5, class40.triangleColorC[j2]));
                }
            }
        }

    }

    private int method317(int j, int k)
    {
        k = 127 - k;
        k = (k * (j & 0x7f)) / 160;
        if(k < 2)
            k = 2;
        else
        if(k > 126)
            k = 126;
        return (j & 0xff80) + k;
    }

   // TODO
    private boolean isMouseWithinTriangle(int mouseX, int mouseY, int a_y, int b_y, int c_y, int a_x, int b_x,
            int c_x)
    {
        if(mouseY < a_y && mouseY < b_y && mouseY < c_y)
            return false;
        if(mouseY > a_y && mouseY > b_y && mouseY > c_y)
            return false;
        if(mouseX < a_x && mouseX < b_x && mouseX < c_x)
            return false;
        if(mouseX > a_x && mouseX > b_x && mouseX > c_x)
            return false;
        int i2 = (mouseY - a_y) * (b_x - a_x) - (mouseX - a_x) * (b_y - a_y);
        int j2 = (mouseY - c_y) * (a_x - c_x) - (mouseX - c_x) * (a_y - c_y);
        int k2 = (mouseY - b_y) * (c_x - b_x) - (mouseX - b_x) * (c_y - b_y);
        return i2 * k2 > 0 && k2 * j2 > 0;
    }

    private boolean isTileOccluded(int level, int tileX, int tileZ)
    {
        int l = this.levelTileCycles[level][tileX][tileZ];
        if(l == -WorldController.cycle)
            return false;
        if(l == WorldController.cycle)
            return true;
        int i1 = tileX << 7;
        int j1 = tileZ << 7;
        if(Occluders.isOccluded(i1 + 1, this.anIntArrayArrayArray440[level][tileX][tileZ], j1 + 1) && Occluders.isOccluded((i1 + 128) - 1, this.anIntArrayArrayArray440[level][tileX + 1][tileZ], j1 + 1) && Occluders.isOccluded((i1 + 128) - 1, this.anIntArrayArrayArray440[level][tileX + 1][tileZ + 1], (j1 + 128) - 1) && Occluders.isOccluded(i1 + 1, this.anIntArrayArrayArray440[level][tileX][tileZ + 1], (j1 + 128) - 1))
        {
            this.levelTileCycles[level][tileX][tileZ] = WorldController.cycle;
            return true;
        } else
        {
            this.levelTileCycles[level][tileX][tileZ] = -WorldController.cycle;
            return false;
        }
    }

    private boolean isWallOccluded(int i, int j, int k, int l)
    {
        if(!this.isTileOccluded(i, j, k))
            return false;
        int i1 = j << 7;
        int j1 = k << 7;
        int k1 = this.anIntArrayArrayArray440[i][j][k] - 1;
        int l1 = k1 - 120;
        int i2 = k1 - 230;
        int j2 = k1 - 238;
        if(l < 16)
        {
            if(l == 1)
            {
                if(i1 > WorldController.cameraX)
                {
                    if(!Occluders.isOccluded(i1, k1, j1))
                        return false;
                    if(!Occluders.isOccluded(i1, k1, j1 + 128))
                        return false;
                }
                if(i > 0)
                {
                    if(!Occluders.isOccluded(i1, l1, j1))
                        return false;
                    if(!Occluders.isOccluded(i1, l1, j1 + 128))
                        return false;
                }
                return Occluders.isOccluded(i1, i2, j1) && Occluders.isOccluded(i1, i2, j1 + 128);
            }
            if(l == 2)
            {
                if(j1 < WorldController.cameraZ)
                {
                    if(!Occluders.isOccluded(i1, k1, j1 + 128))
                        return false;
                    if(!Occluders.isOccluded(i1 + 128, k1, j1 + 128))
                        return false;
                }
                if(i > 0)
                {
                    if(!Occluders.isOccluded(i1, l1, j1 + 128))
                        return false;
                    if(!Occluders.isOccluded(i1 + 128, l1, j1 + 128))
                        return false;
                }
                return Occluders.isOccluded(i1, i2, j1 + 128) && Occluders.isOccluded(i1 + 128, i2, j1 + 128);
            }
            if(l == 4)
            {
                if(i1 < WorldController.cameraX)
                {
                    if(!Occluders.isOccluded(i1 + 128, k1, j1))
                        return false;
                    if(!Occluders.isOccluded(i1 + 128, k1, j1 + 128))
                        return false;
                }
                if(i > 0)
                {
                    if(!Occluders.isOccluded(i1 + 128, l1, j1))
                        return false;
                    if(!Occluders.isOccluded(i1 + 128, l1, j1 + 128))
                        return false;
                }
                return Occluders.isOccluded(i1 + 128, i2, j1) && Occluders.isOccluded(i1 + 128, i2, j1 + 128);
            }
            if(l == 8)
            {
                if(j1 > WorldController.cameraZ)
                {
                    if(!Occluders.isOccluded(i1, k1, j1))
                        return false;
                    if(!Occluders.isOccluded(i1 + 128, k1, j1))
                        return false;
                }
                if(i > 0)
                {
                    if(!Occluders.isOccluded(i1, l1, j1))
                        return false;
                    if(!Occluders.isOccluded(i1 + 128, l1, j1))
                        return false;
                }
                return Occluders.isOccluded(i1, i2, j1) && Occluders.isOccluded(i1 + 128, i2, j1);
            }
        }
        if(!Occluders.isOccluded(i1 + 64, j2, j1 + 64))
            return false;
        if(l == 16)
            return Occluders.isOccluded(i1, i2, j1 + 128);
        if(l == 32)
            return Occluders.isOccluded(i1 + 128, i2, j1 + 128);
        if(l == 64)
            return Occluders.isOccluded(i1 + 128, i2, j1);
        if(l == 128)
        {
            return Occluders.isOccluded(i1, i2, j1);
        } else
        {
            System.out.println("Warning unsupported wall type");
            return true;
        }
    }

    private boolean isOccluded(int i, int j, int k, int l)
    {
        if(!this.isTileOccluded(i, j, k))
            return false;
        int i1 = j << 7;
        int j1 = k << 7;
        return Occluders.isOccluded(i1 + 1, this.anIntArrayArrayArray440[i][j][k] - l, j1 + 1) && Occluders.isOccluded((i1 + 128) - 1, this.anIntArrayArrayArray440[i][j + 1][k] - l, j1 + 1) && Occluders.isOccluded((i1 + 128) - 1, this.anIntArrayArrayArray440[i][j + 1][k + 1] - l, (j1 + 128) - 1) && Occluders.isOccluded(i1 + 1, this.anIntArrayArrayArray440[i][j][k + 1] - l, (j1 + 128) - 1);
    }

    private boolean isAreaOccluded(int i, int j, int k, int l, int i1, int j1)
    {
        if(j == k && l == i1)
        {
            if(!this.isTileOccluded(i, j, l))
                return false;
            int k1 = j << 7;
            int i2 = l << 7;
            return Occluders.isOccluded(k1 + 1, this.anIntArrayArrayArray440[i][j][l] - j1, i2 + 1) && Occluders.isOccluded((k1 + 128) - 1, this.anIntArrayArrayArray440[i][j + 1][l] - j1, i2 + 1) && Occluders.isOccluded((k1 + 128) - 1, this.anIntArrayArrayArray440[i][j + 1][l + 1] - j1, (i2 + 128) - 1) && Occluders.isOccluded(k1 + 1, this.anIntArrayArrayArray440[i][j][l + 1] - j1, (i2 + 128) - 1);
        }
        for(int l1 = j; l1 <= k; l1++)
        {
            for(int j2 = l; j2 <= i1; j2++)
                if(this.levelTileCycles[i][l1][j2] == -WorldController.cycle)
                    return false;

        }

        int k2 = (j << 7) + 1;
        int l2 = (l << 7) + 2;
        int i3 = this.anIntArrayArrayArray440[i][j][l] - j1;
        if(!Occluders.isOccluded(k2, i3, l2))
            return false;
        int j3 = (k << 7) - 1;
        if(!Occluders.isOccluded(j3, i3, l2))
            return false;
        int k3 = (i1 << 7) - 1;
        return Occluders.isOccluded(k2, i3, k3) && Occluders.isOccluded(j3, i3, k3);
    }
    
    private boolean aBoolean434;
    public static boolean lowMem = true;
    private final int anInt437;
    private final int anInt438;
    private final int anInt439;
    private final int[][][] anIntArrayArrayArray440;
    private final Ground[][][] tiles;
    private int anInt442;
    private int obj5CacheCurrPos;
    private final GameObject[] obj5Cache;
    private final int[][][] levelTileCycles;
    private static int anInt446;
    static int activeLevel;
    private static int cycle;
    private static int anInt449;
    private static int anInt450;
    private static int anInt451;
    private static int anInt452;
    static int cameraTileX;
    static int cameraTileZ;
    static int cameraX;
    static int cameraY;
    static int cameraZ;
    private static int anInt458;
    private static int anInt459;
    private static int anInt460;
    private static int anInt461;
    private static GameObject[] aClass28Array462 = new GameObject[100];
    private static final int[] anIntArray463 = {
        53, -53, -53, 53
    };
    private static final int[] anIntArray464 = {
        -53, -53, 53, 53
    };
    private static final int[] anIntArray465 = {
        -45, 45, 45, -45
    };
    private static final int[] anIntArray466 = {
        45, 45, -45, -45
    };
    private static boolean isClicked;
    private static int anInt468;
    private static int anInt469;
    public static int anInt470 = -1;
    public static int anInt471 = -1;

    private static Deque aClass19_477 = new Deque();
    private static final int[] anIntArray478 = {
        19, 55, 38, 155, 255, 110, 137, 205, 76
    };
    private static final int[] anIntArray479 = {
        160, 192, 80, 96, 0, 144, 80, 48, 160
    };
    private static final int[] anIntArray480 = {
        76, 8, 137, 4, 0, 1, 38, 2, 19
    };
    private static final int[] anIntArray481 = {
        0, 0, 2, 0, 0, 2, 1, 1, 0
    };
    private static final int[] anIntArray482 = {
        2, 0, 0, 2, 0, 0, 0, 4, 4
    };
    private static final int[] anIntArray483 = {
        0, 4, 4, 8, 0, 0, 8, 0, 0
    };
    private static final int[] anIntArray484 = {
        1, 1, 0, 0, 0, 8, 0, 0, 8
    };
    private static final int[] anIntArray485 = {
        41, 39248, 41, 4643, 41, 41, 41, 41, 41, 41, 
        41, 41, 41, 41, 41, 43086, 41, 41, 41, 41, 
        41, 41, 41, 8602, 41, 28992, 41, 41, 41, 41, 
        41, 5056, 41, 41, 41, 7079, 41, 41, 41, 41, 
        41, 41, 41, 41, 41, 41, 3131, 41, 41, 41
    };
    private final int[] anIntArray486;
    private final int[] anIntArray487;
    private int anInt488;
    private final int[][] anIntArrayArray489 = {
        new int[16], {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 1
        }, {
            1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 
            1, 0, 1, 1, 1, 1
        }, {
            1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 
            0, 0, 1, 0, 0, 0
        }, {
            0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 
            0, 1, 0, 0, 0, 1
        }, {
            0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 1
        }, {
            1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 
            1, 1, 1, 1, 1, 1
        }, {
            1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 
            0, 0, 1, 1, 0, 0
        }, {
            0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 
            0, 0, 1, 1, 0, 0
        }, {
            1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 
            1, 1, 0, 0, 1, 1
        }, 
        {
            1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 
            0, 0, 1, 0, 0, 0
        }, {
            0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 
            1, 1, 0, 1, 1, 1
        }, {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
            1, 0, 1, 1, 1, 1
        }
    };
    private final int[][] anIntArrayArray490 = {
        {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
            10, 11, 12, 13, 14, 15
        }, {
            12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 
            6, 2, 15, 11, 7, 3
        }, {
            15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 
            5, 4, 3, 2, 1, 0
        }, {
            3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 
            9, 13, 0, 4, 8, 12
        }
    };
    private static boolean[][][][] aBooleanArrayArrayArrayArray491 = new boolean[8][32][51][51];
    private static boolean[][] visibilityMap;
    private static int anInt493;
    private static int anInt494;
    private static int anInt495;
    private static int anInt496;
    private static int anInt497;
    private static int anInt498;

}
