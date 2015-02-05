package com.matelau.junior.centsproject.Models.Design;

/**
 * Created by matelau on 11/23/14.
 */
public class Col {

    String cost_of_living;
    String goods;
    String groceries;
    String health_care;
    String transportation;
    String utilities;
    String location;
    String housing;

    public Col(){

    }

    public Col(String col, String go, String groc, String hc, String trans, String util, String loc, String housing)
    {
        location = loc;
        cost_of_living = col;
        goods =go;
        groceries = groc;
        health_care = hc;
        transportation = trans;
        utilities = util;
        this.housing = housing;

    }


    public String getCost_of_living() {
        return cost_of_living;
    }

    public String getGoods() {
        return goods;
    }

    public String getGroceries() {
        return groceries;
    }

    public String getHealth_care() {
        return health_care;
    }

    public String getTransportation() {
        return transportation;
    }

    public String getUtilities() {
        return utilities;
    }

    public String getLocation() {
        return location;
    }

    public String getHousing() {
        return housing;
    }
}
