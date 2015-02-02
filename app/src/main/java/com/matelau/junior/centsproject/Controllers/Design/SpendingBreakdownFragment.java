package com.matelau.junior.centsproject.Controllers.Design;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
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
    private boolean hasLabels = true;
    private boolean hasLabelsOutside = true;
    private boolean hasCenterCircle = true;
    private boolean hasCenterText1 = true;
    private boolean hasCenterText2 = true;
    private boolean hasArcSeparated = false;
    private boolean hasLabelForSelected = false;
    private int _height = 800; // default val



    public SpendingBreakdownFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_spending_breakdown, container, false);
        CardView cv = (CardView) rootView.findViewById(R.id.spending_card_view);
         _chart = (PieChartView) rootView.findViewById(R.id.spending_vis);
        _occupation = (TextView) rootView.findViewById(R.id.spending_desc);
        //modify circle text to be x percentage in size based on view height
        _height = container.getHeight();
        _occupation.setText(CentsApplication.get_searchedOccupation());


        generateData();

        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * Build Pie Chart
     */
    public void generateData(){
        int numValues = 10;
        //default values
        float salary = 27450; // national median 2013
        String sSalary = ""+salary;
        if(CentsApplication.get_occupationSalary() != null){
            sSalary = CentsApplication.get_occupationSalary();
            //insure valid int is returned if not reset vals
            try{
                salary = Integer.parseInt(sSalary);
                salary = salary/12f;
            }catch(NumberFormatException e){
                e.printStackTrace();
                sSalary =""+ salary;
                salary = 27450/12f;
                _occupation.setText("Natl Median Wage");
            }

        }
        //only show two decimal places in
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);
        String[] labels = {"housing", "food", "transportation", "utilities","student loans","other debt", "insurance","savings","health","misc"};
        float[] percents = {.25f, .20f,.08f, .05f, .08f,.11f,.06f,.07f,.03f,.07f};
        //Percents from Wesley - Food 17, Housing 25, Utilities 6, Transportation 12, Healthcare 5, Insurance 8, Student/Credit Debt 12, Savings 10, Misc 5
//        int[] colors = {Color.RED,Color.DKGRAY,Color.MAGENTA, Color.BLUE, Color.CYAN,Color.LTGRAY, Color.GREEN,  Color.BLACK,Color.YELLOW,Color.argb(255,170,90,12)};
        int[] colors = {Color.argb(255, 0x4d, 0x4d, 0x4d),Color.argb(255,0x5d, 0xa5,0xda), Color.argb(255, 0xFA, 0xA4, 0x3A), Color.LTGRAY, Color.argb(255,0x60, 0xBD, 0x68), Color.argb(255, 0xF1, 0x7C,0xB0),Color.argb(255,0xB2,0x91, 0x2F), Color.argb(255,0xB2,0x76, 0xB2),  Color.argb(255, 0xDE,0xCF, 0x3F),Color.argb(255, 0xF1, 0x58, 0x54)};
        List<ArcValue> values = new ArrayList<ArcValue>();
        for (int i = 0; i < numValues; ++i) {
            ArcValue arcValue = new ArcValue(percents[i], colors[i]);
            if (hasArcSeparated && i == 0) {
                arcValue.setArcSpacing(10);
            }
            float portion = percents[i] * salary;
            String label = labels[i].toUpperCase()+" "+df.format(portion);
            values.add(arcValue.setLabel(label.toCharArray()));
        }

        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);

        //limits the amount of space the pie chart can take from 0-1
        _chart.setCircleFillRatio(0.6f);


        //todo dynamicaly generate fontsize
        double fontsize =   _chart.getHeight() *.1;
        Log.v("PieChart", "Chart: " + _chart.getHeight() + " font: " + fontsize);

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape
//            fontsize =  18f     ;
            //large device
            fontsize = 18f;
        }
        else {
            // Portrait
//             fontsize =  24f;
            //large device
            fontsize = 30f;
        }
        if (hasCenterText1) {
            data.setCenterText1("Monthly Spending");

        // Get roboto-italic font.
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
            data.setCenterText1Typeface(tf);

            data.setCenterText1Color(getResources().getColor(R.color.listing_color));
            data.setCenterText1FontSize(Utils.px2sp(getResources().getDisplayMetrics().scaledDensity, (int)fontsize));

        }

        if (hasCenterText2) {
            data.setCenterText2("Annual Salary of: "+sSalary);

            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");

            data.setCenterText2Typeface(tf);
            data.setCenterText2FontSize(Utils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int)fontsize - 8));
        }

        _chart.setPieChartData(data);

    }

    @Override
    public void onResume() {
        generateData();
        super.onResume();

    }
}
