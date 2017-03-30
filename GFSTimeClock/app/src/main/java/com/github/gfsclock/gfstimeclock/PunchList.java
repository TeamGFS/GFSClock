package com.github.gfsclock.gfstimeclock;


import java.util.ArrayList;
import java.util.List;

public class PunchList {
    private List<Integer> id;
//    private String startDate;
//    private String endDate;

    public PunchList() {}
//    public PunchList(int inID, String start, String end) {
//        id = new ArrayList<Integer>();
//        id.add(inID);
//        startDate = start;
//        endDate = end;
//    };

    public List<Integer> getId() {
        return id;
    }

    public void setId(List<Integer> id) {
        this.id = id;
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

    public PunchList(int inID) {
        id = new ArrayList<Integer>();
        id.add(inID);
//        startDate = start;
//        endDate = end;
    };
}
