package com.matelau.junior.centsproject.Views.VisualizationFragments.Career;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Models.CentsAPIModels.RecordQuery;
import com.matelau.junior.centsproject.Models.CentsAPIModels.RecordsService;
import com.matelau.junior.centsproject.Models.VizModels.Career;
import com.matelau.junior.centsproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CareerSelectionDialogFragment extends DialogFragment{
    private String LOG_TAG = CareerSelectionDialogFragment.class.getSimpleName();

    private LinearLayout _rootLayout;
    private ArrayAdapter<String> _careerAdapter;
    private Spinner _careerSpinner1;
    private Spinner _careerSpinner2;
    private TextView _careerTextView1;
    private TextView _careerTextView2;
//    private TextView __careerTextView2;
    private TextView _vs;
    private View _plusBtn;
    private Button _submit;
    private Button _cancel;
    private boolean isPlus = false;
    private TextView _instructions;

    private String[] _careers;
    private Career _career1;
    private Career _career2;


    public CareerSelectionDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateDialog");

        //clear old values
        if(!CentsApplication.get_selectedVis().equals("Major Comparison")){
            CentsApplication.set_selectedVis(null);
            CentsApplication.set_major1(null);
            CentsApplication.set_major2(null);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_selection_dialog, null, false);
        TextView instructions = (TextView) _rootLayout.findViewById(R.id.selection_instructions);
        instructions.setText("Select a state to view Universities");
        _submit = (Button) _rootLayout.findViewById(R.id.submit_select);
        _cancel = (Button) _rootLayout.findViewById(R.id.cancel_select);
        _vs = (TextView) _rootLayout.findViewById(R.id.vs);
        _instructions = (TextView) _rootLayout.findViewById(R.id.selection_instructions);
        _instructions.setText("Select one or two Majors");
        _careerTextView1 = (TextView) _rootLayout.findViewById(R.id.stateTextView);
        _careerTextView1.setText("Career - 1");
        _careerTextView2 = (TextView) _rootLayout.findViewById(R.id.stateTextView2);
        _plusBtn = _rootLayout.findViewById(R.id.circle);
        _careerSpinner1 = (Spinner) _rootLayout.findViewById(R.id.state_spinner1);
        _careerSpinner2 = (Spinner) _rootLayout.findViewById(R.id.state_spinner2);

        loadCareersList();

        _plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //flip button
                if(isPlus) {
                    addPlusViews();
                }
                else{
                    isPlus = true;
                    _plusBtn.setBackground(getResources().getDrawable(R.drawable.ic_action_new));
                    _vs.setVisibility(View.GONE);
                    _vs.setVisibility(View.GONE);
                    _careerTextView2.setVisibility(View.GONE);
                    _careerSpinner2.setVisibility(View.GONE);

                }
            }
        });

        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Careers Coming Soon.", Toast.LENGTH_SHORT).show();
                if(_career1 != null || _career2 !=null ){
//                    //create query
//                    MajorQuery mQuery = new MajorQuery();
//                    mQuery.setOperation("compare");
//                    List<Career> careers = new ArrayList<Career>();
//                    careers.add((_career1);
////                    CentsApplication.set_major1((_career1);
//                    if(_career2 != null){
//                        careers.add(_career2);
////                        CentsApplication.set_major2(_career2);
//                    }
//                    mQuery.setMajors(careers);
//                    MajorService service = CentsApplication.get_centsRestAdapter().create(CareerSe.class);
//                    service.getMajorInfo(mQuery, new Callback<MajorResponse>() {
//                        @Override
//                        public void success(MajorResponse majorResponse, Response response) {
//                            //Set MajorResponse
//                            CentsApplication.set_mResponse(majorResponse);
//                            //launch summary vis
//                            Log.d(LOG_TAG, "success");
//                            CentsApplication.set_selectedVis("Major Comparison");
//                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                            ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
//                            ft.addToBackStack("major-intro");
//                            ft.commit();
//                            dismiss();
//                        }
//
//                        @Override
//                        public void failure(RetrofitError error) {
//                            Toast.makeText(getActivity(), "Error - please try again", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
                }
                else{
                    Toast.makeText(getActivity(), "You Must Make a Selection", Toast.LENGTH_SHORT).show();

                }
            }
        });

        _cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "cancel");
                dismiss();
            }
        });

        builder.setView(_rootLayout);
        return builder.create();
    }

    private void loadCareersList(){
        if (CentsApplication.get_majors() != null){
            _careers = CentsApplication.get_majors();
//            loadPreviousSearch();
        }
        else{
            RecordsService service = CentsApplication.get_centsRestAdapter().create(RecordsService.class);
            RecordQuery query = new RecordQuery();
            query.setOperation("get");
            ArrayList<String> tables = new ArrayList<String>();
            tables.add("careers");
            query.setTables(tables);
            service.getRecordsV2(new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    //get list
                    Log.d(LOG_TAG, "Received Majors List");
                    _careers = careersFromJson(response2);
                    //cache majors list
                    CentsApplication.set_careers(_careers);
                    initSpinner1();

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(LOG_TAG, error.getMessage());
                    Toast.makeText(getActivity(), "Error loading list please try again later", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private void addPlusViews(){
        isPlus = false;
        _plusBtn.setBackground(getResources().getDrawable(R.drawable.minus));
        //show secondary selections
        _vs.setVisibility(View.VISIBLE);
        _careerTextView2.setVisibility(View.VISIBLE);
        _careerSpinner2.setVisibility(View.VISIBLE);
        _careerTextView2.setText("Major - 2");
        _careerSpinner2.setAdapter(_careerAdapter);
        _careerSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String career = _careers[position];
                _career2 = new Career();
                _career2.setName(career);
                Log.d(LOG_TAG, "Selected Career2: " + career);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initSpinner1(){
        _careerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, _careers);
        _careerSpinner1.setAdapter(_careerAdapter);
        _careerSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String career = _careers[position];

                _career1 = new Career();
                _career1.setName(career);
//                _major1.setName(full_selection.substring(0,first_paren));
                //get lvl
//                String level  = full_selection.substring(first_paren+1, full_selection.length()-1);
//                _major1.setLevel(level);
                Log.d(LOG_TAG, "Selected career: "+ career);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String[] careersFromJson(Response response){
        Gson gson = new Gson();
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {

            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

            String line;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String rsp = sb.toString();
        String[] respArr = gson.fromJson(rsp, String[].class);

        return respArr;

    }
}
