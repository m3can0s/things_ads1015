package com.napworkshop.ads1015.enums;

/**
 * Created by m3can0 on 2017-06-11.
 */

public enum Address {
    DEFAULT(0x48),
    SECOND(0x49),
    THIRD(0x50),
    FOURTH(0x51);

    private int hexa;
    Address(int hexa){
        this.hexa = hexa;
    }

    public int getHexa(){
        return this.hexa;
    }
}
