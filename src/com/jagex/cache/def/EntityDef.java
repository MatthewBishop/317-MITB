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
            if(cache[j].type == (long)i)
                return cache[j];

        anInt56 = (anInt56 + 1) % 20;
        EntityDef entityDef = cache[anInt56] = new EntityDef();
        buffer.position = streamIndices[i];
        entityDef.type = i;
        entityDef.readValues(buffer);
        return entityDef;
    }

    public Model method160()
    {
        if(childrenIDs != null)
        {
            EntityDef entityDef = method161();
            if(entityDef == null)
                return null;
            else
                return entityDef.method160();
        }
        if(anIntArray73 == null)
            return null;
        boolean flag1 = false;
        for(int i = 0; i < anIntArray73.length; i++)
            if(!Model.method463(anIntArray73[i]))
                flag1 = true;

        if(flag1)
            return null;
        Model aclass30_sub2_sub4_sub6s[] = new Model[anIntArray73.length];
        for(int j = 0; j < anIntArray73.length; j++)
            aclass30_sub2_sub4_sub6s[j] = Model.method462(anIntArray73[j]);

        Model model;
        if(aclass30_sub2_sub4_sub6s.length == 1)
            model = aclass30_sub2_sub4_sub6s[0];
        else
            model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);
        if(anIntArray76 != null)
        {
            for(int k = 0; k < anIntArray76.length; k++)
                model.method476(anIntArray76[k], anIntArray70[k]);

        }
        return model;
    }

    public EntityDef method161()
    {
        int j = -1;
        if(anInt57 != -1)
        {
            j = VariableBits.get(anInt57, clientInstance.variousSettings);
        } else
        if(anInt59 != -1)
            j = clientInstance.variousSettings[anInt59];
        if(j < 0 || j >= childrenIDs.length || childrenIDs[j] == -1)
            return null;
        else
            return forID(childrenIDs[j]);
    }

    public static void unpackConfig(Archive archive)
    {
        buffer = new Buffer(archive.getEntry("npc.dat"));
        Buffer buffer2 = new Buffer(archive.getEntry("npc.idx"));
        int totalNPCs = buffer2.readUShort();
        streamIndices = new int[totalNPCs];
        int i = 2;
        for(int j = 0; j < totalNPCs; j++)
        {
            streamIndices[j] = i;
            i += buffer2.readUShort();
        }

        cache = new EntityDef[20];
        for(int k = 0; k < 20; k++)
            cache[k] = new EntityDef();

    }

    public static void nullLoader()
    {
        mruNodes = null;
        streamIndices = null;
        cache = null;
        buffer = null;
    }

    public Model method164(int j, int k, int ai[])
    {
        if(childrenIDs != null)
        {
            EntityDef entityDef = method161();
            if(entityDef == null)
                return null;
            else
                return entityDef.method164(j, k, ai);
        }
        Model model = (Model) mruNodes.get(type);
        if(model == null)
        {
            boolean flag = false;
            for(int i1 = 0; i1 < anIntArray94.length; i1++)
                if(!Model.method463(anIntArray94[i1]))
                    flag = true;

            if(flag)
                return null;
            Model aclass30_sub2_sub4_sub6s[] = new Model[anIntArray94.length];
            for(int j1 = 0; j1 < anIntArray94.length; j1++)
                aclass30_sub2_sub4_sub6s[j1] = Model.method462(anIntArray94[j1]);

            if(aclass30_sub2_sub4_sub6s.length == 1)
                model = aclass30_sub2_sub4_sub6s[0];
            else
                model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);
            if(anIntArray76 != null)
            {
                for(int k1 = 0; k1 < anIntArray76.length; k1++)
                    model.method476(anIntArray76[k1], anIntArray70[k1]);

            }
            model.method469();
            model.method479(64 + anInt85, 850 + anInt92, -30, -50, -30, true);
            mruNodes.put(type, model);
        }
        Model model_1 = Model.aModel_1621;
        model_1.method464(model, Frame.isInvalid(k) & Frame.isInvalid(j));
        if(k != -1 && j != -1)
            model_1.method471(ai, j, k);
        else
        if(k != -1)
            model_1.method470(k);
        if(anInt91 != 128 || anInt86 != 128)
            model_1.method478(anInt91, anInt91, anInt86);
        model_1.method466();
        model_1.anIntArrayArray1658 = null;
        model_1.anIntArrayArray1657 = null;
        if(aByte68 == 1)
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
                anIntArray94 = new int[j];
                for(int j1 = 0; j1 < j; j1++)
                    anIntArray94[j1] = buffer.readUShort();

            } else
            if(i == 2)
                name = buffer.readString();
            else
            if(i == 3)
                description = buffer.readStringBytes();
            else
            if(i == 12)
                aByte68 = buffer.readByte();
            else
            if(i == 13)
                anInt77 = buffer.readUShort();
            else
            if(i == 14)
                anInt67 = buffer.readUShort();
            else
            if(i == 17)
            {
                anInt67 = buffer.readUShort();
                anInt58 = buffer.readUShort();
                anInt83 = buffer.readUShort();
                anInt55 = buffer.readUShort();
            } else
            if(i >= 30 && i < 40)
            {
                if(actions == null)
                    actions = new String[5];
                actions[i - 30] = buffer.readString();
                if(actions[i - 30].equalsIgnoreCase("hidden"))
                    actions[i - 30] = null;
            } else
            if(i == 40)
            {
                int k = buffer.readUByte();
                anIntArray76 = new int[k];
                anIntArray70 = new int[k];
                for(int k1 = 0; k1 < k; k1++)
                {
                    anIntArray76[k1] = buffer.readUShort();
                    anIntArray70[k1] = buffer.readUShort();
                }

            } else
            if(i == 60)
            {
                int l = buffer.readUByte();
                anIntArray73 = new int[l];
                for(int l1 = 0; l1 < l; l1++)
                    anIntArray73[l1] = buffer.readUShort();

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
                aBoolean87 = false;
            else
            if(i == 95)
                combatLevel = buffer.readUShort();
            else
            if(i == 97)
                anInt91 = buffer.readUShort();
            else
            if(i == 98)
                anInt86 = buffer.readUShort();
            else
            if(i == 99)
                aBoolean93 = true;
            else
            if(i == 100)
                anInt85 = buffer.readByte();
            else
            if(i == 101)
                anInt92 = buffer.readByte() * 5;
            else
            if(i == 102)
                anInt75 = buffer.readUShort();
            else
            if(i == 103)
                anInt79 = buffer.readUShort();
            else
            if(i == 106)
            {
                anInt57 = buffer.readUShort();
                if(anInt57 == 65535)
                    anInt57 = -1;
                anInt59 = buffer.readUShort();
                if(anInt59 == 65535)
                    anInt59 = -1;
                int i1 = buffer.readUByte();
                childrenIDs = new int[i1 + 1];
                for(int i2 = 0; i2 <= i1; i2++)
                {
                    childrenIDs[i2] = buffer.readUShort();
                    if(childrenIDs[i2] == 65535)
                        childrenIDs[i2] = -1;
                }

            } else
            if(i == 107)
                aBoolean84 = false;
        } while(true);
    }

    private EntityDef()
    {
        anInt55 = -1;
        anInt57 = -1;
        anInt58 = -1;
        anInt59 = -1;
        combatLevel = -1;
        anInt64 = 1834;
        anInt67 = -1;
        aByte68 = 1;
        anInt75 = -1;
        anInt77 = -1;
        type = -1L;
        anInt79 = 32;
        anInt83 = -1;
        aBoolean84 = true;
        anInt86 = 128;
        aBoolean87 = true;
        anInt91 = 128;
        aBoolean93 = false;
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
