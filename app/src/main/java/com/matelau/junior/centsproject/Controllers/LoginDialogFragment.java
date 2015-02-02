package com.matelau.junior.centsproject.Controllers;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.matelau.junior.centsproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginDialogFragment extends DialogFragment {

    private String LOG_TAG = LoginDialogFragment.class.getSimpleName();
    private LinearLayout _rootLayout;


    public LoginDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateDialog" );
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_login_dialog, null, false);

        builder.setView(_rootLayout);
        builder.setCancelable(true);
        return builder.create();
    }
}
