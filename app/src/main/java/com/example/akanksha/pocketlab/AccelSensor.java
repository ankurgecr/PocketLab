package com.example.akanksha.pocketlab;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class AccelSensor extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accel_sensor);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new AccelPlot();
        fragmentManager.beginTransaction()
                .replace(R.id.container1, fragment)
                .commit();
    }
}
