package com.matelau.junior.centsproject.Views.VisualizationFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matelau.junior.centsproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollegeComparisonSummary extends Fragment {

    RelativeLayout _rootLayout;
    TextView _summary;


    public CollegeComparisonSummary() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_college_comparison_summary, container, false);
        _summary = (TextView) _rootLayout.findViewById(R.id.api_sum_college);
        _summary.setText("TODO CALL College Comparison API and Return Data here");
        return _rootLayout;
    }


}
