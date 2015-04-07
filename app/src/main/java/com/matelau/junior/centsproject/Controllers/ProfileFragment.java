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
import com.matelau.junior.centsproject.R;
import com.matelau.junior.centsproject.Views.Profile.ExpandableListAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_profile, null, false);
        //setup profile card list
        _profileCats = (ExpandableListView) _rootLayout.findViewById(R.id.profile_categories_list);
        prepareListData();
        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        _profileCats.setAdapter(listAdapter);
        return _rootLayout;
    }

    private void prepareListData(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Account Information");
        listDataHeader.add("Recent Searches");
        listDataHeader.add("Preferences");

        // Adding child data
        List<String> accountInfo = new ArrayList<String>();
        //TODO pull info from api
        accountInfo.add("User Details");
//        accountInfo.add("Name");
//        accountInfo.add("Email");
//        accountInfo.add("Location");


        List<String> recentSearches= loadUserSearches(); //new ArrayList<String>();
        //TODO pull recent from api
//        recentSearches.add("List of recent searches.");
//        recentSearches.add("u of u vs byu");
//        recentSearches.add("West Valley City vs Oakland");


        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("coming soon");


        listDataChild.put(listDataHeader.get(0), accountInfo); // Header, Child data
        listDataChild.put(listDataHeader.get(1), recentSearches);
        listDataChild.put(listDataHeader.get(2), comingSoon);

    }

    private List<String> loadUserSearches(){
        final ArrayList<String> searchHistory = new ArrayList<String>();
        //get UserID
        //load user id
        SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        int ID = settings.getInt("ID", 0);
        Log.d(LOG_TAG, "Loaded ID from Prefs: " + ID);
        UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
        service.getQueries(ID, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
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
                Gson gson = new Gson();
                String[] results = gson.fromJson(rsp, String[].class);
                //add upto last 10 results to list
                for(int i = results.length -1; i >= 0; i--){
                    if(searchHistory.size() < 10)
                        searchHistory.add(results[i]);
                    else{
                        break;
                    }
                }
                //update the list
                listDataChild.put(listDataHeader.get(1), searchHistory);

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());

            }
        });
        return searchHistory;
    }


}
