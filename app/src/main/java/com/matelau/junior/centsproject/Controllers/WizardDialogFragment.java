package com.matelau.junior.centsproject.Controllers;


import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.matelau.junior.centsproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WizardDialogFragment extends DialogFragment {
    LinearLayout _rootLayout;
    TextView _question;
    ListView _answers;
    ArrayAdapter<String> _answersAdapter;


    public WizardDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_wizard_dialog, container, false);
        //get question text
        _question = (TextView) _rootLayout.findViewById(R.id.wizard_question);
        _question.setText("What Best Describes Your Situation?");
        //get question list
        String[] initial_answers = new String[]{"Starting or Returning to College", "Graduating College or Changing Jobs", "Moving to a New City", "Just Looking Around"};

        _answers = (ListView) _rootLayout.findViewById(R.id.wizard_options);
        _answersAdapter = new ArrayAdapter<String>(getActivity(), R.layout.wizard_item, initial_answers);
        _answers.setAdapter(_answersAdapter);





        return _rootLayout;
    }


}
