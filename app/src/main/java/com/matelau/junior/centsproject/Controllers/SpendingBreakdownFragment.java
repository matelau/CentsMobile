package com.matelau.junior.centsproject.Controllers;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.matelau.junior.centsproject.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.ArcValue;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.util.Utils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 */
public class SpendingBreakdownFragment extends Fragment {
    private ImageButton _back;
    private PieChartView _chart;
    private TextView _occupation;
    private PieChartData data;
    private boolean hasLabels = false;
    private boolean hasLabelsOutside = true;
    private boolean hasCenterCircle = true;
    private boolean hasCenterText1 = true;
    private boolean hasCenterText2 = true;
    private boolean isExploaded = false;
    private boolean hasArcSeparated = false;
    private boolean hasLabelForSelected = true;



    public SpendingBreakdownFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_spending_breakdown, container, false);
        CardView cv = (CardView) rootView.findViewById(R.id.spending_card_view);
         _chart = (PieChartView) rootView.findViewById(R.id.spending_vis);
        _occupation = (TextView) rootView.findViewById(R.id.sd_occ);

        _occupation.setText(CentsApplication.get_searchedOccupation());

        _back = (ImageButton) rootView.findViewById(R.id.go_to_col);
        _back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                _back.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate));
                Intent colIntent = new Intent(getActivity(), CostOfLivingActivity.class);
                startActivity(colIntent);
                return true;
            }
        });

        generateData();

        // Inflate the layout for this fragment
        return rootView;
    }

    public void generateData(){
        int numValues = 10;
        //default values
        int salary = 27450; // national median 2013
        String sSalary = ""+salary;
        if(CentsApplication.get_occupationSalary() != null){
            sSalary = CentsApplication.get_occupationSalary();
            //insure valid int is returned if not reset vals
            try{
                salary = Integer.parseInt(sSalary);
            }catch(NumberFormatException e){
                e.printStackTrace();
                salary = 27450;
                sSalary =""+ salary;
                _occupation.setText("Natl Median Wage");
            }

        }

        String[] labels = {"housing", "food", "transportation", "utilities","student loans","other debt", "insurance","savings","health","misc"};
        float[] percents = {.25f, .20f,.08f, .05f, .08f,.11f,.06f,.07f,.03f,.07f};
        int[] colors = {Color.RED,Color.DKGRAY,Color.MAGENTA, Color.BLUE, Color.CYAN,Color.LTGRAY, Color.GREEN,  Color.BLACK,Color.YELLOW,Color.argb(255,170,90,12)};
        List<ArcValue> values = new ArrayList<ArcValue>();
        for (int i = 0; i < numValues; ++i) {
            ArcValue arcValue = new ArcValue(percents[i], colors[i]);
            if (isExploaded) {
                arcValue.setArcSpacing(24);
            }

            if (hasArcSeparated && i == 0) {
                arcValue.setArcSpacing(10);
            }
            float portion = percents[i] * salary;
            String label = labels[i]+" "+portion;
            values.add(arcValue.setLabel(label.toCharArray()));
        }

        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);

        if (hasCenterText1) {
            data.setCenterText1("Annual Spending");

//            // Get roboto-italic font.
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
            data.setCenterText1Typeface(tf);
            data.setCenterText1Color(getResources().getColor(R.color.primary));
            data.setCenterText1FontSize(Utils.px2sp(getResources().getDisplayMetrics().scaledDensity,28));
//
//            // Get font size from dimens.xml and convert it to sp(library uses sp values).
//            data.setCenterText1FontSize(Utils.px2sp(getResources().getDisplayMetrics().scaledDensity,
//                    (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
        }

        if (hasCenterText2) {
            data.setCenterText2("based on salary: "+sSalary);

            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");

            data.setCenterText2Typeface(tf);
            data.setCenterText2FontSize(Utils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    20));
        }

        _chart.setPieChartData(data);

    }

}
