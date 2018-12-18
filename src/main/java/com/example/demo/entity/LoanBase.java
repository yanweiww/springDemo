package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Created by TC20021 on 2018/12/11.
 */

public class LoanBase {
    //表名
   private String tabName;

   private List<Map<String, Object>> calList;


    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public List<Map<String, Object>> getCalList() {
        return calList;
    }

    public void setCalList(List<Map<String, Object>> calList) {
        this.calList = calList;
    }
}
