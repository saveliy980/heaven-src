package net.minecraft.util;

import net.minecraft.client.Minecraft;

public class Timer
{
    /**
     * How many full ticks have turned over since the last call to updateTimer(), capped at 10.
     */
    public int elapsedTicks;
    public float field_194147_b;
    public float field_194148_c;
    
    private float speed;

    /**
     * The time reported by the system clock at the last sync, in milliseconds
     */
    private long lastSyncSysClock;
    private float field_194149_e;

    public Timer(float tps)
    {
        this.field_194149_e = 1000.0F / tps;
        this.lastSyncSysClock = Minecraft.getSystemTime();
        speed = 1.0F;
    }

    /**
     * Updates all fields of the Timer using the current time
     */
    public void updateTimer()
    {
        long i = Minecraft.getSystemTime();
        this.field_194148_c = (float)(i - this.lastSyncSysClock) / this.field_194149_e * speed;
        this.lastSyncSysClock = i;
        this.field_194147_b += this.field_194148_c;
        this.elapsedTicks = (int)this.field_194147_b;
        this.field_194147_b -= (float)this.elapsedTicks;
    }
    
    public void setSpeed(float timer) {
    	this.speed = timer;
    }
    
    public float getSpeed() {
    	return speed;
    }
    
    public void reset() {
    	this.speed = 1.0f;
    }
}
