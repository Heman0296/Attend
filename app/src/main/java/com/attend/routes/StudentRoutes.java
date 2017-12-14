package com.attend.routes;

import android.util.Log;

import com.attend.Models;
import com.attend.utils.Constants;
import com.attend.utils.VolleyHandler;
import com.attend.utils.VolleyHandler.ApiResponse;
import com.attend.utils.VolleyHandler.ApiResult;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ankit Joshi on 05-11-2017.
 */

public class StudentRoutes {

    VolleyHandler volleyHandler = new VolleyHandler();

    public void getDetails(String rollno, final ApiResponse<Models.Student> completion) {

        //String url = Constants.URL_GET_STUDENT_DETAILS + rollno;
        //TODO delete below line
        String url = Constants.URL_GET_STUDENT_DETAILS;
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

    public void getSubjectAttendanceSummary(String rollno, String subject, final VolleyHandler.ApiResponse<Models.SubjectAttendanceSummary> completion) {

        String url = Constants.URL_GET_SUBJECT_ATTENDANCE_SUMMARY + rollno + "/" + subject;
        //TODO Remove the below line
        url = Constants.URL_GET_SUBJECT_ATTENDANCE_SUMMARY;
        final Models.SubjectAttendanceSummary subjectAttendanceSummary = new Models.SubjectAttendanceSummary();

        volleyHandler.RequestApi(url, new VolleyHandler.ApiResponse<VolleyHandler.ApiResult>() {
            @Override
            public void onCompletion(VolleyHandler.ApiResult res) {
                if (res.success == 200 && res.dataIsObject()) {
                    JSONObject userObj = res.getDataAsObject();
                    Models.SubjectAttendanceSummary subjectAttendanceSummary = new Gson().fromJson(userObj.toString(), Models.SubjectAttendanceSummary.class);
                    subjectAttendanceSummary.message = res.message;
                    completion.onCompletion(subjectAttendanceSummary);
                } else {
                    completion.onCompletion(subjectAttendanceSummary);
                }
            }
        });

    }

    public void getClassesOfDay(String rollno, final VolleyHandler.ApiResponse<Models.ClassesOfDay[]> completion) {

        String url = Constants.URL_GET_ATTENDANCE_SUMMARYY_ALL_SUBJECTS + rollno;
        //TODO: Remove this line
        url = Constants.URL_GET_CLASSES_OF_DAY;
        final Models.ClassesOfDay classesOfDays[] = null;

        volleyHandler.RequestApi(url, new VolleyHandler.ApiResponse<VolleyHandler.ApiResult>() {
            @Override
            public void onCompletion(VolleyHandler.ApiResult res) {
                if (res.success == 200 && res.dataIsArray()) {
                    JSONArray userArray = res.getDataAsArray();
                    Log.i("StudentRoutes", userArray.toString());
                    Models.ClassesOfDay[] classesOfDays = new Models.ClassesOfDay[userArray.length()];
                    for (int i = 0; i < userArray.length(); i++) {
                        try {
                            classesOfDays[i] = new Gson().fromJson(userArray.getJSONObject(i).toString(), Models.ClassesOfDay.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("StudentRoutes", userArray.toString());
                    ///Models.AttendanceSummaryAllSubjects attendanceSummaryAllSubjects = new Gson().fromJson(userObj.toString(), Models.AttendanceSummaryAllSubjects.class);
                    //attendanceSummaryAllSubjects.message = res.message;
                    completion.onCompletion(classesOfDays);
                } else {
                    completion.onCompletion(classesOfDays);
                }
            }
        });
    }

    public void getAttendanceSummaryAllSubjects(String rollno, final VolleyHandler.ApiResponse<Models.AttendanceSummaryAllSubjects[]> completion) {

        String url = Constants.URL_GET_ATTENDANCE_SUMMARYY_ALL_SUBJECTS + rollno;
        //TODO: Remove this line
        url = Constants.URL_GET_ATTENDANCE_SUMMARYY_ALL_SUBJECTS;
        final Models.AttendanceSummaryAllSubjects attendanceSummaryAllSubjects[] = null;

        volleyHandler.RequestApi(url, new VolleyHandler.ApiResponse<VolleyHandler.ApiResult>() {
            @Override
            public void onCompletion(VolleyHandler.ApiResult res) {
                if (res.success == 200 && res.dataIsArray()) {
                    JSONArray userArray = res.getDataAsArray();
                    Log.i("StudentRoutes", userArray.toString());
                    Models.AttendanceSummaryAllSubjects[] attendanceSummaryAllSubjects = new Models.AttendanceSummaryAllSubjects[userArray.length()];
                    for(int i = 0; i < userArray.length(); i++) {
                        try {
                            attendanceSummaryAllSubjects[i] = new Gson().fromJson(userArray.getJSONObject(i).toString(), Models.AttendanceSummaryAllSubjects.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("StudentRoutes", userArray.toString());
                    ///Models.AttendanceSummaryAllSubjects attendanceSummaryAllSubjects = new Gson().fromJson(userObj.toString(), Models.AttendanceSummaryAllSubjects.class);
                    //attendanceSummaryAllSubjects.message = res.message;
                    completion.onCompletion(attendanceSummaryAllSubjects);
                } else {
                    completion.onCompletion(attendanceSummaryAllSubjects);
                }
            }
        });

    }

    public void getSubjectAttendanceDatewise(String rollno, String subject, final VolleyHandler.ApiResponse<Models.SubjectAttendanceDatewise[]> completion) {

        String url = Constants.URL_GET_SUBJECT_ATTENDANCE_DATEWISE + rollno + "/" + subject;
        //TODO remove the below line
        url = Constants.URL_GET_SUBJECT_ATTENDANCE_DATEWISE;
        final Models.SubjectAttendanceDatewise subjectAttendanceDatewise[] = null;

        volleyHandler.RequestApi(url, new VolleyHandler.ApiResponse<VolleyHandler.ApiResult>() {
            @Override
            public void onCompletion(VolleyHandler.ApiResult res) {
                if (res.success == 200 && res.dataIsArray()) {
                    JSONArray userArray = res.getDataAsArray();
                    Log.i("StudentRoutes", "First");
                    Models.SubjectAttendanceDatewise[] subjectAttendanceDatewise = new Models.SubjectAttendanceDatewise[userArray.length()];
                    Log.i("StudentRoutes", "Second");
                    for(int i = 0; i < userArray.length(); i++) {
                        try {
                            Log.i("StudentRoutes", "Third");
                            subjectAttendanceDatewise[i] = new Gson().fromJson(userArray.getJSONObject(i).toString(), Models.SubjectAttendanceDatewise.class);
                            Log.i("StudentRoutes", String.valueOf(subjectAttendanceDatewise.length));
                        } catch (JSONException e) {
                            Log.i("StudentRoutes", "Fourth");
                            e.printStackTrace();
                        }
                    }
                    Log.i("StudentRoutes", "Fifth");
                    completion.onCompletion(subjectAttendanceDatewise);
                } else {
                    completion.onCompletion(subjectAttendanceDatewise);
                }
            }
        });
    }
}
