package com.napworkshop.ads1015;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.IBinder;
import android.hardware.SensorManager;
import android.util.Log;

import com.google.android.things.userdriver.UserDriverManager;
import com.google.android.things.userdriver.UserSensor;

/**
 * Created by m3can0 on 2017-06-11.
 */

public class ADS1015Service extends Service implements SensorEventListener {
    private final static String TAG = ADS1015Service.class.getSimpleName();

    private SensorManager manager;
    private ADS1015Driver driver;
    private UserSensor sensor;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String tagMethod = ".onStartCommand()";
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        driver = new ADS1015Driver();
        sensor = driver.getUserSensor();

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        manager.registerDynamicSensorCallback(new SensorCallback());

        UserDriverManager userManager = UserDriverManager.getManager();
        userManager.registerSensor(sensor);

        driver.open();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String tagMethod = ".onSensorChanged()";
        float[] analogValues = event.values;

        //On envoit les donnees
        Intent intent = ADS1015BroadcastReceiver.createSenderIntent(analogValues[0], analogValues[1], analogValues[2], analogValues[3]);
        sendBroadcast(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        String tagMethod = ".onAccuracyChanged()";
        Log.i(TAG + tagMethod, " sensor changed...");
    }

    private class SensorCallback extends SensorManager.DynamicSensorCallback {
        @Override
        public void onDynamicSensorConnected(Sensor sensor) {
            //Sensor connected
            String tagMethod = ".onSensorConnected()";
            Log.i(TAG + tagMethod, "Registering the sensor listener !!!!");
            manager.registerListener(ADS1015Service.this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        public void onDynamicSensorDisconnected(Sensor sensor) {
            //Sensor disconnected
            String tagMethod = ".onSensorDisconnected()";
            Log.i(TAG + tagMethod, "Unregistering the sensor listener :(");
            manager.unregisterListener(ADS1015Service.this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        driver.close();
        UserDriverManager userManager = UserDriverManager.getManager();
        userManager.unregisterSensor(sensor);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
