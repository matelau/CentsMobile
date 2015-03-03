package com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Controllers.SearchFragment;
import com.matelau.junior.centsproject.Models.Design.Col;
import com.matelau.junior.centsproject.Models.VizModels.ColiResponse;
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
    private static String _location;
    private static String _location2;
    private static Col _c1;
    private static Col _c2;
    private static String _city2;
    private ImageButton _search;
    private List<Col> _cols;
    private View rootView;

    public CostOfLivingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create visualizations!
        rootView = inflater.inflate(R.layout.fragment_cost_of_living, container, false);
        _cv = (CardView) rootView.findViewById(R.id.col_card_view);
        _search = (ImageButton) rootView.findViewById(R.id.imageSearchButton);

        _loc2 = (TextView) rootView.findViewById(R.id.col_location2);
        _chart = (ColumnChartView) rootView.findViewById(R.id.col_vis);
        updateLocation();

        if(CentsApplication.get_colResponse() != null)
            generateData();

        //update locations
        TextView loc1 = (TextView) rootView.findViewById(R.id.col_location1);
        loc1.setText(_location);
        processLocation2();
        _search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return search frag
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_placeholder, new SearchFragment());
                ft.commit();
               }
        });



        return rootView;
    }

    /**
     * Checks for a second location and updates UI accordingly
     */
    private void processLocation2() {
        if(_location2 == null){
            rootView.findViewById(R.id.second_location).setVisibility(View.GONE);

        }
        else{
            rootView.findViewById(R.id.second_location).setVisibility(View.VISIBLE);
            _loc2.setText(_location2);
            rootView.invalidate();
        }

    }

    /*
        Pulls up Dialog box to select a second location
     */
    public void showSecondCityDialog(){
        FragmentManager fm = getFragmentManager();
        CitySelectionDialogFragment csd = new CitySelectionDialogFragment();
        csd.setTargetFragment(this, 01);
        csd.show(fm, "CitySelection");
    }


    /**
     *  Sets vars for updated location
     */
    private void updateLocation(){
        ColiResponse cResponse = CentsApplication.get_colResponse();
        _location = cResponse.getLocation1();
        _location2 = cResponse.getLocation2();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLocation();
        generateData();
    }

    /**
     * builds visualization bar chart
     */
    private void generateData(){
        int numColumns = 7;

        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        String[] labels_short = {"gen", "housing", "trans", "groc", "util","health","goods"};

        double[] col_vals = listToDblArr(CentsApplication.get_colResponse().getCli1());
        double[] col_vals2 = listToDblArr(CentsApplication.get_colResponse().getCli2());


        List<AxisValue> axisVals = new ArrayList<AxisValue>();
        for(int i = 0; i < numColumns; i++){
            List<ColumnValue> values = new ArrayList<ColumnValue>();
            //normalize col_vals National avg = 0
            float column_value = (float) (col_vals[i] - 100f);
            int c;
            String label = "";
            if(column_value < 0) {
                label =  Math.abs(column_value) + "% below";
            }
            else{
                label = column_value + "% above";
            }
            //if column_value = 0 add a negligible value to display
            if(Math.round(column_value) == 0)
                column_value = 0.1f;
            ColumnValue cv = new ColumnValue(column_value, getResources().getColor(R.color.compliment_primary) );
            cv.setLabel(label.toCharArray());
            values.add(cv);
            //if user adds a comparison city add it to view
            if(col_vals2.length > 0){
                float column_value2 = (float) (col_vals2[i] - 100f);
                String label2 = "";
                if(column_value2 < 0) {
                    label2 =  Math.abs(column_value2) + "% below";
                }
                else{
                    label2 = column_value2 + "% above";
                }
                //if column_value = 0 add a negligible value to display
                if(Math.round(column_value2) == 0)
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
                axisX.setName(CentsApplication.get_colResponse().getLocation1());
                axisY.setName("");
            }
            _chartdata.setAxisXBottom(new Axis(axisVals));
            _chartdata.setAxisYLeft(axisY);
        } else {
            _chartdata.setAxisXBottom(null);
            _chartdata.setAxisYLeft(null);
        }

        _chartdata.setValueLabelTextSize(8);
        //make the bars smaller so they don't overfill chart
        _chartdata.setFillRatio(.7f);
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


    private double[] listToDblArr(List<Double> doubles){
        final double[] result = new double[doubles.size()];
        for(int i = 0; i<doubles.size(); i++){
            result[i] = doubles.get(i).doubleValue();
        }

        return result;
    }


}
