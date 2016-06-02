package com.example.akanksha.pocketlab;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class HumiditySensor extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity_sensor);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new HumidPlot();
        fragmentManager.beginTransaction()
                .replace(R.id.container2, fragment)
                .commit();
    }
}
