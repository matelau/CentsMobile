package com.matelau.junior.centsproject.Models.GoogleAPIModels;

import com.google.gson.annotations.Expose;

/**
 * Created by matelau on 11/25/14.
 */
public class Result {
    @Expose
    private String GsearchResultClass;
    @Expose
    private String width;
    @Expose
    private String height;
    @Expose
    private String imageId;
    @Expose
    private String tbWidth;
    @Expose
    private String tbHeight;
    @Expose
    private String unescapedUrl;
    @Expose
    private String url;
    @Expose
    private String visibleUrl;
    @Expose
    private String title;
    @Expose
    private String titleNoFormatting;
    @Expose
    private String originalContextUrl;
    @Expose
    private String content;
    @Expose
    private String contentNoFormatting;
    @Expose
    private String tbUrl;


    public String getUrl() {
        return url;
    }
}
