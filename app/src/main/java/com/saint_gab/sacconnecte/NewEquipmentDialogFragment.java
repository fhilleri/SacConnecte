package com.saint_gab.sacconnecte;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

    Equipment mEquipmentToEdit;//Est nul en cas de création d'equipment
    boolean editing;//Précise si on modifie ou si on créé un subject

    //Use this instance of the interface to deliver action events
    NewEquipmentDialogFragment.NewEquipmentDialogListener mListener;

    View mView;
    EditText mName;
    EditText mId;
    Timetable mTimetable;
    public NewEquipmentDialogFragment() {}

    @SuppressLint("ValidFragment")
    public NewEquipmentDialogFragment(Object parent, Equipment equipmentToEdit, Timetable timetable)
    {
        mEquipmentToEdit = equipmentToEdit;
        editing = equipmentToEdit != null;
        mTimetable = timetable;

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

        builder.setTitle(editing ? "Modification" : "Nouveau materiel");
        builder.setView(mView);
        builder.setPositiveButton( editing ? "Modifier" : "Ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (verifyContent())
                {
                    if (editing) editEquipment();
                    else createEquipment();
                }
                else Toast.makeText(getContext(), "Impossible de créer le materiel", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Annuler", null);

        mName = mView.findViewById(R.id.dialog_fragment_equipment_name);
        mId = mView.findViewById(R.id.dialog_fragment_equipment_color);

        if (editing) importEquipmentToEdit();

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

    private boolean verifyContent()
    {
        boolean result;
        result = (
                !mName.getText().toString().isEmpty()
                        && !mId.getText().toString().isEmpty());

        return result;
    }
}
