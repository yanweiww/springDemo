package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by TC20021 on 2019/1/11.
 */
@Getter
@Setter
public class EditReport {
    private int id;
    private String reportName;
    private String sqlString;
    private String tableString;
    private String whereString;
    private String lableString;
    private String fieldString;
    private String sign;
    private String whereDetail;
}
