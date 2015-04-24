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

    private List<String> job_titles1 = new ArrayList<String>();
    private List<String> job_titles2 = new ArrayList<String>();
    private List<Double> job_Salaries1 = new ArrayList<Double>();
    private List<Double> job_Salaries2 = new ArrayList<Double>();

    public TopJobsFragment(){
        //Empty Constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get Data
        MajorResponse _mResponse = CentsApplication.get_mResponse();
        List<MajorResponse.Element> elements = _mResponse.getElements();
        boolean secondMajor = false;
        if(elements.size() > 1){
            secondMajor = true;
        }

        getTitlesAndVals(secondMajor, elements);

        //normalize data
        if(job_titles1.size() < 3){
            normalize(job_titles1, job_Salaries1);
        }
        if(secondMajor && job_titles2.size() < 3){
            normalize(job_titles2, job_Salaries2);
        }

        Log.d(LOG_TAG, "Create View top Jobs: Jobs List size1: " + job_titles1.size() + " Jobs List size1: " + job_titles2.size());
        setHasOptionsMenu(false);

        View _rootView = inflater.inflate(R.layout.fragment_top_jobs, container, false);
        ImageButton _search = (ImageButton) _rootView.findViewById(R.id.imageSearchButton);
        //update locations
        String _major1 = elements.get(0).getName();
        if(_major1.length() > 32){
            _major1 = _major1.substring(0, 30)+"...";
        }
        String _major2;
        if(secondMajor){
            _major2 = elements.get(1).getName();
            if(_major2.length() > 32){
                _major2 = _major2.substring(0, 30)+"...";
            }
        }
        else{
            _major2 = null;
        }

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

    /**
     * Sets values for titles and salaries
     * @param secondMajor
     * @param elements
     */
    private void getTitlesAndVals(boolean secondMajor, List<MajorResponse.Element> elements){
        List<String> jobs1 = elements.get(0).getJobs();
        List<String> jobs2 = null;
        if(secondMajor){jobs2 = elements.get(1).getJobs();}

        for(int i = 0 ; i< jobs1.size(); i++){
            //titles
            if(i % 2 == 0){
                String title = jobs1.get(i);
                if(title != null)
                    job_titles1.add(title);
                }

            else{
                    Double salary = Double.parseDouble(jobs1.get(i));
                    job_Salaries1.add(salary);
                }
        }
        if(jobs2 != null){
            for(int i = 0 ; i< jobs2.size(); i++){
                //titles
                if(i % 2 == 0){
                    String title = jobs2.get(i);
                    if(title != null)
                        job_titles2.add(title);
                }
                else{
                    Double salary = Double.parseDouble(jobs2.get(i));
                    job_Salaries2.add(salary);
                }
            }
        }

    }


    /**
     * Insures Tob Job array has at least 3 elements for vis
     * @param job_titles
     * @param salaries
     */
    private void normalize(List<String> job_titles, List<Double> salaries) {
        int prevLength = job_titles.size();
        for(int i = prevLength; i < 3; i++){
            job_titles.add("N/A");
            salaries.add(0.0);
        }
    }

    /**
     * Shows Selection dialog
     */
    private void showMajorSelectionDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        MajorSelectionDialogFragment csd = new MajorSelectionDialogFragment();
        csd.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        csd.show(fm, "tag");
    }

    /**
     * Generates data from models for vis
     */
    private void generateData() {
        ComboLineColumnChartData data = new ComboLineColumnChartData(generateColumnData(), generateLineData());
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        boolean hasAxes = true;
        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(true).setInside(true).setName("Avg. Salary").setHasTiltedLabels(true);
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        }
        chart.setComboLineColumnChartData(data);
    }


    /**
     * Generate National salary data points for vis
     * @return
     */
    private LineChartData generateLineData() {
        List<Line> lines = new ArrayList<Line>();
        int numberOfLines = 1;
        for (int i = 0; i < numberOfLines; ++i) {
            List<PointValue> values = new ArrayList<PointValue>();
            int numberOfPoints = 3;
            for (int j = 0; j < numberOfPoints; ++j) {
                PointValue pt = new PointValue();
                Double _avgSalary = 44888.16;
                pt.set(j, _avgSalary.floatValue());
                pt.setLabel("");
                if(j == 1){
                    pt.setLabel("National Avg. Salary");
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
     * Generate job model data for vis
     * @return
     */
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
