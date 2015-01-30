package com.matelau.junior.centsproject.Controllers;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.matelau.junior.centsproject.Views.VisualizationFragments.CareerComparisonSummary;
import com.matelau.junior.centsproject.R;

import java.util.List;
import java.util.Vector;

/**
 * PagerFragment used to show visualizations and summaries
 */
public class VisualizationPagerFragment extends Fragment {
    protected RelativeLayout _rootlayout;
    protected ViewPager _viewPager;
    protected FragmentPagerAdapter _pageAdapter;
    private String LOG_TAG = VisualizationPagerFragment.class.getSimpleName();


    public VisualizationPagerFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _rootlayout = (RelativeLayout) inflater.inflate(R.layout.fragment_visualization_pager2, container, false);
        _viewPager = (ViewPager) _rootlayout.findViewById(R.id.viewPager);
        //set adapter
        initialisePaging();
        //bind sliding tabs
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) _rootlayout.findViewById(R.id.tabs);
        tabs.setViewPager(_viewPager);

        return _rootlayout;
    }

    /**
     * Initialise the fragments to be paged based on
     */
    private void initialisePaging() {
        List<Fragment> fragments = new Vector<Fragment>();
        String selectedVis = CentsApplication.get_selectedVis();
//        String title = selectedVis.replace("Comparison", " Comparison");
        getActivity().getActionBar().setTitle(selectedVis);
        Log.d(LOG_TAG, "InitialisePaging - SelectedVis: "+selectedVis);
        Toast.makeText(getActivity(), "Loading Vis: "+selectedVis, Toast.LENGTH_SHORT).show();
        //TODO load fragments based on user selections
        switch (selectedVis){
            case "Career Comparison":
                fragments.add(Fragment.instantiate(getActivity(), CareerComparisonSummary.class.getName()));
                fragments.add(Fragment.instantiate(getActivity(), CareerComparisonSummary.class.getName()));
                break;
//            case "College Comparison":
//                break;
//            case "Major Comparison":
//                break;
//            case "COL Comparison":
//                break;
//            case "Spending Breakdown":
//                break;
            default:
                //TODO switch to examples fragments
                fragments.add(Fragment.instantiate(getActivity(), CareerComparisonSummary.class.getName()));
                fragments.add(Fragment.instantiate(getActivity(), CareerComparisonSummary.class.getName()));
                break;
        }


        _pageAdapter  = new PageAdapter(getActivity().getSupportFragmentManager(), fragments);
        _viewPager.setAdapter(_pageAdapter);
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



    private class PageAdapter extends FragmentPagerAdapter{

        private List<Fragment> _fragments;

        public PageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(getActivity().getSupportFragmentManager());
            _fragments = fragments;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return _fragments.get(position);
        }

        @Override
        public int getCount() {
            return _fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //TODO find a better way to determine proper fragment page titles
            return "Test:"+position;
        }
    }

}
