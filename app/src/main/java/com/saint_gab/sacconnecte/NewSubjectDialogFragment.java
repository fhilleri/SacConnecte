package com.saint_gab.sacconnecte;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.text.TimeZoneFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerPopup;
import top.defaults.colorpicker.ColorWheelView;

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

    //Codes d'erreurs
    static final int CORRECT = 0;
    static final int EMPTY_EDIT_TEXT = 1;
    static final int WRONG_COLOR = 2;
    static final int ALREADY_EXIST = 3;


    View mView;
    ColorWheelView mColorWheelView;
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

        builder.setTitle(editing ? getString(R.string.dialog_fragment_new_subject_title_edition) : getString(R.string.dialog_fragment_new_subject_title_new));
        builder.setView(mView);
        builder.setPositiveButton( editing ? getString(R.string.button_edit) : getString(R.string.button_add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int errorCode = verifyContent();
                if (errorCode == CORRECT)
                {
                    if (editing) editSubject();
                    else createSubject();    
                }
                else displayErrors(errorCode);
            }
        });
        builder.setNegativeButton(getString(R.string.button_cancel), null);

        mName = mView.findViewById(R.id.dialog_fragment_subject_name);
        mColor = mView.findViewById(R.id.dialog_fragment_subject_color);

        configureListView();




        final View colorView = (View) mView.findViewById(R.id.dialog_fragment_subject_color_view);

        mColorWheelView = mView.findViewById(R.id.dialog_fragment_subject_colorPicker);

        if (editing) importSubjectToEdit();

        mColorWheelView.subscribe(new ColorObserver() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onColor(int color, boolean fromUser, boolean shouldPropagate) {
                colorView.setBackgroundColor(color);

                //On cherche le code couleur héxadécimal de la couleur
                String red = Integer.toString(Color.red(color), 16);
                String green = Integer.toString(Color.green(color), 16);
                String blue = Integer.toString(Color.blue(color), 16);
                //On s'assure que le code couleur soit bien composé de 6 caractères
                red = (red.length() == 2 ? red : "0" + red);
                green = (green.length() == 2 ? green : "0" + green);
                blue = (blue.length() == 2 ? blue : "0" + blue);

                mColor.setText(("#" + red + green + blue).toUpperCase());
            }
        });

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

    //Importe les différents attributs de l'équipment dans la boîte de dialogue
    private void importSubjectToEdit()
    {
        mName.setText(mSubjectToEdit.getName());
        mColor.setText(mSubjectToEdit.getColor());
        mColorWheelView.setColor(Color.parseColor(mSubjectToEdit.getColor()), false);
        ArrayList<Equipment> subjectEquipments = mSubjectToEdit.getEquipments();
        if (subjectEquipments != null)
        {
            for (int i=0; i<subjectEquipments.size(); i++)
            {
                int index = mTimetable.getIndexOfEquipment(subjectEquipments.get(i));
                mListView.setItemChecked(index, true);
            }
        }
    }

    private void editSubject()
    {
        mSubjectToEdit.setName(mName.getText().toString());
        mSubjectToEdit.setColor(mColor.getText().toString());

        ArrayList<Equipment> equipments = getSelectedEquipments();
        mSubjectToEdit.setEquipments(equipments);

        mListener.onDialogPositiveClick(null);
    }

    private void createSubject()
    {
        String name = mName.getText().toString();
        String color = mColor.getText().toString();

        ArrayList<Equipment> equipments = getSelectedEquipments();

        Subject newSubject = new Subject(name, color, equipments, mTimetable);
        mListener.onDialogPositiveClick(newSubject);
    }

    private ArrayList<Equipment> getSelectedEquipments()
    {
        ArrayList<Equipment> equipments = new ArrayList<>();
        for (int i=0; i<mListView.getCount(); i++)
        {
            if (mListView.isItemChecked(i)) equipments.add(mTimetable.getEquipment(i));
        }
        return equipments;
    }

    /*private boolean verifyContent()
    {
        boolean result;
        result = (
                !mName.getText().toString().isEmpty()
                        && !mColor.getText().toString().isEmpty());

        return result;
    }*/
    private void displayErrors(int errorValue)
    {
        switch(errorValue)
        {
            case EMPTY_EDIT_TEXT:
                Toast.makeText(getContext(), getString(R.string.dialog_fragment_new_subject_error_empty_edit_text), Toast.LENGTH_LONG).show();
                break;
            case WRONG_COLOR:
                Toast.makeText(getContext(), getString(R.string.dialog_fragment_new_subject_error_wrong_color), Toast.LENGTH_LONG).show();
                break;
            case ALREADY_EXIST:
                Toast.makeText(getContext(), getString(R.string.dialog_fragment_new_subject_error_already_exist), Toast.LENGTH_LONG).show();
                break;
        }
    }

    private int verifyContent()
    {
        int result = CORRECT;
        if (mName.getText().toString().isEmpty()
            || mColor.getText().toString().isEmpty()) result = EMPTY_EDIT_TEXT;
        else if(mColor.getText().toString().length() != 7) result = WRONG_COLOR;
        else if(mTimetable.subjectExist(mName.getText().toString()) && !editing) result = ALREADY_EXIST;


        return result;
    }
}
