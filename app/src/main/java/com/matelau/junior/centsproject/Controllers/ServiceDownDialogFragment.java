package com.matelau.junior.centsproject.Controllers;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceDownDialogFragment extends DialogFragment {


    public ServiceDownDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Service Down");
        alertDialogBuilder.setMessage("Sorry for the inconvenience, but our query parser is down. Don't worry you can still use our service via the examples pages");
        //null should be your on click listener
        alertDialogBuilder.setPositiveButton("OK", null);
        return alertDialogBuilder.create();
    }
}
