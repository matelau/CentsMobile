package com.matelau.junior.centsproject.Views.VisualizationFragments.College;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Controllers.SearchFragment;
import com.matelau.junior.centsproject.Models.VizModels.SchoolAPIResponse;
import com.matelau.junior.centsproject.Models.VizModels.SchoolResponse;
import com.matelau.junior.centsproject.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollegeComparisonSummary extends Fragment {

    private RelativeLayout _rootLayout;
    private SchoolResponse _sResponse;
    private TextView _schoolName1;
    private TextView _schoolName2;
    private TextView _rank1;
    private TextView _rank2;
    private TextView _inState1;
    private TextView _inState2;
    private TextView _outState1;
    private TextView _outState2;
    private TextView _gradRate1;
    private TextView _gradRate2;
    private TextView _enrollment1;
    private TextView _enrollment2;
    private TextView _rating1;
    private TextView _rating2;
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
        }
        hasSecondSchool = (_sResponse.getSchool2Name() != null);
        //get Views
        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_college_comparison_summary, container, false);
        _search = (ImageButton) _rootLayout.findViewById(R.id.imageSearchButton);
        _schoolName1 = (TextView) _rootLayout.findViewById(R.id.school1_name);
        _schoolName2 = (TextView) _rootLayout.findViewById(R.id.school2_name);
        _rank1 = (TextView) _rootLayout.findViewById(R.id.rank1);
        _rank2 = (TextView) _rootLayout.findViewById(R.id.rank2);
        _inState1 = (TextView) _rootLayout.findViewById(R.id.in_state1);
        _inState2 = (TextView) _rootLayout.findViewById(R.id.in_state2);
        _outState1 = (TextView) _rootLayout.findViewById(R.id.out_state1);
        _outState2 = (TextView) _rootLayout.findViewById(R.id.out_state2);
        _gradRate1 = (TextView) _rootLayout.findViewById(R.id.grad_rate1);
        _gradRate2 = (TextView) _rootLayout.findViewById(R.id.grad_rate2);
        _enrollment1 = (TextView) _rootLayout.findViewById(R.id.enrollment1);
        _enrollment2 = (TextView) _rootLayout.findViewById(R.id.enrollment2);
        _rating1 = (TextView) _rootLayout.findViewById(R.id.rating1);
        _rating2 = (TextView) _rootLayout.findViewById(R.id.rating2);
        _logo1 = (ImageView) _rootLayout.findViewById(R.id.school1_image);
        _logo2 = (ImageView) _rootLayout.findViewById(R.id.school2_image);


        _search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return search frag
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_placeholder, new SearchFragment());
                ft.commit();
            }
        });
        //school1
        //[in-state,out-state,grad-rate,undergrad,rank,user]
        //all fields could be null
        List<Double> school1Vals = _sResponse.getSchool1();
        if(school1Vals != null && school1Vals.size() > 0){
            _schoolName1.setText(_sResponse.getSchool1Name());
            if(school1Vals.get(4) != null){
                Integer rank = school1Vals.get(4).intValue();
                _rank1.setText(""+rank+" out of 200");
            }
            else{
                _rank1.setText("Not ranked");
            }
            if(school1Vals.get(0) != null){
                _inState1.setText("$" + school1Vals.get(0).intValue());
            }
            else{
                _inState1.setText("Unknown");
            }
            if(school1Vals.get(1) != null){
                _outState1.setText("S"+school1Vals.get(1).intValue());
            }
            else{
                _outState1.setText("Unknown");
            }
            if(school1Vals.get(2) != null){
                _gradRate1.setText(school1Vals.get(2).intValue()+"%");
            }
            else{
                _gradRate1.setText("Unknown");
            }
            if(school1Vals.get(3) != null){
                _enrollment1.setText(school1Vals.get(3).intValue()+" students");
            }
            else{
                _enrollment1.setText("Unknown");
            }

            _rating1.setText(school1Vals.get(5)+ " out of 5.0");
            String univ = _sResponse.getSchool1Name();
            String url1 = getImgUrl(univ); // _logos.get(_sResponse.getSchool1Name())
            Log.d(LOG_TAG, "url1: "+url1);
            Picasso.with(getActivity()).load(url1).placeholder(R.drawable.placeholder).into(_logo1);

            //school2
            if(hasSecondSchool){
                //[in-state,out-state,grad-rate,undergrad,rank,user]
                List<Double> school2Vals = _sResponse.getSchool2();
                if(school2Vals != null && school2Vals.size() > 0){
                    _schoolName2.setText(_sResponse.getSchool2Name());
                    if(school2Vals.get(4) != null){
                        Integer rank = school2Vals.get(4).intValue();
                        _rank2.setText(""+rank+" out of 200");
                    }
                    else{
                        _rank2.setText("Not ranked");
                    }
                    if(school2Vals.get(0) != null){
                        _inState2.setText("$" + school2Vals.get(0).intValue());
                    }
                    else{
                        _inState2.setText("Unknown");
                    }
                    if(school2Vals.get(1) != null){
                        _outState2.setText("S"+school2Vals.get(1).intValue());
                    }
                    else{
                        _outState2.setText("Unknown");
                    }
                    if(school2Vals.get(2) != null){
                        _gradRate2.setText(school2Vals.get(2).intValue()+"%");
                    }
                    else{
                        _gradRate2.setText("Unknown");
                    }
                    if(school2Vals.get(3) != null){
                        _enrollment2.setText(school2Vals.get(3).intValue()+" students");
                    }
                    else{
                        _enrollment2.setText("Unknown");
                    }

                    _rating2.setText(school2Vals.get(5)+ " out of 5.0");
                    univ = _sResponse.getSchool2Name();

                    String url2 = getImgUrl(univ);//"http://www.american-school-search.com/images/logo/"+tag+".gif".toLowerCase(); //_logos.get(_sResponse.getSchool2Name());
                    Log.d(LOG_TAG, "url2: "+url2);
                    Picasso.with(getActivity()).load(url2).placeholder(R.drawable.placeholder).into(_logo2);

                }

            }
            else{
                //hide all school 2 views
                _schoolName2.setVisibility(View.GONE);
                _rank2.setVisibility(View.GONE);
                _inState2.setVisibility(View.GONE);
                _outState2.setVisibility(View.GONE);
                _gradRate2.setVisibility(View.GONE);
                _enrollment2.setVisibility(View.GONE);
                _rating2.setVisibility(View.GONE);
                _logo2.setVisibility(View.GONE);
            }

        }

        return _rootLayout;
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
