package com.jagex.cache.def;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.Client;
import com.jagex.cache.Archive;
import com.jagex.cache.anim.Frame;
import com.jagex.cache.setting.VariableBits;
import com.jagex.entity.model.Model;
import com.jagex.io.Buffer;
import com.jagex.link.Cache;

public final class EntityDef
{

    public static EntityDef forID(int i)
    {
        for(int j = 0; j < 20; j++)
            if(EntityDef.cache[j].type == i)
                return EntityDef.cache[j];

        EntityDef.anInt56 = (EntityDef.anInt56 + 1) % 20;
        EntityDef entityDef = EntityDef.cache[EntityDef.anInt56] = new EntityDef();
        EntityDef.buffer.position = EntityDef.streamIndices[i];
        entityDef.type = i;
        entityDef.readValues(EntityDef.buffer);
        return entityDef;
    }

    public Model method160()
    {
        if(this.childrenIDs != null)
        {
            EntityDef entityDef = this.method161();
            if(entityDef == null)
                return null;
            else
                return entityDef.method160();
        }
        if(this.anIntArray73 == null)
            return null;
        boolean flag1 = false;
        for(int i = 0; i < this.anIntArray73.length; i++)
            if(!Model.method463(this.anIntArray73[i]))
                flag1 = true;

        if(flag1)
            return null;
        Model aclass30_sub2_sub4_sub6s[] = new Model[this.anIntArray73.length];
        for(int j = 0; j < this.anIntArray73.length; j++)
            aclass30_sub2_sub4_sub6s[j] = Model.method462(this.anIntArray73[j]);

        Model model;
        if(aclass30_sub2_sub4_sub6s.length == 1)
            model = aclass30_sub2_sub4_sub6s[0];
        else
            model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);
        if(this.anIntArray76 != null)
        {
            for(int k = 0; k < this.anIntArray76.length; k++)
                model.method476(this.anIntArray76[k], this.anIntArray70[k]);

        }
        return model;
    }

    public EntityDef method161()
    {
        int j = -1;
        if(this.anInt57 != -1)
        {
            j = VariableBits.get(this.anInt57, EntityDef.clientInstance.variousSettings);
        } else
        if(this.anInt59 != -1)
            j = EntityDef.clientInstance.variousSettings[this.anInt59];
        if(j < 0 || j >= this.childrenIDs.length || this.childrenIDs[j] == -1)
            return null;
        else
            return EntityDef.forID(this.childrenIDs[j]);
    }

    public static void unpackConfig(Archive archive)
    {
        EntityDef.buffer = new Buffer(archive.getEntry("npc.dat"));
        Buffer buffer2 = new Buffer(archive.getEntry("npc.idx"));
        int totalNPCs = buffer2.readUShort();
        EntityDef.streamIndices = new int[totalNPCs];
        int i = 2;
        for(int j = 0; j < totalNPCs; j++)
        {
            EntityDef.streamIndices[j] = i;
            i += buffer2.readUShort();
        }

        EntityDef.cache = new EntityDef[20];
        for(int k = 0; k < 20; k++)
            EntityDef.cache[k] = new EntityDef();

    }

    public static void nullLoader()
    {
        EntityDef.mruNodes = null;
        EntityDef.streamIndices = null;
        EntityDef.cache = null;
        EntityDef.buffer = null;
    }

    public Model method164(int j, int k, int ai[])
    {
        if(this.childrenIDs != null)
        {
            EntityDef entityDef = this.method161();
            if(entityDef == null)
                return null;
            else
                return entityDef.method164(j, k, ai);
        }
        Model model = (Model) EntityDef.mruNodes.get(this.type);
        if(model == null)
        {
            boolean flag = false;
            for(int i1 = 0; i1 < this.anIntArray94.length; i1++)
                if(!Model.method463(this.anIntArray94[i1]))
                    flag = true;

            if(flag)
                return null;
            Model aclass30_sub2_sub4_sub6s[] = new Model[this.anIntArray94.length];
            for(int j1 = 0; j1 < this.anIntArray94.length; j1++)
                aclass30_sub2_sub4_sub6s[j1] = Model.method462(this.anIntArray94[j1]);

            if(aclass30_sub2_sub4_sub6s.length == 1)
                model = aclass30_sub2_sub4_sub6s[0];
            else
                model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);
            if(this.anIntArray76 != null)
            {
                for(int k1 = 0; k1 < this.anIntArray76.length; k1++)
                    model.method476(this.anIntArray76[k1], this.anIntArray70[k1]);

            }
            model.method469();
            model.method479(64 + this.anInt85, 850 + this.anInt92, -30, -50, -30, true);
            EntityDef.mruNodes.put(this.type, model);
        }
        Model model_1 = Model.aModel_1621;
        model_1.method464(model, Frame.isInvalid(k) & Frame.isInvalid(j));
        if(k != -1 && j != -1)
            model_1.method471(ai, j, k);
        else
        if(k != -1)
            model_1.method470(k);
        if(this.anInt91 != 128 || this.anInt86 != 128)
            model_1.method478(this.anInt91, this.anInt91, this.anInt86);
        model_1.method466();
        model_1.anIntArrayArray1658 = null;
        model_1.anIntArrayArray1657 = null;
        if(this.aByte68 == 1)
            model_1.aBoolean1659 = true;
        return model_1;
    }

    private void readValues(Buffer buffer)
    {
        do
        {
            int i = buffer.readUByte();
            if(i == 0)
                return;
            if(i == 1)
            {
                int j = buffer.readUByte();
                this.anIntArray94 = new int[j];
                for(int j1 = 0; j1 < j; j1++)
                    this.anIntArray94[j1] = buffer.readUShort();

            } else
            if(i == 2)
                this.name = buffer.readString();
            else
            if(i == 3)
                this.description = buffer.readStringBytes();
            else
            if(i == 12)
                this.aByte68 = buffer.readByte();
            else
            if(i == 13)
                this.anInt77 = buffer.readUShort();
            else
            if(i == 14)
                this.anInt67 = buffer.readUShort();
            else
            if(i == 17)
            {
                this.anInt67 = buffer.readUShort();
                this.anInt58 = buffer.readUShort();
                this.anInt83 = buffer.readUShort();
                this.anInt55 = buffer.readUShort();
            } else
            if(i >= 30 && i < 40)
            {
                if(this.actions == null)
                    this.actions = new String[5];
                this.actions[i - 30] = buffer.readString();
                if(this.actions[i - 30].equalsIgnoreCase("hidden"))
                    this.actions[i - 30] = null;
            } else
            if(i == 40)
            {
                int k = buffer.readUByte();
                this.anIntArray76 = new int[k];
                this.anIntArray70 = new int[k];
                for(int k1 = 0; k1 < k; k1++)
                {
                    this.anIntArray76[k1] = buffer.readUShort();
                    this.anIntArray70[k1] = buffer.readUShort();
                }

            } else
            if(i == 60)
            {
                int l = buffer.readUByte();
                this.anIntArray73 = new int[l];
                for(int l1 = 0; l1 < l; l1++)
                    this.anIntArray73[l1] = buffer.readUShort();

            } else
            if(i == 90)
                buffer.readUShort();
            else
            if(i == 91)
                buffer.readUShort();
            else
            if(i == 92)
                buffer.readUShort();
            else
            if(i == 93)
                this.aBoolean87 = false;
            else
            if(i == 95)
                this.combatLevel = buffer.readUShort();
            else
            if(i == 97)
                this.anInt91 = buffer.readUShort();
            else
            if(i == 98)
                this.anInt86 = buffer.readUShort();
            else
            if(i == 99)
                this.aBoolean93 = true;
            else
            if(i == 100)
                this.anInt85 = buffer.readByte();
            else
            if(i == 101)
                this.anInt92 = buffer.readByte() * 5;
            else
            if(i == 102)
                this.anInt75 = buffer.readUShort();
            else
            if(i == 103)
                this.anInt79 = buffer.readUShort();
            else
            if(i == 106)
            {
                this.anInt57 = buffer.readUShort();
                if(this.anInt57 == 65535)
                    this.anInt57 = -1;
                this.anInt59 = buffer.readUShort();
                if(this.anInt59 == 65535)
                    this.anInt59 = -1;
                int i1 = buffer.readUByte();
                this.childrenIDs = new int[i1 + 1];
                for(int i2 = 0; i2 <= i1; i2++)
                {
                    this.childrenIDs[i2] = buffer.readUShort();
                    if(this.childrenIDs[i2] == 65535)
                        this.childrenIDs[i2] = -1;
                }

            } else
            if(i == 107)
                this.aBoolean84 = false;
        } while(true);
    }

    private EntityDef()
    {
        this.anInt55 = -1;
        this.anInt57 = -1;
        this.anInt58 = -1;
        this.anInt59 = -1;
        this.combatLevel = -1;
        this.anInt64 = 1834;
        this.anInt67 = -1;
        this.aByte68 = 1;
        this.anInt75 = -1;
        this.anInt77 = -1;
        this.type = -1L;
        this.anInt79 = 32;
        this.anInt83 = -1;
        this.aBoolean84 = true;
        this.anInt86 = 128;
        this.aBoolean87 = true;
        this.anInt91 = 128;
        this.aBoolean93 = false;
    }

    public int anInt55;
    private static int anInt56;
    private int anInt57;
    public int anInt58;
    private int anInt59;
    private static Buffer buffer;
    public int combatLevel;
    private final int anInt64;
    public String name;
    public String actions[];
    public int anInt67;
    public byte aByte68;
    private int[] anIntArray70;
    private static int[] streamIndices;
    private int[] anIntArray73;
    public int anInt75;
    private int[] anIntArray76;
    public int anInt77;
    public long type;
    public int anInt79;
    private static EntityDef[] cache;
    public static Client clientInstance;
    public int anInt83;
    public boolean aBoolean84;
    private int anInt85;
    private int anInt86;
    public boolean aBoolean87;
    public int childrenIDs[];
    public byte description[];
    private int anInt91;
    private int anInt92;
    public boolean aBoolean93;
    private int[] anIntArray94;
    public static Cache mruNodes = new Cache(30);

}
