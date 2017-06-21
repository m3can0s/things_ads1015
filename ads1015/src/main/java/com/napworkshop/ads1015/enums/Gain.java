package com.napworkshop.ads1015.enums;

/**
 * Created by m3can0 on 2017-06-10.
 */

public enum Gain {
    v6_144(0x00 ,3.000f),
    v4_096(0x01 ,2.000f),
    v2_048(0x02 ,1.000f),
    v1_024(0x03 ,0.500f),
    v0_512(0x04 ,0.250f),
    v0_256(0x05 ,0.125f);

    private int hexa;     //Valeur pour identifier le Gain sur le registre
    private float step; //millivolt/step
    Gain(int hexa, float step){
        this.hexa = hexa;
        this.step = step;
    }

    public float getStepVoltage(){
        return this.step;
    }

    public int getHexa(){
        return hexa;
    }

    public float getVoltageValue(int analogValue){
        return this.step * (float)analogValue / 1000.0f;
    }

    public static float getVoltageValue(Gain gain, int analogValue){
        return gain.getVoltageValue(analogValue);
    }

    public static Gain getFromHexa(int hexa) {
        if(hexa == v6_144.getHexa()) return v6_144;
        if(hexa == v4_096.getHexa()) return v4_096;
        if(hexa == v2_048.getHexa()) return v2_048;
        if(hexa == v1_024.getHexa()) return v1_024;
        if(hexa == v0_512.getHexa()) return v0_512;
        if(hexa == v0_256.getHexa()) return v0_256;
        return null;
    }
}
