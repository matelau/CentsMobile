package com.matelau.junior.centsproject.Models.VizModels;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 3/4/15.
 */
public class SchoolRequest {
    @Expose
    private String operation;
    @Expose
    private List<School> schools = new ArrayList<School>();

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
     * The schools
     */
    public List<School> getSchools() {
        return schools;
    }

    /**
     *
     * @param schools
     * The schools
     */
    public void setSchools(List<School> schools) {
        this.schools = schools;
    }


}
