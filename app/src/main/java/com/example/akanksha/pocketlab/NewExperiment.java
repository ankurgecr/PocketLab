package com.example.akanksha.pocketlab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class NewExperiment extends ActionBarActivity {
    Button temperatureButton;
    Button humidityButton;
    Button colorButton;
    Button accelButton;
    Button ambiButton;
    Activity newExpSelf = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_experiment);

        temperatureButton = (Button) findViewById(R.id.temperature_button);
        humidityButton = (Button) findViewById(R.id.humid_button);
        colorButton = (Button) findViewById(R.id.color_button);
        accelButton = (Button) findViewById(R.id.accel_button);
        ambiButton = (Button) findViewById(R.id.ambient_button);

        temperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newExpSelf, TemperatureSensor.class);
               // Intent intent = new Intent(newExpSelf, TemperatureSensorBoard.class);
                startActivity(intent);
            }
        });
        humidityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newExpSelf, HumiditySensor.class);
                startActivity(intent);
            }
        });
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newExpSelf, ColorSensor.class);
                startActivity(intent);
            }
        });
        accelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newExpSelf, AccelSensor.class);
                startActivity(intent);
            }
        });
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
}
