package com.example.akanksha.pocketlab;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.util.concurrent.ExecutionException;

// http://stackoverflow.com/questions/24463691/how-to-show-imageview-full-screen-on-imageview-click
public class PhotoView extends Activity {

    Uri imgUri;
    DrawView imageView;
    //ImageView imageView;
    TextView textView;
    Button newDataButton;
    Button saveDataButton;
    //boolean isImageFitToScreen;
    View colorBox;
    Bitmap bm;
    int windowHeight;
    int windowWidth;
    Activity photoViewSelf = this;

    float red;
    float green;
    float blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        imageView = (DrawView) findViewById(R.id.drawView);
        //imageView = (ImageView) findViewById(R.id.imageView);
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
                String data = colorTime + ":rgb:" + red + "," + green + "," + blue + ";";
                SaveDataPointSQL s = new SaveDataPointSQL(MainActivity.exptime);
                s.execute(MainActivity.currentUser, "color",data);
                try {
                    if (s.get() == "Works"){
                        Log.d("DEBUG","Saved new color");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            imgUri = Uri.parse(getIntent().getExtras().getString("imgUri"));
            Bitmap photoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);

            // rotate http://android-er.blogspot.com/2010/07/rotate-bitmap-image-using-matrix.html
            int bitmapHeight = photoBitmap.getHeight();
            int bitmapWidth = photoBitmap.getWidth();

            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            windowHeight = size.y;
            windowWidth = size.x;

            float scaleHeight = ((float) windowHeight/bitmapHeight);
            float scaleWidth = ((float) windowWidth/bitmapWidth);

            Matrix matrix = new Matrix();
            //matrix.postScale(scaleWidth, scaleHeight);
            //matrix.postScale(1,1);
            matrix.postRotate(90);

            bm = Bitmap.createBitmap(photoBitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(bm);

            long reds = 0;
            long greens = 0;
            long blues = 0;
            long numPixels = 0;

            for(int i = 0; i < windowWidth; i++)
            {
                for(int j = 0; j < windowHeight; j++)
                {
                    numPixels++;
                    int c = bm.getPixel(i,j);
                    reds += Color.red(c);
                    greens += Color.green(c);
                    blues += Color.blue(c);
                }
            }

            red = ((float) reds)/numPixels;
            green = ((float) greens) / numPixels;
            blue = ((float) blues) / numPixels;

            setColorText("Red: " + red + "\nGreen: " + green / numPixels + "\nBlue: " + blue);
            //Toast.makeText(this,"Red: " + ((float) reds) / numPixels + " Green: " + ((float) greens) / numPixels + " Blue: " + ((float) blues) / numPixels, Toast.LENGTH_LONG).show();

            setColorboxColor(red, green, blue);
        }
        catch(IOException e)
        {
            Log.d("ERROR", "File not found: " + e);
        }



        /*
        if(isImageFitToScreen) {
            isImageFitToScreen=false;
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            imageView.setAdjustViewBounds(true);
        }else{
            isImageFitToScreen=true;
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        */
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

    public void setColorText(String s)
    {
        textView.setText(s);
    }

    public void setColorboxColor(double r, double g, double b)
    {
        colorBox.setBackgroundColor(Color.rgb((int) r, (int) g, (int) b));
    }

    public void setRed(float r)
    {
        this.red = r;
    }

    public void setGreen(float g)
    {
        this.green = g;
    }

    public void setBlue(float b)
    {
        this.blue = b;
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

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
