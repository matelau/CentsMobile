package com.matelau.junior.centsproject.Views.VisualizationFragments.Major;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Controllers.VisualizationPagerFragment;
import com.matelau.junior.centsproject.Models.CentsAPIServices.MajorService;
import com.matelau.junior.centsproject.Models.CentsAPIServices.UserService;
import com.matelau.junior.centsproject.Models.UserModels.Query;
import com.matelau.junior.centsproject.Models.VizModels.RecordQuery;
import com.matelau.junior.centsproject.Models.CentsAPIServices.RecordsService;
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
    private AutoCompleteTextView _autoComp1;
    private AutoCompleteTextView _autoComp2;
    private boolean initialLoad1 = true;
    private boolean initialLoad2 = true;


    private String[] _majors;


    private TextView _vs;
    private TextView _majorTextView1;
    private TextView _majorTextView2;
    private TextView _instructions;

    private boolean isPlus;
    private Major _major1;
    private Major _major2;
    private boolean valuesLoaded = false;

    private boolean _useAutocomplete;


    public MajorSelectionDialogFragment() {
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

        //clear old values
        if(CentsApplication.get_selectedVis()!= null && !CentsApplication.get_selectedVis().equals("Major Comparison")){
            CentsApplication.set_selectedVis(null);
            CentsApplication.set_major1(null);
            CentsApplication.set_major2(null);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_selection_dialog, null, false);
        _majorSpinner1 = (Spinner) _rootLayout.findViewById(R.id.state_spinner1);
        _majorSpinner2 = (Spinner) _rootLayout.findViewById(R.id.state_spinner2);
        TextView instructions = (TextView) _rootLayout.findViewById(R.id.selection_instructions);
        instructions.setText("Select a state to view Universities");
        _submit = (Button) _rootLayout.findViewById(R.id.submit_select);
        _cancel = (Button) _rootLayout.findViewById(R.id.cancel_select);
        _autoComp1 = (AutoCompleteTextView) _rootLayout.findViewById(R.id.ac_view1);
        _autoComp2 = (AutoCompleteTextView) _rootLayout.findViewById(R.id.ac_view2);
        _vs = (TextView) _rootLayout.findViewById(R.id.vs);
        _instructions = (TextView) _rootLayout.findViewById(R.id.selection_instructions);
        _instructions.setText("Select one or two Majors");
        _majorTextView1 = (TextView) _rootLayout.findViewById(R.id.stateTextView);
        _majorTextView1.setText("Major - 1");
        _majorTextView2 = (TextView) _rootLayout.findViewById(R.id.stateTextView2);
        _plusBtn = _rootLayout.findViewById(R.id.circle);

        loadMajorsList();




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
                    _majorTextView2.setVisibility(View.GONE);
                    _majorSpinner2.setVisibility(View.GONE);
                    _autoComp2.setVisibility(View.GONE);
                    _major2 = null;

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
                    String query = "";
                    if(_major1 != null){
                        majors.add(_major1);
                        CentsApplication.set_major1(_major1);
                        query = _major1.getName()+" ("+_major1.getLevel()+" )";
                    }

                    if(_major2 != null){
                        majors.add(_major2);
                        CentsApplication.set_major2(_major2);
                        query = query+" vs. "+_major2.getName()+" ("+_major2.getLevel()+" )";
                    }
                    if(CentsApplication.is_loggedIN()){
                        storeQuery(query);
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


    private int getMajorPosition(String major){
        major = major.trim();
        int pos = -1;
        for(int i = 0; i < _majors.length; i++){
            if(major.equals(_majors[i])){
                return i;
            }
        }
        return pos;
    }

    private void addPlusViews(){
        isPlus = false;
        _plusBtn.setBackground(getResources().getDrawable(R.drawable.minus));
        //show secondary selections
        _vs.setVisibility(View.VISIBLE);
        _majorTextView2.setVisibility(View.VISIBLE);
        _majorTextView2.setText("Major - 2");
        if(!_useAutocomplete){
            _majorSpinner2.setVisibility(View.VISIBLE);
            _majorSpinner2.setAdapter(_majorAdapter);
            _majorSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String full_selection = _majors[position];
                    _major2 = parseMajor(full_selection);
//                            _major2.setOrder(2);
                    Log.d(LOG_TAG, "Selected Major2: " + _major2);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }
        else{
            //hide spinner show autoComp
            Log.d(LOG_TAG, "Loading AutoText2");
            _autoComp2.setVisibility(View.VISIBLE);
            _autoComp2.requestFocus();
            _autoComp2.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.ac_dropdown_element, _majors));
            _autoComp2.setThreshold(1);
            _autoComp2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv = (TextView) view;
                    String full_selection = tv.getText().toString();
                    _major2 = parseMajor(full_selection);
//                            _major2.setOrder(2);
                    Log.d(LOG_TAG, "Selected Major2: " + _major2);
                }
            });
            _autoComp2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //force user to select one of the auto complete options
                    if(!initialLoad2){
                        _major2 = null;
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

    }

    private void loadPreviousSearch(){
        //load values of previous search if one exists
        MajorResponse m = CentsApplication.get_mResponse();
        if(m != null){
            List<MajorResponse.Element> elements = m.getElements();
            String major = elements.get(0).getName();
            _major1 = parseMajor(major) ;
            Log.d(LOG_TAG, "major1: " + major);
            int pos1 = getMajorPosition(major);
            Log.d(LOG_TAG, "pos1:" + pos1);
            if(!_useAutocomplete){
                _majorSpinner1.setSelection(pos1, true);
            }
            else{
                _autoComp1.setText(major);
            }
            if(elements.size() > 1){
                addPlusViews();
                String major2 = elements.get(1).getName();
                _major2 = parseMajor(major2);
                Log.d(LOG_TAG, "major2: "+major);
                int pos2 = getMajorPosition(major);
                Log.d(LOG_TAG, "pos2:"+pos2);
                if(!_useAutocomplete) {
                    _majorSpinner2.setSelection(pos2, true);
                }
                else{
                    _autoComp2.setText(major2);
                }
            }
        }

    }

    private void loadMajorsList(){
        if (CentsApplication.get_majors() != null){
            _majors = CentsApplication.get_majors();
            initSpinner1();
            loadPreviousSearch();
        }
        else{
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
                    //cache majors list
                    CentsApplication.set_majors(_majors);
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

    /**
     * returns a major object from a string with level set
     * @param major
     * @return
     */
    private Major parseMajor(String major){
        Major m = new Major();
        int first_paren = major.indexOf('(');
        m.setName(major.substring(0,first_paren));
        //get lvl
        String level  = major.substring(first_paren+1, major.length()-1);
        m.setLevel(level);
        return m;
    }

    private void initSpinner1(){
        if(!_useAutocomplete){
            _majorAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, _majors);
            _majorSpinner1.setAdapter(_majorAdapter);
            _majorSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String full_selection = _majors[position];
                    _major1 = parseMajor(full_selection);
                    Log.d(LOG_TAG, "Selected major1: "+_major1);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else{
            //hide spinner show autoComp
            Log.d(LOG_TAG, "Loading AutoText");
            _majorSpinner1.setVisibility(View.GONE);
            _autoComp1.setVisibility(View.VISIBLE);
            _autoComp1.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.ac_dropdown_element, _majors));
            _autoComp1.setThreshold(1);
            _autoComp1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv = (TextView) view;
                    String full_selection = tv.getText().toString();
                    _major1 = parseMajor(full_selection);
                    Log.d(LOG_TAG, "Selected major1: " + _major1);
                }
            });
            _autoComp2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!initialLoad1){
                        _major1 = null;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void storeQuery(String searchText){
        //create query model
        Query q = new Query();
        q.setUrl(searchText);
        //load user id
        SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        int ID = settings.getInt("ID", 0);
        Log.d(LOG_TAG, "Loaded ID from Prefs: "+ID);
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
