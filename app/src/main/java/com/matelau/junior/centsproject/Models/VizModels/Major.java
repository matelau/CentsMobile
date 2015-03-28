package com.matelau.junior.centsproject.Models.VizModels;
import com.google.gson.annotations.Expose;


public class Major {

    @Expose
    private Integer order;
    @Expose
    private String name;
    @Expose
    private String level;

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
        if(name.charAt(name.length()-1) == ' '){
            name = name.substring(0,name.length()-1);
        }
        this.name = name;
    }

    /**
     *
     * @return
     * The level
     */
    public String getLevel() {
        return level;
    }

    /**
     *
     * @param level
     * The level
     */
    public void setLevel(String level) {
        this.level = level;
    }

}
