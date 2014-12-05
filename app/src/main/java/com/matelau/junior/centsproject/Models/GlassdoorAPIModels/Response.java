package com.matelau.junior.centsproject.Models.GlassdoorAPIModels;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 11/29/14.
 */
public class Response {

    @Expose
    public Integer currentPageNumber;
    @Expose
    public Integer totalNumberOfPages;
    @Expose
    public Integer totalRecordCount;
    @Expose
    public List<Employer> employers = new ArrayList<Employer>();



}