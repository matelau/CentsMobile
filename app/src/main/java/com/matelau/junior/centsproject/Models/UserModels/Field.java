package com.matelau.junior.centsproject.Models.UserModels;

import com.google.gson.annotations.Expose;

/**
 * Created by matelau on 4/8/15.
 */
public class Field {

    @Expose
    private String name;
    @Expose
    private String value;

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

    /**
     *
     * @return
     * The value
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value
     * The value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
