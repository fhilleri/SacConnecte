package com.saint_gab.sacconnecte;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class SettingsFragment extends Fragment {

    int currentLanguageId = -1;
    MainActivity mParent;
    Timetable mTimetable;
    View mView;
    Spinner mSpinner;

    boolean configured = false;

    public static SettingsFragment newInstance(Timetable timetable, MainActivity parent) {
        return (new SettingsFragment(timetable, parent));
    }

    @SuppressLint("ValidFragment")
    public SettingsFragment(Timetable timetable, MainActivity parent) {
        mTimetable = timetable;
        mParent = parent;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button importButton = mView.findViewById(R.id.fragment_settings_import_button);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimetable.importIcalFile();
            }
        });

        configureSpinner();

        return mView;
    }

    private void configureSpinner() {
        mSpinner = (Spinner) mView.findViewById(R.id.fragment_settings_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("MyPref", 0);
        String languageCode = pref.getString("language", "fr");
        configured = false;
        switch(languageCode)
        {
            case "fr":
                mSpinner.setSelection(0);
                break;
            case "en":
                mSpinner.setSelection(1);
                break;
            default:
                mSpinner.setSelection(1);
                break;
        }

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String languageCode = languageIdToLanguageCode(position);

                LanguageHelper.changeLocale(getResources(), languageCode);
                SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("language", languageCode);
                editor.commit();
                Log.i("Language", "language set to : " + languageCode);
                if (configured)
                {
                    mParent.reloadSettingFragment();
                    Toast.makeText(getContext(), getString(R.string.toast_relaunch_the_app), Toast.LENGTH_LONG).show();
                }
                configured = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String languageIdToLanguageCode(int languageId)
    {
        switch (languageId) {
            case 0:
                return "fr";
            case 1:
                return "en";
            default:
                return "fr";
        }
    }
}
