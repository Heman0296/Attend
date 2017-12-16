package com.attend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.attend.routes.FaceRecognitionRoutes;
import com.attend.routes.StudentRoutes;
import com.attend.utils.VolleyHandler;

public class MainActivity extends AppCompatActivity {

    public static ProgressDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);

        myDialog = new ProgressDialog(this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);

        SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        String authenticationstatus = prfs.getString("Authentication_Status", null);
        String authenticationId = prfs.getString("Authentication_Id", null);
        String autenticationhPassword = prfs.getString("Authentication_Password", null);

        if (authenticationstatus != null && authenticationstatus.equals("true")) {
            loginSuccessfull(authenticationId, autenticationhPassword);
        }


        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.roll_no);
                String roll_number = textInputLayout.getEditText().getText().toString();
                Log.v("MainActivity", roll_number);
                textInputLayout = (TextInputLayout) findViewById(R.id.password);
                String password = textInputLayout.getEditText().getText().toString();
                Log.v("MainActivity", password);

                login(roll_number, password);


            }
        });*/
    }

    public void verifyLogin(View v) {
        TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.roll_no);
        String roll_number = textInputLayout.getEditText().getText().toString();
        Log.v("MainActivity", roll_number);
        textInputLayout = (TextInputLayout) findViewById(R.id.password);
        String password = textInputLayout.getEditText().getText().toString();
        Log.v("MainActivity", password);

        myDialog.show();
        login(roll_number, password);


    }

    public void loginSuccessfull(String roll_number, String password) {
        SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Authentication_Id", roll_number);
        editor.putString("Authentication_Password", password);
        editor.putString("Authentication_Status", "true");
        editor.apply();

        SharedPreferences sharedPreferences = getSharedPreferences("studentDetails", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("roll_number", roll_number);
        editor.apply();

        Intent intent = new Intent(MainActivity.this.getApplication(), Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        myDialog.dismiss();
        startActivity(intent);
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

    public void login(final String rollno, final String password) {
        StudentRoutes studentRoutes = new StudentRoutes();
        String credentials = rollno + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        studentRoutes.login(auth, new VolleyHandler.ApiResponse<Models.Login>() {
            @Override
            public void onCompletion(Models.Login login) {
                if(login.message != null && login.message.equals("SUCCESS")) {
                    loginSuccessfull(rollno, password);
                }
                else{
                    myDialog.dismiss();
                    Toast.makeText(getApplication(), "Unable to login",
                            Toast.LENGTH_LONG).show();
                }
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
                for (int i = 0; i < attendanceSummaryAllSubjects.length; i++) {
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
