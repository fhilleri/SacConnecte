package com.saint_gab.sacconnecte;

import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;

public class Subject {

    private String mName;
    private String mColor;
    private ArrayList<Lesson> mLessons;
    private ArrayList<Equipment> mEquipments;

    private Timetable mTimetable;

    public Subject(String str, Timetable timetable)
    {
        mTimetable = timetable;
        String[] arguments = str.split(":");
        mName = arguments[0];
        mColor = arguments[1];

        if (arguments.length > 2)
        {
            String[] equipmentsStr = arguments[2].split(",");
            mEquipments = new ArrayList<>();
            for (int i=0; i<equipmentsStr.length; i++)
            {
                mEquipments.add(mTimetable.getEquipment(Integer.decode(equipmentsStr[i])));
            }
        }

        mLessons = new ArrayList<>();
    }

    public Subject(String name, String color, ArrayList<Equipment> equipments, Timetable timetable)
    {
        mTimetable = timetable;
        mEquipments = equipments;
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
            mLessons.remove(lesson);
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
    }

    public String getColor()
    {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public ArrayList<Equipment> getEquipments()
    {
        return mEquipments;
    }

    public void setEquipments(ArrayList<Equipment> equipments)
    {
        mEquipments = equipments;
    }

    public void removeEquipment(Equipment equipment)
    {
        if (mEquipments != null && mEquipments.contains(equipment))    mEquipments.remove(equipment);
    }
}
