package com.attend;

/**
 * Created by Himanshu on 12/15/2017.
 */

public class AttendanceStatusJSON {
    public String period;
    public String attendanceStatus;

    public String getPeriod(){ return period; }

    public void setPeriod(String period) { this.period = period; }

    public String getAttendanceStatus() { return attendanceStatus; }

    public void setAttendanceStatus(String attendanceStatus) { this.attendanceStatus = attendanceStatus; }
}
