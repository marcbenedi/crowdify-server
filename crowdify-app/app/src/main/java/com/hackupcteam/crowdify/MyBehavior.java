package com.hackupcteam.crowdify;

import android.graphics.Color;

/**
 * Created by Hermes on 08/10/2016.
 */
public class MyBehavior {
    private int color;
    private String vibrate;
    private String flash;
    private long durationInMillis;
    private boolean shake;

    public boolean getShake() {
        return shake;
    }

    public void setShake(String shake1) {
        this.shake = shake1.equals("yes");
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getDurationInMillis() {
        return durationInMillis;
    }

    public void setDurationInMillis(long durationInMillis) {
        this.durationInMillis = durationInMillis;
    }

    public String getFlash() {
        return flash;
    }

    public void setFlash(String flash) {
        this.flash = flash;
    }

    public String getVibrate() {
        return vibrate;
    }

    public void setVibrate(String vibrate) {
        this.vibrate = vibrate;
    }
}
