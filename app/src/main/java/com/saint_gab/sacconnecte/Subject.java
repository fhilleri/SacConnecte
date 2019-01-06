package com.saint_gab.sacconnecte;

import android.util.Log;

import java.util.ArrayList;

public class Subject {

    private String mName;
    private String mColor;
    private ArrayList<Lesson> mLessons;

    public Subject(String name, String color)
    {
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

    public String getColor()
    {
        return mColor;
    }
}
