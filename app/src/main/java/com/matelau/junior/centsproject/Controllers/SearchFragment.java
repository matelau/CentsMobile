package com.matelau.junior.centsproject.Controllers;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matelau.junior.centsproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        ImageButton search_submit = (ImageButton) findViewById(R.id.search_button);
//        _editText = (EditText) findViewById(R.id.editText1);
//        _editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH ||
//                        actionId == EditorInfo.IME_ACTION_DONE ||
//                        event.getAction() == KeyEvent.ACTION_DOWN &&
//                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                    //hide keyboard after submit
//                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(_editText.getWindowToken(), 0);
//                    handleSubmit();
//
//                    return true;
//                }
//                return false;
//            }
//        });
//        if(_query != null){
//            _editText.setText(_query);
//        }
//        search_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handleSubmit();
//            }
//        });

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    /**
     * Handles Search submissions
     */
    private void handleSubmit() {
//        String searchText = _editText.getText().toString();
//        Log.v(LOG_TAG, "in handleSubmit: " + searchText);
//        //TODO post to Query Parsing Service and handle response
//        _submitBtn = (ImageButton) findViewById(R.id.search_button);
//        _submitBtn.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate));
//        Toast.makeText(this, "Search for:" + searchText, Toast.LENGTH_SHORT).show();
//        //Todo if valid response from query service store searchText to _query
//        //http://54.183.8.236:6001/query/

    }


}
