package com.github.gfsclock.gfstimeclock;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PunchList {
    private List<String> employeeIds;
    private Date startDate;
    private Date endDate;

    public PunchList() {}
//    public PunchList(int inID, String start, String end) {
//        id = new ArrayList<Integer>();
//        id.add(inID);
//        startDate = start;
//        endDate = end;
//    };

    public List<String> getemployeeIds() {
        return employeeIds;
    }

    public void setemployeeIds(List<String> id) {
        this.employeeIds= id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public PunchList(String inID, Date end) {
        employeeIds = new ArrayList<String>();
        employeeIds.add(inID);
        endDate = end;
        Calendar cal = Calendar.getInstance();
        cal.setTime(end);
        cal.add(Calendar.DATE, -3);
        startDate = cal.getTime();
    }
}
