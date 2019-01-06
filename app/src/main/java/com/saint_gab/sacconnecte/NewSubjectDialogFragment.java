package com.saint_gab.sacconnecte;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class NewSubjectDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NewSubjectDialogListener {
        void onDialogPositiveClick(Subject newSubject);
    }

    Subject mSubjectToEdit;//Est nul en cas de création de subject
    boolean editing;//Précise si on modifie ou si on créé un subject

    //Use this instance of the interface to deliver action events
    NewSubjectDialogListener mListener;

    View mView;
    EditText mName;
    EditText mColor;
    public NewSubjectDialogFragment() {}

    @SuppressLint("ValidFragment")
    public NewSubjectDialogFragment(Object parent, Subject subjectToEdit)
    {
        mSubjectToEdit = subjectToEdit;
        editing = subjectToEdit != null;

        //Verify that the host activity implements the callback interface
        try {
            //Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NewSubjectDialogListener) parent;
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
                if (editing) editSubject();
                else createSubject();
            }
        });
        builder.setNegativeButton("Annuler", null);

        mName = mView.findViewById(R.id.dialog_fragment_subject_name);
        mColor = mView.findViewById(R.id.dialog_fragment_subject_color);

        if (editing) importSubjectToEdit();

        return builder.create();
    }

    private void importSubjectToEdit()
    {
        mName.setText(mSubjectToEdit.getName());
        mColor.setText(mSubjectToEdit.getColor());
    }

    private void editSubject()
    {
        mSubjectToEdit.setName(mName.getText().toString());
        mSubjectToEdit.setColor(mColor.getText().toString());
    }

    private void createSubject()
    {
        String name = mName.getText().toString();
        String color = mColor.getText().toString();
        Subject newSubject = new Subject(name, color);
        mListener.onDialogPositiveClick(newSubject);
    }
}
