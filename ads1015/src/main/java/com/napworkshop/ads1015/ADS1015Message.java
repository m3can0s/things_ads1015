package com.napworkshop.ads1015;

import com.napworkshop.ads1015.enums.Gain;
import com.napworkshop.ads1015.enums.Multiplexer;
import com.napworkshop.ads1015.enums.Register;
import com.napworkshop.ads1015.models.Configuration;

/**
 * Created by m3can0 on 2017-06-14.
 */

public class ADS1015Message {
    private final static String TAG = ADS1015Message.class.getSimpleName();

    //Log.i(TAG, "isConverting = 0x" + String.format("%x", (0x0000000080 & datas[0]) >> 7 ));
    //Log.i(TAG, "singleMode = 0x" + String.format("%x", (0x0000000001 & datas[0])));
    //Log.i(TAG, "muxHexa = 0x" + String.format("%x", (0x0000000070 & datas[0]) >> 4 ));
    //Log.i(TAG, "gain = 0x" + String.format("%x", (0x000000000e & datas[0]) >> 1 ));
    //Log.i(TAG, "data rate = 0x" + String.format("%x", (0x00000000e0 & datas[1]) >> 5 ));
    //og.i(TAG, "comparator = 0x" + String.format("%x", (0x000000001f & datas[1])));

    //private Convertion convertion;                //bit[15, 8]
    //private int comparatorConfig = 0x03;          //bit[4:0]
    //private int dataRate = 0x04;                  //bit[7:5]
    //private int convertionMode = 0x01;            //bit[8]
    //private Gain gain = Gain.v2_048;              //bit[11:9]
    //private Multiplexer mux = Multiplexer.A0;     //bit[14:12]

    public static byte[] ConfigurationToByteArray(Configuration config) {
        int msb = (config.isConverting()? 0x01: 0x00) << 7
                | 0x1 // byte pour le mode single
                | config.getMultiplexer().getHexa() << 4
                | config.getGain().getHexa() << 1;
        //Log.i(TAG, "msb = " + msb);
        byte[] byteConfig = {Register.CONFIGURATION.getHexa(), (byte)msb, (byte)0x83};
        return byteConfig;
    }

    public static  Configuration ConfigurationFromByteArray(byte[] datas){

        boolean isConverting = (0x0000000080 & datas[0]) >> 7 == 0x0;
        Multiplexer mux = Multiplexer.getFromHexa((0x0000000070 & datas[0]) >> 4);
        Gain gain = Gain.getFromHexa((0x000000000e & datas[0]) >> 1);

        return new Configuration.Builder(isConverting, mux).setGain(gain).build();
    }

    public static int ConvertionValueFromByteArray(byte[] datas){
        return datas[0]<<4 | (datas[1]>>4 & 0x0000000f);
    }
}
