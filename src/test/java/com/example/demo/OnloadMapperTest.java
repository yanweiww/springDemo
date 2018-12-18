package com.example.demo;

import com.example.demo.entity.LoanBase;
import com.example.demo.entity.TableRelation;
import com.example.demo.mapper.OnloadMapper;

import com.google.gson.Gson;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.hive.HiveContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by TC20021 on 2018/12/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OnloadMapperTest {

    @Autowired
    OnloadMapper onloadMapper;

    @Test
    public void getLoanBase(){

        String sqlname ="select loan_base_dsj.loan_state,overdue_detail_dsj.cur_date,person_info_dsj.name  from  \n" +
                "loan_base_dsj left join  overdue_detail_dsj  on  loan_base_dsj.id = overdue_detail_dsj.loan_id  \n" +
                "left join  person_info_dsj  on  loan_base_dsj.borrower_id = person_info_dsj.id ";
        SparkConf conf = new SparkConf().setAppName("programdept").setMaster("spark://10.100.200.11:7077")
                .setJars(new String[]{"D:\\datahouse\\demo.jar"});
        JavaSparkContext sc = new JavaSparkContext(conf);
        HiveContext hiveContext = new HiveContext(sc.sc());
        Gson gson = new Gson();

        hiveContext.sql("use oracle_table");
        DataFrame sql = hiveContext.sql(sqlname);
        // DataFrame sql = hiveContext.sql("show tables");
        List<Object> collect =
                sql.collectAsList().stream().map(x->x.toString()).collect(Collectors.toList());
        System.out.println(collect);
        for(int i= 0;i < 5;i++){
            System.out.println(collect.get(i));
        }
        sc.close();

    }
}
