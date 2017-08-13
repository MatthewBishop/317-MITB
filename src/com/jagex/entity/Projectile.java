package com.jagex.entity;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.jagex.cache.anim.Graphic;
import com.jagex.entity.model.Model;

public final class Projectile extends Animable {

    public void target(int _endTick, int destY, int destZ, int destX)
    {
        if(!this.mobile)
        {
            double d = destX - this.sourceX;
            double d2 = destY - this.sourceY;
            double d3 = Math.sqrt(d * d + d2 * d2);
            this.x = this.sourceX + (d * this.leapScale) / d3;
            this.y = this.sourceY + (d2 * this.leapScale) / d3;
            this.z = this.sourceElevation;
        }
        double d1 = (this.startTick + 1) - _endTick;
        this.velocityX = (destX - this.x) / d1;
        this.velocityY = (destY - this.y) / d1;
        this.velocity = Math.sqrt(this.velocityX * this.velocityX + this.velocityY * this.velocityY);
        if(!this.mobile)
            this.velocityZ = -this.velocity * Math.tan(this.elevationPitch * 0.02454369D);
        this.acceleration = (2D * (destZ - this.z - this.velocityZ * d1)) / (d1 * d1);
    }

    @Override
	public Model getModel()
    {
        return AnimableRenderer.getModel(this);
    }

	public Projectile(int i, int j, int l, int i1, int j1, int k1,
                         int l1, int i2, int j2, int k2, int l2)
    {
        this.mobile = false;
        this.graphic = Graphic.graphics[l2];
        this.level = k1;
        this.sourceX = j2;
        this.sourceY = i2;
        this.sourceElevation = l1;
        this.endTick = l;
        this.startTick = i1;
        this.elevationPitch = i;
        this.leapScale = j1;
        this.target = k2;
        this.destinationElevation = j;
        this.mobile = false;
    }

    public void update(int time)
    {
        this.mobile = true;
        this.x += this.velocityX * time;
        this.y += this.velocityY * time;
        this.z += this.velocityZ * time + 0.5D * this.acceleration * time * time;
        this.velocityZ += this.acceleration * time;
        this.yaw = (int)(Math.atan2(this.velocityX, this.velocityY) * 325.94900000000001D) + 1024 & 0x7ff;
        this.pitch = (int)(Math.atan2(this.velocityZ, this.velocity) * 325.94900000000001D) & 0x7ff;
        if(this.graphic.animation != null)
            for(this.elapsed += time; this.elapsed > this.graphic.animation.duration(this.frameIndex);)
            {
                this.elapsed -= this.graphic.animation.duration(this.frameIndex) + 1;
                this.frameIndex++;
                if(this.frameIndex >= this.graphic.animation.frameCount)
                    this.frameIndex = 0;
            }

    }

    public final int endTick;
    public final int startTick;
    private double velocityX;
    private double velocityY;
    private double velocity;
    private double velocityZ;
    private double acceleration;
    private boolean mobile;
    private final int sourceX;
    private final int sourceY;
    private final int sourceElevation;
    public final int destinationElevation;
    public double x;
    public double y;
    public double z;
    private final int elevationPitch;
    private final int leapScale;
    public final int target;
    final Graphic graphic;
    int frameIndex;
    private int elapsed;
    public int yaw;
    int pitch;
    public final int level;
}
