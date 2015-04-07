package com.matelau.junior.centsproject.Views.VisualizationFragments.Career;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Models.VizModels.CareerResponse;
import com.matelau.junior.centsproject.R;
import com.matelau.junior.centsproject.Views.VisualizationFragments.SummaryAdapter;

/**
 * Hosts Career comparison Summary view
 */
public class CareerComparisonSummaryFragment extends Fragment {
    private LinearLayout _rootLayout;
    private TextView _career1Title;
    private TextView _career2Title;
    private ListView _careerSum;
    private SummaryAdapter _careerAdapter;
    private ImageButton _search;

    private String LOG_TAG = CareerComparisonSummaryFragment.class.getSimpleName();

    public CareerComparisonSummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // get data

        Log.d(LOG_TAG, "CreateView");
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_career_comparison_summary, container, false);
        AdView mAdView = (AdView) _rootLayout.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        if(CentsApplication.isDebug())
            adRequest = new AdRequest.Builder().addTestDevice("84B46C4862CAF80187170C1A7901502C").build();
        mAdView.loadAd(adRequest);
        CareerResponse cResponse = CentsApplication.get_cResponse();
        _career1Title = (TextView) _rootLayout.findViewById(R.id.title1);
        _career1Title.setText(cResponse.getCareer1());
        _career2Title = (TextView) _rootLayout.findViewById(R.id.title2);
        String title2 = cResponse.getCareer2();
        if(title2 != null){
            _career2Title.setText(title2);
        }
        else{
            _career2Title.setVisibility(View.GONE);
        }

        //Setup summary list
        _search = (ImageButton) _rootLayout.findViewById(R.id.imageSearchButton);
        _search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch selection Dialog
               showCareerSelectionDialog();
            }
        });
        _careerSum = (ListView) _rootLayout.findViewById(R.id.career_sum_list);
        _careerAdapter = new SummaryAdapter(3, getActivity());
        _careerSum.setAdapter(_careerAdapter);
        return _rootLayout;
    }

    private void showCareerSelectionDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CareerSelectionDialogFragment csd = new CareerSelectionDialogFragment();
        csd.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        csd.show(fm, "tag");
    }


    public String getTitle(){
        return "Summary";
    }





}
