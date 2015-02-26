package com.matelau.junior.centsproject.Models.VizModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 2/26/15.
 */
public class SchoolResponse {

    @SerializedName("school_1")
    @Expose
    private List<Double> school1 = new ArrayList<Double>();
    @SerializedName("school_2")
    @Expose
    private List<Double> school2 = new ArrayList<Double>();
    @SerializedName("school_2_name")
    @Expose
    private String school2Name;
    @SerializedName("query_type")
    @Expose
    private String queryType;
    @Expose
    private String query;
    @Expose
    private String operation;
    @SerializedName("school_1_name")
    @Expose
    private String school1Name;

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
     * The school2Name
     */
    public String getSchool2Name() {
        return school2Name;
    }

    /**
     *
     * @param school2Name
     * The school_2_name
     */
    public void setSchool2Name(String school2Name) {
        this.school2Name = school2Name;
    }

    /**
     *
     * @return
     * The queryType
     */
    public String getQueryType() {
        return queryType;
    }

    /**
     *
     * @param queryType
     * The query_type
     */
    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    /**
     *
     * @return
     * The query
     */
    public String getQuery() {
        return query;
    }

    /**
     *
     * @param query
     * The query
     */
    public void setQuery(String query) {
        this.query = query;
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

    /**
     *
     * @return
     * The school1Name
     */
    public String getSchool1Name() {
        return school1Name;
    }

    /**
     *
     * @param school1Name
     * The school_1_name
     */
    public void setSchool1Name(String school1Name) {
        this.school1Name = school1Name;
    }

}