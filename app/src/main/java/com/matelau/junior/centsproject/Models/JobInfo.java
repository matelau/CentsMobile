package com.matelau.junior.centsproject.Models;

import android.graphics.Bitmap;

/**
 * Created by matelau on 11/24/14.
 * Holds important information for cards from both indeed and glassdoor
 */
public class JobInfo {
    public String jobTitle;
    public String jobCompany;
    public Bitmap companyImg;
    public String jobUrl;

    public JobInfo(String jt, String jc, String jUrl){
        jobTitle = jt;
        jobCompany = jc;
        jobUrl = jUrl;
    }
}
