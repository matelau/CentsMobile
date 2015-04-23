package com.matelau.junior.centsproject.Views.VisualizationFragments.College;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Controllers.VisualizationPagerFragment;
import com.matelau.junior.centsproject.Models.CentsAPIServices.RecordsService;
import com.matelau.junior.centsproject.Models.CentsAPIServices.SchoolService;
import com.matelau.junior.centsproject.Models.CentsAPIServices.UserService;
import com.matelau.junior.centsproject.Models.UserModels.Query;
import com.matelau.junior.centsproject.Models.VizModels.RecordQuery;
import com.matelau.junior.centsproject.Models.VizModels.School;
import com.matelau.junior.centsproject.Models.VizModels.SchoolRequest;
import com.matelau.junior.centsproject.Models.VizModels.SchoolResponse;
import com.matelau.junior.centsproject.R;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollegeSelectionDialogFragment extends DialogFragment {
    private String LOG_TAG = CollegeSelectionDialogFragment.class.getSimpleName();
    private final String[] _state_abbrv = {"AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA","HI","ID","IL","IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ","NM","NY","NC","ND","OH","OK","OR","PA","RI","SC","SD","TN","TX","UT","VT","VA","WA","WV","WI","WY"};
    private String[] _states;
    private HashMap<String, String> _stateMap;
    private LinearLayout _rootLayout;
    private Spinner _states1;
    private Spinner _states2;
    private TextView _stateTextView2;
    private Spinner _universitySpinner1;
    private TextView _universityTextView1;
    private Spinner _universitySpinner2;
    private TextView _universityTextView2;
    private String _university1;
    private String _university2;
    private ArrayAdapter<String> _stateAdapter;
    private TextView _vs;
    private View _plusBtn;
    private Button _submit;
    private Button _cancel;
    private boolean isPlus = true;
    private boolean initLoad1 = true;
    private boolean initLoad2 = true;

    public CollegeSelectionDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateDialog");

        //if selected vis is a college comparison and selected vis is set to college comp clear values
        if(CentsApplication.get_selectedVis()!= null &&  !CentsApplication.get_selectedVis().equals("College Comparison")){
            CentsApplication.set_selectedVis(null);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        _states = getResources().getStringArray(R.array.states_array);
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_selection_dialog, null, false);
        //get views
        TextView instructions = (TextView) _rootLayout.findViewById(R.id.selection_instructions);
        instructions.setText("Select a state to view Universities");
        _submit = (Button) _rootLayout.findViewById(R.id.submit_select);
        _cancel = (Button) _rootLayout.findViewById(R.id.cancel_select);
        _vs = (TextView) _rootLayout.findViewById(R.id.vs);
        _universityTextView1 = (TextView) _rootLayout.findViewById(R.id.optionTextView1);
        _plusBtn = _rootLayout.findViewById(R.id.circle);
        _universityTextView2 = (TextView) _rootLayout.findViewById(R.id.optionTextView2);

        //University1
        _universitySpinner1 = (Spinner) _rootLayout.findViewById(R.id.option_input1);
        //University2
        _universitySpinner2 = (Spinner) _rootLayout.findViewById(R.id.option_input2);
        //State1
        _states1 = (Spinner) _rootLayout.findViewById(R.id.state_spinner1);
        //State2
        _states2 = (Spinner) _rootLayout.findViewById(R.id.state_spinner2);
        _stateTextView2 = (TextView) _rootLayout.findViewById(R.id.stateTextView2);
        _stateAdapter = new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_dropdown_item, _states);
        _states1.setAdapter(_stateAdapter);
        _states1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //load universities
                if (!_states[position].equals("Select State")) {
                    Log.d(LOG_TAG, "Selected State1: " + _states[position]);
                    _universityTextView1.setText("University - 1");
                    _universityTextView1.setVisibility(View.VISIBLE);
                    //store positions in case user searches again
                    CentsApplication.setPos1(position);
                    //check for prior search
                    loadCollegeList1(position, false);
                }
                else{
                    loadPriorSearch();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //flip button
                if(isPlus){
                   addPlusViews();
                }
                else{
                    isPlus = true;
                    _plusBtn.setBackground(getResources().getDrawable(R.drawable.ic_action_new));
                    //hide secondary selection
                    _states2.setVisibility(View.GONE);
                    _stateTextView2.setVisibility(View.GONE);
                    _universitySpinner2.setVisibility(View.GONE);
                    _universityTextView2.setVisibility(View.GONE);
                    _vs.setVisibility(View.GONE);
                    CentsApplication.setPos2(-1);
                    //null values
                    _university2 = null;
                }

            }
        });

        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //submit Universities
                submitUniversities();
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

    /**
     * Submits the selected universities
     */
    private void submitUniversities(){
        if(_university1 != null){
            ArrayList<School> schools = new ArrayList<School>();
            School school1 = new School();
            school1.setName(_university1);
            schools.add(school1);
            CentsApplication.set_university1(_university1);
            CentsApplication.set_sApiResponse(null);
            String query = _university1;
            if(_university2 != null){
                School school2 = new School();
                school2.setName(_university2);
                CentsApplication.set_university2(_university2);
                schools.add(school2);
                query = query + " vs. "+_university2;
            }
            if(CentsApplication.is_loggedIN()){
                storeQuery(query);
            }
            SchoolRequest sr = new SchoolRequest();
            sr.setOperation("compare");
            sr.setSchools(schools);
            SchoolService service = CentsApplication.get_centsRestAdapter().create(SchoolService.class);
            service.getSchools(sr, new Callback<SchoolResponse>() {
                @Override
                public void success(SchoolResponse schoolResponse, Response response) {
                    Log.d(LOG_TAG, "Success");
                    CentsApplication.set_sApiResponse(schoolResponse);
                    //show college comparison
                    showCollegeComparison();

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(LOG_TAG, error.getMessage());
                    Toast.makeText(getActivity(), "Error - Please try again", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    /**
     * Switches fragment to the college comparison summary
     */
    private void showCollegeComparison(){
        //get college sum frag
        CentsApplication.set_selectedVis("College Comparison");
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
        ft.addToBackStack("college-intro");
        ft.commit();
        dismiss();
    }

    /**
     * if a prior university search has been completed preload the state and college list
     */
    private void loadPriorSearch(){
        //check for already performed searches
        SchoolResponse s = CentsApplication.get_sApiResponse();
        //retrieve state position within the state array
        int pos1 = CentsApplication.getPos1();
        if(s != null && pos1 != -1){
            _states1.setSelection(pos1, true);
            loadCollegeList1(pos1,true);
            int pos2 = CentsApplication.getPos2();
            if(pos2 != -1){
                addPlusViews();
                _states2.setSelection(pos2, true);
                loadCollegeList2(pos2,true);
            }
        }
    }
    /**
     * stores a users latest query
     * @param searchText
     */
    private void storeQuery(String searchText){
        //create query model
        Query q = new Query();
        q.setUrl(searchText);
        //load user id
        SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        int ID = settings.getInt("ID", 0);
        Log.d(LOG_TAG, "Loaded ID from Prefs: " + ID);
        UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
        service.storeQuery(q, ID, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(LOG_TAG, "Stored Query");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());

            }
        });
    }

    /**
     * adds secondary views
     */
    private void addPlusViews(){
        isPlus = false;
        _plusBtn.setBackground(getResources().getDrawable(R.drawable.minus));
        //show secondary selections
        _vs.setVisibility(View.VISIBLE);
        _stateTextView2.setVisibility(View.VISIBLE);
        _states2.setVisibility(View.VISIBLE);
        _states2.setAdapter(_stateAdapter);
        _states2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //load universities
                if (!_states[position].equals("Select State")) {
                    Log.d(LOG_TAG, "Selected State2: " + _states[position]);
                    _universityTextView2.setText("University - 2");
                    _universityTextView2.setVisibility(View.VISIBLE);
                    CentsApplication.setPos2(position);
                    loadCollegeList2(position, false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    /**
     * Loads first spinner with colleges from a selected state
     * @param position
     */
    private void loadCollegeList1(int position, final boolean setUni){
        //Load College Records
        RecordsService service = CentsApplication.get_centsRestAdapter().create(RecordsService.class);
        RecordQuery query = new RecordQuery();
        query.setOperation("get");
        ArrayList<String> tables = new ArrayList<String>();
        tables.add("university");
        query.setTables(tables);
        //minus 1 due to select state in states abbreviation
        String abbr = _state_abbrv[position-1];
        Log.d(LOG_TAG, "loading unis in: " + abbr);
        query.setWhere(abbr);
        service.getRecords(query, new Callback<String[]>() {
            @Override
            public void success(String[] response, Response response2) {
                String[] unis1 = response;

                _universitySpinner1.setVisibility(View.VISIBLE);
                _universitySpinner1.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, unis1));
                _universitySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView tv = (TextView) view;
                        if (tv != null ) {
                            _university1 = tv.getText().toString();
                        }
                        Log.d(LOG_TAG, "Selected Uni1: " + _university1);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                //set uni selection to previous search
                if(setUni){
                    for(int i = 0; i < unis1.length; i++){
                        if(unis1[i].trim().equals(CentsApplication.get_university1().trim())){
                            _universitySpinner1.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());

            }
        });
    }


    /**
     * Loads second spinner with colleges from a selected state
     * @param position
     */
    private void loadCollegeList2(int position, final boolean setUni){
        //Load College Records
        RecordsService service = CentsApplication.get_centsRestAdapter().create(RecordsService.class);
        RecordQuery query = new RecordQuery();
        query.setOperation("get");
        ArrayList<String> tables = new ArrayList<String>();
        tables.add("university");
        query.setTables(tables);
        //minus 1 due to select state in states abbreviation
        String abbr = _state_abbrv[position-1];
        Log.d(LOG_TAG, "loading unis in: "+abbr);
        query.setWhere(abbr);
        service.getRecords(query, new Callback<String[]>() {
            @Override
            public void success(String[] response, Response response2) {
                String[] unis2 = response;
                _universitySpinner2.setVisibility(View.VISIBLE);
                _universitySpinner2.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, unis2));

                _universitySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView tv = (TextView) view;
                        if (tv != null) {
                            _university2 = tv.getText().toString();
                        }
                        Log.d(LOG_TAG, "Selected Uni1: " + _university2);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                //set uni selection to previous search
                if(setUni){
                    for(int i = 0; i < unis2.length; i++){
                        if(unis2[i].trim().equals(CentsApplication.get_university2().trim())){
                            _universitySpinner2.setSelection(i);
                            break;
                        }
                    }
                }


            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Destroyed");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Resumed");
    }


}
