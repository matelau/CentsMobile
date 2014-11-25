package com.matelau.junior.centsproject.Views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matelau.junior.centsproject.Models.JobInfo;
import com.matelau.junior.centsproject.R;

import java.util.List;

/**
 * Created by matelau on 11/24/14.
 */
public class JobListRecycleAdapter extends RecyclerView.Adapter<JobViewHolder> {

    private String LOG_TAG = JobListRecycleAdapter.class.getSimpleName();
    private List<JobInfo> _jInfo;


    public JobListRecycleAdapter(List<JobInfo> ji){
        _jInfo = ji;
    }

    @Override
    public void onBindViewHolder(JobViewHolder jobViewHolder, int i) {

        //get data
        JobInfo ji = _jInfo.get(i);
        //set view data
        Log.v(LOG_TAG, "On BindViewHolder setting JTitle: "+ji.jobTitle);
        jobViewHolder._jobTitle.setText(ji.jobTitle);
        jobViewHolder._jobCompany.setText(ji.jobCompany);


    }


    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //create card view
        Log.v(LOG_TAG, "On CreateViewHolder");
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,viewGroup, false);
        return new JobViewHolder(itemView);
    }



    @Override
    public int getItemCount() {
        return _jInfo.size();
    }
}
