package com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.matelau.junior.centsproject.Models.CentsAPIServices.CostOfLivingService;
import com.matelau.junior.centsproject.Models.CentsAPIServices.RecordsService;
import com.matelau.junior.centsproject.Models.CentsAPIServices.UserService;
import com.matelau.junior.centsproject.Models.UserModels.Query;
import com.matelau.junior.centsproject.Models.VizModels.ColiResponse;
import com.matelau.junior.centsproject.Models.VizModels.CostOfLiving;
import com.matelau.junior.centsproject.Models.VizModels.CostOfLivingLocation;
import com.matelau.junior.centsproject.Models.VizModels.RecordQuery;
import com.matelau.junior.centsproject.R;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.matelau.junior.centsproject.R.drawable.minus;

/**
 * Allows Selection of Cities
 */
public class CitySelectionDialogFragment extends DialogFragment {

    private String LOG_TAG = CitySelectionDialogFragment.class.getSimpleName();

    private ArrayAdapter<String> _stateAdapter;
    private Spinner _stateSpinner1;
    private Spinner _stateSpinner2;
    private TextView _cityTextView1;
    private TextView _cityTextView2;
    private TextView _stateTextView;
    private TextView _stateTextView2;
    private TextView _vs;
    private View _plusBtn;

    private Spinner _citySpinner1;
    private Spinner _citySpinner2;

    private String[] _states;
    private String[] _allCities;
    private boolean isPlus = true;


    private String _state1;
    private String _city1;
    private String _city2;
    private String _state2;

    private String _previousCity1 = null;
    private String _previousCity2 = null;

    private boolean _useAutocomplete;
    private AutoCompleteTextView _autoComp1;
    private AutoCompleteTextView _autoComp2;
    private String _prevlocation = null;
    private String _prevlocation2 = null;

    private boolean initialLoad1 = true;
    private boolean initialLoad2 = true;



    public CitySelectionDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateDialog");

        //read selection mechanism from preference
        SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        _useAutocomplete = settings.getBoolean("Autocomplete", true);
        Log.d(LOG_TAG, "Autocomplete value: "+_useAutocomplete);


        //if selected vis is a city comparison switch background fragment to search - to clear old viz
        if(CentsApplication.get_selectedVis()!= null && !CentsApplication.get_selectedVis().equals("COL Comparison")){
            CentsApplication.set_selectedVis(null);
        }
        _states = getResources().getStringArray(R.array.states_array);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_selection_dialog, null, false);
        TextView instructions = (TextView) _rootLayout.findViewById(R.id.selection_instructions);
        if(_useAutocomplete)
            instructions.setText("Select one or two cities");
        _plusBtn = _rootLayout.findViewById(R.id.circle);
        _citySpinner1 = (Spinner) _rootLayout.findViewById(R.id.option_input1);
        _cityTextView1 = (TextView) _rootLayout.findViewById(R.id.optionTextView1);
        _cityTextView2 = (TextView) _rootLayout.findViewById(R.id.optionTextView2);

        _vs = (TextView) _rootLayout.findViewById(R.id.vs);
        _stateTextView = (TextView) _rootLayout.findViewById(R.id.stateTextView);
        _stateTextView2 = (TextView) _rootLayout.findViewById(R.id.stateTextView2);
        _autoComp1 = (AutoCompleteTextView) _rootLayout.findViewById(R.id.ac_view1);
        _autoComp2 = (AutoCompleteTextView) _rootLayout.findViewById(R.id.ac_view2);

        _citySpinner2 = (Spinner) _rootLayout.findViewById(R.id.option_input2);
        Button _submit = (Button) _rootLayout.findViewById(R.id.submit_select);
        Button _cancel = (Button) _rootLayout.findViewById(R.id.cancel_select);
        _stateSpinner2 = (Spinner) _rootLayout.findViewById(R.id.state_spinner2);
        hideView(false);
        _stateSpinner1 = (Spinner) _rootLayout.findViewById(R.id.state_spinner1);
        _stateAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, _states);
        _stateSpinner1.setAdapter(_stateAdapter);
        _stateSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _state1 = _states[position];
                if(!_state1.equals("Select State")){
                    Log.d(LOG_TAG, "Selected State1: "+_state1);
                    //Get list of cities available for state
                    getCities1(_state1);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                _state1 = null;
            }
        });

        _plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlus){
                    addPlusViews();
                }
                else{
                    //isMinus remove all views clear values
                    hideView(true);
                    _city2 = null;
                    _state2 = null;
                    isPlus = true;
                    _plusBtn.setBackground(getResources().getDrawable(R.drawable.ic_action_new));


                }

            }
        });

        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });


        _cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset options
                Log.d(LOG_TAG, "cancel");
                dismiss();

            }
        });

        checkForPreviousSearch();

        builder.setTitle("Enter Locations For Comparison").setView(_rootLayout);
        return builder.create();
    }

    private void handleSubmit(){
        if (_city1 != null && _state1 != null){
            CostOfLiving col = new CostOfLiving();
            CostOfLivingLocation loc1 = new CostOfLivingLocation();
            loc1.setCity(_city1);
            loc1.setState(_state1);
            List<CostOfLivingLocation> lCLL = new ArrayList<CostOfLivingLocation>();
            lCLL.add(loc1);
            CostOfLivingLocation loc2 = new CostOfLivingLocation();
            String query = _city1+", "+_state1;
            if(_city2 != null && _state2 != null){
                loc2.setCity(_city2);
                loc2.setState(_state2);
                lCLL.add(loc2);
                query = query + " vs. "+_city2+", "+_state2;
            }
            if(CentsApplication.is_loggedIN()){
                storeQuery(query);
            }
            col.setLocations(lCLL);
            col.setOperation("compare");
            CostOfLivingService service = CentsApplication.get_centsRestAdapter().create(CostOfLivingService.class);
            service.getColi(col, new Callback<ColiResponse>() {
                @Override
                public void success(ColiResponse coliResponse, Response response) {
                    Log.d(LOG_TAG, "success");
                    CentsApplication.set_colResponse(coliResponse);
                    CentsApplication.set_selectedVis("COL Comparison");
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
                    ft.addToBackStack("col-selection");
                    ft.commit();
                    dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(LOG_TAG, error.getMessage());
                }
            });


        }
        else{
            Toast.makeText(getActivity(), "Error - Try Making Another Selection", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * loads all cities for autocomps
     */
    private void loadAllCities(final ColiResponse c){
        _stateTextView.setText("City - 1");
        _stateSpinner1.setVisibility(View.GONE);
        _autoComp1.setVisibility(View.VISIBLE);
        if (CentsApplication.get_allCities() != null){
            _allCities = CentsApplication.get_allCities();
            loadAutoComp1();
            if(c != null && c.getElements().size() > 1)
                loadAutoComp2();
        }
        else{
            RecordsService service = CentsApplication.get_centsRestAdapter().create(RecordsService.class);
            service.getCitiesV2(new Callback<String[]>() {
                @Override
                public void success(String[] strings, Response response) {
                    _allCities = strings;
                    CentsApplication.set_allCities(strings);
                    loadAutoComp1();
                    if (c != null && c.getElements().size() > 1) {
                        addPlusViews();
                    }


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
     * loads autocomptv1 for use hides spinner views
     */
    private void loadAutoComp1(){
        //hide spinner show autoComp
        Log.d(LOG_TAG, "Loading AutoText");
        _stateTextView.setText("City - 1");
        _stateSpinner1.setVisibility(View.GONE);
        _citySpinner1.setVisibility(View.GONE);
        _autoComp1.setVisibility(View.VISIBLE);
        _autoComp1.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.ac_dropdown_element, _allCities));
        _autoComp1.setThreshold(1);
        _autoComp1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                String full_selection = tv.getText().toString().trim();
                setCityState(full_selection, true);
                Log.d(LOG_TAG, "Selected loc1: " + _city1);
            }
        });
        _autoComp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!initialLoad1) {
                    _city1 = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        _autoComp1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event != null){
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        handleSubmit();
                        return true;
                    }
                }
                return false;
            }
        });

        if(_prevlocation != null){
            _autoComp1.setText(_prevlocation);
            setCityState(_prevlocation, true);
        }

    }

    private void setCityState(String full_selection, boolean loc1){
        if(loc1){
            _state1 =  full_selection.substring(full_selection.indexOf(',') + 1, full_selection.length()).trim();
            _city1 = full_selection.substring(0, full_selection.indexOf(",")).trim();
        }
        else{
            _state2 =  full_selection.substring(full_selection.indexOf(',') + 1, full_selection.length()).trim();
            _city2 = full_selection.substring(0, full_selection.indexOf(",")).trim();

        }


    }

    /**
     * Loads autocomptv 2 for use hides spinner views
     */
    private void loadAutoComp2(){
        //load auto comp 2
        //hide spinner show autoComp
        Log.d(LOG_TAG, "Loading AutoText2");
        _vs.setVisibility(View.VISIBLE);
        _plusBtn.setBackground(getResources().getDrawable(minus));
        isPlus = false;
        _stateTextView2.setText("City - 2");
        _stateTextView2.setVisibility(View.VISIBLE);
        _autoComp2.setVisibility(View.VISIBLE);
        _autoComp2.requestFocus();
        _autoComp2.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.ac_dropdown_element, _allCities));
        _autoComp2.setThreshold(1);
        _autoComp2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                String full_selection = tv.getText().toString();
                setCityState(full_selection, false);
                Log.d(LOG_TAG, "Selected city2: " + _city2);
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
                    _city2 = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        _autoComp2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event != null){
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        handleSubmit();
                        return true;
                    }
                }
                return false;
            }
        });

        if(_prevlocation2 != null){
            _autoComp2.setText(_prevlocation2);
            setCityState(_prevlocation2, false);
        }
    }

    /**
     * sets spinners to previous search if available
     */
    private void checkForPreviousSearch(){
        //check for previous searches
        ColiResponse  c = CentsApplication.get_colResponse();
        if(c != null){
            List<ColiResponse.Element> elements = c.getElements();
            boolean hasSecondCity = false;
            if(elements.size() > 1){
                hasSecondCity = true;
            }

            _prevlocation =  elements.get(0).getName();
            _prevlocation2 = null;
            if(hasSecondCity){
                _prevlocation2 = elements.get(1).getName();
            }

            if(!_useAutocomplete) {
                //state 1
                String state = _prevlocation.substring(_prevlocation.indexOf(',') + 1, _prevlocation.length()).trim();
                _previousCity1 = _prevlocation.substring(0, _prevlocation.indexOf(",")).trim();
                Log.d(LOG_TAG, "Loc1 = " + state + " city1: " + _previousCity1);
                int statePos = getStatePosition(state);
                Log.d(LOG_TAG, "Position = " + statePos);
                _stateSpinner1.setSelection(statePos, true);
                //state 2
                if (hasSecondCity) {
                    addPlusViews();

                    String state2 = _prevlocation2.substring(_prevlocation2.indexOf(',') + 1, _prevlocation2.length()).trim();
                    _previousCity2 = _prevlocation2.substring(0, _prevlocation2.indexOf(",")).trim();
                    Log.d(LOG_TAG, "Loc2 = " + state2 + " city2: " + _previousCity2);
                    int statePos2 = getStatePosition(state2);
                    Log.d(LOG_TAG, "Position = " + statePos2);
                    _stateSpinner2.setSelection(statePos2, true);
                }
            }
            else{
                loadAllCities(c);
            }
        }
        else{
            if(_useAutocomplete){
                loadAllCities(null);
            }

        }

    }


    /**
     * stores the users query
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
     * Adds secondary selection views
     */
    public void addPlusViews(){

        _stateTextView2.setVisibility(View.VISIBLE);
        _vs.setVisibility(View.VISIBLE);
        isPlus = false;
        _plusBtn.setBackground(getResources().getDrawable(minus));

        if(!_useAutocomplete){
            _stateSpinner2.setVisibility(View.VISIBLE);
            _stateSpinner2.setAdapter(_stateAdapter);
            _stateSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    _state2 = _states[position];
                    if(!_state2.equals("Select State")) {
                        Log.d(LOG_TAG, "Selected States: "+_state2);
                        getCities2(_state2);
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
     * Returns the position of state in the states array
     * @param state
     * @return
     */
    public int getStatePosition(String state){
        int pos = -1;
        for(int i = 0; i < _states.length; i++){
            if(state.equals(_states[i])){
                return i;
            }
        }
        return pos;
    }

    /**
     * hides views from the dialog frag
     * @param hideSecondOnly
     */
    public void hideView(boolean hideSecondOnly){
        _stateTextView2.setVisibility(View.GONE);
        _cityTextView2.setVisibility(View.GONE);
        _citySpinner2.setVisibility(View.GONE);
        _vs.setVisibility(View.GONE);
        _stateSpinner2.setVisibility(View.GONE);
        if(_useAutocomplete){
            _autoComp2.setVisibility(View.GONE);
        }
        if(!hideSecondOnly){
            _citySpinner1.setVisibility(View.GONE);
            _cityTextView1.setVisibility(View.GONE);

        }
    }

    /**
     * retrieves cities for the first spinner and displays them
     * @param state
     */
    public void getCities1(String state){
        RecordsService service = CentsApplication.get_centsRestAdapter().create(RecordsService.class);
        RecordQuery query = new RecordQuery();
        query.setOperation("get");
        ArrayList<String> tables = new ArrayList<String>();
        tables.add("cost of living");
        query.setTables(tables);
        query.setWhere(state);
        service.getRecords(query, new Callback<String[]>() {
            @Override
            public void success(String[] response, Response response2) {
                String[] cities = response;// citiesFromJson(response2);
                Log.d(LOG_TAG, "success");
                _citySpinner1.setVisibility(View.VISIBLE);
                _citySpinner1.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cities));
                _citySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView tv = (TextView) view;
                        _city1 = tv.getText().toString();
                        Log.d(LOG_TAG, "selected city1: "+_city1);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        _city1 = null;

                    }
                });
                _cityTextView1.setVisibility(View.VISIBLE);
                if(_previousCity1 != null){
                    for(int index = 0 ; index < cities.length; index++ ){
                        if(cities[index].trim().equals(_previousCity1)){
                            _citySpinner1.setSelection(index,true);
                            break;
                        }
                    }
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());
                Toast.makeText(getActivity(), "Error - Please select again", Toast.LENGTH_SHORT).show();

            }
        });



    }


    /**
     * Retrieves cities for the second spinner and displays them
     * @param state
     */
    public void getCities2(String state){
        RecordsService service = CentsApplication.get_centsRestAdapter().create(RecordsService.class);
        RecordQuery query = new RecordQuery();
        query.setOperation("get");
        ArrayList<String> tables = new ArrayList<String>();
        tables.add("cost of living");
        query.setTables(tables);
        query.setWhere(state);
        service.getRecords(query, new Callback<String[]>() {
            @Override
            public void success(String[] response, Response response2) {
                String[] cities = response; // citiesFromJson(response2);
                Log.d(LOG_TAG, "success");
                _citySpinner2.setVisibility(View.VISIBLE);
                _citySpinner2.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cities));
                _citySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView tv = (TextView) view;
                        _city2 = tv.getText().toString();
                        Log.d(LOG_TAG, "selected city2: "+_city2);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        _city2 = null;

                    }
                });

                _cityTextView2.setVisibility(View.VISIBLE);
                if(_previousCity2 != null){
                    for(int i =0; i < cities.length; i++){
                        if(cities[i].trim().equals(_previousCity2.trim())){
                            _citySpinner2.setSelection(i, true);
                            break;
                        }
                    }
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());
                Toast.makeText(getActivity(), "Error - Please select again", Toast.LENGTH_SHORT).show();

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
