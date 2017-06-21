package com.napworkshop.ads1015.models;

import com.napworkshop.ads1015.enums.Gain;
import com.napworkshop.ads1015.enums.Multiplexer;

/**
 * Created by m3can0 on 2017-06-11.
 */

public class Configuration {
    private final static String TAG = Configuration.class.getSimpleName();

    private boolean isConverting = false;
    private Multiplexer mux = Multiplexer.A0;
    private Gain gain = Gain.v2_048;

    private Configuration(Builder builder){
        this.isConverting = builder.isConverting;
        this.mux = builder.mux;
        this.gain = builder.gain;
    }

    public boolean isConverting() {
        return isConverting;
    }

    public Multiplexer getMultiplexer() {
        return mux;
    }

    public Gain getGain() {
        return gain;
    }

    @Override
    public String toString() {
        String muxValue = null;
        if(mux != null) muxValue = mux.name();
        String gainValue = null;
        if(gain != null) gainValue = gain.name();
        return "Configuration{" +
                "isConverting=" + isConverting +
                ", mux=" + muxValue +
                ", gain=" + gainValue +
                '}';
    }

    public static class Builder {
        private boolean isConverting;
        private Multiplexer mux;
        private Gain gain;

        public Builder(boolean isConverting, Multiplexer mux) {
            if(mux == null){
                throw new NullPointerException();
            }
            this.isConverting = isConverting;
            this.mux = mux;
            this.gain = Gain.v4_096;
        }

        public Builder setGain(Gain gain){
            if(gain == null){
                throw new NullPointerException();
            }
            this.gain = gain;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }
}
