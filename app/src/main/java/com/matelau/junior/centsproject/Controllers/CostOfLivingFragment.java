package com.matelau.junior.centsproject.Controllers;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matelau.junior.centsproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CostOfLivingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CostOfLivingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CostOfLivingFragment extends Fragment {


    public CostOfLivingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //TODO learn how to create visualizations!

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cost_of_living, container, false);
    }


}
