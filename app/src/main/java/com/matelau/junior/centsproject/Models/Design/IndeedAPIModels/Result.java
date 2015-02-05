package com.matelau.junior.centsproject.Models.Design.IndeedAPIModels;

import com.google.gson.annotations.Expose;

/**
 * Created by matelau on 11/23/14.
 * Indeed Job Search Results
 */
public class Result {
    @Expose
    private String jobtitle;
    @Expose
    private String company;
    @Expose
    private String city;
    @Expose
    private String state;
    @Expose
    private String country;
    @Expose
    private String formattedLocation;
    @Expose
    private String source;
    @Expose
    private String date;
    @Expose
    private String snippet;
    @Expose
    private String url;
    @Expose
    private String onmousedown;
    @Expose
    private String jobkey;
    @Expose
    private Boolean sponsored;
    @Expose
    private Boolean expired;
    @Expose
    private Boolean indeedApply;
    @Expose
    private String formattedLocationFull;
    @Expose
    private String formattedRelativeTime;

    public String getJobtitle() {
        return jobtitle;
    }

    public String getCompany() {
        return company;
    }

    public String getDate() {
        return date;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getUrl() {
        return url;
    }

    public String getJobkey() {
        return jobkey;
    }
}
