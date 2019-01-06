package com.saint_gab.sacconnecte;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimetableFragment extends Fragment {

    private View view;
    private Timetable mTimetable;


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
        view = inflater.inflate(R.layout.fragment_timetable, container, false);
        configureViewPagerAndTabs();
        return view;
    }


    private void configureViewPagerAndTabs()
    {
        //Get ViewPager from layout
        ViewPager pager = (ViewPager)view.findViewById(R.id.activity_main_viewpager);
        //Set Adapter DayPageAdapter and glue it together
        pager.setAdapter(new DayPageAdapter(getChildFragmentManager(), getContext(), mTimetable));

        TabLayout tabs = (TabLayout)view.findViewById(R.id.activity_main_tabs);
        //Glue TabsLayout and viewPager together
        tabs.setupWithViewPager(pager);
        //Design purpose, Tabs have the same width
        tabs.setTabMode(TabLayout.MODE_FIXED);
    }
}
