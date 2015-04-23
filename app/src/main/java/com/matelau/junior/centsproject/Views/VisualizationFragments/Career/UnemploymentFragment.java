package com.matelau.junior.centsproject.Views.VisualizationFragments.Career;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Models.VizModels.CareerResponse;
import com.matelau.junior.centsproject.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnemploymentFragment extends Fragment {
    //ntl average 2011 - 6.0, 2012 - 6.8
    private String LOG_TAG = UnemploymentFragment.class.getSimpleName();
    private ComboLineColumnChartView chart;
    private ComboLineColumnChartData data;

    private int numberOfLines = 1;
    private int numberOfPoints = 2;

    private boolean hasAxes = true;
    private boolean hasPoints = true;
    private boolean hasLines = true;
    private boolean isCubic = false;
    private CareerResponse _cResponse;
    private  Double _2011Unemployment;
    private Double _2012Unemployment;
    private final String[] _labels = {"Unemployment Rate 2011", "Unemployment Rate 2012"};

    private static String _career1;
    private static String _career2;
    private ImageButton _search;
    private View _rootView;

    public UnemploymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get Data
        _cResponse = CentsApplication.get_cResponse();
        // set avgs
        _2011Unemployment = _cResponse.getCareerUnemploy3().get(0);
        _2012Unemployment = _cResponse.getCareerUnemploy3().get(1);

        Log.d(LOG_TAG, "Create View unemployment 2011: " + _2011Unemployment + " unemployment 2012: " + _2012Unemployment);
        setHasOptionsMenu(false);

        _rootView = inflater.inflate(R.layout.fragment_labor_stats, container, false);
        _search = (ImageButton) _rootView.findViewById(R.id.imageSearchButton);
        //update locations
        List<CareerResponse.Element> elements = _cResponse.getElements();
        _career1 = elements.get(0).getName();
        if(_career1.length() > 32){
            _career1 = _career1.substring(0,32)+"...";
        }
        _career2 = null;
        if(elements.size() != 1){
            _career2 = elements.get(1).getName();
            if(_career2.length() > 32){
                _career2 = _career2.substring(0,32)+"...";
            }
        }

        TextView loc1 = (TextView) _rootView.findViewById(R.id.col_location1);
        TextView loc2 = (TextView) _rootView.findViewById(R.id.col_location2);
        loc1.setText(_career1);
        if(_career2 != null){
            loc2.setText(_career2);
        }
        else{
            //remove second loc views
            loc2.setVisibility(View.GONE);
            _rootView.findViewById(R.id.second_location).setVisibility(View.VISIBLE);

        }

        _search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show city selection
                showCareerSelectionDialog();

            }
        });

        chart = (ComboLineColumnChartView) _rootView.findViewById(R.id.chart);
        generateData(elements);

        return _rootView;
    }

    /**
     * Show selection dialog
     */
    private void showCareerSelectionDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CareerSelectionDialogFragment csd = new CareerSelectionDialogFragment();
        csd.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        csd.show(fm, "tag");
    }


    /**
     * Generates data for vis
     * @param elements
     */
    private void generateData(List<CareerResponse.Element> elements) {
        // Chart looks the best when line data and column data have similar maximum viewports.
        data = new ComboLineColumnChartData(generateColumnData(elements), generateLineData());
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        //add labels to axis
        axisValues.add(new AxisValue(0, _labels[0].toCharArray() ));
        axisValues.add(new AxisValue(1, _labels[1].toCharArray() ));
        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(true);
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        chart.setComboLineColumnChartData(data);
    }


    /**
     * Generates national avg point data for vis
     * @return
     */
    private LineChartData generateLineData() {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                PointValue pt = new PointValue();
                if(j == 0){
                    pt.set(j, _2011Unemployment.floatValue());
                    pt.setLabel("");
                }
                else{
                    pt.set(j, _2012Unemployment.floatValue());
                    pt.setLabel("National Averages");
                }
                values.add(pt);
            }

            Line line = new Line(values);
            line.setColor(getResources().getColor(R.color.black));
            line.setCubic(isCubic);
            line.setHasLabels(true);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            lines.add(line);
        }

        LineChartData lineChartData = new LineChartData(lines);

        return lineChartData;

    }

    /**
     * Generates unemployment column data for vis
     * @param elements
     * @return
     */
    private ColumnChartData generateColumnData(List<CareerResponse.Element> elements) {
        int numSubcolumns = 1;
        List<Double> unemploy1 = elements.get(0).getCareerUnemploy();
        List<Double> unemploy2 = null;
        if(elements.size() > 1){
            numSubcolumns = 2;
            unemploy2 = elements.get(1).getCareerUnemploy();
        }

        int numColumns = 2;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                SubcolumnValue sc = new SubcolumnValue();
                //i = 0 unemployment 2011
                String label = "";
                if(i == 0 && j == 0){
                    sc.setColor(getResources().getColor(R.color.compliment_primary));
                    if(unemploy1.get(0) != null){
                        float val = unemploy1.get(0).floatValue();
                        if(val == 0.0f){
                            val = 0.1f;
                            label = "0%";
                        }
                        else{
                            label = val+"%";

                        }
                        sc.setValue(val);
                        sc.setLabel(label);
                    }
                    else{
                        sc.setValue(0.0f);
                    }
                }
                else if (numSubcolumns == 2 && i == 0  && j == 1){
                    sc.setColor( getResources().getColor(R.color.gray));
                    if(unemploy2.get(0) != null){
                        float val = unemploy2.get(0).floatValue();
                        if(val == 0.0f){
                            val = 0.1f;
                            label = "0%";
                        }
                        else{
                            label = val+"%";

                        }
                        sc.setValue(val);
                        sc.setLabel(label);
                    }
                    else
                        sc.setValue(0.0f);
                }

                //i = 1 unemployment 2012
                if(i == 1 && j == 0){
                    sc.setColor(getResources().getColor(R.color.compliment_primary));
                    if(unemploy1.get(1) != null){
                        float val = unemploy1.get(1).floatValue();
                        if(val == 0.0f){
                            val = 0.1f;
                            label = "0%";
                        }
                        else{
                            label = val+"%";
                        }
                        sc.setValue(val);
                        sc.setLabel(label);
                    }
                    else{
                        sc.setValue(0.0f);
                    }
                }
                else if (numSubcolumns == 2 && i == 1  && j == 1){
                    sc.setColor( getResources().getColor(R.color.gray));
                    if(unemploy2.get(1)  != null){
                        float val = unemploy2.get(1).floatValue();
                        if(val == 0.0f){
                            val = 0.1f;
                            label = "0%";
                        }
                        else{
                            label = val+"%";
                        }
                        sc.setValue(val);
                        sc.setLabel(label);
                    }
                    else{
                        sc.setValue(0.0f);
                    }
                }

                values.add(sc);
            }
            Column c = new Column(values);
            c.setHasLabels(true);
            c.setHasLabelsOnlyForSelected(true);
            columns.add(c);
        }

        ColumnChartData columnChartData = new ColumnChartData(columns);
        columnChartData.setFillRatio(.3f);
        return columnChartData;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Destroyed");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Resumed");
    }


}
