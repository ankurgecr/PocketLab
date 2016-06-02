package com.example.akanksha.pocketlab;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class VariableSensorAct extends Activity {

    Button getdata;
    EditText pluspin;
    EditText grndpin;
    EditText readpin;
    Activity mself = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variable_sensor);

        getdata = (Button) findViewById(R.id.button3);
        pluspin = (EditText) findViewById(R.id.editText);
        grndpin = (EditText) findViewById(R.id.editText1);
        readpin = (EditText) findViewById(R.id.editText2);


        getdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pluspinnum = -1;
                int grndpinnum = -1;
                int readpinnum = -1;

                try {
                    pluspinnum = Integer.parseInt(pluspin.getText().toString());
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }

                try {
                    grndpinnum = Integer.parseInt(grndpin.getText().toString());
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }

                try {
                    readpinnum = Integer.parseInt(readpin.getText().toString());
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }

                if (pluspinnum == -1 || grndpinnum == -1 || readpinnum == -1){
                    return;
                }
                else if (pluspinnum < 1 || pluspinnum > 48 || grndpinnum < 1 || grndpinnum > 48 || readpinnum < 31 || readpinnum > 46 || readpinnum == grndpinnum || grndpinnum == pluspinnum || pluspinnum == readpinnum) { //not in ranges
                    return;
                }
                Intent intent = new Intent(mself, VariSensor.class);
                intent.putExtra("plus", pluspinnum);
                intent.putExtra("grnd", grndpinnum);
                intent.putExtra("read", readpinnum);
                startActivity(intent);

            }
        });
    }

}
