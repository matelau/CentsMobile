package com.matelau.junior.centsproject.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
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
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.util.Utils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * create an instance of this fragment.
 */
public class CostOfLivingFragment extends Fragment{
    private String LOG_TAG = CostOfLivingFragment.class.getSimpleName();

    CardView _cv;
    ColumnChartView _chart;
    ColumnChartData _chartdata;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = true;
    private boolean hasLabelForSelected = true;
    private TextView _loc2;
    private String _location;
    private String _location2;
    private Col _c1;
    private Col _c2;
    private String _city2;
    private ImageButton _plusBtn;
    private List<Col> _cols;
    private View rootView;

    public CostOfLivingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create visualizations!
        _location = CentsApplication.get_searchedCity()+", "+CentsApplication.get_searchState();
        rootView = inflater.inflate(R.layout.fragment_cost_of_living, container, false);
        _cv = (CardView) rootView.findViewById(R.id.col_card_view);
        _plusBtn = (ImageButton) rootView.findViewById(R.id.plus_icon);
        _loc2 = (TextView) rootView.findViewById(R.id.col_location2);


        _chart = (ColumnChartView) rootView.findViewById(R.id.col_vis);
        _c1 = getCol(CentsApplication.get_searchedCity());
        if(_c1 != null)
            generateData();

        //update locations
        TextView loc1 = (TextView) rootView.findViewById(R.id.col_location1);
        loc1.setText(_location);
        validateLocation2();

        _plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //start Animation
                _plusBtn.startAnimation((AnimationUtils.loadAnimation(getActivity(), R.anim.rotate)));
                //TODO show Popup
                showSecondCityDialog();
            }
        });

        return rootView;
    }

    private void validateLocation2() {
        if(_location2 == null){
            rootView.findViewById(R.id.second_location).setVisibility(View.GONE);

        }
        else{
            rootView.findViewById(R.id.second_location).setVisibility(View.VISIBLE);
            _loc2.setText(_location2);
            //remove plus icon
            ViewGroup viewGroup = (ViewGroup) _plusBtn.getParent();
            viewGroup.removeView(_plusBtn);
            //update layout
            rootView.invalidate();
//            _plusBtn.setVisibility(View.GONE);

        }

    }


    public void showSecondCityDialog(){
       FragmentManager fm = getFragmentManager();
//        getFragmentManager()
        SecondCityDialogFragment secondCity = new SecondCityDialogFragment();
        secondCity.setTargetFragment(this, 01);
        secondCity.show(fm, "tag");


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        _plusBtn.clearAnimation();
        //check if valid result is returned
        if(CentsApplication.get_searchedCity2() != null){
            _city2 = CentsApplication.get_searchedCity2();
            _location2 = _city2+", "+CentsApplication.get_searchState2();
            Log.v(LOG_TAG, "Location 2: " + _location2);

            //set the second col data
            _c2 = getCol(_city2);
            //restore Second Text View and remove + btn
            validateLocation2();
            //update display
            generateData();


        }
    }


    public Col getCol(String city){
        if(_cols == null ){
            _cols = CentsApplication.get_cols();
        }
        for(Col c: _cols){
            if(c.getLocation().equals(city)){
                return c;
            }
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        _location = CentsApplication.get_searchedCity()+", "+CentsApplication.get_searchState();
        if(CentsApplication.get_searchedCity2() != null) {
            _city2 = CentsApplication.get_searchedCity2();
            _location2 = _city2 + ", " + CentsApplication.get_searchState2();
            _c2 = getCol(_city2);
        }
        //check to see if selection has been updated
        _c1 = getCol(CentsApplication.get_searchedCity());
        if(_c1 != null)
            generateData();
        validateLocation2();
    }

    private void generateData(){
        int numSubcolumns = 1;
        int numColumns = 7;

        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
//        String[] labels_long = {"|yyy|", "|yyy|","|yyy|","Groceries costs are |yyy|", "Utilities costs are |yyy|", "H.C. costs are |yyy|", "G&S costs are |yyy|"};
        String[] labels_short = {"overall", "housing", "trans", "groc", "util","health","goods"};
        String[] col_vals = {_c1.getCost_of_living(), _c1.getHousing(), _c1.getTransportation(), _c1.getGroceries(), _c1.getUtilities(), _c1.getHealth_care(), _c1.getGoods()};
        String[] col_vals2 = null;
        if(_c2 != null){
            col_vals2 = new String[]{_c2.getCost_of_living(), _c2.getHousing(), _c2.getTransportation(), _c2.getGroceries(), _c2.getUtilities(), _c2.getHealth_care(), _c2.getGoods()};
        }


        List<AxisValue> axisVals = new ArrayList<AxisValue>();
        for(int i = 0; i < numColumns; i++){
            List<ColumnValue> values = new ArrayList<ColumnValue>();
            //normalize col_vals National avg = 0
            float column_value = Float.parseFloat(col_vals[i]) - 100f;
            int c;
            String label = "";
            if(column_value < 0) {
                label =  Math.abs(column_value) + "% below";
            }
            else{
                label = column_value + "% above";
            }
            //if column_value = 0 add a negligible value to display
            if(column_value == 0f)
                column_value = 0.1f;
            ColumnValue cv = new ColumnValue(column_value, getResources().getColor(R.color.compliment_primary) );
            cv.setLabel(label.toCharArray());
            values.add(cv);
            //if user adds a comparison city add it to view
            if(col_vals2 != null){
                float column_value2 = Float.parseFloat(col_vals2[i]) - 100f;
                String label2 = "";
                if(column_value2 < 0) {
                    label2 =  Math.abs(column_value2) + "% below";
                }
                else{
                    label2 = column_value2 + "% above";
                }
                //if column_value = 0 add a negligible value to display
                if(column_value2 == 0f)
                    column_value2 = 0.1f;
                ColumnValue cv2 = new ColumnValue(column_value2, getResources().getColor(R.color.primary_gray) );
                cv2.setLabel(label2.toCharArray());
                values.add(cv2);
            }

            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
            axisVals.add(new AxisValue(i,labels_short[i].toUpperCase().toCharArray()));
        }

        _chartdata = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName(_c1.getLocation());
                axisY.setName("Cost of Living(%) : National Avg. = 0");
            }
            _chartdata.setAxisXBottom(new Axis(axisVals));
            _chartdata.setAxisYLeft(axisY);
        } else {
            _chartdata.setAxisXBottom(null);
            _chartdata.setAxisYLeft(null);
        }

        _chart.setColumnChartData(_chartdata);
    }

    private LineChartData generateLineData() {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < 1; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < 7; ++j) {
                values.add(new PointValue(j, 100f));
            }

            Line line = new Line(values);
            line.setColor(Utils.COLORS[i]);
            line.setCubic(false);
            line.setHasLabels(hasLabels);
            line.setHasLines(true);
            line.setHasPoints(false);
            lines.add(line);
        }

        LineChartData lineChartData = new LineChartData(lines);

        return lineChartData;

    }


}
