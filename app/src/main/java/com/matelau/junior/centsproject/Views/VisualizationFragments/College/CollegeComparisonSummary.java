package com.matelau.junior.centsproject.Views.VisualizationFragments.College;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Models.VizModels.SchoolAPIResponse;
import com.matelau.junior.centsproject.Models.VizModels.SchoolResponse;
import com.matelau.junior.centsproject.R;
import com.matelau.junior.centsproject.Views.VisualizationFragments.SummaryAdapter;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollegeComparisonSummary extends Fragment {

    private LinearLayout _rootLayout;
    private SchoolResponse _sResponse;
    private TextView _schoolName1;
    private TextView _schoolName2;
    private SummaryAdapter _schoolAdapter;
    private ListView _schoolSum;
    private ImageView _logo1;
    private ImageView _logo2;
    private ImageButton _search;
    private HashMap<String, String> _logos;

    private String LOG_TAG = CollegeComparisonSummary.class.getSimpleName();
    private boolean hasSecondSchool = true;


    public CollegeComparisonSummary() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Log.d(LOG_TAG, "CreateView");
        //get data
        _sResponse = CentsApplication.get_sResponse();
        if(_sResponse == null){
            //Convert api response to match queryparser response
            SchoolAPIResponse sresponse = CentsApplication.get_sApiResponse();
            _sResponse = new SchoolResponse();
            _sResponse.setSchool1(sresponse.getSchool1());
            _sResponse.setSchool2(sresponse.getSchool2());
            _sResponse.setSchool1Name(CentsApplication.get_university1());
            _sResponse.setSchool2Name(CentsApplication.get_university2());
            CentsApplication.set_sResponse(_sResponse);
        }
        hasSecondSchool = (_sResponse.getSchool2().size() > 0);
        //get Views
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_college_summary, container, false);
        _search = (ImageButton) _rootLayout.findViewById(R.id.imageSearchButton);
        _schoolName1 = (TextView) _rootLayout.findViewById(R.id.title1);
        _schoolName2 = (TextView) _rootLayout.findViewById(R.id.title2);
        _schoolSum = (ListView) _rootLayout.findViewById(R.id.college_sum_list);
        _schoolAdapter = new SummaryAdapter(2, getActivity());
        _schoolSum.setAdapter(_schoolAdapter);
        _logo1 = (ImageView) _rootLayout.findViewById(R.id.logo1_image);
        _logo2 = (ImageView) _rootLayout.findViewById(R.id.logo2_image);

        String univ = _sResponse.getSchool1Name();
        _schoolName1.setText(univ);
        String url1 = getImgUrl(univ);
        Log.d(LOG_TAG, "url1: "+url1);
        Picasso.with(getActivity()).load(url1).placeholder(R.drawable.placeholder).into(_logo1);

        if(hasSecondSchool){
             univ = _sResponse.getSchool2Name();
            _schoolName2.setText(univ);
            String url2 = getImgUrl(univ);
            Log.d(LOG_TAG, "url2: "+url2);
            Picasso.with(getActivity()).load(url2).placeholder(R.drawable.placeholder).into(_logo2);
        }
        else{
            _logo2.setVisibility(View.GONE);
            _schoolName2.setVisibility(View.GONE);
        }


        _search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCollegeSelectionDialog();
            }
        });


        return _rootLayout;
    }

    private void showCollegeSelectionDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CollegeSelectionDialogFragment collegeSelect = new CollegeSelectionDialogFragment();
        collegeSelect.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        collegeSelect.show(fm, "tag");
    }

    /**
     * using the University name from the response creates a url using american school search
     * @param univ
     * @return
     */
    private String getImgUrl(String univ){
        String tag = univ.toLowerCase();
        if(tag.contains("-")){
            tag = tag.substring(0, tag.indexOf("-"));
        }
        if(tag.contains(".")){
            tag = tag.replace(".", "");
        }
        tag = tag.replace(" ", "-");
        String url = "http://www.american-school-search.com/images/logo/"+tag+".gif";
        return url;
    }


}
