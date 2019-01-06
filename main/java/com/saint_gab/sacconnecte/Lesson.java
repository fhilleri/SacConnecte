package com.saint_gab.sacconnecte;

public class Lesson {

    Subject mSubject;
    String mStartTime;
    String mEndTime;

    public Lesson(Subject subject, String startTime, String endTime)
    {
        mSubject = subject;
        mStartTime = startTime;
        mEndTime = endTime;

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

    public String getEndTime()
    {
        return mEndTime;
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
