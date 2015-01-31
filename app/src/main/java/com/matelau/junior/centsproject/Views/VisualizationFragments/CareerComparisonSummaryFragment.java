package com.matelau.junior.centsproject.Views.VisualizationFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matelau.junior.centsproject.R;

/**
 * Hosts Career comparison Summary view
 */
public class CareerComparisonSummaryFragment extends Fragment {
    private RelativeLayout _rootLayout;
    private TextView _summary;
    private CardView _cv;

    public CareerComparisonSummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_career_comparison_summary, container, false);
        _cv = (CardView) _rootLayout.findViewById(R.id.career_sum_card);
        _summary = (TextView) _rootLayout.findViewById(R.id.api_sum_career);
        _summary.setText("TODO CALL Career API and Return Data here");
        return _rootLayout;
    }


    public String getTitle(){
        return "Summary";
    }





}
