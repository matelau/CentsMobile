package com.matelau.junior.centsproject.Views.VisualizationFragments.Career;


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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Controllers.VisualizationPagerFragment;
import com.matelau.junior.centsproject.Models.CentsAPIServices.CareerService;
import com.matelau.junior.centsproject.Models.CentsAPIServices.RecordsService;
import com.matelau.junior.centsproject.Models.VizModels.Career;
import com.matelau.junior.centsproject.Models.VizModels.CareerQuery;
import com.matelau.junior.centsproject.Models.VizModels.CareerResponse;
import com.matelau.junior.centsproject.Models.VizModels.RecordQuery;
import com.matelau.junior.centsproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CareerSelectionDialogFragment extends DialogFragment{
    private String LOG_TAG = CareerSelectionDialogFragment.class.getSimpleName();

    private LinearLayout _rootLayout;
    private ArrayAdapter<String> _careerAdapter;
    private Spinner _careerSpinner1;
    private Spinner _careerSpinner2;
    private TextView _careerTextView1;
    private TextView _careerTextView2;
    private AutoCompleteTextView _autoComp1;
    private AutoCompleteTextView _autoComp2;
    private TextView _vs;
    private View _plusBtn;
    private Button _submit;
    private Button _cancel;
    private boolean isPlus = false;
    private TextView _instructions;

    private String[] _careers;
    private Career _career1;
    private Career _career2;
    private boolean _useAutocomplete;


    public CareerSelectionDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateDialog");

        SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        _useAutocomplete = settings.getBoolean("Autocomplete", true);
        Log.d(LOG_TAG, "Autocomplete value: "+_useAutocomplete);

        //clear old values
        if(CentsApplication.get_selectedVis()!= null && !CentsApplication.get_selectedVis().equals("Career Comparison")){
            CentsApplication.set_selectedVis(null);
            CentsApplication.set_cResponse(null);
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
        _instructions.setText("Select one or two Careers");
        _careerTextView1 = (TextView) _rootLayout.findViewById(R.id.stateTextView);
        _careerTextView1.setText("Career - 1");
        _careerTextView2 = (TextView) _rootLayout.findViewById(R.id.stateTextView2);
        _plusBtn = _rootLayout.findViewById(R.id.circle);
        _careerSpinner1 = (Spinner) _rootLayout.findViewById(R.id.state_spinner1);
        _careerSpinner2 = (Spinner) _rootLayout.findViewById(R.id.state_spinner2);
        _autoComp1 = (AutoCompleteTextView) _rootLayout.findViewById(R.id.ac_view1);
        _autoComp2 = (AutoCompleteTextView) _rootLayout.findViewById(R.id.ac_view2);
        loadCareersList();

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
                    _autoComp2.setVisibility(View.GONE);
                    _careerTextView2.setVisibility(View.GONE);
                    _careerSpinner2.setVisibility(View.GONE);

                }
            }
        });

        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_career1 != null ){
//                    //create query
                    CareerQuery query = new CareerQuery();
                    ArrayList<Career> careerList = new ArrayList<Career>();
                    careerList.add(_career1);
                    if(_career2!= null){
                        careerList.add(_career2);
                    }
                    query.setCareers(careerList);
                    query.setOperation("compare");
                    CareerService service = CentsApplication.get_centsRestAdapter().create(CareerService.class);
                    service.getCareerInfo(query, new Callback<CareerResponse>() {
                        @Override
                        public void success(CareerResponse careerResponse, Response response) {
                            Log.d(LOG_TAG, "successful response: ");

                            //set names until api returns career name
                            careerResponse.setCareer1(_career1.getName());
                            if(_career2 != null){
                                careerResponse.setCareer2(_career2.getName());
                            }
                            //set response
                            CentsApplication.set_cResponse(careerResponse);

                            //switch fragments
                            //get career view pager frag
                            switchToViewPager();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(LOG_TAG, error.getMessage());
                            Toast.makeText(getActivity(), "There was an error - Please try again", Toast.LENGTH_SHORT).show();
                            //todo remove this switch after michael fixes the api issues
                            //todo remove setting careerResponse - doing this for testing
//                            CareerResponse testResponse = new CareerResponse();
//                            testResponse.setCareer1("Software Engineer");
//                            testResponse.setCareer2("Music Teacher");
//                            ArrayList<Integer> vals = new ArrayList<Integer>();
//                            vals.add(88000);
//                            testResponse.setCareerSalary1(vals);
//                            ArrayList<Integer> vals2 = new ArrayList<Integer>();
//                            vals2.add(27250);
//                            testResponse.setCareerSalary2(vals2);
//                            ArrayList<Integer> vals3 = new ArrayList<Integer>();
//                            vals3.add(353200);
//                            testResponse.setCareerDemand1(vals3);
//                            ArrayList<Integer> vals4 = new ArrayList<Integer>();
//                            vals4.add(35000);
//                            testResponse.setCareerDemand2(vals4);
//                            ArrayList<Double> vals5 = new ArrayList<Double>();
//                            vals5.add(3.8);
//                            vals5.add(3.2);
//                            testResponse.setCareerUnemploy1(vals5);
//                            ArrayList<Double> vals6 = new ArrayList<Double>();
//                            vals6.add(8.1);
//                            vals6.add(8.5);
//                            testResponse.setCareerUnemploy2(vals6);
//                            ArrayList<Double> vals7 = new ArrayList<Double>();
//                            vals7.add(6.0);
//                            vals7.add(6.8);
//                            testResponse.setCareerUnemploy3(vals7);
//                            CentsApplication.set_cResponse(testResponse);

//                            switchToViewPager();
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

    private void loadCareersList(){
        if (CentsApplication.get_careers() != null){
            _careers = CentsApplication.get_careers();
            initSpinner1();
//            loadPreviousSearch();
        }
        else{
            RecordsService service = CentsApplication.get_centsRestAdapter().create(RecordsService.class);
            RecordQuery query = new RecordQuery();
            query.setOperation("get");
            ArrayList<String> tables = new ArrayList<String>();
            tables.add("careers");
            query.setTables(tables);
            service.getRecordsV2(new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    //get list
                    Log.d(LOG_TAG, "Received Careers List");
                    _careers = careersFromJson(response2);
                    //cache careers list
                    CentsApplication.set_careers(_careers);
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
     * Switch to vis
     */
    private void switchToViewPager(){
        CentsApplication.set_selectedVis("Career Comparison");
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
        ft.addToBackStack("career-intro");
        ft.commit();
        dismiss();
    }

    private void addPlusViews(){
        isPlus = false;
        _plusBtn.setBackground(getResources().getDrawable(R.drawable.minus));
        //show secondary selections
        _vs.setVisibility(View.VISIBLE);
        _careerTextView2.setVisibility(View.VISIBLE);
        _careerTextView2.setText("Career- 2");
        if(!_useAutocomplete){
            _careerSpinner2.setVisibility(View.VISIBLE);
            _careerSpinner2.setAdapter(_careerAdapter);
            _careerSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String career = _careers[position];
                    _career2 = new Career();
                    _career2.setName(career);
                    Log.d(LOG_TAG, "Selected Career2: " + career);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //do nothing
                }
            });
        }
        else{
            //hide spinner show autoComp
            Log.d(LOG_TAG, "Loading AutoText2");
            _autoComp2.setVisibility(View.VISIBLE);
            _autoComp2.requestFocus();
            _autoComp2.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.ac_dropdown_element, _careers));
            _autoComp2.setThreshold(1);
            _autoComp2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv = (TextView) view;
                    String career = tv.getText().toString();
                    _career2 = new Career();
                    _career2.setName(career);
//                    _career2.setOrder(2);
                    Log.d(LOG_TAG, "Selected2: "+ tv.getText().toString());
                }
            });
        }


    }

    private void initSpinner1(){
        if(!_useAutocomplete){
            _careerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, _careers);
            _careerSpinner1.setAdapter(_careerAdapter);
            _careerSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String career = _careers[position];
                    _career1 = new Career();
                    _career1.setName(career);
                    Log.d(LOG_TAG, "Selected career: "+ career);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else{
            //hide spinner show autoComp
            Log.d(LOG_TAG, "Loading AutoText");
            _careerSpinner1.setVisibility(View.GONE);
            _autoComp1.setVisibility(View.VISIBLE);
            _autoComp1.requestFocus();
            _autoComp1.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.ac_dropdown_element, _careers));
            _autoComp1.setThreshold(1);
            _autoComp1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv = (TextView) view;
                    String career = tv.getText().toString();
                    _career1 = new Career();
                    _career1.setName(career);
//                    _career1.setOrder(1);
                    Log.d(LOG_TAG, "Selected1: " + career);
                }
            });
            setPreviousSearchedAutoComp();
        }

    }


    private void setPreviousSearchedAutoComp(){
        //load last searched items
        CareerResponse cResponse = CentsApplication.get_cResponse();
        if(cResponse != null){
            String career1 = cResponse.getCareer1();
            String career2 = cResponse.getCareer2();
            _career1 = new Career();
            _career1.setName(career1);
            _autoComp1.setText(career1);
            if(career2 != null){
                //add secondary search elements
                addPlusViews();
                _career2 = new Career();
                _career2.setName(career2);
                _autoComp2.setText(career2);
            }

        }
    }
    private String[] careersFromJson(Response response){
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
