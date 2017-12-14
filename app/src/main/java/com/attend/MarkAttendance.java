package com.attend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.attend.routes.StudentRoutes;
import com.attend.utils.VolleyHandler;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MarkAttendance extends AppCompatActivity {

    HashMap<String,AttendanceStatus> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        Button button = (Button) findViewById(R.id.button);
        ImageView imageView = (ImageView) findViewById(R.id.takePicture);

        Intent intent = getIntent();
        final ClassList classObject = (ClassList) intent.getSerializableExtra("classObject");
        final boolean connectionStatus = (boolean) intent.getBooleanExtra("connectionStatus", false);
        Log.i("MarkAttendance", String.valueOf(connectionStatus));

        if(connectionStatus){
            button.setEnabled(true);
        }
        else{
            button.setEnabled(false);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectionStatus){
                    Intent intent = new Intent(MarkAttendance.this.getApplication(), BlinkDetection.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("classObject", classObject);
                    startActivity(intent);
                }
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("studentDetails", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("attendanceStatus", "nothing");
        Properties props = new Properties();
        try {
            props.load(new StringReader(json.substring(1, json.length() - 1).replace(", ", "\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, AttendanceStatus> hashMap = new HashMap<String, AttendanceStatus>();
        for (Map.Entry<Object, Object> e : props.entrySet()) {
            List<String> convertedList = Arrays.asList(String.valueOf(e.getValue()).split(","));
            Boolean inTime = Boolean.valueOf(convertedList.get(0));
            Boolean outTime = Boolean.valueOf(convertedList.get(1));
            String subject = String.valueOf(convertedList.get(2));
            hashMap.put((String)e.getKey(), new AttendanceStatus(inTime, outTime, subject));
        }
        Log.v("MarkAttendance", json);

        TextView textView = (TextView) findViewById(R.id.teacherName);
        textView.setText(classObject.getFaculty_name());
        textView = (TextView) findViewById(R.id.classTime);
        textView.setText(classObject.getTime());
        textView = (TextView) findViewById(R.id.location);
        textView.setText(classObject.getLocation());
        textView = (TextView) findViewById(R.id.percentage);

        getSubjectAttendanceSummary("1140917","IT-405");

        textView = (TextView) findViewById(R.id.date);
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        textView.setText(date);

        textView = (TextView) findViewById(R.id.classStatus);
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        if(currentTime.compareTo(classObject.getTime())>=0 && currentTime.compareTo(classObject.getEnd_time())<=0){
            textView.setText("Class Ongoing");
        }
        else{
            textView.setText("Not Ongoing");
        }

        textView = (TextView) findViewById(R.id.inTimeStatus);
        boolean inStatus = false,outStatus = false;
        Iterator it = hashMap.entrySet().iterator();
        for(String currentKey : hashMap.keySet()){
            if(hashMap.get(currentKey).getSubjectName().equals(classObject.getClass())){
                inStatus = hashMap.get(currentKey).getInTimeStatus();
                outStatus = hashMap.get(currentKey).getOutTimeStatus();

            }
        }
        if(inStatus){
            textView.setText("Attendance Marked");
            button.setText("mark out time");
        }
        else{
            textView.setText("Not Marked");
        }

        textView = (TextView) findViewById(R.id.outTimeStatus);
        if(outStatus){
            textView.setText("Attendance Marked");
            textView = (TextView) findViewById(R.id.attendanceStatus);
            textView.setText("Marked");
            button.setEnabled(false);
        }
        else{
            textView.setText("Not Marked");
            textView = (TextView) findViewById(R.id.attendanceStatus);
            textView.setText("Not Marked");
        }

        byte[] data = AppController.getInstance().getData();
        if(data!=null){
            int length = data.length;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, length);
            imageView.setImageBitmap(bitmap);
            imageView.setRotation(270);
        }

        textView = (TextView) findViewById(R.id.noteClickPicture);
        if(AppController.getInstance().getData() != null){
            textView.setText("(Click above picture to retake photo)");
        }
    }

    public void getSubjectAttendanceSummary(String rollno, String subject) {
        StudentRoutes studentRoutes = new StudentRoutes();
        studentRoutes.getSubjectAttendanceSummary(rollno, subject, new VolleyHandler.ApiResponse<Models.SubjectAttendanceSummary>() {
            @Override
            public void onCompletion(Models.SubjectAttendanceSummary subjectAttendanceSummary) {
                Double percentage = ((Double.parseDouble(subjectAttendanceSummary.total_present)/Double.parseDouble(subjectAttendanceSummary.total_attendance))*100);
                String subjectPercentage = String.format("%.1f", percentage);
                TextView textView = (TextView) findViewById(R.id.percentage);
                textView.setText(subjectPercentage);
            }
        });
    }

    @Override
    public void onBackPressed(){
        AppController.getInstance().setData(null);
    }
}
