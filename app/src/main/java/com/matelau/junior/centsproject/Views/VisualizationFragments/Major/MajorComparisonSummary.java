package com.matelau.junior.centsproject.Views.VisualizationFragments.Major;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Models.VizModels.MajorResponse;
import com.matelau.junior.centsproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MajorComparisonSummary extends Fragment {
    private String LOG_TAG = MajorComparisonSummary.class.getSimpleName();
    private LinearLayout _rootLayout;
    private TextView _major1Title;
    private TextView _major2Title;
    private ListView _majorSum;

    private ImageView _image1;
    private ImageView _image2;


    public MajorComparisonSummary() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(LOG_TAG, "CreateView");

        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_major_comparison_summary, container, false);
        //get MajorResponse
        MajorResponse mResponse = CentsApplication.get_mResponse();

        _major1Title = (TextView) _rootLayout.findViewById(R.id.title1);
        _major1Title.setText(CentsApplication.get_major1());
        _major2Title = (TextView) _rootLayout.findViewById(R.id.title2);
        String title2 = CentsApplication.get_major2();
        if(title2 != null){
            _major2Title.setText(title2);
        }
        _image1 = (ImageView) _rootLayout.findViewById(R.id.logo1_image);
        _image2 = (ImageView) _rootLayout.findViewById(R.id.logo2_image);
        //hide images until I find a major image source or get some developed
        _image1.setVisibility(View.GONE);
        _image2.setVisibility(View.GONE);
        //Setup summary list
        _majorSum = (ListView) _rootLayout.findViewById(R.id.major_sum_list);
//        _rAdapter = new SBArrayAdapter();
//        CentsApplication.set_rAdapter(_rAdapter);
//        _sbAttributes.setAdapter(_rAdapter);
        return _rootLayout;


    }





}
