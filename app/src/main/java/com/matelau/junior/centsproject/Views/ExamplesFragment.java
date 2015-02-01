package com.matelau.junior.centsproject.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.matelau.junior.centsproject.R;

/**
 * Displays Examples of different visualizations users could conduct
 */
public class ExamplesFragment extends Fragment {
    private RelativeLayout _rootLayout;
    private ImageView _example;
    private String LOG_TAG = ExamplesFragment.class.getSimpleName();
    public ExamplesFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_examples, container, false);
        //TODO in the future implement viewpager for examples
        _example = (ImageView) _rootLayout.findViewById(R.id.example);
        Log.d(LOG_TAG, "CreateView - Examples");

        return _rootLayout;
    }
}
