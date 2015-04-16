package com.matelau.junior.centsproject.Controllers;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.matelau.junior.centsproject.Models.CentsAPIServices.MajorService;
import com.matelau.junior.centsproject.Models.CentsAPIServices.UserService;
import com.matelau.junior.centsproject.Models.UserModels.CareerRating;
import com.matelau.junior.centsproject.Models.UserModels.DegreeRating;
import com.matelau.junior.centsproject.Models.UserModels.SchoolRating;
import com.matelau.junior.centsproject.Models.UserModels.UserResponse;
import com.matelau.junior.centsproject.R;

import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 */
public class RatingsDialogFragment extends DialogFragment {
    private String LOG_TAG = RatingsDialogFragment.class.getSimpleName();
    private LinearLayout _rootLayout;
    private RatingBar _rating;
    private boolean ratingLoaded = false;
    private List<CareerRating> cRatings;
    private List<DegreeRating> dRatings;
    private List<SchoolRating> sRatings;
    private String toBeRated;
    //0:Major, 1:Career
    private int type;
    private int _id;


    public RatingsDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //get title
        toBeRated = getArguments().getString("element");
        //get type
        type = getArguments().getInt("type");
        //get id
        SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        _id = settings.getInt("ID", 0);


        loadRatingData();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_ratings_dialog, null, false);
        TextView element = (TextView) _rootLayout.findViewById(R.id.toBeRated);
        element.setText("Rate: " + toBeRated);
        _rating = (RatingBar) _rootLayout.findViewById(R.id.rating);
        _rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(ratingLoaded){
                    ratingBar.setRating(rating);
                    rate(rating);
                    Toast.makeText(getActivity(), " Rated: "+rating, Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setView(_rootLayout);
        return builder.create();
    }


    public void rate(float rating){
        switch(type){
            case 0:
                //rate Major
                rateMajor(rating);
                break;
        }
    }


    private void loadRatingData(){
        UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
        service.getRatingsData(_id, new Callback<UserResponse>() {
            @Override
            public void success(UserResponse userResponse, Response response) {
                //ratings
                cRatings = userResponse.getCareerRatings();
                dRatings = userResponse.getDegreeRatings();
                sRatings = userResponse.getSchoolRatings();
                checkForPreviousRating();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());

            }
        });
    }

    private void checkForPreviousRating(){
        switch(type){
            case 0:
                for(DegreeRating d : dRatings){
                    if(toBeRated.contains(d.getName()) && toBeRated.contains(d.getLevel())){
                        _rating.setRating(d.getRating());
                    }
                }
                break;
            default:

                break;
        }

        //update rating loaded var so user can interact with rating
        ratingLoaded = true;

    }

    public void rateMajor(float rating){
        final String level = toBeRated.substring(toBeRated.indexOf("(")+1, toBeRated.length()-1).trim();
        final String major = toBeRated.substring(0, toBeRated.indexOf("(")).trim();
        MajorService service = CentsApplication.get_centsRestAdapter().create(MajorService.class);
        final HashMap<String, Integer> user = new HashMap<String, Integer>();
        user.put("user", _id);
        service.rateMajor(level, major, (int) rating, user, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(LOG_TAG, "updated rating" );
                dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, error.getMessage() );
                Toast.makeText(getActivity(), "Error - Please Try Again Later", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
