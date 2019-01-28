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
        mEquipments = new String[equipmentsIds.length];
        for(int i=0; i<equipmentsIds.length; i++)
        {
            String name = mTimetable.getEquipmentNameFromID(equipmentsIds[i]);
            mEquipments[i] = (name != null ? name : equipmentsIds[i] );
        }
    }

    public String[] getContentStrings()
    {
        return mEquipments;
    }
}
