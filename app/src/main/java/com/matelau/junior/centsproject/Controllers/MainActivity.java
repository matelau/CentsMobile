package com.matelau.junior.centsproject.Controllers;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matelau.junior.centsproject.Models.Col;
import com.matelau.junior.centsproject.Models.IndeedQueryResults;
import com.matelau.junior.centsproject.Models.IndeedService;
import com.matelau.junior.centsproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/*
 * @Author: Asaeli Matelau
 * Using Libraries for visualizations Processing, and RetroFit for api calls
 */
public class MainActivity extends Activity {
//    Hex Color #884412
    private EditText _editText;
    private String classLogTag = MainActivity.class.getSimpleName();

    private Spinner _citiesSpinner;
    private String _city;
    private String _state;
    private String _occupation;
    private  ArrayAdapter<String> _citiesAdapter;
    private ArrayAdapter<String> _stateAdapter;
    private List<Col> _cols;
    private Set<String> _supportedStatesHash;
    private String[] _states;
    private String[] _supportedCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);

        Spinner stateSpinner = (Spinner) findViewById(R.id.state_spinner);
//      //currently supported states
        _states = new String[]{"Arizona", "California", "Colorado", "District of Columbia", "Florida", "Illinois", "Indiana",
                "Massachusetts", "Michigan", "Ohio", "North Carolina", "New York", "Pennsylvania", "Tennessee", "Texas", "Washington",
                "Wisconsin", "Utah"};
        _supportedStatesHash = new HashSet<String>(Arrays.asList(_states));
        //load state data
        _stateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, _states);
        loadLocations();
        _stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(_stateAdapter);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView v = (TextView) view;
                _state = v.getText().toString();
                Log.v(classLogTag, "State Spinner item Selected: "+_state);
                //load cities based on state selected
                loadCities();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                _citiesSpinner.setEnabled(false);

            }
        });


        _citiesSpinner = (Spinner) findViewById(R.id.city_spinner);
        _citiesSpinner.setEnabled(false);
        List<String> cities = new ArrayList<String>();
        cities.add("Select State");
        _citiesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        _citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _citiesSpinner.setAdapter(_citiesAdapter);
        _citiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            TextView v = (TextView) view;
            _city = v.getText().toString();
            Log.v(classLogTag, "City Spinner item Selected: "+_city);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            _citiesSpinner.setEnabled(false);

        }
    });

        ImageButton search_submit = (ImageButton) findViewById(R.id.search_button);
        _editText = (EditText) findViewById(R.id.editText1);
        _editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    //hide keyboard after submit
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(_editText.getWindowToken(), 0);
                    handleSubmit();

                    return true;
                }
                return false;
            }
        });
        search_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });

    }

    private void loadLocations(){
        FetchLocationsTask ft = new FetchLocationsTask();
        ft.execute();
    }

    private void loadCities(){
        String[] cities = null;
        if(_supportedCities!= null){
            _citiesSpinner.setEnabled(true);
            //AZ
            if(_state.equals(_states[0])){
                cities = Arrays.copyOfRange(_supportedCities,0,3);
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


    private void handleSubmit(){
        String searchText = _editText.getText().toString();
        Log.v(classLogTag, "in handleSubmit: "+ searchText);

        if(validSubmission(searchText)){
            //Animate submit button
            ImageButton submitBtn = (ImageButton) findViewById(R.id.search_button);
            submitBtn.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate));
            //call indeed jobs api
            IndeedService service = CentsApplication.get_indeedRestAdapter().create(IndeedService.class);
            Map<String,String> queryMap = new HashMap<String,String>();
            //TODO Mask publisher key
            queryMap.put("publisher", "4507844392785871" );
            String location = (_city+"+"+_state).toLowerCase();
            //TODO create preference for users to set limit, radius, fromage, jt
            queryMap.put("l", location);
            queryMap.put("v","2");
            queryMap.put("limit", "25");
            queryMap.put("radius", "25");
            queryMap.put("fromage","30");
            queryMap.put("jt", "fulltime");
            queryMap.put("q", _occupation.toLowerCase());
            queryMap.put("userip", "1.2.3.4");
            queryMap.put("useragent","Mozilla/%2F4.0%28Firefox%29" );
            queryMap.put("format", "json");
            service.listResults(queryMap, new Callback<IndeedQueryResults>() {
                @Override
                public void success(IndeedQueryResults iqr, Response response) {
                    Log.v(classLogTag, "indeed search results: "+ iqr.getResults().size());
                    //TODO launch activity to create cards from search results
                    //TODO store results within application

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(classLogTag,error.getMessage());

                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Invalid Search", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean validSubmission(String searchText) {
        if((_city != null) && (!_city.equals("")) && (_state != null) && (!_state.equals("")) && (searchText != null) && (!searchText.equals(""))) {
            _occupation = searchText;
            Log.v(classLogTag, "validSubmission - city: "+ _city+" state: "+ _state+" occupation: "+_occupation );
            return true;
        }
        else{

            Log.v(classLogTag, "invalidSubmission");
            return false;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected class FetchLocationsTask extends AsyncTask<Void, Void, String[]>{

        private final String LOG_TAG = FetchLocationsTask.class.getSimpleName();
        private String[] states = null;

        @Override
        protected void onPostExecute(String[] strings) {
            if(strings!= null){
                //TODO return only the cities based on selected state
                Log.i(LOG_TAG, "onPostExecute - updating _supportedcities");
                _supportedCities = new String[strings.length];
                int index = 0;
                for(String s: strings){
                    //Check that location is not a state
                    //edge case city is named after state
                    if(!_supportedStatesHash.contains(s) ||  (s.equals("Washington") && index < 15) || (s.equals("New York") && index == 23)){
                        _supportedCities[index] = s;
                        index++;
                        Log.i(LOG_TAG,"Added: "+s+" To supportedCities");
                    }

                }
            }
        }

        @Override
        protected String[] doInBackground(Void... params) {
            String[] results = null;
            Gson gson = new Gson();
            Log.i(LOG_TAG, "doInBackground - Loading File");
            //load Json file
            try {
                Type tp = new TypeToken<Collection<Col>>(){}.getType();
                InputStream is = getApplicationContext().getAssets().open("col.json");
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = br.readLine())!= null){
                    sb.append(line);
                }
                String json = sb.toString();
                Log.i(LOG_TAG, json);
                //Convert to objects
                _cols = gson.fromJson(json, tp);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(_cols != null){
                Log.i(LOG_TAG, "doInBackground - Processing col.json");
                //pull locations add to result
                results = new String[_cols.size()];
                int i = 0;
                for(Col c: _cols){
                    results[i] = c.getLocation();
                    i++;
                }

            }

            return results;

        }
    }
}
