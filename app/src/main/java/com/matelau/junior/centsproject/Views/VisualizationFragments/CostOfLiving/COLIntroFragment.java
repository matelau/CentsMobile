package com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.matelau.junior.centsproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class COLIntroFragment extends Fragment {

    private RelativeLayout _rootLayout;
    private Button _beginCOL;



    public COLIntroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("Cost of Living Comparison");
        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_colintro, container, false);
        _beginCOL = (Button) _rootLayout.findViewById(R.id.ool_begin);
        _beginCOL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCitySelectionDialog();
            }
        });

        return _rootLayout;
    }


    private void showCitySelectionDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CitySelectionDialogFragment csd = new CitySelectionDialogFragment();
        csd.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        csd.show(fm, "tag");
    }


}
