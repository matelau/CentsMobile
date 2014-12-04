package com.matelau.junior.centsproject.Controllers;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        TextView tv = (TextView) rootView.findViewById(R.id.col_location);
        String location = CentsApplication.get_searchedCity()+", "+CentsApplication.get_searchState();
        tv.setText(location);
        return rootView;
        //TODO draw a line across data points @100
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
            float column_value = Float.parseFloat(col_vals[i]) - 100f;
            col_vals[i] = ""+ column_value;
            //if pos val black else red
            int c;
            if(column_value < 0)
                c = Color.BLACK;
            else
                c = Color.RED;
            ColumnValue cv = new ColumnValue(column_value, c);
            cv.setLabel(col_vals[i].toCharArray());
            values.add(cv);
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
            axisVals.add(new AxisValue(i,labels_short[i].toCharArray()));
        }

        _chartdata = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName(_c.getLocation());
                axisY.setName("% diff of Natl. Average (0)");
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
