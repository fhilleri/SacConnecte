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
@SuppressLint("ValidFragment")
public class EquipmentFragment extends Fragment implements NewEquipmentDialogFragment.NewEquipmentDialogListener {

    private View mView;
    private Timetable mTimetable;

    private boolean onDeleteMode = false;//Si oui, l'appuie sur une matière la supprimera
    private boolean onEditMode = false;//Si oui, l'appuie sur une matière la modifiera

    private Button newSubjectButton;
    private Button editSubjectButton;
    private Button deleteSubjectButton;

    public static EquipmentFragment newInstance(Timetable timetable) {

        return (new EquipmentFragment(timetable));
    }

    public EquipmentFragment() {}

    public EquipmentFragment(Timetable timetable) {
        mTimetable = timetable;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_equipment, container, false);
        configureListView();
        configureButtons();
        return mView;
    }


    private void configureButtons()
    {
        newSubjectButton = mView.findViewById(R.id.fragment_equipment_button_new);
        newSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newSubject();
            }
        });

        deleteSubjectButton = mView.findViewById(R.id.fragment_equipment_button_delete);
        deleteSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteMode = !onDeleteMode;
                onEditMode = false;
                refreshButtonColor();
            }
        });

        editSubjectButton = mView.findViewById(R.id.fragment_equipment_button_edit);
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
        ListView listView = (ListView)mView.findViewById(R.id.fragment_equipment_list_view);
        //Set Adapter DayPageAdapter and glue it together
        listView.setAdapter(new EquipmentAdapter(mTimetable, getContext()));

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
        DialogFragment newEquipmentDialogFragment = new NewEquipmentDialogFragment(this, null, mTimetable);
        newEquipmentDialogFragment.show(getFragmentManager(), "newSubject");
    }

    private void editSubject(int index)
    {
        DialogFragment newEquipmentDialogFragment = new NewEquipmentDialogFragment(this, mTimetable.getEquipment(index), mTimetable);
        newEquipmentDialogFragment.show(getFragmentManager(), "editSubject");
    }

    //Implemente l'interface de communication avec la boîte de dialogue pour récupérer le nouveau Equipment
    @Override
    public void onDialogPositiveClick(Equipment newEquipment) {
        if (newEquipment != null) mTimetable.addEquipment(newEquipment);
        configureListView();

        onEditMode = false;
        refreshButtonColor();
    }

}
