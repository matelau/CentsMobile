package com.matelau.junior.centsproject.Views.VisualizationFragments.College;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
    private String[] _unis;
    private Spinner _states1;
    private Spinner _states2;
    private TextView _stateTextView2;
    private TextView _stateTextView1;
    private Spinner _universitySpinner1;
    private TextView _universityTextView1;
    private Spinner _universitySpinner2;
    private TextView _universityTextView2;
    private String _university1;
    private String _university2;
    private ArrayAdapter<String> _stateAdapter;
    private TextView _vs;
    private View _plusBtn;
    private boolean isPlus = true;
    private AutoCompleteTextView _autoComp1;
    private AutoCompleteTextView _autoComp2;
    private String _prevUni1;
    private String _prevUni2;
    private boolean _useAutocomplete;
    private boolean initialLoad2 = true;
    private boolean initialLoad1 = true;

    public CollegeSelectionDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateDialog");

        //read selection mechanism from preference
        SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        _useAutocomplete = settings.getBoolean("Autocomplete", true);
        Log.d(LOG_TAG, "Autocomplete value: "+_useAutocomplete);

        //if selected vis is a college comparison and selected vis is set to college comp clear values
        if(CentsApplication.get_selectedVis()!= null &&  !CentsApplication.get_selectedVis().equals("College Comparison")){
            CentsApplication.set_selectedVis(null);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        _states = getResources().getStringArray(R.array.states_array);
        LinearLayout _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_selection_dialog, null, false);
        //get views
        TextView instructions = (TextView) _rootLayout.findViewById(R.id.selection_instructions);
        instructions.setText("Select a state to view Universities");
        if(_useAutocomplete){
            instructions.setText("Select one or two Universities");
        }
        Button _submit = (Button) _rootLayout.findViewById(R.id.submit_select);
        Button _cancel = (Button) _rootLayout.findViewById(R.id.cancel_select);
        _vs = (TextView) _rootLayout.findViewById(R.id.vs);
        _universityTextView1 = (TextView) _rootLayout.findViewById(R.id.optionTextView1);
        _plusBtn = _rootLayout.findViewById(R.id.circle);
        _universityTextView2 = (TextView) _rootLayout.findViewById(R.id.optionTextView2);
        _autoComp1 = (AutoCompleteTextView) _rootLayout.findViewById(R.id.ac_view1);
        _autoComp2 = (AutoCompleteTextView) _rootLayout.findViewById(R.id.ac_view2);
        //University1
        _universitySpinner1 = (Spinner) _rootLayout.findViewById(R.id.option_input1);
        //University2
        _universitySpinner2 = (Spinner) _rootLayout.findViewById(R.id.option_input2);
        //State1
        _states1 = (Spinner) _rootLayout.findViewById(R.id.state_spinner1);
        //State2
        _states2 = (Spinner) _rootLayout.findViewById(R.id.state_spinner2);
        _stateTextView1 = (TextView) _rootLayout.findViewById(R.id.stateTextView);
        _stateTextView2 = (TextView) _rootLayout.findViewById(R.id.stateTextView2);
        _stateAdapter = new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_dropdown_item, _states);
        _states1.setAdapter(_stateAdapter);
        if(!_useAutocomplete){
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
                        loadCollegeList1(position, false);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        loadPriorSearch();

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
                    _autoComp2.setVisibility(View.GONE);
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
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
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
        if(s != null ){
            _prevUni1 = s.getElements().get(0).getName();
            if(s.getElements().size() > 1){
                _prevUni2 = s.getElements().get(1).getName();
            }
        }
        if(!_useAutocomplete) {
            int pos1 = CentsApplication.getPos1();
            if (s != null && pos1 != -1) {
                _states1.setSelection(pos1, true);
                loadCollegeList1(pos1, true);
                int pos2 = CentsApplication.getPos2();
                if (pos2 != -1) {
                    addPlusViews();
                    _states2.setSelection(pos2, true);
                    loadCollegeList2(pos2, true);
                }
            }
        }
        else{
            loadAllColleges(s);
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
            public void success(Response response, Response response2) {Log.d(LOG_TAG, "Stored Query");}

            @Override
            public void failure(RetrofitError error) {Log.e(LOG_TAG, error.getMessage());}
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
        if(!_useAutocomplete){
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
        else{
           loadAutoComp2();
        }
    }

    /**
     * loads the complete list of colleges
     * @param s
     */
    private void loadAllColleges(final SchoolResponse s){
        _stateTextView1.setText("University - 1");
        _states1.setVisibility(View.GONE);
        _autoComp1.setVisibility(View.VISIBLE);
        if (CentsApplication.get_unis() != null){
            _unis = CentsApplication.get_unis();
            loadAutoComp1();
            if(s != null && s.getElements().size() > 1)
                loadAutoComp2();
        }
        else{
            RecordsService service = CentsApplication.get_centsRestAdapter().create(RecordsService.class);
            service.getSchoolsV2(new Callback<String[]>() {
                @Override
                public void success(String[] strings, Response response) {
                    _unis = strings;
                    CentsApplication.set_unis(strings);
                    loadAutoComp1();
                    if (s != null && s.getElements().size() > 1)
                        loadAutoComp2();

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(LOG_TAG, error.getMessage());
                    Toast.makeText(getActivity(), "Error loading list please try again later", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * loads views for autocomp and hides others
     */
    private void loadAutoComp1(){
        //hide spinner show autoComp
        Log.d(LOG_TAG, "Loading AutoText");
        _stateTextView1.setText("University 1");
        _states1.setVisibility(View.GONE);
        _universitySpinner1.setVisibility(View.GONE);
        _autoComp1.setVisibility(View.VISIBLE);
        _autoComp1.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.ac_dropdown_element, _unis));
        _autoComp1.setThreshold(1);
        _autoComp1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                String full_selection = tv.getText().toString();
                _university1 = full_selection;
                Log.d(LOG_TAG, "Selected university1: " + _university1);
            }
        });
        _autoComp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!initialLoad1) {
                    _university1 = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        _autoComp1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        submitUniversities();
                        return true;
                    }
                }
                return false;
            }
        });

        if(_prevUni1 != null){
            _autoComp1.setText(_prevUni1);
            _university1 = _prevUni1;
        }
    }

    /**
     * loads views for autocompletion hides others
     */
    private void loadAutoComp2(){
        //load auto comp 2
        //hide spinner show autoComp
        Log.d(LOG_TAG, "Loading AutoText2");
        _stateTextView2.setText("University - 2");
        _stateTextView2.setVisibility(View.VISIBLE);
        _autoComp2.setVisibility(View.VISIBLE);
        _autoComp2.requestFocus();
        _autoComp2.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.ac_dropdown_element, _unis));
        _autoComp2.setThreshold(1);
        _autoComp2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                String full_selection = tv.getText().toString();
                _university2 = full_selection.trim();
                Log.d(LOG_TAG, "Selected uni2: " + _university2);
            }
        });
        _autoComp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //force user to select one of the auto complete options
                if (!initialLoad2) {
                    _university2 = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        _autoComp2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        submitUniversities();
                        return true;
                    }
                }
                return false;
            }
        });

        if(_prevUni2 != null){
            _autoComp2.setText(_prevUni2);
            _university2 = _prevUni2;
        }
    }


    /**
     * Loads first spinner with colleges from a selected state
     * @param position
     */
    private void loadCollegeList1(int position, final boolean setUni){
        if(!_useAutocomplete){
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
                            if (tv != null) {
                                _university1 = tv.getText().toString();
                            }
                            Log.d(LOG_TAG, "Selected Uni1: " + _university1);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    //set uni selection to previous search
                    if (_prevUni1 != null) {
                        for (int i = 0; i < unis1.length; i++) {
                            if (unis1[i].trim().equals(_prevUni1.trim())) {
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
    }


    /**
     * Loads second spinner with colleges from a selected state
     * @param position
     */
    private void loadCollegeList2(int position, final boolean setUni){
        if(!_useAutocomplete){
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
                    if(_prevUni2 != null){
                        for(int i = 0; i < unis2.length; i++){
                            if(unis2[i].trim().equals(_prevUni2.trim())) {
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
