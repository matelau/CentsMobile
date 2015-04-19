package com.matelau.junior.centsproject.Controllers;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.matelau.junior.centsproject.Models.CentsAPIServices.UserService;
import com.matelau.junior.centsproject.Models.UserModels.CareerRating;
import com.matelau.junior.centsproject.Models.UserModels.DegreeRating;
import com.matelau.junior.centsproject.Models.UserModels.SchoolRating;
import com.matelau.junior.centsproject.Models.UserModels.UserResponse;
import com.matelau.junior.centsproject.R;
import com.matelau.junior.centsproject.Views.Profile.ExpandableListAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private String LOG_TAG = ProfileFragment.class.getSimpleName();
    private RelativeLayout _rootLayout;
    private ExpandableListView _profileCats;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private int _id;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_profile, null, false);
        //load user id
        SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        _id = settings.getInt("ID", 0);
        updateCompleted("Use Mobile");
        Log.d(LOG_TAG, "Loaded ID from Prefs: " + _id);
        boolean checked = settings.getBoolean("Autocomplete", true);
        Log.d(LOG_TAG, "Checked value: " + checked);

        //setup profile card list
        _profileCats = (ExpandableListView) _rootLayout.findViewById(R.id.profile_categories_list);

        prepareListData();
        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild, true, getActivity().getSupportFragmentManager());

        // setting list adapter
        _profileCats.setAdapter(listAdapter);
        return _rootLayout;
    }

    /**
     * update the users completed section
     */
    private void updateCompleted(String completed){
        UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
        HashMap<String,String> useMobile = new HashMap<String, String>();
        useMobile.put("section", completed);
        service.updateCompletedData(_id, useMobile, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());

            }
        });
    }

    private void prepareListData(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        // Adding child data
        listDataHeader.add("Account Information");
        listDataHeader.add("Recent Searches");
        listDataHeader.add("Major Ratings");
        listDataHeader.add("University Ratings");
        listDataHeader.add("Career Ratings");
        listDataHeader.add("Preferences");
        listDataHeader.add("Progress");
        listDataHeader.add("To-do");
        loadProfileData();
        loadQueryData();
        loadRatingData();
        loadCompletedData();
        listDataChild.put(listDataHeader.get(0), new ArrayList<String>()); // Header, Child data
        listDataChild.put(listDataHeader.get(1), new ArrayList<String>());
        listDataChild.put(listDataHeader.get(2), new ArrayList<String>());
        listDataChild.put(listDataHeader.get(3), new ArrayList<String>());
        listDataChild.put(listDataHeader.get(4), new ArrayList<String>());
        //need to add a dummy value to list in order for list to have a childview
        ArrayList<String> dmbVal = new ArrayList<String>();
        dmbVal.add("null");
        listDataChild.put(listDataHeader.get(5), dmbVal);
        String[] sections = getActivity().getResources().getStringArray(R.array.sections);
        final ArrayList<String> allSections =  new ArrayList<String>(Arrays.asList(sections));
        //add dummy value to show progress bar
        ArrayList<String> lSections =  new ArrayList<String>(); //
        lSections.add("Completed : 0/13");
        listDataChild.put(listDataHeader.get(6), lSections);
        listDataChild.put(listDataHeader.get(7),allSections);

    }

    private void loadCompletedData(){
        UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
        service.getCompletedData(_id, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String[] completed = translateResponseToArray(response);
                int comps = completed.length;
                ArrayList<String> lSections =  new ArrayList<String>();
                lSections.add("Completed : " + comps + "/13");
                listDataChild.put(listDataHeader.get(6),lSections);
                ArrayList<String> notCompleted = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.sections)));
                for(String s: completed){
                    notCompleted.remove(s);
                }
                listDataChild.put(listDataHeader.get(7),notCompleted);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());

            }
        });
    }

    private void loadQueryData(){
        UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
        service.getQueries(_id, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String[] queries = translateResponseToArray(response);
                ArrayList<String> qs = new ArrayList<String>();
                for (String s : queries) {
                    if(qs.size() < 11)
                        qs.add(s);
                    else
                        break;
                }
                listDataChild.put(listDataHeader.get(1), qs);

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());

            }
        });

    }

    private void loadRatingData(){
        UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
        service.getRatingsData(_id, new Callback<UserResponse>() {
            @Override
            public void success(UserResponse userResponse, Response response) {

                //ratings
                List<CareerRating> cRatings = userResponse.getCareerRatings();
                List<String> cRat = new ArrayList<String>();
                for(CareerRating c: cRatings){
                    String val = c.getName()+": "+c.getRating();
                    cRat.add(val);
                }

                List<DegreeRating> dRatings = userResponse.getDegreeRatings();
                ArrayList<String> dRat = new ArrayList<String>();
                for(DegreeRating d : dRatings){
                    String val = d.getName()+"("+d.getLevel()+") : "+d.getRating();
                    dRat.add(val);
                }

                List<SchoolRating> sRatings = userResponse.getSchoolRatings();
                List<String> sRat = new ArrayList<String>();
                for(SchoolRating s : sRatings){
                    String val = s.getName()+": "+s.getRating();
                    sRat.add(val);
                }
                //major
                listDataChild.put(listDataHeader.get(2), dRat);
                //university
                listDataChild.put(listDataHeader.get(3), sRat);
                //Career
                listDataChild.put(listDataHeader.get(4), cRat);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());

            }
        });


    }

    private void loadProfileData(){
        UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
        service.getProfileData(_id, new Callback<UserResponse>() {
            @Override
            public void success(UserResponse userResponse, Response response) {
                //account info
                List<String> accountInfo = new ArrayList<String>();
                accountInfo.add(0, "Email: " + userResponse.getEmail());
                accountInfo.add(1, "First Name: " + userResponse.getFirstName());
                accountInfo.add(2, "Last Name: " + userResponse.getLastName());
                accountInfo.add(3, "Email Type: " + userResponse.getEmailType());
                listDataChild.put(listDataHeader.get(0), accountInfo);
                //set switch according to stored value
                if(userResponse.getPrefersAutocomplete() != null)
                {
                    //update stored value
                    SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
                    settings.edit().putBoolean("Autocomplete", userResponse.getPrefersAutocomplete()).apply();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());

            }
        });

    }

    private String[] translateResponseToArray(Response response){
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
