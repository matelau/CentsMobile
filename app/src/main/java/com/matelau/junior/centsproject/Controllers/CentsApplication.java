package com.matelau.junior.centsproject.Controllers;

import android.app.Application;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matelau.junior.centsproject.Models.VizModels.CareerResponse;
import com.matelau.junior.centsproject.Models.VizModels.ColiResponse;
import com.matelau.junior.centsproject.Models.VizModels.Major;
import com.matelau.junior.centsproject.Models.VizModels.MajorResponse;
import com.matelau.junior.centsproject.Models.VizModels.SchoolResponse;
import com.matelau.junior.centsproject.Models.VizModels.SpendingBreakdownCategory;
import com.matelau.junior.centsproject.Views.VisualizationFragments.SpendingBreakdown.SpendingBreakdownModDialogFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by matelau on 11/20/14.
 * Maintains Central state of the application
 */
public class CentsApplication extends Application{

    //debug true = show toast, set login credentials
    private static boolean debug = false;
    private static String LOG_TAG = CentsApplication.class.getSimpleName();
    private static Context _centsContext;
    //Api Services
    private static RestAdapter _gdRestAdapter = new RestAdapter.Builder().setEndpoint("https://api.glassdoor.com/").build();
    private static RestAdapter _indeedRestAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint("http://api.indeed.com").build();
    //Note self signed cert is still being used by the query parser
    private static RestAdapter _queryParsingRestAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint("https://trycents.com:6001/").build();
    private static RestAdapter _centsRestAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint("https://trycents.com").build(); //.setClient(new OkClient(getUnsafeOkHttpClient()))

    //Current Selections Vars
    private static String _searchedCity;
    private static String _searchedCity2;
    private static String _searchState2;
    private static String _searchState;
    private static String _searchedOccupation;

    //maintaining ui vars
    private static int _citySpinPos;
    private static int _stateSpinPos;
    private static String _selectedVis = "default";

    //User vars
    private static boolean _loggedIN = false;
    private static String _user;
    private static String _password;

    //Lists
    private static String[] _states;
    private static String[] _cities;

    //Spending Breakdown Vis
    private static String _occupationSalary = null;
    private static Float _disposableIncome;
    private static List<String> _sbLabels;
    private static List<Float> _sbPercents;
    private static List<SpendingBreakdownCategory> _sbValues;
    private static int[] _colors;
    private static ViewPager _viewPager;
    private static SpendingBreakdownModDialogFragment.SBArrayAdapter _rAdapter;
    private static String _currentBreakdown = "default";
    private static boolean _sbToast = false;
    private static boolean _incomeFromQP = false;

    //Cost of Living Vis
    private static ColiResponse _colResponse;

    //School Comp vars
    //response from api
    private static SchoolResponse _sApiResponse;
    private static String _university1;
    private static String _university2;
    //the position of the universities state in the state array
    private static int pos1 = -1;
    private static int pos2 = -1;
    private static String[] _unis;

    //Major Comp Vars
    private static MajorResponse _mResponse;
    private static Major _major1;
    private static Major _major2;
    private static String[] _majors;

    //Career comp vars
    private static CareerResponse _cResponse;
    private static String[] _careers;

    public static Context getAppContext() {return _centsContext;}

    public static RestAdapter get_gdRestAdapter() {return _gdRestAdapter;}

    public static RestAdapter get_indeedRestAdapter() {
        return _indeedRestAdapter;
    }

    public static void set_indeedRestAdapter(RestAdapter _indeedRestAdapter) {
        CentsApplication._indeedRestAdapter = _indeedRestAdapter;
    }

    public static String get_searchedCity() {
        return _searchedCity;
    }

    public static void set_searchedCity(String _searchedCity) {
        CentsApplication._searchedCity = _searchedCity;
    }

    public static String get_searchState() {
        return _searchState;
    }

    public static void set_searchState(String _searchState) {
        CentsApplication._searchState = _searchState;
    }

    public static String get_searchedOccupation() {
        return _searchedOccupation;
    }

    public static void set_searchedOccupation(String _searchedOccupation) {
        CentsApplication._searchedOccupation = _searchedOccupation;
    }

    public static int get_citySpinPos() {
        return _citySpinPos;
    }

    public static void set_citySpinPos(int _citySpinPos) {
        CentsApplication._citySpinPos = _citySpinPos;
    }

    public static int get_stateSpinPos() {
        return _stateSpinPos;
    }

    public static void set_stateSpinPos(int _stateSpinPos) {
        CentsApplication._stateSpinPos = _stateSpinPos;
    }

    public static String get_occupationSalary() {
        return _occupationSalary;
    }

    public static void set_occupationSalary(String _occupationSalary) {
        CentsApplication._occupationSalary = _occupationSalary;
    }

    public static Float get_disposableIncome() {
        return _disposableIncome;
    }

    public static void set_disposableIncome(Float _disposableIncome) {
        CentsApplication._disposableIncome = _disposableIncome;
    }

    public static String[] get_states() {
        return _states;
    }

    public static void set_states(String[] _states) {
        CentsApplication._states = _states;
    }

    public static String[] get_cities() {
        return _cities;
    }

    public static void set_cities(String[] _cities) {
        CentsApplication._cities = _cities;
    }

    public static String get_searchedCity2() {
        return _searchedCity2;
    }

    public static void set_searchedCity2(String _searchedCity2) {
        CentsApplication._searchedCity2 = _searchedCity2;
    }

    public static String get_searchState2() {
        return _searchState2;
    }

    public static void set_searchState2(String _searchState2) {
        CentsApplication._searchState2 = _searchState2;
    }

    public static RestAdapter get_queryParsingRestAdapter() {
        return _queryParsingRestAdapter;
    }

    public static String get_selectedVis() {
        return _selectedVis;
    }

    public static void set_selectedVis(String _selectedVis) {
        CentsApplication._selectedVis = _selectedVis;
    }

    public static boolean is_loggedIN() {
        return _loggedIN;
    }

    public static void set_loggedIN(boolean _loggedIN) {
        CentsApplication._loggedIN = _loggedIN;
    }

    public static RestAdapter get_centsRestAdapter() {
        return _centsRestAdapter;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static String get_password() {
        return _password;
    }

    public static void set_password(String _password) {
        CentsApplication._password = _password;
    }

    public static String get_user() {
        return _user;
    }

    public static void set_user(String _user) {
        CentsApplication._user = _user;
    }

    public static List<String> get_sbLabels() {
        return _sbLabels;
    }

    public static void set_sbLabels(List<String> labels) {
        _sbLabels = labels;
    }

    public static List<Float> get_sbPercents() {
        return _sbPercents;
    }

    public static void set_sbPercents(List<Float> percents) {
        _sbPercents = percents;
    }

    public static List<SpendingBreakdownCategory> get_sbValues() {
        return _sbValues;
    }

    public static void set_sbValues(List<SpendingBreakdownCategory> _sbValues) {
        CentsApplication._sbValues = _sbValues;
    }

    public static int[] get_colors() {
        return _colors;
    }

    public static void set_colors(int[] _colors) {
        CentsApplication._colors = _colors;
    }

    public static ViewPager get_viewPager() {
        return _viewPager;
    }

    public static void set_viewPager(ViewPager _viewPager) {
        CentsApplication._viewPager = _viewPager;
    }


    public static SpendingBreakdownModDialogFragment.SBArrayAdapter get_rAdapter() {
        return _rAdapter;
    }

    public static void set_rAdapter(SpendingBreakdownModDialogFragment.SBArrayAdapter _rAdapter) {
        CentsApplication._rAdapter = _rAdapter;
    }

    public static String get_currentBreakdown() {
        return _currentBreakdown;
    }

    public static void set_currentBreakdown(String _currentBreakdown) {
        CentsApplication._currentBreakdown = _currentBreakdown;
    }

    public static boolean is_sbToast() {
        return _sbToast;
    }

    public static void set_sbToast(boolean _sbToast) {
        CentsApplication._sbToast = _sbToast;
    }

    public static boolean is_incomeFromQP() {
        return _incomeFromQP;
    }

    public static void set_incomeFromQP(boolean _incomeFromQP) {
        CentsApplication._incomeFromQP = _incomeFromQP;
    }

    public static ColiResponse get_colResponse() {
        return _colResponse;
    }

    public static void set_colResponse(ColiResponse _colResponse) {
        CentsApplication._colResponse = _colResponse;
    }

    public static SchoolResponse get_sApiResponse() {
        return _sApiResponse;
    }

    public static void set_sApiResponse(SchoolResponse _sApiResponse) {
        CentsApplication._sApiResponse = _sApiResponse;
    }

    public static String get_university1() {
        return _university1;
    }

    public static void set_university1(String _university1) {
        CentsApplication._university1 = _university1;
    }

    public static String get_university2() {
        return _university2;
    }

    public static void set_university2(String _university2) {
        CentsApplication._university2 = _university2;
    }

    public static String[] get_unis() {
        return _unis;
    }

    public static void set_unis(String[] _unis) {
        CentsApplication._unis = _unis;
    }

    public static MajorResponse get_mResponse() {
        return _mResponse;
    }

    public static void set_mResponse(MajorResponse _mResponse) {
        CentsApplication._mResponse = _mResponse;
    }

    public static Major get_major1() {
        return _major1;
    }

    public static void set_major1(Major _major1) {
        CentsApplication._major1 = _major1;
    }

    public static Major get_major2() {
        return _major2;
    }

    public static void set_major2(Major _major2) {
        CentsApplication._major2 = _major2;
    }

    public static int getPos2() {
        return pos2;
    }

    public static void setPos2(int pos2) {
        CentsApplication.pos2 = pos2;
    }

    public static int getPos1() {
        return pos1;
    }

    public static void setPos1(int pos1) {
        CentsApplication.pos1 = pos1;
    }

    public static String[] get_majors() {
        return _majors;
    }

    public static void set_majors(String[] _majors) {
        CentsApplication._majors = _majors;
    }

    public static String[] get_careers(){
        return _careers;
    }

    public static void set_careers(String[] _careers) {
        CentsApplication._careers = _careers;
    }

    public static CareerResponse get_cResponse() {
        return _cResponse;
    }

    public static void set_cResponse(CareerResponse _cResponse) {
        CentsApplication._cResponse = _cResponse;
    }

    /************************** Static Helper Methods ***********************************************************/

    /**
     * Given a string f - representing a float dollar amount of monthly expenses returns a number between 0-1 representing the amt of a monthly salary
     * f consumes
     * @param f
     * @return
     */
    public static Float convDollarToPercent(String f, boolean tax) {
        Float amt = 0.0f;
        try{
            amt = Float.parseFloat(f);
            Float monthlySalary = _disposableIncome/12f;
            if(tax){
                monthlySalary = Float.parseFloat(_occupationSalary)/12f;
            }
            amt = (amt/monthlySalary);
            //only show two decimal places in values
        }
        catch(NumberFormatException e){
            e.printStackTrace();
        }
        return amt;
    }


    public static String convPercentToDollar(Float p, boolean tax){
        Float percent = p;
        Float monthlySalary = _disposableIncome/12f;
        if(tax){
            monthlySalary = Float.parseFloat(_occupationSalary);
            percent = (percent * monthlySalary)/12f;
        }
        else{
            percent = (percent * monthlySalary);
        }

        //only show two decimal places in values
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.HALF_DOWN);
        return df.format(percent);
    }


    /**
     * Loads SpendingBreakdown file based on filename
     * @param filename
     * @param context
     */
    public static void loadSB(String filename, Context context){
        try{
            Log.d(LOG_TAG, "loading: "+filename);
            Type tp = new TypeToken<Collection<SpendingBreakdownCategory>>(){}.getType();
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null){
                sb.append(line);
            }

            String json = sb.toString();
            Log.d("Load", json);
            Gson gson = new Gson();
            _sbValues = gson.fromJson(json, tp);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Saves sb values
     * @param filename
     * @param context
     */
    public static void saveSB(String filename, Context context){
        Log.d(LOG_TAG, "saving: "+filename);
        Type tp = new TypeToken<Collection<SpendingBreakdownCategory>>(){}.getType();
        Gson gson = new Gson();
        String s = gson.toJson(_sbValues, tp);
        Log.i("Save", s);
        FileOutputStream outputStream;
        try{
            outputStream = context.openFileOutput(filename, MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Deletes files associated with spending breakdown and values
     * @param context
     */
    public static void deleteSB(Context context){
        _occupationSalary = "45000";
        _disposableIncome = null;
        _sbValues = null;
        String[] filenames = new String[]{"default.dat","custom.dat", "student.dat"};
        for(String file: filenames){
            context.deleteFile(file);
            Log.d(LOG_TAG, "deleted: "+file);
        }

    }

    /**
     * Checks if FileExists
     * @param filename
     * @param context
     * @return
     */
    public static boolean doesFileExist(String filename, Context context){
        File file = context.getFileStreamPath(filename);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }


}
