package com.saint_gab.sacconnecte;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class BackpackFragment extends Fragment {

    private View mView;
    private BackpackContent mBackpackContent;
    private Timetable mTimetable;
    private MainActivity mMainActivity;

    private ListView mExpectedContentListView;
    private ListView mUnexpectedContentListView;

    ArrayList<String> unexpectedContentStrings;

    public static BackpackFragment newInstance(BackpackContent backpackContent, Timetable timetable, MainActivity mainActivity) {

        return (new BackpackFragment(backpackContent, timetable, mainActivity));
    }

    @SuppressLint("ValidFragment")
    public BackpackFragment(BackpackContent backpackContent, Timetable timetable, MainActivity mainActivity)
    {
        mTimetable = timetable;
        mBackpackContent = backpackContent;
        mMainActivity = mainActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_backpack, container, false);
        mBackpackContent.setBackpackFragment(this);

        configureListView();

        final Button testButton = mView.findViewById(R.id.fragment_backpack_test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimetable.getExpectedEquipments();
                mBackpackContent.setContent("210074981CD1/2500658671B7");
                testButton.setVisibility(View.GONE);
            }
        });

        Button importButton = mView.findViewById(R.id.fragment_backpack_import_ical);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimetable.importIcalFile();
            }
        });

        return mView;
    }

    private void configureListView()
    {
        mUnexpectedContentListView = mView.findViewById(R.id.fragment_backpack_content_list_view);
        mExpectedContentListView = mView.findViewById(R.id.fragment_backpack_expected_content_list_view);

        ArrayList<Equipment> equipments = mBackpackContent.getContent();
        ArrayList<Equipment> expectedEquipments = mTimetable.getExpectedEquipments();
        ArrayList<Equipment> presentExpectedEquipments = new ArrayList<>();
        String[] unknowEquipments = mBackpackContent.getUnknowEquipmentsId();

        Log.i("BackpackFragment", "");
        Log.i("BackpackFragment", "--- ListView configuration ---");

        Log.i("BackpackFragment", "equipments size = " + equipments.size());

        //On regarde si des equipments attendus sont présents
        for (int i=0; i<equipments.size(); i++)
        {
            Log.i("BackpackFragment", "i =" + i);
            if (expectedEquipments.contains(equipments.get(i)))
            {
                Log.i("BackpackFragment", "present expected equipment : " + equipments.get(i).getName());
                presentExpectedEquipments.add((equipments.get(i)));
                expectedEquipments.remove(equipments.get(i));
            }
            else
            {
                Log.i("BackpackFragment", "equipment : " + equipments.get(i).getName() + " is not expected");
            }
        }

        ArrayList<String> expectedContentStrings = new ArrayList<>();
        unexpectedContentStrings = new ArrayList<>();



        //On remplit le tableau avec les equipments présents et attendus
        Log.i("BackpackFragment", "-presentExpectedEquipments :");
        for (int i=0; i < presentExpectedEquipments.size(); i++)
        {
            Log.i("BackpackFragment", "     " + presentExpectedEquipments.get(i).getName());
            expectedContentStrings.add(presentExpectedEquipments.get(i).getName());
        }

        //On remplit le tableau avec les equipments attendus non présents
        Log.i("BackpackFragment", "-expectedEquipments :");
        for (int i=0; i < expectedEquipments.size(); i++)
        {
            Log.i("BackpackFragment", "     " + expectedEquipments.get(i).getName());
            expectedContentStrings.add(expectedEquipments.get(i).getName());
        }

        //On remplit le tableau avec les equipments présents
        Log.i("BackpackFragment", "-equipments :");
        for (int i=0; i < equipments.size(); i++)
        {
            Log.i("BackpackFragment", "     " + equipments.get(i).getName());
            unexpectedContentStrings.add(equipments.get(i).getName());
        }


        //On remplit le tableau avec les equipments inconnus présents
        Log.i("BackpackFragment", "-unexpectedContentStrings :");
        for (int i=0; i < unknowEquipments.length; i++)
        {
            Log.i("BackpackFragment", "     " + unknowEquipments[i]);
            unexpectedContentStrings.add(unknowEquipments[i]);
        }


        //Configuration de la listView des éléments attendus
        if (expectedContentStrings != null)
        {
            int[] colors = new int[expectedContentStrings.size()];
            for(int i=0; i<expectedContentStrings.size(); i++)
            {
                if (i < presentExpectedEquipments.size()) colors[i] = Color.GREEN;
                else colors[i] = Color.RED;
            }

            String[] stringArray = new String[0];
            ArrayAdapter<String> adapter = new StringAdapterForBackpackFragment(mView.getContext(), expectedContentStrings.toArray(stringArray), colors);
            mExpectedContentListView.setAdapter(adapter);
            Log.i("BackpackFragment", "ListView refreshed !");
        }

        //Configuration de la listView des éléments non attendus
        if (unexpectedContentStrings != null)
        {
            String[] stringArray = new String[0];
            ArrayAdapter<String> adapter = new StringAdapterForBackpackFragment(mView.getContext(), unexpectedContentStrings.toArray(stringArray), null);
            mUnexpectedContentListView.setAdapter(adapter);

            mUnexpectedContentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mTimetable.getEquipmentFromName(unexpectedContentStrings.get(position)) == null)
                    {
                        mMainActivity.createEquipment(unexpectedContentStrings.get(position));
                    }
                }
            });
        }
    }



    public void refreshBackpackContent()
    {
        configureListView();
    }

}
