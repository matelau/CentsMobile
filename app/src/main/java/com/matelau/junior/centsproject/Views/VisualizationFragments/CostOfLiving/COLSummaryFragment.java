package com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Controllers.WizardDialogFragment;
import com.matelau.junior.centsproject.Models.Design.Col;
import com.matelau.junior.centsproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Displays summary for Cost of Living Visualizations
 */
public class COLSummaryFragment extends Fragment {

    private RelativeLayout _rootLayout;
    private TextView _summary;
    private String LOG_TAG = COLSummaryFragment.class.getSimpleName();



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
