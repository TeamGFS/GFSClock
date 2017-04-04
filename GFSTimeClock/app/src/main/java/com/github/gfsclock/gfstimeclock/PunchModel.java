package com.github.gfsclock.gfstimeclock;

import java.util.Date;
import io.realm.RealmObject;

public class PunchModel extends RealmObject{
    /*
    "F1" = Start day
    "F2" = Start Break
    "F3" = Start Lunch
    "F4" = Job Change
    "F5" = End Day
    "F6" = End Break
    "F7" = End Lunch
     */

    private int payroll;
    private String docket;
//    private String jobCode; // used for job change
    private Date timeStamp;
//    private String department; // used for job change

    public int getpayroll() {
        return payroll;
    }

    public void setpayroll(int id) {
        this.payroll = id;
    }

    public String getDocket() {
        return docket;
    }

    public void setDocket(String docket) {
        this.docket = docket;
    }

//    public String getJobCode() {
//        return jobCode;
//    }

//    public void setJobCode(String jobCode) {
//        this.jobCode = jobCode;
//    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

//    public String getDepartment() {
//        return department;
//    }

//    public void setDepartment(String department) {
//        this.department = department;
//    }
}
