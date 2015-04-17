package com.matelau.junior.centsproject.Models.VizModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 3/4/15.
 */
public class SchoolResponse {
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


    public class Element{

        @Expose
        private String name;
        @Expose
        private List<Double> school = new ArrayList<Double>();
        @SerializedName("school_image")
        @Expose
        private Object schoolImage;

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
         * The school
         */
        public List<Double> getSchool() {
            return school;
        }

        /**
         *
         * @param school
         * The school
         */
        public void setSchool(List<Double> school) {
            this.school = school;
        }

        /**
         *
         * @return
         * The schoolImage
         */
        public Object getSchoolImage() {
            return schoolImage;
        }

        /**
         *
         * @param schoolImage
         * The school_image
         */
        public void setSchoolImage(Object schoolImage) {
            this.schoolImage = schoolImage;
        }

    }
}
