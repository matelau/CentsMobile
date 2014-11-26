package com.matelau.junior.centsproject.Views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koushikdutta.ion.Ion;
import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Models.JobInfo;
import com.matelau.junior.centsproject.R;

import java.util.List;

/**
 * Created by matelau on 11/24/14.
 */
public class JobListRecycleAdapter extends RecyclerView.Adapter<JobViewHolder> {

    private String LOG_TAG = JobListRecycleAdapter.class.getSimpleName();
    private List<JobInfo> _jInfo;
    private Context _context;

    public JobListRecycleAdapter(List<JobInfo> ji){
        _jInfo = ji;
    }

    @Override
    public void onBindViewHolder(JobViewHolder jobViewHolder, int i) {

        //get data
        JobInfo ji = _jInfo.get(i);
        //set view data
        Log.v(LOG_TAG, "On BindViewHolder setting JTitle: " + ji.jobTitle);
        jobViewHolder._jobTitle.setText(ji.jobTitle);
        jobViewHolder._jobCompany.setText(ji.jobCompany);

        if(ji.jobUrl != null){
            Ion.with(jobViewHolder._companyImg).load(ji.jobUrl);
        }
        else{
            jobViewHolder._companyImg.setImageResource(R.drawable.placeholder);
//            Ion.with(jobViewHolder._companyImg).load(R.drawable.placeholder);
        }

        if(CentsApplication.is_imgUrlsRdy()){
//            Toast.makeText(_context, "Img Urls Loaded", Toast.LENGTH_SHORT).show();
//            CentsApplication.get_imgUrls().get()
        }
//        if(CentsApplication.get_imgUrls() != null){ //&& CentsApplication.get_imgUrls().size() >= i ){
//            List<String> urls = CentsApplication.get_imgUrls();
//            JobListFragment.get_aq().id(jobViewHolder._companyImg.getId()).image(urls.get(i),true,true,50, R.drawable.placeholder);
//        }


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
}
