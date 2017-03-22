package com.github.gfsclock.gfstimeclock;

import io.realm.RealmObject;
import org.joda.time.DateTime;

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

    private int id;
    private String docket;
    private String jobCode; // used for job change
    private DateTime timeStamp;
    private String department; // used for job change

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocket() {
        return docket;
    }

    public void setDocket(String docket) {
        this.docket = docket;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(DateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
