package com.example.demo.service;


import com.example.demo.entity.RpReportName;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

public interface TestService {

    List<Map<String, Object>> getDatas();

    void queryDatas(String nameData, String tabData, String fieldMean);

    List<RpReportName> getTabs();

    List<Map<String, Object>> getAllQueryFields(Integer reportId);

    Map<String,Object> getQuerySql(Integer reportId, String whereFields);

    String getFTPUrl(String tabName,ByteArrayInputStream inputStream);


}
