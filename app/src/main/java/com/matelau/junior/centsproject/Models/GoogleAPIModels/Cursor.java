package com.matelau.junior.centsproject.Models.GoogleAPIModels;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 11/25/14.
 */
public class Cursor {
    @Expose
    private String resultCount;
    @Expose
    private List<Page> pages = new ArrayList<Page>();
    @Expose
    private String estimatedResultCount;
    @Expose
    private Integer currentPageIndex;
    @Expose
    private String moreResultsUrl;
    @Expose
    private String searchResultTime;
}
