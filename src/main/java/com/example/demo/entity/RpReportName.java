package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * rp_report_name
 */
public class RpReportName {
    /**
     * id
     */
    private Integer reportId;

    /**
     * report_name
     */
    private String reportName;

    /**
     *sql_string
     */
    private String sqlString;

    /**
     *table_string
     */
    private String tableString;

    /**
     *where_string
     */
    private String whereString;

    /**
     *lable_string
     */
    private String lebleString;

    private String flag;



}
