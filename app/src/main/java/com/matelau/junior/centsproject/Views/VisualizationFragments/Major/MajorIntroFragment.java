package com.matelau.junior.centsproject.Views.VisualizationFragments.Major;


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
public class MajorIntroFragment extends Fragment {
    private RelativeLayout _rootLayout;
    private Button _begin;
    private String LOG_TAG = MajorIntroFragment.class.getSimpleName();



    public MajorIntroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_major_intro, container, false);
        _begin = (Button) _rootLayout.findViewById(R.id.major_begin);
        _begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "onClick Switch to ViewPager");
                CentsApplication.set_selectedVis("Major Comparison");
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
                ft.commit();
            }
        });
        //TODO begin major selection Dialog

        return _rootLayout;
    }


}
