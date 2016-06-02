package com.example.akanksha.pocketlab;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by asingh95 on 5/2/2016.
 */
public class AmbientSensor extends Activity{

    Button sensorMenuButton;

    Activity newExpSelf = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambient_sensor);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new AmbientBulbs();
        fragmentManager.beginTransaction()
                .replace(R.id.container5, fragment)
                .commit();

        sensorMenuButton = (Button) findViewById(R.id.sensors_button);
        sensorMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newExpSelf, NewExperiment.class);
                startActivity(intent);
            }
        });
    }
}
