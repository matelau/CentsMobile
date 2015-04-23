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
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {
    private String LOG_TAG = WeatherFragment.class.getSimpleName();
    private LineChartView chart;
    private LineChartData data;
    private int numberOfLines = 12;
    private int numberOfPoints = 2;
    private boolean hasAxes = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = true;
    private boolean hasLabelForSelected = true;

    private ColiResponse _colResponse;

    private static String _location;
    private static String _location2;
    private ImageButton _search;
    private View _rootView;
    private static String[] months = {"JAN", "FEB", "MAR", "APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};


    public WeatherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        _rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        _search = (ImageButton) _rootView.findViewById(R.id.imageSearchButton);

        _colResponse = CentsApplication.get_colResponse();
        List<ColiResponse.Element> elements = _colResponse.getElements();
        chart = (LineChartView) _rootView.findViewById(R.id.chart);
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
     * generates data to be displayed in vis
     */
    private void generateData()
    {
        List<ColiResponse.Element> elements = _colResponse.getElements();
        List<Double> weather1 = elements.get(0).getWeather();
        List<Double> weather1_low = elements.get(0).getWeatherlow();
        List<Double> weather2 = null;
        List<Double>weather2_low = null;
        boolean hasSecondCity = false;
        if(elements.size() > 1){
            weather2 = elements.get(1).getWeather();
            weather2_low = elements.get(1).getWeatherlow();
            hasSecondCity = true;
        }
        List<Line> lines = new ArrayList<Line>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        //add city 1 weather
        for (int i = 0; i < numberOfLines*2; i+=2) {

            List<PointValue> values = new ArrayList<PointValue>();
            //create axis labels
            if(i < 12){
                AxisValue aVal = new AxisValue(i*2, months[i].toCharArray());
                axisValues.add(aVal);
            }

            for (int j = 0; j < numberOfPoints; ++j) {
                int index = i/2;
                float value = 0.0f;
                if(j == 0){
                    value = weather1.get(index).floatValue();
                }

                else{
                    value = weather1_low.get(index).floatValue();
                }
                PointValue pv = new PointValue(i , value);
                pv.setLabel((int) value+"°F");
                values.add(pv);
            }

            Line line = new Line(values);
            line.setColor(getResources().getColor(R.color.compliment_primary));
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            lines.add(line);
        }
        //add city 2
        if(hasSecondCity){
            //offset bounds so values are not in same columns
            for (int i = 1; i < numberOfLines*2; i+=2) {
                List<PointValue> values = new ArrayList<PointValue>();
                for (int j = 0; j < numberOfPoints; ++j) {
                    int index = (i - 1)/2;
                    float value = 0.0f;
                    if(j == 0){
                        value = weather2.get(index).floatValue();
                    }

                    else{
                        value = weather2_low.get(index).floatValue();
                    }
                    PointValue pv = new PointValue(i , value);
                    pv.setLabel((int)value+"°F");
                    values.add(pv);
                }

                Line line = new Line(values);
                line.setColor(getResources().getColor(R.color.gray));
                line.setShape(shape);
                line.setCubic(isCubic);
                line.setFilled(isFilled);
                line.setHasLabels(hasLabels);
                line.setHasLabelsOnlyForSelected(hasLabelForSelected);
                line.setHasLines(hasLines);
                line.setHasPoints(hasPoints);
                lines.add(line);
            }
        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(true).setName("High and Low Temperature Range");
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

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
