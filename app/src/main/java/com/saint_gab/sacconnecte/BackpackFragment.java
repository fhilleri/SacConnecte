package com.saint_gab.sacconnecte;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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

        Button testButton = mView.findViewById(R.id.fragment_backpack_test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimetable.getExpectedEquipments();
                mBackpackContent.setContent("210074981CD1");
            }
        });

        return mView;
    }

    private void configureListView()
    {
        mListView = mView.findViewById(R.id.fragment_backpack_list_view);
        String[] contentNames = mBackpackContent.getContentStrings();
        int[] contentColors = mBackpackContent.getColors();
        if (contentNames != null)
        {
            ArrayAdapter<String> adapter = new StringAdapterForBackpackFragment(mView.getContext(), contentNames, contentColors);
            mListView.setAdapter(adapter);
        }
    }

    public void refreshBackpackContent()
    {
        configureListView();
    }

}
