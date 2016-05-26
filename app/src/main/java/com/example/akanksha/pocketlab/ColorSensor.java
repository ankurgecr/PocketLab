package com.example.akanksha.pocketlab;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

// http://developer.android.com/training/camera/photobasics.html

public class ColorSensor extends ActionBarActivity {

    static int REQUEST_IMAGE_CAPTURE = 1;

    Button captureButton;
    Activity colorSensorSelf = this;
    //private Camera mCamera = null;
    private CameraView mCameraView = null;
    private String mCurrentPhotoPath;
    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_sensor);

        captureButton = (Button) findViewById(R.id.capture_button);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(colorSensorSelf, CameraActivity.class);
                //startActivity(intent);

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    //File photoFile = null;

                    try{
                        photoFile = createImageFile();
                    }
                    catch(IOException e){
                        Log.d("ERROR", "Error occurred while creating file");
                    }

                    if(photoFile != null)
                    {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        }); // setOnClickListener()
    } // onCreate()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_sensor, menu);
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PocketLab_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Uri imgUri = Uri.fromFile(photoFile);
            Intent intent = new Intent(colorSensorSelf, ColorSensor_ViewImage.class);
            intent.putExtra("imgUri",imgUri.toString());
            startActivity(intent);
        }
    }

    public void clickCapture()
    {
        captureButton.performClick();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(colorSensorSelf, NewExperiment.class);
        startActivity(intent);
    }
}
