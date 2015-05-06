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

import com.matelau.junior.centsproject.Models.CentsAPIServices.UserService;
import com.matelau.junior.centsproject.R;
import com.matelau.junior.centsproject.Views.Profile.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    private final String LOG_TAG = AboutFragment.class.getSimpleName();
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_about, null, false);

        //setup profile card list
        ExpandableListView _aboutCats = (ExpandableListView) _rootLayout.findViewById(R.id.about_categories_list);
        prepareListData();
        ExpandableListAdapter listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild, false);
        if(CentsApplication.is_loggedIN()){
            updateCompleted("View About");
        }

        // setting list adapter
        _aboutCats.setAdapter(listAdapter);
        return _rootLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "resumed");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "destroyed");
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
//                Log.e(LOG_TAG, error.getMessage());

            }
        });
    }


    /**
     * Prepares Header views and child elements
     */
    private void prepareListData(){
        //get help strings
        String[] _aboutVals = getResources().getStringArray(R.array.about_elements);
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
