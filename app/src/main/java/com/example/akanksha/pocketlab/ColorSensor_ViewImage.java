package com.example.akanksha.pocketlab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

// http://stackoverflow.com/questions/24463691/how-to-show-imageview-full-screen-on-imageview-click
public class ColorSensor_ViewImage extends Activity {

    Uri imgUri;
    DrawView imageView;
    TextView textView;
    Button newDataButton;
    Button saveDataButton;
    Button toggleButton;
    View colorBox;
    Bitmap bm;
    int windowHeight;
    int windowWidth;
    Activity photoViewSelf = this;

    float red;
    float green;
    float blue;
    float L;
    float a;
    float b;

    boolean isRGB = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_sensor_view_image);

        imageView = (DrawView) findViewById(R.id.drawView);
        textView = (TextView) findViewById(R.id.textView);
        colorBox = (View) findViewById(R.id.colorBox);

        newDataButton = (Button) findViewById(R.id.button_newData);
        newDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(photoViewSelf, ColorSensor.class);
                startActivity(intent);
            }
        });

        saveDataButton = (Button) findViewById(R.id.button_saveData);
        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long colorTime =  System.currentTimeMillis();
                String data = "";
                if(isRGB)
                {
                    data = colorTime + ":rgb:" + red + "," + green + "," + blue + ";";
                }
                else
                {
                    data = colorTime + ":lab:" + L + "," + a + "," + b + ";";
                }
                SaveDataPointSQL s = new SaveDataPointSQL(MainActivity.exptime);
                s.execute(MainActivity.currentUser, "color",data);
                try {
                    if (s.get() == "Works"){
                        Log.d("DEBUG","Saved new color");
                        if(isRGB)
                        {
                            toast("Saved RGB " + red + "," + green + "," + blue);
                        }
                        else
                        {
                            toast("Saved Lab " + L + "," + a + "," + b);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        toggleButton = (Button) findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRGB = !isRGB;
                if(isRGB)
                {
                    toggleButton.setText("Lab");
                }
                else
                {
                    toggleButton.setText("RGB");
                }

                setColorText();
            }
        });


        try {
            imgUri = Uri.parse(getIntent().getExtras().getString("imgUri"));
            Bitmap photoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);

            // rotate http://android-er.blogspot.com/2010/07/rotate-bitmap-image-using-matrix.html
            int bitmapHeight = photoBitmap.getHeight();
            int bitmapWidth = photoBitmap.getWidth();

            Matrix matrix = new Matrix();
            matrix.postRotate(90);

            bm = Bitmap.createBitmap(photoBitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(bm);

            int[] colorArray = new int[bm.getWidth()*bm.getHeight()];
            bm.getPixels(colorArray, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm.getHeight());

            long reds = 0;
            long greens = 0;
            long blues = 0;
            long numPixels = 0;

            for(int i = 0; i < bm.getWidth(); i++)
            {
                for(int j = 0; j < bm.getHeight(); j++)
                {
                    numPixels++;
                    int c = colorArray[i*bm.getHeight()+j];
                    reds += Color.red(c);
                    greens += Color.green(c);
                    blues += Color.blue(c);
                }
            }

            setColor(((float) reds) / numPixels, ((float) greens) / numPixels, ((float) blues) / numPixels);
        }
        catch(IOException e)
        {
            Log.d("ERROR", "File not found: " + e);
        } // try/catch

    } // onCreate()

    // conversions from http://www.brucelindbloom.com/
    // assume sRGB
    private float[] RGBtoLab(float r, float g, float b)
    {
        float[] XYZ = new float[3];
        float[] Lab = new float[3];

        XYZ = RGBtoXYZ(r,g,b);
        Lab = XYZtoLab(XYZ[0],XYZ[1],XYZ[2]);

        return Lab;
    }

    private float[] RGBtoXYZ(float r, float g, float b)
    {
        float[] XYZ = new float[3];

        r = r/255f;
        g = g/255f;
        b = b/255f;

        r = r <= 0.04045 ? r/12 : (float)Math.pow((r+0.055)/1.055,2.4);
        g = g <= 0.04045 ? g/12 : (float)Math.pow((g+0.055)/1.055,2.4);
        b = b <= 0.04045 ? b/12 : (float)Math.pow((b+0.055)/1.055,2.4);

        XYZ[0] = 0.4360747f*r + 0.3850649f*g + 0.1430804f*b; // X
        XYZ[1] = 0.2225045f*r + 0.7168786f*g + 0.0606169f*b; // Y
        XYZ[2] = 0.0139322f*r + 0.0971045f*g + 0.7141733f*b; // Z

        return XYZ;
    }

    private float[] XYZtoLab(float X, float Y, float Z)
    {
        float[] Lab = new float[3];
        float fx, fy, fz;
        float xr, yr, zr;
        float e = 0.008856f;
        float k = 903.3f;

        xr = X/0.964221f;
        yr = 1.0f;
        zr = 0.825211f;

        fx = xr > e ? (float) Math.cbrt(xr) : (k*xr + 16f) / 116f;
        fy = yr > e ? (float) Math.cbrt(yr) : (k*yr + 16f) / 116f;
        fz = zr > e ? (float) Math.cbrt(zr) : (k*zr + 16f) / 116f;

        Lab[0] = 116*fy - 16; // L
        Lab[1] = 500*(fx-fy); // a
        Lab[2] = 200*(fy-fz); // b

        return Lab;
    }

    public int getBitmapPixel(int x,int y)
    {
        return bm.getPixel(x,y);
    }

    public int getWindowHeight()
    {
        return windowHeight;
    }

    public int getWindowWidth()
    {
        return windowWidth;
    }

    private void setColorText()
    {
        String s = "";
        if(this.isRGB)
        {
            s = "Red: " + this.red + "\nGreen: " + this.green + "\nBlue: " + this.blue;
        }
        else
        {
            s = "L: " + this.L + "\na: " + this.a + "\nb: " + this.b;
        }
        textView.setText(s);
    }

    private void setColorboxColor()
    {
        colorBox.setBackgroundColor(Color.rgb((int) this.red, (int) this.green, (int) this.blue));
    }

    public void setColor(float r, float g, float b)
    {
        this.red = r;
        this.green = g;
        this.blue = b;

        float[] Lab = new float[3];
        Lab = RGBtoLab(r,g,b);
        this.L = Lab[0];
        this.a = Lab[1];
        this.b = Lab[2];

        setColorboxColor();
        setColorText();
    }

    private void toast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, message, duration);
                toast.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        File f = new File(imgUri.getPath());
        if(f.exists())
        {
            f.delete();
        }
        super.onBackPressed();
        return;
    }

    @Override
    public void onStop()
    {
        File f = new File(imgUri.getPath());
        if(f.exists())
        {
            f.delete();
        }
        super.onStop();
        return;
    }
} // class ColorSensor_ViewImage
