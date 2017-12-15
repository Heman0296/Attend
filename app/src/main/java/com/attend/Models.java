package com.attend;

/**
 * Created by Ankit Joshi on 05-11-2017.
 */

public class Models {
    public static class Student {
        public String name;
        public String rollno;
        public String email;
        public String phoneno;
        public String year;
        public String branch;
        public String section;
        public String image_path;
        public String message;
    }

    public static class FaceDetails {
        public String identity;
        public String confidence;
        public String message;
    }

    public static class ClassesOfDay {
        public String subject;
        public String classroom;
        public String begin_time;
        public String end_time;
        public String faculty_name;
        public String bluetooth_address;
        public String message;
    }

    public static class SubjectAttendanceSummary {
        public String total_present;
        public String total_attendance;
        public String message;
    }

    public static class AttendanceSummaryAllSubjects {
        public String subject;
        public String total_attendance;
        public String total_present;
        public String message;
    }

    public static class SubjectAttendanceDatewise {
        public String date;
        public boolean presence_flag;
        public int period;
        public String message;
    }

    public static class Login {
        public String token;
        public String message;
        public String duration;
    }
}
