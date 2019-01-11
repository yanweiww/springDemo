package com.example.demo.mapper;

import com.example.demo.entity.ReportSort;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by TC20021 on 2019/1/3.
 */
public interface EditReportMapper {

    //根据id查询报表的基本信息
    @Select("select sql_string,leble_string from rp_report_name where id=#{id} ")
    @Results({
            @Result(property = "sqlString",column = "sql_string"),
            @Result(property = "lebleString" ,column = "leble_string")
    })
    Map<String,Object> getInfo(@Param("id") String id);
    //根据id查询报表的条件字段

    @Select("select id,field_string,show_field,where_detail from rp_relation_query where parent_id=#{id}")
    @Results({
            @Result(property = "fieldString",column = "field_string"),
            @Result(property = "showField" ,column = "show_field"),
            @Result(property = "whereDetail" ,column = "where_detail")
    })
    List<Map<String ,Object>> getWhere(@Param("id") String id);

    //根据页面修改排序，把新排序修改到rp_report_name表中,并返回主键
    @Update("update rp_report_name set sql_string=#{fieldSql},leble_string=#{lableSql} where id=#{repNameId}")
    void updateTosort(ReportSort reportSort);

    //查询自定义报表的信息
    @Select("select " +
            "a.id as id," +
            "a.report_name as reportName," +
            "a.sql_string as sqlString," +
            "a.table_string as tableString," +
            "a.where_string as whereString," +
            "a.leble_string as lableString," +
            "b.field_String as fieldString," +
            "b.sign as sign," +
            "b.where_detail as whereDetail " +

            "from " +
            "rp_report_name as a " +
            "left join  rp_relation_query as b on a.id=b.parent_id  " +
            " where flag='0' ")
    List<Map<String ,Object>> reportTab();



}
