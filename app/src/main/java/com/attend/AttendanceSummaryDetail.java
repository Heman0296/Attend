package com.attend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.attend.routes.StudentRoutes;
import com.attend.utils.VolleyHandler;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AttendanceSummaryDetail extends AppCompatActivity {
    
    public static HashMap<Date,Drawable> statusDatesMap;
    public static Calendar calendar;
    public static Date date;
    public static ColorDrawable presentColorDrawable;
    public static ColorDrawable absentColorDrawable;
    public static AttendanceSummaryList attendanceSummaryObject;
    public static ProgressDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_summary_detail);
        Intent intent = getIntent();

        myDialog = new ProgressDialog(this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);

        attendanceSummaryObject = (AttendanceSummaryList) intent.getSerializableExtra("summaryObject");
        // TODO pass the subject from the attendanceSummaryObject rather than passing hard coded values
        calendar = Calendar.getInstance();
        statusDatesMap = new HashMap<>();
        presentColorDrawable = new ColorDrawable(0xffB2FF59);
        absentColorDrawable = new ColorDrawable(0xffF44336);

        SharedPreferences sharedPreferences = getSharedPreferences("studentDetails", MODE_PRIVATE);
        String rollno = sharedPreferences.getString("roll_number", null);
        String subject = attendanceSummaryObject.getNameOfClass();

        myDialog.show();
        getSubjectAttendanceDatewise(rollno,subject);

    }

    public void getSubjectAttendanceDatewise(String rollno, String subject) {
        StudentRoutes studentRoutes = new StudentRoutes();
        studentRoutes.getSubjectAttendanceDatewise(rollno,subject, new VolleyHandler.ApiResponse<Models.SubjectAttendanceDatewise[]>() {
            @Override
            public void onCompletion(Models.SubjectAttendanceDatewise[] attendanceSummaryDatewise) {
                //Information stored in student object
                if(attendanceSummaryDatewise != null){
                    for(int i=0; i < attendanceSummaryDatewise.length; i++) {
                        String setDate = attendanceSummaryDatewise[i].date;
                        String[] dateVariables = setDate.split("-");
                        calendar.set(Integer.parseInt(dateVariables[0]),Integer.parseInt(dateVariables[1]),Integer.parseInt(dateVariables[2]));
                        date = calendar.getTime();
                        if(attendanceSummaryDatewise[i].presence_flag){
                            statusDatesMap.put(date, presentColorDrawable);
                        }
                        else{
                            statusDatesMap.put(date, absentColorDrawable);
                        }
                    }
                    CaldroidFragment caldroidFragment = new CaldroidFragment();
                    Bundle args = new Bundle();

                    args.putInt(CaldroidFragment.MONTH, calendar.get(Calendar.MONTH) + 1);
                    args.putInt(CaldroidFragment.YEAR, calendar.get(Calendar.YEAR));
                    args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
                    caldroidFragment.setArguments(args);

                    caldroidFragment.setBackgroundDrawableForDates(statusDatesMap);
                    caldroidFragment.refreshView();

                    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                    t.replace(R.id.attendancecalendar, caldroidFragment);
                    t.commit();

                    /*TableLayout presentTableLayout = (TableLayout) findViewById(R.id.presentTableLayout);
                    TableLayout absentTableLayout = (TableLayout) findViewById(R.id.absentTableLayout);
                    presentTableLayout.removeAllViews();
                    absentTableLayout.removeAllViews();
                    for(int i=0; i < attendanceSummaryDatewise.length; i++){
                        TableRow tableRow = new TableRow(getApplicationContext());
                        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                        TextView textView = new TextView(getApplicationContext());
                        textView.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                        String text = attendanceSummaryDatewise[i].date;
                        textView.setText(text);
                        tableRow.addView(textView);

                        if(attendanceSummaryDatewise[i].presence_flag){
                            presentTableLayout.addView(tableRow);
                        }
                        else{
                            absentTableLayout.addView(tableRow);
                        }
                    }*/
                    myDialog.dismiss();
                }
                else{
                    myDialog.dismiss();
                    Toast.makeText(getApplication(), "Unable to fetch data. Try again later!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        myDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), Home.class);
        startActivity(intent);
    }
}
