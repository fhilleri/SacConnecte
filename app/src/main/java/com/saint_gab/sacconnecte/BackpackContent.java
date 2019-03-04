package com.saint_gab.sacconnecte;

import android.graphics.Color;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BackpackContent {

    private Timetable mTimetable;
    private ArrayList<Equipment> mEquipments;
    private ArrayList<String> mUnknowEquipmentsId;
    private BackpackFragment mBackpackFragment;

    public BackpackContent(Timetable timetable)
    {
        Log.i("BackpackContent", "Initialisation");
        mTimetable = timetable;
        mEquipments = new ArrayList<>();
        mUnknowEquipmentsId = new ArrayList<>();
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

        mEquipments.clear();
        mUnknowEquipmentsId.clear();
        for(int i=0; i<correctEquipmentsIds.length; i++)
        {
            String name = mTimetable.getEquipmentNameFromID(correctEquipmentsIds[i]);
            if (name != null)
            {
                mEquipments.add(mTimetable.getEquipmentFromId(correctEquipmentsIds[i]));
            }
            else
            {
                mUnknowEquipmentsId.add(correctEquipmentsIds[i]);
            }
        }
        if (mBackpackFragment != null) mBackpackFragment.refreshBackpackContent();
    }

    public ArrayList<Equipment> getContent()
    {
        return mEquipments;
    }


    public Equipment[] getContentArray()
    {
        Equipment[] result = new Equipment[mEquipments.size()];
        return mEquipments.toArray(result);
    }

    public String[] getUnknowEquipmentsId()
    {
        String[] result = new String[mUnknowEquipmentsId.size()];
        return mUnknowEquipmentsId.toArray(result);
    }

    public String[] getContentIds()
    {
        String[] contentIds = new String[mEquipments.size()];
        for (int i=0; i<contentIds.length; i++)
        {
            contentIds[i] = mEquipments.get(i).getId();
        }
        return contentIds;
    }

    //Retourne les couleurs des equipments de getContentStings, equipment attendu = vert, equipment random = blanc
    /*public int[] getColors()
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
    }*/
}
