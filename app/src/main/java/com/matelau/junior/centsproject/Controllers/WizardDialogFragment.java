package com.matelau.junior.centsproject.Controllers;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.matelau.junior.centsproject.R;
import com.matelau.junior.centsproject.Views.VisualizationFragments.Career.CareerIntroFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.College.CollegeIntroFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving.COLIntroFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.Major.MajorIntroFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.SpendingBreakdown.SpendingBreakdownIntroFragment;

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
    String[] _cost_answers = new String[] {"I'm curious about the cost of living in a city", "I'm curious about how I should be spending my money"};


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
        builder.setCancelable(true);

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
                //launch career comparison viz view pager
                switchToVisFrag("Career Comparison");

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
                break;
            default:
                //Examples
                //launch examples Dialog Fragment
                switchToVisFrag("Examples");
                break;

        }
    }

    /**
     * Switches the current fragment to viz fragment selected within wizard dialog
     * @param selected
     */
    private void switchToVisFrag(String selected){
        //close wizard
        dismiss();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        //Add Intros here as they are created
        getActivity().getActionBar().setTitle(selected);
        if(selected.equals("COL Comparison")){
            //Special Case title change
            getActivity().getActionBar().setTitle("Cost of Living Comparison");
            ft.replace(R.id.fragment_placeholder, new COLIntroFragment());
            ft.addToBackStack("wizard");
            ft.commit();
        }
        else if(selected.equals("College Comparison")){
            ft.replace(R.id.fragment_placeholder, new CollegeIntroFragment());
            ft.addToBackStack("wizard");
            ft.commit();
        }
        else if(selected.equals("Spending Breakdown")){
            ft.replace(R.id.fragment_placeholder, new SpendingBreakdownIntroFragment());
            ft.addToBackStack("wizard");
            ft.commit();
        }
        else if(selected.equals("Career Comparison")){
            ft.replace(R.id.fragment_placeholder, new CareerIntroFragment());
            ft.addToBackStack("wizard");
            ft.commit();
        }
        else if(selected.equals("Major Comparison")){
            ft.replace(R.id.fragment_placeholder, new MajorIntroFragment());
            ft.addToBackStack("wizard");
            ft.commit();
        }
        else{
            CentsApplication.set_selectedVis(selected);
            ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_placeholder,new VisualizationPagerFragment());
            ft.addToBackStack("wizard");
            ft.commit();

        }

    }

    /**
     * Switches view to College Comp or Major Comp
     * @param pos
     */
    private void selectCollegeOrMajor(int pos){
        Log.d(LOG_TAG, "College Item Selected: " + pos);
        switch(pos){
            case 0:
                //College Inquiry
                //Launch college viz view pager
                switchToVisFrag("College Comparison");
                break;
            case 1:
                //Major Inquiry
                //launch Major viz view pager
                switchToVisFrag("Major Comparison");
                break;
            default:
                //still unsure
                break;
        }
    }

    /**
     * Switches view to Spending Breakdown or Cost of Living
     * @param pos
     */
    private void selectCost(int pos){
        Log.d(LOG_TAG, "Cost Item Selected: " + pos);
        switch(pos) {
            case 0:
                //Cost of Living
                //launch cost living viz view pager
                switchToVisFrag("COL Comparison");
                break;
            case 1:
                //Spending
                //launch spending viz view pager
                switchToVisFrag("Spending Breakdown");
                break;
            default:
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "resumed");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "destroyed");
    }
}
