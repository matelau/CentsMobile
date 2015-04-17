package com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving;


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
import com.matelau.junior.centsproject.Models.VizModels.ColiResponse;
import com.matelau.junior.centsproject.R;
import com.matelau.junior.centsproject.Views.VisualizationFragments.SummaryAdapter;

import java.util.List;

/**
 * Displays summary for Cost of Living Visualizations
 */
public class COLSummaryFragment extends Fragment {

    private LinearLayout _rootLayout;
    private TextView _city1Title;
    private TextView _city2Title;
    private ListView _citiesSum;
    private SummaryAdapter _citiesAdapter;
    private ImageButton _search;
    private String LOG_TAG = COLSummaryFragment.class.getSimpleName();



    public COLSummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(LOG_TAG, "CreateView");

        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_col_summary, container, false);
        //get MajorResponse
        ColiResponse colResponse = CentsApplication.get_colResponse();
        if(CentsApplication.isDebug()){
//            do nothing
        }
        else{
            AdView mAdView = (AdView) _rootLayout.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }


        _city1Title = (TextView) _rootLayout.findViewById(R.id.title1);
        List<ColiResponse.Element> elements = colResponse.getElements();
        boolean hasSecondCity = elements.size() > 1;
        _city1Title.setText(elements.get(0).getName());
        _city2Title = (TextView) _rootLayout.findViewById(R.id.title2);
        String title2 = null;
        if(hasSecondCity){
            title2 = elements.get(1).getName();
        }

        if(title2 != null){
            _city2Title.setText(title2);
        }
        else{
            _city2Title.setVisibility(View.GONE);
        }

        //Setup summary list
        _search = (ImageButton) _rootLayout.findViewById(R.id.imageSearchButton);
        _search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch selection Dialog
                showCitySelectionDialog();
            }
        });
        _citiesSum = (ListView) _rootLayout.findViewById(R.id.col_sum_list);
        _citiesAdapter = new SummaryAdapter(0, getActivity());
        _citiesSum.setAdapter(_citiesAdapter);
        return _rootLayout;
    }



    private void showCitySelectionDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CitySelectionDialogFragment csd = new CitySelectionDialogFragment();
        csd.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        csd.show(fm, "tag");
    }





}
