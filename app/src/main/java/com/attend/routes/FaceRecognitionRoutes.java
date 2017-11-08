package com.attend.routes;


import android.graphics.Bitmap;
import android.util.Log;

import com.attend.Models;
import com.attend.utils.Constants;
import com.attend.utils.VolleyHandler;
import com.attend.utils.VolleyHandler.ApiResponse;
import com.attend.utils.VolleyHandler.ApiResult;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Ankit Joshi on 05-11-2017.
 */

public class FaceRecognitionRoutes {

    VolleyHandler volleyHandler = new VolleyHandler();

    public void classify(Bitmap image, final ApiResponse<Models.FaceDetails> completion) {

        String url = Constants.URL_CLASSIFY;
        final Models.FaceDetails faceDetails = new Models.FaceDetails();

        volleyHandler.uploadImage(image, url, new ApiResponse<ApiResult>() {
            @Override
            public void onCompletion(ApiResult res) {
                if (res.success == 200 && res.dataIsObject()) {
                    JSONObject userObj = res.getDataAsObject();
                    Log.i("Classify: ", String.valueOf(res.getDataAsObject()));
                    Models.FaceDetails faceDetails = new Gson().fromJson(userObj.toString(), Models.FaceDetails.class);
                    faceDetails.message = res.message;
                    completion.onCompletion(faceDetails);
                } else {
                    Log.i("Classify: ", "Error");
                    completion.onCompletion(faceDetails);
                }
            }
        });

    }
}
