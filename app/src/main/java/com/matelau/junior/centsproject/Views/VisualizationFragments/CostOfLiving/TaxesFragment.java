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
public class TaxesFragment extends Fragment {
    private String LOG_TAG = LaborStatsFragment.class.getSimpleName();
    private ComboLineColumnChartView chart;
    private ComboLineColumnChartData data;

    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 3;

    private boolean hasAxes = true;
    private boolean hasAxesNames = false;
    private boolean hasPoints = true;
    private boolean hasLines = true;
    private boolean isCubic = false;
    private boolean hasLabels = false;
    private ColiResponse _cResponse;
    private  Double _avgSales;
    private Double _avgMin;
    private Double _avgMax;
    private Double _avgProperty;
    private final String[] _labels = {"SALES TAX", "MIN - INCOME", "MAX - INCOME"};

    private static String _location;
    private static String _location2;
    private ImageButton _search;
    private View _rootView;

    public TaxesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get Data
        _cResponse = CentsApplication.get_colResponse();
        List<ColiResponse.Element> elements = _cResponse.getElements();
        List<Double> taxes = _cResponse.getTaxes3();
        // set avgs
        _avgSales = taxes.get(0);
        _avgMin = taxes.get(1);
        _avgMax = taxes.get(2);

        Log.d(LOG_TAG, "Create View unemployment: " + _avgSales + " Economic Growth: " + _avgMin);
        setHasOptionsMenu(false);

        _rootView = inflater.inflate(R.layout.fragment_labor_stats, container, false);
        _search = (ImageButton) _rootView.findViewById(R.id.imageSearchButton);
        //update locations
        _location = elements.get(0).getName();
        _location2 = null;
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
     * Shows the selection dialog
     */
    private void showCitySelectionDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CitySelectionDialogFragment csd = new CitySelectionDialogFragment();
        csd.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        csd.show(fm, "tag");
    }


    /**
     * generates data for vis
     */
    private void generateData() {
        // Chart looks the best when line data and column data have similar maximum viewports.
        data = new ComboLineColumnChartData(generateColumnData(), generateLineData());
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        //add labels to axis
        axisValues.add(new AxisValue(0, _labels[0].toCharArray() ));
        axisValues.add(new AxisValue(1, _labels[1].toCharArray() ));
        axisValues.add(new AxisValue(2, _labels[2].toCharArray() ));
        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(true).setName("Tax Rates");
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        chart.setComboLineColumnChartData(data);
    }


    /**
     * generates avg tax data to be displayed in vis
     * @return
     */
    private LineChartData generateLineData() {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                PointValue pt = new PointValue();
                if(j == 0){
                    pt.set(j, _avgSales.floatValue());
                    pt.setLabel("");
                }
                else if( j == 1){
                    pt.set(j, _avgMin.floatValue());
                    pt.setLabel("");
                }
                else{
                    pt.set(j, _avgMax.floatValue());
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
     * generates tax data to be displayed in vis
     * @return
     */
    private ColumnChartData generateColumnData() {
        List<ColiResponse.Element> elements = _cResponse.getElements();
        List<Double> taxes1 = elements.get(0).getTaxes();
        List<Double> taxes2 = null;
        int numSubcolumns = 1;
        if(elements.size() > 1){
            numSubcolumns = 2;
            taxes2 = elements.get(1).getTaxes();
        }
        int numColumns = 3;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                SubcolumnValue sc = new SubcolumnValue();
                //i = 0 sales tax
                String label = "";
                if(i == 0 && j == 0){
                    sc.setColor(getResources().getColor(R.color.compliment_primary));
                    if(taxes1.get(0) != null){
                        float val = taxes1.get(0).floatValue();
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
                    if(taxes2.get(0)!= null){
                        float val = taxes2.get(0).floatValue();
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
                //i = 1 min income
                if(i == 1 && j == 0){
                    sc.setColor(getResources().getColor(R.color.compliment_primary));
                    if(taxes1.get(1) != null){
                        float val = taxes1.get(1).floatValue();
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
                    if(taxes2.get(1) != null){
                        float val = taxes2.get(1).floatValue();
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

                //i = 1 max income
                if(i == 2 && j == 0){
                    sc.setColor(getResources().getColor(R.color.compliment_primary));
                    if(taxes1.get(2) != null){
                        float val = taxes1.get(2).floatValue();
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
                else if (numSubcolumns == 2 && i == 2  && j == 1){
                    sc.setColor( getResources().getColor(R.color.gray));
                    if(taxes2.get(2) != null){
                        float val = taxes2.get(2).floatValue();
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
        columnChartData.setFillRatio(.5f);
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
