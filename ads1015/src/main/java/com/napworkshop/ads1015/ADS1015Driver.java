package com.napworkshop.ads1015;

import android.hardware.Sensor;
import android.util.Log;

import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.userdriver.UserSensor;
import com.google.android.things.userdriver.UserSensorDriver;
import com.google.android.things.userdriver.UserSensorReading;
import com.napworkshop.ads1015.enums.Address;
import com.napworkshop.ads1015.enums.Gain;
import com.napworkshop.ads1015.enums.Multiplexer;
import com.napworkshop.ads1015.enums.Register;
import com.napworkshop.ads1015.models.Configuration;

import java.io.IOException;

/**
 * Created by m3can0 on 2017-06-10.
 *
 * Lien:
 * ADS1015Driver
 * https://www.adafruit.com/product/1083
 * http://www.ti.com.cn/cn/lit/ds/symlink/ads1015.pdf
 * https://developer.android.com/things/sdk/pio/i2c.html
 *
 * Drivers
 * https://developer.android.com/guide/topics/sensors/sensors_overview.html
 * https://code.tutsplus.com/fr/tutorials/android-things-understanding-and-writing-drivers--cms-28088
 *
 */

public class ADS1015Driver extends UserSensorDriver {
    private final static String TAG = ADS1015Driver.class.getSimpleName();

    private static final String I2C_DEVICE_NAME = "I2C1";

    private I2cDevice ads1015;

    private float[] voltageValues;

    public ADS1015Driver(){
        initanalogValues();
    }

    private void initanalogValues(){
        //setup the default analog values
        this.voltageValues = new float[4];
        for(int i = 0; i < 4; i++){
            this.voltageValues[i] = 0.0f;
        }
    }

    public boolean open(){
        return this.open(Address.DEFAULT);
    }

    public boolean open(Address address){
        if(address == null){
            throw new NullPointerException();
        }

        boolean success = true;
        try {
            PeripheralManagerService manager = new PeripheralManagerService();
            ads1015 = manager.openI2cDevice(I2C_DEVICE_NAME, address.getHexa());
            Log.d(TAG, "Connected to ads1015 " + String.format("%x", address.getHexa()));
        } catch (IOException e) {
            Log.w(TAG, "Unable to access I2C device", e);
            success = false;
        }
        return success;
    }

    public boolean close(){
        boolean success = true;
        if (ads1015 != null) {
            try {
                ads1015.close();
                ads1015 = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close I2C device", e);
                success = false;
            }
        }
        return success;
    }

    public UserSensor getUserSensor(){
        return new UserSensor.Builder()
                .setName("ADS1015")
                .setVendor("AdaFruit")
                .setCustomType(Sensor.TYPE_DEVICE_PRIVATE_BASE,
                        "com.napworkshop.ads1015",
                        Sensor.REPORTING_MODE_CONTINUOUS)
                .setDriver(this)
                .build();
    }

    private int readConvertion() throws IOException{
        byte[] register = {Register.CONVERSION.getHexa()};
        //On pointe vers le registre de la convertion
        ads1015.write(register, register.length);

        //On lit les 2 bites du resultat de la convertion
        byte[] analogBytes = new byte[2];
        ads1015.read(analogBytes, analogBytes.length);

        //Convertion des valeurs hexa en 12 bits
        return ADS1015Message.ConvertionValueFromByteArray(analogBytes);
    }

    private Configuration readConfiguration() throws IOException {
        //On point vers le registre de configuration
        byte[] register = {Register.CONFIGURATION.getHexa()};
        ads1015.write(register, register.length);

        //On lit les informations sur le registre
        byte[] byteConfig = new byte[2];
        ads1015.read(byteConfig, byteConfig.length);
        return ADS1015Message.ConfigurationFromByteArray(byteConfig);
    }

    private void writeConfiguration(Configuration config) throws IOException{
        //On cree le message en hexa
        byte[] byteConfig = ADS1015Message.ConfigurationToByteArray(config);

        //On ecrit dans le registre de configuration
        ads1015.write(byteConfig, byteConfig.length);
    }

    @Override
    public UserSensorReading read() throws IOException {

        //lire la configuration courante
        Configuration currentConfig = readConfiguration();
        if(currentConfig != null && !currentConfig.isConverting()){
            //On lit la valeur de la convertion
            int analogValue = readConvertion();

            //On convertie en voltage
            float voltageValue = Gain.getVoltageValue(currentConfig.getGain(), analogValue);
            Multiplexer currentMux = currentConfig.getMultiplexer();
            Multiplexer nextMux;
            switch(currentMux){
                case A0:
                    voltageValues[0] = voltageValue;
                    nextMux = Multiplexer.A1;
                    break;
                case A1:
                    voltageValues[1] = voltageValue;
                    nextMux = Multiplexer.A2;
                    break;
                case A2:
                    voltageValues[2] = voltageValue;
                    nextMux = Multiplexer.A3;
                    break;
                case A3:
                    voltageValues[3] = voltageValue;
                    nextMux = Multiplexer.A0;
                    break;
                default:
                    nextMux = Multiplexer.A0;
            }

            //On redemarre le convertisseur avec la prochaine configuration.
            Configuration nextConfig = new Configuration.Builder(true, nextMux).build();
            writeConfiguration(nextConfig);
        }
        return new UserSensorReading(this.voltageValues);
    }
}
