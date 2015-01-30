package com.matelau.junior.centsproject.Controllers;

import android.app.Application;
import android.content.Context;

import com.matelau.junior.centsproject.Models.Col;
import com.matelau.junior.centsproject.Models.JobInfo;

import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by matelau on 11/20/14.
 * Maintains a Central state of the application
 */
public class CentsApplication extends Application{
    private static Context _centsContext;
    //Api Services
    private static RestAdapter _gdRestAdapter = new RestAdapter.Builder().setEndpoint("https://api.glassdoor.com/").build();
    private static RestAdapter _indeedRestAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.BASIC).setEndpoint("http://api.indeed.com").build();
    private static RestAdapter _queryParsingRestAdapter = new RestAdapter.Builder().setEndpoint("http://54.67.106.77:6001/").build();
    private static RestAdapter _centsRestAdapter = new RestAdapter.Builder().setEndpoint("https:/54.67.106.77").build();
    //Current Selections Vars
    private static String _searchedCity;
    private static String _searchState;
    private static String _searchedCity2;
    private static String _searchState2;
    private static String _searchedOccupation;
    private static String _occupationSalary;
    //maintaining ui vars
    private static int _citySpinPos;
    private static int _stateSpinPos;
    private static String _selectedVis = "default";
    //Lists
    private static List<JobInfo> _jobSearchResultList;
    private static String[] _states;
    private static String[] _cities;
    private static List<Col> _cols;

    public static Context getAppContext() {return _centsContext;}
    public static RestAdapter get_gdRestAdapter() {return _gdRestAdapter;}

    public static RestAdapter get_indeedRestAdapter() {
        return _indeedRestAdapter;
    }

    public static void set_indeedRestAdapter(RestAdapter _indeedRestAdapter) {
        CentsApplication._indeedRestAdapter = _indeedRestAdapter;
    }

    public static List<JobInfo> get_jobSearchResultList() {
        return _jobSearchResultList;
    }

    public static void set_jobSearchResultList(List<JobInfo> _jobSearchResultList) {
        CentsApplication._jobSearchResultList = _jobSearchResultList;
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

    public static List<Col> get_cols() {
        return _cols;
    }

    public static void set_cols(List<Col> _cols) {
        CentsApplication._cols = _cols;
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
}
