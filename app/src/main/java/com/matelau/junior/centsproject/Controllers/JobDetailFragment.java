package com.matelau.junior.centsproject.Controllers;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.matelau.junior.centsproject.Models.GlassdoorAPIModels.Employer;
import com.matelau.junior.centsproject.Models.GlassdoorAPIModels.GlassdoorResults;
import com.matelau.junior.centsproject.Models.GlassdoorAPIModels.GlassdoorService;
import com.matelau.junior.centsproject.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class JobDetailFragment extends Fragment {
    String LOG_TAG = JobDetailFragment.class.getSimpleName();
//    final String baseUrl="https://ajax.googleapis.com/ajax/services/search/images?v=1.0&safe=on&=&rsz=1&q=g=";
//    &as_sitesearch=wikimedia.org
    private ImageView _logoView;
    private AQuery aq;
    private String _img_url;
    private View _rootView;
    private Picasso pic;
    private ProgressBar _pbar;
    private TextView _companyTextView;
    private TextView _companyRating;
    private TextView _companyRatingDescr;


    private final Handler _handler = new Handler();
    private final Runnable _updateResults =  new Runnable() {
        @Override
        public void run() {
            updateUI();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        _rootView = inflater.inflate(R.layout.fragment_job_detail, container, false);
        _logoView = (ImageView) _rootView.findViewById(R.id.company_logo);
        aq = new AQuery(getActivity());
        pic = Picasso.with(getActivity());
        pic.setIndicatorsEnabled(true);



        loadGlassdoorData();


        //TODO build out detail view
        return _rootView;


    }



    public void loadGlassdoorData(){
        String company = getCompanyString();
        String location = CentsApplication.get_searchedCity().toLowerCase().replace(' ', '+')+"+"+CentsApplication.get_searchState().toLowerCase().replace(' ', '+');
        Map<String, String> qMap = new HashMap<String,String>();
        qMap.put("action", "employers");
        qMap.put("t.p", "27350");
        qMap.put("t.k", "irqFFDe0Rvo");
        qMap.put("format", "json");
        qMap.put("v", "1");
        qMap.put("useragent", "Mozilla");
        qMap.put("userip", "0.0.0.0");
        qMap.put("l",location);
        qMap.put("q",company);

        GlassdoorService service = CentsApplication.get_gdRestAdapter().create(GlassdoorService.class);
        service.listResults(qMap, new Callback<GlassdoorResults>() {
            @Override
            public void success(GlassdoorResults glassdoorResults, Response response) {
                Log.v(LOG_TAG, "Glassdoor Results in");
                //TODO pull values out of gd  results that I need
                List<Employer> employers = glassdoorResults.response.employers;
                _img_url = employers.get(0).squareLogo.toString().trim();
                _img_url = "http://i.imgur.com/MUfvGRq.jpg";
                if(_img_url != null){
                    updateUI();
                }

                float rating = employers.get(0).overallRating;
                _companyRating.setText("Overall rating: "+rating+" out of 5.0");


//                Toast.makeText(getActivity(), "Rating: " + rating, Toast.LENGTH_SHORT).show();
                String ratingDesc = employers.get(0).ratingDescription;
                _companyRatingDescr.setText(ratingDesc);

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());

            }
        });
    }


    private void updateUI(){
        //load image
        Log.v(LOG_TAG, "Loading img: "+_img_url);
        aq.id(_logoView.getId()).progress(R.id.progress_bar).image(_img_url,true,true);//, 0,AQuery.FADE_IN,AQuery.RATIO_PRESERVE);
        //set data
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                _companyTextView.setText("Company: "+ JobDetailActivity._company);
                _companyTextView.invalidate();
                _rootView.invalidate();
                Log.v(LOG_TAG, "Company Text: "+_companyTextView.getText());
            }
        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.add("Refresh");
        inflater.inflate(R.menu.menu_job_detail,menu);
//        return true;


//        menu.
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(LOG_TAG, "invalidating views");
        int id = item.getItemId();

//            _rootView.invalidate();
//        _logoView.invalidate();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(_img_url !=null){
            Log.v(LOG_TAG, "Loading img: "+_img_url);
            //TODO persist data from on create and load here
            aq.id(_logoView.getId()).image(_img_url);
        }

    }

    private String getCompanyString(){
//        String company = JobDetailActivity._company;
//        company = company.replace(' ', '+').replace("llc","").replace("inc","").replace(",","").replace(".","").toLowerCase();
        return "";

    }


}
