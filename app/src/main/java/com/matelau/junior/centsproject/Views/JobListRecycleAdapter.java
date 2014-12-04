package com.matelau.junior.centsproject.Views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matelau.junior.centsproject.Controllers.JobDetailActivity;
import com.matelau.junior.centsproject.Models.JobInfo;
import com.matelau.junior.centsproject.R;

import java.util.List;

/**
 * Created by matelau on 11/24/14.
 */
public class JobListRecycleAdapter extends RecyclerView.Adapter<JobListRecycleAdapter.JobViewHolder> {

    private String LOG_TAG = JobListRecycleAdapter.class.getSimpleName();
    private List<JobInfo> _jInfo;
    private Context _context;

    public JobListRecycleAdapter(List<JobInfo> ji, Context context){
        _jInfo = ji;
        _context = context;
    }

    @Override
    public void onBindViewHolder(JobViewHolder jobViewHolder, int i) {

        //get data
        JobInfo ji = _jInfo.get(i);
        //set view data
        Log.v(LOG_TAG, "On BindViewHolder setting JTitle: " + ji.jobTitle);
        jobViewHolder._jobTitle.setText(ji.jobTitle);
        jobViewHolder._jobCompany.setText(ji.jobCompany);
        jobViewHolder._jobUrl = ji.url;

    }




    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //create card view
        Log.v(LOG_TAG, "On CreateViewHolder");
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,viewGroup, false);
        _context = viewGroup.getContext();
        return new JobViewHolder(itemView);
    }



    @Override
    public int getItemCount() {
        return _jInfo.size();
    }

    public class JobViewHolder extends RecyclerView.ViewHolder {

        private String LOG_TAG = JobViewHolder.class.getSimpleName();

        protected TextView _jobCompany;
        protected TextView _jobTitle;
        protected String _jobUrl;


        public JobViewHolder(View itemView) {
            super(itemView);
            Log.v(LOG_TAG, "Constructor");
            //Pull Portions of CardView
            _jobCompany = (TextView) itemView.findViewById(R.id.job_company);
            _jobTitle = (TextView) itemView.findViewById(R.id.job_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence company = _jobCompany.getText();
                    CharSequence title =  _jobTitle.getText();
                    Intent jobDetailIntent = new Intent(_context, JobDetailActivity.class);
                    jobDetailIntent.putExtra("Company", company);
                    jobDetailIntent.putExtra("Title", title);
                    jobDetailIntent.putExtra("url", _jobUrl);
                    _context.startActivity(jobDetailIntent);
                }
            });
        }

    }

}


