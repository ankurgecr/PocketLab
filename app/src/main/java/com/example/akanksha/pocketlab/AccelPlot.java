package com.example.akanksha.pocketlab;

import processing.core.PApplet;

/**
 * Created by asingh95 on 3/9/2016.
 */
public class AccelPlot extends PApplet {

    Data[] plot1 = new Data[50];

    double g_width, g_height;
    double grid_w, grid_h;
    int i = 0;
    double last;
    double init;

    public void settings()
    {
        size(1000,1280);
    }

    public void setup()
    {
        background(2, 142, 151);
        GetData();
        Grid();
    }

    public void draw()
    {
        if(i<50)
        {
            plot1[i].display();
            plot1[i].fillGraph();
            drawLine(i);
            i++;
        }
        else
            i = 0;
    }

    void Grid()
    {
        strokeWeight(1);
        fill(255);
        rect(100, 50, (float)grid_w, (float)grid_h);
    }

    void GetData()
    {
        double u;
        for (int i =0; i< 50; i++)
        {
            float xnum = i;
            u = i*10;
            float ynum = (float)u;

            if (i == 0)
            {
                init = ynum;
            }

            plot1[i] = new Data(xnum, ynum);

            if(i == 0)
            {
                g_width = (xnum*10)+100;
                g_height = ((ynum-init)*2)+50;
            }
            else
            {
                if ((xnum*10)+100> g_width)
                    g_width =(xnum*10)+100;
                if(((ynum-init)*2)+50 > g_height)
                    g_height = ((ynum-init)*2)+50;
            }
        }//for

        grid_w = g_width - plot1[0].xpos;
        grid_h = g_height - plot1[0].ypos;
        last = plot1[49].ypos;
        System.out.println(grid_w+" grid_w; "+grid_h+" grid_h");
    }//GetData()

    class Data
    {
        float xpos;
        float ypos;

        Data(float x, float y)
        {
            xpos = (x*10)+100;
            ypos = (float)((y-init)*2 +50);

        }//constructor

        void display()
        {
            stroke(255,0,0);
            fill(0);
            ellipse(xpos, ypos, 5, 5);
        }//display()

        void fillGraph()
        {
            strokeWeight(3);
            stroke(255,195,61);
            line(xpos, ypos, xpos, (float)last);
        }//fillGraph()
    }//class::Data

    void drawLine(int i)
    {
        if(i <= 48)
        {
            stroke(255,0,0);
            line(plot1[i].xpos, plot1[i].ypos, plot1[i+1].xpos, plot1[i+1].ypos);
        }
    }//drawLine()

}
