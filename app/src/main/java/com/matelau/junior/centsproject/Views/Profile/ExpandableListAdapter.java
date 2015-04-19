package com.matelau.junior.centsproject.Views.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Controllers.DisambiguationDialogFragment;
import com.matelau.junior.centsproject.Controllers.ServiceDownDialogFragment;
import com.matelau.junior.centsproject.Controllers.VisualizationPagerFragment;
import com.matelau.junior.centsproject.Models.CentsAPIServices.CareerService;
import com.matelau.junior.centsproject.Models.CentsAPIServices.MajorService;
import com.matelau.junior.centsproject.Models.CentsAPIServices.QueryService;
import com.matelau.junior.centsproject.Models.CentsAPIServices.SchoolService;
import com.matelau.junior.centsproject.Models.CentsAPIServices.UserService;
import com.matelau.junior.centsproject.Models.UserModels.Query;
import com.matelau.junior.centsproject.Models.VizModels.CareerResponse;
import com.matelau.junior.centsproject.Models.VizModels.ColiResponse;
import com.matelau.junior.centsproject.Models.VizModels.Major;
import com.matelau.junior.centsproject.Models.VizModels.MajorResponse;
import com.matelau.junior.centsproject.Models.VizModels.SchoolResponse;
import com.matelau.junior.centsproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by matelau on 3/25/15.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private String LOG_TAG = ExpandableListAdapter.class.getSimpleName();
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private String[] _ratings;
    private boolean _isProfile = true;
    private FragmentManager _fm;
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, boolean isProfile) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._isProfile = isProfile;
    }

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, boolean isProfile, FragmentManager fm) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._isProfile = isProfile;
        this._fm = fm;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }


    private void handleSubmit(String searchText){
        QueryService service = CentsApplication.get_queryParsingRestAdapter().create(QueryService.class);
        service.results(searchText, new Callback<Response>() {
            @Override
            public void success(Response response1, Response response) {
                if (CentsApplication.isDebug())
                    Toast.makeText(_context, response.toString(), Toast.LENGTH_SHORT);

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
                Map<String, String> map = new HashMap<String, String>();
                map = (Map<String, String>) gson.fromJson(rsp, map.getClass());
                //get type
                String type = map.get("query_type");
                if(type == null){
                    Toast.makeText(_context, "We did not understand your query... here are some examples", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(_context, "Ambiguous results: "+elements.size(), Toast.LENGTH_SHORT).show();
//                            showLoading();
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
                            Toast.makeText(_context, "Ambiguous results: "+elements.size(), Toast.LENGTH_SHORT).show();
//                            showLoading();
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
                            Toast.makeText(_context, "Ambiguous results: "+elements.size(), Toast.LENGTH_SHORT).show();
//                            showLoading();
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
                            Toast.makeText(_context, "Ambiguous results: "+majors.size(), Toast.LENGTH_SHORT).show();
//                            showLoading();
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
                    switchToVizPager();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Query Service Error: " + error.getMessage());
                showServiceDownNotification();
                CentsApplication.set_selectedVis("Examples");
                switchToVizPager();
            }
        });


    }

    /**
     * Loads the dialog ontop of current view
     */
    private void showDisambiguation(Bundle args){
        FragmentManager fm = _fm;
        DisambiguationDialogFragment choiceDialog = new DisambiguationDialogFragment();
        choiceDialog.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        choiceDialog.setArguments(args);
        choiceDialog.show(fm, "tag");
    }


    private void switchToVizPager(){
        FragmentTransaction ft = ((FragmentActivity) _context).getSupportFragmentManager().beginTransaction();   //_context.getApplicationContext().get.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
        ft.addToBackStack("main-search");
        ft.commit();
    }

    private void showServiceDownNotification(){
        FragmentManager fm = ((FragmentActivity) _context).getSupportFragmentManager();
        ServiceDownDialogFragment confirmation = new ServiceDownDialogFragment();
        confirmation.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        confirmation.show(fm, "tag");
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        //Get Text
        String childText = (String) getChild(groupPosition, childPosition);
        //Get User ID
        SharedPreferences settings = _context.getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        final int userId = settings.getInt("ID", 0);
        //Recycle Views
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_cat_child, null);
        }
        //By default hide spinner and progress bar
        Spinner spinnerRating = (Spinner) convertView.findViewById(R.id.spinner_rating);
        spinnerRating.setVisibility(View.GONE);
        ProgressBar pb = (ProgressBar) convertView.findViewById(R.id.user_progress);
        pb.setVisibility(View.GONE);

        //Display ratings section 2,3,4
        if(groupPosition >= 2 && groupPosition < 5 && _isProfile){
            //extract rating from string
            final int ratingVal = Integer.parseInt(childText.substring(childText.indexOf(':') + 1, childText.length()).trim());
            //extract rating element
            final String element = childText.substring(0, childText.indexOf(':')).trim();

            //Setup elements used for updating ratings
            final HashMap<String, Integer> user = new HashMap<String, Integer>();
            user.put("user", userId);
            childText = element;
            _ratings = _context.getResources().getStringArray(R.array.rating_values);
            ArrayAdapter<String> ratingAdapter = new ArrayAdapter<String>(_context, android.R.layout.simple_spinner_dropdown_item, _ratings);
            spinnerRating.setAdapter(ratingAdapter);
            //remove one from rating to fit in range 0-4
            spinnerRating.setSelection(ratingVal-1);
            spinnerRating.setVisibility(View.VISIBLE);
            if(groupPosition == 2){
                //major rating
                final String level = childText.substring(childText.indexOf("(")+1, childText.length()-1).trim();
                final String major = childText.substring(0, childText.indexOf("(")).trim();
                Log.d(LOG_TAG, "level: "+level+", Major: "+ major);
                spinnerRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                        MajorService service = CentsApplication.get_centsRestAdapter().create(MajorService.class);
                        service.rateMajor(level, major, position, user, new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {
                                Log.d(LOG_TAG, "Success");
                                //update list
                                updateRatings( 2,  element, position);
                            }
                            @Override
                            public void failure(RetrofitError error) {
                                Log.e(LOG_TAG, error.getMessage());
                                Toast.makeText(_context, "Error - Please try again later", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
            else if(groupPosition == 3 && _isProfile){
                //update university ratings
                spinnerRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                        SchoolService service = CentsApplication.get_centsRestAdapter().create(SchoolService.class);
                        service.rateSchool(element, position, user, new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {
                                Log.d(LOG_TAG, "Success");
                                 //update list
                                updateRatings( 3,  element, position);

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e(LOG_TAG, error.getMessage());
                                Toast.makeText(_context, "Error - Please try again later", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //do nothing
                    }
                });
            }
            else{
                //update career rating
                spinnerRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,final int position, long id) {

                        CareerService service = CentsApplication.get_centsRestAdapter().create(CareerService.class);
                        service.rateCareer(element, position, user, new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {
                                Log.d(LOG_TAG, "Success");
                                //update list
                                updateRatings( 4,  element, position);

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e(LOG_TAG, error.getMessage());
                                Toast.makeText(_context, "Error - Please try again later", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        }
        //completed sections
        if(groupPosition == 6 && _isProfile){
            //show progressbar
            if(childText.contains("Completed :")){
                int numberOfComps = Integer.parseInt(childText.substring(childText.indexOf(":")+1,childText.indexOf("/")).trim());
                float progress = (numberOfComps /13f)*100f;
                pb.setProgress((int) progress);
                pb.setVisibility(View.VISIBLE);
                childText = numberOfComps+"/13";
            }


        }

        //preferences
        if(groupPosition == 5 && _isProfile){
            //hide default layout
            convertView.findViewById(R.id.profile_data).setVisibility(View.GONE);
            convertView.findViewById(R.id.profile_preferences).setVisibility(View.VISIBLE);
            Switch acSwitch = (Switch) convertView.findViewById(R.id.auto_complete_switch);
            boolean checked = settings.getBoolean("Autocomplete", true);
            acSwitch.setChecked(checked);
            acSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharedPreferences settings = _context.getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
                    settings.edit().
                        putBoolean("Autocomplete", isChecked).
                        apply();
                    //update db
                    UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
                    HashMap<String,String> values = new HashMap<String, String>();
                    values.put("prefers_autocomplete", ""+isChecked);
                    HashMap<String, HashMap<String, String>> fields = new HashMap<String, HashMap<String,String>>();
                    fields.put("fields", values);
                    service.updateFields(userId, fields, new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            Log.d(LOG_TAG, "update prefers autocomplete success");

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(LOG_TAG, error.getMessage());
                        }
                    });
                }
            });

        }
        else{
            //set text elements
            convertView.findViewById(R.id.profile_data).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.profile_preferences).setVisibility(View.GONE);
            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);
            if(childText.length() > 30 && _isProfile){
                childText = childText.substring(0,28)+"...";
            }
            txtListChild.setText(childText);
            //conduct search if user clicks previous query
            if(groupPosition == 1 && _isProfile){
                txtListChild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tv = (TextView) v;
                        String query = tv.getText().toString();
                        handleSubmit(query);
                        storeQuery(query);
                        Toast.makeText(_context, "Searching for: "+query, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                txtListChild.setOnClickListener(null);
            }
        }



        return convertView;
    }

    private void storeQuery(String searchText){
        //create query model
        Query q = new Query();
        q.setUrl(searchText);
        //load user id
        SharedPreferences settings = _context.getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
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

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_cat_header, null);
        }
        if (isExpanded)
            convertView.setPadding(0, 0, 0, 0);
        else
            convertView.setPadding(0, 0, 0, 10);

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }


    /**
     * Updates local rating model
     * @param groupPosition
     * @param element
     * @param rating
     */
    private void updateRatings(int groupPosition, String element, int rating){
        String header = _listDataHeader.get(groupPosition);
        List<String> ratingsList = _listDataChild.get(header);
        String newRating = "";
        int index = -1;
        for(int i = 0; i < ratingsList.size(); i++){
            if(ratingsList.get(i).contains(element)){
                newRating = element+" :"+rating;
                index = i;
                break;
            }
        }
        if(!newRating.equals("")){
            ratingsList.remove(index);
            ratingsList.add(index, newRating);
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
