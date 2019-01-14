package com.saint_gab.sacconnecte;

import android.util.Log;

import javax.security.auth.login.LoginException;

public class Lesson {

    Subject mSubject;
    String mStartTime;
    String mEndTime;

    Timetable mTimetable;

    public Lesson(String str, Timetable timetable) {
        mTimetable = timetable;

        Log.i("Lesson", "Lesson: str = " + str);
        String[] arguments = str.split(":");
        Log.i("", "Lesson: subjectIndex = " + arguments[0]);
        mSubject = timetable.getSubject(Integer.decode(arguments[0]));
        mStartTime = arguments[1];
        mEndTime = arguments[2];

        mSubject.addLesson(this);
    }

    public Lesson(Subject subject, String startTime, String endTime)
    {
        mSubject = subject;
        mStartTime = startTime;
        mEndTime = endTime;

        mSubject.addLesson(this);
    }

    public void setSubject(Subject subject)
    {
        mSubject.removeLesson(this);
        mSubject = subject;
        mSubject.addLesson(this);
    }

    public String getName()
    {
        return mSubject.getName();
    }

    public String getStartTime()
    {
        return mStartTime;
    }

    public void setStartTime(String startTime)
    {
        mStartTime = startTime;
        mTimetable.saveTimetable();
    }

    public String getEndTime()
    {
        return mEndTime;
    }

    public void setEndTime(String endTime)
    {
        mEndTime = endTime;
        mTimetable.saveTimetable();
    }

    public String getColor()
    {
        return mSubject.getColor();
    }

    public Subject getSubject()
    {
        return mSubject;
    }

}
