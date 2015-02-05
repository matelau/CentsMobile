package com.matelau.junior.centsproject.Views.Design;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matelau.junior.centsproject.Models.Design.GlassdoorInfo;
import com.matelau.junior.centsproject.R;

import java.util.List;

/**
 * Created by matelau on 12/1/14.
 */
public class JobDetailsRecycleAdapter extends RecyclerView.Adapter<JobDetailsRecycleAdapter.JobDetailViewHolder>{
    private Context _context;
    private List<GlassdoorInfo> _gi;
    private String LOG_TAG = JobDetailsRecycleAdapter.class.getSimpleName();

    public JobDetailsRecycleAdapter(List<GlassdoorInfo> glassInfo, Context context){
        _context = context;
        _gi = glassInfo;
    }


    @Override
    public JobDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_detail_item, parent, false);
        return new JobDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobDetailViewHolder holder, int position) {
        GlassdoorInfo currentInfo = _gi.get(position);
        Log.v(LOG_TAG, "On BindViewHolder: " + currentInfo._category);
        holder._category.setText(currentInfo._category);
        holder._value.setText(currentInfo._value);

    }

    @Override
    public int getItemCount() {
        return _gi.size();
    }

    public class JobDetailViewHolder extends RecyclerView.ViewHolder {

        private String LOG_TAG = JobDetailViewHolder.class.getSimpleName();

        protected TextView _category;
        protected TextView _value;


        public JobDetailViewHolder(View itemView) {
            super(itemView);
            Log.v(LOG_TAG, "Constructor");
            //Pull Portions of CardView
            _category = (TextView) itemView.findViewById(R.id.detail_category);
            _value = (TextView) itemView.findViewById(R.id.detail_value);
           }

    }
}


