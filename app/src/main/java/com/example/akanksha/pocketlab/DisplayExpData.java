package com.example.akanksha.pocketlab;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class DisplayExpData extends ActionBarActivity {

    TextView textdispl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_exp_data);

        textdispl = (TextView) findViewById(R.id.textexpdisplay);

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

        SpannableString displaystring = displayspan(labdata);
        textdispl.setText(displaystring);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_exp_data, menu);
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

    public SpannableString displayspan(ArrayList<String> datafromsql){ //get the string to display

        SpannableString displaystring = new SpannableString("");

        for (int i = 0; i < datafromsql.size(); i++ ){
            //int i=0;

            String tempstr = datafromsql.get(i) + "\n";
            SpannableString tempspan = new SpannableString(tempstr);

            if (i % 2 == 0){ //even, or titles of sections
                tempspan.setSpan(new RelativeSizeSpan(1.25f), 0,tempstr.length() - 1, 0);
                tempspan.setSpan(new ForegroundColorSpan(Color.BLUE), 0, tempstr.length() - 1, 0);
            }

            displaystring = new SpannableString(TextUtils.concat(displaystring, tempspan));

        }

        return displaystring;
    }
}


