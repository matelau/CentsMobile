package com.matelau.junior.centsproject.Views.VisualizationFragments.SpendingBreakdown;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import lecho.lib.hellocharts.model.ArcValue;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.util.Utils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 */
public class SpendingBreakdownFragment extends Fragment {
    private String LOG_TAG = SpendingBreakdownFragment.class.getSimpleName();
    private ImageButton _back;
    private PieChartView _chart;
    private TextView _occupation;
    private EditText _income;
    private PieChartData data;
    private Button _default;
    private Button _student;
    private Button _custom;
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


        initVisVars();


        View rootView = inflater.inflate(R.layout.fragment_spending_breakdown, container, false);
        CardView cv = (CardView) rootView.findViewById(R.id.spending_card_view);
        //get interaction elements
         _chart = (PieChartView) rootView.findViewById(R.id.spending_vis);
        //Income Input
        _income = (EditText) rootView.findViewById(R.id.editText1);
        _income.setText(CentsApplication.get_occupationSalary());
        _income.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                //verify numeric value
                boolean number = true;
                for(int i = 0; i < value.length(); i++){
                    char c = value.charAt(i);

                    if(!Character.isDigit(c))
                        number = false;

                }
                if(!number){
                    Toast.makeText(getActivity(), "Invalid Number - No special characters", Toast.LENGTH_SHORT).show();
                    s.clear();
                }
                else{

                    CentsApplication.set_occupationSalary(s.toString());
                    //redraw viz
                    generateData();
                }

            }
        });
        //Get buttons
        _default = (Button) rootView.findViewById(R.id.default_btn);
        _student = (Button) rootView.findViewById(R.id.student_btn);
        _custom = (Button) rootView.findViewById(R.id.custom_btn);

        _default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch modification dialog frag
                showModDialog();
            }
        });

//        _occupation = (TextView) rootView.findViewById(R.id.spending_desc);
        //modify circle text to be x percentage in size based on view height
        _height = container.getHeight();
//        _occupation.setText(CentsApplication.get_searchedOccupation());

        generateData();

        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * Sets Vis Options to default if not set
     */
    private void initVisVars(){
        //TODO read file to see if user has saved values
        if(CentsApplication.get_occupationSalary() == null){
            CentsApplication.set_occupationSalary("45000");
        }
        if(CentsApplication.get_sbLabels() == null){
            String[] labels = {"housing", "food", "transportation", "utilities","student loans","other debt", "insurance","savings","health","misc"};
            CentsApplication.set_sbLabels(Arrays.asList(labels));
        }
        if(CentsApplication.get_sbPercents() == null){
            //Percents from Wesley - Food 17, Housing 25, Utilities 6, Transportation 12, Healthcare 5, Insurance 8, Student/Credit Debt 12, Savings 10, Misc 5
            Float[] percents = {.25f, .20f,.08f, .05f, .08f,.11f,.06f,.07f,.03f,.07f};
            CentsApplication.set_sbPercents(Arrays.asList(percents));

        }
    }

    /**
     * Loads the Wizard ontop of current view
     */
    private void showModDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        SpendingBreakdownModDialogFragment mod = new SpendingBreakdownModDialogFragment();
        mod.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        mod.show(fm, "tag");
    }

    /**
     * Build Pie Chart
     */
    public void generateData(){
        int numValues = 10;
        //default values
        float salary = 45000; // national median 2013
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
                salary = 45000/12f;
//                _occupation.setText("Natl Median Wage");
            }

        }
        //only show two decimal places in values
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);
        // get labels and percents
        String[] labels = (String[]) CentsApplication.get_sbLabels().toArray();
        Float[] percents = (Float[]) CentsApplication.get_sbPercents().toArray();
        int[] colors = new int[labels.length];
        if(CentsApplication.get_colors() != null && CentsApplication.get_colors().length == labels.length)
            colors = CentsApplication.get_colors();
        else{
            //generate colors randomly
            colors = getColors(labels.length);
            CentsApplication.set_colors(colors);

        }
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

    private int[] getColors(int count){
        int[] def = new int[]{Color.argb(255, 0x4d, 0x4d, 0x4d),Color.argb(255,0x5d, 0xa5,0xda), Color.argb(255, 0xFA, 0xA4, 0x3A), Color.LTGRAY, Color.argb(255,0x60, 0xBD, 0x68), Color.argb(255, 0xF1, 0x7C,0xB0),Color.argb(255,0xB2,0x91, 0x2F), Color.argb(255,0xB2,0x76, 0xB2),  Color.argb(255, 0xDE,0xCF, 0x3F),Color.argb(255, 0xF1, 0x58, 0x54)};
        if(count <= def.length ){
            return Arrays.copyOfRange(def,0,count);
        }
        else
        {
            int[] colors = new int[count];
            //copy over def
            for(int k = 0; k < def.length; k++){
                colors[k] = def[k];
            }

            //get random values for everything else
            Random rnd = new Random();
            for(int i = def.length-1; i < count ; i++){
                //TODO improve color generation - make sure colors are not too bright and not too similar between indices
                colors[i] = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                if(i > 0){
                    int lastcol = colors[i - 1];
                    // greater than causes more brights less than more darks
                    while((lastcol - colors[i] <= 77)){
                        Log.d(LOG_TAG, "color collision");
                        colors[i] = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    }

                }
            }

            return colors;

        }
    }

    @Override
    public void onResume() {
        generateData();
        super.onResume();

    }
}
