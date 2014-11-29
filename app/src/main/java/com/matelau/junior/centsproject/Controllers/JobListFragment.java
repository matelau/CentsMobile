package com.matelau.junior.centsproject.Controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koushikdutta.ion.Ion;
import com.matelau.junior.centsproject.Models.JobInfo;
import com.matelau.junior.centsproject.R;
import com.matelau.junior.centsproject.Views.JobListRecycleAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
public class JobListFragment extends Fragment {
    private RecyclerView _recyclerView;
    private LinearLayoutManager _recyclerLayoutMan;
    private JobListRecycleAdapter _recyclerAdapter;
    private String LOG_TAG = JobListFragment.class.getSimpleName();
    private Ion _ion;
    private Map<String, String> _companyImg = new HashMap<String,String>();
    private String[] urls = new String[25];
    private int _urlsIndex = 0;
    private List<JobInfo> _jl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        Log.v(LOG_TAG, "On CreateView");
        View rootView = inflater.inflate(R.layout.fragment_job_list, container, false);
        _recyclerView = (RecyclerView) rootView.findViewById(R.id.job_recycler_view);
        _recyclerView.setHasFixedSize(true);
        _recyclerLayoutMan = new LinearLayoutManager(getActivity());
        _recyclerLayoutMan.setOrientation(LinearLayoutManager.VERTICAL);
        _recyclerView.setLayoutManager(_recyclerLayoutMan);


       _jl = CentsApplication.get_jobSearchResultList();
        //Check if list is empty and supply place holder values to adapter.
        if(_jl.size() == 0){

            JobInfo ji = new JobInfo("Please Try Again", "Search Returned No Results", null);
            //try again image?
            ji.jobLogoUrl ="http://www.okiwoki.com/images/produits/jeux-video/try-again-pulls-noir-h-l_1.jpg";
//            jl.clear();
            _jl.add(ji);

        }

        _recyclerAdapter = new JobListRecycleAdapter(_jl, getActivity());
        _recyclerView.setAdapter(_recyclerAdapter);


//        _recyclerView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
//            @Override
//            public void onSwipeRight() {
//                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
//                startActivity(mainIntent);
//            }
//        });

//       _recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//           @Override
//           public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
//               return false;
//           }
//
//           @Override
//           public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
//               recyclerView.findViewById(R.id.job_company);
//
//               Intent jobDetailIntent = new Intent(getActivity(), JobDetailActivity.class);
////               jobDetailIntent.putExtra("Company",)
//               startActivity(jobDetailIntent);
//
//           }
//       });


        return rootView;

    }








}
