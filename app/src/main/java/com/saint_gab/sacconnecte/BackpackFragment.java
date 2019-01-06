package com.saint_gab.sacconnecte;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class BackpackFragment extends Fragment {

    private Button boutonTest;

    public static BackpackFragment newInstance() {

        return (new BackpackFragment());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_backpack, container, false);
        boutonTest = (Button) view.findViewById(R.id.bouton_test);
        boutonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Bouton test !!!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
