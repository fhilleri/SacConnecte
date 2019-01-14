package com.saint_gab.sacconnecte;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectFragment extends Fragment implements NewSubjectDialogFragment.NewSubjectDialogListener {

    private View mView;
    private Timetable mTimetable;

    private boolean onDeleteMode = false;//Si oui, l'appuie sur une matière la supprimera
    private boolean onEditMode = false;//Si oui, l'appuie sur une matière la modifiera

    private Button newSubjectButton;
    private Button editSubjectButton;
    private Button deleteSubjectButton;

    public static SubjectFragment newInstance(Timetable timetable) {
        return (new SubjectFragment(timetable));
    }

    public SubjectFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public SubjectFragment(Timetable timetable) {
        mTimetable = timetable;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_subject, container, false);
        configureListView();
        configureButtons();
        return mView;
    }


    private void configureButtons()
    {
        newSubjectButton = mView.findViewById(R.id.fragment_subject_button_new);
        newSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newSubject();
            }
        });

        deleteSubjectButton = mView.findViewById(R.id.fragment_subject_button_delete);
        deleteSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteMode = !onDeleteMode;
                onEditMode = false;
                refreshButtonColor();
            }
        });

        editSubjectButton = mView.findViewById(R.id.fragment_subject_button_edit);
        editSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditMode = !onEditMode;
                onDeleteMode = false;
                refreshButtonColor();
            }
        });
    }

    private void refreshButtonColor()
    {
        deleteSubjectButton.setTextColor(onDeleteMode ? Color.RED : Color.BLACK);
        editSubjectButton.setTextColor(onEditMode ? Color.BLUE : Color.BLACK);
    }

    private void configureListView()
    {
        //Get ViewPager from layout
        ListView listView = (ListView)mView.findViewById(R.id.fragment_subject_list_view);
        //Set Adapter DayPageAdapter and glue it together
        listView.setAdapter(new SubjectAdapter(mTimetable, getContext()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (onDeleteMode) mTimetable.deleteSubject(i);
                if (onEditMode) editSubject(i);
                configureListView();
            }
        });
    }

    private void newSubject()
    {
        DialogFragment newSubjectDialogFragment = new NewSubjectDialogFragment(this, null, mTimetable);
        newSubjectDialogFragment.show(getFragmentManager(), "newSubject");
    }

    private void editSubject(int index)
    {
        DialogFragment newSubjectDialogFragment = new NewSubjectDialogFragment(this, mTimetable.getSubject(index), mTimetable);
        newSubjectDialogFragment.show(getFragmentManager(), "editSubject");
    }

    //Implemente l'interface de communication avec la boîte de dialogue pour récupérer le nouveau Subject
    @Override
    public void onDialogPositiveClick(Subject newSubject) {
        if (newSubject != null) mTimetable.addSubject(newSubject);
        configureListView();

        onEditMode = false;
        refreshButtonColor();
    }
}
