package com.matelau.junior.centsproject.Models.CentsAPIModels;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 3/3/15.
 */
public class CostOfLiving {
    @Expose
    private String operation;
    @Expose
    private List<CostOfLivingLocation> locations = new ArrayList<CostOfLivingLocation>();

    /**
     * @return The operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param operation The operation
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * @return The locations
     */
    public List<CostOfLivingLocation> getLocations() {
        return locations;
    }

    /**
     * @param locations The locations
     */
    public void setLocations(List<CostOfLivingLocation> locations) {
        this.locations = locations;
    }



}



