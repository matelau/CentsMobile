package com.matelau.junior.centsproject.Views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matelau.junior.centsproject.R;

/**
 * Created by matelau on 11/24/14.
 */
public class JobViewHolder extends RecyclerView.ViewHolder {

    private String LOG_TAG = JobViewHolder.class.getSimpleName();

    protected TextView _jobCompany;
    protected TextView _jobTitle;
    protected ImageView _companyImg;

    public JobViewHolder(View itemView) {
        super(itemView);
        Log.v(LOG_TAG, "Constructor");
        //Pull Portions of CardView
        _jobCompany = (TextView) itemView.findViewById(R.id.job_company);
        _jobTitle = (TextView) itemView.findViewById(R.id.job_title);
        _companyImg = (ImageView) itemView.findViewById(R.id.company_image);
    }

    }


