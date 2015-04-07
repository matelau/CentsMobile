package com.matelau.junior.centsproject.Models.VizModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 3/31/15.
 */
public class CareerResponse {
    @SerializedName("career_1")
    @Expose
    private String career1;
    @SerializedName("career_salary_1")
    @Expose
    private List<Integer> careerSalary1 = new ArrayList<Integer>();
    @SerializedName("career_satisfaction_1")
    @Expose
    private Integer careerSatisfaction1;
    @SerializedName("career_demand_1")
    @Expose
    private List<Integer> careerDemand1 = new ArrayList<Integer>();
    @SerializedName("jobs_1")
    @Expose
    private List<Object> jobs1 = new ArrayList<Object>();
    @SerializedName("career_2")
    @Expose
    private String career2;
    @SerializedName("career_salary_2")
    @Expose
    private List<Integer> careerSalary2 = new ArrayList<Integer>();
    @SerializedName("career_satisfaction_2")
    @Expose
    private Integer careerSatisfaction2;
    @SerializedName("career_demand_2")
    @Expose
    private List<Integer> careerDemand2 = new ArrayList<Integer>();
    @SerializedName("jobs_2")
    @Expose
    private List<Object> jobs2 = new ArrayList<Object>();
    @SerializedName("career_unemploy_1")
    @Expose
    private List<Double> careerUnemploy1 = new ArrayList<Double>();
    @SerializedName("career_unemploy_2")
    @Expose
    private List<Double> careerUnemploy2 = new ArrayList<Double>();
    @SerializedName("career_unemploy_3")
    @Expose
    private List<Double> careerUnemploy3 = new ArrayList<Double>();
    @Expose
    private String operation;

    /**
     *
     * @return
     * The career1
     */
    public String getCareer1() {
        return career1;
    }

    /**
     *
     * @param career1
     * The career_1
     */
    public void setCareer1(String career1) {
        this.career1 = career1;
    }

    /**
     *
     * @return
     * The careerSalary1
     */
    public List<Integer> getCareerSalary1() {
        return careerSalary1;
    }

    /**
     *
     * @param careerSalary1
     * The career_salary_1
     */
    public void setCareerSalary1(List<Integer> careerSalary1) {
        this.careerSalary1 = careerSalary1;
    }

    /**
     *
     * @return
     * The careerSatisfaction1
     */
    public Integer getCareerSatisfaction1() {
        return careerSatisfaction1;
    }

    /**
     *
     * @param careerSatisfaction1
     * The career_satisfaction_1
     */
    public void setCareerSatisfaction1(Integer careerSatisfaction1) {
        this.careerSatisfaction1 = careerSatisfaction1;
    }

    /**
     *
     * @return
     * The careerDemand1
     */
    public List<Integer> getCareerDemand1() {
        return careerDemand1;
    }

    /**
     *
     * @param careerDemand1
     * The career_demand_1
     */
    public void setCareerDemand1(List<Integer> careerDemand1) {
        this.careerDemand1 = careerDemand1;
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
     * The career2
     */
    public String getCareer2() {
        return career2;
    }

    /**
     *
     * @param career2
     * The career_2
     */
    public void setCareer2(String career2) {
        this.career2 = career2;
    }

    /**
     *
     * @return
     * The careerSalary2
     */
    public List<Integer> getCareerSalary2() {
        return careerSalary2;
    }

    /**
     *
     * @param careerSalary2
     * The career_salary_2
     */
    public void setCareerSalary2(List<Integer> careerSalary2) {
        this.careerSalary2 = careerSalary2;
    }

    /**
     *
     * @return
     * The careerSatisfaction2
     */
    public Integer getCareerSatisfaction2() {
        return careerSatisfaction2;
    }

    /**
     *
     * @param careerSatisfaction2
     * The career_satisfaction_2
     */
    public void setCareerSatisfaction2(Integer careerSatisfaction2) {
        this.careerSatisfaction2 = careerSatisfaction2;
    }

    /**
     *
     * @return
     * The careerDemand2
     */
    public List<Integer> getCareerDemand2() {
        return careerDemand2;
    }

    /**
     *
     * @param careerDemand2
     * The career_demand_2
     */
    public void setCareerDemand2(List<Integer> careerDemand2) {
        this.careerDemand2 = careerDemand2;
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
     * The careerUnemploy1
     */
    public List<Double> getCareerUnemploy1() {
        return careerUnemploy1;
    }

    /**
     *
     * @param careerUnemploy1
     * The career_unemploy_1
     */
    public void setCareerUnemploy1(List<Double> careerUnemploy1) {
        this.careerUnemploy1 = careerUnemploy1;
    }

    /**
     *
     * @return
     * The careerUnemploy2
     */
    public List<Double> getCareerUnemploy2() {
        return careerUnemploy2;
    }

    /**
     *
     * @param careerUnemploy2
     * The career_unemploy_2
     */
    public void setCareerUnemploy2(List<Double> careerUnemploy2) {
        this.careerUnemploy2 = careerUnemploy2;
    }

    /**
     *
     * @return
     * The careerUnemploy3
     */
    public List<Double> getCareerUnemploy3() {
        return careerUnemploy3;
    }

    /**
     *
     * @param careerUnemploy3
     * The career_unemploy_3
     */
    public void setCareerUnemploy3(List<Double> careerUnemploy3) {
        this.careerUnemploy3 = careerUnemploy3;
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
