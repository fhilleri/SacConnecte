package com.saint_gab.sacconnecte;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class BackpackContent {

    private Timetable mTimetable;
    private String[] mEquipmentsId;
    private String[] mEquipmentsString;
    private BackpackFragment mBackpackFragment;

    public BackpackContent(Timetable timetable)
    {
        Log.i("BackpackContent", "Initialisation");
        mTimetable = timetable;
        mEquipmentsId = new String[0];
        mEquipmentsString = new String[0];
    }

    public void setBackpackFragment(BackpackFragment backpackFragment)
    {
        mBackpackFragment = backpackFragment;
    }

    public void setContent(String contentStr)
    {
        Log.i("BackpackContent", "setContent: contentStr.length = " + contentStr.length());
        String[] equipmentsIds = contentStr.split("/");

        //On élimine les ids qui ne font pas 12 caractères
        int correctEquipmentCount = 0;
        Pattern p = Pattern.compile("^0123456789ABCDEF");
        for (int i=0; i<equipmentsIds.length; i++)
        {
            if (equipmentsIds[i].length() == 12 && !(p.matcher(equipmentsIds[i]).find())) correctEquipmentCount++;
        }
        String[] correctEquipmentsIds = new String[correctEquipmentCount];
        int correctEquipmentWritingIndex = 0;
        for (int i=0; i<equipmentsIds.length; i++)
        {
            if (equipmentsIds[i].length() == 12 && !(p.matcher(equipmentsIds[i]).find()))
            {
                Log.i("BackpackContent", "Valide string :" + equipmentsIds[i]);
                correctEquipmentsIds[correctEquipmentWritingIndex] = equipmentsIds[i];
                correctEquipmentWritingIndex++;
            }
            else
            {
                Log.i("BackpackContent", "Invalide string :" + equipmentsIds[i]);
            }
        }

        mEquipmentsId = correctEquipmentsIds;
        mEquipmentsString = new String[correctEquipmentsIds.length];
        for(int i=0; i<correctEquipmentsIds.length; i++)
        {
            String name = mTimetable.getEquipmentNameFromID(correctEquipmentsIds[i]);
            mEquipmentsString[i] = (name != null ? name : correctEquipmentsIds[i] );
        }
        if (mBackpackFragment != null) mBackpackFragment.refreshBackpackContent();
    }


    public String[] getContentStrings()
    {
        return mEquipmentsString;
    }

    //Retourne les couleurs des equipments de getContentStings, equipment attendu = vert, equipment random = blanc
    public int[] getColors()
    {
        int[] colors = new int[mEquipmentsString.length];
        ArrayList<Equipment> expectedEquipments = mTimetable.getExpectedEquipments();
        for (int i=0; i<mEquipmentsId.length; i++)
        {
            if (expectedEquipments.contains(mTimetable.getEquipmentFromId(mEquipmentsId[i])))
            {
                colors[i] = Color.rgb(0, 255, 0);
            }
            else
            {
                colors[i] = Color.rgb(255, 255, 255);
            }
        }
        return colors;
    }
}
