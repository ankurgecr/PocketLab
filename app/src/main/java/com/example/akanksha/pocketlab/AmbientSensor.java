
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


public class AmbientSensor extends AbstractIOIOActivity
{

    Button newDataButton;
    Button saveDataButton;
    Button sensorMenuButton;

    Activity newExpSelf = this;

    private final static long SAMPLE_PERIOD = 10000; // 10 seconds
    private static final int PLUS_PIN = 28;//44;
    private static final int GND_PIN = 27; //46;
    private static final int INPUT_PIN = 38; //45;

    private double currentLumens;
    private String units;
    private boolean measureLumens = true;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_sensor);

        newDataButton = (Button) findViewById(R.id.new_data_button);
        newDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                measureLumens = true;
            }
        });
        saveDataButton = (Button) findViewById(R.id.save_data_button);
        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logLumens();
            }
        });

        sensorMenuButton = (Button) findViewById(R.id.sensors_button);
        sensorMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newExpSelf, NewExperiment.class);
                startActivity(intent);
            }
        });


        currentLumens = 0;
        units = "Lux";

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new AmbientBulbs();
        fragmentManager.beginTransaction()
                .replace(R.id.container0, fragment)
                .commit();
    } // onCreate()



    class IOIOThread extends AbstractIOIOActivity.IOIOThread
    {
        private DigitalOutput plusPin_;
        private DigitalOutput gndPin_;
        private AnalogInput inputPin_;

        @Override
        protected void setup() throws ConnectionLostException
        {
            gndPin_ = ioio_.openDigitalOutput(GND_PIN, Mode.NORMAL, false); // gnd supply to temp sensor
            plusPin_ = ioio_.openDigitalOutput(PLUS_PIN, Mode.NORMAL, true); // positive supply to temp sensor
            inputPin_ = ioio_.openAnalogInput(INPUT_PIN);
        }

        @Override
        protected void loop() throws ConnectionLostException
        {
            if(measureLumens) {
                double avgLumens = 0;
                double divisor = 0;
                for (int i = 0; i < 30; i++)
                {
                    try
                    {
                        float v = inputPin_.getVoltage();
                        double lumens = v;
                        lumens = 27.565 * Math.pow(10, v);
                        lumens = Math.round(lumens);    // round to 1 dp
                        avgLumens += lumens;
                        divisor++;
                        sleep(100);
                    }
                    catch (Exception e)
                    {
                        toast(e.getMessage());
                    } // try/catch
                }
                if (divisor >= 25)
                {
                    currentLumens = avgLumens / divisor;
                    updateLumensField(avgLumens / divisor);
                }

                measureLumens = false;
            } // if(measureLumens)
        } // loop()
    } // class IOIOThread



    private void logLumens()
    {
        Long lumensTime =  System.currentTimeMillis();
        String newdatastr = lumensTime + ":" + currentLumens + ":" + units + ";";
        SaveDataPointSQL s = new SaveDataPointSQL(MainActivity.exptime);
        s.execute(MainActivity.currentUser, "Lumens",newdatastr);
        try {
            if (s.get().equals("Works"))
            {
                Log.d("DEBUG","Saved new lumens");
                toast("Saved " + currentLumens + units);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected AbstractIOIOActivity.IOIOThread createIOIOThread()
    {
        return new IOIOThread();
    }



    private void updateLumensField(final double lumens)
    {
        currentLumens = lumens;
    }

    public void toast(final String message)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, message, duration);
                toast.show();
            }
        });
    }

    public double getCurrentLumens()
    {
        return currentLumens;
    }

    public String getUnits()
    {
        return units;
    }

    public boolean isMeasuring()
    {
        return measureLumens;
    }

} // class TemperatureSensor