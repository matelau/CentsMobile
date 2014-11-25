package com.matelau.junior.centsproject.Controllers;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matelau.junior.centsproject.Models.JobInfo;
import com.matelau.junior.centsproject.Models.Result;
import com.matelau.junior.centsproject.R;
import com.matelau.junior.centsproject.Views.JobListRecycleAdapter;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class JobListFragment extends Fragment {
    private RecyclerView _recyclerView;
    private LinearLayoutManager _recyclerLayoutMan;
    private String LOG_TAG = JobListFragment.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(LOG_TAG, "On CreateView");

        View rootView = inflater.inflate(R.layout.fragment_job_list, container, false);
        _recyclerView = (RecyclerView) rootView.findViewById(R.id.job_recycler_view);
        _recyclerView.setHasFixedSize(true);
        _recyclerLayoutMan = new LinearLayoutManager(getActivity());
        _recyclerLayoutMan.setOrientation(LinearLayoutManager.VERTICAL);
        _recyclerView.setLayoutManager(_recyclerLayoutMan);


        //Setadapter - preload data
        List<JobInfo> jl = new ArrayList<JobInfo>();
        List<Result> indeedData = CentsApplication.get_jobSearchResultList();
        for(int i = 0; i < indeedData.size(); i++)
        {
            Result currResult = indeedData.get(i);
            JobInfo ji = new JobInfo(currResult.getJobtitle(),currResult.getCompany(),currResult.getUrl());
            jl.add(ji);
        }



        _recyclerView.setAdapter(new JobListRecycleAdapter(jl));



        return rootView;

    }




}
