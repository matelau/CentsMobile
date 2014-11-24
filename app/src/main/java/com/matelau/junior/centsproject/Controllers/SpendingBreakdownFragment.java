package com.matelau.junior.centsproject.Controllers;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matelau.junior.centsproject.R;

/**
 */
public class SpendingBreakdownFragment extends Fragment {


    public SpendingBreakdownFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO learn how to create visualization

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spending_breakdown, container, false);
    }

}
