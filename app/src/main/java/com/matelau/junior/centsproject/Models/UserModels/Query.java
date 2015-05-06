package com.matelau.junior.centsproject.Models.UserModels;

import com.google.gson.annotations.Expose;

/**
 * Created by matelau on 4/6/15.
 */
public class Query {
    @Expose
    private String url;

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
