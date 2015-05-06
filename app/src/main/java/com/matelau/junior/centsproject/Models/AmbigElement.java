package com.matelau.junior.centsproject.Models;

/**
 * Created by matelau on 4/18/15.
 */
public class AmbigElement {
    String name;
    boolean isChecked = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
