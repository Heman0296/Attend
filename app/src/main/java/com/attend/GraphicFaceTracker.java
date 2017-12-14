package com.attend;

/**
 * Created by Himanshu on 12/11/2017.
 */

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

/**
 * Created by shivam on 10/7/17.
 */
public class GraphicFaceTracker extends Tracker<Face> {

    private final float OPEN_THRESHOLD = 0.85f;
    private final float CLOSE_THRESHOLD = 0.4f;
    public BlinkDetection callingActivity;
    private int state = 0;
    private boolean flag;

    public GraphicFaceTracker(BlinkDetection act) {
        callingActivity = act;
        flag = false;
    }

    boolean blink(float value) {
        switch (state) {
            case 0:
                if (value < CLOSE_THRESHOLD) {
                    // Both eyes are initially open
                    state = 1;
                    Log.i("flag value in state 1", Boolean.toString(flag));
                }
                break;

            case 1:
                if (value > OPEN_THRESHOLD) {
                    // Both eyes become closed
                    state = 2;
                    Log.i("flag value in state 2", Boolean.toString(flag));
                }
                break;

            case 2:
                if (value < CLOSE_THRESHOLD) {
                    Log.i("BlinkTracker", "blink occurred!");
                    state = 0;
                    flag = true;
                }
                break;
        }
        return flag;

    }

    /**
     * Update the position/characteristics of the face within the overlay.
     */
    @Override
    public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {

        float left = face.getIsLeftEyeOpenProbability();
        float right = face.getIsRightEyeOpenProbability();
        if ((left == Face.UNCOMPUTED_PROBABILITY) ||
                (right == Face.UNCOMPUTED_PROBABILITY)) {
            // One of the eyes was not detected.  make a toast appear here
            //************change it to snackbar**************
            callingActivity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(callingActivity, "Kindly bring your face infront of the camera.", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }


        float value = Math.min(left, right);
        boolean flag = blink(value);
        if (flag) {
            flag = false;
            try {
                callingActivity.blinkOccured();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


