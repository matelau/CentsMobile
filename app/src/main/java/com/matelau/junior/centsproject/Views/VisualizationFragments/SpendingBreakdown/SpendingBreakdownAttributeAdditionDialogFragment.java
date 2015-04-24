package com.matelau.junior.centsproject.Views.VisualizationFragments.SpendingBreakdown;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Models.CentsAPIServices.UserService;
import com.matelau.junior.centsproject.Models.VizModels.SpendingBreakdownCategory;
import com.matelau.junior.centsproject.R;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpendingBreakdownAttributeAdditionDialogFragment extends DialogFragment {
    private String LOG_TAG = SpendingBreakdownAttributeAdditionDialogFragment.class.getSimpleName();
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
        FrameLayout _rootLayout = (FrameLayout) inflater.inflate(R.layout.fragment_spending_breakdown_attribute_addition, null, false);
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
                Float monthPercent = CentsApplication.convDollarToPercent(dollarAmt, false);
                CentsApplication.get_sbValues().add(new SpendingBreakdownCategory(_category.getText().toString().toUpperCase(), monthPercent,false));
                //Save Addition
                String filename = CentsApplication.get_currentBreakdown()+".dat";
                CentsApplication.saveSB(filename, getActivity());
                if(CentsApplication.is_loggedIN()){
                    //store sb to db via api
                    HashMap<String, String> elements = new HashMap<String,String>();
                    for(SpendingBreakdownCategory current : CentsApplication.get_sbValues()){
                        //dont store taxes it will be calculated
                        if(!current._category.equals("TAXES")){
                            float percent = current._percent * 100f;
                            elements.put(current._category, ""+percent);
                        }
                    }
                    SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
                    int _id = settings.getInt("ID", 0);

                    HashMap<String, HashMap<String, String>> fields = new HashMap<String, HashMap<String,String>>();
                    fields.put("fields", elements);
                    UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
                    service.initSpendingFields(_id, CentsApplication.get_currentBreakdown(), fields, new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            Log.d(LOG_TAG, "updated spending records for: " + CentsApplication.get_currentBreakdown());
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(LOG_TAG, error.getMessage());
                        }
                    });
                }
                Log.d(LOG_TAG, "amt= " + monthPercent);
                //notify adapter of change
                CentsApplication.get_rAdapter().add();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _category.setHint("Category");
                dismiss();
            }
        });
        builder.setView(_rootLayout);
        builder.setCancelable(true);
        return builder.create();
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
