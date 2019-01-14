package com.saint_gab.sacconnecte;

import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;

public class Subject {

    private String mName;
    private String mColor;
    private ArrayList<Lesson> mLessons;

    private Timetable mTimetable;

    public Subject(String str, Timetable timetable)
    {
        mTimetable = timetable;
        String[] arguments = str.split(":");
        mName = arguments[0];
        mColor = arguments[1];

        mLessons = new ArrayList<>();
    }

    public Subject(String name, String color, Timetable timetable)
    {
        mTimetable = timetable;
        mName = name;
        mColor = color;

        mLessons = new ArrayList<>();
    }

    public void addLesson(Lesson lesson)
    {
        mLessons.add(lesson);
    }

    public int getLessonCount()
    {
        return mLessons.size();
    }

    public ArrayList<Lesson> getLessons()
    {
        return mLessons;
    }

    public void removeLesson(Lesson lesson)
    {
        if (mLessons.contains(lesson))
        {
            mLessons.remove(mLessons.indexOf(lesson));
        } else {
            Log.i("Subject", "Unable to remove lesson");
        }

    }

    public String getName()
    {
        return mName;
    }

    public void setName(String name) {
        mName = name;
        mTimetable.saveTimetable();
    }

    public String getColor()
    {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
        mTimetable.saveTimetable();
    }
}
