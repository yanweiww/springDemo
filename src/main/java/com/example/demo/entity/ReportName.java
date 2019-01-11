package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by TC20021 on 2018/12/19.
 */
@Getter
@Setter
public class ReportName {
    private int id;
    private String reName;//报表名
    private String fieldNames;//查询字段名
    private String tableNames;//关联表名
    private String whereNames;//条件字段名
    private String lableName;//查询字段别名
    private String flag;//报表是否删除
}
