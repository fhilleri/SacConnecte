package com.saint_gab.sacconnecte;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class BackpackFragment extends Fragment {

    private View mView;
    private BackpackContent mBackpackContent;
    private Timetable mTimetable;

    private ListView mListView;

    public static BackpackFragment newInstance(BackpackContent backpackContent, Timetable timetable) {

        return (new BackpackFragment(backpackContent, timetable));
    }

    @SuppressLint("ValidFragment")
    public BackpackFragment(BackpackContent backpackContent, Timetable timetable)
    {
        mTimetable = timetable;
        mBackpackContent = backpackContent;
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
                mBackpackContent.setContent("210074981CD1/70002E414659/021348964201");
                testButton.setVisibility(View.GONE);
            }
        });

        return mView;
    }

    private void configureListView()
    {
        mListView = mView.findViewById(R.id.fragment_backpack_list_view);

        ArrayList<Equipment> equipments = mBackpackContent.getContent();
        ArrayList<Equipment> expectedEquipments = mTimetable.getExpectedEquipments();
        ArrayList<Equipment> presentExpectedEquipments = new ArrayList<>();
        String[] unknowEquipments = mBackpackContent.getUnknowEquipmentsId();

        //On regarde si des equipments attendus sont présents
        for (int i=0; i<equipments.size(); i++)
        {
            if (expectedEquipments.contains(equipments.get(i)))
            {
                presentExpectedEquipments.add((equipments.get(i)));
                expectedEquipments.remove(equipments.get(i));
                equipments.remove(equipments.get(i));
            }
        }

        int[] colors = new int[presentExpectedEquipments.size() + equipments.size() + expectedEquipments.size() + unknowEquipments.length];
        String[] strings = new String[colors.length];

        int writingIndex = 0;
        //On remplit le tableau avec les equipments présents et attendus
        for (int i=0; i < presentExpectedEquipments.size(); i++)
        {
            strings[writingIndex] = presentExpectedEquipments.get(i).getName();
            colors[writingIndex] = Color.GREEN;
            writingIndex++;
        }

        //On remplit le tableau avec les equipments présents
        for (int i=0; i < equipments.size(); i++)
        {
            strings[writingIndex] = equipments.get(i).getName();
            colors[writingIndex] = Color.WHITE;
            writingIndex++;
        }

        //On remplit le tableau avec les equipments attendus non présents
        for (int i=0; i < expectedEquipments.size(); i++)
        {
            strings[writingIndex] = expectedEquipments.get(i).getName();
            colors[writingIndex] = Color.RED;
            writingIndex++;
        }

        //On remplit le tableau avec les equipments inconnus présents
        for (int i=0; i < unknowEquipments.length; i++)
        {
            strings[writingIndex] = unknowEquipments[i];
            colors[writingIndex] = Color.WHITE;
            writingIndex++;
        }



        if (strings != null && strings.length > 0)
        {
            ArrayAdapter<String> adapter = new StringAdapterForBackpackFragment(mView.getContext(), strings, colors);
            mListView.setAdapter(adapter);
        }
    }

    public void refreshBackpackContent()
    {
        configureListView();
    }

}
