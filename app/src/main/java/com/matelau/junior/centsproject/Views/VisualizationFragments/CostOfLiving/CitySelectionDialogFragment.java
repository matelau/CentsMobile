package com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.matelau.junior.centsproject.Models.CentsAPIServices.UserService;
import com.matelau.junior.centsproject.Models.UserModels.Query;
import com.matelau.junior.centsproject.Models.VizModels.CostOfLiving;
import com.matelau.junior.centsproject.Models.VizModels.CostOfLivingLocation;
import com.matelau.junior.centsproject.Models.CentsAPIServices.CostOfLivingService;
import com.matelau.junior.centsproject.Models.VizModels.RecordQuery;
import com.matelau.junior.centsproject.Models.CentsAPIServices.RecordsService;
import com.matelau.junior.centsproject.Models.VizModels.ColiResponse;
import com.matelau.junior.centsproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private LinearLayout _rootLayout;
    private ArrayAdapter<String> _stateAdapter;
    private Spinner _stateSpinner1;
    private Spinner _stateSpinner2;
    private TextView _cityTextView1;
    private TextView _cityTextView2;
    private TextView _stateTextView2;
    private TextView _vs;
    private View _plusBtn;

    private Spinner _citySpinner1;
    private Spinner _citySpinner2;
    private Button _submit;
    private Button _cancel;

    private String[] _states;
    private boolean isPlus = true;


    private String _state1;
    private String _city1;
    private String _city2;
    private String _state2;



    public CitySelectionDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateDialog");
        //if selected vis is a city comparison switch background fragment to search - to clear old viz
        if(CentsApplication.get_selectedVis()!= null && !CentsApplication.get_selectedVis().equals("COL Comparison")){
            CentsApplication.set_selectedVis(null);
        }
        _states = getResources().getStringArray(R.array.states_array);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_selection_dialog, null, false);
        _plusBtn = _rootLayout.findViewById(R.id.circle);
        _citySpinner1 = (Spinner) _rootLayout.findViewById(R.id.option_input1);
        _cityTextView1 = (TextView) _rootLayout.findViewById(R.id.optionTextView1);
        _cityTextView2 = (TextView) _rootLayout.findViewById(R.id.optionTextView2);

        _vs = (TextView) _rootLayout.findViewById(R.id.vs);
        _stateTextView2 = (TextView) _rootLayout.findViewById(R.id.stateTextView2);

        _citySpinner2 = (Spinner) _rootLayout.findViewById(R.id.option_input2);
        _submit = (Button) _rootLayout.findViewById(R.id.submit_select);
        _cancel = (Button) _rootLayout.findViewById(R.id.cancel_select);
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
                //TODO add loading image to layout and show on query dismiss in callback
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
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
                            ft.addToBackStack("city-intro");
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
        });


        _cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset options
                Log.d(LOG_TAG, "cancel");
                dismiss();

            }
        });

        //check for previous searches
        ColiResponse  c = CentsApplication.get_colResponse();
        if(c != null){
            List<ColiResponse.Element> elements = c.getElements();
            boolean hasSecondCity = false;
            if(elements.size() > 1){
                hasSecondCity = true;
            }
            //state 1
            String location =  elements.get(0).getName();
            String state = location.substring(location.indexOf(',')+1, location.length()).trim();
            Log.d(LOG_TAG, "Loc1 = "+ state);
            int statePos = getStatePosition(state);
            Log.d(LOG_TAG, "Position = "+ statePos);
            _stateSpinner1.setSelection(statePos, true);
            //state 2
            if(hasSecondCity){
                addPlusViews();
                String location2 = elements.get(1).getName();
                String state2 = location2.substring(location2.indexOf(',') + 1, location2.length()).trim();
                Log.d(LOG_TAG, "Loc2 = "+ state2);
                int statePos2 = getStatePosition(state2);
                Log.d(LOG_TAG, "Position = "+ statePos2);
                _stateSpinner2.setSelection(statePos2, true);
            }
        }


        builder.setTitle("Enter Locations For Comparison").setView(_rootLayout);
        return builder.create();
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


    /**
     * Adds secondary selection views
     */
    public void addPlusViews(){
        _stateSpinner2.setVisibility(View.VISIBLE);
        _stateTextView2.setVisibility(View.VISIBLE);
        _vs.setVisibility(View.VISIBLE);
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
        _vs.setVisibility(View.VISIBLE);
        isPlus = false;
        _plusBtn.setBackground(getResources().getDrawable(minus));

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
        if(hideSecondOnly){
            _stateTextView2.setVisibility(View.GONE);
            _cityTextView2.setVisibility(View.GONE);
            _citySpinner2.setVisibility(View.GONE);
            _vs.setVisibility(View.GONE);
            _stateSpinner2.setVisibility(View.GONE);

        }
        else{
            _stateTextView2.setVisibility(View.GONE);
            _cityTextView2.setVisibility(View.GONE);
            _citySpinner2.setVisibility(View.GONE);
            _vs.setVisibility(View.GONE);
            _stateSpinner2.setVisibility(View.GONE);
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
        service.getRecords(query, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String[] cities = citiesFromJson(response2);
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
        service.getRecords(query, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String[] cities = citiesFromJson(response2);
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

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());
                Toast.makeText(getActivity(), "Error - Please select again", Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * Parses Retrofit response and returns an array of cities
     * @param response
     * @return
     */
    private String[] citiesFromJson(Response response){
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
