package com.jagex.map;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.jagex.Utils;
import com.jagex.cache.def.ObjectDef;
import com.jagex.draw.Texture;
import com.jagex.entity.Animable;
import com.jagex.entity.Animable_Sub5;
import com.jagex.entity.model.Model;
import com.jagex.io.Buffer;
import com.jagex.net.OnDemandFetcher;
import com.jagex.util.FileOperations;

public final class ObjectManager {

    /**
     * Constant naming scheme:
     * *FC - False colour
     * *RES - Resolution (as in: to look something up)
     */
    public static final int NO_BLENDING_BIT   = 1;//Disable Blending
    public static final int NO_OVERLAY_BIT    = 2;
    public static final int NO_COLOUR_RES_BIT = 4;
    public static final int RENDER_HIDDEN_BIT = 16;
    public static final int TILE_SETTINGS_FC = ObjectManager.NO_BLENDING_BIT | ObjectManager.NO_COLOUR_RES_BIT | ObjectManager.NO_OVERLAY_BIT | 8;
    
    public ObjectManager(byte abyte0[][][], int ai[][][])
    {
        ObjectManager.maximumPlane = 99;
        this.width = 104;
        this.length = 104;
        this.tileHeights = ai;
        this.tileFlags = abyte0;
        this.underlays = new byte[4][this.width][this.length];
        this.overlays = new byte[4][this.width][this.length];
        this.overlayShape = new byte[4][this.width][this.length];
        this.aByteArrayArrayArray148 = new byte[4][this.width][this.length];
        this.tile_culling_bitmap = new int[4][this.width + 1][this.length + 1];
        this.shading = new byte[4][this.width + 1][this.length + 1];
        this.tileLighting = new int[this.width + 1][this.length + 1];
        this.hue_buffer = new int[this.length];
        this.saturation_buffer = new int[this.length];
        this.lightness_buffer = new int[this.length];
        this.chroma_buffer = new int[this.length];
        this.buffer_size = new int[this.length];
    }

    private static int method170(int i, int j)
    {
        int k = i + j * 57;
        k = k << 13 ^ k;
        int l = k * (k * k * 15731 + 0xc0ae5) + 0x5208dd0d & 0x7fffffff;
        return l >> 19 & 0xff;
    }


	final void method1344(CollisionMap aclass11[], WorldController worldController, final boolean underwater) {
		int levelAmount;
		if (underwater) {
			levelAmount = 1;
		} else {
			levelAmount = 4;
		}
		if (!underwater) {
			for (int level = 0; level < 4; level++) {
				for (int k = 0; k < 104; k++) {
					for (int i1 = 0; i1 < 104; i1++)
						if ((this.tileFlags[level][k][i1] & 1) == 1) {
							int k1 = level;
							if ((this.tileFlags[1][k][i1] & 2) == 2)
								k1--;
							if (k1 >= 0)
								aclass11[k1].block(k, i1);
						}

				}
			}
			
		}
		final int[][] tileHsl = new int[104][104];
		final int[][] tileLighting = new int[104][104];
		for (int level = 0; level < levelAmount; level++) {
			final byte[][] is_10_ = this.shading[level];
				final int xLight0 = -50;
				final int yLight0 = -10;
				final int zLight0 = -50;
				final int length = (int) Math.sqrt(xLight0 * xLight0 + yLight0 * yLight0 + zLight0 * zLight0);
				final int i_31_ = 768 * length >> 8;
				for (int z = 1; z < 103; z++) {
					for (int x = 1; x < 103; x++) {
						int i_34_ = 74;
						final int xHeightDiff = this.tileHeights[level][x + 1][z] - this.tileHeights[level][x - 1][z];
						final int yHeightDiff = 0x10000;
						final int zHeightDiff = this.tileHeights[level][x][z + 1] - this.tileHeights[level][x][z - 1];
						final int i_37_ = (int) Math.sqrt(xHeightDiff * xHeightDiff + yHeightDiff + zHeightDiff * zHeightDiff);
						final int i_38_ = (xHeightDiff << 8) / i_37_;
						final int i_39_ = -yHeightDiff / i_37_;
						final int i_40_ = (zHeightDiff << 8) / i_37_;
						i_34_ += (xLight0 * i_38_ + yLight0 * i_39_ + zLight0 * i_40_) / i_31_;
						final int i_41_ = (is_10_[x][z - 1] >> 2) + (is_10_[x - 1][z] >> 2) + (is_10_[x + 1][z] >> 3) + (is_10_[x][z + 1] >> 3) + (is_10_[x][z] >> 1);
						tileLighting[x][z] = i_34_ - i_41_;
					}
				}
			
			for (int i_42_ = 0; i_42_ < 104; i_42_++) {
				this.hue_buffer[i_42_] = 0;
				this.saturation_buffer[i_42_] = 0;
				this.lightness_buffer[i_42_] = 0;
				this.chroma_buffer[i_42_] = 0;
				this.buffer_size[i_42_] = 0;
			}
			for (int centreX = -5; centreX < 104; centreX++) {
				for (int y = 0; y < 104; y++) {
					final int maxX = centreX + 5;
					if (maxX < 104) {
						final int i_46_ = this.underlays[level][maxX][y] & 0xff;
						if (i_46_ > 0) {
							final Flo overlayType = Flo.cache[i_46_ - 1];//underlay_flo
							this.hue_buffer[y] += overlayType.weightedHue;
							this.saturation_buffer[y] += overlayType.saturation;
							this.lightness_buffer[y] += overlayType.luminance;
							this.chroma_buffer[y] += overlayType.chroma;
							this.buffer_size[y]++;
						}
					}
					final int minX = centreX - 5;
					if (minX >= 0) {
						final int i_48_ = this.underlays[level][minX][y] & 0xff;//underlay_flo
						if (i_48_ > 0) {
							final Flo overlayType = Flo.cache[i_48_- 1];
							this.hue_buffer[y] -= overlayType.weightedHue;
							this.saturation_buffer[y] -= overlayType.saturation;
							this.lightness_buffer[y] -= overlayType.luminance;
							this.chroma_buffer[y] -= overlayType.chroma;
							this.buffer_size[y]--;
						}
					}
				}
				if (centreX >= 0) {
					int tile_hue = 0;
					int tile_light = 0;
					int tile_chroma = 0;
					int tile_buffer_size = 0;
					int tile_saturation = 0;
					for (int centreY = -5; centreY < 104; centreY++) {
						final int maxY = centreY + 5;
						if (maxY < 104) {
							tile_chroma += this.chroma_buffer[maxY];
							tile_buffer_size += this.buffer_size[maxY];
							tile_saturation += this.saturation_buffer[maxY];
							tile_hue += this.hue_buffer[maxY];
							tile_light += this.lightness_buffer[maxY];
						}
						final int minY = centreY - 5;
						if (minY >= 0) {
							tile_chroma -= this.chroma_buffer[minY];
							tile_light -= this.lightness_buffer[minY];
							tile_hue -= this.hue_buffer[minY];
							tile_buffer_size -= this.buffer_size[minY];
							tile_saturation -= this.saturation_buffer[minY];
						}
						if (centreY >= 0 && tile_buffer_size > 0) {
							tileHsl[centreX][centreY] = this.toHsl(tile_hue * 256 / tile_chroma, tile_saturation / tile_buffer_size, tile_light / tile_buffer_size);
						}
					}
				}
			}
			for (int x = 1; x < 103; x++) {
				for (int z = 1; z < 103; z++) {
					if (underwater || /*Class143_Sub1.allLevelsAreVisible()*/ !ObjectManager.lowMem || (this.tileFlags[0][x][z] & 0x2) != 0 || (this.tileFlags[level][x][z] & 0x10) == 0 && this.getCollisionPlane(x, z, level) == ObjectManager.visibleLevels /* visible_level*/) {
						if (ObjectManager.maximumPlane > level) {
							ObjectManager.maximumPlane = level;
						}
						final int underlayId = this.underlays[level][x][z] & 0xff;
						final int overlayId = this.overlays[level][x][z] & 0xff;
						if (underlayId > 0 || overlayId > 0) {
							final int tileHeight = this.tileHeights[level][x][z];
							final int tileHeightNorth = this.tileHeights[level][x][z + 1];
							final int tileHeightEast = this.tileHeights[level][x + 1][z];
							final int tileHeightNorthEast = this.tileHeights[level][x + 1][z + 1];
							if (level > 0) {
								boolean occlude = true;
								if (underlayId == 0 && this.overlayShape[level][x][z] != 0) {
									occlude = false;
								}
								if (overlayId > 0 && !Flo.cache[underlayId - 1].shadowed) {//overlay_flo
									occlude = false;
								}
								if (occlude && tileHeight == tileHeightEast && tileHeightNorthEast == tileHeight && tileHeight == tileHeightNorth) {
									this.tile_culling_bitmap[level][x][z] |= 0x4;
								}
							}
							int i_66_;
							int underlayMinimapColor;
							if (underlayId <= 0) {
								i_66_ = -1;
								underlayMinimapColor = 0;
							} else {
								i_66_ = tileHsl[x][z];
								int i_68_ = (i_66_ & 0x7f);
								if (i_68_ < 0) {
									i_68_ = 0;
								} else if (i_68_ > 127) {
									i_68_ = 127;
								}
								final int i_69_ = (0xfc00 & i_66_) + (i_66_ & 0x380) + i_68_;
								underlayMinimapColor = Texture.anIntArray1482[ObjectManager.light(i_69_, 96)];
							}
							final int i_73_ = tileLighting[x][z];
							final int i_70_ = tileLighting[x + 1][z];
							final int i_72_ = tileLighting[x][z + 1];
							final int i_71_ = tileLighting[x + 1][z + 1];
							if (overlayId == 0) {
								worldController.addTile(level, x, z, 0, 0, -1, tileHeight, tileHeightEast, tileHeightNorthEast, tileHeightNorth,
										ObjectManager.light(i_66_, i_73_), ObjectManager.light(i_66_, i_70_), ObjectManager.light(i_66_, i_71_), ObjectManager.light(i_66_, i_72_), 0, 0, 0, 0, underlayMinimapColor, 0);
							} else {
								final int overlayShape_ = this.overlayShape[level][x][z] + 1;
								final byte overlayRotation = this.aByteArrayArrayArray148[level][x][z];
								final Flo overlayType = Flo.cache[overlayId - 1];//overlay_flo
								int textureId = overlayType.textureId;
								//TODO ADD this when I get the new texture def loaded. This refers to a != the first byte == 0 on the def.
								//if (textureId >= 0 && !Rasterizer.anInterface5_973.method15(textureId)) {
								//		textureId = -1;
								//}
								int overlayMinimapColor;
								int i_85_;
								if (textureId >= 0) {
									i_85_ = -1;
									overlayMinimapColor = Texture.anIntArray1482[ObjectManager.checkedLight550(Texture.method369(textureId), 96)];
								} else if (overlayType.hslColour == -1) {
									overlayMinimapColor = 0;
									i_85_ = -2;
								} else {
									i_85_ = overlayType.hslColour;
									int i_86_ = (0x7f & i_85_);
									if (i_86_ >= 0) {
										if (i_86_ > 127) {
											i_86_ = 127;
										}
									} else {
										i_86_ = 0;
									}
									final int i_87_ = (0xfc00 & i_85_) + (0x380 & i_85_) + i_86_;
									overlayMinimapColor = Texture.anIntArray1482[ObjectManager.checkedLight550(i_87_, 96)];
								}
								if (overlayType.anInt1198 >= 0) {
									final int i_88_ = overlayType.anInt1198;
									int i_89_ = (0x7f & i_88_);
									if (i_89_ >= 0) {
										if (i_89_ > 127) {
											i_89_ = 127;
										}
									} else {
										i_89_ = 0;
									}
									final int i_90_ = (0xfc00 & i_88_) + (0x380 & i_88_) + i_89_;
									overlayMinimapColor = Texture.anIntArray1482[ObjectManager.checkedLight550(i_90_, 96)];
								}
								worldController.addTile(level, x, z, overlayShape_, overlayRotation, textureId, tileHeight, tileHeightEast, tileHeightNorthEast, tileHeightNorth, ObjectManager.light(i_66_, i_73_), ObjectManager.light(i_66_, i_70_), ObjectManager.light(i_66_, i_71_), ObjectManager.light(i_66_, i_72_), ObjectManager.checkedLight550(i_85_, i_73_), ObjectManager.checkedLight550(i_85_, i_70_), ObjectManager.checkedLight550(i_85_, i_71_), ObjectManager.checkedLight550(i_85_, i_72_), underlayMinimapColor, overlayMinimapColor);
							}
						}
					}
				}
			}
		}

		this.method171b(false, worldController);
	}
	
	public final void addTiles(CollisionMap aclass11[], WorldController worldController, int render_settings) {
		if(false) {
			this.method1344(aclass11, worldController, false);
			return;
		}
		for (int j = 0; j < 4; j++) {
			for (int k = 0; k < 104; k++) {
				for (int i1 = 0; i1 < 104; i1++)
					if ((this.tileFlags[j][k][i1] & 1) == 1) {
						int k1 = j;
						if ((this.tileFlags[1][k][i1] & 2) == 2)
							k1--;
						if (k1 >= 0)
							aclass11[k1].block(k, i1);
					}

			}

		}

		for (int z = 0; z < 4; z++) {
			final int[][] is_6_ = new int[104][104];
			byte shading_[][] = this.shading[z];
			byte byte0 = 96;
			char c = '\u0300';
			byte byte1 = -50;
			byte byte2 = -10;
			byte byte3 = -50;
			int j3 = (int) Math.sqrt(byte1 * byte1 + byte2 * byte2 + byte3 * byte3);
			int l3 = c * j3 >> 8;
			for (int j4 = 1; j4 < this.length - 1; j4++) {
				for (int j5 = 1; j5 < this.width - 1; j5++) {
					int k6 = this.tileHeights[z][j5 + 1][j4] - this.tileHeights[z][j5 - 1][j4];
					int l7 = this.tileHeights[z][j5][j4 + 1] - this.tileHeights[z][j5][j4 - 1];
					int j9 = (int) Math.sqrt(k6 * k6 + 0x10000 + l7 * l7);
					int k12 = (k6 << 8) / j9;
					int l13 = 0x10000 / j9;
					int j15 = (l7 << 8) / j9;
					int j16 = byte0 + (byte1 * k12 + byte2 * l13 + byte3 * j15) / l3;
					int j17 = (shading_[j5 - 1][j4] >> 2) + (shading_[j5 + 1][j4] >> 3) + (shading_[j5][j4 - 1] >> 2)
							+ (shading_[j5][j4 + 1] >> 3) + (shading_[j5][j4] >> 1);
					this.tileLighting[j5][j4] = j16 - j17;
				}

			}
			
	/*
	// HD Shading method :) 
			byte shading_[][] = shading[z];
			final int xLight0 = (int) -50.0F;// -50.0F
			final int yLight0 = (int) -60.0F;// -60.0F
			final int zLight0 = (int) -50.0F;// -50.0F 
			final int i_18_ = (int)	Math.sqrt(xLight0 * xLight0 + yLight0 * yLight0 + zLight0 * zLight0);
			final int i_19_ = 1024 * i_18_ >> 8;
			for (int y = 1; y < 103; y++) {
				for (int x = 1; x < 103; x++) {
					int i_22_ = 96;
					final int i_23_ = -tileHeights[z][x][y + -1] + tileHeights[z][x][1 + y];
					final int i_24_ = tileHeights[z][1 + x][y] + -tileHeights[z][-1 + x][y];
					final int i_25_ = (int) Math.sqrt(65536 + i_24_ * i_24_ - -(i_23_ * i_23_));
					final int i_26_ = -65536 / i_25_;
					final int i_27_ = (i_23_ << 8) / i_25_;
					final int i_28_ = (i_24_ << 8) / i_25_;
					i_22_ += (zLight0 * i_27_ + xLight0 * i_28_ - -(yLight0 * i_26_)) / i_19_;
					final int i_29_ = (shading_[x][y] >> 1) + (shading_[x][y - -1] >> 3) + (shading_[x + 1][y] >> 3)
							+ (shading_[x + -1][y] >> 2) + (shading_[x][y - 1] >> 2);
					tileLighting[x][y] = -(int) (1.7F * i_29_) + i_22_;
				}
			}*/
			 

			for (int k5 = 0; k5 < this.length; k5++) {
				this.hue_buffer[k5] = 0;
				this.saturation_buffer[k5] = 0;
				this.lightness_buffer[k5] = 0;
				this.chroma_buffer[k5] = 0;
				this.buffer_size[k5] = 0;
			}

			for (int centreX = -5; centreX < this.width + 5; centreX++) {
				for (int i8 = 0; i8 < this.length; i8++) {
					int maxX = centreX + 5;
					if (maxX >= 0 && maxX < this.width) {
						int l12 = this.underlays[z][maxX][i8] & 0xff;
						if (l12 > 0) {
							Flo flo = Flo.cache[l12 - 1];
							this.hue_buffer[i8] += flo.weightedHue;
							this.saturation_buffer[i8] += flo.saturation;
							this.lightness_buffer[i8] += flo.luminance;
							this.chroma_buffer[i8] += flo.chroma;
							this.buffer_size[i8]++;
						}
					}
					int minX = centreX - 5;
					if (minX >= 0 && minX < this.width) {
						int i14 = this.underlays[z][minX][i8] & 0xff;
						if (i14 > 0) {
							Flo flo_1 = Flo.cache[i14 - 1];
							this.hue_buffer[i8] -= flo_1.weightedHue;
							this.saturation_buffer[i8] -= flo_1.saturation;
							this.lightness_buffer[i8] -= flo_1.luminance;
							this.chroma_buffer[i8] -= flo_1.chroma;
							this.buffer_size[i8]--;
						}
					}
				}

				if (centreX >= 1 && centreX < this.width - 1) {
					int tile_hue = 0;
					int tile_sat = 0;
					int tile_light = 0;
					int tile_chroma = 0;
					int tile_buffer_size = 0;
					for (int centreY = -5; centreY < this.length + 5; centreY++) {
						int maxY = centreY + 5;
						if (maxY >= 0 && maxY < this.length) {
							tile_hue += this.hue_buffer[maxY];
							tile_sat += this.saturation_buffer[maxY];
							tile_light += this.lightness_buffer[maxY];
							tile_chroma += this.chroma_buffer[maxY];
							tile_buffer_size += this.buffer_size[maxY];
						}
						int minY = centreY - 5;
						if (minY >= 0 && minY < this.length) {
							tile_hue -= this.hue_buffer[minY];
							tile_sat -= this.saturation_buffer[minY];
							tile_light -= this.lightness_buffer[minY];
							tile_chroma -= this.chroma_buffer[minY];
							tile_buffer_size -= this.buffer_size[minY];
						}
						if (centreY >= 1 && centreY < this.length - 1
								&& (!ObjectManager.lowMem || (this.tileFlags[0][centreX][centreY] & 2) != 0
										|| (this.tileFlags[z][centreX][centreY] & 0x10) == 0
												&& this.getCollisionPlane(centreY, z, centreX) == ObjectManager.visibleLevels)) {
							if (z < ObjectManager.maximumPlane)
								ObjectManager.maximumPlane = z;
							int underlay = this.underlays[z][centreX][centreY] & 0xff;
							int overlay = this.overlays[z][centreX][centreY] & 0xff;
							if (underlay > 0 || overlay > 0) {
								int centreHeight = this.tileHeights[z][centreX][centreY];
								int eastHeight = this.tileHeights[z][centreX + 1][centreY];
								int northEastHeight = this.tileHeights[z][centreX + 1][centreY + 1];
								int northHeight = this.tileHeights[z][centreX][centreY + 1];
								int centreLight = this.tileLighting[centreX][centreY];
								int eastLight = this.tileLighting[centreX + 1][centreY];
								int northEastLight = this.tileLighting[centreX + 1][centreY + 1];
								int northLight = this.tileLighting[centreX][centreY + 1];
								int underlay_hsl_real = -1;
								int underlay_hsl = -1;
								if (underlay > 0) {
									int hue = (tile_hue * 256) / tile_chroma;
									int saturation = tile_sat / tile_buffer_size;
									int luminance = tile_light / tile_buffer_size;
									underlay_hsl_real = this.toHsl(hue, saturation, luminance);
									// hue = hue + hueOffset & 0xff;
									// luminance += luminanceOffset;
									if (luminance < 0)
										luminance = 0;
									else if (luminance > 255)
										luminance = 255;
									underlay_hsl = this.toHsl(hue, saturation, luminance);
								}
								if (z > 0) {
									boolean underlay_hidden = true;
									if (underlay == 0 && this.overlayShape[z][centreX][centreY] != 0)
										underlay_hidden = false;
									if (overlay > 0 && !Flo.cache[overlay - 1].shadowed)
										underlay_hidden = false;
									if (underlay_hidden && centreHeight == eastHeight && centreHeight == northEastHeight
											&& centreHeight == northHeight)
										this.tile_culling_bitmap[z][centreX][centreY] |= 0x924;
								}
								int i22 = 0;
								if (underlay_hsl_real != -1)
									i22 = Texture.anIntArray1482[ObjectManager.light(underlay_hsl, 96)];
								if (overlay == 0) {
									worldController.addTile(z, centreX, centreY, 0, 0, -1, centreHeight, eastHeight,
											northEastHeight, northHeight, ObjectManager.light(underlay_hsl_real, centreLight),
											ObjectManager.light(underlay_hsl_real, eastLight),
											ObjectManager.light(underlay_hsl_real, northEastLight),
											ObjectManager.light(underlay_hsl_real, northLight), 0, 0, 0, 0, i22, 0);
								} else {
									int shape = this.overlayShape[z][centreX][centreY] + 1;
									byte rotation = this.aByteArrayArrayArray148[z][centreX][centreY];
									Flo flo_2 = Flo.cache[overlay - 1];
									int overlay_texture = flo_2.textureId;
									int overlay_hsl;
									int overlay_rgb;
									if (overlay_texture >= 0) {
										overlay_rgb = Texture.method369(overlay_texture);
										overlay_hsl = -1;
									} else if (flo_2.groundRgb == 0xff00ff) {
										overlay_rgb = 0;
										overlay_hsl = -2;
										overlay_texture = -1;
									} else {
										overlay_hsl = this.toHsl(flo_2.hue, flo_2.saturation, flo_2.luminance);
										overlay_rgb = Texture.anIntArray1482[this.checkedLight(flo_2.hslColour, 96)];
									}
									worldController.addTile(z, centreX, centreY, shape, rotation, overlay_texture, centreHeight,
											eastHeight, northEastHeight, northHeight,
											ObjectManager.light(underlay_hsl_real, centreLight), ObjectManager.light(underlay_hsl_real, eastLight),
											ObjectManager.light(underlay_hsl_real, northEastLight),
											ObjectManager.light(underlay_hsl_real, northLight), this.checkedLight(overlay_hsl, centreLight),
											this.checkedLight(overlay_hsl, eastLight), this.checkedLight(overlay_hsl, northEastLight),
											this.checkedLight(overlay_hsl, northLight), i22, overlay_rgb);
								}
							}
						}
					}
				}
			}
		}
		this.method171b(false, worldController);
	}

	private void method171b(boolean underwater, WorldController worldController) {
		for (int z = 0; z < 4; z++) {
			for (int j8 = 1; j8 < this.length - 1; j8++) {
				for (int i10 = 1; i10 < this.width - 1; i10++)
					worldController.setCollisionPlane(z, i10, j8, this.getCollisionPlane(j8, z, i10));

			}
		}
		worldController.method305(-10, -50, -50);
		if (!underwater) {
			for (int j1 = 0; j1 < this.width; j1++) {
				for (int l1 = 0; l1 < this.length; l1++)
					if ((this.tileFlags[1][j1][l1] & 2) == 2)
						worldController.method276(l1, j1);

			}
		}
//		if (!underwater) {
//			for (int i_104_ = 0; i_104_ < 4; i_104_++) {
//				for (int i_105_ = 0; i_105_ <= 104; i_105_++) {
//					for (int i_106_ = 0; i_106_ <= 104; i_106_++) {
//						if ((0x1 & tile_culling_bitmap[i_104_][i_106_][i_105_]) != 0) {
//							int i_107_ = i_105_;
//							int i_108_ = i_104_;
//							int i_109_ = i_105_;
//							for (/**/; i_107_ < 104; i_107_++) {
//								if ((tile_culling_bitmap[i_104_][i_106_][i_107_ + 1] & 0x1) == 0) {
//									break;
//								}
//							}
//							for (/**/; i_109_ > 0; i_109_--) {
//								if ((0x1 & tile_culling_bitmap[i_104_][i_106_][-1 + i_109_]) == 0) {
//									break;
//								}
//							}
//							while_131_: for (/**/; i_108_ > 0; i_108_--) {
//								for (int i_110_ = i_109_; i_110_ <= i_107_; i_110_++) {
//									if ((0x1 & tile_culling_bitmap[-1 + i_108_][i_106_][i_110_]) == 0) {
//										break while_131_;
//									}
//								}
//							}
//							int i_111_;
//							while_132_: for (i_111_ = i_104_; i_111_ < 3; i_111_++) {
//								for (int i_112_ = i_109_; i_112_ <= i_107_; i_112_++) {
//									if ((0x1 & tile_culling_bitmap[i_111_ - -1][i_106_][i_112_]) == 0) {
//										break while_132_;
//									}
//								}
//							}
//							final int i_113_ = (1 + -i_109_ + i_107_) * (i_111_ - -1 - i_108_);
//							if (i_113_ >= 8) {
//								final int i_115_ = tileHeights[i_111_][i_106_][i_109_] + -240;
//								final int i_116_ = tileHeights[i_108_][i_106_][i_109_];
//								AbstractTimer.method734(1, 128 * i_106_, 128 * i_106_, i_109_ * 128, 128 + 128 * i_107_, i_115_, i_116_);
//								for (int i_117_ = i_108_; i_111_ >= i_117_; i_117_++) {
//									for (int i_118_ = i_109_; i_107_ >= i_118_; i_118_++) {
//										tile_culling_bitmap[i_117_][i_106_][i_118_] = Class120_Sub12_Sub3.method1207(tile_culling_bitmap[i_117_][i_106_][i_118_], -2);
//									}
//								}
//							}
//						}
//						if ((tile_culling_bitmap[i_104_][i_106_][i_105_] & 0x2) != 0) {
//							int i_119_;
//							for (i_119_ = i_106_; i_119_ > 0 && (0x2 & tile_culling_bitmap[i_104_][-1 + i_119_][i_105_]) != 0; i_119_--) {
//								/* empty */
//							}
//							int i_120_;
//							for (i_120_ = i_106_; i_120_ < 104 && (tile_culling_bitmap[i_104_][i_120_ - -1][i_105_] & 0x2) != 0; i_120_++) {
//								/* empty */
//							}
//							int i_121_ = i_104_;
//							int i_122_ = i_104_;
//							while_133_: for (/**/; i_121_ > 0; i_121_--) {
//								for (int i_123_ = i_119_; i_120_ >= i_123_; i_123_++) {
//									if ((tile_culling_bitmap[-1 + i_121_][i_123_][i_105_] & 0x2) == 0) {
//										break while_133_;
//									}
//								}
//							}
//							while_134_: for (/**/; i_122_ < 3; i_122_++) {
//								for (int i_124_ = i_119_; i_124_ <= i_120_; i_124_++) {
//									if ((0x2 & tile_culling_bitmap[1 + i_122_][i_124_][i_105_]) == 0) {
//										break while_134_;
//									}
//								}
//							}
//							final int i_125_ = (1 + i_122_ - i_121_) * (1 + i_120_ - i_119_);
//							if (i_125_ >= 8) {
//								final int i_127_ = tileHeights[i_122_][i_119_][i_105_] + -240;
//								final int i_128_ = tileHeights[i_121_][i_119_][i_105_];
//								AbstractTimer.method734(2, i_119_ * 128, 128 * i_120_ + 128, 128 * i_105_, 128 * i_105_, i_127_, i_128_);
//								for (int i_129_ = i_121_; i_122_ >= i_129_; i_129_++) {
//									for (int i_130_ = i_119_; i_120_ >= i_130_; i_130_++) {
//										tile_culling_bitmap[i_129_][i_130_][i_105_] = Class120_Sub12_Sub3.method1207(tile_culling_bitmap[i_129_][i_130_][i_105_], -3);
//									}
//								}
//							}
//						}
//						if ((tile_culling_bitmap[i_104_][i_106_][i_105_] & 0x4) != 0) {
//							int i_131_ = i_106_;
//							int i_132_ = i_106_;
//							int i_133_;
//							for (i_133_ = i_105_; i_133_ > 0 && (0x4 & tile_culling_bitmap[i_104_][i_106_][-1 + i_133_]) != 0; i_133_--) {
//								/* empty */
//							}
//							int i_134_;
//							for (i_134_ = i_105_; i_134_ < 104; i_134_++) {
//								if ((tile_culling_bitmap[i_104_][i_106_][1 + i_134_] & 0x4) == 0) {
//									break;
//								}
//							}
//							while_135_: for (/**/; i_131_ > 0; i_131_--) {
//								for (int i_135_ = i_133_; i_135_ <= i_134_; i_135_++) {
//									if ((tile_culling_bitmap[i_104_][-1 + i_131_][i_135_] & 0x4) == 0) {
//										break while_135_;
//									}
//								}
//							}
//							while_136_: for (/**/; i_132_ < 104; i_132_++) {
//								for (int i_136_ = i_133_; i_134_ >= i_136_; i_136_++) {
//									if ((tile_culling_bitmap[i_104_][i_132_ - -1][i_136_] & 0x4) == 0) {
//										break while_136_;
//									}
//								}
//							}
//							if ((1 + -i_133_ + i_134_) * (1 + i_132_ + -i_131_) >= 4) {
//								final int i_137_ = tileHeights[i_104_][i_131_][i_133_];
//								AbstractTimer.method734(4, i_131_ * 128, 128 + i_132_ * 128, i_133_ * 128, 128 + 128 * i_134_, i_137_, i_137_);
//								for (int i_138_ = i_131_; i_132_ >= i_138_; i_138_++) {
//									for (int i_139_ = i_133_; i_139_ <= i_134_; i_139_++) {
//										tile_culling_bitmap[i_104_][i_138_][i_139_] = Class120_Sub12_Sub3.method1207(tile_culling_bitmap[i_104_][i_138_][i_139_], -5);
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
		
		int i2 = 1;
		int j2 = 2;
		int k2 = 4;
		for (int l2 = 0; l2 < 4; l2++) {
			if (l2 > 0) {
				i2 <<= 3;
				j2 <<= 3;
				k2 <<= 3;
			}
			for (int i3 = 0; i3 <= l2; i3++) {
				for (int k3 = 0; k3 <= this.length; k3++) {
					for (int i4 = 0; i4 <= this.width; i4++) {
						if ((this.tile_culling_bitmap[i3][i4][k3] & i2) != 0) {
							int k4 = k3;
							int l5 = k3;
							int i7 = i3;
							int k8 = i3;
							for (; k4 > 0 && (this.tile_culling_bitmap[i3][i4][k4 - 1] & i2) != 0; k4--)
								;
							for (; l5 < this.length && (this.tile_culling_bitmap[i3][i4][l5 + 1] & i2) != 0; l5++)
								;
							label0: for (; i7 > 0; i7--) {
								for (int j10 = k4; j10 <= l5; j10++)
									if ((this.tile_culling_bitmap[i7 - 1][i4][j10] & i2) == 0)
										break label0;

							}

							label1: for (; k8 < l2; k8++) {
								for (int k10 = k4; k10 <= l5; k10++)
									if ((this.tile_culling_bitmap[k8 + 1][i4][k10] & i2) == 0)
										break label1;

							}

							int l10 = ((k8 + 1) - i7) * ((l5 - k4) + 1);
							if (l10 >= 8) {
								char c1 = '\360';
								int k14 = this.tileHeights[k8][i4][k4] - c1;
								int l15 = this.tileHeights[i7][i4][k4];
								WorldController.method277(l2, i4 * 128, l15, i4 * 128, l5 * 128 + 128, k14, k4 * 128,
										1);
								for (int l16 = i7; l16 <= k8; l16++) {
									for (int l17 = k4; l17 <= l5; l17++)
										this.tile_culling_bitmap[l16][i4][l17] &= ~i2;

								}

							}
						}
						if ((this.tile_culling_bitmap[i3][i4][k3] & j2) != 0) {
							int l4 = i4;
							int i6 = i4;
							int j7 = i3;
							int l8 = i3;
							for (; l4 > 0 && (this.tile_culling_bitmap[i3][l4 - 1][k3] & j2) != 0; l4--)
								;
							for (; i6 < this.width && (this.tile_culling_bitmap[i3][i6 + 1][k3] & j2) != 0; i6++)
								;
							label2: for (; j7 > 0; j7--) {
								for (int i11 = l4; i11 <= i6; i11++)
									if ((this.tile_culling_bitmap[j7 - 1][i11][k3] & j2) == 0)
										break label2;

							}

							label3: for (; l8 < l2; l8++) {
								for (int j11 = l4; j11 <= i6; j11++)
									if ((this.tile_culling_bitmap[l8 + 1][j11][k3] & j2) == 0)
										break label3;

							}

							int k11 = ((l8 + 1) - j7) * ((i6 - l4) + 1);
							if (k11 >= 8) {
								char c2 = '\360';
								int l14 = this.tileHeights[l8][l4][k3] - c2;
								int i16 = this.tileHeights[j7][l4][k3];
								WorldController.method277(l2, l4 * 128, i16, i6 * 128 + 128, k3 * 128, l14, k3 * 128,
										2);
								for (int i17 = j7; i17 <= l8; i17++) {
									for (int i18 = l4; i18 <= i6; i18++)
										this.tile_culling_bitmap[i17][i18][k3] &= ~j2;

								}

							}
						}
						if ((this.tile_culling_bitmap[i3][i4][k3] & k2) != 0) {
							int i5 = i4;
							int j6 = i4;
							int k7 = k3;
							int i9 = k3;
							for (; k7 > 0 && (this.tile_culling_bitmap[i3][i4][k7 - 1] & k2) != 0; k7--)
								;
							for (; i9 < this.length && (this.tile_culling_bitmap[i3][i4][i9 + 1] & k2) != 0; i9++)
								;
							label4: for (; i5 > 0; i5--) {
								for (int l11 = k7; l11 <= i9; l11++)
									if ((this.tile_culling_bitmap[i3][i5 - 1][l11] & k2) == 0)
										break label4;

							}

							label5: for (; j6 < this.width; j6++) {
								for (int i12 = k7; i12 <= i9; i12++)
									if ((this.tile_culling_bitmap[i3][j6 + 1][i12] & k2) == 0)
										break label5;

							}

							if (((j6 - i5) + 1) * ((i9 - k7) + 1) >= 4) {
								int j12 = this.tileHeights[i3][i5][k7];
								WorldController.method277(l2, i5 * 128, j12, j6 * 128 + 128, i9 * 128 + 128, j12,
										k7 * 128, 4);
								for (int k13 = i5; k13 <= j6; k13++) {
									for (int i15 = k7; i15 <= i9; i15++)
										this.tile_culling_bitmap[i3][k13][i15] &= ~k2;

								}

							}
						}
					}

				}

			}

		}
	}
	
    private static int calculateHeight(int i, int j)
    {
        int k = (ObjectManager.method176(i + 45365, j + 0x16713, 4) - 128) + (ObjectManager.method176(i + 10294, j + 37821, 2) - 128 >> 1) + (ObjectManager.method176(i, j, 1) - 128 >> 2);
        k = (int)(k * 0.29999999999999999D) + 35;
        if(k < 10)
            k = 10;
        else
        if(k > 60)
            k = 60;
        return k;
    }

    public static void method173(Buffer buffer, OnDemandFetcher class42_sub1)
    {
label0:
        {
            int i = -1;
            do
            {
                int j = buffer.readUSmart();
                if(j == 0)
                    break label0;
                i += j;
                ObjectDef class46 = ObjectDef.forID(i);
                class46.method574(class42_sub1);
                do
                {
                    int k = buffer.readUSmart();
                    if(k == 0)
                        break;
                    buffer.readUByte();
                } while(true);
            } while(true);
        }
    }

    public final void clearRegion(int i, int j, int l, int i1)
    {
        for(int j1 = i; j1 <= i + j; j1++)
        {
            for(int k1 = i1; k1 <= i1 + l; k1++)
                if(k1 >= 0 && k1 < this.width && j1 >= 0 && j1 < this.length)
                {
                    this.shading[0][k1][j1] = 127;
                    if(k1 == i1 && k1 > 0)
                        this.tileHeights[0][k1][j1] = this.tileHeights[0][k1 - 1][j1];
                    if(k1 == i1 + l && k1 < this.width - 1)
                        this.tileHeights[0][k1][j1] = this.tileHeights[0][k1 + 1][j1];
                    if(j1 == i && j1 > 0)
                        this.tileHeights[0][k1][j1] = this.tileHeights[0][k1][j1 - 1];
                    if(j1 == i + j && j1 < this.length - 1)
                        this.tileHeights[0][k1][j1] = this.tileHeights[0][k1][j1 + 1];
                }

        }
    }

    private void method175(int i, WorldController worldController, CollisionMap class11, int j, int k, int l, int i1,
                                 int j1)
    {
        if(ObjectManager.lowMem && (this.tileFlags[0][l][i] & 2) == 0)
        {
            if((this.tileFlags[k][l][i] & 0x10) != 0)
                return;
            if(this.getCollisionPlane(i, k, l) != ObjectManager.visibleLevels)
                return;
        }
        if(k < ObjectManager.maximumPlane)
            ObjectManager.maximumPlane = k;
        int k1 = this.tileHeights[k][l][i];
        int l1 = this.tileHeights[k][l + 1][i];
        int i2 = this.tileHeights[k][l + 1][i + 1];
        int j2 = this.tileHeights[k][l][i + 1];
        int k2 = k1 + l1 + i2 + j2 >> 2;
        ObjectDef class46 = ObjectDef.forID(i1);
        int l2 = l + (i << 7) + (i1 << 14) + 0x40000000;
        if(!class46.hasActions)
            l2 += 0x80000000;
        byte byte0 = (byte)((j1 << 6) + j);
        if(j == 22)
        {
            if(ObjectManager.lowMem && !class46.hasActions && !class46.aBoolean736)
                return;
            Object obj;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj = class46.method578(22, j1, k1, l1, i2, j2, -1);
            else
                obj = new Animable_Sub5(i1, j1, 22, l1, i2, k1, j2, class46.anInt781, true);
            worldController.method280(k, k2, i, ((Animable) (obj)), byte0, l2, l);
            if(class46.aBoolean767 && class46.hasActions && class11 != null)
                class11.block(l, i);
            return;
        }
        if(j == 10 || j == 11)
        {
            Object obj1;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj1 = class46.method578(10, j1, k1, l1, i2, j2, -1);
            else
                obj1 = new Animable_Sub5(i1, j1, 10, l1, i2, k1, j2, class46.anInt781, true);
            if(obj1 != null)
            {
                int i5 = 0;
                if(j == 11)
                    i5 += 256;
                int j4;
                int l4;
                if(j1 == 1 || j1 == 3)
                {
                    j4 = class46.anInt761;
                    l4 = class46.anInt744;
                } else
                {
                    j4 = class46.anInt744;
                    l4 = class46.anInt761;
                }
                if(worldController.method284(l2, byte0, k2, l4, ((Animable) (obj1)), j4, k, i5, i, l) && class46.aBoolean779)
                {
                    Model model;
                    if(obj1 instanceof Model)
                        model = (Model)obj1;
                    else
                        model = class46.method578(10, j1, k1, l1, i2, j2, -1);
                    if(model != null)
                    {
                        for(int j5 = 0; j5 <= j4; j5++)
                        {
                            for(int k5 = 0; k5 <= l4; k5++)
                            {
                                int l5 = model.anInt1650 / 4;
                                if(l5 > 30)
                                    l5 = 30;
                                if(l5 > this.shading[k][l + j5][i + k5])
                                    this.shading[k][l + j5][i + k5] = (byte)l5;
                            }

                        }

                    }
                }
            }
            if(class46.aBoolean767 && class11 != null)
                class11.setLoc(l, i, class46.anInt744, class46.anInt761, j1, class46.aBoolean757);
            return;
        }
        if(j >= 12)
        {
            Object obj2;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj2 = class46.method578(j, j1, k1, l1, i2, j2, -1);
            else
                obj2 = new Animable_Sub5(i1, j1, j, l1, i2, k1, j2, class46.anInt781, true);
            worldController.method284(l2, byte0, k2, 1, ((Animable) (obj2)), 1, k, 0, i, l);
            if(j >= 12 && j <= 17 && j != 13 && k > 0)
                this.tile_culling_bitmap[k][l][i] |= 0x924;
            if(class46.aBoolean767 && class11 != null)
                class11.setLoc(l, i, class46.anInt744, class46.anInt761, j1, class46.aBoolean757);
            return;
        }
        if(j == 0)
        {
            Object obj3;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj3 = class46.method578(0, j1, k1, l1, i2, j2, -1);
            else
                obj3 = new Animable_Sub5(i1, j1, 0, l1, i2, k1, j2, class46.anInt781, true);
            worldController.method282(ObjectManager.anIntArray152[j1], ((Animable) (obj3)), l2, i, byte0, l, null, k2, 0, k);
            if(j1 == 0)
            {
                if(class46.aBoolean779)
                {
                    this.shading[k][l][i] = 50;
                    this.shading[k][l][i + 1] = 50;
                }
                if(class46.aBoolean764)
                    this.tile_culling_bitmap[k][l][i] |= 0x249;
            } else
            if(j1 == 1)
            {
                if(class46.aBoolean779)
                {
                    this.shading[k][l][i + 1] = 50;
                    this.shading[k][l + 1][i + 1] = 50;
                }
                if(class46.aBoolean764)
                    this.tile_culling_bitmap[k][l][i + 1] |= 0x492;
            } else
            if(j1 == 2)
            {
                if(class46.aBoolean779)
                {
                    this.shading[k][l + 1][i] = 50;
                    this.shading[k][l + 1][i + 1] = 50;
                }
                if(class46.aBoolean764)
                    this.tile_culling_bitmap[k][l + 1][i] |= 0x249;
            } else
            if(j1 == 3)
            {
                if(class46.aBoolean779)
                {
                    this.shading[k][l][i] = 50;
                    this.shading[k][l + 1][i] = 50;
                }
                if(class46.aBoolean764)
                    this.tile_culling_bitmap[k][l][i] |= 0x492;
            }
            if(class46.aBoolean767 && class11 != null)
                class11.setWall(l, i, j1, j, class46.aBoolean757);
            if(class46.anInt775 != 16)
                worldController.method290(i, class46.anInt775, l, k);
            return;
        }
        if(j == 1)
        {
            Object obj4;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj4 = class46.method578(1, j1, k1, l1, i2, j2, -1);
            else
                obj4 = new Animable_Sub5(i1, j1, 1, l1, i2, k1, j2, class46.anInt781, true);
            worldController.method282(ObjectManager.anIntArray140[j1], ((Animable) (obj4)), l2, i, byte0, l, null, k2, 0, k);
            if(class46.aBoolean779)
                if(j1 == 0)
                    this.shading[k][l][i + 1] = 50;
                else
                if(j1 == 1)
                    this.shading[k][l + 1][i + 1] = 50;
                else
                if(j1 == 2)
                    this.shading[k][l + 1][i] = 50;
                else
                if(j1 == 3)
                    this.shading[k][l][i] = 50;
            if(class46.aBoolean767 && class11 != null)
                class11.setWall(l, i, j1, j, class46.aBoolean757);
            return;
        }
        if(j == 2)
        {
            int i3 = j1 + 1 & 3;
            Object obj11;
            Object obj12;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
            {
                obj11 = class46.method578(2, 4 + j1, k1, l1, i2, j2, -1);
                obj12 = class46.method578(2, i3, k1, l1, i2, j2, -1);
            } else
            {
                obj11 = new Animable_Sub5(i1, 4 + j1, 2, l1, i2, k1, j2, class46.anInt781, true);
                obj12 = new Animable_Sub5(i1, i3, 2, l1, i2, k1, j2, class46.anInt781, true);
            }
            worldController.method282(ObjectManager.anIntArray152[j1], ((Animable) (obj11)), l2, i, byte0, l, ((Animable) (obj12)), k2, ObjectManager.anIntArray152[i3], k);
            if(class46.aBoolean764)
                if(j1 == 0)
                {
                    this.tile_culling_bitmap[k][l][i] |= 0x249;
                    this.tile_culling_bitmap[k][l][i + 1] |= 0x492;
                } else
                if(j1 == 1)
                {
                    this.tile_culling_bitmap[k][l][i + 1] |= 0x492;
                    this.tile_culling_bitmap[k][l + 1][i] |= 0x249;
                } else
                if(j1 == 2)
                {
                    this.tile_culling_bitmap[k][l + 1][i] |= 0x249;
                    this.tile_culling_bitmap[k][l][i] |= 0x492;
                } else
                if(j1 == 3)
                {
                    this.tile_culling_bitmap[k][l][i] |= 0x492;
                    this.tile_culling_bitmap[k][l][i] |= 0x249;
                }
            if(class46.aBoolean767 && class11 != null)
                class11.setWall(l, i, j1, j, class46.aBoolean757);
            if(class46.anInt775 != 16)
                worldController.method290(i, class46.anInt775, l, k);
            return;
        }
        if(j == 3)
        {
            Object obj5;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj5 = class46.method578(3, j1, k1, l1, i2, j2, -1);
            else
                obj5 = new Animable_Sub5(i1, j1, 3, l1, i2, k1, j2, class46.anInt781, true);
            worldController.method282(ObjectManager.anIntArray140[j1], ((Animable) (obj5)), l2, i, byte0, l, null, k2, 0, k);
            if(class46.aBoolean779)
                if(j1 == 0)
                    this.shading[k][l][i + 1] = 50;
                else
                if(j1 == 1)
                    this.shading[k][l + 1][i + 1] = 50;
                else
                if(j1 == 2)
                    this.shading[k][l + 1][i] = 50;
                else
                if(j1 == 3)
                    this.shading[k][l][i] = 50;
            if(class46.aBoolean767 && class11 != null)
                class11.setWall(l, i, j1, j, class46.aBoolean757);
            return;
        }
        if(j == 9)
        {
            Object obj6;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj6 = class46.method578(j, j1, k1, l1, i2, j2, -1);
            else
                obj6 = new Animable_Sub5(i1, j1, j, l1, i2, k1, j2, class46.anInt781, true);
            worldController.method284(l2, byte0, k2, 1, ((Animable) (obj6)), 1, k, 0, i, l);
            if(class46.aBoolean767 && class11 != null)
                class11.setLoc(l, i, class46.anInt744, class46.anInt761, j1, class46.aBoolean757);
            return;
        }
        if(class46.aBoolean762)
            if(j1 == 1)
            {
                int j3 = j2;
                j2 = i2;
                i2 = l1;
                l1 = k1;
                k1 = j3;
            } else
            if(j1 == 2)
            {
                int k3 = j2;
                j2 = l1;
                l1 = k3;
                k3 = i2;
                i2 = k1;
                k1 = k3;
            } else
            if(j1 == 3)
            {
                int l3 = j2;
                j2 = k1;
                k1 = l1;
                l1 = i2;
                i2 = l3;
            }
        if(j == 4)
        {
            Object obj7;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj7 = class46.method578(4, 0, k1, l1, i2, j2, -1);
            else
                obj7 = new Animable_Sub5(i1, 0, 4, l1, i2, k1, j2, class46.anInt781, true);
            worldController.method283(l2, i, j1 * 512, k, 0, k2, ((Animable) (obj7)), l, byte0, 0, ObjectManager.anIntArray152[j1]);
            return;
        }
        if(j == 5)
        {
            int i4 = 16;
            int k4 = worldController.method300(k, l, i);
            if(k4 > 0)
                i4 = ObjectDef.forID(k4 >> 14 & 0x7fff).anInt775;
            Object obj13;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj13 = class46.method578(4, 0, k1, l1, i2, j2, -1);
            else
                obj13 = new Animable_Sub5(i1, 0, 4, l1, i2, k1, j2, class46.anInt781, true);
            worldController.method283(l2, i, j1 * 512, k, ObjectManager.anIntArray137[j1] * i4, k2, ((Animable) (obj13)), l, byte0, ObjectManager.anIntArray144[j1] * i4, ObjectManager.anIntArray152[j1]);
            return;
        }
        if(j == 6)
        {
            Object obj8;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj8 = class46.method578(4, 0, k1, l1, i2, j2, -1);
            else
                obj8 = new Animable_Sub5(i1, 0, 4, l1, i2, k1, j2, class46.anInt781, true);
            worldController.method283(l2, i, j1, k, 0, k2, ((Animable) (obj8)), l, byte0, 0, 256);
            return;
        }
        if(j == 7)
        {
            Object obj9;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj9 = class46.method578(4, 0, k1, l1, i2, j2, -1);
            else
                obj9 = new Animable_Sub5(i1, 0, 4, l1, i2, k1, j2, class46.anInt781, true);
            worldController.method283(l2, i, j1, k, 0, k2, ((Animable) (obj9)), l, byte0, 0, 512);
            return;
        }
        if(j == 8)
        {
            Object obj10;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj10 = class46.method578(4, 0, k1, l1, i2, j2, -1);
            else
                obj10 = new Animable_Sub5(i1, 0, 4, l1, i2, k1, j2, class46.anInt781, true);
            worldController.method283(l2, i, j1, k, 0, k2, ((Animable) (obj10)), l, byte0, 0, 768);
        }
    }

    private static int method176(int i, int j, int k)
    {
        int l = i / k;
        int i1 = i & k - 1;
        int j1 = j / k;
        int k1 = j & k - 1;
        int l1 = ObjectManager.method186(l, j1);
        int i2 = ObjectManager.method186(l + 1, j1);
        int j2 = ObjectManager.method186(l, j1 + 1);
        int k2 = ObjectManager.method186(l + 1, j1 + 1);
        int l2 = ObjectManager.method184(l1, i2, i1, k);
        int i3 = ObjectManager.method184(j2, k2, i1, k);
        return ObjectManager.method184(l2, i3, k1, k);
    }

    private int toHsl(int h, int s, int l)
    {
        if(l > 179)
            s /= 2;
        if(l > 192)
            s /= 2;
        if(l > 217)
            s /= 2;
        if(l > 243)
            s /= 2;
        return (h / 4 << 10) + (s / 32 << 7) + l / 2;
    }

    public static boolean method178(int i, int j)
    {
        ObjectDef class46 = ObjectDef.forID(i);
        if(j == 11)
            j = 10;
        if(j >= 5 && j <= 8)
            j = 4;
        return class46.method577(j);
    }

    public final void decodeConstructedMapData(int i, int j, CollisionMap aclass11[], int l, int i1, byte abyte0[],
                                int j1, int k1, int l1)
    {
        for(int x = 0; x < 8; x++)
        {
            for(int y = 0; y < 8; y++)
                if(l + x > 0 && l + x < 103 && l1 + y > 0 && l1 + y < 103)
                    aclass11[k1].flags[l + x][l1 + y] &= 0xfeffffff;

        }
        Buffer buffer = new Buffer(abyte0);
        for(int l2 = 0; l2 < 4; l2++)
        {
            for(int i3 = 0; i3 < 64; i3++)
            {
                for(int j3 = 0; j3 < 64; j3++)
                    if(l2 == i && i3 >= i1 && i3 < i1 + 8 && j3 >= j1 && j3 < j1 + 8)
                        this.method181(l1 + Class4.method156(j3 & 7, j, i3 & 7), 0, buffer, l + Class4.method155(j, j3 & 7, i3 & 7), k1, j, 0);
                    else
                        this.method181(-1, 0, buffer, -1, 0, 0, 0);

            }

        }

    }

    public final void decodeRegionMapData(byte abyte0[], int i, int j, int k, int l, CollisionMap aclass11[])
    {
        for(int i1 = 0; i1 < 4; i1++)
        {
            for(int j1 = 0; j1 < 64; j1++)
            {
                for(int k1 = 0; k1 < 64; k1++)
                    if(j + j1 > 0 && j + j1 < 103 && i + k1 > 0 && i + k1 < 103)
                        aclass11[i1].flags[j + j1][i + k1] &= 0xfeffffff;

            }

        }

        Buffer buffer = new Buffer(abyte0);
        for(int l1 = 0; l1 < 4; l1++)
        {
            for(int i2 = 0; i2 < 64; i2++)
            {
                for(int j2 = 0; j2 < 64; j2++) {
                    this.method181(j2 + i, l, buffer, i2 + j, l1, 0, k);
                  //  this.loadGlest(i2, j2);
                }

            }

        }
        for(int x = 0; x < 103; x++)
        {
            for(int y = 0; y < 103; y++) {
            	tileHeights[0][x][y] = this.loadGlest(x, y);
          //      System.out.println("manly");
            	this.underlays[0][x][y] = surfaces[x][y];
            	System.err.println(surfaces[x][y] + 0);
              	this.overlays[0][x][y] = glest2over[surfaces[x][y]];
                this.overlayShape[0][x][y] = 0;
                this.aByteArrayArrayArray148[0][x][y] = 0;
            }

        }

    }

    /*
Surface 1 - Grass
Surface 2 - Secondary Grass
Surface 3 - Road
Surface 4 - Stone
Surface 5 - Custom
     */
    private byte [] glest2under = { 10, 28, 47, 9, (byte) 173 };
    private byte [] glest2over = { 10, 48, 99,  (byte) /*45*/105 , (byte) 173 };
    boolean loaded = false;
    float[][] heights;
    byte[][] surfaces;
    byte[][] objects;
    private int loadGlest(int x, int y) {
    	if(!loaded) {
    		//	int size = 10;
    			ByteBuffer b = ByteBuffer.wrap(FileOperations.ReadFile("Badwater.gbm"));
    			b.order(ByteOrder.LITTLE_ENDIAN);
    			System.out.println(b.getInt());//version
    			int maxPlayers = b.getInt();
    			System.out.println(maxPlayers);//maxplayers
    			int width = b.getInt();
    			System.out.println(width);//width
    			int height = b.getInt();
    			System.out.println(height);//height
    			System.out.println(b.getInt());//altfactr
    			System.out.println(b.getInt());//waterlevel
    			for(int i = 0; i<128; i++)
    				b.get();//title
    			for(int i = 0; i<128; i++)
    				b.get();//author
    			for(int i = 0; i<256; i++)
    				b.get();//description
    			for(int i = 0; i < maxPlayers; i++) {
    				System.out.println("x" +b.getInt());//altfactr
    				System.out.println("y" +b.getInt());//waterlevel
    			}
    			heights = new float[width][height];
    			
    			for (int j = 0; j < height; ++j)
    			{
    				for (int i = 0; i < width; ++i)
    				{
    					heights[i][j] = b.getFloat();
    				//	System.out.println(heights[i][j]);
    				}
    			}
    			
    			surfaces = new byte[width][height];

    			for (int j = 0; j < height; ++j)
    			{
    				for (int w = 0; w < width; ++w)
    				{
    					surfaces[w][j] = b.get();
    				}
    			}

    			objects = new byte[width][height];

    			for (int j = 0; j < height; ++j)
    			{
    				for (int i = 0; i < width; ++i)
    				{
    					objects[i][j] = b.get();
    				/*	if (obj <= 10)
    					{
    						cells[i][j].object = obj;
    					}
    					else
    					{
    						cells[i][j].resource = obj - 10;
    					}*/
    				}
    			}
    		loaded = true;
    	}
    /*	tileHeights[0][x][y] = (int) (heights[x][y] * -50 );
    	System.out.println(tileHeights[0][x][y]);
        this.tileFlags[0][x][y] = 0;
        this.underlays[0][x][y] = 34;
        this.underlays[0][x][y] = 42;
        this.overlayShape[0][x][y] = 0;*/
        return (int) (heights[x][y] * -10 );
    }
    
    private void method181(int y, int regionY, Buffer buffer, int x, int z, int i1,
                                 int regionX)
    {
    //	System.out.println(regionX + "-" + regionY);
    //	if(regionX == 3040 && regionY == 3456) {
    //		loadGlest(x, y);
    //		return;
    //	}
        if(x >= 0 && x < 104 && y >= 0 && y < 104)
        {
            this.tileFlags[z][x][y] = 0;
            do
            {
                int type = buffer.readUByte();
                if(type == 0)
                    if(z == 0)
                    {
              //          this.tileHeights[0][x][y] = -ObjectManager.calculateHeight(0xe3b7b + x + regionX, 0x87cce + y + regionY) * 8;
               //     	System.out.println("1 " +tileHeights[z][x][y]);
                        return;
                    } else
                    {
                //        this.tileHeights[z][x][y] = this.tileHeights[z - 1][x][y] - 240;
                //        	System.out.println("2 " +tileHeights[z][x][y]);
                        return;
                    }
                if(type == 1)
                {
                    int height = buffer.readUByte();
                    if(height == 1)
                        height = 0;
                    if(z == 0)
                    {
                   //     this.tileHeights[0][x][y] = -height * 8;
                  //   	System.out.println("3 " +tileHeights[z][x][y]);
                        return;
                    } else
                    {
                  //      this.tileHeights[z][x][y] = this.tileHeights[z - 1][x][y] - height * 8;
                   //  	System.out.println("4 " + tileHeights[z][x][y]);
                        return;
                    }
                }
                if(type <= 49)
                {
                    this.overlays[z][x][y] = buffer.readByte();
                    this.overlayShape[z][x][y] = (byte)((type - 2) / 4);
                    this.aByteArrayArrayArray148[z][x][y] = (byte)((type - 2) + i1 & 3);
                } else
                if(type <= 81)
                    this.tileFlags[z][x][y] = (byte)(type - 49);
                else
                    this.underlays[z][x][y] = (byte)(type - 81);
            } while(true);
        }
        
        do
        {
            int i2 = buffer.readUByte();
            if(i2 == 0)
                break;
            if(i2 == 1)
            {
                buffer.readUByte();
                return;
            }
            if(i2 <= 49)
                buffer.readUByte();
        } while(true);
    }

    private int getCollisionPlane(int i, int j, int k)
    {
        if((this.tileFlags[j][k][i] & 8) != 0)
            return 0;
        if(j > 0 && (this.tileFlags[1][k][i] & 2) != 0)
            return j - 1;
        else
            return j;
    }

    public final void decodeConstructedLandscapes(CollisionMap aclass11[], WorldController worldController, int i, int j, int k, int l,
                                byte abyte0[], int i1, int j1, int k1)
    {
label0:
        {
            Buffer buffer = new Buffer(abyte0);
            int l1 = -1;
            do
            {
                int i2 = buffer.readUSmart();
                if(i2 == 0)
                    break label0;
                l1 += i2;
                int j2 = 0;
                do
                {
                    int k2 = buffer.readUSmart();
                    if(k2 == 0)
                        break;
                    j2 += k2 - 1;
                    int l2 = j2 & 0x3f;
                    int i3 = j2 >> 6 & 0x3f;
                    int j3 = j2 >> 12;
                    int k3 = buffer.readUByte();
                    int l3 = k3 >> 2;
                    int i4 = k3 & 3;
                    if(j3 == i && i3 >= i1 && i3 < i1 + 8 && l2 >= k && l2 < k + 8)
                    {
                        ObjectDef class46 = ObjectDef.forID(l1);
                        int j4 = j + Class4.method157(j1, class46.anInt761, i3 & 7, l2 & 7, class46.anInt744);
                        int k4 = k1 + Class4.method158(l2 & 7, class46.anInt761, j1, class46.anInt744, i3 & 7);
                        if(j4 > 0 && k4 > 0 && j4 < 103 && k4 < 103)
                        {
                            int l4 = j3;
                            if((this.tileFlags[1][j4][k4] & 2) == 2)
                                l4--;
                            CollisionMap class11 = null;
                            if(l4 >= 0)
                                class11 = aclass11[l4];
                            this.method175(k4, worldController, class11, l3, l, j4, l1, i4 + j1 & 3);
                        }
                    }
                } while(true);
            } while(true);
        }
    }

    private static int method184(int i, int j, int k, int l)
    {
        int i1 = 0x10000 - Texture.anIntArray1471[(k * 1024) / l] >> 1;
        return (i * (0x10000 - i1) >> 16) + (j * i1 >> 16);
    }

	private int checkedLight(int colour, int light) {
		if (colour == -2)
			return 0xbc614e;
		if (colour == -1) {
			if (light < 0)
				light = 0;
			else if (light > 127)
				light = 127;
			light = 127 - light;
			return light;
		}
		light = (light * (colour & 0x7f)) / 128;
		if (light < 2)
			light = 2;
		else if (light > 126)
			light = 126;
		return (colour & 0xff80) + light;
	}

	static final int checkedLight550(final int colour, int light) {
		if (colour == -2) {
			return 12345678;
		}
		if (colour == -1) {
			if (light < 2) {
				light = 2;
			} else if (light > 126) {
				light = 126;
			}
			return light;
		}
		light = light * (0x7f & colour) >> 7;
		if (light < 2) {
			light = 2;
		} else if (light > 126) {
			light = 126;
		}
		return (colour & 0xff80) + light;
	}
	
    private static int method186(int i, int j)
    {
        int k = ObjectManager.method170(i - 1, j - 1) + ObjectManager.method170(i + 1, j - 1) + ObjectManager.method170(i - 1, j + 1) + ObjectManager.method170(i + 1, j + 1);
        int l = ObjectManager.method170(i - 1, j) + ObjectManager.method170(i + 1, j) + ObjectManager.method170(i, j - 1) + ObjectManager.method170(i, j + 1);
        int i1 = ObjectManager.method170(i, j);
        return k / 16 + l / 8 + i1 / 4;
    }

    private static int light(int colour, int light)
    {
        if(colour == -1)
            return 0xbc614e;
        light = (light * (colour & 0x7f)) / 128;
        if(light < 2)
            light = 2;
        else
        if(light > 126)
            light = 126;
        return (colour & 0xff80) + light;
    }

	
	private static final int light550(final int color, int light) {
		if (color == -1) {
			return 12345678;
		}
		light = (color & 0x7f) * light >> 7;
		if (light >= 2) {
			if (light > 126) {
				light = 126;
			}
		} else {
			light = 2;
		}
		return light + (color & 0xff80);
	}
	
    public static void method188(WorldController worldController, int i, int j, int k, int l, CollisionMap class11, int ai[][][], int i1,
                                 int j1, int k1)
    {
        int l1 = ai[l][i1][j];
        int i2 = ai[l][i1 + 1][j];
        int j2 = ai[l][i1 + 1][j + 1];
        int k2 = ai[l][i1][j + 1];
        int l2 = l1 + i2 + j2 + k2 >> 2;
        ObjectDef class46 = ObjectDef.forID(j1);
        int i3 = i1 + (j << 7) + (j1 << 14) + 0x40000000;
        if(!class46.hasActions)
            i3 += 0x80000000;
        byte byte1 = (byte)((i << 6) + k);
        if(k == 22)
        {
            Object obj;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj = class46.method578(22, i, l1, i2, j2, k2, -1);
            else
                obj = new Animable_Sub5(j1, i, 22, i2, j2, l1, k2, class46.anInt781, true);
            worldController.method280(k1, l2, j, ((Animable) (obj)), byte1, i3, i1);
            if(class46.aBoolean767 && class46.hasActions)
                class11.block(i1, j);
            return;
        }
        if(k == 10 || k == 11)
        {
            Object obj1;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj1 = class46.method578(10, i, l1, i2, j2, k2, -1);
            else
                obj1 = new Animable_Sub5(j1, i, 10, i2, j2, l1, k2, class46.anInt781, true);
            if(obj1 != null)
            {
                int j5 = 0;
                if(k == 11)
                    j5 += 256;
                int k4;
                int i5;
                if(i == 1 || i == 3)
                {
                    k4 = class46.anInt761;
                    i5 = class46.anInt744;
                } else
                {
                    k4 = class46.anInt744;
                    i5 = class46.anInt761;
                }
                worldController.method284(i3, byte1, l2, i5, ((Animable) (obj1)), k4, k1, j5, j, i1);
            }
            if(class46.aBoolean767)
                class11.setLoc(i1, j, class46.anInt744, class46.anInt761, i, class46.aBoolean757);
            return;
        }
        if(k >= 12)
        {
            Object obj2;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj2 = class46.method578(k, i, l1, i2, j2, k2, -1);
            else
                obj2 = new Animable_Sub5(j1, i, k, i2, j2, l1, k2, class46.anInt781, true);
            worldController.method284(i3, byte1, l2, 1, ((Animable) (obj2)), 1, k1, 0, j, i1);
            if(class46.aBoolean767)
                class11.setLoc(i1, j, class46.anInt744, class46.anInt761, i, class46.aBoolean757);
            return;
        }
        if(k == 0)
        {
            Object obj3;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj3 = class46.method578(0, i, l1, i2, j2, k2, -1);
            else
                obj3 = new Animable_Sub5(j1, i, 0, i2, j2, l1, k2, class46.anInt781, true);
            worldController.method282(ObjectManager.anIntArray152[i], ((Animable) (obj3)), i3, j, byte1, i1, null, l2, 0, k1);
            if(class46.aBoolean767)
                class11.setWall(i1, j, i, k, class46.aBoolean757);
            return;
        }
        if(k == 1)
        {
            Object obj4;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj4 = class46.method578(1, i, l1, i2, j2, k2, -1);
            else
                obj4 = new Animable_Sub5(j1, i, 1, i2, j2, l1, k2, class46.anInt781, true);
            worldController.method282(ObjectManager.anIntArray140[i], ((Animable) (obj4)), i3, j, byte1, i1, null, l2, 0, k1);
            if(class46.aBoolean767)
                class11.setWall(i1, j, i, k, class46.aBoolean757);
            return;
        }
        if(k == 2)
        {
            int j3 = i + 1 & 3;
            Object obj11;
            Object obj12;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
            {
                obj11 = class46.method578(2, 4 + i, l1, i2, j2, k2, -1);
                obj12 = class46.method578(2, j3, l1, i2, j2, k2, -1);
            } else
            {
                obj11 = new Animable_Sub5(j1, 4 + i, 2, i2, j2, l1, k2, class46.anInt781, true);
                obj12 = new Animable_Sub5(j1, j3, 2, i2, j2, l1, k2, class46.anInt781, true);
            }
            worldController.method282(ObjectManager.anIntArray152[i], ((Animable) (obj11)), i3, j, byte1, i1, ((Animable) (obj12)), l2, ObjectManager.anIntArray152[j3], k1);
            if(class46.aBoolean767)
                class11.setWall(i1, j, i, k, class46.aBoolean757);
            return;
        }
        if(k == 3)
        {
            Object obj5;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj5 = class46.method578(3, i, l1, i2, j2, k2, -1);
            else
                obj5 = new Animable_Sub5(j1, i, 3, i2, j2, l1, k2, class46.anInt781, true);
            worldController.method282(ObjectManager.anIntArray140[i], ((Animable) (obj5)), i3, j, byte1, i1, null, l2, 0, k1);
            if(class46.aBoolean767)
                class11.setWall(i1, j, i, k, class46.aBoolean757);
            return;
        }
        if(k == 9)
        {
            Object obj6;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj6 = class46.method578(k, i, l1, i2, j2, k2, -1);
            else
                obj6 = new Animable_Sub5(j1, i, k, i2, j2, l1, k2, class46.anInt781, true);
            worldController.method284(i3, byte1, l2, 1, ((Animable) (obj6)), 1, k1, 0, j, i1);
            if(class46.aBoolean767)
                class11.setLoc(i1, j, class46.anInt744, class46.anInt761, i, class46.aBoolean757);
            return;
        }
        if(class46.aBoolean762)
            if(i == 1)
            {
                int k3 = k2;
                k2 = j2;
                j2 = i2;
                i2 = l1;
                l1 = k3;
            } else
            if(i == 2)
            {
                int l3 = k2;
                k2 = i2;
                i2 = l3;
                l3 = j2;
                j2 = l1;
                l1 = l3;
            } else
            if(i == 3)
            {
                int i4 = k2;
                k2 = l1;
                l1 = i2;
                i2 = j2;
                j2 = i4;
            }
        if(k == 4)
        {
            Object obj7;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj7 = class46.method578(4, 0, l1, i2, j2, k2, -1);
            else
                obj7 = new Animable_Sub5(j1, 0, 4, i2, j2, l1, k2, class46.anInt781, true);
            worldController.method283(i3, j, i * 512, k1, 0, l2, ((Animable) (obj7)), i1, byte1, 0, ObjectManager.anIntArray152[i]);
            return;
        }
        if(k == 5)
        {
            int j4 = 16;
            int l4 = worldController.method300(k1, i1, j);
            if(l4 > 0)
                j4 = ObjectDef.forID(l4 >> 14 & 0x7fff).anInt775;
            Object obj13;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj13 = class46.method578(4, 0, l1, i2, j2, k2, -1);
            else
                obj13 = new Animable_Sub5(j1, 0, 4, i2, j2, l1, k2, class46.anInt781, true);
            worldController.method283(i3, j, i * 512, k1, ObjectManager.anIntArray137[i] * j4, l2, ((Animable) (obj13)), i1, byte1, ObjectManager.anIntArray144[i] * j4, ObjectManager.anIntArray152[i]);
            return;
        }
        if(k == 6)
        {
            Object obj8;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj8 = class46.method578(4, 0, l1, i2, j2, k2, -1);
            else
                obj8 = new Animable_Sub5(j1, 0, 4, i2, j2, l1, k2, class46.anInt781, true);
            worldController.method283(i3, j, i, k1, 0, l2, ((Animable) (obj8)), i1, byte1, 0, 256);
            return;
        }
        if(k == 7)
        {
            Object obj9;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj9 = class46.method578(4, 0, l1, i2, j2, k2, -1);
            else
                obj9 = new Animable_Sub5(j1, 0, 4, i2, j2, l1, k2, class46.anInt781, true);
            worldController.method283(i3, j, i, k1, 0, l2, ((Animable) (obj9)), i1, byte1, 0, 512);
            return;
        }
        if(k == 8)
        {
            Object obj10;
            if(class46.anInt781 == -1 && class46.childrenIDs == null)
                obj10 = class46.method578(4, 0, l1, i2, j2, k2, -1);
            else
                obj10 = new Animable_Sub5(j1, 0, 4, i2, j2, l1, k2, class46.anInt781, true);
            worldController.method283(i3, j, i, k1, 0, l2, ((Animable) (obj10)), i1, byte1, 0, 768);
        }
    }

  public static boolean method189(int i, byte[] is, int i_250_
  ) //xxx bad method, decompiled with JODE
  {
    boolean bool = true;
    Buffer buffer = new Buffer(is);
    int i_252_ = -1;
    for (;;)
      {
	int i_253_ = buffer.readUSmart ();
	if (i_253_ == 0)
	  break;
	i_252_ += i_253_;
	int i_254_ = 0;
	boolean bool_255_ = false;
	for (;;)
	  {
	    if (bool_255_)
	      {
		int i_256_ = buffer.readUSmart ();
		if (i_256_ == 0)
		  break;
		buffer.readUByte();
	      }
	    else
	      {
		int i_257_ = buffer.readUSmart ();
		if (i_257_ == 0)
		  break;
		i_254_ += i_257_ - 1;
		int i_258_ = i_254_ & 0x3f;
		int i_259_ = i_254_ >> 6 & 0x3f;
		int i_260_ = buffer.readUByte() >> 2;
		int i_261_ = i_259_ + i;
		int i_262_ = i_258_ + i_250_;
		if (i_261_ > 0 && i_262_ > 0 && i_261_ < 103 && i_262_ < 103)
		  {
		    ObjectDef class46 = ObjectDef.forID (i_252_);
		    if (i_260_ != 22 || !ObjectManager.lowMem || class46.hasActions
                    || class46.aBoolean736)
		      {
			bool &= class46.method579 ();
			bool_255_ = true;
		      }
		  }
	      }
	  }
      }
    return bool;
  }

    public final void decodeLandscapes(int localX, CollisionMap aclass11[], int localY, WorldController worldController, byte abyte0[])
    {    	
label0:
        {
            Buffer buffer = new Buffer(abyte0);
            int l = -1;
            do
            {
                int i1 = buffer.readUSmart();
                if(i1 == 0)
                    break label0;
                l += i1;
                int j1 = 0;
                do
                {
                    int k1 = buffer.readUSmart();
                    if(k1 == 0)
                        break;
                    j1 += k1 - 1;
                    int l1 = j1 & 0x3f;
                    int i2 = j1 >> 6 & 0x3f;
                    int j2 = j1 >> 12;
                    int k2 = buffer.readUByte();
                    int l2 = k2 >> 2;
                    int i3 = k2 & 3;
                    int x = i2 + localX;
                    int y = l1 + localY;
                    if(x > 0 && y > 0 && x < 103 && y < 103)
                    {
                        int l3 = j2;
                        if((this.tileFlags[1][x][y] & 2) == 2)
                            l3--;
                        CollisionMap class11 = null;
                        if(l3 >= 0)
                            class11 = aclass11[l3];
                 //       this.method175(y, worldController, class11, l2, j2, x, l, i3);
                        if(l == 1316)
                        System.out.println(y + "-" + l2 + "-" + j2 + "-" + x + "-" + i3);
                    }
                } while(true);
            } while(true);
        }
    
    int id = 1316;
    for(int x = 0; x < 103; x++)
    {
 
        for(int y = 0; y < 103; y++) {
           	this.loadGlest(x, y);
        	if(objects[x][y] > 1)
        	//41-10-0-80-2
       		this.method175(y, worldController, aclass11[0], 10 /*always 10 for trees*/, 0/*always 0 for trees*/, x, id, 2 /*0,1,2,3*/);
       // 	tileHeights[0][x][y] = this.loadGlest(x, y);
      //      System.out.println("manly");
        //	this.underlays[0][x][y] = surfaces[x][y];
         // 	this.overlays[0][x][y] = 0;
        }

    }
    System.out.println("booya");
    }

    private final int[] hue_buffer;
    private final int[] saturation_buffer;
    private final int[] lightness_buffer;
    private final int[] chroma_buffer;
    private final int[] buffer_size;
    private final int[][][] tileHeights;
    private final byte[][][] overlays;
    public static int visibleLevels;
    private final byte[][][] shading;
    private final int[][][] tile_culling_bitmap;
    private final byte[][][] overlayShape;
    private static final int anIntArray137[] = {
        1, 0, -1, 0
    };
    private static final int anInt138 = 323;
    private final int[][] tileLighting;
    private static final int anIntArray140[] = {
        16, 32, 64, 128
    };
    private final byte[][][] underlays;
    private static final int anIntArray144[] = {
        0, -1, 0, 1
    };
    public static int maximumPlane = 99;
    private final int width;
    private final int length;
    private final byte[][][] aByteArrayArrayArray148;
    private final byte[][][] tileFlags;
    public static boolean lowMem = true;
    private static final int anIntArray152[] = {
        1, 2, 4, 8
    };

}
