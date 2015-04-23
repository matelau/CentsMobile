package com.matelau.junior.centsproject.Views.VisualizationFragments.Major;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

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
                showMajorSelectionDialog();
            }
        });
        return _rootLayout;
    }

    /**
     * Show Selection Dialog
     */
    private void showMajorSelectionDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        MajorSelectionDialogFragment msd = new MajorSelectionDialogFragment();
        msd.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        msd.show(fm, "tag");
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
