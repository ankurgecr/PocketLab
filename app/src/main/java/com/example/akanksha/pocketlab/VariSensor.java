package com.example.akanksha.pocketlab;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.AbstractIOIOActivity;


public class VariSensor extends AbstractIOIOActivity {

    Button newDataButton;
    Button saveDataButton;
    TextView resultsview;

    Activity newExpSelf = this;

    private final static long SAMPLE_PERIOD = 10000; // 10 seconds
    private static int PLUS_PIN; // = 28;//44;
    private static int GND_PIN; // = 27; //46;
    private static int INPUT_PIN; // = 35; //45;

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
        setContentView(R.layout.activity_vari_sensor);

        Intent mIntent = getIntent();
        PLUS_PIN = mIntent.getIntExtra("plus", 1);
        GND_PIN = mIntent.getIntExtra("grnd", 2);
        INPUT_PIN = mIntent.getIntExtra("read",31);


        resultsview = (TextView) findViewById(R.id.datadisplay);
        newDataButton = (Button) findViewById(R.id.newdata);
        newDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                measureTemp = true;
            }
        });
        saveDataButton = (Button) findViewById(R.id.savedata);
        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTemp();
            }
        });

        currentTemp = 0;

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
            gndPin_ = ioio_.openDigitalOutput(GND_PIN, DigitalOutput.Spec.Mode.NORMAL, false); // gnd supply to temp sensor
            plusPin_ = ioio_.openDigitalOutput(PLUS_PIN, DigitalOutput.Spec.Mode.NORMAL, true); // positive supply to temp sensor
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
                        avgtemp += v;
                        divisor++;
                        sleep(100);
                    } catch (Exception e) {
                        toast(e.getMessage());
                    } // try/catch

                    Log.d("DEBUG",""+divisor);
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
        String newdatastr = temptime + ":" + currentTemp + ";";
        SaveDataPointSQL s = new SaveDataPointSQL(MainActivity.exptime);
        s.execute(MainActivity.currentUser, "other",newdatastr);
        try {
            if (s.get().equals("Works")){
                Log.d("DEBUG", "Saved new temp");
                toast("Saved " + currentTemp);
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
        settemp(temp);

    }

    public void settemp(final float tmpin){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                resultsview.setText(""+tmpin);
            }
        });
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
