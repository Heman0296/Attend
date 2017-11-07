package com.attend.routes;

import com.google.gson.Gson;
import com.attend.Models;
import com.attend.utils.Constants;
import com.attend.utils.VolleyHandler;
import com.attend.utils.VolleyHandler.*;

import org.json.JSONObject;

/**
 * Created by Ankit Joshi on 05-11-2017.
 */

public class StudentRoutes {

    VolleyHandler volleyHandler = new VolleyHandler();

    public void getDetails(String rollno, final ApiResponse<Models.Student> completion) {

        String url = Constants.URL_GET_STUDENT_DETAILS + rollno;
        final Models.Student student = new Models.Student();

        volleyHandler.RequestApi(url, new ApiResponse<ApiResult>() {
            @Override
            public void onCompletion(ApiResult res) {
                if (res.success == 200 && res.dataIsObject()) {
                    JSONObject userObj = res.getDataAsObject();
                    Models.Student student = new Gson().fromJson(userObj.toString(), Models.Student.class);
                    student.message = res.message;
                    completion.onCompletion(student);
                } else {
                    completion.onCompletion(student);
                }
            }
        });

    }
}
