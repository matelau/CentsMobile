package com.matelau.junior.centsproject.Models.Design.GlassdoorAPIModels;

import com.google.gson.annotations.Expose;

/**
 * Created by matelau on 11/29/14.
 */
public class Ceo {

    @Expose
    public String name;
    @Expose
    public String title;
    @Expose
    public Image image;
    @Expose
    public Integer numberOfRatings;
    @Expose
    public Integer pctApprove;
    @Expose
    public Integer pctDisapprove;
}
