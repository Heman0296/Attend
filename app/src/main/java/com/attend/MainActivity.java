package com.attend;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.attend.routes.FaceRecognitionRoutes;
import com.attend.routes.StudentRoutes;
import com.attend.utils.VolleyHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*getSubjectAttendanceSummary("1140917", "IT-401");*/
        getStudentDetails("1140917");
    }

    // TODO: Test code to be removed
    public void getStudentDetails(String rollno) {
        StudentRoutes studentRoutes = new StudentRoutes();
        studentRoutes.getDetails(rollno, new VolleyHandler.ApiResponse<Models.Student>() {
            @Override
            public void onCompletion(Models.Student student) {
                //Information stored in student object
                Log.v("MainActivity", student.message + " " + student.name);
            }
        });
    }

    // TODO: Test code to be removed
    public void getSubjectAttendanceSummary(String rollno, String subject) {
        StudentRoutes studentRoutes = new StudentRoutes();
        studentRoutes.getSubjectAttendanceSummary(rollno, subject, new VolleyHandler.ApiResponse<Models.SubjectAttendanceSummary>() {
            @Override
            public void onCompletion(Models.SubjectAttendanceSummary subjectAttendanceSummary) {
                //Information stored in student object
                Log.v("MainActivity", subjectAttendanceSummary.message + " " + subjectAttendanceSummary.total_attendance + " " + subjectAttendanceSummary.total_present);
            }
        });
    }

    // TODO: Test code to be removed
    public void uploadImage(Bitmap image) {
        FaceRecognitionRoutes faceRecognitionRoutes = new FaceRecognitionRoutes();
        faceRecognitionRoutes.classify(image, new VolleyHandler.ApiResponse<Models.FaceDetails>() {
            @Override
            public void onCompletion(Models.FaceDetails faceDetails) {
                //Information stored in faceDetails object
            }
        });
    }
}
