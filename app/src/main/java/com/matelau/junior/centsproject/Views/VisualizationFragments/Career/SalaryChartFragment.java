package com.matelau.junior.centsproject.Views.VisualizationFragments.Career;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Models.VizModels.CareerResponse;
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
public class SalaryChartFragment extends Fragment {
    private RelativeLayout _rootView;
    private LineChartView chart;
    private LineChartData data;
    private int numberOfLines = 1;
    private int maxNumberOfLines = 2;
    private int numberOfPoints = 10;

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private CareerResponse _cResponse;
    private ImageButton _search;


    public SalaryChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_salary_chart, container, false);
        chart = (LineChartView) _rootView.findViewById(R.id.chart);
        _search = (ImageButton) _rootView.findViewById(R.id.imageSearchButton);
        _cResponse = CentsApplication.get_cResponse();
        List<CareerResponse.Element> elements = _cResponse.getElements();
        TextView loc1 = (TextView) _rootView.findViewById(R.id.career_title1);
        loc1.setText(elements.get(0).getName());
        TextView loc2 = (TextView) _rootView.findViewById(R.id.career_title2);
        boolean hasSecondCareer = false;
        if(elements.size() > 1){
            loc2.setText(elements.get(1).getName());
            hasSecondCareer = true;
            numberOfLines = 2;
        }
        else{
            loc2.setVisibility(View.GONE);
        }


        generateData(hasSecondCareer);

        _search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show city selection
                showCareerSelectionDialog();

            }
        });
        return _rootView;
    }

    private void showCareerSelectionDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CareerSelectionDialogFragment csd = new CareerSelectionDialogFragment();
        csd.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        csd.show(fm, "tag");
    }


    private void generateData(boolean hasSecondCareer) {
        List<CareerResponse.Element> elements = _cResponse.getElements();
        List<Double> career1Sal = elements.get(0).getCareerSalary();
        List<Double> career2Sal = null;
        if(hasSecondCareer)
            career2Sal = elements.get(1).getCareerSalary();

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {
            if( i == 0){
                List<PointValue> values = new ArrayList<PointValue>();
                for (int j = 0; j <= numberOfPoints; ++j) {
                    if(career1Sal.get(j) != null){
                        values.add(new PointValue(j, career1Sal.get(j).floatValue()));
                    }
                }
                Line line = new Line(values);

                line.setColor(getResources().getColor(R.color.compliment_primary));
                line.setShape(ValueShape.CIRCLE);
                line.setHasPoints(hasPoints);
                lines.add(line);
            }
            else{
                List<PointValue> values = new ArrayList<PointValue>();
                for (int j = 0; j <= numberOfPoints; ++j) {
                    if(career2Sal.get(j) != null){
                        values.add(new PointValue(j, career2Sal.get(j).floatValue()));

                    }
                }
                Line line = new Line(values);

                line.setColor(getResources().getColor(R.color.gray));
                line.setShape(ValueShape.SQUARE);
                line.setHasPoints(hasPoints);
                lines.add(line);
            }

        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            String[] years = {"2003", "2004","2005","2006","2007","2008","2009","2010","2011","2012","2013"};
            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            for(int i = 0; i < years.length; i++){
                AxisValue val = new AxisValue(i);
                val.setLabel(years[i]);
                axisValues.add(val);
            }

            axisX.setValues(axisValues);
            Axis axisY = new Axis().setHasLines(true).setHasTiltedLabels(true).setInside(true);
            if (hasAxesNames) {
                axisY.setName("Average National Salaries ($)");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

    }



}
