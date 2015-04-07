package com.matelau.junior.centsproject.Models.VizModels;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matelau on 3/3/15.
 */
public class RecordQuery {
    @Expose
    private String operation;
    @Expose
    private List<String> tables = new ArrayList<String>();
    @Expose
    private String select;
    @Expose
    private String where;

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
     * The tables
     */
    public List<String> getTables() {
        return tables;
    }

    /**
     *
     * @param tables
     * The tables
     */
    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    /**
     *
     * @return
     * The select
     */
    public String getSelect() {
        return select;
    }

    /**
     *
     * @param select
     * The select
     */
    public void setSelect(String select) {
        this.select = select;
    }

    /**
     *
     * @return
     * The where
     */
    public String getWhere() {
        return where;
    }

    /**
     *
     * @param where
     * The where
     */
    public void setWhere(String where) {
        this.where = where;
    }
}
