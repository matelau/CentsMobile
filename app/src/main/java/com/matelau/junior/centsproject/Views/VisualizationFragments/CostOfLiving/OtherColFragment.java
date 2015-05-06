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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
public class OtherColFragment extends Fragment {
    private String LOG_TAG = LaborStatsFragment.class.getSimpleName();
    private ComboLineColumnChartView chart;

    private ColiResponse _cResponse;
    private  Double _avgSalary;
    private Double _avgProperty;
    private final String[] _labels = {"AVERAGE SALARY", "PROPERTY TAX"};

    public OtherColFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get Data
        _cResponse = CentsApplication.get_colResponse();
        List<ColiResponse.Element> elements = _cResponse.getElements();
        // set avgs
        _avgSalary = _cResponse.getLabor3().get(1);
        _avgProperty = _cResponse.getTaxes3().get(3);

        Log.d(LOG_TAG, "Create View AVG SALARY: " + _avgSalary + " PROPERTY TAX " + _avgProperty);
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
     * Show selection dialog
     */
    private void showCitySelectionDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CitySelectionDialogFragment csd = new CitySelectionDialogFragment();
        csd.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        csd.show(fm, "tag");
    }

    /**
     * Generate data to be used by vis
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
            Axis axisY = new Axis().setHasLines(true).setInside(true).setName("Amount in USD").setHasTiltedLabels(true);
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);

        }
        chart.setComboLineColumnChartData(data);
    }

    /**
     * generate avg data for vis
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
                    pt.set(j, _avgSalary.floatValue());
                    pt.setLabel("National Averages");

                }
                else{
                    pt.set(j, _avgProperty.floatValue());
                    pt.setLabel("");
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

    /**
     * generate salary and tax data for vis
     * @return
     */
    private ColumnChartData generateColumnData() {
        int numSubcolumns = 1;
        List<ColiResponse.Element> elements=  _cResponse.getElements();
        if(elements.size() > 1){
            numSubcolumns = 2;
        }
        int numColumns = 2;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                SubcolumnValue sc = new SubcolumnValue();
                //i = 0 avg salary
                String label = "";
                if(i == 0 && j == 0){
                    sc.setColor(getResources().getColor(R.color.compliment_primary));
                    if(elements.get(0).getLabor().get(1) != null){
                        float val = elements.get(0).getLabor().get(1).floatValue();
                        if(val == 0.0f){
                            val = 0.1f;
                            label = "$0";
                        }
                        else{
                            label = "$"+ NumberFormat.getNumberInstance(Locale.US).format((int)val);

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
                    if(elements.get(1).getLabor().get(1) != null){
                        float val =elements.get(1).getLabor().get(1).floatValue();
                        if(val == 0.0f){
                            val = 0.1f;
                            label = "$0";
                        }
                        else{
                            label = "$"+NumberFormat.getNumberInstance(Locale.US).format((int)val);

                        }
                        sc.setValue(val);
                        sc.setLabel(label);
                    }
                    else
                        sc.setValue(0.0f);
                }

                //i = 1 property tax
                if(i == 1 && j == 0){
                    sc.setColor(getResources().getColor(R.color.compliment_primary));
                    if(elements.get(0).getTaxes().get(3) != null){
                        float val = elements.get(0).getTaxes().get(3).floatValue();
                        if(val == 0.0f){
                            val = 0.1f;
                            label = "$0";
                        }
                        else{
                            label = "$"+NumberFormat.getNumberInstance(Locale.US).format((int)val);

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
                    if(elements.get(1).getTaxes().get(3) != null){
                        float val = elements.get(1).getTaxes().get(3).floatValue();
                        if(val == 0.0f){
                            val = 0.1f;
                            label = "$0";
                        }
                        else{
                            label = "$"+NumberFormat.getNumberInstance(Locale.US).format((int)val);

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
