package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Created by TC20021 on 2018/12/11.
 */
@Getter
@Setter
public class LoanBase {
    //表名
   private String tabName;
   //表别名
   private String showName;

   private List<Map<String, Object>> calList;


}
