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
        public String message;
    }

    public static class FaceDetails {
        public String identity;
        public String confidence;
        public String message;
    }

    public static class ClassesOfDay {
        public String subject;
        public String classLocation;
        public String timeOfClass;
        public String faculty;
        public String bluetoothID;
        public String message;
    }

    public static class SubjectAttendanceSummary {
        public int total_present;
        public int total_attendance;
        public String message;
    }

    public static class AttendanceSummaryAllSubjects {
        public String subject;
        public int total;
        public int present;
        public String message;
    }

    public static class SubjectAttendanceDatewise {
        public String date;
        public String attendanceStatus;
        public String message;
    }
}
