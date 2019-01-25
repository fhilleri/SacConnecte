package com.saint_gab.sacconnecte;

public class BackpackContent {

    private Timetable mTimetable;
    private String[] mEquipments;

    public BackpackContent(Timetable timetable)
    {
        mTimetable = timetable;
    }

    public void setContent(String contentStr)
    {
        String[] equipmentsIds = contentStr.split("/");
        for(int i=0; i<equipmentsIds.length; i++)
        {

        }
    }
}
