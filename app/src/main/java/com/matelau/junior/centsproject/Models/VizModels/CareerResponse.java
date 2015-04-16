package com.matelau.junior.centsproject.Models.VizModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 3/31/15.
 */
public class CareerResponse {

    @Expose
    private String operation;
    @Expose
    private List<Element> elements = new ArrayList<Element>();
    @SerializedName("career_unemploy_3")
    @Expose
    private List<Double> careerUnemploy3 = new ArrayList<Double>();

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
     * The elements
     */
    public List<Element> getElements() {
        return elements;
    }

    /**
     *
     * @param elements
     * The elements
     */
    public void setElements(List<Element> elements) {
        this.elements = elements;
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
    public class Element{
        @Expose
        private String name;
        @SerializedName("career_salary")
        @Expose
        private List<Double> careerSalary = new ArrayList<>();
        @SerializedName("career_demand")
        @Expose
        private List<Double> careerDemand = new ArrayList<>();
        @SerializedName("career_unemploy")
        @Expose
        private List<Double> careerUnemploy = new ArrayList<Double>();
        @SerializedName("career_rating")
        @Expose
        private Double careerRating;

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
         * The careerSalary
         */
        public List<Double> getCareerSalary() {
            return careerSalary;
        }

        /**
         *
         * @param careerSalary
         * The career_salary
         */
        public void setCareerSalary(List<Double> careerSalary) {
            this.careerSalary = careerSalary;
        }

        /**
         *
         * @return
         * The careerDemand
         */
        public List<Double> getCareerDemand() {
            return careerDemand;
        }

        /**
         *
         * @param careerDemand
         * The career_demand
         */
        public void setCareerDemand(List<Double> careerDemand) {
            this.careerDemand = careerDemand;
        }

        /**
         *
         * @return
         * The careerUnemploy
         */
        public List<Double> getCareerUnemploy() {
            return careerUnemploy;
        }

        /**
         *
         * @param careerUnemploy
         * The career_unemploy
         */
        public void setCareerUnemploy(List<Double> careerUnemploy) {
            this.careerUnemploy = careerUnemploy;
        }

        /**
         *
         * @return
         * The careerRating
         */
        public Double getCareerRating() {
            return careerRating;
        }

        /**
         *
         * @param careerRating
         * The career_rating
         */
        public void setCareerRating(Double careerRating) {
            this.careerRating = careerRating;
        }
    }
}
