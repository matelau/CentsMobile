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
public class AboutFragment extends Fragment {
    private RelativeLayout _rootLayout;
    private ExpandableListView _aboutCats;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private String[] _aboutVals;


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_about, null, false);

        //setup profile card list
        _aboutCats = (ExpandableListView) _rootLayout.findViewById(R.id.about_categories_list);
        prepareListData();
        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        _aboutCats.setAdapter(listAdapter);
        return _rootLayout;
    }

    private void prepareListData(){
        //get help strings
        _aboutVals = getResources().getStringArray(R.array.about_elements);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Our Goals");
        listDataHeader.add("Where we get our data");
        listDataHeader.add("How we calculate Cents' ratings");
        listDataHeader.add("About us");


        // Adding child data
        for(int i = 0; i < _aboutVals.length; i++){
            List<String> helpInfo = new ArrayList<String>();
            helpInfo.add(_aboutVals[i]);
            listDataChild.put(listDataHeader.get(i), helpInfo);

        }

    }


}
