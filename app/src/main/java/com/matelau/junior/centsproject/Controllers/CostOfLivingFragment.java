package com.matelau.junior.centsproject.Controllers;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.matelau.junior.centsproject.Models.Col;
import com.matelau.junior.centsproject.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ColumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * create an instance of this fragment.
 */
public class CostOfLivingFragment extends Fragment {

    CardView _cv;
    ColumnChartView _chart;
    ColumnChartData _chartdata;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = true;
    private boolean hasLabelForSelected = false;
    private Col _c;
    private ImageButton _back;
    private ImageButton _forward;
    public CostOfLivingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create visualizations!
        View rootView = inflater.inflate(R.layout.fragment_cost_of_living, container, false);
        _cv = (CardView) rootView.findViewById(R.id.col_card_view);


        _chart = (ColumnChartView) rootView.findViewById(R.id.col_vis);
        _c = CentsApplication.get_c();
        if(_c != null)
            generateData();

         _back = (ImageButton) rootView.findViewById(R.id.go_to_jobList);
        _back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                _back.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate));
                Intent jLIntent = new Intent(getActivity(), JobListActivity.class);
                startActivity(jLIntent);

                return true;
            }
        });

        _forward = (ImageButton) rootView.findViewById(R.id.go_to_spending);
        _forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                _forward.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate));
                Intent sBIntent = new Intent(getActivity(), SpendingBreakdown.class);
                startActivity(sBIntent);
                return true;
            }
        });

        TextView tv = (TextView) rootView.findViewById(R.id.col_location);
        String location = CentsApplication.get_searchedCity()+", "+CentsApplication.get_searchState();
        tv.setText(location);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //check to see if selection has been updated
        _c = CentsApplication.get_c();
        if(_c != null)
            generateData();
    }

    private void generateData(){
        int numSubcolumns = 1;
        int numColumns = 7;

        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        String[] labels_long = {"overall", "Housing","Transportation","Groceries", "Utilties", "Health Care", "Goods & Services"};
        String[] labels_short = {"overall", "housing", "trans", "groc", "util","health","goods"};
        String[] col_vals = {_c.getCost_of_living(), _c.getHousing(), _c.getTransportation(), _c.getGroceries(),_c.getUtilities(),_c.getHealth_care(),_c.getGoods()};


        List<AxisValue> axisVals = new ArrayList<AxisValue>();
        for(int i = 0; i < numColumns; i++){
            List<ColumnValue> values = new ArrayList<ColumnValue>();
            float column_value = Float.parseFloat(col_vals[i]);
            ColumnValue cv = new ColumnValue(column_value, getResources().getColor(R.color.primary_dark));
            cv.setLabel(col_vals[i].toCharArray());
            values.add(cv);
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
            axisVals.add(new AxisValue(i,labels_short[i].toCharArray()));
        }

//        ColumnValue col_cv = new ColumnValue( col, Utils.pickColor());
//
//
//
//
//        ColumnValue housing_cv = new ColumnValue(housing, Utils.pickColor());
//
//        ColumnValue trans_cv = new ColumnValue(transportation, Utils.pickColor());
//
//        ColumnValue groc_cv = new ColumnValue(groceries, Utils.pickColor());
//
//        ColumnValue util_cv = new ColumnValue(utilities, Utils.pickColor());
//
//        ColumnValue hc_cv = new ColumnValue(healthCare, Utils.pickColor());
//
//        ColumnValue goods_cv = new ColumnValue(goods, Utils.pickColor());
////            cv.setLabel()
//            values.add(new ColumnValue(goods, Utils.pickColor()));
//
//

//        }

        _chartdata = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName(_c.getLocation());
                axisY.setName("Percentage of Natl. Average");
            }
            _chartdata.setAxisXBottom(new Axis(axisVals));
            _chartdata.setAxisYLeft(axisY);
        } else {
            _chartdata.setAxisXBottom(null);
            _chartdata.setAxisYLeft(null);
        }

        _chart.setColumnChartData(_chartdata);
    }


}
