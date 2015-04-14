package com.matelau.junior.centsproject.Views.VisualizationFragments.Major;


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
import com.matelau.junior.centsproject.Models.VizModels.MajorResponse;
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
public class TopJobsFragment extends Fragment {
    private String LOG_TAG = TopJobsFragment.class.getSimpleName();
    private ComboLineColumnChartView chart;
    private ComboLineColumnChartData data;

    private int numberOfLines = 1;
    private int numberOfPoints = 3;

    private boolean hasAxes = true;
    private boolean hasAxesNames = false;
    private boolean hasPoints = true;
    private boolean hasLines = true;
    private boolean isCubic = false;
    private boolean hasLabels = false;
    private MajorResponse _mResponse;
    private Double _avgSalary = 44888.16;
    private List<String> job_titles1 = new ArrayList<String>();
    private List<String> job_titles2 = new ArrayList<String>();
    private List<Double> job_Salaries1 = new ArrayList<Double>();
    private List<Double> job_Salaries2 = new ArrayList<Double>();



    private static String _major1;
    private static String _major2;
    private ImageButton _search;
    private View _rootView;

    public TopJobsFragment(){
        //Empty Constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get Data
        _mResponse = CentsApplication.get_mResponse();
        boolean secondMajor = false;
        if(_mResponse.getName_2() != null){
            secondMajor = true;
        }

        getTitlesAndVals(secondMajor);

        //normalize data
        if(job_titles1.size() < 3){
            normalize(job_titles1, job_Salaries1);
        }
        if(secondMajor && job_titles2.size() < 3){
            normalize(job_titles2, job_Salaries2);
        }

        Log.d(LOG_TAG, "Create View top Jobs: Jobs List size1: " + job_titles1.size() + " Jobs List size1: " + job_titles2.size());
        setHasOptionsMenu(false);

        _rootView = inflater.inflate(R.layout.fragment_top_jobs, container, false);
        _search = (ImageButton) _rootView.findViewById(R.id.imageSearchButton);
        //update locations
        _major1 = _mResponse.getName_1();
        _major2 = _mResponse.getName_2();
        TextView major1Name = (TextView) _rootView.findViewById(R.id.major1);
        TextView major2Name = (TextView) _rootView.findViewById(R.id.major2);
        major1Name.setText(_major1);
        if(_major2 != null){
            major2Name.setText(_major2);
        }
        else{
            //remove second loc views
            major2Name.setVisibility(View.GONE);
            _rootView.findViewById(R.id.second_location).setVisibility(View.VISIBLE);

        }

        _search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show selection
                showMajorSelectionDialog();
            }
        });

        chart = (ComboLineColumnChartView) _rootView.findViewById(R.id.chart);
        generateData();

        return _rootView;
    }

    private void getTitlesAndVals(boolean secondMajor){
        for(int i = 0 ; i< _mResponse.getJobs1().size(); i++){
            //titles
            if(i % 2 == 0){
                String title = (String) _mResponse.getJobs1().get(i);
                if(title != null)
                    job_titles1.add(title);
                if(_mResponse.getJobs2().size() > 0 && secondMajor){
                    String title2 = (String) _mResponse.getJobs2().get(i);
                    if(title2 != null)
                        job_titles2.add(title2);
                }
            }
            else{
                Double salary = (Double) _mResponse.getJobs1().get(i);
                if(salary != null)
                    job_Salaries1.add(salary);
                if(_mResponse.getJobs2().size() > 0 && secondMajor){
                    Double salary2 = (Double) _mResponse.getJobs2().get(i);
                    if(salary2 != null){
                        job_Salaries2.add(salary2);
                    }
                }
            }
        }
    }

    private void normalize(List<String> job_titles, List<Double> salaries) {
        int prevLength = job_titles.size();
        for(int i = prevLength; i < 3; i++){
            job_titles.add("N/A");
            salaries.add(0.0);
        }
    }

    private void showMajorSelectionDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        MajorSelectionDialogFragment csd = new MajorSelectionDialogFragment();
        csd.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        csd.show(fm, "tag");
    }

    private void generateData() {
        data = new ComboLineColumnChartData(generateColumnData(), generateLineData());
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
//        axisValues
        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(true).setInside(true).setName("Avg. Salary").setHasTiltedLabels(true);
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        }
        chart.setComboLineColumnChartData(data);
    }

    private LineChartData generateLineData() {
        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                PointValue pt = new PointValue();
                pt.set(j, _avgSalary.floatValue());
                pt.setLabel("");
                if(j == 1){
                    pt.setLabel("National Avg. Salary");
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

    private ColumnChartData generateColumnData() {
        int numSubcolumns = 1;
        if(job_titles2.size() > 0){
            numSubcolumns = 2;
        }
        int numColumns = 3;
        if(job_titles1.size() > job_titles2.size()){
            numColumns = job_titles1.size();
        }
        else{
            numColumns = job_titles2.size();
        }

        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                SubcolumnValue sc = new SubcolumnValue();
                if (j == 0) {
                    String label = "";
                    sc.setColor(getResources().getColor(R.color.compliment_primary));
                    float val = job_Salaries1.get(i).floatValue();
                    if (val == 0.0f) {
                        val = 0.1f;
                        label = "N/A";
                    } else {
                        label = job_titles1.get(i);
                    }
                    sc.setValue(val);
                    if(label.length() > 30){
                        label = label.substring(0,28)+"...";
                    }
                    sc.setLabel(label);
                }
                else {
                    String label = "";
                    sc.setColor(getResources().getColor(R.color.gray));
                    float val = job_Salaries2.get(i).floatValue();
                    if (val == 0.0f) {
                        val = 0.1f;
                        label = "N/A";
                    } else {
                        label = job_titles2.get(i);
                    }
                    sc.setValue(val);
                    if(label.length() > 30){
                        label = label.substring(0,28)+"...";
                    }
                    sc.setLabel(label);
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

}
