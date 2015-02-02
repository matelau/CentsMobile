package com.matelau.junior.centsproject.Models.Design;

/**
 * Created by matelau on 11/24/14.
 * Holds important information for cards from both indeed and glassdoor
 */
public class JobInfo {
    public String jobTitle;
    public String jobCompany;
    public String url;
    public String jobLogoUrl;

    public JobInfo(String jt, String jc, String applyUrl){
        jobTitle = jt;
        jobCompany = jc;
        url = applyUrl;

    }


}
