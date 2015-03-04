package com.matelau.junior.centsproject.Views.VisualizationFragments.College;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Models.CentsAPIModels.RecordQuery;
import com.matelau.junior.centsproject.Models.CentsAPIModels.RecordsService;
import com.matelau.junior.centsproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollegeSelectionDialogFragment extends DialogFragment {
    private String LOG_TAG = CollegeSelectionDialogFragment.class.getSimpleName();
    private String[] _states = {"UT", "MA"};
    private LinearLayout _rootLayout;
    private Spinner _states1;

    public CollegeSelectionDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_college_selection_dialog, null, false);
        //Load College Records
        RecordsService service = CentsApplication.get_centsRestAdapter().create(RecordsService.class);
        RecordQuery query = new RecordQuery();
        query.setOperation("get");
        ArrayList<String> tables = new ArrayList<String>();
        tables.add("university");
        query.setTables(tables);
        query.setWhere("UT");
        service.getRecords(query, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

                BufferedReader reader = null;
                StringBuilder sb = new StringBuilder();
                try {

                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                    String line;

                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String rsp = sb.toString();
                Log.d(LOG_TAG, "success: "+rsp);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());

            }
        });
        builder.setView(_rootLayout);
        return builder.create();

    }

    private void handleResponse(Response response){
        Gson gson = new Gson();
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {

            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

            String line;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String rsp = sb.toString();
//        ColiResponse colResponse = gson.fromJson(rsp, ColiResponse.class);
//        CentsApplication.set_colResponse(colResponse);
//        CentsApplication.set_selectedVis("College Comparison");
//        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
//        ft.commit();
        dismiss();
    }
}
