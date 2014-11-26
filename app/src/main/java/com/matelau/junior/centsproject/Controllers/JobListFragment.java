package com.matelau.junior.centsproject.Controllers;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koushikdutta.ion.Ion;
import com.matelau.junior.centsproject.Models.JobInfo;
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
    private ArrayList<String> _jiToLogo;
    private Ion _ion;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(LOG_TAG, "On CreateView");
        _jiToLogo = new ArrayList<String>();
        View rootView = inflater.inflate(R.layout.fragment_job_list, container, false);
        _recyclerView = (RecyclerView) rootView.findViewById(R.id.job_recycler_view);
        _recyclerView.setHasFixedSize(true);
        _recyclerLayoutMan = new LinearLayoutManager(getActivity());
        _recyclerLayoutMan.setOrientation(LinearLayoutManager.VERTICAL);
        _recyclerView.setLayoutManager(_recyclerLayoutMan);


        List<JobInfo> jl = CentsApplication.get_jobSearchResultList();
        _recyclerView.setAdapter(new JobListRecycleAdapter(jl));

        _recyclerView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeLeft() {
                //launch col activity
                Intent costLivingIntent = new Intent(getActivity(),CostOfLivingActivity.class);
                startActivity(costLivingIntent);
            }

            @Override
            public void onSwipeRight() {
                Intent mainIntent = new Intent(getActivity(),MainActivity.class);
                startActivity(mainIntent);
            }
        });






        return rootView;

    }




}
