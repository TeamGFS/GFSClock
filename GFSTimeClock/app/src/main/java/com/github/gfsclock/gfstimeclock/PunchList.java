package com.github.gfsclock.gfstimeclock;


import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class PunchList {
    private List<Integer> id;
    private String startDate;
    private String endDate;

    public PunchList() {}
    public PunchList(int inID, String start, String end) {
        id = new ArrayList<Integer>();
        id.add(inID);

        DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");
        startDate = stringToDateTime(start).toString(fmt);
        endDate = stringToDateTime(end).toString(fmt);
    };

    public static String dateTimeToString(DateTime dateTime){
        Long dateTimeL = dateTime.getMillis();
        return dateTimeL.toString();
    }

    public static DateTime stringToDateTime(String timestamp){
        Long dateTimeL = Long.parseLong(timestamp);
        return new DateTime(dateTimeL);
    }


}
