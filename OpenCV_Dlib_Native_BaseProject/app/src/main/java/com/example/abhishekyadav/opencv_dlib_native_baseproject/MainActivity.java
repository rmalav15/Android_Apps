package com.example.abhishekyadav.opencv_dlib_native_baseproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import static org.opencv.imgproc.Imgproc.resize;



public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    private static final String TAG = "MainActivity";
    JavaCameraView javaCameraView;
    Mat mRgba,mGray,im_small;
    static int Glob=0;
     int SKIP_FRAMES = 20;
     static long count = 0;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        if(Glob==0)
        {
            initDetectors();
            Glob=1;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.ac);
        setContentView(R.layout.activity_main);

        javaCameraView = (JavaCameraView)findViewById(R.id.java_camera_view);
        Log.i(TAG, "onCreate: 2");
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        Log.i(TAG, "onCreate: 3");
        javaCameraView.setCvCameraViewListener(this);

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native void ConvertGray(long addrInput,long addrOutput);
    public native  static void initDetectors();
    public native void LandmarkDetection(long addrInput,long addrOutput);

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
        im_small = new Mat(height,width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        Log.i(TAG, "onCameraViewStopped: ");
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        count++;
        Log.i(TAG, "onCameraFrame: "+ count);
        //mRgba = inputFrame.gray();
        if ( count % SKIP_FRAMES != 0 )
        {
            ConvertGray(mRgba.getNativeObjAddr(),mGray.getNativeObjAddr());
            return mGray;
        }
        //resize(mRgba, im_small, Size(), 1.0/FACE_DOWNSAMPLE_RATIO, 1.0/FACE_DOWNSAMPLE_RATIO);
        //ConvertGray(mRgba.getNativeObjAddr(),mGray.getNativeObjAddr());
        LandmarkDetection(mRgba.getNativeObjAddr(),mGray.getNativeObjAddr());
        return mGray;
    }
}
