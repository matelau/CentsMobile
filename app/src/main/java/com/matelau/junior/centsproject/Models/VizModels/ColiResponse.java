package com.matelau.junior.centsproject.Models.VizModels;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Asaeli on 2/7/2015.
 */
public class ColiResponse {
    @Expose
    private List<Element> elements = new ArrayList<Element>();
    @SerializedName("labor_3")
    @Expose
    private List<Double> labor3 = new ArrayList<Double>();
    @SerializedName("taxes_3")
    @Expose
    private List<Double> taxes3 = new ArrayList<Double>();
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
     * The labor3
     */
    public List<Double> getLabor3() {
        return labor3;
    }

    /**
     *
     * @param labor3
     * The labor_3
     */
    public void setLabor3(List<Double> labor3) {
        this.labor3 = labor3;
    }

    /**
     *
     * @return
     * The taxes3
     */
    public List<Double> getTaxes3() {
        return taxes3;
    }

    /**
     *
     * @param taxes3
     * The taxes_3
     */
    public void setTaxes3(List<Double> taxes3) {
        this.taxes3 = taxes3;
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
        private List<Double> cli = new ArrayList<Double>();
        @Expose
        private List<Double> labor = new ArrayList<Double>();
        @Expose
        private List<Double> taxes = new ArrayList<Double>();
        @Expose
        private String name;
        @Expose
        private List<Double> weather = new ArrayList<Double>();
        @Expose
        private List<Double> weatherlow = new ArrayList<Double>();

        /**
         *
         * @return
         * The cli
         */
        public List<Double> getCli() {
            return cli;
        }

        /**
         *
         * @param cli
         * The cli
         */
        public void setCli(List<Double> cli) {
            this.cli = cli;
        }

        /**
         *
         * @return
         * The labor
         */
        public List<Double> getLabor() {
            return labor;
        }

        /**
         *
         * @param labor
         * The labor
         */
        public void setLabor(List<Double> labor) {
            this.labor = labor;
        }

        /**
         *
         * @return
         * The taxes
         */
        public List<Double> getTaxes() {
            return taxes;
        }

        /**
         *
         * @param taxes
         * The taxes
         */
        public void setTaxes(List<Double> taxes) {
            this.taxes = taxes;
        }

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
         * The weather
         */
        public List<Double> getWeather() {
            return weather;
        }

        /**
         *
         * @param weather
         * The weather
         */
        public void setWeather(List<Double> weather) {
            this.weather = weather;
        }

        /**
         *
         * @return
         * The weatherlow
         */
        public List<Double> getWeatherlow() {
            return weatherlow;
        }

        /**
         *
         * @param weatherlow
         * The weatherlow
         */
        public void setWeatherlow(List<Double> weatherlow) {
            this.weatherlow = weatherlow;
        }
    }
}
