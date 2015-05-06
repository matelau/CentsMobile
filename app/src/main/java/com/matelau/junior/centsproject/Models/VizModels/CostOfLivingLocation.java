package com.matelau.junior.centsproject.Models.VizModels;

import com.google.gson.annotations.Expose;

/**
 * Created by matelau on 3/3/15.
 */
public class CostOfLivingLocation {
    @Expose
    private String city;
    @Expose
    private String state;

    /**
     * @return The city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return The state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state The state
     */
    public void setState(String state) {
        this.state = state;
    }


}
