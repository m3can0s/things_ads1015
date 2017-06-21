package com.napworkshop.i2c_ads1015;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.napworkshop.ads1015.ADS1015BroadcastReceiver;
import com.napworkshop.ads1015.ADS1015Service;

public class MainActivity extends AppCompatActivity implements ADS1015BroadcastReceiver.Listener{
    private final static String TAG = MainActivity.class.getSimpleName();

    private ADS1015BroadcastReceiver receiver;

    private TextView a0Tv, a1Tv, a2Tv, a3Tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        a0Tv = (TextView) findViewById(R.id.a0_value);
        a1Tv = (TextView) findViewById(R.id.a1_value);
        a2Tv = (TextView) findViewById(R.id.a2_value);
        a3Tv = (TextView) findViewById(R.id.a3_value);

        //Register receiver
        receiver = new ADS1015BroadcastReceiver(this);
        IntentFilter filter = new IntentFilter(ADS1015BroadcastReceiver.ACTION);
        registerReceiver(receiver, filter);

        //On demmarre le service qui gere le capteur
        Intent intent = new Intent(this, ADS1015Service.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);

        //On arrete le service qui gere le capteur
        Intent intent = new Intent(this, ADS1015Service.class);
        stopService(intent);
    }

    @Override
    public void onADS1015Updated(float valueA0, float valueA1, float valueA2, float valueA3) {
        //Log.i(TAG, "analogValues = {" + valueA0 + ", " + valueA1 + ", " + valueA2 + ", " + valueA3 + "}");
        a0Tv.setText(formatResource("A0", valueA0));
        a1Tv.setText(formatResource("A1", valueA1));
        a2Tv.setText(formatResource("A2", valueA2));
        a3Tv.setText(formatResource("A3", valueA3));
    }

    private String formatResource(String input, float value){
        String res = getString(R.string.analog_value);
        return String.format(res, input, value);
    }
}
