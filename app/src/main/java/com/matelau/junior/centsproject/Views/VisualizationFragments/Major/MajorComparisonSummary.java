package com.matelau.junior.centsproject.Views.VisualizationFragments.Major;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.matelau.junior.centsproject.Controllers.CentsApplication;
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

        AdView mAdView = (AdView) _rootLayout.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        if(CentsApplication.isDebug()){
            adRequest = new AdRequest.Builder().addTestDevice("84B46C4862CAF80187170C1A7901502C").build();
        }

        mAdView.loadAd(adRequest);

        _major1Title = (TextView) _rootLayout.findViewById(R.id.title1);
        _major1Title.setText(CentsApplication.get_major1().getName());
        _major2Title = (TextView) _rootLayout.findViewById(R.id.title2);
        if(CentsApplication.get_major2() != null){
            String title2 = CentsApplication.get_major2().getName();
            _major2Title.setText(title2);
        }
        else{
            _major2Title.setVisibility(View.GONE);
        }

        //Setup summary list
        _search = (ImageButton) _rootLayout.findViewById(R.id.imageSearchButton);
        _search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch selection Dialog
                showMajorSelectionDialog();
            }
        });
        _majorSum = (ListView) _rootLayout.findViewById(R.id.major_sum_list);
        _majorAdapter = new SummaryAdapter(1, getActivity());
        _majorSum.setAdapter(_majorAdapter);
        return _rootLayout;


    }

    private void showMajorSelectionDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        MajorSelectionDialogFragment msd = new MajorSelectionDialogFragment();
        msd.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        msd.show(fm, "tag");
    }





}
