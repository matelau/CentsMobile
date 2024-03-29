package com.matelau.junior.centsproject.Views.VisualizationFragments.College;

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


public class CollegeIntroFragment extends Fragment {
    private String LOG_TAG = CollegeIntroFragment.class.getSimpleName();

    public CollegeIntroFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_college_intro, container, false);
        Button _begin = (Button) _rootLayout.findViewById(R.id.college_begin);
        _begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "onClick Switch to ViewPager");
                showCollegeSelectionDialog();
            }
        });
        //TODO begin college selection Dialog
        return _rootLayout;
    }


    /**
     * Show selection dialog
     */
    private void showCollegeSelectionDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CollegeSelectionDialogFragment collegeSelect = new CollegeSelectionDialogFragment();
        collegeSelect.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        collegeSelect.show(fm, "tag");
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
