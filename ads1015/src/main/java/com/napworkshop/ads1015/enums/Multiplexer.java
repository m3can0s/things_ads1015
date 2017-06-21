package com.napworkshop.ads1015.enums;

/**
 * Created by m3can0 on 2017-06-10.
 */

public enum Multiplexer{
    Diff(0x00),
    A0(0x04),
    A1(0x05),
    A2(0x06),
    A3(0x07);

    private int hexa;
    private Multiplexer(int id){
        this.hexa = id;
    }

    public int getHexa(){
        return hexa;
    }

    public static Multiplexer getFromHexa(int hexa){
        if(hexa == Diff.getHexa()) return Diff;
        if(hexa == A0.getHexa()) return A0;
        if(hexa == A1.getHexa()) return A1;
        if(hexa == A2.getHexa()) return A2;
        if(hexa == A3.getHexa()) return A3;
        return null;
    }
}
