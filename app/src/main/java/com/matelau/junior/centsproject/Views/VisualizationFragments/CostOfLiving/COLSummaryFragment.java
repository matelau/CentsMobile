package com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matelau.junior.centsproject.R;

/**
 * Displays summary for Cost of Living Visualizations
 */
public class COLSummaryFragment extends Fragment {

    private RelativeLayout _rootLayout;
    private TextView _summary;
    private String LOG_TAG = COLSummaryFragment.class.getSimpleName();
    private Button _begin;



    public COLSummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(LOG_TAG, "CreateView" );
        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_col_summary, container, false);
        _summary = (TextView) _rootLayout.findViewById(R.id.api_sum_col);

        _summary.setText("TODO CALL COL API and Return Data here");
        _begin = (Button) _rootLayout.findViewById(R.id.ool_begin);
        _begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCitySelectionDialog();
            }
        });
//        Launch Selection Of Cities
//        showCitySelectionDialog();
        return _rootLayout;
    }



    private void showCitySelectionDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CitySelectionDialogFragment csd = new CitySelectionDialogFragment();
        csd.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        csd.show(fm, "tag");
    }





}
