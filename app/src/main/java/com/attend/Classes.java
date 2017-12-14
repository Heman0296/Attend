package com.attend;

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

import com.attend.routes.StudentRoutes;
import com.attend.utils.VolleyHandler;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Classes extends Fragment {
    public static HashMap<String,String> hashMap = new HashMap<String,String>();
    public static SharedPreferences sharedPreferences;
    private List<ClassList> classList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ClassesAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_classes, container,false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mAdapter = new ClassesAdapter(classList,this.getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        sharedPreferences = this.getActivity().getSharedPreferences("studentDetails", Context.MODE_PRIVATE);

        getClassesOfDay("1140917");
        return rootView;
    }

    public void getClassesOfDay(String rollno) {
        StudentRoutes studentRoutes = new StudentRoutes();
        studentRoutes.getClassesOfDay(rollno, new VolleyHandler.ApiResponse<Models.ClassesOfDay[]>() {
            @Override
            public void onCompletion(Models.ClassesOfDay[] classesOfDay) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(sharedPreferences.contains("statusDate")){
                    String previousDate = sharedPreferences.getString("statusDate",null);
                    if(previousDate.compareTo(new SimpleDateFormat("yyyyMMdd").format(new Date()))!=0){
                        AttendanceStatus attendanceStatus;
                        for(int i = 0; i < classesOfDay.length; i++){
                            attendanceStatus  = new AttendanceStatus(false,false,classesOfDay[i].subject);
                            hashMap.put(String.valueOf(i + 1),String.valueOf(attendanceStatus.getInTimeStatus()) + "," + String.valueOf(attendanceStatus.getOutTimeStatus()) + "," + String.valueOf(attendanceStatus.getSubjectName()));
                        }
                        Gson gson = new Gson();
                        String json =   gson.toJson(hashMap);
                        editor.putString("attendanceStatus", json);
                        editor.apply();
                        editor.commit();
                        editor.putString("statusDate", new SimpleDateFormat("yyyyMMdd").format(new Date()));
                        editor.apply();
                        editor.commit();
                    }
                }
                else{
                    AttendanceStatus attendanceStatus;
                    for(int i = 0; i < classesOfDay.length; i++){
                        attendanceStatus = new AttendanceStatus(false,false,classesOfDay[i].subject);
                        hashMap.put(String.valueOf(i + 1),String.valueOf(attendanceStatus.getInTimeStatus()) + "," + String.valueOf(attendanceStatus.getOutTimeStatus()) + "," + String.valueOf(attendanceStatus.getSubjectName()));
                    }
                    Gson gson = new Gson();
                    String json =  gson.toJson(hashMap);
                    editor.putString("attendanceStatus", json);
                    editor.apply();
                    editor.commit();
                    editor.putString("statusDate", new SimpleDateFormat("yyyyMMdd").format(new Date()));
                    editor.apply();
                    editor.commit();
                }

                for(int i = 0; i < classesOfDay.length; i++){
                    ClassList classObject = new ClassList(classesOfDay[i].subject.toString(), classesOfDay[i].classroom.toString(), classesOfDay[i].begin_time.toString().substring(0,5), classesOfDay[i].end_time.toString().substring(0,5), classesOfDay[i].faculty_name.toString(), classesOfDay[i].bluetooth_address.toString());
                    classList.add(classObject);

                }
                sharedPreferences.getAll();
                mAdapter.notifyDataSetChanged();
            }

        });
    }
}
