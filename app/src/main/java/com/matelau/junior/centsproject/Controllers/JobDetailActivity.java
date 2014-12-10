package com.matelau.junior.centsproject.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.matelau.junior.centsproject.Models.GlassdoorAPIModels.Employer;
import com.matelau.junior.centsproject.Models.GlassdoorAPIModels.GlassdoorResults;
import com.matelau.junior.centsproject.Models.GlassdoorAPIModels.GlassdoorService;
import com.matelau.junior.centsproject.Models.GlassdoorInfo;
import com.matelau.junior.centsproject.R;
import com.matelau.junior.centsproject.Views.JobDetailsRecycleAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class JobDetailActivity extends Activity {
    private String LOG_TAG = JobDetailActivity.class.getSimpleName();
    private String _company;
    private String _title;
    private String _jobUrl;
    private ImageView _logoView;
    private String _img_url;
    private View _rootView;
    private Picasso pic;
    private ProgressBar _pbar;
    private String _compSite;
    private Button _glassdoorBtn;
    private Button _indeedBtn;
    private Button  _siteButton;


    private RecyclerView _recyclerView;
    private LinearLayoutManager _recyclerLayoutMan;
    private JobDetailsRecycleAdapter _recyclerAdapter;
    private List<GlassdoorInfo> _gi = new ArrayList<GlassdoorInfo>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        //set back button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        _company =  i.getCharSequenceExtra("Company").toString();
        _title = i.getCharSequenceExtra("Title").toString();
        _jobUrl = i.getStringExtra("url");
        setContentView(R.layout.fragment_job_detail);
        _rootView =  findViewById(R.id.job_detail_view);
        _logoView = (ImageView) _rootView.findViewById(R.id.company_logo);
        pic = Picasso.with(this);
        _pbar  = (ProgressBar) _rootView.findViewById(R.id.progress_bar);
        _glassdoorBtn = (Button) _rootView.findViewById(R.id.button);
        _glassdoorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.glassdoor.com/index.htm";
                if(_company != null){
                    String comp = _company.replace(' ','-').toLowerCase();
                    url ="http://www.glassdoor.com/Reviews/"+comp+"-reviews-SRCH_KE0,4.htm";
                }
                Intent glassDoor = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(glassDoor);
            }
        });

        _siteButton = (Button) _rootView.findViewById(R.id.company_site);
        _siteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insure http prepends site
                String http = "http://";
                if(_compSite.indexOf("http://") < 0)
                    _compSite = http+_compSite;
                Intent openCompSite = new Intent(Intent.ACTION_VIEW, Uri.parse(_compSite));
                startActivity(openCompSite);

            }
        });


        _indeedBtn  = (Button) _rootView.findViewById(R.id.button2);
        _indeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_jobUrl == null){
                    Intent glassDoor = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.indeed.com/"));
                    startActivity(glassDoor);

                }
                else{
                    Intent glassDoor = new Intent(Intent.ACTION_VIEW, Uri.parse(_jobUrl));
                    startActivity(glassDoor);
                }

            }
        });


        _recyclerView = (RecyclerView) _rootView.findViewById(R.id.job_details);
        _recyclerView.setHasFixedSize(true);
        _recyclerLayoutMan = new LinearLayoutManager(this);
        _recyclerLayoutMan.setOrientation(LinearLayoutManager.VERTICAL);
        _recyclerView.setLayoutManager(_recyclerLayoutMan);
        _recyclerAdapter = new JobDetailsRecycleAdapter(_gi, this);
        _recyclerView.setAdapter(_recyclerAdapter);


        loadGlassdoorData();

    }

    private String getCompanyString(){
        String company = _company;
        company = company.replace(' ', '+').replace("llc","").replace("inc","").replace(",","").replace(".","").toLowerCase();
        return company;
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
                // pull values out for display
                //TODO check for valid response
                List<Employer> employers = glassdoorResults.response.employers;
                if(employers.size() != 0){
                    _img_url = employers.get(0).squareLogo.toString().trim();
                    if(_img_url != null && _img_url.length() != 0){
                        updateUI();
                    }

                    Employer currEmployer = employers.get(0);
                    if(currEmployer != null && currEmployer.numberOfRatings != 0){
                        _gi.add(new GlassdoorInfo("Company", _company));
                        float rating = currEmployer.overallRating;
                        String denom = "/5.0";
                        _gi.add(new GlassdoorInfo("Overall rating", rating+denom));
                        String detail = currEmployer.cultureAndValuesRating;
                        //"cultureAndValuesRating": "3.6",
                        _gi.add(new GlassdoorInfo("Culture and Values", detail+denom));
                        //"seniorLeadershipRating": "2.9",
                        detail = currEmployer.seniorLeadershipRating;
                        _gi.add(new GlassdoorInfo("Senior Leadership", detail+denom));
                        //"compensationAndBenefitsRating": "2.6",
                        detail = currEmployer.compensationAndBenefitsRating;
                        _gi.add(new GlassdoorInfo("Compensation and Benefits", detail+denom));
                        //"careerOpportunitiesRating": "2.9",
                        detail = currEmployer.careerOpportunitiesRating;
                        _gi.add(new GlassdoorInfo("Career Opportunities", detail+denom));
                        // "workLifeBalanceRating": "3.5"
                        detail = currEmployer.workLifeBalanceRating;
                        _gi.add(new GlassdoorInfo("Work Life Balance", detail+denom));
                        detail = currEmployer.ratingDescription;
                        _gi.add(new GlassdoorInfo("Employees Feel", detail));
                        detail = currEmployer.industry;
                        _gi.add(new GlassdoorInfo("Industry", detail));
                        detail = currEmployer.numberOfRatings+"";
                        _gi.add(new GlassdoorInfo("Number of Ratings", detail));
                        _compSite = currEmployer.website;
                        updateAdapterUI();

                    }
                    else{
                       noResults();
                    }

                }
                else
                    noResults();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());

            }
        });
    }

    private void noResults(){
        _gi.clear();
        _gi.add(new GlassdoorInfo("Glassdoor Info","No Results"));
        _siteButton.setVisibility(View.GONE);
        updateAdapterUI();
    }

    private void updateAdapterUI(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _recyclerAdapter = new JobDetailsRecycleAdapter(_gi, getApplicationContext());
                _recyclerView.setAdapter(_recyclerAdapter);
                _recyclerView.invalidate();
            }
        });

    }

    private void updateUI(){
        //load image
        Log.v(LOG_TAG, "Loading img: "+_img_url);
        //, 0,AQuery.FADE_IN,AQuery.RATIO_PRESERVE);
        //set data
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pic.load(_img_url).fit().into(_logoView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        _pbar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        Log.e(LOG_TAG, "Error Loading Logo: "+_img_url);

                    }
                });
//                Log.v(LOG_TAG, "Company Text: " + _companyTextView.getText());
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_job_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //reset extra search results
        CentsApplication.set_searchedCity2(null);
        CentsApplication.set_searchState2(null);
        //always return to main
        Intent returnToSearch = new Intent(this, MainActivity.class);
        startActivity(returnToSearch);
        return true;
    }
}
