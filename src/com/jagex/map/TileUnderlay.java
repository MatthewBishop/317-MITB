package com.jagex.map;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

//Class43
final class TileUnderlay
{

    public TileUnderlay(int southwestColor, int southeastColor, int northeastColor, int northwestColor, int texture, int rgb, boolean flag)
    {
        this.flat = true;
        this.southwestColor = southwestColor;
        this.southeastColor = southeastColor;
        this.northeastColor = northeastColor;
        this.northwestColor = northwestColor;
        this.texture = texture;
        this.rgb = rgb;
        this.flat = flag;
    }

    final int southwestColor;
    final int southeastColor;
    final int northeastColor;
    final int northwestColor;
    final int texture;
    boolean flat;
    final int rgb;
}
