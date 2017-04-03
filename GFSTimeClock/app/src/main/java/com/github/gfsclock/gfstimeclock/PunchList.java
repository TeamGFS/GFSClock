package com.github.gfsclock.gfstimeclock;


import java.util.ArrayList;
import java.util.List;

public class PunchList {
    private List<String> employeeIds;
//    private String startDate;
//    private String endDate;

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

//    public String getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(String startDate) {
//        this.startDate = startDate;
//    }
//
//    public String getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(String endDate) {
//        this.endDate = endDate;
//    }

    public PunchList(String inID) {
        employeeIds = new ArrayList<String>();
        employeeIds.add(inID);
//        startDate = start;
//        endDate = end;
    };
}
