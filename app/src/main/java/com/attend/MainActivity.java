package com.attend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.attend.routes.FaceRecognitionRoutes;
import com.attend.routes.StudentRoutes;
import com.attend.utils.VolleyHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.roll_no);
                String roll_number = textInputLayout.getEditText().getText().toString();
                Log.v("MainActivity", roll_number);
                textInputLayout = (TextInputLayout) findViewById(R.id.password);
                String password = textInputLayout.getEditText().getText().toString();
                Log.v("MainActivity", password);

                //TODO add here the code for login authentication

                boolean loginStatus = true;

                if(loginStatus){
                    SharedPreferences sharedPreferences = getSharedPreferences("studentDetails", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("roll_number",roll_number);
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this.getApplication(), Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        });
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
   /* public void getClassesOfDay(String rollno) {
        StudentRoutes studentRoutes = new StudentRoutes();
        studentRoutes.getClassesOfDay(rollno, new VolleyHandler.ApiResponse<Models.ClassesOfDay[]>() {
            @Override
           *//* public void onCompletion(Models.ClassesOfDay[] classesOfDay) {
                //Information stored in student object
                Log.v("MainActivity", classesOfDay.bluetooth_address);
            }*//*
        });
    }*/

    // TODO: Test code to be removed
    public void getAttendanceSummaryAllSubjects(String rollno) {
        StudentRoutes studentRoutes = new StudentRoutes();
        studentRoutes.getAttendanceSummaryAllSubjects(rollno, new VolleyHandler.ApiResponse<Models.AttendanceSummaryAllSubjects[]>() {
            @Override
            public void onCompletion(Models.AttendanceSummaryAllSubjects[] attendanceSummaryAllSubjects) {
                //Information stored in student object
                for(int i=0; i < attendanceSummaryAllSubjects.length; i++) {
                    Log.i("MainActivity", attendanceSummaryAllSubjects[i].subject);
                }
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
