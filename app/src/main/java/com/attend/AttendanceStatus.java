package com.attend;

/**
 * Created by Himanshu on 12/13/2017.
 */

public class AttendanceStatus {
    private boolean inTimeStatus, outTimeStatus;
    private String subjectName;

    public AttendanceStatus(boolean inTimeStatus, boolean outTimeStatus,String subjectName){
        this.inTimeStatus = inTimeStatus;
        this.outTimeStatus = outTimeStatus;
        this.subjectName = subjectName;
    }

    public String getSubjectName(){ return subjectName; }

    public void setSubjectName(String subjectName){ this.subjectName = subjectName; }

    public boolean getInTimeStatus(){
        return inTimeStatus;
    }

    public void setInTimeStatus(boolean inTimeStatus){ this.inTimeStatus = inTimeStatus; }

    public boolean getOutTimeStatus(){
        return outTimeStatus;
    }

    public void setOutTimeStatus(boolean outTimeStatus){ this.outTimeStatus = outTimeStatus; }
}
