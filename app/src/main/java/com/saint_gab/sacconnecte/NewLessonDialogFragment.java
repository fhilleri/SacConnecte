package com.saint_gab.sacconnecte;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class NewLessonDialogFragment extends DialogFragment {

    NewLessonDialogListener mListener;

    private View mView;
    private Lesson mLessonToEdit;
    private Timetable mTimetable;

    private Spinner mSpinner;
    private TimePicker mStartTime;
    private TimePicker mEndTime;

    private boolean editing;

    //Codes d'erreurs
    static final int CORRECT = 0;
    static final int WRONG_TIMES_ORDER = 1;


    public interface NewLessonDialogListener {
        void onDialogPositiveClick(Lesson newLesson);
    }

    public NewLessonDialogFragment(Object parent, Timetable timetable, Lesson lessonToEdit)
    {
        mListener = (NewLessonDialogListener) parent;
        mTimetable = timetable;
        mLessonToEdit = lessonToEdit;
        editing = mLessonToEdit != null;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_fragment_new_lesson, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(editing ? getString(R.string.dialog_fragment_new_lesson_title_edition) : getString(R.string.dialog_fragment_new_lesson_title_new));
        builder.setView(mView);
        builder.setPositiveButton( editing ? getString(R.string.button_edit) : getString(R.string.button_add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int errorCode = verifyContent();
                if (errorCode == 0)//On empêche l'utilisateur de créer un cours avec des valeurs vides
                {
                    if (editing) editLesson();
                    else createLesson();    
                } else  {
                    displayErrors(errorCode);
                }
            }
        });
        builder.setNegativeButton(getString(R.string.button_cancel), null);

        configureSpinner();
        configureTimePickers();

        if (editing) importLesson();

        return builder.create();
    }

    private void configureTimePickers()
    {
        mStartTime = mView.findViewById(R.id.dialog_fragment_new_lesson_time_start);
        mEndTime = mView.findViewById(R.id.dialog_fragment_new_lesson_time_end);

        mStartTime.setIs24HourView(true);
        mEndTime.setIs24HourView(true);
        mEndTime.setHour((mEndTime.getHour() + 1 ) % 24);
    }

    private void importLesson()
    {
        int index = mTimetable.getSubjectIndex(mLessonToEdit.getSubject());
        mSpinner.setSelection(index);
        mStartTime.setHour(mLessonToEdit.getStartTime().getHour());
        mStartTime.setMinute(mLessonToEdit.getStartTime().getMinute());
        mEndTime.setHour(mLessonToEdit.getEndTime().getHour());
        mEndTime.setMinute(mLessonToEdit.getEndTime().getMinute());
    }

    private void configureSpinner()
    {
        mSpinner = mView.findViewById(R.id.dialog_fragment_new_lesson_spinner);
        String[] subjectNames = mTimetable.getSubjectNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, subjectNames);
        mSpinner.setAdapter(adapter);
    }

    private void editLesson()
    {
        Subject subject = mTimetable.getSubject(mSpinner.getSelectedItemPosition());
        mLessonToEdit.setSubject(subject);
        mLessonToEdit.setStartTime(new Time(mStartTime.getHour(), mStartTime.getMinute()));
        mLessonToEdit.setEndTime(new Time(mEndTime.getHour(), mEndTime.getMinute()));
        mListener.onDialogPositiveClick(null);
    }

    private void createLesson()
    {
        Subject subject = mTimetable.getSubject(mSpinner.getSelectedItemPosition());
        Time startTime = new Time(mStartTime.getHour(), mStartTime.getMinute());
        Time endTime = new Time(mEndTime.getHour(), mEndTime.getMinute());
        Lesson newLesson = new Lesson(subject, startTime, endTime, mTimetable);
        mListener.onDialogPositiveClick(newLesson);
    }

    private void displayErrors(int errorValue)
    {
        switch(errorValue)
        {
            case WRONG_TIMES_ORDER:
                Toast.makeText(getContext(), getString(R.string.dialog_fragment_new_lesson_error_wrong_times_order), Toast.LENGTH_LONG).show();
                break;
        }
    }

    private int verifyContent()
    {
        int result = CORRECT;

        Time startTime = new Time(mStartTime.getHour(), mStartTime.getMinute());
        Time endTime = new Time(mEndTime.getHour(), mEndTime.getMinute());

        if (startTime.isAfter(endTime)) result = WRONG_TIMES_ORDER;

        return result;
    }

}
