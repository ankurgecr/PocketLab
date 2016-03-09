package com.example.akanksha.pocketlab;

import processing.core.PApplet;

/**
 * Created by asingh95 on 3/9/2016.
 */
public class Thermometer extends PApplet {

    int j = 0;
    int degree = 50;

    @Override
    public void settings()
    {
        size(1000,1280);
    }

    @Override
    public void setup()
    {
        background(200);
        thermoScale();
        // font = loadFont("ArialNarrow-Bold-32.vlw");
        // textFont(font);
        drawTempMarker();
    }

    @Override
    public void draw()
    {
        int marker;
        temperature0();
        marker = degree*5;
        if(j<marker)
        {
            temperatureInc(j);
            j++;
        }//if
    }

    void thermoScale()
    {
        noStroke();
        fill(255);
        rect(440, 225, 120, 700);

        noStroke();
        fill(0, 0,255);
        ellipse(500, 925, 225, 225);
    }//thermoScale()

    void temperature0()
    {
        noStroke();
        fill(0,0,255);
        rect(440, 810, 120, 25);
    }//temperature0()

    void temperatureInc(int i)
    {
        noStroke();
        fill(i,0,(255-i));
        rect(440, (810 - (i-1)), 120, 25);
    }//temperatureInc()

    void drawTempMarker()
    {

        for(int a = 0; a < 6; a++)
        {
            strokeWeight(4);
            stroke(0);
            line(440, 810-(a*100), 410, 810-(a*100));

        }//for

        for(int a = 0; a < 5; a++)
        {
            strokeWeight(2);
            stroke(0);
            line(440, 760-(a*100), 420, 760-(a*100));

        }//for

        for(int a = 0; a <10; a++)
        {
            strokeWeight(1);
            stroke(0);
            line(440, 785-(a*50), 430, 785-(a*50));
        }//for
    }//drawTempMarker()
}
