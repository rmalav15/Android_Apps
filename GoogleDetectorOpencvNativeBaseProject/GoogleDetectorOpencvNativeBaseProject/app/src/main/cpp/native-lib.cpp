#include <jni.h>
#include <string>


#include<iostream>
#include <jni.h>
#include <android/log.h>
#include <opencv2/opencv.hpp>

using namespace std;
using namespace cv;

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_abhishekyadav_googledetectoropencvnativebaseproject_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


int width = 640;
int height = 480;
float Image2dCord[12];

extern "C"
JNIEXPORT jdoubleArray JNICALL
Java_com_example_abhishekyadav_googledetectoropencvnativebaseproject_FaceGraphic_PoseEstimate(
        JNIEnv *env, jobject instance, jdoubleArray  landmarkCord_) {
    jdoubleArray Array   = (env)->NewDoubleArray(4);

    jdouble *landmarkCord = env->GetDoubleArrayElements(landmarkCord_, NULL);

    int i;

    for (i=0; i < 12; i++)
        Image2dCord[i] = landmarkCord[i];


    // 2D image points. If you change the image, you need to change vector
    std::vector<cv::Point2d> image_points;
    for (i=0; i < 12; i++)
        image_points.push_back( cv::Point2d(Image2dCord[i], Image2dCord[++i]) );
    /*0,1   // Nose tip
    2,3     // Chin
    4,5     // Left eye left corner
    6,7     // Right eye right corner
    8,9    // Left Mouth corner
    10,11   // Right mouth corner*/

    // 3D model points.
    std::vector<cv::Point3d> model_points;
    model_points.push_back(cv::Point3d(0.0f, 0.0f, 0.0f));               // Nose tip
    model_points.push_back(cv::Point3d(0.0f, -330.0f, -65.0f));          // Chin
    model_points.push_back(cv::Point3d(-225.0f, 170.0f, -135.0f));       // Left eye left corner
    model_points.push_back(cv::Point3d(225.0f, 170.0f, -135.0f));        // Right eye right corner
    model_points.push_back(cv::Point3d(-150.0f, -150.0f, -125.0f));      // Left Mouth corner
    model_points.push_back(cv::Point3d(150.0f, -150.0f, -125.0f));       // Right mouth corner



    // Camera internals
    double focal_length = width; // Approximate focal length.
    Point2d center = cv::Point2d(width/2,height/2);
    typedef cv::Matx<double, 3, 3> Mat33d;
    Mat33d camera_matrix(focal_length, 0, center.x, 0, focal_length, center.y, 0, 0, 1);



    cv::Mat dist_coeffs = cv::Mat::zeros(4,1,cv::DataType<double>::type); // Assuming no lens distortion


    // Output rotation and translation
    cv::Mat rotation_vector; // Rotation in axis-angle form
    cv::Mat translation_vector;

    // Solve for pose
    cv::solvePnP(model_points, image_points, camera_matrix, dist_coeffs, rotation_vector, translation_vector);

    // Project a 3D point (0, 0, 1000.0) onto the image plane.
    // We use this to draw a line sticking out of the nose

    vector<Point3d> nose_end_point3D;
    vector<Point2d> nose_end_point2D;
    nose_end_point3D.push_back(Point3d(0,0,1000.0));

    projectPoints(nose_end_point3D, rotation_vector, translation_vector, camera_matrix, dist_coeffs, nose_end_point2D);

  /*  for(int i=0; i < image_points.size(); i++)
    {
        circle(im, image_points[i], 3, Scalar(0,0,255), -1);
    }

    cv::line(im,image_points[0], nose_end_point2D[0], cv::Scalar(255,0,0), 2);
*/
    double fibo[2];
    fibo[0]= image_points[0].x;
    fibo[0]= image_points[0].y;
    fibo[2] = nose_end_point2D[0].x;
    fibo[3] = nose_end_point2D[0].y;

    (env)->SetDoubleArrayRegion(Array,0,2,fibo);

    env->ReleaseDoubleArrayElements(landmarkCord_, landmarkCord, 0);
}