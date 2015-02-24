package com.matelau.junior.centsproject.Views.VisualizationFragments.SpendingBreakdown;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Models.VizModels.SpendingBreakdownCategory;
import com.matelau.junior.centsproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpendingBreakdownAttributeAdditionDialogFragment extends DialogFragment {
    private String LOG_TAG = SpendingBreakdownAttributeAdditionDialogFragment.class.getSimpleName();
    private FrameLayout _rootLayout;
    private EditText _value;
    private EditText _category;
//    protected


    public SpendingBreakdownAttributeAdditionDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "OnCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        _rootLayout = (FrameLayout) inflater.inflate(R.layout.fragment_spending_breakdown_attribute_addition, null, false);
        _value = (EditText) _rootLayout.findViewById(R.id.editText1);
        _category = (EditText) _rootLayout.findViewById(R.id.attr_category);

        _category.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText et = (EditText) v;
                et.setHint("");
            }
        });

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // add values to Application lvl array
                String dollarAmt = _value.getText().toString();
                Float monthPercent = CentsApplication.convDollarToPercent(dollarAmt);
                CentsApplication.get_sbValues().add(new SpendingBreakdownCategory(_category.getText().toString().toUpperCase(), monthPercent,false));
                Log.d(LOG_TAG, "amt= " + monthPercent);
                //notify adapter of change
                CentsApplication.get_rAdapter().add();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _category.setHint("Category");
            }
        });
        builder.setView(_rootLayout);
        builder.setCancelable(true);



        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
