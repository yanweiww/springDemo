package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by TC20021 on 2018/12/24.
 * 表表页面显示的查询记录
 */
@Getter
@Setter
public class RpReportQuery {
    private int id;
    private int reportId;
    private String sqlWhere;
    private String state;//状态
    private String startTime;
    private String endTime;
    private String requestUrl;//文件保存路径

    private String sqlStr;
    private String lebStr;
    private String sqls;

}
