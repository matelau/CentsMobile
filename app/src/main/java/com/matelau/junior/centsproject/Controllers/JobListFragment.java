package com.matelau.junior.centsproject.Controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matelau.junior.centsproject.Models.JobInfo;
import com.matelau.junior.centsproject.R;
import com.matelau.junior.centsproject.Views.JobListRecycleAdapter;

import java.util.List;

/**

 */
public class JobListFragment extends Fragment {
    private RecyclerView _recyclerView;
    private LinearLayoutManager _recyclerLayoutMan;
    private JobListRecycleAdapter _recyclerAdapter;
    private List<JobInfo> _jl;
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


       _jl = CentsApplication.get_jobSearchResultList();
        //Check if list is empty and supply place holder values to adapter.
        if(_jl == null || _jl.size() == 0){

            JobInfo ji = new JobInfo("Please Try Again", "Search Returned No Results", null);
            //try again image?
            ji.jobLogoUrl ="http://www.okiwoki.com/images/produits/jeux-video/try-again-pulls-noir-h-l_1.jpg";
//            jl.clear();
            _jl.add(ji);

        }

        _recyclerAdapter = new JobListRecycleAdapter(_jl, getActivity());
        _recyclerView.setAdapter(_recyclerAdapter);

        return rootView;

    }








}
