package com.matelau.junior.centsproject.Controllers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.matelau.junior.centsproject.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by matelau on 12/8/14.
 */
public class SecondCityDialogFragment extends DialogFragment {
    private String LOG_TAG = SecondCityDialogFragment.class.getSimpleName();

    private ArrayAdapter<String> _stateAdapter;
    private ArrayAdapter<String> _citiesAdapter;
    private String[] _states;
    private String[] _supportedCities;
    private String _state;
    private String _city;
    private Spinner _citiesSpinner;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        _states = CentsApplication.get_states();
        _supportedCities = CentsApplication.get_cities();

        //prepare spinners
        View view = inflater.inflate(R.layout.state_and_city_spinner, null);
        Spinner states = (Spinner) view.findViewById(R.id.state_spinner);

        _stateAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, _states);
        states.setAdapter(_stateAdapter);
        states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _state = _states[position];
                CentsApplication.set_searchState2(_state);
                Log.v(LOG_TAG,"Setting State: "+_state );
                loadCities();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        _citiesSpinner = (Spinner) view.findViewById(R.id.city_spinner);
        List<String> cities = new ArrayList<String>();
        cities.add("Select State");
        _citiesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cities);
        _citiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView v = (TextView) view;
                if (v != null) {
                    String city = v.getText().toString();
                    if (city != null) {
                        _city = v.getText().toString();
                        CentsApplication.set_searchedCity2(_city);
                        Log.v(LOG_TAG,"Setting City: "+_city );

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        _citiesSpinner.setAdapter(_citiesAdapter);

        //build dialog
        builder.setTitle("Select Location For Comparison")
                .setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //update fragment with second location
                        TextView tv = (TextView) _citiesSpinner.getSelectedView();
                        CentsApplication.set_searchedCity2(tv.getText().toString());
                        getTargetFragment().onActivityResult(getTargetRequestCode(), 00, null);



                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //reset 2nd options
                        CentsApplication.set_searchedCity2(null);
                        CentsApplication.set_searchState2(null);
                    }
                });

        return builder.create();
    }

    /**
     * loads subset of cities based on current state selection
     */
    private void loadCities(){
        String[] cities = null;
        //TODO remove already selected city
        //todo move loadCities to a helper to reduce redundant code
        //return only the cities in a selected state
        if(_supportedCities!= null){
            _citiesSpinner.setEnabled(true);
            //AZ
            if(_state.equals(_states[0])){
                cities = Arrays.copyOfRange(_supportedCities, 0, 3);
            }
            //CA
            else if(_state.equals(_states[1])){
                cities = Arrays.copyOfRange(_supportedCities,3,8);
            }
            else if(_state.equals(_states[2])){
                cities = Arrays.copyOfRange(_supportedCities,8,11);
            }
            else if(_state.equals(_states[3])){
                cities = Arrays.copyOfRange(_supportedCities,11,12);
            }
            else if(_state.equals(_states[4])){
                cities = Arrays.copyOfRange(_supportedCities,12,16);
            }
            else if(_state.equals(_states[5])){
                cities = Arrays.copyOfRange(_supportedCities,16,18);
            }
            else if(_state.equals(_states[6])){
                cities = Arrays.copyOfRange(_supportedCities,18,19);
            }
            else if(_state.equals(_states[7])){
                cities = Arrays.copyOfRange(_supportedCities,19,20);
            }
            else if(_state.equals(_states[8])){
                cities = Arrays.copyOfRange(_supportedCities,20,21);
            }
            else if(_state.equals(_states[9])){
                cities = Arrays.copyOfRange(_supportedCities,21,22);
            }
            else if(_state.equals(_states[10])){
                cities = Arrays.copyOfRange(_supportedCities,22,23);
            }
            //New York
            else if(_state.equals(_states[11])){
                cities = Arrays.copyOfRange(_supportedCities,23,26);
            }
            else if(_state.equals(_states[12])){
                cities = Arrays.copyOfRange(_supportedCities,26,27);
            }
            else if(_state.equals(_states[13])){
                cities = Arrays.copyOfRange(_supportedCities,27,29);
            }
            else if(_state.equals(_states[14])){
                cities = Arrays.copyOfRange(_supportedCities,29,35);
            }
            else if(_state.equals(_states[15])){
                cities = Arrays.copyOfRange(_supportedCities,35,39);
            }
            else if(_state.equals(_states[16])){
                cities = Arrays.copyOfRange(_supportedCities,39,42);
            }
            //utah
            else{
                cities = Arrays.copyOfRange(_supportedCities,42,45);
            }
            //update cities adapter
            if(cities != null){
                _citiesAdapter.clear();
                //reset city to match new list
                _city = cities[0];
                for(String s: cities){
                    _citiesAdapter.add(s);
                }

            }

        }
    }
}
