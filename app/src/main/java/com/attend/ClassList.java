package com.attend;

import java.io.Serializable;

/**
 * Created by Himanshu on 11/5/2017.
 */

public class ClassList implements Serializable {

    private String subject, classroom, begin_time, end_time, faculty_name, bluetooth_address;

    public ClassList(String subject, String classroom, String begin_time, String end_time, String faculty_name, String bluetooth_address) {
        this.subject = subject;
        this.classroom = classroom;
        this.begin_time = begin_time;
        this.end_time = end_time;
        this.faculty_name = faculty_name;
        this.bluetooth_address = bluetooth_address;
    }

    public String getNameOfClass() {
        return subject;
    }

    public void setNameOfClass(String subject) {
        this.subject = subject;
    }

    public String getLocation() {
        return classroom;
    }

    public void setLocation(String classroom) {
        this.classroom = classroom;
    }

    public String getTime() {
        return begin_time;
    }

    public void setTime(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getBluetooth_address() {
        return bluetooth_address;
    }

    public void setBluetooth_address(String bluetooth_address) {
        this.bluetooth_address = bluetooth_address;
    }

    public String getFaculty_name() {
        return faculty_name;
    }

    public void setFaculty_name(String faculty_name) {
        this.faculty_name = faculty_name;
    }
}
