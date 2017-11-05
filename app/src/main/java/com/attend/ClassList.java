package com.attend;

/**
 * Created by Himanshu on 11/5/2017.
 */

public class ClassList {

    private String nameOfClass, location, time;

    public ClassList(String nameOfClass, String location, String time) {
        this.nameOfClass = nameOfClass;
        this.location = location;
        this.time = time;
    }

    public String getNameOfClass() {
        return nameOfClass;
    }

    public void setNameOfClass(String nameOfClass) {
        this.nameOfClass = nameOfClass;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
