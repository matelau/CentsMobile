package com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Controllers.VisualizationPagerFragment;
import com.matelau.junior.centsproject.Models.VizModels.CentsService;
import com.matelau.junior.centsproject.Models.VizModels.ColiQuery;
import com.matelau.junior.centsproject.Models.VizModels.ColiResponse;
import com.matelau.junior.centsproject.Models.VizModels.Location;
import com.matelau.junior.centsproject.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Allows Selection of Cities
 */
public class CitySelectionDialogFragment extends DialogFragment {

    private String LOG_TAG = CitySelectionDialogFragment.class.getSimpleName();

    private LinearLayout _rootLayout;
    private ArrayAdapter<String> _stateAdapter;
    private ArrayAdapter<String> _citiesAdapter1;
    private Spinner _stateSpinner1;
    private Spinner _citiesSpinner1;
    private ArrayAdapter<String> _citiesAdapter2;
    private Spinner _stateSpinner2;
    private Spinner _citiesSpinner2;
    private TextView _error;
    private FragmentActivity _fragAct;

    private String[] _states;
    private String[] _supportedCities;


    private String _state1;
    private String _state2;



    public CitySelectionDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_city_selection_dialog, null, false);
        _error = (TextView) _rootLayout.findViewById(R.id.city_select_error);
        //set selections
        _states = CentsApplication.get_states();
        _supportedCities = CentsApplication.get_cities();
        ArrayList<String> citiesList = new ArrayList<>();
        citiesList.add("Select State");


        //set views for city/state 1
        _stateAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, _states);
        _stateSpinner1 = (Spinner) _rootLayout.findViewById(R.id.state_spinner1);
        _stateSpinner1.setAdapter(_stateAdapter);
        _citiesSpinner1 = (Spinner) _rootLayout.findViewById(R.id.city_spinner1);
        _citiesAdapter1 = (new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, citiesList));
        _citiesSpinner1.setAdapter(_citiesAdapter1);

        _stateSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _state1 = _states[position];
                CentsApplication.set_searchState(_state1);
                Log.v(LOG_TAG, "Setting State: " + _state1);
                //update city spinner
                String[] cities = citiesInState(_state1);
                if(cities != null)
                {
                    _citiesSpinner1.setEnabled(true);
                    _citiesAdapter1.clear();
                    for(String city: cities){
                        Log.v(LOG_TAG, "adding city: " +city);
                        _citiesAdapter1.add(city);

                    }

                    _citiesSpinner1.invalidate();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _citiesSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView v = (TextView) view;
                String city = v.getText().toString();
                if(!city.contains("Select"))
                    CentsApplication.set_searchedCity(city);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //set views for city/state 2
        _stateSpinner2 = (Spinner) _rootLayout.findViewById(R.id.state_spinner2);
        _stateSpinner2.setAdapter(_stateAdapter);
        _citiesSpinner2 = (Spinner) _rootLayout.findViewById(R.id.city_spinner2);
        _citiesAdapter2 = (new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, citiesList));
        _citiesSpinner2.setAdapter(_citiesAdapter2);

        _stateSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _state2 = _states[position];
                CentsApplication.set_searchState2(_state2);
                Log.v(LOG_TAG, "Setting State2: " + _state2);
                //update city spinner
                String[] cities = citiesInState(_state2);
                if(cities != null)
                {
                    _citiesSpinner2.setEnabled(true);
                    _citiesAdapter2.clear();
                    for(String city: cities){
                        Log.v(LOG_TAG, "adding city2: " +city);
                        _citiesAdapter2.add(city);

                    }

                    _citiesSpinner2.invalidate();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _citiesSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView v = (TextView) view;
                String city = v.getText().toString();
                if(!city.contains("Select"))
                    CentsApplication.set_searchedCity2(city);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //store the activity in global so the callback can reference when switching out the view
        _fragAct = getActivity();

        //build dialog
        builder.setTitle("Select Locations For Comparison")
                .setView(_rootLayout)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String city1 = CentsApplication.get_searchedCity();
                        String state1 = CentsApplication.get_searchState();
                        String city2 = CentsApplication.get_searchedCity2();
                        String state2 = CentsApplication.get_searchState2();


                        if (city1 != null && state1 != null && city2 != null && state2 != null) {
                            Log.d(LOG_TAG, "ColiQuery - "+city1 + ", "+city2);
                            CentsService service = CentsApplication.get_centsRestAdapter().create(CentsService.class);
                            Location loc1 = new Location(city1, state1);
                            Location loc2 = new Location(city2, state2);
                            List<Location> locs = new ArrayList<Location>();
                            locs.add(loc1);
                            locs.add(loc2);
                            ColiQuery colQuery = new ColiQuery("compare", locs);
                            service.coliQuery(colQuery, new Callback<ColiResponse>() {
                                @Override
                                public void success(ColiResponse coliResponse, Response response) {
                                    Log.d(LOG_TAG, "Successful ColiResponse Loc1" + coliResponse.getLocation1()+ " loc2"+coliResponse.getLocation2());
                                    CentsApplication.set_selectedVis("COL Comparison");
                                    FragmentTransaction ft = _fragAct.getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
                                    ft.commit();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.e(LOG_TAG, error.getMessage());
                                    CentsApplication.set_selectedVis("COL Comparison");
                                    FragmentTransaction ft = _fragAct.getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
                                    ft.commit();
                                }
                            });

                        }
//                        //update fragment with second location
//                        TextView tv = (TextView) _citiesSpinner1.getSelectedView();
//                        CentsApplication.set_searchedCity2(tv.getText().toString());
//                        getTargetFragment().onActivityResult(getTargetRequestCode(), 00, null);
                        //TODO call api show old vis til new ones are created


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //reset options
                        CentsApplication.set_searchedCity(null);
                        CentsApplication.set_searchState(null);
                        CentsApplication.set_searchedCity2(null);
                        CentsApplication.set_searchState2(null);
                    }
                });

        return builder.create();
    }


    /**
     * loads subset of cities based on current state selection
     */
    private String[] citiesInState(String state) {
        String[] cities = null;
        //todo create API Endpoint for dynamic city selection based on what is in the db
        //return only the cities in a selected state
        if (_supportedCities != null) {
            //AZ
            if (state.equals(_states[0])) {
                cities = Arrays.copyOfRange(_supportedCities, 0, 3);
            }
            //CA
            else if (state.equals(_states[1])) {
                cities = Arrays.copyOfRange(_supportedCities, 3, 8);
            } else if (state.equals(_states[2])) {
                cities = Arrays.copyOfRange(_supportedCities, 8, 11);
            } else if (state.equals(_states[3])) {
                cities = Arrays.copyOfRange(_supportedCities, 11, 12);
            } else if (state.equals(_states[4])) {
                cities = Arrays.copyOfRange(_supportedCities, 12, 16);
            } else if (state.equals(_states[5])) {
                cities = Arrays.copyOfRange(_supportedCities, 16, 18);
            } else if (state.equals(_states[6])) {
                cities = Arrays.copyOfRange(_supportedCities, 18, 19);
            } else if (state.equals(_states[7])) {
                cities = Arrays.copyOfRange(_supportedCities, 19, 20);
            } else if (state.equals(_states[8])) {
                cities = Arrays.copyOfRange(_supportedCities, 20, 21);
            } else if (state.equals(_states[9])) {
                cities = Arrays.copyOfRange(_supportedCities, 21, 22);
            } else if (state.equals(_states[10])) {
                cities = Arrays.copyOfRange(_supportedCities, 22, 23);
            }
            //New York
            else if (state.equals(_states[11])) {
                cities = Arrays.copyOfRange(_supportedCities, 23, 26);
            } else if (state.equals(_states[12])) {
                cities = Arrays.copyOfRange(_supportedCities, 26, 27);
            } else if (state.equals(_states[13])) {
                cities = Arrays.copyOfRange(_supportedCities, 27, 29);
            } else if (state.equals(_states[14])) {
                cities = Arrays.copyOfRange(_supportedCities, 29, 35);
            } else if (state.equals(_states[15])) {
                cities = Arrays.copyOfRange(_supportedCities, 35, 39);
            } else if (state.equals(_states[16])) {
                cities = Arrays.copyOfRange(_supportedCities, 39, 42);
            }
            //utah
            else {
                cities = Arrays.copyOfRange(_supportedCities, 42, 45);
            }



            }

        return cities;


        }

}
