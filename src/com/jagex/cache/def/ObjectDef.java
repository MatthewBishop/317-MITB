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
import com.jagex.net.OnDemandFetcher;

public final class ObjectDef
{

    public static ObjectDef forID(int i)
    {
        for(int j = 0; j < 20; j++)
            if(ObjectDef.cache[j].type == i)
                return ObjectDef.cache[j];

        ObjectDef.cacheIndex = (ObjectDef.cacheIndex + 1) % 20;
        ObjectDef class46 = ObjectDef.cache[ObjectDef.cacheIndex];
        ObjectDef.buffer.position = ObjectDef.streamIndices[i];
        class46.type = i;
        class46.setDefaults();
        class46.readValues(ObjectDef.buffer);
        return class46;
    }

    private void setDefaults()
    {
        this.anIntArray773 = null;
        this.anIntArray776 = null;
        this.name = null;
        this.description = null;
        this.modifiedModelColors = null;
        this.originalModelColors = null;
        this.anInt744 = 1;
        this.anInt761 = 1;
        this.aBoolean767 = true;
        this.aBoolean757 = true;
        this.hasActions = false;
        this.aBoolean762 = false;
        this.aBoolean769 = false;
        this.aBoolean764 = false;
        this.anInt781 = -1;
        this.anInt775 = 16;
        this.aByte737 = 0;
        this.aByte742 = 0;
        this.actions = null;
        this.anInt746 = -1;
        this.anInt758 = -1;
        this.aBoolean751 = false;
        this.aBoolean779 = true;
        this.anInt748 = 128;
        this.anInt772 = 128;
        this.anInt740 = 128;
        this.anInt768 = 0;
        this.anInt738 = 0;
        this.anInt745 = 0;
        this.anInt783 = 0;
        this.aBoolean736 = false;
        this.aBoolean766 = false;
        this.anInt760 = -1;
        this.anInt774 = -1;
        this.anInt749 = -1;
        this.childrenIDs = null;
    }

    public void method574(OnDemandFetcher class42_sub1)
    {
        if(this.anIntArray773 == null)
            return;
        for(int j = 0; j < this.anIntArray773.length; j++)
            class42_sub1.method560(this.anIntArray773[j] & 0xffff, 0);
    }

    public static void nullLoader()
    {
        ObjectDef.mruNodes1 = null;
        ObjectDef.mruNodes2 = null;
        ObjectDef.streamIndices = null;
        ObjectDef.cache = null;
ObjectDef.buffer = null;
    }

    public static void unpackConfig(Archive archive)
    {
        ObjectDef.buffer = new Buffer(archive.getEntry("loc.dat"));
        Buffer buffer = new Buffer(archive.getEntry("loc.idx"));
        int totalObjects = buffer.readUShort();
        ObjectDef.streamIndices = new int[totalObjects];
        int i = 2;
        for(int j = 0; j < totalObjects; j++)
        {
            ObjectDef.streamIndices[j] = i;
            i += buffer.readUShort();
        }

        ObjectDef.cache = new ObjectDef[20];
        for(int k = 0; k < 20; k++)
            ObjectDef.cache[k] = new ObjectDef();

    }

    public boolean method577(int i)
    {
        if(this.anIntArray776 == null)
        {
            if(this.anIntArray773 == null)
                return true;
            if(i != 10)
                return true;
            boolean flag1 = true;
            for(int k = 0; k < this.anIntArray773.length; k++)
                flag1 &= Model.method463(this.anIntArray773[k] & 0xffff);

            return flag1;
        }
        for(int j = 0; j < this.anIntArray776.length; j++)
            if(this.anIntArray776[j] == i)
                return Model.method463(this.anIntArray773[j] & 0xffff);

        return true;
    }

    public Model method578(int i, int j, int k, int l, int i1, int j1, int k1)
    {
        Model model = this.method581(i, k1, j);
        if(model == null)
            return null;
        if(this.aBoolean762 || this.aBoolean769)
            model = new Model(this.aBoolean762, this.aBoolean769, model);
        if(this.aBoolean762)
        {
            int l1 = (k + l + i1 + j1) / 4;
            for(int i2 = 0; i2 < model.vertexCount; i2++)
            {
                int j2 = model.anIntArray1627[i2];
                int k2 = model.anIntArray1629[i2];
                int l2 = k + ((l - k) * (j2 + 64)) / 128;
                int i3 = j1 + ((i1 - j1) * (j2 + 64)) / 128;
                int j3 = l2 + ((i3 - l2) * (k2 + 64)) / 128;
                model.anIntArray1628[i2] += j3 - l1;
            }

            model.method467();
        }
        return model;
    }

    public boolean method579()
    {
        if(this.anIntArray773 == null)
            return true;
        boolean flag1 = true;
        for(int i = 0; i < this.anIntArray773.length; i++)
            flag1 &= Model.method463(this.anIntArray773[i] & 0xffff);
            return flag1;
    }

    public ObjectDef method580()
    {
        int i = -1;
        if(this.anInt774 != -1)
        {
        	i = VariableBits.get(this.anInt774, ObjectDef.clientInstance.variousSettings);
        } else
        if(this.anInt749 != -1)
            i = ObjectDef.clientInstance.variousSettings[this.anInt749];
        if(i < 0 || i >= this.childrenIDs.length || this.childrenIDs[i] == -1)
            return null;
        else
            return ObjectDef.forID(this.childrenIDs[i]);
    }

    private Model method581(int j, int k, int l)
    {
        Model model = null;
        long l1;
        if(this.anIntArray776 == null)
        {
            if(j != 10)
                return null;
            l1 = (this.type << 6) + l + ((long)(k + 1) << 32);
            Model model_1 = (Model) ObjectDef.mruNodes2.get(l1);
            if(model_1 != null)
                return model_1;
            if(this.anIntArray773 == null)
                return null;
            boolean flag1 = this.aBoolean751 ^ (l > 3);
            int k1 = this.anIntArray773.length;
            for(int i2 = 0; i2 < k1; i2++)
            {
                int l2 = this.anIntArray773[i2];
                if(flag1)
                    l2 += 0x10000;
                model = (Model) ObjectDef.mruNodes1.get(l2);
                if(model == null)
                {
                    model = Model.method462(l2 & 0xffff);
                    if(model == null)
                        return null;
                    if(flag1)
                        model.method477();
                    ObjectDef.mruNodes1.put(l2, model);
                }
                if(k1 > 1)
                    ObjectDef.aModelArray741s[i2] = model;
            }

            if(k1 > 1)
                model = new Model(k1, ObjectDef.aModelArray741s);
        } else
        {
            int i1 = -1;
            for(int j1 = 0; j1 < this.anIntArray776.length; j1++)
            {
                if(this.anIntArray776[j1] != j)
                    continue;
                i1 = j1;
                break;
            }

            if(i1 == -1)
                return null;
            l1 = (this.type << 6) + (i1 << 3) + l + ((long)(k + 1) << 32);
            Model model_2 = (Model) ObjectDef.mruNodes2.get(l1);
            if(model_2 != null)
                return model_2;
            int j2 = this.anIntArray773[i1];
            boolean flag3 = this.aBoolean751 ^ (l > 3);
            if(flag3)
                j2 += 0x10000;
            model = (Model) ObjectDef.mruNodes1.get(j2);
            if(model == null)
            {
                model = Model.method462(j2 & 0xffff);
                if(model == null)
                    return null;
                if(flag3)
                    model.method477();
                ObjectDef.mruNodes1.put(j2, model);
            }
        }
        boolean flag;
        flag = this.anInt748 != 128 || this.anInt772 != 128 || this.anInt740 != 128;
        boolean flag2;
        flag2 = this.anInt738 != 0 || this.anInt745 != 0 || this.anInt783 != 0;
        Model model_3 = new Model(this.modifiedModelColors == null, Frame.isInvalid(k), l == 0 && k == -1 && !flag && !flag2, model);
        if(k != -1)
        {
            model_3.method469();
            model_3.method470(k);
            model_3.anIntArrayArray1658 = null;
            model_3.anIntArrayArray1657 = null;
        }
        while(l-- > 0) 
            model_3.method473();
        if(this.modifiedModelColors != null)
        {
            for(int k2 = 0; k2 < this.modifiedModelColors.length; k2++)
                model_3.method476(this.modifiedModelColors[k2], this.originalModelColors[k2]);

        }
        if(flag)
            model_3.method478(this.anInt748, this.anInt740, this.anInt772);
        if(flag2)
            model_3.method475(this.anInt738, this.anInt745, this.anInt783);
        model_3.method479(64 + this.aByte737, 768 + this.aByte742 * 5, -50, -10, -50, !this.aBoolean769);
        if(this.anInt760 == 1)
            model_3.anInt1654 = model_3.modelHeight;
        ObjectDef.mruNodes2.put(l1, model_3);
        return model_3;
    }

    private void readValues(Buffer buffer)
    {
        int i = -1;
label0:
        do
        {
            int j;
            do
            {
                j = buffer.readUByte();
                if(j == 0)
                    break label0;
                if(j == 1)
                {
                    int k = buffer.readUByte();
                    if(k > 0)
                        if(this.anIntArray773 == null || ObjectDef.lowMem)
                        {
                            this.anIntArray776 = new int[k];
                            this.anIntArray773 = new int[k];
                            for(int k1 = 0; k1 < k; k1++)
                            {
                                this.anIntArray773[k1] = buffer.readUShort();
                                this.anIntArray776[k1] = buffer.readUByte();
                            }

                        } else
                        {
                            buffer.position += k * 3;
                        }
                } else
                if(j == 2)
                    this.name = buffer.readString();
                else
                if(j == 3)
                    this.description = buffer.readStringBytes();
                else
                if(j == 5)
                {
                    int l = buffer.readUByte();
                    if(l > 0)
                        if(this.anIntArray773 == null || ObjectDef.lowMem)
                        {
                            this.anIntArray776 = null;
                            this.anIntArray773 = new int[l];
                            for(int l1 = 0; l1 < l; l1++)
                                this.anIntArray773[l1] = buffer.readUShort();

                        } else
                        {
                            buffer.position += l * 2;
                        }
                } else
                if(j == 14)
                    this.anInt744 = buffer.readUByte();
                else
                if(j == 15)
                    this.anInt761 = buffer.readUByte();
                else
                if(j == 17)
                    this.aBoolean767 = false;
                else
                if(j == 18)
                    this.aBoolean757 = false;
                else
                if(j == 19)
                {
                    i = buffer.readUByte();
                    if(i == 1)
                        this.hasActions = true;
                } else
                if(j == 21)
                    this.aBoolean762 = true;
                else
                if(j == 22)
                    this.aBoolean769 = true;
                else
                if(j == 23)
                    this.aBoolean764 = true;
                else
                if(j == 24)
                {
                    this.anInt781 = buffer.readUShort();
                    if(this.anInt781 == 65535)
                        this.anInt781 = -1;
                } else
                if(j == 28)
                    this.anInt775 = buffer.readUByte();
                else
                if(j == 29)
                    this.aByte737 = buffer.readByte();
                else
                if(j == 39)
                    this.aByte742 = buffer.readByte();
                else
                if(j >= 30 && j < 39)
                {
                    if(this.actions == null)
                        this.actions = new String[5];
                    this.actions[j - 30] = buffer.readString();
                    if(this.actions[j - 30].equalsIgnoreCase("hidden"))
                        this.actions[j - 30] = null;
                } else
                if(j == 40)
                {
                    int i1 = buffer.readUByte();
                    this.modifiedModelColors = new int[i1];
                    this.originalModelColors = new int[i1];
                    for(int i2 = 0; i2 < i1; i2++)
                    {
                        this.modifiedModelColors[i2] = buffer.readUShort();
                        this.originalModelColors[i2] = buffer.readUShort();
                    }

                } else
                if(j == 60)
                    this.anInt746 = buffer.readUShort();
                else
                if(j == 62)
                    this.aBoolean751 = true;
                else
                if(j == 64)
                    this.aBoolean779 = false;
                else
                if(j == 65)
                    this.anInt748 = buffer.readUShort();
                else
                if(j == 66)
                    this.anInt772 = buffer.readUShort();
                else
                if(j == 67)
                    this.anInt740 = buffer.readUShort();
                else
                if(j == 68)
                    this.anInt758 = buffer.readUShort();
                else
                if(j == 69)
                    this.anInt768 = buffer.readUByte();
                else
                if(j == 70)
                    this.anInt738 = buffer.readShort();
                else
                if(j == 71)
                    this.anInt745 = buffer.readShort();
                else
                if(j == 72)
                    this.anInt783 = buffer.readShort();
                else
                if(j == 73)
                    this.aBoolean736 = true;
                else
                if(j == 74)
                {
                    this.aBoolean766 = true;
                } else
                {
                    if(j != 75)
                        continue;
                    this.anInt760 = buffer.readUByte();
                }
                continue label0;
            } while(j != 77);
            this.anInt774 = buffer.readUShort();
            if(this.anInt774 == 65535)
                this.anInt774 = -1;
            this.anInt749 = buffer.readUShort();
            if(this.anInt749 == 65535)
                this.anInt749 = -1;
            int j1 = buffer.readUByte();
            this.childrenIDs = new int[j1 + 1];
            for(int j2 = 0; j2 <= j1; j2++)
            {
                this.childrenIDs[j2] = buffer.readUShort();
                if(this.childrenIDs[j2] == 65535)
                    this.childrenIDs[j2] = -1;
            }

        } while(true);
        if(i == -1)
        {
            this.hasActions = this.anIntArray773 != null && (this.anIntArray776 == null || this.anIntArray776[0] == 10);
            if(this.actions != null)
                this.hasActions = true;
        }
        if(this.aBoolean766)
        {
            this.aBoolean767 = false;
            this.aBoolean757 = false;
        }
        if(this.anInt760 == -1)
            this.anInt760 = this.aBoolean767 ? 1 : 0;
    }

    private ObjectDef()
    {
        this.type = -1;
    }

    public boolean aBoolean736;
    private byte aByte737;
    private int anInt738;
    public String name;
    private int anInt740;
    private static final Model[] aModelArray741s = new Model[4];
    private byte aByte742;
    public int anInt744;
    private int anInt745;
    public int anInt746;
    private int[] originalModelColors;
    private int anInt748;
    public int anInt749;
    private boolean aBoolean751;
    public static boolean lowMem;
    private static Buffer buffer;
    public int type;
    private static int[] streamIndices;
    public boolean aBoolean757;
    public int anInt758;
    public int childrenIDs[];
    private int anInt760;
    public int anInt761;
    public boolean aBoolean762;
    public boolean aBoolean764;
    public static Client clientInstance;
    private boolean aBoolean766;
    public boolean aBoolean767;
    public int anInt768;
    private boolean aBoolean769;
    private static int cacheIndex;
    private int anInt772;
    private int[] anIntArray773;
    public int anInt774;
    public int anInt775;
    private int[] anIntArray776;
    public byte description[];
    public boolean hasActions;
    public boolean aBoolean779;
    public static Cache mruNodes2 = new Cache(30);
    public int anInt781;
    private static ObjectDef[] cache;
    private int anInt783;
    private int[] modifiedModelColors;
    public static Cache mruNodes1 = new Cache(500);
    public String actions[];

}
