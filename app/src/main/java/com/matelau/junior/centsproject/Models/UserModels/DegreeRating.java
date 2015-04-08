package com.matelau.junior.centsproject.Models.UserModels;

import com.google.gson.annotations.Expose;

/**
 * Created by matelau on 4/7/15.
 */
public class DegreeRating {
    @Expose
    private String name;
    @Expose
    private String level;
    @Expose
    private Long rating;

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
     * The level
     */
    public String getLevel() {
        return level;
    }

    /**
     *
     * @param level
     * The level
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     *
     * @return
     * The rating
     */
    public Long getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     * The rating
     */
    public void setRating(Long rating) {
        this.rating = rating;
    }

}
