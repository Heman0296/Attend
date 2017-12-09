package com.attend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.attend.routes.StudentRoutes;
import com.attend.utils.VolleyHandler;

/**
 * Created by Himanshu on 9/13/2017.
 */

public class Profile extends Fragment {

    public static Models.Student details = new Models.Student();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_profile, container, false);
        setStudentDetails("1140917");
        return rootView;
    }

    public void setStudentDetails(String rollno){
        StudentRoutes studentRoutes = new StudentRoutes();
        studentRoutes.getDetails(rollno, new VolleyHandler.ApiResponse<Models.Student>() {
            @Override
            public void onCompletion(Models.Student studentDetails) {
                details.branch = studentDetails.branch;
                details.email = studentDetails.email;
                details.name = studentDetails.name;
                details.phoneno = studentDetails.phoneno;
                details.rollno = studentDetails.rollno;
                details.year = studentDetails.year;
                details.section = studentDetails.section;
                details.image_path = studentDetails.image_path;

                TextView textView = (TextView) getActivity().findViewById(R.id.name);
                textView.setText(details.name);
                textView = (TextView) getActivity().findViewById(R.id.phone_number);
                textView.setText(details.phoneno);
                textView = (TextView) getActivity().findViewById(R.id.email_id);
                textView.setText(details.email);
                textView = (TextView) getActivity().findViewById(R.id.roll_number);
                textView.setText(details.rollno);
                textView = (TextView) getActivity().findViewById(R.id.branch);
                textView.setText(details.branch);
            }
        });
    }
}
