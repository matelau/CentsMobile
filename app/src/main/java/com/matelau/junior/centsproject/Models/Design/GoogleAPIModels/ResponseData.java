package com.matelau.junior.centsproject.Models.Design.GoogleAPIModels;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 11/25/14.
 */
public class ResponseData {
    @Expose
    private List<Result> results = new ArrayList<Result>();
    @Expose
    private Cursor cursor;

    public List<Result> getResults() {
        return results;
    }
}
