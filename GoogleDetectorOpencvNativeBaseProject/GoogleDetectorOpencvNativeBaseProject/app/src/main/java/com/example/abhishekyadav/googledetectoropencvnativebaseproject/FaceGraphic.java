package com.example.abhishekyadav.googledetectoropencvnativebaseproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

import static android.opengl.GLES20.glEnable;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_2D;
import static org.opencv.imgproc.Imgproc.line;

/**
 * Created by abhishekyadav on 01/01/18.
 */

class FaceGraphic extends GraphicOverlay.Graphic {

    public native double[] PoseEstimate(float[] landmarkCord);

    public  float [] LandmarkCord;

    private static final float FACE_POSITION_RADIUS = 10.0f;
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private static final int COLOR_CHOICES[] = {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.WHITE,
            Color.YELLOW
    };
    private static int mCurrentColorIndex = 0;

    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    private Paint mBoxPaint;

    private volatile Face mFace;
    private int mFaceId;
    private float mFaceHappiness;

    FaceGraphic(GraphicOverlay overlay) {
        super(overlay);

        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];

        mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(selectedColor);

        mIdPaint = new Paint();
        mIdPaint.setColor(selectedColor);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(selectedColor);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
        LandmarkCord = new float[12];

    }

    void setId(int id) {
        mFaceId = id;
    }


    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    void updateFace(Face face) {
        mFace = face;
        postInvalidate();
    }

    /**
     * Draws the face annotations for position on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) {
            return;
        }

        // Draws a circle at the position of the detected face, with the face's track id below.
        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);
        canvas.drawCircle(x, y, FACE_POSITION_RADIUS, mFacePositionPaint);
        //canvas.drawText("id: " + mFaceId, x + ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);
        //canvas.drawText("happiness: " + String.format("%.2f", face.getIsSmilingProbability()), x - ID_X_OFFSET, y - ID_Y_OFFSET, mIdPaint);
        //canvas.drawText("right eye: " + String.format("%.2f", face.getIsRightEyeOpenProbability()), x + ID_X_OFFSET * 2, y + ID_Y_OFFSET * 2, mIdPaint);
        //canvas.drawText("left eye: " + String.format("%.2f", face.getIsLeftEyeOpenProbability()), x - ID_X_OFFSET*2, y - ID_Y_OFFSET*2, mIdPaint);

        // Draws a bounding box around the face.
        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f);
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;
        //canvas.drawRect(left, top, right, bottom, mBoxPaint);

        for (Landmark landmark : face.getLandmarks()) {

            float cx = (translateX(landmark.getPosition().x));
            float cy = (translateY(landmark.getPosition().y));
                canvas.drawCircle(cx, cy, FACE_POSITION_RADIUS, mIdPaint);
        }
        for (Landmark landmark : face.getLandmarks()) {

            float cx =  (translateX(landmark.getPosition().x) );
            float cy = (translateY(landmark.getPosition().y ));
            //canvas.drawCircle(cx, cy, FACE_POSITION_RADIUS, mIdPaint);
            if(landmark.getType() == Landmark.NOSE_BASE)
            {
                LandmarkCord[0] = cx;
                LandmarkCord[1] = cy;
                canvas.drawCircle(cx, cy, FACE_POSITION_RADIUS, mIdPaint);
            }
            if(landmark.getType() == Landmark.BOTTOM_MOUTH)
            {
                LandmarkCord[2] = cx;
                LandmarkCord[3]= cy;
                canvas.drawCircle(cx, cy, FACE_POSITION_RADIUS, mIdPaint);
            }
            if(landmark.getType() == Landmark.LEFT_EYE)
            {
                LandmarkCord[4] = cx;
                LandmarkCord[5] = cy;
                canvas.drawCircle(cx, cy, FACE_POSITION_RADIUS, mIdPaint);
            }
            if(landmark.getType() == Landmark.RIGHT_EYE)
            {
                LandmarkCord[6] = cx;
                LandmarkCord[7] = cy;
                canvas.drawCircle(cx, cy, FACE_POSITION_RADIUS, mIdPaint);
            }
            if(landmark.getType() == Landmark.LEFT_MOUTH)
            {
                LandmarkCord[8] = cx;
                LandmarkCord[9] = cy;
                canvas.drawCircle(cx, cy, FACE_POSITION_RADIUS, mIdPaint);
            }
            if(landmark.getType() == Landmark.RIGHT_MOUTH)
            {
                LandmarkCord[10] = cx;
                LandmarkCord[11] = cy;
                canvas.drawCircle(cx, cy, FACE_POSITION_RADIUS, mIdPaint);
            }

        }
        double[] returned = PoseEstimate(LandmarkCord);

        //line(im,image_points[0], nose_end_point2D[0], cv::Scalar(255,0,0), 2);
        canvas.drawLine((float)returned[0],(float)returned[1],(float)returned[2],(float)returned[3],mIdPaint);
        //glEnable(GL_TEXTURE_2D);


    }
}
