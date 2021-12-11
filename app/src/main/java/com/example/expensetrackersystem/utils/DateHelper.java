package com.example.expensetrackersystem.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
    private static final String TAG = "DateHelper";

    public static Date convertStringToDate(String date) {
        try {

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
            Date formattedDate = dateFormat.parse(date);
            cal.setTime(formattedDate);
            return cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "convertStringToDate: " + e.toString());
            return null;
        }
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
