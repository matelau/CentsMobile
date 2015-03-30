package com.matelau.junior.centsproject.Views.VisualizationFragments.Career;


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
public class CareerIntroFragment extends Fragment {
    private String LOG_TAG = CareerIntroFragment.class.getSimpleName();
    private RelativeLayout _rootLayout;
    private Button _begin;



    public CareerIntroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_career_intro, container, false);
        _begin = (Button) _rootLayout.findViewById(R.id.career_begin);
        _begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(LOG_TAG, "onClick Switch to ViewPager");
//                CentsApplication.set_selectedVis("Career Comparison");
//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment()).addToBackStack(null);
//                ft.commit();
                showCareerSelectionDialog();
            }
        });
        //TODO add begin on Click listener to start career selections
        return _rootLayout;
    }

    private void showCareerSelectionDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CareerSelectionDialogFragment careerSelect = new CareerSelectionDialogFragment();
        careerSelect.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        careerSelect.show(fm, "tag");
    }

}
