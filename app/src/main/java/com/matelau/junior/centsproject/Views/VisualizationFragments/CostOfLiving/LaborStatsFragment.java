package com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving;

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
import com.matelau.junior.centsproject.Models.VizModels.ColiResponse;
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
public class LaborStatsFragment extends Fragment {
    private String LOG_TAG = LaborStatsFragment.class.getSimpleName();
    private ComboLineColumnChartView chart;

    private ColiResponse _cResponse;
    private  Double _avgUnemployment;
    private Double _avgEGrowth;
    private final String[] _labels = {"Unemployment Rate", "Economic Growth"};

    public LaborStatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get Data
        _cResponse = CentsApplication.get_colResponse();
        List<ColiResponse.Element> elements = _cResponse.getElements();
        // set avgs
        _avgUnemployment = _cResponse.getLabor3().get(0);
        _avgEGrowth = _cResponse.getLabor3().get(2);

        Log.d(LOG_TAG, "Create View unemployment: " + _avgUnemployment + " Economic Growth: " + _avgEGrowth);
        setHasOptionsMenu(false);

        View _rootView = inflater.inflate(R.layout.fragment_labor_stats, container, false);
        ImageButton _search = (ImageButton) _rootView.findViewById(R.id.imageSearchButton);
        //update locations
        String _location = elements.get(0).getName();
        String _location2 = null;
        if(elements.size() > 1){
            _location2 = elements.get(1).getName();
        }
        TextView loc1 = (TextView) _rootView.findViewById(R.id.col_location1);
        TextView loc2 = (TextView) _rootView.findViewById(R.id.col_location2);
        loc1.setText(_location);
        if(_location2 != null){
            loc2.setText(_location2);
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
                showCitySelectionDialog();

            }
        });

        chart = (ComboLineColumnChartView) _rootView.findViewById(R.id.chart);

        generateData();

        return _rootView;
    }


    /**
     * Shows selection dialog
     */
    private void showCitySelectionDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CitySelectionDialogFragment csd = new CitySelectionDialogFragment();
        csd.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        csd.show(fm, "tag");
    }

    /**
     * generates data to be displayed in vis
     */
    private void generateData() {
        // Chart looks the best when line data and column data have similar maximum viewports.
        ComboLineColumnChartData data = new ComboLineColumnChartData(generateColumnData(), generateLineData());
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        //add labels to axis
        axisValues.add(new AxisValue(0, _labels[0].toCharArray() ));
        axisValues.add(new AxisValue(1, _labels[1].toCharArray() ));
        boolean hasAxes = true;
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
     * generates avg data for vis
     * @return
     */
    private LineChartData generateLineData() {

        List<Line> lines = new ArrayList<Line>();
        int numberOfLines = 1;
        for (int i = 0; i < numberOfLines; ++i) {
            List<PointValue> values = new ArrayList<PointValue>();
            int numberOfPoints = 2;
            for (int j = 0; j < numberOfPoints; ++j) {
                PointValue pt = new PointValue();
                if(j == 0){
                    pt.set(j, _avgUnemployment.floatValue());
                    pt.setLabel("");
                }
                else{
                    pt.set(j, _avgEGrowth.floatValue());
                    pt.setLabel("National Averages");
                }
                values.add(pt);
            }

            Line line = new Line(values);
            line.setColor(getResources().getColor(R.color.black));
            boolean isCubic = false;
            line.setCubic(isCubic);
            line.setHasLabels(true);
            boolean hasLines = true;
            line.setHasLines(hasLines);
            boolean hasPoints = true;
            line.setHasPoints(hasPoints);
            lines.add(line);
        }

        LineChartData lineChartData = new LineChartData(lines);

        return lineChartData;

    }

    private ColumnChartData generateColumnData() {
        int numSubcolumns = 1;
        List<ColiResponse.Element> elements = _cResponse.getElements();
        boolean hasSecondCity = false;
        if(elements.size() > 1){
            numSubcolumns = 2;
            hasSecondCity = true;
        }
        int numColumns = 2;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                SubcolumnValue sc = new SubcolumnValue();
                //i = 0 unemployment
                String label = "";
                if(i == 0 && j == 0){
                    sc.setColor(getResources().getColor(R.color.compliment_primary));
                    if(elements.get(0).getLabor().get(0) != null){
                        float val = elements.get(0).getLabor().get(0).floatValue();
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
                    if(elements.get(1).getLabor().get(0) != null){
                        float val = elements.get(1).getLabor().get(0).floatValue();
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

                //i = 1 economic growth
                if(i == 1 && j == 0){
                    sc.setColor(getResources().getColor(R.color.compliment_primary));
                    if(elements.get(0).getLabor().get(2) != null){
                        float val = elements.get(0).getLabor().get(2).floatValue();
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
                    if(elements.get(1).getLabor().get(2) != null){
                        float val =elements.get(1).getLabor().get(2).floatValue();
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
