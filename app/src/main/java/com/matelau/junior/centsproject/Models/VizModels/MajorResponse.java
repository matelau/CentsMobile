package com.matelau.junior.centsproject.Models.VizModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 3/5/15.
 */
public class MajorResponse {

    @SerializedName("name_1")
    @Expose
    private String name_1;

    @SerializedName("name_2")
    @Expose
    private String name_2;

    @SerializedName("jobs_1")
    @Expose
    private List<Object> jobs1 = new ArrayList<Object>();
    @SerializedName("degree_1")
    @Expose
    private List<Float> major1 = new ArrayList<Float>();
    @SerializedName("jobs_2")
    @Expose
    private List<Object> jobs2 = new ArrayList<Object>();
    @SerializedName("degree_2")
    @Expose
    private List<Float> major2 = new ArrayList<Float>();
    @Expose
    private String operation;


    /**
     *
     * @return
     * The name_1
     */
    public String getName_1() {
        return name_1;
    }

    /**
     *
     * @param name_1
     * The major_2_name
     */
    public void setName_1(String name_1) {
        this.name_1 = name_1;
    }

    /**
     *
     * @return
     * The name_2
     */
    public String getName_2() {
        return name_2;
    }

    /**
     *
     * @param name_2
     * The major_1_name
     */
    public void setName_2(String name_2) {
        this.name_2 = name_2;
    }


    /**
     *
     * @return
     * The jobs1
     */
    public List<Object> getJobs1() {
        return jobs1;
    }

    /**
     *
     * @param jobs1
     * The jobs_1
     */
    public void setJobs1(List<Object> jobs1) {
        this.jobs1 = jobs1;
    }

    /**
     *
     * @return
     * The major1
     */
    public List<Float> getMajor1() {
        return major1;
    }

    /**
     *
     * @param major1
     * The major_1
     */
    public void setMajor1(List<Float> major1) {
        this.major1 = major1;
    }

    /**
     *
     * @return
     * The jobs2
     */
    public List<Object> getJobs2() {
        return jobs2;
    }

    /**
     *
     * @param jobs2
     * The jobs_2
     */
    public void setJobs2(List<Object> jobs2) {
        this.jobs2 = jobs2;
    }

    /**
     *
     * @return
     * The major2
     */
    public List<Float> getMajor2() {
        return major2;
    }

    /**
     *
     * @param major2
     * The major_2
     */
    public void setMajor2(List<Float> major2) {
        this.major2 = major2;
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
}
