package com.example.akanksha.pocketlab;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * Created by asingh95 on 5/2/2016.
 */
public class AmbientBulbs extends PApplet
{
    AmbientSensor myActivity;

    PFont font;

    int lumens = 17750;      //input lumens from sensor

    int MAX_LUMENS = 64000;  //determines matrix size (sqrt(MAX_LUMENS),sqrt(MAX_LUMENS))
    //matrix must always be a square value and a multiple of a 100
    //one bulb = 100 lumens

    int m_size= 1;          //max row and col length
    int row = 0;
    int col = 0;

    int lit_bulbs = 0;      //number of bulbs that need to be lit
    int bulbs = 0;          //number of bulbs that are lit
    int inc;
    float radius;        //increment value of bulb positions and bulb radius size
    int startX, startY;     //(0,0) bulb position in matrix
    float expand = 1;         //expanding value of light radius when a bulb is being lit; depends on i and radius
    int i = 1;

    int dimness = 0;
    int dim_value = 5;

    public void settings()
    {
        size(width-50,width+100);
    }

    public void setup()
    {

        myActivity = (AmbientSensor) getActivity();

        font = createFont("sans-serif-light",56);
        textFont(font);

        background(0);

        lit_bulbs = lumens/1000;

        dimness = lumens%1000;

        if (dimness > 0)
        {
            dim_value = dimness/200;
            lit_bulbs++;
        }//there is a bulb lit at below 100 lumens

        if((lit_bulbs == 0)&&(lumens>0))
        {
            lit_bulbs = 1;
        }//there is a single bulb to be turned on

        if(lit_bulbs>(MAX_LUMENS/1000))
        {
            dim_value = 5;
            lit_bulbs = MAX_LUMENS/1000;
        }//the input lumens is greater than the max number of available bulbs

        //print("lit_bulbs"+lit_bulbs+" ");
        if(MAX_LUMENS <= 1000)
            m_size = 1;      //build a 1x1 matrix if MAX_LUMENS is below the max lumen of one bulb
        else
            m_size = round(sqrt(MAX_LUMENS/1000));

        inc = 100;
        radius = (width/(float)((width/1000)))/15;
        expand = i*(radius/5);

        if (m_size == 1) //for single bulb place it in center of canvas
        {
            startX = (width/2);
            startY = (height/2);
        }
        else
        {
            if(m_size > 6)
            {
                startX = (width/m_size);
                startY = (height/m_size);
            }
            else
            {
                startX = (width / m_size) + inc;
                startY = (height / m_size) + inc;
            }
        }
        emptyGrid(startX, startY);
    }

    public void draw()
    {
        lumens = (int) myActivity.getCurrentLumens();

        frameRate(5);
        //color on;
        //on = color(255,231,22,255-(40*i));
        textAlign(CENTER);
        fill(255);
        textSize(75);
        text(lumens + " lumens", width/2, height/16);
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

    void Bulb(float x, float y, float r, int c)
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
        int i, j;
        //color off;
        //off = color(255,255,255,50);

        for(i = 0; i<m_size; i++)
        {
            for(j = 0; j<m_size; j++)
            {
                Bulb(firstX+(i*inc),firstY+(j*inc),radius,0);
            }//for
        }//for
        drawScale(firstX);
    }//emptyGrid()

    void drawScale(int firstX)
    {
        Bulb(firstX+(5*inc),height-(radius+5),radius,0);
        textAlign(CENTER);
        fill(255);
        textSize(40);
        text("0L", firstX + (5 * inc), height - 5);

        Bulb(firstX + (4 * inc), height - (radius + 5), radius, 0);
        Bulb(firstX + (4 * inc), height - (radius + 5), (radius / 5), 1);
        textAlign(CENTER);
        fill(255);
        textSize(40);
        text(">200L", firstX + (4 * inc), height - 5);

        Bulb(firstX + (3 * inc), height - (radius + 5), radius, 0);
        Bulb(firstX + (3 * inc), height - (radius + 5), (radius / 5), 1);
        Bulb(firstX + (3 * inc), height - (radius + 5), 2 * (radius / 5), 1);
        textAlign(CENTER);
        fill(255);
        textSize(40);
        text(">400L", firstX + (3 * inc), height - 5);

        Bulb(firstX + (2 * inc), height - (radius + 5), radius, 0);
        Bulb(firstX + (2 * inc), height - (radius + 5), (radius / 5), 1);
        Bulb(firstX + (2 * inc), height - (radius + 5), 2 * (radius / 5), 1);
        Bulb(firstX + (2 * inc), height - (radius + 5), 3 * (radius / 5), 1);
        textAlign(CENTER);
        fill(255);
        textSize(40);
        text(">600L",firstX+(2*inc),height-5);

        Bulb(firstX+(inc),height-(radius+5),radius,0);
        Bulb(firstX+(inc),height-(radius+5),(radius/5),1);
        Bulb(firstX+(inc),height-(radius+5),2*(radius/5),1);
        Bulb(firstX+(inc),height-(radius+5),3*(radius/5),1);
        Bulb(firstX+(inc),height-(radius+5),4*(radius/5),1);
        textAlign(CENTER);
        fill(255);
        textSize(40);
        text(">800L",firstX+(inc),height-5);

        Bulb(firstX,height-(radius+5),radius,1);
        Bulb(firstX,height-(radius+5),(radius/5),1);
        Bulb(firstX,height-(radius+5),2*(radius/5),1);
        Bulb(firstX,height-(radius+5),3*(radius/5),1);
        Bulb(firstX,height-(radius+5),4*(radius/5),1);
        textAlign(CENTER);
        fill(255);
        textSize(40);
        text("1000L",firstX,height-5);

    }//drawScale()

}
