package com.example.demo;

import com.example.demo.commons.ExcelExportUtil;
import com.example.demo.commons.FTPConfigBean;
import com.example.demo.commons.FTPUtil;

import com.example.demo.entity.RpReportName;
import com.example.demo.mapper.EditReportMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TC20021 on 2018/12/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OnloadMapperTest {


    @Autowired
    private FTPConfigBean ftpConfigBean;

    @Autowired
    private EditReportMapper editReportMapper;

    @Test
    public void getLoanBase() throws Exception {

        Map<String, String> whereAndSqlString = editReportMapper.selectWhereAndSqlString(58);
        String whereString = whereAndSqlString.get("whereString");
        System.out.println(whereString);

        String sqlString = whereAndSqlString.get("sqlString");
        String lebleString = whereAndSqlString.get("lebleString");
        RpReportName rpReportName = new RpReportName();
        rpReportName.setReportId(58);
        rpReportName.setLebleString(lebleString + "," + "aaa");
        //判断field_type是什么类型字段
        //if (strings[2] == "1") rpReportName.setSqlString(sqlString + "," + "111");
        rpReportName.setWhereString(whereString + "," + "111");
        System.out.println(rpReportName.getWhereString());
        editReportMapper.updateWhereAndSqlString(rpReportName);
    }
}
