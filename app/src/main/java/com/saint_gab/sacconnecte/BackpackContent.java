package com.saint_gab.sacconnecte;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
    private Resources mRes;

    private Context mContext;

    public BackpackContent(Timetable timetable, Context context, Resources res)
    {
        Log.i("BackpackContent", "Initialisation");
        mContext = context;
        mTimetable = timetable;
        mEquipments = new ArrayList<>();
        mUnknowEquipmentsId = new ArrayList<>();
        mRes = res;
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
        boolean emptyList = false;
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
                if (equipmentsIds[i] == "n") emptyList = true;
            }
        }

        mEquipments.clear();
        mUnknowEquipmentsId.clear();
        if (!emptyList)
        {
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
        }
        if (mBackpackFragment != null) mBackpackFragment.refreshBackpackContent();

        sendNotification();
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

    public void sendNotification()
    {
        ArrayList<Equipment> expectedEquipments = mTimetable.getExpectedEquipments();
        ArrayList<Equipment> presentExpectedEquipments = new ArrayList<>();

        for (int i=0; i<mEquipments.size(); i++)
        {
            if (expectedEquipments.contains(mEquipments.get(i)))
            {
                presentExpectedEquipments.add(mEquipments.get(i));
                expectedEquipments.remove(mEquipments.get(i));
            }
        }

        //--- Notification ---
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "com.saint_gab.sacconnecte")
                .setSmallIcon(R.drawable.ic_logosac)
                .setContentTitle(mRes.getString(R.string.app_name))
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (mRes.getString(R.string.language).equals("fr")) builder.setContentText(expectedEquipments.size() > 0 ? "Il vous manque " + expectedEquipments.size() +  (expectedEquipments.size() > 1 ? " matériels" : " matériel") : "Vous avez tout votre matériel");
        builder.setContentText(expectedEquipments.size() > 0 ? "You miss " + expectedEquipments.size() +  (expectedEquipments.size() > 1 ? " equipments" : " equipment") : "You have all your equipments");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
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
