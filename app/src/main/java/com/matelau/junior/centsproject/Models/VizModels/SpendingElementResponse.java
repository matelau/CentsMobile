package com.matelau.junior.centsproject.Models.VizModels;

import com.google.gson.annotations.Expose;

/**
 * Created by matelau on 4/20/15.
 */
public class SpendingElementResponse {
    @Expose
    private String name;
    @Expose
    private Float value;
    @Expose
    private String category;

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The value
     */
    public Float getValue() {
        return value;
    }

    /**
     * @param value The value
     */
    public void setValue(Float value) {
        this.value = value;
    }

    /**
     * @return The category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category The category
     */
    public void setCategory(String category) {
        this.category = category;
    }

}
