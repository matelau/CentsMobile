package com.matelau.junior.centsproject.Controllers;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.matelau.junior.centsproject.Models.CentsAPIModels.QueryService;
import com.matelau.junior.centsproject.R;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private String LOG_TAG = SearchFragment.class.getSimpleName();
    private EditText _editText;
    private String _query;
    private RelativeLayout _rootLayout;
    private   ImageButton _submitBtn;
    private TextView _feedback;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_search, container, false);

        ImageButton search_submit = (ImageButton) _rootLayout.findViewById(R.id.search_button);
        _editText = (EditText) _rootLayout.findViewById(R.id.editText1);
        _editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    //hide keyboard after submit
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(_editText.getWindowToken(), 0);
                    handleSubmit();

                    return true;
                }
                return false;
            }
        });
        if(_query != null){
            _editText.setText(_query);
        }
        search_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });

        _feedback = (TextView) _rootLayout.findViewById(R.id.feedback);
        _feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //allow user to select email client and send feedback
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "admin@trycents.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FEEDBACK");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        return _rootLayout;
    }

    /**
     * Handles Search submissions
     */
    private void handleSubmit() {
        String searchText = _editText.getText().toString();
        Log.v(LOG_TAG, "in handleSubmit: " + searchText);
        //TODO post to Query Parsing Service and handle response
        _submitBtn = (ImageButton) _rootLayout.findViewById(R.id.search_button);
        _submitBtn.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate));
        if(CentsApplication.isDebug())
            Toast.makeText(getActivity(), "Search for:" + searchText, Toast.LENGTH_SHORT).show();
        //Todo if valid response and user is logged in from query service store searchText to _query
        //http://54.183.8.236:6001/query/
        QueryService service = CentsApplication.get_queryParsingRestAdapter().create(QueryService.class);
        service.results(searchText, new Callback<Response>() {
            @Override
            public void success(Response response1, Response response) {
                if(CentsApplication.isDebug())
                    Toast.makeText(getActivity(),response.toString(), Toast.LENGTH_SHORT);
                Log.v(LOG_TAG, "Query Service Response: "+response.toString());
                _query =  _editText.getText().toString();
                //TODO Process Response and route accordingly
                _submitBtn.clearAnimation();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Query Service Error: "+error.getMessage());
                _submitBtn.clearAnimation();
            }
        });
    }


}
