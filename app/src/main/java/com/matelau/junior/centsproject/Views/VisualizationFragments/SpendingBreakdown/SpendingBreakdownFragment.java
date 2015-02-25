package com.matelau.junior.centsproject.Views.VisualizationFragments.SpendingBreakdown;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.matelau.junior.centsproject.Models.VizModels.SpendingBreakdownCategory;
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
    private int _height = 800;


    public SpendingBreakdownFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_spending_breakdown, container, false);

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
                        SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
                        settings.edit().putString("salary", s.toString());
                        Log.d(LOG_TAG, "put salary: "+s.toString());
                        //redraw viz
                        generateData();
                }

            }
        });
        //Get buttons
        _default = (Button) rootView.findViewById(R.id.default_btn);
        _student = (Button) rootView.findViewById(R.id.student_btn);
        _custom = (Button) rootView.findViewById(R.id.custom_btn);
        initVisVars();

        _default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch modification dialog frag
                setDefaultVars();
            }
        });

        _student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //override default labels and percents
                initStudentVars();

            }
        });

        _custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //override default labels and percents
                initCustomVars();

            }
        });


        //modify circle text to be x percentage in size based on view height
        _height = container.getHeight();

        generateData();

        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * Sets Vis Options to default if not set
     */
    private void initVisVars(){
        // read file to see if user has saved values
        String filename = CentsApplication.get_currentBreakdown()+".dat";
        if(CentsApplication.doesFileExist(filename, getActivity())){
            //loadfile
            CentsApplication.loadSB(filename, getActivity());
        }
        else{
            if(CentsApplication.get_sbValues() == null){
                ArrayList<String> labels = new ArrayList<String>(Arrays.asList("TAXES","HOUSING", "FOOD", "TRANSPORTATION", "UTILITIES","LOANS","OTHER DEBT", "INSURANCE","SAVINGS","HEALTH","MISC"));
                //Percents from Wesley - Food 17, Housing 25, Utilities 6, Transportation 12, Healthcare 5, Insurance 8, Student/Credit Debt 12, Savings 10, Misc 5
                ArrayList<Float> percents = new ArrayList<Float>(Arrays.asList(0.0f,.25f, .20f,.08f, .05f, .08f,.11f,.06f,.07f,.03f,.07f));
                ArrayList<SpendingBreakdownCategory> sbcVals = listBuilder(labels,percents);
                CentsApplication.set_sbValues(sbcVals);
            }
        }
        SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        String salary = settings.getString("salary", "");
        if(salary == null || salary.equals("")){
            CentsApplication.set_occupationSalary("45000");
            settings.edit().putString("salary","45000");
        }
        else{
            CentsApplication.set_occupationSalary(salary);
        }


        //only show toast once
        if(CentsApplication.is_sbToast() == false){
            Toast.makeText(getActivity(), "Click default, student, or custom to edit values", Toast.LENGTH_SHORT).show();
            CentsApplication.set_sbToast(true);

        }

//        selectButton();
    }

    /**
     *Student Default Template
     */
    private void initStudentVars(){
        //if student vars are already saved/loaded use those instead these values overwrite
        String filename = "student.dat";
        if(CentsApplication.doesFileExist(filename, getActivity())){
            //loadfile
            CentsApplication.loadSB(filename, getActivity());
        }
        else{
            ArrayList<String> labels = new ArrayList<String>(Arrays.asList("TAXES","FOOD","HOUSING","UTILITIES","TRANSPORTATION","TUITION","BOOKS","SAVINGS","MISC"));
            //"Taxes":0, "Food":14, "Housing":21, "Utilities":6, "Transportation":12, "Tuition":25, "Books":6, "Savings":8, "Misc":8
            ArrayList<Float> percents = new ArrayList<Float>(Arrays.asList(0.0f,.14f,.21f,.06f,.12f,.25f,.06f,.08f,.08f));
            ArrayList<SpendingBreakdownCategory> values = listBuilder(labels, percents);
            CentsApplication.set_sbValues(values);
        }

        showModDialog("student");
    }

    private void setDefaultVars(){
        //if  vars are already saved/loaded use those instead these values overwrite
        String filename ="default.dat";
        if(CentsApplication.doesFileExist(filename, getActivity())){
            //loadfile
            CentsApplication.loadSB(filename, getActivity());
        }
        else{
            ArrayList<String> labels = new ArrayList<String>(Arrays.asList("TAXES","HOUSING", "FOOD", "TRANSPORTATION", "UTILITIES","LOANS","OTHER DEBT", "INSURANCE","SAVINGS","HEALTH","MISC"));
            //Percents from Wesley - Food 17, Housing 25, Utilities 6, Transportation 12, Healthcare 5, Insurance 8, Student/Credit Debt 12, Savings 10, Misc 5
            ArrayList<Float> percents = new ArrayList<Float>(Arrays.asList(0.0f,.25f, .20f,.08f, .05f, .08f,.11f,.06f,.07f,.03f,.07f));
            ArrayList<SpendingBreakdownCategory> values = (ArrayList<SpendingBreakdownCategory>) listBuilder(labels, percents);
            CentsApplication.set_sbValues(values);
        }

        showModDialog("default");
    }

    /**
     *Student Default Template
     */
    private void initCustomVars(){
        // if vars are already saved/loaded use those instead these values overwrite
        String filename = "custom.dat";
        if(CentsApplication.doesFileExist(filename, getActivity())){
            //loadfile
            CentsApplication.loadSB(filename, getActivity());
        }
        else{
            ArrayList<String> labels = new ArrayList<String>(Arrays.asList("TAXES","FOOD","HOUSING","UTILITIES"));
            ArrayList<Float> percents = new ArrayList<Float>(Arrays.asList(0.0f,.17f,.25f,.06f));
            ArrayList<SpendingBreakdownCategory> values = (ArrayList<SpendingBreakdownCategory>) listBuilder(labels, percents);
            CentsApplication.set_sbValues(values);
        }

        showModDialog("custom");
    }


    /**
     * Loads the modification list on top of current view
     */
    private void showModDialog(String selection){
        CentsApplication.set_currentBreakdown(selection);
        try{
            calculateTaxes(Float.parseFloat(CentsApplication.get_occupationSalary()));

        }
        catch(NumberFormatException e){
            e.printStackTrace();
        }

        FragmentManager fm = getActivity().getSupportFragmentManager();
        SpendingBreakdownModDialogFragment mod = new SpendingBreakdownModDialogFragment();
        mod.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        mod.show(fm, "tag");
    }

    /**
     * calculates monthly tax percent for input salary
     * @param salary
     */
    private void calculateTaxes(Float salary){
        if(salary < 9075){
            CentsApplication.get_sbValues().get(0)._percent = 0.0f;
        }
        else{
            int[] brackets = new int[]{0,9075,36900,89350,186350,405100,406750};
            float[] percents = new float[]{0.0f, 0.2f, 0.25f, 0.35f, 0.38f, 0.43f, 0.45f, 0.496f};
            float taxed_income = 0.0f;
            int included_brackets = 0;
            //iterate through included brackets and update indices to include
            for(;included_brackets < 6; included_brackets++){
                if(salary < brackets[included_brackets]){
                    included_brackets--;
                    break;
                }
            }
            //sum up amt of tax paid per bracket
            for(int i = 0; i < included_brackets+1; i++){
                taxed_income += (percents[i] * (salary - brackets[i]));
            }

            //store result
            float result = taxed_income/salary;
            CentsApplication.get_sbValues().get(0)._percent = result;
            normalizeValues(taxed_income, salary);
            Log.d(LOG_TAG, "Tax ="+result);

        }
    }

    /**
     * After Taxes are calculated for income the value of percents need to be normalized to add up to 100
     */
    private void normalizeValues(Float taxedIncome, Float income){
        List<SpendingBreakdownCategory> values = CentsApplication.get_sbValues();
        Float remainingIncome = income - taxedIncome;
        Float[] newVals = new Float[values.size()-1];

        for(int i = 1; i < values.size(); i++){
            Float current = values.get(i)._percent;
//            Log.d(LOG_TAG, "current Percent: "+current);
            Float amtOfRemaining = (current * remainingIncome);
            Float newPercent =  amtOfRemaining/income;
//            Log.d(LOG_TAG, "new Percent: "+newPercent);
            newVals[i-1] = newPercent;
//            values.get(i)._percent = newPercent;
        }

        Float sum = 0.0f;
        sum += values.get(0)._percent;
        for(int i = 0; i < newVals.length; i++){
            sum += newVals[i];
        }
        Log.d(LOG_TAG, "Percents sum: "+sum);
        //insure values add up to ~ 1.0f
        if(Math.abs(sum - 1.0f ) == 0.0f){
            //update values
            for(int i = 1; i < newVals.length; i++){
                values.get(i)._percent = newVals[i-1];
            }

            Log.d(LOG_TAG, "normalize - updates values");
            String filename = CentsApplication.get_currentBreakdown()+".dat";
            CentsApplication.saveSB(filename, getActivity());
        }
    }

    /**
     * Given a list of labels and percents creates a List of SBC
     * @param s
     * @param p
     * @return
     */
    private ArrayList<SpendingBreakdownCategory> listBuilder(List<String> s, List<Float> p){
        ArrayList<SpendingBreakdownCategory> values = new ArrayList<SpendingBreakdownCategory>();
        for(int i = 0; i < s.size(); i++){
            SpendingBreakdownCategory sbc = new SpendingBreakdownCategory(s.get(i), p.get(i), false);
            if(s.get(i).toUpperCase().equals("TAXES") ) {
               sbc._locked = true;
            }
            values.add(sbc);
        }

        return values;


    }



    /**
     * Build Pie Chart
     */
    public void generateData(){

        //convert salary
        float salary = 45000;
        String sSalary = ""+salary;
        if(CentsApplication.get_occupationSalary() != null){
            sSalary = CentsApplication.get_occupationSalary();
            //insure valid int is returned if not reset vals
            try{
                salary = Float.parseFloat(sSalary);
                salary = salary/12f;
            }catch(NumberFormatException e){
                e.printStackTrace();
                sSalary =""+ salary;
                salary = 45000/12f;
            }
        }

        calculateTaxes(salary * 12f);


        //only show two decimal places in values
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.HALF_DOWN);
        // get labels and percents
        List<SpendingBreakdownCategory> sbcVals = CentsApplication.get_sbValues();

        int numValues = sbcVals.size();
        String[] labels = new String[sbcVals.size()];
        Float[] percents = new Float[sbcVals.size()];
        for(int i = 0; i < labels.length; i++){
            labels[i] = sbcVals.get(i)._category;
            percents[i] = sbcVals.get(i)._percent;
        }

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
            float monthlyPortion = percents[i] * salary;
            String label = labels[i].toUpperCase()+" "+df.format(monthlyPortion);
            values.add(arcValue.setLabel(label.toCharArray()));
        }

        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);

        //limits the amount of space the pie chart can take from 0-1
        _chart.setCircleFillRatio(0.65f);


        //todo dynamicaly generate fontsize
        double fontsize =   _chart.getHeight() *.1;
        Log.v("PieChart", "Chart: " + _chart.getHeight() + " font: " + fontsize);

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {

            fontsize = 18f;
        }
        else {
            fontsize = 30f;
        }


        float completion = percentCompletion(percents);

        //check if over/under budget and modify text to show by how much
        if (hasCenterText1) {
            data.setCenterText1Color(getResources().getColor(R.color.black));

            if(completion > 1.0f){
                data.setCenterText1("Over Spending By:");


            }
            else if(completion < .99f){
                data.setCenterText1("Under Spending By:");
            }
            else{
                data.setCenterText1("At 100%");
            }


            // Get roboto-italic font.
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
            data.setCenterText1Typeface(tf);



            data.setCenterText1FontSize(Utils.px2sp(getResources().getDisplayMetrics().scaledDensity, (int)fontsize));

        }

        if (hasCenterText2) {

            if(completion > 1.0f){
                Float percent = completion - 1.0f;
                data.setCenterText2("$"+CentsApplication.convPercentToDollar(percent) + " You must spend less");
                data.setCenterText2Color(getResources().getColor(R.color.red));

            }
            else if(completion < .9995f){
                Float percent = 1f- completion;
                data.setCenterText2("$"+CentsApplication.convPercentToDollar(percent));
                data.setCenterText2Color(getResources().getColor(R.color.blue));
            }
            else{
                data.setCenterText2("Congratulations");
                data.setCenterText2Color(getResources().getColor(R.color.DarkGreen));

            }

            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");

            data.setCenterText2Typeface(tf);
            data.setCenterText2FontSize(Utils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int)fontsize - 8));
        }

        _chart.setPieChartData(data);
        _chart.invalidate();

    }

    /**
     * Sums percents array
     * @param percents
     * @return
     */
    private float percentCompletion(Float[] percents){
        Float sum = 0.0f;
        for(Float f: percents){
            sum += f;
        }
        return sum;
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
