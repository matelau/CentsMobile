package com.matelau.junior.centsproject.Controllers;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.matelau.junior.centsproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WizardDialogFragment extends DialogFragment {
    private String LOG_TAG = WizardDialogFragment.class.getSimpleName();
    LinearLayout _rootLayout;
    TextView _question;
    ListView _answers;
    ArrayAdapter<String> _answersAdapter;
    String[] _college_answers = new String[] {"I'm not sure where I want to study", "I'm not sure what I want to study","I'm not sure about either"};
    String[] _cost_answers = new String[] {"I'm curious about the cost of living in a city", "I'm curious about my spending breakdown"};


    public WizardDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_wizard_dialog, null, false);
        //get question text
        _question = (TextView) _rootLayout.findViewById(R.id.wizard_question);
        _question.setText("What Best Describes Your Situation?");
        //get question list
        String[] initial_answers = new String[]{"Starting or Returning to College", "Graduating College or Changing Jobs", "Considering Moving or Financial Spending", "Just Looking Around"};

        _answers = (ListView) _rootLayout.findViewById(R.id.wizard_options);
        _answers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectInitialItem(position);
            }
        });
        _answersAdapter = new ArrayAdapter<String>(getActivity(), R.layout.wizard_item, initial_answers);
        _answers.setAdapter(_answersAdapter);

        builder.setView(_rootLayout);

        return builder.create();
    }

    private void selectInitialItem(int pos){
        Log.d(LOG_TAG, "Initial Item Selected: " + pos);
        switch(pos) {
            case 0:
                //College or Major inquiry
                _answersAdapter = new ArrayAdapter<String>(getActivity(), R.layout.wizard_item, _college_answers);
                _answers.setAdapter(_answersAdapter);
                _answers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectCollegeOrMajor(position);
                    }
                });
                break;
            case 1:
                //Career inquiry
                //TODO launch career comparison fragment
                break;
            case 2:
                //Cost of Living or spending breakdown inquiry
                _answersAdapter = new ArrayAdapter<String>(getActivity(), R.layout.wizard_item, _cost_answers);
                _answers.setAdapter(_answersAdapter);
                _answers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectCost(position);
                    }
                });
                //TODO launch living inquiry
                break;
            default:
                //Examples?
                break;

        }
    }


    private void selectCollegeOrMajor(int pos){
        Log.d(LOG_TAG, "College Item Selected: " + pos);
        switch(pos){
            case 0:
                //College Inquiry
                break;
            case 1:
                //Major Inquiry
                break;
            default:
                //still unsure
                break;
        }
    }

    private void selectCost(int pos){
        Log.d(LOG_TAG, "Cost Item Selected: " + pos);
        switch(pos) {
            case 0:
                //Cost of Living
                break;
            case 1:
                //Spending
                break;
            default:
                break;
        }

    }

}
