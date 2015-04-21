package com.matelau.junior.centsproject.Views.VisualizationFragments.SpendingBreakdown;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Controllers.SearchFragment;
import com.matelau.junior.centsproject.Models.CentsAPIServices.UserService;
import com.matelau.junior.centsproject.Models.VizModels.SpendingBreakdownCategory;
import com.matelau.junior.centsproject.Models.VizModels.SpendingElementResponse;
import com.matelau.junior.centsproject.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 */
public class SpendingBreakdownFragment extends Fragment {
    private String LOG_TAG = SpendingBreakdownFragment.class.getSimpleName();
    private ImageButton _search;
    private PieChartView _chart;
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
//    private int _height = 800;
    private int _id;


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

        //Get buttons
        _search = (ImageButton) rootView.findViewById(R.id.imageSearchButton);
        _default = (Button) rootView.findViewById(R.id.default_btn);
        _student = (Button) rootView.findViewById(R.id.student_btn);
        _custom = (Button) rootView.findViewById(R.id.custom_btn);


        initVisVars();

        _income.setText(CentsApplication.get_occupationSalary());
        _income.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Get All Locked values

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                //verify numeric value
                boolean number = true;
                for (int i = 0; i < value.length(); i++) {
                    char c = value.charAt(i);

                    if (!Character.isDigit(c))
                        number = false;

                }
                if (!number || value.length() == 0) {
                    //not a debug notice
                    Toast.makeText(getActivity(), "Invalid Number - No special characters", Toast.LENGTH_SHORT).show();
                    s.clear();
                    //on error set value to zero
                    s.append("0");
                    updateIncome(s);
                } else {
                    updateIncome(s);

                }

            }
        });

        _default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch modification dialog frag
                setDefaultVars(true);
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


        _search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return search frag
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_placeholder, new SearchFragment());
                ft.addToBackStack("spending-breakdown");
                ft.commit();
            }
        });

        //modify circle text to be x percentage in size based on view height
//        _height = container.getHeight();

        generateData();

        // Inflate the layout for this fragment
        return rootView;
    }



    private void updateIncome(Editable s){
        //get previous sal
        Float prevDisposable = CentsApplication.get_disposableIncome();
        CentsApplication.set_occupationSalary(s.toString());
        SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        settings.edit().putString("salary", s.toString()).apply();
        Log.d(LOG_TAG, "put salary: " + s.toString());
        //calculate new tax percentage
        calculateTaxes(Float.parseFloat(s.toString()));
        //get locked values and update their percentage based on new values
        Float currDisposable = CentsApplication.get_disposableIncome();
        updateLockedPercentage(prevDisposable, currDisposable);
        //redraw viz
        generateData();
    }

    /**
     * Sets Vis Options to default if not set
     */
    private void initVisVars(){
        if(CentsApplication.is_loggedIN()){
            //load user id
            SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
            _id = settings.getInt("ID", 0);
            //check for spending breakdown on db
            UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
            service.getDefaultSpendingData(_id, new Callback<SpendingElementResponse[]>() {
               @Override
                public void success(SpendingElementResponse[] spendingElementResponse, Response response) {
                    List<SpendingElementResponse> elements = Arrays.asList(spendingElementResponse);
                    if (elements.size() == 0) {
                        //no values saved init values
                        setDefaultVars(false);
                    }
                    else {
                        List<SpendingBreakdownCategory> sbVals = new ArrayList<SpendingBreakdownCategory>();
                        for (SpendingElementResponse current : elements) {
                            Float f = current.getValue()/100f;
                            SpendingBreakdownCategory val = new SpendingBreakdownCategory(current.getName(),f, false);
                            sbVals.add(val);
                        }
                        //add taxes
                        sbVals.add(0, new SpendingBreakdownCategory("TAXES", 0.0f, true));
                        CentsApplication.set_sbValues(sbVals);
                        calculateTaxes(Float.parseFloat(CentsApplication.get_occupationSalary()));
                        //update viz
                        generateData();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(LOG_TAG, error.getMessage());

                }
            });
        }
        // read file to see if user has saved values
        String filename = CentsApplication.get_currentBreakdown()+".dat";
        if(CentsApplication.doesFileExist(filename, getActivity())){
            //loadfile
            CentsApplication.loadSB(filename, getActivity());
        }
        else{
                setDefaultVars(false);

        }
        loadSalaryFromPref();


        //only show toast once
        if(CentsApplication.is_sbToast() == false){
            Toast.makeText(getActivity(), "Click default, student, or custom to edit values", Toast.LENGTH_SHORT).show();
            CentsApplication.set_sbToast(true);

        }
        //calculateTaxes
        calculateTaxes(Float.parseFloat(CentsApplication.get_occupationSalary()));
    }

    private void loadSalaryFromPref(){
        SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        String salary = settings.getString("salary", "");
        Log.d(LOG_TAG, "Loaded Salary from pref: " + salary);
        if(salary != ""){
            //set var
            CentsApplication.set_occupationSalary(salary);
            //set View
        }
        else {
            CentsApplication.set_occupationSalary("45000");

        }
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

    private void setDefaultVars(boolean showMod){
        //if  vars are already saved/loaded use those instead these values overwrite
        String filename ="default.dat";
        if(CentsApplication.doesFileExist(filename, getActivity())){
            //loadfile
            CentsApplication.loadSB(filename, getActivity());
            Log.d(LOG_TAG, "Loaded default vars from file");
        }
        else{
            Log.d(LOG_TAG, "Set default vars");
            ArrayList<String> labels = new ArrayList<String>(Arrays.asList("TAXES","HOUSING", "FOOD", "TRANSPORTATION", "UTILITIES","LOANS","OTHER DEBT", "INSURANCE","SAVINGS","HEALTH","MISC"));
            //Percents from Wesley - Food 17, Housing 25, Utilities 6, Transportation 12, Healthcare 5, Insurance 8, Student/Credit Debt 12, Savings 10, Misc 5
            ArrayList<Float> percents = new ArrayList<Float>(Arrays.asList(0.0f,.25f, .20f,.08f, .05f, .08f,.11f,.06f,.07f,.03f,.07f));
            ArrayList<SpendingBreakdownCategory> values = (ArrayList<SpendingBreakdownCategory>) listBuilder(labels, percents);
            CentsApplication.set_sbValues(values);
        }

        if(CentsApplication.is_loggedIN()){
            storeDefault();
        }

        if(showMod)
            showModDialog("default");
    }


    /**
     * updates default sb values on db
     */
    private void storeDefault(){
        ArrayList<SpendingBreakdownCategory> values = (ArrayList<SpendingBreakdownCategory>) CentsApplication.get_sbValues();
        //build map to be passed as value
        HashMap<String, String> elements = new HashMap<String,String>();
        for(SpendingBreakdownCategory current : values){
            //dont store taxes it will be calculated
            if(!current._category.equals("TAXES")){
                float percent = current._percent * 100f;
                elements.put(current._category, ""+percent);
            }
        }

        HashMap<String, HashMap<String, String>> fields = new HashMap<String, HashMap<String,String>>();
        fields.put("fields", elements);
        UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
        service.initDefaultSpendingFields(_id, "default", fields, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(LOG_TAG, "updated spending records");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());
            }
        });
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
     * updates the values of locked items based on new salary - does not modify taxes
     */
    private void updateLockedPercentage(Float prevDisposable, Float currentDisposable){
        List<SpendingBreakdownCategory> vals = CentsApplication.get_sbValues();
        for(int i = 1; i < vals.size(); i++){
            SpendingBreakdownCategory curr = vals.get(i);
            if(curr._locked == true){
                Float amt = curr._percent * prevDisposable;
                Log.d(LOG_TAG, "previous val: "+amt);
                Float newPercent = amt/currentDisposable;
                curr._percent = newPercent;
                //previous val and current value should be equal
                Log.d(LOG_TAG, "updated locked percent: "+newPercent +" new value: "+ (newPercent * currentDisposable));
            }
        }

        //save updated vals
        saveVals(vals);
    }

    /**
     * sets app sb context to vals and writes file
      * @param vals
     */
    private void saveVals(List<SpendingBreakdownCategory> vals){
        CentsApplication.set_sbValues(vals);
        String filename = CentsApplication.get_currentBreakdown()+".dat";
        CentsApplication.saveSB(filename, getActivity());
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
     * @param spending_income
     */
    private void calculateTaxes(Float spending_income){
        if(spending_income < 9075){
            CentsApplication.get_sbValues().get(0)._percent = 0.0f;
        }
        else{
            int[] brackets = new int[]{0,9075,36900,89350,186350,405100,406750};
            float[] percents = new float[]{0.0f, 0.2f, 0.25f, 0.35f, 0.38f, 0.43f, 0.45f, 0.496f};
            float taxed_income = 0.0f;
            int max_index = 0;
            //iterate through included brackets and update indices to include
            for(;max_index < 6; max_index++){
                if(spending_income < brackets[max_index]){
                    max_index--;
                    break;
                }
            }
            //sum up amt of tax paid per bracket
            int i = 1;
            for(; i < max_index+1; i++){
                taxed_income += (percents[i] * (brackets[i] - brackets[i-1]));
            }
            taxed_income += (percents[i]*(spending_income-brackets[i-1]));
            //store results

            float result = taxed_income/spending_income;
            CentsApplication.get_sbValues().get(0)._percent = result;
            Float disposableIncome = spending_income - taxed_income;
            CentsApplication.set_disposableIncome(disposableIncome);
            saveVals(CentsApplication.get_sbValues());
            Log.d(LOG_TAG, "TaxedIncome ="+taxed_income);
            Log.d(LOG_TAG, "DisposableIncome ="+disposableIncome);
            Log.d(LOG_TAG, "Tax ="+result);

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

        //only show two decimal places in values
        DecimalFormat df = new DecimalFormat("##");
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
        List<SliceValue> values = new ArrayList<SliceValue>();

        //---------Taxed value-------------

        SliceValue taxArcValue = new SliceValue(percents[0], colors[0]);

        float taxMonthlyPortion = percents[0] * salary;
        String taxLabel = labels[0].toUpperCase()+" "+df.format(taxMonthlyPortion);
        values.add(taxArcValue.setLabel(taxLabel));
        //---------All Other Values -------------
        for (int i = 1; i < numValues; ++i) {
            SliceValue arcValue = new SliceValue(percents[i], colors[i]);

            float monthlyPortion = percents[i] * (CentsApplication.get_disposableIncome()/12f);
            String label = labels[i].toUpperCase()+" "+df.format(monthlyPortion);
            values.add(arcValue.setLabel(label));
        }

        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);

        //limits the amount of space the pie chart can take from 0-1
        _chart.setCircleFillRatio(0.65f);


        //todo dynamicaly generate fontsize
        int fontsize =  0; // _chart.getHeight() *.1;

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {

            fontsize = 20;
        }
        else {
            fontsize = 30;
        }


        float completion = percentCompletion(percents);
        Log.d(LOG_TAG, "completion: "+completion);

        //check if over/under budget and modify text to show by how much
        if (hasCenterText1) {
            data.setCenterText1Color(getResources().getColor(R.color.black));
            if(completion > 1.001f){
                data.setCenterText1("Over Spending By:");
            }
            else if(completion < .995f){
                data.setCenterText1("Under Spending By:");
            }
            else{
                data.setCenterText1("At 100%");
            }
            // Get roboto-italic font.
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
            data.setCenterText1Typeface(tf);
            data.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity, fontsize));

        }

        if (hasCenterText2) {
            if(completion > 1.001f){
                Float percent = completion - 1.0f;
                data.setCenterText2("$"+CentsApplication.convPercentToDollar(percent, false) + " You must spend less");
                data.setCenterText2Color(getResources().getColor(R.color.red));

            }
            else if(completion < .995f){
                Float percent = 1f- completion;
                data.setCenterText2("$"+CentsApplication.convPercentToDollar(percent, false));
                data.setCenterText2Color(getResources().getColor(R.color.blue));
            }
            else{
                data.setCenterText2("Congratulations");
                data.setCenterText2Color(getResources().getColor(R.color.DarkGreen));

            }

            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");

            data.setCenterText2Typeface(tf);
            data.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity, (fontsize - 8)));
        }

        _chart.setPieChartData(data);
        _chart.invalidate();

    }

    /**
     * Sums percents array to check if we are over or under
     * @param percents
     * @return
     */
    private float percentCompletion(Float[] percents){
        Float sum = 0.0f;
        for(Float f: percents){
            sum += f;
        }
        //remove tax percent
        sum -= CentsApplication.get_sbValues().get(0)._percent;
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
                //color generation - make sure colors are not too bright and not too similar between indices
                colors[i] = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                if(i > 0){
                    int lastcol = colors[i - 1];
                    // greater than causes more brights less than more darks
                    while((lastcol - colors[i] <= 77)){
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
