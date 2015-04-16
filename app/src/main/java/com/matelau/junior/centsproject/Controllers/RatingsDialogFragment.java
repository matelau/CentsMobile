package com.matelau.junior.centsproject.Controllers;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.matelau.junior.centsproject.R;

/**
 */
public class RatingsDialogFragment extends DialogFragment {
    LinearLayout _rootLayout;


    public RatingsDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //get title
        String toBeRated = getArguments().getString("element");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_ratings_dialog, null, false);
        TextView element = (TextView) _rootLayout.findViewById(R.id.toBeRated);
        element.setText("Rate: " + toBeRated);
        RatingBar rating = (RatingBar) _rootLayout.findViewById(R.id.rating);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingBar.setRating(rating);
                ratingBar.invalidate();
                Toast.makeText(getActivity(), "Coming Soon rated: "+rating, Toast.LENGTH_SHORT).show();
                _rootLayout.invalidate();
            }
        });

        builder.setView(_rootLayout);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }
}
