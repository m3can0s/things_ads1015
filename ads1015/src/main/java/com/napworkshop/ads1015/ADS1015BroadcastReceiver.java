package com.napworkshop.ads1015;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by m3can0 on 2017-06-15.
 */

public class ADS1015BroadcastReceiver extends BroadcastReceiver {
    private final static String TAG = ADS1015BroadcastReceiver.class.getSimpleName();

    public final static String ACTION = TAG;
    private final static String EXTRA_A0 = "extra_a0";
    private final static String EXTRA_A1 = "extra_a1";
    private final static String EXTRA_A2 = "extra_a2";
    private final static String EXTRA_A3 = "extra_a3";


    private Listener listener;

    public ADS1015BroadcastReceiver(Listener listener){
        super();
        if(listener == null){
            throw new NullPointerException();
        }
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action!= null && action.equals(ACTION)){
            float valueA0 = intent.getFloatExtra(EXTRA_A0, 0.0f);
            float valueA1 = intent.getFloatExtra(EXTRA_A1, 0.0f);
            float valueA2 = intent.getFloatExtra(EXTRA_A2, 0.0f);
            float valueA3 = intent.getFloatExtra(EXTRA_A3, 0.0f);
            this.listener.onADS1015Updated(valueA0, valueA1, valueA2, valueA3);
        }
    }

    public static Intent createSenderIntent(float valueA0, float valueA1, float valueA2, float valueA3){
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.putExtra(EXTRA_A0, valueA0);
        intent.putExtra(EXTRA_A1, valueA1);
        intent.putExtra(EXTRA_A2, valueA2);
        intent.putExtra(EXTRA_A3, valueA3);
        return intent;
    }

    public interface Listener{
        void onADS1015Updated(float valueA0, float valueA1, float valueA2, float valueA3);
    }
}
