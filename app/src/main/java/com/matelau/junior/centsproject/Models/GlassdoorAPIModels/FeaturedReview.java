package com.matelau.junior.centsproject.Models.GlassdoorAPIModels;

/**
 * Created by matelau on 11/29/14.
 */

import com.google.gson.annotations.Expose;

public class FeaturedReview {

    @Expose
    public Integer id;
    @Expose
    public Boolean currentJob;
    @Expose
    public String reviewDateTime;
    @Expose
    public String jobTitle;
    @Expose
    public String location;
    @Expose
    public String headline;
    @Expose
    public String pros;
    @Expose
    public String cons;

}
