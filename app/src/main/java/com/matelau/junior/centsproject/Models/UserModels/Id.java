package com.matelau.junior.centsproject.Models.UserModels;

import com.google.gson.annotations.Expose;

/**
 * Created by matelau on 4/6/15.
 */
public class Id {
    @Expose
    private int id;

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

}
