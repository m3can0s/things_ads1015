package com.napworkshop.ads1015.enums;

/**
 * Created by m3can0 on 2017-06-10.
 */

public enum Register{
    CONVERSION(0x00),
    CONFIGURATION(0x01),
    THRESHOLD_LOW(0x02),
    THRESHOLD_HIGH(0x03);

    private int hexa;
    private Register(int hexa){
        this.hexa = hexa;
    }

    public byte getHexa(){
        return (byte)this.hexa;
    }
}
