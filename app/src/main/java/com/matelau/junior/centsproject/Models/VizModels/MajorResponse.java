package com.matelau.junior.centsproject.Models.VizModels;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 3/5/15.
 */
public class MajorResponse {

    @Expose
    private List<Element> elements = new ArrayList<Element>();
    @Expose
    private String operation;

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


    public class Element {

        @Expose
        private String name;
        @Expose
        private List<Float> degree = new ArrayList<>();
        @Expose
        private List<String> jobs = new ArrayList<String>();

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
         * The degree
         */
        public List<Float> getDegree() {
            return degree;
        }

        /**
         *
         * @param degree
         * The degree
         */
        public void setDegree(List<Float> degree) {
            this.degree = degree;
        }

        /**
         *
         * @return
         * The jobs
         */
        public List<String> getJobs() {
            return jobs;
        }

        /**
         *
         * @param jobs
         * The jobs
         */
        public void setJobs(List<String> jobs) {
            this.jobs = jobs;
        }

    }
}
