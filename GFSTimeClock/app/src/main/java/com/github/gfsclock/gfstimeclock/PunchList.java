package com.github.gfsclock.gfstimeclock;


import java.util.ArrayList;
import java.util.List;

public class PunchList {
    private List<Integer> id;
    private String startDate;
    private String endDate;

    public PunchList() {}
    public PunchList(int inID, String start, String end) {
        id = new ArrayList<Integer>();
        id.add(inID);
        startDate = start;
        endDate = end;
    };
    public PunchList(int inID) {
        id = new ArrayList<Integer>();
        id.add(inID);
//        startDate = start;
//        endDate = end;
    };
}
