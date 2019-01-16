package com.saint_gab.sacconnecte;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.icu.text.TimeZoneFormat;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class NewSubjectDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NewEquipmentDialogListener {
        void onDialogPositiveClick(Subject newSubject);
    }

    Subject mSubjectToEdit;//Est nul en cas de création de subject
    boolean editing;//Précise si on modifie ou si on créé un subject

    //Use this instance of the interface to deliver action events
    NewEquipmentDialogListener mListener;

    View mView;
    EditText mName;
    EditText mColor;
    ListView mListView;
    Timetable mTimetable;
    public NewSubjectDialogFragment() {}

    @SuppressLint("ValidFragment")
    public NewSubjectDialogFragment(Object parent, Subject subjectToEdit, Timetable timetable)
    {
        mSubjectToEdit = subjectToEdit;
        editing = subjectToEdit != null;
        mTimetable = timetable;

        //Verify that the host activity implements the callback interface
        try {
            //Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NewEquipmentDialogListener) parent;
        } catch (ClassCastException e) {
            //The activity doesn't implement the interface, throw exeption
            throw new ClassCastException(getActivity().toString() + " must implement NewSubjectDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_fragment_new_subject, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(editing ? "Modification" : "Nouvelle matière");
        builder.setView(mView);
        builder.setPositiveButton( editing ? "Modifier" : "Ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (verifyContent())
                {
                    if (editing) editSubject();
                    else createSubject();    
                }
                else Toast.makeText(getContext(), "Impossible de créer la matière", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Annuler", null);

        mName = mView.findViewById(R.id.dialog_fragment_subject_name);
        mColor = mView.findViewById(R.id.dialog_fragment_subject_color);

        configureListView();

        if (editing) importSubjectToEdit();

        return builder.create();
    }

    private boolean verifyValues()
    {
        //On vérifie que les valeurs entré par l'utilisateur sont correct
        return true;
    }

    private void configureListView()
    {
        mListView = mView.findViewById(R.id.dialog_fragment_subject_listView);
        String[] equipmentNames = mTimetable.getEquipmentNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, equipmentNames);
        mListView.setAdapter(adapter);
    }

    private void importSubjectToEdit()
    {
        mName.setText(mSubjectToEdit.getName());
        mColor.setText(mSubjectToEdit.getColor());
        ArrayList<Equipment> subjectEquipments = mSubjectToEdit.getEquipments();
        for (int i=0; i<subjectEquipments.size(); i++)
        {

        }
    }

    private void editSubject()
    {
        mSubjectToEdit.setName(mName.getText().toString());
        mSubjectToEdit.setColor(mColor.getText().toString());

        ArrayList<Equipment> equipments = new ArrayList<>();
        SparseBooleanArray sparseBooleanArray = mListView.getCheckedItemPositions();
        for (int i=0; i<mListView.getCheckedItemCount(); i++)
        {
            if (sparseBooleanArray.get(i)) equipments.add(mTimetable.getEquipment(i));
        }
        mSubjectToEdit.setEquipments(equipments);

        mListener.onDialogPositiveClick(null);
    }

    private void createSubject()
    {
        String name = mName.getText().toString();
        String color = mColor.getText().toString();

        ArrayList<Equipment> equipments = new ArrayList<>();
        SparseBooleanArray sparseBooleanArray = mListView.getCheckedItemPositions();
        for (int i=0; i<mListView.getCheckedItemCount(); i++)
        {
            if (sparseBooleanArray.get(i)) equipments.add(mTimetable.getEquipment(i));
        }

        Subject newSubject = new Subject(name, color, equipments, mTimetable);
        mListener.onDialogPositiveClick(newSubject);
    }

    private boolean verifyContent()
    {
        boolean result;
        result = (
                !mName.getText().toString().isEmpty()
                        && !mColor.getText().toString().isEmpty());

        return result;
    }
}
