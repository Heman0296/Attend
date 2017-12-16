package com.attend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.attend.routes.StudentRoutes;
import com.attend.utils.VolleyHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Himanshu on 9/13/2017.
 */

public class AttendanceSummary extends Fragment {

    public static ProgressDialog myDialog;
    private List<AttendanceSummaryList> summaryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AttendanceSummaryAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_classes, container,false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mAdapter = new AttendanceSummaryAdapter(summaryList,this.getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        myDialog = new ProgressDialog(getContext());
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("studentDetails", Context.MODE_PRIVATE);
        String rollno = sharedPreferences.getString("roll_number", null);

        myDialog.show();
        getAttendanceSummaryAllSubjects(rollno);
        return rootView;
    }

    public void getAttendanceSummaryAllSubjects(String rollno) {
        StudentRoutes studentRoutes = new StudentRoutes();
        studentRoutes.getAttendanceSummaryAllSubjects(rollno, new VolleyHandler.ApiResponse<Models.AttendanceSummaryAllSubjects[]>() {
            @Override
            public void onCompletion(Models.AttendanceSummaryAllSubjects[] attendanceSummaryAllSubjects) {
                //Information stored in student object
                if( attendanceSummaryAllSubjects != null){
                    summaryList = new ArrayList<AttendanceSummaryList>();

                    for(int i=0; i < attendanceSummaryAllSubjects.length; i++) {
                        Double percentage = ((Double.parseDouble(attendanceSummaryAllSubjects[i].total_present)/Double.parseDouble(attendanceSummaryAllSubjects[i].total_attendance))*100);
                        String subjectPercentage = String.format("%.1f", percentage);
                        AttendanceSummaryList attendanceSummaryListObject = new AttendanceSummaryList(attendanceSummaryAllSubjects[i].subject,subjectPercentage);
                        summaryList.add(attendanceSummaryListObject);
                    }
                    mAdapter.notifyDataSetChanged();
                    myDialog.dismiss();
                }
                else{
                    myDialog.dismiss();
                    Toast.makeText(getActivity().getApplication(), "Unable to fetch data. Please try later!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
