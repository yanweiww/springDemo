package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by TC20021 on 2018/12/19.
 */
@Getter
@Setter
public class RelationQuery {

    private int id;
    private int parentId;//rp_report_name表主键
    private String fieldString;//查询字段
    private String sign="否";//是否必填
    private String type;//类型属性
    private String fieldMeans;//字段别名
    private String whereDetail;//字段取值类型

}
