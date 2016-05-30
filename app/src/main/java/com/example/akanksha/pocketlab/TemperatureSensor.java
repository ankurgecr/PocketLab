/**
 * code from the book Getting Started with IOIO
 * <br>Copyright 2011 Simon Monk
 *
 * <p>This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2
 * as published by the Free Software Foundation (see COPYING).
 *
 * <p>This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.example.akanksha.pocketlab;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.DigitalOutput.Spec.Mode;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.AbstractIOIOActivity;


public class TemperatureSensor extends AbstractIOIOActivity {

    Button newDataButton;
    Button saveDataButton;
    Button sensorMenuButton;

    Activity newExpSelf = this;

    private final static long SAMPLE_PERIOD = 10000; // 10 seconds
    private static final int PLUS_PIN = 28;//44;
    private static final int GND_PIN = 27; //46;
    private static final int INPUT_PIN = 35; //45;

    private float currentTemp;
    private String units;
    private boolean measureTemp = true;

    /**
     * Called when the activity is first created. Here we normally initialize
     * our GUI.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_sensor);

        newDataButton = (Button) findViewById(R.id.new_data_button);
        newDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                measureTemp = true;
            }
        });
        saveDataButton = (Button) findViewById(R.id.save_data_button);
        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTemp();
            }
        });

        sensorMenuButton = (Button) findViewById(R.id.sensors_button);
        sensorMenuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(newExpSelf, NewExperiment.class);
                startActivity(intent);
            }
        });

        currentTemp = 0;
        units = "F";

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new Thermometer();
        fragmentManager.beginTransaction()
                .replace(R.id.container0, fragment)
                .commit();
    } // onCreate()

    /**
     * This is the thread on which all the IOIO activity happens. It will be run
     * every time the application is resumed and aborted when it is paused. The
     * method setup() will be called right after a connection with the IOIO has
     * been established (which might happen several times!). Then, loop() will
     * be called repetitively until the IOIO gets disconnected.
     */
    class IOIOThread extends AbstractIOIOActivity.IOIOThread {
        private DigitalOutput plusPin_;
        private DigitalOutput gndPin_;
        private AnalogInput inputPin_;

        /**
         * Called every time a connection with IOIO has been established.
         * Typically used to open pins.
         *
         * @throws ConnectionLostException
         *             When IOIO connection is lost.
         *
         * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#setup()
         */
        @Override
        protected void setup() throws ConnectionLostException {
            gndPin_ = ioio_.openDigitalOutput(GND_PIN, Mode.NORMAL, false); // gnd supply to temp sensor
            plusPin_ = ioio_.openDigitalOutput(PLUS_PIN, Mode.NORMAL, true); // positive supply to temp sensor
            inputPin_ = ioio_.openAnalogInput(INPUT_PIN);
        }

        /**
         * Called repetitively while the IOIO is connected.
         *
         * @throws ConnectionLostException
         *             When IOIO connection is lost.
         * @throws InterruptedException
         *
         * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#loop()
         */
        @Override
        protected void loop() throws ConnectionLostException {
            if(measureTemp) {
                float avgtemp = 0;
                float divisor = 0;
                for (int i = 0; i < 30; i++) {
                    try {
                        float v = inputPin_.getVoltage();
                        float voltsPerDegree = 0.02f;
                        float temp = v / voltsPerDegree;
                        if (units.equals("F")) {
                            temp = ((v / voltsPerDegree) * 9.0f / 5.0f) + 32;
                        }
                        // round to 1 dp
                        temp = Math.round(temp * 10) / 10.0f;
                        avgtemp += temp;
                        divisor++;
                        sleep(100);
                    } catch (Exception e) {
                        toast(e.getMessage());
                    } // try/catch
                }
                if (divisor >= 25) {
                    currentTemp = avgtemp / divisor;
                    updateTempField(avgtemp / divisor);
                }

                measureTemp = false;
            } // if(measureTemp)
        } // loop()
    } // class IOIOThread

    private void logTemp() {
        Long temptime =  System.currentTimeMillis();
        String newdatastr = temptime + ":" + currentTemp + ":" + units + ";";
        SaveDataPointSQL s = new SaveDataPointSQL(MainActivity.exptime);
        s.execute(MainActivity.currentUser, "temperature",newdatastr);
        try {
            if (s.get().equals("Works")){
                Log.d("DEBUG","Saved new temp");
                toast("Saved " + currentTemp + " degrees " + units);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method to create our IOIO thread.
     *
     * @see ioio.lib.util.AbstractIOIOActivity#createIOIOThread()
     */
    @Override
    protected AbstractIOIOActivity.IOIOThread createIOIOThread() {
        return new IOIOThread();
    }

    private void updateTempField(final float temp) {
        currentTemp = temp;
    }

    public void toast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, message, duration);
                toast.show();
            }
        });
    }

    public float getCurrentTemp()
    {
        return currentTemp;
    }

    public String getUnits()
    {
        return units;
    }

    public boolean isMeasuring()
    {
        return measureTemp;
    }

    public void toggleUnits()
    {
        if(units.equals("C"))
        {
            units = "F";
            currentTemp = CtoF(currentTemp);
        }
        else if(units.equals("F"))
        {
            units = "C";
            currentTemp = FtoC(currentTemp);
        }
    }

    private float CtoF(float c)
    {
        return 9f/5f*c + 32f;
    }

    private float FtoC(float f)
    {
        return 5f/9f*(f-32);
    }

} // class TemperatureSensor