package com.matelau.junior.centsproject.Models.VizModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by matelau on 4/20/15.
 */
public class SpendingResponse {
    @Expose
    private String query;
    @Expose
    private String operation;
    @Expose
    private Double income;
    @SerializedName("query_type")
    @Expose
    private String queryType;

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
     * The income
     */
    public Double getIncome() {
        return income;
    }

    /**
     *
     * @param income
     * The income
     */
    public void setIncome(Double income) {
        this.income = income;
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

}
