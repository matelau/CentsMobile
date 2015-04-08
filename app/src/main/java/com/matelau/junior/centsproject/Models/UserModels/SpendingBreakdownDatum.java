package com.matelau.junior.centsproject.Models.UserModels;

import com.google.gson.annotations.Expose;

/**
 * Created by matelau on 4/7/15.
 */
public class SpendingBreakdownDatum {

    @Expose
    private String name;
    @Expose
    private Long value;
    @Expose
    private String category;

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
    public Long getValue() {
        return value;
    }

    /**
     *
     * @param value
     * The value
     */
    public void setValue(Long value) {
        this.value = value;
    }

    /**
     *
     * @return
     * The category
     */
    public String getCategory() {
        return category;
    }

    /**
     *
     * @param category
     * The category
     */
    public void setCategory(String category) {
        this.category = category;
    }
}
