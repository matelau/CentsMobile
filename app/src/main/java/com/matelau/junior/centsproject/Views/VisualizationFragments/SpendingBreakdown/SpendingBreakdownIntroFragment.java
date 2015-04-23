package com.matelau.junior.centsproject.Views.VisualizationFragments.SpendingBreakdown;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Controllers.VisualizationPagerFragment;
import com.matelau.junior.centsproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpendingBreakdownIntroFragment extends Fragment {
    private String LOG_TAG = SpendingBreakdownIntroFragment.class.getSimpleName();
    private RelativeLayout _rootLayout;
    private Button _begin;



    public SpendingBreakdownIntroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_spending_breakdown_intro, container, false);
        _begin = (Button) _rootLayout.findViewById(R.id.spending_begin);
        _begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "onClick Switch to ViewPager");
                CentsApplication.set_selectedVis("Spending Breakdown");
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
                ft.addToBackStack("spending-intro");
                ft.commit();
            }
        });

        return _rootLayout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Destroyed");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Resumed");
    }




}
