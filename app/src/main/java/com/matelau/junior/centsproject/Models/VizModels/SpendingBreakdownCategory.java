package com.matelau.junior.centsproject.Models.VizModels;

/**
 * Model for Spending Breakdown Elements
 */
public class SpendingBreakdownCategory {
    public String _category;
    public Float _percent;
    public boolean _locked;


    public SpendingBreakdownCategory(String c, Float p, boolean l){
        _category = c;
        _percent = p;
        _locked = l;
    }

}
