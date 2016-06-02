package com.example.akanksha.pocketlab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by asingh95 on 6/1/2016.
 */
public class GuidePage extends ActionBarActivity {

    Activity newExpSelf = this;
    Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);

        homeButton = (Button) findViewById(R.id.home_button);

        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(newExpSelf, HomeScreen.class);
                startActivity(intent);
            }
        });
    }

}
