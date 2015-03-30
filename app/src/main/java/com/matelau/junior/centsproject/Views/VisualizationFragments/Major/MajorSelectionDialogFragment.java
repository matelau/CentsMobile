package com.matelau.junior.centsproject.Views.VisualizationFragments.Major;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
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
import com.matelau.junior.centsproject.Controllers.VisualizationPagerFragment;
import com.matelau.junior.centsproject.Models.CentsAPIModels.MajorService;
import com.matelau.junior.centsproject.Models.CentsAPIModels.RecordQuery;
import com.matelau.junior.centsproject.Models.CentsAPIModels.RecordsService;
import com.matelau.junior.centsproject.Models.VizModels.Major;
import com.matelau.junior.centsproject.Models.VizModels.MajorQuery;
import com.matelau.junior.centsproject.Models.VizModels.MajorResponse;
import com.matelau.junior.centsproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MajorSelectionDialogFragment extends DialogFragment{
    private String LOG_TAG = MajorSelectionDialogFragment.class.getSimpleName();
    private LinearLayout _rootLayout;
    private Button _submit;
    private Button _cancel;
    private View _plusBtn;
    private Spinner _majorSpinner1;
    private Spinner _majorSpinner2;
    private ArrayAdapter<String> _majorAdapter;


    private String[] _majors;


    private TextView _vs;
    private TextView _majorTextView1;
    private TextView _majorTextView2;
    private TextView _instructions;

    private boolean isPlus;
    private Major _major1;
    private Major _major2;


    public MajorSelectionDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateDialog");
        loadMajorsList();
        //if selected vis is a major comparison switch background fragment to search - to clear old viz
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
        _majorTextView1 = (TextView) _rootLayout.findViewById(R.id.stateTextView);
        _majorTextView1.setText("Major - 1");
        _majorTextView2 = (TextView) _rootLayout.findViewById(R.id.stateTextView2);
        _plusBtn = _rootLayout.findViewById(R.id.circle);
        _majorSpinner1 = (Spinner) _rootLayout.findViewById(R.id.state_spinner1);
        _majorSpinner2 = (Spinner) _rootLayout.findViewById(R.id.state_spinner2);

        _plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //flip button
                if(isPlus) {
                    isPlus = false;
                    _plusBtn.setBackground(getResources().getDrawable(R.drawable.minus));
                    //show secondary selections
                    _vs.setVisibility(View.VISIBLE);
                    _majorTextView2.setVisibility(View.VISIBLE);
                    _majorSpinner2.setVisibility(View.VISIBLE);
                    _majorTextView2.setText("Major - 2");
                    _majorSpinner2.setAdapter(_majorAdapter);
                    _majorSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String full_selection = _majors[position];
                            int first_paren = full_selection.indexOf('(');
                            _major2 = new Major();
                            _major2.setName(full_selection.substring(0,first_paren));
                            //get lvl
                            String level  = full_selection.substring(first_paren+1, full_selection.length()-1);
                            _major2.setLevel(level);
//                            _major2.setOrder(2);
                            Log.d(LOG_TAG, "Selected Major2: "+_major2);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
                else{
                    isPlus = true;
                    _plusBtn.setBackground(getResources().getDrawable(R.drawable.ic_action_new));
                    _vs.setVisibility(View.GONE);
                    _vs.setVisibility(View.GONE);
                    _majorTextView2.setVisibility(View.GONE);
                    _majorSpinner2.setVisibility(View.GONE);

                }
            }
        });

        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_major1 != null || _major2 !=null ){
                    //create query
                    MajorQuery mQuery = new MajorQuery();
                    mQuery.setOperation("compare");
                    List<Major> majors = new ArrayList<Major>();
                    majors.add(_major1);
                    CentsApplication.set_major1(_major1);
                    if(_major2 != null){
                        majors.add(_major2);
                        CentsApplication.set_major2(_major2);
                    }
                    mQuery.setMajors(majors);
                    MajorService service = CentsApplication.get_centsRestAdapter().create(MajorService.class);
                    service.getMajorInfo(mQuery, new Callback<MajorResponse>() {
                        @Override
                        public void success(MajorResponse majorResponse, Response response) {
                            //Set MajorResponse
                            CentsApplication.set_mResponse(majorResponse);
                            //launch summary vis
                            Log.d(LOG_TAG, "success");
                            CentsApplication.set_selectedVis("Major Comparison");
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
                            ft.addToBackStack("major-intro");
                            ft.commit();
                            dismiss();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getActivity(), "Error - please try again", Toast.LENGTH_SHORT).show();

                        }
                    });
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

    private void loadMajorsList(){
        RecordsService service = CentsApplication.get_centsRestAdapter().create(RecordsService.class);
        RecordQuery query = new RecordQuery();
        query.setOperation("get");
        ArrayList<String> tables = new ArrayList<String>();
        tables.add("majors");
        query.setTables(tables);
        service.getRecords(query, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                //get list
                Log.d(LOG_TAG, "Received Majors List");
                _majors = majorsFromJson(response2);
                _majorAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, _majors);
                _majorSpinner1.setAdapter(_majorAdapter);
                _majorSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String full_selection = _majors[position];
                        int first_paren = full_selection.indexOf('(');
                        _major1 = new Major();
                        _major1.setName(full_selection.substring(0,first_paren));
                        //get lvl
                        String level  = full_selection.substring(first_paren+1, full_selection.length()-1);
                        _major1.setLevel(level);
//                        _major1.setOrder(1);
                        Log.d(LOG_TAG, "Selected major1: "+_major1);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());
            }
        });
    }

    private String[] majorsFromJson(Response response){
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
