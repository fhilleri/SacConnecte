package com.saint_gab.sacconnecte;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class DayPageAdapter extends FragmentPagerAdapter {

    private DayFragment mDayFragments[];
    private Timetable mTimetable;

    //Constructeur
    public DayPageAdapter(FragmentManager mgr, Context context, Timetable timetable)
    {
        super(mgr);
        Resources res =  context.getResources();
        mTimetable = timetable;
        String[] daysNames = res.getStringArray(R.array.days_name);
        mDayFragments = new DayFragment[daysNames.length];

        for(int i=0; i<mDayFragments.length; i++)
        {
            mDayFragments[i] = DayFragment.newInstance(timetable, i, daysNames[i]);
        }
    }

    @Override
    public int getCount() {
        return mDayFragments.length;
    }

    @Override
    public Fragment getItem(int position)
    {
        return mDayFragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return mDayFragments[position].mDay;
    }

}
