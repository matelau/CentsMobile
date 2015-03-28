package com.matelau.junior.centsproject.Models.VizModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 3/4/15.
 */
public class SchoolResponse {
    @SerializedName("school_2_name")
    @Expose
    private String school2Name;
    @SerializedName("school_1_name")
    @Expose
    private String school1Name;
    @Expose
    private String query;
    @SerializedName("school_1")
    @Expose
    private List<Double> school1 = new ArrayList<Double>();
    @SerializedName("school_1_image")
    @Expose
    private Object school1Image;
    @SerializedName("school_2")
    @Expose
    private List<Double> school2 = new ArrayList<Double>();
    @SerializedName("school_2_image")
    @Expose
    private Object school2Image;
    @Expose
    private String operation;

    /**
     *
     * @return
     * The school1
     */
    public List<Double> getSchool1() {
        return school1;
    }

    /**
     *
     * @param school1
     * The school_1
     */
    public void setSchool1(List<Double> school1) {
        this.school1 = school1;
    }

    /**
     *
     * @return
     * The school1Image
     */
    public Object getSchool1Image() {
        return school1Image;
    }

    /**
     *
     * @param school1Image
     * The school_1_image
     */
    public void setSchool1Image(Object school1Image) {
        this.school1Image = school1Image;
    }

    /**
     *
     * @return
     * The school2
     */
    public List<Double> getSchool2() {
        return school2;
    }

    /**
     *
     * @param school2
     * The school_2
     */
    public void setSchool2(List<Double> school2) {
        this.school2 = school2;
    }

    /**
     *
     * @return
     * The school2Image
     */
    public Object getSchool2Image() {
        return school2Image;
    }

    /**
     *
     * @param school2Image
     * The school_2_image
     */
    public void setSchool2Image(Object school2Image) {
        this.school2Image = school2Image;
    }

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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSchool2Name() {
        return school2Name;
    }

    public void setSchool2Name(String school2Name) {
        this.school2Name = school2Name;
    }

    public String getSchool1Name() {
        return school1Name;
    }

    public void setSchool1Name(String school1Name) {
        this.school1Name = school1Name;
    }
}
