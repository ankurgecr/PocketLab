package com.example.akanksha.pocketlab;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.Math;

// http://stackoverflow.com/questions/24463691/how-to-show-imageview-full-screen-on-imageview-click
public class PhotoView extends Activity {

    Uri imgUri;
    DrawView imageView;
    //ImageView imageView;
    //TextView textView;
    boolean isImageFitToScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        imageView = (DrawView) findViewById(R.id.drawView);
        //imageView = (ImageView) findViewById(R.id.imageView);
        //textView = (TextView) findViewById(R.id.textView);

        try {
            imgUri = Uri.parse(getIntent().getExtras().getString("imgUri"));
            Bitmap photoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);

            // rotate http://android-er.blogspot.com/2010/07/rotate-bitmap-image-using-matrix.html
            int bitmapHeight = photoBitmap.getHeight();
            int bitmapWidth = photoBitmap.getWidth();

            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            int windowHeight = size.y;
            int windowWidth = size.x;

            float scaleHeight = ((float) windowHeight/bitmapHeight);
            float scaleWidth = ((float) windowWidth/bitmapWidth);

            Matrix matrix = new Matrix();
            //matrix.postScale(scaleWidth, scaleHeight);
            //matrix.postScale(1,1);
            matrix.postRotate(90);

            Bitmap resizedBitmap = Bitmap.createBitmap(photoBitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(resizedBitmap);

            long reds = 0;
            long greens = 0;
            long blues = 0;
            long numPixels = 0;

            for(int i = 0; i < windowWidth; i++)
            {
                for(int j = 0; j < windowHeight; j++)
                {
                    numPixels++;
                    int c = resizedBitmap.getPixel(i,j);
                    reds += Color.red(c);
                    greens += Color.green(c);
                    blues += Color.blue(c);
                }
            }

            //textView.setText("Red: " + ((float) reds) / numPixels + " Green: " + ((float) greens) / numPixels + " Blue: " + ((float) blues) / numPixels);
            Toast.makeText(this,"Red: " + ((float) reds) / numPixels + " Green: " + ((float) greens) / numPixels + " Blue: " + ((float) blues) / numPixels, Toast.LENGTH_LONG).show();
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
