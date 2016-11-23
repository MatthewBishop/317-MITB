package com.jagex.entity.model;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.cache.anim.FrameBase;
import com.jagex.cache.anim.Frame;
import com.jagex.draw.Texture;
import com.jagex.entity.Animable;
import com.jagex.io.Buffer;
import com.jagex.net.OnDemandFetcherParent;

public final class Model extends Animable {

//    public Class33 aClass33Array1425[];
 //   public int modelHeight = 1000;
    
    public static void nullLoader()
    {
        Model.aClass21Array1661 = null;
        Model.SINE = null;
        Model.COSINE = null;
    }

    public static void method459(int i, OnDemandFetcherParent onDemandFetcherParent)
    {
        Model.aClass21Array1661 = new Class21[i];
        Model.aOnDemandFetcherParent_1662 = onDemandFetcherParent;
    }

    public static void method460(byte abyte0[], int j)
    {
        if(abyte0 == null)
        {
            Class21 class21 = Model.aClass21Array1661[j] = new Class21();
            class21.anInt369 = 0;
            class21.anInt370 = 0;
            class21.anInt371 = 0;
            return;
        }
        Buffer buffer = new Buffer(abyte0);
        buffer.position = abyte0.length - 18;
        Class21 class21_1 = Model.aClass21Array1661[j] = new Class21();
        class21_1.aByteArray368 = abyte0;
        class21_1.anInt369 = buffer.readUShort();
        class21_1.anInt370 = buffer.readUShort();
        class21_1.anInt371 = buffer.readUByte();
        int k = buffer.readUByte();
        int l = buffer.readUByte();
        int i1 = buffer.readUByte();
        int j1 = buffer.readUByte();
        int k1 = buffer.readUByte();
        int l1 = buffer.readUShort();
        int i2 = buffer.readUShort();
        int j2 = buffer.readUShort();
        int k2 = buffer.readUShort();
        int l2 = 0;
        class21_1.anInt372 = l2;
        l2 += class21_1.anInt369;
        class21_1.anInt378 = l2;
        l2 += class21_1.anInt370;
        class21_1.anInt381 = l2;
        if(l == 255)
            l2 += class21_1.anInt370;
        else
            class21_1.anInt381 = -l - 1;
        class21_1.anInt383 = l2;
        if(j1 == 1)
            l2 += class21_1.anInt370;
        else
            class21_1.anInt383 = -1;
        class21_1.anInt380 = l2;
        if(k == 1)
            l2 += class21_1.anInt370;
        else
            class21_1.anInt380 = -1;
        class21_1.anInt376 = l2;
        if(k1 == 1)
            l2 += class21_1.anInt369;
        else
            class21_1.anInt376 = -1;
        class21_1.anInt382 = l2;
        if(i1 == 1)
            l2 += class21_1.anInt370;
        else
            class21_1.anInt382 = -1;
        class21_1.anInt377 = l2;
        l2 += k2;
        class21_1.anInt379 = l2;
        l2 += class21_1.anInt370 * 2;
        class21_1.anInt384 = l2;
        l2 += class21_1.anInt371 * 6;
        class21_1.anInt373 = l2;
        l2 += l1;
        class21_1.anInt374 = l2;
        l2 += i2;
        class21_1.anInt375 = l2;
        l2 += j2;
    }

    public static void method461(int j)
    {
        Model.aClass21Array1661[j] = null;
    }

    public static Model method462(int j)
    {
        if(Model.aClass21Array1661 == null)
            return null;
        Class21 class21 = Model.aClass21Array1661[j];
        if(class21 == null)
        {
            Model.aOnDemandFetcherParent_1662.method548(j);
            return null;
        } else
        {
            return new Model(j);
        }
    }

    public static boolean method463(int i)
    {
        if(Model.aClass21Array1661 == null)
            return false;
        Class21 class21 = Model.aClass21Array1661[i];
        if(class21 == null)
        {
            Model.aOnDemandFetcherParent_1662.method548(i);
            return false;
        } else
        {
            return true;
        }
    }

    private Model()
    {
        this.aBoolean1659 = false;
    }

    private Model(int i)
    {
        this.aBoolean1659 = false;
        Class21 class21 = Model.aClass21Array1661[i];
        this.vertexCount = class21.anInt369;
        this.anInt1630 = class21.anInt370;
        this.anInt1642 = class21.anInt371;
        this.anIntArray1627 = new int[this.vertexCount];
        this.anIntArray1628 = new int[this.vertexCount];
        this.anIntArray1629 = new int[this.vertexCount];
        this.anIntArray1631 = new int[this.anInt1630];
        this.anIntArray1632 = new int[this.anInt1630];
        this.anIntArray1633 = new int[this.anInt1630];
        this.anIntArray1643 = new int[this.anInt1642];
        this.anIntArray1644 = new int[this.anInt1642];
        this.anIntArray1645 = new int[this.anInt1642];
        if(class21.anInt376 >= 0)
            this.anIntArray1655 = new int[this.vertexCount];
        if(class21.anInt380 >= 0)
            this.anIntArray1637 = new int[this.anInt1630];
        if(class21.anInt381 >= 0)
            this.anIntArray1638 = new int[this.anInt1630];
        else
            this.anInt1641 = -class21.anInt381 - 1;
        if(class21.anInt382 >= 0)
            this.anIntArray1639 = new int[this.anInt1630];
        if(class21.anInt383 >= 0)
            this.anIntArray1656 = new int[this.anInt1630];
        this.anIntArray1640 = new int[this.anInt1630];
        Buffer buffer = new Buffer(class21.aByteArray368);
        buffer.position = class21.anInt372;
        Buffer buffer_1 = new Buffer(class21.aByteArray368);
        buffer_1.position = class21.anInt373;
        Buffer buffer_2 = new Buffer(class21.aByteArray368);
        buffer_2.position = class21.anInt374;
        Buffer buffer_3 = new Buffer(class21.aByteArray368);
        buffer_3.position = class21.anInt375;
        Buffer buffer_4 = new Buffer(class21.aByteArray368);
        buffer_4.position = class21.anInt376;
        int k = 0;
        int l = 0;
        int i1 = 0;
        for(int j1 = 0; j1 < this.vertexCount; j1++)
        {
            int k1 = buffer.readUByte();
            int i2 = 0;
            if((k1 & 1) != 0)
                i2 = buffer_1.readSmart();
            int k2 = 0;
            if((k1 & 2) != 0)
                k2 = buffer_2.readSmart();
            int i3 = 0;
            if((k1 & 4) != 0)
                i3 = buffer_3.readSmart();
            this.anIntArray1627[j1] = k + i2;
            this.anIntArray1628[j1] = l + k2;
            this.anIntArray1629[j1] = i1 + i3;
            k = this.anIntArray1627[j1];
            l = this.anIntArray1628[j1];
            i1 = this.anIntArray1629[j1];
            if(this.anIntArray1655 != null)
                this.anIntArray1655[j1] = buffer_4.readUByte();
        }

        buffer.position = class21.anInt379;
        buffer_1.position = class21.anInt380;
        buffer_2.position = class21.anInt381;
        buffer_3.position = class21.anInt382;
        buffer_4.position = class21.anInt383;
        for(int l1 = 0; l1 < this.anInt1630; l1++)
        {
            this.anIntArray1640[l1] = buffer.readUShort();
            if(this.anIntArray1637 != null)
                this.anIntArray1637[l1] = buffer_1.readUByte();
            if(this.anIntArray1638 != null)
                this.anIntArray1638[l1] = buffer_2.readUByte();
            if(this.anIntArray1639 != null)
                this.anIntArray1639[l1] = buffer_3.readUByte();
            if(this.anIntArray1656 != null)
                this.anIntArray1656[l1] = buffer_4.readUByte();
        }

        buffer.position = class21.anInt377;
        buffer_1.position = class21.anInt378;
        int j2 = 0;
        int l2 = 0;
        int j3 = 0;
        int k3 = 0;
        for(int l3 = 0; l3 < this.anInt1630; l3++)
        {
            int i4 = buffer_1.readUByte();
            if(i4 == 1)
            {
                j2 = buffer.readSmart() + k3;
                k3 = j2;
                l2 = buffer.readSmart() + k3;
                k3 = l2;
                j3 = buffer.readSmart() + k3;
                k3 = j3;
                this.anIntArray1631[l3] = j2;
                this.anIntArray1632[l3] = l2;
                this.anIntArray1633[l3] = j3;
            }
            if(i4 == 2)
            {
                j2 = j2;
                l2 = j3;
                j3 = buffer.readSmart() + k3;
                k3 = j3;
                this.anIntArray1631[l3] = j2;
                this.anIntArray1632[l3] = l2;
                this.anIntArray1633[l3] = j3;
            }
            if(i4 == 3)
            {
                j2 = j3;
                l2 = l2;
                j3 = buffer.readSmart() + k3;
                k3 = j3;
                this.anIntArray1631[l3] = j2;
                this.anIntArray1632[l3] = l2;
                this.anIntArray1633[l3] = j3;
            }
            if(i4 == 4)
            {
                int k4 = j2;
                j2 = l2;
                l2 = k4;
                j3 = buffer.readSmart() + k3;
                k3 = j3;
                this.anIntArray1631[l3] = j2;
                this.anIntArray1632[l3] = l2;
                this.anIntArray1633[l3] = j3;
            }
        }

        buffer.position = class21.anInt384;
        for(int j4 = 0; j4 < this.anInt1642; j4++)
        {
            this.anIntArray1643[j4] = buffer.readUShort();
            this.anIntArray1644[j4] = buffer.readUShort();
            this.anIntArray1645[j4] = buffer.readUShort();
        }

    }

    public Model(int i, Model aclass30_sub2_sub4_sub6s[])
    {
        this.aBoolean1659 = false;
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        this.vertexCount = 0;
        this.anInt1630 = 0;
        this.anInt1642 = 0;
        this.anInt1641 = -1;
        for(int k = 0; k < i; k++)
        {
            Model model = aclass30_sub2_sub4_sub6s[k];
            if(model != null)
            {
                this.vertexCount += model.vertexCount;
                this.anInt1630 += model.anInt1630;
                this.anInt1642 += model.anInt1642;
                flag |= model.anIntArray1637 != null;
                if(model.anIntArray1638 != null)
                {
                    flag1 = true;
                } else
                {
                    if(this.anInt1641 == -1)
                        this.anInt1641 = model.anInt1641;
                    if(this.anInt1641 != model.anInt1641)
                        flag1 = true;
                }
                flag2 |= model.anIntArray1639 != null;
                flag3 |= model.anIntArray1656 != null;
            }
        }

        this.anIntArray1627 = new int[this.vertexCount];
        this.anIntArray1628 = new int[this.vertexCount];
        this.anIntArray1629 = new int[this.vertexCount];
        this.anIntArray1655 = new int[this.vertexCount];
        this.anIntArray1631 = new int[this.anInt1630];
        this.anIntArray1632 = new int[this.anInt1630];
        this.anIntArray1633 = new int[this.anInt1630];
        this.anIntArray1643 = new int[this.anInt1642];
        this.anIntArray1644 = new int[this.anInt1642];
        this.anIntArray1645 = new int[this.anInt1642];
        if(flag)
            this.anIntArray1637 = new int[this.anInt1630];
        if(flag1)
            this.anIntArray1638 = new int[this.anInt1630];
        if(flag2)
            this.anIntArray1639 = new int[this.anInt1630];
        if(flag3)
            this.anIntArray1656 = new int[this.anInt1630];
        this.anIntArray1640 = new int[this.anInt1630];
        this.vertexCount = 0;
        this.anInt1630 = 0;
        this.anInt1642 = 0;
        int l = 0;
        for(int i1 = 0; i1 < i; i1++)
        {
            Model model_1 = aclass30_sub2_sub4_sub6s[i1];
            if(model_1 != null)
            {
                for(int j1 = 0; j1 < model_1.anInt1630; j1++)
                {
                    if(flag)
                        if(model_1.anIntArray1637 == null)
                        {
                            this.anIntArray1637[this.anInt1630] = 0;
                        } else
                        {
                            int k1 = model_1.anIntArray1637[j1];
                            if((k1 & 2) == 2)
                                k1 += l << 2;
                            this.anIntArray1637[this.anInt1630] = k1;
                        }
                    if(flag1)
                        if(model_1.anIntArray1638 == null)
                            this.anIntArray1638[this.anInt1630] = model_1.anInt1641;
                        else
                            this.anIntArray1638[this.anInt1630] = model_1.anIntArray1638[j1];
                    if(flag2)
                        if(model_1.anIntArray1639 == null)
                            this.anIntArray1639[this.anInt1630] = 0;
                        else
                            this.anIntArray1639[this.anInt1630] = model_1.anIntArray1639[j1];
                    if(flag3 && model_1.anIntArray1656 != null)
                        this.anIntArray1656[this.anInt1630] = model_1.anIntArray1656[j1];
                    this.anIntArray1640[this.anInt1630] = model_1.anIntArray1640[j1];
                    this.anIntArray1631[this.anInt1630] = this.method465(model_1, model_1.anIntArray1631[j1]);
                    this.anIntArray1632[this.anInt1630] = this.method465(model_1, model_1.anIntArray1632[j1]);
                    this.anIntArray1633[this.anInt1630] = this.method465(model_1, model_1.anIntArray1633[j1]);
                    this.anInt1630++;
                }

                for(int l1 = 0; l1 < model_1.anInt1642; l1++)
                {
                    this.anIntArray1643[this.anInt1642] = this.method465(model_1, model_1.anIntArray1643[l1]);
                    this.anIntArray1644[this.anInt1642] = this.method465(model_1, model_1.anIntArray1644[l1]);
                    this.anIntArray1645[this.anInt1642] = this.method465(model_1, model_1.anIntArray1645[l1]);
                    this.anInt1642++;
                }

                l += model_1.anInt1642;
            }
        }

    }

    public Model(Model aclass30_sub2_sub4_sub6s[])
    {
        int i = 2;//was parameter
        this.aBoolean1659 = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;
        this.vertexCount = 0;
        this.anInt1630 = 0;
        this.anInt1642 = 0;
        this.anInt1641 = -1;
        for(int k = 0; k < i; k++)
        {
            Model model = aclass30_sub2_sub4_sub6s[k];
            if(model != null)
            {
                this.vertexCount += model.vertexCount;
                this.anInt1630 += model.anInt1630;
                this.anInt1642 += model.anInt1642;
                flag1 |= model.anIntArray1637 != null;
                if(model.anIntArray1638 != null)
                {
                    flag2 = true;
                } else
                {
                    if(this.anInt1641 == -1)
                        this.anInt1641 = model.anInt1641;
                    if(this.anInt1641 != model.anInt1641)
                        flag2 = true;
                }
                flag3 |= model.anIntArray1639 != null;
                flag4 |= model.anIntArray1640 != null;
            }
        }

        this.anIntArray1627 = new int[this.vertexCount];
        this.anIntArray1628 = new int[this.vertexCount];
        this.anIntArray1629 = new int[this.vertexCount];
        this.anIntArray1631 = new int[this.anInt1630];
        this.anIntArray1632 = new int[this.anInt1630];
        this.anIntArray1633 = new int[this.anInt1630];
        this.anIntArray1634 = new int[this.anInt1630];
        this.anIntArray1635 = new int[this.anInt1630];
        this.anIntArray1636 = new int[this.anInt1630];
        this.anIntArray1643 = new int[this.anInt1642];
        this.anIntArray1644 = new int[this.anInt1642];
        this.anIntArray1645 = new int[this.anInt1642];
        if(flag1)
            this.anIntArray1637 = new int[this.anInt1630];
        if(flag2)
            this.anIntArray1638 = new int[this.anInt1630];
        if(flag3)
            this.anIntArray1639 = new int[this.anInt1630];
        if(flag4)
            this.anIntArray1640 = new int[this.anInt1630];
        this.vertexCount = 0;
        this.anInt1630 = 0;
        this.anInt1642 = 0;
        int i1 = 0;
        for(int j1 = 0; j1 < i; j1++)
        {
            Model model_1 = aclass30_sub2_sub4_sub6s[j1];
            if(model_1 != null)
            {
                int k1 = this.vertexCount;
                for(int l1 = 0; l1 < model_1.vertexCount; l1++)
                {
                    this.anIntArray1627[this.vertexCount] = model_1.anIntArray1627[l1];
                    this.anIntArray1628[this.vertexCount] = model_1.anIntArray1628[l1];
                    this.anIntArray1629[this.vertexCount] = model_1.anIntArray1629[l1];
                    this.vertexCount++;
                }

                for(int i2 = 0; i2 < model_1.anInt1630; i2++)
                {
                    this.anIntArray1631[this.anInt1630] = model_1.anIntArray1631[i2] + k1;
                    this.anIntArray1632[this.anInt1630] = model_1.anIntArray1632[i2] + k1;
                    this.anIntArray1633[this.anInt1630] = model_1.anIntArray1633[i2] + k1;
                    this.anIntArray1634[this.anInt1630] = model_1.anIntArray1634[i2];
                    this.anIntArray1635[this.anInt1630] = model_1.anIntArray1635[i2];
                    this.anIntArray1636[this.anInt1630] = model_1.anIntArray1636[i2];
                    if(flag1)
                        if(model_1.anIntArray1637 == null)
                        {
                            this.anIntArray1637[this.anInt1630] = 0;
                        } else
                        {
                            int j2 = model_1.anIntArray1637[i2];
                            if((j2 & 2) == 2)
                                j2 += i1 << 2;
                            this.anIntArray1637[this.anInt1630] = j2;
                        }
                    if(flag2)
                        if(model_1.anIntArray1638 == null)
                            this.anIntArray1638[this.anInt1630] = model_1.anInt1641;
                        else
                            this.anIntArray1638[this.anInt1630] = model_1.anIntArray1638[i2];
                    if(flag3)
                        if(model_1.anIntArray1639 == null)
                            this.anIntArray1639[this.anInt1630] = 0;
                        else
                            this.anIntArray1639[this.anInt1630] = model_1.anIntArray1639[i2];
                    if(flag4 && model_1.anIntArray1640 != null)
                        this.anIntArray1640[this.anInt1630] = model_1.anIntArray1640[i2];
                    this.anInt1630++;
                }

                for(int k2 = 0; k2 < model_1.anInt1642; k2++)
                {
                    this.anIntArray1643[this.anInt1642] = model_1.anIntArray1643[k2] + k1;
                    this.anIntArray1644[this.anInt1642] = model_1.anIntArray1644[k2] + k1;
                    this.anIntArray1645[this.anInt1642] = model_1.anIntArray1645[k2] + k1;
                    this.anInt1642++;
                }

                i1 += model_1.anInt1642;
            }
        }

        this.method466();
    }

    public Model(boolean flag, boolean flag1, boolean flag2, Model model)
    {
        this.aBoolean1659 = false;
        this.vertexCount = model.vertexCount;
        this.anInt1630 = model.anInt1630;
        this.anInt1642 = model.anInt1642;
        if(flag2)
        {
            this.anIntArray1627 = model.anIntArray1627;
            this.anIntArray1628 = model.anIntArray1628;
            this.anIntArray1629 = model.anIntArray1629;
        } else
        {
            this.anIntArray1627 = new int[this.vertexCount];
            this.anIntArray1628 = new int[this.vertexCount];
            this.anIntArray1629 = new int[this.vertexCount];
            for(int j = 0; j < this.vertexCount; j++)
            {
                this.anIntArray1627[j] = model.anIntArray1627[j];
                this.anIntArray1628[j] = model.anIntArray1628[j];
                this.anIntArray1629[j] = model.anIntArray1629[j];
            }

        }
        if(flag)
        {
            this.anIntArray1640 = model.anIntArray1640;
        } else
        {
            this.anIntArray1640 = new int[this.anInt1630];
            System.arraycopy(model.anIntArray1640, 0, this.anIntArray1640, 0, this.anInt1630);

        }
        if(flag1)
        {
            this.anIntArray1639 = model.anIntArray1639;
        } else
        {
            this.anIntArray1639 = new int[this.anInt1630];
            if(model.anIntArray1639 == null)
            {
                for(int l = 0; l < this.anInt1630; l++)
                    this.anIntArray1639[l] = 0;

            } else
            {
                System.arraycopy(model.anIntArray1639, 0, this.anIntArray1639, 0, this.anInt1630);

            }
        }
        this.anIntArray1655 = model.anIntArray1655;
        this.anIntArray1656 = model.anIntArray1656;
        this.anIntArray1637 = model.anIntArray1637;
        this.anIntArray1631 = model.anIntArray1631;
        this.anIntArray1632 = model.anIntArray1632;
        this.anIntArray1633 = model.anIntArray1633;
        this.anIntArray1638 = model.anIntArray1638;
        this.anInt1641 = model.anInt1641;
        this.anIntArray1643 = model.anIntArray1643;
        this.anIntArray1644 = model.anIntArray1644;
        this.anIntArray1645 = model.anIntArray1645;
    }

    public Model(boolean flag, boolean flag1, Model model)
    {
        this.aBoolean1659 = false;
        this.vertexCount = model.vertexCount;
        this.anInt1630 = model.anInt1630;
        this.anInt1642 = model.anInt1642;
        if(flag)
        {
            this.anIntArray1628 = new int[this.vertexCount];
            System.arraycopy(model.anIntArray1628, 0, this.anIntArray1628, 0, this.vertexCount);

        } else
        {
            this.anIntArray1628 = model.anIntArray1628;
        }
        if(flag1)
        {
            this.anIntArray1634 = new int[this.anInt1630];
            this.anIntArray1635 = new int[this.anInt1630];
            this.anIntArray1636 = new int[this.anInt1630];
            for(int k = 0; k < this.anInt1630; k++)
            {
                this.anIntArray1634[k] = model.anIntArray1634[k];
                this.anIntArray1635[k] = model.anIntArray1635[k];
                this.anIntArray1636[k] = model.anIntArray1636[k];
            }

            this.anIntArray1637 = new int[this.anInt1630];
            if(model.anIntArray1637 == null)
            {
                for(int l = 0; l < this.anInt1630; l++)
                    this.anIntArray1637[l] = 0;

            } else
            {
                System.arraycopy(model.anIntArray1637, 0, this.anIntArray1637, 0, this.anInt1630);

            }
            this.aClass33Array1425 = new Class33[this.vertexCount];
            for(int j1 = 0; j1 < this.vertexCount; j1++)
            {
                Class33 class33 = this.aClass33Array1425[j1] = new Class33();
                Class33 class33_1 = model.aClass33Array1425[j1];
                class33.anInt602 = class33_1.anInt602;
                class33.anInt603 = class33_1.anInt603;
                class33.anInt604 = class33_1.anInt604;
                class33.anInt605 = class33_1.anInt605;
            }

            this.aClass33Array1660 = model.aClass33Array1660;
        } else
        {
            this.anIntArray1634 = model.anIntArray1634;
            this.anIntArray1635 = model.anIntArray1635;
            this.anIntArray1636 = model.anIntArray1636;
            this.anIntArray1637 = model.anIntArray1637;
        }
        this.anIntArray1627 = model.anIntArray1627;
        this.anIntArray1629 = model.anIntArray1629;
        this.anIntArray1640 = model.anIntArray1640;
        this.anIntArray1639 = model.anIntArray1639;
        this.anIntArray1638 = model.anIntArray1638;
        this.anInt1641 = model.anInt1641;
        this.anIntArray1631 = model.anIntArray1631;
        this.anIntArray1632 = model.anIntArray1632;
        this.anIntArray1633 = model.anIntArray1633;
        this.anIntArray1643 = model.anIntArray1643;
        this.anIntArray1644 = model.anIntArray1644;
        this.anIntArray1645 = model.anIntArray1645;
        this.modelHeight = model.modelHeight;
        this.anInt1651 = model.anInt1651; 
        this.anInt1650 = model.anInt1650;
        this.anInt1653 = model.anInt1653;
        this.anInt1652 = model.anInt1652;
        this.anInt1646 = model.anInt1646;
        this.anInt1648 = model.anInt1648;
        this.anInt1649 = model.anInt1649;
        this.anInt1647 = model.anInt1647;
    }

    public void method464(Model model, boolean flag)
    {
        this.vertexCount = model.vertexCount;
        this.anInt1630 = model.anInt1630;
        this.anInt1642 = model.anInt1642;
        if(Model.anIntArray1622.length < this.vertexCount)
        {
            Model.anIntArray1622 = new int[this.vertexCount + 100];
            Model.anIntArray1623 = new int[this.vertexCount + 100];
            Model.anIntArray1624 = new int[this.vertexCount + 100];
        }
        this.anIntArray1627 = Model.anIntArray1622;
        this.anIntArray1628 = Model.anIntArray1623;
        this.anIntArray1629 = Model.anIntArray1624;
        for(int k = 0; k < this.vertexCount; k++)
        {
            this.anIntArray1627[k] = model.anIntArray1627[k];
            this.anIntArray1628[k] = model.anIntArray1628[k];
            this.anIntArray1629[k] = model.anIntArray1629[k];
        }

        if(flag)
        {
            this.anIntArray1639 = model.anIntArray1639;
        } else
        {
            if(Model.anIntArray1625.length < this.anInt1630)
                Model.anIntArray1625 = new int[this.anInt1630 + 100];
            this.anIntArray1639 = Model.anIntArray1625;
            if(model.anIntArray1639 == null)
            {
                for(int l = 0; l < this.anInt1630; l++)
                    this.anIntArray1639[l] = 0;

            } else
            {
                System.arraycopy(model.anIntArray1639, 0, this.anIntArray1639, 0, this.anInt1630);

            }
        }
        this.anIntArray1637 = model.anIntArray1637;
        this.anIntArray1640 = model.anIntArray1640;
        this.anIntArray1638 = model.anIntArray1638;
        this.anInt1641 = model.anInt1641;
        this.anIntArrayArray1658 = model.anIntArrayArray1658;
        this.anIntArrayArray1657 = model.anIntArrayArray1657;
        this.anIntArray1631 = model.anIntArray1631;
        this.anIntArray1632 = model.anIntArray1632;
        this.anIntArray1633 = model.anIntArray1633;
        this.anIntArray1634 = model.anIntArray1634;
        this.anIntArray1635 = model.anIntArray1635;
        this.anIntArray1636 = model.anIntArray1636;
        this.anIntArray1643 = model.anIntArray1643;
        this.anIntArray1644 = model.anIntArray1644;
        this.anIntArray1645 = model.anIntArray1645;
    }

    private int method465(Model model, int i)
    {
        int j = -1;
        int k = model.anIntArray1627[i];
        int l = model.anIntArray1628[i];
        int i1 = model.anIntArray1629[i];
        for(int j1 = 0; j1 < this.vertexCount; j1++)
        {
            if(k != this.anIntArray1627[j1] || l != this.anIntArray1628[j1] || i1 != this.anIntArray1629[j1])
                continue;
            j = j1;
            break;
        }

        if(j == -1)
        {
            this.anIntArray1627[this.vertexCount] = k;
            this.anIntArray1628[this.vertexCount] = l;
            this.anIntArray1629[this.vertexCount] = i1;
            if(model.anIntArray1655 != null)
                this.anIntArray1655[this.vertexCount] = model.anIntArray1655[i];
            j = this.vertexCount++;
        }
        return j;
    }

    public void method466()
    {
        this.modelHeight = 0;
        this.anInt1650 = 0;
        this.anInt1651 = 0;
        for(int i = 0; i < this.vertexCount; i++)
        {
            int j = this.anIntArray1627[i];
            int k = this.anIntArray1628[i];
            int l = this.anIntArray1629[i];
            if(-k > this.modelHeight)
                this.modelHeight = -k;
            if(k > this.anInt1651)
                this.anInt1651 = k;
            int i1 = j * j + l * l;
            if(i1 > this.anInt1650)
                this.anInt1650 = i1;
        }
        this.anInt1650 = (int)(Math.sqrt(this.anInt1650) + 0.98999999999999999D);
        this.anInt1653 = (int)(Math.sqrt(this.anInt1650 * this.anInt1650 + this.modelHeight * this.modelHeight) + 0.98999999999999999D);
        this.anInt1652 = this.anInt1653 + (int)(Math.sqrt(this.anInt1650 * this.anInt1650 + this.anInt1651 * this.anInt1651) + 0.98999999999999999D);
    }

    public void method467()
    {
        this.modelHeight = 0;
        this.anInt1651 = 0;
        for(int i = 0; i < this.vertexCount; i++)
        {
            int j = this.anIntArray1628[i];
            if(-j > this.modelHeight)
                this.modelHeight = -j;
            if(j > this.anInt1651)
                this.anInt1651 = j;
        }

        this.anInt1653 = (int)(Math.sqrt(this.anInt1650 * this.anInt1650 + this.modelHeight * this.modelHeight) + 0.98999999999999999D);
        this.anInt1652 = this.anInt1653 + (int)(Math.sqrt(this.anInt1650 * this.anInt1650 + this.anInt1651 * this.anInt1651) + 0.98999999999999999D);
    }

    private void method468()
    {
        this.modelHeight = 0;
        this.anInt1650 = 0;
        this.anInt1651 = 0;
        this.anInt1646 = 0xf423f;
        this.anInt1647 = 0xfff0bdc1;
        this.anInt1648 = 0xfffe7961;
        this.anInt1649 = 0x1869f;
        for(int j = 0; j < this.vertexCount; j++)
        {
            int k = this.anIntArray1627[j];
            int l = this.anIntArray1628[j];
            int i1 = this.anIntArray1629[j];
            if(k < this.anInt1646)
                this.anInt1646 = k;
            if(k > this.anInt1647)
                this.anInt1647 = k;
            if(i1 < this.anInt1649)
                this.anInt1649 = i1;
            if(i1 > this.anInt1648)
                this.anInt1648 = i1;
            if(-l > this.modelHeight)
                this.modelHeight = -l;
            if(l > this.anInt1651)
                this.anInt1651 = l;
            int j1 = k * k + i1 * i1;
            if(j1 > this.anInt1650)
                this.anInt1650 = j1;
        }

        this.anInt1650 = (int)Math.sqrt(this.anInt1650);
        this.anInt1653 = (int)Math.sqrt(this.anInt1650 * this.anInt1650 + this.modelHeight * this.modelHeight);
            this.anInt1652 = this.anInt1653 + (int)Math.sqrt(this.anInt1650 * this.anInt1650 + this.anInt1651 * this.anInt1651);
    }

    public void method469()
    {
        if(this.anIntArray1655 != null)
        {
            int ai[] = new int[256];
            int j = 0;
            for(int l = 0; l < this.vertexCount; l++)
            {
                int j1 = this.anIntArray1655[l];
                ai[j1]++;
                if(j1 > j)
                    j = j1;
            }

            this.anIntArrayArray1657 = new int[j + 1][];
            for(int k1 = 0; k1 <= j; k1++)
            {
                this.anIntArrayArray1657[k1] = new int[ai[k1]];
                ai[k1] = 0;
            }

            for(int j2 = 0; j2 < this.vertexCount; j2++)
            {
                int l2 = this.anIntArray1655[j2];
                this.anIntArrayArray1657[l2][ai[l2]++] = j2;
            }

            this.anIntArray1655 = null;
        }
        if(this.anIntArray1656 != null)
        {
            int ai1[] = new int[256];
            int k = 0;
            for(int i1 = 0; i1 < this.anInt1630; i1++)
            {
                int l1 = this.anIntArray1656[i1];
                ai1[l1]++;
                if(l1 > k)
                    k = l1;
            }

            this.anIntArrayArray1658 = new int[k + 1][];
            for(int i2 = 0; i2 <= k; i2++)
            {
                this.anIntArrayArray1658[i2] = new int[ai1[i2]];
                ai1[i2] = 0;
            }

            for(int k2 = 0; k2 < this.anInt1630; k2++)
            {
                int i3 = this.anIntArray1656[k2];
                this.anIntArrayArray1658[i3][ai1[i3]++] = k2;
            }

            this.anIntArray1656 = null;
        }
    }

    public void method470(int i)
    {
        if(this.anIntArrayArray1657 == null)
            return;
        if(i == -1)
            return;
        Frame frame = Frame.lookup(i);
        if(frame == null)
            return;
        FrameBase frameBase = frame.aClass18_637;
        Model.anInt1681 = 0;
        Model.anInt1682 = 0;
        Model.anInt1683 = 0;
        for(int k = 0; k < frame.transformationCount; k++)
        {
            int l = frame.transformationIndices[k];
            this.method472(frameBase.transformationType[l], frameBase.labels[l], frame.transformX[k], frame.transformY[k], frame.transformZ[k]);
        }

    }

    public void method471(int ai[], int j, int k)
    {
        if(k == -1)
            return;
        if(ai == null || j == -1)
        {
            this.method470(k);
            return;
        }
        Frame frame = Frame.lookup(k);
        if(frame == null)
            return;
        Frame class36_1 = Frame.lookup(j);
        if(class36_1 == null)
        {
            this.method470(k);
            return;
        }
        FrameBase frameBase = frame.aClass18_637;
        Model.anInt1681 = 0;
        Model.anInt1682 = 0;
        Model.anInt1683 = 0;
        int l = 0;
        int i1 = ai[l++];
        for(int j1 = 0; j1 < frame.transformationCount; j1++)
        {
            int k1;
            for(k1 = frame.transformationIndices[j1]; k1 > i1; i1 = ai[l++]);
            if(k1 != i1 || frameBase.transformationType[k1] == 0)
                this.method472(frameBase.transformationType[k1], frameBase.labels[k1], frame.transformX[j1], frame.transformY[j1], frame.transformZ[j1]);
        }

        Model.anInt1681 = 0;
        Model.anInt1682 = 0;
        Model.anInt1683 = 0;
        l = 0;
        i1 = ai[l++];
        for(int l1 = 0; l1 < class36_1.transformationCount; l1++)
        {
            int i2;
            for(i2 = class36_1.transformationIndices[l1]; i2 > i1; i1 = ai[l++]);
            if(i2 == i1 || frameBase.transformationType[i2] == 0)
                this.method472(frameBase.transformationType[i2], frameBase.labels[i2], class36_1.transformX[l1], class36_1.transformY[l1], class36_1.transformZ[l1]);
        }

    }

    private void method472(int i, int ai[], int j, int k, int l)
    {
        int i1 = ai.length;
        if(i == 0)
        {
            int j1 = 0;
            Model.anInt1681 = 0;
            Model.anInt1682 = 0;
            Model.anInt1683 = 0;
            for(int k2 = 0; k2 < i1; k2++)
            {
                int l3 = ai[k2];
                if(l3 < this.anIntArrayArray1657.length)
                {
                    int ai5[] = this.anIntArrayArray1657[l3];
                    for(int i5 = 0; i5 < ai5.length; i5++)
                    {
                        int j6 = ai5[i5];
                        Model.anInt1681 += this.anIntArray1627[j6];
                        Model.anInt1682 += this.anIntArray1628[j6];
                        Model.anInt1683 += this.anIntArray1629[j6];
                        j1++;
                    }

                }
            }

            if(j1 > 0)
            {
                Model.anInt1681 = Model.anInt1681 / j1 + j;
                Model.anInt1682 = Model.anInt1682 / j1 + k;
                Model.anInt1683 = Model.anInt1683 / j1 + l;
                return;
            } else
            {
                Model.anInt1681 = j;
                Model.anInt1682 = k;
                Model.anInt1683 = l;
                return;
            }
        }
        if(i == 1)
        {
            for(int k1 = 0; k1 < i1; k1++)
            {
                int l2 = ai[k1];
                if(l2 < this.anIntArrayArray1657.length)
                {
                    int ai1[] = this.anIntArrayArray1657[l2];
                    for(int i4 = 0; i4 < ai1.length; i4++)
                    {
                        int j5 = ai1[i4];
                        this.anIntArray1627[j5] += j;
                        this.anIntArray1628[j5] += k;
                        this.anIntArray1629[j5] += l;
                    }

                }
            }

            return;
        }
        if(i == 2)
        {
            for(int l1 = 0; l1 < i1; l1++)
            {
                int i3 = ai[l1];
                if(i3 < this.anIntArrayArray1657.length)
                {
                    int ai2[] = this.anIntArrayArray1657[i3];
                    for(int j4 = 0; j4 < ai2.length; j4++)
                    {
                        int k5 = ai2[j4];
                        this.anIntArray1627[k5] -= Model.anInt1681;
                        this.anIntArray1628[k5] -= Model.anInt1682;
                        this.anIntArray1629[k5] -= Model.anInt1683;
                        int k6 = (j & 0xff) * 8;
                        int l6 = (k & 0xff) * 8;
                        int i7 = (l & 0xff) * 8;
                        if(i7 != 0)
                        {
                            int j7 = Model.SINE[i7];
                            int i8 = Model.COSINE[i7];
                            int l8 = this.anIntArray1628[k5] * j7 + this.anIntArray1627[k5] * i8 >> 16;
                            this.anIntArray1628[k5] = this.anIntArray1628[k5] * i8 - this.anIntArray1627[k5] * j7 >> 16;
                            this.anIntArray1627[k5] = l8;
                        }
                        if(k6 != 0)
                        {
                            int k7 = Model.SINE[k6];
                            int j8 = Model.COSINE[k6];
                            int i9 = this.anIntArray1628[k5] * j8 - this.anIntArray1629[k5] * k7 >> 16;
                            this.anIntArray1629[k5] = this.anIntArray1628[k5] * k7 + this.anIntArray1629[k5] * j8 >> 16;
                            this.anIntArray1628[k5] = i9;
                        }
                        if(l6 != 0)
                        {
                            int l7 = Model.SINE[l6];
                            int k8 = Model.COSINE[l6];
                            int j9 = this.anIntArray1629[k5] * l7 + this.anIntArray1627[k5] * k8 >> 16;
                            this.anIntArray1629[k5] = this.anIntArray1629[k5] * k8 - this.anIntArray1627[k5] * l7 >> 16;
                            this.anIntArray1627[k5] = j9;
                        }
                        this.anIntArray1627[k5] += Model.anInt1681;
                        this.anIntArray1628[k5] += Model.anInt1682;
                        this.anIntArray1629[k5] += Model.anInt1683;
                    }

                }
            }

            return;
        }
        if(i == 3)
        {
            for(int i2 = 0; i2 < i1; i2++)
            {
                int j3 = ai[i2];
                if(j3 < this.anIntArrayArray1657.length)
                {
                    int ai3[] = this.anIntArrayArray1657[j3];
                    for(int k4 = 0; k4 < ai3.length; k4++)
                    {
                        int l5 = ai3[k4];
                        this.anIntArray1627[l5] -= Model.anInt1681;
                        this.anIntArray1628[l5] -= Model.anInt1682;
                        this.anIntArray1629[l5] -= Model.anInt1683;
                        this.anIntArray1627[l5] = (this.anIntArray1627[l5] * j) / 128;
                        this.anIntArray1628[l5] = (this.anIntArray1628[l5] * k) / 128;
                        this.anIntArray1629[l5] = (this.anIntArray1629[l5] * l) / 128;
                        this.anIntArray1627[l5] += Model.anInt1681;
                        this.anIntArray1628[l5] += Model.anInt1682;
                        this.anIntArray1629[l5] += Model.anInt1683;
                    }

                }
            }

            return;
        }
        if(i == 5 && this.anIntArrayArray1658 != null && this.anIntArray1639 != null)
        {
            for(int j2 = 0; j2 < i1; j2++)
            {
                int k3 = ai[j2];
                if(k3 < this.anIntArrayArray1658.length)
                {
                    int ai4[] = this.anIntArrayArray1658[k3];
                    for(int l4 = 0; l4 < ai4.length; l4++)
                    {
                        int i6 = ai4[l4];
                        this.anIntArray1639[i6] += j * 8;
                        if(this.anIntArray1639[i6] < 0)
                            this.anIntArray1639[i6] = 0;
                        if(this.anIntArray1639[i6] > 255)
                            this.anIntArray1639[i6] = 255;
                    }

                }
            }

        }
    }

    public void method473()
    {
        for(int j = 0; j < this.vertexCount; j++)
        {
            int k = this.anIntArray1627[j];
            this.anIntArray1627[j] = this.anIntArray1629[j];
            this.anIntArray1629[j] = -k;
        }

    }

    public void method474(int i)
    {
        int k = Model.SINE[i];
        int l = Model.COSINE[i];
        for(int i1 = 0; i1 < this.vertexCount; i1++)
        {
            int j1 = this.anIntArray1628[i1] * l - this.anIntArray1629[i1] * k >> 16;
            this.anIntArray1629[i1] = this.anIntArray1628[i1] * k + this.anIntArray1629[i1] * l >> 16;
            this.anIntArray1628[i1] = j1;
        }
    }

    public void method475(int i, int j, int l)
    {
        for(int i1 = 0; i1 < this.vertexCount; i1++)
        {
            this.anIntArray1627[i1] += i;
            this.anIntArray1628[i1] += j;
            this.anIntArray1629[i1] += l;
        }

    }

    public void method476(int i, int j)
    {
        for(int k = 0; k < this.anInt1630; k++)
            if(this.anIntArray1640[k] == i)
                this.anIntArray1640[k] = j;

    }

    public void method477()
    {
        for(int j = 0; j < this.vertexCount; j++)
            this.anIntArray1629[j] = -this.anIntArray1629[j];

        for(int k = 0; k < this.anInt1630; k++)
        {
            int l = this.anIntArray1631[k];
            this.anIntArray1631[k] = this.anIntArray1633[k];
            this.anIntArray1633[k] = l;
        }
    }

    public void method478(int i, int j, int l)
    {
        for(int i1 = 0; i1 < this.vertexCount; i1++)
        {
            this.anIntArray1627[i1] = (this.anIntArray1627[i1] * i) / 128;
            this.anIntArray1628[i1] = (this.anIntArray1628[i1] * l) / 128;
            this.anIntArray1629[i1] = (this.anIntArray1629[i1] * j) / 128;
        }

    }

    public void method479(int i, int j, int k, int l, int i1, boolean flag)
    {
        int j1 = (int)Math.sqrt(k * k + l * l + i1 * i1);
        int k1 = j * j1 >> 8;
        if(this.anIntArray1634 == null)
        {
            this.anIntArray1634 = new int[this.anInt1630];
            this.anIntArray1635 = new int[this.anInt1630];
            this.anIntArray1636 = new int[this.anInt1630];
        }
        if(this.aClass33Array1425 == null)
        {
            this.aClass33Array1425 = new Class33[this.vertexCount];
            for(int l1 = 0; l1 < this.vertexCount; l1++)
                this.aClass33Array1425[l1] = new Class33();

        }
        for(int i2 = 0; i2 < this.anInt1630; i2++)
        {
            int j2 = this.anIntArray1631[i2];
            int l2 = this.anIntArray1632[i2];
            int i3 = this.anIntArray1633[i2];
            int j3 = this.anIntArray1627[l2] - this.anIntArray1627[j2];
            int k3 = this.anIntArray1628[l2] - this.anIntArray1628[j2];
            int l3 = this.anIntArray1629[l2] - this.anIntArray1629[j2];
            int i4 = this.anIntArray1627[i3] - this.anIntArray1627[j2];
            int j4 = this.anIntArray1628[i3] - this.anIntArray1628[j2];
            int k4 = this.anIntArray1629[i3] - this.anIntArray1629[j2];
            int l4 = k3 * k4 - j4 * l3;
            int i5 = l3 * i4 - k4 * j3;
            int j5;
            for(j5 = j3 * j4 - i4 * k3; l4 > 8192 || i5 > 8192 || j5 > 8192 || l4 < -8192 || i5 < -8192 || j5 < -8192; j5 >>= 1)
            {
                l4 >>= 1;
                i5 >>= 1;
            }

            int k5 = (int)Math.sqrt(l4 * l4 + i5 * i5 + j5 * j5);
            if(k5 <= 0)
                k5 = 1;
            l4 = (l4 * 256) / k5;
            i5 = (i5 * 256) / k5;
            j5 = (j5 * 256) / k5;
            if(this.anIntArray1637 == null || (this.anIntArray1637[i2] & 1) == 0)
            {
                Class33 class33_2 = this.aClass33Array1425[j2];
                class33_2.anInt602 += l4;
                class33_2.anInt603 += i5;
                class33_2.anInt604 += j5;
                class33_2.anInt605++;
                class33_2 = this.aClass33Array1425[l2];
                class33_2.anInt602 += l4;
                class33_2.anInt603 += i5;
                class33_2.anInt604 += j5;
                class33_2.anInt605++;
                class33_2 = this.aClass33Array1425[i3];
                class33_2.anInt602 += l4;
                class33_2.anInt603 += i5;
                class33_2.anInt604 += j5;
                class33_2.anInt605++;
            } else
            {
                int l5 = i + (k * l4 + l * i5 + i1 * j5) / (k1 + k1 / 2);
                this.anIntArray1634[i2] = Model.method481(this.anIntArray1640[i2], l5, this.anIntArray1637[i2]);
            }
        }

        if(flag)
        {
            this.method480(i, k1, k, l, i1);
        } else
        {
            this.aClass33Array1660 = new Class33[this.vertexCount];
            for(int k2 = 0; k2 < this.vertexCount; k2++)
            {
                Class33 class33 = this.aClass33Array1425[k2];
                Class33 class33_1 = this.aClass33Array1660[k2] = new Class33();
                class33_1.anInt602 = class33.anInt602;
                class33_1.anInt603 = class33.anInt603;
                class33_1.anInt604 = class33.anInt604;
                class33_1.anInt605 = class33.anInt605;
            }

        }
        if(flag)
        {
            this.method466();
        } else
        {
            this.method468();
        }
    }

    public void method480(int i, int j, int k, int l, int i1)
    {
        for(int j1 = 0; j1 < this.anInt1630; j1++)
        {
            int k1 = this.anIntArray1631[j1];
            int i2 = this.anIntArray1632[j1];
            int j2 = this.anIntArray1633[j1];
            if(this.anIntArray1637 == null)
            {
                int i3 = this.anIntArray1640[j1];
                Class33 class33 = this.aClass33Array1425[k1];
                int k2 = i + (k * class33.anInt602 + l * class33.anInt603 + i1 * class33.anInt604) / (j * class33.anInt605);
                this.anIntArray1634[j1] = Model.method481(i3, k2, 0);
                class33 = this.aClass33Array1425[i2];
                k2 = i + (k * class33.anInt602 + l * class33.anInt603 + i1 * class33.anInt604) / (j * class33.anInt605);
                this.anIntArray1635[j1] = Model.method481(i3, k2, 0);
                class33 = this.aClass33Array1425[j2];
                k2 = i + (k * class33.anInt602 + l * class33.anInt603 + i1 * class33.anInt604) / (j * class33.anInt605);
                this.anIntArray1636[j1] = Model.method481(i3, k2, 0);
            } else
            if((this.anIntArray1637[j1] & 1) == 0)
            {
                int j3 = this.anIntArray1640[j1];
                int k3 = this.anIntArray1637[j1];
                Class33 class33_1 = this.aClass33Array1425[k1];
                int l2 = i + (k * class33_1.anInt602 + l * class33_1.anInt603 + i1 * class33_1.anInt604) / (j * class33_1.anInt605);
                this.anIntArray1634[j1] = Model.method481(j3, l2, k3);
                class33_1 = this.aClass33Array1425[i2];
                l2 = i + (k * class33_1.anInt602 + l * class33_1.anInt603 + i1 * class33_1.anInt604) / (j * class33_1.anInt605);
                this.anIntArray1635[j1] = Model.method481(j3, l2, k3);
                class33_1 = this.aClass33Array1425[j2];
                l2 = i + (k * class33_1.anInt602 + l * class33_1.anInt603 + i1 * class33_1.anInt604) / (j * class33_1.anInt605);
                this.anIntArray1636[j1] = Model.method481(j3, l2, k3);
            }
        }

        this.aClass33Array1425 = null;
        this.aClass33Array1660 = null;
        this.anIntArray1655 = null;
        this.anIntArray1656 = null;
        if(this.anIntArray1637 != null)
        {
            for(int l1 = 0; l1 < this.anInt1630; l1++)
                if((this.anIntArray1637[l1] & 2) == 2)
                    return;

        }
        this.anIntArray1640 = null;
    }

    private static int method481(int i, int j, int k)
    {
        if((k & 2) == 2)
        {
            if(j < 0)
                j = 0;
            else
            if(j > 127)
                j = 127;
            j = 127 - j;
            return j;
        }
        j = j * (i & 0x7f) >> 7;
        if(j < 2)
            j = 2;
        else
        if(j > 126)
            j = 126;
        return (i & 0xff80) + j;
    }
    
    @Override
	public void method443(Animable model, int i, int j, int k, int l, int i1, int j1, int k1, 
            int l1, int i2)
    {
        ModelRenderer.method443((Model)model, i, j, k, l, i1, j1, k1, l1, i2);
    }

    public static final Model aModel_1621 = new Model();
    private static int[] anIntArray1622 = new int[2000];
    private static int[] anIntArray1623 = new int[2000];
    private static int[] anIntArray1624 = new int[2000];
    private static int[] anIntArray1625 = new int[2000];
    public int vertexCount;
    public int anIntArray1627[];
    public int anIntArray1628[];
    public int anIntArray1629[];
    public int anInt1630;
    public int anIntArray1631[];
    public int anIntArray1632[];
    public int anIntArray1633[];
    int[] anIntArray1634;
    int[] anIntArray1635;
    int[] anIntArray1636;
    public int anIntArray1637[];
    int[] anIntArray1638;
    int[] anIntArray1639;
    public int anIntArray1640[];
    private int anInt1641;
    int anInt1642;
    int[] anIntArray1643;
    int[] anIntArray1644;
    int[] anIntArray1645;
    public int anInt1646;
    public int anInt1647;
    public int anInt1648;
    public int anInt1649;
    public int anInt1650;
    public int anInt1651;
    int anInt1652;
    int anInt1653;
    public int anInt1654;
    private int[] anIntArray1655;
    private int[] anIntArray1656;
    public int anIntArrayArray1657[][];
    public int anIntArrayArray1658[][];
    public boolean aBoolean1659;
    public Class33 aClass33Array1660[];
    private static Class21[] aClass21Array1661;
    private static OnDemandFetcherParent aOnDemandFetcherParent_1662;

    private static int anInt1681;
    private static int anInt1682;
    private static int anInt1683;
    
    public static int SINE[];
    public static int COSINE[];


    static 
    {
        Model.SINE = Texture.anIntArray1470;
        Model.COSINE = Texture.anIntArray1471;
    }
}
