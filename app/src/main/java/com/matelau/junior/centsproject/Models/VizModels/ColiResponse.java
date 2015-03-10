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
        private String operation;
        @SerializedName("location_1")
        @Expose
        private String location1;
        @SerializedName("cli_1")
        @Expose
        private List<Double> cli1 = new ArrayList<Double>();
        @SerializedName("labor_1")
        @Expose
        private List<Double> labor1 = new ArrayList<Double>();
        @SerializedName("taxes_1")
        @Expose
        private List<Double> taxes1 = new ArrayList<Double>();
        @SerializedName("weather_1")
        @Expose
        private List<Double> weather1 = new ArrayList<Double>();
        @SerializedName("weatherlow_1")
        @Expose
        private List<Double> weatherlow1 = new ArrayList<Double>();
        @SerializedName("location_2")
        @Expose
        private String location2;
        @SerializedName("cli_2")
        @Expose
        private List<Double> cli2 = new ArrayList<Double>();
        @SerializedName("labor_2")
        @Expose
        private List<Double> labor2 = new ArrayList<Double>();
        @SerializedName("labor_3")
        @Expose
        private List<Double> labor3 = new ArrayList<Double>();
        @SerializedName("taxes_2")
        @Expose
        private List<Double> taxes2 = new ArrayList<Double>();
        @SerializedName("weather_2")
        @Expose
        private List<Double> weather2 = new ArrayList<Double>();
        @SerializedName("weatherlow_2")
        @Expose
        private List<Double> weatherlow2 = new ArrayList<Double>();

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
         * The location1
         */
        public String getLocation1() {
            return location1;
        }

        /**
         *
         * @param location1
         * The location_1
         */
        public void setLocation1(String location1) {
            this.location1 = location1;
        }

        /**
         *
         * @return
         * The cli1
         */
        public List<Double> getCli1() {
            return cli1;
        }

        /**
         *
         * @param cli1
         * The cli_1
         */
        public void setCli1(List<Double> cli1) {
            this.cli1 = cli1;
        }

        /**
         *
         * @return
         * The labor1
         */
        public List<Double> getLabor1() {
            return labor1;
        }

        /**
         *
         * @param labor1
         * The labor_1
         */
        public void setLabor1(List<Double> labor1) {
            this.labor1 = labor1;
        }

        /**
         *
         * @return
         * The taxes1
         */
        public List<Double> getTaxes1() {
            return taxes1;
        }

        /**
         *
         * @param taxes1
         * The taxes_1
         */
        public void setTaxes1(List<Double> taxes1) {
            this.taxes1 = taxes1;
        }

        /**
         *
         * @return
         * The weather1
         */
        public List<Double> getWeather1() {
            return weather1;
        }

        /**
         *
         * @param weather1
         * The weather_1
         */
        public void setWeather1(List<Double> weather1) {
            this.weather1 = weather1;
        }

        /**
         *
         * @return
         * The weatherlow1
         */
        public List<Double> getWeatherlow1() {
            return weatherlow1;
        }

        /**
         *
         * @param weatherlow1
         * The weatherlow_1
         */
        public void setWeatherlow1(List<Double> weatherlow1) {
            this.weatherlow1 = weatherlow1;
        }

        /**
         *
         * @return
         * The location2
         */
        public String getLocation2() {
            return location2;
        }

        /**
         *
         * @param location2
         * The location_2
         */
        public void setLocation2(String location2) {
            this.location2 = location2;
        }

        /**
         *
         * @return
         * The cli2
         */
        public List<Double> getCli2() {
            return cli2;
        }

        /**
         *
         * @param cli2
         * The cli_2
         */
        public void setCli2(List<Double> cli2) {
            this.cli2 = cli2;
        }

        /**
         *
         * @return
         * The labor2
         */
        public List<Double> getLabor2() {
            return labor2;
        }

        public List<Double> getLabor3() {
            return labor3;
        }

        public void setLabor3(List<Double> labor3) {
            this.labor3 = labor3;
        }

    /**
         *
         * @param labor2
         * The labor_2
         */
        public void setLabor2(List<Double> labor2) {
            this.labor2 = labor2;
        }

        /**
         *
         * @return
         * The taxes2
         */
        public List<Double> getTaxes2() {
            return taxes2;
        }

        /**
         *
         * @param taxes2
         * The taxes_2
         */
        public void setTaxes2(List<Double> taxes2) {
            this.taxes2 = taxes2;
        }

        /**
         *
         * @return
         * The weather2
         */
        public List<Double> getWeather2() {
            return weather2;
        }

        /**
         *
         * @param weather2
         * The weather_2
         */
        public void setWeather2(List<Double> weather2) {
            this.weather2 = weather2;
        }

        /**
         *
         * @return
         * The weatherlow2
         */
        public List<Double> getWeatherlow2() {
            return weatherlow2;
        }

        /**
         *
         * @param weatherlow2
         * The weatherlow_2
         */
        public void setWeatherlow2(List<Double> weatherlow2) {
            this.weatherlow2 = weatherlow2;
        }


}
