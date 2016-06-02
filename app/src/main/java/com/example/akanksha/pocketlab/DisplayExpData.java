package com.example.akanksha.pocketlab;

import android.app.Activity;
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


public class DisplayExpData extends Activity {

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


