package com.saint_gab.sacconnecte;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class SettingsFragment extends Fragment {

    Timetable mTimetable;
    View mView;

    public static SettingsFragment newInstance(Timetable timetable) {
        return (new SettingsFragment(timetable));
    }

    @SuppressLint("ValidFragment")
    public SettingsFragment(Timetable timetable) {
        mTimetable = timetable;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_settings, container, false);

        Button importButton = mView.findViewById(R.id.fragment_settings_import_button);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimetable.importIcalFile();
            }
        });

        return mView;
    }

}
