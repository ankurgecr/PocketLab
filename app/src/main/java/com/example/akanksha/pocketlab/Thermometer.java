package com.example.akanksha.pocketlab;

import android.app.Activity;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * Created by asingh95 on 3/9/2016.
 */
public class Thermometer extends PApplet {

    TemperatureSensor myActivity;

    /*int j = 0;
    int input_degree = 75;
    int degree;*/

    float input_degree = 75;
    float degree;
    int j = 0;
    int celsius = 0;
    float marker;

    PFont font;

    @Override
    public void settings()
    {
        size(1000, 1280);
    }

    @Override
    public void setup()
    {
        myActivity = (TemperatureSensor) getActivity();

        background(11, 34, 127);
        //thermoScale();
        font = createFont("sans-serif-light",22);
        textFont(font);
        //drawTempMarker();

        thermoScale();
        drawTempMarker();

    }

    @Override
    public void draw()
    {
        input_degree = myActivity.getCurrentTemp();
        background(11, 34, 127);
        thermoScale();
        drawTempMarker();
        temperature0();

        if (celsius == 1)
        {
            degree = (5*(input_degree-32))/9;
        }
        else if (celsius == 0)
        {
            degree = input_degree;
        }

        marker = degree*5;
        while(j<marker)
        {
            temperatureInc(j);
            j++;
        }
        j=0;
        displaydegree(degree);
        tapEvent();
    }

    void tapEvent()
    {
        if (mousePressed)
        {
            background(11, 34, 127);
            thermoScale();
            drawTempMarker();
            while (j < marker) {
                temperatureInc(j);
                j++;
            }
            j = 0;
            displaydegree(degree);

            if (celsius == 0) //fahrenheit
            {
                celsius = 1;
            } else if (celsius == 1) //celsius
            {
                celsius = 0;
            }
        }
    }//tapEvent()

    void thermoScale()
    {
        noStroke();
        fill(255);
        rect(440, 225, 120, 700);

        noStroke();
        fill(21,66,245);
        ellipse(500, 925, 225, 225);
    }//thermoScale()

    void temperature0()
    {
        noStroke();
        fill(21,66,245);
        rect(440, 810, 120, 25);
    }//temperature0()

    void temperatureInc(int i)
    {
        noStroke();
        fill(21+(i*2),66-i,(245-i));
        rect(440, (810 - (i - 1)), 120, 25);
    }//temperatureInc()

    void drawTempMarker()
    {
        for(int a = 0; a < 6; a++)
        {
            strokeWeight(4);
            stroke(255);
            line(440, 810 - (a * 100), 410, 810 - (a*100));

            textAlign(RIGHT);
            textSize(32);
            fill(0,255,255);
            text(a * 20, 400, 810 - (a * 100));
        }//for

        for(int a = 0; a < 5; a++)
        {
            strokeWeight(2);
            stroke(255);
            line(440, 760 - (a * 100), 420, 760 - (a*100));

            textAlign(RIGHT);
            textSize(26);
            fill(255);
            text((a * 20) + 10, 415, 760 - (a*100));

        }//for

        for(int a = 0; a <10; a++)
        {
            strokeWeight(1);
            stroke(255);
            line(440, 785-(a*50), 430, 785-(a*50));
        }//for
    }//drawTempMarker()

    void displaydegree(float deg)
    {
        if (celsius == 0)
        {
            textAlign(RIGHT);
            textSize(36);
            fill(245,222,0);
            text("Fahrenheit",(width/3),height/3);
        }
        else if (celsius == 1)
        {
            textAlign(RIGHT);
            textSize(36);
            fill(245,222,0);
            text("Celsius",(width/3),height/3);
        }

        textAlign(LEFT);
        textSize(36);
        fill(255);
        text(deg+" degrees",2*(width/3),height/3);

        textAlign(CENTER);
        textSize(26);
        fill(255);
        text("Tap to convert",(width/2),(height-(height/10)));
    }//displaydegree()


}
