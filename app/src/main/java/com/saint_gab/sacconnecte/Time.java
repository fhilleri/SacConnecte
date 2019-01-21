package com.saint_gab.sacconnecte;

public class Time {

    private int mHour;
    private int mMinute;

    public Time(String str)
    {
        String[] arguments = str.split("h");
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

}
