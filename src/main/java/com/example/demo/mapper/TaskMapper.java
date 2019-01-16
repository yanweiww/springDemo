package com.example.demo.mapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by TC20021 on 2018/12/27.
 */
public interface TaskMapper {

    /**
     * 定时任务启动，查询数据库中状态为查询中的字段，取出
     */
    @Select("select id,rep_name_id,sql_str,leb_Str,sqls from rp_report_select where state='select' order by id limit 1")
    @Results({
            @Result(property = "repNameId",column = "rep_name_id"),
            @Result(property = "sqlStr",column = "sql_str"),
            @Result(property = "lebStr" ,column = "leb_Str")
        })
    Map<String ,Object> getQueryAll();


}