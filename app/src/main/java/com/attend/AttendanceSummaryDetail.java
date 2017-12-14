package com.attend;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_summary_detail);
        Intent intent = getIntent();
        attendanceSummaryObject = (AttendanceSummaryList) intent.getSerializableExtra("summaryObject");
        // TODO pass the subject from the attendanceSummaryObject rather than passing hard coded values
        calendar = Calendar.getInstance();
        statusDatesMap = new HashMap<>();
        presentColorDrawable = new ColorDrawable(0xffB2FF59);
        absentColorDrawable = new ColorDrawable(0xffF44336);

        getSubjectAttendanceDatewise("1140917","IT-405");

    }

    public void getSubjectAttendanceDatewise(String rollno, String subject) {
        StudentRoutes studentRoutes = new StudentRoutes();
        studentRoutes.getSubjectAttendanceDatewise(rollno,subject, new VolleyHandler.ApiResponse<Models.SubjectAttendanceDatewise[]>() {
            @Override
            public void onCompletion(Models.SubjectAttendanceDatewise[] attendanceSummaryDatewise) {
                //Information stored in student object
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
            }
        });
    }
}
