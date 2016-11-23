package com.jagex.cache.graphics;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.Client;
import com.jagex.cache.Archive;
import com.jagex.cache.anim.Frame;
import com.jagex.cache.def.EntityDef;
import com.jagex.cache.def.ItemDef;
import com.jagex.entity.model.Model;
import com.jagex.io.Buffer;
import com.jagex.link.Cache;
import com.jagex.util.TextClass;

public final class RSInterface
{

    public void swapInventoryItems(int i, int j)
    {
        int k = this.inv[i];
        this.inv[i] = this.inv[j];
        this.inv[j] = k;
        k = this.invStackSizes[i];
        this.invStackSizes[i] = this.invStackSizes[j];
        this.invStackSizes[j] = k;
    }

    public static void unpack(Archive archive, TextDrawingArea textDrawingAreas[], Archive streamLoader_1)
    {
        RSInterface.aMRUNodes_238 = new Cache(50000);
        Buffer buffer = new Buffer(archive.getEntry("data"));
        int i = -1;
        int j = buffer.readUShort();
        RSInterface.interfaceCache = new RSInterface[j];
        while(buffer.position < buffer.payload.length)
        {
            int k = buffer.readUShort();
            if(k == 65535)
            {
                i = buffer.readUShort();
                k = buffer.readUShort();
            }
            RSInterface rsInterface = RSInterface.interfaceCache[k] = new RSInterface();
            rsInterface.id = k;
            rsInterface.parentID = i;
            rsInterface.type = buffer.readUByte();
            rsInterface.atActionType = buffer.readUByte();
            rsInterface.anInt214 = buffer.readUShort();
            rsInterface.width = buffer.readUShort();
            rsInterface.height = buffer.readUShort();
            rsInterface.aByte254 = (byte) buffer.readUByte();
            rsInterface.anInt230 = buffer.readUByte();
            if(rsInterface.anInt230 != 0)
                rsInterface.anInt230 = (rsInterface.anInt230 - 1 << 8) + buffer.readUByte();
            else
                rsInterface.anInt230 = -1;
            int i1 = buffer.readUByte();
            if(i1 > 0)
            {
                rsInterface.anIntArray245 = new int[i1];
                rsInterface.anIntArray212 = new int[i1];
                for(int j1 = 0; j1 < i1; j1++)
                {
                    rsInterface.anIntArray245[j1] = buffer.readUByte();
                    rsInterface.anIntArray212[j1] = buffer.readUShort();
                }

            }
            int k1 = buffer.readUByte();
            if(k1 > 0)
            {
                rsInterface.valueIndexArray = new int[k1][];
                for(int l1 = 0; l1 < k1; l1++)
                {
                    int i3 = buffer.readUShort();
                    rsInterface.valueIndexArray[l1] = new int[i3];
                    for(int l4 = 0; l4 < i3; l4++)
                        rsInterface.valueIndexArray[l1][l4] = buffer.readUShort();

                }

            }
            if(rsInterface.type == 0)
            {
                rsInterface.scrollMax = buffer.readUShort();
                rsInterface.aBoolean266 = buffer.readUByte() == 1;
                int i2 = buffer.readUShort();
                rsInterface.children = new int[i2];
                rsInterface.childX = new int[i2];
                rsInterface.childY = new int[i2];
                for(int j3 = 0; j3 < i2; j3++)
                {
                    rsInterface.children[j3] = buffer.readUShort();
                    rsInterface.childX[j3] = buffer.readShort();
                    rsInterface.childY[j3] = buffer.readShort();
                }

            }
            if(rsInterface.type == 1)
            {
                buffer.readUShort();
                buffer.readUByte();
            }
            if(rsInterface.type == 2)
            {
                rsInterface.inv = new int[rsInterface.width * rsInterface.height];
                rsInterface.invStackSizes = new int[rsInterface.width * rsInterface.height];
                rsInterface.aBoolean259 = buffer.readUByte() == 1;
                rsInterface.isInventoryInterface = buffer.readUByte() == 1;
                rsInterface.usableItemInterface = buffer.readUByte() == 1;
                rsInterface.aBoolean235 = buffer.readUByte() == 1;
                rsInterface.invSpritePadX = buffer.readUByte();
                rsInterface.invSpritePadY = buffer.readUByte();
                rsInterface.spritesX = new int[20];
                rsInterface.spritesY = new int[20];
                rsInterface.sprites = new Sprite[20];
                for(int j2 = 0; j2 < 20; j2++)
                {
                    int k3 = buffer.readUByte();
                    if(k3 == 1)
                    {
                        rsInterface.spritesX[j2] = buffer.readShort();
                        rsInterface.spritesY[j2] = buffer.readShort();
                        String s1 = buffer.readString();
                        if(streamLoader_1 != null && s1.length() > 0)
                        {
                            int i5 = s1.lastIndexOf(",");
                            rsInterface.sprites[j2] = RSInterface.method207(Integer.parseInt(s1.substring(i5 + 1)), streamLoader_1, s1.substring(0, i5));
                        }
                    }
                }

                rsInterface.actions = new String[5];
                for(int l3 = 0; l3 < 5; l3++)
                {
                    rsInterface.actions[l3] = buffer.readString();
                    if(rsInterface.actions[l3].length() == 0)
                        rsInterface.actions[l3] = null;
                }

            }
            if(rsInterface.type == 3)
                rsInterface.aBoolean227 = buffer.readUByte() == 1;
            if(rsInterface.type == 4 || rsInterface.type == 1)
            {
                rsInterface.centerText = buffer.readUByte() == 1;
                int k2 = buffer.readUByte();
                if(textDrawingAreas != null)
                    rsInterface.textDrawingAreas = textDrawingAreas[k2];
                rsInterface.aBoolean268 = buffer.readUByte() == 1;
            }
            if(rsInterface.type == 4)
            {
                rsInterface.message = buffer.readString();
                rsInterface.aString228 = buffer.readString();
            }
            if(rsInterface.type == 1 || rsInterface.type == 3 || rsInterface.type == 4)
                rsInterface.textColor = buffer.readInt();
            if(rsInterface.type == 3 || rsInterface.type == 4)
            {
                rsInterface.anInt219 = buffer.readInt();
                rsInterface.anInt216 = buffer.readInt();
                rsInterface.anInt239 = buffer.readInt();
            }
            if(rsInterface.type == 5)
            {
                String s = buffer.readString();
                if(streamLoader_1 != null && s.length() > 0)
                {
                    int i4 = s.lastIndexOf(",");
                    rsInterface.sprite1 = RSInterface.method207(Integer.parseInt(s.substring(i4 + 1)), streamLoader_1, s.substring(0, i4));
                }
                s = buffer.readString();
                if(streamLoader_1 != null && s.length() > 0)
                {
                    int j4 = s.lastIndexOf(",");
                    rsInterface.sprite2 = RSInterface.method207(Integer.parseInt(s.substring(j4 + 1)), streamLoader_1, s.substring(0, j4));
                }
            }
            if(rsInterface.type == 6)
            {
                int l = buffer.readUByte();
                if(l != 0)
                {
                    rsInterface.anInt233 = 1;
                    rsInterface.mediaID = (l - 1 << 8) + buffer.readUByte();
                }
                l = buffer.readUByte();
                if(l != 0)
                {
                    rsInterface.anInt255 = 1;
                    rsInterface.anInt256 = (l - 1 << 8) + buffer.readUByte();
                }
                l = buffer.readUByte();
                if(l != 0)
                    rsInterface.anInt257 = (l - 1 << 8) + buffer.readUByte();
                else
                    rsInterface.anInt257 = -1;
                l = buffer.readUByte();
                if(l != 0)
                    rsInterface.anInt258 = (l - 1 << 8) + buffer.readUByte();
                else
                    rsInterface.anInt258 = -1;
                rsInterface.anInt269 = buffer.readUShort();
                rsInterface.anInt270 = buffer.readUShort();
                rsInterface.anInt271 = buffer.readUShort();
            }
            if(rsInterface.type == 7)
            {
                rsInterface.inv = new int[rsInterface.width * rsInterface.height];
                rsInterface.invStackSizes = new int[rsInterface.width * rsInterface.height];
                rsInterface.centerText = buffer.readUByte() == 1;
                int l2 = buffer.readUByte();
                if(textDrawingAreas != null)
                    rsInterface.textDrawingAreas = textDrawingAreas[l2];
                rsInterface.aBoolean268 = buffer.readUByte() == 1;
                rsInterface.textColor = buffer.readInt();
                rsInterface.invSpritePadX = buffer.readShort();
                rsInterface.invSpritePadY = buffer.readShort();
                rsInterface.isInventoryInterface = buffer.readUByte() == 1;
                rsInterface.actions = new String[5];
                for(int k4 = 0; k4 < 5; k4++)
                {
                    rsInterface.actions[k4] = buffer.readString();
                    if(rsInterface.actions[k4].length() == 0)
                        rsInterface.actions[k4] = null;
                }

            }
            if(rsInterface.atActionType == 2 || rsInterface.type == 2)
            {
                rsInterface.selectedActionName = buffer.readString();
                rsInterface.spellName = buffer.readString();
                rsInterface.spellUsableOn = buffer.readUShort();
            }

            if(rsInterface.type == 8)
			  rsInterface.message = buffer.readString();

            if(rsInterface.atActionType == 1 || rsInterface.atActionType == 4 || rsInterface.atActionType == 5 || rsInterface.atActionType == 6)
            {
                rsInterface.tooltip = buffer.readString();
                if(rsInterface.tooltip.length() == 0)
                {
                    if(rsInterface.atActionType == 1)
                        rsInterface.tooltip = "Ok";
                    if(rsInterface.atActionType == 4)
                        rsInterface.tooltip = "Select";
                    if(rsInterface.atActionType == 5)
                        rsInterface.tooltip = "Select";
                    if(rsInterface.atActionType == 6)
                        rsInterface.tooltip = "Continue";
                }
            }
        
//aryan	Bot.notifyInterface(rsInterface);
	}
        RSInterface.aMRUNodes_238 = null;
    }

    private Model method206(int i, int j)
    {
        Model model = (Model) RSInterface.aMRUNodes_264.get((i << 16) + j);
        if(model != null)
            return model;
        if(i == 1)
            model = Model.method462(j);
        if(i == 2)
            model = EntityDef.forID(j).method160();
        if(i == 3)
            model = Client.myPlayer.method453();
        if(i == 4)
            model = ItemDef.forID(j).method202(50);
        if(i == 5)
            model = null;
        if(model != null)
            RSInterface.aMRUNodes_264.put((i << 16) + j, model);
        return model;
    }

    private static Sprite method207(int i, Archive archive, String s)
    {
        long l = (TextClass.method585(s) << 8) + i;
        Sprite sprite = (Sprite) RSInterface.aMRUNodes_238.get(l);
        if(sprite != null)
            return sprite;
        try
        {
            sprite = new Sprite(archive, s, i);
            RSInterface.aMRUNodes_238.put(l, sprite);
        }
        catch(Exception _ex)
        {
            return null;
        }
        return sprite;
    }

    public static void method208(boolean flag, Model model)
    {
        int i = 0;//was parameter
        int j = 5;//was parameter
        if(flag)
            return;
        RSInterface.aMRUNodes_264.clear();
        if(model != null && j != 4)
            RSInterface.aMRUNodes_264.put((j << 16) + i, model);
    }

    public Model method209(int j, int k, boolean flag)
    {
        Model model;
        if(flag)
            model = this.method206(this.anInt255, this.anInt256);
        else
            model = this.method206(this.anInt233, this.mediaID);
        if(model == null)
            return null;
        if(k == -1 && j == -1 && model.anIntArray1640 == null)
            return model;
        Model model_1 = new Model(true, Frame.isInvalid(k) & Frame.isInvalid(j), false, model);
        if(k != -1 || j != -1)
            model_1.method469();
        if(k != -1)
            model_1.method470(k);
        if(j != -1)
            model_1.method470(j);
        model_1.method479(64, 768, -50, -10, -50, true);
            return model_1;
    }

    public RSInterface()
    {
    }

    public Sprite sprite1;
    public int anInt208;
    public Sprite sprites[];
    public static RSInterface interfaceCache[];
    public int anIntArray212[];
    public int anInt214;
    public int spritesX[];
    public int anInt216;
    public int atActionType;
    public String spellName;
    public int anInt219;
    public int width;
    public String tooltip;
    public String selectedActionName;
    public boolean centerText;
    public int scrollPosition;
    public String actions[];
    public int valueIndexArray[][];
    public boolean aBoolean227;
    public String aString228;
    public int anInt230;
    public int invSpritePadX;
    public int textColor;
    public int anInt233;
    public int mediaID;
    public boolean aBoolean235;
    public int parentID;
    public int spellUsableOn;
    private static Cache aMRUNodes_238;
    public int anInt239;
    public int children[];
    public int childX[];
    public boolean usableItemInterface;
    public TextDrawingArea textDrawingAreas;
    public int invSpritePadY;
    public int anIntArray245[];
    public int anInt246;
    public int spritesY[];
    public String message;
    public boolean isInventoryInterface;
    public int id;
    public int invStackSizes[];
    public int inv[];
    public byte aByte254;
    private int anInt255;
    private int anInt256;
    public int anInt257;
    public int anInt258;
    public boolean aBoolean259;
    public Sprite sprite2;
    public int scrollMax;
    public int type;
    public int anInt263;
    private static final Cache aMRUNodes_264 = new Cache(30);
    public int anInt265;
    public boolean aBoolean266;
    public int height;
    public boolean aBoolean268;
    public int anInt269;
    public int anInt270;
    public int anInt271;
    public int childY[];

}
