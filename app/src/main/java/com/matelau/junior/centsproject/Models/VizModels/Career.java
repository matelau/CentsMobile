package com.matelau.junior.centsproject.Models.VizModels;

import com.google.gson.annotations.Expose;

/**
 * Created by matelau on 3/30/15.
 */
public class Career {
    @Expose
    private Integer order;
    @Expose
    private String name;

    /**
     *
     * @return
     * The order
     */
    public Integer getOrder() {
        return order;
    }

    /**
     *
     * @param order
     * The order
     */
    public void setOrder(Integer order) {
        this.order = order;
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

}
