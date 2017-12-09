package com.attend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attend.routes.StudentRoutes;
import com.attend.utils.VolleyHandler;

import java.util.ArrayList;
import java.util.List;

public class Classes extends Fragment {
    private List<ClassList> classList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ClassesAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_classes, container,false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mAdapter = new ClassesAdapter(classList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getClassesOfDay("1140917");
        return rootView;
    }

    public void getClassesOfDay(String rollno) {
        StudentRoutes studentRoutes = new StudentRoutes();
        studentRoutes.getClassesOfDay(rollno, new VolleyHandler.ApiResponse<Models.ClassesOfDay[]>() {
            @Override
            public void onCompletion(Models.ClassesOfDay[] classesOfDay) {
                for(int i = 0; i < classesOfDay.length; i++){
                    ClassList classObject = new ClassList(classesOfDay[i].subject.toString(), classesOfDay[i].classroom.toString(), classesOfDay[i].begin_time.toString().substring(0,4), classesOfDay[i].end_time.toString().substring(0,4), classesOfDay[i].faculty_name.toString(), classesOfDay[i].bluetooth_address.toString());
                    classList.add(classObject);
                }
                mAdapter.notifyDataSetChanged();
            }

        });
    }
}
