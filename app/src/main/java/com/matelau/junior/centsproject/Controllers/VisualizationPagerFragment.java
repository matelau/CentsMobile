package com.matelau.junior.centsproject.Controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.matelau.junior.centsproject.Models.CentsAPIServices.UserService;
import com.matelau.junior.centsproject.Models.VizModels.MajorResponse;
import com.matelau.junior.centsproject.Views.VisualizationFragments.Career.SalaryChartFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.Career.UnemploymentFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving.CostOfLivingFragment;
import com.matelau.junior.centsproject.R;
import com.matelau.junior.centsproject.Views.VisualizationFragments.Career.CareerComparisonSummaryFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.Career.CareerIntroFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.College.CollegeComparisonSummary;
import com.matelau.junior.centsproject.Views.VisualizationFragments.College.CollegeIntroFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving.COLIntroFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving.COLSummaryFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving.LaborStatsFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving.OtherColFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving.TaxesFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving.WeatherFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.Major.MajorComparisonSummary;
import com.matelau.junior.centsproject.Views.VisualizationFragments.Major.MajorIntroFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.Major.TopJobsFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.SpendingBreakdown.SpendingBreakdownFragment;
import com.matelau.junior.centsproject.Views.VisualizationFragments.SpendingBreakdown.SpendingBreakdownIntroFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * PagerFragment used to show visualizations and summaries
 */
public class VisualizationPagerFragment extends Fragment {
    protected RelativeLayout _rootlayout;
    protected ViewPager _viewPager;
    protected FragmentStatePagerAdapter _pageAdapter;
    private String LOG_TAG = VisualizationPagerFragment.class.getSimpleName();


    public VisualizationPagerFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _rootlayout = (RelativeLayout) inflater.inflate(R.layout.fragment_visualization_pager2, container, false);
        _viewPager = new ViewPager(getActivity());
        _viewPager = (ViewPager) _rootlayout.findViewById(R.id.viewPager);
        CentsApplication.set_viewPager(_viewPager);
        //set adapter
        initialisePaging();
        //bind sliding tabs
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) _rootlayout.findViewById(R.id.tabs);
        tabs.setViewPager(_viewPager);
        return _rootlayout;
    }

    /**
     * Initialise the fragments to be paged based on selected vis
     */
    private void initialisePaging() {
        List<Fragment> fragments = new Vector<Fragment>();
        String selectedVis = CentsApplication.get_selectedVis();
        getActivity().getActionBar().setTitle(selectedVis);
        if(selectedVis == null){
            //return to examples
            CentsApplication.set_selectedVis("Examples");
            selectedVis = "Examples";
        }
        String completed = "";

        switch (selectedVis) {
            case "Career Comparison":
                completed = "View Career Comparison";
                fragments.add(Fragment.instantiate(getActivity(), CareerComparisonSummaryFragment.class.getName()));
                fragments.add(Fragment.instantiate(getActivity(), UnemploymentFragment.class.getName()));
                fragments.add(Fragment.instantiate(getActivity(), SalaryChartFragment.class.getName()));
//                fragments.add(Fragment.instantiate(getActivity(), CareerComparisonSummaryFragment.class.getName()));
                break;
            case "College Comparison":
                completed = "View College Comparison";
                fragments.add(Fragment.instantiate(getActivity(), CollegeComparisonSummary.class.getName()));
                break;
            case "Major Comparison":
                completed = "View Major Comparison";
                fragments.add(Fragment.instantiate(getActivity(), MajorComparisonSummary.class.getName()));
                //check for top jobs
                boolean addTJ = addTobJobs();
                if(addTJ){
                    fragments.add(Fragment.instantiate(getActivity(), TopJobsFragment.class.getName()));
                }
                break;
            case "COL Comparison":
                completed = "View City Comparison";
                getActivity().getActionBar().setTitle("City Comparison");
                fragments.add(Fragment.instantiate(getActivity(), COLSummaryFragment.class.getName()));
                fragments.add(Fragment.instantiate(getActivity(), CostOfLivingFragment.class.getName()));
                fragments.add(Fragment.instantiate(getActivity(), LaborStatsFragment.class.getName()));
                fragments.add(Fragment.instantiate(getActivity(), TaxesFragment.class.getName()));
                fragments.add(Fragment.instantiate(getActivity(), OtherColFragment.class.getName()));
                fragments.add(Fragment.instantiate(getActivity(), WeatherFragment.class.getName()));
                break;
            case "Spending Breakdown":
                completed = "View Spending Breakdown";
                fragments.add(Fragment.instantiate(getActivity(), SpendingBreakdownFragment.class.getName()));
//                fragments.add(Fragment.instantiate(getActivity(), SpendingBreakdownModDialogFragment.class.getName()));
                break;
            default:
                //load examples fragments
                completed = "Use Examples";
                fragments.add(Fragment.instantiate(getActivity(), COLIntroFragment.class.getName()));
                fragments.add(Fragment.instantiate(getActivity(), SpendingBreakdownIntroFragment.class.getName()));
                fragments.add(Fragment.instantiate(getActivity(), CollegeIntroFragment.class.getName()));
                fragments.add(Fragment.instantiate(getActivity(), CareerIntroFragment.class.getName()));
                fragments.add(Fragment.instantiate(getActivity(), MajorIntroFragment.class.getName()));
                break;
        }

        if(CentsApplication.is_loggedIN()){
            updateCompleted(completed);
        }
        //set fragments
        _pageAdapter = new PageAdapter(getActivity().getSupportFragmentManager(), fragments);
        _viewPager.setAdapter(_pageAdapter);
        CentsApplication.set_viewPager(_viewPager);
        //This line is required so the viewPager does not destroy pages when they are removed from the screen
        //if there are more tabs created this number will need to increase
        _viewPager.setOffscreenPageLimit(5);

    }

    /**
     * update the users completed section
     */
    private void updateCompleted(String completed){
        SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        int id = settings.getInt("ID", 0);
        UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
        HashMap<String,String> completedTask = new HashMap<String, String>();
        completedTask.put("section", completed);
        service.updateCompletedData(id, completedTask, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());

            }
        });
    }

    private boolean addTobJobs(){
        //check if any elements have
        List<MajorResponse.Element> elements = CentsApplication.get_mResponse().getElements();
        for(MajorResponse.Element current : elements){
            if(current.getJobs().size() > 0){
                return true;
            }
        }

        return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    /**
     * Adapter used to switch views inside the view pager based on user selections
     */
    private class PageAdapter extends FragmentStatePagerAdapter {
        // holds the list of current fragments used by the view pager
        private List<Fragment> _fragments;
        private String LOG_TAG = PageAdapter.class.getSimpleName();

        public PageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(getActivity().getSupportFragmentManager());
            _fragments = fragments;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position){
            _fragments.get(position).setRetainInstance(true);
            return _fragments.get(position);
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public int getCount() {
            return _fragments.size();
        }



        @Override
        /**
         * removes views no longer being shown
         */
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.d(LOG_TAG, "Destroy Item: "+object.toString());
            FragmentManager fm = ((Fragment) object).getFragmentManager();
            FragmentTransaction trans = fm.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();
        }



        @Override
        /**
         * Returns titles for pager sliding tabs
         */
        public CharSequence getPageTitle(int position) {
            String selectedVis = CentsApplication.get_selectedVis();
            Log.d(LOG_TAG, "SelectedVis:"+selectedVis+" - GetPageTitle");
            String[] tabTitles;
            switch (selectedVis) {
                case "Career Comparison":
                    tabTitles = new String[]{"Summary", "Unemployment", "Salary"};
                    return tabTitles[position];
                case "College Comparison":
                    tabTitles = new String[]{"Summary"};
                    return tabTitles[position];
                case "Major Comparison":
                    boolean addTJ = addTobJobs();
                    if(addTJ) {
                        tabTitles = new String[]{"Summary", "Top Jobs"};
                    }
                    else{
                        tabTitles = new String[]{"Summary"};
                    }
                    return tabTitles[position];
                case "COL Comparison":
                    tabTitles = new String[]{"Summary", "Cost of Living", "Labor Stats","Taxes","Other", "Weather"};
                    return tabTitles[position];
                case "Spending Breakdown":
                    tabTitles = new String[]{"Visualization"};
                    return tabTitles[position];
                default:
                    tabTitles = new String[]{"City Comparison","Spending Breakdown", "College Comparison","Career Comparison", "Major Comparison"};
                    return tabTitles[position];
            }
        }

    }
}
