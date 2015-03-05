package com.matelau.junior.centsproject.Models.CentsAPIModels;

import com.google.gson.annotations.Expose;

/**
 * Created by matelau on 3/4/15.
 */
public class School {

    @Expose
    private String name;

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }
}
