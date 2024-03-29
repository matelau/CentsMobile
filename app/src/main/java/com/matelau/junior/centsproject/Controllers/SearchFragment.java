package com.matelau.junior.centsproject.Controllers;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.matelau.junior.centsproject.Models.CentsAPIServices.QueryService;
import com.matelau.junior.centsproject.Models.CentsAPIServices.UserService;
import com.matelau.junior.centsproject.Models.UserModels.Query;
import com.matelau.junior.centsproject.Models.VizModels.CareerResponse;
import com.matelau.junior.centsproject.Models.VizModels.ColiResponse;
import com.matelau.junior.centsproject.Models.VizModels.Major;
import com.matelau.junior.centsproject.Models.VizModels.MajorResponse;
import com.matelau.junior.centsproject.Models.VizModels.SchoolResponse;
import com.matelau.junior.centsproject.Models.VizModels.SpendingQPResponse;
import com.matelau.junior.centsproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private String LOG_TAG = SearchFragment.class.getSimpleName();
    private EditText _editText;
    private String _query;
    private RelativeLayout _rootLayout;
    private ImageButton _submitBtn;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_search, container, false);
        SliderLayout _slider = (SliderLayout) _rootLayout.findViewById(R.id.slider);

        if(CentsApplication.isDebug()){
//            only load ad if not in debug
        }
        else{
            AdView mAdView = (AdView) _rootLayout.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        ImageButton search_submit = (ImageButton) _rootLayout.findViewById(R.id.search_button);
        _editText = (EditText) _rootLayout.findViewById(R.id.editText1);
        _editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    handleSubmit();

                    return true;
                }
                return false;
            }
        });
        if(_query != null){
            _editText.setText(_query);
        }
        search_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });
        //Setup Slider
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("City Comparison",R.drawable.city);
        file_maps.put("Plan Spending",R.drawable.spend);
        file_maps.put("College Comparison", R.drawable.school);
        file_maps.put("Career Comparison", R.drawable.career);
        file_maps.put("Major Comparison", R.drawable.major);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView baseSliderView) {
                            String type = baseSliderView.getDescription();
                            Random r = new Random();
                            int index = 0;
                            switch (type) {
                                case "City Comparison":
                                    String[] popLocationQueries = {"Dallas, TX vs Madison, WI","Fort Lauderdale vs Houston","slc vs sf","Boston, MA vs Detroit, MI","Omaha, NE vs NYC"};
                                    index = Math.abs(r.nextInt() % popLocationQueries.length);
                                    _editText.setText(popLocationQueries[index]);
                                    break;
                                case "Plan Spending":
                                    String[] popSpending = { "What can I afford with a salary of 35,000?", "What can I afford as a physical therapist (PT)?"};
                                    index = Math.abs(r.nextInt() % popSpending.length);
                                    _editText.setText(popSpending[index]);
                                    break;
                                case "College Comparison":
                                    String[] popSchools = {"LSU vs Bama", "U of U vs BYU", "Harvard", "Princeton vs Yale", "UCLA vs Stanford"};
                                    index = Math.abs(r.nextInt() % popSchools.length);
                                    _editText.setText(popSchools[index]);
                                    break;
                                case "Career Comparison":
                                    String[] popCareers = {"Software Developer vs Account Manager", "Registered Nurse", "Director of Operations", "Anesthesiologist vs Surgeon", "Sales Associate"};
                                    index = Math.abs(r.nextInt() % popCareers.length);
                                    _editText.setText(popCareers[index]);
                                    break;
                                default:
                                    //major comp
                                    String[] popMajors = {"Computer Science vs Civil Engineering", "Art History", "Business Management Bachelors", "Law", "Computer Science Masters"};
                                    index = Math.abs(r.nextInt() % popMajors.length);
                                    _editText.setText(popMajors[index]);
                                    break;

                            }
                        }
                    });

            //add your extra information
            textSliderView.getBundle()
                    .putString("extra", name);

            _slider.addSlider(textSliderView);
        }
        _slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            _slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            _slider.setCustomAnimation(new DescriptionAnimation());
        _slider.setDuration(4000);

        return _rootLayout;
    }

    /**
     * Handles Search submissions
     */
    private void handleSubmit() {
        String searchText = _editText.getText().toString();
        Log.v(LOG_TAG, "in handleSubmit: " + searchText);
        //hide keyboard after submit
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_editText.getWindowToken(), 0);
        if(CentsApplication.is_loggedIN()){
            updateCompleted("Use Main Search");
        }
        if(searchText.trim().equals("")){
            Toast.makeText(getActivity(), "You must enter a search.", Toast.LENGTH_SHORT).show();
        }
        else{
            //post to Query Parsing Service and handle response
            _submitBtn = (ImageButton) _rootLayout.findViewById(R.id.search_button);
            _submitBtn.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate));
            if(CentsApplication.isDebug())
                Toast.makeText(getActivity(), "Search for:" + searchText, Toast.LENGTH_SHORT).show();
            //store searchText via api
            if(CentsApplication.is_loggedIN()){
                storeQuery(searchText);
            }
            QueryService service = CentsApplication.get_queryParsingRestAdapter().create(QueryService.class);
            service.results(searchText, new Callback<Response>() {
                @Override
                public void success(Response response1, Response response) {
                    if(CentsApplication.isDebug())
                        Toast.makeText(getActivity(),response.toString(), Toast.LENGTH_SHORT);

                    _query =  _editText.getText().toString();
                    _submitBtn.clearAnimation();
                    //Process Response and route accordingly
                    //Try to get response body
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
                    //Build Map
                    Gson gson = new Gson();
                    Map<String,String> map=new HashMap<String,String>();
                    map=(Map<String,String>) gson.fromJson(rsp, map.getClass());
                    //get type
                    String type = map.get("query_type");
                    if(type == null || type.equals("loan")){
                        Toast.makeText(getActivity(), "We did not understand your query... here are some examples", Toast.LENGTH_SHORT).show();
                        CentsApplication.set_selectedVis("Examples");
                        switchToVizPager();
                    }
                    //route properly
                    else if(type.equals("city")){
                        //create coli obj and launch coli viz
                        ColiResponse colResponse = gson.fromJson(rsp, ColiResponse.class);
                        List<ColiResponse.Element> elements = colResponse.getElements();
                        CentsApplication.set_colResponse(colResponse);
                        if(elements.size() <= 2){
                            CentsApplication.set_selectedVis("COL Comparison");
                            switchToVizPager();
                        }
                        else{
                            //handle disambiguations
                            if(CentsApplication.isDebug())
                                Toast.makeText(getActivity(), "Ambiguous results: "+elements.size(), Toast.LENGTH_SHORT).show();
                            Bundle args = new Bundle();
                            args.putInt("type", 2);
                            ArrayList<String> ambigSearchResults = new ArrayList<String>();
                            for(ColiResponse.Element e: elements){
                                ambigSearchResults.add(e.getName());
                            }
                            args.putStringArrayList("elements", ambigSearchResults);
                            showDisambiguation(args);
                        }

                    }
                    else if(type.equals("school")){
                        //create school obj and launch viz
                        SchoolResponse sResponse = gson.fromJson(rsp, SchoolResponse.class);
                        List<SchoolResponse.Element> elements = sResponse.getElements();
                        CentsApplication.set_sApiResponse(sResponse);
                        if(elements.size() <= 2){
                            CentsApplication.set_selectedVis("College Comparison");
                            switchToVizPager();
                        }
                        else{
                            //handle disambiguations
                            if(CentsApplication.isDebug())
                                Toast.makeText(getActivity(), "Ambiguous results: "+elements.size(), Toast.LENGTH_SHORT).show();
                            Bundle args = new Bundle();
                            args.putInt("type", 3);
                            ArrayList<String> ambigSearchResults = new ArrayList<String>();
                            for(SchoolResponse.Element e: elements){
                                ambigSearchResults.add(e.getName());
                            }
                            args.putStringArrayList("elements", ambigSearchResults);
                            showDisambiguation(args);

                        }

                    }
                    else if(type.equals("career")){
                        //create career obj and launch viz
                        CareerResponse cResponse = gson.fromJson(rsp, CareerResponse.class);
                        List<CareerResponse.Element> elements = cResponse.getElements();
                        CentsApplication.set_cResponse(cResponse);
                        if(elements.size() <= 2){
                            CentsApplication.set_selectedVis("Career Comparison");
                            switchToVizPager();
                        }
                        else{
                            //handle disambiguations
                            if(CentsApplication.isDebug())
                                Toast.makeText(getActivity(), "Ambiguous results: "+elements.size(), Toast.LENGTH_SHORT).show();
                            Bundle args = new Bundle();
                            args.putInt("type", 1);
                            ArrayList<String> ambigSearchResults = new ArrayList<String>();
                            for(CareerResponse.Element e: elements){
                                ambigSearchResults.add(e.getName());
                            }
                            args.putStringArrayList("elements", ambigSearchResults);
                            showDisambiguation(args);
                        }

                    }
                    else if(type.equals("major")){
                        //create major obj and launch viz
                        MajorResponse mResponse = gson.fromJson(rsp, MajorResponse.class);
                        List<MajorResponse.Element> majors = mResponse.getElements();
                        //get first two results update names
                        CentsApplication.set_mResponse(mResponse);
                        CentsApplication.set_selectedVis("Major Comparison");
                        if(majors.size() == 2){
                            Major major1 = new Major();
                            Major major2 = new Major();
                            major1.setName(majors.get(0).getName());
                            major2.setName(majors.get(1).getName());
                            CentsApplication.set_major1(major1);
                            CentsApplication.set_major2(major2);
                            switchToVizPager();
                        }
                        else{
                            Major major1 = new Major();
                            major1.setName(majors.get(0).getName());
                            CentsApplication.set_major1(major1);
                            switchToVizPager();
                        }
                        //handle disambiguations
                        if(majors.size() > 2){
                            if(CentsApplication.isDebug())
                                    Toast.makeText(getActivity(), "Ambiguous results: "+majors.size(), Toast.LENGTH_SHORT).show();
                            Bundle args = new Bundle();
                            args.putInt("type", 0);
                            List<MajorResponse.Element> elements = mResponse.getElements();
                            ArrayList<String> ambigSearchResults = new ArrayList<String>();
                            for(MajorResponse.Element e: elements){
                                ambigSearchResults.add(e.getName());
                            }
                            args.putStringArrayList("elements", ambigSearchResults);

                            showDisambiguation(args);
                        }

                    }
                    else if(type.equals("spending")){
                        //goto spending breakdown
                        CentsApplication.set_selectedVis("Spending Breakdown");
                        if(map.containsKey("income")){
                            CentsApplication.set_incomeFromQP(true);
                            SpendingQPResponse sr = gson.fromJson(rsp, SpendingQPResponse.class);
                            Double salary = sr.getIncome();
                            SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
                            settings.edit().putString("salary", salary.intValue()+"").apply();
                            CentsApplication.set_occupationSalary(salary.intValue()+"");
                        }
                        else{
                            CentsApplication.set_occupationSalary(null);
                        }
                        switchToVizPager();
                    }
                    else{
                        //goto spending breakdown
                        CentsApplication.set_selectedVis("Examples");
                        switchToVizPager();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(LOG_TAG, "Query Service Error: "+error.getMessage());
                    showServiceDownNotification();
                    CentsApplication.set_selectedVis("Examples");
                    switchToVizPager();
                    _submitBtn.clearAnimation();
                }
            });
        }
    }

    /**
     * update the users completed section
     */
    private void updateCompleted(String completed){
        SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        int id = settings.getInt("ID", 0);
        UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
        HashMap<String,String> completedTask = new HashMap<String, String>();
        completedTask.put("section", completed);
        service.updateCompletedData(id, completedTask, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());
            }
        });
    }

    /**
     * stores the last query to the db
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
     * Switches to appropriate views
     */
    private void switchToVizPager(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
        ft.addToBackStack("main-search");
        ft.commit();
    }


    /**
     * Loads the dialog ontop of current view
     */
    private void showServiceDownNotification(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ServiceDownDialogFragment confirmation = new ServiceDownDialogFragment();
        confirmation.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        confirmation.show(fm, "tag");
    }


    /**
     * Loads the dialog ontop of current view
     */
    private void showDisambiguation(Bundle args){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DisambiguationDialogFragment choiceDialog = new DisambiguationDialogFragment();
        choiceDialog.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        choiceDialog.setArguments(args);
        choiceDialog.show(fm, "tag");
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
