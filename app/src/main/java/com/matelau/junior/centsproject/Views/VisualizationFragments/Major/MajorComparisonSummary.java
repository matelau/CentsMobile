package com.matelau.junior.centsproject.Views.VisualizationFragments.Major;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Controllers.SearchFragment;
import com.matelau.junior.centsproject.Models.VizModels.MajorResponse;
import com.matelau.junior.centsproject.R;
import com.matelau.junior.centsproject.Views.VisualizationFragments.SummaryAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MajorComparisonSummary extends Fragment {
    private String LOG_TAG = MajorComparisonSummary.class.getSimpleName();
    private LinearLayout _rootLayout;
    private TextView _major1Title;
    private TextView _major2Title;
    private ListView _majorSum;
    private SummaryAdapter _majorAdapter;
    private ImageView _image1;
    private ImageView _image2;
    private ImageButton _search;


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
        else{
            _major2Title.setVisibility(View.GONE);
        }
//        _image1 = (ImageView) _rootLayout.findViewById(R.id.logo1_image);
//        _image2 = (ImageView) _rootLayout.findViewById(R.id.logo2_image);
        //hide images until I find a major image source or get some developed
//        _image1.setVisibility(View.INVISIBLE);
//        _image1.setMaxHeight(0);
//        _image1.setMaxWidth(0);
//        _image2.setMaxHeight(0);
//        _image2.setMaxWidth(0);
//        _image2.setVisibility(View.INVISIBLE);
        //Setup summary list
        _search = (ImageButton) _rootLayout.findViewById(R.id.imageSearchButton);
        _search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return search frag
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_placeholder, new SearchFragment());
                ft.addToBackStack("major-summary");
                ft.commit();
            }
        });
        _majorSum = (ListView) _rootLayout.findViewById(R.id.major_sum_list);
        _majorAdapter = new SummaryAdapter(1, getActivity());
        _majorSum.setAdapter(_majorAdapter);
        return _rootLayout;


    }





}
