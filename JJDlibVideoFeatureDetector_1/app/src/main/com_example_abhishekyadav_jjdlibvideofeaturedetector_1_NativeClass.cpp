#include<com_example_abhishekyadav_jjdlibvideofeaturedetector_1_NativeClass.h>
#include <android/log.h>
 #include <thread>
thread_local frontal_face_detector detector ;
 shape_predictor pose_model;

using namespace dlib;
using namespace std;
using namespace cv;

/*
 * Class:     com_example_abhishekyadav_jjdlibvideofeaturedetector_1_NativeClass
 * Method:    getMessage
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_example_abhishekyadav_jjdlibvideofeaturedetector_11_NativeClass_getMessage
  (JNIEnv *env, jclass){
  env->NewStringUTF("JNI message");
  }


JNIEXPORT jstring JNICALL Java_com_example_abhishekyadav_jjdlibvideofeaturedetector_11_NativeClass_initDetectors
  (JNIEnv *env, jclass){
        __android_log_print(ANDROID_LOG_INFO, "MainActivity", "Hellowrold");
        if(detector.num_detectors()==0 )
           {
                __android_log_print(ANDROID_LOG_INFO, "MainActivity", "num detectors 0");
               detector = get_frontal_face_detector();
                if(detector.num_detectors()>0 )
                    __android_log_print(ANDROID_LOG_INFO, "MainActivity", "fetched the detectors successfully");
                else
                    __android_log_print(ANDROID_LOG_INFO, "MainActivity", "tried to fetch but failed");
           }
        else
             __android_log_print(ANDROID_LOG_INFO, "MainActivity", "detectors already are non zero");
        deserialize("storage/emulated/0/shape_predictor_68_face_landmarks.dat")>>pose_model;
        __android_log_print(ANDROID_LOG_INFO, "MainActivity", "end");

  }

void toGray(Mat img, Mat& gray)
    {
        cvtColor(img,gray,CV_RGBA2GRAY);
    }

/*
 * Class:     com_example_abhishekyadav_jjdlibvideofeaturedetector_1_NativeClass
 * Method:    LandmarkDetection
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_com_example_abhishekyadav_jjdlibvideofeaturedetector_11_NativeClass_LandmarkDetection
  (JNIEnv *env, jclass thiz, jlong addrInput, jlong addrOutput){
    __android_log_print(ANDROID_LOG_INFO, "MainActivity", "jitu");
    Mat& image = *(Mat*) addrInput;
    Mat& dst = *(Mat*) addrOutput;
__android_log_print(ANDROID_LOG_INFO, "MainActivity", "jitu2");
    faceDetectionDlib(image,dst);
    //toGray(image,dst);

__android_log_print(ANDROID_LOG_INFO, "MainActivity", "jitu 3");

  }



void faceDetectionDlib(Mat &img , Mat &dst){

    //try{
        //image_window win
        //frontal_face_detector detector = get_frontal_face_detector();
        //shape_predictor pose_model;
        //deserialize("storage/emulated/0/shape_predictor_68_face_landmarks.dat")>>pose_model;
        __android_log_print(ANDROID_LOG_INFO, "MainActivity", "faceDetectionDlib start0");
        cv_image<bgr_pixel> cimg(img);
        __android_log_print(ANDROID_LOG_INFO, "MainActivity", "faceDetectionDlib start");
        // Detect faces using dlib
        std::vector<dlib::rectangle> faces = detector(cimg,0);
        // find the pose of each face using dlib
        __android_log_print(ANDROID_LOG_INFO, "MainActivity", "faceDetectionDlib detected");
        std::vector<full_object_detection> shapes;

        for(unsigned long i=0; i<faces.size();++i)
            shapes.push_back(pose_model(cimg,faces[i]));

        __android_log_print(ANDROID_LOG_INFO, "MainActivity", "faceDetectionDlib 1");
         // render to mat
         dst = img.clone();
         renderToMat(shapes,dst);
         __android_log_print(ANDROID_LOG_INFO, "MainActivity", "faceDetectionDlib rendered");
   // }
    //catch(serialization_error& e)
    //{
      //  cout<< endl << e.what() << endl;
    //}

}

void renderToMat(std::vector<full_object_detection>& dets, Mat& dst)
{
    Scalar color;
    int sz=3;
    color = Scalar(0,255,0);

    for(unsigned long idx = 0; idx< dets.size();idx++)
    {
        for(unsigned long i = 1; i<=16 ;++i)
            cv::line(dst,Point(dets[idx].part(i).x(),dets[idx].part(i).y()),Point(dets[idx].part(i-1).x(),dets[idx].part(i-1).y()),color,sz);

        // for nose
        for(unsigned long i = 28; i<=30 ;++i)
            cv::line(dst,Point(dets[idx].part(i).x(),dets[idx].part(i).y()),Point(dets[idx].part(i-1).x(),dets[idx].part(i-1).y()),color,sz);

        // left eyebrow
        for(unsigned long i = 18; i<=21 ;++i)
            cv::line(dst,Point(dets[idx].part(i).x(),dets[idx].part(i).y()),Point(dets[idx].part(i-1).x(),dets[idx].part(i-1).y()),color,sz);

        // right eyebrow
        for(unsigned long i = 23; i<=26 ;++i)
            cv::line(dst,Point(dets[idx].part(i).x(),dets[idx].part(i).y()),Point(dets[idx].part(i-1).x(),dets[idx].part(i-1).y()),color,sz);

        // bottom part of the nose
         for(unsigned long i = 31; i<=35 ;++i)
            cv::line(dst,Point(dets[idx].part(i).x(),dets[idx].part(i).y()),Point(dets[idx].part(i-1).x(),dets[idx].part(i-1).y()),color,sz);
        // line from the nose to the bottom part  above
        cv::line(dst,Point(dets[idx].part(30).x(),dets[idx].part(30).y()),Point(dets[idx].part(35).x(),dets[idx].part(35).y()),color,sz);

        // left eye
        for(unsigned long i = 37; i<=41 ;++i)
            cv::line(dst,Point(dets[idx].part(i).x(),dets[idx].part(i).y()),Point(dets[idx].part(i-1).x(),dets[idx].part(i-1).y()),color,sz);
        cv::line(dst,Point(dets[idx].part(36).x(),dets[idx].part(36).y()),Point(dets[idx].part(41).x(),dets[idx].part(41).y()),color,sz);

        // left eye
                for(unsigned long i = 37; i<=41 ;++i)
                    cv::line(dst,Point(dets[idx].part(i).x(),dets[idx].part(i).y()),Point(dets[idx].part(i-1).x(),dets[idx].part(i-1).y()),color,sz);
                cv::line(dst,Point(dets[idx].part(36).x(),dets[idx].part(36).y()),Point(dets[idx].part(41).x(),dets[idx].part(41).y()),color,sz);

        // right eye
                for(unsigned long i = 43; i<=47 ;++i)
                    cv::line(dst,Point(dets[idx].part(i).x(),dets[idx].part(i).y()),Point(dets[idx].part(i-1).x(),dets[idx].part(i-1).y()),color,sz);
                cv::line(dst,Point(dets[idx].part(42).x(),dets[idx].part(42).y()),Point(dets[idx].part(47).x(),dets[idx].part(47).y()),color,sz);

        // lips outer part
                for(unsigned long i = 49; i<=59 ;++i)
                    cv::line(dst,Point(dets[idx].part(i).x(),dets[idx].part(i).y()),Point(dets[idx].part(i-1).x(),dets[idx].part(i-1).y()),color,sz);
                cv::line(dst,Point(dets[idx].part(48).x(),dets[idx].part(48).y()),Point(dets[idx].part(59).x(),dets[idx].part(59).y()),color,sz);

        // lips inside part
                for(unsigned long i = 61; i<=67 ;++i)
                    cv::line(dst,Point(dets[idx].part(i).x(),dets[idx].part(i).y()),Point(dets[idx].part(i-1).x(),dets[idx].part(i-1).y()),color,sz);
                cv::line(dst,Point(dets[idx].part(60).x(),dets[idx].part(60).y()),Point(dets[idx].part(67).x(),dets[idx].part(67).y()),color,sz);

    }
}
