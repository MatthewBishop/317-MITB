package com.jagex.cache.def;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.cache.Archive;
import com.jagex.cache.graphics.Sprite;
import com.jagex.draw.DrawingArea;
import com.jagex.draw.Texture;
import com.jagex.draw.render.SpriteRenderer;
import com.jagex.entity.model.Model;
import com.jagex.entity.model.ModelRenderer;
import com.jagex.io.Buffer;
import com.jagex.link.Cache;

public final class ItemDef
{

    public static void nullLoader()
    {
        ItemDef.mruNodes2 = null;
        ItemDef.mruNodes1 = null;
        ItemDef.streamIndices = null;
        ItemDef.cache = null;
        ItemDef.buffer = null;
    }

    public boolean method192(int j)
    {
        int k = this.anInt175;
        int l = this.anInt166;
        if(j == 1)
        {
            k = this.anInt197;
            l = this.anInt173;
        }
        if(k == -1)
            return true;
        boolean flag = true;
        if(!Model.method463(k))
            flag = false;
        if(l != -1 && !Model.method463(l))
            flag = false;
        return flag;
    }

    public static void unpackConfig(Archive archive)
    {
        ItemDef.buffer = new Buffer(archive.getEntry("obj.dat"));
        Buffer buffer = new Buffer(archive.getEntry("obj.idx"));
        ItemDef.totalItems = buffer.readUShort();
        ItemDef.streamIndices = new int[ItemDef.totalItems];
        int i = 2;
        for(int j = 0; j < ItemDef.totalItems; j++)
        {
            ItemDef.streamIndices[j] = i;
            i += buffer.readUShort();
        }

        ItemDef.cache = new ItemDef[10];
        for(int k = 0; k < 10; k++)
            ItemDef.cache[k] = new ItemDef();

    }

    public Model method194(int j)
    {
        int k = this.anInt175;
        int l = this.anInt166;
        if(j == 1)
        {
            k = this.anInt197;
            l = this.anInt173;
        }
        if(k == -1)
            return null;
        Model model = Model.method462(k);
        if(l != -1)
        {
            Model model_1 = Model.method462(l);
            Model aclass30_sub2_sub4_sub6s[] = {
                    model, model_1
            };
            model = new Model(2, aclass30_sub2_sub4_sub6s);
        }
        if(this.modifiedModelColors != null)
        {
            for(int i1 = 0; i1 < this.modifiedModelColors.length; i1++)
                model.method476(this.modifiedModelColors[i1], this.originalModelColors[i1]);

        }
        return model;
    }

    public boolean method195(int j)
    {
        int k = this.anInt165;
        int l = this.anInt188;
        int i1 = this.anInt185;
        if(j == 1)
        {
            k = this.anInt200;
            l = this.anInt164;
            i1 = this.anInt162;
        }
        if(k == -1)
            return true;
        boolean flag = true;
        if(!Model.method463(k))
            flag = false;
        if(l != -1 && !Model.method463(l))
            flag = false;
        if(i1 != -1 && !Model.method463(i1))
            flag = false;
        return flag;
    }

    public Model method196(int i)
    {
        int j = this.anInt165;
        int k = this.anInt188;
        int l = this.anInt185;
        if(i == 1)
        {
            j = this.anInt200;
            k = this.anInt164;
            l = this.anInt162;
        }
        if(j == -1)
            return null;
        Model model = Model.method462(j);
        if(k != -1)
            if(l != -1)
            {
                Model model_1 = Model.method462(k);
                Model model_3 = Model.method462(l);
                Model aclass30_sub2_sub4_sub6_1s[] = {
                        model, model_1, model_3
                };
                model = new Model(3, aclass30_sub2_sub4_sub6_1s);
            } else
            {
                Model model_2 = Model.method462(k);
                Model aclass30_sub2_sub4_sub6s[] = {
                        model, model_2
                };
                model = new Model(2, aclass30_sub2_sub4_sub6s);
            }
        if(i == 0 && this.aByte205 != 0)
            model.method475(0, this.aByte205, 0);
        if(i == 1 && this.aByte154 != 0)
            model.method475(0, this.aByte154, 0);
        if(this.modifiedModelColors != null)
        {
            for(int i1 = 0; i1 < this.modifiedModelColors.length; i1++)
                model.method476(this.modifiedModelColors[i1], this.originalModelColors[i1]);

        }
        return model;
    }

    private void setDefaults()
    {
        this.modelID = 0;
        this.name = null;
        this.description = null;
        this.modifiedModelColors = null;
        this.originalModelColors = null;
        this.modelZoom = 2000;
        this.modelRotation1 = 0;
        this.modelRotation2 = 0;
        this.anInt204 = 0;
        this.modelOffset1 = 0;
        this.modelOffset2 = 0;
        this.stackable = false;
        this.value = 1;
        this.membersObject = false;
        this.groundActions = null;
        this.actions = null;
        this.anInt165 = -1;
        this.anInt188 = -1;
        this.aByte205 = 0;
        this.anInt200 = -1;
        this.anInt164 = -1;
        this.aByte154 = 0;
        this.anInt185 = -1;
        this.anInt162 = -1;
        this.anInt175 = -1;
        this.anInt166 = -1;
        this.anInt197 = -1;
        this.anInt173 = -1;
        this.stackIDs = null;
        this.stackAmounts = null;
        this.certID = -1;
        this.certTemplateID = -1;
        this.anInt167 = 128;
        this.anInt192 = 128;
        this.anInt191 = 128;
        this.anInt196 = 0;
        this.anInt184 = 0;
        this.team = 0;
    }

    public static ItemDef forID(int i)
    {
        for(int j = 0; j < 10; j++)
            if(ItemDef.cache[j].id == i)
                return ItemDef.cache[j];

        ItemDef.cacheIndex = (ItemDef.cacheIndex + 1) % 10;
        ItemDef itemDef = ItemDef.cache[ItemDef.cacheIndex];
        ItemDef.buffer.position = ItemDef.streamIndices[i];
        itemDef.id = i;
        itemDef.setDefaults();
        itemDef.readValues(ItemDef.buffer);
        if(itemDef.certTemplateID != -1)
            itemDef.toNote();
        if(!ItemDef.isMembers && itemDef.membersObject)
        {
            itemDef.name = "Members Object";
            itemDef.description = "Login to a members' server to use this object.".getBytes();
            itemDef.groundActions = null;
            itemDef.actions = null;
            itemDef.team = 0;
        }
        return itemDef;
    }

    private void toNote()
    {
        ItemDef itemDef = ItemDef.forID(this.certTemplateID);
        this.modelID = itemDef.modelID;
        this.modelZoom = itemDef.modelZoom;
        this.modelRotation1 = itemDef.modelRotation1;
        this.modelRotation2 = itemDef.modelRotation2;

        this.anInt204 = itemDef.anInt204;
        this.modelOffset1 = itemDef.modelOffset1;
        this.modelOffset2 = itemDef.modelOffset2;
        this.modifiedModelColors = itemDef.modifiedModelColors;
        this.originalModelColors = itemDef.originalModelColors;
        ItemDef itemDef_1 = ItemDef.forID(this.certID);
        this.name = itemDef_1.name;
        this.membersObject = itemDef_1.membersObject;
        this.value = itemDef_1.value;
        String s = "a";
        char c = itemDef_1.name.charAt(0);
        if(c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')
            s = "an";
        this.description = ("Swap this note at any bank for " + s + " " + itemDef_1.name + ".").getBytes();
        this.stackable = true;
    }

    public static Sprite getSprite(int i, int j, int k)
    {
        if(k == 0)
        {
            Sprite sprite = (Sprite) ItemDef.mruNodes1.get(i);
            if(sprite != null && sprite.resizeHeight != j && sprite.resizeHeight != -1)
            {
                sprite.unlink();
                sprite = null;
            }
            if(sprite != null)
                return sprite;
        }
        ItemDef itemDef = ItemDef.forID(i);
        if(itemDef.stackIDs == null)
            j = -1;
        if(j > 1)
        {
            int i1 = -1;
            for(int j1 = 0; j1 < 10; j1++)
                if(j >= itemDef.stackAmounts[j1] && itemDef.stackAmounts[j1] != 0)
                    i1 = itemDef.stackIDs[j1];

            if(i1 != -1)
                itemDef = ItemDef.forID(i1);
        }
        Model model = itemDef.asGroundStack(1);
        if(model == null)
            return null;
        Sprite sprite = null;
        if(itemDef.certTemplateID != -1)
        {
            sprite = ItemDef.getSprite(itemDef.certID, 10, -1);
            if(sprite == null)
                return null;
        }
        Sprite sprite2 = new Sprite(32, 32);
        int k1 = Texture.originViewX;
        int l1 = Texture.originViewY;
        int ai[] = Texture.anIntArray1472;
        int ai1[] = DrawingArea.pixels;
        int i2 = DrawingArea.width;
        int j2 = DrawingArea.height;
        int k2 = DrawingArea.topX;
        int l2 = DrawingArea.bottomX;
        int i3 = DrawingArea.topY;
        int j3 = DrawingArea.bottomY;
        Texture.aBoolean1464 = false;
        DrawingArea.initDrawingArea(32, 32, sprite2.raster);
        DrawingArea.drawPixels(32, 0, 0, 0, 32);
        Texture.method364();
        int k3 = itemDef.modelZoom;
        if(k == -1)
            k3 = (int)(k3 * 1.5D);
        if(k > 0)
            k3 = (int)(k3 * 1.04D);
        int l3 = Texture.anIntArray1470[itemDef.modelRotation1] * k3 >> 16;
        int i4 = Texture.anIntArray1471[itemDef.modelRotation1] * k3 >> 16;
        ModelRenderer.render(model, 0, itemDef.modelRotation2, itemDef.anInt204, itemDef.modelRotation1, itemDef.modelOffset1, l3 + model.modelHeight / 2 + itemDef.modelOffset2, i4 + itemDef.modelOffset2);
        for(int i5 = 31; i5 >= 0; i5--)
        {
            for(int j4 = 31; j4 >= 0; j4--)
                if(sprite2.raster[i5 + j4 * 32] == 0)
                    if(i5 > 0 && sprite2.raster[(i5 - 1) + j4 * 32] > 1)
                        sprite2.raster[i5 + j4 * 32] = 1;
                    else
                    if(j4 > 0 && sprite2.raster[i5 + (j4 - 1) * 32] > 1)
                        sprite2.raster[i5 + j4 * 32] = 1;
                    else
                    if(i5 < 31 && sprite2.raster[i5 + 1 + j4 * 32] > 1)
                        sprite2.raster[i5 + j4 * 32] = 1;
                    else
                    if(j4 < 31 && sprite2.raster[i5 + (j4 + 1) * 32] > 1)
                        sprite2.raster[i5 + j4 * 32] = 1;

        }

        if(k > 0)
        {
            for(int j5 = 31; j5 >= 0; j5--)
            {
                for(int k4 = 31; k4 >= 0; k4--)
                    if(sprite2.raster[j5 + k4 * 32] == 0)
                        if(j5 > 0 && sprite2.raster[(j5 - 1) + k4 * 32] == 1)
                            sprite2.raster[j5 + k4 * 32] = k;
                        else
                        if(k4 > 0 && sprite2.raster[j5 + (k4 - 1) * 32] == 1)
                            sprite2.raster[j5 + k4 * 32] = k;
                        else
                        if(j5 < 31 && sprite2.raster[j5 + 1 + k4 * 32] == 1)
                            sprite2.raster[j5 + k4 * 32] = k;
                        else
                        if(k4 < 31 && sprite2.raster[j5 + (k4 + 1) * 32] == 1)
                            sprite2.raster[j5 + k4 * 32] = k;

            }

        } else
        if(k == 0)
        {
            for(int k5 = 31; k5 >= 0; k5--)
            {
                for(int l4 = 31; l4 >= 0; l4--)
                    if(sprite2.raster[k5 + l4 * 32] == 0 && k5 > 0 && l4 > 0 && sprite2.raster[(k5 - 1) + (l4 - 1) * 32] > 0)
                        sprite2.raster[k5 + l4 * 32] = 0x302020;

            }

        }
        if(itemDef.certTemplateID != -1)
        {
            int l5 = sprite.resizeWidth;
            int j6 = sprite.resizeHeight;
            sprite.resizeWidth = 32;
            sprite.resizeHeight = 32;
            SpriteRenderer.drawSprite(sprite, 0, 0);
            sprite.resizeWidth = l5;
            sprite.resizeHeight = j6;
        }
        if(k == 0)
            ItemDef.mruNodes1.put(i, sprite2);
        DrawingArea.initDrawingArea(j2, i2, ai1);
        DrawingArea.setDrawingArea(j3, k2, l2, i3);
        Texture.originViewX = k1;
        Texture.originViewY = l1;
        Texture.anIntArray1472 = ai;
        Texture.aBoolean1464 = true;
        if(itemDef.stackable)
            sprite2.resizeWidth = 33;
        else
            sprite2.resizeWidth = 32;
        sprite2.resizeHeight = j;
        return sprite2;
    }

    public Model asGroundStack(int i)
    {
        if(this.stackIDs != null && i > 1)
        {
            int j = -1;
            for(int k = 0; k < 10; k++)
                if(i >= this.stackAmounts[k] && this.stackAmounts[k] != 0)
                    j = this.stackIDs[k];

            if(j != -1)
                return ItemDef.forID(j).asGroundStack(1);
        }
        Model model = (Model) ItemDef.mruNodes2.get(this.id);
        if(model != null)
            return model;
        model = Model.method462(this.modelID);
        if(model == null)
            return null;
        if(this.anInt167 != 128 || this.anInt192 != 128 || this.anInt191 != 128)
            model.method478(this.anInt167, this.anInt191, this.anInt192);
        if(this.modifiedModelColors != null)
        {
            for(int l = 0; l < this.modifiedModelColors.length; l++)
                model.method476(this.modifiedModelColors[l], this.originalModelColors[l]);

        }
        model.method479(64 + this.anInt196, 768 + this.anInt184, -50, -10, -50, true);
        model.aBoolean1659 = true;
        ItemDef.mruNodes2.put(this.id, model);
        return model;
    }

    public Model method202(int i)
    {
        if(this.stackIDs != null && i > 1)
        {
            int j = -1;
            for(int k = 0; k < 10; k++)
                if(i >= this.stackAmounts[k] && this.stackAmounts[k] != 0)
                    j = this.stackIDs[k];

            if(j != -1)
                return ItemDef.forID(j).method202(1);
        }
        Model model = Model.method462(this.modelID);
        if(model == null)
            return null;
        if(this.modifiedModelColors != null)
        {
            for(int l = 0; l < this.modifiedModelColors.length; l++)
                model.method476(this.modifiedModelColors[l], this.originalModelColors[l]);

        }
        return model;
    }

    private void readValues(Buffer buffer)
    {
        do
        {
            int i = buffer.readUByte();
            if(i == 0)
                return;
            if(i == 1)
                this.modelID = buffer.readUShort();
            else
            if(i == 2)
                this.name = buffer.readString();
            else
            if(i == 3)
                this.description = buffer.readStringBytes();
            else
            if(i == 4)
                this.modelZoom = buffer.readUShort();
            else
            if(i == 5)
                this.modelRotation1 = buffer.readUShort();
            else
            if(i == 6)
                this.modelRotation2 = buffer.readUShort();
            else
            if(i == 7)
            {
                this.modelOffset1 = buffer.readUShort();
                if(this.modelOffset1 > 32767)
                    this.modelOffset1 -= 0x10000;
            } else
            if(i == 8)
            {
                this.modelOffset2 = buffer.readUShort();
                if(this.modelOffset2 > 32767)
                    this.modelOffset2 -= 0x10000;
            } else
            if(i == 10)
                buffer.readUShort();
            else
            if(i == 11)
                this.stackable = true;
            else
            if(i == 12)
                this.value = buffer.readInt();
            else
            if(i == 16)
                this.membersObject = true;
            else
            if(i == 23)
            {
                this.anInt165 = buffer.readUShort();
                this.aByte205 = buffer.readByte();
            } else
            if(i == 24)
                this.anInt188 = buffer.readUShort();
            else
            if(i == 25)
            {
                this.anInt200 = buffer.readUShort();
                this.aByte154 = buffer.readByte();
            } else
            if(i == 26)
                this.anInt164 = buffer.readUShort();
            else
            if(i >= 30 && i < 35)
            {
                if(this.groundActions == null)
                    this.groundActions = new String[5];
                this.groundActions[i - 30] = buffer.readString();
                if(this.groundActions[i - 30].equalsIgnoreCase("hidden"))
                    this.groundActions[i - 30] = null;
            } else
            if(i >= 35 && i < 40)
            {
                if(this.actions == null)
                    this.actions = new String[5];
                this.actions[i - 35] = buffer.readString();
            } else
            if(i == 40)
            {
                int j = buffer.readUByte();
                this.modifiedModelColors = new int[j];
                this.originalModelColors = new int[j];
                for(int k = 0; k < j; k++)
                {
                    this.modifiedModelColors[k] = buffer.readUShort();
                    this.originalModelColors[k] = buffer.readUShort();
                }

            } else
            if(i == 78)
                this.anInt185 = buffer.readUShort();
            else
            if(i == 79)
                this.anInt162 = buffer.readUShort();
            else
            if(i == 90)
                this.anInt175 = buffer.readUShort();
            else
            if(i == 91)
                this.anInt197 = buffer.readUShort();
            else
            if(i == 92)
                this.anInt166 = buffer.readUShort();
            else
            if(i == 93)
                this.anInt173 = buffer.readUShort();
            else
            if(i == 95)
                this.anInt204 = buffer.readUShort();
            else
            if(i == 97)
                this.certID = buffer.readUShort();
            else
            if(i == 98)
                this.certTemplateID = buffer.readUShort();
            else
            if(i >= 100 && i < 110)
            {
                if(this.stackIDs == null)
                {
                    this.stackIDs = new int[10];
                    this.stackAmounts = new int[10];
                }
                this.stackIDs[i - 100] = buffer.readUShort();
                this.stackAmounts[i - 100] = buffer.readUShort();
            } else
            if(i == 110)
                this.anInt167 = buffer.readUShort();
            else
            if(i == 111)
                this.anInt192 = buffer.readUShort();
            else
            if(i == 112)
                this.anInt191 = buffer.readUShort();
            else
            if(i == 113)
                this.anInt196 = buffer.readByte();
            else
            if(i == 114)
                this.anInt184 = buffer.readByte() * 5;
            else
            if(i == 115)
                this.team = buffer.readUByte();
        } while(true);
    }

    private ItemDef()
    {
        this.id = -1;
    }

    private byte aByte154;
    public int value;
    private int[] modifiedModelColors;
    public int id;
    public static Cache mruNodes1 = new Cache(100);
    public static Cache mruNodes2 = new Cache(50);
    private int[] originalModelColors;
    public boolean membersObject;
    private int anInt162;
    private int certTemplateID;
    private int anInt164;
    private int anInt165;
    private int anInt166;
    private int anInt167;
    public String groundActions[];
    private int modelOffset1;
    public String name;
    private static ItemDef[] cache;
    private int anInt173;
    private int modelID;
    private int anInt175;
    public boolean stackable;
    public byte description[];
    private int certID;
    private static int cacheIndex;
    public int modelZoom;
    public static boolean isMembers = true;
    private static Buffer buffer;
    private int anInt184;
    private int anInt185;
    private int anInt188;
    public String actions[];
    public int modelRotation1;
    private int anInt191;
    private int anInt192;
    private int[] stackIDs;
    private int modelOffset2;
    private static int[] streamIndices;
    private int anInt196;
    private int anInt197;
    public int modelRotation2;
    private int anInt200;
    private int[] stackAmounts;
    public int team;
    public static int totalItems;
    private int anInt204;
    private byte aByte205;

}
