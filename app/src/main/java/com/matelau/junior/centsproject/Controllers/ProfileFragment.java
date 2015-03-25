package com.matelau.junior.centsproject.Controllers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.matelau.junior.centsproject.R;
import com.matelau.junior.centsproject.Views.Profile.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
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
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> accountInfo = new ArrayList<String>();
        //TODO pull info from api
        accountInfo.add("comingSoon");
        accountInfo.add("Name");
        accountInfo.add("Email");
        accountInfo.add("Location");


        List<String> recentSearches= new ArrayList<String>();
        //TODO pull recent from api
        recentSearches.add("comingSoon");
        recentSearches.add("u of u vs byu");
        recentSearches.add("West Valley City vs Oakland");


        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("comingSoon");


        listDataChild.put(listDataHeader.get(0), accountInfo); // Header, Child data
        listDataChild.put(listDataHeader.get(1), recentSearches);
        listDataChild.put(listDataHeader.get(2), comingSoon);

    }


}
