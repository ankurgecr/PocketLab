package com.example.akanksha.pocketlab;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * Created by asingh95 on 5/2/2016.
 */
public class AmbientBulbs extends PApplet {

    PFont font;

    int lumens = 1775;      //input lumens from sensor
    int MAX_LUMENS = 2500;  //determines matrix size (sqrt(MAX_LUMENS),sqrt(MAX_LUMENS))
    //matrix must always be a square value and a multiple of a 100
    //one bulb = 100 lumens

    int m_size= 1;          //max row and col length
    int row = 0;
    int col = 0;

    int lit_bulbs = 0;      //number of bulbs that need to be lit
    int bulbs = 0;          //number of bulbs that are lit
    int inc, radius;        //increment value of bulb positions and bulb radius size
    int startX, startY;     //(0,0) bulb position in matrix
    int expand = 1;         //expanding value of light radius when a bulb is being lit; depends on i and radius
    int i = 1;

    int dimness = 0;
    int dim_value = 5;

    public void settings()
    {
        size(width-100,width-100);
    }

    public void setup()
    {
        font = createFont("sans-serif-light",56);
        textFont(font);

        background(0);

        lit_bulbs = lumens/100;

        dimness = lumens%100;


        if (dimness > 0)
        {
            dim_value = dimness/20;
            lit_bulbs++;
        }//there is a bulb lit at below 100 lumens

        if((lit_bulbs == 0)&&(lumens>0))
        {
            lit_bulbs = 1;
        }//there is a single bulb to be turned on

        if(lit_bulbs>(MAX_LUMENS/100))
        {
            dim_value = 5;
            lit_bulbs = MAX_LUMENS/100;
        }//the input lumens is greater than the max number of available bulbs

        //print("lit_bulbs"+lit_bulbs+" ");
        if(MAX_LUMENS <= 100)
            m_size = 1;      //build a 1x1 matrix if MAX_LUMENS is below the max lumen of one bulb
        else
            m_size = round(sqrt(MAX_LUMENS/100));

        inc = 150;
        radius = (width/(width/1000))/12;
        expand = i*(radius/5);

        if (m_size == 1) //for single bulb place it in center of canvas
        {
            startX = (width/2);
            startY = (height/2);
        }
        else
        {
            startX = (width/m_size)+inc;
            startY = (height/m_size)+inc;
        }
        emptyGrid(startX, startY);
    }

    public void draw()
    {
        frameRate(5);
        //color on;
        //on = color(255,231,22,255-(40*i));
        textAlign(CENTER);
        fill(255);
        text(lumens+" lumens",width/2,height/16);
        if(lumens > 0)
        {
            if(lit_bulbs==1)
            {
                if(i<dim_value)
                {
                    Bulb(startX+(row*inc),startY+(col*inc),expand,1);
                    i++;
                    expand = i*(radius/5);
                }
                else
                if(i>=dim_value)
                    bulbs++;
            }//if there is only one bulb

            if(bulbs == 0)
            {
                if(expand<=radius)
                {
                    Bulb(startX+(row*inc),startY+(col*inc),expand,1);
                    i++;
                    expand = i*(radius/5);
                }
                else
                if(expand>radius)
                {
                    bulbs = 1;
                    bulbs++;
                    col++;
                    i = 1;
                    expand = i*(radius/5);
                }
            }//if we're at the very first bulb
            if(bulbs > 0)
            {
                if((bulbs<lit_bulbs)&&(row<m_size))
                {
                    if(col<m_size)
                    {
                        Bulb(startX+(row*inc),startY+(col*inc),expand,1);
                        i++;
                        expand = i*(radius/5);
                        if(expand<=radius)
                        {
                            Bulb(startX+(row*inc),startY+(col*inc),expand,1);
                            i++;
                            expand = i*(radius/5);
                        }
                        else
                        if(expand > radius)
                        {
                            bulbs++;
                            col++;
                            i = 1;
                            expand = i*(radius/5);
                        }
                    }//while we're not at the end of the column
                    else
                    {
                        row++;
                        col=0;
                    }//if we've reached the end of the column
                }//if there are more bulbs

                if(bulbs == lit_bulbs)
                {
                    if(i<=dim_value)
                    {
                        if(col >= m_size)
                        {
                            row++;
                            col = 0;
                        }
                        Bulb(startX+(row*inc),startY+(col*inc),expand,1);
                        i++;
                        expand = i*(radius/5);
                    }
                }//if at last bulb

            }//otherwise check to see if there are more bulbs to be turned on
        }//if there is a lumens value
    }

    void Bulb(int x, int y, int r, int c)
    {
        if (c == 0)
            fill(255,255,255,50);
        else
            fill(255,231,22,255-(40*i));
        stroke(c);
        ellipse(x, y, r, r);
    }//Bulb()

    void emptyGrid(int firstX, int firstY)
    {
        int i = 0;
        int j = 0;
        //color off;
        //off = color(255,255,255,50);

        for(i = 0; i<m_size; i++)
        {
            for(j = 0; j<m_size; j++)
            {
                Bulb(firstX+(i*inc),firstY+(j*inc),radius,0);
            }//for
        }//for
    }//emptyGrid()

}
