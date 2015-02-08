package com.matelau.junior.centsproject.Models.VizModels;

import java.util.List;

/**
 * Created by matelau on 1/30/15.
 */
public class ColiQuery {
    String operation;
    List<Location> locations;

    public ColiQuery(String op, List<Location> l){
        operation = op;
        locations = l;

    }
}



