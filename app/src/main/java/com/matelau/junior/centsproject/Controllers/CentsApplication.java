package com.matelau.junior.centsproject.Controllers;

import android.app.Application;
import android.content.Context;

import com.matelau.junior.centsproject.Models.JobInfo;

import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by matelau on 11/20/14.
 */
public class CentsApplication extends Application{
    private static Context _centsContext;
    private static RestAdapter _googRestAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint("https://ajax.googleapis.com").build();
    private static RestAdapter _indeedRestAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.BASIC).setEndpoint("http://api.indeed.com").build();
    private static String _searchedCity;
    private static String _searchState;
    private static String _searchedOccupation;
    private static int _citySpinPos;
    private static int _stateSpinPos;
    private static List<JobInfo> _jobSearchResultList;
    private static List<String> _imgUrls;
    public CentsApplication(){
        _centsContext = this;
    }
    private static boolean _imgUrlsRdy = false;

    public static Context getAppContext() {return _centsContext;}
    public static RestAdapter get_googRestAdapter() {return _googRestAdapter;}

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

    public static List<String> get_imgUrls() {
        return _imgUrls;
    }

    public static void add_imgUrl(String _imgUrl) {
        CentsApplication._imgUrls.add(_imgUrl);
    }

    public static boolean is_imgUrlsRdy() {
        return _imgUrlsRdy;
    }

    public static void set_imgUrlsRdy(boolean _imgUrlsRdy) {
        CentsApplication._imgUrlsRdy = _imgUrlsRdy;
    }

    public static void set_imgUrls(List<String> _imgUrls) {
        CentsApplication._imgUrls = _imgUrls;
    }
}
