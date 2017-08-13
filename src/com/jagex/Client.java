package com.jagex;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import java.awt.*;
import java.io.*;
import java.net.*;
import com.jagex.cache.Index;
import com.jagex.cache.Archive;
import com.jagex.cache.anim.Animation;
import com.jagex.cache.anim.Frame;
import com.jagex.cache.anim.Graphic;
import com.jagex.cache.def.EntityDef;
import com.jagex.cache.def.Flo;
import com.jagex.cache.def.ItemDef;
import com.jagex.cache.def.ObjectDef;
import com.jagex.cache.graphics.IndexedImage;
import com.jagex.cache.graphics.RSInterface;
import com.jagex.cache.graphics.Sprite;
import com.jagex.cache.graphics.TextDrawingArea;
import com.jagex.cache.setting.VariableBits;
import com.jagex.cache.setting.VariableParameter;
import com.jagex.draw.DrawingArea;
import com.jagex.draw.RSImageProducer;
import com.jagex.draw.Texture;
import com.jagex.draw.render.IndexedImageRenderer;
import com.jagex.draw.render.SpriteRenderer;
import com.jagex.entity.Animable;
import com.jagex.entity.AnimableObject;
import com.jagex.entity.AnimableRenderer;
import com.jagex.entity.Projectile;
import com.jagex.entity.RenderableObject;
import com.jagex.entity.Entity;
import com.jagex.entity.Item;
import com.jagex.entity.NPC;
import com.jagex.entity.Player;
import com.jagex.entity.model.IDK;
import com.jagex.entity.model.Model;
import com.jagex.entity.model.ModelRenderer;
import com.jagex.io.BitBuffer;
import com.jagex.io.PacketStream;
import com.jagex.io.Buffer;
import com.jagex.link.Linkable;
import com.jagex.link.Deque;
import com.jagex.map.CollisionMap;
import com.jagex.map.Object1;
import com.jagex.map.Object2;
import com.jagex.map.Object3;
import com.jagex.map.GameObject;
import com.jagex.map.ObjectManager;
import com.jagex.map.Pathfinder;
import com.jagex.map.SpawnedObject;
import com.jagex.map.WorldController;
import com.jagex.net.ISAACRandomGen;
import com.jagex.net.OnDemandData;
import com.jagex.net.OnDemandFetcher;
import com.jagex.net.RSSocket;
import com.jagex.net.SizeConstants;
import com.jagex.sign.signlink;
import com.jagex.sound.Sounds;
import com.jagex.util.Censor;
import com.jagex.util.MouseDetection;
import com.jagex.util.Skills;
import com.jagex.util.TextClass;
import com.jagex.util.TextInput;
import com.jagex.window.RSApplet;
import com.jagex.window.RSBase;

public final class Client extends RSBase {

	@Override
	public void init() {
		Client.nodeID = Integer.parseInt(this.applet.getParameter("nodeid"));
		RSBase.portOff = Integer.parseInt(this.applet.getParameter("portoff"));
		String s = this.applet.getParameter("lowmem");
		if (s != null && s.equals("1"))
			Client.setLowMem();
		else
			Client.setHighMem();
		String s1 = this.applet.getParameter("free");
		Client.isMembers = !(s1 != null && s1.equals("1"));
		this.applet.initClientFrame(503, 765, true);
	}
	
	static RSImageProducer aRSImageProducer_1109;
	
	public RSApplet applet = new RSApplet(this);
	public static Sprite[] cacheSprite;
	public static int frameWidth = 765;
	public static int frameHeight = 503;
	public static int screenAreaWidth = 512;
	public static int screenAreaHeight = 334;
	
	public static int cameraZoom = 600;
	public static boolean showChatComponents = true;
	public static boolean showTabComponents = true;
	public static boolean changeTabArea = RSBase.frameMode == ScreenMode.FIXED ? false : true;
	public static boolean changeChatArea = RSBase.frameMode == ScreenMode.FIXED ? false : true;
	public static boolean transparentTabArea = false;

	public int drawCount;

	
	
    private void setBounds2() {
        Texture.method365(479, 96);
        this.anIntArray1180 = Texture.anIntArray1472;
        Texture.method365(190, 261);
        this.anIntArray1181 = Texture.anIntArray1472;
        Texture.method365(512, 334);
        this.anIntArray1182 = Texture.anIntArray1472;
        int ai[] = new int[9];
        for(int i8 = 0; i8 < 9; i8++)
        {
            int k8 = 128 + i8 * 32 + 15;
            int l8 = 600 + k8 * 3;
            int i9 = Texture.anIntArray1470[k8];
            ai[i8] = l8 * i9 >> 16;
        }

        WorldController.method310(500, 800, Client.screenAreaWidth, Client.screenAreaHeight, ai);
	}
    
	public void frameMode(ScreenMode screenMode) {
		if (RSBase.frameMode != screenMode) {
			RSBase.frameMode = screenMode;
			if (screenMode == ScreenMode.FIXED) {
				Client.frameWidth = 765;
				Client.frameHeight = 503;
				Client.cameraZoom = 600;
				Constants.VIEW_DISTANCE = 9;
				Client.changeChatArea = false;
				Client.changeTabArea = false;
			} else if (screenMode == ScreenMode.RESIZABLE) {
				Client.frameWidth = 766;
				Client.frameHeight = 529;	
				Client.cameraZoom = 850;
				Constants.VIEW_DISTANCE = 10;
			} else if (screenMode == ScreenMode.FULLSCREEN) {
				Client.cameraZoom = 600;
				Constants.VIEW_DISTANCE = 10;
				Client.frameWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
				Client.frameHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
			}
			this.rebuildFrameSize(screenMode, Client.frameWidth, Client.frameHeight);
			this.setBounds();
			System.out.println("ScreenMode: " + screenMode.toString());
		}
		Client.showChatComponents = screenMode == ScreenMode.FIXED ? true : Client.showChatComponents;
		Client.showTabComponents = screenMode == ScreenMode.FIXED ? true : Client.showTabComponents;
	}
	
	public void rebuildFrameSize(ScreenMode screenMode, int screenWidth, int screenHeight) {
		try {
			Client.screenAreaWidth = (screenMode == ScreenMode.FIXED) ? 512 : screenWidth;
			Client.screenAreaHeight = (screenMode == ScreenMode.FIXED) ? 334 : screenHeight;
			Client.frameWidth = screenWidth;
			Client.frameHeight = screenHeight;
			this.applet.refreshFrameSize(screenMode == ScreenMode.FULLSCREEN, screenWidth, screenHeight, screenMode == ScreenMode.RESIZABLE, screenMode != ScreenMode.FIXED);
			this.setBounds();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void refreshFrameSize(boolean loggedIn) {
		if (RSBase.frameMode == ScreenMode.RESIZABLE) {
			if (Client.frameWidth != (this.applet.appletClient() ? this.applet.getGameComponent().getWidth() : this.applet.gameFrame.getFrameWidth())) {
				Client.frameWidth = (this.applet.appletClient() ? this.applet.getGameComponent().getWidth() : this.applet.gameFrame.getFrameWidth());
				Client.screenAreaWidth = Client.frameWidth;
				if(loggedIn)
					this.setBounds();
			}
			if (Client.frameHeight != (this.applet.appletClient() ? this.applet.getGameComponent().getHeight() : this.applet.gameFrame.getFrameHeight())) {
				Client.frameHeight = (this.applet.appletClient() ? this.applet.getGameComponent().getHeight() : this.applet.gameFrame.getFrameHeight());
				Client.screenAreaHeight = Client.frameHeight;
				if(loggedIn)
					this.setBounds();
			}
		}
	}
	
	
	private void setBounds() {
		Texture.method365(RSBase.frameMode == ScreenMode.FIXED ? (this.aRSImageProducer_1166 != null ? this.aRSImageProducer_1166.canvasWidth : 519) : Client.frameWidth, RSBase.frameMode == ScreenMode.FIXED ? (this.aRSImageProducer_1166 != null ? this.aRSImageProducer_1166.canvasHeight : 165) : Client.frameHeight);
		this.anIntArray1180 = Texture.anIntArray1472;
		Texture.method365(RSBase.frameMode == ScreenMode.FIXED ? (this.aRSImageProducer_1163 != null ? this.aRSImageProducer_1163.canvasWidth : 249) : Client.frameWidth, RSBase.frameMode == ScreenMode.FIXED ? (this.aRSImageProducer_1163 != null ? this.aRSImageProducer_1163.canvasHeight : 335) : Client.frameHeight);
		this.anIntArray1181 = Texture.anIntArray1472;
		Texture.method365(Client.screenAreaWidth, Client.screenAreaHeight);
		this.anIntArray1182 = Texture.anIntArray1472;
		int ai[] = new int[9];
		for(int i8 = 0; i8 < 9; i8++) {
			int k8 = 128 + i8 * 32 + 15;
			int l8 = 600 + k8 * 3;
			int i9 = Texture.anIntArray1470[k8];
			ai[i8] = l8 * i9 >> 16;
		}
		if (RSBase.frameMode == ScreenMode.RESIZABLE && (Client.frameWidth >= 766) && (Client.frameWidth <= 1025) && (Client.frameHeight >= 504) && (Client.frameHeight <= 850)) {
			Constants.VIEW_DISTANCE = 9;
			Client.cameraZoom = 575;
		} else if (RSBase.frameMode == ScreenMode.FIXED) {
			Client.cameraZoom = 600;
		} else if (RSBase.frameMode == ScreenMode.RESIZABLE || RSBase.frameMode == ScreenMode.FULLSCREEN) {
			Constants.VIEW_DISTANCE = 10;
			Client.cameraZoom = 600;
		}
		WorldController.method310(500, 800, Client.screenAreaWidth, Client.screenAreaHeight, ai);
		if (this.loggedIn) {
			this.aRSImageProducer_1165 = new RSImageProducer(Client.screenAreaWidth, Client.screenAreaHeight);
		}
	}
	
	private Jaggrab jaggrab;
	
    private static String intToKOrMilLongName(int i)
    {
        String s = String.valueOf(i);
        for(int k = s.length() - 3; k > 0; k -= 3)
            s = s.substring(0, k) + "," + s.substring(k);
        if(s.length() > 8)
            s = "@gre@" + s.substring(0, s.length() - 8) + " million @whi@(" + s + ")";
        else
        if(s.length() > 4)
            s = "@cya@" + s.substring(0, s.length() - 4) + "K @whi@(" + s + ")";
        return " " + s;
    }

    private void stopMidi()
    {
        signlink.midifade = 0;
        signlink.midi = "stop";
    }
    
    private boolean menuHasAddFriend(int j)
    {
        if(j < 0)
            return false;
        int k = this.menuActionID[j];
        if(k >= 2000)
            k -= 2000;
        return k == 337;
    }

    private void drawChatArea()
    {
        this.aRSImageProducer_1166.initDrawingArea();
        Texture.anIntArray1472 = this.anIntArray1180;
        IndexedImageRenderer.draw(this.chatBack, 0, 0);
        if(this.messagePromptRaised)
        {
            this.chatTextDrawingArea.drawText(0, this.aString1121, 40, 239);
            this.chatTextDrawingArea.drawText(128, this.promptInput + "*", 60, 239);
        } else
        if(this.inputDialogState == 1)
        {
            this.chatTextDrawingArea.drawText(0, "Enter amount:", 40, 239);
            this.chatTextDrawingArea.drawText(128, this.amountOrNameInput + "*", 60, 239);
        } else
        if(this.inputDialogState == 2)
        {
            this.chatTextDrawingArea.drawText(0, "Enter name:", 40, 239);
            this.chatTextDrawingArea.drawText(128, this.amountOrNameInput + "*", 60, 239);
        } else
        if(this.aString844 != null)
        {
            this.chatTextDrawingArea.drawText(0, this.aString844, 40, 239);
            this.chatTextDrawingArea.drawText(128, "Click to continue", 60, 239);
        } else
        if(this.backDialogID != -1)
            this.drawInterface(0, 0, RSInterface.interfaceCache[this.backDialogID], 0);
        else
        if(this.dialogID != -1)
        {
            this.drawInterface(0, 0, RSInterface.interfaceCache[this.dialogID], 0);
        } else
        {
            TextDrawingArea textDrawingArea = this.aTextDrawingArea_1271;
            int j = 0;
            DrawingArea.setDrawingArea(77, 0, 463, 0);
            for(int k = 0; k < 100; k++)
                if(this.chatMessages[k] != null)
                {
                    int l = this.chatTypes[k];
                    int i1 = (70 - j * 14) + this.anInt1089;
                    String s1 = this.chatNames[k];
                    byte byte0 = 0;
                    if(s1 != null && s1.startsWith("@cr1@"))
                    {
                        s1 = s1.substring(5);
                        byte0 = 1;
                    }
                    if(s1 != null && s1.startsWith("@cr2@"))
                    {
                        s1 = s1.substring(5);
                        byte0 = 2;
                    }
                    if(l == 0)
                    {
                        if(i1 > 0 && i1 < 110)
                            textDrawingArea.method385(0, this.chatMessages[k], i1, 4);
                        j++;
                    }
                    if((l == 1 || l == 2) && (l == 1 || this.publicChatMode == 0 || this.publicChatMode == 1 && this.isFriendOrSelf(s1)))
                    {
                        if(i1 > 0 && i1 < 110)
                        {
                            int j1 = 4;
                            if(byte0 == 1)
                            {
                                IndexedImageRenderer.draw(this.modIcons[0], j1, i1 - 12);
                                j1 += 14;
                            }
                            if(byte0 == 2)
                            {
                                IndexedImageRenderer.draw(this.modIcons[1], j1, i1 - 12);
                                j1 += 14;
                            }
                            textDrawingArea.method385(0, s1 + ":", i1, j1);
                            j1 += textDrawingArea.getTextWidth(s1) + 8;
                            textDrawingArea.method385(255, this.chatMessages[k], i1, j1);
                        }
                        j++;
                    }
                    if((l == 3 || l == 7) && this.splitPrivateChat == 0 && (l == 7 || this.privateChatMode == 0 || this.privateChatMode == 1 && this.isFriendOrSelf(s1)))
                    {
                        if(i1 > 0 && i1 < 110)
                        {
                            int k1 = 4;
                            textDrawingArea.method385(0, "From", i1, k1);
                            k1 += textDrawingArea.getTextWidth("From ");
                            if(byte0 == 1)
                            {
                                IndexedImageRenderer.draw(this.modIcons[0], k1, i1 - 12);
                                k1 += 14;
                            }
                            if(byte0 == 2)
                            {
                                IndexedImageRenderer.draw(this.modIcons[1], k1, i1 - 12);
                                k1 += 14;
                            }
                            textDrawingArea.method385(0, s1 + ":", i1, k1);
                            k1 += textDrawingArea.getTextWidth(s1) + 8;
                            textDrawingArea.method385(0x800000, this.chatMessages[k], i1, k1);
                        }
                        j++;
                    }
                    if(l == 4 && (this.tradeMode == 0 || this.tradeMode == 1 && this.isFriendOrSelf(s1)))
                    {
                        if(i1 > 0 && i1 < 110)
                            textDrawingArea.method385(0x800080, s1 + " " + this.chatMessages[k], i1, 4);
                        j++;
                    }
                    if(l == 5 && this.splitPrivateChat == 0 && this.privateChatMode < 2)
                    {
                        if(i1 > 0 && i1 < 110)
                            textDrawingArea.method385(0x800000, this.chatMessages[k], i1, 4);
                        j++;
                    }
                    if(l == 6 && this.splitPrivateChat == 0 && this.privateChatMode < 2)
                    {
                        if(i1 > 0 && i1 < 110)
                        {
                            textDrawingArea.method385(0, "To " + s1 + ":", i1, 4);
                            textDrawingArea.method385(0x800000, this.chatMessages[k], i1, 12 + textDrawingArea.getTextWidth("To " + s1));
                        }
                        j++;
                    }
                    if(l == 8 && (this.tradeMode == 0 || this.tradeMode == 1 && this.isFriendOrSelf(s1)))
                    {
                        if(i1 > 0 && i1 < 110)
                            textDrawingArea.method385(0x7e3200, s1 + " " + this.chatMessages[k], i1, 4);
                        j++;
                    }
                }

            DrawingArea.defaultDrawingAreaSize();
            this.anInt1211 = j * 14 + 7;
            if(this.anInt1211 < 78)
                this.anInt1211 = 78;
            this.method30(77, this.anInt1211 - this.anInt1089 - 77, 0, 463, this.anInt1211);
            String s;
            if(Client.localPlayer != null && Client.localPlayer.name != null)
                s = Client.localPlayer.name;
            else
                s = TextClass.fixName(this.myUsername);
            textDrawingArea.method385(0, s + ":", 90, 4);
            textDrawingArea.method385(255, this.inputString + "*", 90, 6 + textDrawingArea.getTextWidth(s + ": "));
            DrawingArea.method339(77, 0, 479, 0);
        }
        if(this.menuOpen)
            this.drawMenu(0, RSBase.frameMode == ScreenMode.FIXED ? 338 : 0);
        this.aRSImageProducer_1166.drawGraphics(Client.frameHeight - 503 + 357, this.applet.graphics, Client.frameWidth - 765 + 17);
        this.aRSImageProducer_1165.initDrawingArea();
        Texture.anIntArray1472 = this.anIntArray1182;
    }

    protected void drawLoadingText(int i, String s)
    {
        this.resetImageProducers();
        this.applet.drawLoadingText(i, s);
    }

    
    public Socket openSocket(int i)
        throws IOException
    {
        return new Socket(InetAddress.getByName(this.applet.getCodeBase().getHost()), i);
    }

    private void processMenuClick()
    {
        if(this.activeInterfaceType != 0)
            return;
        int j = this.applet.clickMode3;
        if(this.spellSelected == 1 && this.applet.saveClickX >= 516 && this.applet.saveClickY >= 160 && this.applet.saveClickX <= 765 && this.applet.saveClickY <= 205)
            j = 0;
        if(this.menuOpen)
        {
            if(j != 1)
            {
                int k = this.applet.mouseX;
                int j1 = this.applet.mouseY;
                if(this.menuScreenArea == 0)
                {
                    k -= 4;
                    j1 -= 4;
                }
                if(this.menuScreenArea == 1)
                {
                    k -= 553;
                    j1 -= 205;
                }
                if(this.menuScreenArea == 2)
                {
                    k -= 17;
                    j1 -= 357;
                }
                if(k < this.menuOffsetX - 10 || k > this.menuOffsetX + this.menuWidth + 10 || j1 < this.menuOffsetY - 10 || j1 > this.menuOffsetY + this.anInt952 + 10)
                {
                    this.menuOpen = false;
                    if(this.menuScreenArea == 1)
                        this.needDrawTabArea = true;
                    if(this.menuScreenArea == 2)
                        this.inputTaken = true;
                }
            }
            if(j == 1)
            {
                int l = this.menuOffsetX;
                int k1 = this.menuOffsetY;
                int i2 = this.menuWidth;
                int k2 = this.applet.saveClickX;
                int l2 = this.applet.saveClickY;
                if(this.menuScreenArea == 0)
                {
                    k2 -= 4;
                    l2 -= 4;
                }
                if(this.menuScreenArea == 1)
                {
                    k2 -= 553;
                    l2 -= 205;
                }
                if(this.menuScreenArea == 2)
                {
                    k2 -= 17;
                    l2 -= 357;
                }
                int i3 = -1;
                for(int j3 = 0; j3 < this.menuActionRow; j3++)
                {
                    int k3 = k1 + 31 + (this.menuActionRow - 1 - j3) * 15;
                    if(k2 > l && k2 < l + i2 && l2 > k3 - 13 && l2 < k3 + 3)
                        i3 = j3;
                }

                if(i3 != -1)
                    this.doAction(i3);
                this.menuOpen = false;
                if(this.menuScreenArea == 1)
                    this.needDrawTabArea = true;
                if(this.menuScreenArea == 2)
                {
                    this.inputTaken = true;
                }
            }
        } else
        {
            if(j == 1 && this.menuActionRow > 0)
            {
                int i1 = this.menuActionID[this.menuActionRow - 1];
                if(i1 == 632 || i1 == 78 || i1 == 867 || i1 == 431 || i1 == 53 || i1 == 74 || i1 == 454 || i1 == 539 || i1 == 493 || i1 == 847 || i1 == 447 || i1 == 1125)
                {
                    int l1 = this.menuActionCmd2[this.menuActionRow - 1];
                    int j2 = this.menuActionCmd3[this.menuActionRow - 1];
                    RSInterface class9 = RSInterface.interfaceCache[j2];
                    if(class9.aBoolean259 || class9.aBoolean235)
                    {
                        this.aBoolean1242 = false;
                        this.anInt989 = 0;
                        this.anInt1084 = j2;
                        this.anInt1085 = l1;
                        this.activeInterfaceType = 2;
                        this.anInt1087 = this.applet.saveClickX;
                        this.anInt1088 = this.applet.saveClickY;
                        if(RSInterface.interfaceCache[j2].parentID == this.openInterfaceID)
                            this.activeInterfaceType = 1;
                        if(RSInterface.interfaceCache[j2].parentID == this.backDialogID)
                            this.activeInterfaceType = 3;
                        return;
                    }
                }
            }
            if(j == 1 && (this.anInt1253 == 1 || this.menuHasAddFriend(this.menuActionRow - 1)) && this.menuActionRow > 2)
                j = 2;
            if(j == 1 && this.menuActionRow > 0)
                this.doAction(this.menuActionRow - 1);
            if(j == 2 && this.menuActionRow > 0)
                this.determineMenuSize();
        }
    }

    private void saveMidi(boolean flag, byte abyte0[])
    {
        signlink.midifade = flag ? 1 : 0;
        signlink.midisave(abyte0, abyte0.length);
    }

    private void method22()
    {
        try
        {
            this.anInt985 = -1;
            this.aClass19_1056.clear();
            this.projectiles.clear();
            Texture.method366();
            this.unlinkMRUNodes();
            this.worldController.initToNull();
            System.gc();
            for(int i = 0; i < 4; i++)
                this.aClass11Array1230[i].reset();

            for(int l = 0; l < 4; l++)
            {
                for(int k1 = 0; k1 < 104; k1++)
                {
                    for(int j2 = 0; j2 < 104; j2++)
                        this.byteGroundArray[l][k1][j2] = 0;

                }

            }

            ObjectManager objectManager = new ObjectManager(this.byteGroundArray, this.intGroundArray);
            int k2 = this.aByteArrayArray1183.length;
            this.stream.writeOpcode(0);
            if(!this.aBoolean1159)
            {
                for(int i3 = 0; i3 < k2; i3++)
                {
                    int i4 = (this.anIntArray1234[i3] >> 8) * 64 - this.baseX;
                    int k5 = (this.anIntArray1234[i3] & 0xff) * 64 - this.baseY;
                    byte abyte0[] = this.aByteArrayArray1183[i3];
                    if(abyte0 != null)
                        objectManager.decodeRegionMapData(abyte0, k5, i4, (this.anInt1069 - 6) * 8, (this.anInt1070 - 6) * 8, this.aClass11Array1230);
                }

                for(int j4 = 0; j4 < k2; j4++)
                {
                    int l5 = (this.anIntArray1234[j4] >> 8) * 64 - this.baseX;
                    int k7 = (this.anIntArray1234[j4] & 0xff) * 64 - this.baseY;
                    byte abyte2[] = this.aByteArrayArray1183[j4];
                    if(abyte2 == null && this.anInt1070 < 800)
                        objectManager.clearRegion(k7, 64, 64, l5);
                }

                Client.anInt1097++;
                if(Client.anInt1097 > 160)
                {
                    Client.anInt1097 = 0;
                    this.stream.writeOpcode(238);
                    this.stream.writeByte(96);
                }
                this.stream.writeOpcode(0);
                for(int i6 = 0; i6 < k2; i6++)
                {
                    byte abyte1[] = this.aByteArrayArray1247[i6];
                    if(abyte1 != null)
                    {
                        int l8 = (this.anIntArray1234[i6] >> 8) * 64 - this.baseX;
                        int k9 = (this.anIntArray1234[i6] & 0xff) * 64 - this.baseY;
                        objectManager.decodeLandscapes(l8, this.aClass11Array1230, k9, this.worldController, abyte1);
                   //     objectManager.decodeGlestObjects(); 
                    }
                }

            }
            if(this.aBoolean1159)
            {
                for(int j3 = 0; j3 < 4; j3++)
                {
                    for(int k4 = 0; k4 < 13; k4++)
                    {
                        for(int j6 = 0; j6 < 13; j6++)
                        {
                            int l7 = this.anIntArrayArrayArray1129[j3][k4][j6];
                            if(l7 != -1)
                            {
                                int i9 = l7 >> 24 & 3;
                                int l9 = l7 >> 1 & 3;
                                int j10 = l7 >> 14 & 0x3ff;
                                int l10 = l7 >> 3 & 0x7ff;
                                int j11 = (j10 / 8 << 8) + l10 / 8;
                                for(int l11 = 0; l11 < this.anIntArray1234.length; l11++)
                                {
                                    if(this.anIntArray1234[l11] != j11 || this.aByteArrayArray1183[l11] == null)
                                        continue;
                                    objectManager.decodeConstructedMapData(i9, l9, this.aClass11Array1230, k4 * 8, (j10 & 7) * 8, this.aByteArrayArray1183[l11], (l10 & 7) * 8, j3, j6 * 8);
                                    break;
                                }

                            }
                        }

                    }

                }

                for(int l4 = 0; l4 < 13; l4++)
                {
                    for(int k6 = 0; k6 < 13; k6++)
                    {
                        int i8 = this.anIntArrayArrayArray1129[0][l4][k6];
                        if(i8 == -1)
                            objectManager.clearRegion(k6 * 8, 8, 8, l4 * 8);
                    }

                }

                this.stream.writeOpcode(0);
                for(int l6 = 0; l6 < 4; l6++)
                {
                    for(int j8 = 0; j8 < 13; j8++)
                    {
                        for(int j9 = 0; j9 < 13; j9++)
                        {
                            int i10 = this.anIntArrayArrayArray1129[l6][j8][j9];
                            if(i10 != -1)
                            {
                                int k10 = i10 >> 24 & 3;
                                int i11 = i10 >> 1 & 3;
                                int k11 = i10 >> 14 & 0x3ff;
                                int i12 = i10 >> 3 & 0x7ff;
                                int j12 = (k11 / 8 << 8) + i12 / 8;
                                for(int k12 = 0; k12 < this.anIntArray1234.length; k12++)
                                {
                                    if(this.anIntArray1234[k12] != j12 || this.aByteArrayArray1247[k12] == null)
                                        continue;
                                    objectManager.decodeConstructedLandscapes(this.aClass11Array1230, this.worldController, k10, j8 * 8, (i12 & 7) * 8, l6, this.aByteArrayArray1247[k12], (k11 & 7) * 8, i11, j9 * 8);
                                    break;
                                }

                            }
                        }

                    }

                }

            }
            this.stream.writeOpcode(0);
            objectManager.addTiles(this.aClass11Array1230, this.worldController, 0);
            this.aRSImageProducer_1165.initDrawingArea();
            this.stream.writeOpcode(0);
            int k3 = ObjectManager.maximumPlane;
            if(k3 > this.plane)
                k3 = this.plane;
            if(k3 < this.plane - 1)
                k3 = this.plane - 1;
            if(Client.lowMem)
                this.worldController.method275(ObjectManager.maximumPlane);
            else
                this.worldController.method275(0);
            for(int i5 = 0; i5 < 104; i5++)
            {
                for(int i7 = 0; i7 < 104; i7++)
                    this.spawnGroundItem(i5, i7);

            }

            Client.anInt1051++;
            if(Client.anInt1051 > 98)
            {
                Client.anInt1051 = 0;
                this.stream.writeOpcode(150);
            }
            this.method63();
        }
        catch(Exception exception) { }
        ObjectDef.mruNodes1.clear();
        if(this.applet.gameFrame != null)
        {
            this.stream.writeOpcode(210);
            this.stream.writeInt(0x3f008edd);
        }
        if(Client.lowMem && signlink.cache_dat != null)
        {
            int j = this.onDemandFetcher.getVersionCount(0);
            for(int i1 = 0; i1 < j; i1++)
            {
                int l1 = this.onDemandFetcher.getModelIndex(i1);
                if((l1 & 0x79) == 0)
                    Model.method461(i1);
            }

        }
        System.gc();
        Texture.method367();
        this.onDemandFetcher.method566();
        int k = (this.anInt1069 - 6) / 8 - 1;
        int j1 = (this.anInt1069 + 6) / 8 + 1;
        int i2 = (this.anInt1070 - 6) / 8 - 1;
        int l2 = (this.anInt1070 + 6) / 8 + 1;
        if(this.aBoolean1141)
        {
            k = 49;
            j1 = 50;
            i2 = 49;
            l2 = 50;
        }
        for(int l3 = k; l3 <= j1; l3++)
        {
            for(int j5 = i2; j5 <= l2; j5++)
                if(l3 == k || l3 == j1 || j5 == i2 || j5 == l2)
                {
                    int j7 = this.onDemandFetcher.method562(0, j5, l3);
                    if(j7 != -1)
                        this.onDemandFetcher.method560(j7, 3);
                    int k8 = this.onDemandFetcher.method562(1, j5, l3);
                    if(k8 != -1)
                        this.onDemandFetcher.method560(k8, 3);
                }

        }

    }

    private void unlinkMRUNodes()
    {
        ObjectDef.mruNodes1.clear();
        ObjectDef.mruNodes2.clear();
        EntityDef.mruNodes.clear();
        ItemDef.mruNodes2.clear();
        ItemDef.mruNodes1.clear();
        Player.mruNodes.clear();
        AnimableRenderer.spotanimcache.clear();
    }

    private void method24(int i)
    {
        int ai[] = this.minimapImage.raster;
        int j = ai.length;
        for(int k = 0; k < j; k++)
            ai[k] = 0;

        for(int l = 1; l < 103; l++)
        {
            int i1 = 24628 + (103 - l) * 512 * 4;
            for(int k1 = 1; k1 < 103; k1++)
            {
                if((this.byteGroundArray[i][k1][l] & 0x18) == 0)
                    this.worldController.method309(ai, i1, i, k1, l);
                if(i < 3 && (this.byteGroundArray[i + 1][k1][l] & 8) != 0)
                    this.worldController.method309(ai, i1, i + 1, k1, l);
                i1 += 4;
            }

        }

        int j1 = ((238 + (int)(Math.random() * 20D)) - 10 << 16) + ((238 + (int)(Math.random() * 20D)) - 10 << 8) + ((238 + (int)(Math.random() * 20D)) - 10);
        int l1 = (238 + (int)(Math.random() * 20D)) - 10 << 16;
        SpriteRenderer.initRaster(this.minimapImage);
        for(int i2 = 1; i2 < 103; i2++)
        {
            for(int j2 = 1; j2 < 103; j2++)
            {
                if((this.byteGroundArray[i][j2][i2] & 0x18) == 0)
                    this.method50(i2, j1, j2, l1, i);
                if(i < 3 && (this.byteGroundArray[i + 1][j2][i2] & 8) != 0)
                    this.method50(i2, j1, j2, l1, i + 1);
            }

        }

        this.aRSImageProducer_1165.initDrawingArea();
        this.minimapFunctionCount = 0;
        for(int k2 = 0; k2 < 104; k2++)
        {
            for(int l2 = 0; l2 < 104; l2++)
            {
                int i3 = this.worldController.method303(this.plane, k2, l2);
                if(i3 != 0)
                {
                    i3 = i3 >> 14 & 0x7fff;
                    int j3 = ObjectDef.forID(i3).anInt746;
                    if(j3 >= 0)
                    {
                        int k3 = k2;
                        int l3 = l2;
                        if(j3 != 22 && j3 != 29 && j3 != 34 && j3 != 36 && j3 != 46 && j3 != 47 && j3 != 48)
                        {
                            byte byte0 = 104;
                            byte byte1 = 104;
                            int ai1[][] = this.aClass11Array1230[this.plane].flags;
                            for(int i4 = 0; i4 < 10; i4++)
                            {
                                int j4 = (int)(Math.random() * 4D);
                                if(j4 == 0 && k3 > 0 && k3 > k2 - 3 && (ai1[k3 - 1][l3] & 0x1280108) == 0)
                                    k3--;
                                if(j4 == 1 && k3 < byte0 - 1 && k3 < k2 + 3 && (ai1[k3 + 1][l3] & 0x1280180) == 0)
                                    k3++;
                                if(j4 == 2 && l3 > 0 && l3 > l2 - 3 && (ai1[k3][l3 - 1] & 0x1280102) == 0)
                                    l3--;
                                if(j4 == 3 && l3 < byte1 - 1 && l3 < l2 + 3 && (ai1[k3][l3 + 1] & 0x1280120) == 0)
                                    l3++;
                            }

                        }
                        this.minimapFunctions[this.minimapFunctionCount] = this.mapFunctions[j3];
                        this.minimapFunctionX[this.minimapFunctionCount] = k3;
                        this.minimapFunctionY[this.minimapFunctionCount] = l3;
                        this.minimapFunctionCount++;
                    }
                }
            }

        }

    }

    private void spawnGroundItem(int i, int j)
    {
        Deque deque = this.levelObjects[this.plane][i][j];
        if(deque == null)
        {
            this.worldController.method295(this.plane, i, j);
            return;
        }
        int k = 0xfa0a1f01;
        Object obj = null;
        for(Item item = (Item)deque.getFront(); item != null; item = (Item)deque.getNext())
        {
            ItemDef itemDef = ItemDef.forID(item.id);
            int l = itemDef.value;
            if(itemDef.stackable)
                l *= item.amount + 1;
//	notifyItemSpawn(item, i + baseX, j + baseY);
	
            if(l > k)
            {
                k = l;
                obj = item;
            }
        }

        deque.pushFront(((Linkable) (obj)));
        Object obj1 = null;
        Object obj2 = null;
        for(Item class30_sub2_sub4_sub2_1 = (Item)deque.getFront(); class30_sub2_sub4_sub2_1 != null; class30_sub2_sub4_sub2_1 = (Item)deque.getNext())
        {
            if(class30_sub2_sub4_sub2_1.id != ((Item) (obj)).id && obj1 == null)
                obj1 = class30_sub2_sub4_sub2_1;
            if(class30_sub2_sub4_sub2_1.id != ((Item) (obj)).id && class30_sub2_sub4_sub2_1.id != ((Item) (obj1)).id && obj2 == null)
                obj2 = class30_sub2_sub4_sub2_1;
        }

        int i1 = i + (j << 7) + 0x60000000;
        this.worldController.method281(i, i1, ((Animable) (obj1)), this.tileHeight(this.plane, j * 128 + 64, i * 128 + 64), ((Animable) (obj2)), ((Animable) (obj)), this.plane, j);
    }

    private void method26(boolean flag)
    {
        for(int j = 0; j < this.npcCount; j++)
        {
            NPC npc = this.npcArray[this.npcIndices[j]];
            int k = 0x20000000 + (this.npcIndices[j] << 14);
            if(npc == null || !npc.isVisible() || npc.desc.aBoolean93 != flag)
                continue;
            int l = npc.x >> 7;
            int i1 = npc.y >> 7;
            if(l < 0 || l >= 104 || i1 < 0 || i1 >= 104)
                continue;
            if(npc.anInt1540 == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64)
            {
                if(this.anIntArrayArray929[l][i1] == this.anInt1265)
                    continue;
                this.anIntArrayArray929[l][i1] = this.anInt1265;
            }
            if(!npc.desc.aBoolean84)
                k += 0x80000000;
            this.worldController.method285(this.plane, npc.anInt1552, this.tileHeight(this.plane, npc.y, npc.x), k, npc.y, (npc.anInt1540 - 1) * 64 + 60, npc.x, npc, npc.aBoolean1541);
        }
    }

    private boolean replayWave()
    {
            return signlink.wavereplay();
    }

    private void buildInterfaceMenu(int i, RSInterface class9, int k, int l, int i1, int j1)
    {
        if(class9.type != 0 || class9.children == null || class9.aBoolean266)
            return;
        if(k < i || i1 < l || k > i + class9.width || i1 > l + class9.height)
            return;
        int k1 = class9.children.length;
        for(int l1 = 0; l1 < k1; l1++)
        {
            int i2 = class9.childX[l1] + i;
            int j2 = (class9.childY[l1] + l) - j1;
            RSInterface class9_1 = RSInterface.interfaceCache[class9.children[l1]];
            i2 += class9_1.anInt263;
            j2 += class9_1.anInt265;
            if((class9_1.anInt230 >= 0 || class9_1.anInt216 != 0) && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height)
                if(class9_1.anInt230 >= 0)
                    this.anInt886 = class9_1.anInt230;
                else
                    this.anInt886 = class9_1.id;
            if (class9_1.type == 8 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
                this.anInt1315 = class9_1.id;
            }
            if(class9_1.type == 0)
            {
                this.buildInterfaceMenu(i2, class9_1, k, j2, i1, class9_1.scrollPosition);
                if(class9_1.scrollMax > class9_1.height)
                    this.method65(i2 + class9_1.width, class9_1.height, k, i1, class9_1, j2, true, class9_1.scrollMax);
            } else
            {
                if(class9_1.atActionType == 1 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height)
                {
                    boolean flag = false;
                    if(class9_1.anInt214 != 0)
                        flag = this.buildFriendsListMenu(class9_1);
                    if(!flag)
                    {
                        //System.out.println("1"+class9_1.tooltip + ", " + class9_1.interfaceID);
                        this.menuActionName[this.menuActionRow] = class9_1.tooltip + ", " + class9_1.id;
                        this.menuActionID[this.menuActionRow] = 315;
                        this.menuActionCmd3[this.menuActionRow] = class9_1.id;
                        this.menuActionRow++;
                    }
                }
                if(class9_1.atActionType == 2 && this.spellSelected == 0 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height)
                {
                    String s = class9_1.selectedActionName;
                    if(s.indexOf(" ") != -1)
                        s = s.substring(0, s.indexOf(" "));
                    this.menuActionName[this.menuActionRow] = s + " @gre@" + class9_1.spellName;
                    this.menuActionID[this.menuActionRow] = 626;
                    this.menuActionCmd3[this.menuActionRow] = class9_1.id;
                    this.menuActionRow++;
                }
                if(class9_1.atActionType == 3 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height)
                {
                    this.menuActionName[this.menuActionRow] = "Close";
                    this.menuActionID[this.menuActionRow] = 200;
                    this.menuActionCmd3[this.menuActionRow] = class9_1.id;
                    this.menuActionRow++;
                }
                if(class9_1.atActionType == 4 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height)
                {
                    //System.out.println("2"+class9_1.tooltip + ", " + class9_1.interfaceID);
                    this.menuActionName[this.menuActionRow] = class9_1.tooltip + ", " + class9_1.id;
                    this.menuActionID[this.menuActionRow] = 169;
                    this.menuActionCmd3[this.menuActionRow] = class9_1.id;
                    this.menuActionRow++;
                }
                if(class9_1.atActionType == 5 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height)
                {
                    //System.out.println("3"+class9_1.tooltip + ", " + class9_1.interfaceID);
                    this.menuActionName[this.menuActionRow] = class9_1.tooltip + ", " + class9_1.id;
                    this.menuActionID[this.menuActionRow] = 646;
                    this.menuActionCmd3[this.menuActionRow] = class9_1.id;
                    this.menuActionRow++;
                }
                if(class9_1.atActionType == 6 && !this.aBoolean1149 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height)
                {
                    //System.out.println("4"+class9_1.tooltip + ", " + class9_1.interfaceID);
                    this.menuActionName[this.menuActionRow] = class9_1.tooltip + ", " + class9_1.id;
                    this.menuActionID[this.menuActionRow] = 679;
                    this.menuActionCmd3[this.menuActionRow] = class9_1.id;
                    this.menuActionRow++;
                }
                if(class9_1.type == 2)
                {
                    int k2 = 0;
                    for(int l2 = 0; l2 < class9_1.height; l2++)
                    {
                        for(int i3 = 0; i3 < class9_1.width; i3++)
                        {
                            int j3 = i2 + i3 * (32 + class9_1.invSpritePadX);
                            int k3 = j2 + l2 * (32 + class9_1.invSpritePadY);
                            if(k2 < 20)
                            {
                                j3 += class9_1.spritesX[k2];
                                k3 += class9_1.spritesY[k2];
                            }
                            if(k >= j3 && i1 >= k3 && k < j3 + 32 && i1 < k3 + 32)
                            {
                                this.mouseInvInterfaceIndex = k2;
                                this.lastActiveInvInterface = class9_1.id;
                                if(class9_1.inv[k2] > 0)
                                {
                                    ItemDef itemDef = ItemDef.forID(class9_1.inv[k2] - 1);
                                    if(this.itemSelected == 1 && class9_1.isInventoryInterface)
                                    {
                                        if(class9_1.id != this.anInt1284 || k2 != this.anInt1283)
                                        {
                                            this.menuActionName[this.menuActionRow] = "Use " + this.selectedItemName + " with @lre@" + itemDef.name;
                                            this.menuActionID[this.menuActionRow] = 870;
                                            this.menuActionCmd1[this.menuActionRow] = itemDef.id;
                                            this.menuActionCmd2[this.menuActionRow] = k2;
                                            this.menuActionCmd3[this.menuActionRow] = class9_1.id;
                                            this.menuActionRow++;
                                        }
                                    } else
                                    if(this.spellSelected == 1 && class9_1.isInventoryInterface)
                                    {
                                        if((this.spellUsableOn & 0x10) == 16)
                                        {
                                            this.menuActionName[this.menuActionRow] = this.spellTooltip + " @lre@" + itemDef.name;
                                            this.menuActionID[this.menuActionRow] = 543;
                                            this.menuActionCmd1[this.menuActionRow] = itemDef.id;
                                            this.menuActionCmd2[this.menuActionRow] = k2;
                                            this.menuActionCmd3[this.menuActionRow] = class9_1.id;
                                            this.menuActionRow++;
                                        }
                                    } else
                                    {
                                        if(class9_1.isInventoryInterface)
                                        {
                                            for(int l3 = 4; l3 >= 3; l3--)
                                                if(itemDef.actions != null && itemDef.actions[l3] != null)
                                                {
                                                    this.menuActionName[this.menuActionRow] = itemDef.actions[l3] + " @lre@" + itemDef.name;
                                                    if(l3 == 3)
                                                        this.menuActionID[this.menuActionRow] = 493;
                                                    if(l3 == 4)
                                                        this.menuActionID[this.menuActionRow] = 847;
                                                    this.menuActionCmd1[this.menuActionRow] = itemDef.id;
                                                    this.menuActionCmd2[this.menuActionRow] = k2;
                                                    this.menuActionCmd3[this.menuActionRow] = class9_1.id;
                                                    this.menuActionRow++;
                                                } else
                                                if(l3 == 4)
                                                {
                                                    this.menuActionName[this.menuActionRow] = "Drop @lre@" + itemDef.name;
                                                    this.menuActionID[this.menuActionRow] = 847;
                                                    this.menuActionCmd1[this.menuActionRow] = itemDef.id;
                                                    this.menuActionCmd2[this.menuActionRow] = k2;
                                                    this.menuActionCmd3[this.menuActionRow] = class9_1.id;
                                                    this.menuActionRow++;
                                                }

                                        }
                                        if(class9_1.usableItemInterface)
                                        {
                                            this.menuActionName[this.menuActionRow] = "Use @lre@" + itemDef.name;
                                            this.menuActionID[this.menuActionRow] = 447;
                                            this.menuActionCmd1[this.menuActionRow] = itemDef.id;
                                            this.menuActionCmd2[this.menuActionRow] = k2;
                                            this.menuActionCmd3[this.menuActionRow] = class9_1.id;
                                            this.menuActionRow++;
                                        }
                                        if(class9_1.isInventoryInterface && itemDef.actions != null)
                                        {
                                            for(int i4 = 2; i4 >= 0; i4--)
                                                if(itemDef.actions[i4] != null)
                                                {
                                                    this.menuActionName[this.menuActionRow] = itemDef.actions[i4] + " @lre@" + itemDef.name;
                                                    if(i4 == 0)
                                                        this.menuActionID[this.menuActionRow] = 74;
                                                    if(i4 == 1)
                                                        this.menuActionID[this.menuActionRow] = 454;
                                                    if(i4 == 2)
                                                        this.menuActionID[this.menuActionRow] = 539;
                                                    this.menuActionCmd1[this.menuActionRow] = itemDef.id;
                                                    this.menuActionCmd2[this.menuActionRow] = k2;
                                                    this.menuActionCmd3[this.menuActionRow] = class9_1.id;
                                                    this.menuActionRow++;
                                                }

                                        }
                                        if(class9_1.actions != null)
                                        {
                                            for(int j4 = 4; j4 >= 0; j4--)
                                                if(class9_1.actions[j4] != null)
                                                {
                                                    this.menuActionName[this.menuActionRow] = class9_1.actions[j4] + " @lre@" + itemDef.name;
                                                    if(j4 == 0)
                                                        this.menuActionID[this.menuActionRow] = 632;
                                                    if(j4 == 1)
                                                        this.menuActionID[this.menuActionRow] = 78;
                                                    if(j4 == 2)
                                                        this.menuActionID[this.menuActionRow] = 867;
                                                    if(j4 == 3)
                                                        this.menuActionID[this.menuActionRow] = 431;
                                                    if(j4 == 4)
                                                        this.menuActionID[this.menuActionRow] = 53;
                                                    this.menuActionCmd1[this.menuActionRow] = itemDef.id;
                                                    this.menuActionCmd2[this.menuActionRow] = k2;
                                                    this.menuActionCmd3[this.menuActionRow] = class9_1.id;
                                                    this.menuActionRow++;
                                                }

                                        }
                                        this.menuActionName[this.menuActionRow] = "Examine @lre@" + itemDef.name + " @gre@(@whi@" + (class9_1.inv[k2] - 1) + "@gre@)";
                                        this.menuActionID[this.menuActionRow] = 1125;
                                        this.menuActionCmd1[this.menuActionRow] = itemDef.id;
                                        this.menuActionCmd2[this.menuActionRow] = k2;
                                        this.menuActionCmd3[this.menuActionRow] = class9_1.id;
                                        this.menuActionRow++;
                                    }
                                }
                            }
                            k2++;
                        }

                    }

                }
            }
        }

    }

    private void method30(int j, int k, int l, int i1, int j1)
    {
        IndexedImageRenderer.draw(this.scrollBar1, i1, l);
        IndexedImageRenderer.draw(this.scrollBar2, i1, (l + j) - 16);
        DrawingArea.drawPixels(j - 32, l + 16, i1, this.anInt1002, 16);
        int k1 = ((j - 32) * j) / j1;
        if(k1 < 8)
            k1 = 8;
        int l1 = ((j - 32 - k1) * k) / (j1 - j);
        DrawingArea.drawPixels(k1, l + 16 + l1, i1, this.anInt1063, 16);
        DrawingArea.method341(l + 16 + l1, this.anInt902, k1, i1);
        DrawingArea.method341(l + 16 + l1, this.anInt902, k1, i1 + 1);
        DrawingArea.method339(l + 16 + l1, this.anInt902, 16, i1);
        DrawingArea.method339(l + 17 + l1, this.anInt902, 16, i1);
        DrawingArea.method341(l + 16 + l1, this.anInt927, k1, i1 + 15);
        DrawingArea.method341(l + 17 + l1, this.anInt927, k1 - 1, i1 + 14);
        DrawingArea.method339(l + 15 + l1 + k1, this.anInt927, 16, i1);
        DrawingArea.method339(l + 14 + l1 + k1, this.anInt927, 15, i1 + 1);
    }

    private void updateNPCs(BitBuffer buffer, int i)
    {
        this.anInt839 = 0;
        this.anInt893 = 0;
        this.method139(buffer);
        this.method46(i, buffer);
        this.method86(buffer);
        for(int k = 0; k < this.anInt839; k++)
        {
            int l = this.anIntArray840[k];
            if(this.npcArray[l].anInt1537 != Client.loopCycle)
            {
                this.npcArray[l].desc = null;
                this.npcArray[l] = null;
            }
        }

        if(buffer.position != i)
        {
            Utils.reporterror(this.myUsername + " size mismatch in getnpcpos - pos:" + buffer.position + " psize:" + i);
            throw new RuntimeException("eek");
        }
        for(int i1 = 0; i1 < this.npcCount; i1++)
            if(this.npcArray[this.npcIndices[i1]] == null)
            {
                Utils.reporterror(this.myUsername + " null entry in npc list - pos:" + i1 + " size:" + this.npcCount);
                throw new RuntimeException("eek");
            }

    }

    private void processChatModeClick()
    {
        if(this.applet.clickMode3 == 1)
        {
            if(this.applet.saveClickX >= 6 && this.applet.saveClickX <= 106 && this.applet.saveClickY >= 467 && this.applet.saveClickY <= 499)
            {
                this.publicChatMode = (this.publicChatMode + 1) % 4;
                this.aBoolean1233 = true;
                this.inputTaken = true;
                this.stream.writeOpcode(95);
                this.stream.writeByte(this.publicChatMode);
                this.stream.writeByte(this.privateChatMode);
                this.stream.writeByte(this.tradeMode);
            }
            if(this.applet.saveClickX >= 135 && this.applet.saveClickX <= 235 && this.applet.saveClickY >= 467 && this.applet.saveClickY <= 499)
            {
                this.privateChatMode = (this.privateChatMode + 1) % 3;
                this.aBoolean1233 = true;
                this.inputTaken = true;
                this.stream.writeOpcode(95);
                this.stream.writeByte(this.publicChatMode);
                this.stream.writeByte(this.privateChatMode);
                this.stream.writeByte(this.tradeMode);
            }
            if(this.applet.saveClickX >= 273 && this.applet.saveClickX <= 373 && this.applet.saveClickY >= 467 && this.applet.saveClickY <= 499)
            {
                this.tradeMode = (this.tradeMode + 1) % 3;
                this.aBoolean1233 = true;
                this.inputTaken = true;
                this.stream.writeOpcode(95);
                this.stream.writeByte(this.publicChatMode);
                this.stream.writeByte(this.privateChatMode);
                this.stream.writeByte(this.tradeMode);
            }
            if(this.applet.saveClickX >= 412 && this.applet.saveClickX <= 512 && this.applet.saveClickY >= 467 && this.applet.saveClickY <= 499)
                if(this.openInterfaceID == -1)
                {
                    this.clearTopInterfaces();
                    this.reportAbuseInput = "";
                    this.canMute = false;
                    for(int i = 0; i < RSInterface.interfaceCache.length; i++)
                    {
                        if(RSInterface.interfaceCache[i] == null || RSInterface.interfaceCache[i].anInt214 != 600)
                            continue;
                        this.reportAbuseInterfaceID = this.openInterfaceID = RSInterface.interfaceCache[i].parentID;
                        break;
                    }

                } else
                {
                    this.pushMessage("Please close the interface you have open before using 'report abuse'", 0, "");
                }
            Client.anInt940++;
            if(Client.anInt940 > 1386)
            {
                Client.anInt940 = 0;
                this.stream.writeOpcode(165);
                this.stream.writeByte(0);
                int j = this.stream.position;
                this.stream.writeByte(139);
                this.stream.writeByte(150);
                this.stream.writeShort(32131);
                this.stream.writeByte((int)(Math.random() * 256D));
                this.stream.writeShort(3250);
                this.stream.writeByte(177);
                this.stream.writeShort(24859);
                this.stream.writeByte(119);
                if((int)(Math.random() * 2D) == 0)
                    this.stream.writeShort(47234);
                if((int)(Math.random() * 2D) == 0)
                    this.stream.writeByte(21);
                this.stream.writeSizeByte(this.stream.position - j);
            }
        }
    }

    private void method33(int index)
    {
    	/*
    	 166-1
167-2
168-3
169-4
170-5
171-6
173-7
287-8
304-9
    	 */
        int type = VariableParameter.get(index);
        if(type == 0)
            return;
        int k = this.variousSettings[index];
        if(type == 1)
        {
            if(k == 1)
                Texture.method372(0.90000000000000002D);
            if(k == 2)
                Texture.method372(0.80000000000000004D);
            if(k == 3)
                Texture.method372(0.69999999999999996D);
            if(k == 4)
                Texture.method372(0.59999999999999998D);
            ItemDef.mruNodes1.clear();
            this.welcomeScreenRaised = true;
        }
        if(type == 3)
        {
            boolean flag1 = this.musicEnabled;
            if(k == 0)
            {
                this.adjustVolume(this.musicEnabled, 0);
                this.musicEnabled = true;
            }
            if(k == 1)
            {
                this.adjustVolume(this.musicEnabled, -400);
                this.musicEnabled = true;
            }
            if(k == 2)
            {
                this.adjustVolume(this.musicEnabled, -800);
                this.musicEnabled = true;
            }
            if(k == 3)
            {
                this.adjustVolume(this.musicEnabled, -1200);
                this.musicEnabled = true;
            }
            if(k == 4)
                this.musicEnabled = false;
            if(this.musicEnabled != flag1 && !Client.lowMem)
            {
                if(this.musicEnabled)
                {
                    this.nextSong = this.currentSong;
                    this.songChanging = true;
                    this.onDemandFetcher.method558(2, this.nextSong);
                } else
                {
                    this.stopMidi();
                }
                this.prevSong = 0;
            }
        }
        if(type == 4)
        {
            if(k == 0)
            {
                this.aBoolean848 = true;
                this.setWaveVolume(0);
            }
            if(k == 1)
            {
                this.aBoolean848 = true;
                this.setWaveVolume(-400);
            }
            if(k == 2)
            {
                this.aBoolean848 = true;
                this.setWaveVolume(-800);
            }
            if(k == 3)
            {
                this.aBoolean848 = true;
                this.setWaveVolume(-1200);
            }
            if(k == 4)
                this.aBoolean848 = false;
        }
        if(type == 5)
            this.anInt1253 = k;
        if(type == 6)
            this.anInt1249 = k;
        if(type == 8)
        {
            this.splitPrivateChat = k;
            this.inputTaken = true;
        }
        if(type == 9)
            this.anInt913 = k;
    }

    private void updateEntities()
    {
        try{
            int anInt974 = 0;
            for(int j = -1; j < this.playerCount + this.npcCount; j++)
        {
            Object obj;
            if(j == -1)
                obj = Client.localPlayer;
            else
            if(j < this.playerCount)
                obj = this.playerArray[this.playerIndices[j]];
            else
                obj = this.npcArray[this.npcIndices[j - this.playerCount]];
            if(obj == null || !((Entity) (obj)).isVisible())
                continue;
            if(obj instanceof NPC)
            {
                EntityDef entityDef = ((NPC)obj).desc;
                if(entityDef.childrenIDs != null)
                    entityDef = entityDef.method161();
                if(entityDef == null)
                    continue;
            }
            if(j < this.playerCount)
            {
                int l = 30;
                Player player = (Player)obj;
                if(player.headIcon != 0)
                {
                    this.npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
                    if(this.spriteDrawX > -1)
                    {
                        for(int i2 = 0; i2 < 8; i2++)
                            if((player.headIcon & 1 << i2) != 0)
                            {
                                SpriteRenderer.drawSprite(this.headIcons[i2], this.spriteDrawX - 12, this.spriteDrawY - l);
                                l -= 25;
                            }

                    }
                }
                if(j >= 0 && this.anInt855 == 10 && this.anInt933 == this.playerIndices[j])
                {
                    this.npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
                    if(this.spriteDrawX > -1)
                        SpriteRenderer.drawSprite(this.headIcons[7], this.spriteDrawX - 12, this.spriteDrawY - l);
                }
            } else
            {
                EntityDef entityDef_1 = ((NPC)obj).desc;
                if(entityDef_1.anInt75 >= 0 && entityDef_1.anInt75 < this.headIcons.length)
                {
                    this.npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
                    if(this.spriteDrawX > -1)
                        SpriteRenderer.drawSprite(this.headIcons[entityDef_1.anInt75], this.spriteDrawX - 12, this.spriteDrawY - 30);
                }
                if(this.anInt855 == 1 && this.anInt1222 == this.npcIndices[j - this.playerCount] && Client.loopCycle % 20 < 10)
                {
                    this.npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
                    if(this.spriteDrawX > -1)
                        SpriteRenderer.drawSprite(this.headIcons[2], this.spriteDrawX - 12, this.spriteDrawY - 28);
                }
            }
            if(((Entity) (obj)).textSpoken != null && (j >= this.playerCount || this.publicChatMode == 0 || this.publicChatMode == 3 || this.publicChatMode == 1 && this.isFriendOrSelf(((Player)obj).name)))
            {
                this.npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height);
                if(this.spriteDrawX > -1 && anInt974 < this.anInt975)
                {
                    this.anIntArray979[anInt974] = this.chatTextDrawingArea.method384(((Entity) (obj)).textSpoken) / 2;
                    this.anIntArray978[anInt974] = this.chatTextDrawingArea.anInt1497;
                    this.anIntArray976[anInt974] = this.spriteDrawX;
                    this.anIntArray977[anInt974] = this.spriteDrawY;
                    this.anIntArray980[anInt974] = ((Entity) (obj)).anInt1513;
                    this.anIntArray981[anInt974] = ((Entity) (obj)).anInt1531;
                    this.anIntArray982[anInt974] = ((Entity) (obj)).textCycle;
                    this.aStringArray983[anInt974++] = ((Entity) (obj)).textSpoken;
                    if(this.anInt1249 == 0 && ((Entity) (obj)).anInt1531 >= 1 && ((Entity) (obj)).anInt1531 <= 3)
                    {
                        this.anIntArray978[anInt974] += 10;
                        this.anIntArray977[anInt974] += 5;
                    }
                    if(this.anInt1249 == 0 && ((Entity) (obj)).anInt1531 == 4)
                        this.anIntArray979[anInt974] = 60;
                    if(this.anInt1249 == 0 && ((Entity) (obj)).anInt1531 == 5)
                        this.anIntArray978[anInt974] += 5;
                }
            }
            if(((Entity) (obj)).loopCycleStatus > Client.loopCycle)
            { try{
                this.npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
                if(this.spriteDrawX > -1)
                {
                    int i1 = (((Entity) (obj)).currentHealth * 30) / ((Entity) (obj)).maxHealth;
                    if(i1 > 30)
                        i1 = 30;
                    DrawingArea.drawPixels(5, this.spriteDrawY - 3, this.spriteDrawX - 15, 65280, i1);
                    DrawingArea.drawPixels(5, this.spriteDrawY - 3, (this.spriteDrawX - 15) + i1, 0xff0000, 30 - i1);
                }
            }catch(Exception e){ }
            }
            for(int j1 = 0; j1 < 4; j1++)
                if(((Entity) (obj)).hitsLoopCycle[j1] > Client.loopCycle)
                {
                    this.npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height / 2);
                    if(this.spriteDrawX > -1)
                    {
                        if(j1 == 1)
                            this.spriteDrawY -= 20;
                        if(j1 == 2)
                        {
                            this.spriteDrawX -= 15;
                            this.spriteDrawY -= 10;
                        }
                        if(j1 == 3)
                        {
                            this.spriteDrawX += 15;
                            this.spriteDrawY -= 10;
                        }
                        SpriteRenderer.drawSprite(this.hitMarks[((Entity) (obj)).hitMarkTypes[j1]], this.spriteDrawX - 12, this.spriteDrawY - 12);
                        this.aTextDrawingArea_1270.drawText(0, String.valueOf(((Entity) (obj)).hitArray[j1]), this.spriteDrawY + 4, this.spriteDrawX);
                        this.aTextDrawingArea_1270.drawText(0xffffff, String.valueOf(((Entity) (obj)).hitArray[j1]), this.spriteDrawY + 3, this.spriteDrawX - 1);
                    }
                }

        }
        for(int k = 0; k < anInt974; k++)
        {
            int k1 = this.anIntArray976[k];
            int l1 = this.anIntArray977[k];
            int j2 = this.anIntArray979[k];
            int k2 = this.anIntArray978[k];
            boolean flag = true;
            while(flag) 
            {
                flag = false;
                for(int l2 = 0; l2 < k; l2++)
                    if(l1 + 2 > this.anIntArray977[l2] - this.anIntArray978[l2] && l1 - k2 < this.anIntArray977[l2] + 2 && k1 - j2 < this.anIntArray976[l2] + this.anIntArray979[l2] && k1 + j2 > this.anIntArray976[l2] - this.anIntArray979[l2] && this.anIntArray977[l2] - this.anIntArray978[l2] < l1)
                    {
                        l1 = this.anIntArray977[l2] - this.anIntArray978[l2];
                        flag = true;
                    }

            }
            this.spriteDrawX = this.anIntArray976[k];
            this.spriteDrawY = this.anIntArray977[k] = l1;
            String s = this.aStringArray983[k];
            if(this.anInt1249 == 0)
            {
                int i3 = 0xffff00;
                if(this.anIntArray980[k] < 6)
                    i3 = this.anIntArray965[this.anIntArray980[k]];
                if(this.anIntArray980[k] == 6)
                    i3 = this.anInt1265 % 20 >= 10 ? 0xffff00 : 0xff0000;
                if(this.anIntArray980[k] == 7)
                    i3 = this.anInt1265 % 20 >= 10 ? 65535 : 255;
                if(this.anIntArray980[k] == 8)
                    i3 = this.anInt1265 % 20 >= 10 ? 0x80ff80 : 45056;
                if(this.anIntArray980[k] == 9)
                {
                    int j3 = 150 - this.anIntArray982[k];
                    if(j3 < 50)
                        i3 = 0xff0000 + 1280 * j3;
                    else
                    if(j3 < 100)
                        i3 = 0xffff00 - 0x50000 * (j3 - 50);
                    else
                    if(j3 < 150)
                        i3 = 65280 + 5 * (j3 - 100);
                }
                if(this.anIntArray980[k] == 10)
                {
                    int k3 = 150 - this.anIntArray982[k];
                    if(k3 < 50)
                        i3 = 0xff0000 + 5 * k3;
                    else
                    if(k3 < 100)
                        i3 = 0xff00ff - 0x50000 * (k3 - 50);
                    else
                    if(k3 < 150)
                        i3 = (255 + 0x50000 * (k3 - 100)) - 5 * (k3 - 100);
                }
                if(this.anIntArray980[k] == 11)
                {
                    int l3 = 150 - this.anIntArray982[k];
                    if(l3 < 50)
                        i3 = 0xffffff - 0x50005 * l3;
                    else
                    if(l3 < 100)
                        i3 = 65280 + 0x50005 * (l3 - 50);
                    else
                    if(l3 < 150)
                        i3 = 0xffffff - 0x50000 * (l3 - 100);
                }
                if(this.anIntArray981[k] == 0)
                {
                    this.chatTextDrawingArea.drawText(0, s, this.spriteDrawY + 1, this.spriteDrawX);
                    this.chatTextDrawingArea.drawText(i3, s, this.spriteDrawY, this.spriteDrawX);
                }
                if(this.anIntArray981[k] == 1)
                {
                    this.chatTextDrawingArea.method386(0, s, this.spriteDrawX, this.anInt1265, this.spriteDrawY + 1);
                    this.chatTextDrawingArea.method386(i3, s, this.spriteDrawX, this.anInt1265, this.spriteDrawY);
                }
                if(this.anIntArray981[k] == 2)
                {
                    this.chatTextDrawingArea.method387(this.spriteDrawX, s, this.anInt1265, this.spriteDrawY + 1, 0);
                    this.chatTextDrawingArea.method387(this.spriteDrawX, s, this.anInt1265, this.spriteDrawY, i3);
                }
                if(this.anIntArray981[k] == 3)
                {
                    this.chatTextDrawingArea.method388(150 - this.anIntArray982[k], s, this.anInt1265, this.spriteDrawY + 1, this.spriteDrawX, 0);
                    this.chatTextDrawingArea.method388(150 - this.anIntArray982[k], s, this.anInt1265, this.spriteDrawY, this.spriteDrawX, i3);
                }
                if(this.anIntArray981[k] == 4)
                {
                    int i4 = this.chatTextDrawingArea.method384(s);
                    int k4 = ((150 - this.anIntArray982[k]) * (i4 + 100)) / 150;
                    DrawingArea.setDrawingArea(334, this.spriteDrawX - 50, this.spriteDrawX + 50, 0);
                    this.chatTextDrawingArea.method385(0, s, this.spriteDrawY + 1, (this.spriteDrawX + 50) - k4);
                    this.chatTextDrawingArea.method385(i3, s, this.spriteDrawY, (this.spriteDrawX + 50) - k4);
                    DrawingArea.defaultDrawingAreaSize();
                }
                if(this.anIntArray981[k] == 5)
                {
                    int j4 = 150 - this.anIntArray982[k];
                    int l4 = 0;
                    if(j4 < 25)
                        l4 = j4 - 25;
                    else
                    if(j4 > 125)
                        l4 = j4 - 125;
                    DrawingArea.setDrawingArea(this.spriteDrawY + 5, 0, 512, this.spriteDrawY - this.chatTextDrawingArea.anInt1497 - 1);
                    this.chatTextDrawingArea.drawText(0, s, this.spriteDrawY + 1 + l4, this.spriteDrawX);
                    this.chatTextDrawingArea.drawText(i3, s, this.spriteDrawY + l4, this.spriteDrawX);
                    DrawingArea.defaultDrawingAreaSize();
                }
            } else
            {
                this.chatTextDrawingArea.drawText(0, s, this.spriteDrawY + 1, this.spriteDrawX);
                this.chatTextDrawingArea.drawText(0xffff00, s, this.spriteDrawY, this.spriteDrawX);
            }
        }
    }catch(Exception e){ }

    }

    private void delFriend(long l)
    {
        try
        {
            if(l == 0L)
                return;
            for(int i = 0; i < this.friendsCount; i++)
            {
                if(this.friendsListAsLongs[i] != l)
                    continue;
                this.friendsCount--;
                this.needDrawTabArea = true;
                for(int j = i; j < this.friendsCount; j++)
                {
                    this.friendsList[j] = this.friendsList[j + 1];
                    this.friendsNodeIDs[j] = this.friendsNodeIDs[j + 1];
                    this.friendsListAsLongs[j] = this.friendsListAsLongs[j + 1];
                }

                this.stream.writeOpcode(215);
                this.stream.writeLong(l);
                break;
            }
        }
        catch(RuntimeException runtimeexception)
        {
            Utils.reporterror("18622, " + false + ", " + l + ", " + runtimeexception.toString());
            throw new RuntimeException();
        }
    }

    private void drawTabArea()
    {
        this.aRSImageProducer_1163.initDrawingArea();
        Texture.anIntArray1472 = this.anIntArray1181;
        IndexedImageRenderer.draw(this.invBack, 0, 0);
        if(this.invOverlayInterfaceID != -1)
            this.drawInterface(0, 0, RSInterface.interfaceCache[this.invOverlayInterfaceID], 0);
        else
        if(this.tabInterfaceIDs[this.tabID] != -1)
            this.drawInterface(0, 0, RSInterface.interfaceCache[this.tabInterfaceIDs[this.tabID]], 0);
        if(this.menuOpen && this.menuScreenArea == 1)
            this.drawMenu(RSBase.frameMode == ScreenMode.FIXED ? 516 : 0, RSBase.frameMode == ScreenMode.FIXED ? 168 : 0);
        this.aRSImageProducer_1163.drawGraphics(205, this.applet.graphics, 553);
        this.aRSImageProducer_1165.initDrawingArea();
        Texture.anIntArray1472 = this.anIntArray1182;
    }

    private void method37(int j)
    {
        if(!Client.lowMem)
        {
            if(Texture.anIntArray1480[17] >= j)
            {
                IndexedImage indexedImage = Texture.aBackgroundArray1474s[17];
                int k = indexedImage.width * indexedImage.height - 1;
                int j1 = indexedImage.width * this.anInt945 * 2;
                byte abyte0[] = indexedImage.raster;
                byte abyte3[] = this.aByteArray912;
                for(int i2 = 0; i2 <= k; i2++)
                    abyte3[i2] = abyte0[i2 - j1 & k];

                indexedImage.raster = abyte3;
                this.aByteArray912 = abyte0;
                Texture.method370(17);
                Client.anInt854++;
                if(Client.anInt854 > 1235)
                {
                    Client.anInt854 = 0;
                    this.stream.writeOpcode(226);
                    this.stream.writeByte(0);
                    int l2 = this.stream.position;
                    this.stream.writeShort(58722);
                    this.stream.writeByte(240);
                    this.stream.writeShort((int)(Math.random() * 65536D));
                    this.stream.writeByte((int)(Math.random() * 256D));
                    if((int)(Math.random() * 2D) == 0)
                        this.stream.writeShort(51825);
                    this.stream.writeByte((int)(Math.random() * 256D));
                    this.stream.writeShort((int)(Math.random() * 65536D));
                    this.stream.writeShort(7130);
                    this.stream.writeShort((int)(Math.random() * 65536D));
                    this.stream.writeShort(61657);
                    this.stream.writeSizeByte(this.stream.position - l2);
                }
            }
            if(Texture.anIntArray1480[24] >= j)
            {
                IndexedImage background_1 = Texture.aBackgroundArray1474s[24];
                int l = background_1.width * background_1.height - 1;
                int k1 = background_1.width * this.anInt945 * 2;
                byte abyte1[] = background_1.raster;
                byte abyte4[] = this.aByteArray912;
                for(int j2 = 0; j2 <= l; j2++)
                    abyte4[j2] = abyte1[j2 - k1 & l];

                background_1.raster = abyte4;
                this.aByteArray912 = abyte1;
                Texture.method370(24);
            }
            if(Texture.anIntArray1480[34] >= j)
            {
                IndexedImage background_2 = Texture.aBackgroundArray1474s[34];
                int i1 = background_2.width * background_2.height - 1;
                int l1 = background_2.width * this.anInt945 * 2;
                byte abyte2[] = background_2.raster;
                byte abyte5[] = this.aByteArray912;
                for(int k2 = 0; k2 <= i1; k2++)
                    abyte5[k2] = abyte2[k2 - l1 & i1];

                background_2.raster = abyte5;
                this.aByteArray912 = abyte2;
                Texture.method370(34);
            }
        }
    }

    private void method38()
    {
        for(int i = -1; i < this.playerCount; i++)
        {
            int j;
            if(i == -1)
                j = this.myPlayerIndex;
            else
                j = this.playerIndices[i];
            Player player = this.playerArray[j];
            if(player != null && player.textCycle > 0)
            {
                player.textCycle--;
                if(player.textCycle == 0)
                    player.textSpoken = null;
            }
        }

        for(int k = 0; k < this.npcCount; k++)
        {
            int l = this.npcIndices[k];
            NPC npc = this.npcArray[l];
            if(npc != null && npc.textCycle > 0)
            {
                npc.textCycle--;
                if(npc.textCycle == 0)
                    npc.textSpoken = null;
            }
        }

    }

    private void calcCameraPos()
    {
        int i = this.anInt1098 * 128 + 64;
        int j = this.anInt1099 * 128 + 64;
        int k = this.tileHeight(this.plane, j, i) - this.anInt1100;
        if(this.xCameraPos < i)
        {
            this.xCameraPos += this.anInt1101 + ((i - this.xCameraPos) * this.anInt1102) / 1000;
            if(this.xCameraPos > i)
                this.xCameraPos = i;
        }
        if(this.xCameraPos > i)
        {
            this.xCameraPos -= this.anInt1101 + ((this.xCameraPos - i) * this.anInt1102) / 1000;
            if(this.xCameraPos < i)
                this.xCameraPos = i;
        }
        if(this.zCameraPos < k)
        {
            this.zCameraPos += this.anInt1101 + ((k - this.zCameraPos) * this.anInt1102) / 1000;
            if(this.zCameraPos > k)
                this.zCameraPos = k;
        }
        if(this.zCameraPos > k)
        {
            this.zCameraPos -= this.anInt1101 + ((this.zCameraPos - k) * this.anInt1102) / 1000;
            if(this.zCameraPos < k)
                this.zCameraPos = k;
        }
        if(this.yCameraPos < j)
        {
            this.yCameraPos += this.anInt1101 + ((j - this.yCameraPos) * this.anInt1102) / 1000;
            if(this.yCameraPos > j)
                this.yCameraPos = j;
        }
        if(this.yCameraPos > j)
        {
            this.yCameraPos -= this.anInt1101 + ((this.yCameraPos - j) * this.anInt1102) / 1000;
            if(this.yCameraPos < j)
                this.yCameraPos = j;
        }
        i = this.anInt995 * 128 + 64;
        j = this.anInt996 * 128 + 64;
        k = this.tileHeight(this.plane, j, i) - this.anInt997;
        int l = i - this.xCameraPos;
        int i1 = k - this.zCameraPos;
        int j1 = j - this.yCameraPos;
        int k1 = (int)Math.sqrt(l * l + j1 * j1);
        int l1 = (int)(Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
        int i2 = (int)(Math.atan2(l, j1) * -325.94900000000001D) & 0x7ff;
        if(l1 < 128)
            l1 = 128;
        if(l1 > 383)
            l1 = 383;
        if(this.yCameraCurve < l1)
        {
            this.yCameraCurve += this.anInt998 + ((l1 - this.yCameraCurve) * this.anInt999) / 1000;
            if(this.yCameraCurve > l1)
                this.yCameraCurve = l1;
        }
        if(this.yCameraCurve > l1)
        {
            this.yCameraCurve -= this.anInt998 + ((this.yCameraCurve - l1) * this.anInt999) / 1000;
            if(this.yCameraCurve < l1)
                this.yCameraCurve = l1;
        }
        int j2 = i2 - this.xCameraCurve;
        if(j2 > 1024)
            j2 -= 2048;
        if(j2 < -1024)
            j2 += 2048;
        if(j2 > 0)
        {
            this.xCameraCurve += this.anInt998 + (j2 * this.anInt999) / 1000;
            this.xCameraCurve &= 0x7ff;
        }
        if(j2 < 0)
        {
            this.xCameraCurve -= this.anInt998 + (-j2 * this.anInt999) / 1000;
            this.xCameraCurve &= 0x7ff;
        }
        int k2 = i2 - this.xCameraCurve;
        if(k2 > 1024)
            k2 -= 2048;
        if(k2 < -1024)
            k2 += 2048;
        if(k2 < 0 && j2 > 0 || k2 > 0 && j2 < 0)
            this.xCameraCurve = i2;
    }

    private void drawMenu2(int x, int y)
    {
        int i = this.menuOffsetX;
        int j = this.menuOffsetY;
        int k = this.menuWidth;
        int l = this.anInt952;
        int i1 = 0x5d5447;
        DrawingArea.drawPixels(l, j, i, i1, k);
        DrawingArea.drawPixels(16, j + 1, i + 1, 0, k - 2);
        DrawingArea.fillPixels(i + 1, k - 2, l - 19, 0, j + 18);
        this.chatTextDrawingArea.method385(i1, "Choose Option", j + 14, i + 3);
        int j1 = this.applet.mouseX;
        int k1 = this.applet.mouseY;
        if(this.menuScreenArea == 0)
        {
            j1 -= 4;
            k1 -= 4;
        }
        if(this.menuScreenArea == 1)
        {
            j1 -= 553;
            k1 -= 205;
        }
        if(this.menuScreenArea == 2)
        {
            j1 -= 17;
            k1 -= 357;
        }
        for(int l1 = 0; l1 < this.menuActionRow; l1++)
        {
            int i2 = j + 31 + (this.menuActionRow - 1 - l1) * 15;
            int j2 = 0xffffff;
            if(j1 > i && j1 < i + k && k1 > i2 - 13 && k1 < i2 + 3)
                j2 = 0xffff00;
            this.chatTextDrawingArea.method389(true, i + 3, j2, this.menuActionName[l1], i2);
        }

    }

	public void drawMenu(int x, int y) {
		int xPos = this.menuOffsetX - (x - 4);
		int yPos = (-y + 4) + this.menuOffsetY;
		int w = this.menuWidth;
		int h = this.anInt952 + 1;
		this.inputTaken = true;
		this.tabAreaAltered = true;
		int menuColor = 0x5d5447;
		DrawingArea.drawPixels(h, yPos, xPos, menuColor, w);
		DrawingArea.drawPixels(16, yPos + 1, xPos + 1, 0, w - 2);
		DrawingArea.fillPixels(xPos + 1, w - 2, h - 19, 0, yPos + 18);
		this.chatTextDrawingArea.method385(menuColor, "Choose Option", yPos + 14, xPos + 3);
		int mouseX = this.applet.mouseX - (x);
		int mouseY = (-y) + this.applet.mouseY;
		for (int i = 0; i < this.menuActionRow; i++) {
			int textY = yPos + 31 + (this.menuActionRow - 1 - i) * 15;
			int textColor = 0xffffff;
			if (mouseX > xPos && mouseX < xPos + w && mouseY > textY - 13 && mouseY < textY + 3) {
				DrawingArea.drawPixels(15, textY - 11, xPos + 3, 0x6f695d, this.menuWidth - 6);
				textColor = 0xffff00;
			}
			this.chatTextDrawingArea.method389(true, xPos + 3, textColor, this.menuActionName[i], textY);
		}
	}
	
    private void addFriend(long l)
    {
        try
        {
            if(l == 0L)
                return;
            if(this.friendsCount >= 100 && this.anInt1046 != 1)
            {
                this.pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
                return;
            }
            if(this.friendsCount >= 200)
            {
                this.pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
                return;
            }
            String s = TextClass.fixName(TextClass.nameForLong(l));
            for(int i = 0; i < this.friendsCount; i++)
                if(this.friendsListAsLongs[i] == l)
                {
                    this.pushMessage(s + " is already on your friend list", 0, "");
                    return;
                }
            for(int j = 0; j < this.ignoreCount; j++)
                if(this.ignoreListAsLongs[j] == l)
                {
                    this.pushMessage("Please remove " + s + " from your ignore list first", 0, "");
                    return;
                }

            if(s.equals(Client.localPlayer.name))
            {
                return;
            } else
            {
                this.friendsList[this.friendsCount] = s;
                this.friendsListAsLongs[this.friendsCount] = l;
                this.friendsNodeIDs[this.friendsCount] = 0;
                this.friendsCount++;
                this.needDrawTabArea = true;
                this.stream.writeOpcode(188);
                this.stream.writeLong(l);
                return;
            }
        }
        catch(RuntimeException runtimeexception)
        {
            Utils.reporterror("15283, " + (byte)68 + ", " + l + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    private int tileHeight(int i, int j, int k)
    {
        int l = k >> 7;
        int i1 = j >> 7;
        if(l < 0 || i1 < 0 || l > 103 || i1 > 103)
            return 0;
        int j1 = i;
        if(j1 < 3 && (this.byteGroundArray[1][l][i1] & 2) == 2)
            j1++;
        int k1 = k & 0x7f;
        int l1 = j & 0x7f;
        int i2 = this.intGroundArray[j1][l][i1] * (128 - k1) + this.intGroundArray[j1][l + 1][i1] * k1 >> 7;
        int j2 = this.intGroundArray[j1][l][i1 + 1] * (128 - k1) + this.intGroundArray[j1][l + 1][i1 + 1] * k1 >> 7;
        return i2 * (128 - l1) + j2 * l1 >> 7;
    }

    private static String intToKOrMil(int j)
    {
        if(j < 0x186a0)
            return String.valueOf(j);
        if(j < 0x989680)
            return j / 1000 + "K";
        else
            return j / 0xf4240 + "M";
    }

    private void resetLogout()
    {
        try
        {
            if(this.socketStream != null)
                this.socketStream.close();
        }
        catch(Exception _ex) { }
        this.socketStream = null;
        this.loggedIn = false;
        this.unlinkMRUNodes();
        this.worldController.initToNull();
        for(int i = 0; i < 4; i++)
            this.aClass11Array1230[i].reset();

        System.gc();
        this.stopMidi();
        this.currentSong = -1;
        this.nextSong = -1;
        this.prevSong = 0;
    }

    private void method45()
    {
        this.aBoolean1031 = true;
        for(int j = 0; j < 7; j++)
        {
            this.anIntArray1065[j] = -1;
            for(int k = 0; k < IDK.length; k++)
            {
                if(IDK.cache[k].validStyle || IDK.cache[k].part != j + (this.aBoolean1047 ? 0 : 7))
                    continue;
                this.anIntArray1065[j] = k;
                break;
            }

        }

    }

    private void method46(int i, BitBuffer buffer)
    {
        while(buffer.bitPosition + 21 < i * 8)
        {
            int k = buffer.readBits(14);
            if(k == 16383)
                break;
            if(this.npcArray[k] == null)
                this.npcArray[k] = new NPC();
            NPC npc = this.npcArray[k];
            this.npcIndices[this.npcCount++] = k;
            npc.anInt1537 = Client.loopCycle;
            int l = buffer.readBits(5);
            if(l > 15)
                l -= 32;
            int i1 = buffer.readBits(5);
            if(i1 > 15)
                i1 -= 32;
            int j1 = buffer.readBits(1);
            npc.desc = EntityDef.forID(buffer.readBits(12));
            int k1 = buffer.readBits(1);
            if(k1 == 1)
                this.anIntArray894[this.anInt893++] = k;
            npc.anInt1540 = npc.desc.aByte68;
            npc.anInt1504 = npc.desc.anInt79;
            npc.anInt1554 = npc.desc.anInt67;
            npc.anInt1555 = npc.desc.anInt58;
            npc.anInt1556 = npc.desc.anInt83;
            npc.anInt1557 = npc.desc.anInt55;
            npc.anInt1511 = npc.desc.anInt77;
            npc.setPos(Client.localPlayer.smallX[0] + i1, Client.localPlayer.smallY[0] + l, j1 == 1);
        }
        buffer.disableBitAccess();
    }

	@Override
	public void processGameLoop()
    {
        if(this.rsAlreadyLoaded || this.loadingError || this.genericLoadingError)
            return;
        Client.loopCycle++;
        if(!this.loggedIn)
            this.processLoginScreenInput(this.applet.saveClickX, this.applet.saveClickY, this.applet.clickMode3);
        else
            this.mainGameProcessor();
        this.processOnDemandQueue();
    }

    private void method47(boolean flag)
    {
        if(Client.localPlayer.x >> 7 == this.destX && Client.localPlayer.y >> 7 == this.destY)
            this.destX = 0;
        int j = this.playerCount;
        if(flag)
            j = 1;
        for(int l = 0; l < j; l++)
        {
            Player player;
            int i1;
            if(flag)
            {
                player = Client.localPlayer;
                i1 = this.myPlayerIndex << 14;
            } else
            {
                player = this.playerArray[this.playerIndices[l]];
                i1 = this.playerIndices[l] << 14;
            }
            if(player == null || !player.isVisible())
                continue;
            player.aBoolean1699 = (Client.lowMem && this.playerCount > 50 || this.playerCount > 200) && !flag && player.anInt1517 == player.anInt1511;
            int j1 = player.x >> 7;
            int k1 = player.y >> 7;
            if(j1 < 0 || j1 >= 104 || k1 < 0 || k1 >= 104)
                continue;
            if(player.aModel_1714 != null && Client.loopCycle >= player.anInt1707 && Client.loopCycle < player.anInt1708)
            {
                player.aBoolean1699 = false;
                player.anInt1709 = this.tileHeight(this.plane, player.y, player.x);
                this.worldController.method286(this.plane, player.y, player, player.anInt1552, player.anInt1722, player.x, player.anInt1709, player.anInt1719, player.anInt1721, i1, player.anInt1720);
                continue;
            }
            if((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64)
            {
                if(this.anIntArrayArray929[j1][k1] == this.anInt1265)
                    continue;
                this.anIntArrayArray929[j1][k1] = this.anInt1265;
            }
            player.anInt1709 = this.tileHeight(this.plane, player.y, player.x);
            this.worldController.method285(this.plane, player.anInt1552, player.anInt1709, i1, player.y, 60, player.x, player, player.aBoolean1541);
        }

    }

    private boolean promptUserForInput(RSInterface class9)
    {
        int j = class9.anInt214;
        if(this.anInt900 == 2)
        {
            if(j == 201)
            {
                this.inputTaken = true;
                this.inputDialogState = 0;
                this.messagePromptRaised = true;
                this.promptInput = "";
                this.friendsListAction = 1;
                this.aString1121 = "Enter name of friend to add to list";
            }
            if(j == 202)
            {
                this.inputTaken = true;
                this.inputDialogState = 0;
                this.messagePromptRaised = true;
                this.promptInput = "";
                this.friendsListAction = 2;
                this.aString1121 = "Enter name of friend to delete from list";
            }
        }
        if(j == 205)
        {
            this.anInt1011 = 250;
            return true;
        }
        if(j == 501)
        {
            this.inputTaken = true;
            this.inputDialogState = 0;
            this.messagePromptRaised = true;
            this.promptInput = "";
            this.friendsListAction = 4;
            this.aString1121 = "Enter name of player to add to list";
        }
        if(j == 502)
        {
            this.inputTaken = true;
            this.inputDialogState = 0;
            this.messagePromptRaised = true;
            this.promptInput = "";
            this.friendsListAction = 5;
            this.aString1121 = "Enter name of player to delete from list";
        }
        if(j >= 300 && j <= 313)
        {
            int k = (j - 300) / 2;
            int j1 = j & 1;
            int i2 = this.anIntArray1065[k];
            if(i2 != -1)
            {
                do
                {
                    if(j1 == 0 && --i2 < 0)
                        i2 = IDK.length - 1;
                    if(j1 == 1 && ++i2 >= IDK.length)
                        i2 = 0;
                } while(IDK.cache[i2].validStyle || IDK.cache[i2].part != k + (this.aBoolean1047 ? 0 : 7));
                this.anIntArray1065[k] = i2;
                this.aBoolean1031 = true;
            }
        }
        if(j >= 314 && j <= 323)
        {
            int l = (j - 314) / 2;
            int k1 = j & 1;
            int j2 = this.anIntArray990[l];
            if(k1 == 0 && --j2 < 0)
                j2 = Client.anIntArrayArray1003[l].length - 1;
            if(k1 == 1 && ++j2 >= Client.anIntArrayArray1003[l].length)
                j2 = 0;
            this.anIntArray990[l] = j2;
            this.aBoolean1031 = true;
        }
        if(j == 324 && !this.aBoolean1047)
        {
            this.aBoolean1047 = true;
            this.method45();
        }
        if(j == 325 && this.aBoolean1047)
        {
            this.aBoolean1047 = false;
            this.method45();
        }
        if(j == 326)
        {
            this.stream.writeOpcode(101);
            this.stream.writeByte(this.aBoolean1047 ? 0 : 1);
            for(int i1 = 0; i1 < 7; i1++)
                this.stream.writeByte(this.anIntArray1065[i1]);

            for(int l1 = 0; l1 < 5; l1++)
                this.stream.writeByte(this.anIntArray990[l1]);

            return true;
        }
        if(j == 613)
            this.canMute = !this.canMute;
        if(j >= 601 && j <= 612)
        {
            this.clearTopInterfaces();
            if(this.reportAbuseInput.length() > 0)
            {
                this.stream.writeOpcode(218);
                this.stream.writeLong(TextClass.longForName(this.reportAbuseInput));
                this.stream.writeByte(j - 601);
                this.stream.writeByte(this.canMute ? 1 : 0);
            }
        }
        return false;
    }

    private void method49(BitBuffer buffer)
    {
        for(int j = 0; j < this.anInt893; j++)
        {
            int k = this.anIntArray894[j];
            Player player = this.playerArray[k];
            int l = buffer.readUByte();
            if((l & 0x40) != 0)
                l += buffer.readUByte() << 8;
            this.method107(l, k, buffer, player);
        }

    }

    private void method50(int i, int k, int l, int i1, int j1)
    {
        int k1 = this.worldController.method300(j1, l, i);
        if(k1 != 0)
        {
            int l1 = this.worldController.method304(j1, l, i, k1);
            int k2 = l1 >> 6 & 3;
            int i3 = l1 & 0x1f;
            int k3 = k;
            if(k1 > 0)
                k3 = i1;
            int ai[] = this.minimapImage.raster;
            int k4 = 24624 + l * 4 + (103 - i) * 512 * 4;
            int i5 = k1 >> 14 & 0x7fff;
            ObjectDef class46_2 = ObjectDef.forID(i5);
            if(class46_2.anInt758 != -1)
            {
                IndexedImage background_2 = this.mapScenes[class46_2.anInt758];
                if(background_2 != null)
                {
                    int i6 = (class46_2.anInt744 * 4 - background_2.width) / 2;
                    int j6 = (class46_2.anInt761 * 4 - background_2.height) / 2;
                    IndexedImageRenderer.draw(background_2, 48 + l * 4 + i6, 48 + (104 - i - class46_2.anInt761) * 4 + j6);
                }
            } else
            {
                if(i3 == 0 || i3 == 2)
                    if(k2 == 0)
                    {
                        ai[k4] = k3;
                        ai[k4 + 512] = k3;
                        ai[k4 + 1024] = k3;
                        ai[k4 + 1536] = k3;
                    } else
                    if(k2 == 1)
                    {
                        ai[k4] = k3;
                        ai[k4 + 1] = k3;
                        ai[k4 + 2] = k3;
                        ai[k4 + 3] = k3;
                    } else
                    if(k2 == 2)
                    {
                        ai[k4 + 3] = k3;
                        ai[k4 + 3 + 512] = k3;
                        ai[k4 + 3 + 1024] = k3;
                        ai[k4 + 3 + 1536] = k3;
                    } else
                    if(k2 == 3)
                    {
                        ai[k4 + 1536] = k3;
                        ai[k4 + 1536 + 1] = k3;
                        ai[k4 + 1536 + 2] = k3;
                        ai[k4 + 1536 + 3] = k3;
                    }
                if(i3 == 3)
                    if(k2 == 0)
                        ai[k4] = k3;
                    else
                    if(k2 == 1)
                        ai[k4 + 3] = k3;
                    else
                    if(k2 == 2)
                        ai[k4 + 3 + 1536] = k3;
                    else
                    if(k2 == 3)
                        ai[k4 + 1536] = k3;
                if(i3 == 2)
                    if(k2 == 3)
                    {
                        ai[k4] = k3;
                        ai[k4 + 512] = k3;
                        ai[k4 + 1024] = k3;
                        ai[k4 + 1536] = k3;
                    } else
                    if(k2 == 0)
                    {
                        ai[k4] = k3;
                        ai[k4 + 1] = k3;
                        ai[k4 + 2] = k3;
                        ai[k4 + 3] = k3;
                    } else
                    if(k2 == 1)
                    {
                        ai[k4 + 3] = k3;
                        ai[k4 + 3 + 512] = k3;
                        ai[k4 + 3 + 1024] = k3;
                        ai[k4 + 3 + 1536] = k3;
                    } else
                    if(k2 == 2)
                    {
                        ai[k4 + 1536] = k3;
                        ai[k4 + 1536 + 1] = k3;
                        ai[k4 + 1536 + 2] = k3;
                        ai[k4 + 1536 + 3] = k3;
                    }
            }
        }
        k1 = this.worldController.method302(j1, l, i);
        if(k1 != 0)
        {
            int i2 = this.worldController.method304(j1, l, i, k1);
            int l2 = i2 >> 6 & 3;
            int j3 = i2 & 0x1f;
            int l3 = k1 >> 14 & 0x7fff;
            ObjectDef class46_1 = ObjectDef.forID(l3);
            if(class46_1.anInt758 != -1)
            {
                IndexedImage background_1 = this.mapScenes[class46_1.anInt758];
                if(background_1 != null)
                {
                    int j5 = (class46_1.anInt744 * 4 - background_1.width) / 2;
                    int k5 = (class46_1.anInt761 * 4 - background_1.height) / 2;
                    IndexedImageRenderer.draw(background_1, 48 + l * 4 + j5, 48 + (104 - i - class46_1.anInt761) * 4 + k5);
                }
            } else
            if(j3 == 9)
            {
                int l4 = 0xeeeeee;
                if(k1 > 0)
                    l4 = 0xee0000;
                int ai1[] = this.minimapImage.raster;
                int l5 = 24624 + l * 4 + (103 - i) * 512 * 4;
                if(l2 == 0 || l2 == 2)
                {
                    ai1[l5 + 1536] = l4;
                    ai1[l5 + 1024 + 1] = l4;
                    ai1[l5 + 512 + 2] = l4;
                    ai1[l5 + 3] = l4;
                } else
                {
                    ai1[l5] = l4;
                    ai1[l5 + 512 + 1] = l4;
                    ai1[l5 + 1024 + 2] = l4;
                    ai1[l5 + 1536 + 3] = l4;
                }
            }
        }
        k1 = this.worldController.method303(j1, l, i);
        if(k1 != 0)
        {
            int j2 = k1 >> 14 & 0x7fff;
            ObjectDef class46 = ObjectDef.forID(j2);
            if(class46.anInt758 != -1)
            {
                IndexedImage indexedImage = this.mapScenes[class46.anInt758];
                if(indexedImage != null)
                {
                    int i4 = (class46.anInt744 * 4 - indexedImage.width) / 2;
                    int j4 = (class46.anInt761 * 4 - indexedImage.height) / 2;
                    IndexedImageRenderer.draw(indexedImage, 48 + l * 4 + i4, 48 + (104 - i - class46.anInt761) * 4 + j4);
                }
            }
        }
    }

    public static void main(String args[])
    {
    	args = new String[] { "0","0","highmem","members","32"};
        try
        {
            System.out.println("RS2 user client - release #" + 317);
            if(args.length != 5)
            {
                System.out.println("Usage: node-id, port-offset, [lowmem/highmem], [free/members]");
                return;
            }
            Client.nodeID = Integer.parseInt(args[0]);
            RSBase.portOff = Integer.parseInt(args[1]);
            if(args[2].equals("lowmem"))
                Client.setLowMem();
            else
            if(args[2].equals("highmem"))
            {
                Client.setHighMem();
            } else
            {
                System.out.println("Usage: node-id, port-offset, [lowmem/highmem], [free/members]");
                return;
            }
            if(args[3].equals("free"))
                Client.isMembers = false;
            else
            if(args[3].equals("members"))
            {
                Client.isMembers = true;
            } else
            {
                System.out.println("Usage: node-id, port-offset, [lowmem/highmem], [free/members]");
                return;
            }
            signlink.startpriv();
            Client client1 = new Client();
            client1.applet.initClientFrame(503, 765, false);
            client1.frameMode(ScreenMode.RESIZABLE);
        }
        catch(Exception exception)
        {
        }
    }

    private void loadingStages()
    {
        if(Client.lowMem && this.loadingStage == 2 && ObjectManager.visibleLevels != this.plane)
        {
            this.aRSImageProducer_1165.initDrawingArea();
            this.aTextDrawingArea_1271.drawText(0, "Loading - please wait.", 151, 257);
            this.aTextDrawingArea_1271.drawText(0xffffff, "Loading - please wait.", 150, 256);
            this.aRSImageProducer_1165.drawGraphics(4, this.applet.graphics, 4);
            this.loadingStage = 1;
            this.aLong824 = System.currentTimeMillis();
        }
        if(this.loadingStage == 1)
        {
            int j = this.method54();
            if(j != 0 && System.currentTimeMillis() - this.aLong824 > 0x57e40L)
            {
                Utils.reporterror(this.myUsername + " glcfb " + this.aLong1215 + "," + j + "," + Client.lowMem + "," + this.indexs[0] + "," + this.onDemandFetcher.getNodeCount() + "," + this.plane + "," + this.anInt1069 + "," + this.anInt1070);
                this.aLong824 = System.currentTimeMillis();
            }
        }
        if(this.loadingStage == 2 && this.plane != this.anInt985)
        {
            this.anInt985 = this.plane;
            this.method24(this.plane);
        }
    }

    private int method54()
    {
        for(int i = 0; i < this.aByteArrayArray1183.length; i++)
        {
            if(this.aByteArrayArray1183[i] == null && this.anIntArray1235[i] != -1)
                return -1;
            if(this.aByteArrayArray1247[i] == null && this.anIntArray1236[i] != -1)
                return -2;
        }

        boolean flag = true;
        for(int j = 0; j < this.aByteArrayArray1183.length; j++)
        {
            byte abyte0[] = this.aByteArrayArray1247[j];
            if(abyte0 != null)
            {
                int k = (this.anIntArray1234[j] >> 8) * 64 - this.baseX;
                int l = (this.anIntArray1234[j] & 0xff) * 64 - this.baseY;
                if(this.aBoolean1159)
                {
                    k = 10;
                    l = 10;
                }
                flag &= ObjectManager.method189(k, abyte0, l);
            }
        }

        if(!flag)
            return -3;
        if(this.aBoolean1080)
        {
            return -4;
        } else
        {
            this.loadingStage = 2;
            ObjectManager.visibleLevels = this.plane;
            this.method22();
            this.stream.writeOpcode(121);
            return 0;
        }
    }

    private void updateSceneProjectiles()
    {
        for(Projectile p = (Projectile)this.projectiles.getFront(); p != null; p = (Projectile)this.projectiles.getNext())
            if(p.level != this.plane || Client.loopCycle > p.startTick)
                p.unlink();
            else
            if(Client.loopCycle >= p.endTick)
            {
                if(p.target > 0)
                {
                    NPC npc = this.npcArray[p.target - 1];
                    if(npc != null && npc.x >= 0 && npc.x < 13312 && npc.y >= 0 && npc.y < 13312)
                        p.target(Client.loopCycle, npc.y, this.tileHeight(p.level, npc.y, npc.x) - p.destinationElevation, npc.x);
                }
                if(p.target < 0)
                {
                    int index = -p.target - 1;
                    Player player;
                    if(index == this.localPlayerIndex)
                        player = Client.localPlayer;
                    else
                        player = this.playerArray[index];
                    if(player != null && player.x >= 0 && player.x < 13312 && player.y >= 0 && player.y < 13312)
                        p.target(Client.loopCycle, player.y, this.tileHeight(p.level, player.y, player.x) - p.destinationElevation, player.x);
                }
                p.update(this.anInt945);
                this.worldController.method285(this.plane, p.yaw, (int)p.z, -1, (int)p.y, 60, (int)p.x, p, false);
            }

    }

    private void processOnDemandQueue()
    {
        do
        {
            OnDemandData onDemandData;
            do
            {
                onDemandData = this.onDemandFetcher.getNextNode();
                if(onDemandData == null)
                    return;
                if(onDemandData.dataType == 0)
                {
                    Model.method460(onDemandData.buffer, onDemandData.ID);
                    if((this.onDemandFetcher.getModelIndex(onDemandData.ID) & 0x62) != 0)
                    {
                        this.needDrawTabArea = true;
                        if(this.backDialogID != -1)
                            this.inputTaken = true;
                    }
                }
                if(onDemandData.dataType == 1 && onDemandData.buffer != null)
                    Frame.load(onDemandData.buffer);
                if(onDemandData.dataType == 2 && onDemandData.ID == this.nextSong && onDemandData.buffer != null)
                    this.saveMidi(this.songChanging, onDemandData.buffer);
                if(onDemandData.dataType == 3 && this.loadingStage == 1)
                {
                    for(int i = 0; i < this.aByteArrayArray1183.length; i++)
                    {
                        if(this.anIntArray1235[i] == onDemandData.ID)
                        {
                            this.aByteArrayArray1183[i] = onDemandData.buffer;
                            if(onDemandData.buffer == null)
                                this.anIntArray1235[i] = -1;
                            break;
                        }
                        if(this.anIntArray1236[i] != onDemandData.ID)
                            continue;
                        this.aByteArrayArray1247[i] = onDemandData.buffer;
                        if(onDemandData.buffer == null)
                            this.anIntArray1236[i] = -1;
                        break;
                    }

                }
            } while(onDemandData.dataType != 93 || !this.onDemandFetcher.method564(onDemandData.ID));
            ObjectManager.method173(new Buffer(onDemandData.buffer), this.onDemandFetcher);
        } while(true);
    }

    private boolean saveWave(byte abyte0[], int i)
    {
        return abyte0 == null || signlink.wavesave(abyte0, i);
    }

    private void method60(int i)
    {
        RSInterface class9 = RSInterface.interfaceCache[i];
        for(int j = 0; j < class9.children.length; j++)
        {
            if(class9.children[j] == -1)
                break;
            RSInterface class9_1 = RSInterface.interfaceCache[class9.children[j]];
            if(class9_1.type == 1)
                this.method60(class9_1.id);
            class9_1.anInt246 = 0;
            class9_1.anInt208 = 0;
        }
    }

    private void drawHeadIcon()
    {
        if(this.anInt855 != 2)
            return;
        this.calcEntityScreenPos((this.anInt934 - this.baseX << 7) + this.anInt937, this.anInt936 * 2, (this.anInt935 - this.baseY << 7) + this.anInt938);
        if(this.spriteDrawX > -1 && Client.loopCycle % 20 < 10)
            SpriteRenderer.drawSprite(this.headIcons[2], this.spriteDrawX - 12, this.spriteDrawY - 28);
    }

    private void mainGameProcessor()
    {
    	this.refreshFrameSize(true);
        if(this.anInt1104 > 1)
            this.anInt1104--;
        if(this.anInt1011 > 0)
            this.anInt1011--;
        for(int j = 0; j < 5; j++)
            if(!this.parsePacket())
                break;

        if(!this.loggedIn)
            return;
        synchronized(this.mouseDetection.syncObject)
        {
            if(Client.flagged)
            {
                if(this.applet.clickMode3 != 0 || this.mouseDetection.coordsIndex >= 40)
                {
                    this.stream.writeOpcode(45);
                    this.stream.writeByte(0);
                    int j2 = this.stream.position;
                    int j3 = 0;
                    for(int j4 = 0; j4 < this.mouseDetection.coordsIndex; j4++)
                    {
                        if(j2 - this.stream.position >= 240)
                            break;
                        j3++;
                        int l4 = this.mouseDetection.coordsY[j4];
                        if(l4 < 0)
                            l4 = 0;
                        else
                        if(l4 > 502)
                            l4 = 502;
                        int k5 = this.mouseDetection.coordsX[j4];
                        if(k5 < 0)
                            k5 = 0;
                        else
                        if(k5 > 764)
                            k5 = 764;
                        int i6 = l4 * 765 + k5;
                        if(this.mouseDetection.coordsY[j4] == -1 && this.mouseDetection.coordsX[j4] == -1)
                        {
                            k5 = -1;
                            l4 = -1;
                            i6 = 0x7ffff;
                        }
                        if(k5 == this.anInt1237 && l4 == this.anInt1238)
                        {
                            if(this.anInt1022 < 2047)
                                this.anInt1022++;
                        } else
                        {
                            int j6 = k5 - this.anInt1237;
                            this.anInt1237 = k5;
                            int k6 = l4 - this.anInt1238;
                            this.anInt1238 = l4;
                            if(this.anInt1022 < 8 && j6 >= -32 && j6 <= 31 && k6 >= -32 && k6 <= 31)
                            {
                                j6 += 32;
                                k6 += 32;
                                this.stream.writeShort((this.anInt1022 << 12) + (j6 << 6) + k6);
                                this.anInt1022 = 0;
                            } else
                            if(this.anInt1022 < 8)
                            {
                                this.stream.writeTriByte(0x800000 + (this.anInt1022 << 19) + i6);
                                this.anInt1022 = 0;
                            } else
                            {
                                this.stream.writeInt(0xc0000000 + (this.anInt1022 << 19) + i6);
                                this.anInt1022 = 0;
                            }
                        }
                    }

                    this.stream.writeSizeByte(this.stream.position - j2);
                    if(j3 >= this.mouseDetection.coordsIndex)
                    {
                        this.mouseDetection.coordsIndex = 0;
                    } else
                    {
                        this.mouseDetection.coordsIndex -= j3;
                        for(int i5 = 0; i5 < this.mouseDetection.coordsIndex; i5++)
                        {
                            this.mouseDetection.coordsX[i5] = this.mouseDetection.coordsX[i5 + j3];
                            this.mouseDetection.coordsY[i5] = this.mouseDetection.coordsY[i5 + j3];
                        }

                    }
                }
            } else
            {
                this.mouseDetection.coordsIndex = 0;
            }
        }
        if(this.applet.clickMode3 != 0)
        {
            long l = (this.applet.aLong29 - this.aLong1220) / 50L;
            if(l > 4095L)
                l = 4095L;
            this.aLong1220 = this.applet.aLong29;
            int k2 = this.applet.saveClickY;
            if(k2 < 0)
                k2 = 0;
            else
            if(k2 > 502)
                k2 = 502;
            int k3 = this.applet.saveClickX;
            if(k3 < 0)
                k3 = 0;
            else
            if(k3 > 764)
                k3 = 764;
            int k4 = k2 * 765 + k3;
            int j5 = 0;
            if(this.applet.clickMode3 == 2)
                j5 = 1;
            int l5 = (int)l;
            this.stream.writeOpcode(241);
            this.stream.writeInt((l5 << 20) + (j5 << 19) + k4);
        }
        if(this.anInt1016 > 0)
            this.anInt1016--;
        if(this.applet.keyArray[1] == 1 || this.applet.keyArray[2] == 1 || this.applet.keyArray[3] == 1 || this.applet.keyArray[4] == 1)
            this.aBoolean1017 = true;
        if(this.aBoolean1017 && this.anInt1016 <= 0)
        {
            this.anInt1016 = 20;
            this.aBoolean1017 = false;
            this.stream.writeOpcode(86);
            this.stream.writeShort(this.anInt1184);
            this.stream.writeShortA(this.minimapInt1);
        }
        if(this.applet.awtFocus && !this.aBoolean954)
        {
            this.aBoolean954 = true;
            this.stream.writeOpcode(3);
            this.stream.writeByte(1);
        }
        if(!this.applet.awtFocus && this.aBoolean954)
        {
            this.aBoolean954 = false;
            this.stream.writeOpcode(3);
            this.stream.writeByte(0);
        }
        this.loadingStages();
        this.method115();
        this.method90();
        this.anInt1009++;
        if(this.anInt1009 > 750)
            this.dropClient();
        this.method114();
        this.method95();
        this.method38();
        this.anInt945++;
        if(this.crossType != 0)
        {
            this.crossIndex += 20;
            if(this.crossIndex >= 400)
                this.crossType = 0;
        }
        if(this.atInventoryInterfaceType != 0)
        {
            this.atInventoryLoopCycle++;
            if(this.atInventoryLoopCycle >= 15)
            {
                if(this.atInventoryInterfaceType == 2)
                    this.needDrawTabArea = true;
                if(this.atInventoryInterfaceType == 3)
                    this.inputTaken = true;
                this.atInventoryInterfaceType = 0;
            }
        }
        if(this.activeInterfaceType != 0)
        {
            this.anInt989++;
            if(this.applet.mouseX > this.anInt1087 + 5 || this.applet.mouseX < this.anInt1087 - 5 || this.applet.mouseY > this.anInt1088 + 5 || this.applet.mouseY < this.anInt1088 - 5)
                this.aBoolean1242 = true;
            if(this.applet.clickMode2 == 0)
            {
                if(this.activeInterfaceType == 2)
                    this.needDrawTabArea = true;
                if(this.activeInterfaceType == 3)
                    this.inputTaken = true;
                this.activeInterfaceType = 0;
                if(this.aBoolean1242 && this.anInt989 >= 5)
                {
                    this.lastActiveInvInterface = -1;
                    this.processRightClick();
                    if(this.lastActiveInvInterface == this.anInt1084 && this.mouseInvInterfaceIndex != this.anInt1085)
                    {
                        RSInterface class9 = RSInterface.interfaceCache[this.anInt1084];
                        int j1 = 0;
                        if(this.anInt913 == 1 && class9.anInt214 == 206)
                            j1 = 1;
                        if(class9.inv[this.mouseInvInterfaceIndex] <= 0)
                            j1 = 0;
                        if(class9.aBoolean235)
                        {
                            int l2 = this.anInt1085;
                            int l3 = this.mouseInvInterfaceIndex;
                            class9.inv[l3] = class9.inv[l2];
                            class9.invStackSizes[l3] = class9.invStackSizes[l2];
                            class9.inv[l2] = -1;
                            class9.invStackSizes[l2] = 0;
                        } else
                        if(j1 == 1)
                        {
                            int i3 = this.anInt1085;
                            for(int i4 = this.mouseInvInterfaceIndex; i3 != i4;)
                                if(i3 > i4)
                                {
                                    class9.swapInventoryItems(i3, i3 - 1);
                                    i3--;
                                } else
                                if(i3 < i4)
                                {
                                    class9.swapInventoryItems(i3, i3 + 1);
                                    i3++;
                                }

                        } else
                        {
                            class9.swapInventoryItems(this.anInt1085, this.mouseInvInterfaceIndex);
                        }
                        this.stream.writeOpcode(214);
                        this.stream.writeLEShortA(this.anInt1084);
                        this.stream.writeNegatedByte(j1);
                        this.stream.writeLEShortA(this.anInt1085);
                        this.stream.writeLEShort(this.mouseInvInterfaceIndex);
                    }
                } else
                if((this.anInt1253 == 1 || this.menuHasAddFriend(this.menuActionRow - 1)) && this.menuActionRow > 2)
                    this.determineMenuSize();
                else
                if(this.menuActionRow > 0)
                    this.doAction(this.menuActionRow - 1);
                this.atInventoryLoopCycle = 10;
                this.applet.clickMode3 = 0;
            }
        }
        if(WorldController.anInt470 != -1)
        {
            int k = WorldController.anInt470;
            int k1 = WorldController.anInt471;
            boolean flag = pathfinder.doWalkTo(0, this, 0, 0, 0, 0, Client.localPlayer.smallY[0], 0, k1, Client.localPlayer.smallX[0], true, k, this.aClass11Array1230[this.plane]);
            WorldController.anInt470 = -1;
            if(flag)
            {
                this.crossX = this.applet.saveClickX;
                this.crossY = this.applet.saveClickY;
                this.crossType = 1;
                this.crossIndex = 0;
            }
        }
        if(this.applet.clickMode3 == 1 && this.aString844 != null)
        {
            this.aString844 = null;
            this.inputTaken = true;
            this.applet.clickMode3 = 0;
        }
        this.processMenuClick();
        this.processMainScreenClick();
        this.processTabClick();
        this.processChatModeClick();
        if(this.applet.clickMode2 == 1 || this.applet.clickMode3 == 1)
            this.anInt1213++;
        if (this.anInt1500 != 0 || this.anInt1044 != 0 || this.anInt1129 != 0) {
            if (this.anInt1501 < 50 && !this.menuOpen) {
                 this.anInt1501++;
                 if (this.anInt1501 == 50) {
                     if (this.anInt1500 != 0) {
                         this.inputTaken = true;
                     }
                     if (this.anInt1044 != 0) {
                         this.needDrawTabArea = true;
                     }
                 }
             }
         } else if (this.anInt1501 > 0) {
             this.anInt1501--;
         }
        if(this.loadingStage == 2)
            this.method108();
        if(this.loadingStage == 2 && this.aBoolean1160)
            this.calcCameraPos();
        for(int i1 = 0; i1 < 5; i1++)
            this.anIntArray1030[i1]++;

        this.method73();
        this.applet.idleTime++;
        if(this.applet.idleTime > 4500)
        {
            this.anInt1011 = 250;
            this.applet.idleTime -= 500;
            this.stream.writeOpcode(202);
        }
        this.anInt988++;
        if(this.anInt988 > 500)
        {
            this.anInt988 = 0;
            int l1 = (int)(Math.random() * 8D);
            if((l1 & 1) == 1)
                this.anInt1278 += this.anInt1279;
            if((l1 & 2) == 2)
                this.anInt1131 += this.anInt1132;
            if((l1 & 4) == 4)
                this.anInt896 += this.anInt897;
        }
        if(this.anInt1278 < -50)
            this.anInt1279 = 2;
        if(this.anInt1278 > 50)
            this.anInt1279 = -2;
        if(this.anInt1131 < -55)
            this.anInt1132 = 2;
        if(this.anInt1131 > 55)
            this.anInt1132 = -2;
        if(this.anInt896 < -40)
            this.anInt897 = 1;
        if(this.anInt896 > 40)
            this.anInt897 = -1;
        this.anInt1254++;
        if(this.anInt1254 > 500)
        {
            this.anInt1254 = 0;
            int i2 = (int)(Math.random() * 8D);
            if((i2 & 1) == 1)
                this.minimapInt2 += this.anInt1210;
            if((i2 & 2) == 2)
                this.minimapInt3 += this.anInt1171;
        }
        if(this.minimapInt2 < -60)
            this.anInt1210 = 2;
        if(this.minimapInt2 > 60)
            this.anInt1210 = -2;
        if(this.minimapInt3 < -20)
            this.anInt1171 = 1;
        if(this.minimapInt3 > 10)
            this.anInt1171 = -1;
        this.anInt1010++;
        if(this.anInt1010 > 50)
            this.stream.writeOpcode(0);
        try
        {
            if(this.socketStream != null && this.stream.position > 0)
            {
                this.socketStream.queueBytes(this.stream.position, this.stream.payload);
                this.stream.position = 0;
                this.anInt1010 = 0;
            }
        }
        catch(IOException _ex)
        {
            this.dropClient();
        }
        catch(Exception exception)
        {
            this.resetLogout();
        }
    }

    private void method63()
    {
        SpawnedObject class30_sub1 = (SpawnedObject)this.aClass19_1179.getFront();
        for(; class30_sub1 != null; class30_sub1 = (SpawnedObject)this.aClass19_1179.getNext())
            if(class30_sub1.anInt1294 == -1)
            {
                class30_sub1.anInt1302 = 0;
                this.method89(class30_sub1);
            } else
            {
                class30_sub1.unlink();
            }

    }

    private void resetGameframe() {
        this.aRSImageProducer_1166 = null;
        this.aRSImageProducer_1163 = null;
        this.aRSImageProducer_1165 = null;
        this.aRSImageProducer_1123 = null;
        this.aRSImageProducer_1124 = null;
        this.aRSImageProducer_1125 = null;
    }
    private void resetImageProducers()
    {
    	if(Client.aRSImageProducer_1109 != null)
    		return;
    	this.resetGameframe();
        
        
		Client.aRSImageProducer_1109 = new RSImageProducer(360, 200);
		DrawingArea.setAllPixelsToZero();
        this.welcomeScreenRaised = true;
    }
	
   private void method65(int i, int j, int k, int l, RSInterface class9, int i1, boolean flag,
                          int j1)
    {
        int anInt992;
        if(this.aBoolean972)
            anInt992 = 32;
        else
            anInt992 = 0;
        this.aBoolean972 = false;
        if(k >= i && k < i + 16 && l >= i1 && l < i1 + 16)
        {
            class9.scrollPosition -= this.anInt1213 * 4;
            if(flag)
            {
                this.needDrawTabArea = true;
            }
        } else
        if(k >= i && k < i + 16 && l >= (i1 + j) - 16 && l < i1 + j)
        {
            class9.scrollPosition += this.anInt1213 * 4;
            if(flag)
            {
                this.needDrawTabArea = true;
            }
        } else
        if(k >= i - anInt992 && k < i + 16 + anInt992 && l >= i1 + 16 && l < (i1 + j) - 16 && this.anInt1213 > 0)
        {
            int l1 = ((j - 32) * j) / j1;
            if(l1 < 8)
                l1 = 8;
            int i2 = l - i1 - 16 - l1 / 2;
            int j2 = j - 32 - l1;
            class9.scrollPosition = ((j1 - j) * i2) / j2;
            if(flag)
                this.needDrawTabArea = true;
            this.aBoolean972 = true;
        }
    }

    private boolean method66(int i, int j, int k)
    {
        int i1 = i >> 14 & 0x7fff;
        int j1 = this.worldController.method304(this.plane, k, j, i);
        if(j1 == -1)
            return false;
        int k1 = j1 & 0x1f;
        int l1 = j1 >> 6 & 3;
        if(k1 == 10 || k1 == 11 || k1 == 22)
        {
            ObjectDef class46 = ObjectDef.forID(i1);
            int i2;
            int j2;
            if(l1 == 0 || l1 == 2)
            {
                i2 = class46.anInt744;
                j2 = class46.anInt761;
            } else
            {
                i2 = class46.anInt761;
                j2 = class46.anInt744;
            }
            int surroundings = class46.surroundings;
            if(l1 != 0)
                surroundings = (surroundings << l1 & 0xf) + (surroundings >> 4 - l1);
            pathfinder.doWalkTo(surroundings, this, 2, 0, j2, 0, Client.localPlayer.smallY[0], i2, j, Client.localPlayer.smallX[0], false, k, this.aClass11Array1230[this.plane]);
        } else
        {
            pathfinder.doWalkTo(0, this, 2, l1, 0, k1 + 1, Client.localPlayer.smallY[0], 0, j, Client.localPlayer.smallX[0], false, k, this.aClass11Array1230[this.plane]);
        }
        this.crossX = this.applet.saveClickX;
        this.crossY = this.applet.saveClickY;
        this.crossType = 2;
        this.crossIndex = 0;
        return true;
    }

    private Archive fetchArchive(int i)
    {
        byte abyte0[] = null;
 
        try
        {
            if(this.indexs[0] != null)
                abyte0 = this.indexs[0].get(i);
        }
        catch(Exception _ex) { 
        	_ex.printStackTrace();
        }
        if (Constants.ENABLE_JAGGRAB && this.jaggrab.check(abyte0, i))
        	abyte0 = null;
        if(abyte0 != null)
        {
            Archive archive = new Archive(abyte0);
            return archive;
        }
        return Constants.ENABLE_JAGGRAB ? this.jaggrab.get(i) : null;
    }

    private void dropClient()
    {
        if(this.anInt1011 > 0)
        {
            this.resetLogout();
            return;
        }
        this.aRSImageProducer_1165.initDrawingArea();
        this.aTextDrawingArea_1271.drawText(0, "Connection lost", 144, 257);
        this.aTextDrawingArea_1271.drawText(0xffffff, "Connection lost", 143, 256);
        this.aTextDrawingArea_1271.drawText(0, "Please wait - attempting to reestablish", 159, 257);
        this.aTextDrawingArea_1271.drawText(0xffffff, "Please wait - attempting to reestablish", 158, 256);
        this.aRSImageProducer_1165.drawGraphics(4, this.applet.graphics, 4);
        this.anInt1021 = 0;
        this.destX = 0;
        RSSocket rsSocket = this.socketStream;
        this.loggedIn = false;
        this.loginFailures = 0;
        this.login(this.myUsername, this.myPassword, true);
        if(!this.loggedIn)
            this.resetLogout();
        try
        {
            rsSocket.close();
        }
        catch(Exception _ex)
        {
        }
    }

    private void doAction(int i)
    {
        if(i < 0)
            return;
        if(this.inputDialogState != 0)
        {
            this.inputDialogState = 0;
            this.inputTaken = true;
        }
        int j = this.menuActionCmd2[i];
        int k = this.menuActionCmd3[i];
        int l = this.menuActionID[i];
        int i1 = this.menuActionCmd1[i];
        if(l >= 2000)
            l -= 2000;
        if(l == 582)
        {
            NPC npc = this.npcArray[i1];
            if(npc != null)
            {
                pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, npc.smallY[0], Client.localPlayer.smallX[0], false, npc.smallX[0], this.aClass11Array1230[this.plane]);
                this.crossX = this.applet.saveClickX;
                this.crossY = this.applet.saveClickY;
                this.crossType = 2;
                this.crossIndex = 0;
                this.stream.writeOpcode(57);
                this.stream.writeShortA(this.anInt1285);
                this.stream.writeShortA(i1);
                this.stream.writeLEShort(this.anInt1283);
                this.stream.writeShortA(this.anInt1284);
            }
        }
        if(l == 234)
        {
            boolean flag1 = pathfinder.doWalkTo(0, this, 2, 0, 0, 0, Client.localPlayer.smallY[0], 0, k, Client.localPlayer.smallX[0], false, j, this.aClass11Array1230[this.plane]);
            if(!flag1)
                flag1 = pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, k, Client.localPlayer.smallX[0], false, j, this.aClass11Array1230[this.plane]);
            this.crossX = this.applet.saveClickX;
            this.crossY = this.applet.saveClickY;
            this.crossType = 2;
            this.crossIndex = 0;
            this.stream.writeOpcode(236);
            this.stream.writeLEShort(k + this.baseY);
            this.stream.writeShort(i1);
            this.stream.writeLEShort(j + this.baseX);
        }
        if(l == 62 && this.method66(i1, k, j))
        {
            this.stream.writeOpcode(192);
            this.stream.writeShort(this.anInt1284);
            this.stream.writeLEShort(i1 >> 14 & 0x7fff);
            this.stream.writeLEShortA(k + this.baseY);
            this.stream.writeLEShort(this.anInt1283);
            this.stream.writeLEShortA(j + this.baseX);
            this.stream.writeShort(this.anInt1285);
        }
        if(l == 511)
        {
            boolean flag2 = pathfinder.doWalkTo(0, this, 2, 0, 0, 0, Client.localPlayer.smallY[0], 0, k, Client.localPlayer.smallX[0], false, j, this.aClass11Array1230[this.plane]);
            if(!flag2)
                flag2 = pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, k, Client.localPlayer.smallX[0], false, j, this.aClass11Array1230[this.plane]);
            this.crossX = this.applet.saveClickX;
            this.crossY = this.applet.saveClickY;
            this.crossType = 2;
            this.crossIndex = 0;
            this.stream.writeOpcode(25);
            this.stream.writeLEShort(this.anInt1284);
            this.stream.writeShortA(this.anInt1285);
            this.stream.writeShort(i1);
            this.stream.writeShortA(k + this.baseY);
            this.stream.writeLEShortA(this.anInt1283);
            this.stream.writeShort(j + this.baseX);
        }
        if(l == 74)
        {
            this.stream.writeOpcode(122);
            this.stream.writeLEShortA(k);
            this.stream.writeShortA(j);
            this.stream.writeLEShort(i1);
            this.atInventoryLoopCycle = 0;
            this.atInventoryInterface = k;
            this.atInventoryIndex = j;
            this.atInventoryInterfaceType = 2;
            if(RSInterface.interfaceCache[k].parentID == this.openInterfaceID)
                this.atInventoryInterfaceType = 1;
            if(RSInterface.interfaceCache[k].parentID == this.backDialogID)
                this.atInventoryInterfaceType = 3;
        }
        if(l == 315)
        {
            RSInterface class9 = RSInterface.interfaceCache[k];
            boolean flag8 = true;
            if(class9.anInt214 > 0)
                flag8 = this.promptUserForInput(class9);
            if(flag8)
            {
                this.stream.writeOpcode(185);
                this.stream.writeShort(k);
            }
        }
        if(l == 561)
        {
            Player player = this.playerArray[i1];
            if(player != null)
            {
                pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, player.smallY[0], Client.localPlayer.smallX[0], false, player.smallX[0], this.aClass11Array1230[this.plane]);
                this.crossX = this.applet.saveClickX;
                this.crossY = this.applet.saveClickY;
                this.crossType = 2;
                this.crossIndex = 0;
                Client.anInt1188 += i1;
                if(Client.anInt1188 >= 90)
                {
                    this.stream.writeOpcode(136);
                    Client.anInt1188 = 0;
                }
                this.stream.writeOpcode(128);
                this.stream.writeShort(i1);
            }
        }
        if(l == 20)
        {
            NPC class30_sub2_sub4_sub1_sub1_1 = this.npcArray[i1];
            if(class30_sub2_sub4_sub1_sub1_1 != null)
            {
                pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, class30_sub2_sub4_sub1_sub1_1.smallY[0], Client.localPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_1.smallX[0], this.aClass11Array1230[this.plane]);
                this.crossX = this.applet.saveClickX;
                this.crossY = this.applet.saveClickY;
                this.crossType = 2;
                this.crossIndex = 0;
                this.stream.writeOpcode(155);
                this.stream.writeLEShort(i1);
            }
        }
        if(l == 779)
        {
            Player class30_sub2_sub4_sub1_sub2_1 = this.playerArray[i1];
            if(class30_sub2_sub4_sub1_sub2_1 != null)
            {
                pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, class30_sub2_sub4_sub1_sub2_1.smallY[0], Client.localPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_1.smallX[0], this.aClass11Array1230[this.plane]);
                this.crossX = this.applet.saveClickX;
                this.crossY = this.applet.saveClickY;
                this.crossType = 2;
                this.crossIndex = 0;
                this.stream.writeOpcode(153);
                this.stream.writeLEShort(i1);
            }
        }
        if(l == 516)
            if(!this.menuOpen)
                this.worldController.method312(this.applet.saveClickY - 4, this.applet.saveClickX - 4);
            else
                this.worldController.method312(k - 4, j - 4);
        if(l == 1062)
        {
            Client.anInt924 += this.baseX;
            if(Client.anInt924 >= 113)
            {
                this.stream.writeOpcode(183);
                this.stream.writeTriByte(0xe63271);
                Client.anInt924 = 0;
            }
            this.method66(i1, k, j);
            this.stream.writeOpcode(228);
            this.stream.writeShortA(i1 >> 14 & 0x7fff);
            this.stream.writeShortA(k + this.baseY);
            this.stream.writeShort(j + this.baseX);
        }
        if(l == 679 && !this.aBoolean1149)
        {
            this.stream.writeOpcode(40);
            this.stream.writeShort(k);
            this.aBoolean1149 = true;
        }
        if(l == 431)
        {
            this.stream.writeOpcode(129);
            this.stream.writeShortA(j);
            this.stream.writeShort(k);
            this.stream.writeShortA(i1);
            this.atInventoryLoopCycle = 0;
            this.atInventoryInterface = k;
            this.atInventoryIndex = j;
            this.atInventoryInterfaceType = 2;
            if(RSInterface.interfaceCache[k].parentID == this.openInterfaceID)
                this.atInventoryInterfaceType = 1;
            if(RSInterface.interfaceCache[k].parentID == this.backDialogID)
                this.atInventoryInterfaceType = 3;
        }
        if(l == 337 || l == 42 || l == 792 || l == 322)
        {
            String s = this.menuActionName[i];
            int k1 = s.indexOf("@whi@");
            if(k1 != -1)
            {
                long l3 = TextClass.longForName(s.substring(k1 + 5).trim());
                if(l == 337)
                    this.addFriend(l3);
                if(l == 42)
                    this.addIgnore(l3);
                if(l == 792)
                    this.delFriend(l3);
                if(l == 322)
                    this.delIgnore(l3);
            }
        }
        if(l == 53)
        {
            this.stream.writeOpcode(135);
            this.stream.writeLEShort(j);
            this.stream.writeShortA(k);
            this.stream.writeLEShort(i1);
            this.atInventoryLoopCycle = 0;
            this.atInventoryInterface = k;
            this.atInventoryIndex = j;
            this.atInventoryInterfaceType = 2;
            if(RSInterface.interfaceCache[k].parentID == this.openInterfaceID)
                this.atInventoryInterfaceType = 1;
            if(RSInterface.interfaceCache[k].parentID == this.backDialogID)
                this.atInventoryInterfaceType = 3;
        }
        if(l == 539)
        {
            this.stream.writeOpcode(16);
            this.stream.writeShortA(i1);
            this.stream.writeLEShortA(j);
            this.stream.writeLEShortA(k);
            this.atInventoryLoopCycle = 0;
            this.atInventoryInterface = k;
            this.atInventoryIndex = j;
            this.atInventoryInterfaceType = 2;
            if(RSInterface.interfaceCache[k].parentID == this.openInterfaceID)
                this.atInventoryInterfaceType = 1;
            if(RSInterface.interfaceCache[k].parentID == this.backDialogID)
                this.atInventoryInterfaceType = 3;
        }
        if(l == 484 || l == 6)
        {
            String s1 = this.menuActionName[i];
            int l1 = s1.indexOf("@whi@");
            if(l1 != -1)
            {
                s1 = s1.substring(l1 + 5).trim();
                String s7 = TextClass.fixName(TextClass.nameForLong(TextClass.longForName(s1)));
                boolean flag9 = false;
                for(int j3 = 0; j3 < this.playerCount; j3++)
                {
                    Player class30_sub2_sub4_sub1_sub2_7 = this.playerArray[this.playerIndices[j3]];
                    if(class30_sub2_sub4_sub1_sub2_7 == null || class30_sub2_sub4_sub1_sub2_7.name == null || !class30_sub2_sub4_sub1_sub2_7.name.equalsIgnoreCase(s7))
                        continue;
                    pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, class30_sub2_sub4_sub1_sub2_7.smallY[0], Client.localPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_7.smallX[0], this.aClass11Array1230[this.plane]);
                    if(l == 484)
                    {
                        this.stream.writeOpcode(139);
                        this.stream.writeLEShort(this.playerIndices[j3]);
                    }
                    if(l == 6)
                    {
                        Client.anInt1188 += i1;
                        if(Client.anInt1188 >= 90)
                        {
                            this.stream.writeOpcode(136);
                            Client.anInt1188 = 0;
                        }
                        this.stream.writeOpcode(128);
                        this.stream.writeShort(this.playerIndices[j3]);
                    }
                    flag9 = true;
                    break;
                }

                if(!flag9)
                    this.pushMessage("Unable to find " + s7, 0, "");
            }
        }
        if(l == 870)
        {
            this.stream.writeOpcode(53);
            this.stream.writeShort(j);
            this.stream.writeShortA(this.anInt1283);
            this.stream.writeLEShortA(i1);
            this.stream.writeShort(this.anInt1284);
            this.stream.writeLEShort(this.anInt1285);
            this.stream.writeShort(k);
            this.atInventoryLoopCycle = 0;
            this.atInventoryInterface = k;
            this.atInventoryIndex = j;
            this.atInventoryInterfaceType = 2;
            if(RSInterface.interfaceCache[k].parentID == this.openInterfaceID)
                this.atInventoryInterfaceType = 1;
            if(RSInterface.interfaceCache[k].parentID == this.backDialogID)
                this.atInventoryInterfaceType = 3;
        }
        if(l == 847)
        {
            this.stream.writeOpcode(87);
            this.stream.writeShortA(i1);
            this.stream.writeShort(k);
            this.stream.writeShortA(j);
            this.atInventoryLoopCycle = 0;
            this.atInventoryInterface = k;
            this.atInventoryIndex = j;
            this.atInventoryInterfaceType = 2;
            if(RSInterface.interfaceCache[k].parentID == this.openInterfaceID)
                this.atInventoryInterfaceType = 1;
            if(RSInterface.interfaceCache[k].parentID == this.backDialogID)
                this.atInventoryInterfaceType = 3;
        }
        if(l == 626)
        {
            RSInterface class9_1 = RSInterface.interfaceCache[k];
            this.spellSelected = 1;
            this.anInt1137 = k;
            this.spellUsableOn = class9_1.spellUsableOn;
            this.itemSelected = 0;
            this.needDrawTabArea = true;
            String s4 = class9_1.selectedActionName;
            if(s4.indexOf(" ") != -1)
                s4 = s4.substring(0, s4.indexOf(" "));
            String s8 = class9_1.selectedActionName;
            if(s8.indexOf(" ") != -1)
                s8 = s8.substring(s8.indexOf(" ") + 1);
            this.spellTooltip = s4 + " " + class9_1.spellName + " " + s8;
            if(this.spellUsableOn == 16)
            {
                this.needDrawTabArea = true;
                this.tabID = 3;
                this.tabAreaAltered = true;
            }
            return;
        }
        if(l == 78)
        {
            this.stream.writeOpcode(117);
            this.stream.writeLEShortA(k);
            this.stream.writeLEShortA(i1);
            this.stream.writeLEShort(j);
            this.atInventoryLoopCycle = 0;
            this.atInventoryInterface = k;
            this.atInventoryIndex = j;
            this.atInventoryInterfaceType = 2;
            if(RSInterface.interfaceCache[k].parentID == this.openInterfaceID)
                this.atInventoryInterfaceType = 1;
            if(RSInterface.interfaceCache[k].parentID == this.backDialogID)
                this.atInventoryInterfaceType = 3;
        }
        if(l == 27)
        {
            Player class30_sub2_sub4_sub1_sub2_2 = this.playerArray[i1];
            if(class30_sub2_sub4_sub1_sub2_2 != null)
            {
                pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, class30_sub2_sub4_sub1_sub2_2.smallY[0], Client.localPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_2.smallX[0], this.aClass11Array1230[this.plane]);
                this.crossX = this.applet.saveClickX;
                this.crossY = this.applet.saveClickY;
                this.crossType = 2;
                this.crossIndex = 0;
                Client.anInt986 += i1;
                if(Client.anInt986 >= 54)
                {
                    this.stream.writeOpcode(189);
                    this.stream.writeByte(234);
                    Client.anInt986 = 0;
                }
                this.stream.writeOpcode(73);
                this.stream.writeLEShort(i1);
            }
        }
        if(l == 213)
        {
            boolean flag3 = pathfinder.doWalkTo(0, this, 2, 0, 0, 0, Client.localPlayer.smallY[0], 0, k, Client.localPlayer.smallX[0], false, j, this.aClass11Array1230[this.plane]);
            if(!flag3)
                flag3 = pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, k, Client.localPlayer.smallX[0], false, j, this.aClass11Array1230[this.plane]);
            this.crossX = this.applet.saveClickX;
            this.crossY = this.applet.saveClickY;
            this.crossType = 2;
            this.crossIndex = 0;
            this.stream.writeOpcode(79);
            this.stream.writeLEShort(k + this.baseY);
            this.stream.writeShort(i1);
            this.stream.writeShortA(j + this.baseX);
        }
        if(l == 632)
        {
            this.stream.writeOpcode(145);
            this.stream.writeShortA(k);
            this.stream.writeShortA(j);
            this.stream.writeShortA(i1);
            this.atInventoryLoopCycle = 0;
            this.atInventoryInterface = k;
            this.atInventoryIndex = j;
            this.atInventoryInterfaceType = 2;
            if(RSInterface.interfaceCache[k].parentID == this.openInterfaceID)
                this.atInventoryInterfaceType = 1;
            if(RSInterface.interfaceCache[k].parentID == this.backDialogID)
                this.atInventoryInterfaceType = 3;
        }
        if(l == 493)
        {
            this.stream.writeOpcode(75);
            this.stream.writeLEShortA(k);
            this.stream.writeLEShort(j);
            this.stream.writeShortA(i1);
            this.atInventoryLoopCycle = 0;
            this.atInventoryInterface = k;
            this.atInventoryIndex = j;
            this.atInventoryInterfaceType = 2;
            if(RSInterface.interfaceCache[k].parentID == this.openInterfaceID)
                this.atInventoryInterfaceType = 1;
            if(RSInterface.interfaceCache[k].parentID == this.backDialogID)
                this.atInventoryInterfaceType = 3;
        }
        if(l == 652)
        {
            boolean flag4 = pathfinder.doWalkTo(0, this, 2, 0, 0, 0, Client.localPlayer.smallY[0], 0, k, Client.localPlayer.smallX[0], false, j, this.aClass11Array1230[this.plane]);
            if(!flag4)
                flag4 = pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, k, Client.localPlayer.smallX[0], false, j, this.aClass11Array1230[this.plane]);
            this.crossX = this.applet.saveClickX;
            this.crossY = this.applet.saveClickY;
            this.crossType = 2;
            this.crossIndex = 0;
            this.stream.writeOpcode(156);
            this.stream.writeShortA(j + this.baseX);
            this.stream.writeLEShort(k + this.baseY);
            this.stream.writeLEShortA(i1);
        }
        if(l == 94)
        {
            boolean flag5 = pathfinder.doWalkTo(0, this, 2, 0, 0, 0, Client.localPlayer.smallY[0], 0, k, Client.localPlayer.smallX[0], false, j, this.aClass11Array1230[this.plane]);
            if(!flag5)
                flag5 = pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, k, Client.localPlayer.smallX[0], false, j, this.aClass11Array1230[this.plane]);
            this.crossX = this.applet.saveClickX;
            this.crossY = this.applet.saveClickY;
            this.crossType = 2;
            this.crossIndex = 0;
            this.stream.writeOpcode(181);
            this.stream.writeLEShort(k + this.baseY);
            this.stream.writeShort(i1);
            this.stream.writeLEShort(j + this.baseX);
            this.stream.writeShortA(this.anInt1137);
        }
        if(l == 646)
        {
            this.stream.writeOpcode(185);
            this.stream.writeShort(k);
            RSInterface class9_2 = RSInterface.interfaceCache[k];
            if(class9_2.valueIndexArray != null && class9_2.valueIndexArray[0][0] == 5)
            {
                int i2 = class9_2.valueIndexArray[0][1];
                if(this.variousSettings[i2] != class9_2.anIntArray212[0])
                {
                    this.variousSettings[i2] = class9_2.anIntArray212[0];
                    this.method33(i2);
                    this.needDrawTabArea = true;
                }
            }
        }
        if(l == 225)
        {
            NPC class30_sub2_sub4_sub1_sub1_2 = this.npcArray[i1];
            if(class30_sub2_sub4_sub1_sub1_2 != null)
            {
                pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, class30_sub2_sub4_sub1_sub1_2.smallY[0], Client.localPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_2.smallX[0], this.aClass11Array1230[this.plane]);
                this.crossX = this.applet.saveClickX;
                this.crossY = this.applet.saveClickY;
                this.crossType = 2;
                this.crossIndex = 0;
                Client.anInt1226 += i1;
                if(Client.anInt1226 >= 85)
                {
                    this.stream.writeOpcode(230);
                    this.stream.writeByte(239);
                    Client.anInt1226 = 0;
                }
                this.stream.writeOpcode(17);
                this.stream.writeLEShortA(i1);
            }
        }
        if(l == 965)
        {
            NPC class30_sub2_sub4_sub1_sub1_3 = this.npcArray[i1];
            if(class30_sub2_sub4_sub1_sub1_3 != null)
            {
                pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, class30_sub2_sub4_sub1_sub1_3.smallY[0], Client.localPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_3.smallX[0], this.aClass11Array1230[this.plane]);
                this.crossX = this.applet.saveClickX;
                this.crossY = this.applet.saveClickY;
                this.crossType = 2;
                this.crossIndex = 0;
                Client.anInt1134++;
                if(Client.anInt1134 >= 96)
                {
                    this.stream.writeOpcode(152);
                    this.stream.writeByte(88);
                    Client.anInt1134 = 0;
                }
                this.stream.writeOpcode(21);
                this.stream.writeShort(i1);
            }
        }
        if(l == 413)
        {
            NPC class30_sub2_sub4_sub1_sub1_4 = this.npcArray[i1];
            if(class30_sub2_sub4_sub1_sub1_4 != null)
            {
                pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, class30_sub2_sub4_sub1_sub1_4.smallY[0], Client.localPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_4.smallX[0], this.aClass11Array1230[this.plane]);
                this.crossX = this.applet.saveClickX;
                this.crossY = this.applet.saveClickY;
                this.crossType = 2;
                this.crossIndex = 0;
                this.stream.writeOpcode(131);
                this.stream.writeLEShortA(i1);
                this.stream.writeShortA(this.anInt1137);
            }
        }
        if(l == 200)
            this.clearTopInterfaces();
        if(l == 1025)
        {
            NPC class30_sub2_sub4_sub1_sub1_5 = this.npcArray[i1];
            if(class30_sub2_sub4_sub1_sub1_5 != null)
            {
                EntityDef entityDef = class30_sub2_sub4_sub1_sub1_5.desc;
                if(entityDef.childrenIDs != null)
                    entityDef = entityDef.method161();
                if(entityDef != null)
                {
                    String s9;
                    if(entityDef.description != null)
                        s9 = new String(entityDef.description);
                    else
                        s9 = "It's a " + entityDef.name + ".";
                    this.pushMessage(s9, 0, "");
                }
            }
        }
        if(l == 900)
        {
            this.method66(i1, k, j);
            this.stream.writeOpcode(252);
            this.stream.writeLEShortA(i1 >> 14 & 0x7fff);
            this.stream.writeLEShort(k + this.baseY);
            this.stream.writeShortA(j + this.baseX);
        }
        if(l == 412)
        {
            NPC class30_sub2_sub4_sub1_sub1_6 = this.npcArray[i1];
            if(class30_sub2_sub4_sub1_sub1_6 != null)
            {
                pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, class30_sub2_sub4_sub1_sub1_6.smallY[0], Client.localPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_6.smallX[0], this.aClass11Array1230[this.plane]);
                this.crossX = this.applet.saveClickX;
                this.crossY = this.applet.saveClickY;
                this.crossType = 2;
                this.crossIndex = 0;
                this.stream.writeOpcode(72);
                this.stream.writeShortA(i1);
            }
        }
        if(l == 365)
        {
            Player class30_sub2_sub4_sub1_sub2_3 = this.playerArray[i1];
            if(class30_sub2_sub4_sub1_sub2_3 != null)
            {
                pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, class30_sub2_sub4_sub1_sub2_3.smallY[0], Client.localPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_3.smallX[0], this.aClass11Array1230[this.plane]);
                this.crossX = this.applet.saveClickX;
                this.crossY = this.applet.saveClickY;
                this.crossType = 2;
                this.crossIndex = 0;
                this.stream.writeOpcode(249);
                this.stream.writeShortA(i1);
                this.stream.writeLEShort(this.anInt1137);
            }
        }
        if(l == 729)
        {
            Player class30_sub2_sub4_sub1_sub2_4 = this.playerArray[i1];
            if(class30_sub2_sub4_sub1_sub2_4 != null)
            {
                pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, class30_sub2_sub4_sub1_sub2_4.smallY[0], Client.localPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_4.smallX[0], this.aClass11Array1230[this.plane]);
                this.crossX = this.applet.saveClickX;
                this.crossY = this.applet.saveClickY;
                this.crossType = 2;
                this.crossIndex = 0;
                this.stream.writeOpcode(39);
                this.stream.writeLEShort(i1);
            }
        }
        if(l == 577)
        {
            Player class30_sub2_sub4_sub1_sub2_5 = this.playerArray[i1];
            if(class30_sub2_sub4_sub1_sub2_5 != null)
            {
                pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, class30_sub2_sub4_sub1_sub2_5.smallY[0], Client.localPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_5.smallX[0], this.aClass11Array1230[this.plane]);
                this.crossX = this.applet.saveClickX;
                this.crossY = this.applet.saveClickY;
                this.crossType = 2;
                this.crossIndex = 0;
                this.stream.writeOpcode(139);
                this.stream.writeLEShort(i1);
            }
        }
        if(l == 956 && this.method66(i1, k, j))
        {
            this.stream.writeOpcode(35);
            this.stream.writeLEShort(j + this.baseX);
            this.stream.writeShortA(this.anInt1137);
            this.stream.writeShortA(k + this.baseY);
            this.stream.writeLEShort(i1 >> 14 & 0x7fff);
        }
        if(l == 567)
        {
            boolean flag6 = pathfinder.doWalkTo(0, this, 2, 0, 0, 0, Client.localPlayer.smallY[0], 0, k, Client.localPlayer.smallX[0], false, j, this.aClass11Array1230[this.plane]);
            if(!flag6)
                flag6 = pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, k, Client.localPlayer.smallX[0], false, j, this.aClass11Array1230[this.plane]);
            this.crossX = this.applet.saveClickX;
            this.crossY = this.applet.saveClickY;
            this.crossType = 2;
            this.crossIndex = 0;
            this.stream.writeOpcode(23);
            this.stream.writeLEShort(k + this.baseY);
            this.stream.writeLEShort(i1);
            this.stream.writeLEShort(j + this.baseX);
        }
        if(l == 867)
        {
            if((i1 & 3) == 0)
                Client.anInt1175++;
            if(Client.anInt1175 >= 59)
            {
                this.stream.writeOpcode(200);
                this.stream.writeShort(25501);
                Client.anInt1175 = 0;
            }
            this.stream.writeOpcode(43);
            this.stream.writeLEShort(k);
            this.stream.writeShortA(i1);
            this.stream.writeShortA(j);
            this.atInventoryLoopCycle = 0;
            this.atInventoryInterface = k;
            this.atInventoryIndex = j;
            this.atInventoryInterfaceType = 2;
            if(RSInterface.interfaceCache[k].parentID == this.openInterfaceID)
                this.atInventoryInterfaceType = 1;
            if(RSInterface.interfaceCache[k].parentID == this.backDialogID)
                this.atInventoryInterfaceType = 3;
        }
        if(l == 543)
        {
            this.stream.writeOpcode(237);
            this.stream.writeShort(j);
            this.stream.writeShortA(i1);
            this.stream.writeShort(k);
            this.stream.writeShortA(this.anInt1137);
            this.atInventoryLoopCycle = 0;
            this.atInventoryInterface = k;
            this.atInventoryIndex = j;
            this.atInventoryInterfaceType = 2;
            if(RSInterface.interfaceCache[k].parentID == this.openInterfaceID)
                this.atInventoryInterfaceType = 1;
            if(RSInterface.interfaceCache[k].parentID == this.backDialogID)
                this.atInventoryInterfaceType = 3;
        }
        if(l == 606)
        {
            String s2 = this.menuActionName[i];
            int j2 = s2.indexOf("@whi@");
            if(j2 != -1)
                if(this.openInterfaceID == -1)
                {
                    this.clearTopInterfaces();
                    this.reportAbuseInput = s2.substring(j2 + 5).trim();
                    this.canMute = false;
                    for(int i3 = 0; i3 < RSInterface.interfaceCache.length; i3++)
                    {
                        if(RSInterface.interfaceCache[i3] == null || RSInterface.interfaceCache[i3].anInt214 != 600)
                            continue;
                        this.reportAbuseInterfaceID = this.openInterfaceID = RSInterface.interfaceCache[i3].parentID;
                        break;
                    }

                } else
                {
                    this.pushMessage("Please close the interface you have open before using 'report abuse'", 0, "");
                }
        }
        if(l == 491)
        {
            Player class30_sub2_sub4_sub1_sub2_6 = this.playerArray[i1];
            if(class30_sub2_sub4_sub1_sub2_6 != null)
            {
                pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, class30_sub2_sub4_sub1_sub2_6.smallY[0], Client.localPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_6.smallX[0], this.aClass11Array1230[this.plane]);
                this.crossX = this.applet.saveClickX;
                this.crossY = this.applet.saveClickY;
                this.crossType = 2;
                this.crossIndex = 0;
                this.stream.writeOpcode(14);
                this.stream.writeShortA(this.anInt1284);
                this.stream.writeShort(i1);
                this.stream.writeShort(this.anInt1285);
                this.stream.writeLEShort(this.anInt1283);
            }
        }
        if(l == 639)
        {
            String s3 = this.menuActionName[i];
            int k2 = s3.indexOf("@whi@");
            if(k2 != -1)
            {
                long l4 = TextClass.longForName(s3.substring(k2 + 5).trim());
                int k3 = -1;
                for(int i4 = 0; i4 < this.friendsCount; i4++)
                {
                    if(this.friendsListAsLongs[i4] != l4)
                        continue;
                    k3 = i4;
                    break;
                }

                if(k3 != -1 && this.friendsNodeIDs[k3] > 0)
                {
                    this.inputTaken = true;
                    this.inputDialogState = 0;
                    this.messagePromptRaised = true;
                    this.promptInput = "";
                    this.friendsListAction = 3;
                    this.aLong953 = this.friendsListAsLongs[k3];
                    this.aString1121 = "Enter message to send to " + this.friendsList[k3];
                }
            }
        }
        if(l == 454)
        {
            this.stream.writeOpcode(41);
            this.stream.writeShort(i1);
            this.stream.writeShortA(j);
            this.stream.writeShortA(k);
            this.atInventoryLoopCycle = 0;
            this.atInventoryInterface = k;
            this.atInventoryIndex = j;
            this.atInventoryInterfaceType = 2;
            if(RSInterface.interfaceCache[k].parentID == this.openInterfaceID)
                this.atInventoryInterfaceType = 1;
            if(RSInterface.interfaceCache[k].parentID == this.backDialogID)
                this.atInventoryInterfaceType = 3;
        }
        if(l == 478)
        {
            NPC class30_sub2_sub4_sub1_sub1_7 = this.npcArray[i1];
            if(class30_sub2_sub4_sub1_sub1_7 != null)
            {
                pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, class30_sub2_sub4_sub1_sub1_7.smallY[0], Client.localPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_7.smallX[0], this.aClass11Array1230[this.plane]);
                this.crossX = this.applet.saveClickX;
                this.crossY = this.applet.saveClickY;
                this.crossType = 2;
                this.crossIndex = 0;
                if((i1 & 3) == 0)
                    Client.anInt1155++;
                if(Client.anInt1155 >= 53)
                {
                    this.stream.writeOpcode(85);
                    this.stream.writeByte(66);
                    Client.anInt1155 = 0;
                }
                this.stream.writeOpcode(18);
                this.stream.writeLEShort(i1);
            }
        }
        if(l == 113)
        {
            this.method66(i1, k, j);
            this.stream.writeOpcode(70);
            this.stream.writeLEShort(j + this.baseX);
            this.stream.writeShort(k + this.baseY);
            this.stream.writeLEShortA(i1 >> 14 & 0x7fff);
        }
        if(l == 872)
        {
            this.method66(i1, k, j);
            this.stream.writeOpcode(234);
            this.stream.writeLEShortA(j + this.baseX);
            this.stream.writeShortA(i1 >> 14 & 0x7fff);
            this.stream.writeLEShortA(k + this.baseY);
        }
        if(l == 502)
        {
            this.method66(i1, k, j);
            this.stream.writeOpcode(132);
            this.stream.writeLEShortA(j + this.baseX);
            this.stream.writeShort(i1 >> 14 & 0x7fff);
            this.stream.writeShortA(k + this.baseY);
        }
        if(l == 1125)
        {
            ItemDef itemDef = ItemDef.forID(i1);
            RSInterface class9_4 = RSInterface.interfaceCache[k];
            String s5;
            if(class9_4 != null && class9_4.invStackSizes[j] >= 0x186a0)
                s5 = class9_4.invStackSizes[j] + " x " + itemDef.name;
            else
            if(itemDef.description != null)
                s5 = new String(itemDef.description);
            else
                s5 = "It's a " + itemDef.name + ".";
            this.pushMessage(s5, 0, "");
        }
        if(l == 169)
        {
            this.stream.writeOpcode(185);
            this.stream.writeShort(k);
            RSInterface class9_3 = RSInterface.interfaceCache[k];
            if(class9_3.valueIndexArray != null && class9_3.valueIndexArray[0][0] == 5)
            {
                int l2 = class9_3.valueIndexArray[0][1];
                this.variousSettings[l2] = 1 - this.variousSettings[l2];
                this.method33(l2);
                this.needDrawTabArea = true;
            }
        }
        if(l == 447)
        {
            this.itemSelected = 1;
            this.anInt1283 = j;
            this.anInt1284 = k;
            this.anInt1285 = i1;
            this.selectedItemName = ItemDef.forID(i1).name;
            this.spellSelected = 0;
            this.needDrawTabArea = true;
            return;
        }
        if(l == 1226)
        {
            int j1 = i1 >> 14 & 0x7fff;
            ObjectDef class46 = ObjectDef.forID(j1);
            String s10;
            if(class46.description != null)
                s10 = new String(class46.description);
            else
                s10 = "It's a " + class46.name + ".";
            this.pushMessage(s10, 0, "");
        }
        if(l == 244)
        {
            boolean flag7 = pathfinder.doWalkTo(0, this, 2, 0, 0, 0, Client.localPlayer.smallY[0], 0, k, Client.localPlayer.smallX[0], false, j, this.aClass11Array1230[this.plane]);
            if(!flag7)
                flag7 = pathfinder.doWalkTo(0, this, 2, 0, 1, 0, Client.localPlayer.smallY[0], 1, k, Client.localPlayer.smallX[0], false, j, this.aClass11Array1230[this.plane]);
            this.crossX = this.applet.saveClickX;
            this.crossY = this.applet.saveClickY;
            this.crossType = 2;
            this.crossIndex = 0;
            this.stream.writeOpcode(253);
            this.stream.writeLEShort(j + this.baseX);
            this.stream.writeLEShortA(k + this.baseY);
            this.stream.writeShortA(i1);
        }
        if(l == 1448)
        {
            ItemDef itemDef_1 = ItemDef.forID(i1);
            String s6;
            if(itemDef_1.description != null)
                s6 = new String(itemDef_1.description);
            else
                s6 = "It's a " + itemDef_1.name + ".";
            this.pushMessage(s6, 0, "");
        }
        this.itemSelected = 0;
            this.spellSelected = 0;
            this.needDrawTabArea = true;

    }

    private void method70()
    {
        this.anInt1251 = 0;
        int j = (Client.localPlayer.x >> 7) + this.baseX;
        int k = (Client.localPlayer.y >> 7) + this.baseY;
        if(j >= 3053 && j <= 3156 && k >= 3056 && k <= 3136)
            this.anInt1251 = 1;
        if(j >= 3072 && j <= 3118 && k >= 9492 && k <= 9535)
            this.anInt1251 = 1;
        if(this.anInt1251 == 1 && j >= 3139 && j <= 3199 && k >= 3008 && k <= 3062)
            this.anInt1251 = 0;
    }

    private void build3dScreenMenu()
    {
        if(this.itemSelected == 0 && this.spellSelected == 0)
        {
            this.menuActionName[this.menuActionRow] = "Walk here";
            this.menuActionID[this.menuActionRow] = 516;
            this.menuActionCmd2[this.menuActionRow] = this.applet.mouseX;
            this.menuActionCmd3[this.menuActionRow] = this.applet.mouseY;
            this.menuActionRow++;
        }
        int j = -1;
        for(int k = 0; k < ModelRenderer.anInt1687; k++)
        {
            int l = ModelRenderer.anIntArray1688[k];
            int i1 = l & 0x7f;
            int j1 = l >> 7 & 0x7f;
            int k1 = l >> 29 & 3;
            int l1 = l >> 14 & 0x7fff;
            if(l == j)
                continue;
            j = l;
            if(k1 == 2 && this.worldController.method304(this.plane, i1, j1, l) >= 0)
            {
                ObjectDef class46 = ObjectDef.forID(l1);
                if(class46.childrenIDs != null)
                    class46 = class46.method580();
                if(class46 == null)
                    continue;
                if(this.itemSelected == 1)
                {
                    this.menuActionName[this.menuActionRow] = "Use " + this.selectedItemName + " with @cya@" + class46.name;
                    this.menuActionID[this.menuActionRow] = 62;
                    this.menuActionCmd1[this.menuActionRow] = l;
                    this.menuActionCmd2[this.menuActionRow] = i1;
                    this.menuActionCmd3[this.menuActionRow] = j1;
                    this.menuActionRow++;
                } else
                if(this.spellSelected == 1)
                {
                    if((this.spellUsableOn & 4) == 4)
                    {
                        this.menuActionName[this.menuActionRow] = this.spellTooltip + " @cya@" + class46.name;
                        this.menuActionID[this.menuActionRow] = 956;
                        this.menuActionCmd1[this.menuActionRow] = l;
                        this.menuActionCmd2[this.menuActionRow] = i1;
                        this.menuActionCmd3[this.menuActionRow] = j1;
                        this.menuActionRow++;
                    }
                } else
                {
                    if(class46.actions != null)
                    {
                        for(int i2 = 4; i2 >= 0; i2--)
                            if(class46.actions[i2] != null)
                            {
                                this.menuActionName[this.menuActionRow] = class46.actions[i2] + " @cya@" + class46.name;
                                if(i2 == 0)
                                    this.menuActionID[this.menuActionRow] = 502;
                                if(i2 == 1)
                                    this.menuActionID[this.menuActionRow] = 900;
                                if(i2 == 2)
                                    this.menuActionID[this.menuActionRow] = 113;
                                if(i2 == 3)
                                    this.menuActionID[this.menuActionRow] = 872;
                                if(i2 == 4)
                                    this.menuActionID[this.menuActionRow] = 1062;
                                this.menuActionCmd1[this.menuActionRow] = l;
                                this.menuActionCmd2[this.menuActionRow] = i1;
                                this.menuActionCmd3[this.menuActionRow] = j1;
                                this.menuActionRow++;
                            }

                    }
                    this.menuActionName[this.menuActionRow] = "Examine @cya@" + class46.name + " @gre@(@whi@" + l1 + "@gre@) (@whi@" + (i1 + this.baseX) + "," + (j1 + this.baseY) + "@gre@)";
                    this.menuActionID[this.menuActionRow] = 1226;
                    this.menuActionCmd1[this.menuActionRow] = class46.type << 14;
                    this.menuActionCmd2[this.menuActionRow] = i1;
                    this.menuActionCmd3[this.menuActionRow] = j1;
                    this.menuActionRow++;
                }
            }
            if(k1 == 1)
            {
                NPC npc = this.npcArray[l1];
                if(npc.desc.aByte68 == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64)
                {
                    for(int j2 = 0; j2 < this.npcCount; j2++)
                    {
                        NPC npc2 = this.npcArray[this.npcIndices[j2]];
                        if(npc2 != null && npc2 != npc && npc2.desc.aByte68 == 1 && npc2.x == npc.x && npc2.y == npc.y)
                            this.buildAtNPCMenu(npc2.desc, this.npcIndices[j2], j1, i1);
                    }

                    for(int l2 = 0; l2 < this.playerCount; l2++)
                    {
                        Player player = this.playerArray[this.playerIndices[l2]];
                        if(player != null && player.x == npc.x && player.y == npc.y)
                            this.buildAtPlayerMenu(i1, this.playerIndices[l2], player, j1);
                    }

                }
                this.buildAtNPCMenu(npc.desc, l1, j1, i1);
            }
            if(k1 == 0)
            {
                Player player = this.playerArray[l1];
                if((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64)
                {
                    for(int k2 = 0; k2 < this.npcCount; k2++)
                    {
                        NPC class30_sub2_sub4_sub1_sub1_2 = this.npcArray[this.npcIndices[k2]];
                        if(class30_sub2_sub4_sub1_sub1_2 != null && class30_sub2_sub4_sub1_sub1_2.desc.aByte68 == 1 && class30_sub2_sub4_sub1_sub1_2.x == player.x && class30_sub2_sub4_sub1_sub1_2.y == player.y)
                            this.buildAtNPCMenu(class30_sub2_sub4_sub1_sub1_2.desc, this.npcIndices[k2], j1, i1);
                    }

                    for(int i3 = 0; i3 < this.playerCount; i3++)
                    {
                        Player class30_sub2_sub4_sub1_sub2_2 = this.playerArray[this.playerIndices[i3]];
                        if(class30_sub2_sub4_sub1_sub2_2 != null && class30_sub2_sub4_sub1_sub2_2 != player && class30_sub2_sub4_sub1_sub2_2.x == player.x && class30_sub2_sub4_sub1_sub2_2.y == player.y)
                            this.buildAtPlayerMenu(i1, this.playerIndices[i3], class30_sub2_sub4_sub1_sub2_2, j1);
                    }

                }
                this.buildAtPlayerMenu(i1, l1, player, j1);
            }
            if(k1 == 3)
            {
                Deque class19 = this.levelObjects[this.plane][i1][j1];
                if(class19 != null)
                {
                    for(Item item = (Item)class19.getTail(); item != null; item = (Item)class19.getPrevious())
                    {
                        ItemDef itemDef = ItemDef.forID(item.id);
                        if(this.itemSelected == 1)
                        {
                            this.menuActionName[this.menuActionRow] = "Use " + this.selectedItemName + " with @lre@" + itemDef.name;
                            this.menuActionID[this.menuActionRow] = 511;
                            this.menuActionCmd1[this.menuActionRow] = item.id;
                            this.menuActionCmd2[this.menuActionRow] = i1;
                            this.menuActionCmd3[this.menuActionRow] = j1;
                            this.menuActionRow++;
                        } else
                        if(this.spellSelected == 1)
                        {
                            if((this.spellUsableOn & 1) == 1)
                            {
                                this.menuActionName[this.menuActionRow] = this.spellTooltip + " @lre@" + itemDef.name;
                                this.menuActionID[this.menuActionRow] = 94;
                                this.menuActionCmd1[this.menuActionRow] = item.id;
                                this.menuActionCmd2[this.menuActionRow] = i1;
                                this.menuActionCmd3[this.menuActionRow] = j1;
                                this.menuActionRow++;
                            }
                        } else
                        {
                            for(int j3 = 4; j3 >= 0; j3--)
                                if(itemDef.groundActions != null && itemDef.groundActions[j3] != null)
                                {
                                    this.menuActionName[this.menuActionRow] = itemDef.groundActions[j3] + " @lre@" + itemDef.name;
                                    if(j3 == 0)
                                        this.menuActionID[this.menuActionRow] = 652;
                                    if(j3 == 1)
                                        this.menuActionID[this.menuActionRow] = 567;
                                    if(j3 == 2)
                                        this.menuActionID[this.menuActionRow] = 234;
                                    if(j3 == 3)
                                        this.menuActionID[this.menuActionRow] = 244;
                                    if(j3 == 4)
                                        this.menuActionID[this.menuActionRow] = 213;
                                    this.menuActionCmd1[this.menuActionRow] = item.id;
                                    this.menuActionCmd2[this.menuActionRow] = i1;
                                    this.menuActionCmd3[this.menuActionRow] = j1;
                                    this.menuActionRow++;
                                } else
                                if(j3 == 2)
                                {
                                    this.menuActionName[this.menuActionRow] = "Take @lre@" + itemDef.name;
                                    this.menuActionID[this.menuActionRow] = 234;
                                    this.menuActionCmd1[this.menuActionRow] = item.id;
                                    this.menuActionCmd2[this.menuActionRow] = i1;
                                    this.menuActionCmd3[this.menuActionRow] = j1;
                                    this.menuActionRow++;
                                }

                            this.menuActionName[this.menuActionRow] = "Examine @lre@" + itemDef.name + " @gre@(@whi@" + item.id + "@gre@)";
                            this.menuActionID[this.menuActionRow] = 1448;
                            this.menuActionCmd1[this.menuActionRow] = item.id;
                            this.menuActionCmd2[this.menuActionRow] = i1;
                            this.menuActionCmd3[this.menuActionRow] = j1;
                            this.menuActionRow++;
                        }
                    }

                }
            }
        }
    }

	@Override
	public void cleanUpForQuit()
    {
        signlink.reporterror = false;
        try
        {
            if(this.socketStream != null)
                this.socketStream.close();
        }
        catch(Exception _ex) { }
        this.socketStream = null;
        this.stopMidi();
        if(this.mouseDetection != null)
            this.mouseDetection.running = false;
        this.mouseDetection = null;
        this.onDemandFetcher.disable();
        this.onDemandFetcher = null;
        this.aStream_834 = null;
        this.stream = null;
        this.aStream_847 = null;
        this.inBuffer = null;
        this.anIntArray1234 = null;
        this.aByteArrayArray1183 = null;
        this.aByteArrayArray1247 = null;
        this.anIntArray1235 = null;
        this.anIntArray1236 = null;
        this.intGroundArray = null;
        this.byteGroundArray = null;
        this.worldController = null;
        this.aClass11Array1230 = null;
        this.pathfinder.kill();
        this.pathfinder = null;
        this.aByteArray912 = null;
        this.resetGameframe();
        this.invBack = null;
        this.mapBack = null;
        this.chatBack = null;
        this.backBase1 = null;
        this.backBase2 = null;
        this.backHmid1 = null;
        this.sideIcons = null;
        this.redStone1 = null;
        this.redStone2 = null;
        this.redStone3 = null;
        this.redStone1_2 = null;
        this.redStone2_2 = null;
        this.redStone1_3 = null;
        this.redStone2_3 = null;
        this.redStone3_2 = null;
        this.redStone1_4 = null;
        this.redStone2_4 = null;
        this.compass = null;
        this.hitMarks = null;
        this.headIcons = null;
        this.crosses = null;
        this.mapDotItem = null;
        this.mapDotNPC = null;
        this.mapDotPlayer = null;
        this.mapDotFriend = null;
        this.mapDotTeam = null;
        this.mapScenes = null;
        this.mapFunctions = null;
        this.anIntArrayArray929 = null;
        this.playerArray = null;
        this.playerIndices = null;
        this.anIntArray894 = null;
        this.aBufferArray895s = null;
        this.anIntArray840 = null;
        this.npcArray = null;
        this.npcIndices = null;
        this.levelObjects = null;
        this.aClass19_1179 = null;
        this.projectiles = null;
        this.aClass19_1056 = null;
        this.menuActionCmd2 = null;
        this.menuActionCmd3 = null;
        this.menuActionID = null;
        this.menuActionCmd1 = null;
        this.menuActionName = null;
        this.variousSettings = null;
        this.minimapFunctionX = null;
        this.minimapFunctionY = null;
        this.minimapFunctions = null;
        this.minimapImage = null;
        this.friendsList = null;
        this.friendsListAsLongs = null;
        this.friendsNodeIDs = null;
        Client.aRSImageProducer_1109 = null;
        ObjectDef.nullLoader();
        EntityDef.nullLoader();
        ItemDef.nullLoader();
        Flo.cache = null;
        IDK.cache = null;
        RSInterface.interfaceCache = null;
        Animation.animations = null;
        Graphic.graphics = null;
        AnimableRenderer.spotanimcache = null;
        VariableParameter.destroy();
        Player.mruNodes = null;
        Texture.nullLoader();
        WorldController.nullLoader();
        Model.nullLoader();
        ModelRenderer.nullLoader();
        Frame.clearFrames();
        System.gc();
    }

    private void printDebug()
    {
        System.out.println("============");
        System.out.println("flame-cycle:" + this.flameTick);
        if(this.onDemandFetcher != null)
            System.out.println("Od-cycle:" + this.onDemandFetcher.onDemandCycle);
        System.out.println("loop-cycle:" + Client.loopCycle);
        System.out.println("draw-cycle:" + Client.anInt1061);
        System.out.println("ptype:" + this.pktType);
        System.out.println("psize:" + this.pktSize);
        if(this.socketStream != null)
            this.socketStream.printDebug();
        this.applet.shouldDebug = true;
    }

    private void method73()
    {
        do
        {
            int j = this.applet.readChar(-796);
            if(j == -1)
                break;
            if(this.openInterfaceID != -1 && this.openInterfaceID == this.reportAbuseInterfaceID)
            {
                if(j == 8 && this.reportAbuseInput.length() > 0)
                    this.reportAbuseInput = this.reportAbuseInput.substring(0, this.reportAbuseInput.length() - 1);
                if((j >= 97 && j <= 122 || j >= 65 && j <= 90 || j >= 48 && j <= 57 || j == 32) && this.reportAbuseInput.length() < 12)
                    this.reportAbuseInput += (char)j;
            } else
            if(this.messagePromptRaised)
            {
                if(j >= 32 && j <= 122 && this.promptInput.length() < 80)
                {
                    this.promptInput += (char)j;
                    this.inputTaken = true;
                }
                if(j == 8 && this.promptInput.length() > 0)
                {
                    this.promptInput = this.promptInput.substring(0, this.promptInput.length() - 1);
                    this.inputTaken = true;
                }
                if(j == 13 || j == 10)
                {
                    this.messagePromptRaised = false;
                    this.inputTaken = true;
                    if(this.friendsListAction == 1)
                    {
                        long l = TextClass.longForName(this.promptInput);
                        this.addFriend(l);
                    }
                    if(this.friendsListAction == 2 && this.friendsCount > 0)
                    {
                        long l1 = TextClass.longForName(this.promptInput);
                        this.delFriend(l1);
                    }
                    if(this.friendsListAction == 3 && this.promptInput.length() > 0)
                    {
                        this.stream.writeOpcode(126);
                        this.stream.writeByte(0);
                        int k = this.stream.position;
                        this.stream.writeLong(this.aLong953);
                        TextInput.method526(this.promptInput, this.stream);
                        this.stream.writeSizeByte(this.stream.position - k);
                        this.promptInput = TextInput.processText(this.promptInput);
                        this.promptInput = Censor.doCensor(this.promptInput);
                        this.pushMessage(this.promptInput, 6, TextClass.fixName(TextClass.nameForLong(this.aLong953)));
                        if(this.privateChatMode == 2)
                        {
                            this.privateChatMode = 1;
                            this.aBoolean1233 = true;
                            this.stream.writeOpcode(95);
                            this.stream.writeByte(this.publicChatMode);
                            this.stream.writeByte(this.privateChatMode);
                            this.stream.writeByte(this.tradeMode);
                        }
                    }
                    if(this.friendsListAction == 4 && this.ignoreCount < 100)
                    {
                        long l2 = TextClass.longForName(this.promptInput);
                        this.addIgnore(l2);
                    }
                    if(this.friendsListAction == 5 && this.ignoreCount > 0)
                    {
                        long l3 = TextClass.longForName(this.promptInput);
                        this.delIgnore(l3);
                    }
                }
            } else
            if(this.inputDialogState == 1)
            {
                if(j >= 48 && j <= 57 && this.amountOrNameInput.length() < 10)
                {
                    this.amountOrNameInput += (char)j;
                    this.inputTaken = true;
                }
                if(j == 8 && this.amountOrNameInput.length() > 0)
                {
                    this.amountOrNameInput = this.amountOrNameInput.substring(0, this.amountOrNameInput.length() - 1);
                    this.inputTaken = true;
                }
                if(j == 13 || j == 10)
                {
                    if(this.amountOrNameInput.length() > 0)
                    {
                        int i1 = 0;
                        try
                        {
                            i1 = Integer.parseInt(this.amountOrNameInput);
                        }
                        catch(Exception _ex) { }
                        this.stream.writeOpcode(208);
                        this.stream.writeInt(i1);
                    }
                    this.inputDialogState = 0;
                    this.inputTaken = true;
                }
            } else
            if(this.inputDialogState == 2)
            {
                if(j >= 32 && j <= 122 && this.amountOrNameInput.length() < 12)
                {
                    this.amountOrNameInput += (char)j;
                    this.inputTaken = true;
                }
                if(j == 8 && this.amountOrNameInput.length() > 0)
                {
                    this.amountOrNameInput = this.amountOrNameInput.substring(0, this.amountOrNameInput.length() - 1);
                    this.inputTaken = true;
                }
                if(j == 13 || j == 10)
                {
                    if(this.amountOrNameInput.length() > 0)
                    {
                        this.stream.writeOpcode(60);
                        this.stream.writeLong(TextClass.longForName(this.amountOrNameInput));
                    }
                    this.inputDialogState = 0;
                    this.inputTaken = true;
                }
            } else
            if(this.backDialogID == -1)
            {
                if(j >= 32 && j <= 122 && this.inputString.length() < 80)
                {
                    this.inputString += (char)j;
                    this.inputTaken = true;
                }
                if(j == 8 && this.inputString.length() > 0)
                {
                    this.inputString = this.inputString.substring(0, this.inputString.length() - 1);
                    this.inputTaken = true;
                }
                if((j == 13 || j == 10) && this.inputString.length() > 0)
                {
					if(this.inputString.equals("::fixed")) {
						this.frameMode(ScreenMode.FIXED);
					}
					if(this.inputString.equals("::resize")) {
						this.frameMode(ScreenMode.RESIZABLE);
					}
					if(this.inputString.equals("::full")) {
						this.frameMode(ScreenMode.FULLSCREEN);
					}
                    if(this.inputString.equals("::fpson"))
                        Client.fpsOn = true;
                    if(this.inputString.equals("::fpsoff"))
                        Client.fpsOn = false;
                    if(this.inputString.equals("::lag"))
                        this.printDebug();
                    if(this.myPrivilege == 2)
                    {
                      if(this.inputString.equals("::clientdrop"))
                            this.dropClient();

                        if(this.inputString.equals("::prefetchmusic"))
                        {
                            for(int j1 = 0; j1 < this.onDemandFetcher.getVersionCount(2); j1++)
                                this.onDemandFetcher.method563((byte)1, 2, j1);

                        }

                        if(this.inputString.equals("::noclip"))
                        {
                            for(int k1 = 0; k1 < 4; k1++)
                            {
                                for(int i2 = 1; i2 < 103; i2++)
                                {
                                    for(int k2 = 1; k2 < 103; k2++)
                                        this.aClass11Array1230[k1].flags[i2][k2] = 0;

                                }

                            }

                        }
                    }
                    if(this.inputString.startsWith("::"))
                    {
                        this.stream.writeOpcode(103);
                        this.stream.writeByte(this.inputString.length() - 1);
                        this.stream.writeJString(this.inputString.substring(2));
                    } else
                    {
                        String s = this.inputString.toLowerCase();	
                        int j2 = 0;
                        if(s.startsWith("yellow:"))
                        {
                            j2 = 0;
                            this.inputString = this.inputString.substring(7);
                        } else
                        if(s.startsWith("red:"))
                        {
                            j2 = 1;
                            this.inputString = this.inputString.substring(4);
                        } else
                        if(s.startsWith("green:"))
                        {
                            j2 = 2;
                            this.inputString = this.inputString.substring(6);
                        } else
                        if(s.startsWith("cyan:"))
                        {
                            j2 = 3;
                            this.inputString = this.inputString.substring(5);
                        } else
                        if(s.startsWith("purple:"))
                        {
                            j2 = 4;
                            this.inputString = this.inputString.substring(7);
                        } else
                        if(s.startsWith("white:"))
                        {
                            j2 = 5;
                            this.inputString = this.inputString.substring(6);
                        } else
                        if(s.startsWith("flash1:"))
                        {
                            j2 = 6;
                            this.inputString = this.inputString.substring(7);
                        } else
                        if(s.startsWith("flash2:"))
                        {
                            j2 = 7;
                            this.inputString = this.inputString.substring(7);
                        } else
                        if(s.startsWith("flash3:"))
                        {
                            j2 = 8;
                            this.inputString = this.inputString.substring(7);
                        } else
                        if(s.startsWith("glow1:"))
                        {
                            j2 = 9;
                            this.inputString = this.inputString.substring(6);
                        } else
                        if(s.startsWith("glow2:"))
                        {
                            j2 = 10;
                            this.inputString = this.inputString.substring(6);
                        } else
                        if(s.startsWith("glow3:"))
                        {
                            j2 = 11;
                            this.inputString = this.inputString.substring(6);
                        }
                        s = this.inputString.toLowerCase();
                        int i3 = 0;
                        if(s.startsWith("wave:"))
                        {
                            i3 = 1;
                            this.inputString = this.inputString.substring(5);
                        } else
                        if(s.startsWith("wave2:"))
                        {
                            i3 = 2;
                            this.inputString = this.inputString.substring(6);
                        } else
                        if(s.startsWith("shake:"))
                        {
                            i3 = 3;
                            this.inputString = this.inputString.substring(6);
                        } else
                        if(s.startsWith("scroll:"))
                        {
                            i3 = 4;
                            this.inputString = this.inputString.substring(7);
                        } else
                        if(s.startsWith("slide:"))
                        {
                            i3 = 5;
                            this.inputString = this.inputString.substring(6);
                        }
                        this.stream.writeOpcode(4);
                        this.stream.writeByte(0);
                        int j3 = this.stream.position;
                        this.stream.writeByteS(i3);
                        this.stream.writeByteS(j2);
                        this.aStream_834.position = 0;
                        TextInput.method526(this.inputString, this.aStream_834);
                        this.stream.writeReverseDataA(0, this.aStream_834.payload, this.aStream_834.position);
                        this.stream.writeSizeByte(this.stream.position - j3);
                        this.inputString = TextInput.processText(this.inputString);
                        this.inputString = Censor.doCensor(this.inputString);
                        Client.localPlayer.textSpoken = this.inputString;
                        Client.localPlayer.anInt1513 = j2;
                        Client.localPlayer.anInt1531 = i3;
                        Client.localPlayer.textCycle = 150;
                        if(this.myPrivilege == 2)
                            this.pushMessage(Client.localPlayer.textSpoken, 2, "@cr2@" + Client.localPlayer.name);
                        else
                        if(this.myPrivilege == 1)
                            this.pushMessage(Client.localPlayer.textSpoken, 2, "@cr1@" + Client.localPlayer.name);
                        else
                            this.pushMessage(Client.localPlayer.textSpoken, 2, Client.localPlayer.name);
                        if(this.publicChatMode == 2)
                        {
                            this.publicChatMode = 3;
                            this.aBoolean1233 = true;
                            this.stream.writeOpcode(95);
                            this.stream.writeByte(this.publicChatMode);
                            this.stream.writeByte(this.privateChatMode);
                            this.stream.writeByte(this.tradeMode);
                        }
                    }
                    this.inputString = "";
                    this.inputTaken = true;
                }
            }
        } while(true);
    }

    private void buildChatAreaMenu(int j)
    {
        int l = 0;
        for(int i1 = 0; i1 < 100; i1++)
        {
            if(this.chatMessages[i1] == null)
                continue;
            int j1 = this.chatTypes[i1];
            int k1 = (70 - l * 14) + this.anInt1089 + 4;
            if(k1 < -20)
                break;
            String s = this.chatNames[i1];
            boolean flag = false;
            if(s != null && s.startsWith("@cr1@"))
            {
                s = s.substring(5);
                boolean flag1 = true;
            }
            if(s != null && s.startsWith("@cr2@"))
            {
                s = s.substring(5);
                byte byte0 = 2;
            }
            if(j1 == 0)
                l++;
            if((j1 == 1 || j1 == 2) && (j1 == 1 || this.publicChatMode == 0 || this.publicChatMode == 1 && this.isFriendOrSelf(s)))
            {
                if(j > k1 - 14 && j <= k1 && !s.equals(Client.localPlayer.name))
                {
                    if(this.myPrivilege >= 1)
                    {
                        this.menuActionName[this.menuActionRow] = "Report abuse @whi@" + s;
                        this.menuActionID[this.menuActionRow] = 606;
                        this.menuActionRow++;
                    }
                    this.menuActionName[this.menuActionRow] = "Add ignore @whi@" + s;
                    this.menuActionID[this.menuActionRow] = 42;
                    this.menuActionRow++;
                    this.menuActionName[this.menuActionRow] = "Add friend @whi@" + s;
                    this.menuActionID[this.menuActionRow] = 337;
                    this.menuActionRow++;
                }
                l++;
            }
            if((j1 == 3 || j1 == 7) && this.splitPrivateChat == 0 && (j1 == 7 || this.privateChatMode == 0 || this.privateChatMode == 1 && this.isFriendOrSelf(s)))
            {
                if(j > k1 - 14 && j <= k1)
                {
                    if(this.myPrivilege >= 1)
                    {
                        this.menuActionName[this.menuActionRow] = "Report abuse @whi@" + s;
                        this.menuActionID[this.menuActionRow] = 606;
                        this.menuActionRow++;
                    }
                    this.menuActionName[this.menuActionRow] = "Add ignore @whi@" + s;
                    this.menuActionID[this.menuActionRow] = 42;
                    this.menuActionRow++;
                    this.menuActionName[this.menuActionRow] = "Add friend @whi@" + s;
                    this.menuActionID[this.menuActionRow] = 337;
                    this.menuActionRow++;
                }
                l++;
            }
            if(j1 == 4 && (this.tradeMode == 0 || this.tradeMode == 1 && this.isFriendOrSelf(s)))
            {
                if(j > k1 - 14 && j <= k1)
                {
                    this.menuActionName[this.menuActionRow] = "Accept trade @whi@" + s;
                    this.menuActionID[this.menuActionRow] = 484;
                    this.menuActionRow++;
                }
                l++;
            }
            if((j1 == 5 || j1 == 6) && this.splitPrivateChat == 0 && this.privateChatMode < 2)
                l++;
            if(j1 == 8 && (this.tradeMode == 0 || this.tradeMode == 1 && this.isFriendOrSelf(s)))
            {
                if(j > k1 - 14 && j <= k1)
                {
                    this.menuActionName[this.menuActionRow] = "Accept challenge @whi@" + s;
                    this.menuActionID[this.menuActionRow] = 6;
                    this.menuActionRow++;
                }
                l++;
            }
        }

    }

    private void drawFriendsListOrWelcomeScreen(RSInterface class9)
    {
        int j = class9.anInt214;
        if(j >= 1 && j <= 100 || j >= 701 && j <= 800)
        {
            if(j == 1 && this.anInt900 == 0)
            {
                class9.message = "Loading friend list";
                class9.atActionType = 0;
                return;
            }
            if(j == 1 && this.anInt900 == 1)
            {
                class9.message = "Connecting to friendserver";
                class9.atActionType = 0;
                return;
            }
            if(j == 2 && this.anInt900 != 2)
            {
                class9.message = "Please wait...";
                class9.atActionType = 0;
                return;
            }
            int k = this.friendsCount;
            if(this.anInt900 != 2)
                k = 0;
            if(j > 700)
                j -= 601;
            else
                j--;
            if(j >= k)
            {
                class9.message = "";
                class9.atActionType = 0;
                return;
            } else
            {
                class9.message = this.friendsList[j];
                class9.atActionType = 1;
                return;
            }
        }
        if(j >= 101 && j <= 200 || j >= 801 && j <= 900)
        {
            int l = this.friendsCount;
            if(this.anInt900 != 2)
                l = 0;
            if(j > 800)
                j -= 701;
            else
                j -= 101;
            if(j >= l)
            {
                class9.message = "";
                class9.atActionType = 0;
                return;
            }
            if(this.friendsNodeIDs[j] == 0)
                class9.message = "@red@Offline";
            else
            if(this.friendsNodeIDs[j] == Client.nodeID)
                class9.message = "@gre@World-" + (this.friendsNodeIDs[j] - 9);
            else
                class9.message = "@yel@World-" + (this.friendsNodeIDs[j] - 9);
            class9.atActionType = 1;
            return;
        }
        if(j == 203)
        {
            int i1 = this.friendsCount;
            if(this.anInt900 != 2)
                i1 = 0;
            class9.scrollMax = i1 * 15 + 20;
            if(class9.scrollMax <= class9.height)
                class9.scrollMax = class9.height + 1;
            return;
        }
        if(j >= 401 && j <= 500)
        {
            if((j -= 401) == 0 && this.anInt900 == 0)
            {
                class9.message = "Loading ignore list";
                class9.atActionType = 0;
                return;
            }
            if(j == 1 && this.anInt900 == 0)
            {
                class9.message = "Please wait...";
                class9.atActionType = 0;
                return;
            }
            int j1 = this.ignoreCount;
            if(this.anInt900 == 0)
                j1 = 0;
            if(j >= j1)
            {
                class9.message = "";
                class9.atActionType = 0;
                return;
            } else
            {
                class9.message = TextClass.fixName(TextClass.nameForLong(this.ignoreListAsLongs[j]));
                class9.atActionType = 1;
                return;
            }
        }
        if(j == 503)
        {
            class9.scrollMax = this.ignoreCount * 15 + 20;
            if(class9.scrollMax <= class9.height)
                class9.scrollMax = class9.height + 1;
            return;
        }
        if(j == 327)
        {
            class9.anInt270 = 150;
            class9.anInt271 = (int)(Math.sin(Client.loopCycle / 40D) * 256D) & 0x7ff;
            if(this.aBoolean1031)
            {
                for(int k1 = 0; k1 < 7; k1++)
                {
                    int l1 = this.anIntArray1065[k1];
                    if(l1 >= 0 && !IDK.cache[l1].method537())
                        return;
                }

                this.aBoolean1031 = false;
                Model aclass30_sub2_sub4_sub6s[] = new Model[7];
                int i2 = 0;
                for(int j2 = 0; j2 < 7; j2++)
                {
                    int k2 = this.anIntArray1065[j2];
                    if(k2 >= 0)
                        aclass30_sub2_sub4_sub6s[i2++] = IDK.cache[k2].method538();
                }

                Model model = new Model(i2, aclass30_sub2_sub4_sub6s);
                for(int l2 = 0; l2 < 5; l2++)
                    if(this.anIntArray990[l2] != 0)
                    {
                        model.method476(Client.anIntArrayArray1003[l2][0], Client.anIntArrayArray1003[l2][this.anIntArray990[l2]]);
                        if(l2 == 1)
                            model.method476(Client.anIntArray1204[0], Client.anIntArray1204[this.anIntArray990[l2]]);
                    }

                model.method469();
                model.method470(Animation.animations[Client.localPlayer.anInt1511].primaryFrames[0]);
                model.method479(64, 850, -30, -50, -30, true);
                class9.anInt233 = 5;
                class9.mediaID = 0;
                RSInterface.method208(this.aBoolean994, model);
            }
            return;
        }
        if(j == 324)
        {
            if(this.aClass30_Sub2_Sub1_Sub1_931 == null)
            {
                this.aClass30_Sub2_Sub1_Sub1_931 = class9.sprite1;
                this.aClass30_Sub2_Sub1_Sub1_932 = class9.sprite2;
            }
            if(this.aBoolean1047)
            {
                class9.sprite1 = this.aClass30_Sub2_Sub1_Sub1_932;
                return;
            } else
            {
                class9.sprite1 = this.aClass30_Sub2_Sub1_Sub1_931;
                return;
            }
        }
        if(j == 325)
        {
            if(this.aClass30_Sub2_Sub1_Sub1_931 == null)
            {
                this.aClass30_Sub2_Sub1_Sub1_931 = class9.sprite1;
                this.aClass30_Sub2_Sub1_Sub1_932 = class9.sprite2;
            }
            if(this.aBoolean1047)
            {
                class9.sprite1 = this.aClass30_Sub2_Sub1_Sub1_931;
                return;
            } else
            {
                class9.sprite1 = this.aClass30_Sub2_Sub1_Sub1_932;
                return;
            }
        }
        if(j == 600)
        {
            class9.message = this.reportAbuseInput;
            if(Client.loopCycle % 20 < 10)
            {
                class9.message += "|";
                return;
            } else
            {
                class9.message += " ";
                return;
            }
        }
        if(j == 613)
            if(this.myPrivilege >= 1)
            {
                if(this.canMute)
                {
                    class9.textColor = 0xff0000;
                    class9.message = "Moderator option: Mute player for 48 hours: <ON>";
                } else
                {
                    class9.textColor = 0xffffff;
                    class9.message = "Moderator option: Mute player for 48 hours: <OFF>";
                }
            } else
            {
                class9.message = "";
            }
        if(j == 650 || j == 655)
            if(this.anInt1193 != 0)
            {
                String s;
                if(this.daysSinceLastLogin == 0)
                    s = "earlier today";
                else
                if(this.daysSinceLastLogin == 1)
                    s = "yesterday";
                else
                    s = this.daysSinceLastLogin + " days ago";
                class9.message = "You last logged in " + s + " from: " + signlink.dns;
            } else
            {
                class9.message = "";
            }
        if(j == 651)
        {
            if(this.unreadMessages == 0)
            {
                class9.message = "0 unread messages";
                class9.textColor = 0xffff00;
            }
            if(this.unreadMessages == 1)
            {
                class9.message = "1 unread message";
                class9.textColor = 65280;
            }
            if(this.unreadMessages > 1)
            {
                class9.message = this.unreadMessages + " unread messages";
                class9.textColor = 65280;
            }
        }
        if(j == 652)
            if(this.daysSinceRecovChange == 201)
            {
                if(this.membersInt == 1)
                    class9.message = "@yel@This is a non-members world: @whi@Since you are a member we";
                else
                    class9.message = "";
            } else
            if(this.daysSinceRecovChange == 200)
            {
                class9.message = "You have not yet set any password recovery questions.";
            } else
            {
                String s1;
                if(this.daysSinceRecovChange == 0)
                    s1 = "Earlier today";
                else
                if(this.daysSinceRecovChange == 1)
                    s1 = "Yesterday";
                else
                    s1 = this.daysSinceRecovChange + " days ago";
                class9.message = s1 + " you changed your recovery questions";
            }
        if(j == 653)
            if(this.daysSinceRecovChange == 201)
            {
                if(this.membersInt == 1)
                    class9.message = "@whi@recommend you use a members world instead. You may use";
                else
                    class9.message = "";
            } else
            if(this.daysSinceRecovChange == 200)
                class9.message = "We strongly recommend you do so now to secure your account.";
            else
                class9.message = "If you do not remember making this change then cancel it immediately";
        if(j == 654)
        {
            if(this.daysSinceRecovChange == 201)
                if(this.membersInt == 1)
                {
                    class9.message = "@whi@this world but member benefits are unavailable whilst here.";
                    return;
                } else
                {
                    class9.message = "";
                    return;
                }
            if(this.daysSinceRecovChange == 200)
            {
                class9.message = "Do this from the 'account management' area on our front webpage";
                return;
            }
            class9.message = "Do this from the 'account management' area on our front webpage";
        }
    }

    private void drawSplitPrivateChat()
    {
        if(this.splitPrivateChat == 0)
            return;
        TextDrawingArea textDrawingArea = this.aTextDrawingArea_1271;
        int i = 0;
        if(this.anInt1104 != 0)
            i = 1;
        for(int j = 0; j < 100; j++)
            if(this.chatMessages[j] != null)
            {
                int k = this.chatTypes[j];
                String s = this.chatNames[j];
                byte byte1 = 0;
                if(s != null && s.startsWith("@cr1@"))
                {
                    s = s.substring(5);
                    byte1 = 1;
                }
                if(s != null && s.startsWith("@cr2@"))
                {
                    s = s.substring(5);
                    byte1 = 2;
                }
                if((k == 3 || k == 7) && (k == 7 || this.privateChatMode == 0 || this.privateChatMode == 1 && this.isFriendOrSelf(s)))
                {
                    int l = 329 - i * 13;
                    int k1 = 4;
                    textDrawingArea.method385(0, "From", l, k1);
                    textDrawingArea.method385(65535, "From", l - 1, k1);
                    k1 += textDrawingArea.getTextWidth("From ");
                    if(byte1 == 1)
                    {
                        IndexedImageRenderer.draw(this.modIcons[0], k1, l - 12);
                        k1 += 14;
                    }
                    if(byte1 == 2)
                    {
                        IndexedImageRenderer.draw(this.modIcons[1], k1, l - 12);
                        k1 += 14;
                    }
                    textDrawingArea.method385(0, s + ": " + this.chatMessages[j], l, k1);
                    textDrawingArea.method385(65535, s + ": " + this.chatMessages[j], l - 1, k1);
                    if(++i >= 5)
                        return;
                }
                if(k == 5 && this.privateChatMode < 2)
                {
                    int i1 = 329 - i * 13;
                    textDrawingArea.method385(0, this.chatMessages[j], i1, 4);
                    textDrawingArea.method385(65535, this.chatMessages[j], i1 - 1, 4);
                    if(++i >= 5)
                        return;
                }
                if(k == 6 && this.privateChatMode < 2)
                {
                    int j1 = 329 - i * 13;
                    textDrawingArea.method385(0, "To " + s + ": " + this.chatMessages[j], j1, 4);
                    textDrawingArea.method385(65535, "To " + s + ": " + this.chatMessages[j], j1 - 1, 4);
                    if(++i >= 5)
                        return;
                }
            }

    }

    private void pushMessage(String s, int i, String s1)
    {
        if(i == 0 && this.dialogID != -1)
        {
            this.aString844 = s;
            this.applet.clickMode3 = 0;
        }
        if(this.backDialogID == -1)
            this.inputTaken = true;
        for(int j = 99; j > 0; j--)
        {
            this.chatTypes[j] = this.chatTypes[j - 1];
            this.chatNames[j] = this.chatNames[j - 1];
            this.chatMessages[j] = this.chatMessages[j - 1];
        }

        this.chatTypes[0] = i;
        this.chatNames[0] = s1;
        this.chatMessages[0] = s;
    }

    private void processTabClick()
    {
        if(this.applet.clickMode3 == 1)
        {
            if(this.applet.saveClickX >= 539 && this.applet.saveClickX <= 573 && this.applet.saveClickY >= 169 && this.applet.saveClickY < 205 && this.tabInterfaceIDs[0] != -1)
            {
                this.needDrawTabArea = true;
                this.tabID = 0;
                this.tabAreaAltered = true;
            }
            if(this.applet.saveClickX >= 569 && this.applet.saveClickX <= 599 && this.applet.saveClickY >= 168 && this.applet.saveClickY < 205 && this.tabInterfaceIDs[1] != -1)
            {
                this.needDrawTabArea = true;
                this.tabID = 1;
                this.tabAreaAltered = true;
            }
            if(this.applet.saveClickX >= 597 && this.applet.saveClickX <= 627 && this.applet.saveClickY >= 168 && this.applet.saveClickY < 205 && this.tabInterfaceIDs[2] != -1)
            {
                this.needDrawTabArea = true;
                this.tabID = 2;
                this.tabAreaAltered = true;
            }
            if(this.applet.saveClickX >= 625 && this.applet.saveClickX <= 669 && this.applet.saveClickY >= 168 && this.applet.saveClickY < 203 && this.tabInterfaceIDs[3] != -1)
            {
                this.needDrawTabArea = true;
                this.tabID = 3;
                this.tabAreaAltered = true;
            }
            if(this.applet.saveClickX >= 666 && this.applet.saveClickX <= 696 && this.applet.saveClickY >= 168 && this.applet.saveClickY < 205 && this.tabInterfaceIDs[4] != -1)
            {
                this.needDrawTabArea = true;
                this.tabID = 4;
                this.tabAreaAltered = true;
            }
            if(this.applet.saveClickX >= 694 && this.applet.saveClickX <= 724 && this.applet.saveClickY >= 168 && this.applet.saveClickY < 205 && this.tabInterfaceIDs[5] != -1)
            {
                this.needDrawTabArea = true;
                this.tabID = 5;
                this.tabAreaAltered = true;
            }
            if(this.applet.saveClickX >= 722 && this.applet.saveClickX <= 756 && this.applet.saveClickY >= 169 && this.applet.saveClickY < 205 && this.tabInterfaceIDs[6] != -1)
            {
                this.needDrawTabArea = true;
                this.tabID = 6;
                this.tabAreaAltered = true;
            }
            if(this.applet.saveClickX >= 540 && this.applet.saveClickX <= 574 && this.applet.saveClickY >= 466 && this.applet.saveClickY < 502 && this.tabInterfaceIDs[7] != -1)
            {
                this.needDrawTabArea = true;
                this.tabID = 7;
                this.tabAreaAltered = true;
            }
            if(this.applet.saveClickX >= 572 && this.applet.saveClickX <= 602 && this.applet.saveClickY >= 466 && this.applet.saveClickY < 503 && this.tabInterfaceIDs[8] != -1)
            {
                this.needDrawTabArea = true;
                this.tabID = 8;
                this.tabAreaAltered = true;
            }
            if(this.applet.saveClickX >= 599 && this.applet.saveClickX <= 629 && this.applet.saveClickY >= 466 && this.applet.saveClickY < 503 && this.tabInterfaceIDs[9] != -1)
            {
                this.needDrawTabArea = true;
                this.tabID = 9;
                this.tabAreaAltered = true;
            }
            if(this.applet.saveClickX >= 627 && this.applet.saveClickX <= 671 && this.applet.saveClickY >= 467 && this.applet.saveClickY < 502 && this.tabInterfaceIDs[10] != -1)
            {
                this.needDrawTabArea = true;
                this.tabID = 10;
                this.tabAreaAltered = true;
            }
            if(this.applet.saveClickX >= 669 && this.applet.saveClickX <= 699 && this.applet.saveClickY >= 466 && this.applet.saveClickY < 503 && this.tabInterfaceIDs[11] != -1)
            {
                this.needDrawTabArea = true;
                this.tabID = 11;
                this.tabAreaAltered = true;
            }
            if(this.applet.saveClickX >= 696 && this.applet.saveClickX <= 726 && this.applet.saveClickY >= 466 && this.applet.saveClickY < 503 && this.tabInterfaceIDs[12] != -1)
            {
                this.needDrawTabArea = true;
                this.tabID = 12;
                this.tabAreaAltered = true;
            }
            if(this.applet.saveClickX >= 724 && this.applet.saveClickX <= 758 && this.applet.saveClickY >= 466 && this.applet.saveClickY < 502 && this.tabInterfaceIDs[13] != -1)
            {
                this.needDrawTabArea = true;
                this.tabID = 13;
                this.tabAreaAltered = true;
            }
        }
    }

    private void resetImageProducers2()
    {
        if(this.aRSImageProducer_1166 != null)
            return;
        Client.aRSImageProducer_1109 = null;
        this.aRSImageProducer_1166 = new RSImageProducer(479, 96);
        DrawingArea.setAllPixelsToZero();
        SpriteRenderer.drawSprite(Client.cacheSprite[19], 0, 0);
        this.aRSImageProducer_1163 = new RSImageProducer(190, 261);
        this.aRSImageProducer_1165 = new RSImageProducer(512, 334);
        DrawingArea.setAllPixelsToZero();
        this.aRSImageProducer_1123 = new RSImageProducer(496, 50);
        this.aRSImageProducer_1124 = new RSImageProducer(269, 37);
        this.aRSImageProducer_1125 = new RSImageProducer(249, 45);
        this.welcomeScreenRaised = true;
    }

    private String getDocumentBaseHost()
    {
        if(this.applet.gameFrame != null)
            return "runescape.com";
        else
            return this.applet.getDocumentBase().getHost().toLowerCase();
    }

    private void processRightClick()
    {
        if(this.activeInterfaceType != 0)
            return;
        this.menuActionName[0] = "Cancel";
        this.menuActionID[0] = 1107;
        this.menuActionRow = 1;
        this.buildSplitPrivateChatMenu();
        this.anInt886 = 0;
        if(this.applet.mouseX > 4 && this.applet.mouseY > 4 && this.applet.mouseX < 516 && this.applet.mouseY < 338)
            if(this.openInterfaceID != -1)
                this.buildInterfaceMenu(4, RSInterface.interfaceCache[this.openInterfaceID], this.applet.mouseX, 4, this.applet.mouseY, 0);
            else
                this.build3dScreenMenu();
        if(this.anInt886 != this.anInt1026)
            this.anInt1026 = this.anInt886;
        this.anInt886 = 0;
        if(this.applet.mouseX > 553 && this.applet.mouseY > 205 && this.applet.mouseX < 743 && this.applet.mouseY < 466)
            if(this.invOverlayInterfaceID != -1)
                this.buildInterfaceMenu(553, RSInterface.interfaceCache[this.invOverlayInterfaceID], this.applet.mouseX, 205, this.applet.mouseY, 0);
            else
            if(this.tabInterfaceIDs[this.tabID] != -1)
                this.buildInterfaceMenu(553, RSInterface.interfaceCache[this.tabInterfaceIDs[this.tabID]], this.applet.mouseX, 205, this.applet.mouseY, 0);
        if(this.anInt886 != this.anInt1048)
        {
            this.needDrawTabArea = true;
            this.anInt1048 = this.anInt886;
        }
        this.anInt886 = 0;
        if(this.applet.mouseX > 17 && this.applet.mouseY > 357 && this.applet.mouseX < 496 && this.applet.mouseY < 453)
            if(this.backDialogID != -1)
                this.buildInterfaceMenu(17, RSInterface.interfaceCache[this.backDialogID], this.applet.mouseX, 357, this.applet.mouseY, 0);
            else
            if(this.applet.mouseY < 434 && this.applet.mouseX < 426)
                this.buildChatAreaMenu(this.applet.mouseY - 357);
        if(this.backDialogID != -1 && this.anInt886 != this.anInt1039)
        {
            this.inputTaken = true;
            this.anInt1039 = this.anInt886;
        }
        boolean flag = false;
        while(!flag) 
        {
            flag = true;
            for(int j = 0; j < this.menuActionRow - 1; j++)
                if(this.menuActionID[j] < 1000 && this.menuActionID[j + 1] > 1000)
                {
                    String s = this.menuActionName[j];
                    this.menuActionName[j] = this.menuActionName[j + 1];
                    this.menuActionName[j + 1] = s;
                    int k = this.menuActionID[j];
                    this.menuActionID[j] = this.menuActionID[j + 1];
                    this.menuActionID[j + 1] = k;
                    k = this.menuActionCmd2[j];
                    this.menuActionCmd2[j] = this.menuActionCmd2[j + 1];
                    this.menuActionCmd2[j + 1] = k;
                    k = this.menuActionCmd3[j];
                    this.menuActionCmd3[j] = this.menuActionCmd3[j + 1];
                    this.menuActionCmd3[j + 1] = k;
                    k = this.menuActionCmd1[j];
                    this.menuActionCmd1[j] = this.menuActionCmd1[j + 1];
                    this.menuActionCmd1[j + 1] = k;
                    flag = false;
                }

        }
    }

    private void login(String s, String s1, boolean flag)
    {
        signlink.errorname = s;
        try
        {
            if(!flag)
            {
                this.loginMessage1 = "";
                this.loginMessage2 = "Connecting to server...";
                this.drawLoginScreen(true);
            }
            this.socketStream = new RSSocket(this.openSocket(43594 + RSBase.portOff));
            long l = TextClass.longForName(s);
            int i = (int)(l >> 16 & 31L);
            this.stream.position = 0;
            this.stream.writeByte(14);
            this.stream.writeByte(i);
            this.socketStream.queueBytes(2, this.stream.payload);
            for(int j = 0; j < 8; j++)
                this.socketStream.read();

            int k = this.socketStream.read();
            int i1 = k;
            if(k == 0)
            {
                this.socketStream.flushInputStream(this.inBuffer.payload, 8);
                this.inBuffer.position = 0;
                this.aLong1215 = this.inBuffer.readLong();
                int ai[] = new int[4];
                ai[0] = (int)(Math.random() * 99999999D);
                ai[1] = (int)(Math.random() * 99999999D);
                ai[2] = (int)(this.aLong1215 >> 32);
                ai[3] = (int)this.aLong1215;
                this.stream.position = 0;
                this.stream.writeByte(10);
                this.stream.writeInt(ai[0]);
                this.stream.writeInt(ai[1]);
                this.stream.writeInt(ai[2]);
                this.stream.writeInt(ai[3]);
                this.stream.writeInt(signlink.uid);
                this.stream.writeJString(s);
                this.stream.writeJString(s1);
                this.stream.encodeRSA();
                this.aStream_847.position = 0;
                if(flag)
                    this.aStream_847.writeByte(18);
                else
                    this.aStream_847.writeByte(16);
                this.aStream_847.writeByte(this.stream.position + 36 + 1 + 1 + 2);
                this.aStream_847.writeByte(255);
                this.aStream_847.writeShort(317);
                this.aStream_847.writeByte(Client.lowMem ? 1 : 0);
                for(int l1 = 0; l1 < 9; l1++)
                    this.aStream_847.writeInt(Constants.ENABLE_JAGGRAB ? this.jaggrab.expectedCRCs[l1] : 0);

                this.aStream_847.writeBytes(this.stream.payload, 0, this.stream.position);
                this.stream.encryption = new ISAACRandomGen(ai);
                for(int j2 = 0; j2 < 4; j2++)
                    ai[j2] += 50;

                this.encryption = new ISAACRandomGen(ai);
                this.socketStream.queueBytes(this.aStream_847.position, this.aStream_847.payload);
                k = this.socketStream.read();
            }
            if(k == 1)
            {
                try
                {
                    Thread.sleep(2000L);
                }
                catch(Exception _ex) { }
                this.login(s, s1, flag);
                return;
            }
            if(k == 2)
            {
                this.myPrivilege = this.socketStream.read();
                Client.flagged = this.socketStream.read() == 1;
                this.aLong1220 = 0L;
                this.anInt1022 = 0;
                this.mouseDetection.coordsIndex = 0;
                this.applet.awtFocus = true;
                this.aBoolean954 = true;
                this.loggedIn = true;
                this.stream.position = 0;
                this.inBuffer.position = 0;
                this.pktType = -1;
                this.anInt841 = -1;
                this.anInt842 = -1;
                this.anInt843 = -1;
                this.pktSize = 0;
                this.anInt1009 = 0;
                this.anInt1104 = 0;
                this.anInt1011 = 0;
                this.anInt855 = 0;
                this.menuActionRow = 0;
                this.menuOpen = false;
                this.applet.idleTime = 0;
                for(int j1 = 0; j1 < 100; j1++)
                    this.chatMessages[j1] = null;

                this.itemSelected = 0;
                this.spellSelected = 0;
                this.loadingStage = 0;
                this.anInt1062 = 0;
                this.anInt1278 = (int)(Math.random() * 100D) - 50;
                this.anInt1131 = (int)(Math.random() * 110D) - 55;
                this.anInt896 = (int)(Math.random() * 80D) - 40;
                this.minimapInt2 = (int)(Math.random() * 120D) - 60;
                this.minimapInt3 = (int)(Math.random() * 30D) - 20;
                this.minimapInt1 = (int)(Math.random() * 20D) - 10 & 0x7ff;
                this.anInt1021 = 0;
                this.anInt985 = -1;
                this.destX = 0;
                this.destY = 0;
                this.playerCount = 0;
                this.npcCount = 0;
                for(int i2 = 0; i2 < this.maxPlayers; i2++)
                {
                    this.playerArray[i2] = null;
                    this.aBufferArray895s[i2] = null;
                }

                for(int k2 = 0; k2 < 16384; k2++)
                    this.npcArray[k2] = null;

                Client.localPlayer = this.playerArray[this.myPlayerIndex] = new Player();
                this.projectiles.clear();
                this.aClass19_1056.clear();
                for(int l2 = 0; l2 < 4; l2++)
                {
                    for(int i3 = 0; i3 < 104; i3++)
                    {
                        for(int k3 = 0; k3 < 104; k3++)
                            this.levelObjects[l2][i3][k3] = null;

                    }

                }

                this.aClass19_1179 = new Deque();
                this.anInt900 = 0;
                this.friendsCount = 0;
                this.dialogID = -1;
                this.backDialogID = -1;
                this.openInterfaceID = -1;
                this.invOverlayInterfaceID = -1;
                this.anInt1018 = -1;
                this.aBoolean1149 = false;
                this.tabID = 3;
                this.inputDialogState = 0;
                this.menuOpen = false;
                this.messagePromptRaised = false;
                this.aString844 = null;
                this.anInt1055 = 0;
                this.anInt1054 = -1;
                this.aBoolean1047 = true;
                this.method45();
                for(int j3 = 0; j3 < 5; j3++)
                    this.anIntArray990[j3] = 0;

                for(int l3 = 0; l3 < 5; l3++)
                {
                    this.atPlayerActions[l3] = null;
                    this.atPlayerArray[l3] = false;
                }

                Client.anInt1175 = 0;
                Client.anInt1134 = 0;
                Client.anInt986 = 0;
                Client.anInt1288 = 0;
                Client.anInt924 = 0;
                Client.anInt1188 = 0;
                Client.anInt1155 = 0;
                Client.anInt1226 = 0;
                int anInt941 = 0;
                int anInt1260 = 0;
                this.resetImageProducers2();
                this.setBounds();
                return;
            }
            if(k == 3)
            {
                this.loginMessage1 = "";
                this.loginMessage2 = "Invalid username or password.";
                return;
            }
            if(k == 4)
            {
                this.loginMessage1 = "Your account has been disabled.";
                this.loginMessage2 = "Please check your message-center for details.";
                return;
            }
            if(k == 5)
            {
                this.loginMessage1 = "Your account is already logged in.";
                this.loginMessage2 = "Try again in 60 secs...";
                return;
            }
            if(k == 6)
            {
                this.loginMessage1 = "RuneScape has been updated!";
                this.loginMessage2 = "Please reload this page.";
                return;
            }
            if(k == 7)
            {
                this.loginMessage1 = "This world is full.";
                this.loginMessage2 = "Please use a different world.";
                return;
            }
            if(k == 8)
            {
                this.loginMessage1 = "Unable to connect.";
                this.loginMessage2 = "Login server offline.";
                return;
            }
            if(k == 9)
            {
                this.loginMessage1 = "Login limit exceeded.";
                this.loginMessage2 = "Too many connections from your address.";
                return;
            }
            if(k == 10)
            {
                this.loginMessage1 = "Unable to connect.";
                this.loginMessage2 = "Bad session id.";
                return;
            }
            if(k == 11)
            {
                this.loginMessage2 = "Login server rejected session.";
                this.loginMessage2 = "Please try again.";
                return;
            }
            if(k == 12)
            {
                this.loginMessage1 = "You need a members account to login to this world.";
                this.loginMessage2 = "Please subscribe, or use a different world.";
                return;
            }
            if(k == 13)
            {
                this.loginMessage1 = "Could not complete login.";
                this.loginMessage2 = "Please try using a different world.";
                return;
            }
            if(k == 14)
            {
                this.loginMessage1 = "The server is being updated.";
                this.loginMessage2 = "Please wait 1 minute and try again.";
                return;
            }
            if(k == 15)
            {
                this.loggedIn = true;
                this.stream.position = 0;
                this.inBuffer.position = 0;
                this.pktType = -1;
                this.anInt841 = -1;
                this.anInt842 = -1;
                this.anInt843 = -1;
                this.pktSize = 0;
                this.anInt1009 = 0;
                this.anInt1104 = 0;
                this.menuActionRow = 0;
                this.menuOpen = false;
                this.aLong824 = System.currentTimeMillis();
                return;
            }
            if(k == 16)
            {
                this.loginMessage1 = "Login attempts exceeded.";
                this.loginMessage2 = "Please wait 1 minute and try again.";
                return;
            }
            if(k == 17)
            {
                this.loginMessage1 = "You are standing in a members-only area.";
                this.loginMessage2 = "To play on this world move to a free area first";
                return;
            }
            if(k == 20)
            {
                this.loginMessage1 = "Invalid loginserver requested";
                this.loginMessage2 = "Please try using a different world.";
                return;
            }
            if(k == 21)
            {
                for(int k1 = this.socketStream.read(); k1 >= 0; k1--)
                {
                    this.loginMessage1 = "You have only just left another world";
                    this.loginMessage2 = "Your profile will be transferred in: " + k1 + " seconds";
                    this.drawLoginScreen(true);
                    try
                    {
                        Thread.sleep(1000L);
                    }
                    catch(Exception _ex) { }
                }

                this.login(s, s1, flag);
                return;
            }
            if(k == -1)
            {
                if(i1 == 0)
                {
                    if(this.loginFailures < 2)
                    {
                        try
                        {
                            Thread.sleep(2000L);
                        }
                        catch(Exception _ex) { }
                        this.loginFailures++;
                        this.login(s, s1, flag);
                        return;
                    } else
                    {
                        this.loginMessage1 = "No response from loginserver";
                        this.loginMessage2 = "Please wait 1 minute and try again.";
                        return;
                    }
                } else
                {
                    this.loginMessage1 = "No response from server";
                    this.loginMessage2 = "Please try using a different world.";
                    return;
                }
            } else
            {
                System.out.println("response:" + k);
                this.loginMessage1 = "Unexpected server response";
                this.loginMessage2 = "Please try using a different world.";
                return;
            }
        }
        catch(IOException _ex)
        {
            this.loginMessage1 = "";
        }
        this.loginMessage2 = "Error connecting to server.";
    }

    Pathfinder pathfinder = new Pathfinder();
    
	private void method86(BitBuffer buffer)
    {
        for(int j = 0; j < this.anInt893; j++)
        {
            int k = this.anIntArray894[j];
            NPC npc = this.npcArray[k];
            int l = buffer.readUByte();
            if((l & 0x10) != 0)
            {
                int i1 = buffer.readLEUShort();
                if(i1 == 65535)
                    i1 = -1;
                int i2 = buffer.readUByte();
                if(i1 == npc.anim && i1 != -1)
                {
                    int l2 = Animation.animations[i1].replayMode;
                    if(l2 == 1)
                    {
                        npc.anInt1527 = 0;
                        npc.anInt1528 = 0;
                        npc.anInt1529 = i2;
                        npc.anInt1530 = 0;
                    }
                    if(l2 == 2)
                        npc.anInt1530 = 0;
                } else
                if(i1 == -1 || npc.anim == -1 || Animation.animations[i1].priority >= Animation.animations[npc.anim].priority)
                {
                    npc.anim = i1;
                    npc.anInt1527 = 0;
                    npc.anInt1528 = 0;
                    npc.anInt1529 = i2;
                    npc.anInt1530 = 0;
                    npc.anInt1542 = npc.smallXYIndex;
                }
            }
            if((l & 8) != 0)
            {
                int j1 = buffer.readUByteA();
                int j2 = buffer.readNegUByte();
                npc.updateHitData(j2, j1, Client.loopCycle);
                npc.loopCycleStatus = Client.loopCycle + 300;
                npc.currentHealth = buffer.readUByteA();
                npc.maxHealth = buffer.readUByte();
            }
            if((l & 0x80) != 0)
            {
                npc.anInt1520 = buffer.readUShort();
                int k1 = buffer.readInt();
                npc.anInt1524 = k1 >> 16;
                npc.anInt1523 = Client.loopCycle + (k1 & 0xffff);
                npc.anInt1521 = 0;
                npc.anInt1522 = 0;
                if(npc.anInt1523 > Client.loopCycle)
                    npc.anInt1521 = -1;
                if(npc.anInt1520 == 65535)
                    npc.anInt1520 = -1;
            }
            if((l & 0x20) != 0)
            {
                npc.interactingEntity = buffer.readUShort();
                if(npc.interactingEntity == 65535)
                    npc.interactingEntity = -1;
            }
            if((l & 1) != 0)
            {
                npc.textSpoken = buffer.readString();
                npc.textCycle = 100;
//	entityMessage(npc);
	
            }
            if((l & 0x40) != 0)
            {
                int l1 = buffer.readNegUByte();
                int k2 = buffer.readUByteS();
                npc.updateHitData(k2, l1, Client.loopCycle);
                npc.loopCycleStatus = Client.loopCycle + 300;
                npc.currentHealth = buffer.readUByteS();
                npc.maxHealth = buffer.readNegUByte();
            }
            if((l & 2) != 0)
            {
                npc.desc = EntityDef.forID(buffer.readLEUShortA());
                npc.anInt1540 = npc.desc.aByte68;
                npc.anInt1504 = npc.desc.anInt79;
                npc.anInt1554 = npc.desc.anInt67;
                npc.anInt1555 = npc.desc.anInt58;
                npc.anInt1556 = npc.desc.anInt83;
                npc.anInt1557 = npc.desc.anInt55;
                npc.anInt1511 = npc.desc.anInt77;
            }
            if((l & 4) != 0)
            {
                npc.anInt1538 = buffer.readLEUShort();
                npc.anInt1539 = buffer.readLEUShort();
            }
        }
    }

    private void buildAtNPCMenu(EntityDef entityDef, int i, int j, int k)
    {
        if(this.menuActionRow >= 400)
            return;
        if(entityDef.childrenIDs != null)
            entityDef = entityDef.method161();
        if(entityDef == null)
            return;
        if(!entityDef.aBoolean84)
            return;
        String s = entityDef.name;
        if(entityDef.combatLevel != 0)
            s = s + Client.combatDiffColor(Client.localPlayer.combatLevel, entityDef.combatLevel) + " (level-" + entityDef.combatLevel + ")";
        if(this.itemSelected == 1)
        {
            this.menuActionName[this.menuActionRow] = "Use " + this.selectedItemName + " with @yel@" + s;
            this.menuActionID[this.menuActionRow] = 582;
            this.menuActionCmd1[this.menuActionRow] = i;
            this.menuActionCmd2[this.menuActionRow] = k;
            this.menuActionCmd3[this.menuActionRow] = j;
            this.menuActionRow++;
            return;
        }
        if(this.spellSelected == 1)
        {
            if((this.spellUsableOn & 2) == 2)
            {
                this.menuActionName[this.menuActionRow] = this.spellTooltip + " @yel@" + s;
                this.menuActionID[this.menuActionRow] = 413;
                this.menuActionCmd1[this.menuActionRow] = i;
                this.menuActionCmd2[this.menuActionRow] = k;
                this.menuActionCmd3[this.menuActionRow] = j;
                this.menuActionRow++;
            }
        } else
        {
            if(entityDef.actions != null)
            {
                for(int l = 4; l >= 0; l--)
                    if(entityDef.actions[l] != null && !entityDef.actions[l].equalsIgnoreCase("attack"))
                    {
                        this.menuActionName[this.menuActionRow] = entityDef.actions[l] + " @yel@" + s;
                        if(l == 0)
                            this.menuActionID[this.menuActionRow] = 20;
                        if(l == 1)
                            this.menuActionID[this.menuActionRow] = 412;
                        if(l == 2)
                            this.menuActionID[this.menuActionRow] = 225;
                        if(l == 3)
                            this.menuActionID[this.menuActionRow] = 965;
                        if(l == 4)
                            this.menuActionID[this.menuActionRow] = 478;
                        this.menuActionCmd1[this.menuActionRow] = i;
                        this.menuActionCmd2[this.menuActionRow] = k;
                        this.menuActionCmd3[this.menuActionRow] = j;
                        this.menuActionRow++;
                    }

            }
            if(entityDef.actions != null)
            {
                for(int i1 = 4; i1 >= 0; i1--)
                    if(entityDef.actions[i1] != null && entityDef.actions[i1].equalsIgnoreCase("attack"))
                    {
                        char c = '\0';
                        if(entityDef.combatLevel > Client.localPlayer.combatLevel)
                            c = '\u07D0';
                        this.menuActionName[this.menuActionRow] = entityDef.actions[i1] + " @yel@" + s;
                        if(i1 == 0)
                            this.menuActionID[this.menuActionRow] = 20 + c;
                        if(i1 == 1)
                            this.menuActionID[this.menuActionRow] = 412 + c;
                        if(i1 == 2)
                            this.menuActionID[this.menuActionRow] = 225 + c;
                        if(i1 == 3)
                            this.menuActionID[this.menuActionRow] = 965 + c;
                        if(i1 == 4)
                            this.menuActionID[this.menuActionRow] = 478 + c;
                        this.menuActionCmd1[this.menuActionRow] = i;
                        this.menuActionCmd2[this.menuActionRow] = k;
                        this.menuActionCmd3[this.menuActionRow] = j;
                        this.menuActionRow++;
                    }

            }
            this.menuActionName[this.menuActionRow] = "Examine @yel@" + s + " @gre@(@whi@" + entityDef.type + "@gre@)";
            this.menuActionID[this.menuActionRow] = 1025;
            this.menuActionCmd1[this.menuActionRow] = i;
            this.menuActionCmd2[this.menuActionRow] = k;
            this.menuActionCmd3[this.menuActionRow] = j;
            this.menuActionRow++;
        }
    }

    private void buildAtPlayerMenu(int i, int j, Player player, int k)
    {
        if(player == Client.localPlayer)
            return;
        if(this.menuActionRow >= 400)
            return;
        String s;
        if(player.skill == 0)
            s = player.name + Client.combatDiffColor(Client.localPlayer.combatLevel, player.combatLevel) + " (level-" + player.combatLevel + ")";
        else
            s = player.name + " (skill-" + player.skill + ")";
        if(this.itemSelected == 1)
        {
            this.menuActionName[this.menuActionRow] = "Use " + this.selectedItemName + " with @whi@" + s;
            this.menuActionID[this.menuActionRow] = 491;
            this.menuActionCmd1[this.menuActionRow] = j;
            this.menuActionCmd2[this.menuActionRow] = i;
            this.menuActionCmd3[this.menuActionRow] = k;
            this.menuActionRow++;
        } else
        if(this.spellSelected == 1)
        {
            if((this.spellUsableOn & 8) == 8)
            {
                this.menuActionName[this.menuActionRow] = this.spellTooltip + " @whi@" + s;
                this.menuActionID[this.menuActionRow] = 365;
                this.menuActionCmd1[this.menuActionRow] = j;
                this.menuActionCmd2[this.menuActionRow] = i;
                this.menuActionCmd3[this.menuActionRow] = k;
                this.menuActionRow++;
            }
        } else
        {
            for(int l = 4; l >= 0; l--)
                if(this.atPlayerActions[l] != null)
                {
                    this.menuActionName[this.menuActionRow] = this.atPlayerActions[l] + " @whi@" + s;
                    char c = '\0';
                    if(this.atPlayerActions[l].equalsIgnoreCase("attack"))
                    {
                        if(player.combatLevel > Client.localPlayer.combatLevel)
                            c = '\u07D0';
                        if(Client.localPlayer.team != 0 && player.team != 0)
                            if(Client.localPlayer.team == player.team)
                                c = '\u07D0';
                            else
                                c = '\0';
                    } else
                    if(this.atPlayerArray[l])
                        c = '\u07D0';
                    if(l == 0)
                        this.menuActionID[this.menuActionRow] = 561 + c;
                    if(l == 1)
                        this.menuActionID[this.menuActionRow] = 779 + c;
                    if(l == 2)
                        this.menuActionID[this.menuActionRow] = 27 + c;
                    if(l == 3)
                        this.menuActionID[this.menuActionRow] = 577 + c;
                    if(l == 4)
                        this.menuActionID[this.menuActionRow] = 729 + c;
                    this.menuActionCmd1[this.menuActionRow] = j;
                    this.menuActionCmd2[this.menuActionRow] = i;
                    this.menuActionCmd3[this.menuActionRow] = k;
                    this.menuActionRow++;
                }

        }
        for(int i1 = 0; i1 < this.menuActionRow; i1++)
            if(this.menuActionID[i1] == 516)
            {
                this.menuActionName[i1] = "Walk here @whi@" + s;
                return;
            }

    }

    private void method89(SpawnedObject class30_sub1)
    {
        int i = 0;
        int j = -1;
        int k = 0;
        int l = 0;
        if(class30_sub1.anInt1296 == 0)
            i = this.worldController.method300(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
        if(class30_sub1.anInt1296 == 1)
            i = this.worldController.method301(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
        if(class30_sub1.anInt1296 == 2)
            i = this.worldController.method302(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
        if(class30_sub1.anInt1296 == 3)
            i = this.worldController.method303(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
        if(i != 0)
        {
            int i1 = this.worldController.method304(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298, i);
            j = i >> 14 & 0x7fff;
            k = i1 & 0x1f;
            l = i1 >> 6;
        }
        class30_sub1.anInt1299 = j;
        class30_sub1.anInt1301 = k;
        class30_sub1.anInt1300 = l;
    }

    private void method90()
    {
        for(int i = 0; i < this.anInt1062; i++)
            if(this.anIntArray1250[i] <= 0)
            {
                boolean flag1 = false;
                try
                {
                    if(this.anIntArray1207[i] == this.anInt874 && this.anIntArray1241[i] == this.anInt1289)
                    {
                        if(!this.replayWave())
                            flag1 = true;
                    } else
                    {
                        Buffer buffer = Sounds.method241(this.anIntArray1241[i], this.anIntArray1207[i]);
                        if(System.currentTimeMillis() + buffer.position / 22 > this.aLong1172 + this.anInt1257 / 22)
                        {
                            this.anInt1257 = buffer.position;
                            this.aLong1172 = System.currentTimeMillis();
                            if(this.saveWave(buffer.payload, buffer.position))
                            {
                                this.anInt874 = this.anIntArray1207[i];
                                this.anInt1289 = this.anIntArray1241[i];
                            } else
                            {
                                flag1 = true;
                            }
                        }
                    }
                }
                catch(Exception exception) { }
                if(!flag1 || this.anIntArray1250[i] == -5)
                {
                    this.anInt1062--;
                    for(int j = i; j < this.anInt1062; j++)
                    {
                        this.anIntArray1207[j] = this.anIntArray1207[j + 1];
                        this.anIntArray1241[j] = this.anIntArray1241[j + 1];
                        this.anIntArray1250[j] = this.anIntArray1250[j + 1];
                    }

                    i--;
                } else
                {
                    this.anIntArray1250[i] = -5;
                }
            } else
            {
                this.anIntArray1250[i]--;
            }

        if(this.prevSong > 0)
        {
            this.prevSong -= 20;
            if(this.prevSong < 0)
                this.prevSong = 0;
            if(this.prevSong == 0 && this.musicEnabled && !Client.lowMem)
            {
                this.nextSong = this.currentSong;
                this.songChanging = true;
                this.onDemandFetcher.method558(2, this.nextSong);
            }
        }
    }

    @Override
	public void startUp()
    {
        this.drawLoadingText(20, "Starting up");
        if(signlink.sunjava)
            this.applet.minDelay = 5;
        if(Client.aBoolean993)
        {
 //           rsAlreadyLoaded = true;
 //           return;
        }
        Client.aBoolean993 = true;
        boolean flag = true;
        String s = this.getDocumentBaseHost();
        if(s.endsWith("jagex.com"))
            flag = true;
        if(s.endsWith("runescape.com"))
            flag = true;
        if(s.endsWith("192.168.1.2"))
            flag = true;
        if(s.endsWith("192.168.1.229"))
            flag = true;
        if(s.endsWith("192.168.1.228"))
            flag = true;
        if(s.endsWith("192.168.1.227"))
            flag = true;
        if(s.endsWith("192.168.1.226"))
            flag = true;
        if(s.endsWith("127.0.0.1"))
            flag = true;
        if(!flag)
        {
            this.genericLoadingError = true;
            return;
        }
        if(signlink.cache_dat != null)
        {
            for(int i = 0; i < 5; i++)
                this.indexs[i] = new Index(signlink.cache_dat, signlink.cache_idx[i], i + 1);

        }
        try
        {
        	if(Constants.ENABLE_JAGGRAB)
        		this.jaggrab = new Jaggrab(this);
            this.titleStreamLoader = this.fetchArchive(1);
            this.aTextDrawingArea_1270 = new TextDrawingArea(false, "p11_full", this.titleStreamLoader);
            this.aTextDrawingArea_1271 = new TextDrawingArea(false, "p12_full", this.titleStreamLoader);
            this.chatTextDrawingArea = new TextDrawingArea(false, "b12_full", this.titleStreamLoader);
            TextDrawingArea aTextDrawingArea_1273 = new TextDrawingArea(true, "q8_full", this.titleStreamLoader);
            this.drawLoadingText(10, "Connecting to fileserver");
            Archive configArchive = this.fetchArchive(2);
            Archive interfaceArchive = this.fetchArchive(3);
            Archive mediaArchive = this.fetchArchive(4);
            Archive textureArchive = this.fetchArchive(6);
            Archive wordencArchive = this.fetchArchive(7);
            Archive soundArchive = this.fetchArchive(8);
            this.byteGroundArray = new byte[4][104][104];
            this.intGroundArray = new int[4][105][105];
            this.worldController = new WorldController(this.intGroundArray);
            for(int j = 0; j < 4; j++)
                this.aClass11Array1230[j] = new CollisionMap(0, 0, 104, 104);

            this.minimapImage = new Sprite(512, 512);
            Archive streamLoader_6 = this.fetchArchive(5);
            this.drawLoadingText(60, "Connecting to update server");
            this.onDemandFetcher = new OnDemandFetcher();
            this.onDemandFetcher.start(streamLoader_6, this);
            Frame.init(this.onDemandFetcher.getAnimCount());
            Model.method459(this.onDemandFetcher.getVersionCount(0), this.onDemandFetcher);
            if(!Client.lowMem)
            {
                this.nextSong = 0;
                try
                {
                    this.nextSong = Integer.parseInt(this.applet.getParameter("music"));
                }
                catch(Exception _ex) { }
                this.songChanging = true;
                this.onDemandFetcher.method558(2, this.nextSong);
                while(this.onDemandFetcher.getNodeCount() > 0)
                {
                    this.processOnDemandQueue();
                    try
                    {
                        Thread.sleep(100L);
                    }
                    catch(Exception _ex) { }
                    if(this.onDemandFetcher.anInt1349 > 3)
                    {
                    	this.applet.loadError();
                        return;
                    }
                }
            }
            this.drawLoadingText(65, "Requesting animations");
            int k = this.onDemandFetcher.getVersionCount(1);
            for(int i1 = 0; i1 < k; i1++)
                this.onDemandFetcher.method558(1, i1);

            while(this.onDemandFetcher.getNodeCount() > 0)
            {
                int j1 = k - this.onDemandFetcher.getNodeCount();
                if(j1 > 0)
                    this.drawLoadingText(65, "Loading animations - " + (j1 * 100) / k + "%");
                this.processOnDemandQueue();
                try
                {
                    Thread.sleep(100L);
                }
                catch(Exception _ex) { }
                if(this.onDemandFetcher.anInt1349 > 3)
                {
                	this.applet.loadError();
                    return;
                }
            }
            this.drawLoadingText(70, "Requesting models");
            k = this.onDemandFetcher.getVersionCount(0);
            for(int k1 = 0; k1 < k; k1++)
            {
                int l1 = this.onDemandFetcher.getModelIndex(k1);
                if((l1 & 1) != 0)
                    this.onDemandFetcher.method558(0, k1);
            }

            k = this.onDemandFetcher.getNodeCount();
            while(this.onDemandFetcher.getNodeCount() > 0)
            {
                int i2 = k - this.onDemandFetcher.getNodeCount();
                if(i2 > 0)
                    this.drawLoadingText(70, "Loading models - " + (i2 * 100) / k + "%");
                this.processOnDemandQueue();
                try
                {
                    Thread.sleep(100L);
                }
                catch(Exception _ex) { }
            }
            if(this.indexs[0] != null)
            {
                this.drawLoadingText(75, "Requesting maps");
                this.onDemandFetcher.method558(3, this.onDemandFetcher.method562(0, 48, 47));
                this.onDemandFetcher.method558(3, this.onDemandFetcher.method562(1, 48, 47));
                this.onDemandFetcher.method558(3, this.onDemandFetcher.method562(0, 48, 48));
                this.onDemandFetcher.method558(3, this.onDemandFetcher.method562(1, 48, 48));
                this.onDemandFetcher.method558(3, this.onDemandFetcher.method562(0, 48, 49));
                this.onDemandFetcher.method558(3, this.onDemandFetcher.method562(1, 48, 49));
                this.onDemandFetcher.method558(3, this.onDemandFetcher.method562(0, 47, 47));
                this.onDemandFetcher.method558(3, this.onDemandFetcher.method562(1, 47, 47));
                this.onDemandFetcher.method558(3, this.onDemandFetcher.method562(0, 47, 48));
                this.onDemandFetcher.method558(3, this.onDemandFetcher.method562(1, 47, 48));
                this.onDemandFetcher.method558(3, this.onDemandFetcher.method562(0, 148, 48));
                this.onDemandFetcher.method558(3, this.onDemandFetcher.method562(1, 148, 48));
                k = this.onDemandFetcher.getNodeCount();
                while(this.onDemandFetcher.getNodeCount() > 0)
                {
                    int j2 = k - this.onDemandFetcher.getNodeCount();
                    if(j2 > 0)
                        this.drawLoadingText(75, "Loading maps - " + (j2 * 100) / k + "%");
                    this.processOnDemandQueue();
                    try
                    {
                        Thread.sleep(100L);
                    }
                    catch(Exception _ex) { }
                }
            }
            k = this.onDemandFetcher.getVersionCount(0);
            for(int k2 = 0; k2 < k; k2++)
            {
                int l2 = this.onDemandFetcher.getModelIndex(k2);
                byte byte0 = 0;
                if((l2 & 8) != 0)
                    byte0 = 10;
                else
                if((l2 & 0x20) != 0)
                    byte0 = 9;
                else
                if((l2 & 0x10) != 0)
                    byte0 = 8;
                else
                if((l2 & 0x40) != 0)
                    byte0 = 7;
                else
                if((l2 & 0x80) != 0)
                    byte0 = 6;
                else
                if((l2 & 2) != 0)
                    byte0 = 5;
                else
                if((l2 & 4) != 0)
                    byte0 = 4;
                if((l2 & 1) != 0)
                    byte0 = 3;
                if(byte0 != 0)
                    this.onDemandFetcher.method563(byte0, 0, k2);
            }

            this.onDemandFetcher.method554(Client.isMembers);
            if(!Client.lowMem)
            {
                int l = this.onDemandFetcher.getVersionCount(2);
                for(int i3 = 1; i3 < l; i3++)
                    if(this.onDemandFetcher.method569(i3))
                        this.onDemandFetcher.method563((byte)1, 2, i3);

            }
            this.drawLoadingText(80, "Unpacking media");
            
			File[] file = new File(Constants.findcachedir() + "/sprites/sprites/").listFiles();
			int size = file.length;
			Client.cacheSprite = new Sprite[size];
			System.out.println("Images Loaded: "+size);
			for (int i = 0; i < size; i ++) {
				Client.cacheSprite[i] = new Sprite("Sprites/" + i);
			}
			
            this.invBack = new IndexedImage(mediaArchive, "invback", 0);
            this.chatBack = new IndexedImage(mediaArchive, "chatback", 0);
            this.mapBack = new IndexedImage("mapback", 0);
            this.backBase1 = new IndexedImage(mediaArchive, "backbase1", 0);
            this.backBase2 = new IndexedImage(mediaArchive, "backbase2", 0);
            this.backHmid1 = new IndexedImage(mediaArchive, "backhmid1", 0);
            for(int j3 = 0; j3 < 13; j3++)
                this.sideIcons[j3] = new IndexedImage(mediaArchive, "sideicons", j3);

            this.compass = new Sprite(mediaArchive, "compass", 0);
            try
            {
                for(int k3 = 0; k3 < 100; k3++)
                    this.mapScenes[k3] = new IndexedImage(mediaArchive, "mapscene", k3);

            }
            catch(Exception _ex) { }
            try
            {
                for(int l3 = 0; l3 < 100; l3++)
                    this.mapFunctions[l3] = new Sprite(mediaArchive, "mapfunction", l3);

            }
            catch(Exception _ex) { }
            try
            {
                for(int i4 = 0; i4 < 20; i4++)
                    this.hitMarks[i4] = new Sprite(mediaArchive, "hitmarks", i4);

            }
            catch(Exception _ex) { }
            try
            {
                for(int j4 = 0; j4 < 20; j4++)
                    this.headIcons[j4] = new Sprite(mediaArchive, "headicons", j4);

            }
            catch(Exception _ex) { }
            this.mapFlag = new Sprite(mediaArchive, "mapmarker", 0);
            this.mapMarker = new Sprite(mediaArchive, "mapmarker", 1);
            for(int k4 = 0; k4 < 8; k4++)
                this.crosses[k4] = new Sprite(mediaArchive, "cross", k4);

            this.mapDotItem = new Sprite(mediaArchive, "mapdots", 0);
            this.mapDotNPC = new Sprite(mediaArchive, "mapdots", 1);
            this.mapDotPlayer = new Sprite(mediaArchive, "mapdots", 2);
            this.mapDotFriend = new Sprite(mediaArchive, "mapdots", 3);
            this.mapDotTeam = new Sprite(mediaArchive, "mapdots", 4);
            this.scrollBar1 = new IndexedImage(mediaArchive, "scrollbar", 0);
            this.scrollBar2 = new IndexedImage(mediaArchive, "scrollbar", 1);
            this.redStone1 = new IndexedImage(mediaArchive, "redstone1", 0);
            this.redStone2 = new IndexedImage(mediaArchive, "redstone2", 0);
            this.redStone3 = new IndexedImage(mediaArchive, "redstone3", 0);
            this.redStone1_2 = new IndexedImage(mediaArchive, "redstone1", 0);
            this.redStone1_2.flipHorizontally();
            this.redStone2_2 = new IndexedImage(mediaArchive, "redstone2", 0);
            this.redStone2_2.flipHorizontally();
            this.redStone1_3 = new IndexedImage(mediaArchive, "redstone1", 0);
            this.redStone1_3.flipVertically();
            this.redStone2_3 = new IndexedImage(mediaArchive, "redstone2", 0);
            this.redStone2_3.flipVertically();
            this.redStone3_2 = new IndexedImage(mediaArchive, "redstone3", 0);
            this.redStone3_2.flipVertically();
            this.redStone1_4 = new IndexedImage(mediaArchive, "redstone1", 0);
            this.redStone1_4.flipHorizontally();
            this.redStone1_4.flipVertically();
            this.redStone2_4 = new IndexedImage(mediaArchive, "redstone2", 0);
            this.redStone2_4.flipHorizontally();
            this.redStone2_4.flipVertically();
            for(int l4 = 0; l4 < 2; l4++)
                this.modIcons[l4] = new IndexedImage(mediaArchive, "mod_icons", l4);            
            
            int i5 = (int)(Math.random() * 21D) - 10;
            int j5 = (int)(Math.random() * 21D) - 10;
            int k5 = (int)(Math.random() * 21D) - 10;
            int l5 = (int)(Math.random() * 41D) - 20;
            for(int i6 = 0; i6 < 100; i6++)
            {
                if(this.mapFunctions[i6] != null)
                    this.mapFunctions[i6].recolour(i5 + l5, j5 + l5, k5 + l5);
                if(this.mapScenes[i6] != null)
                    this.mapScenes[i6].offsetColour(i5 + l5, j5 + l5, k5 + l5);
            }

            this.drawLoadingText(83, "Unpacking textures");
            Texture.method368(textureArchive);
            Texture.method372(0.80000000000000004D);
            Texture.method367();
            this.drawLoadingText(86, "Unpacking config");
            Animation.init(configArchive);
            ObjectDef.unpackConfig(configArchive);
            Flo.init(configArchive);
            ItemDef.unpackConfig(configArchive);
            EntityDef.unpackConfig(configArchive);
            IDK.unpackConfig(configArchive);
            Graphic.unpackConfig(configArchive);
            VariableParameter.unpackConfig(configArchive);
            VariableBits.unpackConfig(configArchive);
            ItemDef.isMembers = Client.isMembers;
            if(!Client.lowMem)
            {
                this.drawLoadingText(90, "Unpacking sounds");
                byte abyte0[] = soundArchive.getEntry("sounds.dat");
                Buffer buffer = new Buffer(abyte0);
                Sounds.unpack(buffer);
            }
            this.drawLoadingText(95, "Unpacking interfaces");
            TextDrawingArea aclass30_sub2_sub1_sub4s[] = {
                    this.aTextDrawingArea_1270, this.aTextDrawingArea_1271, this.chatTextDrawingArea, aTextDrawingArea_1273
            };
            RSInterface.unpack(interfaceArchive, aclass30_sub2_sub1_sub4s, mediaArchive);
            this.drawLoadingText(100, "Preparing game engine");
            
            Minimap.prepare(this.mapBack);
            this.setBounds();
            Censor.loadConfig(wordencArchive);
            this.mouseDetection = new MouseDetection(this);
            Utils.startRunnable(this.mouseDetection, 10);
            RenderableObject.clientInstance = this;
            ObjectDef.clientInstance = this;
            EntityDef.clientInstance = this;
            return;
        }
        catch(Exception exception)
        {
        	exception.printStackTrace();
            Utils.reporterror("loaderror " + this.applet.aString1049 + " " + this.applet.anInt1079);
        }
        this.loadingError = true;
    }

	private void method91(BitBuffer buffer, int i)
    {
        while(buffer.bitPosition + 10 < i * 8)
        {
            int j = buffer.readBits(11);
            if(j == 2047)
                break;
            if(this.playerArray[j] == null)
            {
                this.playerArray[j] = new Player();
                if(this.aBufferArray895s[j] != null)
                    this.playerArray[j].updatePlayer(this.aBufferArray895s[j]);
            }
            this.playerIndices[this.playerCount++] = j;
            Player player = this.playerArray[j];
            player.anInt1537 = Client.loopCycle;
            int k = buffer.readBits(1);
            if(k == 1)
                this.anIntArray894[this.anInt893++] = j;
            int l = buffer.readBits(1);
            int i1 = buffer.readBits(5);
            if(i1 > 15)
                i1 -= 32;
            int j1 = buffer.readBits(5);
            if(j1 > 15)
                j1 -= 32;
            player.setPos(Client.localPlayer.smallX[0] + j1, Client.localPlayer.smallY[0] + i1, l == 1);
        }
        buffer.disableBitAccess();
    }


	public boolean inCircle(int circleX, int circleY, int clickX, int clickY, int radius) {
		return java.lang.Math.pow((circleX + radius - clickX), 2) + java.lang.Math.pow((circleY + radius - clickY), 2) < java.lang.Math.pow(radius, 2);
	}
	
	public boolean mouseMapPosition() {
		if (this.applet.mouseX >= Client.frameWidth - 21 && this.applet.mouseX <= Client.frameWidth && this.applet.mouseY >= 0 && this.applet.mouseY <= 21) {
            return false;
		}
        return true;
	}
	
    private void processMainScreenClick()
    {
        if(this.anInt1021 != 0)
            return;
        if(this.applet.clickMode3 == 1)
        {
			int i = this.applet.saveClickX - 25 - 547;
			int j = this.applet.saveClickY - 5 - 3;
			if (RSBase.frameMode != ScreenMode.FIXED) {
				i = this.applet.saveClickX - (Client.frameWidth - 182 + 24);
				j = this.applet.saveClickY - 8;
			}
            if(/*i >= 0 && j >= 0 && i < 146 && j < 151*/this.inCircle(0, 0, i, j, 76) && this.mouseMapPosition())
            {
                i -= 73;
                j -= 75;
                int k = this.minimapInt1 + this.minimapInt2 & 0x7ff;
                int i1 = Texture.anIntArray1470[k];
                int j1 = Texture.anIntArray1471[k];
                i1 = i1 * (this.minimapInt3 + 256) >> 8;
                j1 = j1 * (this.minimapInt3 + 256) >> 8;
                int k1 = j * i1 + i * j1 >> 11;
                int l1 = j * j1 - i * i1 >> 11;
                int i2 = Client.localPlayer.x + k1 >> 7;
                int j2 = Client.localPlayer.y - l1 >> 7;
                boolean flag1 = pathfinder.doWalkTo(0, this, 1, 0, 0, 0, Client.localPlayer.smallY[0], 0, j2, Client.localPlayer.smallX[0], true, i2, this.aClass11Array1230[this.plane]);
                if(flag1)
                {
                    this.stream.writeByte(i);
                    this.stream.writeByte(j);
                    this.stream.writeShort(this.minimapInt1);
                    this.stream.writeByte(57);
                    this.stream.writeByte(this.minimapInt2);
                    this.stream.writeByte(this.minimapInt3);
                    this.stream.writeByte(89);
                    this.stream.writeShort(Client.localPlayer.x);
                    this.stream.writeShort(Client.localPlayer.y);
                    this.stream.writeByte(pathfinder.anInt1264);
                    this.stream.writeByte(63);
                }
            }
            Client.anInt1117++;
            if(Client.anInt1117 > 1151)
            {
                Client.anInt1117 = 0;
                this.stream.writeOpcode(246);
                this.stream.writeByte(0);
                int l = this.stream.position;
                if((int)(Math.random() * 2D) == 0)
                    this.stream.writeByte(101);
                this.stream.writeByte(197);
                this.stream.writeShort((int)(Math.random() * 65536D));
                this.stream.writeByte((int)(Math.random() * 256D));
                this.stream.writeByte(67);
                this.stream.writeShort(14214);
                if((int)(Math.random() * 2D) == 0)
                    this.stream.writeShort(29487);
                this.stream.writeShort((int)(Math.random() * 65536D));
                if((int)(Math.random() * 2D) == 0)
                    this.stream.writeByte(220);
                this.stream.writeByte(180);
                this.stream.writeSizeByte(this.stream.position - l);
            }
        }
    }

    private String interfaceIntToString(int j)
    {
        if(j < 0x3b9ac9ff)
            return String.valueOf(j);
        else
            return "*";
    }

    private void showErrorScreen()
    {
        Graphics g = this.applet.getGameComponent().getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, 765, 503);
        this.applet.method4(1);
        if(this.loadingError)
        {
            g.setFont(new Font("Helvetica", 1, 16));
            g.setColor(Color.yellow);
            int k = 35;
            g.drawString("Sorry, an error has occured whilst loading RuneScape", 30, k);
            k += 50;
            g.setColor(Color.white);
            g.drawString("To fix this try the following (in order):", 30, k);
            k += 50;
            g.setColor(Color.white);
            g.setFont(new Font("Helvetica", 1, 12));
            g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, k);
            k += 30;
            g.drawString("2: Try clearing your web-browsers cache from tools->internet options", 30, k);
            k += 30;
            g.drawString("3: Try using a different game-world", 30, k);
            k += 30;
            g.drawString("4: Try rebooting your computer", 30, k);
            k += 30;
            g.drawString("5: Try selecting a different version of Java from the play-game menu", 30, k);
        }
        if(this.genericLoadingError)
        {
            g.setFont(new Font("Helvetica", 1, 20));
            g.setColor(Color.white);
            g.drawString("Error - unable to load game!", 50, 50);
            g.drawString("To play RuneScape make sure you play from", 50, 100);
            g.drawString("http://www.runescape.com", 50, 150);
        }
        if(this.rsAlreadyLoaded)
        {
            g.setColor(Color.yellow);
            int l = 35;
            g.drawString("Error a copy of RuneScape already appears to be loaded", 30, l);
            l += 50;
            g.setColor(Color.white);
            g.drawString("To fix this try the following (in order):", 30, l);
            l += 50;
            g.setColor(Color.white);
            g.setFont(new Font("Helvetica", 1, 12));
            g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, l);
            l += 30;
            g.drawString("2: Try rebooting your computer, and reloading", 30, l);
            l += 30;
        }
    }

    private void method95()
    {
        for(int j = 0; j < this.npcCount; j++)
        {
            int k = this.npcIndices[j];
            NPC npc = this.npcArray[k];
            if(npc != null)
                this.method96(npc);
        }
    }

    private void method96(Entity entity)
    {
        if(entity.x < 128 || entity.y < 128 || entity.x >= 13184 || entity.y >= 13184)
        {
            entity.anim = -1;
            entity.anInt1520 = -1;
            entity.anInt1547 = 0;
            entity.anInt1548 = 0;
            entity.x = entity.smallX[0] * 128 + entity.anInt1540 * 64;
            entity.y = entity.smallY[0] * 128 + entity.anInt1540 * 64;
            entity.method446();
        }
        if(entity == Client.localPlayer && (entity.x < 1536 || entity.y < 1536 || entity.x >= 11776 || entity.y >= 11776))
        {
            entity.anim = -1;
            entity.anInt1520 = -1;
            entity.anInt1547 = 0;
            entity.anInt1548 = 0;
            entity.x = entity.smallX[0] * 128 + entity.anInt1540 * 64;
            entity.y = entity.smallY[0] * 128 + entity.anInt1540 * 64;
            entity.method446();
        }
        if(entity.anInt1547 > Client.loopCycle)
            this.method97(entity);
        else
        if(entity.anInt1548 >= Client.loopCycle)
            this.method98(entity);
        else
            this.method99(entity);
        this.method100(entity);
        this.method101(entity);
    }

    private void method97(Entity entity)
    {
        int i = entity.anInt1547 - Client.loopCycle;
        int j = entity.anInt1543 * 128 + entity.anInt1540 * 64;
        int k = entity.anInt1545 * 128 + entity.anInt1540 * 64;
        entity.x += (j - entity.x) / i;
        entity.y += (k - entity.y) / i;
        entity.anInt1503 = 0;
        if(entity.anInt1549 == 0)
            entity.turnDirection = 1024;
        if(entity.anInt1549 == 1)
            entity.turnDirection = 1536;
        if(entity.anInt1549 == 2)
            entity.turnDirection = 0;
        if(entity.anInt1549 == 3)
            entity.turnDirection = 512;
    }

    private void method98(Entity entity)
    {
        if(entity.anInt1548 == Client.loopCycle || entity.anim == -1 || entity.anInt1529 != 0 || entity.anInt1528 + 1 > Animation.animations[entity.anim].duration(entity.anInt1527))
        {
            int i = entity.anInt1548 - entity.anInt1547;
            int j = Client.loopCycle - entity.anInt1547;
            int k = entity.anInt1543 * 128 + entity.anInt1540 * 64;
            int l = entity.anInt1545 * 128 + entity.anInt1540 * 64;
            int i1 = entity.anInt1544 * 128 + entity.anInt1540 * 64;
            int j1 = entity.anInt1546 * 128 + entity.anInt1540 * 64;
            entity.x = (k * (i - j) + i1 * j) / i;
            entity.y = (l * (i - j) + j1 * j) / i;
        }
        entity.anInt1503 = 0;
        if(entity.anInt1549 == 0)
            entity.turnDirection = 1024;
        if(entity.anInt1549 == 1)
            entity.turnDirection = 1536;
        if(entity.anInt1549 == 2)
            entity.turnDirection = 0;
        if(entity.anInt1549 == 3)
            entity.turnDirection = 512;
        entity.anInt1552 = entity.turnDirection;
    }

    private void method99(Entity entity)
    {
        entity.anInt1517 = entity.anInt1511;
        if(entity.smallXYIndex == 0)
        {
            entity.anInt1503 = 0;
            return;
        }
        if(entity.anim != -1 && entity.anInt1529 == 0)
        {
            Animation animation = Animation.animations[entity.anim];
            if(entity.anInt1542 > 0 && animation.animatingPrecedence == 0)
            {
                entity.anInt1503++;
                return;
            }
            if(entity.anInt1542 <= 0 && animation.walkingPrecedence == 0)
            {
                entity.anInt1503++;
                return;
            }
        }
        int i = entity.x;
        int j = entity.y;
        int k = entity.smallX[entity.smallXYIndex - 1] * 128 + entity.anInt1540 * 64;
        int l = entity.smallY[entity.smallXYIndex - 1] * 128 + entity.anInt1540 * 64;
        if(k - i > 256 || k - i < -256 || l - j > 256 || l - j < -256)
        {
            entity.x = k;
            entity.y = l;
            return;
        }
        if(i < k)
        {
            if(j < l)
                entity.turnDirection = 1280;
            else
            if(j > l)
                entity.turnDirection = 1792;
            else
                entity.turnDirection = 1536;
        } else
        if(i > k)
        {
            if(j < l)
                entity.turnDirection = 768;
            else
            if(j > l)
                entity.turnDirection = 256;
            else
                entity.turnDirection = 512;
        } else
        if(j < l)
            entity.turnDirection = 1024;
        else
            entity.turnDirection = 0;
        int i1 = entity.turnDirection - entity.anInt1552 & 0x7ff;
        if(i1 > 1024)
            i1 -= 2048;
        int j1 = entity.anInt1555;
        if(i1 >= -256 && i1 <= 256)
            j1 = entity.anInt1554;
        else
        if(i1 >= 256 && i1 < 768)
            j1 = entity.anInt1557;
        else
        if(i1 >= -768 && i1 <= -256)
            j1 = entity.anInt1556;
        if(j1 == -1)
            j1 = entity.anInt1554;
        entity.anInt1517 = j1;
        int k1 = 4;
        if(entity.anInt1552 != entity.turnDirection && entity.interactingEntity == -1 && entity.anInt1504 != 0)
            k1 = 2;
        if(entity.smallXYIndex > 2)
            k1 = 6;
        if(entity.smallXYIndex > 3)
            k1 = 8;
        if(entity.anInt1503 > 0 && entity.smallXYIndex > 1)
        {
            k1 = 8;
            entity.anInt1503--;
        }
        if(entity.aBooleanArray1553[entity.smallXYIndex - 1])
            k1 <<= 1;
        if(k1 >= 8 && entity.anInt1517 == entity.anInt1554 && entity.anInt1505 != -1)
            entity.anInt1517 = entity.anInt1505;
        if(i < k)
        {
            entity.x += k1;
            if(entity.x > k)
                entity.x = k;
        } else
        if(i > k)
        {
            entity.x -= k1;
            if(entity.x < k)
                entity.x = k;
        }
        if(j < l)
        {
            entity.y += k1;
            if(entity.y > l)
                entity.y = l;
        } else
        if(j > l)
        {
            entity.y -= k1;
            if(entity.y < l)
                entity.y = l;
        }
        if(entity.x == k && entity.y == l)
        {
            entity.smallXYIndex--;
            if(entity.anInt1542 > 0)
                entity.anInt1542--;
        }
    }

    private void method100(Entity entity)
    {
        if(entity.anInt1504 == 0)
            return;
        if(entity.interactingEntity != -1 && entity.interactingEntity < 32768)
        {
            NPC npc = this.npcArray[entity.interactingEntity];
            if(npc != null)
            {
                int i1 = entity.x - npc.x;
                int k1 = entity.y - npc.y;
                if(i1 != 0 || k1 != 0)
                    entity.turnDirection = (int)(Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
            }
        }
        if(entity.interactingEntity >= 32768)
        {
            int j = entity.interactingEntity - 32768;
            if(j == this.localPlayerIndex)
                j = this.myPlayerIndex;
            Player player = this.playerArray[j];
            if(player != null)
            {
                int l1 = entity.x - player.x;
                int i2 = entity.y - player.y;
                if(l1 != 0 || i2 != 0)
                    entity.turnDirection = (int)(Math.atan2(l1, i2) * 325.94900000000001D) & 0x7ff;
            }
        }
        if((entity.anInt1538 != 0 || entity.anInt1539 != 0) && (entity.smallXYIndex == 0 || entity.anInt1503 > 0))
        {
            int k = entity.x - (entity.anInt1538 - this.baseX - this.baseX) * 64;
            int j1 = entity.y - (entity.anInt1539 - this.baseY - this.baseY) * 64;
            if(k != 0 || j1 != 0)
                entity.turnDirection = (int)(Math.atan2(k, j1) * 325.94900000000001D) & 0x7ff;
            entity.anInt1538 = 0;
            entity.anInt1539 = 0;
        }
        int l = entity.turnDirection - entity.anInt1552 & 0x7ff;
        if(l != 0)
        {
            if(l < entity.anInt1504 || l > 2048 - entity.anInt1504)
                entity.anInt1552 = entity.turnDirection;
            else
            if(l > 1024)
                entity.anInt1552 -= entity.anInt1504;
            else
                entity.anInt1552 += entity.anInt1504;
            entity.anInt1552 &= 0x7ff;
            if(entity.anInt1517 == entity.anInt1511 && entity.anInt1552 != entity.turnDirection)
            {
                if(entity.anInt1512 != -1)
                {
                    entity.anInt1517 = entity.anInt1512;
                    return;
                }
                entity.anInt1517 = entity.anInt1554;
            }
        }
    }

    private void method101(Entity entity)
    {
        entity.aBoolean1541 = false;
        if(entity.anInt1517 != -1)
        {
            Animation animation = Animation.animations[entity.anInt1517];
            entity.anInt1519++;
            if(entity.anInt1518 < animation.frameCount && entity.anInt1519 > animation.duration(entity.anInt1518))
            {
                entity.anInt1519 = 0;
                entity.anInt1518++;
            }
            if(entity.anInt1518 >= animation.frameCount)
            {
                entity.anInt1519 = 0;
                entity.anInt1518 = 0;
            }
        }
        if(entity.anInt1520 != -1 && Client.loopCycle >= entity.anInt1523)
        {
            if(entity.anInt1521 < 0)
                entity.anInt1521 = 0;
            Animation animation_1 = Graphic.graphics[entity.anInt1520].animation;
            for(entity.anInt1522++; entity.anInt1521 < animation_1.frameCount && entity.anInt1522 > animation_1.duration(entity.anInt1521); entity.anInt1521++)
                entity.anInt1522 -= animation_1.duration(entity.anInt1521);

            if(entity.anInt1521 >= animation_1.frameCount && (entity.anInt1521 < 0 || entity.anInt1521 >= animation_1.frameCount))
                entity.anInt1520 = -1;
        }
        if(entity.anim != -1 && entity.anInt1529 <= 1)
        {
            Animation animation_2 = Animation.animations[entity.anim];
            if(animation_2.animatingPrecedence == 1 && entity.anInt1542 > 0 && entity.anInt1547 <= Client.loopCycle && entity.anInt1548 < Client.loopCycle)
            {
                entity.anInt1529 = 1;
                return;
            }
        }
        if(entity.anim != -1 && entity.anInt1529 == 0)
        {
            Animation animation_3 = Animation.animations[entity.anim];
            for(entity.anInt1528++; entity.anInt1527 < animation_3.frameCount && entity.anInt1528 > animation_3.duration(entity.anInt1527); entity.anInt1527++)
                entity.anInt1528 -= animation_3.duration(entity.anInt1527);

            if(entity.anInt1527 >= animation_3.frameCount)
            {
                entity.anInt1527 -= animation_3.loopOffset;
                entity.anInt1530++;
                if(entity.anInt1530 >= animation_3.maximumLoops)
                    entity.anim = -1;
                if(entity.anInt1527 < 0 || entity.anInt1527 >= animation_3.frameCount)
                    entity.anim = -1;
            }
            entity.aBoolean1541 = animation_3.stretches;
        }
        if(entity.anInt1529 > 0)
            entity.anInt1529--;
    }

    private void drawGameScreen()
    {
        if(this.welcomeScreenRaised)
        {
            this.welcomeScreenRaised = false;
            
            this.needDrawTabArea = true;
            this.inputTaken = true;
            this.tabAreaAltered = true;
            this.aBoolean1233 = true;
        }
        if(this.loadingStage == 2)
            this.method146();
        if(this.menuOpen && this.menuScreenArea == 1)
            this.needDrawTabArea = true;
        if(this.invOverlayInterfaceID != -1)
        {
            boolean flag1 = this.method119(this.anInt945, this.invOverlayInterfaceID);
            if(flag1)
                this.needDrawTabArea = true;
        }
        if(this.atInventoryInterfaceType == 2)
            this.needDrawTabArea = true;
        if(this.activeInterfaceType == 2)
            this.needDrawTabArea = true;
        if(this.needDrawTabArea)
        {
            this.drawTabArea();
            this.needDrawTabArea = false;
        }
        if(this.backDialogID == -1)
        {
            this.aClass9_1059.scrollPosition = this.anInt1211 - this.anInt1089 - 77;
            if(this.applet.mouseX > 448 && this.applet.mouseX < 560 && this.applet.mouseY > 332)
                this.method65(463, 77, this.applet.mouseX - 17, this.applet.mouseY - 357, this.aClass9_1059, 0, false, this.anInt1211);
            int i = this.anInt1211 - 77 - this.aClass9_1059.scrollPosition;
            if(i < 0)
                i = 0;
            if(i > this.anInt1211 - 77)
                i = this.anInt1211 - 77;
            if(this.anInt1089 != i)
            {
                this.anInt1089 = i;
                this.inputTaken = true;
            }
        }
        if(this.backDialogID != -1)
        {
            boolean flag2 = this.method119(this.anInt945, this.backDialogID);
            if(flag2)
                this.inputTaken = true;
        }
        if(this.atInventoryInterfaceType == 3)
            this.inputTaken = true;
        if(this.activeInterfaceType == 3)
            this.inputTaken = true;
        if(this.aString844 != null)
            this.inputTaken = true;
        if(this.menuOpen && this.menuScreenArea == 2)
            this.inputTaken = true;
        if(this.inputTaken)
        {
            this.drawChatArea();
            this.inputTaken = false;
        }
        if(this.anInt1054 != -1)
            this.tabAreaAltered = true;
        if(this.tabAreaAltered)
        {
            if(this.anInt1054 != -1 && this.anInt1054 == this.tabID)
            {
                this.anInt1054 = -1;
                this.stream.writeOpcode(120);
                this.stream.writeByte(this.tabID);
            }
            this.tabAreaAltered = false;
            this.aRSImageProducer_1125.initDrawingArea();
            IndexedImageRenderer.draw(this.backHmid1, 0, 0);
            if(this.invOverlayInterfaceID == -1)
            {
                if(this.tabInterfaceIDs[this.tabID] != -1)
                {
                    if(this.tabID == 0)
                        IndexedImageRenderer.draw(this.redStone1, 22, 10);
                    if(this.tabID == 1)
                        IndexedImageRenderer.draw(this.redStone2, 54, 8);
                    if(this.tabID == 2)
                        IndexedImageRenderer.draw(this.redStone2, 82, 8);
                    if(this.tabID == 3)
                        IndexedImageRenderer.draw(this.redStone3, 110, 8);
                    if(this.tabID == 4)
                        IndexedImageRenderer.draw(this.redStone2_2, 153, 8);
                    if(this.tabID == 5)
                        IndexedImageRenderer.draw(this.redStone2_2, 181, 8);
                    if(this.tabID == 6)
                        IndexedImageRenderer.draw(this.redStone1_2, 209, 9);
                }
                if(this.tabInterfaceIDs[0] != -1 && (this.anInt1054 != 0 || Client.loopCycle % 20 < 10))
                    IndexedImageRenderer.draw(this.sideIcons[0], 29, 13);
                if(this.tabInterfaceIDs[1] != -1 && (this.anInt1054 != 1 || Client.loopCycle % 20 < 10))
                    IndexedImageRenderer.draw(this.sideIcons[1], 53, 11);
                if(this.tabInterfaceIDs[2] != -1 && (this.anInt1054 != 2 || Client.loopCycle % 20 < 10))
                    IndexedImageRenderer.draw(this.sideIcons[2], 82, 11);
                if(this.tabInterfaceIDs[3] != -1 && (this.anInt1054 != 3 || Client.loopCycle % 20 < 10))
                    IndexedImageRenderer.draw(this.sideIcons[3], 115, 12);
                if(this.tabInterfaceIDs[4] != -1 && (this.anInt1054 != 4 || Client.loopCycle % 20 < 10))
                    IndexedImageRenderer.draw(this.sideIcons[4], 153, 13);
                if(this.tabInterfaceIDs[5] != -1 && (this.anInt1054 != 5 || Client.loopCycle % 20 < 10))
                    IndexedImageRenderer.draw(this.sideIcons[5], 180, 11);
                if(this.tabInterfaceIDs[6] != -1 && (this.anInt1054 != 6 || Client.loopCycle % 20 < 10))
                    IndexedImageRenderer.draw(this.sideIcons[6], 208, 13);
            }
            this.aRSImageProducer_1125.drawGraphics(160, this.applet.graphics, 516);
            this.aRSImageProducer_1124.initDrawingArea();
            IndexedImageRenderer.draw(this.backBase2, 0, 0);
            if(this.invOverlayInterfaceID == -1)
            {
                if(this.tabInterfaceIDs[this.tabID] != -1)
                {
                    if(this.tabID == 7)
                        IndexedImageRenderer.draw(this.redStone1_3, 42, 0);
                    if(this.tabID == 8)
                        IndexedImageRenderer.draw(this.redStone2_3, 74, 0);
                    if(this.tabID == 9)
                        IndexedImageRenderer.draw(this.redStone2_3, 102, 0);
                    if(this.tabID == 10)
                        IndexedImageRenderer.draw(this.redStone3_2, 130, 1);
                    if(this.tabID == 11)
                        IndexedImageRenderer.draw(this.redStone2_4, 173, 0);
                    if(this.tabID == 12)
                        IndexedImageRenderer.draw(this.redStone2_4, 201, 0);
                    if(this.tabID == 13)
                        IndexedImageRenderer.draw(this.redStone1_4, 229, 0);
                }
                if(this.tabInterfaceIDs[8] != -1 && (this.anInt1054 != 8 || Client.loopCycle % 20 < 10))
                    IndexedImageRenderer.draw(this.sideIcons[7], 74, 2);
                if(this.tabInterfaceIDs[9] != -1 && (this.anInt1054 != 9 || Client.loopCycle % 20 < 10))
                    IndexedImageRenderer.draw(this.sideIcons[8], 102, 3);
                if(this.tabInterfaceIDs[10] != -1 && (this.anInt1054 != 10 || Client.loopCycle % 20 < 10))
                    IndexedImageRenderer.draw(this.sideIcons[9], 137, 4);
                if(this.tabInterfaceIDs[11] != -1 && (this.anInt1054 != 11 || Client.loopCycle % 20 < 10))
                    IndexedImageRenderer.draw(this.sideIcons[10], 174, 2);
                if(this.tabInterfaceIDs[12] != -1 && (this.anInt1054 != 12 || Client.loopCycle % 20 < 10))
                    IndexedImageRenderer.draw(this.sideIcons[11], 201, 2);
                if(this.tabInterfaceIDs[13] != -1 && (this.anInt1054 != 13 || Client.loopCycle % 20 < 10))
                    IndexedImageRenderer.draw(this.sideIcons[12], 226, 2);
            }
            this.aRSImageProducer_1124.drawGraphics(466, this.applet.graphics, 496);
            this.aRSImageProducer_1165.initDrawingArea();
        }
        if(this.aBoolean1233)
        {
            this.aBoolean1233 = false;
            this.aRSImageProducer_1123.initDrawingArea();
            IndexedImageRenderer.draw(this.backBase1, 0, 0);
            this.aTextDrawingArea_1271.method382(0xffffff, 55, "Public chat", 28, true);
            if(this.publicChatMode == 0)
                this.aTextDrawingArea_1271.method382(65280, 55, "On", 41, true);
            if(this.publicChatMode == 1)
                this.aTextDrawingArea_1271.method382(0xffff00, 55, "Friends", 41, true);
            if(this.publicChatMode == 2)
                this.aTextDrawingArea_1271.method382(0xff0000, 55, "Off", 41, true);
            if(this.publicChatMode == 3)
                this.aTextDrawingArea_1271.method382(65535, 55, "Hide", 41, true);
            this.aTextDrawingArea_1271.method382(0xffffff, 184, "Private chat", 28, true);
            if(this.privateChatMode == 0)
                this.aTextDrawingArea_1271.method382(65280, 184, "On", 41, true);
            if(this.privateChatMode == 1)
                this.aTextDrawingArea_1271.method382(0xffff00, 184, "Friends", 41, true);
            if(this.privateChatMode == 2)
                this.aTextDrawingArea_1271.method382(0xff0000, 184, "Off", 41, true);
            this.aTextDrawingArea_1271.method382(0xffffff, 324, "Trade/compete", 28, true);
            if(this.tradeMode == 0)
                this.aTextDrawingArea_1271.method382(65280, 324, "On", 41, true);
            if(this.tradeMode == 1)
                this.aTextDrawingArea_1271.method382(0xffff00, 324, "Friends", 41, true);
            if(this.tradeMode == 2)
                this.aTextDrawingArea_1271.method382(0xff0000, 324, "Off", 41, true);
            this.aTextDrawingArea_1271.method382(0xffffff, 458, "Report abuse", 33, true);
            this.aRSImageProducer_1123.drawGraphics(453, this.applet.graphics, 0);
            this.aRSImageProducer_1165.initDrawingArea();
        }
        this.anInt945 = 0;
    }

    private boolean buildFriendsListMenu(RSInterface class9)
    {
        int i = class9.anInt214;
        if(i >= 1 && i <= 200 || i >= 701 && i <= 900)
        {
            if(i >= 801)
                i -= 701;
            else
            if(i >= 701)
                i -= 601;
            else
            if(i >= 101)
                i -= 101;
            else
                i--;
            this.menuActionName[this.menuActionRow] = "Remove @whi@" + this.friendsList[i];
            this.menuActionID[this.menuActionRow] = 792;
            this.menuActionRow++;
            this.menuActionName[this.menuActionRow] = "Message @whi@" + this.friendsList[i];
            this.menuActionID[this.menuActionRow] = 639;
            this.menuActionRow++;
            return true;
        }
        if(i >= 401 && i <= 500)
        {
            this.menuActionName[this.menuActionRow] = "Remove @whi@" + class9.message;
            this.menuActionID[this.menuActionRow] = 322;
            this.menuActionRow++;
            return true;
        } else
        {
            return false;
        }
    }

    private void method104()
    {
        AnimableObject class30_sub2_sub4_sub3 = (AnimableObject)this.aClass19_1056.getFront();
        for(; class30_sub2_sub4_sub3 != null; class30_sub2_sub4_sub3 = (AnimableObject)this.aClass19_1056.getNext())
            if(class30_sub2_sub4_sub3.anInt1560 != this.plane || class30_sub2_sub4_sub3.aBoolean1567)
                class30_sub2_sub4_sub3.unlink();
            else
            if(Client.loopCycle >= class30_sub2_sub4_sub3.anInt1564)
            {
                class30_sub2_sub4_sub3.method454(this.anInt945);
                if(class30_sub2_sub4_sub3.aBoolean1567)
                    class30_sub2_sub4_sub3.unlink();
                else
                    this.worldController.method285(class30_sub2_sub4_sub3.anInt1560, 0, class30_sub2_sub4_sub3.anInt1563, -1, class30_sub2_sub4_sub3.anInt1562, 60, class30_sub2_sub4_sub3.anInt1561, class30_sub2_sub4_sub3, false);
            }

    }

    private void drawInterface(int j, int k, RSInterface class9, int l)
    {
        if(class9.type != 0 || class9.children == null)
            return;
        if(class9.aBoolean266 && this.anInt1026 != class9.id && this.anInt1048 != class9.id && this.anInt1039 != class9.id)
            return;
        int i1 = DrawingArea.topX;
        int j1 = DrawingArea.topY;
        int k1 = DrawingArea.bottomX;
        int l1 = DrawingArea.bottomY;
        DrawingArea.setDrawingArea(l + class9.height, k, k + class9.width, l);
        int i2 = class9.children.length;
        for(int j2 = 0; j2 < i2; j2++)
        {
            int k2 = class9.childX[j2] + k;
            int l2 = (class9.childY[j2] + l) - j;
            RSInterface class9_1 = RSInterface.interfaceCache[class9.children[j2]];
            k2 += class9_1.anInt263;
            l2 += class9_1.anInt265;
            if(class9_1.anInt214 > 0)
                this.drawFriendsListOrWelcomeScreen(class9_1);
            if(class9_1.type == 0)
            {
                if(class9_1.scrollPosition > class9_1.scrollMax - class9_1.height)
                    class9_1.scrollPosition = class9_1.scrollMax - class9_1.height;
                if(class9_1.scrollPosition < 0)
                    class9_1.scrollPosition = 0;
                this.drawInterface(class9_1.scrollPosition, k2, class9_1, l2);
                if(class9_1.scrollMax > class9_1.height)
                    this.method30(class9_1.height, class9_1.scrollPosition, l2, k2 + class9_1.width, class9_1.scrollMax);
            } else
            if(class9_1.type != 1)
                if(class9_1.type == 2)
                {
                    int i3 = 0;
                    for(int l3 = 0; l3 < class9_1.height; l3++)
                    {
                        for(int l4 = 0; l4 < class9_1.width; l4++)
                        {
                            int k5 = k2 + l4 * (32 + class9_1.invSpritePadX);
                            int j6 = l2 + l3 * (32 + class9_1.invSpritePadY);
                            if(i3 < 20)
                            {
                                k5 += class9_1.spritesX[i3];
                                j6 += class9_1.spritesY[i3];
                            }
                            if(class9_1.inv[i3] > 0)
                            {
                                int k6 = 0;
                                int j7 = 0;
                                int j9 = class9_1.inv[i3] - 1;
                                if(k5 > DrawingArea.topX - 32 && k5 < DrawingArea.bottomX && j6 > DrawingArea.topY - 32 && j6 < DrawingArea.bottomY || this.activeInterfaceType != 0 && this.anInt1085 == i3)
                                {
                                    int l9 = 0;
                                    if(this.itemSelected == 1 && this.anInt1283 == i3 && this.anInt1284 == class9_1.id)
                                        l9 = 0xffffff;
                                    Sprite class30_sub2_sub1_sub1_2 = ItemDef.getSprite(j9, class9_1.invStackSizes[i3], l9);
                                    if(class30_sub2_sub1_sub1_2 != null)
                                    {
                                        if(this.activeInterfaceType != 0 && this.anInt1085 == i3 && this.anInt1084 == class9_1.id)
                                        {
                                            k6 = this.applet.mouseX - this.anInt1087;
                                            j7 = this.applet.mouseY - this.anInt1088;
                                            if(k6 < 5 && k6 > -5)
                                                k6 = 0;
                                            if(j7 < 5 && j7 > -5)
                                                j7 = 0;
                                            if(this.anInt989 < 5)
                                            {
                                                k6 = 0;
                                                j7 = 0;
                                            }
                                            SpriteRenderer.drawSprite1(class30_sub2_sub1_sub1_2, k5 + k6, j6 + j7, 128);
                                            if(j6 + j7 < DrawingArea.topY && class9.scrollPosition > 0)
                                            {
                                                int i10 = (this.anInt945 * (DrawingArea.topY - j6 - j7)) / 3;
                                                if(i10 > this.anInt945 * 10)
                                                    i10 = this.anInt945 * 10;
                                                if(i10 > class9.scrollPosition)
                                                    i10 = class9.scrollPosition;
                                                class9.scrollPosition -= i10;
                                                this.anInt1088 += i10;
                                            }
                                            if(j6 + j7 + 32 > DrawingArea.bottomY && class9.scrollPosition < class9.scrollMax - class9.height)
                                            {
                                                int j10 = (this.anInt945 * ((j6 + j7 + 32) - DrawingArea.bottomY)) / 3;
                                                if(j10 > this.anInt945 * 10)
                                                    j10 = this.anInt945 * 10;
                                                if(j10 > class9.scrollMax - class9.height - class9.scrollPosition)
                                                    j10 = class9.scrollMax - class9.height - class9.scrollPosition;
                                                class9.scrollPosition += j10;
                                                this.anInt1088 -= j10;
                                            }
                                        } else
                                        if(this.atInventoryInterfaceType != 0 && this.atInventoryIndex == i3 && this.atInventoryInterface == class9_1.id)
                                            SpriteRenderer.drawSprite1(class30_sub2_sub1_sub1_2, k5, j6, 128);
                                        else
                                            SpriteRenderer.drawSprite(class30_sub2_sub1_sub1_2, k5, j6);
                                        if(class30_sub2_sub1_sub1_2.resizeWidth == 33 || class9_1.invStackSizes[i3] != 1)
                                        {
                                            int k10 = class9_1.invStackSizes[i3];
                                            this.aTextDrawingArea_1270.method385(0, Client.intToKOrMil(k10), j6 + 10 + j7, k5 + 1 + k6);
                                            this.aTextDrawingArea_1270.method385(0xffff00, Client.intToKOrMil(k10), j6 + 9 + j7, k5 + k6);
                                        }
                                    }
                                }
                            } else
                            if(class9_1.sprites != null && i3 < 20)
                            {
                                Sprite class30_sub2_sub1_sub1_1 = class9_1.sprites[i3];
                                if(class30_sub2_sub1_sub1_1 != null)
                                    SpriteRenderer.drawSprite(class30_sub2_sub1_sub1_1, k5, j6);
                            }
                            i3++;
                        }

                    }

                } else
                if(class9_1.type == 3)
                {
                    boolean flag = false;
                    if(this.anInt1039 == class9_1.id || this.anInt1048 == class9_1.id || this.anInt1026 == class9_1.id)
                        flag = true;
                    int j3;
                    if(this.interfaceIsSelected(class9_1))
                    {
                        j3 = class9_1.anInt219;
                        if(flag && class9_1.anInt239 != 0)
                            j3 = class9_1.anInt239;
                    } else
                    {
                        j3 = class9_1.textColor;
                        if(flag && class9_1.anInt216 != 0)
                            j3 = class9_1.anInt216;
                    }
                    if(class9_1.aByte254 == 0)
                    {
                        if(class9_1.aBoolean227)
                            DrawingArea.drawPixels(class9_1.height, l2, k2, j3, class9_1.width);
                        else
                            DrawingArea.fillPixels(k2, class9_1.width, class9_1.height, j3, l2);
                    } else
                    if(class9_1.aBoolean227)
                        DrawingArea.method335(j3, l2, class9_1.width, class9_1.height, 256 - (class9_1.aByte254 & 0xff), k2);
                    else
                        DrawingArea.method338(l2, class9_1.height, 256 - (class9_1.aByte254 & 0xff), j3, class9_1.width, k2);
                } else
                if(class9_1.type == 4)
                {
                    TextDrawingArea textDrawingArea = class9_1.textDrawingAreas;
                    String s = class9_1.message;
                    boolean flag1 = false;
                    if(this.anInt1039 == class9_1.id || this.anInt1048 == class9_1.id || this.anInt1026 == class9_1.id)
                        flag1 = true;
                    int i4;
                    if(this.interfaceIsSelected(class9_1))
                    {
                        i4 = class9_1.anInt219;
                        if(flag1 && class9_1.anInt239 != 0)
                            i4 = class9_1.anInt239;
                        if(class9_1.aString228.length() > 0)
                            s = class9_1.aString228;
                    } else
                    {
                        i4 = class9_1.textColor;
                        if(flag1 && class9_1.anInt216 != 0)
                            i4 = class9_1.anInt216;
                    }
                    if(class9_1.atActionType == 6 && this.aBoolean1149)
                    {
                        s = "Please wait...";
                        i4 = class9_1.textColor;
                    }
                    if(DrawingArea.width == 479)
                    {
                        if(i4 == 0xffff00)
                            i4 = 255;
                        if(i4 == 49152)
                            i4 = 0xffffff;
                    }
                    for(int l6 = l2 + textDrawingArea.anInt1497; s.length() > 0; l6 += textDrawingArea.anInt1497)
                    {
                        if(s.indexOf("%") != -1)
                        {
                            do
                            {
                                int k7 = s.indexOf("%1");
                                if(k7 == -1)
                                    break;
                                s = s.substring(0, k7) + this.interfaceIntToString(this.extractInterfaceValues(class9_1, 0)) + s.substring(k7 + 2);
                            } while(true);
                            do
                            {
                                int l7 = s.indexOf("%2");
                                if(l7 == -1)
                                    break;
                                s = s.substring(0, l7) + this.interfaceIntToString(this.extractInterfaceValues(class9_1, 1)) + s.substring(l7 + 2);
                            } while(true);
                            do
                            {
                                int i8 = s.indexOf("%3");
                                if(i8 == -1)
                                    break;
                                s = s.substring(0, i8) + this.interfaceIntToString(this.extractInterfaceValues(class9_1, 2)) + s.substring(i8 + 2);
                            } while(true);
                            do
                            {
                                int j8 = s.indexOf("%4");
                                if(j8 == -1)
                                    break;
                                s = s.substring(0, j8) + this.interfaceIntToString(this.extractInterfaceValues(class9_1, 3)) + s.substring(j8 + 2);
                            } while(true);
                            do
                            {
                                int k8 = s.indexOf("%5");
                                if(k8 == -1)
                                    break;
                                s = s.substring(0, k8) + this.interfaceIntToString(this.extractInterfaceValues(class9_1, 4)) + s.substring(k8 + 2);
                            } while(true);
                        }
                        int l8 = s.indexOf("\\n");
                        String s1;
                        if(l8 != -1)
                        {
                            s1 = s.substring(0, l8);
                            s = s.substring(l8 + 2);
                        } else
                        {
                            s1 = s;
                            s = "";
                        }
                        if(class9_1.centerText)
                            textDrawingArea.method382(i4, k2 + class9_1.width / 2, s1, l6, class9_1.aBoolean268);
                        else
                            textDrawingArea.method389(class9_1.aBoolean268, k2, i4, s1, l6);
                    }

                } else
                if(class9_1.type == 5)
                {
                    Sprite sprite;
                    if(this.interfaceIsSelected(class9_1))
                        sprite = class9_1.sprite2;
                    else
                        sprite = class9_1.sprite1;
                    if(sprite != null)
                        SpriteRenderer.drawSprite(sprite, k2, l2);
                } else
                if(class9_1.type == 6)
                {
                    int k3 = Texture.originViewX;
                    int j4 = Texture.originViewY;
                    Texture.originViewX = k2 + class9_1.width / 2;
                    Texture.originViewY = l2 + class9_1.height / 2;
                    int i5 = Texture.anIntArray1470[class9_1.anInt270] * class9_1.anInt269 >> 16;
                    int l5 = Texture.anIntArray1471[class9_1.anInt270] * class9_1.anInt269 >> 16;
                    boolean flag2 = this.interfaceIsSelected(class9_1);
                    int i7;
                    if(flag2)
                        i7 = class9_1.anInt258;
                    else
                        i7 = class9_1.anInt257;
                    Model model;
                    if(i7 == -1)
                    {
                        model = class9_1.method209(-1, -1, flag2);
                    } else
                    {
                        Animation animation = Animation.animations[i7];
                        model = class9_1.method209(animation.secondaryFrames[class9_1.anInt246], animation.primaryFrames[class9_1.anInt246], flag2);
                    }
                    if(model != null)
                    	ModelRenderer.render(model, 0, class9_1.anInt271, 0, class9_1.anInt270, 0, i5, l5);
                    Texture.originViewX = k3;
                    Texture.originViewY = j4;
                } else
                if(class9_1.type == 7)
                {
                    TextDrawingArea textDrawingArea_1 = class9_1.textDrawingAreas;
                    int k4 = 0;
                    for(int j5 = 0; j5 < class9_1.height; j5++)
                    {
                        for(int i6 = 0; i6 < class9_1.width; i6++)
                        {
                            if(class9_1.inv[k4] > 0)
                            {
                                ItemDef itemDef = ItemDef.forID(class9_1.inv[k4] - 1);
                                String s2 = itemDef.name;
                                if(itemDef.stackable || class9_1.invStackSizes[k4] != 1)
                                    s2 = s2 + " x" + Client.intToKOrMilLongName(class9_1.invStackSizes[k4]);
                                int i9 = k2 + i6 * (115 + class9_1.invSpritePadX);
                                int k9 = l2 + j5 * (12 + class9_1.invSpritePadY);
                                if(class9_1.centerText)
                                    textDrawingArea_1.method382(class9_1.textColor, i9 + class9_1.width / 2, s2, k9, class9_1.aBoolean268);
                                else
                                    textDrawingArea_1.method389(class9_1.aBoolean268, i9, class9_1.textColor, s2, k9);
                            }
                            k4++;
                        }

                    }

                }           	else if (class9_1.type == 8
    						&& (this.anInt1500 == class9_1.id
    								|| this.anInt1044 == class9_1.id || this.anInt1129 == class9_1.id)
    						&& this.anInt1501 == 50 && !this.menuOpen) {
    					int boxWidth = 0;
    					int boxHeight = 0;
    					TextDrawingArea textDrawingArea_2 = this.aTextDrawingArea_1271;
    					for (String s1 = class9_1.message; s1.length() > 0;) {
    						if (s1.indexOf("%") != -1) {
    							do {
    								int k7 = s1.indexOf("%1");
    								if (k7 == -1)
    									break;
    								s1 = s1.substring(0, k7)
    										+ this.interfaceIntToString(this.extractInterfaceValues(
    												class9_1, 0))
    										+ s1.substring(k7 + 2);
    							} while (true);
    							do {
    								int l7 = s1.indexOf("%2");
    								if (l7 == -1)
    									break;
    								s1 = s1.substring(0, l7)
    										+ this.interfaceIntToString(this.extractInterfaceValues(
    												class9_1, 1))
    										+ s1.substring(l7 + 2);
    							} while (true);
    							do {
    								int i8 = s1.indexOf("%3");
    								if (i8 == -1)
    									break;
    								s1 = s1.substring(0, i8)
    										+ this.interfaceIntToString(this.extractInterfaceValues(
    												class9_1, 2))
    										+ s1.substring(i8 + 2);
    							} while (true);
    							do {
    								int j8 = s1.indexOf("%4");
    								if (j8 == -1)
    									break;
    								s1 = s1.substring(0, j8)
    										+ this.interfaceIntToString(this.extractInterfaceValues(
    												class9_1, 3))
    										+ s1.substring(j8 + 2);
    							} while (true);
    							do {
    								int k8 = s1.indexOf("%5");
    								if (k8 == -1)
    									break;
    								s1 = s1.substring(0, k8)
    										+ this.interfaceIntToString(this.extractInterfaceValues(
    												class9_1, 4))
    										+ s1.substring(k8 + 2);
    							} while (true);
    						}
    						int l7 = s1.indexOf("\\n");
    						String s4;
    						if (l7 != -1) {
    							s4 = s1.substring(0, l7);
    							s1 = s1.substring(l7 + 2);
    						} else {
    							s4 = s1;
    							s1 = "";
    						}
    						int j10 = textDrawingArea_2.getTextWidth(s4);
    						if (j10 > boxWidth) {
    							boxWidth = j10;
    						}
    						boxHeight += textDrawingArea_2.anInt1497 + 1;
    					}
    					boxWidth += 6;
    					boxHeight += 7;
    					int xPos = (k2 + class9_1.width) - 5 - boxWidth;
    					int yPos = l2 + class9_1.height + 5;
    					if (xPos < k2 + 5)
    						xPos = k2 + 5;
    					if (xPos + boxWidth > k + class9.width)
    						xPos = (k + class9.width) - boxWidth;
    					if (yPos + boxHeight > l + class9.height)
    						yPos = (l2 - boxHeight);
    					DrawingArea.drawPixels(boxHeight, yPos, xPos, 0xFFFFA0,
    							boxWidth);
    					DrawingArea.fillPixels(xPos, boxWidth, boxHeight, 0, yPos);
    					String s2 = class9_1.message;
    					for (int j11 = yPos + textDrawingArea_2.anInt1497 + 2; s2
    							.length() > 0; j11 += textDrawingArea_2.anInt1497 + 1) {// anInt1497
    						if (s2.indexOf("%") != -1) {
    							do {
    								int k7 = s2.indexOf("%1");
    								if (k7 == -1)
    									break;
    								s2 = s2.substring(0, k7)
    										+ this.interfaceIntToString(this.extractInterfaceValues(
    												class9_1, 0))
    										+ s2.substring(k7 + 2);
    							} while (true);
    							do {
    								int l7 = s2.indexOf("%2");
    								if (l7 == -1)
    									break;
    								s2 = s2.substring(0, l7)
    										+ this.interfaceIntToString(this.extractInterfaceValues(
    												class9_1, 1))
    										+ s2.substring(l7 + 2);
    							} while (true);
    							do {
    								int i8 = s2.indexOf("%3");
    								if (i8 == -1)
    									break;
    								s2 = s2.substring(0, i8)
    										+ this.interfaceIntToString(this.extractInterfaceValues(
    												class9_1, 2))
    										+ s2.substring(i8 + 2);
    							} while (true);
    							do {
    								int j8 = s2.indexOf("%4");
    								if (j8 == -1)
    									break;
    								s2 = s2.substring(0, j8)
    										+ this.interfaceIntToString(this.extractInterfaceValues(
    												class9_1, 3))
    										+ s2.substring(j8 + 2);
    							} while (true);
    							do {
    								int k8 = s2.indexOf("%5");
    								if (k8 == -1)
    									break;
    								s2 = s2.substring(0, k8)
    										+ this.interfaceIntToString(this.extractInterfaceValues(
    												class9_1, 4))
    										+ s2.substring(k8 + 2);
    							} while (true);
    						}
    						int l11 = s2.indexOf("\\n");
    						String s5;
    						if (l11 != -1) {
    							s5 = s2.substring(0, l11);
    							s2 = s2.substring(l11 + 2);
    						} else {
    							s5 = s2;
    							s2 = "";
    						}
    						if (class9_1.centerText) {
    							textDrawingArea_2.method382(yPos, xPos
    									+ class9_1.width / 2, s5, j11, false);
    						} else {
    							if (s5.contains("\\r")) {
    								String text = s5
    										.substring(0, s5.indexOf("\\r"));
    								String text2 = s5
    										.substring(s5.indexOf("\\r") + 2);
    								textDrawingArea_2.method389(false, xPos + 3, 0,
    										text, j11);
    								int rightX = boxWidth + xPos
    										- textDrawingArea_2.getTextWidth(text2)
    										- 2;
    								textDrawingArea_2.method389(false, rightX, 0,
    										text2, j11);
    								System.out.println("Box: " + boxWidth + "");
    							} else
    								textDrawingArea_2.method389(false, xPos + 3, 0,
    										s5, j11);
    						}
    					}
    				}
        
        }

        DrawingArea.setDrawingArea(l1, i1, k1, j1);
    }

    private void method107(int i, int j, BitBuffer buffer, Player player)
    {
        if((i & 0x400) != 0)
        {
            player.anInt1543 = buffer.readUByteS();
            player.anInt1545 = buffer.readUByteS();
            player.anInt1544 = buffer.readUByteS();
            player.anInt1546 = buffer.readUByteS();
            player.anInt1547 = buffer.readLEUShortA() + Client.loopCycle;
            player.anInt1548 = buffer.readUShortA() + Client.loopCycle;
            player.anInt1549 = buffer.readUByteS();
            player.method446();
        }
        if((i & 0x100) != 0)
        {
            player.anInt1520 = buffer.readLEUShort();
            int k = buffer.readInt();
            player.anInt1524 = k >> 16;
            player.anInt1523 = Client.loopCycle + (k & 0xffff);
            player.anInt1521 = 0;
            player.anInt1522 = 0;
            if(player.anInt1523 > Client.loopCycle)
                player.anInt1521 = -1;
            if(player.anInt1520 == 65535)
                player.anInt1520 = -1;
        }
        if((i & 8) != 0)
        {
            int l = buffer.readLEUShort();
            if(l == 65535)
                l = -1;
            int i2 = buffer.readNegUByte();
            if(l == player.anim && l != -1)
            {
                int i3 = Animation.animations[l].replayMode;
                if(i3 == 1)
                {
                    player.anInt1527 = 0;
                    player.anInt1528 = 0;
                    player.anInt1529 = i2;
                    player.anInt1530 = 0;
                }
                if(i3 == 2)
                    player.anInt1530 = 0;
            } else
            if(l == -1 || player.anim == -1 || Animation.animations[l].priority >= Animation.animations[player.anim].priority)
            {
                player.anim = l;
                player.anInt1527 = 0;
                player.anInt1528 = 0;
                player.anInt1529 = i2;
                player.anInt1530 = 0;
                player.anInt1542 = player.smallXYIndex;
            }
        }
        if((i & 4) != 0)
        {
            player.textSpoken = buffer.readString();
            if(player.textSpoken.charAt(0) == '~')
            {
                player.textSpoken = player.textSpoken.substring(1);
                this.pushMessage(player.textSpoken, 2, player.name);
            } else
            if(player == Client.localPlayer)
                this.pushMessage(player.textSpoken, 2, player.name);
            player.anInt1513 = 0;
            player.anInt1531 = 0;
            player.textCycle = 150;
        }
        if((i & 0x80) != 0)
        {
            int i1 = buffer.readLEUShort();
            int j2 = buffer.readUByte();
            int j3 = buffer.readNegUByte();
            int k3 = buffer.position;
            if(player.name != null && player.visible)
            {
                long l3 = TextClass.longForName(player.name);
                boolean flag = false;
                if(j2 <= 1)
                {
                    for(int i4 = 0; i4 < this.ignoreCount; i4++)
                    {
                        if(this.ignoreListAsLongs[i4] != l3)
                            continue;
                        flag = true;
                        break;
                    }

                }
                if(!flag && this.anInt1251 == 0)
                    try
                    {
                        this.aStream_834.position = 0;
                        buffer.readReverseData(j3, 0, this.aStream_834.payload);
                        this.aStream_834.position = 0;
                        String s = TextInput.method525(j3, this.aStream_834);
                        s = Censor.doCensor(s);
                        player.textSpoken = s;
                        player.anInt1513 = i1 >> 8;
	                    player.privelage = j2;

                        //entityMessage(player);
	
                        player.anInt1531 = i1 & 0xff;
                        player.textCycle = 150;
                        if(j2 == 2 || j2 == 3)
                            this.pushMessage(s, 1, "@cr2@" + player.name);
                        else
                        if(j2 == 1)
                            this.pushMessage(s, 1, "@cr1@" + player.name);
                        else
                            this.pushMessage(s, 2, player.name);
                    }
                    catch(Exception exception)
                    {
                        Utils.reporterror("cde2");
                    }
            }
            buffer.position = k3 + j3;
        }
        if((i & 1) != 0)
        {
            player.interactingEntity = buffer.readLEUShort();
            if(player.interactingEntity == 65535)
                player.interactingEntity = -1;
        }
        if((i & 0x10) != 0)
        {
            int j1 = buffer.readNegUByte();
            byte abyte0[] = new byte[j1];
            Buffer buffer_1 = new Buffer(abyte0);
            buffer.readData(abyte0, 0, j1);
            this.aBufferArray895s[j] = buffer_1;
            player.updatePlayer(buffer_1);
        }
        if((i & 2) != 0)
        {
            player.anInt1538 = buffer.readLEUShortA();
            player.anInt1539 = buffer.readLEUShort();
        }
        if((i & 0x20) != 0)
        {
            int k1 = buffer.readUByte();
            int k2 = buffer.readUByteA();
            player.updateHitData(k2, k1, Client.loopCycle);
            player.loopCycleStatus = Client.loopCycle + 300;
            player.currentHealth = buffer.readNegUByte();
            player.maxHealth = buffer.readUByte();
        }
        if((i & 0x200) != 0)
        {
            int l1 = buffer.readUByte();
            int l2 = buffer.readUByteS();
            player.updateHitData(l2, l1, Client.loopCycle);
            player.loopCycleStatus = Client.loopCycle + 300;
            player.currentHealth = buffer.readUByte();
            player.maxHealth = buffer.readNegUByte();
        }
    }

    private void method108()
    {
        try
        {
            int j = Client.localPlayer.x + this.anInt1278;
            int k = Client.localPlayer.y + this.anInt1131;
            if(this.anInt1014 - j < -500 || this.anInt1014 - j > 500 || this.anInt1015 - k < -500 || this.anInt1015 - k > 500)
            {
                this.anInt1014 = j;
                this.anInt1015 = k;
            }
            if(this.anInt1014 != j)
                this.anInt1014 += (j - this.anInt1014) / 16;
            if(this.anInt1015 != k)
                this.anInt1015 += (k - this.anInt1015) / 16;
            if(this.applet.keyArray[1] == 1)
                this.anInt1186 += (-24 - this.anInt1186) / 2;
            else
            if(this.applet.keyArray[2] == 1)
                this.anInt1186 += (24 - this.anInt1186) / 2;
            else
                this.anInt1186 /= 2;
            if(this.applet.keyArray[3] == 1)
                this.anInt1187 += (12 - this.anInt1187) / 2;
            else
            if(this.applet.keyArray[4] == 1)
                this.anInt1187 += (-12 - this.anInt1187) / 2;
            else
                this.anInt1187 /= 2;
              this.minimapInt1 = this.minimapInt1 + this.anInt1186 / 2 & 0x7ff;
              this.anInt1184 += this.anInt1187 / 2;
            if(this.anInt1184 < 128)
                this.anInt1184 = 128;
            if(this.anInt1184 > 383)
                this.anInt1184 = 383;
            int l = this.anInt1014 >> 7;
            int i1 = this.anInt1015 >> 7;
            int j1 = this.tileHeight(this.plane, this.anInt1015, this.anInt1014);
            int maxY = 0;
            if(l > 3 && i1 > 3 && l < 100 && i1 < 100)
            {
                for(int l1 = l - 4; l1 <= l + 4; l1++)
                {
                    for(int k2 = i1 - 4; k2 <= i1 + 4; k2++)
                    {
                        int l2 = this.plane;
                        if(l2 < 3 && (this.byteGroundArray[1][l1][k2] & 2) == 2)
                            l2++;
                        int i3 = j1 - this.intGroundArray[l2][l1][k2];
                        if(i3 > maxY)
                            maxY = i3;
                    }

                }

            }
            Client.anInt1005++;
            if(Client.anInt1005 > 1512)
            {
                Client.anInt1005 = 0;
                this.stream.writeOpcode(77);
                this.stream.writeByte(0);
                int i2 = this.stream.position;
                this.stream.writeByte((int)(Math.random() * 256D));
                this.stream.writeByte(101);
                this.stream.writeByte(233);
                this.stream.writeShort(45092);
                if((int)(Math.random() * 2D) == 0)
                    this.stream.writeShort(35784);
                this.stream.writeByte((int)(Math.random() * 256D));
                this.stream.writeByte(64);
                this.stream.writeByte(38);
                this.stream.writeShort((int)(Math.random() * 65536D));
                this.stream.writeShort((int)(Math.random() * 65536D));
                this.stream.writeSizeByte(this.stream.position - i2);
            }
            int y = maxY * 192;
            if(y > 0x17f00)
                y = 0x17f00;
            if(y < 32768)
                y = 32768;
            if(y > this.cameraMaxY)
            {
                this.cameraMaxY += (y - this.cameraMaxY) / 24;
                return;
            }
            if(y < this.cameraMaxY)
            {
                this.cameraMaxY += (y - this.cameraMaxY) / 80;
            }
        }
        catch(Exception _ex)
        {
            Utils.reporterror("glfc_ex " + Client.localPlayer.x + "," + Client.localPlayer.y + "," + this.anInt1014 + "," + this.anInt1015 + "," + this.anInt1069 + "," + this.anInt1070 + "," + this.baseX + "," + this.baseY);
            throw new RuntimeException("eek");
        }
    }

	@Override
	public void processDrawing()
    {
        if(this.rsAlreadyLoaded || this.loadingError || this.genericLoadingError)
        {
            this.showErrorScreen();
            return;
        }
        Client.anInt1061++;
        if(!this.loggedIn)
            this.drawLoginScreen(false);
        else
            this.drawGameScreen();
        this.anInt1213 = 0;
    }

    private boolean isFriendOrSelf(String s)
    {
        if(s == null)
            return false;
        for(int i = 0; i < this.friendsCount; i++)
            if(s.equalsIgnoreCase(this.friendsList[i]))
                return true;
        return s.equalsIgnoreCase(Client.localPlayer.name);
    }

    private static String combatDiffColor(int i, int j)
    {
        int k = i - j;
        if(k < -9)
            return "@red@";
        if(k < -6)
            return "@or3@";
        if(k < -3)
            return "@or2@";
        if(k < 0)
            return "@or1@";
        if(k > 9)
            return "@gre@";
        if(k > 6)
            return "@gr3@";
        if(k > 3)
            return "@gr2@";
        if(k > 0)
            return "@gr1@";
        else
            return "@yel@";
    }

    private void setWaveVolume(int i)
    {
        signlink.wavevol = i;
    }

    private void draw3dScreen()
    {
		if (Client.showChatComponents) {
			this.drawSplitPrivateChat();
		}
        if(this.crossType == 1)
        {
            SpriteRenderer.drawSprite(this.crosses[this.crossIndex / 100], this.crossX - 8 - 4, this.crossY - 8 - 4);
            Client.anInt1142++;
            if(Client.anInt1142 > 67)
            {
                Client.anInt1142 = 0;
                this.stream.writeOpcode(78);
            }
        }
        if(this.crossType == 2)
            SpriteRenderer.drawSprite(this.crosses[4 + this.crossIndex / 100], this.crossX - 8 - 4, this.crossY - 8 - 4);
        if(this.anInt1018 != -1)
        {
            this.method119(this.anInt945, this.anInt1018);
            this.drawInterface(0, 0, RSInterface.interfaceCache[this.anInt1018], 0);
        }
        if(this.openInterfaceID != -1)
        {
            this.method119(this.anInt945, this.openInterfaceID);
            this.drawInterface(0, 0, RSInterface.interfaceCache[this.openInterfaceID], 0);
        }
        this.method70();
        if(!this.menuOpen)
        {
            this.processRightClick();
            this.drawTooltip();
        } else
        if(this.menuScreenArea == 0)
            this.drawMenu(RSBase.frameMode == ScreenMode.FIXED ? 4 : 0, RSBase.frameMode == ScreenMode.FIXED ? 4 : 0);
        if(this.anInt1055 == 1)
            SpriteRenderer.drawSprite(this.headIcons[1], 472, 296);
        if(Client.fpsOn)
        {
            char c = '\u01FB';
            int k = 20;
            int i1 = 0xffff00;
            if(this.applet.fps < 15)
                i1 = 0xff0000;
            this.aTextDrawingArea_1271.method380("Fps:" + this.applet.fps, c, i1, k);
            k += 15;
            Runtime runtime = Runtime.getRuntime();
            int j1 = (int)((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
            i1 = 0xffff00;
            if(j1 > 0x2000000 && Client.lowMem)
                i1 = 0xff0000;
            this.aTextDrawingArea_1271.method380("Mem:" + j1 + "k", c, 0xffff00, k);
            k += 15;
        }
        if(this.anInt1104 != 0)
        {
            int j = this.anInt1104 / 50;
            int l = j / 60;
            j %= 60;
            if(j < 10)
                this.aTextDrawingArea_1271.method385(0xffff00, "System update in: " + l + ":0" + j, 329, 4);
            else
                this.aTextDrawingArea_1271.method385(0xffff00, "System update in: " + l + ":" + j, 329, 4);
            Client.anInt849++;
            if(Client.anInt849 > 75)
            {
                Client.anInt849 = 0;
                this.stream.writeOpcode(148);
            }
        }
    }

    private void addIgnore(long l)
    {
        try
        {
            if(l == 0L)
                return;
            if(this.ignoreCount >= 100)
            {
                this.pushMessage("Your ignore list is full. Max of 100 hit", 0, "");
                return;
            }
            String s = TextClass.fixName(TextClass.nameForLong(l));
            for(int j = 0; j < this.ignoreCount; j++)
                if(this.ignoreListAsLongs[j] == l)
                {
                    this.pushMessage(s + " is already on your ignore list", 0, "");
                    return;
                }
            for(int k = 0; k < this.friendsCount; k++)
                if(this.friendsListAsLongs[k] == l)
                {
                    this.pushMessage("Please remove " + s + " from your friend list first", 0, "");
                    return;
                }

            this.ignoreListAsLongs[this.ignoreCount++] = l;
            this.needDrawTabArea = true;
            this.stream.writeOpcode(133);
            this.stream.writeLong(l);
            return;
        }
        catch(RuntimeException runtimeexception)
        {
            Utils.reporterror("45688, " + l + ", " + 4 + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    private void method114()
    {
        for(int i = -1; i < this.playerCount; i++)
        {
            int j;
            if(i == -1)
                j = this.myPlayerIndex;
            else
                j = this.playerIndices[i];
            Player player = this.playerArray[j];
            if(player != null)
                this.method96(player);
        }

    }

    private void method115()
    {
        if(this.loadingStage == 2)
        {
            for(SpawnedObject class30_sub1 = (SpawnedObject)this.aClass19_1179.getFront(); class30_sub1 != null; class30_sub1 = (SpawnedObject)this.aClass19_1179.getNext())
            {
                if(class30_sub1.anInt1294 > 0)
                    class30_sub1.anInt1294--;
                if(class30_sub1.anInt1294 == 0)
                {
                    if(class30_sub1.anInt1299 < 0 || ObjectManager.method178(class30_sub1.anInt1299, class30_sub1.anInt1301))
                    {
                        this.method142(class30_sub1.anInt1298, class30_sub1.anInt1295, class30_sub1.anInt1300, class30_sub1.anInt1301, class30_sub1.anInt1297, class30_sub1.anInt1296, class30_sub1.anInt1299);
                        class30_sub1.unlink();
                    }
                } else
                {
                    if(class30_sub1.anInt1302 > 0)
                        class30_sub1.anInt1302--;
                    if(class30_sub1.anInt1302 == 0 && class30_sub1.anInt1297 >= 1 && class30_sub1.anInt1298 >= 1 && class30_sub1.anInt1297 <= 102 && class30_sub1.anInt1298 <= 102 && (class30_sub1.anInt1291 < 0 || ObjectManager.method178(class30_sub1.anInt1291, class30_sub1.anInt1293)))
                    {
                        this.method142(class30_sub1.anInt1298, class30_sub1.anInt1295, class30_sub1.anInt1292, class30_sub1.anInt1293, class30_sub1.anInt1297, class30_sub1.anInt1296, class30_sub1.anInt1291);
                        class30_sub1.anInt1302 = -1;
                        if(class30_sub1.anInt1291 == class30_sub1.anInt1299 && class30_sub1.anInt1299 == -1)
                            class30_sub1.unlink();
                        else
                        if(class30_sub1.anInt1291 == class30_sub1.anInt1299 && class30_sub1.anInt1292 == class30_sub1.anInt1300 && class30_sub1.anInt1293 == class30_sub1.anInt1301)
                            class30_sub1.unlink();
                    }
                }
            }

        }
    }

    private void determineMenuSize()
    {
        int i = this.chatTextDrawingArea.getTextWidth("Choose Option");
        for(int j = 0; j < this.menuActionRow; j++)
        {
            int k = this.chatTextDrawingArea.getTextWidth(this.menuActionName[j]);
            if(k > i)
                i = k;
        }

        i += 8;
        int l = 15 * this.menuActionRow + 21;
        if(this.applet.saveClickX > 4 && this.applet.saveClickY > 4 && this.applet.saveClickX < 516 && this.applet.saveClickY < 338)
        {
            int i1 = this.applet.saveClickX - 4 - i / 2;
            if(i1 + i > 512)
                i1 = 512 - i;
            if(i1 < 0)
                i1 = 0;
            int l1 = this.applet.saveClickY - 4;
            if(l1 + l > 334)
                l1 = 334 - l;
            if(l1 < 0)
                l1 = 0;
            this.menuOpen = true;
            this.menuScreenArea = 0;
            this.menuOffsetX = i1;
            this.menuOffsetY = l1;
            this.menuWidth = i;
            this.anInt952 = 15 * this.menuActionRow + 22;
        }
        if(this.applet.saveClickX > 553 && this.applet.saveClickY > 205 && this.applet.saveClickX < 743 && this.applet.saveClickY < 466)
        {
            int j1 = this.applet.saveClickX - 553 - i / 2;
            if(j1 < 0)
                j1 = 0;
            else
            if(j1 + i > 190)
                j1 = 190 - i;
            int i2 = this.applet.saveClickY - 205;
            if(i2 < 0)
                i2 = 0;
            else
            if(i2 + l > 261)
                i2 = 261 - l;
            this.menuOpen = true;
            this.menuScreenArea = 1;
            this.menuOffsetX = j1;
            this.menuOffsetY = i2;
            this.menuWidth = i;
            this.anInt952 = 15 * this.menuActionRow + 22;
        }
        if(this.applet.saveClickX > 17 && this.applet.saveClickY > 357 && this.applet.saveClickX < 496 && this.applet.saveClickY < 453)
        {
            int k1 = this.applet.saveClickX - 17 - i / 2;
            if(k1 < 0)
                k1 = 0;
            else
            if(k1 + i > 479)
                k1 = 479 - i;
            int j2 = this.applet.saveClickY - 357;
            if(j2 < 0)
                j2 = 0;
            else
            if(j2 + l > 96)
                j2 = 96 - l;
            this.menuOpen = true;
            this.menuScreenArea = 2;
            this.menuOffsetX = k1;
            this.menuOffsetY = j2;
            this.menuWidth = i;
            this.anInt952 = 15 * this.menuActionRow + 22;
        }
    }

    private void method117(BitBuffer buffer)
    {
        buffer.enableBitAccess();
        int j = buffer.readBits(1);
        if(j == 0)
            return;
        int k = buffer.readBits(2);
        if(k == 0)
        {
            this.anIntArray894[this.anInt893++] = this.myPlayerIndex;
            return;
        }
        if(k == 1)
        {
            int l = buffer.readBits(3);
            Client.localPlayer.moveInDir(false, l);
            int k1 = buffer.readBits(1);
            if(k1 == 1)
                this.anIntArray894[this.anInt893++] = this.myPlayerIndex;
            return;
        }
        if(k == 2)
        {
            int i1 = buffer.readBits(3);
            Client.localPlayer.moveInDir(true, i1);
            int l1 = buffer.readBits(3);
            Client.localPlayer.moveInDir(true, l1);
            int j2 = buffer.readBits(1);
            if(j2 == 1)
                this.anIntArray894[this.anInt893++] = this.myPlayerIndex;
            return;
        }
        if(k == 3)
        {
            this.plane = buffer.readBits(2);
            int j1 = buffer.readBits(1);
            int i2 = buffer.readBits(1);
            if(i2 == 1)
                this.anIntArray894[this.anInt893++] = this.myPlayerIndex;
            int k2 = buffer.readBits(7);
            int l2 = buffer.readBits(7);
            Client.localPlayer.setPos(l2, k2, j1 == 1);
        }
    }

    private boolean method119(int i, int j)
    {
        boolean flag1 = false;
        RSInterface class9 = RSInterface.interfaceCache[j];
        for(int k = 0; k < class9.children.length; k++)
        {
            if(class9.children[k] == -1)
                break;
            RSInterface class9_1 = RSInterface.interfaceCache[class9.children[k]];
            if(class9_1.type == 1)
                flag1 |= this.method119(i, class9_1.id);
            if(class9_1.type == 6 && (class9_1.anInt257 != -1 || class9_1.anInt258 != -1))
            {
                boolean flag2 = this.interfaceIsSelected(class9_1);
                int l;
                if(flag2)
                    l = class9_1.anInt258;
                else
                    l = class9_1.anInt257;
                if(l != -1)
                {
                    Animation animation = Animation.animations[l];
                    for(class9_1.anInt208 += i; class9_1.anInt208 > animation.duration(class9_1.anInt246);)
                    {
                        class9_1.anInt208 -= animation.duration(class9_1.anInt246) + 1;
                        class9_1.anInt246++;
                        if(class9_1.anInt246 >= animation.frameCount)
                        {
                            class9_1.anInt246 -= animation.loopOffset;
                            if(class9_1.anInt246 < 0 || class9_1.anInt246 >= animation.frameCount)
                                class9_1.anInt246 = 0;
                        }
                        flag1 = true;
                    }

                }
            }
        }

        return flag1;
    }

    private int method120()
    {
        int j = 3;
        if(this.yCameraCurve < 310)
        {
            int k = this.xCameraPos >> 7;
            int l = this.yCameraPos >> 7;
            int i1 = Client.localPlayer.x >> 7;
            int j1 = Client.localPlayer.y >> 7;
            if((this.byteGroundArray[this.plane][k][l] & 4) != 0)
                j = this.plane;
            int k1;
            if(i1 > k)
                k1 = i1 - k;
            else
                k1 = k - i1;
            int l1;
            if(j1 > l)
                l1 = j1 - l;
            else
                l1 = l - j1;
            if(k1 > l1)
            {
                int i2 = (l1 * 0x10000) / k1;
                int k2 = 32768;
                while(k != i1) 
                {
                    if(k < i1)
                        k++;
                    else
                    if(k > i1)
                        k--;
                    if((this.byteGroundArray[this.plane][k][l] & 4) != 0)
                        j = this.plane;
                    k2 += i2;
                    if(k2 >= 0x10000)
                    {
                        k2 -= 0x10000;
                        if(l < j1)
                            l++;
                        else
                        if(l > j1)
                            l--;
                        if((this.byteGroundArray[this.plane][k][l] & 4) != 0)
                            j = this.plane;
                    }
                }
            } else
            {
                int j2 = (k1 * 0x10000) / l1;
                int l2 = 32768;
                while(l != j1) 
                {
                    if(l < j1)
                        l++;
                    else
                    if(l > j1)
                        l--;
                    if((this.byteGroundArray[this.plane][k][l] & 4) != 0)
                        j = this.plane;
                    l2 += j2;
                    if(l2 >= 0x10000)
                    {
                        l2 -= 0x10000;
                        if(k < i1)
                            k++;
                        else
                        if(k > i1)
                            k--;
                        if((this.byteGroundArray[this.plane][k][l] & 4) != 0)
                            j = this.plane;
                    }
                }
            }
        }
        if((this.byteGroundArray[this.plane][Client.localPlayer.x >> 7][Client.localPlayer.y >> 7] & 4) != 0)
            j = this.plane;
        return j;
    }

    private int method121()
    {
        int j = this.tileHeight(this.plane, this.yCameraPos, this.xCameraPos);
        if(j - this.zCameraPos < 800 && (this.byteGroundArray[this.plane][this.xCameraPos >> 7][this.yCameraPos >> 7] & 4) != 0)
            return this.plane;
        else
            return 3;
    }

    private void delIgnore(long l)
    {
        try
        {
            if(l == 0L)
                return;
            for(int j = 0; j < this.ignoreCount; j++)
                if(this.ignoreListAsLongs[j] == l)
                {
                    this.ignoreCount--;
                    this.needDrawTabArea = true;
                    System.arraycopy(this.ignoreListAsLongs, j + 1, this.ignoreListAsLongs, j, this.ignoreCount - j);

                    this.stream.writeOpcode(74);
                    this.stream.writeLong(l);
                    return;
                }

            return;
        }
        catch(RuntimeException runtimeexception)
        {
            Utils.reporterror("47229, " + 3 + ", " + l + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    private void adjustVolume(boolean flag, int i)
    {
        signlink.midivol = i;
        if(flag)
            signlink.midi = "voladjust";
    }

    private int extractInterfaceValues(RSInterface class9, int j)
    {
        if(class9.valueIndexArray == null || j >= class9.valueIndexArray.length)
            return -2;
        try
        {
            int ai[] = class9.valueIndexArray[j];
            int k = 0;
            int l = 0;
            int i1 = 0;
            do
            {
                int j1 = ai[l++];
                int k1 = 0;
                byte byte0 = 0;
                if(j1 == 0)
                    return k;
                if(j1 == 1)
                    k1 = this.currentStats[ai[l++]];
                if(j1 == 2)
                    k1 = this.maxStats[ai[l++]];
                if(j1 == 3)
                    k1 = this.currentExp[ai[l++]];
                if(j1 == 4)
                {
                    RSInterface class9_1 = RSInterface.interfaceCache[ai[l++]];
                    int k2 = ai[l++];
                    if(k2 >= 0 && k2 < ItemDef.totalItems && (!ItemDef.forID(k2).membersObject || Client.isMembers))
                    {
                        for(int j3 = 0; j3 < class9_1.inv.length; j3++)
                            if(class9_1.inv[j3] == k2 + 1)
                                k1 += class9_1.invStackSizes[j3];

                    }
                }
                if(j1 == 5)
                    k1 = this.variousSettings[ai[l++]];
                if(j1 == 6)
                    k1 = Client.anIntArray1019[this.maxStats[ai[l++]] - 1];
                if(j1 == 7)
                    k1 = (this.variousSettings[ai[l++]] * 100) / 46875;
                if(j1 == 8)
                    k1 = Client.localPlayer.combatLevel;
                if(j1 == 9)
                {
                    for(int l1 = 0; l1 < Skills.skillsCount; l1++)
                        if(Skills.skillEnabled[l1])
                            k1 += this.maxStats[l1];

                }
                if(j1 == 10)
                {
                    RSInterface class9_2 = RSInterface.interfaceCache[ai[l++]];
                    int l2 = ai[l++] + 1;
                    if(l2 >= 0 && l2 < ItemDef.totalItems && (!ItemDef.forID(l2).membersObject || Client.isMembers))
                    {
                        for(int k3 = 0; k3 < class9_2.inv.length; k3++)
                        {
                            if(class9_2.inv[k3] != l2)
                                continue;
                            k1 = 0x3b9ac9ff;
                            break;
                        }

                    }
                }
                if(j1 == 11)
                    k1 = this.energy;
                if(j1 == 12)
                    k1 = this.weight;
                if(j1 == 13)
                {
                    int i2 = this.variousSettings[ai[l++]];
                    int i3 = ai[l++];
                    k1 = (i2 & 1 << i3) == 0 ? 0 : 1;
                }
                if(j1 == 14)
                {
                    int j2 = ai[l++];

                    k1 = VariableBits.get(j2, this.variousSettings);
                }
                if(j1 == 15)
                    byte0 = 1;
                if(j1 == 16)
                    byte0 = 2;
                if(j1 == 17)
                    byte0 = 3;
                if(j1 == 18)
                    k1 = (Client.localPlayer.x >> 7) + this.baseX;
                if(j1 == 19)
                    k1 = (Client.localPlayer.y >> 7) + this.baseY;
                if(j1 == 20)
                    k1 = ai[l++];
                if(byte0 == 0)
                {
                    if(i1 == 0)
                        k += k1;
                    if(i1 == 1)
                        k -= k1;
                    if(i1 == 2 && k1 != 0)
                        k /= k1;
                    if(i1 == 3)
                        k *= k1;
                    i1 = 0;
                } else
                {
                    i1 = byte0;
                }
            } while(true);
        }
        catch(Exception _ex)
        {
            return -1;
        }
    }

    private void drawTooltip()
    {
        if(this.menuActionRow < 2 && this.itemSelected == 0 && this.spellSelected == 0)
            return;
        String s;
        if(this.itemSelected == 1 && this.menuActionRow < 2)
            s = "Use " + this.selectedItemName + " with...";
        else
        if(this.spellSelected == 1 && this.menuActionRow < 2)
            s = this.spellTooltip + "...";
        else
            s = this.menuActionName[this.menuActionRow - 1];
        if(this.menuActionRow > 2)
            s = s + "@whi@ / " + (this.menuActionRow - 2) + " more options";
        this.chatTextDrawingArea.method390(4, 0xffffff, s, Client.loopCycle / 1000, 15);
    }

    private static void drawMinimap(Client client, int minimapInt1, int minimapInt2, int minimapInt3)
    {
        if(client.anInt1021 == 2)
        {
            SpriteRenderer.drawSprite(Client.cacheSprite[44], Client.frameWidth - 181, 0);//map/compas
            SpriteRenderer.drawSprite(Client.cacheSprite[45], Client.frameWidth - 158, 7);//mapback
            
    /*        byte abyte0[] = mapBack.raster;
            int ai[] = DrawingArea.pixels;
            int k2 = abyte0.length;
            for(int i5 = 0; i5 < k2; i5++)
                if(abyte0[i5] == 0)
                    ai[i5] = 0;*/

           //SpriteRenderer.draw(compass, 33, minimapInt1, anIntArray1057, 256, anIntArray968, 25, 0, 0, 33, 25);
            
            SpriteRenderer.draw(client.compass, 33, minimapInt1, Minimap.compassClipLengths, 256, Minimap.compassClipStarts, (RSBase.frameMode == ScreenMode.FIXED ? 25 : 24), 4, (RSBase.frameMode == ScreenMode.FIXED ? 29 : Client.frameWidth - 176), 33, 25);
			if (client.menuOpen) {
				client.drawMenu(RSBase.frameMode == ScreenMode.FIXED ? 516 : 0, 0);
			}
			return;
        }
        int i = minimapInt1 + minimapInt2 & 0x7ff;
        int j = 48 + Client.localPlayer.x / 32;
        int l2 = 464 - Client.localPlayer.y / 32;
        SpriteRenderer.draw(client.minimapImage, 151, i, Minimap.minimapLineLengths, 256 + minimapInt3, Minimap.minimapLineStarts, l2, (RSBase.frameMode == ScreenMode.FIXED ? 9 : 7), (RSBase.frameMode == ScreenMode.FIXED ? 54 : Client.frameWidth - 158), 146, j);
		
        for(int j5 = 0; j5 < client.minimapFunctionCount; j5++)
        {
            int k = (client.minimapFunctionX[j5] * 4 + 2) - Client.localPlayer.x / 32;
            int i3 = (client.minimapFunctionY[j5] * 4 + 2) - Client.localPlayer.y / 32;
            Minimap.drawOntoMinimap(client.minimapFunctions[j5], k, i3, minimapInt1, minimapInt2, minimapInt3);
        }

        for(int k5 = 0; k5 < 104; k5++)
        {
            for(int l5 = 0; l5 < 104; l5++)
            {
                Deque class19 = client.levelObjects[client.plane][k5][l5];
                if(class19 != null)
                {
                    int l = (k5 * 4 + 2) - Client.localPlayer.x / 32;
                    int j3 = (l5 * 4 + 2) - Client.localPlayer.y / 32;
                    Minimap.drawOntoMinimap(client.mapDotItem, l, j3, minimapInt1, minimapInt2, minimapInt3);
                }
            }

        }

        for(int i6 = 0; i6 < client.npcCount; i6++)
        {
            NPC npc = client.npcArray[client.npcIndices[i6]];
            if(npc != null && npc.isVisible())
            {
                EntityDef entityDef = npc.desc;
                if(entityDef.childrenIDs != null)
                    entityDef = entityDef.method161();
                if(entityDef != null && entityDef.aBoolean87 && entityDef.aBoolean84)
                {
                    int i1 = npc.x / 32 - Client.localPlayer.x / 32;
                    int k3 = npc.y / 32 - Client.localPlayer.y / 32;
                    Minimap.drawOntoMinimap(client.mapDotNPC, i1, k3, minimapInt1, minimapInt2, minimapInt3);
                }
            }
        }

        for(int j6 = 0; j6 < client.playerCount; j6++)
        {
            Player player = client.playerArray[client.playerIndices[j6]];
            if(player != null && player.isVisible())
            {
                int j1 = player.x / 32 - Client.localPlayer.x / 32;
                int l3 = player.y / 32 - Client.localPlayer.y / 32;
                boolean flag1 = false;
                long l6 = TextClass.longForName(player.name);
                for(int k6 = 0; k6 < client.friendsCount; k6++)
                {
                    if(l6 != client.friendsListAsLongs[k6] || client.friendsNodeIDs[k6] == 0)
                        continue;
                    flag1 = true;
                    break;
                }

                boolean flag2 = false;
                if(Client.localPlayer.team != 0 && player.team != 0 && Client.localPlayer.team == player.team)
                    flag2 = true;
                if(flag1)
                    Minimap.drawOntoMinimap(client.mapDotFriend, j1, l3, minimapInt1, minimapInt2, minimapInt3);
                else
                if(flag2)
                    Minimap.drawOntoMinimap(client.mapDotTeam, j1, l3, minimapInt1, minimapInt2, minimapInt3);
                else
                    Minimap.drawOntoMinimap(client.mapDotPlayer, j1, l3, minimapInt1, minimapInt2, minimapInt3);
            }
        }

        if(client.anInt855 != 0 && Client.loopCycle % 20 < 10)
        {
            if(client.anInt855 == 1 && client.anInt1222 >= 0 && client.anInt1222 < client.npcArray.length)
            {
                NPC class30_sub2_sub4_sub1_sub1_1 = client.npcArray[client.anInt1222];
                if(class30_sub2_sub4_sub1_sub1_1 != null)
                {
                    int k1 = class30_sub2_sub4_sub1_sub1_1.x / 32 - Client.localPlayer.x / 32;
                    int i4 = class30_sub2_sub4_sub1_sub1_1.y / 32 - Client.localPlayer.y / 32;
                    Minimap.method81(client.mapMarker, i4, k1, minimapInt1, minimapInt2, minimapInt3);
                }
            }
            if(client.anInt855 == 2)
            {
                int l1 = ((client.anInt934 - client.baseX) * 4 + 2) - Client.localPlayer.x / 32;
                int j4 = ((client.anInt935 - client.baseY) * 4 + 2) - Client.localPlayer.y / 32;
                Minimap.method81(client.mapMarker, j4, l1, minimapInt1, minimapInt2, minimapInt3);
            }
            if(client.anInt855 == 10 && client.anInt933 >= 0 && client.anInt933 < client.playerArray.length)
            {
                Player class30_sub2_sub4_sub1_sub2_1 = client.playerArray[client.anInt933];
                if(class30_sub2_sub4_sub1_sub2_1 != null)
                {
                    int i2 = class30_sub2_sub4_sub1_sub2_1.x / 32 - Client.localPlayer.x / 32;
                    int k4 = class30_sub2_sub4_sub1_sub2_1.y / 32 - Client.localPlayer.y / 32;
                    Minimap.method81(client.mapMarker, k4, i2, minimapInt1, minimapInt2, minimapInt3);
                }
            }
        }
        if(client.destX != 0)
        {
            int j2 = (client.destX * 4 + 2) - Client.localPlayer.x / 32;
            int l4 = (client.destY * 4 + 2) - Client.localPlayer.y / 32;
            Minimap.drawOntoMinimap(client.mapFlag, j2, l4, minimapInt1, minimapInt2, minimapInt3);
        }
        //DrawingArea.drawPixels(3, 78, 97, 0xffffff, 3);
        //aRSImageProducer_1165.initDrawingArea();
        
		DrawingArea.drawPixels(3, (RSBase.frameMode == ScreenMode.FIXED ? 83 : 80), (RSBase.frameMode == ScreenMode.FIXED ? 127 : Client.frameWidth - 88), 0xffffff, 3);
		if (RSBase.frameMode == ScreenMode.FIXED) {
			SpriteRenderer.drawSprite(Client.cacheSprite[19], 0, 0);
		} else {
			SpriteRenderer.drawSprite(Client.cacheSprite[44], Client.frameWidth - 181, 0);
		}
		SpriteRenderer.draw(client.compass, 33, minimapInt1, Minimap.compassClipLengths, 256, Minimap.compassClipStarts, (RSBase.frameMode == ScreenMode.FIXED ? 25 : 24), 4, (RSBase.frameMode == ScreenMode.FIXED ? 29 : Client.frameWidth - 176), 33, 25);

		if (client.menuOpen) {
			client.drawMenu(RSBase.frameMode == ScreenMode.FIXED ? 516 : 0, 0);
		}
		if (RSBase.frameMode == ScreenMode.FIXED) {
			client.aRSImageProducer_1165.initDrawingArea();
		}
    }

    private void npcScreenPos(Entity entity, int i)
    {
        this.calcEntityScreenPos(entity.x, i, entity.y);

//aryan	entity.entScreenX = spriteDrawX; entity.entScreenY = spriteDrawY;
	    }

    private void calcEntityScreenPos(int i, int j, int l)
    {
        if(i < 128 || l < 128 || i > 13056 || l > 13056)
        {
            this.spriteDrawX = -1;
            this.spriteDrawY = -1;
            return;
        }
        int i1 = this.tileHeight(this.plane, l, i) - j;
        i -= this.xCameraPos;
        i1 -= this.zCameraPos;
        l -= this.yCameraPos;
        int j1 = Model.SINE[this.yCameraCurve];
        int k1 = Model.COSINE[this.yCameraCurve];
        int l1 = Model.SINE[this.xCameraCurve];
        int i2 = Model.COSINE[this.xCameraCurve];
        int j2 = l * l1 + i * i2 >> 16;
        l = l * i2 - i * l1 >> 16;
        i = j2;
        j2 = i1 * k1 - l * j1 >> 16;
        l = i1 * j1 + l * k1 >> 16;
        i1 = j2;
        if(l >= 50)
        {
            this.spriteDrawX = Texture.originViewX + (i << Constants.VIEW_DISTANCE) / l;
            this.spriteDrawY = Texture.originViewY + (i1 << Constants.VIEW_DISTANCE) / l;
        } else
        {
            this.spriteDrawX = -1;
            this.spriteDrawY = -1;
        }
    }

    private void buildSplitPrivateChatMenu()
    {
        if(this.splitPrivateChat == 0)
            return;
        int i = 0;
        if(this.anInt1104 != 0)
            i = 1;
        for(int j = 0; j < 100; j++)
            if(this.chatMessages[j] != null)
            {
                int k = this.chatTypes[j];
                String s = this.chatNames[j];
                boolean flag1 = false;
                if(s != null && s.startsWith("@cr1@"))
                {
                    s = s.substring(5);
                    boolean flag2 = true;
                }
                if(s != null && s.startsWith("@cr2@"))
                {
                    s = s.substring(5);
                    byte byte0 = 2;
                }
                if((k == 3 || k == 7) && (k == 7 || this.privateChatMode == 0 || this.privateChatMode == 1 && this.isFriendOrSelf(s)))
                {
                    int l = 329 - i * 13;
                    if(this.applet.mouseX > 4 && this.applet.mouseY - 4 > l - 10 && this.applet.mouseY - 4 <= l + 3)
                    {
                        int i1 = this.aTextDrawingArea_1271.getTextWidth("From:  " + s + this.chatMessages[j]) + 25;
                        if(i1 > 450)
                            i1 = 450;
                        if(this.applet.mouseX < 4 + i1)
                        {
                            if(this.myPrivilege >= 1)
                            {
                                this.menuActionName[this.menuActionRow] = "Report abuse @whi@" + s;
                                this.menuActionID[this.menuActionRow] = 2606;
                                this.menuActionRow++;
                            }
                            this.menuActionName[this.menuActionRow] = "Add ignore @whi@" + s;
                            this.menuActionID[this.menuActionRow] = 2042;
                            this.menuActionRow++;
                            this.menuActionName[this.menuActionRow] = "Add friend @whi@" + s;
                            this.menuActionID[this.menuActionRow] = 2337;
                            this.menuActionRow++;
                        }
                    }
                    if(++i >= 5)
                        return;
                }
                if((k == 5 || k == 6) && this.privateChatMode < 2 && ++i >= 5)
                    return;
            }

    }

    private void method130(int j, int k, int l, int i1, int j1, int k1,
                           int l1, int i2, int j2)
    {
        SpawnedObject class30_sub1 = null;
        for(SpawnedObject class30_sub1_1 = (SpawnedObject)this.aClass19_1179.getFront(); class30_sub1_1 != null; class30_sub1_1 = (SpawnedObject)this.aClass19_1179.getNext())
        {
            if(class30_sub1_1.anInt1295 != l1 || class30_sub1_1.anInt1297 != i2 || class30_sub1_1.anInt1298 != j1 || class30_sub1_1.anInt1296 != i1)
                continue;
            class30_sub1 = class30_sub1_1;
            break;
        }

        if(class30_sub1 == null)
        {
            class30_sub1 = new SpawnedObject();
            class30_sub1.anInt1295 = l1;
            class30_sub1.anInt1296 = i1;
            class30_sub1.anInt1297 = i2;
            class30_sub1.anInt1298 = j1;
            this.method89(class30_sub1);
            this.aClass19_1179.pushBack(class30_sub1);
        }
        class30_sub1.anInt1291 = k;
        class30_sub1.anInt1293 = k1;
        class30_sub1.anInt1292 = l;
        class30_sub1.anInt1302 = j2;
        class30_sub1.anInt1294 = j;
    }

    private boolean interfaceIsSelected(RSInterface class9)
    {
        if(class9.anIntArray245 == null)
            return false;
        for(int i = 0; i < class9.anIntArray245.length; i++)
        {
            int j = this.extractInterfaceValues(class9, i);
            int k = class9.anIntArray212[i];
            if(class9.anIntArray245[i] == 2)
            {
                if(j >= k)
                    return false;
            } else
            if(class9.anIntArray245[i] == 3)
            {
                if(j <= k)
                    return false;
            } else
            if(class9.anIntArray245[i] == 4)
            {
                if(j == k)
                    return false;
            } else
            if(j != k)
                return false;
        }

        return true;
    }

    private void method134(BitBuffer buffer)
    {
        int j = buffer.readBits(8);
        if(j < this.playerCount)
        {
            for(int k = j; k < this.playerCount; k++)
                this.anIntArray840[this.anInt839++] = this.playerIndices[k];

        }
        if(j > this.playerCount)
        {
            Utils.reporterror(this.myUsername + " Too many players");
            throw new RuntimeException("eek");
        }
        this.playerCount = 0;
        for(int l = 0; l < j; l++)
        {
            int i1 = this.playerIndices[l];
            Player player = this.playerArray[i1];
            int j1 = buffer.readBits(1);
            if(j1 == 0)
            {
                this.playerIndices[this.playerCount++] = i1;
                player.anInt1537 = Client.loopCycle;
            } else
            {
                int k1 = buffer.readBits(2);
                if(k1 == 0)
                {
                    this.playerIndices[this.playerCount++] = i1;
                    player.anInt1537 = Client.loopCycle;
                    this.anIntArray894[this.anInt893++] = i1;
                } else
                if(k1 == 1)
                {
                    this.playerIndices[this.playerCount++] = i1;
                    player.anInt1537 = Client.loopCycle;
                    int l1 = buffer.readBits(3);
                    player.moveInDir(false, l1);
                    int j2 = buffer.readBits(1);
                    if(j2 == 1)
                        this.anIntArray894[this.anInt893++] = i1;
                } else
                if(k1 == 2)
                {
                    this.playerIndices[this.playerCount++] = i1;
                    player.anInt1537 = Client.loopCycle;
                    int i2 = buffer.readBits(3);
                    player.moveInDir(true, i2);
                    int k2 = buffer.readBits(3);
                    player.moveInDir(true, k2);
                    int l2 = buffer.readBits(1);
                    if(l2 == 1)
                        this.anIntArray894[this.anInt893++] = i1;
                } else
                if(k1 == 3)
                    this.anIntArray840[this.anInt839++] = i1;
            }
        }
    }

    private void drawLoginScreen(boolean flag)
    {
        this.resetImageProducers();

     
        Client.aRSImageProducer_1109.initDrawingArea();
        DrawingArea.drawPixels(200, 0, 0, 0x5d5447, 360);
       // IndexedImageRenderer.draw(LoadingScreen.aBackground_966, 0, 0);
     //   System.out.println("w:" + LoadingScreen.aBackground_966.width + "h:"+LoadingScreen.aBackground_966.height);
        int c = 360;//360
        int c1 = 200;
            int j = c1 / 2 - 40;
            if(this.loginMessage1.length() > 0)
            {
                this.chatTextDrawingArea.method382(0xffff00, c / 2, this.loginMessage1, j - 15, true);
                this.chatTextDrawingArea.method382(0xffff00, c / 2, this.loginMessage2, j, true);
                j += 30;
            } else
            {
                this.chatTextDrawingArea.method382(0xffff00, c / 2, this.loginMessage2, j - 7, true);
                j += 30;
            }
            this.chatTextDrawingArea.method389(true, c / 2 - 90, 0xffffff, "Username: " + this.myUsername + ((this.loginScreenCursorPos == 0) & (Client.loopCycle % 40 < 20) ? "@yel@|" : ""), j);
            j += 15;
            this.chatTextDrawingArea.method389(true, c / 2 - 88, 0xffffff, "Password: " + TextClass.passwordAsterisks(this.myPassword) + ((this.loginScreenCursorPos == 1) & (Client.loopCycle % 40 < 20) ? "@yel@|" : ""), j);
            j += 15;
            
            int i1 = c / 2;//this was -80, so add +80 to button click
            int l1 = c1 / 2 + 50;
            if(!flag)
            {

                //IndexedImageRenderer.draw(LoadingScreen.aBackground_967, i1 - 73, l1 - 20);
            
                DrawingArea.drawPixels(41, l1 - 20, i1 - 73, 0x4d4233, 147);
                this.chatTextDrawingArea.method382(0xffffff, i1, "Login", l1 + 5, true);
            }
            this.aTextDrawingArea_1271.method389(true,15,0xFFFF00,"MX: " + this.applet.mouseX + " , MY: " + this.applet.mouseY + " , SX: " +Client.frameWidth  + " , SY: " +Client.frameHeight  , 15);
            this.aTextDrawingArea_1271.method389(true,15,0xFFFF00,"X: " + (i1 - 73 + (Client.frameWidth - 361)/2) + " , Y: " + (l1 - 20 + (Client.frameHeight - 161)/2) + " , W: " +147  + " , H: " +41  , 35);
            Client.aRSImageProducer_1109.drawGraphics(/*171*/(Client.frameHeight - 161)/2, this.applet.graphics, /*202*/(Client.frameWidth - 361)/2);
        if(this.welcomeScreenRaised)
        {
            Graphics g = this.applet.getGameComponent().getGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, Client.frameWidth, Client.frameHeight);
            this.welcomeScreenRaised = false;
        }
        
    }

	@Override
	public void raiseWelcomeScreen()
    {
        this.welcomeScreenRaised = true;
    }

    private void method137(BitBuffer buffer, int j)
    {
        if(j == 84)
        {
            int k = buffer.readUByte();
            int j3 = this.anInt1268 + (k >> 4 & 7);
            int i6 = this.anInt1269 + (k & 7);
            int l8 = buffer.readUShort();
            int k11 = buffer.readUShort();
            int l13 = buffer.readUShort();
            if(j3 >= 0 && i6 >= 0 && j3 < 104 && i6 < 104)
            {
                Deque class19_1 = this.levelObjects[this.plane][j3][i6];
                if(class19_1 != null)
                {
                    for(Item class30_sub2_sub4_sub2_3 = (Item)class19_1.getFront(); class30_sub2_sub4_sub2_3 != null; class30_sub2_sub4_sub2_3 = (Item)class19_1.getNext())
                    {
                        if(class30_sub2_sub4_sub2_3.id != (l8 & 0x7fff) || class30_sub2_sub4_sub2_3.amount != k11)
                            continue;
                        class30_sub2_sub4_sub2_3.amount = l13;
                        break;
                    }

                    this.spawnGroundItem(j3, i6);
                }
            }
            return;
        }
        if(j == 105)
        {
            int l = buffer.readUByte();
            int k3 = this.anInt1268 + (l >> 4 & 7);
            int j6 = this.anInt1269 + (l & 7);
            int i9 = buffer.readUShort();
            int l11 = buffer.readUByte();
            int i14 = l11 >> 4 & 0xf;
            int i16 = l11 & 7;
            if(Client.localPlayer.smallX[0] >= k3 - i14 && Client.localPlayer.smallX[0] <= k3 + i14 && Client.localPlayer.smallY[0] >= j6 - i14 && Client.localPlayer.smallY[0] <= j6 + i14 && this.aBoolean848 && !Client.lowMem && this.anInt1062 < 50)
            {
                this.anIntArray1207[this.anInt1062] = i9;
                this.anIntArray1241[this.anInt1062] = i16;
                this.anIntArray1250[this.anInt1062] = Sounds.anIntArray326[i9];
                this.anInt1062++;
            }
        }
        if(j == 215)
        {
            int i1 = buffer.readUShortA();
            int l3 = buffer.readUByteS();
            int x = this.anInt1268 + (l3 >> 4 & 7);
            int y = this.anInt1269 + (l3 & 7);
            int i12 = buffer.readUShortA();
            int j14 = buffer.readUShort();
            if(x >= 0 && y >= 0 && x < 104 && y < 104 && i12 != this.localPlayerIndex)
            {
                Item class30_sub2_sub4_sub2_2 = new Item();
                class30_sub2_sub4_sub2_2.id = i1;
                class30_sub2_sub4_sub2_2.amount = j14;
                if(this.levelObjects[this.plane][x][y] == null)
                    this.levelObjects[this.plane][x][y] = new Deque();
                this.levelObjects[this.plane][x][y].pushBack(class30_sub2_sub4_sub2_2);
                this.spawnGroundItem(x, y);
            }
            return;
        }
        if(j == 156)
        {
            int j1 = buffer.readUByteA();
            int i4 = this.anInt1268 + (j1 >> 4 & 7);
            int l6 = this.anInt1269 + (j1 & 7);
            int k9 = buffer.readUShort();
            if(i4 >= 0 && l6 >= 0 && i4 < 104 && l6 < 104)
            {
                Deque class19 = this.levelObjects[this.plane][i4][l6];
                if(class19 != null)
                {
                    for(Item item = (Item)class19.getFront(); item != null; item = (Item)class19.getNext())
                    {
                        if(item.id != (k9 & 0x7fff))
                            continue;
                        item.unlink();
                        break;
                    }

                    if(class19.getFront() == null)
                        this.levelObjects[this.plane][i4][l6] = null;
                    this.spawnGroundItem(i4, l6);
                }
            }
            return;
        }
        if(j == 160)
        {
            int k1 = buffer.readUByteS();
            int j4 = this.anInt1268 + (k1 >> 4 & 7);
            int i7 = this.anInt1269 + (k1 & 7);
            int l9 = buffer.readUByteS();
            int j12 = l9 >> 2;
            int k14 = l9 & 3;
            int j16 = this.anIntArray1177[j12];
            int j17 = buffer.readUShortA();
            if(j4 >= 0 && i7 >= 0 && j4 < 103 && i7 < 103)
            {
                int j18 = this.intGroundArray[this.plane][j4][i7];
                int i19 = this.intGroundArray[this.plane][j4 + 1][i7];
                int l19 = this.intGroundArray[this.plane][j4 + 1][i7 + 1];
                int k20 = this.intGroundArray[this.plane][j4][i7 + 1];
                if(j16 == 0)
                {
                    Object1 class10 = this.worldController.method296(this.plane, j4, i7);
                    if(class10 != null)
                    {
                        int k21 = class10.uid >> 14 & 0x7fff;
                        if(j12 == 2)
                        {
                            class10.aClass30_Sub2_Sub4_278 = new RenderableObject(k21, 4 + k14, 2, i19, l19, j18, k20, j17, false);
                            class10.aClass30_Sub2_Sub4_279 = new RenderableObject(k21, k14 + 1 & 3, 2, i19, l19, j18, k20, j17, false);
                        } else
                        {
                            class10.aClass30_Sub2_Sub4_278 = new RenderableObject(k21, k14, j12, i19, l19, j18, k20, j17, false);
                        }
                    }
                }
                if(j16 == 1)
                {
                    Object2 class26 = this.worldController.method297(j4, i7, this.plane);
                    if(class26 != null)
                        class26.aClass30_Sub2_Sub4_504 = new RenderableObject(class26.uid >> 14 & 0x7fff, 0, 4, i19, l19, j18, k20, j17, false);
                }
                if(j16 == 2)
                {
                    GameObject class28 = this.worldController.method298(j4, i7, this.plane);
                    if(j12 == 11)
                        j12 = 10;
                    if(class28 != null)
                        class28.renderable = new RenderableObject(class28.key >> 14 & 0x7fff, k14, j12, i19, l19, j18, k20, j17, false);
                }
                if(j16 == 3)
                {
                    Object3 class49 = this.worldController.method299(i7, j4, this.plane);
                    if(class49 != null)
                        class49.aClass30_Sub2_Sub4_814 = new RenderableObject(class49.uid >> 14 & 0x7fff, k14, 22, i19, l19, j18, k20, j17, false);
                }
            }
            return;
        }
        if(j == 147)
        {
            int l1 = buffer.readUByteS();
            int k4 = this.anInt1268 + (l1 >> 4 & 7);
            int j7 = this.anInt1269 + (l1 & 7);
            int i10 = buffer.readUShort();
            byte byte0 = buffer.readByteS();
            int l14 = buffer.readLEUShort();
            byte byte1 = buffer.readNegByte();
            int k17 = buffer.readUShort();
            int k18 = buffer.readUByteS();
            int j19 = k18 >> 2;
            int i20 = k18 & 3;
            int l20 = this.anIntArray1177[j19];
            byte byte2 = buffer.readByte();
            int l21 = buffer.readUShort();
            byte byte3 = buffer.readNegByte();
            Player player;
            if(i10 == this.localPlayerIndex)
                player = Client.localPlayer;
            else
                player = this.playerArray[i10];
            if(player != null)
            {
                ObjectDef class46 = ObjectDef.forID(l21);
                int i22 = this.intGroundArray[this.plane][k4][j7];
                int j22 = this.intGroundArray[this.plane][k4 + 1][j7];
                int k22 = this.intGroundArray[this.plane][k4 + 1][j7 + 1];
                int l22 = this.intGroundArray[this.plane][k4][j7 + 1];
                Model model = class46.method578(j19, i20, i22, j22, k22, l22, -1);
                if(model != null)
                {
                    this.method130(k17 + 1, -1, 0, l20, j7, 0, this.plane, k4, l14 + 1);
                    player.anInt1707 = l14 + Client.loopCycle;
                    player.anInt1708 = k17 + Client.loopCycle;
                    player.aModel_1714 = model;
                    int i23 = class46.anInt744;
                    int j23 = class46.anInt761;
                    if(i20 == 1 || i20 == 3)
                    {
                        i23 = class46.anInt761;
                        j23 = class46.anInt744;
                    }
                    player.anInt1711 = k4 * 128 + i23 * 64;
                    player.anInt1713 = j7 * 128 + j23 * 64;
                    player.anInt1712 = this.tileHeight(this.plane, player.anInt1713, player.anInt1711);
                    if(byte2 > byte0)
                    {
                        byte byte4 = byte2;
                        byte2 = byte0;
                        byte0 = byte4;
                    }
                    if(byte3 > byte1)
                    {
                        byte byte5 = byte3;
                        byte3 = byte1;
                        byte1 = byte5;
                    }
                    player.anInt1719 = k4 + byte2;
                    player.anInt1721 = k4 + byte0;
                    player.anInt1720 = j7 + byte3;
                    player.anInt1722 = j7 + byte1;
                }
            }
        }
        if(j == 151)
        {
            int i2 = buffer.readUByteA();
            int l4 = this.anInt1268 + (i2 >> 4 & 7);
            int k7 = this.anInt1269 + (i2 & 7);
            int j10 = buffer.readLEUShort();
            int k12 = buffer.readUByteS();
            int i15 = k12 >> 2;
            int k16 = k12 & 3;
            int l17 = this.anIntArray1177[i15];
            if(l4 >= 0 && k7 >= 0 && l4 < 104 && k7 < 104)
                this.method130(-1, j10, k16, l17, k7, i15, this.plane, l4, 0);
            return;
        }
        if(j == 4)
        {
            int j2 = buffer.readUByte();
            int i5 = this.anInt1268 + (j2 >> 4 & 7);
            int l7 = this.anInt1269 + (j2 & 7);
            int k10 = buffer.readUShort();
            int l12 = buffer.readUByte();
            int j15 = buffer.readUShort();
            if(i5 >= 0 && l7 >= 0 && i5 < 104 && l7 < 104)
            {
                i5 = i5 * 128 + 64;
                l7 = l7 * 128 + 64;
                AnimableObject class30_sub2_sub4_sub3 = new AnimableObject(this.plane, Client.loopCycle, j15, k10, this.tileHeight(this.plane, l7, i5) - l12, l7, i5);
                this.aClass19_1056.pushBack(class30_sub2_sub4_sub3);
            }
            return;
        }
        if(j == 44)
        {
            int k2 = buffer.readLEUShortA();
            int j5 = buffer.readUShort();
            int i8 = buffer.readUByte();
            int l10 = this.anInt1268 + (i8 >> 4 & 7);
            int i13 = this.anInt1269 + (i8 & 7);
            if(l10 >= 0 && i13 >= 0 && l10 < 104 && i13 < 104)
            {
                Item class30_sub2_sub4_sub2_1 = new Item();
                class30_sub2_sub4_sub2_1.id = k2;
                class30_sub2_sub4_sub2_1.amount = j5;
                if(this.levelObjects[this.plane][l10][i13] == null)
                    this.levelObjects[this.plane][l10][i13] = new Deque();
                this.levelObjects[this.plane][l10][i13].pushBack(class30_sub2_sub4_sub2_1);
                this.spawnGroundItem(l10, i13);
            }
            return;
        }
        if(j == 101)
        {
            int l2 = buffer.readNegUByte();
            int k5 = l2 >> 2;
            int j8 = l2 & 3;
            int i11 = this.anIntArray1177[k5];
            int j13 = buffer.readUByte();
            int k15 = this.anInt1268 + (j13 >> 4 & 7);
            int l16 = this.anInt1269 + (j13 & 7);
            if(k15 >= 0 && l16 >= 0 && k15 < 104 && l16 < 104)
                this.method130(-1, -1, j8, i11, l16, k5, this.plane, k15, 0);
            return;
        }
        if(j == 117)
        {
            int i3 = buffer.readUByte();
            int l5 = this.anInt1268 + (i3 >> 4 & 7);
            int k8 = this.anInt1269 + (i3 & 7);
            int j11 = l5 + buffer.readByte();
            int k13 = k8 + buffer.readByte();
            int l15 = buffer.readShort();
            int i17 = buffer.readUShort();
            int i18 = buffer.readUByte() * 4;
            int l18 = buffer.readUByte() * 4;
            int k19 = buffer.readUShort();
            int j20 = buffer.readUShort();
            int i21 = buffer.readUByte();
            int j21 = buffer.readUByte();
            if(l5 >= 0 && k8 >= 0 && l5 < 104 && k8 < 104 && j11 >= 0 && k13 >= 0 && j11 < 104 && k13 < 104 && i17 != 65535)
            {
                l5 = l5 * 128 + 64;
                k8 = k8 * 128 + 64;
                j11 = j11 * 128 + 64;
                k13 = k13 * 128 + 64;
                Projectile class30_sub2_sub4_sub4 = new Projectile(i21, l18, k19 + Client.loopCycle, j20 + Client.loopCycle, j21, this.plane, this.tileHeight(this.plane, k8, l5) - i18, k8, l5, l15, i17);
                class30_sub2_sub4_sub4.target(k19 + Client.loopCycle, k13, this.tileHeight(this.plane, k13, j11) - l18, j11);
                this.projectiles.pushBack(class30_sub2_sub4_sub4);
            }
        }
    }

    private static void setLowMem()
    {
        WorldController.lowMem = true;
        Texture.lowMem = true;
        Client.lowMem = true;
        ObjectManager.lowMem = true;
        ObjectDef.lowMem = true;
    }

    private static void setHighMem()
    {
        WorldController.lowMem = false;
        Texture.lowMem = false;
        Client.lowMem = false;
        ObjectManager.lowMem = false;
        ObjectDef.lowMem = false;
    }
    
    private void method139(BitBuffer buffer)
    {
        buffer.enableBitAccess();
        int k = buffer.readBits(8);
        if(k < this.npcCount)
        {
            for(int l = k; l < this.npcCount; l++)
                this.anIntArray840[this.anInt839++] = this.npcIndices[l];

        }
        if(k > this.npcCount)
        {
            Utils.reporterror(this.myUsername + " Too many npcs");
            throw new RuntimeException("eek");
        }
        this.npcCount = 0;
        for(int i1 = 0; i1 < k; i1++)
        {
            int j1 = this.npcIndices[i1];
            NPC npc = this.npcArray[j1];
            int k1 = buffer.readBits(1);
            if(k1 == 0)
            {
                this.npcIndices[this.npcCount++] = j1;
                npc.anInt1537 = Client.loopCycle;
            } else
            {
                int l1 = buffer.readBits(2);
                if(l1 == 0)
                {
                    this.npcIndices[this.npcCount++] = j1;
                    npc.anInt1537 = Client.loopCycle;
                    this.anIntArray894[this.anInt893++] = j1;
                } else
                if(l1 == 1)
                {
                    this.npcIndices[this.npcCount++] = j1;
                    npc.anInt1537 = Client.loopCycle;
                    int i2 = buffer.readBits(3);
                    npc.moveInDir(false, i2);
                    int k2 = buffer.readBits(1);
                    if(k2 == 1)
                        this.anIntArray894[this.anInt893++] = j1;
                } else
                if(l1 == 2)
                {
                    this.npcIndices[this.npcCount++] = j1;
                    npc.anInt1537 = Client.loopCycle;
                    int j2 = buffer.readBits(3);
                    npc.moveInDir(true, j2);
                    int l2 = buffer.readBits(3);
                    npc.moveInDir(true, l2);
                    int i3 = buffer.readBits(1);
                    if(i3 == 1)
                        this.anIntArray894[this.anInt893++] = j1;
                } else
                if(l1 == 3)
                    this.anIntArray840[this.anInt839++] = j1;
            }
        }

    }
    
    private void processLoginScreenInput(int click_x, int click_y, int click_type) {
    	this.refreshFrameSize(false);   	
    	
    	/* select password and username fields */
    	int j = Client.frameHeight / 2 - 40;
    	j += 30;
    	j += 25;
    	if (click_type == 1 && click_y >= j - 15 && click_y < j)
    		this.loginScreenCursorPos = 0;
     	
    	j += 15;
    	if (click_type == 1 && click_y >= j - 15 && click_y < j)
    		this.loginScreenCursorPos = 1;
    	j += 15;
    	
    	int minX = (Client.frameWidth / 2) - 75;
    	int minY = (Client.frameHeight / 2) + 49;
    	if (this.applet.clickMode3 == 1 && click_x >= minX && click_x <= minX + 150
    			&& click_y >= minY && click_y <= minY + 40) {
    		this.loginFailures = 0;
    		this.login(this.myUsername, this.myPassword, false);
    		if (this.loggedIn)
    			return;
    	}
    	do {
    		int l1 = this.applet.readChar(-796);
    		if (l1 == -1)
    			break;
    		boolean flag1 = false;
    		for (int i2 = 0; i2 < Client.validUserPassChars.length(); i2++) {
    			if (l1 != Client.validUserPassChars.charAt(i2))
    				continue;
    			flag1 = true;
    			break;
    		}

    		if (this.loginScreenCursorPos == 0) {
    			if (l1 == 8 && this.myUsername.length() > 0)
    				this.myUsername = this.myUsername.substring(0, this.myUsername.length() - 1);
    			if (l1 == 9 || l1 == 10 || l1 == 13)
    				this.loginScreenCursorPos = 1;
    			if (flag1)
    				this.myUsername += (char) l1;
    			if (this.myUsername.length() > 12)
    				this.myUsername = this.myUsername.substring(0, 12);
    		} else if (this.loginScreenCursorPos == 1) {
    			if (l1 == 8 && this.myPassword.length() > 0)
    				this.myPassword = this.myPassword.substring(0, this.myPassword.length() - 1);
    			if (l1 == 9 || l1 == 10 || l1 == 13) {
    				this.loginScreenCursorPos = 0;
    	    		this.loginFailures = 0;
    	    		this.login(this.myUsername, this.myPassword, false);
    	    		if (this.loggedIn)
    	    			return;
    			}
    			if (flag1)
    				this.myPassword += (char) l1;
    			if (this.myPassword.length() > 20)
    				this.myPassword = this.myPassword.substring(0, 20);
    		}
    	} while (true);
    	return;
    }

    private void method142(int i, int j, int k, int l, int i1, int j1, int k1
    )
    {
        if(i1 >= 1 && i >= 1 && i1 <= 102 && i <= 102)
        {
            if(Client.lowMem && j != this.plane)
                return;
            int i2 = 0;
            if(j1 == 0)
                i2 = this.worldController.method300(j, i1, i);
            if(j1 == 1)
                i2 = this.worldController.method301(j, i1, i);
            if(j1 == 2)
                i2 = this.worldController.method302(j, i1, i);
            if(j1 == 3)
                i2 = this.worldController.method303(j, i1, i);
            if(i2 != 0)
            {
                int i3 = this.worldController.method304(j, i1, i, i2);
                int j2 = i2 >> 14 & 0x7fff;
                int k2 = i3 & 0x1f;
                int l2 = i3 >> 6;
                if(j1 == 0)
                {
                    this.worldController.method291(i1, j, i, (byte)-119);
                    ObjectDef class46 = ObjectDef.forID(j2);
                    if(class46.aBoolean767)
                        this.aClass11Array1230[j].removeWall(i1, i, l2, k2, class46.aBoolean757);
                }
                if(j1 == 1)
                    this.worldController.method292(i, j, i1);
                if(j1 == 2)
                {
                    this.worldController.method293(j, i1, i);
                    ObjectDef class46_1 = ObjectDef.forID(j2);
                    if(i1 + class46_1.anInt744 > 103 || i + class46_1.anInt744 > 103 || i1 + class46_1.anInt761 > 103 || i + class46_1.anInt761 > 103)
                        return;
                    if(class46_1.aBoolean767)
                        this.aClass11Array1230[j].removeLoc(i1, i, class46_1.anInt744, class46_1.anInt761, l2, class46_1.aBoolean757);
                }
                if(j1 == 3)
                {
                    this.worldController.method294(j, i, i1);
                    ObjectDef class46_2 = ObjectDef.forID(j2);
                    if(class46_2.aBoolean767 && class46_2.hasActions)
                        this.aClass11Array1230[j].removeFloorDecoration(i1, i);
                }
            }
            if(k1 >= 0)
            {
                int j3 = j;
                if(j3 < 3 && (this.byteGroundArray[1][i1][i] & 2) == 2)
                    j3++;
                ObjectManager.method188(this.worldController, k, i, l, j3, this.aClass11Array1230[j], this.intGroundArray, i1, k1, j);
            }
        }
    }

    private void updatePlayers(int i, BitBuffer buffer)
    {
        this.anInt839 = 0;
        this.anInt893 = 0;
        this.method117(buffer);
        this.method134(buffer);
        this.method91(buffer, i);
        this.method49(buffer);
        for(int k = 0; k < this.anInt839; k++)
        {
            int l = this.anIntArray840[k];
            if(this.playerArray[l].anInt1537 != Client.loopCycle)
                this.playerArray[l] = null;
        }

        if(buffer.position != i)
        {
            Utils.reporterror("Error packet size mismatch in getplayer pos:" + buffer.position + " psize:" + i);
            throw new RuntimeException("eek");
        }
        for(int i1 = 0; i1 < this.playerCount; i1++)
            if(this.playerArray[this.playerIndices[i1]] == null)
            {
                Utils.reporterror(this.myUsername + " null entry in pl list - pos:" + i1 + " size:" + this.playerCount);
                throw new RuntimeException("eek");
            }

    }

    private void setCameraPos(int j, int k, int l, int i1, int j1, int k1)
    {
        int l1 = 2048 - k & 0x7ff;
        int i2 = 2048 - j1 & 0x7ff;
        int j2 = 0;
        int k2 = 0;
        int l2 = j;
        if(l1 != 0)
        {
            int i3 = Model.SINE[l1];
            int k3 = Model.COSINE[l1];
            int i4 = k2 * k3 - l2 * i3 >> 16;
            l2 = k2 * i3 + l2 * k3 >> 16;
            k2 = i4;
        }
        if(i2 != 0)
        {
/* xxx            if(cameratoggle){
            	if(zoom == 0)
                zoom = k2;
              if(lftrit == 0)
                lftrit = j2;
              if(fwdbwd == 0)
                fwdbwd = l2;
              k2 = zoom;
              j2 = lftrit;
              l2 = fwdbwd;
            }
*/
            int j3 = Model.SINE[i2];
            int l3 = Model.COSINE[i2];
            int j4 = l2 * j3 + j2 * l3 >> 16;
            l2 = l2 * l3 - j2 * j3 >> 16;
            j2 = j4;
        }
        this.xCameraPos = l - j2;
        this.zCameraPos = i1 - k2;
        this.yCameraPos = k1 - l2;
        this.yCameraCurve = k;
        this.xCameraCurve = j1;
    }

    private boolean parsePacket()
    {
        if(this.socketStream == null)
            return false;
        try
        {
            int i = this.socketStream.available();
            if(i == 0)
                return false;
            if(this.pktType == -1)
            {
                this.socketStream.flushInputStream(this.inBuffer.payload, 1);
                this.pktType = this.inBuffer.payload[0] & 0xff;
                if(this.encryption != null)
                    this.pktType = this.pktType - this.encryption.getNextKey() & 0xff;
                this.pktSize = SizeConstants.packetSizes[this.pktType];
                i--;
            }
            if(this.pktSize == -1)
                if(i > 0)
                {
                    this.socketStream.flushInputStream(this.inBuffer.payload, 1);
                    this.pktSize = this.inBuffer.payload[0] & 0xff;
                    i--;
                } else
                {
                    return false;
                }
            if(this.pktSize == -2)
                if(i > 1)
                {
                    this.socketStream.flushInputStream(this.inBuffer.payload, 2);
                    this.inBuffer.position = 0;
                    this.pktSize = this.inBuffer.readUShort();
                    i -= 2;
                } else
                {
                    return false;
                }
            if(i < this.pktSize)
                return false;
            this.inBuffer.position = 0;
            this.socketStream.flushInputStream(this.inBuffer.payload, this.pktSize);
            this.anInt1009 = 0;
            this.anInt843 = this.anInt842;
            this.anInt842 = this.anInt841;
            this.anInt841 = this.pktType;
            if(this.pktType == 81)
            {
                this.updatePlayers(this.pktSize, this.inBuffer);
                this.aBoolean1080 = false;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 176)
            {
                this.daysSinceRecovChange = this.inBuffer.readNegUByte();
                this.unreadMessages = this.inBuffer.readUShortA();
                this.membersInt = this.inBuffer.readUByte();
                this.anInt1193 = this.inBuffer.readIMEInt();
                this.daysSinceLastLogin = this.inBuffer.readUShort();
                if(this.anInt1193 != 0 && this.openInterfaceID == -1)
                {
                    signlink.dnslookup(TextClass.method586(this.anInt1193));
                    this.clearTopInterfaces();
                    char c = '\u028A';
                    if(this.daysSinceRecovChange != 201 || this.membersInt == 1)
                        c = '\u028F';
                    this.reportAbuseInput = "";
                    this.canMute = false;
                    for(int k9 = 0; k9 < RSInterface.interfaceCache.length; k9++)
                    {
                        if(RSInterface.interfaceCache[k9] == null || RSInterface.interfaceCache[k9].anInt214 != c)
                            continue;
                        this.openInterfaceID = RSInterface.interfaceCache[k9].parentID;
                        break;
                    }

                }
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 64)
            {
                this.anInt1268 = this.inBuffer.readNegUByte();
                this.anInt1269 = this.inBuffer.readUByteS();
                for(int j = this.anInt1268; j < this.anInt1268 + 8; j++)
                {
                    for(int l9 = this.anInt1269; l9 < this.anInt1269 + 8; l9++)
                        if(this.levelObjects[this.plane][j][l9] != null)
                        {
                            this.levelObjects[this.plane][j][l9] = null;
                            this.spawnGroundItem(j, l9);
                        }

                }

                for(SpawnedObject class30_sub1 = (SpawnedObject)this.aClass19_1179.getFront(); class30_sub1 != null; class30_sub1 = (SpawnedObject)this.aClass19_1179.getNext())
                    if(class30_sub1.anInt1297 >= this.anInt1268 && class30_sub1.anInt1297 < this.anInt1268 + 8 && class30_sub1.anInt1298 >= this.anInt1269 && class30_sub1.anInt1298 < this.anInt1269 + 8 && class30_sub1.anInt1295 == this.plane)
                        class30_sub1.anInt1294 = 0;

                this.pktType = -1;
                return true;
            }
            if(this.pktType == 185)
            {
                int k = this.inBuffer.readLEUShortA();
                RSInterface.interfaceCache[k].anInt233 = 3;
                if(Client.localPlayer.desc == null)
                    RSInterface.interfaceCache[k].mediaID = (Client.localPlayer.anIntArray1700[0] << 25) + (Client.localPlayer.anIntArray1700[4] << 20) + (Client.localPlayer.equipment[0] << 15) + (Client.localPlayer.equipment[8] << 10) + (Client.localPlayer.equipment[11] << 5) + Client.localPlayer.equipment[1];
                else
                    RSInterface.interfaceCache[k].mediaID = (int)(0x12345678L + Client.localPlayer.desc.type);
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 107)
            {
                this.aBoolean1160 = false;
                for(int l = 0; l < 5; l++)
                    this.aBooleanArray876[l] = false;

                this.pktType = -1;
                return true;
            }
            if(this.pktType == 72)
            {
                int i1 = this.inBuffer.readLEUShort();
                RSInterface class9 = RSInterface.interfaceCache[i1];
                for(int k15 = 0; k15 < class9.inv.length; k15++)
                {
                    class9.inv[k15] = -1;
                    class9.inv[k15] = 0;
                }

                this.pktType = -1;
                return true;
            }
            if(this.pktType == 214)
            {
                this.ignoreCount = this.pktSize / 8;
                for(int j1 = 0; j1 < this.ignoreCount; j1++)
                    this.ignoreListAsLongs[j1] = this.inBuffer.readLong();

                this.pktType = -1;
                return true;
            }
            if(this.pktType == 166)
            {
                this.aBoolean1160 = true;
                this.anInt1098 = this.inBuffer.readUByte();
                this.anInt1099 = this.inBuffer.readUByte();
                this.anInt1100 = this.inBuffer.readUShort();
                this.anInt1101 = this.inBuffer.readUByte();
                this.anInt1102 = this.inBuffer.readUByte();
                if(this.anInt1102 >= 100)
                {
                    this.xCameraPos = this.anInt1098 * 128 + 64;
                    this.yCameraPos = this.anInt1099 * 128 + 64;
                    this.zCameraPos = this.tileHeight(this.plane, this.yCameraPos, this.xCameraPos) - this.anInt1100;
                }
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 134)
            {
                this.needDrawTabArea = true;
                int k1 = this.inBuffer.readUByte();
                int i10 = this.inBuffer.readMEInt();
                int l15 = this.inBuffer.readUByte();
                this.currentExp[k1] = i10;
                this.currentStats[k1] = l15;
                this.maxStats[k1] = 1;
                for(int k20 = 0; k20 < 98; k20++)
                    if(i10 >= Client.anIntArray1019[k20])
                        this.maxStats[k1] = k20 + 2;

                this.pktType = -1;
                return true;
            }
            if(this.pktType == 71)
            {
                int l1 = this.inBuffer.readUShort();
                int j10 = this.inBuffer.readUByteA();
                if(l1 == 65535)
                    l1 = -1;
                this.tabInterfaceIDs[j10] = l1;
                this.needDrawTabArea = true;
                this.tabAreaAltered = true;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 74)
            {
                int i2 = this.inBuffer.readLEUShort();
                if(i2 == 65535)
                    i2 = -1;
                if(i2 != this.currentSong && this.musicEnabled && !Client.lowMem && this.prevSong == 0)
                {
                    this.nextSong = i2;
                    this.songChanging = true;
                    this.onDemandFetcher.method558(2, this.nextSong);
                }
                this.currentSong = i2;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 121)
            {
                int j2 = this.inBuffer.readLEUShortA();
                int k10 = this.inBuffer.readUShortA();
                if(this.musicEnabled && !Client.lowMem)
                {
                    this.nextSong = j2;
                    this.songChanging = false;
                    this.onDemandFetcher.method558(2, this.nextSong);
                    this.prevSong = k10;
                }
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 109)
            {
                this.resetLogout();
                this.pktType = -1;
                return false;
            }
            if(this.pktType == 70)
            {
                int k2 = this.inBuffer.readShort();
                int l10 = this.inBuffer.readLEShort();
                int i16 = this.inBuffer.readLEUShort();
                RSInterface class9_5 = RSInterface.interfaceCache[i16];
                class9_5.anInt263 = k2;
                class9_5.anInt265 = l10;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 73 || this.pktType == 241)
            {
                
	//mapReset();
	int l2 = this.anInt1069;
                int i11 = this.anInt1070;
                if(this.pktType == 73)
                {
                    l2 = this.inBuffer.readUShortA();
                    i11 = this.inBuffer.readUShort();
                    this.aBoolean1159 = false;
                }
                if(this.pktType == 241)
                {
                    i11 = this.inBuffer.readUShortA();
                    this.inBuffer.enableBitAccess();
                    for(int j16 = 0; j16 < 4; j16++)
                    {
                        for(int l20 = 0; l20 < 13; l20++)
                        {
                            for(int j23 = 0; j23 < 13; j23++)
                            {
                                int i26 = this.inBuffer.readBits(1);
                                if(i26 == 1)
                                    this.anIntArrayArrayArray1129[j16][l20][j23] = this.inBuffer.readBits(26);
                                else
                                    this.anIntArrayArrayArray1129[j16][l20][j23] = -1;
                            }

                        }

                    }

                    this.inBuffer.disableBitAccess();
                    l2 = this.inBuffer.readUShort();
                    this.aBoolean1159 = true;
                }
                if(this.anInt1069 == l2 && this.anInt1070 == i11 && this.loadingStage == 2)
                {
                    this.pktType = -1;
                    return true;
                }
                this.anInt1069 = l2;
                this.anInt1070 = i11;
                this.baseX = (this.anInt1069 - 6) * 8;
                this.baseY = (this.anInt1070 - 6) * 8;
                this.aBoolean1141 = (this.anInt1069 / 8 == 48 || this.anInt1069 / 8 == 49) && this.anInt1070 / 8 == 48;
                if(this.anInt1069 / 8 == 48 && this.anInt1070 / 8 == 148)
                    this.aBoolean1141 = true;
                this.loadingStage = 1;
                this.aLong824 = System.currentTimeMillis();
                this.aRSImageProducer_1165.initDrawingArea();
                this.aTextDrawingArea_1271.drawText(0, "Loading - please wait.", 151, 257);
                this.aTextDrawingArea_1271.drawText(0xffffff, "Loading - please wait.", 150, 256);
                this.aRSImageProducer_1165.drawGraphics(4, this.applet.graphics, 4);
                if(this.pktType == 73)
                {
                    int k16 = 0;
                    for(int i21 = (this.anInt1069 - 6) / 8; i21 <= (this.anInt1069 + 6) / 8; i21++)
                    {
                        for(int k23 = (this.anInt1070 - 6) / 8; k23 <= (this.anInt1070 + 6) / 8; k23++)
                            k16++;

                    }

                    this.aByteArrayArray1183 = new byte[k16][];
                    this.aByteArrayArray1247 = new byte[k16][];
                    this.anIntArray1234 = new int[k16];
                    this.anIntArray1235 = new int[k16];
                    this.anIntArray1236 = new int[k16];
                    k16 = 0;
                    for(int l23 = (this.anInt1069 - 6) / 8; l23 <= (this.anInt1069 + 6) / 8; l23++)
                    {
                        for(int j26 = (this.anInt1070 - 6) / 8; j26 <= (this.anInt1070 + 6) / 8; j26++)
                        {
                            this.anIntArray1234[k16] = (l23 << 8) + j26;
                            if(this.aBoolean1141 && (j26 == 49 || j26 == 149 || j26 == 147 || l23 == 50 || l23 == 49 && j26 == 47))
                            {
                                this.anIntArray1235[k16] = -1;
                                this.anIntArray1236[k16] = -1;
                                k16++;
                            } else
                            {
                                int k28 = this.anIntArray1235[k16] = this.onDemandFetcher.method562(0, j26, l23);
                                if(k28 != -1)
                                    this.onDemandFetcher.method558(3, k28);
                                int j30 = this.anIntArray1236[k16] = this.onDemandFetcher.method562(1, j26, l23);
                                if(j30 != -1)
                                    this.onDemandFetcher.method558(3, j30);
                                k16++;
                            }
                        }

                    }

                }
                if(this.pktType == 241)
                {
                    int l16 = 0;
                    int ai[] = new int[676];
                    for(int i24 = 0; i24 < 4; i24++)
                    {
                        for(int k26 = 0; k26 < 13; k26++)
                        {
                            for(int l28 = 0; l28 < 13; l28++)
                            {
                                int k30 = this.anIntArrayArrayArray1129[i24][k26][l28];
                                if(k30 != -1)
                                {
                                    int k31 = k30 >> 14 & 0x3ff;
                                    int i32 = k30 >> 3 & 0x7ff;
                                    int k32 = (k31 / 8 << 8) + i32 / 8;
                                    for(int j33 = 0; j33 < l16; j33++)
                                    {
                                        if(ai[j33] != k32)
                                            continue;
                                        k32 = -1;
                                        break;
                                    }

                                    if(k32 != -1)
                                        ai[l16++] = k32;
                                }
                            }

                        }

                    }

                    this.aByteArrayArray1183 = new byte[l16][];
                    this.aByteArrayArray1247 = new byte[l16][];
                    this.anIntArray1234 = new int[l16];
                    this.anIntArray1235 = new int[l16];
                    this.anIntArray1236 = new int[l16];
                    for(int l26 = 0; l26 < l16; l26++)
                    {
                        int i29 = this.anIntArray1234[l26] = ai[l26];
                        int l30 = i29 >> 8 & 0xff;
                        int l31 = i29 & 0xff;
                        int j32 = this.anIntArray1235[l26] = this.onDemandFetcher.method562(0, l31, l30);
                        if(j32 != -1)
                            this.onDemandFetcher.method558(3, j32);
                        int i33 = this.anIntArray1236[l26] = this.onDemandFetcher.method562(1, l31, l30);
                        if(i33 != -1)
                            this.onDemandFetcher.method558(3, i33);
                    }

                }
                int i17 = this.baseX - this.anInt1036;
                int j21 = this.baseY - this.anInt1037;
                this.anInt1036 = this.baseX;
                this.anInt1037 = this.baseY;
                for(int j24 = 0; j24 < 16384; j24++)
                {
                    NPC npc = this.npcArray[j24];
                    if(npc != null)
                    {
                        for(int j29 = 0; j29 < 10; j29++)
                        {
                            npc.smallX[j29] -= i17;
                            npc.smallY[j29] -= j21;
                        }

                        npc.x -= i17 * 128;
                        npc.y -= j21 * 128;
                    }
                }

                for(int i27 = 0; i27 < this.maxPlayers; i27++)
                {
                    Player player = this.playerArray[i27];
                    if(player != null)
                    {
                        for(int i31 = 0; i31 < 10; i31++)
                        {
                            player.smallX[i31] -= i17;
                            player.smallY[i31] -= j21;
                        }

                        player.x -= i17 * 128;
                        player.y -= j21 * 128;
                    }
                }

                this.aBoolean1080 = true;
                byte byte1 = 0;
                byte byte2 = 104;
                byte byte3 = 1;
                if(i17 < 0)
                {
                    byte1 = 103;
                    byte2 = -1;
                    byte3 = -1;
                }
                byte byte4 = 0;
                byte byte5 = 104;
                byte byte6 = 1;
                if(j21 < 0)
                {
                    byte4 = 103;
                    byte5 = -1;
                    byte6 = -1;
                }
                for(int k33 = byte1; k33 != byte2; k33 += byte3)
                {
                    for(int l33 = byte4; l33 != byte5; l33 += byte6)
                    {
                        int i34 = k33 + i17;
                        int j34 = l33 + j21;
                        for(int k34 = 0; k34 < 4; k34++)
                            if(i34 >= 0 && j34 >= 0 && i34 < 104 && j34 < 104)
                                this.levelObjects[k34][k33][l33] = this.levelObjects[k34][i34][j34];
                            else
                                this.levelObjects[k34][k33][l33] = null;

                    }

                }

                for(SpawnedObject class30_sub1_1 = (SpawnedObject)this.aClass19_1179.getFront(); class30_sub1_1 != null; class30_sub1_1 = (SpawnedObject)this.aClass19_1179.getNext())
                {
                    class30_sub1_1.anInt1297 -= i17;
                    class30_sub1_1.anInt1298 -= j21;
                    if(class30_sub1_1.anInt1297 < 0 || class30_sub1_1.anInt1298 < 0 || class30_sub1_1.anInt1297 >= 104 || class30_sub1_1.anInt1298 >= 104)
                        class30_sub1_1.unlink();
                }

                if(this.destX != 0)
                {
                    this.destX -= i17;
                    this.destY -= j21;
                }
                this.aBoolean1160 = false;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 208)
            {
                int i3 = this.inBuffer.readLEShort();
                if(i3 >= 0)
                    this.method60(i3);
                this.anInt1018 = i3;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 99)
            {
                this.anInt1021 = this.inBuffer.readUByte();
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 75)
            {
                int j3 = this.inBuffer.readLEUShortA();
                int j11 = this.inBuffer.readLEUShortA();
                RSInterface.interfaceCache[j11].anInt233 = 2;
                RSInterface.interfaceCache[j11].mediaID = j3;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 114)
            {
                this.anInt1104 = this.inBuffer.readLEUShort() * 30;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 60)
            {
                this.anInt1269 = this.inBuffer.readUByte();
                this.anInt1268 = this.inBuffer.readNegUByte();
                while(this.inBuffer.position < this.pktSize)
                {
                    int k3 = this.inBuffer.readUByte();
                    this.method137(this.inBuffer, k3);
                }
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 35)
            {
                int l3 = this.inBuffer.readUByte();
                int k11 = this.inBuffer.readUByte();
                int j17 = this.inBuffer.readUByte();
                int k21 = this.inBuffer.readUByte();
                this.aBooleanArray876[l3] = true;
                this.anIntArray873[l3] = k11;
                this.anIntArray1203[l3] = j17;
                this.anIntArray928[l3] = k21;
                this.anIntArray1030[l3] = 0;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 174)
            {
                int i4 = this.inBuffer.readUShort();
                int l11 = this.inBuffer.readUByte();
                int k17 = this.inBuffer.readUShort();
                if(this.aBoolean848 && !Client.lowMem && this.anInt1062 < 50)
                {
                    this.anIntArray1207[this.anInt1062] = i4;
                    this.anIntArray1241[this.anInt1062] = l11;
                    this.anIntArray1250[this.anInt1062] = k17 + Sounds.anIntArray326[i4];
                    this.anInt1062++;
                }
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 104)
            {
                int j4 = this.inBuffer.readNegUByte();
                int i12 = this.inBuffer.readUByteA();
                String s6 = this.inBuffer.readString();
                if(j4 >= 1 && j4 <= 5)
                {
                    if(s6.equalsIgnoreCase("null"))
                        s6 = null;
                    this.atPlayerActions[j4 - 1] = s6;
                    this.atPlayerArray[j4 - 1] = i12 == 0;
                }
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 78)
            {
                this.destX = 0;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 253)
            {
                String s = this.inBuffer.readString();
                if(s.endsWith(":tradereq:"))
                {
                    String s3 = s.substring(0, s.indexOf(":"));
                    long l17 = TextClass.longForName(s3);
                    boolean flag2 = false;
                    for(int j27 = 0; j27 < this.ignoreCount; j27++)
                    {
                        if(this.ignoreListAsLongs[j27] != l17)
                            continue;
                        flag2 = true;
                        break;
                    }

                    if(!flag2 && this.anInt1251 == 0)
                        this.pushMessage("wishes to trade with you.", 4, s3);
                } else
                if(s.endsWith(":duelreq:"))
                {
                    String s4 = s.substring(0, s.indexOf(":"));
                    long l18 = TextClass.longForName(s4);
                    boolean flag3 = false;
                    for(int k27 = 0; k27 < this.ignoreCount; k27++)
                    {
                        if(this.ignoreListAsLongs[k27] != l18)
                            continue;
                        flag3 = true;
                        break;
                    }

                    if(!flag3 && this.anInt1251 == 0)
                        this.pushMessage("wishes to duel with you.", 8, s4);
                } else
                if(s.endsWith(":chalreq:"))
                {
                    String s5 = s.substring(0, s.indexOf(":"));
                    long l19 = TextClass.longForName(s5);
                    boolean flag4 = false;
                    for(int l27 = 0; l27 < this.ignoreCount; l27++)
                    {
                        if(this.ignoreListAsLongs[l27] != l19)
                            continue;
                        flag4 = true;
                        break;
                    }

                    if(!flag4 && this.anInt1251 == 0)
                    {
                        String s8 = s.substring(s.indexOf(":") + 1, s.length() - 9);
                        this.pushMessage(s8, 8, s5);
                    }
                } else
                {
                    this.pushMessage(s, 0, "");
                }
                this.pktType = -1;
	//serverMessage(s);
	
                return true;
            }
            if(this.pktType == 1)
            {
                for(int k4 = 0; k4 < this.playerArray.length; k4++)
                    if(this.playerArray[k4] != null)
                        this.playerArray[k4].anim = -1;

                for(int j12 = 0; j12 < this.npcArray.length; j12++)
                    if(this.npcArray[j12] != null)
                        this.npcArray[j12].anim = -1;

                this.pktType = -1;
                return true;
            }
            if(this.pktType == 50)
            {
                long l4 = this.inBuffer.readLong();
                int i18 = this.inBuffer.readUByte();
                String s7 = TextClass.fixName(TextClass.nameForLong(l4));
                for(int k24 = 0; k24 < this.friendsCount; k24++)
                {
                    if(l4 != this.friendsListAsLongs[k24])
                        continue;
                    if(this.friendsNodeIDs[k24] != i18)
                    {
                        this.friendsNodeIDs[k24] = i18;
                        this.needDrawTabArea = true;
                        if(i18 > 0)
                            this.pushMessage(s7 + " has logged in.", 5, "");
                        if(i18 == 0)
                            this.pushMessage(s7 + " has logged out.", 5, "");
                    }
                    s7 = null;
                    break;
                }

                if(s7 != null && this.friendsCount < 200)
                {
                    this.friendsListAsLongs[this.friendsCount] = l4;
                    this.friendsList[this.friendsCount] = s7;
                    this.friendsNodeIDs[this.friendsCount] = i18;
                    this.friendsCount++;
                    this.needDrawTabArea = true;
                }
                for(boolean flag6 = false; !flag6;)
                {
                    flag6 = true;
                    for(int k29 = 0; k29 < this.friendsCount - 1; k29++)
                        if(this.friendsNodeIDs[k29] != Client.nodeID && this.friendsNodeIDs[k29 + 1] == Client.nodeID || this.friendsNodeIDs[k29] == 0 && this.friendsNodeIDs[k29 + 1] != 0)
                        {
                            int j31 = this.friendsNodeIDs[k29];
                            this.friendsNodeIDs[k29] = this.friendsNodeIDs[k29 + 1];
                            this.friendsNodeIDs[k29 + 1] = j31;
                            String s10 = this.friendsList[k29];
                            this.friendsList[k29] = this.friendsList[k29 + 1];
                            this.friendsList[k29 + 1] = s10;
                            long l32 = this.friendsListAsLongs[k29];
                            this.friendsListAsLongs[k29] = this.friendsListAsLongs[k29 + 1];
                            this.friendsListAsLongs[k29 + 1] = l32;
                            this.needDrawTabArea = true;
                            flag6 = false;
                        }

                }

                this.pktType = -1;
                return true;
            }
            if(this.pktType == 110)
            {
                if(this.tabID == 12)
                    this.needDrawTabArea = true;
                this.energy = this.inBuffer.readUByte();
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 254)
            {
                this.anInt855 = this.inBuffer.readUByte();
                if(this.anInt855 == 1)
                    this.anInt1222 = this.inBuffer.readUShort();
                if(this.anInt855 >= 2 && this.anInt855 <= 6)
                {
                    if(this.anInt855 == 2)
                    {
                        this.anInt937 = 64;
                        this.anInt938 = 64;
                    }
                    if(this.anInt855 == 3)
                    {
                        this.anInt937 = 0;
                        this.anInt938 = 64;
                    }
                    if(this.anInt855 == 4)
                    {
                        this.anInt937 = 128;
                        this.anInt938 = 64;
                    }
                    if(this.anInt855 == 5)
                    {
                        this.anInt937 = 64;
                        this.anInt938 = 0;
                    }
                    if(this.anInt855 == 6)
                    {
                        this.anInt937 = 64;
                        this.anInt938 = 128;
                    }
                    this.anInt855 = 2;
                    this.anInt934 = this.inBuffer.readUShort();
                    this.anInt935 = this.inBuffer.readUShort();
                    this.anInt936 = this.inBuffer.readUByte();
                }
                if(this.anInt855 == 10)
                    this.anInt933 = this.inBuffer.readUShort();
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 248)
            {
                int i5 = this.inBuffer.readUShortA();
                int k12 = this.inBuffer.readUShort();
                if(this.backDialogID != -1)
                {
                    this.backDialogID = -1;
                    this.inputTaken = true;
                }
                if(this.inputDialogState != 0)
                {
                    this.inputDialogState = 0;
                    this.inputTaken = true;
                }
                this.openInterfaceID = i5;
                this.invOverlayInterfaceID = k12;
                this.needDrawTabArea = true;
                this.tabAreaAltered = true;
                this.aBoolean1149 = false;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 79)
            {
                int j5 = this.inBuffer.readLEUShort();
                int l12 = this.inBuffer.readUShortA();
                RSInterface class9_3 = RSInterface.interfaceCache[j5];
                if(class9_3 != null && class9_3.type == 0)
                {
                    if(l12 < 0)
                        l12 = 0;
                    if(l12 > class9_3.scrollMax - class9_3.height)
                        l12 = class9_3.scrollMax - class9_3.height;
                    class9_3.scrollPosition = l12;
                }
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 68)
            {
                for(int k5 = 0; k5 < this.variousSettings.length; k5++)
                    if(this.variousSettings[k5] != this.anIntArray1045[k5])
                    {
                        this.variousSettings[k5] = this.anIntArray1045[k5];
                        this.method33(k5);
                        this.needDrawTabArea = true;
                    }

                this.pktType = -1;
                return true;
            }
            if(this.pktType == 196)
            {
                long l5 = this.inBuffer.readLong();
                int j18 = this.inBuffer.readInt();
                int l21 = this.inBuffer.readUByte();
                boolean flag5 = false;
                for(int i28 = 0; i28 < 100; i28++)
                {
                    if(this.anIntArray1240[i28] != j18)
                        continue;
                    flag5 = true;
                    break;
                }

                if(l21 <= 1)
                {
                    for(int l29 = 0; l29 < this.ignoreCount; l29++)
                    {
                        if(this.ignoreListAsLongs[l29] != l5)
                            continue;
                        flag5 = true;
                        break;
                    }

                }
                if(!flag5 && this.anInt1251 == 0)
                    try
                    {
                        this.anIntArray1240[this.anInt1169] = j18;
                        this.anInt1169 = (this.anInt1169 + 1) % 100;
                        String s9 = TextInput.method525(this.pktSize - 13, this.inBuffer);
                        if(l21 != 3)
                            s9 = Censor.doCensor(s9);
                        if(l21 == 2 || l21 == 3)
                            this.pushMessage(s9, 7, "@cr2@" + TextClass.fixName(TextClass.nameForLong(l5)));
                        else
                        if(l21 == 1)
                            this.pushMessage(s9, 7, "@cr1@" + TextClass.fixName(TextClass.nameForLong(l5)));
                        else
                            this.pushMessage(s9, 3, TextClass.fixName(TextClass.nameForLong(l5)));
                    }
                    catch(Exception exception1)
                    {
                        Utils.reporterror("cde1");
                    }
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 85)
            {
                this.anInt1269 = this.inBuffer.readNegUByte();
                this.anInt1268 = this.inBuffer.readNegUByte();
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 24)
            {
                this.anInt1054 = this.inBuffer.readUByteS();
                if(this.anInt1054 == this.tabID)
                {
                    if(this.anInt1054 == 3)
                        this.tabID = 1;
                    else
                        this.tabID = 3;
                    this.needDrawTabArea = true;
                }
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 246)
            {
                int i6 = this.inBuffer.readLEUShort();
                int i13 = this.inBuffer.readUShort();
                int k18 = this.inBuffer.readUShort();
                if(k18 == 65535)
                {
                    RSInterface.interfaceCache[i6].anInt233 = 0;
                    this.pktType = -1;
                    return true;
                } else
                {
                    ItemDef itemDef = ItemDef.forID(k18);
                    RSInterface.interfaceCache[i6].anInt233 = 4;
                    RSInterface.interfaceCache[i6].mediaID = k18;
                    RSInterface.interfaceCache[i6].anInt270 = itemDef.modelRotation1;
                    RSInterface.interfaceCache[i6].anInt271 = itemDef.modelRotation2;
                    RSInterface.interfaceCache[i6].anInt269 = (itemDef.modelZoom * 100) / i13;
                    this.pktType = -1;
                    return true;
                }
            }
            if(this.pktType == 171)
            {
                boolean flag1 = this.inBuffer.readUByte() == 1;
                int j13 = this.inBuffer.readUShort();
                RSInterface.interfaceCache[j13].aBoolean266 = flag1;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 142)
            {
                int j6 = this.inBuffer.readLEUShort();
                this.method60(j6);
                if(this.backDialogID != -1)
                {
                    this.backDialogID = -1;
                    this.inputTaken = true;
                }
                if(this.inputDialogState != 0)
                {
                    this.inputDialogState = 0;
                    this.inputTaken = true;
                }
                this.invOverlayInterfaceID = j6;
                this.needDrawTabArea = true;
                this.tabAreaAltered = true;
                this.openInterfaceID = -1;
                this.aBoolean1149 = false;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 126)
            {
                String s1 = this.inBuffer.readString();
                int k13 = this.inBuffer.readUShortA();
                RSInterface.interfaceCache[k13].message = s1;
                if(RSInterface.interfaceCache[k13].parentID == this.tabInterfaceIDs[this.tabID])
                    this.needDrawTabArea = true;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 206)
            {
                this.publicChatMode = this.inBuffer.readUByte();
                this.privateChatMode = this.inBuffer.readUByte();
                this.tradeMode = this.inBuffer.readUByte();
                this.aBoolean1233 = true;
                this.inputTaken = true;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 240)
            {
                if(this.tabID == 12)
                    this.needDrawTabArea = true;
                this.weight = this.inBuffer.readShort();
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 8)
            {
                int k6 = this.inBuffer.readLEUShortA();
                int l13 = this.inBuffer.readUShort();
                RSInterface.interfaceCache[k6].anInt233 = 1;
                RSInterface.interfaceCache[k6].mediaID = l13;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 122)
            {
                int l6 = this.inBuffer.readLEUShortA();
                int i14 = this.inBuffer.readLEUShortA();
                int i19 = i14 >> 10 & 0x1f;
                int i22 = i14 >> 5 & 0x1f;
                int l24 = i14 & 0x1f;
                RSInterface.interfaceCache[l6].textColor = (i19 << 19) + (i22 << 11) + (l24 << 3);
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 53)
            {
                this.needDrawTabArea = true;
                int i7 = this.inBuffer.readUShort();
                RSInterface class9_1 = RSInterface.interfaceCache[i7];
                int j19 = this.inBuffer.readUShort();
                for(int j22 = 0; j22 < j19; j22++)
                {
                    int i25 = this.inBuffer.readUByte();
                    if(i25 == 255)
                        i25 = this.inBuffer.readIMEInt();
                    class9_1.inv[j22] = this.inBuffer.readLEUShortA();
                    class9_1.invStackSizes[j22] = i25;
                }

                for(int j25 = j19; j25 < class9_1.inv.length; j25++)
                {
                    class9_1.inv[j25] = 0;
                    class9_1.invStackSizes[j25] = 0;
                }

                this.pktType = -1;
                return true;
            }
            if(this.pktType == 230)
            {
                int j7 = this.inBuffer.readUShortA();
                int j14 = this.inBuffer.readUShort();
                int k19 = this.inBuffer.readUShort();
                int k22 = this.inBuffer.readLEUShortA();
                RSInterface.interfaceCache[j14].anInt270 = k19;
                RSInterface.interfaceCache[j14].anInt271 = k22;
                RSInterface.interfaceCache[j14].anInt269 = j7;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 221)
            {
                this.anInt900 = this.inBuffer.readUByte();
                this.needDrawTabArea = true;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 177)
            {
                this.aBoolean1160 = true;
                this.anInt995 = this.inBuffer.readUByte();
                this.anInt996 = this.inBuffer.readUByte();
                this.anInt997 = this.inBuffer.readUShort();
                this.anInt998 = this.inBuffer.readUByte();
                this.anInt999 = this.inBuffer.readUByte();
                if(this.anInt999 >= 100)
                {
                    int k7 = this.anInt995 * 128 + 64;
                    int k14 = this.anInt996 * 128 + 64;
                    int i20 = this.tileHeight(this.plane, k14, k7) - this.anInt997;
                    int l22 = k7 - this.xCameraPos;
                    int k25 = i20 - this.zCameraPos;
                    int j28 = k14 - this.yCameraPos;
                    int i30 = (int)Math.sqrt(l22 * l22 + j28 * j28);
                    this.yCameraCurve = (int)(Math.atan2(k25, i30) * 325.94900000000001D) & 0x7ff;
                    this.xCameraCurve = (int)(Math.atan2(l22, j28) * -325.94900000000001D) & 0x7ff;
                    if(this.yCameraCurve < 128)
                        this.yCameraCurve = 128;
                    if(this.yCameraCurve > 383)
                        this.yCameraCurve = 383;
                }
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 249)
            {
                this.anInt1046 = this.inBuffer.readUByteA();
                this.localPlayerIndex = this.inBuffer.readLEUShortA();
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 65)
            {
                this.updateNPCs(this.inBuffer, this.pktSize);
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 27)
            {
                this.messagePromptRaised = false;
                this.inputDialogState = 1;
                this.amountOrNameInput = "";
                this.inputTaken = true;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 187)
            {
                this.messagePromptRaised = false;
                this.inputDialogState = 2;
                this.amountOrNameInput = "";
                this.inputTaken = true;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 97)
            {
                int l7 = this.inBuffer.readUShort();
                this.method60(l7);
                if(this.invOverlayInterfaceID != -1)
                {
                    this.invOverlayInterfaceID = -1;
                    this.needDrawTabArea = true;
                    this.tabAreaAltered = true;
                }
                if(this.backDialogID != -1)
                {
                    this.backDialogID = -1;
                    this.inputTaken = true;
                }
                if(this.inputDialogState != 0)
                {
                    this.inputDialogState = 0;
                    this.inputTaken = true;
                }
                this.openInterfaceID = l7;
                this.aBoolean1149 = false;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 218)
            {
                int i8 = this.inBuffer.readLEShortA();
                this.dialogID = i8;
                this.inputTaken = true;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 87)
            {
                int j8 = this.inBuffer.readLEUShort();
                int l14 = this.inBuffer.readMEInt();
                this.anIntArray1045[j8] = l14;
                if(this.variousSettings[j8] != l14)
                {
                    this.variousSettings[j8] = l14;
                    this.method33(j8);
                    this.needDrawTabArea = true;
                    if(this.dialogID != -1)
                        this.inputTaken = true;
                }
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 36)
            {
                int k8 = this.inBuffer.readLEUShort();
                byte byte0 = this.inBuffer.readByte();
                this.anIntArray1045[k8] = byte0;
                if(this.variousSettings[k8] != byte0)
                {
                    this.variousSettings[k8] = byte0;
                    this.method33(k8);
                    this.needDrawTabArea = true;
                    if(this.dialogID != -1)
                        this.inputTaken = true;
                }
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 61)
            {
                this.anInt1055 = this.inBuffer.readUByte();
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 200)
            {
                int l8 = this.inBuffer.readUShort();
                int i15 = this.inBuffer.readShort();
                RSInterface class9_4 = RSInterface.interfaceCache[l8];
                class9_4.anInt257 = i15;
                if(i15 == -1)
                {
                    class9_4.anInt246 = 0;
                    class9_4.anInt208 = 0;
                }
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 219)
            {
                if(this.invOverlayInterfaceID != -1)
                {
                    this.invOverlayInterfaceID = -1;
                    this.needDrawTabArea = true;
                    this.tabAreaAltered = true;
                }
                if(this.backDialogID != -1)
                {
                    this.backDialogID = -1;
                    this.inputTaken = true;
                }
                if(this.inputDialogState != 0)
                {
                    this.inputDialogState = 0;
                    this.inputTaken = true;
                }
                this.openInterfaceID = -1;
                this.aBoolean1149 = false;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 34)
            {
                this.needDrawTabArea = true;
                int i9 = this.inBuffer.readUShort();
                RSInterface class9_2 = RSInterface.interfaceCache[i9];
                while(this.inBuffer.position < this.pktSize)
                {
                    int j20 = this.inBuffer.readUSmart();
                    int i23 = this.inBuffer.readUShort();
                    int l25 = this.inBuffer.readUByte();
                    if(l25 == 255)
                        l25 = this.inBuffer.readInt();
                    if(j20 >= 0 && j20 < class9_2.inv.length)
                    {
                        class9_2.inv[j20] = i23;
                        class9_2.invStackSizes[j20] = l25;
                    }
                }
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 105 || this.pktType == 84 || this.pktType == 147 || this.pktType == 215 || this.pktType == 4 || this.pktType == 117 || this.pktType == 156 || this.pktType == 44 || this.pktType == 160 || this.pktType == 101 || this.pktType == 151)
            {
                this.method137(this.inBuffer, this.pktType);
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 106)
            {
                this.tabID = this.inBuffer.readNegUByte();
                this.needDrawTabArea = true;
                this.tabAreaAltered = true;
                this.pktType = -1;
                return true;
            }
            if(this.pktType == 164)
            {
                int j9 = this.inBuffer.readLEUShort();
                this.method60(j9);
                if(this.invOverlayInterfaceID != -1)
                {
                    this.invOverlayInterfaceID = -1;
                    this.needDrawTabArea = true;
                    this.tabAreaAltered = true;
                }
                this.backDialogID = j9;
                this.inputTaken = true;
                this.openInterfaceID = -1;
                this.aBoolean1149 = false;
                this.pktType = -1;
                return true;
            }
            Utils.reporterror("T1 - " + this.pktType + "," + this.pktSize + " - " + this.anInt842 + "," + this.anInt843);
            this.resetLogout();
        }
        catch(IOException _ex)
        {
            this.dropClient();
        }
        catch(Exception exception)
        {
            String s2 = "T2 - " + this.pktType + "," + this.anInt842 + "," + this.anInt843 + " - " + this.pktSize + "," + (this.baseX + Client.localPlayer.smallX[0]) + "," + (this.baseY + Client.localPlayer.smallY[0]) + " - ";
            for(int j15 = 0; j15 < this.pktSize && j15 < 50; j15++)
                s2 = s2 + this.inBuffer.payload[j15] + ",";

            Utils.reporterror(s2);
            this.resetLogout();
        }
        return true;
    }

    private void method146()
    {
        this.anInt1265++;
        this.method47(true);
        this.method26(true);
        this.method47(false);
        this.method26(false);
        this.updateSceneProjectiles();
        this.method104();
        if(!this.aBoolean1160)
        {
            int cameraPitch = this.anInt1184;
            if(this.cameraMaxY / 256 > cameraPitch)
                cameraPitch = this.cameraMaxY / 256;
            if(this.aBooleanArray876[4] && this.anIntArray1203[4] + 128 > cameraPitch)
                cameraPitch = this.anIntArray1203[4] + 128;
            int k = this.minimapInt1 + this.anInt896 & 0x7ff;
            this.setCameraPos(Client.cameraZoom + cameraPitch * ((Constants.VIEW_DISTANCE == 9) && (RSBase.frameMode == ScreenMode.RESIZABLE) ? 2 : Constants.VIEW_DISTANCE == 10 ? 5 : 3), cameraPitch, this.anInt1014, this.tileHeight(this.plane, Client.localPlayer.y, Client.localPlayer.x) - 50, k, this.anInt1015);
        }
        int j;
        if(!this.aBoolean1160)
            j = this.method120();
        else
            j = this.method121();
        int l = this.xCameraPos;
        int i1 = this.zCameraPos;
        int j1 = this.yCameraPos;
        int k1 = this.yCameraCurve;
        int l1 = this.xCameraCurve;
        for(int i2 = 0; i2 < 5; i2++)
            if(this.aBooleanArray876[i2])
            {
                int j2 = (int)((Math.random() * (this.anIntArray873[i2] * 2 + 1) - this.anIntArray873[i2]) + Math.sin(this.anIntArray1030[i2] * (this.anIntArray928[i2] / 100D)) * this.anIntArray1203[i2]);
                if(i2 == 0)
                    this.xCameraPos += j2;
                if(i2 == 1)
                    this.zCameraPos += j2;
                if(i2 == 2)
                    this.yCameraPos += j2;
                if(i2 == 3)
                    this.xCameraCurve = this.xCameraCurve + j2 & 0x7ff;
                if(i2 == 4)
                {
                    this.yCameraCurve += j2;
                    if(this.yCameraCurve < 128)
                        this.yCameraCurve = 128;
                    if(this.yCameraCurve > 383)
                        this.yCameraCurve = 383;
                }
            }

        int k2 = Texture.anInt1481;
        ModelRenderer.aBoolean1684 = true;
            ModelRenderer.anInt1687 = 0;
            ModelRenderer.anInt1685 = this.applet.mouseX - 4;
            ModelRenderer.anInt1686 = this.applet.mouseY - 4;
            DrawingArea.setAllPixelsToZero();
//xxx disables graphics            if(graphicsEnabled){
            this.worldController.method313(this.xCameraPos, this.yCameraPos, this.xCameraCurve, this.zCameraPos, j, this.yCameraCurve);
            this.worldController.clearObj5Cache();
            this.updateEntities();
            this.drawHeadIcon();
            this.method37(k2);
            this.draw3dScreen();
    		if (RSBase.frameMode != ScreenMode.FIXED) {
    		//TODO	drawChatArea();
    			Client.drawMinimap(this, this.minimapInt1, this.minimapInt2, this.minimapInt3);
    		//	drawTabArea();
    		}
    		this.aRSImageProducer_1165.drawGraphics(RSBase.frameMode == ScreenMode.FIXED ? 4 : 0, this.applet.graphics, RSBase.frameMode == ScreenMode.FIXED ? 4 : 0);
            this.xCameraPos = l;
            this.zCameraPos = i1;
            this.yCameraPos = j1;
            this.yCameraCurve = k1;
            this.xCameraCurve = l1;
//            }
    }

    private void clearTopInterfaces()
    {
        this.stream.writeOpcode(130);
        if(this.invOverlayInterfaceID != -1)
        {
            this.invOverlayInterfaceID = -1;
            this.needDrawTabArea = true;
            this.aBoolean1149 = false;
            this.tabAreaAltered = true;
        }
        if(this.backDialogID != -1)
        {
            this.backDialogID = -1;
            this.inputTaken = true;
            this.aBoolean1149 = false;
        }
        this.openInterfaceID = -1;
    }

    private Client()
    {
        this.friendsNodeIDs = new int[200];
        this.levelObjects = new Deque[4][104][104];
        this.aStream_834 = new PacketStream(new byte[5000]);
        this.npcArray = new NPC[16384];
        this.npcIndices = new int[16384];
        this.anIntArray840 = new int[1000];
        this.aStream_847 = new PacketStream(new byte[5000]);
        this.aBoolean848 = true;
        this.openInterfaceID = -1;
        this.currentExp = new int[Skills.skillsCount];
        this.anIntArray873 = new int[5];
        this.anInt874 = -1;
        this.aBooleanArray876 = new boolean[5];
        this.reportAbuseInput = "";
        this.localPlayerIndex = -1;
        this.menuOpen = false;
        this.inputString = "";
        this.maxPlayers = 2048;
        this.myPlayerIndex = 2047;
        this.playerArray = new Player[this.maxPlayers];
        this.playerIndices = new int[this.maxPlayers];
        this.anIntArray894 = new int[this.maxPlayers];
        this.aBufferArray895s = new Buffer[this.maxPlayers];
        this.anInt897 = 1;
        this.anInt902 = 0x766654;
        this.aByteArray912 = new byte[16384];
        this.currentStats = new int[Skills.skillsCount];
        this.ignoreListAsLongs = new long[100];
        this.loadingError = false;
        this.anInt927 = 0x332d25;
        this.anIntArray928 = new int[5];
        this.anIntArrayArray929 = new int[104][104];
        this.chatTypes = new int[100];
        this.chatNames = new String[100];
        this.chatMessages = new String[100];
        this.sideIcons = new IndexedImage[13];
        this.aBoolean954 = true;
        this.friendsListAsLongs = new long[200];
        this.currentSong = -1;
        this.spriteDrawX = -1;
        this.spriteDrawY = -1;
        this.variousSettings = new int[2000];
        this.aBoolean972 = false;
        this.anInt975 = 50;
        this.anIntArray976 = new int[this.anInt975];
        this.anIntArray977 = new int[this.anInt975];
        this.anIntArray978 = new int[this.anInt975];
        this.anIntArray979 = new int[this.anInt975];
        this.anIntArray980 = new int[this.anInt975];
        this.anIntArray981 = new int[this.anInt975];
        this.anIntArray982 = new int[this.anInt975];
        this.aStringArray983 = new String[this.anInt975];
        this.anInt985 = -1;
        this.hitMarks = new Sprite[20];
        this.anIntArray990 = new int[5];
        this.aBoolean994 = false;
        this.anInt1002 = 0x23201b;
        this.amountOrNameInput = "";
        this.projectiles = new Deque();
        this.aBoolean1017 = false;
        this.anInt1018 = -1;
        this.anIntArray1030 = new int[5];
        this.aBoolean1031 = false;
        this.mapFunctions = new Sprite[100];
        this.dialogID = -1;
        this.maxStats = new int[Skills.skillsCount];
        this.anIntArray1045 = new int[2000];
        this.aBoolean1047 = true;
       
        this.anInt1054 = -1;
        this.aClass19_1056 = new Deque();
        
        this.aClass9_1059 = new RSInterface();
        this.mapScenes = new IndexedImage[100];
        this.anInt1063 = 0x4d4233;
        this.anIntArray1065 = new int[7];
        this.minimapFunctionX = new int[1000];
        this.minimapFunctionY = new int[1000];
        this.aBoolean1080 = false;
        this.friendsList = new String[200];
        this.inBuffer = new BitBuffer(new byte[5000]);
        this.menuActionCmd2 = new int[500];
        this.menuActionCmd3 = new int[500];
        this.menuActionID = new int[500];
        this.menuActionCmd1 = new int[500];
        this.headIcons = new Sprite[20];
        this.tabAreaAltered = false;
        this.aString1121 = "";
        this.atPlayerActions = new String[5];
        this.atPlayerArray = new boolean[5];
        this.anIntArrayArrayArray1129 = new int[4][13][13];
        this.anInt1132 = 2;
        this.minimapFunctions = new Sprite[1000];
        this.aBoolean1141 = false;
        this.aBoolean1149 = false;
        this.crosses = new Sprite[8];
        this.musicEnabled = true;
        this.needDrawTabArea = false;
        this.loggedIn = false;
        this.canMute = false;
        this.aBoolean1159 = false;
        this.aBoolean1160 = false;
        this.anInt1171 = 1;
        this.myUsername = "mopar";
        this.myPassword = "bob";
        this.genericLoadingError = false;
        this.reportAbuseInterfaceID = -1;
        this.aClass19_1179 = new Deque();
        this.anInt1184 = 128;
        this.invOverlayInterfaceID = -1;
        this.stream = new PacketStream(new byte[5000]);
        this.menuActionName = new String[500];
        this.anIntArray1203 = new int[5];
        this.anIntArray1207 = new int[50];
        this.anInt1210 = 2;
        this.anInt1211 = 78;
        this.promptInput = "";
        this.modIcons = new IndexedImage[2];
        this.tabID = 3;
        this.inputTaken = false;
        this.songChanging = true;
        
        this.aClass11Array1230 = new CollisionMap[4];
        this.aBoolean1233 = false;
        this.anIntArray1240 = new int[100];
        this.anIntArray1241 = new int[50];
        this.aBoolean1242 = false;
        this.anIntArray1250 = new int[50];
        this.rsAlreadyLoaded = false;
        this.welcomeScreenRaised = false;
        this.messagePromptRaised = false;
        this.loginMessage1 = "";
        this.loginMessage2 = "Enter your username & password.";
        this.backDialogID = -1;
        this.anInt1279 = 2;
        this.anInt1289 = -1;
    }

    private int ignoreCount;
    private long aLong824;
    private int[] friendsNodeIDs;
    private Deque[][][] levelObjects;

    private PacketStream aStream_834;
    private NPC[] npcArray;
    private int npcCount;
    private int[] npcIndices;
    private int anInt839;
    private int[] anIntArray840;
    private int anInt841;
    private int anInt842;
    private int anInt843;
    private String aString844;
    private int privateChatMode;
    private PacketStream aStream_847;
    private boolean aBoolean848;
    private static int anInt849;
    private static int anInt854;
    private int anInt855;
    private int openInterfaceID;
    private int xCameraPos;
    private int zCameraPos;
    private int yCameraPos;
    private int yCameraCurve;
    private int xCameraCurve;
    private int myPrivilege;
    private final int[] currentExp;
    private IndexedImage redStone1_3;
    private IndexedImage redStone2_3;
    private IndexedImage redStone3_2;
    private IndexedImage redStone1_4;
    private IndexedImage redStone2_4;
    private Sprite mapFlag;
    private Sprite mapMarker;
    private final int[] anIntArray873;
    private int anInt874;
    private final boolean[] aBooleanArray876;
    private int weight;
    private MouseDetection mouseDetection;
    private String reportAbuseInput;
    private int localPlayerIndex;
    private boolean menuOpen;
    private int anInt886;
    private String inputString;
    private final int maxPlayers;
    private final int myPlayerIndex;
    private Player[] playerArray;
    private int playerCount;
    private int[] playerIndices;
    private int anInt893;
    private int[] anIntArray894;
    private Buffer[] aBufferArray895s;
    private int anInt896;
    private int anInt897;
    private int friendsCount;
    private int anInt900;
    private final int anInt902;

    private byte[] aByteArray912;
    private int anInt913;
    private int crossX;
    private int crossY;
    private int crossIndex;
    private int crossType;
    int plane;
    private final int[] currentStats;
    private static int anInt924;
    private final long[] ignoreListAsLongs;
    private boolean loadingError;
    private final int anInt927;
    private final int[] anIntArray928;
    private int[][] anIntArrayArray929;
    private Sprite aClass30_Sub2_Sub1_Sub1_931;
    private Sprite aClass30_Sub2_Sub1_Sub1_932;
    private int anInt933;
    private int anInt934;
    private int anInt935;
    private int anInt936;
    private int anInt937;
    private int anInt938;
    private static int anInt940;
    private final int[] chatTypes;
    private final String[] chatNames;
    private final String[] chatMessages;
    private int anInt945;
    private WorldController worldController;
    private IndexedImage[] sideIcons;
    private int menuScreenArea;
    private int menuOffsetX;
    private int menuOffsetY;
    private int menuWidth;
    private int anInt952;
    private long aLong953;
    private boolean aBoolean954;
    private long[] friendsListAsLongs;
    private int currentSong;
    private static boolean lowMem;
    private int spriteDrawX;
    private int spriteDrawY;
    private final int[] anIntArray965 = {
        0xffff00, 0xff0000, 65280, 65535, 0xff00ff, 0xffffff
    };

    public final Index[] indexs = new Index[5];
    public int variousSettings[];
    private boolean aBoolean972;
    private final int anInt975;
    private final int[] anIntArray976;
    private final int[] anIntArray977;
    private final int[] anIntArray978;
    private final int[] anIntArray979;
    private final int[] anIntArray980;
    private final int[] anIntArray981;
    private final int[] anIntArray982;
    private final String[] aStringArray983;
    private int cameraMaxY;
    private int anInt985;
    private static int anInt986;
    private Sprite[] hitMarks;
    private int anInt988;
    private int anInt989;
    private final int[] anIntArray990;
    private static boolean aBoolean993;
    private final boolean aBoolean994;
    private int anInt995;
    private int anInt996;
    private int anInt997;
    private int anInt998;
    private int anInt999;
    private ISAACRandomGen encryption;
    private final int anInt1002;
    public static final int[][] anIntArrayArray1003 = {
        {
            6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 
            2983, 54193
        }, {
            8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 
            56621, 4783, 1341, 16578, 35003, 25239
        }, {
            25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 
            10153, 56621, 4783, 1341, 16578, 35003
        }, {
            4626, 11146, 6439, 12, 4758, 10270
        }, {
            4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574
        }
    };
    private String amountOrNameInput;
    private static int anInt1005;
    private int daysSinceLastLogin;
    private int pktSize;
    private int pktType;
    private int anInt1009;
    private int anInt1010;
    private int anInt1011;
    private Deque projectiles;
    private int anInt1014;
    private int anInt1015;
    private int anInt1016;
    private boolean aBoolean1017;
    private int anInt1018;
    private static final int[] anIntArray1019;
    private int anInt1021;
    private int anInt1022;
    private int loadingStage;
    private IndexedImage scrollBar1;
    private IndexedImage scrollBar2;
    private int anInt1026;
    private IndexedImage backBase1;
    private IndexedImage backBase2;
    private IndexedImage backHmid1;
    private final int[] anIntArray1030;
    private boolean aBoolean1031;
    private Sprite[] mapFunctions;
    private int baseX;
    private int baseY;
    private int anInt1036;
    private int anInt1037;
    private int loginFailures;
    private int anInt1039;
    private int dialogID;
    private final int[] maxStats;
    private final int[] anIntArray1045;
    private int anInt1046;
    private boolean aBoolean1047;
    private int anInt1048;
    private static int anInt1051;
    Archive titleStreamLoader;
    private int anInt1054;
    private int anInt1055;
    private Deque aClass19_1056;
    private final RSInterface aClass9_1059;
    private IndexedImage[] mapScenes;
    private static int anInt1061;
    private int anInt1062;
    private final int anInt1063;
    private int friendsListAction;
    private final int[] anIntArray1065;
    private int mouseInvInterfaceIndex;
    private int lastActiveInvInterface;
    private OnDemandFetcher onDemandFetcher;
    private int anInt1069;
    private int anInt1070;
    private int minimapFunctionCount;
    private int[] minimapFunctionX;
    private int[] minimapFunctionY;
    private Sprite mapDotItem;
    private Sprite mapDotNPC;
    private Sprite mapDotPlayer;
    private Sprite mapDotFriend;
    private Sprite mapDotTeam;
    private boolean aBoolean1080;
    private String[] friendsList;
    private BitBuffer inBuffer;
    private int anInt1084;
    private int anInt1085;
    private int activeInterfaceType;
    private int anInt1087;
    private int anInt1088;
    private int anInt1089;
    private int[] menuActionCmd2;
    private int[] menuActionCmd3;
    private int[] menuActionID;
    private int[] menuActionCmd1;
    private Sprite[] headIcons;
    private static int anInt1097;
    private int anInt1098;
    private int anInt1099;
    private int anInt1100;
    private int anInt1101;
    private int anInt1102;
    private boolean tabAreaAltered;
    private int anInt1104;

    private static int anInt1117;
    private int membersInt;
    private String aString1121;
    private Sprite compass;
    private RSImageProducer aRSImageProducer_1123;
    private RSImageProducer aRSImageProducer_1124;
    private RSImageProducer aRSImageProducer_1125;
    public static Player localPlayer;
    private final String[] atPlayerActions;
    private final boolean[] atPlayerArray;
    private final int[][][] anIntArrayArrayArray1129;
    private final int[] tabInterfaceIDs = {
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1
    };
    private int anInt1131;
    private int anInt1132;
    private int menuActionRow;
    private static int anInt1134;
    private int spellSelected;
    private int anInt1137;
    private int spellUsableOn;
    private String spellTooltip;
    private Sprite[] minimapFunctions;
    private boolean aBoolean1141;
    private static int anInt1142;
    private IndexedImage redStone1;
    private IndexedImage redStone2;
    private IndexedImage redStone3;
    private IndexedImage redStone1_2;
    private IndexedImage redStone2_2;
    private int energy;
    private boolean aBoolean1149;
    private Sprite[] crosses;
    private boolean musicEnabled;
    private boolean needDrawTabArea;
    private int unreadMessages;
    private static int anInt1155;
    private static boolean fpsOn;
    public boolean loggedIn;
    private boolean canMute;
    private boolean aBoolean1159;
    private boolean aBoolean1160;
    public static int loopCycle;
    private static final String validUserPassChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
    private RSImageProducer aRSImageProducer_1163;
    private RSImageProducer aRSImageProducer_1165;
    private RSImageProducer aRSImageProducer_1166;
    private int daysSinceRecovChange;
    private RSSocket socketStream;
    private int anInt1169;
    private int minimapInt3;
    private int anInt1171;
    private long aLong1172;
    private String myUsername;
    private String myPassword;
    private static int anInt1175;
    private boolean genericLoadingError;
    private final int[] anIntArray1177 = {
        0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
        2, 2, 3
    };
    private int reportAbuseInterfaceID;
    private Deque aClass19_1179;
    private int[] anIntArray1180;
    private int[] anIntArray1181;
    private int[] anIntArray1182;
    private byte[][] aByteArrayArray1183;
    private int anInt1184;
    private int minimapInt1;
    private int anInt1186;
    private int anInt1187;
    private static int anInt1188;
    private int invOverlayInterfaceID;
    private PacketStream stream;
    private int anInt1193;
    private int splitPrivateChat;
    private IndexedImage invBack;
    private IndexedImage mapBack;
    private IndexedImage chatBack;
    private String[] menuActionName;
    private final int[] anIntArray1203;
    public static final int[] anIntArray1204 = {
        9104, 10275, 7595, 3610, 7975, 8526, 918, 38802, 24466, 10145, 
        58654, 5027, 1457, 16565, 34991, 25486
    };
    private static boolean flagged;
    private final int[] anIntArray1207;
    private int flameTick;
    private int minimapInt2;
    private int anInt1210;
    private int anInt1211;
    private String promptInput;
    private int anInt1213;
    private int[][][] intGroundArray;
    private long aLong1215;
    private int loginScreenCursorPos = 0;
    private final IndexedImage[] modIcons;
    private long aLong1220;
    private int tabID;
    private int anInt1222;
    private boolean inputTaken;
    private int inputDialogState;
    private static int anInt1226;
    private int nextSong;
    private boolean songChanging;
    CollisionMap[] aClass11Array1230;
    private boolean aBoolean1233;
    private int[] anIntArray1234;
    private int[] anIntArray1235;
    private int[] anIntArray1236;
    private int anInt1237;
    private int anInt1238;
    public final int anInt1239 = 100;
    private final int[] anIntArray1240;
    private final int[] anIntArray1241;
    private boolean aBoolean1242;
    private int atInventoryLoopCycle;
    private int atInventoryInterface;
    private int atInventoryIndex;
    private int atInventoryInterfaceType;
    private byte[][] aByteArrayArray1247;
    private int tradeMode;
    private int anInt1249;
    private final int[] anIntArray1250;
    private int anInt1251;
    private final boolean rsAlreadyLoaded;
    private int anInt1253;
    private int anInt1254;
    private boolean welcomeScreenRaised;
    private boolean messagePromptRaised;
    private int anInt1257;
    private byte[][][] byteGroundArray;
    private int prevSong;
    private int destX;
    private int destY;
    private Sprite minimapImage;
    private int anInt1265;
    private String loginMessage1;
    private String loginMessage2;
    private int anInt1268;
    private int anInt1269;
    private TextDrawingArea aTextDrawingArea_1270;
    private TextDrawingArea aTextDrawingArea_1271;
    private TextDrawingArea chatTextDrawingArea;
    private int backDialogID;
    private int anInt1278;
    private int anInt1279;
    private int itemSelected;
    private int anInt1283;
    private int anInt1284;
    private int anInt1285;
    private String selectedItemName;
    private int publicChatMode;
    private static int anInt1288;
    private int anInt1289;
	public static int anInt1290;

    static 
    {
        anIntArray1019 = new int[99];
        int i = 0;
        for(int j = 0; j < 99; j++)
        {
            int l = j + 1;
            int i1 = (int)(l + 300D * Math.pow(2D, l / 7D));
            i += i1;
            Client.anIntArray1019[j] = i / 4;
        }
    }
    
	public int anInt1044;//377
	public int anInt1129;//377
	public int anInt1315;//377
	public int anInt1500;//377
	public int anInt1501;//377

	private static boolean isMembers = true;

	private static int nodeID = 10;

	@Override
	public void writepath(int currentIndex, int movementType, int[] waypointX, int[] waypointY) {
		int waypointCount = currentIndex;
		if (waypointCount > 25)
			waypointCount = 25;
		currentIndex--;
		int x = waypointX[currentIndex];
		int y = waypointY[currentIndex];

		Client.anInt1288 += waypointCount;
		if (Client.anInt1288 >= 92) {
			this.stream.writeOpcode(36);
			this.stream.writeInt(0);
			Client.anInt1288 = 0;
		}
		if (movementType == 0) {
			this.stream.writeOpcode(164);
			this.stream.writeByte(waypointCount + waypointCount + 3);
		}
		if (movementType == 1) {
			this.stream.writeOpcode(248);
			this.stream.writeByte(waypointCount + waypointCount + 3 + 14);
		}
		if (movementType == 2) {
			this.stream.writeOpcode(98);
			this.stream.writeByte(waypointCount + waypointCount + 3);
		}
		this.stream.writeLEShortA(x + this.baseX);
		this.destX = waypointX[0];
		this.destY = waypointY[0];
		for (int j7 = 1; j7 < waypointCount; j7++) {
			currentIndex--;
			this.stream.writeByte(waypointX[currentIndex] - x);
			this.stream.writeByte(waypointY[currentIndex] - y);
		}

		this.stream.writeLEShort(y + this.baseY);
		this.stream.writeNegatedByte(this.applet.keyArray[5] != 1 ? 0 : 1);
	}
}
