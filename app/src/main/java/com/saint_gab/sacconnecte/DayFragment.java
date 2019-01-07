package com.saint_gab.sacconnecte;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DayFragment extends Fragment {

    //ListView
    ArrayList<Lesson> lessons;
    ListView listView;
    private static LessonAdapter adapter;

    private TimetableFragment mParent;
    private Timetable mTimetable;
    private int mIndex;//index of this DayFragment in the DayPageAdapter list
    public String mDay;

    private View mView;

    public static DayFragment newInstance(TimetableFragment parent, Timetable timetable, int index, String day)
    {
        return(new DayFragment(parent, timetable, index, day));
    }

    @SuppressLint("ValidFragment")
    public DayFragment(TimetableFragment parent, Timetable timetable, int index, String day) {
        mParent = parent;
        mTimetable = timetable;
        mIndex = index;
        mDay = day;
    }

    public DayFragment() {
        // Required empty public constructor
        mDay = "Unnamed";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_day, container, false);

        listView = (ListView)mView.findViewById(R.id.list_view);

        lessons = mTimetable.getLessons(mIndex);


        adapter = new LessonAdapter(lessons, getContext());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("DayFragment", "onItemClick: i = " + i);
                switch (mParent.getMode())
                {
                    case 1:
                        editlesson(i);
                        break;
                    case 2:
                        delete(i);
                        break;
                }
            }
        });

        return mView;
    }

    private void editlesson(int index)
    {
        DialogFragment newLessonDialogFragment = new NewLessonDialogFragment(mParent, mTimetable, lessons.get(index));
        newLessonDialogFragment.show(getFragmentManager(), "newSubject");
    }

    public void delete(int index)
    {
        mTimetable.deleteLesson(mIndex, index);
        adapter = new LessonAdapter(lessons, getContext());
        listView.setAdapter(adapter);
    }

}
