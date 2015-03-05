package com.matelau.junior.centsproject.Views.VisualizationFragments;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Models.VizModels.MajorResponse;

/**
 * Created by matelau on 3/5/15.
 */
public class SummaryAdapter extends BaseAdapter {
    private String LOG_TAG = SummaryAdapter.class.getSimpleName();
    private int _type = -1;
    private MajorResponse _mResponse;

    public SummaryAdapter(int type){
        //Set Type -  0 COL Sum, 1 Major Sum, 2 College Sum, 3 Career Sum
        _type = type;
        switch(_type){
            case 1:
                //get MajorResponse
                _mResponse = CentsApplication.get_mResponse();
                break;
            default:
                break;
        }
    }

    @Override
    public int getCount() {
        switch(_type){
            case 1:
                return _mResponse.getJobs1().size();
            default:
                return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
