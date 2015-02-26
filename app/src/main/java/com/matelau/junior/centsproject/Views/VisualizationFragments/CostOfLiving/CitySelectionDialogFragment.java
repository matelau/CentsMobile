package com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Controllers.VisualizationPagerFragment;
import com.matelau.junior.centsproject.Models.CentsAPIModels.QueryService;
import com.matelau.junior.centsproject.Models.VizModels.ColiResponse;
import com.matelau.junior.centsproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

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
    private FragmentActivity _fragAct;

    private EditText _city1;
    private EditText _city2;
    private Button _submit;
    private Button _cancel;

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
        _city1 = (EditText) _rootLayout.findViewById(R.id.city_input1);
        _city2 = (EditText) _rootLayout.findViewById(R.id.city_input2);
        _submit = (Button) _rootLayout.findViewById(R.id.submit_city_select);
        _cancel = (Button) _rootLayout.findViewById(R.id.cancel_city_select);

        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO add loading image to layout and show on query dismiss in callback
                String city1 = _city1.getText().toString();
                String city2 = _city2.getText().toString();
                //call query parser for now
                if (city1.length() > 0) {
                    String searchText = city1;
                    if (city2.length() > 0) {
                        //compare two cities
                        searchText += " vs " + city2;

                    }
                    QueryService service = CentsApplication.get_queryParsingRestAdapter().create(QueryService.class);
                    service.results(searchText, new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {
                                //create coli obj and launch coli viz
                                handleResponse(response);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d(LOG_TAG, error.getMessage());

                            }
                        });
                }

            }
        });


        _cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset options
                CentsApplication.set_searchedCity(null);
                CentsApplication.set_searchState(null);
                CentsApplication.set_searchedCity2(null);
                CentsApplication.set_searchState2(null);

            }
        });


        builder.setTitle("Enter Locations For Comparison").setView(_rootLayout);

        return builder.create();
    }

    private void handleResponse(Response response){
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
        ColiResponse colResponse = gson.fromJson(rsp, ColiResponse.class);
        CentsApplication.set_colResponse(colResponse);
        CentsApplication.set_selectedVis("COL Comparison");
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
        ft.commit();
        dismiss();
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
