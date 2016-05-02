package com.example.akanksha.pocketlab;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * Created by asingh95 on 4/2/2016.
 */
public class SplashLogo extends PApplet {

    int a = 0;
    int x = 0;
    int y = 0;
    int z = 255;

    PFont myFont;

    @Override
    public void settings()
    {
        size(width-100, width-100);
    }

    @Override
    public void setup()
    {
        background(11, 34, 127);
        myFont = createFont("Georgia",width/10);
        EmptyTestTubes();
    }

    @Override
    public void draw()
    {
        frameRate(100);
        if(a<256 && x<256 && y<256 && z>=0)
        {
            Logo(0, y - 65, z - 171);
            TestTubes(a,x,y,z);
            a++;
            x++;
            y++;
            z--;
        }
    }

    void makeTube(int x, int y, int tWidth, int tHeight)
    {
        stroke(200,75);
        line(x, y, x, y+tHeight);
        stroke(200,75);
        line(x+tWidth, y, x+tWidth, y+tHeight);
        stroke(200,75);
        line(x,y,x+tWidth,y);
        stroke(200,75);
        line(x,y+(tHeight/5),x+tWidth,y+(tHeight/5));
        stroke(200,75);
        noFill();
        arc(x+(tWidth/2), y+tHeight,tWidth,tWidth,0,PI);
    }//makeTube()

    void EmptyTestTubes()
    {
        int p_y;
        int tubeW,tubeWSemi,tubeH;

        tubeW = (width/10);
        tubeWSemi = tubeW/2;
        tubeH = tubeW*3;

        p_y = height/3;

        makeTube((width/3)-tubeWSemi,p_y,tubeW,tubeH);
        //left side test tube
        makeTube((width/2)-tubeWSemi,p_y,tubeW,tubeH+50);
        //center test tube
        makeTube((2*(width/3))-tubeWSemi,p_y,tubeW,tubeH);
        //right side test tube

    }//EmptyTestTubes()

    void TestTubes(int Aop, int q, int r, int s)
    {
        int p_y;
        int tubeW,tubeWSemi,tubeH;

        tubeW = (width/10);
        tubeWSemi = tubeW/2;
        tubeH = tubeW*3;

        p_y = height/3;

        noStroke();
        fill(0,q,255,Aop);
        arc((width/3), p_y+tubeH,tubeW,tubeW,0,PI);
        noStroke();
        fill(0,q,255,Aop);
        rect((width/3)-tubeWSemi,p_y+(tubeH/5),tubeW,tubeH-(tubeH/5));
        //left side test tube

        noStroke();
        fill(q,r-100,s,Aop);
        arc((width/2), p_y+(tubeH+50),tubeW,tubeW,0,PI);
        noStroke();
        fill(q,r-100,s,Aop);
        rect((width/2)-tubeWSemi,p_y+((tubeH+50)/5),tubeW,(tubeH+50)-((tubeH+50)/5));
        //center test tube

        noStroke();
        fill(243,s,218,Aop);
        arc(2*(width/3), p_y+tubeH,tubeW,tubeW,0,PI);
        noStroke();
        fill(243,s,218,Aop);
        rect((2*(width/3))-tubeWSemi,p_y+(tubeH/5),tubeW,tubeH-(tubeH/5));
        //right side test tube
    }//TestTubes()

    void Logo(int g, int h, int j)
    {
        textFont(myFont);
        textAlign(CENTER, CENTER);
        fill(g,h,j,a);
        text("PocketLab",width/2,height/5);
    }//Logo()


}
