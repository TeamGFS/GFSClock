package com.github.gfsclock.gfstimeclock;


import java.util.ArrayList;
import java.util.List;

public class PunchList {
    private List<String> employeIds;
//    private String startDate;
//    private String endDate;

    public PunchList() {}
//    public PunchList(int inID, String start, String end) {
//        id = new ArrayList<Integer>();
//        id.add(inID);
//        startDate = start;
//        endDate = end;
//    };

    public List<String> getId() {
        return employeIds;
    }

    public void setId(List<String> id) {
        this.employeIds = id;
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
        employeIds = new ArrayList<String>();
        employeIds.add(inID);
//        startDate = start;
//        endDate = end;
    };
}
