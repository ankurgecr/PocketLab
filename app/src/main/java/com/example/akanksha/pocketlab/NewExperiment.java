package com.example.akanksha.pocketlab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.text.SpannableString;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class NewExperiment extends ActionBarActivity {
    Button temperatureButton;
    //Button humidityButton;
    Button colorButton;
    Button savecsvButton;
    //Button accelButton;
    Button ambiButton;
    Activity newExpSelf = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_experiment);

        temperatureButton = (Button) findViewById(R.id.temperature_button);
        //humidityButton = (Button) findViewById(R.id.humid_button);
        colorButton = (Button) findViewById(R.id.color_button);
        savecsvButton = (Button) findViewById(R.id.csv_button);
        //accelButton = (Button) findViewById(R.id.accel_button);
        ambiButton = (Button) findViewById(R.id.ambient_button);

        temperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newExpSelf, TemperatureSensor.class);
               // Intent intent = new Intent(newExpSelf, TemperatureSensorBoard.class);
                startActivity(intent);
            }
        });
        /*humidityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newExpSelf, HumiditySensor.class);
                startActivity(intent);
            }
        });*/
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newExpSelf, ColorSensor.class);
                startActivity(intent);
            }
        });
        savecsvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(newExpSelf);

                //alert.setTitle("Title");
                alert.setTitle("Are you sure you want to save the following lab?");
                //alert.setMessage("");

                // Set an EditText view to get user input
                TextView expinfo = new TextView(newExpSelf);
                /*String s1= "Hello Everyone";
                SpannableString ss1=  new SpannableString(s1);
                ss1.setSpan(new RelativeSizeSpan(2f), 0,5, 0); // set size
                ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, 0);// set color */
                String displaystring = "\n";

                AllInfoSQL s = new AllInfoSQL(MainActivity.exptime);
                ArrayList<String> labdata = new ArrayList<String>();
                try {
                    s.execute(MainActivity.currentUser);
                    labdata = s.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < labdata.size(); i++ ){
                    displaystring += labdata.get(i) + "\n";
                }

                expinfo.setText(displaystring);
                //expinfo.setFocusable(false);
                alert.setView(expinfo);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        //return;
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                // from http://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked
                final AlertDialog dialog = alert.create();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                     //put something here


                        dialog.dismiss();
                        return;
                    }
                });
            }
        });
        /*accelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newExpSelf, AccelSensor.class);
                startActivity(intent);
            }
        });*/
        ambiButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newExpSelf, AmbientSensor.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_experiment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void savetocsv(String dataval){

    }

    public String csvstring(ArrayList<String> datavals){
        String datastring = "";

        return datastring;
    }

    public String displayspan(){
        String displaystring = "";

        return displaystring;
    }
}
