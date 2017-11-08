package com.attend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Himanshu on 9/13/2017.
 */

public class AttendanceSummary extends Fragment {

    private List<AttendanceSummaryList> summaryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AttendanceSummaryAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_classes, container,false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mAdapter = new AttendanceSummaryAdapter(summaryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareAttendanceSummary();
        return rootView;
    }

    private void prepareAttendanceSummary(){
        AttendanceSummaryList summaryObject = new AttendanceSummaryList("Autometa Lecture","80%");
        summaryList.add(summaryObject);

        summaryObject = new AttendanceSummaryList("Autometa Tut","80%");
        summaryList.add(summaryObject);

        summaryObject = new AttendanceSummaryList("Autometa Lecture","80%");
        summaryList.add(summaryObject);

        summaryObject = new AttendanceSummaryList("Autometa Lecture","80%");
        summaryList.add(summaryObject);

        summaryObject = new AttendanceSummaryList("Autometa Lecture","80%");
        summaryList.add(summaryObject);

        summaryObject = new AttendanceSummaryList("Autometa Lecture","80%");
        summaryList.add(summaryObject);
    }
}
