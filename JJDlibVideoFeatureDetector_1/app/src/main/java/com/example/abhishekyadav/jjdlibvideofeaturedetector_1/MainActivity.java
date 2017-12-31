package com.example.abhishekyadav.jjdlibvideofeaturedetector_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.lang.annotation.Native;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    private static final String TAG = "MainActivity";

    JavaCameraView javaCameraView;
    Mat mRgba,mGray;
    static int glob=0;

    static{
        System.loadLibrary("MyLibs");
        Log.i(TAG, "static initializer: lib loaded");
        if(glob==0)
        {
            NativeClass.initDetectors();
            Log.i(TAG, "static initializer: init detectors successful");
            glob=1;
        }
        
    }
    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch(status)
            {
                case BaseLoaderCallback.SUCCESS :{
                    Log.i(TAG, "onManagerConnected: SUCCESS");
                    javaCameraView.enableView();
                    break;
                }
                default:
                {
                    super.onManagerConnected(status);
                    Log.i(TAG, "onManagerConnected: default");
                    break;
                }

            }

        }
    };

    static{

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: 1");
        javaCameraView = (JavaCameraView)findViewById(R.id.java_camera_view);
        Log.i(TAG, "onCreate: 2");
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        Log.i(TAG, "onCreate: 3");
        javaCameraView.setCvCameraViewListener( this);
        Log.i(TAG, "onCreate: finished");

        
    }

    @Override
    protected void onPause()
    {
        Log.i(TAG, "onPause: ");
        super.onPause();
        if(javaCameraView!=null)
            javaCameraView.disableView();
    }

    @Override
    protected void onDestroy()
    {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        if(javaCameraView!=null)
            javaCameraView.disableView();
    }

    @Override
    protected void onResume()
    {
        Log.i(TAG, "onResume: ");
        super.onResume();
        if(OpenCVLoader.initDebug())
        {
            Log.i(TAG, "Opencv loaded successfully");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else
        {
            Log.i(TAG, "Opencv not loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0,this,mLoaderCallBack);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        Log.i(TAG, "onCameraViewStarted: ");
        mRgba = new Mat(height,width, CvType.CV_8UC4);
        mGray = new Mat(height,width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        Log.i(TAG, "onCameraViewStopped: ");
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Log.i(TAG, "onCameraFrame: ");
        mRgba = inputFrame.rgba();

        NativeClass.LandmarkDetection(mRgba.getNativeObjAddr(),mGray.getNativeObjAddr());
        return mGray;
    }
}


