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


public class HelpFragment extends Fragment {
    private RelativeLayout _rootLayout;
    private ExpandableListView _profileCats;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private String[] helpVals;


    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_help, null, false);

        //setup profile card list
        _profileCats = (ExpandableListView) _rootLayout.findViewById(R.id.help_categories_list);
        prepareListData();
        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild, false);

        // setting list adapter
        _profileCats.setAdapter(listAdapter);
        return _rootLayout;
    }

    private void prepareListData(){
        //get help strings
        helpVals = getResources().getStringArray(R.array.help_elements);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("What type of searches can I make?");
        listDataHeader.add("How should I structure my searches?");
        listDataHeader.add("My search is redirecting me to examples. Why?");
        listDataHeader.add("Where is my career?");
        listDataHeader.add("Where is my city?");
        listDataHeader.add("Where is my university?");
        listDataHeader.add("Where is my major?");

        // Adding child data
        for(int i = 0; i < helpVals.length; i++){
            List<String> helpInfo = new ArrayList<String>();
            helpInfo.add(helpVals[i]);
            listDataChild.put(listDataHeader.get(i), helpInfo);

        }

    }
}
