package com.matelau.junior.centsproject.Views.VisualizationFragments.SpendingBreakdown;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpendingBreakdownModDialogFragment extends DialogFragment {
    private String LOG_TAG = SpendingBreakdownModDialogFragment.class.getSimpleName();
    private LinearLayout _rootLayout;
    private RelativeLayout _circle;
    private ImageButton _spending_plus;
    private RecyclerView _sbAttributes;
    private LinearLayoutManager _sbLayoutManager;
    private SpendingBreakdownRecycleAdapter _rAdapter;
    private List<String> _sbAttr;
    private List<Float> _sbAttrVals;


    public SpendingBreakdownModDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "OnCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_spending_breakdown_mod_dialog, null, false);
        _circle = (RelativeLayout) _rootLayout.findViewById(R.id.plus_spending_category);
        _spending_plus = (ImageButton) _circle.findViewById(R.id.circle_btn);
        //add more cats
//        _spending_plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(LOG_TAG, "Spending onClick");
//                _spending_plus.startAnimation((AnimationUtils.loadAnimation(getActivity(), R.anim.rotate)));
////                _spending_plus.clearAnimation();
//                LayoutInflater lf = getActivity().getLayoutInflater();
////                _rootLayout.addView(lf.inflate(R.layout.spending_breakdown_mod_element), 1);
////                _rootLayout.addView(1, lf.inflate(R.layout.spending_breakdown_mod_element, this, false));
////                _rootLayout.addView(inflater.inflate(R.layout.spending_breakdown_mod_element, this, false));
//
//            }
//        });

        //******  setup Attribute List ********************
        //get Attr
        _sbAttr = CentsApplication.get_sbLabels();
        _sbAttrVals = CentsApplication.get_sbPercents();
        //setup dynamic listview
        _sbAttributes = (RecyclerView) _rootLayout.findViewById(R.id.sb_attr_list);
        _sbAttributes.setHasFixedSize(false);
        _sbLayoutManager = new LinearLayoutManager(getActivity());
        _sbLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        _sbAttributes.setLayoutManager(_sbLayoutManager);
        _rAdapter = new SpendingBreakdownRecycleAdapter(_sbAttr, _sbAttrVals, getActivity());
        _sbAttributes.setAdapter(_rAdapter);


        builder.setView(_rootLayout);
        return builder.create();
    }
}
