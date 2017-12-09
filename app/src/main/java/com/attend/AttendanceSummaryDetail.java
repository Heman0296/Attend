package com.attend;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AttendanceSummaryDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_summary_detail);

        //Initialize the Caldroid calendar fragment

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        caldroidFragment.setArguments(args);

        //Set color for present and absent dates

        HashMap<Date,Drawable> presentDatesMap = new HashMap<>();
        ColorDrawable presentColorDrawable = new ColorDrawable(0xffB2FF59);
        cal.set(2017,10,16);
        Date date = cal.getTime();
        presentDatesMap.put(date, presentColorDrawable);
        cal.set(2017,9,16);
        date = cal.getTime();
        presentDatesMap.put(date, presentColorDrawable);

        /*HashMap<Date,Drawable> absentDatesMap = new HashMap<>();
        ColorDrawable absentColorDrawable = new ColorDrawable(0xffF44336);
        date = new Date(2017-10-16);
        presentDatesMap.put(date, absentColorDrawable);
        date = new Date(2017-11-17);
        presentDatesMap.put(date, absentColorDrawable);*/

        caldroidFragment.setBackgroundDrawableForDates(presentDatesMap);
        //caldroidFragment.setBackgroundDrawableForDates(absentDatesMap);
        caldroidFragment.refreshView();

        //Commit the changes to the LinearLayout in the UI

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.attendancecalendar, caldroidFragment);
        t.commit();
    }
}
