package com.attend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.attend.routes.FaceRecognitionRoutes;
import com.attend.routes.StudentRoutes;
import com.attend.utils.VolleyHandler;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static HashMap<String,AttendanceStatus> hashMap;
    public static String rollno;
    public static String period;
    public static String subject;
    public static String connectionStatus;
    public static SharedPreferences sharedPreferences;
    public static ClassList classObject;
    public static Intent intent;
    public static ProgressDialog myDialog;
    public static String markStatus = "false";
    public byte[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);
        intent = getIntent();

        myDialog = new ProgressDialog(this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);

        Button button = (Button) findViewById(R.id.button);
        ImageView imageView = (ImageView) findViewById(R.id.takePicture);
        Bitmap bitmap = null;

        sharedPreferences = getSharedPreferences("studentDetails", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("attendanceStatus", "nothing");
        JSONObject jObject  = null;
        try {
            jObject = new JSONObject(json);
            JSONArray userArray = jObject.getJSONArray("list");
            hashMap = new HashMap<>();
            for(int i = 0; i < userArray.length(); i++) {
                try {
                    Log.i("MarkAttendance", String.valueOf(userArray.length()));
                    String key = userArray.getJSONObject(i).keys().next().toString();
                    String value = userArray.getJSONObject(i).getString(userArray.getJSONObject(i).keys().next());
                    Log.i("MarkAttendance", value);
                    Log.i("MarkAttendance", key);
                    List<String> convertedList = Arrays.asList(value.split(","));
                    Log.i("MarkAttendance", String.valueOf(convertedList.size()));
                    Boolean inTime = Boolean.valueOf(convertedList.get(0));
                    Boolean outTime = Boolean.valueOf(convertedList.get(1));
                    String subject = String.valueOf(convertedList.get(2));
                    hashMap.put(key, new AttendanceStatus(inTime, outTime, subject));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        classObject = (ClassList) intent.getSerializableExtra("classObject");
        rollno = sharedPreferences.getString("roll_number", null);
        subject = classObject.getNameOfClass();
        period = classObject.getPeriod();

        data = AppController.getInstance().getData();
        if(data!=null){
            int length = data.length;
            bitmap = BitmapFactory.decodeByteArray(data, 0, length);
            imageView.setImageBitmap(bitmap);
        }

        connectionStatus = intent.getStringExtra("connectionStatus");
        Log.i("MarkAttendance", String.valueOf(connectionStatus));

        if(connectionStatus.equals("true")){
            button.setEnabled(true);
        }
        else{
            button.setEnabled(false);
            Toast.makeText(getApplication(), "Unable to find beacon. Go back and try connecting again!",
                    Toast.LENGTH_LONG).show();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectionStatus.equals("true")){
                    Intent intent = new Intent(MarkAttendance.this.getApplication(), BlinkDetection.class);
                    intent.putExtra("connectionStatus", connectionStatus);
                    intent.putExtra("classObject", classObject);
                    startActivity(intent);
                    finish();
                }
            }
        });

        Log.v("MarkAttendance", json);

        TextView textView = (TextView) findViewById(R.id.teacherName);
        textView.setText(classObject.getFaculty_name());
        textView = (TextView) findViewById(R.id.classTime);
        textView.setText(classObject.getTime());
        textView = (TextView) findViewById(R.id.location);
        textView.setText(classObject.getLocation());
        textView = (TextView) findViewById(R.id.percentage);

        getSubjectAttendanceSummary(rollno,subject);

        textView = (TextView) findViewById(R.id.date);
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        textView.setText(date);

        textView = (TextView) findViewById(R.id.classStatus);
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        if(currentTime.compareTo(classObject.getTime())>=0 && currentTime.compareTo(classObject.getEnd_time())<=0){
            textView.setText("Class Ongoing");
            //button.setEnabled(false);
            textView.setTextColor(Color.parseColor("#AEEA00"));
        }
        else{
            textView.setText("Not Ongoing");
            textView.setTextColor(Color.parseColor("#F44336"));
        }

        textView = (TextView) findViewById(R.id.inTimeStatus);
        boolean inStatus = false,outStatus = false;
        Iterator it = hashMap.entrySet().iterator();
        for(String currentKey : hashMap.keySet()){
            if(hashMap.get(currentKey).getSubjectName().toString().equals(classObject.getNameOfClass().toString())){
                inStatus = hashMap.get(currentKey).getInTimeStatus();
                outStatus = hashMap.get(currentKey).getOutTimeStatus();

            }
        }

        if(data == null){
            button.setEnabled(false);
        }
        else{
            button.setEnabled(true);
        }
        if(inStatus){
            textView.setText("Attendance Marked");
            textView.setTextColor(Color.parseColor("#AEEA00"));
            button.setText("mark out time");
        }
        else{
            textView.setText("Not Marked");
            textView.setTextColor(Color.parseColor("#F44336"));
            button.setText("Mark in time");
        }

        textView = (TextView) findViewById(R.id.outTimeStatus);
        if(outStatus){
            textView.setText("Attendance Marked");
            textView.setTextColor(Color.parseColor("#AEEA00"));
        }
        else{
            textView.setText("Not Marked");
            textView.setTextColor(Color.parseColor("#F44336"));
            button.setText("Mark out time");
        }

        if(inStatus && outStatus){
            String markedStatus = sharedPreferences.getString("status", "false");
            if(markedStatus.equals("true")) {
                TextView difftextView = (TextView) findViewById(R.id.attendanceStatus);
                difftextView.setText("Fully Marked");
                difftextView.setTextColor(Color.parseColor("#AEEA00"));
            }
            else {
                textView = (TextView) findViewById(R.id.attendanceStatus);
                textView.setText("Not Marked");
                textView.setTextColor(Color.parseColor("#F44336"));
            }
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
                myDialog.show();
                if(subjectAttendanceSummary != null){
                    Double percentage = ((Double.parseDouble(subjectAttendanceSummary.total_present)/Double.parseDouble(subjectAttendanceSummary.total_attendance))*100);
                    String subjectPercentage = String.format("%.1f", percentage);
                    TextView textView = (TextView) findViewById(R.id.percentage);
                    textView.setText(subjectPercentage);
                    myDialog.dismiss();
                }
                else{
                    myDialog.dismiss();
                    Toast.makeText(getApplication(), "Unable to load attendance summary. View later!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void uploadImage(Bitmap image, final ProgressDialog verifyImageDialog) {
        FaceRecognitionRoutes faceRecognitionRoutes = new FaceRecognitionRoutes();
        faceRecognitionRoutes.classify(image, new VolleyHandler.ApiResponse<Models.FaceDetails>() {
            @Override
            public void onCompletion(Models.FaceDetails faceDetails) {
                //Information stored in faceDetails object
                verifyImageDialog.dismiss();
                if(Double.parseDouble(faceDetails.confidence) >= 0.5 && faceDetails.identity.equals(rollno)){
                    Iterator it = hashMap.entrySet().iterator();
                    for(String currentKey : hashMap.keySet()){
                        if(hashMap.get(currentKey).getSubjectName().equals(subject)){
                            boolean inStatus = hashMap.get(currentKey).getInTimeStatus();
                            boolean outStatus = hashMap.get(currentKey).getOutTimeStatus();
                            String begin_time = classObject.getTime();
                            String end_time = classObject.getEnd_time();
                            String current_time = new SimpleDateFormat("kk:mm").format(new Date());
                            int begin_time_amount = (Integer.parseInt(begin_time.substring(0,2)) * 60) + Integer.parseInt(begin_time.substring(3,5));
                            int end_time_amount = (Integer.parseInt(end_time.substring(0,2)) * 60) + Integer.parseInt(end_time.substring(3,5));
                            int current_time_amount = (Integer.parseInt(current_time.substring(0,2)) * 60) + Integer.parseInt(current_time.substring(3,5));
                            if(!inStatus){
                                if(current_time_amount >= begin_time_amount && current_time_amount <= begin_time_amount + 10){
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    Gson gson = new Gson();
                                    String json = sharedPreferences.getString("attendanceStatus", "nothing");
                                    Properties props = new Properties();
                                    try {
                                        props.load(new StringReader(json.substring(1, json.length() - 1).replace(", ", "\n")));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Map<String, String> tempHashMap = new HashMap<String, String>();
                                    for (Map.Entry<Object, Object> e : props.entrySet()) {
                                        tempHashMap.put((String)e.getKey(), String.valueOf(e.getValue()));
                                    }
                                    AttendanceStatus attendanceStatus = new AttendanceStatus(true,false,subject);
                                    hashMap.put(currentKey,attendanceStatus);
                                    tempHashMap.put(String.valueOf(currentKey),"true,false," + String.valueOf(subject));
                                    json =  "";
                                    Iterator iterator = hashMap.entrySet().iterator();
                                    for(String key : hashMap.keySet()){
                                        json += "\"" + key + "\":" + "\"" + hashMap.get(key).getInTimeStatus() + "," + hashMap.get(key).getOutTimeStatus() + "," + hashMap.get(key).getSubjectName() + "\"},{";
                                    }
                                    json = json.substring(0,json.length() - 2);
                                    String newJson = "{list:[{" + json + "]}";
                                    editor.putString("attendanceStatus", newJson);
                                    editor.apply();
                                    editor.commit();
                                    TextView textView = (TextView) findViewById(R.id.inTimeStatus);
                                    textView.setText("Attendance Marked");
                                    textView.setTextColor(Color.parseColor("#AEEA00"));
                                }
                                else{
                                    Toast.makeText(getApplication(), "Wrong time to mark attendance",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                if(current_time_amount <= end_time_amount && current_time_amount >= end_time_amount - 10){
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    Gson gson = new Gson();
                                    String json = sharedPreferences.getString("attendanceStatus", "nothing");
                                    Properties props = new Properties();
                                    try {
                                        props.load(new StringReader(json.substring(1, json.length() - 1).replace(", ", "\n")));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Map<String, String> tempHashMap = new HashMap<String, String>();
                                    for (Map.Entry<Object, Object> e : props.entrySet()) {
                                        tempHashMap.put((String)e.getKey(), String.valueOf(e.getValue()));
                                    }
                                    AttendanceStatus attendanceStatus = new AttendanceStatus(true,true,subject);
                                    hashMap.put(currentKey,attendanceStatus);
                                    tempHashMap.put(String.valueOf(currentKey),"true,true," + String.valueOf(subject));
                                    json =  "";
                                    Iterator iterator = hashMap.entrySet().iterator();
                                    for(String key : hashMap.keySet()){
                                        json += "\"" + key + "\":" + "\"" + hashMap.get(key).getInTimeStatus() + "," + hashMap.get(key).getOutTimeStatus() + "," + hashMap.get(key).getSubjectName() + "\"},{";
                                    }
                                    json = json.substring(0,json.length() - 2);
                                    String newJson = "{list:[{" + json + "]}";
                                    editor.putString("attendanceStatus", newJson);
                                    editor.apply();
                                    editor.commit();
                                    TextView textView = (TextView) findViewById(R.id.outTimeStatus);
                                    textView.setText("Attendance Marked");
                                    textView.setTextColor(Color.parseColor("#AEEA00"));
                                    markAttendance(rollno, subject, period);
                                }
                                else{
                                    Toast.makeText(getApplication(), "Wrong time to mark attendance",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                            AppController.getInstance().setData(null);
                        }
                        AppController.getInstance().setData(null);
                    }
                    AppController.getInstance().setData(null);
                }
                AppController.getInstance().setData(null);
            }
        });
        AppController.getInstance().setData(null);
    }

    public void markAttendance(String rollno, String subject, String period){
        StudentRoutes studentRoutes = new StudentRoutes();
        studentRoutes.markAttendance(rollno, subject, period, new VolleyHandler.ApiResponse<Models.MarkAttendanceOnServer>() {
            @Override
            public void onCompletion(Models.MarkAttendanceOnServer markAttendanceOnServer) {
                // To do after attendance has been marked.
                markStatus = "true";
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("markAttendanceStatus", markAttendanceOnServer.marked);
                String status = markAttendanceOnServer.marked;
                if(status.equals("true")){
                    SharedPreferences statusSharedPreferences = getSharedPreferences("studentDetails", MODE_PRIVATE);
                    editor = statusSharedPreferences.edit();
                    editor.putString("status", "true");
                    editor.apply();
                    editor.commit();
                    TextView textView = (TextView) findViewById(R.id.attendanceStatus);
                    textView.setText("Fully Marked");
                    textView.setTextColor(Color.parseColor("#AEEA00"));
                    Button button = (Button) findViewById(R.id.button);
                    button.setEnabled(false);
                }
                else{
                    Toast.makeText(getApplication(), "Unable to mark attendance. Try again",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        myDialog.dismiss();
        String status = sharedPreferences.getString("status", "false");
        if(status.equals("true")){
            TextView textView = (TextView) findViewById(R.id.attendanceStatus);
            textView.setText("Fully Marked");
            textView.setTextColor(Color.parseColor("#AEEA00"));
            Button button = (Button) findViewById(R.id.button);
            button.setEnabled(false);
        }
    }

    public void verifyImage(View v){
        if(true){
            ProgressDialog verifyImageDialog = new ProgressDialog(this);
            verifyImageDialog = new ProgressDialog(this);
            verifyImageDialog.setMessage("Please wait...");
            verifyImageDialog.setCancelable(false);
            verifyImageDialog.show();
            int length = data.length;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, length);
            uploadImage(bitmap, verifyImageDialog);
        }
    }

    @Override
    public void onBackPressed() {
        AppController.getInstance().setData(null);
        Intent intent = new Intent(getApplication(), Home.class);
        startActivity(intent);
    }
}
