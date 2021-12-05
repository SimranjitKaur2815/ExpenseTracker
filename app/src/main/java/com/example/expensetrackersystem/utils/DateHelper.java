package com.example.expensetrackersystem.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateHelper {
    public static String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("MMM dd, yyyy");
        return date.format(cal.getTime());
    }

    public static String getProcessedDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM dd, yyyy");
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return month_date.format(cal.getTime());
    }

    public static String getTodayDate(String pattern) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat(pattern);
        return month_date.format(cal.getTime());
    }

    public static String getDay(int day, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat month_date = new SimpleDateFormat(pattern);
        return month_date.format(cal.getTime());
    }

    public static String getDateInCommonFormat(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(timestamp);
    }
}
