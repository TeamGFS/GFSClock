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

    private String payroll;
    private String docket;
    private String jobCode;
    private Date timestamp;

    public String getpayroll() {
        return payroll;
    }

    public void setpayroll(int id) {
        this.payroll = Integer.toString(id);
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
