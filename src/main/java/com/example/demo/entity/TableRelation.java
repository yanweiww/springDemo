package com.example.demo.entity;


import lombok.Getter;
import lombok.Setter;

/**
 * Created by TC20021 on 2018/12/14.
 */

public class TableRelation {
    private String tableOne;
    private String fieldOne;
    private String tableRelation;
    private String tableTwo;
    private String fieldTwo;

    public String getTableOne() {
        return tableOne;
    }

    public void setTableOne(String tableOne) {
        this.tableOne = tableOne;
    }

    public String getFieldOne() {
        return fieldOne;
    }

    public void setFieldOne(String fieldOne) {
        this.fieldOne = fieldOne;
    }

    public String getTableRelation() {
        return tableRelation;
    }

    public void setTableRelation(String tableRelation) {
        this.tableRelation = tableRelation;
    }

    public String getTableTwo() {
        return tableTwo;
    }

    public void setTableTwo(String tableTwo) {
        this.tableTwo = tableTwo;
    }

    public String getFieldTwo() {
        return fieldTwo;
    }

    public void setFieldTwo(String fieldTwo) {
        this.fieldTwo = fieldTwo;
    }
}
