package com.matelau.junior.centsproject.Controllers;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matelau.junior.centsproject.R;

/**

 */
public class JobListFragment extends Fragment {
    private RecyclerView _recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_job_list, container, false);
        _recyclerView = (RecyclerView) rootView.findViewById(R.id.job_recycler_view);
        //TODO Setadapter

        //http://api.glassdoor.com/api/api.htm?t.p=27350&t.k=irqFFDe0Rvo&userip=0.0.0.0&useragent=&format=json&v=1&action=employers&q=GM
        //TODO launch call to glassdoor to get company reviews + metadata based on indeed job results


        return rootView;

    }



}
