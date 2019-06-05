package com.saint_gab.sacconnecte;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class NewEquipmentDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NewEquipmentDialogListener {
        void onDialogPositiveClick(Equipment newEquipment);
    }

    //Codes d'erreurs
    static final int CORRECT = 0;
    static final int EMPTY_EDIT_TEXT = 1;
    static final int ILLEGAL_ID_CHARACTERS = 2;
    static final int WRONG_ID_CHARACTER_NUMBER = 3;
    static final int ALREADY_EXIST = 4;

    Equipment mEquipmentToEdit;//Est nul en cas de création d'equipment
    boolean editing;//Précise si on modifie ou si on créé un subject

    //Use this instance of the interface to deliver action events
    NewEquipmentDialogFragment.NewEquipmentDialogListener mListener;

    String mUnknowId;

    View mView;
    EditText mName;
    EditText mId;
    Timetable mTimetable;
    public NewEquipmentDialogFragment() {}

    @SuppressLint("ValidFragment")
    public NewEquipmentDialogFragment(Object parent, Equipment equipmentToEdit, Timetable timetable, String id)
    {
        mEquipmentToEdit = equipmentToEdit;
        editing = equipmentToEdit != null;
        mTimetable = timetable;
        mUnknowId = id;

        //Verify that the host activity implements the callback interface
        try {
            //Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NewEquipmentDialogFragment.NewEquipmentDialogListener) parent;
        } catch (ClassCastException e) {
            //The activity doesn't implement the interface, throw exeption
            throw new ClassCastException(getActivity().toString() + " must implement NewEquipmentDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_fragment_new_equipment, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(editing ? getString(R.string.dialog_framgent_new_equipment_title_edition) : getString(R.string.dialog_framgent_new_equipment_title_new));
        builder.setView(mView);
        builder.setPositiveButton( editing ? getString(R.string.button_edit) : getString(R.string.button_add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int errorCode = verifyContent();
                if (errorCode == CORRECT)
                {
                    if (editing) editEquipment();
                    else createEquipment();
                }
                else displayErrors(errorCode);
            }
        });
        builder.setNegativeButton(getString(R.string.button_cancel), null);

        mName = mView.findViewById(R.id.dialog_fragment_equipment_name);
        mId = mView.findViewById(R.id.dialog_fragment_equipment_color);

        if (editing) importEquipmentToEdit();
        if (mUnknowId != null) mId.setText(mUnknowId);

        return builder.create();
    }

    private boolean verifyValues()
    {
        //On vérifie que les valeurs entré par l'utilisateur sont correct
        return true;
    }

    private void importEquipmentToEdit()
    {
        mName.setText(mEquipmentToEdit.getName());
        mId.setText(mEquipmentToEdit.getId());
    }

    private void editEquipment()
    {
        mEquipmentToEdit.setName(mName.getText().toString());
        mEquipmentToEdit.setId(mId.getText().toString());
        mListener.onDialogPositiveClick(null);
    }

    private void createEquipment()
    {
        String name = mName.getText().toString();
        String id = mId.getText().toString();
        Equipment newEquipment = new Equipment(name, id);
        mListener.onDialogPositiveClick(newEquipment);
    }
    
    private void displayErrors(int errorValue)
    {
        switch(errorValue)
        {
            case EMPTY_EDIT_TEXT:
                Toast.makeText(getContext(), getString(R.string.dialog_fragment_new_equipment_error_empty_edit_text), Toast.LENGTH_LONG).show();
                break;
            case ILLEGAL_ID_CHARACTERS:
                Toast.makeText(getContext(), getString(R.string.dialog_fragment_new_equipment_error_illegal_id_characters), Toast.LENGTH_LONG).show();
                break;
            case WRONG_ID_CHARACTER_NUMBER:
                Toast.makeText(getContext(), getString(R.string.dialog_fragment_new_equipment_error_wrong_id_character_number), Toast.LENGTH_LONG).show();
                break;
            case ALREADY_EXIST:
                Toast.makeText(getContext(), getString(R.string.dialog_fragment_new_equipment_error_already_exist), Toast.LENGTH_LONG).show();
                break;
        }
    }

    private int verifyContent()
    {
        int result = CORRECT;
        if (mName.getText().toString().isEmpty()
            || mId.getText().toString().isEmpty())
        {
            result = EMPTY_EDIT_TEXT;
        }
        else if(mId.getText().toString().matches("[^0123456789ABCDEF]")) result = ILLEGAL_ID_CHARACTERS;
        else if(mId.getText().toString().length() != 12) result = WRONG_ID_CHARACTER_NUMBER;
        else if(mTimetable.equipmentExist(mId.getText().toString())) result = ALREADY_EXIST;

        mId.setText(mId.getText().toString().toUpperCase());

        return result;
    }
}
