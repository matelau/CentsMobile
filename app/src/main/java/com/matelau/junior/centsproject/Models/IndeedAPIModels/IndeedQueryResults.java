package com.matelau.junior.centsproject.Models.IndeedAPIModels;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 11/23/14.
 */
public class IndeedQueryResults {
    @Expose
    private Integer version;
    @Expose
    private String query;
    @Expose
    private String location;
    @Expose
    private Boolean dupefilter;
    @Expose
    private Boolean highlight;
    @Expose
    private Integer radius;
    @Expose
    private Integer start;
    @Expose
    private Integer end;
    @Expose
    private Integer totalResults;
    @Expose
    private Integer pageNumber;
    @Expose
    private List<Result> results = new ArrayList<Result>();

    public List<Result> getResults() {
        return results;
    }
}
