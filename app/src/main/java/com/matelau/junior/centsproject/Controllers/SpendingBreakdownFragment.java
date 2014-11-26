package com.matelau.junior.centsproject.Controllers;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.matelau.junior.centsproject.R;

import lecho.lib.hellocharts.view.PieChartView;

/**
 */
public class SpendingBreakdownFragment extends Fragment {
    ImageButton _back;


    public SpendingBreakdownFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_spending_breakdown, container, false);
        CardView cv = (CardView) rootView.findViewById(R.id.spending_card_view);
        PieChartView chart = (PieChartView) rootView.findViewById(R.id.spending_vis);

        _back = (ImageButton) rootView.findViewById(R.id.go_to_col);
        _back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                _back.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate));
                Intent colIntent = new Intent(getActivity(), CostOfLivingActivity.class);
                startActivity(colIntent);
                return true;
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

}
