package com.example.akanksha.pocketlab;

import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;


public class CameraActivity extends ActionBarActivity {
    // from http://blog.rhesoft.com/2015/04/02/tutorial-how-to-use-camera-with-android-and-android-studio/
    private Camera mCamera = null;
    private CameraView mCameraView = null;
    Button whiteBalanceButton;
    private boolean whiteBalanceLocked = false;

    @Override
    protected void onStart(){
        super.onStart();
        try{
            mCamera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e){
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }
        //final Camera.Parameters p = camera.getParameters();
        //SurfaceView preview = (SurfaceView)findViewById(R.id.PREVIEW);
        //SurfaceHolder mHolder = preview.getHolder();
        //mHolder.addCallback(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        try {
            try {
                mCamera = Camera.open();//you can use open(int) to use different cameras
            } catch (Exception e) {
                Log.d("ERROR", "Failed to get camera: " + e.getMessage());
            }
            //mCamera.setPreviewCallback(null);

        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        try
        {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();

        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        try{
            mCamera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e){
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if(mCamera != null) {
            mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            FrameLayout camera_view = (FrameLayout)findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout
        }

        whiteBalanceButton = (Button) findViewById(R.id.white_balance_button);
        whiteBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Camera.Parameters params = mCamera.getParameters();
                if (params.isAutoWhiteBalanceLockSupported()) {
                    params.setAutoWhiteBalanceLock(!whiteBalanceLocked);
                    whiteBalanceLocked = !whiteBalanceLocked;
                    if(!whiteBalanceLocked)
                    {
                        whiteBalanceButton.setText("X");
                    }
                    else
                    {
                        whiteBalanceButton.setText("");
                    }
                }
                mCamera.setParameters(params);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
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
}
