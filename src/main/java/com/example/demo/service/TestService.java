package com.example.demo.service;


import com.example.demo.entity.ExpressionField;
import com.example.demo.entity.RpReportName;
import org.apache.ibatis.annotations.Param;

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

    Map<String, Object> getAllTableFields(Integer reportId);

    void insertExpression(String allAppendExpressions, String allUpdateExpressions, Integer reportId);

    void deleteFieldExpression(Integer ifieldId);

    /**
     * 查询自定义公式表中是否有数据
     */
    List<ExpressionField> getExression(int id);


}
