package com.matelau.junior.centsproject.Models.CentsAPIModels;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 3/30/15.
 */
public class CareerQuery {
    @Expose
    private String operation;
    @Expose
    private List<Career> careers = new ArrayList<Career>();

    /**
     *
     * @return
     * The operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     *
     * @param operation
     * The operation
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     *
     * @return
     * The careers
     */
    public List<Career> getCareers() {
        return careers;
    }

    /**
     *
     * @param careers
     * The careers
     */
    public void setCareers(List<Career> careers) {
        this.careers = careers;
    }
}
