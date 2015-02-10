package com.matelau.junior.centsproject.Models.VizModels;

import java.util.List;

/**
 * Created by matelau on 2/9/15.
 */
public class ColiQuery {
    public String operation;
    public List<Location> locations;

    public ColiQuery(){
        //empty const
    }

    public ColiQuery(String op, List<Location> l){
        operation = op;
        locations = l;

    }
}
