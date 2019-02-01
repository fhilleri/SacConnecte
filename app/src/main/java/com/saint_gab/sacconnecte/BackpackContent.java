package com.saint_gab.sacconnecte;

public class BackpackContent {

    private Timetable mTimetable;
    private String[] mEquipments;
    private BackpackFragment mBackpackFragment;

    public BackpackContent(Timetable timetable)
    {
        mTimetable = timetable;
    }

    public void setBackpackFragment(BackpackFragment backpackFragment)
    {
        mBackpackFragment = backpackFragment;
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
        if (mBackpackFragment != null) mBackpackFragment.refreshBackpackContent();
    }



    public String[] getContentStrings()
    {
        return mEquipments;
    }
}
