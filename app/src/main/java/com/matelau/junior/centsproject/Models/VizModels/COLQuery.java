package com.matelau.junior.centsproject.Models.VizModels;

import java.util.List;

/**
 * Created by matelau on 2/9/15.
 */
public class COLQuery {
    public String operation;
    public List<Location> locations;

    public COLQuery(){
        //empty const
    }

    public COLQuery(String op, List<Location> l){
        operation = op;
        locations = l;

    }
}
