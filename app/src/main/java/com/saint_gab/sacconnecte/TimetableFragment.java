package com.saint_gab.sacconnecte;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimetableFragment extends Fragment implements NewLessonDialogFragment.NewLessonDialogListener {

    private View mView;
    private Timetable mTimetable;

    private Button mNewLessonButton;
    private Button mEditLessonButton;
    private Button mDeleteLessonButton;

    private TabLayout mTabs;

    private boolean onDeleteMode = false;
    private boolean onEditMode = false;


    public static TimetableFragment newInstance(Timetable timetable) {
        return (new TimetableFragment(timetable));
    }

    public TimetableFragment(){}//Empty constructor

    @SuppressLint("ValidFragment")
    public TimetableFragment(Timetable timetable)
    {
        mTimetable = timetable;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_timetable, container, false);
        configureViewPagerAndTabs();
        return mView;
    }


    private void configureViewPagerAndTabs()
    {
        //Get ViewPager from layout
        ViewPager pager = (ViewPager)mView.findViewById(R.id.fragment_timetable_activity_main_viewpager);
        //Set Adapter DayPageAdapter and glue it together
        pager.setAdapter(new DayPageAdapter(this, getChildFragmentManager(), getContext(), mTimetable));

        mTabs = (TabLayout)mView.findViewById(R.id.activity_main_tabs);
        //Glue TabsLayout and viewPager together
        mTabs.setupWithViewPager(pager);
        //Design purpose, Tabs have the same width
        mTabs.setTabMode(TabLayout.MODE_FIXED);

        configureButtons();
    }

    private void configureButtons()
    {
        mNewLessonButton = mView.findViewById(R.id.fragment_timetable_button_new);
        mNewLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newLesson();
            }
        });

        mEditLessonButton = mView.findViewById(R.id.fragment_timetable_button_edit);
        mEditLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButton();
            }
        });

        mDeleteLessonButton = mView.findViewById(R.id.fragment_timetable_button_delete);
        mDeleteLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteButton();
            }
        });
    }

    private void newLesson()
    {
        DialogFragment newLessonDialogFragment = new NewLessonDialogFragment(this, mTimetable, null);
        newLessonDialogFragment.show(getFragmentManager(), "newSubject");
    }

    private void editButton()
    {
        onEditMode = !onEditMode;
        if (onEditMode) onDeleteMode = false;
        refreshButtonColor();
    }

    private void deleteButton()
    {
        onDeleteMode = !onDeleteMode;
        if (onDeleteMode) onEditMode = false;
        refreshButtonColor();
    }

    private void refreshButtonColor()
    {
        mEditLessonButton.setTextColor(onEditMode ? Color.BLUE : Color.BLACK);
        mDeleteLessonButton.setTextColor(onDeleteMode ? Color.RED : Color.BLACK);
    }

    public int getMode()//Rien -> 0 Edition -> 1 Suppression -> 2
    {
        if (onEditMode) return 1;
        else if (onDeleteMode) return 2;
        else return 0;
    }

    @Override
    public void onDialogPositiveClick(Lesson newLesson) {
        if (newLesson != null) mTimetable.addLesson(newLesson, mTabs.getSelectedTabPosition());
        onDeleteMode = false;
        onEditMode = false;
        refreshButtonColor();
    }
}
