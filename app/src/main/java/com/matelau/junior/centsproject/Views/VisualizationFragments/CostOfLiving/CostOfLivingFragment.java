package com.matelau.junior.centsproject.Views.VisualizationFragments.CostOfLiving;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
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
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

//import lecho.lib.hellocharts.util.Utils;


/**
 * create an instance of this fragment.
 */
public class CostOfLivingFragment extends Fragment{
    private String LOG_TAG = CostOfLivingFragment.class.getSimpleName();

    CardView _cv;
    ColumnChartView _chart;
    ColumnChartData _chartdata;
    private boolean hasLabels = true;
    private boolean hasLabelForSelected = true;
    private TextView _loc2;
    private static String _location;
    private static String _location2;
    private ImageButton _search;
    private View _rootView;
    private final String[] _labels_short = {"Overall", "Goods", "grocery", "health", "housing","trans","util"};

    public CostOfLivingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "CreateView");
        // create visualizations!
        _rootView = inflater.inflate(R.layout.fragment_cost_of_living, container, false);
        _cv = (CardView) _rootView.findViewById(R.id.col_card_view);
        _search = (ImageButton) _rootView.findViewById(R.id.imageSearchButton);

        _loc2 = (TextView) _rootView.findViewById(R.id.col_location2);
        _chart = (ColumnChartView) _rootView.findViewById(R.id.col_vis);
        updateLocation();

        if(CentsApplication.get_colResponse() != null)
            generateData();

        //update locations
        TextView loc1 = (TextView) _rootView.findViewById(R.id.col_location1);
        loc1.setText(_location);
        processLocation2();
        _search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show city selection
                showCitySelectionDialog();

               }
        });

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
     * Checks for a second location and updates UI accordingly
     */
    private void processLocation2() {
        if(_location2 == null){
            _rootView.findViewById(R.id.second_location).setVisibility(View.GONE);

        }
        else{
            _rootView.findViewById(R.id.second_location).setVisibility(View.VISIBLE);
            _loc2.setText(_location2);
            _rootView.invalidate();
        }

    }


    /**
     *  Sets vars for updated location
     */
    private void updateLocation(){
        ColiResponse cResponse = CentsApplication.get_colResponse();
        List<ColiResponse.Element> elements = cResponse.getElements();
        _location = elements.get(0).getName();
        if(elements.size() > 1)
            _location2 = elements.get(1).getName();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Resumed");
        updateLocation();
        generateData();
    }

    /**
     * builds visualization bar chart
     */
    private void generateData(){
        int numColumns = 7;

        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<ColiResponse.Element> elements = CentsApplication.get_colResponse().getElements();

        double[] col_vals = listToDblArr(elements.get(0).getCli());
        double[] col_vals2 = {};
        if(elements.size() > 1){
            col_vals2 = listToDblArr(elements.get(1).getCli());
        }

        List<AxisValue> axisVals = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for(int i = 0; i < numColumns; i++){
            values = new ArrayList<SubcolumnValue>();
            //normalize col_vals National avg = 0
            float column_value = (float) (col_vals[i] - 100f);
            int c;
            String label = "";
            if(column_value < 0) {
                label =  (int)Math.abs(column_value) + "% below";
            }
            else{
                label = (int)column_value + "% above";
            }
            //if column_value = 0 add a negligible value to display
            if(Math.round(column_value) == 0)
                column_value = 0.1f;

            SubcolumnValue cv = new SubcolumnValue(column_value, getResources().getColor(R.color.compliment_primary));
            cv.setLabel(label);
            values.add(cv);
            //if user adds a comparison city add it to view
            if(col_vals2.length > 0){
                float column_value2 = (float) (col_vals2[i] - 100f);
                String label2 = "";
                if(column_value2 < 0) {
                    label2 =  (int)Math.abs(column_value2) + "% below";
                }
                else{
                    label2 = (int)column_value2 + "% above";
                }
                //if column_value = 0 add a negligible value to display
                if(Math.round(column_value2) == 0)
                    column_value2 = 0.1f;
                SubcolumnValue cv2 = new SubcolumnValue(column_value2, getResources().getColor(R.color.primary_gray));
                cv2.setLabel(label2);
                values.add(cv2);
            }

            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
            AxisValue currentAxis = new AxisValue(i);
            currentAxis.setLabel(_labels_short[i].toUpperCase());
            axisVals.add(currentAxis);
        }

        _chartdata = new ColumnChartData(columns);

        Axis axisX = new Axis(axisVals);
        axisX.setTextSize(8);
        Axis axisY = new Axis().setHasLines(true);
        axisY.setName("");
        _chartdata.setAxisXBottom(axisX);
        _chartdata.setAxisYLeft(axisY);

        _chartdata.setValueLabelTextSize(9);
        //make the bars smaller so they don't overfill chart
        _chartdata.setFillRatio(.7f);
        _chart.setColumnChartData(_chartdata);
    }


    /**
     * converts double list to array
     * @param doubles
     * @return
     */
    private double[] listToDblArr(List<Double> doubles){
        final double[] result = new double[doubles.size()];
        for(int i = 0; i<doubles.size(); i++){
            result[i] = doubles.get(i).doubleValue();
        }

        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Destroyed");
    }

}
