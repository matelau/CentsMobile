package com.matelau.junior.centsproject.Views.VisualizationFragments.SpendingBreakdown;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpendingBreakdownModDialogFragment extends DialogFragment {
    private String LOG_TAG = SpendingBreakdownModDialogFragment.class.getSimpleName();
    private RelativeLayout _rootLayout;
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

//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_spending_breakdown_mod_dialog, null, false);
//        _circle = (RelativeLayout) _rootLayout.findViewById(R.id.plus_spending_category);
//        _spending_plus = (ImageButton) _circle.findViewById(R.id.circle_btn);
//        //add more cats
//        _spending_plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(LOG_TAG, "Spending onClick");
//                //TODO add spinning animation
//                _spending_plus.startAnimation((AnimationUtils.loadAnimation(getActivity(), R.anim.rotate)));
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                SpendingBreakdownAttributeAdditionDialogFragment addition = new SpendingBreakdownAttributeAdditionDialogFragment();
//                addition.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
//                addition.show(fm, "tag");
//            }
//        });
//
//        //******  setup Attribute List ********************
//        //get Attr
//        _sbAttr = CentsApplication.get_sbLabels();
//        _sbAttrVals = CentsApplication.get_sbPercents();
//        //setup dynamic listview
//        _sbAttributes = (RecyclerView) _rootLayout.findViewById(R.id.sb_attr_list);
//        _sbAttributes.setHasFixedSize(false);
//        _sbLayoutManager = new LinearLayoutManager(getActivity());
//        _sbLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        _sbAttributes.setLayoutManager(_sbLayoutManager);
//        _rAdapter = new SpendingBreakdownRecycleAdapter(_sbAttr, _sbAttrVals, getActivity());
//        _sbAttributes.setAdapter(_rAdapter);
//
//        return _rootLayout;
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "OnCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_spending_breakdown_mod_dialog, null, false);
        _circle = (RelativeLayout) _rootLayout.findViewById(R.id.plus_spending_category);
        _spending_plus = (ImageButton) _circle.findViewById(R.id.circle_btn);
        //add more cats
        _spending_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Spending onClick");
                //TODO add spinning animation
                _spending_plus.startAnimation((AnimationUtils.loadAnimation(getActivity(), R.anim.rotate)));
                FragmentManager fm = getActivity().getSupportFragmentManager();
                SpendingBreakdownAttributeAdditionDialogFragment addition = new SpendingBreakdownAttributeAdditionDialogFragment();
                addition.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
                addition.show(fm, "tag");
            }
        });

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


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d(LOG_TAG, "onActivityResult");
//        _rAdapter.updateLists();
//    }
}
