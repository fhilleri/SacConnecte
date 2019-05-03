package com.saint_gab.sacconnecte;

import android.util.Log;

public class Time {

    private int mHour;
    private int mMinute;

    public Time(String str)
    {
        Log.i("Time", "Time: timeStr = " + str);
        String[] arguments = str.split("h");
        if (arguments[0].charAt(0) == '0')
        {
            arguments[0] = arguments[0].substring(1);
        }
        mHour = Integer.decode(arguments[0]);
        mMinute = Integer.decode(arguments[1]);
    }

    public Time(int hour, int minute)
    {
        mHour = hour;
        mMinute = minute;
    }

    public int getHour()
    {
        return mHour;
    }

    public int getMinute()
    {
        return mMinute;
    }

    public int getTotalMinute() {return mMinute + mHour * 60;}

    public String toString()
    {
        return mHour + "h" + mMinute;
    }


    public boolean isAfter(Time otherTime)
    {
        return (getTotalMinute() > otherTime.getTotalMinute());
    }

}
