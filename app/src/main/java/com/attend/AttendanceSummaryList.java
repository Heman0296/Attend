package com.attend;

/**
 * Created by Himanshu on 11/7/2017.
 */

public class AttendanceSummaryList {
    private String nameOfClass, percentage;

    public AttendanceSummaryList(String nameOfClass, String percentage) {
        this.nameOfClass = nameOfClass;
        this.percentage = percentage;
    }

    public String getNameOfClass() {
        return nameOfClass;
    }

    public void setNameOfClass(String nameOfClass) {
        this.nameOfClass = nameOfClass;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
