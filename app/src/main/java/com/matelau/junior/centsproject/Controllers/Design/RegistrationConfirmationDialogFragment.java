package com.matelau.junior.centsproject.Controllers.Design;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationConfirmationDialogFragment extends DialogFragment{


    public RegistrationConfirmationDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Confirm Email");
        alertDialogBuilder.setMessage("You must confirm your email (check your spam folder) before you will be able to update your profile or login again.");
        //null should be your on click listener
        alertDialogBuilder.setPositiveButton("OK", null);
        return alertDialogBuilder.create();
    }
}
