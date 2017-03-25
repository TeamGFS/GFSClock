package com.github.gfsclock.gfstimeclock;

import java.util.Date;

import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.SignStyle;
import org.threeten.bp.temporal.ChronoField;

/**
 * Created by kentkent on 3/24/17.
 */

public class DateFormatUtil {
    public static String dateObjToString(Date dateObj){
        DateTimeFormatter dateStr = new DateTimeFormatterBuilder()
                .appendValue(ChronoField.INSTANT_SECONDS, 1, 19, SignStyle.NEVER)
                .appendValue(ChronoField.MILLI_OF_SECOND, 3)
                .toFormatter();
        return dateStr.toString();
    }

    public static Date strToDate(String dateString){
        Date date = new Date(Long.parseLong(dateString) * 1000);
        return date;
    }
}
