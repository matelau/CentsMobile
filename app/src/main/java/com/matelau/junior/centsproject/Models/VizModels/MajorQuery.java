package com.matelau.junior.centsproject.Models.VizModels;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 3/5/15.
 */
public class MajorQuery {
    @Expose
    private String operation;
    @Expose
    private List<Major> majors = new ArrayList<Major>();

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
     * The majors
     */
    public List<Major> getMajors() {
        return majors;
    }

    /**
     *
     * @param majors
     * The majors
     */
    public void setMajors(List<Major> majors) {
        this.majors = majors;
    }



}


